var oceanic_stations = [914900, 931190, 932910, 933080, 934170, 934340, 934360, 935160, 935450, 936140, 938060, 938440, 938960, 939440, 939860, 939970];
var currentrose;
var previousdata = [];
var selected;

function createchart(seriedata) {
    var categories = ['N', 'NNE', 'NE', 'ENE', 'E', 'ESE', 'SE', 'SSE', 'S', 'SSW', 'SW', 'WSW', 'W', 'WNW', 'NW', 'NNW'];

    // source: http://stackoverflow.com/a/26216564
    currentrose = Highcharts.chart('windrosecontainer', {

        series: seriedata,

        chart: {
            polar: true,
            type: 'line'
        },

        tooltip: {
            headerFormat: '<span style="color:{point.color}">\u25CF</span>  <b>{series.name}</b><br/>',
            pointFormat: '<b>direction:</b> {point.x:.2f}\u00b0<br/><b>speed:</b> {point.y:.2f} km/h'
        },

        title: {
            text: ''
        },
        pane: {
            // size: '85%'
        },
        legend: {
            enabled: true,
            align: 'right',
            layout: 'vertical'
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
                text: 'Wind speed'
            },
            labels: {
                formatter: function () {
                    return this.value;
                }
            },
            reversedStacks: false
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
                                        var totalwind = 0;
                                        var countwind = 0;
                                        var totaldir = 0;
                                        var countdir = 0;
                                        for (var key in station['measurement']) {
                                            var measurement = station['measurement'][key];
                                            if (measurement['type'] == 'wind') {
                                                totalwind += parseInt(measurement['value']);
                                                countwind++;
                                            }
                                            if (measurement['type'] == 'wind_dir') {
                                                totaldir += parseFloat(measurement['value']);
                                                countdir++;
                                            }
                                        }
                                    }

                                    var avgdir = totaldir / countdir;
                                    var avgwind = Math.round((totalwind / countwind) * 100) / 100;

                                    if (Math.round(avgwind) != 0) {
                                        var name = station['name'].toString();
                                        name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                                        winddata.push({
                                            name: name,
                                            data: [[0, 0], [avgdir, avgwind]],
                                            animation: false
                                        });
                                    }
                                }
                            }
                        }
                    }
                    destroydummywindrose();
                    createchart(winddata);
                }
            }
        );
        setTimeout(function () {
            update();
        }, 60000);
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

        plotOptions: {
            series: {
                animation: false
            }
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
