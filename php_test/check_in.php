<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");
/*
$_POST['ID'] memberID
$_POST['GROUPID'] groupID // ID
$_POST['METHOD']

*/
$timenow = date('Y-m-d'); // 2018-11-06
$query = "select ID from study202.attendence where memberID="."'".$_POST['ID']."' and DATE(datetime)="."'".$timenow."'";
$result = mysqli_query($conn, $query);
$num_rows = mysqli_num_rows($result);
if($num_rows == 0){
  $query = "select attendencetime, attendencelatetime from study202.group where ID="."'".$_POST['GROUPID']."'";
  $result = mysqli_query($conn, $query);
  $row = mysqli_fetch_array($result);
  $attendencetime = strtotime(date('Y-m-d').' '.$row['attendencetime']);
  $attendencelatetime = strtotime(date('Y-m-d').' '.$row['attendencelatetime']);
  if (($attendencetime - strtotime("now")) <= 3600){
    //출석가능한 시간 이면서 출석시간보다 시간이 빠른 경우
    if ($attendencetime >= strtotime("now")){
      //출석으로 입력
      //$data_stream = "'".$_POST['ID']."','".$_POST['METHOD']."','".$_POST['GROUPID']."',"."0";
      //$query = "insert into study202.attendence (memberID, method, groupID, state) VALUES (".$data_stream.")";
      echo "0";

    } else if ( $attendencetime < strtotime("now") && strtotime("now") <= $attendencelatetime ) {
      //지각으로 입력
      //$data_stream = "'".$_POST['ID']."','".$_POST['METHOD']."','".$_POST['GROUPID']."',"."1";
      //$query = "insert into study202.attendence (memberID, method, groupID, state) VALUES (".$data_stream.")";
      echo "1";
    } else {
      // 결석으로 입력
      $data_stream = "'".$_POST['ID']."','".$_POST['METHOD']."','".$_POST['GROUPID']."',"."2";
      $query = "insert into study202.attendence (memberID, method, groupID, state) VALUES (".$data_stream.")";
      echo "2";
      $result = mysqli_query($conn, $query);
    }

  } else
    echo "3";
} else {
  echo "-1";
}

mysqli_close($conn);
?>
