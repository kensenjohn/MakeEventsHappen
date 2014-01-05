

<form method="post" id="frm_save_clients">
    <div class="form-group">
        <div class="row">
            <div class="col-md-8">
                <label for="clientName" class="form_label">Name</label><span class="required"> *</span>
                <input type="text" class="form-control" id="clientName" name="clientName" placeholder="Client Name (e.g Ron and Susan) ">
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="row">
            <div class="col-md-4">
                <label for="clientFirstName" class="form_label">First Name</label>
                <input type="text" class="form-control" id="clientFirstName" name="clientFirstName" placeholder="First Name">
            </div>
            <div class="col-md-4">
                <label for="clientLastName" class="form_label">Last Name</label>
                <input type="text" class="form-control" id="clientLastName" name="clientLastName" placeholder="Last Name">
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="row">
            <div class="col-md-8">
                <label for="clientEmail" class="form_label">Email</label><span class="required"> *</span>
                <input type="email" class="form-control" id="clientEmail" name="clientEmail" placeholder="Email">
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="row">
            <div class="col-md-8">
                <label for="clientCompanyName" class="form_label">Company Name</label>
                <input type="text" class="form-control" id="clientCompanyName" name="clientCompanyName" placeholder="Company Name">
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="row">
            <div class="col-md-4">
                <label for="clientCellPhone" class="form_label">Cell Phone</label>
                <input type="tel" class="form-control" id="clientCellPhone" name="clientCellPhone" placeholder="Cell Phone">
            </div>
            <div class="col-md-4">
                <label for="clientWorkPhone" class="form_label">Phone</label>
                <input type="tel" class="form-control" id="clientWorkPhone" name="clientWorkPhone" placeholder="Phone">
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="row">
            <div class="col-md-4">
                <label for="clientAddress1" class="form_label">Address 1</label>
                <input type="text" class="form-control" id="clientAddress1" name="clientAddress1" placeholder="Address 1">
            </div>
            <div class="col-md-4">
                <label for="clientAddress2" class="form_label">Address 2</label>
                <input type="text" class="form-control" id="clientAddress2" name="clientAddress2" placeholder="Address 2">
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="row">
            <div class="col-md-4">
                <label for="clientCity" class="form_label">City</label>
                <input type="text" class="form-control" id="clientCity" name="clientCity" placeholder="City">
            </div>
            <div class="col-md-4">
                <label for="clientState" class="form_label">State</label>
                <input type="text" class="form-control" id="clientState" name="clientState" placeholder="State">
            </div>
        </div>
    </div>
    <div class="form-group">
        <div class="row">
            <div class="col-md-4">
                <label for="clientPostalCode" class="form_label">Postal Code</label>
                <input type="text" class="form-control" id="clientPostalCode" name="clientPostalCode" placeholder="Postal Code">
            </div>
            <div class="col-md-4">
                <label for="clientCountry" class="form_label">Country</label>
                <input type="text" class="form-control" id="clientCountry" name="clientCountry" placeholder="Country">
            </div>
        </div>
    </div>
    <div class="checkbox">
        <label for="isCorporateClient" class="form_label">
            <input type="checkbox" id="isCorporateClient" name = "isCorporateClient">
            Corporate Client
        </label>
    </div>
    <button type="button" class="btn  btn-filled" id="btn_save_client">Save Changes</button>
    <input type="hidden"  id="clientId" name="clientId" value="">
    <input type="hidden"  id="userId" name="userId" value="">
    <input type="hidden"  id="userInfoId" name="userInfoId" value="">
</form>