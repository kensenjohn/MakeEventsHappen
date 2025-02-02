package com.events.proc.event.website;

import com.events.bean.common.FeatureBean;
import com.events.bean.event.EventRequestBean;
import com.events.bean.event.website.*;
import com.events.bean.users.UserBean;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Utility;
import com.events.common.exception.ExceptionHandler;
import com.events.common.feature.Feature;
import com.events.common.feature.FeatureType;
import com.events.common.security.DataSecurityChecker;
import com.events.event.website.AccessEventWebsite;
import com.events.event.website.AccessEventWebsitePage;
import com.events.event.website.AccessWebsiteThemes;
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

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 2/22/14
 * Time: 5:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProcLoadEventWebsitePageFeatures extends HttpServlet {
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
                    if(!Utility.isNullOrEmpty(sEventId) && !Utility.isNullOrEmpty(sPageType)) {
                        EventWebsiteRequestBean eventWebsiteRequestBean = new EventWebsiteRequestBean();
                        eventWebsiteRequestBean.setEventId( sEventId );

                        AccessEventWebsite accessEventWebsite = new AccessEventWebsite();
                        EventWebsiteBean eventWebsiteBean = accessEventWebsite.getEventWebsite(eventWebsiteRequestBean);
                        if(eventWebsiteBean!=null && !Utility.isNullOrEmpty(eventWebsiteBean.getEventWebsiteId())) {

                            EventWebsitePageBean eventWebsitePageBeanReg = new EventWebsitePageBean();
                            eventWebsitePageBeanReg.setEventWebsiteId( eventWebsiteBean.getEventWebsiteId() );
                            eventWebsitePageBeanReg.setWebsiteThemeId( eventWebsiteBean.getWebsiteThemeId() );
                            eventWebsitePageBeanReg.setType( sPageType );


                            AccessEventWebsitePage accessEventWebsitePage = new AccessEventWebsitePage();
                            EventWebsitePageBean eventWebsitePageBean = accessEventWebsitePage.getEventWebsitePageByType(eventWebsitePageBeanReg);

                            if(eventWebsitePageBean!=null && !Utility.isNullOrEmpty(eventWebsitePageBean.getEventWebsitePageId() )) {
                                EventWebsitePageFeature eventWebsitePageFeature = new EventWebsitePageFeature();
                                ArrayList<EventWebsitePageFeatureBean> arrMultipleFeatureBean =  eventWebsitePageFeature.getMultipleFeatures(eventWebsitePageBean.getEventWebsitePageId() );

                                JSONObject jsonEventWebsitePageFeatures = new JSONObject();
                                if(arrMultipleFeatureBean!=null && !arrMultipleFeatureBean.isEmpty()) {
                                    for(EventWebsitePageFeatureBean eventWebsitePageFeatureBean : arrMultipleFeatureBean ) {
                                        jsonEventWebsitePageFeatures.put(eventWebsitePageFeatureBean.getFeatureName() , eventWebsitePageFeatureBean.getValue() );
                                    }
                                }
                                jsonResponseObj.put("event_website_page_feature" , jsonEventWebsitePageFeatures);
                                jsonResponseObj.put("event_website_page" , eventWebsitePageBean.toJson());
                                jsonResponseObj.put("page_type" , sPageType );
                                jsonResponseObj.put("image_host", Utility.getImageUploadHost() );
                                jsonResponseObj.put("bucket", Utility.getS3Bucket() );

                                EventRequestBean eventRequestBean = new EventRequestBean();
                                eventRequestBean.setEventId(sEventId);

                                FeatureBean featureBean = new FeatureBean();
                                featureBean.setEventId(sEventId );
                                featureBean.setFeatureType(FeatureType.image_location);

                                Feature feature = new Feature();
                                featureBean = feature.getFeature(featureBean);
                                if(featureBean!=null && !Utility.isNullOrEmpty(featureBean.getFeatureId())){
                                    jsonResponseObj.put("image_folder_location",featureBean.getValue() );
                                }



                                Text okText = new OkText("Website Themes loaded","status_mssg") ;
                                arrOkText.add(okText);
                                responseStatus = RespConstants.Status.OK;
                            } else {
                                Text errorText = new ErrorText("Please select a theme for this website. We were unable to load the colors and fonts.","err_mssg") ;
                                arrErrorText.add(errorText);

                                responseStatus = RespConstants.Status.ERROR;
                            }

                        }  else {
                            Text errorText = new ErrorText("Please select a theme for this website. We were unable to load page information.","err_mssg") ;
                            arrErrorText.add(errorText);

                            responseStatus = RespConstants.Status.ERROR;
                        }

                    } else {
                        Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(loadWebPageRecords - 003)","err_mssg") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;
                    }
                } else {
                    appLogging.info("Invalid request in Proc Page (loggedInUserBean)" + ParseUtil.checkNullObject(loggedInUserBean) );
                    Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(loadWebPageRecords - 002)","err_mssg") ;
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
            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(loadWebPageRecords - 001)","err_mssg") ;
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