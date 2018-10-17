<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

$sql="delete from study202.groupsearchboard where ID='".$_GET[ID]."'";

mysqli_query($conn,$sql);

echo $sql;
?>