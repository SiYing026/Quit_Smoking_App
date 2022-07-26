<?php 

function readOneUser($dbc) {
	$query = "SELECT * FROM user WHERE user_id = '{$_GET['user_id']}'";

	if ($r = mysqli_query($dbc, $query)) {
		//retrieve and print every record
		while ($row = mysqli_fetch_array($r)) {
			$response[0]['name'] = $row['name'];
			$response[0]['email'] = $row['email'];
			$response[0]['password'] = $row['password'];
			$response[0]['pro'] = $row['pro'];
			$response[0]['amt_cigarette'] = $row['amt_cigarette'];
			$response[0]['price_cigarette'] = $row['price_cigarette'];
		}
	}
	else {
		$response[0]['success'] = "0";
		$response[0]['message'] = "Fail to read user because " . mysqli_error($dbc) . "The query was: " . $query;
	}

	echo json_encode($response);
}

function updateUser($dbc) {
	$query = "UPDATE user 
				SET pro = '{$_POST['pro']}' 
				WHERE user_id = '{$_POST['user_id']}'";

	if (mysqli_query($dbc, $query)) {
		$response[0]['success'] = "1";
		$response[0]['message'] = "User updated successfully";
	}
	else {
		$response[0]['success'] = "0";
		$response[0]['message'] = "Fail to update user because " . mysqli_error($dbc) . "The query was: " . $query;
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
		readOneUser($dbc);
		break;

	case "readAll":
		break;

	case "update":
		updateUser($dbc);
		break;

	case "delete":
		break;
}

?>