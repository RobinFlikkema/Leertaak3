from API import database
import datetime


class Measurements:

    def __init__(self):
        self.measurements_pos = dict(temp=2, dew=3, air_station=4, air_sea=5, vis=6, wind=7, par=8, snow_fall=9,
                                     froze=10, rain=11, snow=12, hail=13, tun=14, tor=15, cloud=16, wind_dir=17)
        self.db = database.Database()
        # TODO: production value
        self.prefix = "/home/csv-storage/"
        # self.prefix = ""

    @staticmethod
    def to_date(timestamp):
        return datetime.datetime.fromtimestamp(timestamp).strftime('%Y-%m-%d')

    def get_station_data(self, station, measurements, time_from, time_to, limit):
        station_data = self.db.select_station_data(station)

        if station_data is None:
            return {"error": {"code": "-3", "message": "Invalid station ID."}}

        data = {'station': []}
        data['station'].append({'id': '{}'.format(station), 'longitude': '{}'.format(station_data[1]),
                                'latitude': '{}'.format(station_data[2]), 'measurement': []})

        if time_from != 0:
            date = time_from
        else:
            date = datetime.datetime.timestamp(datetime.datetime.now())

        file_counter = 0
        while True:
            try:
                if file_counter == sum(1 for _ in open(self.prefix + self.to_date(date) + ".csv", 'r',
                                                       encoding='utf-8')):
                    date += 86400
                    file_counter = 0
                with open(self.prefix + self.to_date(date) + ".csv", 'r', encoding='utf-8') as csv:
                    for line in reversed(list(csv)):
                        file_counter += 1
                        value = line.strip().split(",")
                        if int(value[1]) > time_to != 0:
                            return data
                        elif int(value[1]) >= time_from:
                            if int(value[0]) == station:
                                for i in range(len(measurements)):
                                    if measurements[i] in self.measurements_pos.keys():
                                        if not len(data['station'][0]['measurement']) == limit * len(measurements):
                                            data['station'][0]['measurement'].append(
                                                {'time': value[1], 'type': measurements[i],
                                                 'value': value[self.measurements_pos[measurements[i]]]}, )
                                        else:
                                            return data
                    else:
                        try:
                            open(self.prefix + self.to_date(date + 86400) + ".csv", 'r', encoding='utf-8')
                        except IOError:
                            break
            except IOError:
                break

        if not data['station'][0]['measurement']:
            return {"error": {"code": "-2", "message": "No data available."}}
        else:
            return data

    def get_stations_data(self, measurements, time_from, time_to, limit, station_ids=None):
        stations = []
        data = {'station': []}

        if station_ids:

            for st_id in station_ids:
                check = self.db.select_station_data(st_id)

                if check is None:
                    # TODO: maybe return error
                    station_ids.remove(st_id)

        if time_from != 0:
            date = time_from
        else:
            date = datetime.datetime.timestamp(datetime.datetime.now())

        complete = 0
        file_counter = 0
        while True:
            try:
                if file_counter == sum(1 for _ in open(self.prefix + self.to_date(date) + ".csv", 'r',
                                                       encoding='utf-8')):
                    date += 86400
                    file_counter = 0
                with open(self.prefix + self.to_date(date) + ".csv", 'r', encoding='utf-8') as csv:
                    for line in reversed(list(csv)):
                        file_counter += 1
                        value = line.strip().split(",")
                        if int(value[1]) > time_to != 0:
                            return data
                        elif int(value[1]) >= time_from:
                            if station_ids:
                                for j in range(len(station_ids)):
                                    if int(value[0]) in station_ids:
                                        if value[0] not in stations:
                                            station_data = self.db.select_station_data(value[0])
                                            data['station'].append(
                                                {'id': value[0], 'longitude': '{}'.format(station_data[1]),
                                                 'latitude': '{}'.format(station_data[2]), 'measurement': []})
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
                                                if complete == len(data['station']):
                                                    return data
                                                complete += 1
                                        except ValueError:
                                            continue
                            else:
                                if value[0] not in stations:
                                    station_data = self.db.select_station_data(value[0])
                                    data['station'].append(
                                        {'id': value[0], 'longitude': '{}'.format(station_data[1]),
                                         'latitude': '{}'.format(station_data[2]), 'measurement': []})
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
                                                if complete == len(data['station']):
                                                    return data
                                                complete += 1
                                        except ValueError:
                                            continue
                    else:
                        try:
                            open(self.prefix + self.to_date(date + 86400) + ".csv", 'r', encoding='utf-8')
                        except IOError:
                            break
            except IOError:
                break

        if not data['station']:
            return {"error": {"code": "-2", "message": "No data available."}}
        else:
            return data

    def get_country_data(self, country, measurements, time_from, time_to, limit):
        stations_data = self.db.select_country_data(country)

        if not stations_data:
            return {"error": {"code": "-4", "message": "Invalid country."}}

        stations = []
        data = {'station': []}

        if time_from != 0:
            date = time_from
        else:
            date = datetime.datetime.timestamp(datetime.datetime.now())

        complete = 0
        file_counter = 0
        while True:
            try:
                if file_counter == sum(1 for _ in open(self.prefix + self.to_date(date) + ".csv", 'r',
                                                       encoding='utf-8')):
                    date += 86400
                    file_counter = 0
                with open(self.prefix + self.to_date(date) + ".csv", 'r', encoding='utf-8') as csv:
                    for line in reversed(list(csv)):
                        file_counter += 1
                        value = line.strip().split(",")
                        if int(value[1]) > time_to != 0:
                            return data
                        elif int(value[1]) >= time_from:
                            for j in range(len(stations_data)):
                                if int(value[0]) == stations_data[j][0]:
                                    if int(value[0]) not in stations:
                                        data['station'].append(
                                            {'id': stations_data[j][0], 'longitude': '{}'.format(stations_data[j][1]),
                                             'latitude': '{}'.format(stations_data[j][2]), 'measurement': []})
                                        stations.append(stations_data[j][0])
                            for i in range(len(measurements)):
                                if measurements[i] in self.measurements_pos.keys():
                                    try:
                                        stn = stations.index(int(value[0]))
                                        if not len(data['station'][stn]['measurement']) == limit * len(measurements):
                                            data['station'][stn]['measurement'].append(
                                                {'time': value[1], 'type': measurements[i],
                                                 'value': value[self.measurements_pos[measurements[i]]]}, )
                                        else:
                                            if complete == len(data['station']):
                                                return data
                                            complete += 1
                                    except ValueError:
                                        continue
                    else:
                        try:
                            open(self.prefix + self.to_date(date + 86400) + ".csv", 'r', encoding='utf-8')
                        except IOError:
                            break
            except IOError:
                break

        if not data['station']:
            return {"error": {"code": "-2", "message": "No data available."}}
        else:
            return data
