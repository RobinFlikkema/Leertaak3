from bottle import json_dumps, error


class HTTPError:
    """ Definitions for HTTP error codes.

    This class is being used to provide methods to return error messages attached to specific HTTP error codes.

    """

    @error(404)
    def four_o_four_error(_):
        """ Return JSON formatted error when the 404 Not Found error is being raised (calls to non-existent methods).

        Returns:
            JSON formatted error.

        """
        return json_dumps({"error": {"code": "-1", "message": "Invalid method."}})
