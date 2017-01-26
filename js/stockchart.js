$(document).ready(function () {
    // Create the chart
    Highcharts.stockChart('stockchartcontainer', {

        rangeSelector: {
            selected: 1
        },

        title: {
            text: ""
        },

        series: [{
            name: 'AAPL',
            data: 0,
            tooltip: {
                valueDecimals: 2
            }
        }],

        credits: {
            enabled: false
        }
    });
});