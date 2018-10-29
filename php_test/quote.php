<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

$id = mysqli_real_escape_string($conn, $_POST['ID']);
$query = "select quote.content,quote.author from study202.quote where quote.ID = '$id'";

$result = mysqli_query($conn, $query);
$row_num = mysqli_num_rows($result);
if($row_num == 1){
$row = mysqli_fetch_array($result);
echo "{";
echo "\"quote\":";
echo "[";
echo "{";
echo "\"content\":\"$row[content]\",\"author\":\"$row[author]\"";
echo "}";
echo "]";
echo "}";}
else{ echo "-1";}

mysqli_close($conn);
?>
