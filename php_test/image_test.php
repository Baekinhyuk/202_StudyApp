<?php
header('Content-Type: text/html; charset=utf-8');
$conn=mysqli_connect("localhost","root","a123s123","study202");

if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
 $DefaultId = 0;

 $ImageData = $_POST['image_path'];

 $ImageName = $_POST['image_name'];

 $GetOldIdSQL ="SELECT id FROM study202.image ORDER BY id ASC";

 $Query = mysqli_query($conn,$GetOldIdSQL);

 while($row = mysqli_fetch_array($Query)){

 $DefaultId = $row['id'];
 }

 $ImagePath = "images/$DefaultId.jpg";

 $ServerURL = "https://cauteam202.com/$ImagePath";

 $InsertSQL = "insert into study202.image (image_path,image_name) values ('$ServerURL','$ImageName')";

 if(mysqli_query($conn, $InsertSQL)){

 file_put_contents($ImagePath,base64_decode($ImageData));

 echo "Your Image Has Been Uploaded.";
} else {
   echo "Not Uploaded!!!";
}

 mysqli_close($conn);
 }else{
 echo "Not Uploaded";
 }


?>
