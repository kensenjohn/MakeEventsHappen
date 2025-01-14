<%@ page import="com.events.common.Constants" %>
<%@ page import="com.events.bean.users.UserBean" %>
<%@ page import="com.events.users.CookieUser" %>
<%@ page import="com.events.bean.users.CookieRequestBean" %>
<%@ page import="com.events.bean.users.CookieUserResponseBean" %>
<%@ page import="com.events.common.Utility" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@ page import="com.events.bean.users.CookieUserBean" %>
<%@ page import="com.events.bean.vendors.VendorBean" %>
<%@ page import="com.events.bean.vendors.website.VendorWebsiteBean" %>
<%@ page import="com.events.common.ParseUtil" %>
<%@ page import="com.events.common.Configuration" %>
<jsp:include page="/com/events/common/header_top.jsp">
    <jsp:param name="page_title" value=""/>
</jsp:include>
<jsp:include page="/com/events/common/header_bottom.jsp"/>
<%
    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    String APPLICATION_DOMAIN = applicationConfig.get(Constants.APPLICATION_DOMAIN);
    Logger appLogging = LoggerFactory.getLogger(Constants.APPLICATION_LOG);

    String sSubDomain = ParseUtil.checkNull(request.getParameter("subdomain"));
    String sHomeUrl = Utility.createSiteDomainUrl(  sSubDomain );


    Cookie[] cookies = request.getCookies();
    if(cookies!=null) {
        for(int cookieCount = 0; cookieCount < cookies.length; cookieCount++) {
            Cookie cookie1 = cookies[cookieCount];
            if (Constants.COOKIEUSER_ID.equals(cookie1.getName())) {
                String cookieUserId = cookie1.getValue();

                CookieRequestBean cookieRequestBean = new CookieRequestBean();
                cookieRequestBean.setCookieUserId(cookieUserId);
                CookieUser cookieUser = new CookieUser();
                CookieUserResponseBean cookieUserResponseBean = cookieUser.getCookieUser(cookieRequestBean);
                if(cookieUserResponseBean != null && !Utility.isNullOrEmpty(cookieUserResponseBean.getCookieUserId()) ) {

                    UserBean userBeanFromSession = (UserBean)request.getSession().getAttribute(Constants.USER_LOGGED_IN_BEAN);
                    CookieUserBean cookieUserBean = cookieUserResponseBean.getCookieUserBean();

                    if(userBeanFromSession!=null && cookieUserBean!=null && userBeanFromSession.getUserId().equalsIgnoreCase(cookieUserBean.getUserId()) ) {
                        cookie1.setMaxAge( 0 ); //deletes cookie immediately
                        cookie1.setPath("/");
                        cookie1.setValue(cookieUserResponseBean.getCookieUserId());

                        //response.set
                        response.addCookie( cookie1 );
                    }
                }

            }
        }
    }
    session.removeAttribute( Constants.USER_LOGGED_IN_BEAN);

    response.sendRedirect(sHomeUrl);

%>
<body>
<div class="page_wrap">
    <jsp:include page="/com/events/common/top_nav.jsp"/>
    <jsp:include page="/com/events/common/menu_bar.jsp"/>
    <div class="breadcrumb_format">
        <div class="container">
            <div class="page-title">Logout</div>
        </div>
    </div>
    <div class="container">
    </div>
</div>
</body>
<jsp:include page="/com/events/common/footer_top.jsp"/>
<script   type="text/javascript">

</script>
<jsp:include page="/com/events/common/footer_bottom.jsp"/>