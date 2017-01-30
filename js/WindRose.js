$(function () {

    var polar = Highcharts.chart('windrosecontainer', {

        chart: {
            polar: true
        },

        title: {
            text: ''
        },

        pane: {
            startAngle: 0,
            endAngle: 360
        },

        xAxis: {
            tickInterval: 45,
            min: 0,
            max: 360,
            labels: {
            }
        },

        yAxis: {
            min: 0
        },

        plotOptions: {
            series: {
                pointStart: 0,
                pointInterval: 45
            },
            column: {
                pointPadding: 0,
                groupPadding: 0
            }
        },

        legend: {
            enabled: false
        },

        series: [{
            type: 'column',
            name: 'wind'
        }],

        credits: {
            enabled: false
        }

    });
    // updatechart(polar)
    // setInterval(function () {
    //     updatechart(polar);
    // }, 5000);
});

function updatechart(chart) {
    $.ajax({
        url: 'https://vm.robinflikkema.nl/api/country?name=New+Zealand',
        type: 'get',
        dataType: 'json',
        withCredentials: true,

        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Basic YWRtaW46dGVzdDEyMw==");
        },

        success: function (data) {
            var stations = []
            $.each(data, function (check, station) {                            // Check whether or not there is any data.
                if (check == "error"){
                    console.log("Error")
                    return;
                }
                $.each(station, function (_, station_numbers) {                 // Cycle through stations.
                    var stationdata = []
                    $.each(station_numbers, function (_, measurement) {         // Cycle through measurements in the stations.
                        stationdata.push(measurement);
                    });
                    var temp = new Station( stationdata[0],                     // Get all the data and construct a station object.
                        stationdata[1],
                        stationdata[2],
                        stationdata[3]);
                    stations.push(temp);                                        // Put Station object in an array for further handling.
                    // temp.printDetails();
                });
                // Cycle through the received data
                $.each(stations, function (_, data) {
                    // TODO: Put data in chart.
                })
            });
        }
    });
}