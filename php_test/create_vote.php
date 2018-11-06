<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");
/*
$_POST['AUTHOR']
$_POST['VOTEDID']
$_POST['GROUPID']
$_POST['VOTETYPE']
$_POST['CONTENT']
*/
$query = "select ID from study202.member where groupID="."'".$_POST['GROUPID']."'";
$result = mysqli_query($conn, $query);
$num_rows = mysqli_num_rows($result);


$data_stream = "'".$_POST['AUTHOR']."','".$_POST['VOTEDID']."','".$_POST['GROUPID']."','".$_POST['VOTETYPE']."','".$num_rows."','".$_POST['CONTENT']."'";
$query = "insert into study202.vote(author,votedID,groupID,votetype,numofvoters,content) values (".$data_stream.")";
$result = mysqli_query($conn, $query);

if($result){

		echo "1";

}else{
    echo $query;
     }
    mysqli_close($conn);
?>
