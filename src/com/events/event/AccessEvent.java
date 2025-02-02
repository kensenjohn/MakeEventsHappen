package com.events.event;

import com.events.bean.common.FeatureBean;
import com.events.bean.event.EventBean;
import com.events.bean.event.EventDisplayDateBean;
import com.events.bean.event.EventRequestBean;
import com.events.bean.event.EventResponseBean;
import com.events.common.Configuration;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Utility;
import com.events.common.feature.Feature;
import com.events.common.feature.FeatureType;
import com.events.data.event.AccessEventData;
import com.events.data.feature.FeatureData;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 12/21/13
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccessEvent {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APPLICATION_LOG);

    public EventResponseBean getEventInfo(EventRequestBean eventRequestBean) {
        EventResponseBean eventResponseBean = new EventResponseBean();
        if(eventRequestBean!=null && !"".equalsIgnoreCase(eventRequestBean.getEventId())) {
            AccessEventData accessEventData = new AccessEventData();
            EventBean eventBean = accessEventData.getEvent(eventRequestBean);
            if(eventBean!=null && !"".equalsIgnoreCase(eventBean.getEventId())){
                eventResponseBean.setEventBean(eventBean);
            }
        }
        return eventResponseBean;
    }

    public EventDisplayDateBean getEventSelectedDisplayDate(EventRequestBean eventRequestBean) {
        EventDisplayDateBean eventDisplayDateBean = new EventDisplayDateBean();

        if(eventRequestBean!=null && !"".equalsIgnoreCase(eventRequestBean.getEventId())) {

            FeatureBean featureEventDay = new FeatureBean();
            featureEventDay.setFeatureType(FeatureType.event_day);

            FeatureBean featureEventTime = new FeatureBean();
            featureEventTime.setFeatureType(FeatureType.event_time);

            FeatureBean featureEventTimeZone = new FeatureBean();
            featureEventTimeZone.setFeatureType(FeatureType.event_timezone);

            ArrayList<FeatureBean> arrFeatureBean = new ArrayList<FeatureBean>();
            arrFeatureBean.add(featureEventDay);
            arrFeatureBean.add(featureEventTime);
            arrFeatureBean.add(featureEventTimeZone);

            FeatureData featureData = new FeatureData();
            ArrayList<FeatureBean> arrMultipleFeatureBean = featureData.getMultipleFeatures(arrFeatureBean,eventRequestBean.getEventId() );
            if(arrMultipleFeatureBean!=null && !arrMultipleFeatureBean.isEmpty()) {
                for(FeatureBean featureBean : arrMultipleFeatureBean ) {
                    switch(featureBean.getFeatureType()) {
                        case event_day:
                            eventDisplayDateBean.setSelectedDay(featureBean.getValue());
                            break;
                        case event_time:
                            eventDisplayDateBean.setSelectedTime(featureBean.getValue());
                            break;
                        case event_timezone:
                            eventDisplayDateBean.setSelectedTimeZone(featureBean.getValue());
                            break;
                    }
                }
            }
        }
        return eventDisplayDateBean;
    }

    public FeatureBean getFeatureValue(EventRequestBean eventRequestBean ,FeatureType featureType ){
        FeatureBean featureBean = new FeatureBean();
        if(eventRequestBean!=null && !Utility.isNullOrEmpty(eventRequestBean.getEventId()) && featureType!=null ) {

            featureBean.setEventId( eventRequestBean.getEventId() );
            featureBean.setFeatureType( featureType );

            Feature feature  = new Feature();
            featureBean = feature.getFeature( featureBean );
        }
        return featureBean;
    }

    public ArrayList<EventBean> getVendorEvents(EventRequestBean eventRequestBean){
        ArrayList<EventBean> arrEventBean = new ArrayList<EventBean>();
        if(eventRequestBean !=null  && !Utility.isNullOrEmpty(eventRequestBean.getEventVendorId())) {
            AccessEventData accessEventData = new AccessEventData();
            arrEventBean = accessEventData.getVendorEvents( eventRequestBean );

        }
        return arrEventBean;
    }
    public ArrayList<EventBean> getClientEvents(EventRequestBean eventRequestBean){
        ArrayList<EventBean> arrEventBean = new ArrayList<EventBean>();
        if(eventRequestBean !=null  && !Utility.isNullOrEmpty(eventRequestBean.getEventClient())) {
            AccessEventData accessEventData = new AccessEventData();
            arrEventBean = accessEventData.getClientEvents( eventRequestBean );

        }
        return arrEventBean;
    }

    public JSONObject getJsonArrEventBean( ArrayList<EventBean> arrEventBean ) {
        JSONObject jsonArrEventBean = new JSONObject();
        Long iNumOfEvents = 0L;
        if(arrEventBean!=null && !arrEventBean.isEmpty() ) {
            for(EventBean eventBean : arrEventBean ) {
                jsonArrEventBean.put(ParseUtil.LToS(iNumOfEvents), eventBean.toJson() );
                iNumOfEvents++;
            }
        }
        jsonArrEventBean.put("num_of_events",iNumOfEvents);
        return jsonArrEventBean;
    }


    public JSONObject getJsonHmEventBean( HashMap<String,EventBean> hmEventBean ){
        JSONObject jsonEventBean = new JSONObject();
        if( hmEventBean!=null && !hmEventBean.isEmpty()) {
            for(Map.Entry<String, EventBean> mapEventBean : hmEventBean.entrySet()) {
                EventBean eventBean = mapEventBean.getValue();
                jsonEventBean.put( mapEventBean.getKey(), eventBean.toJson() );
            }
        }
        return jsonEventBean;
    }

}
