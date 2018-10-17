<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");
/*
$_POST['TITLE']
$_POST['CONTENT']
$_POST['ATTENDENCETIME']
$_POST['ATTENDENCELATE']
$_POST['LATEFINE']
$_POST['ABSENTFINE']
$_POST['LEADERID']
*/

$data_stream = "'".$_POST['CONTENT']."','".$_POST['ATTENDENCETIME']."','".$_POST['ATTENDENCELATE']."','".$_POST['LEADERID']."',".$_POST['LATEFINE'].",".$_POST['ABSENTFINE'];
$query = "insert into study202.group(MEMO,ATTENDENCETIME,ATTENDENCELATETIME,LEADERID,LATEFINE,ABSENCEFINE) values (".$data_stream.")";
$result = mysqli_query($conn, $query);
     
if($result){
	$sql = "select ID from study202.group where LEADERID="."'".$_POST['LEADERID']."'";
	$res = mysqli_query($conn, $sql);
	$row = mysqli_fetch_array($res);
	
	$data_stream = "'".$_POST['TITLE']."','".$_POST['CONTENT']."',".$row[ID].",'".$_POST['LEADERID']."'";
	
	$query = "insert into groupsearchboard(TITLE,CONTENT,GROUPID,MEMBERID) values (".$data_stream.")";
	$result = mysqli_query($conn, $query);
	
	$query = "update study202.member set groupID = $row[ID] where ID = '".$_POST['LEADERID']."'";
	$result = mysqli_query($conn, $query);
	if($result)
		echo "1";
	else
		echo $query;
}else{
    echo $query;
     }
    mysqli_close($conn);
?>