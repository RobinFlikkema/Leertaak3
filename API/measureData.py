measurements_pos = dict(temp=3, dew=4, air_station=5, air_sea=6, vis=7, wind=8, par=9, snow_fall=10, froze=11, rain=12,
                        snow=13, hail=14, tun=15, tor=16, cloud=17, wind_dir=18)
measurements_values = {}


def get_stationdata(station, date, time, measurements, limit):
    try:
        with open(date + ".csv", 'r', encoding='utf-8') as csv:
            for line in csv:
                values = line.strip().split(",")
                if limit:
                    counter = 0
                    while counter < limit:
                        for i in (0, measurements - 1):
                            if values[0] == station and measurements[i] in measurements_pos.keys():
                                # TODO: add to measurements_values
                    counter += 1
                else:
                    for i in (0, measurements - 1):
                        if values[0] == station and measurements[i] in measurements_pos.keys():
                            # TODO: add to measurements_values
    except IOError:
        return 'Error', 'Data for the defined date is not available.'


def get_countrydata(country, limit, measurements):
    return None
