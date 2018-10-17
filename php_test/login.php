<?php
include "./password.php";
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

$id = mysqli_real_escape_string($conn, $_POST['ID']);
$query = "select member.ID from study202.member where member.ID = '$id'"; 

$result = mysqli_query($conn, $query);
$row = mysqli_num_rows($result);

    if($row== 1){
    $password = mysqli_real_escape_string($conn, $_POST['PASSWORD']);
    $query2 = "select member.PASSWORD from study202.member where member.ID = '$id'"; 
    $result2 = mysqli_query($conn, $query2);
    $row2 = mysqli_fetch_array($result2);
             if(password_verify($password, $row2[PASSWORD])){
		$query3 = "select member.groupID from study202.member where member.ID = '$id'";
		$result3 = mysqli_query($conn, $query3);
		$row_num = mysqli_num_rows($result3);	
		if($row_num == 1){
			$row3 = mysqli_fetch_array($result3);
			echo "$row3[groupID]";}
		else{		
			echo "-1";
		}
		}
	else{	
		echo "1";}
    }
    else{
      echo "0";
    } 
    mysqli_close($conn);
?>