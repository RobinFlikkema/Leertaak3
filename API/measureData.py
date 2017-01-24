from API import database
import datetime


class Measurements:

    def __init__(self):
        self.measurements_pos = dict(temp=2, dew=3, air_station=4, air_sea=5, vis=6, wind=7, par=8, snow_fall=9,
                                     froze=10, rain=11, snow=12, hail=13, tun=14, tor=15, cloud=16, wind_dir=17)
        self.db = database.Conn()
        # TODO: production value
        # self.prefix = "/home/csv-storage/"
        self.prefix = ""

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

        if time_from == 0 and time_to == 0:
            counter = 0
            while counter < limit:
                try:
                    date = datetime.datetime.timestamp(datetime.datetime.now())
                    if counter == sum(1 for line in open(self.prefix + self.to_date(date) + ".csv", 'r',
                                                         encoding='utf-8')):
                        date += 86400
                    with open(self.prefix + self.to_date(date) + ".csv", 'r', encoding='utf-8') as csv:
                        for line in reversed(list(csv)):
                            counter += 1
                            value = line.strip().split(",")
                            if int(value[0]) == station:
                                for i in range(len(measurements)):
                                    if measurements[i] in self.measurements_pos.keys():
                                        data['station'][0]['measurement'].append(
                                            {'time': value[1], 'type': measurements[i],
                                             'value': value[self.measurements_pos[measurements[i]]]}, )
                        else:
                            try:
                                open(self.prefix + self.to_date(date + 86400) + ".csv", 'r', encoding='utf-8')
                            except IOError:
                                break
                except IOError:
                    break

        elif time_from != 0 and time_to != 0:
            counter = 0
            while counter < limit:
                try:
                    date = time_from
                    if counter == sum(1 for line in open(self.prefix + self.to_date(date) + ".csv", 'r',
                                                         encoding='utf-8')):
                        date += 86400
                    if date >= time_to:
                        break
                    with open(self.prefix + self.to_date(date) + ".csv", 'r', encoding='utf-8') as csv:
                        for line in reversed(list(csv)):
                            counter += 1
                            value = line.strip().split(",")
                            if int(value[0]) == station:
                                for i in range(len(measurements)):
                                    if measurements[i] in self.measurements_pos.keys():
                                        data['station'][0]['measurement'].append(
                                            {'time': value[1], 'type': measurements[i],
                                             'value': value[self.measurements_pos[measurements[i]]]}, )
                        else:
                            try:
                                open(self.prefix + self.to_date(date + 86400) + ".csv", 'r', encoding='utf-8')
                            except IOError:
                                break
                except IOError:
                    break

        elif time_from != 0:
            counter = 0
            while counter < limit:
                try:
                    date = time_from
                    if counter == sum(1 for line in open(self.prefix + self.to_date(date) + ".csv", 'r',
                                                         encoding='utf-8')):
                        date += 86400
                    with open(self.prefix + self.to_date(date) + ".csv", 'r', encoding='utf-8') as csv:
                        for line in reversed(list(csv)):
                            counter += 1
                            value = line.strip().split(",")
                            if int(value[0]) == station:
                                for i in range(len(measurements)):
                                    if measurements[i] in self.measurements_pos.keys():
                                        data['station'][0]['measurement'].append(
                                            {'time': value[1], 'type': measurements[i],
                                             'value': value[self.measurements_pos[measurements[i]]]}, )
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

    def get_stations_data(self, measurements, limit, station_ids=None):
        # TODO: do we need time_from and time_to?
        if station_ids:

            stations = []

            for st_id in station_ids:
                check = self.db.select_station_data(st_id)

                if check is None:
                    # TODO: maybe return error
                    station_ids.remove(st_id)

            data = {'station': []}

            counter = 0
            while counter < limit:
                try:
                    date = datetime.datetime.timestamp(datetime.datetime.now())
                    if counter == sum(1 for line in open(self.prefix + self.to_date(date) + ".csv", 'r',
                                                         encoding='utf-8')):
                        date += 86400
                    with open(self.prefix + self.to_date(date) + ".csv", 'r', encoding='utf-8') as csv:
                        for line in reversed(list(csv)):
                            counter += 1
                            value = line.strip().split(",")
                            for i in range(len(measurements)):
                                for j in range(len(station_ids)):
                                    if int(value[0]) == station_ids[j]:
                                        if value[0] not in stations:
                                            station_data = self.db.select_station_data(value[0])
                                            data['station'].append(
                                                {'id': value[0], 'longitude': '{}'.format(station_data[1]),
                                                 'latitude': '{}'.format(station_data[2]), 'measurement': []})
                                            stations.append(value[0])
                                        if measurements[i] in self.measurements_pos.keys():
                                            for k in (range(len(data['station']))):
                                                if data['station'][k]['id'] == value[0]:
                                                    data['station'][k]['measurement'].append(
                                                        {'time': value[1], 'type': measurements[i],
                                                         'value': value[self.measurements_pos[measurements[i]]]}, )
                        else:
                            try:
                                open(self.prefix + self.to_date(date + 86400) + ".csv", 'r', encoding='utf-8')
                            except IOError:
                                break
                except IOError:
                    break

        else:
            stations = []
            data = {'station': []}

            counter = 0
            while counter < limit:
                try:
                    date = datetime.datetime.timestamp(datetime.datetime.now())
                    if counter == sum(1 for line in open(self.prefix + self.to_date(date) + ".csv", 'r',
                                                         encoding='utf-8')):
                        date += 86400
                    with open(self.prefix + self.to_date(date) + ".csv", 'r', encoding='utf-8') as csv:
                        for line in reversed(list(csv)):
                            counter += 1
                            value = line.strip().split(",")
                            for i in range(len(measurements)):
                                if value[0] not in stations:
                                    station_data = self.db.select_station_data(value[0])
                                    data['station'].append(
                                        {'id': value[0], 'longitude': '{}'.format(station_data[1]),
                                         'latitude': '{}'.format(station_data[2]), 'measurement': []})
                                    stations.append(value[0])
                                if measurements[i] in self.measurements_pos.keys():
                                    for k in (range(len(data['station']))):
                                        if data['station'][k]['id'] == value[0]:
                                            data['station'][k]['measurement'].append(
                                                {'time': value[1], 'type': measurements[i],
                                                 'value': value[self.measurements_pos[measurements[i]]]}, )
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

    def get_country_data(self, country, measurements, limit):
        stations_data = self.db.select_country_data(country)

        if stations_data is None:
            return {"error": {"code": "-4", "message": "Invalid country."}}

        stations = []
        data = {'station': []}

        counter = 0
        while counter < limit:
            try:
                date = datetime.datetime.timestamp(datetime.datetime.now())
                if counter == sum(1 for line in open(self.prefix + self.to_date(date) + ".csv", 'r', encoding='utf-8')):
                    date += 86400
                with open(self.prefix + self.to_date(date) + ".csv", 'r', encoding='utf-8') as csv:
                    for line in reversed(list(csv)):
                        counter += 1
                        value = line.strip().split(",")
                        for i in range(len(measurements)):
                            for j in range(len(stations_data)):
                                if int(value[0]) == stations_data[j][0]:
                                    if value[0] not in stations:
                                        data['station'].append(
                                            {'id': value[0], 'longitude': '{}'.format(stations_data[j][1]),
                                             'latitude': '{}'.format(stations_data[j][2]), 'measurement': []})
                                        stations.append(value[0])
                                    if measurements[i] in self.measurements_pos.keys():
                                        for k in (range(len(data['station']))):
                                            if data['station'][k]['id'] == value[0]:
                                                data['station'][k]['measurement'].append(
                                                    {'time': value[1], 'type': measurements[i],
                                                     'value': value[self.measurements_pos[measurements[i]]]}, )
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


# TODO: remove this in production
if __name__ == '__main__':
    m = Measurements()
    # print(m.get_station_data(station=914900, time_from=0, time_to=0,
    #                          measurements=['temp', 'wind_dir', 'wind'], limit=20))
    # print(m.get_country_data(country='New Zealand', measurements=['temp', 'wind_dir', 'wind'], limit=20))
    print(m.get_stations_data(measurements=['temp', 'wind_dir', 'wind'], limit=20))
