var oceanic_stations = [914900, 931190, 932910, 933080, 934170, 934340, 934360, 935160, 935450, 936140, 938060, 938440, 938960, 939440, 939860, 939970];

$(document).ready(function () {
    function update() {
        $.ajax({
            url: 'https://vm.robinflikkema.nl/api/country?name=New+Zealand',
            type: 'get',
            dataType: 'json',
            withCredentials: true,

            success: function (data) {
                var highestspeeds = []
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
                                    var localspeeds = []
                                    for (var key in station['measurement']) {
                                        var measurement = station['measurement'][key];
                                        if (measurement['type'] == 'wind') {
                                            localspeeds.push(parseFloat(measurement['value']));
                                        }
                                    }
                                    var name = station['name'].toString();
                                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                                    highestspeeds.push([name, Math.max(...localspeeds)]);
                                }
                            }
                        }
                    }
                }
                for (i = 0; i < highestspeeds.length; i++){
                    if(!isFinite(highestspeeds[i][1])){
                        highestspeeds.splice(i,1);
                    }
                }
                highestspeeds.sort(sortFunction);
                for (i = 0; i < highestspeeds.length; i++){
                    highestspeeds[i][1] = highestspeeds[i][1] + " km/h";
                }
                createtable(highestspeeds);
            }
        });
        setTimeout(function () {
            update();
        }, 15000);
    }

    update();
});

// source: http://stackoverflow.com/a/15164958
var createtable = function (tableData) {
    try {
        var previoustable = document.getElementById('currenttable');
        previoustable.parentNode.removeChild(previoustable);
    } catch (e) {}

    var tablepanel = document.getElementById("tablecontainer");
    var table = document.createElement('table');
    table.id = "currenttable"
    table.className = 'table table-hover';
    var tableBody = document.createElement('tbody');
    var tablehead = table.createTHead();
    tablehead.className = 'thead-default';
    var head = tablehead.insertRow(0);
    var stationname = head.insertCell(0);
    var windspeed = head.insertCell(1);
    stationname.innerHTML = "<b>Cityname</b>";
    windspeed.innerHTML = "<b>Windspeed</b>";

    var count = 0;
    tableData.forEach(function (rowData) {
        count++;
        if (count <= 5) {
            var row = document.createElement('tr');
            rowData.forEach(function (cellData) {
                var cell = document.createElement('td');
                cell.appendChild(document.createTextNode(cellData));
                row.appendChild(cell);
            });

            tableBody.appendChild(row);
        }
    });

    table.appendChild(tableBody);
    tablepanel.appendChild(table);
}

// source: http://stackoverflow.com/a/16097058
var sortFunction = function (a, b) {
    if (a[1] === b[1]) {
        return 0;
    }
    else {
        return (a[1] > b[1]) ? -1 : 1;
    }
}