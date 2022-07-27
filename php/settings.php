<?php 

function readOneSetting($dbc) {
	$query = "SELECT * FROM settings WHERE setting_type = '{$_GET['setting_type']}'";

	if ($r = mysqli_query($dbc, $query)) {
		//retrieve and print every record
		while ($row = mysqli_fetch_array($r)) {
			$response[0]['setting_id'] = $row['setting_id'];
			$response[0]['setting_type'] = $row['setting_type'];
			$response[0]['setting_content'] = $row['setting_content'];
		}
	}
	else {
		$response[0]['success'] = "0";
		$response[0]['message'] = "Fail to read settings because " . mysqli_error($dbc) . "The query was: " . $query;
	}

	echo json_encode($response);
}

function updateSetting($dbc) {
	$query = "UPDATE settings 
				SET setting_content = '{$_POST['setting_content']}' 
				WHERE setting_type = '{$_GET['setting_type']}'";

	if (mysqli_query($dbc, $query)) {
		$response[0]['success'] = "1";
		$response[0]['message'] = "Settings updated successfully";
	}
	else {
		$response[0]['success'] = "0";
		$response[0]['message'] = "Fail to update settings because " . mysqli_error($dbc) . "The query was: " . $query;
	}

	echo json_encode($response);
}

//connect and select database
$dbc = mysqli_connect('localhost', 'root', '');
mysqli_select_db($dbc, 'quit_smoking');

$action = $_GET['action'];

switch ($action) {
	case "create":
		break;

	case "readOne":
		readOneSetting($dbc);
		break;

	case "readAll":
		break;

	case "update":
		updateSetting($dbc);
		break;

	case "delete":
		break;
}

?>