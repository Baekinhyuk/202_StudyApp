<?php

header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

$sql = "select latefine, absencefine from study202.group where ID=".$_GET['GROUPID'];
$result2 = mysqli_query($conn,$sql);
$fines = mysqli_fetch_array($result2); // fines[latefine] fine[absencefine]


$sql="select ID, fine from study202.member where groupID=".$_GET['GROUPID'];


if($result=mysqli_query($conn,$sql)){
  $row_num = mysqli_num_rows($result);
  echo "{";
  echo "\"Stats\":";

  echo "[";
  for($i =0;$i<$row_num;$i++){
    $row = mysqli_fetch_array($result);
    $sql = "select state from study202.attendence where memberID ="."'".$row['ID']."' and groupID=".$_GET['GROUPID'];
    $result1 = mysqli_query($conn,$sql);
    $row1_num = mysqli_num_rows($result1);
    $total = 0;
    $present = 0;
    $absent = 0;
    $late = 0;
    for($j = 0; $j<$row1_num;$j++){
      $row1 = mysqli_fetch_array($result1);
      if($row1['state'] == 0){
        $present = $present + 1;
      }else if($row1['state'] == 1){
        $late = $late + 1;
      }else if($row1['state'] == 2){
        $absent = $absent + 1;
      }
      $total = $total + 1;
    }
    $totalfine = $late * $fines['latefine'] + $absent * $fines['absencefine'];

    echo "{";

    echo "\"id\":\"$row[ID]\", \"total\":\"$total\", \"present\":\"$present\", \"late\":\"$late\", \"absent\":\"$absent\", \"totalfine\":\"$totalfine\", \"fine\":\"$row[fine]\"";

    echo "}";
if($i<$row_num-1){
echo ",";
}
}
echo "]";

echo "}";
}
else{
echo $sql;
}
?>
