<?php 

function createForum($dbc) {
	$query = "INSERT INTO community_forum (forum_id, title, content) VALUES (0, '{$_POST["title"]}', '{$_POST["content"]}')";
				
	if (mysqli_query($dbc, $query)) {
		$response[0]['error'] = "0";
		$response[0]['message'] = "Forum created successfully.";
	}
	else {
		$response[0]['error'] = "1";
		$response[0]['message'] = "Fail to create forum because " . mysqli_error($dbc) . "The query was: " . $query;
	}

	echo json_encode($response);
}

function readAllForum($dbc) {
	$query = "SELECT * FROM community_forum";

	if ($r = mysqli_query($dbc, $query)) {
		//retrieve and print every record
		while ($row = mysqli_fetch_array($r)) {
			$forum['forum_id'] = $row['forum_id'];
			$forum['title'] = $row['title'];
			$forum['content'] = $row['content'];
			$response[] = $forum;
		}
	}
	else {
		$response[0]['error'] = "1";
		$response[0]['message'] = "Fail to create achievement because " . mysqli_error($dbc) . "The query was: " . $query;
	}

	echo json_encode($response);

	$myfile = fopen("test888.txt", "w") or die("Unable to open file!");
    fwrite($myfile,print_r($response, true));
    fclose($myfile);
}


//connect and select database
$dbc = mysqli_connect('localhost', 'root', '');
mysqli_select_db($dbc, 'quit_smoking');

$action = $_GET['action'];

switch ($action) {
	case "create":
		createForum($dbc);
		break;

	case "readOne":
		break;

	case "readAll":
		readAllForum($dbc);
		break;

	case "update":
		break;

	case "delete":
		break;
}


?>