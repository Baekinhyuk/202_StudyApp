<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

  $sql = "update study202.member set groupID = ".$_POST['GROUPID']." where id = '".$_POST['ID']."'";
  $result = mysqli_query($conn, $sql);

  if($result){
     $sql = "delete from waitgroupregist where memberID ='".$_POST['ID']."'";
     if(mysqli_query($conn, $sql))
        echo "1";

  }else{
      echo $sql;
       }

      mysqli_close($conn);
?>
