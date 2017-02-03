from gevent import monkey; monkey.patch_all()
from bottle import debug, request, json_dumps, run, default_app, response, static_file
from API import measure_data, error
import daemon
import lockfile
import signal
import os

# Bottle debug mode
debug(False)


class ApiServer:
    """ Control API server and provide API functionality.

    This class provides functions to control the API server by starting it and to provide a way to cleanly shutdown the
    server. This class also provides functions to route request to the appropriate methods defined in other classes.

    """
    def __init__(self):
        # Initialize class with Measurement object, define the Bottle app to be used, attach routes to Bottle app
        # and create HTTPError object.
        self.m = measure_data.Measurements()
        self.app = default_app()
        self.routes()
        error.HTTPError()
        # Used to define the path to the storage location of the downloadable CSV files.
        self.csv_store = "/root/API/csv_storage/"

    @staticmethod
    def cleanup():
        """ Remove leftover PID file if present

        When the server crashes, the PID file may not have been removed. When this is the case, remove the PID file.

        Raises:
            FileNotFoundError: the PID file is not present. Pass along when this is the case.

        """
        try:
            os.remove("/var/run/api_server.pid.lock")
        except FileNotFoundError:
            pass

    def routes(self):
        """ Attach routes to Bottle app.

        Attach the routes to the defined Bottle app.

        """
        self.app.route('/api/station', method="GET", callback=self.station_data)
        self.app.route('/api/country', method="GET", callback=self.country_data)
        self.app.route('/api/stations', method="GET", callback=self.stations_data)
        self.app.route('/api/csv', method="GET", callback=self.download_csv)

    def start(self, bottle_server='gevent', host='localhost', port=8080):
        """ Start the API server.

        Args:
            bottle_server: the type of server to use
            (see: https://bottlepy.org/docs/dev/deployment.html#switching-the-server-backend).
            host: the IP-address or hostname from the interface to which the Bottle server must be attached to.
            port: the port which the Bottle server must use.

        """

        # Remove a leftover PID file if present.
        self.cleanup()

        # Log errors to defined error log.
        log = open('/var/log/api_server.log', 'w')

        # Start server as a daemon (in background). Create a PID file.
        server_daemon = daemon.DaemonContext(pidfile=lockfile.FileLock('/var/run/api_server.pid'), stderr=log)

        # Cleanly shutdown server when SIGTERM signal is received.
        server_daemon.signal_map = {
            signal.SIGTERM: self.cleanup,
        }

        with server_daemon:
            # Start the server.
            run(server=bottle_server, host=host, port=port)

    def station_data(self):
        """ Retrieve and return measurement data from specific station.

        When the /api/station route is being called, the data from the defined station is being returned.

        Returns:
            JSON formatted error in case st_id is not defined.
            JSON formatted measurement data in case at least st_id is defined.

        """

        # Set the response Content-Type header to JSON format.
        response.content_type = 'application/json'

        st_id = request.query.st_id
        # Get time_from and time_to parameter which is used to retrieve data within a specific time period.
        time_from = 0 if request.query.time_from is "" else request.query.time_from
        time_to = 0 if request.query.time_to is "" else request.query.time_to
        # Limit the amount of measurements returned.
        limit = 20 if request.query.limit is "" else request.query.limit
        # Measurements to request data from.
        measurements = ['temp', 'wind', 'wind_dir', 'hum']

        if st_id == '':
            return json_dumps({"error": {"code": "-6", "message": "Station ID missing."}}, indent=2)

        return json_dumps(self.m.get_station_data(st_id, measurements, int(time_from), int(time_to), int(limit)),
                          indent=2)

    def stations_data(self):
        """ Retrieve and return measurement data from specific stations.

        When the /api/stations route is being called, the data from the defined stations is being returned.

        Returns:
            JSON formatted error in case station_ids is not defined.
            JSON formatted measurement data in case at least station_ids is defined.
            JSON formatted measurement data in case at least stn_limit is defined.

        """
        response.content_type = 'application/json'

        # A list of station IDs from which measurement data has to be returned.
        station_ids = request.query.station_ids
        time_from = 0 if request.query.time_from is "" else request.query.time_from
        time_to = 0 if request.query.time_to is "" else request.query.time_to
        limit = 20 if request.query.limit is "" else request.query.limit
        stn_limit = 20 if request.query.limit is "" else request.query.stn_limit
        measurements = ['temp', 'wind', 'wind_dir', 'hum']

        if station_ids == '' and request.query.stn_limit == '':
            return json_dumps({"error": {"code": "-6", "message": "Station IDs missing."}}, indent=2)

        if station_ids != '':
            ids = []
            stn_split = station_ids.strip().split(",")
            for st in stn_split:
                ids.append(st)
        else:
            ids = None

        return json_dumps(self.m.get_stations_data(measurements, int(time_from), int(time_to), int(limit),
                                                   int(stn_limit), ids), indent=2)

    def country_data(self):
        """ Retrieve and return measurement data based on country name.

        When the /api/country route is being called,
        the data from stations present within the defined country is being returned.

        Returns:
            JSON formatted error in case name is not defined.
            JSON formatted measurement data in case at least name is defined.

        """
        response.content_type = 'application/json'

        name = request.query.name
        time_from = 0 if request.query.time_from is "" else request.query.time_from
        time_to = 0 if request.query.time_to is "" else request.query.time_to
        limit = 20 if request.query.limit is "" else request.query.limit
        measurements = ['temp', 'wind', 'wind_dir', 'hum']

        if name == '':
            return json_dumps({"error": {"code": "-5", "message": "Country name missing."}}, indent=2)

        return json_dumps(self.m.get_country_data(name, measurements, int(time_from), int(time_to), int(limit)),
                          indent=2)

    def download_csv(self):
        """ Retrieve measurement data based on country name and serve a CSV file.

        When the /api/csv route is being called,
        the data from stations present within the defined country is being inserted in a CSV file and the CSV file is
        being returned.

        Returns:
            An string containing a error message when no data was found.
            A CSV file if measurement data was found.

        """
        response.content_type = 'application/json'

        limit = 20 if request.query.limit is "" else request.query.limit
        measurements = ['temp', 'wind', 'wind_dir', 'hum']

        result = self.m.download('New Zealand', measurements, int(limit))

        if result:
            return static_file("measurements.csv", root=self.csv_store, mimetype="text/csv", download=True)
        else:
            return "CSV file cannot be download based on the given parameters."

if __name__ == '__main__':
    # Create ApiServer object.
    server = ApiServer()
    # Start the server.
    server.start()
