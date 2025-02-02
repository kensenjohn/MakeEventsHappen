function loadEventInfo(callbackmethod, eventId) {
    if(eventId!=undefined) {
        var actionUrl = "/proc_load_event.aeve";
        var methodType = "POST";
        var dataString = 'event_id='+eventId;
        makeAjaxCall(actionUrl,dataString,methodType,callbackmethod);
    }
}

function populateEventInfo (jsonResult) {
    if(jsonResult!=undefined) {
        var varResponseObj = jsonResult.response;
        if(jsonResult.status == 'error'  && varResponseObj !=undefined ) {
            var varIsMessageExist = varResponseObj.is_message_exist;
            if(varIsMessageExist == true) {
                var jsonResponseMessage = varResponseObj.messages;
                var varArrErrorMssg = jsonResponseMessage.error_mssg;
                displayMssgBoxMessages(varArrErrorMssg, true);
            }

        } else if( jsonResult.status == 'ok' && varResponseObj !=undefined) {
            var varIsPayloadExist = varResponseObj.is_payload_exist;
            if(varIsPayloadExist == true) {
                var jsonResponseObj = varResponseObj.payload;
                processEventInfo(jsonResponseObj.event_bean);
            }
        } else {
            displayMssgBoxAlert("Please try again later (populateEventInfo - 1)", true);
        }
    } else {
        displayMssgBoxAlert("Please try again later (populateEventInfo - 2)", true);
    }
}

function processEventInfo(varEventBean) {
    if(varEventBean!=undefined) {
        $('#eventId').val(varEventBean.event_id);
        $('#eventName').val(varEventBean.event_name);
        $('#eventDay').val(varEventBean.event_display_date.selected_day);
        $('#eventTime').val(varEventBean.event_display_date.selected_time);
        $('#eventTimeZone').val(varEventBean.event_display_date.selected_timezone);
        $('#client_selector').val(varEventBean.client_id);
        $('#event_title').text(varEventBean.event_name);

        if( $.fn.pickadate){
            var pickerDay = $('#eventDay').pickadate('picker');
            if(pickerDay!=undefined){
                pickerDay.set('select', varEventBean.event_display_date.selected_day, { format: 'yyyy/mmmm/dd' });
            }
        }

        if( $.fn.pickatime ){
            var pickerTime = $('#eventTime').pickatime('picker');
            if(pickerTime!=undefined){
                pickerTime.set('select', varEventBean.event_display_date.selected_time, { format: 'h:i A' });
            }
        }

    }
}