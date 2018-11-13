<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");
/*

$_POST['ID']
$_POST['VALUE'] 정산값
*/

$query = "update study202.member set fine = fine - ".$_POST['VALUE']." where ID ="."'".$_POST['ID']."'";
$result = mysqli_query($conn, $query);
/*$query = "select fine from study202.member where ID ="."'".$_POST['ID']."'";
$result = mysqli_query($conn, $query);
$row = mysqli_fetch_array($result);*/

if($result){

    echo "1";
		//echo $row['fine'];

}else{
    echo "error";
     }
    mysqli_close($conn);
?>
