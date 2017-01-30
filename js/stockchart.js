$(function () {

    var stockchart = Highcharts.stockChart('stockchartcontainer', {

        chart: {
            events: {
                load: {

                }
            }
        },

        rangeSelector: {
            selected: 1
        },

        title: {
            text: ''
        },

        series: [{
            name: 'temp',
            data: [0, 0, 0, 0, 0, 0, 0, 0, 0]
        }],

        credits: {
            enabled: false
        }
    });
});