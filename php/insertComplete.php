<?php

if ($_SERVER['REQUEST_METHOD']=='POST'){
 
    $achievementid = $_POST['achievementid'];
    $userid = $_POST['userid'];
    
    $conn = mysqli_connect("localhost", "root", "");
    mysqli_select_db($conn, "quit_smoking");
 
    $sql = "INSERT INTO userachievement ( user_id, achievement_id) "
            . "VALUES ( '$userid', '$achievementid')";
 
    if (mysqli_query($conn, $sql)) {
  
        echo "Success! Congratulation your account has been register!";
        mysqli_close($conn);

    }else{

        echo "Fail to registration your account!";
        mysqli_close($conn);

    }
    }//end post_add

?>