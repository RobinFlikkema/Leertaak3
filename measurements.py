def get(station, date, time, measurements, limit=None):
    with open(date + ".csv", 'r', encoding='utf-8') as csv:
        for line in csv:
            values = line.strip().split(",")
            if values[0] == station:
