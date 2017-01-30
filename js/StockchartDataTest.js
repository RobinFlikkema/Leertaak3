$(document).ready(function () {
    $.ajax({
        url: 'https://vm.robinflikkema.nl/api/country?name=New+Zealand',
        type: 'get',
        dataType: 'json',
        withCredentials: true,

        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic YWRtaW46dGVzdDEyMw==");
        },

        success: function (data) {
            var parsed = testparse(data);
            console.log(parsed);
        }
    });
});

function round(value, decimals) {
    return Number(Math.round(value + 'e' + decimals) + 'e-' + decimals);
}