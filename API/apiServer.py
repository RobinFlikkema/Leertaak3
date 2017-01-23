from bottle import route, debug, request, error, run, json_dumps
from API import measureData

# Bottle debug mode
# TODO: disable debug mode in production
debug(True)

m = measureData.Measurements()


@route('/station')
def station_data():
    station = request.query.id
    time_from = request.query.time_from
    time_to = request.query.time_to
    limit = request.query.limit
    measurements = ['temp', 'wind', 'wind_dir']
    return json_dumps(m.get_station_data(station, time_from, time_to, limit, measurements))


@route('/country')
def country_data():
    country = request.query.country
    limit = request.query.limit
    measurements = ['temp', 'wind', 'wind_dir']
    return json_dumps(m.get_country_data(country, limit, measurements))


@error(404)
def four_o_four_error(code):
    return json_dumps({"error": {"code": "-1", "message": "Invalid method."}})


if __name__ == '__main__':
    # TODO: change variables to production values
    run(server='paste', host='127.0.0.1', port=80, debug=True, reloader=True)
