<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");
/*
$_POST['TITLE']
$_POST['CONTENT']
$_POST['groupID']
$_POST['memberID']
$_POST['image_path']
$_POST['image_name']
$_POST['state']
*/
$ImageData = $_POST['image_path'];
$data_stream = "'".$_POST['memberID']."',0,'".$_POST['groupID']."',".$_POST['state'];
$query = "insert into study202.attendence (memberID, method, groupID, state) VALUES (".$data_stream.")";
$result = mysqli_query($conn, $query);

$ImagePath = "images/".$_POST['image_name'].$_POST['memberID'].".jpg";
$ServerURL = "https://cauteam202.com/$ImagePath";

$data_stream = "'".$_POST['image_name']."','".$ServerURL."','".$_POST['groupID']."','".$_POST['memberID']."'";
$query = "insert into study202.groupboard(TITLE,IMAGE,GROUPID,MEMBERID) values (".$data_stream.")";
$result = mysqli_query($conn, $query);

if($result){
    file_put_contents($ImagePath,base64_decode($ImageData));
		echo "1";

}else{
    echo $query;
     }
    mysqli_close($conn);
?>
