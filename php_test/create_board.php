<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");
/*
$_POST['TITLE']
$_POST['CONTENT']
$_POST['GROUPID']
$_POST['MEMBERID']
*/

$data_stream = "'".$_POST['TITLE']."','".$_POST['CONTENT']."','".$_POST['GROUPID']."','".$_POST['MEMBERID']."'";
$query = "insert into study202.groupboard(TITLE,CONTENT,GROUPID,MEMBERID) values (".$data_stream.")";
$result = mysqli_query($conn, $query);

if($result){

		echo "1";

}else{
    echo $query;
     }
    mysqli_close($conn);
?>
