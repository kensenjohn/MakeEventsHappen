package com.events.proc.event.checklist;

import com.events.bean.dashboard.checklist.ChecklistTemplateBean;
import com.events.bean.dashboard.checklist.ChecklistTemplateRequestBean;
import com.events.bean.dashboard.checklist.ChecklistTemplateResponseBean;
import com.events.bean.users.ParentTypeBean;
import com.events.bean.users.UserBean;
import com.events.common.Configuration;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Utility;
import com.events.common.exception.ExceptionHandler;
import com.events.common.security.DataSecurityChecker;
import com.events.dashboard.checklist.AccessChecklistTemplate;
import com.events.dashboard.checklist.BuildChecklistTemplate;
import com.events.json.*;
import com.events.users.AccessUsers;
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
 * Date: 6/19/14
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcLoadEventChecklistTemplates extends HttpServlet {
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

                    AccessUsers accessUers = new AccessUsers();
                    ParentTypeBean parentTypeBean = accessUers.getParentTypeBeanFromUser( loggedInUserBean );

                    ChecklistTemplateRequestBean checklistTemplateRequestBean = new ChecklistTemplateRequestBean();
                    checklistTemplateRequestBean.setVendorId( parentTypeBean.getVendorId() );

                    AccessChecklistTemplate accessChecklistTemplate = new AccessChecklistTemplate();
                    ArrayList<ChecklistTemplateBean> arrChecklistTemplateBean = accessChecklistTemplate.getAllChecklistTemplatesByVendor( checklistTemplateRequestBean );
                    if(arrChecklistTemplateBean==null || (arrChecklistTemplateBean!=null && arrChecklistTemplateBean.isEmpty()) ) {
                        // Create a default checklist.
                        BuildChecklistTemplate buildChecklistTemplate = new BuildChecklistTemplate();
                        ChecklistTemplateResponseBean checklistTemplateResponseBean = buildChecklistTemplate.createDefaultChecklistTemplate( checklistTemplateRequestBean );
                        if(checklistTemplateResponseBean!=null && checklistTemplateResponseBean.getChecklistTemplateBean()!=null && !Utility.isNullOrEmpty(checklistTemplateResponseBean.getChecklistTemplateBean().getChecklistTemplateId() ) ){
                            arrChecklistTemplateBean = new ArrayList<ChecklistTemplateBean>();
                            arrChecklistTemplateBean.add( checklistTemplateResponseBean.getChecklistTemplateBean() );
                        }
                    }
                    JSONObject jsonAllChecklistTemplates = accessChecklistTemplate.getJsonAllChecklistTemplates(arrChecklistTemplateBean);
                    Long lNumberOfChecklistTemplates = 0L;
                    if(jsonAllChecklistTemplates!=null) {
                        lNumberOfChecklistTemplates = jsonAllChecklistTemplates.optLong("num_of_checklist_templates");
                        if(lNumberOfChecklistTemplates>0){
                            jsonResponseObj.put("all_checklist_templates", jsonAllChecklistTemplates );
                        }
                    }
                    jsonResponseObj.put("num_of_checklist_templates",lNumberOfChecklistTemplates);

                    Text okText = new OkText("Load of Conversation Complete.","status_mssg") ;
                    arrOkText.add(okText);
                    responseStatus = RespConstants.Status.OK;
                } else {
                    appLogging.info("Invalid request in Proc Page (loggedInUserBean)" + ParseUtil.checkNullObject(loggedInUserBean) );
                    Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(deleteChecklistTemplateItem - 002)","err_mssg") ;
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
            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(deleteChecklistTemplateItem - 001)","err_mssg") ;
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