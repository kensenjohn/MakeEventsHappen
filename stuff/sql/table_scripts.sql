#command to create the database
#create database eventadmin;
#create database eventhost;

#SELECT PASSWORD('FD3C0_______________');

#CREATE USER 'appadmin'@'localhost' IDENTIFIED BY PASSWORD '*XXXXXXXXXXXXXXXX';
#GRANT SELECT, INSERT, UPDATE, DELETE ON eventadmin.* TO 'appadmin'@'localhost';
#GRANT SELECT, INSERT, UPDATE, DELETE ON eventhost.* TO 'appadmin'@'localhost';


create table GTUSER ( USERID VARCHAR(45) NOT NULL, USERTYPE VARCHAR(45) NOT NULL, FK_PARENTID VARCHAR(45) NOT NULL COMMENT 'This will hold the foreign key from Client, Vendor, and Admin table Primary Key' ,FK_USERINFOID VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45),MODIFIEDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45), DEL_ROW INT(1) NOT NULL DEFAULT 0 , PRIMARY KEY (USERID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTUSERINFO ( USERINFOID VARCHAR(45) NOT NULL, FIRST_NAME VARCHAR(256) NOT NULL, LAST_NAME VARCHAR(256), ADDRESS_1 VARCHAR(1024) , ADDRESS_2 VARCHAR(1024), CITY VARCHAR(1024), STATE VARCHAR(30), COUNTRY VARCHAR(45), IP_ADDRESS VARCHAR(1024), CELL_PHONE VARCHAR(15), PHONE_NUM VARCHAR(15), EMAIL VARCHAR(100) , COMPANY VARCHAR(500) , ZIPCODE VARCHAR(45), WEBSITE VARCHAR(2000), DEL_ROW INT(1) NOT NULL DEFAULT 0,CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45),MODIFIEDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45), PRIMARY KEY (USERINFOID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTCLIENT( CLIENTID VARCHAR(45) NOT NULL, CLIENTNAME VARCHAR(400) NOT NULL, IS_CORPORATE_CLIENT INT(1) NOT NULL DEFAULT 0,  FK_VENDORID VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 , HUMANCREATEDATE VARCHAR(45),MODIFIEDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45), DEL_ROW INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (CLIENTID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTVENDOR( VENDORID VARCHAR(45) NOT NULL, VENDORNAME VARCHAR(400) NOT NULL, FOLDER VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 , HUMANCREATEDATE VARCHAR(45),MODIFIEDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45) , DEL_ROW INT(1) NOT NULL DEFAULT 0,  PRIMARY KEY (VENDORID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTADMIN ( ADMINID VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 , HUMANCREATEDATE VARCHAR(45),MODIFIEDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45) , DEL_ROW INT(1) NOT NULL DEFAULT 0,  PRIMARY KEY (ADMINID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTPASSWORD ( PASSWORDID VARCHAR(45) NOT NULL, PASSWORD VARCHAR(75) NOT NULL, FK_USERID VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45),MODIFIEDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45) , PASSWORD_STATUS VARCHAR(5) NOT NULL, PRIMARY KEY (PASSWORDID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTEVENT ( EVENTID VARCHAR(45) NOT NULL,EVENTNAME VARCHAR(400) NOT NULL,EVENTDATE BIGINT(20) NOT NULL DEFAULT 0,HUMANEVENTDATE VARCHAR(45) NOT NULL, FK_FOLDERID VARCHAR(45) NOT NULL,FK_CLIENTID VARCHAR(45) NOT NULL,FK_VENDORID VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 , HUMANCREATEDATE VARCHAR(45),MODIFIEDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45), DEL_ROW INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (EVENTID) ) ENGINE = MyISAM  DEFAULT CHARSET = utf8;
create table GTFEATURES ( FEATUREID  VARCHAR(45) NOT NULL, FEATURENAME  VARCHAR(75) NOT NULL,FK_EVENTID  VARCHAR(45) NOT NULL, VALUE  VARCHAR(500) NOT NULL, PRIMARY KEY (FEATUREID) ) ENGINE = MyISAM  DEFAULT CHARSET = utf8;
create TABLE GTUPLOADS (UPLOADID   VARCHAR(45) NOT NULL, FILENAME TEXT NOT NULL, PATH TEXT NOT NULL,CREATEDATE BIGINT(20) NOT NULL DEFAULT 0,HUMANCREATEDATE VARCHAR(45) NOT NULL, PRIMARY KEY (UPLOADID) ) ENGINE = MyISAM  DEFAULT CHARSET = utf8;

create TABLE GTGUESTGROUP( GUESTGROUPID  VARCHAR(45) NOT NULL, GROUPNAME  TEXT NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 , HUMANCREATEDATE VARCHAR(45), DEL_ROW INT(1) NOT NULL DEFAULT 0 , PRIMARY KEY (GUESTGROUPID) ) ENGINE = MyISAM  DEFAULT CHARSET = utf8;
create TABLE GTGUEST( GUESTID  VARCHAR(45) NOT NULL, FK_GUESTGROUPID VARCHAR(45) NOT NULL, FIRST_NAME  TEXT NOT NULL, MIDDLE_NAME  TEXT NOT NULL, LAST_NAME  TEXT NOT NULL, COMPANY VARCHAR(45), DEL_ROW INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (GUESTID) ) ENGINE = MyISAM  DEFAULT CHARSET = utf8;
create TABLE GTGUESTGROUPPHONE ( GUESTGROUPPHONEID  VARCHAR(45) NOT NULL, FK_GUESTGROUPID VARCHAR(45) NOT NULL, FK_GUESTID   VARCHAR(45), PHONE_NUM VARCHAR(50) NOT NULL, PRIMARY_CONTACT INT(1) NOT NULL DEFAULT 0  COMMENT 'All Communications are sent to this phone number' , PRIMARY KEY (GUESTGROUPPHONEID) ) ENGINE = MyISAM  DEFAULT CHARSET = utf8;
create TABLE GTGUESTGROUPEMAIL ( GUESTGROUPEMAILID  VARCHAR(45) NOT NULL, FK_GUESTGROUPID VARCHAR(45) NOT NULL, FK_GUESTID   VARCHAR(45), EMAIL_ID VARCHAR(500) NOT NULL, PRIMARY_CONTACT INT(1) NOT NULL DEFAULT 0  COMMENT 'All Communications are sent to this email' , PRIMARY KEY (GUESTGROUPEMAILID) ) ENGINE = MyISAM  DEFAULT CHARSET = utf8;
create TABLE GTGUESTGROUPADDRESS ( GUESTGROUPADDRESSID  VARCHAR(45) NOT NULL, FK_GUESTGROUPID VARCHAR(45) NOT NULL, FK_GUESTID   VARCHAR(45), ADDRESS_1 VARCHAR(1024) , ADDRESS_2 VARCHAR(1024), CITY VARCHAR(1024), STATE VARCHAR(150), COUNTRY VARCHAR(150), ZIPCODE VARCHAR(45), PRIMARY_CONTACT INT(1) NOT NULL DEFAULT 0  COMMENT 'All Communications are sent to this address' , PRIMARY KEY (GUESTGROUPADDRESSID) ) ENGINE = MyISAM  DEFAULT CHARSET = utf8;
create table GTEVENTGUESTGROUP ( EVENTGUESTGROUPID VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, FK_GUESTGROUPID VARCHAR(45) NOT NULL,TOTAL_INVITED_SEATS INT(11), RSVP_SEATS INT(11), WILL_NOT_ATTEND INT(1) NOT NULL DEFAULT 0, HAS_RESPONDED INT(1) NOT NULL DEFAULT 0, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 , HUMANCREATEDATE VARCHAR(45), DEL_ROW INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (EVENTGUESTGROUPID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

create TABLE GTGUESTCREATEJOB( GUESTCREATEJOBID VARCHAR(45) NOT NULL, FK_UPLOADID  VARCHAR(45) NOT NULL, FK_EVENTID  VARCHAR(45) NOT NULL, FK_USERID  VARCHAR(45) NOT NULL, JOBSTATUS   VARCHAR(100) NOT NULL,  CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 , HUMANCREATEDATE VARCHAR(45), PRIMARY KEY (GUESTCREATEJOBID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTEMAILTEMPLATE( EMAILTEMPLATEID VARCHAR(45) NOT NULL, EMAILTEMPLATENAME VARCHAR(100) NOT NULL, FROM_ADDRESS_NAME VARCHAR(256) NOT NULL, FROM_EMAIL_ADDRESS VARCHAR(256) NOT NULL, TO_ADDRESS_NAME VARCHAR(256) , TO_EMAIL_ADDRESS VARCHAR(256), EMAIL_SUBJECT TEXT, HTML_BODY TEXT NOT NULL, TEXT_BODY TEXT NOT NULL, PRIMARY KEY (EMAILTEMPLATEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTEMAILQUEUE ( EMAILQUEUEID VARCHAR(45) NOT NULL, FROM_ADDRESS VARCHAR(256) NOT NULL,FROM_ADDRESS_NAME VARCHAR(256), TO_ADDRESS VARCHAR(256) NOT NULL,TO_ADDRESS_NAME VARCHAR(256),CC_ADDRESS VARCHAR(256), CC_ADDRESSNAME VARCHAR(256), BCC_ADDRESS VARCHAR(256), BCC_ADDRESSNAME VARCHAR(256), EMAIL_SUBJECT TEXT, HTML_BODY TEXT NOT NULL, TEXT_BODY TEXT NOT NULL, STATUS VARCHAR(10) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, MODIFIEDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45), HUMANMODIFYDATE VARCHAR(45), PRIMARY KEY (EMAILQUEUEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

create table GTCOOKIEUSER( COOKIEUSERID VARCHAR(45) NOT NULL, FK_USERID VARCHAR(45) NOT NULL,  CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 , HUMANCREATEDATE VARCHAR(45), PRIMARY KEY (COOKIEUSERID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTFORGOTPASSWORD( FORGOTPASSWORDID VARCHAR(45) NOT NULL,FK_USERID VARCHAR(45), SECURE_TOKEN_ID TEXT, CREATEDATE bigint(20), HUMANCREATEDATE VARCHAR(45), IS_USABLE INT(1) NOT NULL DEFAULT 1, PRIMARY KEY (FORGOTPASSWORDID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

create table GTEVENTEMAIL( EVENTEMAILID VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, FK_USERID VARCHAR(45) NOT NULL, FROM_ADDRESS_NAME VARCHAR(256) NOT NULL, FROM_EMAIL_ADDRESS VARCHAR(256) NOT NULL, EMAIL_SUBJECT TEXT, HTML_BODY TEXT NOT NULL, TEXT_BODY TEXT NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0 , HUMANCREATEDATE VARCHAR(45), DEL_ROW INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (EVENTEMAILID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTEVENTEMAILFEATURES( EVENTEMAILFEATUREID  VARCHAR(45) NOT NULL, FEATURENAME  VARCHAR(75) NOT NULL, FK_EVENTEMAILID  VARCHAR(45) NOT NULL, VALUE  VARCHAR(500) NOT NULL, PRIMARY KEY (EVENTEMAILFEATUREID) ) ENGINE = MyISAM  DEFAULT CHARSET = utf8;

CREATE TABLE GTEMAILSCHEDULE ( EMAILSCHEDULEID VARCHAR(45) NOT NULL, FK_EVENTEMAILID VARCHAR(45) NOT NULL,  FK_EVENTID VARCHAR(45), FK_USERID VARCHAR(45), CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45), SCHEDULEDSENDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANSCHEDULEDSENDDATE VARCHAR(45), SCHEDULE_STATUS VARCHAR(45) NOT NULL,PRIMARY KEY (EMAILSCHEDULEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

CREATE TABLE GTGUESTWEBRESPONSE ( GUESTWEBRESPONSEID VARCHAR(45) NOT NULL,WEB_RESPONSE_TYPE  VARCHAR(45) NOT NULL, LINKID  VARCHAR(45) NOT NULL,  LINK_DOMAIN  TEXT, FK_GUESTGROUPID VARCHAR(45) NOT NULL ,  FK_EVENTID VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45), RESPONSE_STATUS VARCHAR(45) NOT NULL ,FK_USERID VARCHAR(45) NOT NULL, RESPONSEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANRESPONSEDATE VARCHAR(45),  PRIMARY KEY (GUESTWEBRESPONSEID,WEB_RESPONSE_TYPE,LINKID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;


create table GTVENDORWEBSITE( VENDORWEBSITEID VARCHAR(45) NOT NULL, FK_VENDORID VARCHAR(45) NOT NULL,FK_USERID VARCHAR(45) NOT NULL, CREATEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANCREATEDATE VARCHAR(45), PRIMARY KEY (VENDORWEBSITEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
create table GTVENDORWEBSITEFEATURES ( VENDORWEBSITEFEATUREID VARCHAR(45) NOT NULL, FK_VENDORWEBSITEID  VARCHAR(45) NOT NULL, FEATURENAME  VARCHAR(75) NOT NULL, VALUE  TEXT NOT NULL,MODIFIEDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45),FK_USERID VARCHAR(45) NOT NULL, PRIMARY KEY (VENDORWEBSITEFEATUREID) ) ENGINE = MyISAM  DEFAULT CHARSET = utf8;

CREATE TABLE GTUSERROLES(  USERROLEID VARCHAR(45) NOT NULL,  FK_ROLEID VARCHAR(45) NOT NULL,  FK_USERID VARCHAR(45) NOT NULL,PRIMARY KEY (USERROLEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTROLEPERMISSIONS(  ROLEPERMISSIONID VARCHAR(45)  NOT NULL,  FK_ROLEID VARCHAR(45) NOT NULL,  FK_PERMISSIONID VARCHAR(45) NOT NULL,PRIMARY KEY (ROLEPERMISSIONID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTROLES(  ROLEID VARCHAR(45) NOT NULL,  FK_PARENTID VARCHAR(45) NOT NULL,  NAME VARCHAR(45) NOT NULL,  CREATEDATE BIGINT DEFAULT 0 NOT NULL,  HUMANCREATEDATE VARCHAR(45),  IS_SITEADMIN INT DEFAULT 0 NOT NULL,PRIMARY KEY (ROLEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTPERMISSIONGROUP(  PERMISSIONGROUPID VARCHAR(45)  NOT NULL,  GROUP_NAME VARCHAR(100) NOT NULL,  FK_PARENTID VARCHAR(45) NOT NULL,PRIMARY KEY (PERMISSIONGROUPID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTPERMISSIONS(  PERMISSIONID VARCHAR(45) NOT NULL,  PERMISSIONGROUPID VARCHAR(45) NOT NULL,  SHORT_NAME VARCHAR(100) NOT NULL,  DISPLAY_TEXT VARCHAR(100) NOT NULL,  DESCRIPTION LONGTEXT,  FK_PARENTID VARCHAR(45) NOT NULL,PRIMARY KEY (PERMISSIONID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

CREATE TABLE GTPARTNERVENDORS(PARTNERID VARCHAR(45) NOT NULL, FK_VENDORID VARCHAR(45) NOT NULL, FK_VENDORID_PARTNER VARCHAR(45) NOT NULL,PRIMARY KEY (PARTNERID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTVENDORFEATURES ( VENDORFEATUREID VARCHAR(45) NOT NULL, FK_VENDORID  VARCHAR(45) NOT NULL, FEATURENAME  VARCHAR(75) NOT NULL, VALUE  TEXT NOT NULL,MODIFIEDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45),FK_USERID VARCHAR(45) NOT NULL, PRIMARY KEY (VENDORFEATUREID) ) ENGINE = MyISAM  DEFAULT CHARSET = utf8;


CREATE TABLE GTEVENTVENDORS(EVENTVENDORID VARCHAR(45) NOT NULL, FK_EVENTID VARCHAR(45) NOT NULL, FK_VENDORID VARCHAR(45) NOT NULL,PRIMARY KEY (EVENTVENDORID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTEVENTVENDORFEATURES (EVENTVENDORFEATUREID VARCHAR(45) NOT NULL, FK_EVENTVENDORID VARCHAR(45) NOT NULL, FEATURENAME  VARCHAR(75) NOT NULL, VALUE  TEXT NOT NULL,MODIFIEDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45),FK_USERID VARCHAR(45) NOT NULL, PRIMARY KEY (EVENTVENDORFEATUREID) ) ENGINE = MyISAM  DEFAULT CHARSET = utf8;

CREATE TABLE GTWEBSITETHEME( WEBSITETHEMEID VARCHAR(45) NOT NULL, FK_VENDORID VARCHAR(45) NOT NULL, NAME  VARCHAR(500) NOT NULL, SCREENSHOT_NAME  VARCHAR(500) NOT NULL, PRIMARY KEY (WEBSITETHEMEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTWEBSITEFONT(WEBSITEFONTID VARCHAR(45) NOT NULL, FK_WEBSITETHEMEID VARCHAR(45) NOT NULL, FONT_NAME VARCHAR(250) NOT NULL, FONT_CSS_NAME VARCHAR(250) NOT NULL, IS_DEFAULT INT(1) NOT NULL DEFAULT 0 ,  PRIMARY KEY (WEBSITEFONTID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTWEBSITECOLOR(WEBSITECOLORID VARCHAR(45) NOT NULL, FK_WEBSITETHEMEID VARCHAR(45) NOT NULL, COLOR_NAME VARCHAR(250) NOT NULL, COLOR_CSS_NAME VARCHAR(250) NOT NULL, COLOR_SWATCH_NAME VARCHAR(250) NOT NULL, IS_DEFAULT INT(1) NOT NULL DEFAULT 0 , PRIMARY KEY (WEBSITECOLORID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;


CREATE TABLE GTEVENTWEBSITE ( EVENTWEBSITEID VARCHAR(45) NOT NULL, FK_EVENTID  VARCHAR(45) NOT NULL,  FK_WEBSITETHEMEID VARCHAR(45) NOT NULL,FK_WEBSITEFONTID VARCHAR(45) NOT NULL,FK_WEBSITECOLORID VARCHAR(45) NOT NULL, FK_USERID  VARCHAR(45) NOT NULL,URL_UNIQUE_NAME VARCHAR(75) NOT NULL,  PRIMARY KEY (EVENTWEBSITEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

CREATE TABLE GTTHEMEPAGE(THEMEPAGEID  VARCHAR(45) NOT NULL ,   FK_WEBSITETHEMEID  VARCHAR(45) NOT NULL , TYPE  VARCHAR(45) NOT NULL, IS_SHOW INT(1) NOT NULL DEFAULT 0,  PRIMARY KEY (THEMEPAGEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID VARCHAR(45) NOT NULL, FK_THEMEPAGEID  VARCHAR(45) NOT NULL, FEATUREDESCRIPTION  VARCHAR(100) NOT NULL, FEATURENAME  VARCHAR(75) NOT NULL, VALUE  TEXT NOT NULL, PRIMARY KEY (THEMEPAGEFEATUREID) ) ENGINE = MyISAM  DEFAULT CHARSET = utf8;

CREATE TABLE GTEVENTWEBSITEPAGE(EVENTWEBSITEPAGEID  VARCHAR(45) NOT NULL ,   FK_EVENTWEBSITEID  VARCHAR(45) NOT NULL , FK_WEBSITETHEMEID  VARCHAR(45) NOT NULL , TYPE  VARCHAR(45) NOT NULL, IS_SHOW INT(1) NOT NULL DEFAULT 0,  PRIMARY KEY (EVENTWEBSITEPAGEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTEVENTWEBSITEPAGEFEATURES ( EVENTWEBSITEPAGEFEATUREID VARCHAR(45) NOT NULL, FK_EVENTWEBSITEPAGEID  VARCHAR(45) NOT NULL, FEATUREDESCRIPTION  VARCHAR(100) NOT NULL, FEATURENAME  VARCHAR(75) NOT NULL, VALUE  TEXT NOT NULL,MODIFIEDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45),FK_USERID VARCHAR(45) NOT NULL, PRIMARY KEY (EVENTWEBSITEPAGEFEATUREID) ) ENGINE = MyISAM  DEFAULT CHARSET = utf8;

CREATE TABLE GTEVENTPARTY(EVENTPARTYID VARCHAR(45) NOT NULL, FK_EVENTWEBSITEID  VARCHAR(45) NOT NULL , EVENTPARTYTYPE VARCHAR(100) NOT NULL, NAME VARCHAR(250) NOT NULL, DESCRIPTION TEXT, FK_UPLOADID VARCHAR(45), PRIMARY KEY (EVENTPARTYID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTSOCIALMEDIA(SOCIALMEDIAID VARCHAR(45) NOT NULL, FK_EVENTPARTYID VARCHAR(45) NOT NULL,SOCIALMEDIATYPE  VARCHAR(250) NOT NULL, URL  VARCHAR(1000) NOT NULL ,  PRIMARY KEY (SOCIALMEDIAID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

CREATE TABLE GTEVENTHOTELS(EVENTHOTELID VARCHAR(45) NOT NULL, FK_EVENTWEBSITEID  VARCHAR(45) NOT NULL , NAME TEXT NOT NULL, PHONE VARCHAR(45), ADDRESS TEXT, URL TEXT , INSTRUCTIONS TEXT , PRIMARY KEY (EVENTHOTELID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTEVENTREGISTRY(EVENTREGISTRYID VARCHAR(45) NOT NULL, FK_EVENTWEBSITEID  VARCHAR(45) NOT NULL , NAME TEXT NOT NULL, URL TEXT , INSTRUCTIONS TEXT , PRIMARY KEY (EVENTREGISTRYID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTEVENTCONTACTUS(EVENTCONTACTUSID VARCHAR(45) NOT NULL, FK_EVENTWEBSITEID  VARCHAR(45) NOT NULL , NAME TEXT NOT NULL, PHONE TEXT ,  EMAIL VARCHAR(250) , PRIMARY KEY (EVENTCONTACTUSID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

CREATE TABLE GTPARENTSITEENABLED(PARENTSITEENABLEDID VARCHAR(45) NOT NULL, FK_USERID VARCHAR(45) NOT NULL, IS_ALLOWED_ACCESS  INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (PARENTSITEENABLEDID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;

CREATE TABLE GTINVOICES(INVOICEID VARCHAR(45) NOT NULL, FK_USERID VARCHAR(45) NOT NULL, FK_VENDORID VARCHAR(45) NOT NULL, FK_CLIENTID VARCHAR(45) NOT NULL, INVOICE_NUMBER  VARCHAR(250) NOT NULL,  CONTRACT_PO_NUMBER  VARCHAR(250), INVOICEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANINVOICEDATE VARCHAR(45), DUEDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANDUEDATE VARCHAR(45), MODIFIEDDATE BIGINT(20) NOT NULL DEFAULT 0, HUMANMODIFIEDDATE VARCHAR(45), DISCOUNT_PERCENTAGE VARCHAR(45), TAX_PERCENTAGE VARCHAR(45),TERMS_CONDITIONS VARCHAR(100),  NOTE TEXT, STATUS VARCHAR(100) NOT NULL, DEL_ROW INT(1) NOT NULL DEFAULT 0, PRIMARY KEY (INVOICEID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;
CREATE TABLE GTINVOICEITEMS( INVOICEITEMID VARCHAR(45) NOT NULL , FK_INVOICEID  VARCHAR(45) NOT NULL, ITEM_NAME TEXT NOT NULL,ITEM_DESCRIPTION TEXT, UNIT_COST  VARCHAR(45) NOT NULL, QUANTITY VARCHAR(45) NOT NULL,  DISCOUNT_PERCENTAGE VARCHAR(45), TAX_PERCENTAGE VARCHAR(45),  PRIMARY KEY (INVOICEITEMID) ) ENGINE = MyISAM DEFAULT CHARSET = utf8;


  /* Role Permission Queries */
INSERT into GTPERMISSIONGROUP( PERMISSIONGROUPID,GROUP_NAME,FK_PARENTID ) VALUES ( 'b92b1850-1588-49b3-9401-def8f2f76be7', 'Client Management' ,'VENDOR' );
INSERT into GTPERMISSIONGROUP( PERMISSIONGROUPID,GROUP_NAME,FK_PARENTID ) VALUES ( '5c07e6dd-c2b1-4ace-a2b9-7ae284653f2f', 'Event Management' ,'VENDOR' );
INSERT into GTPERMISSIONGROUP( PERMISSIONGROUPID,GROUP_NAME,FK_PARENTID ) VALUES ( 'd980472b-cceb-470c-a506-bf1e9c885414', 'Roles and Permission Management' ,'VENDOR' );
INSERT into GTPERMISSIONGROUP( PERMISSIONGROUPID,GROUP_NAME,FK_PARENTID ) VALUES ( '9a480e38-c70a-407f-9702-ae23173af4c7', 'Dashboard Management' ,'VENDOR' );
INSERT into GTPERMISSIONGROUP( PERMISSIONGROUPID,GROUP_NAME,FK_PARENTID ) VALUES ( 'f4ca73f8-978c-489a-8a47-4d4d1c63e470', 'Invoices' ,'VENDOR' );
INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('1ad79779-eec3-42da-8ed8-e04551549119','b92b1850-1588-49b3-9401-def8f2f76be7','ACCESS_CLIENTS_TAB','Access Clients Tab','Will be able to see/access the Clients tab in the menu bar.' ,'VENDOR');
INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('ddb5b93c-6686-4f78-9ab6-f2898e547bfd','b92b1850-1588-49b3-9401-def8f2f76be7','CREATE_NEW_CLIENT','Create Client','User will be able to create a new client.' ,'VENDOR');
INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('f717b471-cec7-46c6-837c-c848082bdc1f','5c07e6dd-c2b1-4ace-a2b9-7ae284653f2f','ACCESS_EVENTS_TAB','Access Events Tab','Will be able to see/access the Events tab in the menu bar.' ,'VENDOR');
INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('ecb57888-35dd-4ab9-8246-da48a88e1dec','5c07e6dd-c2b1-4ace-a2b9-7ae284653f2f','CREATE_NEW_EVENT','Create Event','User will be able to create a new event.' ,'VENDOR');
INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('ec103a0e-d209-4e69-882d-b2b8e6745d82','d980472b-cceb-470c-a506-bf1e9c885414','EDIT_ROLE_PERMISSION','Edit Role Permissions','User will be able to create or edit permissions for a role.' ,'VENDOR');
INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('1da9462a-826f-461d-8464-3ecc4ef64870','d980472b-cceb-470c-a506-bf1e9c885414','DELETE_ROLE','Delete Roles','User will be able to delete role.' ,'VENDOR');
INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('ee3dd4d5-e627-45ed-be87-864a669ea31a','5c07e6dd-c2b1-4ace-a2b9-7ae284653f2f','DELETE_EVENT','Delete Events','User will be able to delete events.' ,'VENDOR');
INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('2a0bbcd1-3a4e-47de-8bb1-1e3f3a3d39e4','b92b1850-1588-49b3-9401-def8f2f76be7','DELETE_CLIENT','Delete Client','Delete clients.' ,'VENDOR');
INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('8a09886a-0d68-4197-a7c2-19ac4932ca82','b92b1850-1588-49b3-9401-def8f2f76be7','EDIT_CLIENT','Edit Client','Edit clients.' ,'VENDOR');

INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('54513d80-4859-4b5b-b499-b5e1aef793b4','9a480e38-c70a-407f-9702-ae23173af4c7','ACCESS_DASHBOARD_TAB','Access Dashboard Tab','Will be able to see/access the Dashboard tab in the menu bar.' ,'VENDOR');
INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('fd8e381b-8d2d-4c8f-aa63-91dbf8dab4b8','9a480e38-c70a-407f-9702-ae23173af4c7','MANAGE_VENDOR_WEBSITE','Manage Business Website','User will be able add/edit to the Website and Landing Page.' ,'VENDOR');
INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('9460e239-b648-484c-aa45-90b08d964815','9a480e38-c70a-407f-9702-ae23173af4c7','MANAGE_TEAM_MEMBERS','Manage Team Members','User will be able to add/edit Team Members' ,'VENDOR');
INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('5e729ba7-febe-4fb2-a4a6-bc2006eafab9','9a480e38-c70a-407f-9702-ae23173af4c7','MANAGE_ROLE_PERMISSION','Manage Roles And Permissions','User will be able see the Roles and Permissions tab.' ,'VENDOR');
INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('d62b519a-a397-4f27-80bb-ca9b12a39b81','9a480e38-c70a-407f-9702-ae23173af4c7','MANAGE_PARTNER_VENDORS','Manage Partner Vendors','User add/edit Partner Vendors' ,'VENDOR');

INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('61c71ac9-51d4-4daa-ad30-1cec74061935','f4ca73f8-978c-489a-8a47-4d4d1c63e470','EDIT_INVOICE','Create/Edit Invoice','User can create or edit Invoices' ,'VENDOR');
INSERT into GTPERMISSIONS(PERMISSIONID,PERMISSIONGROUPID, SHORT_NAME,DISPLAY_TEXT,      DESCRIPTION,FK_PARENTID ) VALUES ('5298fb7c-afb6-436f-aa9f-ff8905e416bc','f4ca73f8-978c-489a-8a47-4d4d1c63e470','VIEW_INVOICE','View Invoices','User can view Invoices' ,'VENDOR');

INSERT INTO GTROLES( ROLEID, FK_PARENTID, NAME,      CREATEDATE, HUMANCREATEDATE, IS_SITEADMIN) VALUE ('6111905d-0bd0-43a7-ae16-7aa02de05ddb','VENDOR','Lead Coordinator',    1391025009,'2014-01-29 13:50:42', 0);
INSERT INTO GTROLES( ROLEID, FK_PARENTID, NAME,      CREATEDATE, HUMANCREATEDATE, IS_SITEADMIN) VALUE ('fa138a2f-aee2-4ff3-aaa0-247cf7df9493','VENDOR','Intern',   1391025009,'2014-01-29 13:50:42', 0);
INSERT INTO GTROLES( ROLEID, FK_PARENTID, NAME,      CREATEDATE, HUMANCREATEDATE, IS_SITEADMIN) VALUE ('87021355-f8c8-405e-a032-c8bf81c1b371','VENDOR','Site Admin',   1391025009,'2014-01-29 13:50:42', 1);
INSERT INTO GTROLES( ROLEID, FK_PARENTID, NAME,      CREATEDATE, HUMANCREATEDATE, IS_SITEADMIN) VALUE ('7f325fba-7a59-429c-ba39-bdd0c2cc5c9d','VENDOR','Client',   1391025009,'2014-01-29 13:50:42', 0);

/* Website Theme Queries */
INSERT INTO GTWEBSITETHEME ( WEBSITETHEMEID , FK_VENDORID , NAME , SCREENSHOT_NAME) VALUES('01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'ALL_VENDORS', 'Elegant' , 'Elegant.png');
INSERT INTO GTWEBSITEFONT ( WEBSITEFONTID , FK_WEBSITETHEMEID , FONT_NAME , FONT_CSS_NAME, IS_DEFAULT) VALUES('e412b7ca-26a4-454d-a8ed-d14927c2744d','01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'Amatic', 'Amatic.css', 1);
INSERT INTO GTWEBSITEFONT ( WEBSITEFONTID , FK_WEBSITETHEMEID , FONT_NAME , FONT_CSS_NAME, IS_DEFAULT) VALUES('32f1787f-1c80-4b17-8b53-a1f69e6954cc','01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'Arizonia', 'Arizonia.css', 0);
INSERT INTO GTWEBSITECOLOR ( WEBSITECOLORID , FK_WEBSITETHEMEID , COLOR_NAME , COLOR_CSS_NAME,COLOR_SWATCH_NAME, IS_DEFAULT) VALUES('e775dcbb-e55e-4f19-86e6-6ad334a58bc9','01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'Joyous Yellow', 'joyous_yellow.css', 'joyous_yellow.png', 0);
INSERT INTO GTWEBSITECOLOR ( WEBSITECOLORID , FK_WEBSITETHEMEID , COLOR_NAME , COLOR_CSS_NAME,COLOR_SWATCH_NAME, IS_DEFAULT) VALUES('899f1736-6c2d-4fdd-bff5-5c6e4367caba','01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'Dark Blue', 'dark_blue.css', 'dark_blue.png', 0);
INSERT INTO GTWEBSITECOLOR ( WEBSITECOLORID , FK_WEBSITETHEMEID , COLOR_NAME , COLOR_CSS_NAME,COLOR_SWATCH_NAME, IS_DEFAULT) VALUES('63839c30-8a3d-4478-bf03-e8c88bde2473','01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'Green Wing', 'green_wing.css', 'green_wing.png', 0);
INSERT INTO GTWEBSITECOLOR ( WEBSITECOLORID , FK_WEBSITETHEMEID , COLOR_NAME , COLOR_CSS_NAME,COLOR_SWATCH_NAME, IS_DEFAULT) VALUES('29f8ee4d-5f15-4edd-81c8-13027724f41a','01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'Warm Red', 'warm_red.css', 'warm_red.png', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  '3b4967b0-891d-4b88-8bf0-77373a0b25f8', '01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'welcome', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  '695bb6a0-2d8a-4a47-90ed-f244355413d3', '01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'invitation', 1);
INSERT INTO GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID,FK_THEMEPAGEID, FEATUREDESCRIPTION, FEATURENAME, VALUE) VALUES ('b3b25199-9482-4829-a615-3f084a652299','3b4967b0-891d-4b88-8bf0-77373a0b25f8', 'Caption Title', 'caption_title', 'Julie & Simon');
INSERT INTO GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID,FK_THEMEPAGEID, FEATUREDESCRIPTION, FEATURENAME, VALUE) VALUES ('f8e6c535-7143-438a-9512-b29949d7bac0','3b4967b0-891d-4b88-8bf0-77373a0b25f8', 'Caption Tag Line', 'caption_tag_line', 'Two hearts that beat as one.');
INSERT INTO GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID,FK_THEMEPAGEID, FEATUREDESCRIPTION, FEATURENAME, VALUE) VALUES ('c05ab8a5-fe58-4b1c-9520-071829db7f7b','695bb6a0-2d8a-4a47-90ed-f244355413d3', 'Invitation Name', 'invite_name', 'Julie Sorson & Simon DeGaule');

INSERT INTO GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID,FK_THEMEPAGEID, FEATUREDESCRIPTION, FEATURENAME, VALUE) VALUES ('1f565e08-a4e6-4d87-9769-0f8dfeef726a','695bb6a0-2d8a-4a47-90ed-f244355413d3', 'Invite Text', 'invite_text', 'invite you to their wedding on');
INSERT INTO GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID,FK_THEMEPAGEID, FEATUREDESCRIPTION, FEATURENAME, VALUE) VALUES ('97685e4c-6440-4a24-a328-e23d9a699fd7','695bb6a0-2d8a-4a47-90ed-f244355413d3', 'Date of Event', 'invite_date', 'May 26th 2015');
INSERT INTO GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID,FK_THEMEPAGEID, FEATUREDESCRIPTION, FEATURENAME, VALUE) VALUES ('4a6bbe2f-6811-4c4b-b2f6-c2b8b698a926','695bb6a0-2d8a-4a47-90ed-f244355413d3', 'Location Name', 'invite_location_name', 'Copeland Park' );
INSERT INTO GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID,FK_THEMEPAGEID, FEATUREDESCRIPTION, FEATURENAME, VALUE) VALUES ('ce5bf12f-7ebf-426c-8cc6-0309ebf46415','695bb6a0-2d8a-4a47-90ed-f244355413d3', 'Location Address', 'invite_address', '11001 N 15th St, Tampa, FL 33612');

INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  'c8dc5bf7-11d1-4ea3-99ec-78174f8ea28b', '01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'couples', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  'eeaf77ca-d5e5-4d1c-8802-5e06ae008cb5', '01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'bridesmaids', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  '767846a7-010b-49d3-b46f-8b7ce4699e0f', '01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'groomsmen', 1);

INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  'de569c29-65d5-4ae1-9427-f1f7b56ac3ae', '01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'ceremony', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  '6ba3f885-2b13-463d-adaf-78009931ddb4', '01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'reception', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  '657efd43-6231-4772-88e6-0d9aca27b880', '01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'travel', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  'a9232d98-37b3-4eb7-95f4-a32801c49e98', '01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'hotels', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  '66ad66f6-0322-48ba-ae6b-2cfc3377b871', '01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'registry', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  '7e737b14-4152-4d91-8628-5d558085df3d', '01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'rsvp', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  '46f3000c-ab5f-434e-8f76-4014790b99f5', '01934335-ff6a-4a4e-8220-01ee2ef6a7eb', 'contactus', 1);


INSERT INTO GTWEBSITETHEME ( WEBSITETHEMEID , FK_VENDORID , NAME , SCREENSHOT_NAME) VALUES('15eb45a2-6370-4d03-849f-b4dd381eb73d', 'ALL_VENDORS', 'Windsor' , 'Windsor.jpg');
INSERT INTO GTWEBSITEFONT ( WEBSITEFONTID , FK_WEBSITETHEMEID , FONT_NAME , FONT_CSS_NAME, IS_DEFAULT) VALUES('80562fa4-766e-4ebb-8209-123b5d4b3112','15eb45a2-6370-4d03-849f-b4dd381eb73d', 'Amatic', 'Amatic.css', 1);
INSERT INTO GTWEBSITEFONT ( WEBSITEFONTID , FK_WEBSITETHEMEID , FONT_NAME , FONT_CSS_NAME, IS_DEFAULT) VALUES('a810a6d6-aa5f-4538-ab86-d3d18a90447c','15eb45a2-6370-4d03-849f-b4dd381eb73d', 'Arizonia', 'Arizonia.css', 0);
INSERT INTO GTWEBSITECOLOR ( WEBSITECOLORID , FK_WEBSITETHEMEID , COLOR_NAME , COLOR_CSS_NAME,COLOR_SWATCH_NAME, IS_DEFAULT) VALUES('67744b9f-7ef9-4340-acf5-34caf936c5a3','15eb45a2-6370-4d03-849f-b4dd381eb73d', 'Joyous Yellow', 'joyous_yellow.css', 'joyous_yellow.png', 0);
INSERT INTO GTWEBSITECOLOR ( WEBSITECOLORID , FK_WEBSITETHEMEID , COLOR_NAME , COLOR_CSS_NAME,COLOR_SWATCH_NAME, IS_DEFAULT) VALUES('d8069956-1faa-4f76-ba6d-5b82dae5cc61','15eb45a2-6370-4d03-849f-b4dd381eb73d', 'Dark Blue', 'dark_blue.css', 'dark_blue.png', 0);
INSERT INTO GTWEBSITECOLOR ( WEBSITECOLORID , FK_WEBSITETHEMEID , COLOR_NAME , COLOR_CSS_NAME,COLOR_SWATCH_NAME, IS_DEFAULT) VALUES('e8b5b978-ff54-4767-91a5-3e1f0c13dcf6','15eb45a2-6370-4d03-849f-b4dd381eb73d', 'Green Wing', 'green_wing.css', 'green_wing.png', 0);
INSERT INTO GTWEBSITECOLOR ( WEBSITECOLORID , FK_WEBSITETHEMEID , COLOR_NAME , COLOR_CSS_NAME,COLOR_SWATCH_NAME, IS_DEFAULT) VALUES('77ae1fc4-0fdc-49d7-8253-a50786651ef5','15eb45a2-6370-4d03-849f-b4dd381eb73d', 'Warm Red', 'warm_red.css', 'warm_red.png', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  '3d437eb37-11d6-4cc5-83d3-9ab100cd42d6', '15eb45a2-6370-4d03-849f-b4dd381eb73d', 'welcome', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  '3289f5b8-68e3-441b-979c-16a3af69c9f3', '15eb45a2-6370-4d03-849f-b4dd381eb73d', 'invitation', 1);
INSERT INTO GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID,FK_THEMEPAGEID, FEATUREDESCRIPTION, FEATURENAME, VALUE) VALUES ('2f70443e-b1ef-4cc7-9859-9d02fb177d86','3d437eb37-11d6-4cc5-83d3-9ab100cd42d6', 'Caption Title', 'caption_title', 'Julie & Simon');
INSERT INTO GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID,FK_THEMEPAGEID, FEATUREDESCRIPTION, FEATURENAME, VALUE) VALUES ('c8d4d877-1fed-4efc-8de3-9327e996ab41','3d437eb37-11d6-4cc5-83d3-9ab100cd42d6', 'Caption Tag Line', 'caption_tag_line', 'Two hearts that beat as one.');
INSERT INTO GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID,FK_THEMEPAGEID, FEATUREDESCRIPTION, FEATURENAME, VALUE) VALUES ('329cb306-2a74-4c8e-8440-d5c2f08666f0','3289f5b8-68e3-441b-979c-16a3af69c9f3', 'Invitation Name', 'invite_name', 'Julie Sorson & Simon DeGaule');

INSERT INTO GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID,FK_THEMEPAGEID, FEATUREDESCRIPTION, FEATURENAME, VALUE) VALUES ('fe4f3e41-55a1-4c7f-8fff-3a5822f34623','3289f5b8-68e3-441b-979c-16a3af69c9f3', 'Invite Text', 'invite_text', 'invite you to their wedding on');
INSERT INTO GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID,FK_THEMEPAGEID, FEATUREDESCRIPTION, FEATURENAME, VALUE) VALUES ('d67a55f6-4611-46f2-a713-055aec0f96df','3289f5b8-68e3-441b-979c-16a3af69c9f3', 'Date of Event', 'invite_date', 'May 26th 2015');
INSERT INTO GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID,FK_THEMEPAGEID, FEATUREDESCRIPTION, FEATURENAME, VALUE) VALUES ('bba97396-bca4-4bf3-8694-b523c1a70743','3289f5b8-68e3-441b-979c-16a3af69c9f3', 'Location Name', 'invite_location_name', 'Copeland Park' );
INSERT INTO GTTHEMEPAGEFEATURES ( THEMEPAGEFEATUREID,FK_THEMEPAGEID, FEATUREDESCRIPTION, FEATURENAME, VALUE) VALUES ('6a4950c3-a572-4056-9034-42e1252e92ba','3289f5b8-68e3-441b-979c-16a3af69c9f3', 'Location Address', 'invite_address', '11001 N 15th St, Tampa, FL 33612');

INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  '9f60e26a-3de5-4a5b-baff-1caeb8d5ba60', '15eb45a2-6370-4d03-849f-b4dd381eb73d', 'couples', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  'd3a4d55b-4335-4457-a17c-335a448d8df0', '15eb45a2-6370-4d03-849f-b4dd381eb73d', 'bridesmaids', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  '4e091d9a-d47a-41a4-a285-cdc3f1918100', '15eb45a2-6370-4d03-849f-b4dd381eb73d', 'groomsmen', 1);

INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  'ba1e1ae7-f914-4694-8324-a3d5e55e4342', '15eb45a2-6370-4d03-849f-b4dd381eb73d', 'ceremony', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  'dbd85153-0c8e-451a-bdab-e05fa8cc4ccc', '15eb45a2-6370-4d03-849f-b4dd381eb73d', 'reception', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  'de14fa6f-8b80-49f9-8103-f1002170b67f', '15eb45a2-6370-4d03-849f-b4dd381eb73d', 'travel', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  'cc9d7dbc-9841-4b19-b9af-73c7694871a2', '15eb45a2-6370-4d03-849f-b4dd381eb73d', 'hotels', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  '2b862dae-2c6d-4b46-b2af-622433d3b1d4', '15eb45a2-6370-4d03-849f-b4dd381eb73d', 'registry', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  'c9119ac7-862a-420c-9566-06646003ad1b', '15eb45a2-6370-4d03-849f-b4dd381eb73d', 'rsvp', 1);
INSERT INTO GTTHEMEPAGE ( THEMEPAGEID,FK_WEBSITETHEMEID,TYPE, IS_SHOW) VALUES(  '917bd5ac-7b73-47a3-af53-6c1b2b3aa4b0', '15eb45a2-6370-4d03-849f-b4dd381eb73d', 'contactus', 1);

/** Email Template */
INSERT INTO GTEMAILTEMPLATE (EMAILTEMPLATEID, EMAILTEMPLATENAME, FROM_ADDRESS_NAME, FROM_EMAIL_ADDRESS, EMAIL_SUBJECT, HTML_BODY, TEXT_BODY) VALUES ('88f6ab36-8455-11e1-88a0-35d635b03cf1', 'NEWPASSWORD', 'webmaster@ismyplanner.com', 'webmaster@ismyplanner.com', 'Password Reset Request', '<link rel="stylesheet" type="text/css" href="{{APPLICATION_LINK}}/css/style.css"><div class="span3" style>&nbsp;<br></div></div><br>Hello {{GIVEN_NAME}},<br><br>We received a request to reset your password.<br>Please click the link below.<br> <br><a href="{{NEW_PASSWORD_RESET_LINK}}" target="_blank">Reset Password</a><br><br>Thank You,<br>{{BUSINESS_NAME}}', 'Hello {{GIVEN_NAME}},\n\nWe received a request to reset your password.\nPlease copy the link below to a browser.\n\{{NEW_PASSWORD_RESET_LINK}}\n\nThank You,\{{PRODUCT_NAME}} Customer Support');
INSERT INTO GTEMAILTEMPLATE (EMAILTEMPLATEID, EMAILTEMPLATENAME, FROM_ADDRESS_NAME, FROM_EMAIL_ADDRESS, EMAIL_SUBJECT, HTML_BODY, TEXT_BODY) VALUES ('79c56fec-7e65-4a3b-b648-a33fdda6fd2b','CLIENT_PARENTSITE_ACCESS','webmaster@ismyplanner.com','webmaster@ismyplanner.com', '{{VENDOR_NAME}}, Welcome!!', 'Hello {{GIVEN_NAME}},<br>Welcome to our planning portal.<br>Please click the link below to access your website.<br>{{{PORTAL_LINK}}}<br><br>Thank You,<br>{{VENDOR_NAME}}','Hello {{GIVEN_NAME}},\nWelcome to our planning portal.\nPlease click the link below to access your website.\n{{{PORTAL_LINK}}}\n\nThank You,\n{{VENDOR_NAME}}');
INSERT INTO GTEMAILTEMPLATE (EMAILTEMPLATEID, EMAILTEMPLATENAME, FROM_ADDRESS_NAME, FROM_EMAIL_ADDRESS, EMAIL_SUBJECT, HTML_BODY, TEXT_BODY) VALUES ('62de3a65-0261-4a92-94ff-550b772075ec','NEW_TEAM_MEMBER_ACCESS','webmaster@ismyplanner.com','webmaster@ismyplanner.com', '{{VENDOR_NAME}}, Welcome!!', 'Hello {{GIVEN_NAME}},<br>Welcome to our planning portal.<br>Please login using the link below.<br>{{{PORTAL_LINK}}}<br><br>Thank You,<br>{{VENDOR_USER_NAME}}','Hello {{GIVEN_NAME}},\nWelcome to our planning portal.\nPlease login using the link below.\n{{{PORTAL_LINK}}\n\nThank You,\n{{VENDOR_USER_NAME}}');
INSERT INTO GTEMAILTEMPLATE (EMAILTEMPLATEID, EMAILTEMPLATENAME, FROM_ADDRESS_NAME, FROM_EMAIL_ADDRESS, EMAIL_SUBJECT, HTML_BODY, TEXT_BODY) VALUES ('7ab83780-05ac-4b87-b377-c1b075aa6651','NEW_VENDOR_ACCESS','webmaster@ismyplanner.com','webmaster@ismyplanner.com', 'Welcome to {{APPLICATION_NAME}}!!', 'Welcome to your free trial of {{APPLICATION_NAME}}.<br>You may login using the link below.<br>{{{PORTAL_LINK}}}<br>Explore all the features which will help you improve your business and your relationship with your clients.<br>We are adding new features regularly based on your feedback and experience.<br>You may contact us with any questions or suggestions at {{{CONTACT_US_PAGE}}}<br><br>Thank You,<br>{{APPLICATION_NAME}}','Welcome to your free trial of {{APPLICATION_NAME}}.\nYou may login using the link below.\n{{{PORTAL_LINK}}}\nExplore all the features which will help you improve your business and your relationship with your clients.\nWe are adding new features regularly based on your feedback and experience.\nYou may contact us with any questions or suggestions at {{{CONTACT_US_PAGE}}}\n\nThank You,\n{{APPLICATION_NAME}}');
INSERT INTO GTEMAILTEMPLATE (EMAILTEMPLATEID, EMAILTEMPLATENAME, FROM_ADDRESS_NAME, FROM_EMAIL_ADDRESS, EMAIL_SUBJECT, HTML_BODY, TEXT_BODY) VALUES ('0a18120e-96c8-41bd-9138-86db033243ba','CLIENT_INVOICE','webmaster@ismyplanner.com','webmaster@ismyplanner.com', '{{VENDOR_NAME}} : Your Invoice', 'Dear {{CLIENT_GIVEN_NAME}},<br>Please click on the link below to download your invoice.<br>{{{INVOICE_LINK}}}<br><br> If you have any questions please contact us at {{{VENDOR_EMAIL}}}.<br><br> Thank You for your business.<br>{{VENDOR_NAME}}','Dear {{CLIENT_GIVEN_NAME}},\nPlease click on the link below to download your invoice.\n{{{INVOICE_LINK}}}\n\n If you have any questions please contact us at {{{VENDOR_EMAIL}}}.\n\n Thank You for your business.\n{{VENDOR_NAME}}');

