function createstockchart(seriesdata) {
    var stockchart = Highcharts.stockChart('stockchartcontainer', {

        global: {
            useUTC: false,
            timezoneOffset: 60
        },

        chart: {
            events: {
                load: function () {
                    var series = this.series;
                    setInterval(function () {
                        $.ajax({
                            url: 'https://vm.robinflikkema.nl/api/country?name=New+Zealand',
                            type: 'get',
                            dataType: 'json',
                            withCredentials: true,

                            beforeSend: function (xhr) {
                                xhr.setRequestHeader("Authorization", "Basic YWRtaW46dGVzdDEyMw==");
                            },

                            success: function (data) {
                                var parsed = updateparse(data);
                                for(var i = 0; i < parsed.length; i++){
                                    for(var j = 0; j < stockchart.series.length; j++){
                                        if(stockchart.series[j].name == parsed[i][0]){
                                            stockchart.series[j].addPoint([parsed[i][1], parsed[i][2]], false, true);
                                        }
                                    }
                                }
                                stockchart.redraw();
                                console.debug("updated");
                            }
                        });
                    }, 10000);
                }
            }
        },

        rangeSelector: {
            selected: 1
        },

        legend: {
            enabled: true,
            layout: 'horizontal'
        },

        title: {
            text: ''
        },

        series: seriesdata,

        credits: {
            enabled: false
        }
    });
}

$(document).ready(function () {
    console.debug(new Date().getTime());
    $.ajax({
        url: 'https://vm.robinflikkema.nl/api/country?name=New+Zealand',
        type: 'get',
        dataType: 'json',
        withCredentials: true,

        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic YWRtaW46dGVzdDEyMw==");
        },

        success: function (data) {
            console.debug(new Date().getTime());
            var parsed = setupparse(data);
            createstockchart(parsed);
        },

        complete: function (obj, message) {
            if(message != 'success'){
                console.debug(message);
            }
        }
    })
});

var setupparse = function (data) {
    var temps = [];
    for (var key in data) {
        if (data.hasOwnProperty(key)) {
            if(key == 'error'){
                break;
            }
            var root = data[key];
            for (var key in root) {
                if (root.hasOwnProperty(key)) {

                    var stations = root[key];
                    var temp = []
                    for (var key in stations['measurement']) {
                        if (stations['measurement'].hasOwnProperty(key)) {
                            var properties = stations['measurement'][key];
                            if(properties['type'] == 'temp'){
                                temp.push([parseInt(properties['time'])*1000, parseFloat(properties['value'])]);
                            }
                        }
                    }
                    temp.sort(function (a, b) {
                        if (a[0] === b[0]) {
                            return 0;
                        }
                        else {
                            return (a[0] < b[0]) ? -1 : 1;
                        }
                    });

                    temps.push({
                        name: stations['name'].toString(),
                        data: temp
                    });
                }
            }
        }
    }
    return temps;
};

var updateparse = function (data) {
    var temp = []
    for (var key in data) {
        if (data.hasOwnProperty(key)) {
            if(key == 'error'){
                break;
            }
            var root = data[key];
            for (var key in root) {
                if (root.hasOwnProperty(key)) {

                    var stations = root[key];

                    for (var key in stations['measurement']) {
                        if (stations['measurement'].hasOwnProperty(key)) {
                            var properties = stations['measurement'][key];
                            if(properties['type'] == 'temp'){
                                temp.push([stations['name'].toString(), parseInt(properties['time'])*1000, parseFloat(properties['value'])]);
                            }
                        }
                    }
                }
            }
        }
    }
    return temp;
}