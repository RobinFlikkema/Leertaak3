var humdummystock;

function createhumstockchart(seriesdata) {
    var humstockchart = Highcharts.stockChart('humidstockchartcontainer', {

        rangeSelector: {
            enabled: false
        },

        tooltip: {
            pointFormat: '<span style="color:{point.color}">\u25CF</span> {series.name}: <b>{point.y:.2f}%</b><br/>',
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
                            var parsed = updateparsehum(data);
                            for (var i = 0; i < parsed.length; i++) {
                                for (var j = 0; j < humstockchart.series.length; j++) {
                                    if (humstockchart.series[j].name == parsed[i][0]) {
                                        humstockchart.series[j].addPoint([parsed[i][1], parsed[i][2]], false);
                                    }
                                }
                            }
                            humstockchart.redraw(false);
                        }
                    });
                    setTimeout(function () {
                        update();
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
    createhumdummychartstockchart();
    $.ajax({
        url: 'https://vm.robinflikkema.nl/api/country?name=New+Zealand',
        type: 'get',
        dataType: 'json',
        withCredentials: true,


        success: function (data) {
            var parsed = setupparsehum(data);
            destroyhumdummychartstockchart();
            createhumstockchart(parsed);
        },

        complete: function (obj, message) {
            if (message != 'success') {
            }
        }
    });
});

function setupparsehum(data) {
    var hums = [];
    for (var key in data) {
        if (data.hasOwnProperty(key)) {
            if (key == 'error') {
                break;
            }
            var root = data[key];
            for (var key in root) {
                if (root.hasOwnProperty(key)) {

                    var station = root[key];
                    var hum = []
                    for (var key in station['measurement']) {
                        if (station['measurement'].hasOwnProperty(key)) {
                            var properties = station['measurement'][key];
                            if (properties['type'] == 'hum') {
                                hum.push([parseInt(properties['time']) * 1000 + 3600000, parseFloat(properties['value'])]);
                            }
                        }
                    }
                    hum.sort(function (a, b) {
                        if (a[0] === b[0]) {
                            return 0;
                        }
                        else {
                            return (a[0] < b[0]) ? -1 : 1;
                        }
                    });

                    var name = station['name'].toString();
                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                    hums.push({
                        name: name,
                        data: hum
                    });
                }
            }
        }
    }
    return hums;
};

function updateparsehum(data) {
    var hum = []
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
                            if (properties['type'] == 'hum') {
                                var name = stations['name'].toString();
                                name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                                hum.push([name, parseInt(properties['time']) * 1000, parseFloat(properties['value'])]);
                            }
                        }
                    }
                }
            }
        }
    }
    return hum;
}

var createhumdummychartstockchart = function () {
    humdummystock = Highcharts.stockChart('humidstockchartcontainer', {

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

var destroyhumdummychartstockchart = function () {
    try {
        humdummystock.destroy();
    } catch (e) {
    }
}