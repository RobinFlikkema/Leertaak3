from API import measureData
from bottle import *

# Bottle debug mode
# TODO: disable debug mode in production
debug(True)


@route('/api/station')
def station_data():
    station = request.query.id
    date = request.query.date
    time = request.query.time
    limit = request.query.limit
    measurements = ['temp', 'wind', 'wind_dir']
    return measureData.get_station_data(station, date, time, limit, measurements)


@route('/api/country')
def country_data():
    country = request.query.country
    limit = request.query.limit
    measurements = ['temp', 'wind', 'wind_dir']
    return measureData.get_country_data(country, limit, measurements)


@error(404)
def four_o_four_error(code):
    return '{"error": {"code": "404", "message": "Invalid method"}}'


if __name__ == '__main__':
    # TODO: change variables to production values
    run(host='localhost', port=80, debug=True, reloader=True)
