<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");
  /*
  $_POST['ID']
  $_POST['ISLEADER']
  $_POST['GROUPID']

  */
$groupid = $_POST['GROUPID'];
if ($_POST['ISLEADER']) {
   $sql = "select ID from study202.member where groupID =".$groupid." order by TRUST desc";
   if ($result = mysqli_query($conn, $sql)) {
     $num_rows = mysqli_num_rows($result);
  	if ($num_rows <= 1) {
  		$sql = "delete from study202.group where groupID ='".$groupid."'";
  	  mysqli_query($conn, $sql);
  	} else {
  		$row = mysqli_fetch_array($result);
  		$sql = "update study202.group set leaderID ='".$row['ID']."' where id ='".$groupid."'";
      mysqli_query($conn, $sql);

  	}
  } else {
  	echo $sql;
      }

  }

  $sql = "update study202.member set groupID = NULL where id = '".$_POST['ID']."'";
  $result = mysqli_query($conn, $sql);

  if($result){

  		echo "1";

  }else{
      echo $sql;
       }

      mysqli_close($conn);
?>
