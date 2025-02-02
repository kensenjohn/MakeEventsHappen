package com.events.vendors.website;

import com.events.bean.users.ParentTypeBean;
import com.events.bean.users.UserBean;
import com.events.bean.vendors.VendorBean;
import com.events.bean.vendors.VendorResponseBean;
import com.events.bean.vendors.website.*;
import com.events.common.Configuration;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Utility;
import com.events.data.vendors.website.AccessVendorWebsiteData;
import com.events.users.AccessUsers;
import com.events.vendors.AccessVendors;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 1/26/14
 * Time: 11:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccessVendorWebsite extends VendorWebsite{
    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    public VendorWebsiteFeatureBean getSubDomain(VendorBean vendorBean) {

        VendorWebsiteFeatureBean vendorWebsiteFeatureBeanFromDB = new VendorWebsiteFeatureBean();
        if(vendorBean != null && !Utility.isNullOrEmpty(vendorBean.getVendorId())) {

            VendorWebsiteRequestBean vendorWebsiteRequestBean = new VendorWebsiteRequestBean();
            vendorWebsiteRequestBean.setVendorId( vendorBean.getVendorId());

            VendorWebsiteBean vendorWebsiteBean = generateVendorWebsiteBean( vendorWebsiteRequestBean );
            vendorWebsiteBean.setVendorWebsiteId( vendorBean.getVendorId());

            AccessVendorWebsiteData accessVendorWebsiteData = new AccessVendorWebsiteData();
            vendorWebsiteBean = accessVendorWebsiteData.getVendorWebsiteByVendorId(vendorWebsiteBean);

            if(vendorWebsiteBean!=null && !Utility.isNullOrEmpty(vendorWebsiteBean.getVendorWebsiteId())) {

                VendorWebsiteFeatureBean vendorWebsiteFeatureBean = generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.subdomain_name) ;
                vendorWebsiteFeatureBean.setVendorWebsiteId( vendorWebsiteBean.getVendorWebsiteId() );

                VendorWebsiteFeature vendorWebsiteFeature = new VendorWebsiteFeature();
                vendorWebsiteFeatureBeanFromDB =  vendorWebsiteFeature.getFeature(vendorWebsiteFeatureBean);
            }

        }
        return vendorWebsiteFeatureBeanFromDB;
    }

    public VendorResponseBean getVendorBySubDomain(VendorWebsiteRequestBean vendorWebsiteRequestBean) {
        VendorResponseBean vendorResponseBean = new VendorResponseBean();
        if(vendorWebsiteRequestBean != null && !Utility.isNullOrEmpty(vendorWebsiteRequestBean.getSubDomain())
                && !"www".equalsIgnoreCase(vendorWebsiteRequestBean.getSubDomain())) {
            AccessVendorWebsiteData accessVendorWebsiteData = new AccessVendorWebsiteData();
            vendorResponseBean = accessVendorWebsiteData.getVendorBySubDomain(vendorWebsiteRequestBean);
        }
        return vendorResponseBean;
    }
    public VendorWebsiteResponseBean getVendorWebsiteByVendorId(VendorWebsiteRequestBean vendorWebsiteRequestBean) {
        VendorWebsiteResponseBean vendorWebsiteResponseBean = new VendorWebsiteResponseBean();
        if(vendorWebsiteRequestBean!=null && !Utility.isNullOrEmpty(vendorWebsiteRequestBean.getVendorId())) {
            VendorWebsiteBean vendorWebsiteBean = generateVendorWebsiteBean( vendorWebsiteRequestBean );

            AccessVendorWebsiteData accessVendorWebsiteData = new AccessVendorWebsiteData();
            vendorWebsiteBean = accessVendorWebsiteData.getVendorWebsiteByVendorId(vendorWebsiteBean);

            ArrayList<VendorWebsiteFeatureBean> arrMultipleFeatureBean = getFeatures(vendorWebsiteBean);

            vendorWebsiteResponseBean.setVendorWebsiteBean( vendorWebsiteBean);
            vendorWebsiteResponseBean.setArrVendorWebsiteFeatureBean( arrMultipleFeatureBean );
        }
        return vendorWebsiteResponseBean;
    }

    public VendorWebsiteResponseBean getVendorWebsiteFeaturesByVendorId(VendorWebsiteRequestBean vendorWebsiteRequestBean){
        VendorWebsiteResponseBean vendorWebsiteResponseBean =  new VendorWebsiteResponseBean();
        if(vendorWebsiteRequestBean!=null && !Utility.isNullOrEmpty(vendorWebsiteRequestBean.getVendorId())) {
            VendorWebsiteBean vendorWebsiteBean = new VendorWebsiteBean();
            vendorWebsiteBean.setVendorId( vendorWebsiteRequestBean.getVendorId() );

            AccessVendorWebsiteData accessVendorWebsiteData = new AccessVendorWebsiteData();
            vendorWebsiteResponseBean = accessVendorWebsiteData.getVendorWebsiteFeaturesByVendorId( vendorWebsiteBean );
        }
        return vendorWebsiteResponseBean;
    }

    public VendorWebsiteResponseBean getVendorWebsiteByWebsiteId(VendorWebsiteRequestBean vendorLandingPageRequestBean) {
        VendorWebsiteResponseBean vendorWebsiteResponseBean = new VendorWebsiteResponseBean();
        if(vendorLandingPageRequestBean!=null && !Utility.isNullOrEmpty(vendorLandingPageRequestBean.getVendorWebsiteId())) {
            VendorWebsiteBean vendorWebsiteBean = generateVendorWebsiteBean( vendorLandingPageRequestBean );

            AccessVendorWebsiteData accessVendorWebsiteData = new AccessVendorWebsiteData();
            vendorWebsiteBean= accessVendorWebsiteData.getVendorWebsiteByWebsiteId(vendorWebsiteBean);

            vendorWebsiteResponseBean.setVendorWebsiteBean( vendorWebsiteBean);
        }
        return vendorWebsiteResponseBean;
    }

    public ArrayList<VendorWebsiteFeatureBean> getFeatures( VendorWebsiteBean vendorWebsiteBean) {
        ArrayList<VendorWebsiteFeatureBean> arrMultipleFeatureBean = new ArrayList<VendorWebsiteFeatureBean>();
        if(vendorWebsiteBean!=null && !Utility.isNullOrEmpty(vendorWebsiteBean.getVendorWebsiteId())) {


            ArrayList<VendorWebsiteFeatureBean> arrVendorWebsiteFeatureBean = new ArrayList<VendorWebsiteFeatureBean>();
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_bkg_color) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_border_color) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_facebook_feed_script) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_filled_button_color) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_filled_button_text_color) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_highlighted_color) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_landingpagephoto) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_logo) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_navbar_breadcrumb_tab_color) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_pinterest_feed_script) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_plain_button_color) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_plain_button_text_color) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_text_color) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_themename) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_greeting_header) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_greeting_text) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.subdomain_name) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_about_us) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_contact) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_privacy) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_followus) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_footer_about_us) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_footer_contact) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_footer_privacy) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_footer_facebook) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_footer_twitter) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_footer_pinterest) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.saved_themename) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.show_landingpage_landingpagephoto) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.show_landingpage_social_media) );
            arrVendorWebsiteFeatureBean.add( generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.show_landingpage_greeting) );


            String sVendorWebsiteId = ParseUtil.checkNull(vendorWebsiteBean.getVendorWebsiteId());
            VendorWebsiteFeature vendorWebsiteFeature = new VendorWebsiteFeature();
            arrMultipleFeatureBean = vendorWebsiteFeature.getMultipleFeatures(arrVendorWebsiteFeatureBean, sVendorWebsiteId );

        }
        return arrMultipleFeatureBean;
    }

    public HashMap<Constants.VENDOR_WEBSITE_FEATURETYPE , VendorWebsiteFeatureBean> getPublishedFeaturesForWebPages( VendorWebsiteBean vendorWebsiteBean) {
        HashMap<Constants.VENDOR_WEBSITE_FEATURETYPE , VendorWebsiteFeatureBean> hmVendorWebsiteFeatureBean =   new HashMap<Constants.VENDOR_WEBSITE_FEATURETYPE, VendorWebsiteFeatureBean>();
        if(vendorWebsiteBean!=null && !Utility.isNullOrEmpty(vendorWebsiteBean.getVendorWebsiteId())) {


            HashMap<Constants.VENDOR_WEBSITE_FEATURETYPE , VendorWebsiteFeatureBean> hmDefaultVendorWebsiteFeatureBean = new HashMap<Constants.VENDOR_WEBSITE_FEATURETYPE , VendorWebsiteFeatureBean>();
            hmDefaultVendorWebsiteFeatureBean.put( Constants.VENDOR_WEBSITE_FEATURETYPE.published_bkg_color, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_bkg_color , "#ffffff") );
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_border_color, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_border_color, "#dbf1ff"));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_filled_button_color, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_filled_button_color, "#3F9CFF"));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_filled_button_text_color, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_filled_button_text_color, "#ffffff"));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_highlighted_color, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_highlighted_color, "#3F9CFF"));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_logo, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_logo));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_navbar_breadcrumb_tab_color, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_navbar_breadcrumb_tab_color, "#FCFCFC"));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_plain_button_color, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_plain_button_color, "#ffffff"));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_plain_button_text_color, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_plain_button_text_color, "#333333"));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_text_color, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_text_color, "#666666"));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_about_us, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_about_us ));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_contact, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_contact ));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_privacy, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_privacy));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_facebook, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_facebook ));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_twitter, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_twitter ));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_pinterest, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_footer_pinterest));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_about_us, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_about_us));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_contact, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_contact ));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_privacy, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_privacy ));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_followus, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.show_footer_followus));

            String sVendorWebsiteId = ParseUtil.checkNull(vendorWebsiteBean.getVendorWebsiteId());
            VendorWebsiteFeature vendorWebsiteFeature = new VendorWebsiteFeature();
            hmVendorWebsiteFeatureBean = vendorWebsiteFeature.getMultipleFeaturesWithDefaultValue(hmDefaultVendorWebsiteFeatureBean, sVendorWebsiteId);

        }
        return hmVendorWebsiteFeatureBean;
    }

    public HashMap<Constants.VENDOR_WEBSITE_FEATURETYPE , VendorWebsiteFeatureBean> getPublishedFeaturesForLandingPage( VendorWebsiteBean vendorWebsiteBean) {
        HashMap<Constants.VENDOR_WEBSITE_FEATURETYPE , VendorWebsiteFeatureBean> hmVendorWebsiteFeatureBean =   new HashMap<Constants.VENDOR_WEBSITE_FEATURETYPE, VendorWebsiteFeatureBean>();
        if(vendorWebsiteBean!=null && !Utility.isNullOrEmpty(vendorWebsiteBean.getVendorWebsiteId())) {


            HashMap<Constants.VENDOR_WEBSITE_FEATURETYPE , VendorWebsiteFeatureBean> hmDefaultVendorWebsiteFeatureBean = new HashMap<Constants.VENDOR_WEBSITE_FEATURETYPE , VendorWebsiteFeatureBean>();
            hmDefaultVendorWebsiteFeatureBean.put( Constants.VENDOR_WEBSITE_FEATURETYPE.published_greeting_header, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_greeting_header)   );
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_greeting_text, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_greeting_text));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_facebook_feed_script, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_facebook_feed_script ));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_pinterest_feed_script, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_pinterest_feed_script ));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.published_landingpagephoto, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.published_landingpagephoto));

            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.show_landingpage_social_media, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.show_landingpage_social_media ));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.show_landingpage_landingpagephoto, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.show_landingpage_landingpagephoto));
            hmDefaultVendorWebsiteFeatureBean.put(Constants.VENDOR_WEBSITE_FEATURETYPE.show_landingpage_greeting, generateVendorWebsiteFeatureBean(Constants.VENDOR_WEBSITE_FEATURETYPE.show_landingpage_greeting));


            String sVendorWebsiteId = ParseUtil.checkNull(vendorWebsiteBean.getVendorWebsiteId());
            VendorWebsiteFeature vendorWebsiteFeature = new VendorWebsiteFeature();
            hmVendorWebsiteFeatureBean = vendorWebsiteFeature.getMultipleFeaturesWithDefaultValue(hmDefaultVendorWebsiteFeatureBean, sVendorWebsiteId);

        }
        return hmVendorWebsiteFeatureBean;
    }

    public VendorWebsiteURLBean getVendorWebsiteUrlBean(UserBean userBean){
        VendorWebsiteURLBean vendorWebsiteURLBean = new VendorWebsiteURLBean();

        if(userBean!=null && !Utility.isNullOrEmpty(userBean.getUserId())) {
            AccessUsers accessUsers = new AccessUsers();
            ParentTypeBean parentTypeBean = accessUsers.getParentTypeBeanFromUser(userBean);
            if(parentTypeBean!=null && parentTypeBean.getVendorBean()!=null){
                VendorBean vendorBean = parentTypeBean.getVendorBean();

                vendorWebsiteURLBean = getVendorWebsiteUrlBean(vendorBean);

            }
        }
        

        return vendorWebsiteURLBean;
    }
    public VendorWebsiteURLBean getVendorWebsiteUrlBean(VendorBean vendorBean){
        VendorWebsiteURLBean vendorWebsiteURLBean = new VendorWebsiteURLBean();
        if(vendorBean!=null && !Utility.isNullOrEmpty(vendorBean.getVendorId())) {
            String sDomain = ParseUtil.checkNull(applicationConfig.get(Constants.APPLICATION_DOMAIN));
            String sProtocol = ParseUtil.checkNull(applicationConfig.get(Constants.PROP_LINK_PROTOCOL));

            AccessVendorWebsite accessVendorWebsite = new AccessVendorWebsite();
            VendorWebsiteFeatureBean vendorWebsiteFeatureBean = accessVendorWebsite.getSubDomain(vendorBean);
            String sSubDomain = ParseUtil.checkNull(vendorWebsiteFeatureBean.getValue());

            vendorWebsiteURLBean.setSubDomain(ParseUtil.checkNull(sSubDomain));
            vendorWebsiteURLBean.setDomain(sDomain );
            vendorWebsiteURLBean.setProtocol( sProtocol );
        }
        return vendorWebsiteURLBean;
    }
}
