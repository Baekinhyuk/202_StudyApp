<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

$id = mysqli_real_escape_string($conn, $_POST['ID']);
$query = "select member.ID from study202.member where member.ID = '$id'"; 

$result = mysqli_query($conn, $query);
$row = mysqli_num_rows($result);

    if($row== 1){
    $password = mysqli_real_escape_string($conn, $_POST['PASSWORD']);
    $query2 = "select member.ID,member.PASSWORD from study202.member where member.ID = '$id' and member.PASSWORD = '$password'"; 
    $result2 = mysqli_query($conn, $query2);
    $row2 = mysqli_num_rows($result2);
             if($row2 ==1){
		echo "2";}
	else{	
		echo "1";}
    }
    else{
      echo "0";
    } 
    mysqli_close($conn);
?>