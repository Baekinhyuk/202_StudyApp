<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");
/*
$_POST['TITLE']
$_POST['CONTENT']
$_POST['ID']

*/

$query = "update study202.groupboard set title='".$_POST['TITLE']."',"."content='".$_POST['CONTENT']."' where id='".$_POST['ID']."'"; 
$result = mysqli_query($conn, $query);
     
if($result){	
	
		echo "1";
	
}else{
    echo $query;
     }
    mysqli_close($conn);
?>