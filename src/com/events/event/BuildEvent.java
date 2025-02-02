package com.events.event;

import com.events.bean.DateObject;
import com.events.bean.clients.ClientBean;
import com.events.bean.clients.ClientRequestBean;
import com.events.bean.clients.ClientResponseBean;
import com.events.bean.common.FeatureBean;
import com.events.bean.common.notify.NotifyBean;
import com.events.bean.event.EventRequestBean;
import com.events.bean.event.EventResponseBean;
import com.events.bean.users.ParentTypeBean;
import com.events.bean.users.UserBean;
import com.events.bean.users.UserInfoBean;
import com.events.bean.users.UserRequestBean;
import com.events.clients.AccessClients;
import com.events.clients.BuildClients;
import com.events.common.*;
import com.events.common.exception.ExceptionHandler;
import com.events.common.exception.clients.EditClientException;
import com.events.common.feature.Feature;
import com.events.common.feature.FeatureType;
import com.events.common.notify.Notification;
import com.events.data.event.BuildEventData;
import com.events.users.AccessUsers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 12/21/13
 * Time: 3:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuildEvent {

    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APPLICATION_LOG);

    public EventResponseBean saveEvent(EventRequestBean eventRequestBean) {

        EventResponseBean eventResponseBean = new EventResponseBean();
        DateObject eventDate = DateSupport.convertTime(eventRequestBean.getEventDay()+ " " + eventRequestBean.getEventTime(),
                DateSupport.getTimeZone( eventRequestBean.getEventTimeZone() ), "yyyy/MMMMM/dd"+ " " + "hh:mm a" ,
                DateSupport.getTimeZone(Constants.DEFAULT_TIMEZONE) , Constants.DATE_PATTERN_TZ  );

        eventRequestBean.setEventDate(eventDate.getMillis());
        eventRequestBean.setEventHumanDate(eventDate.getFormattedTime());

        if(eventRequestBean!=null && "".equalsIgnoreCase(eventRequestBean.getEventId())) {
            eventRequestBean.setEventId(Utility.getNewGuid());
            eventResponseBean = createEvent(eventRequestBean);

            if(eventResponseBean!=null && !Utility.isNullOrEmpty(eventResponseBean.getEventId())){
                UserRequestBean userRequestBean = new UserRequestBean();
                userRequestBean.setUserId( eventRequestBean.getUserId() );

                AccessUsers accessUsers = new AccessUsers();
                UserBean userBean = accessUsers.getUserById(userRequestBean );
                if( userBean!=null ) {
                    ParentTypeBean parentTypeBean = accessUsers.getParentTypeBeanFromUser( userBean );

                    if(parentTypeBean!=null){
                        if(parentTypeBean.isUserAVendor()) {

                            String sPlannerMessage = "Created a new event called '" + ParseUtil.checkNull( eventRequestBean.getEventName() ) + "'";
                            String sClientId = ParseUtil.checkNull( eventRequestBean.getEventClient() );
                            ClientResponseBean clientResponseBean = new ClientResponseBean();
                            if(!Utility.isNullOrEmpty( sClientId )) {
                                ClientRequestBean clientRequestBean = new ClientRequestBean();
                                clientRequestBean.setClientId( sClientId );
                                clientRequestBean.setVendorId( userBean.getParentId() );

                                AccessClients accessClients = new AccessClients();
                                ClientBean clientBean = accessClients.getClient( clientRequestBean );
                                clientResponseBean = accessClients.getClientContactInfo(clientRequestBean);

                                if(clientBean!=null && !Utility.isNullOrEmpty(clientBean.getClientName())){
                                    sPlannerMessage = sPlannerMessage + " for client '" + ParseUtil.checkNull( clientBean.getClientName() ) + "'.";
                                }

                            }
                            NotifyBean notifyBean = new NotifyBean();
                            notifyBean.setFrom(eventRequestBean.getUserId());
                            notifyBean.setTo(Constants.NOTIFICATION_RECEPIENTS.ALL_PLANNERS.toString());
                            notifyBean.setMessage(sPlannerMessage );

                            Notification.createNewNotifyRecord( notifyBean );


                            if(clientResponseBean!=null && !Utility.isNullOrEmpty(clientResponseBean.getUserId())){
                                String sClientMessage = "Created a new event '" + ParseUtil.checkNull( eventRequestBean.getEventName() ) + "' for you.";

                                NotifyBean clientNotifyBean = new NotifyBean();
                                clientNotifyBean.setFrom(eventRequestBean.getUserId());
                                clientNotifyBean.setTo(clientResponseBean.getUserId());
                                clientNotifyBean.setMessage(sClientMessage );

                                Notification.createNewNotifyRecord( clientNotifyBean );
                            }


                        } else if(parentTypeBean.isUserAClient()){

                            String sPlannerMessage = "Created an event called '" + ParseUtil.checkNull( eventRequestBean.getEventName() ) + "'";
                            NotifyBean notifyBean = new NotifyBean();
                            notifyBean.setFrom(eventRequestBean.getUserId());
                            notifyBean.setTo(Constants.NOTIFICATION_RECEPIENTS.ALL_PLANNERS.toString());
                            notifyBean.setMessage(sPlannerMessage );

                            Notification.createNewNotifyRecord( notifyBean );
                        }
                    }

                }


                Feature feature = new Feature();
                FeatureBean featureBeanEventLocation = new FeatureBean();
                featureBeanEventLocation.setEventId( eventResponseBean.getEventId() );
                featureBeanEventLocation.setFeatureType(FeatureType.image_location);
                featureBeanEventLocation.setValue( Utility.getNewGuid());
                feature.setFeatureValue( featureBeanEventLocation );
            }

        } else {
            appLogging.info("Update Event invoked");
            eventResponseBean = updateEvent(eventRequestBean);
        }
        if(eventResponseBean!=null && !"".equalsIgnoreCase(eventResponseBean.getEventId())) {
            FeatureBean featureBeanEventDay = new FeatureBean();
            featureBeanEventDay.setEventId( eventResponseBean.getEventId() );
            featureBeanEventDay.setFeatureType( FeatureType.event_day );
            featureBeanEventDay.setValue( eventRequestBean.getEventDay());

            FeatureBean featureBeanEventTime = new FeatureBean();
            featureBeanEventTime.setEventId( eventResponseBean.getEventId() );
            featureBeanEventTime.setFeatureType(FeatureType.event_time);
            featureBeanEventTime.setValue( eventRequestBean.getEventTime());

            FeatureBean featureBeanEventTimeZone = new FeatureBean();
            featureBeanEventTimeZone.setEventId( eventResponseBean.getEventId() );
            featureBeanEventTimeZone.setFeatureType(FeatureType.event_timezone);
            featureBeanEventTimeZone.setValue( eventRequestBean.getEventTimeZone());



            Feature feature = new Feature();
            feature.setFeatureValue( featureBeanEventDay );
            feature.setFeatureValue( featureBeanEventTime );
            feature.setFeatureValue( featureBeanEventTimeZone );
        }
        return eventResponseBean;
    }

    public EventResponseBean createEvent(EventRequestBean eventRequestBean){
        EventResponseBean eventResponseBean =  new EventResponseBean();
        if(eventRequestBean!=null){
            String sClientID = ParseUtil.checkNull(eventRequestBean.getEventClient());
            if( "create_client".equalsIgnoreCase(eventRequestBean.getEventClient()) ) {
                ClientResponseBean clientResponseBean = createClient(eventRequestBean);
                if(clientResponseBean!=null){
                    sClientID = clientResponseBean.getClientId();
                }
                appLogging.info("Client Bean has been created : " + sClientID );
            }
            eventRequestBean.setEventClient(sClientID);

            BuildEventData buildEventData = new BuildEventData();
            Integer iNumOfRows = buildEventData.insertEvent(eventRequestBean);
            if(iNumOfRows>0) {
                eventResponseBean.setEventId( eventRequestBean.getEventId() );
            }
        }
        return eventResponseBean;
    }

    public EventResponseBean updateEvent(EventRequestBean eventRequestBean){
        EventResponseBean eventResponseBean =  new EventResponseBean();
        if(eventRequestBean!=null){
            BuildEventData buildEventData = new BuildEventData();
            Integer iNumOfRows = buildEventData.updateEvent(eventRequestBean);
            if(iNumOfRows>0) {
                eventResponseBean.setEventId( eventRequestBean.getEventId() );
            }
        }
        return eventResponseBean;
    }

    public EventResponseBean toggleEventDelete(EventRequestBean eventRequestBean){
        EventResponseBean eventResponseBean =  new EventResponseBean();
        if(eventRequestBean!=null){
            BuildEventData buildEventData = new BuildEventData();
            Integer numOfRowsUpdated = buildEventData.toggleEventDelete(eventRequestBean);
            if(numOfRowsUpdated>0){
                eventResponseBean.setDeleteEvent(true);
                appLogging.info("Event Was deleted successfully : " + eventRequestBean );
            } else {
                appLogging.error("Event could not be deleted : " + eventRequestBean );
            }
        }
        return eventResponseBean;
    }

    public ClientResponseBean createClient(EventRequestBean eventRequestBean) {
        ClientResponseBean clientResponseBean = new ClientResponseBean();
        if(eventRequestBean!=null && "create_client".equalsIgnoreCase(eventRequestBean.getEventClient())) {
            ClientRequestBean clientRequestBean = new ClientRequestBean();
            clientRequestBean.setClientName(eventRequestBean.getEventClientName());
            clientRequestBean.setVendorId(eventRequestBean.getEventVendorId());

            UserRequestBean userRequestBean = new UserRequestBean();
            userRequestBean.setEmail( eventRequestBean.getEventClientEmail() );
            clientRequestBean.setUserRequestBean(userRequestBean);

            BuildClients buildClients = new BuildClients();
            try {
                clientResponseBean = buildClients.createClient(clientRequestBean);

                appLogging.info("Client Response after create was called : " + clientResponseBean.getClientId() );
            } catch (EditClientException e) {
                appLogging.error("Client could not be created : " + ExceptionHandler.getStackTrace(e));
            }
        }  else{
            appLogging.info("CInvalid request bean. " );
        }
        return clientResponseBean;
    }
}
