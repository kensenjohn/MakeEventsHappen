package com.events.proc.clients;

import com.events.bean.clients.ClientBean;
import com.events.bean.clients.ClientRequestBean;
import com.events.bean.users.UserBean;
import com.events.bean.vendors.VendorBean;
import com.events.bean.vendors.VendorRequestBean;
import com.events.clients.AccessClients;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Perm;
import com.events.common.Utility;
import com.events.common.exception.ExceptionHandler;
import com.events.common.security.DataSecurityChecker;
import com.events.json.*;
import com.events.users.permissions.CheckPermission;
import com.events.vendors.AccessVendors;
import com.events.vendors.BuildVendors;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 12/17/13
 * Time: 4:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcLoadClients   extends HttpServlet {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APPLICATION_LOG);
    public void doPost(HttpServletRequest request,  HttpServletResponse response)  throws ServletException, IOException {
        RespObjectProc responseObject = new RespObjectProc();
        JSONObject jsonResponseObj = new JSONObject();
        ArrayList<Text> arrOkText = new ArrayList<Text>();
        ArrayList<Text> arrErrorText = new ArrayList<Text>();
        RespConstants.Status responseStatus = RespConstants.Status.ERROR;

        try{
            if( !DataSecurityChecker.isInsecureInputResponse(request) ) {

                UserBean loggedInUserBean = new UserBean();
                if(request.getSession().getAttribute(Constants.USER_LOGGED_IN_BEAN)!=null) {
                    loggedInUserBean = (UserBean)request.getSession().getAttribute(Constants.USER_LOGGED_IN_BEAN);
                }

                if(loggedInUserBean!=null && !"".equalsIgnoreCase(loggedInUserBean.getUserId())) {

                    boolean isLoadLeads = ParseUtil.sTob( request.getParameter("load_leads"));
                    ClientRequestBean clientRequestBean = new ClientRequestBean();
                    clientRequestBean.setClientId( loggedInUserBean.getParentId());

                    if(isLoadLeads){
                        clientRequestBean.setLead( true );
                    } else {
                        clientRequestBean.setLead( false );
                    }

                    AccessClients accessClients = new AccessClients();
                    if(!accessClients.isClient( clientRequestBean ) ){
                        VendorRequestBean vendorRequestBean = new VendorRequestBean();
                        vendorRequestBean.setUserId( loggedInUserBean.getUserId() );
                        AccessVendors accessVendor = new AccessVendors();
                        VendorBean vendorBean = accessVendor.getVendorByUserId( vendorRequestBean ) ;  // get  vendor from user id

                        if(vendorBean!=null && !"".equalsIgnoreCase(vendorBean.getVendorId() ))  {
                            clientRequestBean.setVendorId( vendorBean.getVendorId() );

                            HashMap<Integer,ClientBean> hmClientBean = accessClients.getAllClientsSummary(clientRequestBean);
                            JSONObject jsonObject = accessClients.convertAllClientsSummaryToJson( hmClientBean );
                            jsonResponseObj.put("all_client_summary",jsonObject);
                            jsonResponseObj.put("num_of_clients",hmClientBean.size());

                            boolean canDeleteClient = false;
                            CheckPermission checkPermission = new CheckPermission(loggedInUserBean);
                            if(checkPermission!=null) {
                                canDeleteClient = checkPermission.can(Perm.DELETE_CLIENT);
                            }
                            jsonResponseObj.put("can_delete_client", canDeleteClient );

                            Text okText = new OkText("Loading of All Client Summary completed","status_mssg") ;
                            arrOkText.add(okText);
                            responseStatus = RespConstants.Status.OK;
                        } else {
                            appLogging.info("Could not load a valid Vendor for user id that was provided " + ParseUtil.checkNullObject(loggedInUserBean) );
                            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(loadclient - 004)","err_mssg") ;
                            arrErrorText.add(errorText);

                            responseStatus = RespConstants.Status.ERROR;
                        }
                    } else {
                        Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. You are not authorized to access this feature.","err_mssg") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;
                    }


                } else {
                    appLogging.info("Invalid request in Proc Page  (loggedInUserBean)" + ParseUtil.checkNullObject(loggedInUserBean) );
                    Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(loadclient - 002)","err_mssg") ;
                    arrErrorText.add(errorText);

                    responseStatus = RespConstants.Status.ERROR;
                }

            } else {
                appLogging.info("Insecure Parameters used in this Proc Page " + Utility.dumpRequestParameters(request).toString()  + " --> " + this.getClass().getName());
                Text errorText = new ErrorText("Please use valid parameters. We have identified insecure parameters in your form.","account_num") ;
                arrErrorText.add(errorText);
                responseStatus = RespConstants.Status.ERROR;
            }
        } catch(Exception e) {
            appLogging.info("An exception occurred in the Proc Page " + ExceptionHandler.getStackTrace(e) );
            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(loadclient - 001)","err_mssg") ;
            arrErrorText.add(errorText);

            responseStatus = RespConstants.Status.ERROR;
        }


        responseObject.setErrorMessages(arrErrorText);
        responseObject.setOkMessages(arrOkText);
        responseObject.setResponseStatus(responseStatus);
        responseObject.setJsonResponseObj(jsonResponseObj);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write( responseObject.getJson().toString() );
    }
}
