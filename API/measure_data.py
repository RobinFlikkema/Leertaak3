from file_read_backwards import FileReadBackwards
from API import database
import datetime


class Measurements:
    """ Search for, collect and return measurement data.

    This class is being used to provide functions to search through stored CSV files and collect measurement data
    based on parameters defined in the API calls.

    """

    def __init__(self):
        # Initialize class with dictionary which contains positions for measurement in the CSV files, a database object
        # and a prefix to the path where the CSV files are stored within the system.
        self.measurements_pos = dict(temp=2, dew=3, air_station=4, air_sea=5, vis=6, wind=7, pre=8, snow_fall=9,
                                     froze=10, rain=11, snow=12, hail=13, tun=14, tor=15, cloud=16, wind_dir=17)
        self.db = database.Database()
        # Used to define the path to the CSV storage location.
        self.prefix = "/home/csv-storage/"
        # Used to define the path to were the downloadable CSV files are stored.
        self.csv_store = "/root/API/csv_storage/"

    @staticmethod
    def to_date(timestamp):
        """ Convert UNIX timestamp to date.

        Args:
            timestamp: the UNIX timestamp to be converted.

        Returns:
            The UNIX timestamp converted to a date (format: YYYY-MM-DD)

        """
        return datetime.datetime.fromtimestamp(timestamp).strftime('%Y-%m-%d')

    @staticmethod
    def to_time(timestamp):
        """ Convert UNIX timestamp to time.

        Args:
            timestamp: the UNIX timestamp to be converted.

        Returns:
            The UNIX timestamp converted to a time (format: )

        """
        return datetime.datetime.fromtimestamp(timestamp).strftime('%H:%M:%S')

    def get_station_data(self, station, measurements, time_from, time_to, limit):
        """ Collect measurement data for the defined station.

        This method is being used to collect data for a defined station from one or more CSV files.
        The method returns a JSON string containing information about the station and the measurements.

        Args:
            station: the ID of the station to collect the measurements from.
            measurements: a list of measurements to collect data for.
            time_from: a UNIX timestamp which is used to define the start of the time period within which
            measurement data has to be collected.
            time_to: a UNIX timestamp which is used to define the end of the time period within which
            measurement data has to be collected.
            limit: the maximum amount of measurements to be collected.

        Returns:
            Error when a non-existent station ID was provided.
            Error when no measurement data is available.
            Collected measurement data when no errors occurred.

        Raises:
            IOError: when a CSV file is not present, an IOError occurs.

        """

        # Collect information about the station from the database.
        station_data = self.db.select_station_data(station)

        if station_data is None:
            return {"error": {"code": "-3", "message": "Invalid station ID."}}

        # Add the information to the station JSON object.
        data = {'station': []}
        data['station'].append({'id': '{}'.format(station), 'longitude': '{}'.format(station_data[1]),
                                'latitude': '{}'.format(station_data[2]), 'name': '{}'.format(station_data[3]),
                                'measurement': []})

        # If time_from is given, use the defined timestamp as a start. Otherwise use date from today.
        if time_from != 0:
            date = time_from
        else:
            date = datetime.datetime.timestamp(datetime.datetime.now())

        while True:
            try:
                with open(self.prefix + self.to_date(date) + ".csv", 'r', encoding='utf-8') as csv:
                    # Search CSV in reversed order to start with collecting the most recently added measurements.
                    for line in csv:
                        value = line.strip().split(",")
                        # If time_to is reached, return data.
                        if int(value[1]) > time_to != 0:
                            if not data['station'][0]['measurement']:
                                return {"error": {"code": "-2", "message": "No data available."}}
                            else:
                                return data
                        elif int(value[1]) >= time_from:
                            # If stn in CSV matches station, continue.
                            if int(value[0]) == station:
                                for i in range(len(measurements)):
                                    # If measurement in measurements_pos dictionary, add new measurement JSON object.
                                    if measurements[i] in self.measurements_pos.keys():
                                        # If amount of measurement values collected equals the defined limit times the
                                        # amount of measurements to collect data from, return the data. Else continue.
                                        if not len(data['station'][0]['measurement']) == limit * len(measurements):
                                            data['station'][0]['measurement'].append(
                                                {'time': value[1], 'type': measurements[i],
                                                 'value': value[self.measurements_pos[measurements[i]]]}, )
                                        else:
                                            return data
                    else:
                        try:
                            # Test if next is present.
                            open(self.prefix + self.to_date(date + 86400) + ".csv", 'r', encoding='utf-8')
                            # Increase date by one day if no exception
                            date += 86400
                        except IOError:
                            # When IOError is being raised, stop with searching.
                            break
            except IOError:
                # When IOError is being raised, stop with searching.
                break

        # If no measurements collected, return error.
        if not data['station'][0]['measurement']:
            return {"error": {"code": "-2", "message": "No data available."}}
        else:
            return data

    def get_stations_data(self, measurements, time_from, time_to, limit, stn_limit, station_ids=None):
        """ Collect measurement data for the defined stations.

        This method is being used to collect data for a defined stations from one or more CSV files.
        The station_ids parameter is not mandatory. When not defined, data for all stations defined in the CSV files
        is being collected.
        The method returns a JSON string containing information about the stations and the measurements.

        Args:
            measurements: a list of measurements to collect data for.
            time_from: a UNIX timestamp which is used to define the start of the time period within which
            measurement data has to be collected.
            time_to: a UNIX timestamp which is used to define the end of the time period within which
            measurement data has to be collected.
            limit: the maximum amount of measurements to be collected.
            stn_limit: the maximum amount of different stations to collect the measurements from.
            station_ids: a list of IDs of stations to collect the measurements from.

        Returns:
            Error when no measurement data is available.
            Collected measurement data when no errors occurred.

        Raises:
            ValueError: when a station is not present in stations list, a ValueError occurs.
            IOError: when a CSV file is not present, an IOError occurs.

        """
        stations = []
        data = {'station': []}

        if station_ids:
            # Check for every station ID is ID is valid. When not valid, remove from station_ids list.
            for st_id in station_ids:
                check = self.db.select_station_data(st_id)

                if check is None:
                    station_ids.remove(st_id)

            for i in range(len(station_ids)):
                # Append new station object if not present in stations list.
                station_data = self.db.select_station_data(station_ids[i])
                data['station'].append(
                    {'id': station_ids[i], 'longitude': '{}'.format(station_data[1]),
                     'latitude': '{}'.format(station_data[2]), 'name':
                         '{}'.format(station_data[3]), 'measurement': []})
                stations.append(station_ids[i])

        if time_from != 0:
            date = time_from
        else:
            date = datetime.datetime.timestamp(datetime.datetime.now())

        complete = 0
        while True:
            try:
                with open(self.prefix + self.to_date(date) + ".csv", 'r', encoding='utf-8') as csv:
                    for line in csv:
                        value = line.strip().split(",")
                        if int(value[1]) > time_to != 0:
                            if not data['station']:
                                return {"error": {"code": "-2", "message": "No data available."}}
                            else:
                                return data
                        elif int(value[1]) >= time_from:
                            if station_ids:
                                for i in range(len(measurements)):
                                    if measurements[i] in self.measurements_pos.keys():
                                        try:
                                            # Check if stn from CSV is present in stations list and return the index
                                            stn = stations.index(value[0])
                                            if not len(data['station'][stn]['measurement']) == limit * len(
                                                    measurements):
                                                data['station'][stn]['measurement'].append(
                                                    {'time': value[1], 'type': measurements[i],
                                                     'value': value[self.measurements_pos[measurements[i]]]}, )
                                            else:
                                                # If all the defined stations have the amount of measurements they need
                                                # attached to them, return the data. Else continue.
                                                if complete == len(data['station']):
                                                    return data
                                                complete += 1
                                        except ValueError:
                                            # Continue when ValueError occurs.
                                            continue
                            else:
                                if value[0] not in stations:
                                    station_data = self.db.select_station_data(value[0])
                                    data['station'].append(
                                        {'id': value[0], 'longitude': '{}'.format(station_data[1]),
                                         'latitude': '{}'.format(station_data[2]), 'name': '{}'.format(station_data[3]),
                                         'measurement': []})
                                    stations.append(value[0])
                                for i in range(len(measurements)):
                                    if measurements[i] in self.measurements_pos.keys():
                                        try:
                                            stn = stations.index(value[0])
                                            if not len(data['station'][stn]['measurement']) == limit * len(
                                                    measurements):
                                                data['station'][stn]['measurement'].append(
                                                    {'time': value[1], 'type': measurements[i],
                                                     'value': value[self.measurements_pos[measurements[i]]]}, )
                                            else:
                                                # When amount of station objects == stn_limit, return the data.
                                                # Else continue.
                                                if stn_limit == len(data['station']):
                                                    return data
                                        except ValueError:
                                            continue
                    else:
                        try:
                            open(self.prefix + self.to_date(date + 86400) + ".csv", 'r', encoding='utf-8')
                            date += 86400
                        except IOError:
                            break
            except IOError:
                break

        if not data['station']:
            return {"error": {"code": "-2", "message": "No data available."}}
        else:
            return data

    def get_country_data(self, country, measurements, time_from, time_to, limit):
        """ Collect measurement data for the stations present in the defined country.

        This method is being used to collect data for stations present in the defined country
        from one or more CSV files.
        The method returns a JSON string containing information about the stations and the measurements.

        Args:
            country: the name of the country.
            measurements: a list of measurements to collect data for.
            time_from: a UNIX timestamp which is used to define the start of the time period within which
            measurement data has to be collected.
            time_to: a UNIX timestamp which is used to define the end of the time period within which
            measurement data has to be collected.
            limit: the maximum amount of measurements to be collected.

        Returns:
            Error when no measurement data is available.
            Collected measurement data when no errors occurred.

        Raises:
            ValueError: when a station is not present in stations list, a ValueError occurs.
            IOError: when a CSV file is not present, an IOError occurs.

        """

        # Fetch information about the stations present in the defined country from the database.
        stations_data = self.db.select_country_data(country)

        # If no information from stations fetched, return error.
        if not stations_data:
            return {"error": {"code": "-4", "message": "Invalid country."}}

        stations = []
        data = {'station': []}

        for i in range(len(stations_data)):
            if int(stations_data[i][0]) not in stations:
                data['station'].append(
                    {'id': stations_data[i][0], 'longitude': '{}'.format(stations_data[i][1]),
                     'latitude': '{}'.format(stations_data[i][2]),
                     'name': '{}'.format(stations_data[i][3]), 'measurement': []})
                stations.append(stations_data[i][0])

        if time_from != 0:
            date = time_from
        else:
            date = datetime.datetime.timestamp(datetime.datetime.now())

        complete = 0
        while True:
            try:
                csv = FileReadBackwards(self.prefix + self.to_date(date) + ".csv", encoding="utf-8")
                for line in csv:
                    value = line.strip().split(",")
                    if int(value[1]) > time_to != 0:
                        if not data['station']:
                            return {"error": {"code": "-2", "message": "No data available."}}
                        else:
                            return data
                    elif int(value[1]) >= time_from:
                        for i in range(len(measurements)):
                            if measurements[i] in self.measurements_pos.keys():
                                try:
                                    stn = stations.index(int(value[0]))
                                    if not len(data['station'][stn]['measurement']) == limit * len(measurements):
                                        data['station'][stn]['measurement'].append(
                                            {'time': value[1], 'type': measurements[i],
                                             'value': value[self.measurements_pos[measurements[i]]]}, )
                                    else:
                                        # When all stations have the requested amount of measurements, return data.
                                        # Else continue.
                                        if complete == len(data['station']):
                                            return data
                                        complete += 1
                                except ValueError:
                                    continue
                else:
                    try:
                        open(self.prefix + self.to_date(date + 86400) + ".csv", 'r', encoding='utf-8')
                        date += 86400
                    except IOError:
                        break
            except IOError:
                break

        if not data['station']:
            return {"error": {"code": "-2", "message": "No data available."}}
        else:
            return data

    def download(self, country, measurements, limit):
        """ Collect measurement data for the stations present in the defined country and return a CSV file.

        This method is being used to collect data for stations present in the defined country
        from one or more CSV files.
        The method returns a CSV file containing information about the stations and the measurements.

        Args:
            country: the name of the country.
            measurements: a list of measurements to collect data for.
            limit: the maximum amount of measurements to be collected.

        Returns:
            Error when no measurement data is available.
            CSV file when no errors occurred.

        Raises:
            ValueError: when a station is not present in stations list, a ValueError occurs.
            IOError: when a CSV file is not present, an IOError occurs.

        """
        stations_data = self.db.select_country_data(country)

        if not stations_data:
            return {"error": {"code": "-4", "message": "Invalid country."}}

        # A dictionary containing the names of the different measurements.
        measurements_name = dict(temp="Temperature (°C)", dew="Dew point (°C)",
                                 air_station="Air pressure station level (mBAR)",
                                 air_sea="Air pressure sea level (mBAR)", vis="Visibility (km)",
                                 wind="Wind speed (km/h)", pre="Precipitation (cm)", snow_fall="Snow fall (cm)",
                                 froze="Frozen", rain="Rain", snow="Snow", hail="Hail", tun="Thunder", tor="Tornado",
                                 cloud="Cloudiness (%)", wind_dir="Wind direction (°)")

        stations = {}

        for j in range(len(stations_data)):
            stations[(stations_data[j][0])] = 0

        date = datetime.datetime.timestamp(datetime.datetime.now())
        download_csv = open(self.csv_store + 'measurements.csv', 'w', encoding="utf-8")

        # Write the head to the CSV file.
        csv_head = ''
        for i in range(len(measurements)):
            if i == len(measurements) - 1:
                csv_head += '"' + measurements_name[measurements[i]] + '"'
            else:
                csv_head += '"' + measurements_name[measurements[i]] + '",'

        download_csv.write('"Station name",' + '"Date",' + '"Time",' + csv_head)

        new_line = ''
        complete = 0
        counter = 0
        while True:
            try:
                download_csv = open(self.csv_store + 'measurements.csv', 'a', encoding="utf-8")
                csv = FileReadBackwards(self.prefix + self.to_date(date) + ".csv", encoding="utf-8")
                for line in csv:
                    value = line.strip().split(",")
                    if int(value[0]) in stations.keys():
                        # Add a new line containing the station name and the date and time of the measurement.
                        station_data = self.db.select_station_data(value[0])
                        new_line += '"' + station_data[3] + '",' + '"' + self.to_date(int(value[1])) + '",' + \
                                    self.to_time(int(value[1])) + '",'
                        for i in range(len(measurements)):
                            if measurements[i] in self.measurements_pos.keys():
                                if not stations[int(value[0])] == limit * len(measurements):
                                    if self.measurements_pos[measurements[i]] in range(10, 15):
                                        if value[self.measurements_pos[measurements[i]]] == 1:
                                            measurement_value = "yes"
                                        else:
                                            measurement_value = "no"
                                    else:
                                        measurement_value = value[self.measurements_pos[measurements[i]]]
                                    # Insert a new measurement.
                                    if i == len(measurements) - 1:
                                        new_line += '"' + measurement_value + '"'
                                    else:
                                        new_line += '"' + measurement_value + '",'
                                    stations[int(value[0])] += 1
                                else:
                                    if complete == len(stations):
                                        return True
                                    complete += 1
                        # Write line to CSV file.
                        download_csv.write("\n" + new_line)
                        new_line = ''
                else:
                    try:
                        open(self.prefix + self.to_date(date + 86400) + ".csv", 'r', encoding='utf-8')
                        date += 86400
                    except IOError:
                        break
            except IOError:
                break

        download_csv.close()

        if not stations:
            return {"error": {"code": "-2", "message": "No data available."}}
        else:
            return True
