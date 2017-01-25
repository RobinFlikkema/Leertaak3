from bottle import route, debug, request, error, run, json_dumps
from API import measureData

# TODO: disable debug mode in production
# Bottle debug mode
debug(True)

m = measureData.Measurements()


@route('/api/station')
def station_data():
    st_id = request.query.st_id
    time_from = 0 if request.query.time_from is "" else request.query.time_from
    time_to = 0 if request.query.time_to is "" else request.query.time_to
    limit = 20 if request.query.limit is "" else request.query.limit
    measurements = ['temp', 'wind', 'wind_dir']

    if st_id == '':
        return json_dumps({"error": {"code": "-6", "message": "Station ID missing."}})

    return json_dumps(m.get_station_data(st_id, measurements, time_from, time_to, limit))


@route('/api/stations')
def stations_data():
    station_ids = request.query.station_ids
    time_from = 0 if request.query.time_from is "" else request.query.time_from
    time_to = 0 if request.query.time_to is "" else request.query.time_to
    limit = 20 if request.query.limit is "" else request.query.limit
    measurements = ['temp', 'wind', 'wind_dir']

    if station_ids == '':
        return json_dumps({"error": {"code": "-6", "message": "Station ID missing."}})

    return json_dumps(m.get_stations_data(station_ids, measurements, time_from, time_to, limit))


@route('/api/country')
def country_data():
    name = request.query.name
    time_from = 0 if request.query.time_from is "" else request.query.time_from
    time_to = 0 if request.query.time_to is "" else request.query.time_to
    limit = 20 if request.query.limit is "" else request.query.limit
    measurements = ['temp', 'wind', 'wind_dir']

    if name == '':
        return json_dumps({"error": {"code": "-5", "message": "Country name missing."}})

    return json_dumps(m.get_country_data(name, measurements, time_from, time_to, limit))


@error(404)
def four_o_four_error(code):
    return json_dumps({"error": {"code": "-1", "message": "Invalid method."}})


if __name__ == '__main__':
    # TODO: disable debugging and reloader in production
    run(server='paste', host='127.0.0.1', port=8080, debug=True, reloader=True)
