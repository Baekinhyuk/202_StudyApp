<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

$id = mysqli_real_escape_string($conn, $_POST['ID']);
$query = "select member.ID from study202.member where member.ID = '$id'"; 

$result = mysqli_query($conn, $query);
$row = mysqli_num_rows($result);

    if($row== 1){
      echo "1";
    }
    else{
      echo "0";
    } 
    mysqli_close($conn);
?>