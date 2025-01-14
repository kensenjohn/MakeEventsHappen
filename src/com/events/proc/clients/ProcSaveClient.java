package com.events.proc.clients;

import com.events.bean.clients.ClientBean;
import com.events.bean.clients.ClientRequestBean;
import com.events.bean.clients.ClientResponseBean;
import com.events.bean.users.ParentTypeBean;
import com.events.bean.users.UserBean;
import com.events.bean.users.UserRequestBean;
import com.events.bean.vendors.VendorBean;
import com.events.bean.vendors.VendorRequestBean;
import com.events.clients.AccessClients;
import com.events.clients.BuildClients;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Perm;
import com.events.common.Utility;
import com.events.common.exception.ExceptionHandler;
import com.events.common.security.DataSecurityChecker;
import com.events.json.*;
import com.events.users.AccessUsers;
import com.events.users.permissions.CheckPermission;
import com.events.vendors.AccessVendors;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 12/17/13
 * Time: 11:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProcSaveClient  extends HttpServlet {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APPLICATION_LOG);

    public void doPost(HttpServletRequest request,  HttpServletResponse response)  throws ServletException, IOException {
        RespObjectProc responseObject = new RespObjectProc();
        JSONObject jsonResponseObj = new JSONObject();
        ArrayList<Text> arrOkText = new ArrayList<Text>();
        ArrayList<Text> arrErrorText = new ArrayList<Text>();
        RespConstants.Status responseStatus = RespConstants.Status.ERROR;

        try{
            if( !DataSecurityChecker.isInsecureInputResponse(request) ) {
                String sClienttName = ParseUtil.checkNull(request.getParameter("clientName"));
                boolean isCorporateClient = ParseUtil.sTob(request.getParameter("isCorporateClient"));
                String sClientFirstName = ParseUtil.checkNull(request.getParameter("clientFirstName"));
                String sClientLastName = ParseUtil.checkNull(request.getParameter("clientLastName"));
                String sClientEmail = ParseUtil.checkNull(request.getParameter("clientEmail"));
                String sClientCompanyName = ParseUtil.checkNull(request.getParameter("clientCompanyName"));
                String sClientCellPhone = ParseUtil.checkNull(request.getParameter("clientCellPhone"));
                String sClientWorkPhone = ParseUtil.checkNull(request.getParameter("clientWorkPhone"));
                String sClientAddress1 = ParseUtil.checkNull(request.getParameter("clientAddress1"));
                String sClientAddress2 = ParseUtil.checkNull(request.getParameter("clientAddress2"));
                String sClientCity = ParseUtil.checkNull(request.getParameter("clientCity"));
                String sClientState = ParseUtil.checkNull(request.getParameter("clientState"));
                String sClientCountry = ParseUtil.checkNull(request.getParameter("clientCountry"));
                String sClientPostalCode = ParseUtil.checkNull(request.getParameter("clientPostalCode"));
                String sClientId = ParseUtil.checkNull(request.getParameter("client_id"));
                String sUserId = ParseUtil.checkNull(request.getParameter("userId"));
                String sUserInfoId = ParseUtil.checkNull(request.getParameter("userInfoId"));
                boolean isLead = ParseUtil.sTob(request.getParameter("is_lead"));

                if( (!isLead&&Utility.isNullOrEmpty(sClienttName))  || Utility.isNullOrEmpty(sClientEmail) || Utility.isNullOrEmpty(sClientFirstName)) {
                    appLogging.info("Please fill in all required fields");
                    Text errorText = new ErrorText("Please fill in all required fields","account_num") ;
                    arrErrorText.add(errorText);
                    responseStatus = RespConstants.Status.ERROR;
                } else {

                    UserBean loggedInUserBean = new UserBean();
                    if(request.getSession().getAttribute(Constants.USER_LOGGED_IN_BEAN)!=null) {
                        loggedInUserBean = (UserBean)request.getSession().getAttribute(Constants.USER_LOGGED_IN_BEAN);
                    }
                    if(loggedInUserBean!=null && !"".equalsIgnoreCase(loggedInUserBean.getUserId())) {

                        CheckPermission checkPermission = new CheckPermission(loggedInUserBean);
                        if(checkPermission!=null && checkPermission.can(Perm.EDIT_CLIENT)) {
                            AccessUsers accessUsers = new AccessUsers();
                            ParentTypeBean parentTypeBean = accessUsers.getParentTypeBeanFromUser( loggedInUserBean );

                            VendorBean vendorBean = parentTypeBean.getVendorBean() ;  // get  vendor from user id

                            UserRequestBean userRequestBean = new UserRequestBean();
                            userRequestBean.setFirstName(sClientFirstName);
                            userRequestBean.setLastName(sClientLastName);
                            userRequestBean.setEmail(sClientEmail);
                            userRequestBean.setCompanyName(sClientCompanyName);
                            userRequestBean.setCellPhone(sClientCellPhone);
                            userRequestBean.setWorkPhone(sClientWorkPhone);
                            userRequestBean.setAddress1(sClientAddress1);
                            userRequestBean.setAddress2(sClientAddress2);
                            userRequestBean.setCity(sClientCity);
                            userRequestBean.setState(sClientState);
                            userRequestBean.setCountry(sClientCountry);
                            userRequestBean.setPostalCode(sClientPostalCode);
                            userRequestBean.setUserId(sUserId);
                            userRequestBean.setUserInfoId(sUserInfoId);
                            userRequestBean.setUserType(Constants.USER_TYPE.CLIENT);
                            userRequestBean.setParentId(sClientId);

                            if(isLead){
                                sClienttName = sClientFirstName;
                            }
                            ClientRequestBean clientRequestBean = new ClientRequestBean();
                            clientRequestBean.setClientName(sClienttName);
                            clientRequestBean.setCorporateClient(isCorporateClient);
                            clientRequestBean.setUserRequestBean(userRequestBean);
                            clientRequestBean.setClientId(sClientId);
                            clientRequestBean.setVendorId(vendorBean.getVendorId());
                            clientRequestBean.setLead( isLead );

                            boolean isSaveClientAllowed = false;
                            UserBean userBean = accessUsers.getUserByEmail( userRequestBean );

                            if(userBean!=null && !Utility.isNullOrEmpty(userBean.getUserId())){
                                AccessClients accessClients = new AccessClients();
                                ClientResponseBean clientResponseBean = accessClients.getClientContactInfo( clientRequestBean );
                                if(clientResponseBean!=null && clientResponseBean.getUserInfoBean()!=null ){
                                    ClientBean clientBean = clientResponseBean.getClientBean();

                                    if(clientBean!=null && !Utility.isNullOrEmpty(clientRequestBean.getClientId()) && ( clientBean.getClientId().equalsIgnoreCase(clientRequestBean.getClientId()) ) ) {
                                        isSaveClientAllowed = true;
                                    } else {
                                        isSaveClientAllowed = false;
                                        Text errorText = new ErrorText("The email you entered is already in use. Please use a different email address.","err_mssg") ;
                                        arrErrorText.add(errorText);
                                    }
                                }
                            } else {
                                isSaveClientAllowed = true;
                            }

                            if(isSaveClientAllowed){
                                BuildClients buildClients = new BuildClients();
                                ClientResponseBean clientResponseBean = buildClients.saveClient(clientRequestBean);
                                appLogging.info("Proc page to save client" );
                                if(clientResponseBean!=null && !"".equalsIgnoreCase(clientResponseBean.getClientId())){
                                    Text okText = new OkText("Your changes were saved successfully.","status_mssg") ;
                                    arrOkText.add(okText);
                                    responseStatus = RespConstants.Status.OK;

                                    jsonResponseObj.put("client_response",clientResponseBean.toJson());
                                } else {
                                    appLogging.info("A client could not be created . " + clientRequestBean );
                                    Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(002)","err_mssg") ;
                                    arrErrorText.add(errorText);

                                    responseStatus = RespConstants.Status.ERROR;
                                }
                            } else {
                                responseStatus = RespConstants.Status.ERROR;
                            }



                        } else {
                            Text errorText = new ErrorText("Oops!! You are not authorized to perform this action.(saveClient - 003)","err_mssg") ;
                            arrErrorText.add(errorText);

                            responseStatus = RespConstants.Status.ERROR;
                        }

                    } else {
                        appLogging.info("Could identify a logged on user . ");
                        Text errorText = new ErrorText("Oops!! Please login and try again. We were unable to process your request at this time.","err_mssg") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;
                    }
                }
            } else {
                appLogging.info("Insecure Parameters used in this Proc Page " + Utility.dumpRequestParameters(request).toString()  + " --> " + this.getClass().getName());
                Text errorText = new ErrorText("Please use valid parameters. We have identified insecure parameters in your form.","account_num") ;
                arrErrorText.add(errorText);
                responseStatus = RespConstants.Status.ERROR;
            }
        } catch(Exception e) {
            appLogging.info("An exception occurred in the Proc Page " + ExceptionHandler.getStackTrace(e) );
            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(001)","err_mssg") ;
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
