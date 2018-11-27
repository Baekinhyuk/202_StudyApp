<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");
// 지각 벌금 값 가져오기
$query = "select latefine from study202.group where ID="."'".$_POST['groupID']."'";
$result = mysqli_query($conn, $query);
$row = mysqli_fetch_array($result);

if($_POST['state'] == 1){ // 지각이라면 멤버 벌금 update (신뢰도 추가해야함)
  $query = "update study202.member set fine = fine + ".$row['latefine']." where ID ="."'".$_POST['memberID']."'";
  $result = mysqli_query($conn, $query);
}

// 멤버 벌금 값 가져오기 (신뢰도 추가해야함)
$query = "select fine, TRUST from study202.member where ID ="."'".$_POST['memberID']."'";
$result = mysqli_query($conn, $query);
$row = mysqli_fetch_array($result);

if($_POST['state'] == 0){ // 출석 시 신뢰도 처리
  $query = "update study202.member set TRUST = TRUST + 0.01 * (1 - ".$row['TRUST']." ) where ID ="."'".$_POST['memberID']."'";
  $result = mysqli_query($conn, $query);
}

// 출석하기  method 0 : 사진 , 1 : 비콘 , 2 : gps
$data_stream = "'".$_POST['memberID']."',1,'".$_POST['groupID']."',".$_POST['state'].",".$row['fine'];
$query = "insert into study202.attendence (memberID, method, groupID, state, fine) VALUES (".$data_stream.")";
$result = mysqli_query($conn, $query);

    mysqli_close($conn);
?>
