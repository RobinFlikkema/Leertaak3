var oceanic_stations = [914900, 931190, 932910, 933080, 934170, 934340, 934360, 935160, 935450, 936140, 938060, 938440, 938960, 939440, 939860, 939970];
var currentrose;
var previousdata = [];

function createchart(seriedata) {
    var categories = ['N', 'NNE', 'NE', 'ENE', 'E', 'ESE', 'SE', 'SSE', 'S', 'SSW', 'SW', 'WSW', 'W', 'WNW', 'NW', 'NNW'];

    // source: http://stackoverflow.com/a/26216564
    currentrose = Highcharts.chart('windrosecontainer', {

        series: seriedata,

        chart: {
            polar: true,
            type: 'line'
        },
        title: {
            text: 'Wind Rose'
        },
        pane: {
            size: '85%'
        },
        legend: {
            enabled: false,
            layout: 'horizontal'
        },
        xAxis: {
            min: 0,
            max: 360,
            type: "",
            tickInterval: 22.5,
            tickmarkPlacement: 'on',
            labels: {
                formatter: function () {
                    return categories[this.value / 22.5] + '°';
                }
            }
        },
        yAxis: {
            min: 0,
            endOnTick: false,
            showLastLabel: true,
            title: {
                text: 'Frequency (%)'
            },
            labels: {
                formatter: function () {
                    return this.value + '%';
                }
            },
            reversedStacks: false
        },

        plotOptions: {
            series: {
                stacking: 'normal',
                shadow: false,
                groupPadding: 0,
                pointPlacement: 'on'
            }
        },

        credits: {
            enabled: false
        }
    });
}

$(function () {
    createdummywindrose();
    function update() {
        $.ajax({
                url: 'https://vm.robinflikkema.nl/api/country?name=New+Zealand',
                type: 'get',
                dataType: 'json',
                withCredentials: true,

                success: function (data) {
                    var winddata = [];
                    for (var key in data) {
                        if (data.hasOwnProperty(key)) {
                            if (key == "error") {
                                break;
                            }
                            var root = data[key];
                            for (var key in root) {
                                if (root.hasOwnProperty(key)) {
                                    var station = root[key];
                                    if (oceanic_stations.indexOf(parseInt(station['id'])) != -1) {
                                        var totalwind = [];
                                        var totaldir = [];
                                        for (var key in station['measurement']) {
                                            var measurement = station['measurement'][key];
                                            if (measurement['type'] == 'wind') {
                                                totalwind.push(parseFloat(measurement['value']));
                                            }
                                            if (measurement['type'] == 'wind_dir') {
                                                totaldir.push(parseFloat(measurement['value']));
                                            }
                                        }
                                        var datafield = [];

                                        for (i = 0; i < totaldir.length; i++) {
                                            datafield.push([totaldir[i], totalwind[i]]);
                                        }
                                    }

                                    previousdata = datafield;
                                    var name = station['name'].toString();
                                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                                    winddata.push({
                                        name: name,
                                        data: datafield
                                    });
                                }
                            }
                        }
                    }
                    console.debug("data isn't equal");
                    destroydummywindrose();
                    createchart(winddata);
                }
            }
        );
        setTimeout(function () {
            update();
        }, 5000);
    }

    update();
})
;

var createdummywindrose = function () {
    currentrose = Highcharts.chart('windrosecontainer', {
        chart: {
            polar: true,
            type: 'column'
        },

        lang: {
            noData: 'Loading chart data'
        },

        title: {
            text: ''
        },

        credits: {
            enabled: false
        }
    });
}

var destroydummywindrose = function () {
    try {
        currentrose.destroy();
    } catch (e) {
    }
}