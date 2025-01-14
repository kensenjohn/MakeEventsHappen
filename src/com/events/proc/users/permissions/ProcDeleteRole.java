package com.events.proc.users.permissions;

import com.events.bean.users.UserBean;
import com.events.bean.users.permissions.UserRolePermissionRequestBean;
import com.events.bean.users.permissions.UserRolePermissionResponseBean;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Perm;
import com.events.common.Utility;
import com.events.common.exception.ExceptionHandler;
import com.events.common.security.DataSecurityChecker;
import com.events.json.*;
import com.events.users.permissions.CheckPermission;
import com.events.users.permissions.UserRolePermission;
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
 * Date: 1/30/14
 * Time: 9:07 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProcDeleteRole extends HttpServlet {
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
                    String sRoleId = ParseUtil.checkNull(request.getParameter("role_id"));

                    CheckPermission checkPermission = new CheckPermission(loggedInUserBean);
                    if( checkPermission.can(Perm.DELETE_ROLE ) ) {
                        UserRolePermissionRequestBean userRolePermissionRequestBean = new UserRolePermissionRequestBean();
                        userRolePermissionRequestBean.setRoleId( sRoleId );
                        userRolePermissionRequestBean.setUserId( loggedInUserBean.getUserId() );

                        UserRolePermission userRolePermission = new UserRolePermission();
                        UserRolePermissionResponseBean userRolePermissionResponseBean = userRolePermission.deleteRole(  userRolePermissionRequestBean );
                        if(userRolePermissionResponseBean!=null){
                            String sMessage = ParseUtil.checkNull( userRolePermissionResponseBean.getMessage() );
                            if(userRolePermissionResponseBean.isSuccess()){
                                jsonResponseObj.put("deleted_role_id",sRoleId);
                                jsonResponseObj.put("is_deleted",true);

                                Text okText = new OkText("The role was successfully deleted.","status_mssg") ;
                                arrOkText.add(okText);
                                responseStatus = RespConstants.Status.OK;
                            } else {
                                Text errorText = new ErrorText("Oops!! " + sMessage ,"err_mssg") ;
                                arrErrorText.add(errorText);
                                responseStatus = RespConstants.Status.ERROR;
                            }
                        } else {
                            appLogging.error("Response  to Delete Role  was null : " + sRoleId + " - " + ParseUtil.checkNullObject(loggedInUserBean) );
                            Text errorText = new ErrorText("Oops!! We were unable to delete the role you requested." ,"err_mssg") ;
                            arrErrorText.add(errorText);

                            responseStatus = RespConstants.Status.ERROR;
                        }
                    } else {
                        appLogging.error("No Permission to Delete Role : " + sRoleId + " - " + ParseUtil.checkNullObject(loggedInUserBean) );
                        Text errorText = new ErrorText("Oops!! Please make sure you are authorized to execute this action.(deleteRole - 003)","err_mssg") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;
                    }
                } else {
                    appLogging.info("Invalid request in Proc Page (loggedInUserBean)" + ParseUtil.checkNullObject(loggedInUserBean) );
                    Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(deleteRole - 002)","err_mssg") ;
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
            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(deleteRole - 001)","err_mssg") ;
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