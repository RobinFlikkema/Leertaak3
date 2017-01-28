from bottle import route, debug, request, error, json_dumps, run
from API import measure_data
import daemon
import lockfile
import signal
import os

# TODO: disable debug mode in production
# Bottle debug mode
debug(True)


class ApiServer:

    def __init__(self):
        self.m = measure_data.Measurements()
        self.cleanup()

    @staticmethod
    def cleanup():
        try:
            os.remove("/var/run/api_server.pid.lock")
        except FileNotFoundError:
            pass

    @route('/api/station')
    def station_data(self):
        st_id = request.query.st_id
        time_from = 0 if request.query.time_from is "" else request.query.time_from
        time_to = 0 if request.query.time_to is "" else request.query.time_to
        limit = 20 if request.query.limit is "" else request.query.limit
        measurements = ['temp', 'wind', 'wind_dir']

        if st_id == '':
            return json_dumps({"error": {"code": "-6", "message": "Station ID missing."}})

        return json_dumps(self.m.get_station_data(st_id, measurements, time_from, time_to, limit))

    @route('/api/stations')
    def stations_data(self):
        station_ids = request.query.station_ids
        time_from = 0 if request.query.time_from is "" else request.query.time_from
        time_to = 0 if request.query.time_to is "" else request.query.time_to
        limit = 20 if request.query.limit is "" else request.query.limit
        measurements = ['temp', 'wind', 'wind_dir']

        if station_ids == '':
            return json_dumps({"error": {"code": "-6", "message": "Station ID missing."}})

        return json_dumps(self.m.get_stations_data(station_ids, measurements, time_from, time_to, limit))

    @route('/api/country')
    def country_data(self):
        name = request.query.name
        time_from = 0 if request.query.time_from is "" else request.query.time_from
        time_to = 0 if request.query.time_to is "" else request.query.time_to
        limit = 20 if request.query.limit is "" else request.query.limit
        measurements = ['temp', 'wind', 'wind_dir']

        if name == '':
            return json_dumps({"error": {"code": "-5", "message": "Country name missing."}})

        return json_dumps(self.m.get_country_data(name, measurements, time_from, time_to, limit))

    @error(404)
    def four_o_four_error(_):
        return json_dumps({"error": {"code": "-1", "message": "Invalid method."}})


if __name__ == '__main__':
    server = ApiServer()

    log = open('/var/log/api_server.log', 'a')
    server_daemon = daemon.DaemonContext(pidfile=lockfile.FileLock('/var/run/api_server.pid'), stderr=log)

    server_daemon.signal_map = {
        signal.SIGTERM: server.cleanup,
    }

    with server_daemon:
        run(server='paste', host='127.0.0.1', port=8080)
