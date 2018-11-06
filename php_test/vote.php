<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");
/*

$_POST['ID']
$_POST['MEMBERID']
$_POST['VOTE'] pro = 0 , con = 1
*/
$query = "select * from study202.voteresult where voteID="."'".$_POST['ID']."' AND memberID="."'".$_POST['MEMBERID']."'";
$result = mysqli_query($conn, $query);
$num_rows = mysqli_num_rows($result);

if($num_rows == 0) {
  $data_stream = "'".$_POST['ID']."','".$_POST['MEMBERID']."','".$_POST['VOTE']."'";

  $query = "insert into study202.voteresult (voteID, memberID, proandcon) values (".$data_stream.")";
  $result = mysqli_query($conn, $query);


  if($result){
    if( $_POST['VOTE'] == 0 )
      $query = "update study202.vote set pros = pros + 1 where ID ="."'".$_POST['ID']."'";
    else {
      $query = "update study202.vote set cons = cons + 1 where ID ="."'".$_POST['ID']."'";
    }
    $result = mysqli_query($conn, $query);
    echo "1";
}else {
  echo $query;
}

}else{
    echo $query;
     }
    mysqli_close($conn);
?>
