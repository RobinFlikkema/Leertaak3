from api import measurements
from bottle import *

# Bottle debug mode
debug(True)


@route('/api/station')
def station():
    station = request.query.id
    date = ("" if request.query.date is "" else request.query.date)  # TODO: fixen
    time = ("" if request.query.time is "" else request.query.time)  # TODO: fixen
    limit = (20 if request.query.limit is "" else request.query.limit)
    measurements = ['hum', 'temp', 'wind']
    measurements.get(station, date, time, measurements)


@route('/api/country')
def country():
    country = request.query.country
    limit = (10 if request.query.limit is "" else request.query.limit)
    measurements = ['hum', 'temp', 'wind']
    measurements.get(station, measurements)

if __name__ == '__main__':
    run(host='localhost', port=80, debug=True, reloader=True)
