<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");
$groupid = mysqli_real_escape_string($conn, $_POST['groupID']);
$sql="SELECT member.ID,member.NAME,member.EMAIL,member.PHONE,group.leaderID,member.TRUST from study202.member join study202.group on member.groupID = group.ID where group.ID = '$groupid'";
if($result=mysqli_query($conn,$sql)){
$row_num = mysqli_num_rows($result);
echo "{";
echo "\"member\":";
echo "[";
for($i =0;$i<$row_num;$i++){
$row = mysqli_fetch_array($result);
echo "{";
$today = date('Y-m-d');  
$sqlattendence = "SELECT attendence.state from study202.attendence where attendence.memberID = '$row[ID]' and DATE(attendence.datetime)="."'".$today."'";
$resultattendence=mysqli_query($conn,$sqlattendence);
$row_num_att = mysqli_num_rows($resultattendence);
$row_att = mysqli_fetch_array($resultattendence);
if($row_num_att == 0){
echo "\"id\":\"$row[ID]\", \"name\":\"$row[NAME]\", \"email\":\"$row[EMAIL]\",\"phone\":\"$row[PHONE]\",\"leaderID\":\"$row[leaderID]\",\"trust\":\"$row[TRUST]\",\"attendence\":\"-1\"";
}
else{
  for($k =0;$k<$row_num_att;$k++){
      if($row_num_att==($k+1)){
        echo "\"id\":\"$row[ID]\", \"name\":\"$row[NAME]\", \"email\":\"$row[EMAIL]\",\"phone\":\"$row[PHONE]\",\"leaderID\":\"$row[leaderID]\",\"trust\":\"$row[TRUST]\",\"attendence\":\"$row_att[state]\"";
      }
  }
}
echo "}";
if($i<$row_num-1){
echo ",";
}
}
echo "]";
echo "}";
}
else{
echo "failed to get data from database.";
}
?>
