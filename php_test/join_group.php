<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");
/*

$_POST['ID']
$_POST['GROUPID']
*/

$query = "insert into study202.waitgroupregist (groupID, memberID) VALUES (".$_POST['GROUPID'].", '".$_POST['ID']."')";
$result = mysqli_query($conn, $query);

if($result){

		echo "1";

}else{
    echo $query;
     }
    mysqli_close($conn);
?>
