<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

$id = mysqli_real_escape_string($conn, $_POST['ID']);
$latitude = mysqli_real_escape_string($conn, $_POST['Latitude']);
$longitude = mysqli_real_escape_string($conn, $_POST['Longitude']);

$query = "select Latitude, Longitude from study202.member where member.ID = '$id'";
$result = mysqli_query($conn, $query);
$row = mysqli_fetch_array($result);

$query = "UPDATE study202.member set member.Latitude='$latitude',member.Longitude='$longitude' where member.ID = '$id'";

$result = mysqli_query($conn, $query);

if($result){
  if($row['Latitude'] == null){
    $data_stream = "'"."GPS위치 설정"."','".$_POST['GROUPID']."','".$_POST['ID']."'";
  } else {
    $data_stream = "'"."GPS위치 변경"."','".$_POST['GROUPID']."','".$_POST['ID']."'";
  }
  $query = "insert into study202.groupboard(TITLE,GROUPID,MEMBERID) values (".$data_stream.")";
  $result = mysqli_query($conn, $query);

  echo "1";
}
else{
  echo "0";
}

mysqli_close($conn);
?>
