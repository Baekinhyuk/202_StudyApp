<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

$sql="select memberID, TRUST from study202.waitgroupregist left join study202.member on memberID = study202.member.ID where study202.waitgroupregist.groupID=".$_GET['GROUPID'];

if($result=mysqli_query($conn,$sql)){
$row_num = mysqli_num_rows($result);
echo "{";
echo "\"waiting\":";

echo "[";
for($i =0;$i<$row_num;$i++){
$row = mysqli_fetch_array($result);
echo "{";

echo "\"id\":\"$row[memberID]\", \"trust\":\"$row[TRUST]\"";

echo "}";
if($i<$row_num-1){
echo ",";
}
}
echo "]";

echo "}";
}
else{
echo "failed to get data from fetch_waiting.php.";
}
?>
