package com.events.common.email;

import com.events.bean.common.email.*;
import com.events.bean.event.EveryEventResponseBean;
import com.events.common.Constants;
import com.events.common.ParseUtil;
import com.events.common.Utility;
import com.events.common.email.setting.AccessEventEmail;
import com.events.common.email.setting.EventEmailFeature;
import com.events.data.email.EventEmailData;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 1/13/14
 * Time: 10:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class EveryEventEmail {
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APPLICATION_LOG);
    public EveryEventEmailResponseBean getEveryEventEmail(EveryEventEmailRequestBean everyEventEmailRequestBean) {
        EveryEventEmailResponseBean everyEventEmailResponseBean = new EveryEventEmailResponseBean();
        if(everyEventEmailRequestBean!=null && !Utility.isNullOrEmpty(everyEventEmailRequestBean.getEventId())) {
            EventEmailData eventEmailData = new EventEmailData();
            ArrayList<EventEmailBean> arrEventEmailBean = eventEmailData.getAllEventEmails(everyEventEmailRequestBean);
            if(arrEventEmailBean!=null && !arrEventEmailBean.isEmpty()) {
                HashMap<String,ArrayList<EventEmailFeatureBean> > hmEveryEventEmailFeatureBean = getFeatures( arrEventEmailBean );

                everyEventEmailResponseBean.setArrEventEmailBean(arrEventEmailBean);
                everyEventEmailResponseBean.setNumOfEventEmails(arrEventEmailBean.size());
                everyEventEmailResponseBean.setHmEveryEventEmailFeatureBean(hmEveryEventEmailFeatureBean);
            }
        }
        return everyEventEmailResponseBean;
    }

    public HashMap<String,ArrayList<EventEmailFeatureBean> >  getFeatures(ArrayList<EventEmailBean> arrEventEmailBean) {
        HashMap<String,ArrayList<EventEmailFeatureBean> > hmEveryEventEmailFeatureBean = new HashMap<String, ArrayList<EventEmailFeatureBean>>();
        if(arrEventEmailBean!=null && !arrEventEmailBean.isEmpty()) {

            AccessEventEmail accessEventEmail = new AccessEventEmail();

            ArrayList<EventEmailFeatureBean> arrEventEmailFeatureBean = new ArrayList<EventEmailFeatureBean>();
            arrEventEmailFeatureBean.add( accessEventEmail.getEventEmailFeatureTypeBean(Constants.EventEmailFeatureType.send_email_rule) );
            arrEventEmailFeatureBean.add( accessEventEmail.getEventEmailFeatureTypeBean(Constants.EventEmailFeatureType.email_send_day) );
            arrEventEmailFeatureBean.add( accessEventEmail.getEventEmailFeatureTypeBean(Constants.EventEmailFeatureType.email_send_time) );
            arrEventEmailFeatureBean.add( accessEventEmail.getEventEmailFeatureTypeBean(Constants.EventEmailFeatureType.email_send_timezone) );
            arrEventEmailFeatureBean.add( accessEventEmail.getEventEmailFeatureTypeBean(Constants.EventEmailFeatureType.email_send_status) );
            arrEventEmailFeatureBean.add( accessEventEmail.getEventEmailFeatureTypeBean(Constants.EventEmailFeatureType.action) );

            for(EventEmailBean eventEmailBean : arrEventEmailBean) {
                String sEventEmailId = ParseUtil.checkNull(eventEmailBean.getEventEmailId());
                EventEmailFeature eventEmailFeature = new EventEmailFeature();
                ArrayList<EventEmailFeatureBean> arrMultipleFeatureBean = eventEmailFeature.getMultipleFeatures(arrEventEmailFeatureBean, sEventEmailId );

                if(arrMultipleFeatureBean!=null && !arrMultipleFeatureBean.isEmpty()) {
                    hmEveryEventEmailFeatureBean.put(sEventEmailId,arrMultipleFeatureBean );
                }
            }
        }
        return hmEveryEventEmailFeatureBean;
    }

    public JSONObject getEveryEventEmailJson(EveryEventEmailResponseBean everyEventEmailResponseBean) {
        JSONObject jsonEveryEventEmailObject = new JSONObject();
        if(everyEventEmailResponseBean!=null && everyEventEmailResponseBean.getNumOfEventEmails() >0 ){

            ArrayList<EventEmailBean> arrEventEmailBean = everyEventEmailResponseBean.getArrEventEmailBean();
            if(arrEventEmailBean!=null && !arrEventEmailBean.isEmpty()) {
                HashMap<String,ArrayList<EventEmailFeatureBean> > hmEveryEventEmailFeatureBean = everyEventEmailResponseBean.getHmEveryEventEmailFeatureBean();
                Integer iTrackNumOfEventEmails = 0;
                for(EventEmailBean eventEmailBean : arrEventEmailBean) {
                    EveryEventEmailBean everyEventEmailBean = new EveryEventEmailBean();
                    everyEventEmailBean.setEventEmailId( eventEmailBean.getEventEmailId() );
                    everyEventEmailBean.setEventId( eventEmailBean.getEventId() );
                    everyEventEmailBean.setName( eventEmailBean.getSubject() );
                    if(hmEveryEventEmailFeatureBean!=null && !hmEveryEventEmailFeatureBean.isEmpty()) {
                        ArrayList<EventEmailFeatureBean> arrEventEmailFeatures = hmEveryEventEmailFeatureBean.get(eventEmailBean.getEventEmailId());

                        if( arrEventEmailFeatures!=null && !arrEventEmailFeatures.isEmpty() ) {
                            String sSendDay =  Constants.EMPTY;
                            String sSendTime =  Constants.EMPTY;
                            String sSendTimeZone =  Constants.EMPTY;
                            String sSendRule =  Constants.EMPTY;
                            String sStatus =  Constants.EMPTY;
                            for( EventEmailFeatureBean eventEmailFeatureBean : arrEventEmailFeatures )  {

                                String sValue = ParseUtil.checkNull(eventEmailFeatureBean.getValue());
                                String sFeatureName = ParseUtil.checkNull(eventEmailFeatureBean.getFeatureName());
                                if(Constants.EventEmailFeatureType.email_send_day.toString().equalsIgnoreCase(sFeatureName)){
                                    sSendDay = sValue;
                                } else if(Constants.EventEmailFeatureType.email_send_time.toString().equalsIgnoreCase(sFeatureName)) {
                                    sSendTime = sValue;
                                } else if(Constants.EventEmailFeatureType.email_send_timezone.toString().equalsIgnoreCase(sFeatureName)) {
                                    sSendTimeZone = sValue;
                                } else if(Constants.EventEmailFeatureType.email_send_status.toString().equalsIgnoreCase(sFeatureName)) {
                                    sStatus = sValue;
                                } else if(Constants.EventEmailFeatureType.send_email_rule.toString().equalsIgnoreCase(sFeatureName)) {
                                    sSendRule = sValue;
                                }
                            }
                            everyEventEmailBean.setSendDate(sSendDay + " " + sSendTime + " " + sSendTimeZone);
                            everyEventEmailBean.setSendRule(  sSendRule);
                            everyEventEmailBean.setStatus( sStatus );
                        }
                    }
                    jsonEveryEventEmailObject.put(iTrackNumOfEventEmails.toString(),everyEventEmailBean.toJson());
                    iTrackNumOfEventEmails++;
                }
                jsonEveryEventEmailObject.put("num_of_eventemails" , iTrackNumOfEventEmails );
            }

        }
        return jsonEveryEventEmailObject;
    }
}
