package com.events.proc.event.website;

import com.events.bean.event.website.EventHotelRequest;
import com.events.bean.event.website.EventHotelsBean;
import com.events.bean.event.website.EventWebsiteBean;
import com.events.bean.event.website.EventWebsiteRequestBean;
import com.events.bean.users.UserBean;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Utility;
import com.events.common.exception.ExceptionHandler;
import com.events.common.security.DataSecurityChecker;
import com.events.event.website.AccessEventWebsite;
import com.events.event.website.BuildEventHotels;
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
 * Date: 2/28/14
 * Time: 11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProcSaveEventWebsiteHotel  extends HttpServlet {
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
                    String sEventWebsiteId =  ParseUtil.checkNull(request.getParameter("event_website_id"));
                    String sEventHotelId =  ParseUtil.checkNull(request.getParameter("event_hotel_id"));

                    if(Utility.isNullOrEmpty(sEventWebsiteId)){
                        EventWebsiteRequestBean eventWebsiteRequestBean = new EventWebsiteRequestBean();
                        eventWebsiteRequestBean.setEventId( sEventId );

                        AccessEventWebsite accessEventWebsite = new AccessEventWebsite();
                        EventWebsiteBean eventWebsiteBean = accessEventWebsite.getEventWebsite(eventWebsiteRequestBean);

                        if(eventWebsiteBean!=null && !Utility.isNullOrEmpty(eventWebsiteBean.getEventWebsiteId())) {
                            sEventWebsiteId = eventWebsiteBean.getEventWebsiteId();
                        }

                    }
                    String sName = ParseUtil.checkNull(request.getParameter("hotel_name"));
                    String sPhone = ParseUtil.checkNull(request.getParameter("hotel_phone"));
                    String sAddress = ParseUtil.checkNull(request.getParameter("hotel_address"));
                    String sUrl = ParseUtil.checkNull(request.getParameter("hotel_url"));
                    String sInstructions = ParseUtil.checkNull(request.getParameter("hotel_instructions"));

                    EventHotelRequest eventHotelRequest = new EventHotelRequest();
                    eventHotelRequest.setEventHotelId(sEventHotelId);
                    eventHotelRequest.setEventWebsiteId(sEventWebsiteId);
                    eventHotelRequest.setName(sName);
                    eventHotelRequest.setPhone(sPhone);
                    eventHotelRequest.setAddress(sAddress);
                    eventHotelRequest.setUrl(sUrl);
                    eventHotelRequest.setInstructions(sInstructions);


                    BuildEventHotels buildEventHotel = new BuildEventHotels();
                    EventHotelsBean eventHotelBean = buildEventHotel.saveEventHotel( eventHotelRequest ) ;
                    if(eventHotelBean!=null && !Utility.isNullOrEmpty(eventHotelBean.getEventHotelId())) {
                        jsonResponseObj.put("event_hotel_bean" , eventHotelBean.toJson());
                        Text okText = new OkText("Your changes were successfully updated.","status_mssg") ;
                        arrOkText.add(okText);
                        responseStatus = RespConstants.Status.OK;
                    } else {
                        Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(saveEventHotels - 002)","err_mssg") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;
                    }

                } else {
                    appLogging.info("Invalid request in Proc Page (loggedInUserBean)" + ParseUtil.checkNullObject(loggedInUserBean) );
                    Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(saveEventHotels - 002)","err_mssg") ;
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
            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(saveEventHotels - 001)","err_mssg") ;
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
