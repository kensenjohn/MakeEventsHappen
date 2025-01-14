package com.events.proc.vendors.partner;

import com.events.bean.users.UserBean;
import com.events.bean.vendors.VendorBean;
import com.events.bean.vendors.VendorRequestBean;
import com.events.bean.vendors.partner.EveryPartnerVendorBean;
import com.events.bean.vendors.partner.PartnerVendorRequestBean;
import com.events.common.Configuration;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Utility;
import com.events.common.exception.ExceptionHandler;
import com.events.common.security.DataSecurityChecker;
import com.events.json.*;
import com.events.vendors.AccessVendors;
import com.events.vendors.partner.AccessPartnerVendor;
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
 * User: kensen
 * Date: 2/4/14
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcLoadAllPartnerVendors  extends HttpServlet {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APPLICATION_LOG);

    public void doPost(HttpServletRequest request,  HttpServletResponse response)  throws ServletException, IOException {
        RespObjectProc responseObject = new RespObjectProc();
        JSONObject jsonResponseObj = new JSONObject();
        ArrayList<Text> arrOkText = new ArrayList<Text>();
        ArrayList<Text> arrErrorText = new ArrayList<Text>();
        RespConstants.Status responseStatus = RespConstants.Status.ERROR;
        try{
            if( !DataSecurityChecker.isInsecureInputResponse(request) ) {
                UserBean loggedInUserBean = (UserBean)request.getSession().getAttribute(Constants.USER_LOGGED_IN_BEAN);

                if(loggedInUserBean!=null && !Utility.isNullOrEmpty(loggedInUserBean.getUserId()) ) {
                    String sUserId = ParseUtil.checkNull(loggedInUserBean.getUserId());
                    VendorRequestBean vendorRequestBean = new VendorRequestBean();
                    vendorRequestBean.setUserId(sUserId);

                    AccessVendors accessVendors = new AccessVendors();
                    VendorBean vendorBean = accessVendors.getVendorByUserId(vendorRequestBean);

                    if(vendorBean!=null && !Utility.isNullOrEmpty(vendorBean.getVendorId())) {
                        PartnerVendorRequestBean partnerVendorRequestBean = new   PartnerVendorRequestBean();
                        partnerVendorRequestBean.setVendorId( vendorBean.getVendorId() );
                        AccessPartnerVendor accessPartnerVendor = new AccessPartnerVendor();
                        ArrayList<EveryPartnerVendorBean> arrEveryPartnerVendorBeans = accessPartnerVendor.getAllPartnerVendorsForVendor(partnerVendorRequestBean);
                        JSONObject everyPartnerVendorJson = accessPartnerVendor.getAllPartnerVendorsForVendorJson(arrEveryPartnerVendorBeans);

                        Integer iNumOfPartnerVendors = 0;
                        if(everyPartnerVendorJson!=null){
                            iNumOfPartnerVendors = everyPartnerVendorJson.optInt("num_of_partner_vendors");
                            jsonResponseObj.put("every_partner_vendor_bean", everyPartnerVendorJson);
                        }

                        jsonResponseObj.put( "num_of_partner_vendors", iNumOfPartnerVendors );

                        Text okText = new OkText("Partner Vendors are successfully loaded.","status_mssg") ;
                        arrOkText.add(okText);
                        responseStatus = RespConstants.Status.OK;
                    } else {
                        Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later,.(lAPVendors - 003)","err_mssg") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;
                    }

                } else {
                    appLogging.info("Invalid request in Proc Page (loggedInUserBean)" + ParseUtil.checkNullObject(loggedInUserBean) );
                    Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(lAPVendors - 002)","err_mssg") ;
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
            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(lAPVendors - 001)","err_mssg") ;
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
