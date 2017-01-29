<?php
/**
 * Change the password of a user in a SQLite database. The new password can be used to access the client dashboard and the API.
 */
    
	class loginCredentialsDB extends SQLite3
    {
        function __construct()
        {
            $this->open('/var/www/login_credentials.db');
            $this->busyTimeout(1000);
        }
    }

    $db = new loginCredentialsDB();

    $result = $db->query("SELECT password FROM login WHERE username='{$_SERVER['PHP_AUTH_USER']}'");
    $old_password_from_db = $result->fetchArray();

    if ($old_password_from_db[0] == $_SERVER['PHP_AUTH_PW']) {
            $result = $db->exec("UPDATE login SET password='{$_POST['NEW_AUTH_PW']}'");
    }
    $db->close();
    echo $result;