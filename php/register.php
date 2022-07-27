<?php
 
if ($_SERVER['REQUEST_METHOD']=='POST'){
 
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    $amt_cigarette = $_POST['amt_cigarette'];
    $price_cigarette = $_POST['price_cigarette'];
    date_default_timezone_set("Asia/Kuala_Lumpur");
    $date = date("Y-m-d H:i:s");
 
    $password = password_hash($password, PASSWORD_DEFAULT);

    $conn = mysqli_connect("localhost", "root", "", "quit_smoking");
 
    $sql = "INSERT INTO user (user_id, name, email, password, pro, amt_cigarette, price_cigarette, created_time) VALUES (0, '$name', '$email', '$password', '1', '$amt_cigarette', '$price_cigarette', '$date')";
 
    if (mysqli_query($conn, $sql)) {
   
        echo "Success! Congratulation your account has been register!";
        mysqli_close($conn);

    }else{

        echo "Fail to registration your account! Error: " . mysqli_error($conn);
        mysqli_close($conn);

    }
 
}
?>