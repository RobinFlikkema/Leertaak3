from API import measureData, jsonReturn
from bottle import *

# Bottle debug mode
# TODO: disable debug mode in production
debug(True)


@route('/api/station')
def station_data():
    station = request.query.id
    date = ("" if request.query.date is "" else request.query.date)  # TODO: set default values
    time = ("" if request.query.time is "" else request.query.time)  # TODO: set default values
    limit = (20 if request.query.limit is "" else request.query.limit)
    measurements = ['temp', 'wind', 'wind_dir']
    return measureData.get_stationdata(station, date, time, limit, measurements)


@route('/api/country')
def country_data():
    country = request.query.country
    limit = (10 if request.query.limit is "" else request.query.limit)
    measurements = ['temp', 'wind', 'wind_dir']
    return measureData.get_countrydata(country, limit, measurements)


@error(404)
def four_o_four_error(code):
    return '{"error": {"code": "404", "message": "Invalid method"}}'


if __name__ == '__main__':
    # TODO: change variables to production values
    run(host='localhost', port=80, debug=True, reloader=True)
