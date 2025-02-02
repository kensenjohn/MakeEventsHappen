package com.events.event.website;

import com.events.bean.event.website.EventWebsiteBean;
import com.events.bean.event.website.EventWebsitePageBean;
import com.events.common.Constants;
import com.events.common.Utility;
import com.events.data.event.website.AccessEventWebsitePageData;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 2/21/14
 * Time: 12:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccessEventWebsitePage {
    public ArrayList<EventWebsitePageBean> getEventWebsitePage(EventWebsiteBean eventWebsiteBean) {
        ArrayList<EventWebsitePageBean> arrEventWebsitePageBean = new ArrayList<EventWebsitePageBean>();
        if(eventWebsiteBean!=null && !Utility.isNullOrEmpty(eventWebsiteBean.getEventWebsiteId())
                && !Utility.isNullOrEmpty(eventWebsiteBean.getWebsiteThemeId()) ) {
            AccessEventWebsitePageData accessEventWebsitePageData = new AccessEventWebsitePageData();
            arrEventWebsitePageBean = accessEventWebsitePageData.getEventWebsitePage(eventWebsiteBean );
        }
        return arrEventWebsitePageBean;
    }

    public HashMap<Constants.EVENT_WEBSITE_PAGETYPE , EventWebsitePageBean > getHashEventWebsitePage(EventWebsiteBean eventWebsiteBean) {
        HashMap<Constants.EVENT_WEBSITE_PAGETYPE , EventWebsitePageBean > hmEventWebsitePage = new HashMap<Constants.EVENT_WEBSITE_PAGETYPE, EventWebsitePageBean>();

        if(eventWebsiteBean!=null && !Utility.isNullOrEmpty(eventWebsiteBean.getEventWebsiteId())
                && !Utility.isNullOrEmpty(eventWebsiteBean.getWebsiteThemeId()) ) {
            AccessEventWebsitePageData accessEventWebsitePageData = new AccessEventWebsitePageData();
            ArrayList<EventWebsitePageBean> arrEventWebsitePageBean =  accessEventWebsitePageData.getEventWebsitePage(eventWebsiteBean );
            if(arrEventWebsitePageBean!=null && !arrEventWebsitePageBean.isEmpty()) {
                for(EventWebsitePageBean eventWebsitePageBean : arrEventWebsitePageBean ){
                    if(eventWebsitePageBean!=null && !Utility.isNullOrEmpty(eventWebsitePageBean.getEventWebsiteId())) {
                        hmEventWebsitePage.put( Constants.EVENT_WEBSITE_PAGETYPE.valueOf( eventWebsitePageBean.getType()) , eventWebsitePageBean );
                    }
                }
            }
        }
        return hmEventWebsitePage;
    }

    public JSONObject getJsonEventWebsitePage(ArrayList<EventWebsitePageBean> arrEventWebsitePageBean){
        JSONObject jsonObject = new JSONObject();
        if(arrEventWebsitePageBean!=null && !arrEventWebsitePageBean.isEmpty()){
            for(EventWebsitePageBean eventWebsitePageBean : arrEventWebsitePageBean ) {
                jsonObject.put(eventWebsitePageBean.getType() , eventWebsitePageBean.toJson() );
            }
        }
        return jsonObject;
    }

    public EventWebsitePageBean getEventWebsitePageByType(EventWebsitePageBean eventWebsitePageBeanReg) {
        EventWebsitePageBean eventWebsitePageBean = new EventWebsitePageBean();
        if(eventWebsitePageBeanReg!=null && !Utility.isNullOrEmpty(eventWebsitePageBeanReg.getEventWebsiteId())
                && !Utility.isNullOrEmpty(eventWebsitePageBeanReg.getWebsiteThemeId()) && !Utility.isNullOrEmpty(eventWebsitePageBeanReg.getType())) {
            AccessEventWebsitePageData accessEventWebsitePageData = new AccessEventWebsitePageData();
            eventWebsitePageBean = accessEventWebsitePageData.getEventWebsitePageByType(eventWebsitePageBeanReg );
        }
        return eventWebsitePageBean;
    }


}
