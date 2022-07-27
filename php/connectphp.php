<?php

if ($_SERVER['REQUEST_METHOD']=='POST'){
 
    $title = $_POST['title'];
    $hint = $_POST['hint'];
    $reward = $_POST['reward'];
    
    $conn = mysqli_connect("localhost", "root", "");
    mysqli_select_db($conn, "quit_smoking");
 
    $sql = "INSERT INTO achievement (achievement_id, title, hint, reward) VALUES (0, '$title' , '$hint', '$reward')";
 
    if (mysqli_query($conn, $sql)) {
   
        echo "Success! Congratulation your account has been register!";
        mysqli_close($conn);

    }else{

        echo "Fail to registration your account!";
        mysqli_close($conn);

    }
    }//end post_add

?>