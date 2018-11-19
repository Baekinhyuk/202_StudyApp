<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

$id = mysqli_real_escape_string($conn, $_POST['ID']);
$query = "select member.Latitude,member.Longitude from study202.member where member.ID = '$id'";

$result = mysqli_query($conn, $query);

    if(empty($row[Latitude]) || empty($row[Longitude])){
	echo "-1";
    }	
    else{
	$row = mysqli_fetch_array($result);
	echo "{";
	echo "\"GPS_state\":";
	echo "[";
	echo "{";
	echo "\"Latitude\":\"$row[Latitude]\",\"Longitude\":\"$row[Longitude]\"";
	echo "}";
	echo "]";
	echo "}";
    }
    
    mysqli_close($conn);
?>
