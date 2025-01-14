package com.events.bean.vendors.website;

import com.events.common.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: root
 * Date: 1/26/14
 * Time: 11:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class VendorWebsiteRequestBean {


    private String vendorId = Constants.EMPTY;
    private String themeName = Constants.EMPTY;
    private String logoImage = Constants.EMPTY;
    private String landingPageImage = Constants.EMPTY;
    private String facebookFeedScript = Constants.EMPTY;
    private String pinterestFeedScript = Constants.EMPTY;
    private String greetingHeader = Constants.EMPTY;
    private String greetingText = Constants.EMPTY;

    private String subDomain = Constants.EMPTY;
    private String customDomain = Constants.EMPTY;
    private boolean isCustomDomainUsed = false;


    private String background = Constants.EMPTY;
    private String highlightedTextOrLink = Constants.EMPTY;
    private String text = Constants.EMPTY;
    private String navBreadTabBackground = Constants.EMPTY;
    private String border = Constants.EMPTY;
    private String filledButton = Constants.EMPTY;
    private String filledButtonText = Constants.EMPTY;

    private String plainButton = Constants.EMPTY;
    private String plainButtonText = Constants.EMPTY;

    private String modifiedByUserId = Constants.EMPTY;

    private String action = Constants.EMPTY;

    private String vendorWebsiteId = Constants.EMPTY;
    private String vendorWebsiteTypeName = Constants.EMPTY;


    private String aboutUs = Constants.EMPTY;
    private String contact = Constants.EMPTY;
    private String privacy = Constants.EMPTY;

    private String facebookFollowUs = Constants.EMPTY;
    private String twitterFollowUs = Constants.EMPTY;
    private String pinterestFollowUs = Constants.EMPTY;
    private String contents = Constants.EMPTY;

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getAboutUs() {
        return aboutUs;
    }

    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getFacebookFollowUs() {
        return facebookFollowUs;
    }

    public void setFacebookFollowUs(String facebookFollowUs) {
        this.facebookFollowUs = facebookFollowUs;
    }

    public String getTwitterFollowUs() {
        return twitterFollowUs;
    }

    public void setTwitterFollowUs(String twitterFollowUs) {
        this.twitterFollowUs = twitterFollowUs;
    }

    public String getPinterestFollowUs() {
        return pinterestFollowUs;
    }

    public void setPinterestFollowUs(String pinterestFollowUs) {
        this.pinterestFollowUs = pinterestFollowUs;
    }

    public String getVendorWebsiteTypeName() {
        return vendorWebsiteTypeName;
    }

    public void setVendorWebsiteTypeName(String vendorWebsiteTypeName) {
        this.vendorWebsiteTypeName = vendorWebsiteTypeName;
    }

    public String getGreetingHeader() {
        return greetingHeader;
    }

    public void setGreetingHeader(String greetingHeader) {
        this.greetingHeader = greetingHeader;
    }

    public String getGreetingText() {
        return greetingText;
    }

    public void setGreetingText(String greetingText) {
        this.greetingText = greetingText;
    }

    public String getSubDomain() {
        return subDomain;
    }

    public void setSubDomain(String subDomain) {
        this.subDomain = subDomain;
    }

    public String getCustomDomain() {
        return customDomain;
    }

    public void setCustomDomain(String customDomain) {
        this.customDomain = customDomain;
    }

    public boolean isCustomDomainUsed() {
        return isCustomDomainUsed;
    }

    public void setCustomDomainUsed(boolean customDomainUsed) {
        isCustomDomainUsed = customDomainUsed;
    }

    public String getVendorWebsiteId() {
        return vendorWebsiteId;
    }

    public void setVendorWebsiteId(String vendorWebsiteId) {
        this.vendorWebsiteId = vendorWebsiteId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public String getLandingPageImage() {
        return landingPageImage;
    }

    public void setLandingPageImage(String landingPageImage) {
        this.landingPageImage = landingPageImage;
    }

    public String getFacebookFeedScript() {
        return facebookFeedScript;
    }

    public void setFacebookFeedScript(String facebookFeedScript) {
        this.facebookFeedScript = facebookFeedScript;
    }

    public String getPinterestFeedScript() {
        return pinterestFeedScript;
    }

    public void setPinterestFeedScript(String pinterestFeedScript) {
        this.pinterestFeedScript = pinterestFeedScript;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getHighlightedTextOrLink() {
        return highlightedTextOrLink;
    }

    public void setHighlightedTextOrLink(String highlightedTextOrLink) {
        this.highlightedTextOrLink = highlightedTextOrLink;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNavBreadTabBackground() {
        return navBreadTabBackground;
    }

    public void setNavBreadTabBackground(String navBreadTabBackground) {
        this.navBreadTabBackground = navBreadTabBackground;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getFilledButton() {
        return filledButton;
    }

    public void setFilledButton(String filledButton) {
        this.filledButton = filledButton;
    }

    public String getFilledButtonText() {
        return filledButtonText;
    }

    public void setFilledButtonText(String filledButtonText) {
        this.filledButtonText = filledButtonText;
    }

    public String getPlainButton() {
        return plainButton;
    }

    public void setPlainButton(String plainButton) {
        this.plainButton = plainButton;
    }

    public String getPlainButtonText() {
        return plainButtonText;
    }

    public void setPlainButtonText(String plainButtonText) {
        this.plainButtonText = plainButtonText;
    }

    public String getModifiedByUserId() {
        return modifiedByUserId;
    }

    public void setModifiedByUserId(String modifiedByUserId) {
        this.modifiedByUserId = modifiedByUserId;
    }
}
