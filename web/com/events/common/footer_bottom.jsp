<%@ page import="com.events.bean.vendors.website.VendorWebsiteFeatureBean" %>
<%@ page import="com.events.common.Constants" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.events.common.Utility" %>
<%@ page import="com.events.bean.vendors.website.VendorWebsiteBean" %>
<%@ page import="com.events.bean.vendors.VendorBean" %>
<%@ page import="com.events.vendors.website.AccessVendorWebsite" %>
<%@ page import="com.events.common.ParseUtil" %>
<%@ page import="com.events.users.AccessUsers" %>
<%@ page import="com.events.bean.users.ParentTypeBean" %>
<%@ page import="com.events.vendors.AccessVendors" %>
<%@ page import="com.events.bean.vendors.VendorResponseBean" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%

    Logger appLogging = LoggerFactory.getLogger(Constants.APPLICATION_LOG);

    String sHomeUrl = Constants.EMPTY;
    boolean isVendorSubDomainUsed = false;
    String sCopyrightCompany = Constants.EMPTY;
    String sCopyrightCompanyUrl = Constants.EMPTY;
    HashMap<Constants.VENDOR_WEBSITE_FEATURETYPE , VendorWebsiteFeatureBean> hmVendorWebsiteFeatureBean = new HashMap<Constants.VENDOR_WEBSITE_FEATURETYPE, VendorWebsiteFeatureBean>();
    if( session.getAttribute("SUBDOMAIN_VENDOR") != null && session.getAttribute("SUBDOMAIN_VENDOR_WEBSITE") !=null ) {
        VendorBean vendorBean = (VendorBean)  session.getAttribute("SUBDOMAIN_VENDOR");

        AccessVendors accessVendors = new AccessVendors();
        VendorResponseBean vendorResponseBean = accessVendors.getVendorContactInfo( vendorBean );
        if(vendorResponseBean!=null && vendorResponseBean.getUserInfoBean()!=null){
            sHomeUrl = ParseUtil.checkNull(vendorResponseBean.getUserInfoBean().getWebsite());
        }

        sCopyrightCompany = ParseUtil.checkNull( vendorBean.getVendorName());
        VendorWebsiteBean vendorWebsiteBean = (VendorWebsiteBean) session.getAttribute("SUBDOMAIN_VENDOR_WEBSITE");
        if(vendorWebsiteBean!=null && !Utility.isNullOrEmpty(vendorWebsiteBean.getVendorWebsiteId())){
            isVendorSubDomainUsed = true;
            AccessVendorWebsite accessVendorWebsite = new AccessVendorWebsite();
            hmVendorWebsiteFeatureBean =  accessVendorWebsite.getPublishedFeaturesForWebPages(vendorWebsiteBean);
        }
    }
    com.events.common.Configuration applicationConfig = com.events.common.Configuration.getInstance(com.events.common.Constants.APPLICATION_PROP);

    boolean isAboutUsShown = false;
    boolean isContactShown = false;
    boolean isPrivacyShown = false;
    boolean isPricingShown = false;
    boolean isFollowusShown = false;


    String sFacebookUrl = Constants.EMPTY;
    String sTwitterUrl = Constants.EMPTY;
    String sPinterestUrl = Constants.EMPTY;
    if(isVendorSubDomainUsed && hmVendorWebsiteFeatureBean!=null && !hmVendorWebsiteFeatureBean.isEmpty()) {
        VendorWebsiteFeatureBean vendorWebsiteFeaturAboutUsBean =  hmVendorWebsiteFeatureBean.get(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_about_us );
        if(vendorWebsiteFeaturAboutUsBean!=null) {
            isAboutUsShown = ParseUtil.sTob(vendorWebsiteFeaturAboutUsBean.getValue() );
        }

        VendorWebsiteFeatureBean vendorWebsiteFeatueContactBean =  hmVendorWebsiteFeatureBean.get(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_contact);
        if(vendorWebsiteFeatueContactBean!=null) {
            isContactShown = ParseUtil.sTob(vendorWebsiteFeatueContactBean.getValue() );
        }

        VendorWebsiteFeatureBean vendorWebsiteFeatuePrivacyBean =  hmVendorWebsiteFeatureBean.get(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_privacy);
        if(vendorWebsiteFeatuePrivacyBean!=null) {
            isPrivacyShown = ParseUtil.sTob(vendorWebsiteFeatuePrivacyBean.getValue() );
        }

        VendorWebsiteFeatureBean vendorWebsiteFeatueFollowUsBean =  hmVendorWebsiteFeatureBean.get(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_followus);
        if(vendorWebsiteFeatueFollowUsBean!=null) {
            isFollowusShown = ParseUtil.sTob(vendorWebsiteFeatueFollowUsBean.getValue() );
        }

        if(isFollowusShown){
            VendorWebsiteFeatureBean vendorWebsiteFeatueFacebookUrlBean =  hmVendorWebsiteFeatureBean.get(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_facebook);
            if(vendorWebsiteFeatueFacebookUrlBean!=null) {
                sFacebookUrl = ParseUtil.checkNull(vendorWebsiteFeatueFacebookUrlBean.getValue() );
            }
            if(!Utility.isNullOrEmpty(sFacebookUrl) &&  ( !sFacebookUrl.startsWith("http://") || !sFacebookUrl.startsWith("https://"))  ){
                sFacebookUrl = "https://"+sFacebookUrl;
            }

            VendorWebsiteFeatureBean vendorWebsiteFeatueTwitterUrlBean =  hmVendorWebsiteFeatureBean.get(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_twitter);
            if(vendorWebsiteFeatueTwitterUrlBean!=null) {
                sTwitterUrl = ParseUtil.checkNull(vendorWebsiteFeatueTwitterUrlBean.getValue() );
            }
            if(!Utility.isNullOrEmpty(sTwitterUrl) &&  ( !sTwitterUrl.startsWith("http://") || !sTwitterUrl.startsWith("https://"))  ){
                sTwitterUrl = "https://"+sTwitterUrl;
            }

            VendorWebsiteFeatureBean vendorWebsiteFeatuePinterestUrlBean =  hmVendorWebsiteFeatureBean.get(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_pinterest);
            if(vendorWebsiteFeatuePinterestUrlBean!=null) {
                sPinterestUrl = ParseUtil.checkNull(vendorWebsiteFeatuePinterestUrlBean.getValue() );
            }
            if(!Utility.isNullOrEmpty(sPinterestUrl) &&  ( !sPinterestUrl.startsWith("http://") || !sPinterestUrl.startsWith("https://"))  ){
                sPinterestUrl = "https://"+sPinterestUrl;
            }
        }
    } else {
        isAboutUsShown = true;
        isContactShown = true;
        isPrivacyShown = true;
        isFollowusShown = true;
        isPricingShown = true;

        sFacebookUrl =  ParseUtil.checkNull(applicationConfig.get("facebook_url"));
        sTwitterUrl =  ParseUtil.checkNull(applicationConfig.get("twitter_url"));
    }


     String sCopyrightYear = applicationConfig.get("copyright_year");
    if(Utility.isNullOrEmpty(sCopyrightCompany)){
        sCopyrightCompany = ParseUtil.checkNull(applicationConfig.get("copyright_company"));
        sHomeUrl = ParseUtil.checkNull(applicationConfig.get("copyright_company_website"));
    }
