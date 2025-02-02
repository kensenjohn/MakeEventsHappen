package com.events.proc.event;

import com.events.bean.clients.ClientBean;
import com.events.bean.clients.ClientRequestBean;
import com.events.bean.common.notify.NotifyBean;
import com.events.bean.event.EventRequestBean;
import com.events.bean.event.EventResponseBean;
import com.events.bean.users.UserBean;
import com.events.bean.users.UserRequestBean;
import com.events.bean.vendors.VendorBean;
import com.events.bean.vendors.VendorRequestBean;
import com.events.clients.AccessClients;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Utility;
import com.events.common.exception.ExceptionHandler;
import com.events.common.security.DataSecurityChecker;
import com.events.event.BuildEvent;
import com.events.json.*;
import com.events.users.AccessUsers;
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
 * Date: 12/21/13
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcSaveEvent   extends HttpServlet {
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

                if(loggedInUserBean!=null && !"".equalsIgnoreCase(loggedInUserBean.getUserId())) {

                    String sEventId = ParseUtil.checkNull(request.getParameter("eventId"));
                    String sEventName = ParseUtil.checkNull(request.getParameter("eventName"));
                    String sEventDay = ParseUtil.checkNull(request.getParameter("formatted_eventDay"));
                    String sEventTime = ParseUtil.checkNull(request.getParameter("eventTime"));
                    String sEventTimeZone = ParseUtil.checkNull(request.getParameter("eventTimeZone"));
                    String sEventClient = ParseUtil.checkNull(request.getParameter("eventClient"));
                    String sClientName = ParseUtil.checkNull(request.getParameter("clientName"));
                    String sClientEmail = ParseUtil.checkNull(request.getParameter("clientEmail"));
                    boolean isEventClientCorporate = ParseUtil.sTob(request.getParameter("isClientCorporate"));


                    boolean isLoggedInUserAClient = false;
                    boolean isLoggedInUserAVendor = false;
                    ClientRequestBean clientRequestBean = new ClientRequestBean();
                    clientRequestBean.setClientId( loggedInUserBean.getParentId());

                    AccessClients accessClients = new AccessClients();
                    ClientBean clientBean = accessClients.getClient( clientRequestBean );
                    if(clientBean!=null && !Utility.isNullOrEmpty(clientBean.getClientId())) {
                        isLoggedInUserAClient = true;
                    }

                    VendorBean vendorBean = new VendorBean();

                    VendorRequestBean vendorRequestBean = new VendorRequestBean();
                    AccessVendors accessVendor = new AccessVendors();
                    if(isLoggedInUserAClient) {
                        vendorRequestBean.setVendorId(  clientBean.getVendorId() );
                        vendorBean = accessVendor.getVendor( vendorRequestBean );
                    } else {
                        vendorRequestBean.setUserId( loggedInUserBean.getUserId() );
                        vendorBean = accessVendor.getVendorByUserId( vendorRequestBean ) ;  // get  vendor from user id
                    }

                    boolean isAllowedToCreateClient = true;
                    if("create_client".equalsIgnoreCase(sEventClient ) &&!Utility.isNullOrEmpty(sClientEmail)){
                        UserRequestBean userRequestBean = new UserRequestBean();
                        userRequestBean.setEmail( sClientEmail );
                        AccessUsers accessUsers = new AccessUsers();
                        UserBean userBean = accessUsers.getUserByEmail( userRequestBean );

                        if(userBean!=null && !Utility.isNullOrEmpty(userBean.getUserId())) {
                            isAllowedToCreateClient = false;
                        }
                    }




                    if( sEventClient!=null && "create_client".equalsIgnoreCase(sEventClient )
                            && ("".equalsIgnoreCase(sClientName) || "".equalsIgnoreCase(sClientEmail))  ) {
                        Text errorText = new ErrorText("Please enter a valid Client Name and Client Email","err_mssg") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;
                    } else if ( "".equalsIgnoreCase(sEventName) || "".equalsIgnoreCase(sEventDay)
                            || "".equalsIgnoreCase(sEventTime) || "".equalsIgnoreCase(sEventTimeZone) || "".equalsIgnoreCase(sEventClient) ) {

                        Text errorText = new ErrorText("Please enter a valid Client Name and Client Email","err_mssg") ;
                        arrErrorText.add(errorText);
                        responseStatus = RespConstants.Status.ERROR;
                    } else if (!isLoggedInUserAClient && ( vendorBean ==null || (vendorBean!=null && Utility.isNullOrEmpty(vendorBean.getVendorId())) )  ) {
                        Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please login and try again.","err_mssg") ;
                        arrErrorText.add(errorText);
                        responseStatus = RespConstants.Status.ERROR;
                    } else if (isLoggedInUserAClient && ( clientBean ==null || (clientBean!=null && Utility.isNullOrEmpty(clientBean.getClientId())) )  ) {
                        Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please login and try again.","err_mssg") ;
                        arrErrorText.add(errorText);
                        responseStatus = RespConstants.Status.ERROR;
                    } else if("create_client".equalsIgnoreCase(sEventClient ) && !isAllowedToCreateClient ){
                        Text errorText = new ErrorText("Please use a different client email. The email address already exists.","err_mssg") ;
                        arrErrorText.add(errorText);
                        responseStatus = RespConstants.Status.ERROR;
                    } else {

                        EventRequestBean eventRequestBean = new EventRequestBean();
                        eventRequestBean.setEventId(sEventId);
                        eventRequestBean.setEventName( sEventName );
                        eventRequestBean.setEventDay(sEventDay);
                        eventRequestBean.setEventTime( sEventTime );
                        eventRequestBean.setEventTimeZone( sEventTimeZone );
                        eventRequestBean.setEventClient(sEventClient);
                        eventRequestBean.setEventClientName(sClientName);
                        eventRequestBean.setEventClientEmail(sClientEmail);
                        eventRequestBean.setEventClientCorporate( isEventClientCorporate );
                        eventRequestBean.setEventVendorId( vendorBean.getVendorId() );
                        eventRequestBean.setUserId( loggedInUserBean.getUserId() );

                        BuildEvent buildEvent = new BuildEvent();
                        EventResponseBean eventResponseBean =  buildEvent.saveEvent(eventRequestBean);

                        if(eventResponseBean!=null && !"".equalsIgnoreCase(eventResponseBean.getEventId())) {
                            jsonResponseObj.put("event_id",eventResponseBean.getEventId());

                            Text okText = new OkText("The event was saved successfully","status_mssg") ;
                            arrOkText.add(okText);
                            responseStatus = RespConstants.Status.OK;
                        } else {
                            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(saveEvent - 003)","err_mssg") ;
                            arrErrorText.add(errorText);

                            responseStatus = RespConstants.Status.ERROR;
                        }
                    }

                }   else {
                    appLogging.info("Invalid request in Proc Page (loggedInUserBean)" + ParseUtil.checkNullObject(loggedInUserBean) );
                    Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(saveEvent - 002)","err_mssg") ;
                    arrErrorText.add(errorText);

                    responseStatus = RespConstants.Status.ERROR;
                }

            }  else {
                appLogging.info("Insecure Parameters used in this Proc Page " + Utility.dumpRequestParameters(request).toString()  + " --> " + this.getClass().getName());
                Text errorText = new ErrorText("Please use valid parameters. We have identified insecure parameters in your form.","account_num") ;
                arrErrorText.add(errorText);
                responseStatus = RespConstants.Status.ERROR;
            }

        }  catch(Exception e) {
            appLogging.info("An exception occurred in the Proc Page " + ExceptionHandler.getStackTrace(e) );
            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(saveEvent - 001)","err_mssg") ;
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
