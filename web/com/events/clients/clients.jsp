<jsp:include page="/com/events/common/header_top.jsp">
    <jsp:param name="page_title" value=""/>
</jsp:include>
<link rel="stylesheet" href="/css/dataTables/jquery.dataTables.css" id="theme_date">
<link rel="stylesheet" href="/css/dataTables/jquery.dataTables_styled.css" id="theme_time">
<jsp:include page="/com/events/common/header_bottom.jsp"/>
<body>
<div class="page_wrap">
    <jsp:include page="/com/events/common/top_nav.jsp">
        <jsp:param name="AFTER_LOGIN_REDIRECT" value="index.jsp"/>
    </jsp:include>
    <jsp:include page="/com/events/common/menu_bar.jsp">
        <jsp:param name="client_active" value="currently_active"/>
    </jsp:include>
    <div class="breadcrumb_format">
        <div class="container">
            <div class="page-title">Clients</div>
        </div>
    </div>

    <div class="container">
        <div class="content_format">
            <div class="row">
                <div class="col-md-8">
                    <div  style="float:left"><button type="button" class="btn btn-filled" id="btn_new_client">
                        <i class="fa fa-plus"></i> Add a Client</button></div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <table cellpadding="0" cellspacing="0" border="0" class="display table dataTable" id="every_client" >
                        <thead>
                        <tr role="row">
                            <th role="columnheader">Name</th>
                            <th role="columnheader">Email</th>
                            <th role="columnheader"></th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script id="template_client_row" type="text/x-handlebars-template">
    <tr>
        <td>{{client_name}}</td>
        <td>{{client_email}}</td>
        <td>
            <a id="edit_{{client_id}}" href="/com/events/clients/client_contact_form.jsp?client_id={{client_id}}&client_datatype=contact_info" class="btn btn-default btn-xs"><i class="fa fa-pencil"></i> Edit</a>
            &nbsp;&nbsp;
            <a id="delete_{{client_id}}" class="btn btn-default btn-xs"><i class="fa fa-trash-o"></i> Delete</a>
        </td>
    </tr>
</script>
<jsp:include page="/com/events/common/footer_top.jsp"/>
<script src="/js/handlebars-v1.3.0.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/underscore.js/1.5.2/underscore-min.js"></script>
<script src="http://cdnjs.cloudflare.com/ajax/libs/backbone.js/1.1.0/backbone-min.js"></script>
<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.10.3/jquery-ui.min.js"></script>
<script src="/js/jquery.dataTables.min.js"></script>
<script   type="text/javascript">
    $(window).load(function() {
        loadClients(populateClientList);
    });
    function loadClients(callbackmethod) {
        var actionUrl = "/proc_load_clients.aeve";
        var methodType = "POST";
        var dataString = '';
        makeAjaxCall(actionUrl,dataString,methodType,callbackmethod);
    }
    function populateClientList(jsonResult) {
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
                    var varNumOfClients = jsonResponseObj.num_of_clients;
                    if(varNumOfClients>0){
                        this.clientsSummaryListModel = new ClientsSummaryListModel({
                            'bb_num_of_clients' : varNumOfClients,
                            'bb_client_list_summary' : jsonResponseObj.all_client_summary
                        });
                        var clientsSummaryView = new ClientsSummaryListView({model:this.clientsSummaryListModel});
                        clientsSummaryView.render();
                        $("#every_client").append(clientsSummaryView.el);
                    } else {
                        //displayMssgBoxAlert("Create a new client here.", true);
                    }
                    initializeContactUsTable();
                }
            } else {
                displayMssgBoxAlert("Please try again later (populateClientList - 1)", true);
            }
        } else {
            displayMssgBoxAlert("Please try again later (populateClientList - 2)", true);
        }
    }

    var ClientsSummaryListModel = Backbone.Model.extend({
        defaults: {
            bb_num_of_clients: 0 ,
            bb_client_list_summary: undefined
        }
    });
    var ClientsSummaryListView = Backbone.View.extend({
        tagName: "tbody",
        id : "every_client_rows",
        initialize: function(){
            this.varNumOfClients = this.model.get('bb_num_of_clients');
            this.varClientListSummary = this.model.get('bb_client_list_summary');
        },
        template : Handlebars.compile( $('#template_client_row').html() ),
        render : function() {
            for(i=0;i<this.varNumOfClients;i++){
                var varClientBean = this.varClientListSummary[i];

                var clientRow = this.template(  eval(varClientBean)  );
                $(this.el).append( clientRow );
            }
        }
    });
    function initializeContactUsTable(){

        objEveryClientTable =  $('#every_client').dataTable({
            "bPaginate": false,
            "bInfo": false,
            "bFilter": false,
            "aoColumns":  [
                {"bSortable": true,"sClass":"col-md-5"},
                {"bSortable": true},
                { "bSortable": false,"sClass": "center" }
            ]
        });
    }
</script>
<jsp:include page="/com/events/common/footer_bottom.jsp"/>