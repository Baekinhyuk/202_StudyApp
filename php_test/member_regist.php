<?php
include "./password.php";
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");
$hash = password_hash($_POST['PASSWORD'], PASSWORD_DEFAULT);

$data_stream = "'".$_POST['ID']."','".$hash."','".$_POST['NAME']."','".$_POST['NICKNAME']."','".$_POST['EMAIL']."','".$_POST['PHONE']."','".$_POST['BIRTHDAY']."'";
$query = "insert into member(ID,PASSWORD,NAME,NICKNAME,EMAIL,PHONE,BIRTHDAY) values (".$data_stream.")";
$result = mysqli_query($conn, $query);
     
    if($result)
      echo "1";
    else
      echo "-1";
     
    mysqli_close($conn);
?>