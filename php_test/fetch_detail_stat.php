<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

$sql="select * from study202.attendence where groupID='".$_GET['GROUPID']."' and memberID='".$_GET['ID']."' ORDER BY id DESC";

if($result=mysqli_query($conn,$sql)){
$row_num = mysqli_num_rows($result);
echo "{";
echo "\"attendence\":";

echo "[";
for($i =0;$i<$row_num;$i++){
$row = mysqli_fetch_array($result);
//$row[CONTENT] = str_replace("\n", "\\n", $row[CONTENT]);
echo "{";

echo "\"method\":\"$row[method]\", \"datetime\":\"$row[datetime]\", \"state\":\"$row[state]\", \"fine\":\"$row[fine]\"";

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
