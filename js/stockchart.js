var dummychart;

function createstockchart(seriesdata) {
    var stockchart = Highcharts.stockChart('stockchartcontainer', {

        rangeSelector: {
            enabled: false
        },

        tooltip: {
            valueSuffix: ' \u00b0C'
        },

        chart: {
            events: {
                load: function update() {
                    $.ajax({
                        url: 'https://vm.robinflikkema.nl/api/country?name=New+Zealand',
                        type: 'get',
                        dataType: 'json',
                        withCredentials: true,

                        success: function (data) {
                            var parsed = updateparse(data);
                            for (var i = 0; i < parsed.length; i++) {
                                for (var j = 0; j < stockchart.series.length; j++) {
                                    if (stockchart.series[j].name == parsed[i][0]) {
                                        // console.debug(stockchart.series[j]['data'][i]);
                                        // if (stockchart.series[j]['data'][i].y != null) {
                                        //     if (stockchart.series[j]['data'][i].y != parsed[i][2]) {
                                        //         console.debug("data is NOT the same");
                                        //     }
                                        // }
                                        stockchart.series[j].addPoint([parsed[i][1], parsed[i][2]], false);
                                    }
                                }
                            }
                            stockchart.redraw();
                            console.debug("updated");
                        },

                        complete: function () {
                            bool = false;
                        }
                    });
                    setTimeout(function () {
                        update();
                    }, 5000);
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
    createdummychart();
    console.debug(new Date().getTime());
    $.ajax({
        url: 'https://vm.robinflikkema.nl/api/country?name=New+Zealand',
        type: 'get',
        dataType: 'json',
        withCredentials: true,


        success: function (data) {
            console.debug(new Date().getTime());
            var parsed = setupparse(data);
            destroydummychart();
            createstockchart(parsed);
        },

        complete: function (obj, message) {
            if (message != 'success') {
                console.debug(message);
            }
        }
    });
});

var setupparse = function (data) {
    var temps = [];
    for (var key in data) {
        if (data.hasOwnProperty(key)) {
            if (key == 'error') {
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
                            if (properties['type'] == 'temp') {
                                temp.push([parseInt(properties['time']) * 1000 + 3600000, parseFloat(properties['value'])]);
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

                    var name = stations['name'].toString();
                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                    temps.push({
                        name: name,
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
            if (key == 'error') {
                break;
            }
            var root = data[key];
            for (var key in root) {
                if (root.hasOwnProperty(key)) {

                    var stations = root[key];

                    for (var key in stations['measurement']) {
                        if (stations['measurement'].hasOwnProperty(key)) {
                            var properties = stations['measurement'][key];
                            if (properties['type'] == 'temp') {
                                var name = stations['name'].toString();
                                name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                                temp.push([name, parseInt(properties['time']) * 1000, parseFloat(properties['value'])]);
                            }
                        }
                    }
                }
            }
        }
    }
    return temp;
}

var createdummychart = function () {
    dummychart = Highcharts.stockChart('stockchartcontainer', {

        global: {
            useUTC: false,
            timezoneOffset: 60
        },

        lang: {
            noData: "Loading chart data"
        },

        rangeSelector: {
            selected: 1
        },

        legend: {
            enabled: false,
        },

        title: {
            text: ''
        },

        credits: {
            enabled: false
        }
    });
}

var destroydummychart = function () {
    try {
        dummychart.destroy();
    } catch (e) {
    }
}