<?php

function readNotification() {
	$query = "SELECT * FROM notification ORDER BY date_entered DESC";
}

$action = $_GET['action'];

// connect and select
$dbc = mysqli_connect('localhost', 'root', '');
mysqli_select_db($dbc, 'quit_smoking');

switch ($action) {
	case "create":
		break;

	case "read":
		readNotification();
		break;

	case "update":
		break;

	case "delete":
		break;
}

mysqli_close($dbc); // close the database connection

?>