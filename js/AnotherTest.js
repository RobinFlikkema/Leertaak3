$(document).ready(function () {
// ajaxcall();
});

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
        }
    })
});

var testparse = function (data) {
    var station_ids = [];
    var averages = [];
    for (var key in data) {
        if (data.hasOwnProperty(key)) {
            if(key == 'error'){
                break;
            }
            var root = data[key];
            for (var key in root) {
                if (root.hasOwnProperty(key)) {

                    var stations = root[key];
                    var tottemp = 0;
                    var count = 0;
                    for (var key in stations['measurement']) {
                        if (stations['measurement'].hasOwnProperty(key)) {
                            var properties = stations['measurement'][key];
                            console.debug(properties['type']);
                            if(properties['type'] == 'temp'){
                                tottemp += parseFloat(properties['value']);
                                count++;
                            }
                        }
                    }
                    var avg = round((tottemp / count), 2);
                    station_ids.push(stations['id']);
                    averages.push(avg);
                }
            }
        }
    }
    return [station_ids, averages];
};

function round(value, decimals) {
    return Number(Math.round(value + 'e' + decimals) + 'e-' + decimals);
}