<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

$sql="select * from study202.member";

if($result=mysqli_query($conn,$sql)){
$row_num = mysqli_num_rows($result);
echo "{";
echo "\"member\":";

echo "[";
for($i =0;$i<$row_num;$i++){
$row = mysqli_fetch_array($result);
echo "{";

echo "\"id\":\"$row[ID]\", \"password\":\"$row[PASSWORD]\" , \"name\":\"$row[NAME]\", \"nickname\":\"$row[NICKNAME]\", \"email\":\"$row[EMAIL]\",\"phone\":\"$row[PHONE]\"";

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