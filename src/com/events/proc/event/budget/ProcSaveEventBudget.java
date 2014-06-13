package com.events.proc.event.budget;

import com.events.bean.event.budget.EventBudgetBean;
import com.events.bean.event.budget.EventBudgetRequestBean;
import com.events.bean.event.budget.EventBudgetResponseBean;
import com.events.bean.users.UserBean;
import com.events.common.Configuration;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Utility;
import com.events.common.exception.ExceptionHandler;
import com.events.common.security.DataSecurityChecker;
import com.events.event.budget.BuildEventBudget;
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
 * Date: 5/16/14
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProcSaveEventBudget extends HttpServlet {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APPLICATION_LOG);
    private static final Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);

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
                    String sEventId = ParseUtil.checkNull( request.getParameter("event_id") );
                    String sEventBudgetId = ParseUtil.checkNull( request.getParameter("eventbudget_id") );
                    String sEstimatedBudget = ParseUtil.checkNull( request.getParameter("budget_estimate") );
                    Double dEstimatedBudget = ParseUtil.sToD(  sEstimatedBudget  );

                    if(Utility.isNullOrEmpty(sEventId)){
                        Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please select a valid event.","err_mssg") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;
                    } else if( !ParseUtil.isValidDouble( sEstimatedBudget )){
                        Text errorText = new ErrorText("Please enter a valid estimated budget amount","err_mssg") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;
                    } else {
                        EventBudgetRequestBean eventBudgetRequestBean = new EventBudgetRequestBean();
                        eventBudgetRequestBean.setEventId( sEventId );
                        eventBudgetRequestBean.setEventBudgetId( sEventBudgetId );
                        eventBudgetRequestBean.setUserId( loggedInUserBean.getUserId() );
                        eventBudgetRequestBean.setEstimatedBudget( dEstimatedBudget );

                        BuildEventBudget buildEventBudget = new BuildEventBudget();
                        EventBudgetResponseBean eventBudgetResponseBean = buildEventBudget.saveEventBudgetEstimate(eventBudgetRequestBean);

                        if( eventBudgetResponseBean!=null ) {
                            EventBudgetBean eventBudgetBean = eventBudgetResponseBean.getEventBudgetBean();
                            if(eventBudgetBean!=null && !Utility.isNullOrEmpty(eventBudgetBean.getEventBudgetId() )) {
                                Text okText = new OkText("Your changes were saved successfully.","status_mssg") ;
                                arrOkText.add(okText);
                                responseStatus = RespConstants.Status.OK;

                                jsonResponseObj.put( "event_budget_bean", eventBudgetBean.toJson() );
                            } else {
                                Text errorText = new ErrorText("Oops!! We were unable create/update your Estimated Budget. Please try again later.(saveEventBudget - 004)","err_mssg") ;
                                arrErrorText.add(errorText);

                                responseStatus = RespConstants.Status.ERROR;
                            }
                        } else {
                            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(saveEventBudget - 003)","err_mssg") ;
                            arrErrorText.add(errorText);

                            responseStatus = RespConstants.Status.ERROR;
                        }
                    }




                } else {
                    appLogging.info("Invalid request in Proc Page (loggedInUserBean)" + ParseUtil.checkNullObject(loggedInUserBean) );
                    Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(saveEventBudget - 002)","err_mssg") ;
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
            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(saveEventBudget - 001)","err_mssg") ;
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