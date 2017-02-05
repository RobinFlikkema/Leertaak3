var dummystock;

function createstockchart(seriesdata) {

    // Creates a Highcharts stockchart with configurations in a dictionary format.
    var stockchart = Highcharts.stockChart('tempstockchartcontainer', {

        rangeSelector: {
            enabled: false
        },

        tooltip: {
            pointFormat: '<span style="color:{point.color}">\u25CF</span> {series.name}: <b>{point.y:.2f} \u00b0C</b><br/>',
        },

        scrollbar: {
            liveRedraw: false
        },

        chart: {
            events: {
                load: function update() {
                    // This call will retrieve data from the API and update the chart with the addPoint() method.
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
                                        stockchart.series[j].addPoint([parsed[i][1], parsed[i][2]], false);
                                    }
                                }
                            }
                            stockchart.redraw(false); // Redraw the Chart without animations because this will most likely confuse the user.
                        }
                    });
                    // Update the chart every 10 seconds
                    setTimeout(function () {
                        update();
                    }, 10000);
                }
            }
        },

        // Display values from 0 to 30 because temperature will most likely never exceed 30
        // also Highcharts auto resizes otherwise which might confuse the user.
        yAxis: {
            min: 0,
            max: 30
        },

        legend: {
            enabled: true,
            layout: 'horizontal'
        },

        // Title is provided by Bootstrap's panel element
        title: {
            text: ''
        },

        // The initial data provided by the initial AJAX call which was parsed by setupparse()
        series: seriesdata,

        credits: {
            enabled: false
        }
    });
}

$(document).ready(function () {
    // When the DOM is fully loaded.
    createdummychartstockchart(); // Dummy chart used for showing that the data is being loaded
    // Initial AJAX request
    $.ajax({
        url: 'https://vm.robinflikkema.nl/api/country?name=New+Zealand',
        type: 'get',
        dataType: 'json',
        withCredentials: true,


        success: function (data) {
            var parsed = setupparse(data); // On success and response parse the data.
            destroydummychartstockchart(); // Destroys the "load screen"
            createstockchart(parsed); // Create new chart with the parsed data
        }
    });
});

function setupparse(data) {
    //Setup parse for the initial chart construction. Necessary for HighCharts

    var temps = [];

    // Parse the JSON by using a bunch of for loops that will go deeper into each layer of JSON.
    for (var key in data) {
        if (data.hasOwnProperty(key)) {
            if (key == 'error') { // When there is no data to be received, stop immediately.
                break;
            }
            var root = data[key];
            for (var key in root) {
                if (root.hasOwnProperty(key)) {

                    var station = root[key];
                    var temp = []
                    for (var key in station['measurement']) {
                        if (station['measurement'].hasOwnProperty(key)) {
                            var properties = station['measurement'][key];
                            if (properties['type'] == 'temp') {
                                // Time * 1000 to convert the UNIX timestamp into milliseconds so that it can be seen as a legitimate date.
                                temp.push([parseInt(properties['time']) * 1000 + 3600000, parseFloat(properties['value'])]);
                            }
                        }
                    }

                    // Sort the temperature data because HighCharts requires a consecutive data stream.
                    temp.sort(function (a, b) {
                        if (a[0] === b[0]) {
                            return 0;
                        }
                        else {
                            return (a[0] < b[0]) ? -1 : 1;
                        }
                    });

                    // Capitalize the station name and push the data into a dictionary type that is accepted by Highcharts.
                    var name = station['name'].toString();
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

function updateparse(data) {
    // updateparse functions similarly if not almost identical to setupparse()
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

var createdummychartstockchart = function () {

    // Creates a dummy chart that is replaced later to show that data is being loaded.
    dummystock = Highcharts.stockChart('tempstockchartcontainer', {

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

var destroydummychartstockchart = function () {
    // Simply destroys the dummy chart so that it can be replaced by the real one
    try {
        dummystock.destroy();
    } catch (e) {
    }
}