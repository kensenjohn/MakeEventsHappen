<%@ page import="com.events.common.Constants" %>
<%@ page import="com.events.common.Utility" %>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%
    com.events.common.Configuration analyticsConfig = com.events.common.Configuration.getInstance(com.events.common.Constants.ANALYTICS_PROP);
    Logger appLogging = LoggerFactory.getLogger(Constants.APPLICATION_LOG);

    String sGoogleTrackId = com.events.common.ParseUtil.checkNull(analyticsConfig.get(com.events.common.Constants.ANALYTICS_KEYS.GOOGLE_TRACKING_ID.getKey()));

    if(!"".equals(sGoogleTrackId))
    {
%>                                                                                                                                    i

<script type="text/javascript">

    var varGoogleTrackId = '<%=sGoogleTrackId%>'
    var _gaq = _gaq || [];
    _gaq.push(['_setAccount', varGoogleTrackId]);
    _gaq.push(['_trackPageview']);

    (function() {
        var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
        ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
        var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
    })();

</script>
<%
    }

    String sMixPanelTrackId = com.events.common.ParseUtil.checkNull(analyticsConfig.get(Constants.ANALYTICS_KEYS.MIXPANEL_TRACKING_ID.getKey()));
    if(!Utility.isNullOrEmpty( sMixPanelTrackId ) ) {
%>
<!-- start Mixpanel --><script type="text/javascript">(function(e,b){if(!b.__SV){var a,f,i,g;window.mixpanel=b;a=e.createElement("script");a.type="text/javascript";a.async=!0;a.src=("https:"===e.location.protocol?"https:":"http:")+'//cdn.mxpnl.com/libs/mixpanel-2.2.min.js';f=e.getElementsByTagName("script")[0];f.parentNode.insertBefore(a,f);b._i=[];b.init=function(a,e,d){function f(b,h){var a=h.split(".");2==a.length&&(b=b[a[0]],h=a[1]);b[h]=function(){b.push([h].concat(Array.prototype.slice.call(arguments,0)))}}var c=b;"undefined"!==
typeof d?c=b[d]=[]:d="mixpanel";c.people=c.people||[];c.toString=function(b){var a="mixpanel";"mixpanel"!==d&&(a+="."+d);b||(a+=" (stub)");return a};c.people.toString=function(){return c.toString(1)+".people (stub)"};i="disable track track_pageview track_links track_forms register register_once alias unregister identify name_tag set_config people.set people.set_once people.increment people.append people.track_charge people.clear_charges people.delete_user".split(" ");for(g=0;g<i.length;g++)f(c,i[g]);
b._i.push([a,e,d])};b.__SV=1.2}})(document,window.mixpanel||[]);
mixpanel.init("<%=sMixPanelTrackId%>");</script><!-- end Mixpanel -->
<%
} else {
%>        <!-- start Mixpanel --><script type="text/javascript">var mixpanel = undefined;</script><!-- end Mixpanel --><%
    }
%>