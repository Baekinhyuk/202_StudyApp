<?php
header('Content-Type: text/html; charset=euc-kr');
$conn=mysqli_connect("localhost","root","apmsetup","202_study");

$data_stream = "'".$_POST['ID']."','".$_POST['PASSWORD']."','".$_POST['NAME']."','".$_POST['NICKNAME']."','".$_POST['EMAIL']."','".$_POST['PHONE']."','".$_POST['BIRTHDAY']."'";
$query = "insert into member(ID,PASSWORD,NAME,NICKNAME,EMAIL,PHONE,BIRTHDAY) values (".$data_stream.")";
$result = mysqli_query($conn, $query);
     
    if($result)
      echo "1";
    else
      echo "-1";
     
    mysqli_close($conn);
?>