package com.events.proc.dashboard.checklist;

import com.events.bean.dashboard.checklist.ChecklistTemplateItemBean;
import com.events.bean.dashboard.checklist.ChecklistTemplateRequestBean;
import com.events.bean.dashboard.checklist.ChecklistTemplateResponseBean;
import com.events.bean.dashboard.checklist.ChecklistTemplateTodoBean;
import com.events.bean.users.UserBean;
import com.events.common.Configuration;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Utility;
import com.events.common.exception.ExceptionHandler;
import com.events.common.security.DataSecurityChecker;
import com.events.dashboard.checklist.BuildChecklistTemplate;
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
 * Date: 6/14/14
 * Time: 9:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProcSaveChecklistTemplateItem extends HttpServlet {
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
                    String sChecklistTemplateItemId = ParseUtil.checkNull(request.getParameter("checklist_template_item_id"));
                    String sChecklistTemplateId = ParseUtil.checkNull(request.getParameter("checklist_template_id"));
                    String sChecklistTemplateItemName = ParseUtil.checkNull(request.getParameter("checklist_template_item_name"));
                    String[] aChecklistTodoId = request.getParameterValues("checklist_template_todo_id[]");

                    ArrayList<String> arrChecklistTodoId = new ArrayList<String>();
                    if(aChecklistTodoId!=null && aChecklistTodoId.length>0){
                        for(String checklistTodoId : aChecklistTodoId )  {
                            arrChecklistTodoId.add(checklistTodoId);
                        }
                    }

                    ArrayList<ChecklistTemplateTodoBean> arrChecklistTemplateTodoBean = new ArrayList<ChecklistTemplateTodoBean>();
                    if(arrChecklistTodoId!=null && !arrChecklistTodoId.isEmpty() ) {
                        for(String checklistTodoId : arrChecklistTodoId )  {
                            ChecklistTemplateTodoBean checklistTemplateTodoBean = new ChecklistTemplateTodoBean();
                            checklistTemplateTodoBean.setChecklistTemplateTodoId( checklistTodoId );
                            checklistTemplateTodoBean.setChecklistTemplateItemId( sChecklistTemplateItemId );
                            checklistTemplateTodoBean.setChecklistTemplateId( sChecklistTemplateId );
                            String sTodo = ParseUtil.checkNull( request.getParameter("checklist_item_todo_"+checklistTodoId) );
                            if(!Utility.isNullOrEmpty(sTodo)){
                                checklistTemplateTodoBean.setName( sTodo );
                                arrChecklistTemplateTodoBean.add( checklistTemplateTodoBean );
                            }
                        }
                    }

                    /*checklist_template_todo_id[]:40a1267e-99c3-629e-5abd-e81ccfe85f9f
                    checklist_template_todo_id[]:8*/
                    if( Utility.isNullOrEmpty(sChecklistTemplateItemName)) {
                        Text errorText = new ErrorText("","err_mssg") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;
                    } else {

                        ChecklistTemplateRequestBean checklistTemplateRequestBean = new ChecklistTemplateRequestBean();
                        checklistTemplateRequestBean.setChecklistTemplateItemId( sChecklistTemplateItemId );
                        checklistTemplateRequestBean.setChecklistTemplateId( sChecklistTemplateId );
                        checklistTemplateRequestBean.setChecklistTemplateItemName( sChecklistTemplateItemName );
                        checklistTemplateRequestBean.setArrChecklistTemplateTodoBean( arrChecklistTemplateTodoBean );



                        BuildChecklistTemplate buildChecklistTemplate = new BuildChecklistTemplate();
                        ChecklistTemplateResponseBean checklistTemplateResponseBean = buildChecklistTemplate.saveChecklistTemplateItem( checklistTemplateRequestBean );

                        if(checklistTemplateResponseBean!=null){
                            ChecklistTemplateItemBean checklistTemplateItemBean = checklistTemplateResponseBean.getChecklistTemplateItemBean();

                            if(checklistTemplateItemBean!=null && !Utility.isNullOrEmpty(checklistTemplateItemBean.getChecklistTemplateItemId() )) {
                                jsonResponseObj.put( "checklist_template_item_bean", checklistTemplateItemBean.toJson() );

                                Text okText = new OkText("Your changes were saved successfully.","status_mssg") ;
                                arrOkText.add(okText);
                                responseStatus = RespConstants.Status.OK;
                            } else {
                                Text errorText = new ErrorText("Oops!! We were unable to save the checklist template item. Please try again later.(saveChecklistTemplateItem - 004)","err_mssg") ;
                                arrErrorText.add(errorText);

                                responseStatus = RespConstants.Status.ERROR;
                            }
                        } else {
                            Text errorText = new ErrorText("Oops!! We were unable to save the checklist template item. Please try again later.(saveChecklistTemplateItem - 003)","err_mssg") ;
                            arrErrorText.add(errorText);

                            responseStatus = RespConstants.Status.ERROR;
                        }
                    }
                } else {
                    appLogging.info("Invalid request in Proc Page (loggedInUserBean)" + ParseUtil.checkNullObject(loggedInUserBean) );
                    Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(saveChecklistTemplateItem - 002)","err_mssg") ;
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
            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(saveChecklistTemplate - 001)","err_mssg") ;
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

