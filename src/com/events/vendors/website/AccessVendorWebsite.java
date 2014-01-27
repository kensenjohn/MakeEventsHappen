package com.events.vendors.website;

import com.events.bean.vendors.website.VendorWebsiteBean;
import com.events.bean.vendors.website.VendorWebsiteFeatureBean;
import com.events.bean.vendors.website.VendorWebsiteRequestBean;
import com.events.bean.vendors.website.VendorWebsiteResponseBean;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Utility;
import com.events.data.vendors.website.AccessVendorWebsiteData;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 1/26/14
 * Time: 11:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccessVendorWebsite extends VendorWebsite{
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

            String sVendorWebsiteId = ParseUtil.checkNull(vendorWebsiteBean.getVendorWebsiteId());
            VendorWebsiteFeature vendorWebsiteFeature = new VendorWebsiteFeature();
            arrMultipleFeatureBean = vendorWebsiteFeature.getMultipleFeatures(arrVendorWebsiteFeatureBean, sVendorWebsiteId );

        }
        return arrMultipleFeatureBean;
    }
}