package com.events.proc.comments;

import com.events.bean.common.comments.CommentsBean;
import com.events.bean.common.comments.CommentsRequestBean;
import com.events.bean.common.comments.CommentsResponseBean;
import com.events.bean.users.UserBean;
import com.events.bean.users.UserInfoBean;
import com.events.bean.users.UserRequestBean;
import com.events.common.Constants;
import com.events.common.DateSupport;
import com.events.common.ParseUtil;
import com.events.common.Utility;
import com.events.common.comments.BuildComments;
import com.events.common.exception.ExceptionHandler;
import com.events.common.security.DataSecurityChecker;
import com.events.data.comments.BuildCommentsData;
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
 * Date: 8/26/14
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProcSaveComments  extends HttpServlet {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APPLICATION_LOG);

    public void doPost(HttpServletRequest request,  HttpServletResponse response)  throws ServletException, IOException {
        RespObjectProc responseObject = new RespObjectProc();
        JSONObject jsonResponseObj = new JSONObject();
        ArrayList<Text> arrOkText = new ArrayList<Text>();
        ArrayList<Text> arrErrorText = new ArrayList<Text>();
        RespConstants.Status responseStatus = RespConstants.Status.ERROR;

        try{
            if( !DataSecurityChecker.isInsecureInputResponse(request) ) {

                UserBean loggedInUserBean = new UserBean();
                if(request.getSession().getAttribute(Constants.USER_LOGGED_IN_BEAN)!=null) {
                    loggedInUserBean = (UserBean)request.getSession().getAttribute(Constants.USER_LOGGED_IN_BEAN);
                }
                if(loggedInUserBean!=null && !"".equalsIgnoreCase(loggedInUserBean.getUserId())) {
                    String sCommentBody = ParseUtil.checkNull(request.getParameter("comment_body"));
                    String sParentCommentId = ParseUtil.checkNull(request.getParameter("parent_comment_id"));

                    appLogging.info("sCommentBody . " + sCommentBody + " - " + loggedInUserBean.getUserId() + " - " + DateSupport.getUTCDateTime());
                    Long lCurrentTime = DateSupport.getEpochMillis();

                    CommentsRequestBean commentsRequestBean = new CommentsRequestBean();
                    commentsRequestBean.setComment( sCommentBody );
                    commentsRequestBean.setCreateDate( lCurrentTime );
                    commentsRequestBean.setHumanCreateDate( DateSupport.getUTCDateTime() );
                    commentsRequestBean.setParentId( sParentCommentId );
                    commentsRequestBean.setUserId( loggedInUserBean.getUserId() );

                    BuildComments buildComments = new BuildComments();
                    CommentsResponseBean commentsResponseBean = buildComments.saveComment( commentsRequestBean );

                    if(commentsResponseBean!=null && commentsResponseBean.getCommentsBean()!=null){
                        CommentsBean commentsBean = commentsResponseBean.getCommentsBean();

                        if(commentsBean!=null && !Utility.isNullOrEmpty(commentsBean.getCommentsId())){

                            commentsBean.setUserName( loggedInUserBean.getUserInfoBean().getEmail() );

                            jsonResponseObj.put( "comments_bean", commentsBean.toJson() );

                            Text okText = new OkText("Your comment was saved successfully.","status_mssg") ;
                            arrOkText.add(okText);
                            responseStatus = RespConstants.Status.OK;

                        } else {
                            Text errorText = new ErrorText("Oops!! We were unable to process your request. Please try again later.(saveComments - 002)","err_mssg") ;
                            arrErrorText.add(errorText);

                            responseStatus = RespConstants.Status.ERROR;
                        }
                    } else {
                        Text errorText = new ErrorText("Oops!! We were unable to process your request. Please try again later.(saveComments - 001)","err_mssg") ;
                        arrErrorText.add(errorText);

                        responseStatus = RespConstants.Status.ERROR;
                    }



                } else {
                    appLogging.info("Could identify a logged on user . ");
                    Text errorText = new ErrorText("Oops!! Please login and try again. We were unable to process your request at this time.","err_mssg") ;
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
            Text errorText = new ErrorText("Oops!! We were unable to process your request at this time. Please try again later.(001)","err_mssg") ;
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
