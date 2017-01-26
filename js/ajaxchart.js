$(document).ready(function () {
    $.ajax({
        url: 'https://vm.robinflikkema.nl/api/country?name=New+Zealand',
        type: 'get',
        dataType: 'json',
        withCredentials: true,

        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", make_auth());
        },
        success: function (data) {

        }
    });
});

function make_auth() {
    var token = 'admin' + ':' + 'test123';
    var hash = btoa(token);
    return 'Basic ' + hash;
}