<?php

header( 'content-type: application/json');
    $conn = mysqli_connect("localhost", "root", "", "quit_smoking");
    
    
    $query = "SELECT * FROM achievement";
    
    $result = mysqli_query($conn, $query);
    
    $json_data= array();
    while($row = mysqli_fetch_assoc($result)){
        $json_data[]=$row;
    }
    
    echo json_encode($json_data);
?>
