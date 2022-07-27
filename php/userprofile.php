<?php

if (!empty($_GET['email'])){


    $dbc = mysqli_connect('localhost', 'root', '');
    mysqli_select_db($dbc, 'quit_smoking');

    $query = "SELECT * FROM user WHERE email='{$_GET['email']}' ";

    if ($r = mysqli_query($dbc, $query)) {
		if (mysqli_num_rows($r) > 0) {
		//retrieve and print every record
			while ($row = mysqli_fetch_array($r)) {
				$response[0]['name'] = $row['name'];
				$response[0]['email'] = $row['email'];
				$response[0]['password'] = $row['password'];
				$response[0]['amt_cigarette'] = $row['amt_cigarette'];
				$response[0]['price_cigarette'] = $row['price_cigarette'];
				$response[0]['created_time'] = $row['created_time'];
			}
		}
		else {
			$response[0]['message'] = "User not found.";
		}
		
	}
	else {
		$response[0]['error'] = 1;
		$response[0]['message'] = "Fail to display because " . mysqli_error($dbc) . "The query was: " . $query;
	}

	echo  json_encode($response);
}

?>