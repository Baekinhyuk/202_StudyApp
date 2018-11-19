<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

$id = mysqli_real_escape_string($conn, $_POST['ID']);
$latitude = mysqli_real_escape_string($conn, $_POST['Latitude']);
$longitude = mysqli_real_escape_string($conn, $_POST['Longitude']);

$query = "UPDATE study202.member set member.Latitude='$latitude',member.Longitude='$longitude' where member.ID = '$id'";

$result = mysqli_query($conn, $query);

if($result = mysqli_query($conn, $query)){
  echo "1";
}
else{
  echo "0";
}

mysqli_close($conn);
?>
