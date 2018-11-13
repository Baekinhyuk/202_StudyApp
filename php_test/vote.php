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

    $query = "select * from study202.vote where ID="."'".$_POST['ID']."'";
    $result = mysqli_query($conn, $query);
    $row = mysqli_fetch_array($result);
    $cutline = (int)((int)$row['numofvoters'] / 2) + 1;

    if($row['pros'] >= $cutline ) {
      if($row['votetype'] == 0) { //결석으로
        $sql="select ID from study202.attendence where memberID="."'".$row['votedID']."' order by ID desc";
        $result = mysqli_query($conn,$sql);
        $row2 = mysqli_fetch_array($result);
        $sql="update study202.attendence set state = 2 where ID=".$row2['ID'];
        $result = mysqli_query($conn,$sql);

      }else{ // 강퇴
          $sql="update study202.member set groupID = NULL, fine = 0 where ID ="."'".$row['votedID']."'";
          mysqli_query($conn,$sql);
      }

      $sql="delete from study202.vote where ID='".$_POST['ID']."'";
      mysqli_query($conn,$sql);
    }else if($row['numofvoters'] == ($row['pros'] + $row['cons'])) {
      $sql="delete from study202.vote where ID='".$_POST['ID']."'";
      mysqli_query($conn,$sql);

    }

    echo "1";
}else {
  echo $query;
}

}else{
    echo $query;
     }
    mysqli_close($conn);
?>
