package com.events.proc.event.website;

import com.events.bean.event.website.EventWebsiteBean;
import com.events.bean.event.website.EventWebsitePageBean;
import com.events.bean.event.website.EventWebsitePageFeatureBean;
import com.events.bean.event.website.EventWebsiteRequestBean;
import com.events.bean.users.UserBean;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Utility;
import com.events.common.exception.ExceptionHandler;
import com.events.common.security.DataSecurityChecker;
import com.events.event.website.AccessEventWebsite;
import com.events.event.website.AccessEventWebsitePage;
import com.events.event.website.EventWebsitePageFeature;
import com.events.json.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 2/24/14
 * Time: 10:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcSaveEventWebsitePageFeatures extends HttpServlet {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APPLICATION_LOG);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                    String sEventId =  ParseUtil.checkNull(request.getParameter("event_id"));
                    String sPageType =  ParseUtil.checkNull(request.getParameter("page_type"));


                    EventWebsiteRequestBean eventWebsiteRequestBean = new EventWebsiteRequestBean();
                    eventWebsiteRequestBean.setEventId( sEventId );

                    AccessEventWebsite accessEventWebsite = new AccessEventWebsite();
                    EventWebsiteBean eventWebsiteBean = accessEventWebsite.getEventWebsite( eventWebsiteRequestBean );

                    EventWebsitePageBean eventWebsitePageBeanReq = new EventWebsitePageBean();
                    eventWebsitePageBeanReq.setEventWebsiteId(eventWebsiteBean.getEventWebsiteId());
                    eventWebsitePageBeanReq.setWebsiteThemeId( eventWebsiteBean.getWebsiteThemeId() );
                    eventWebsitePageBeanReq.setType( sPageType );


                    AccessEventWebsitePage accessEventWebsitePage = new AccessEventWebsitePage();
                    EventWebsitePageBean eventWebsitePageBean = accessEventWebsitePage.getEventWebsitePageByType(eventWebsitePageBeanReq);

                    if(eventWebsitePageBean!=null && !Utility.isNullOrEmpty(eventWebsitePageBean.getEventWebsitePageId())) {
                        Map<String,String[]> mapParameters = request.getParameterMap();
                        for(Map.Entry<String,String[]> requestParameters : mapParameters.entrySet()) {
                            String sKey =  requestParameters.getKey();
                            String[] strArrValue =  requestParameters.getValue();
                            if(!"event_id".equalsIgnoreCase(sKey) &&  !"page_type".equalsIgnoreCase(sKey)  &&  !"subdomain".equalsIgnoreCase(sKey)) {

                                for(String sValue : strArrValue )  {
                                    EventWebsitePageFeatureBean requestEWPFBean = new EventWebsitePageFeatureBean();
                                    requestEWPFBean.setEventWebsitePageId( eventWebsitePageBean.getEventWebsitePageId() );
                                    requestEWPFBean.setFeatureType( Constants.EVENT_WEBSITE_PAGE_FEATURETYPE.valueOf(  sKey ) );

                                    EventWebsitePageFeature eventWebsitePageFeature = new EventWebsitePageFeature();
                                    EventWebsitePageFeatureBean eventEWPFFromDB = eventWebsitePageFeature.getFeature( requestEWPFBean );

                                    if(eventEWPFFromDB==null || (eventEWPFFromDB!=null && Utility.isNullOrEmpty(eventEWPFFromDB.getEventWebsitePageFeatureId())) ) {

                                        eventEWPFFromDB.setUserId( eventWebsiteBean.getUserId() );
                                        eventEWPFFromDB.setEventWebsitePageId( eventWebsitePageBean.getEventWebsitePageId() );
                                        eventEWPFFromDB.setEventWebsitePageFeatureId( Utility.getNewGuid() );

                                        eventEWPFFromDB.setFeatureDescription( "-" );
                                        eventEWPFFromDB.setFeatureName( Constants.EVENT_WEBSITE_PAGE_FEATURETYPE.valueOf(  sKey ).toString() );
                                        eventEWPFFromDB.setFeatureType( Constants.EVENT_WEBSITE_PAGE_FEATURETYPE.valueOf(  sKey ) );
                                    }

                                    eventEWPFFromDB.setValue( sValue );

                                    Integer iNumOfFeaturesSet = eventWebsitePageFeature.setFeatureValue( eventEWPFFromDB );

                                }
                            }
                        }

                        if( "reception".equals(sPageType) || "ceremony".equals(sPageType)) {
                            String sKey = sPageType+"_showmap";
                            boolean isShowMap = ParseUtil.sTob( request.getParameter(sKey)) ;
                            if(!isShowMap){
                                EventWebsitePageFeatureBean requestEWPFBean = new EventWebsitePageFeatureBean();
                                requestEWPFBean.setEventWebsitePageId( eventWebsitePageBean.getEventWebsitePageId() );
                                requestEWPFBean.setFeatureType( Constants.EVENT_WEBSITE_PAGE_FEATURETYPE.valueOf(  sKey ) );

                                EventWebsitePageFeature eventWebsitePageFeature = new EventWebsitePageFeature();
                                EventWebsitePageFeatureBean eventEWPFFromDB = eventWebsitePageFeature.getFeature( requestEWPFBean );
                                if(eventEWPFFromDB!=null && !Utility.isNullOrEmpty(eventEWPFFromDB.getEventWebsitePageFeatureId())){
                                    eventEWPFFromDB.setValue( "off" );

                                    Integer iNumOfFeaturesSet = eventWebsitePageFeature.setFeatureValue( eventEWPFFromDB );
                                }
                            }
                        }
                        Text okText = new OkText("Your changes were successfully updated.","status_mssg") ;
                        arrOkText.add(okText);
                        responseStatus = RespConstants.Status.OK;
                    } else {
                        Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(saveEventWebPage - 003)","err_mssg") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;
                    }
                } else {
                    appLogging.info("Invalid request in Proc Page (loggedInUserBean)" + ParseUtil.checkNullObject(loggedInUserBean) );
                    Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(saveEventWebPage - 002)","err_mssg") ;
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
            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(saveEventWebPage - 001)","err_mssg") ;
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
