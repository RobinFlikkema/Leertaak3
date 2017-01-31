function createchart(seriesdata) {

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

        series: seriesdata,

        credits: {
            enabled: false
        }

    });
}

$(function () {
   createchart([])
});