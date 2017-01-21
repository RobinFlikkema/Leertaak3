measurements_pos = dict(temp=3, dew=4, air_station=5, air_sea=6, vis=7, wind=8, par=9, snow_fall=10, froze=11, rain=12,
                        snow=13, hail=14, tun=15, tor=16, cloud=17, wind_dir=18)
data = {}


def get_station_data(station, date, time, measurements, limit=20):
    data['station'] = station
    try:
        with open(date + ".csv", 'r', encoding='utf-8') as csv:
            for line in csv:
                value = line.strip().split(",")
                if limit:
                    counter = 0
                    while counter < 20:
                        for i in (0, len(measurements) - 1):
                            if int(value[0]) == station:
                                print(station)
                                if measurements[i] in measurements_pos.keys():
                                    data[station][value[1]][value[2]].append(value[measurements_pos[i]])
                        counter += 1
                elif date:
                    for i in (0, len(measurements) - 1):
                        if int(value[0]) == station and value[1] == date:
                            if measurements[i] in measurements_pos.keys():
                                data[station][date][value[2]].append(value[measurements_pos[i]])
                elif time:
                    for i in (0, len(measurements) - 1):
                        if int(value[0]) == station and value[2] == time:
                            if measurements[i] in measurements_pos.keys():
                                data[station][value[1]][time].append(value[measurements_pos[i]])
                elif time and date:
                    for i in (0, len(measurements) - 1):
                        if int(value[0]) == station and value[1] == date and value[2] == time:
                            if measurements[i] in measurements_pos.keys():
                                data[station][date][time].append(value[measurements_pos[i]])
    except IOError:
        # TODO: add error message to data
        pass
    return data


def get_country_data(country, limit=10, measurements=None):
    return None


if __name__ == '__main__':
    print(get_station_data(226950, 'weatherdata', time='', measurements=['temp', 'wind', 'wind_dir']))
