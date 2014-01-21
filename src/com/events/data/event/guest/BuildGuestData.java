package com.events.data.event.guest;

import com.events.bean.event.guest.GuestRequestBean;
import com.events.common.Configuration;
import com.events.common.Constants;
import com.events.common.DateSupport;
import com.events.common.Utility;
import com.events.common.db.DBDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 1/2/14
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class BuildGuestData {
    Configuration applicationConfig = Configuration.getInstance(Constants.APPLICATION_PROP);
    private String EVENTADMIN_DB = applicationConfig.get(Constants.EVENTADMIN_DB);
    private static final Logger appLogging = LoggerFactory.getLogger(Constants.APPLICATION_LOG);

    public Integer insertGuestGroup(GuestRequestBean guestRequestBean){
        Integer numOfRowsInserted = 0;
        if( guestRequestBean!=null && !Utility.isNullOrEmpty(guestRequestBean.getGuestGroupId())  && !Utility.isNullOrEmpty(guestRequestBean.getGuestGroupName()) ) {
            //  GUESTGROUPID  VARCHAR(45) NOT NULL, GROUPNAME  TEXT NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 ,
            //  HUMANCREATEDATE VARCHAR(45), DEL_ROW INT(1) NOT NULL DEFAULT 0
            String sQuery = "INSERT into GTGUESTGROUP(GUESTGROUPID,GROUPNAME,CREATEDATE,      HUMANCREATEDATE ) VALUES " +
                    " (?,?,?,    ?)";
            ArrayList<Object> aParams = DBDAO.createConstraint(guestRequestBean.getGuestGroupId(),guestRequestBean.getGuestGroupName(), DateSupport.getEpochMillis(),
                    DateSupport.getUTCDateTime());

            numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, EVENTADMIN_DB, "BuildGuestData.java", "insertGuestGroup() ");
        }
        return numOfRowsInserted;
    }

    public Integer insertGuest(GuestRequestBean guestRequestBean) {
        Integer numOfRowsInserted = 0;
        if( guestRequestBean!=null && !Utility.isNullOrEmpty(guestRequestBean.getGuestId())  && !Utility.isNullOrEmpty(guestRequestBean.getGuestGroupId()) ) {
            String sQuery = "INSERT into GTGUEST(GUESTID,FK_GUESTGROUPID,FIRST_NAME,      MIDDLE_NAME,LAST_NAME,COMPANY ) VALUES " +
                    " (?,?,?,    ?,?,?)";
            ArrayList<Object> aParams = DBDAO.createConstraint(guestRequestBean.getGuestId(),guestRequestBean.getGuestGroupId(), guestRequestBean.getFirstName(),
                    Constants.EMPTY,guestRequestBean.getLastName(),guestRequestBean.getCompany());

            numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, EVENTADMIN_DB, "BuildGuestData.java", "insertGuest() ");
        }
        return numOfRowsInserted;
    }


    public Integer insertGuestPhone(GuestRequestBean guestRequestBean, String sPhoneNum) {
        Integer numOfRowsInserted = 0;
        if( guestRequestBean!=null && !Utility.isNullOrEmpty(sPhoneNum)  && !Utility.isNullOrEmpty(guestRequestBean.getGuestGroupId()) ) {
            // GTGUESTGROUPPHONE ( GUESTGROUPPHONEID  VARCHAR(45) NOT NULL, FK_GUESTGROUPID VARCHAR(45) NOT NULL,
            //  FK_GUESTID   VARCHAR(45), PHONE_NUM VARCHAR(50) NOT NULL, PRIMARY_CONTACT
            String sQuery = "INSERT into GTGUESTGROUPPHONE(GUESTGROUPPHONEID,FK_GUESTGROUPID,FK_GUESTID,      PHONE_NUM,PRIMARY_CONTACT) VALUES " +
                    " (?,?,?,    ?,?)";
            ArrayList<Object> aParams = DBDAO.createConstraint(guestRequestBean.getGuestGroupPhoneId(),guestRequestBean.getGuestGroupId(),guestRequestBean.getGuestId(),
                    sPhoneNum,guestRequestBean.isPrimaryContactInfo()?"1":"0");

            numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, EVENTADMIN_DB, "BuildGuestData.java", "insertGuestPhone() ");
        }
        return numOfRowsInserted;
    }

    public Integer insertGuestEmail(GuestRequestBean guestRequestBean) {
        Integer numOfRowsInserted = 0;
        if( guestRequestBean!=null && !Utility.isNullOrEmpty(guestRequestBean.getEmail())  && !Utility.isNullOrEmpty(guestRequestBean.getGuestGroupId()) ) {
            // GTGUESTGROUPEMAIL ( GUESTGROUPEMAILID  VARCHAR(45) NOT NULL, FK_GUESTGROUPID VARCHAR(45) NOT NULL,
            // FK_GUESTID   VARCHAR(45), EMAIL_ID VARCHAR(500) NOT NULL, PRIMARY_CONTACT
            String sQuery = "INSERT into GTGUESTGROUPEMAIL( GUESTGROUPEMAILID,FK_GUESTGROUPID,FK_GUESTID,      EMAIL_ID,PRIMARY_CONTACT ) VALUES " +
                    " (?,?,?,    ?,?)";
            ArrayList<Object> aParams = DBDAO.createConstraint(guestRequestBean.getGuestGroupEmailId(),guestRequestBean.getGuestGroupId(),guestRequestBean.getGuestId(),
                    guestRequestBean.getEmail(),guestRequestBean.isPrimaryContactInfo()?"1":"0");

            numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, EVENTADMIN_DB, "BuildGuestData.java", "insertGuest() ");
        }
        return numOfRowsInserted;
    }

    public Integer insertGuestAddress(GuestRequestBean guestRequestBean) {
        Integer numOfRowsInserted = 0;
        appLogging.info("Going to insert address");
        if( guestRequestBean!=null && !Utility.isNullOrEmpty(guestRequestBean.getGuestGroupId()) ) {
            String sQuery = "INSERT into GTGUESTGROUPADDRESS( GUESTGROUPADDRESSID,FK_GUESTGROUPID,FK_GUESTID,      ADDRESS_1,ADDRESS_2,CITY,  " +
                    " STATE,COUNTRY,ZIPCODE,    PRIMARY_CONTACT ) VALUES " +
                    " (?,?,?,    ?,?,?,   ?,?,?,   ?)";
            ArrayList<Object> aParams = DBDAO.createConstraint(guestRequestBean.getGuestGroupAddressId(),guestRequestBean.getGuestGroupId(),guestRequestBean.getGuestId(),
                    guestRequestBean.getAddress1(),guestRequestBean.getAddress2(),guestRequestBean.getCity(),
                    guestRequestBean.getState(),guestRequestBean.getCountry(),guestRequestBean.getZipCode(),
                    guestRequestBean.isPrimaryContactInfo()?"1":"0");

            numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, EVENTADMIN_DB, "BuildGuestData.java", "insertGuestAddress() ");

            appLogging.info("Going to insert address numOfRowsInserted : " + numOfRowsInserted );
        }
        return numOfRowsInserted;

    }
    public Integer insertEventGuestGroup(GuestRequestBean guestRequestBean){
        Integer numOfRowsInserted = 0;
        if( guestRequestBean!=null && !Utility.isNullOrEmpty(guestRequestBean.getEventGuestGroupId()) && !Utility.isNullOrEmpty(guestRequestBean.getEventId())
                && !Utility.isNullOrEmpty(guestRequestBean.getGuestGroupId()) ) {
            // GTEVENTGUESTGROUP ( EVENTGUESTGROUPID VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, FK_GUESTGROUPID
            // VARCHAR(45) NOT NULL,TOTAL_INVITED_SEATS INT(11), RSVP_SEATS INT(11), WILL_ATTEND INT(1) NOT NULL DEFAULT 0,
            // HAS_RESPONDED INT(1) NOT NULL DEFAULT 0, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 , HUMANCREATEDATE VARCHAR(45)
            String sQuery = "INSERT into GTEVENTGUESTGROUP(EVENTGUESTGROUPID,FK_GUESTGROUPID,FK_EVENTID,     TOTAL_INVITED_SEATS,RSVP_SEATS,WILL_NOT_ATTEND," +
                    "HAS_RESPONDED,CREATEDATE,HUMANCREATEDATE ) VALUES " +
                    " (?,?,?,    ?,?,?,    ?,?,?)";
            ArrayList<Object> aParams = DBDAO.createConstraint(guestRequestBean.getEventGuestGroupId(),guestRequestBean.getGuestGroupId(),guestRequestBean.getEventId(),
                    guestRequestBean.getInvitedSeats(),guestRequestBean.getRsvpSeats(),guestRequestBean.isNotAttending()?"1":"0",
                    guestRequestBean.isHasResponded()?"1":"0",DateSupport.getEpochMillis(), DateSupport.getUTCDateTime());

            numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, EVENTADMIN_DB, "BuildGuestData.java", "insertEventGuestGroup() ");
        }
        return numOfRowsInserted;
    }

    public Integer updateEventGuestGroup(GuestRequestBean guestRequestBean){
        Integer numOfRowsInserted = 0;
        if( guestRequestBean!=null && !Utility.isNullOrEmpty(guestRequestBean.getEventGuestGroupId()) && !Utility.isNullOrEmpty(guestRequestBean.getEventId())
                && !Utility.isNullOrEmpty(guestRequestBean.getGuestGroupId()) ) {

            String sQuery = "UPDATE GTEVENTGUESTGROUP SET TOTAL_INVITED_SEATS =?,RSVP_SEATS =?,WILL_NOT_ATTEND=?,    " +
                    " HAS_RESPONDED=?,CREATEDATE=?,HUMANCREATEDATE=?  WHERE " +
                    " EVENTGUESTGROUPID=? AND FK_GUESTGROUPID=? AND FK_EVENTID=?";

            ArrayList<Object> aParams = DBDAO.createConstraint(  guestRequestBean.getInvitedSeats(),guestRequestBean.getRsvpSeats(),guestRequestBean.isNotAttending()?"1":"0",
                    guestRequestBean.isHasResponded()?"1":"0",DateSupport.getEpochMillis(), DateSupport.getUTCDateTime(),
                    guestRequestBean.getEventGuestGroupId(),guestRequestBean.getGuestGroupId(),guestRequestBean.getEventId());

            numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, EVENTADMIN_DB, "BuildGuestData.java", "insertEventGuestGroup() ");

        }
        return numOfRowsInserted;
    }

    public Integer updateGuestGroup(GuestRequestBean guestRequestBean){
        Integer numOfRowsInserted = 0;
        if( guestRequestBean!=null && !Utility.isNullOrEmpty(guestRequestBean.getGuestGroupId())  && !Utility.isNullOrEmpty(guestRequestBean.getGuestGroupName()) ) {
            String sQuery = "UPDATE GTGUESTGROUP SET GROUPNAME = ? WHERE GUESTGROUPID = ? ";
            ArrayList<Object> aParams = DBDAO.createConstraint(guestRequestBean.getGuestGroupName(),guestRequestBean.getGuestGroupId());

            numOfRowsInserted = DBDAO.putRowsQuery(sQuery, aParams, EVENTADMIN_DB, "BuildGuestData.java", "updateGuestGroup() ");
        }
        return numOfRowsInserted;
    }


    public Integer deleteGuestPhone(GuestRequestBean guestRequestBean){
        Integer numOfRowsDeleted = 0;
        if(guestRequestBean!=null && !Utility.isNullOrEmpty(guestRequestBean.getGuestGroupId())){
            String sQuery = "DELETE FROM GTGUESTGROUPPHONE WHERE FK_GUESTGROUPID = ?";
            ArrayList<Object> aParams = DBDAO.createConstraint(guestRequestBean.getGuestGroupId());
            numOfRowsDeleted = DBDAO.putRowsQuery(sQuery, aParams, EVENTADMIN_DB, "BuildGuestData.java", "deleteGuestPhone() ");
        }
        return numOfRowsDeleted;
    }
    public Integer deleteGuestEmails(GuestRequestBean guestRequestBean){
        Integer numOfRowsDeleted = 0;
        if(guestRequestBean!=null && !Utility.isNullOrEmpty(guestRequestBean.getGuestGroupId())){
            String sQuery = "DELETE FROM GTGUESTGROUPEMAIL WHERE FK_GUESTGROUPID = ?";
            ArrayList<Object> aParams = DBDAO.createConstraint(guestRequestBean.getGuestGroupId());
            numOfRowsDeleted = DBDAO.putRowsQuery(sQuery, aParams, EVENTADMIN_DB, "BuildGuestData.java", "deleteGuestEmails() ");
        }
        return numOfRowsDeleted;
    }
    public Integer deleteGuestAddress(GuestRequestBean guestRequestBean){
        Integer numOfRowsDeleted = 0;
        if(guestRequestBean!=null && !Utility.isNullOrEmpty(guestRequestBean.getGuestGroupId())){
            String sQuery = "DELETE FROM GTGUESTGROUPADDRESS WHERE FK_GUESTGROUPID = ?";
            ArrayList<Object> aParams = DBDAO.createConstraint(guestRequestBean.getGuestGroupId());
            numOfRowsDeleted = DBDAO.putRowsQuery(sQuery, aParams, EVENTADMIN_DB, "BuildGuestData.java", "deleteGuestAddress() ");
        }
        return numOfRowsDeleted;
    }
    public Integer deleteGuest(GuestRequestBean guestRequestBean){
        Integer numOfRowsDeleted = 0;
        if(guestRequestBean!=null && !Utility.isNullOrEmpty(guestRequestBean.getGuestGroupId())){
            String sQuery = "DELETE FROM GTGUEST WHERE FK_GUESTGROUPID = ?";
            ArrayList<Object> aParams = DBDAO.createConstraint(guestRequestBean.getGuestGroupId());
            numOfRowsDeleted = DBDAO.putRowsQuery(sQuery, aParams, EVENTADMIN_DB, "BuildGuestData.java", "deleteGuest() ");
        }
        return numOfRowsDeleted;
    }
}
