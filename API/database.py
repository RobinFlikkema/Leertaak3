import sqlite3


class Database:
    """ Open and close connection with database and select station data.

    This class provides functions to open and close a database connection
    and to select station data.

    """

    def __init__(self):
        # Used to define path to 'stations.db' file.
        self.prefix = "/root/API/"

    def open(self):
        """ Open a connection with the database.

        Returns:
            conn: Database connection object.
            c: Cursor database connection object.

        """
        conn = sqlite3.connect(self.prefix + 'stations.db')
        c = conn.cursor()
        return conn, c

    @staticmethod
    def close(conn):
        """ Close a connection with the database.

        Args:
            conn: Database connection object.

        """
        conn.close()

    def select_station_data(self, station_id):
        """ Select station data from stations table based on station ID.

        Args:
            station_id: ID of station.

        Returns:
            fetched_row: the information about the station.

        """
        conn, c = self.open()

        c.execute("SELECT country, latitude, longitude, name FROM stations WHERE stn = {s}"
                  .format(s=station_id))

        fetched_row = c.fetchone()

        self.close(conn)

        return fetched_row

    def select_country_data(self, country):
        """ Select station data from stations table based on country name.

        Args:
            country: Name of country.

        Returns:
            fetched_rows: the information about the stations.

        """
        conn, c = self.open()

        c.execute("SELECT stn, latitude, longitude, name FROM stations WHERE country LIKE '%{c}%'"
                  .format(c=country))

        fetched_rows = c.fetchall()

        self.close(conn)

        return fetched_rows