%>
<footer>
    <div class="container">
        <div class="row">
            <div class="col-md-12 col-sm-12">
                <div class="row">
                    <div class="col-md-8 col-sm-8">
                        <div id="footer-tabs">
                            <ul class="footer-tabs">
                                <%if(isAboutUsShown){%><li><a href="/com/events/common/about_us.jsp">About</a></li><%}%>
                                <%if(isContactShown){%><li><a href="/com/events/common/contact.jsp">Contact</a></li><%}%>
                                <%if(isPrivacyShown){%><li><a href="/com/events/common/privacy.jsp">Privacy</a></li><%}%>
                                <%if(isPricingShown){%><li><a href="/com/events/common/pricing.jsp">Pricing</a></li><%}%>
                                <li><a href="/com/events/common/faqsupport/faq.jsp">FAQ (How it Works?)</a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="col-md-4 col-sm-4" style="text-align:right;">
                        <%if(isFollowusShown){%>
                        <h4>
                            <%if(!Utility.isNullOrEmpty(sFacebookUrl)){%><a  href="<%=sFacebookUrl%>" target="_blank"><i class="fa fa-facebook"></i></a><%}%>
                            <%if(!Utility.isNullOrEmpty(sTwitterUrl)){%><a  href="<%=sTwitterUrl%>" target="_blank"><i class="fa fa-twitter"></i></a><%}%>
                            <%if(!Utility.isNullOrEmpty(sPinterestUrl)){%><a  href="<%=sPinterestUrl%>" target="_blank"><i class="fa fa-pinterest"></i></a><%}%>
                        </h4>
                        <%}%>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 col-sm-12">
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 col-sm-12">

                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 col-sm-12">
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12" style="text-align:right;">
                        <a href="<%=sHomeUrl%>">&copy; <%=sCopyrightYear %> <%=sCopyrightCompany %></a>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12 col-sm-12">
                        &nbsp;
                    </div>
                </div>
            </div>
        </div>
    </div>
</footer>