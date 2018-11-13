<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");
/*

$_GET['ID']
*/

$query = "select fine from study202.member where ID ="."'".$_GET['ID']."'";
$result = mysqli_query($conn, $query);
$row = mysqli_fetch_array($result);

if($result){

		echo $row['fine'];

}else{
    echo "error";
     }
    mysqli_close($conn);
?>
