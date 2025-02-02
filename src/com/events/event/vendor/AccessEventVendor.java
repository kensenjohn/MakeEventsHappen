package com.events.event.vendor;

import com.events.bean.event.vendor.EventVendorBean;
import com.events.bean.event.vendor.EventVendorFeatureBean;
import com.events.bean.event.vendor.EventVendorRequestBean;
import com.events.bean.event.vendor.EveryEventVendorBean;
import com.events.bean.vendors.VendorBean;
import com.events.bean.vendors.VendorFeatureBean;
import com.events.bean.vendors.VendorRequestBean;
import com.events.bean.vendors.partner.EveryPartnerVendorBean;
import com.events.bean.vendors.partner.PartnerVendorBean;
import com.events.bean.vendors.partner.PartnerVendorRequestBean;
import com.events.bean.vendors.partner.PartnerVendorResponseBean;
import com.events.common.Constants;
import com.events.common.Utility;
import com.events.data.event.vendor.AccessEventVendorData;
import com.events.vendors.AccessVendors;
import com.events.vendors.partner.AccessPartnerVendor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 2/10/14
 * Time: 5:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class AccessEventVendor {

    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APPLICATION_LOG);

    public ArrayList<EveryEventVendorBean> getPotentialVendors(EventVendorRequestBean eventVendorRequestBean) {
        ArrayList<EveryEventVendorBean> arrEveryEventVendorBean = new ArrayList<EveryEventVendorBean>();
        if(eventVendorRequestBean!=null && !Utility.isNullOrEmpty( eventVendorRequestBean.getVendorId() )){

            PartnerVendorRequestBean partnerVendorRequestBean = new   PartnerVendorRequestBean();
            partnerVendorRequestBean.setVendorId( eventVendorRequestBean.getVendorId() );
            AccessPartnerVendor accessPartnerVendor = new AccessPartnerVendor();
            ArrayList<EveryPartnerVendorBean> arrEveryPartnerVendorBeans = accessPartnerVendor.getAllPartnerVendorsForVendor(partnerVendorRequestBean);

            if( arrEveryPartnerVendorBeans!=null && !arrEveryPartnerVendorBeans.isEmpty() )  {
                ArrayList<EventVendorBean> arrEventVendorBean = getEventVendors( eventVendorRequestBean );
                HashMap<String, ArrayList<EventVendorFeatureBean> > hmEventVendorFeatures = new HashMap<String, ArrayList<EventVendorFeatureBean>>();
                HashMap<String, EventVendorBean > hmEventVendorBean = new HashMap<String, EventVendorBean>();
                if(arrEventVendorBean!=null && !arrEventVendorBean.isEmpty()){
                    for(EventVendorBean eventVendorBean : arrEventVendorBean ) {
                        ArrayList<EventVendorFeatureBean> arrMultipleFeatureBean = getEventVendorSummaryFeatures(eventVendorBean);

                        if(arrMultipleFeatureBean!=null && !arrMultipleFeatureBean.isEmpty()) {
                            hmEventVendorFeatures.put( eventVendorBean.getEventVendorId() , arrMultipleFeatureBean);
                            hmEventVendorBean.put( eventVendorBean.getVendorId() , eventVendorBean );
                        }
                    }
                }

                for(EveryPartnerVendorBean everyPartnerVendorBean : arrEveryPartnerVendorBeans ) {
                    EveryEventVendorBean everyEventVendorBean = new EveryEventVendorBean();
                    everyEventVendorBean.setEventId( eventVendorRequestBean.getEventId() );
                    everyEventVendorBean.setEveryPartnerVendorBean( everyPartnerVendorBean );

                    EventVendorBean eventVendorBean = hmEventVendorBean.get( everyPartnerVendorBean.getPartnerVendorId() );
                    if(eventVendorBean!=null && !Utility.isNullOrEmpty(eventVendorBean.getEventVendorId())) {
                        everyEventVendorBean.setEventVendorBean( eventVendorBean );
                        ArrayList<EventVendorFeatureBean> arrEventVendorFeatures = hmEventVendorFeatures.get( eventVendorBean.getEventVendorId() );
                        if(arrEventVendorFeatures!=null && !arrEventVendorFeatures.isEmpty() ) {
                            setEveryEventVendorMultiFeatures( everyEventVendorBean , arrEventVendorFeatures );
                        }
                    }
                    arrEveryEventVendorBean.add(everyEventVendorBean);
                }
            }
        }
        return arrEveryEventVendorBean;
    }

    public void setEveryEventVendorMultiFeatures( EveryEventVendorBean everyEventVendorBean , ArrayList<EventVendorFeatureBean> arrEventVendorFeatures ) {

        if(everyEventVendorBean!=null && arrEventVendorFeatures!=null && !arrEventVendorFeatures.isEmpty() ) {
            for( EventVendorFeatureBean eventVendorFeatureBean : arrEventVendorFeatures ) {
                if(Constants.EVENT_VENDOR_FEATURETYPE.current_user_action.equals(eventVendorFeatureBean.getFeatureType())) {
                    if( Constants.EVENT_VENDOR_USER_ACTION.is_assigned.toString().equals( eventVendorFeatureBean.getValue() ) ){
                        everyEventVendorBean.setAssignedToEvent( true );
                    } else if( Constants.EVENT_VENDOR_USER_ACTION.is_recommended.toString().equals( eventVendorFeatureBean.getValue() ) ){
                        everyEventVendorBean.setRecommendedForEvent( true );
                    } else if( Constants.EVENT_VENDOR_USER_ACTION.is_shortlisted.toString().equals( eventVendorFeatureBean.getValue() ) ){
                        everyEventVendorBean.setShortlistedForEvent( true );
                    }
                }
            }
        }
    }

    public JSONObject getEveryEventVendorJSON(ArrayList<EveryEventVendorBean> arrEveryEventVendorBean){
        JSONObject everyEventVendorJsonObject = new JSONObject();
        if(arrEveryEventVendorBean!=null && !arrEveryEventVendorBean.isEmpty() ) {
            Integer iNumOfRecs = 0;
            for(EveryEventVendorBean everyEventVendorBean : arrEveryEventVendorBean) {
                everyEventVendorJsonObject.put(iNumOfRecs.toString(),everyEventVendorBean.toJson());
                iNumOfRecs++;
            }
            everyEventVendorJsonObject.put("num_of_potential_event_vendors",iNumOfRecs);
        }
        return everyEventVendorJsonObject;
    }

    public ArrayList<EventVendorBean>  getEventVendors( EventVendorRequestBean eventVendorRequestBean ){
        ArrayList<EventVendorBean> arrEventVendorBean = new ArrayList<EventVendorBean>();
        if(eventVendorRequestBean!=null && !Utility.isNullOrEmpty( eventVendorRequestBean.getEventId() )){
            AccessEventVendorData accessEventVendorData = new AccessEventVendorData();
            
            arrEventVendorBean = accessEventVendorData.getEventVendorsByEventId( eventVendorRequestBean );
        }
        return arrEventVendorBean;
    }

    public ArrayList<EventVendorFeatureBean> getEventVendorSummaryFeatures(EventVendorBean eventVendorBean){
        ArrayList<EventVendorFeatureBean> arrMultipleFeatureBean = new ArrayList<EventVendorFeatureBean>();
        if(eventVendorBean!=null && !Utility.isNullOrEmpty(eventVendorBean.getEventVendorId() )) {
            ArrayList<EventVendorFeatureBean> arrTmpMultipleFeatureBean = new ArrayList<EventVendorFeatureBean>();
            arrTmpMultipleFeatureBean.add(EventVendorFeature.generateEventVendorFeatureBean(Constants.EVENT_VENDOR_FEATURETYPE.current_user_action));
            EventVendorFeature vendorFeature = new EventVendorFeature();
            arrMultipleFeatureBean =  vendorFeature.getMultipleFeatures(arrTmpMultipleFeatureBean , eventVendorBean.getEventVendorId()  );
        }
        return arrMultipleFeatureBean;
    }
    public ArrayList<EveryEventVendorBean> getEventVendorsList(EventVendorRequestBean eventVendorRequestBean) {

        ArrayList<EveryEventVendorBean> arrEveryEventVendorBean = new ArrayList<EveryEventVendorBean>();
        if(eventVendorRequestBean!=null && !Utility.isNullOrEmpty( eventVendorRequestBean.getEventId() )){
            ArrayList<EventVendorBean> arrEventVendorBean = getEventVendors( eventVendorRequestBean );   //All Event Vendor Beans
            if(arrEventVendorBean!=null && !arrEventVendorBean.isEmpty()){
                for(EventVendorBean eventVendorBean : arrEventVendorBean ) {

                    EveryEventVendorBean everyEventVendorBean = new EveryEventVendorBean();
                    everyEventVendorBean.setEventId( eventVendorBean.getEventId() );
                    everyEventVendorBean.setEventVendorBean( eventVendorBean );

                    ArrayList<EventVendorFeatureBean> arrMultipleFeatureBean = getEventVendorSummaryFeatures(eventVendorBean);   //Features of Event Vendor Beans
                    setEveryEventVendorMultiFeatures(everyEventVendorBean , arrMultipleFeatureBean);

                    PartnerVendorRequestBean partnerVendorRequestBean = new   PartnerVendorRequestBean();
                    partnerVendorRequestBean.setPartnerVendorId( eventVendorBean.getVendorId() );
                    AccessPartnerVendor accessPartnerVendor = new AccessPartnerVendor();
                    PartnerVendorResponseBean partnerVendorResponseBean = accessPartnerVendor.getPartnerVendorDetails( partnerVendorRequestBean );   // Details of the Vendors
                    if(partnerVendorResponseBean!=null && !Utility.isNullOrEmpty(partnerVendorResponseBean.getPartnerVendorId())) {
                        PartnerVendorBean partnerVendorBean = partnerVendorResponseBean.getPartnerVendorBean();

                        VendorRequestBean vendorRequestBean = new VendorRequestBean();
                        vendorRequestBean.setVendorId( partnerVendorBean.getPartnerVendorId() );

                        AccessVendors accessVendor = new AccessVendors();
                        VendorBean vendorBean = accessVendor.getVendor(vendorRequestBean);    // Vendor Name



                        PartnerVendorRequestBean tmpPartnerVendorRequestBean = new PartnerVendorRequestBean();
                        tmpPartnerVendorRequestBean.setPartnerVendorId(vendorBean.getVendorId());

                        ArrayList<VendorFeatureBean> arrMultipleVendorFeatureBean = accessPartnerVendor.getPartnerVendorSummaryFeatures( tmpPartnerVendorRequestBean );

                        EveryPartnerVendorBean everyPartnerVendorBean = new EveryPartnerVendorBean();
                        everyPartnerVendorBean.setPartnerVendorId(  vendorBean.getVendorId() );
                        everyPartnerVendorBean.setName( vendorBean.getVendorName() );

                        accessPartnerVendor.setEveryPartnerVendorMultiFeature(everyPartnerVendorBean , arrMultipleVendorFeatureBean);
                        everyEventVendorBean.setEveryPartnerVendorBean( everyPartnerVendorBean );


                        arrEveryEventVendorBean.add(everyEventVendorBean);

                    }

                }
            }
        }
        return arrEveryEventVendorBean;
    }

    public EventVendorBean getEventVendor( EventVendorRequestBean eventVendorRequestBean ){
        EventVendorBean eventVendorBean = new EventVendorBean();
        if(eventVendorRequestBean!=null && !Utility.isNullOrEmpty( eventVendorRequestBean.getEventId() )
                && !Utility.isNullOrEmpty( eventVendorRequestBean.getVendorId() ) ){
            AccessEventVendorData accessEventVendorData = new AccessEventVendorData();
            eventVendorBean = accessEventVendorData.getEventVendor( eventVendorRequestBean );
        }
        return eventVendorBean;
    }
}
