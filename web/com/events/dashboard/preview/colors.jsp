<%@ page import="com.events.common.ParseUtil" %>
<jsp:include page="/com/events/common/header_top.jsp">
    <jsp:param name="page_title" value=""/>
</jsp:include>
<meta http-equiv="x-ua-compatible" content="IE=10">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="/css/spectrum.css">
<jsp:include page="/com/events/common/header_bottom.jsp"/>
<%

    String sColorBackground = ParseUtil.checkNull(request.getParameter("website_color_bkg")).replaceAll("%23","#");
    String sColorTabackground = ParseUtil.checkNull(request.getParameter("website_color_tab_bkg")).replaceAll("%23","#");
    String sColorBreadCrumbBackground = ParseUtil.checkNull(request.getParameter("website_color_breadcrumb_bkg")).replaceAll("%23","#");
    String sColorBorder = ParseUtil.checkNull(request.getParameter("website_color_border")).replaceAll("%23","#");


    String sColorFilledButton = ParseUtil.checkNull(request.getParameter("website_color_filled_button")).replaceAll("%23","#");
    String sColorFilledButtonTxt = ParseUtil.checkNull(request.getParameter("website_color_filled_button_txt")).replaceAll("%23","#");
    String sColorHoverDefaultButton = ParseUtil.checkNull(request.getParameter("website_color_default_button")).replaceAll("%23","#");
    String sColorHoverDefaultButtonTxt = ParseUtil.checkNull(request.getParameter("website_color_default_button_txt")).replaceAll("%23","#");

    String sColorDefaultTxt = ParseUtil.checkNull(request.getParameter("website_color_default_text")).replaceAll("%23","#");
%>
<body>
<div class="page_wrap">
    <div class="top_navbar_format">
        <div class="container">
            <div class="top_navbar_links">
                <ul class="nav navbar-nav navbar-right menu">
                    <li><a href="">Hi Tester</a></li>
                    <li><a href="">Default Text</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="menu_bar">
        <div class="container">
            <div class="menu_logo">
                <a href="#"><img src="/img/logo.png" alt=""></a>
            </div>
            <div class="menu_links">
                <ul class="nav navbar-nav navbar-right menu">
                    <li class="navbar-btn navbar-btn-format">
                        <button  type="button" class="btn  btn-filled" id="btn_create_event">
                            <span class="glyphicon glyphicon-plus"></span><a>  Create Event </a>
                        </button>
                    </li>
                    <li class="active"><a href="#">Active</a></li>
                    <li class=""><a href="#">Inactive</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="breadcrumb_format">
        <div class="container">
            <div class="page-title">Breadcrumb area. See Border top and below.</div>
        </div>
    </div>
    <div class="container">
        <div class="content_format">
            <div class="row">
                <div class="col-md-12">
                    <ul class="tabs">
                        <li class="active"><a href="#">Active Tab</a></li>
                        <li><a href="#">Inactive 1</a></li>
                        <li><a href="#">Inactive 2</a></li>
                    </ul>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    &nbsp;
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <h1>Default text color 1</h1>
                    <h2>Default text color 2</h2>
                    <h3>Default text color 3</h3>
                </div>
                <div class="col-md-6">
                    <form method="post" id="frm_website_colors">
                        <div class="form-group">
                            <div class="row">
                                <div class="col-md-6">
                                    <label for="preview_formname" class="form_label">Name</label><span class="required"> *</span>
                                    <input type="text"  class="form-control"  name="preview_formname" id="preview_formname"  placeholder="Name" value="Preview Test">
                                </div>
                                <div class="col-md-6">
                                    <label for="preview_formage" class="form_label">Age</label><span class="required"> *</span>
                                    <input type="text"  class="form-control"  name="preview_formage" id="preview_formage"  placeholder="Your Age ">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    &nbsp;
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <button type="button" class="btn btn-default" >Default Button</button>
                                </div>
                                <div class="col-md-6">
                                    <button type="button" class="btn btn-filled">Filled Button</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>