<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

$sql="select groupsearchboard.ID, TITLE, CONTENT, GROUPID, MEMBERID, ATTENDENCETIME, ATTENDENCELATETIME, LATEFINE, ABSENCEFINE from groupsearchboard left join study202.group on groupID = study202.group.id ORDER BY id DESC";

if($result=mysqli_query($conn,$sql)){
$row_num = mysqli_num_rows($result);
echo "{";
echo "\"board\":";

echo "[";
for($i =0;$i<$row_num;$i++){
$row = mysqli_fetch_array($result);
$row['CONTENT'] = str_replace("\n", "\\n", $row['CONTENT']);
echo "{";

echo "\"id\":\"$row[ID]\", \"title\":\"$row[TITLE]\", \"content\":\"$row[CONTENT]\", \"groupid\":\"$row[GROUPID]\", \"memberid\":\"$row[MEMBERID]\", \"attendencetime\":\"$row[ATTENDENCETIME]\", \"attendencelatetime\":\"$row[ATTENDENCELATETIME]\", \"latefine\":\"$row[LATEFINE]\", \"absencefine\":\"$row[ABSENCEFINE]\"";

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
