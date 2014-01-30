package com.events.proc.users.permissions;

import com.events.bean.users.UserBean;
import com.events.bean.users.permissions.PermissionsBean;
import com.events.bean.users.permissions.RolePermissionsBean;
import com.events.bean.users.permissions.UserRolePermissionRequestBean;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Perm;
import com.events.common.Utility;
import com.events.common.exception.ExceptionHandler;
import com.events.common.security.DataSecurityChecker;
import com.events.json.ErrorText;
import com.events.json.RespConstants;
import com.events.json.RespObjectProc;
import com.events.json.Text;
import com.events.users.permissions.AccessPermissions;
import com.events.users.permissions.AccessRolePermissions;
import com.events.users.permissions.CheckPermission;
import com.events.users.permissions.UserRolePermission;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 1/30/14
 * Time: 1:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcLoadRolePermissions extends HttpServlet {
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

                if(loggedInUserBean!=null && !"".equalsIgnoreCase(loggedInUserBean.getUserId())) {
                    String sRoleId = ParseUtil.checkNull(request.getParameter("role_id"));


                    CheckPermission checkPermission = new CheckPermission(loggedInUserBean);
                    if( checkPermission.can(Perm.VIEW_ROLE_PERMMISIONS ) ) {

                        Constants.USER_TYPE loggedInUserType = loggedInUserBean.getUserType();
                        UserRolePermissionRequestBean userRolePermRequest = new UserRolePermissionRequestBean();
                        userRolePermRequest.setRoleId( sRoleId);
                        userRolePermRequest.setUserType(loggedInUserType);
                        UserRolePermission userRolePermission = new UserRolePermission();
                        userRolePermission.getRolePermissions(userRolePermRequest);


                    } else {
                        appLogging.error("No Permission to View Role Permission : " + sRoleId + " - " + ParseUtil.checkNullObject(loggedInUserBean) );
                        Text errorText = new ErrorText("Oops!! Please make sure you are authorized to execute this action.(deleteRole - 003)","err_mssg") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;
                    }
                } else {
                    appLogging.info("Invalid request in Proc Page (loggedInUserBean)" + ParseUtil.checkNullObject(loggedInUserBean) );
                    Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(loadRolePerms - 002)","err_mssg") ;
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
            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(loadRolePerms - 001)","err_mssg") ;
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