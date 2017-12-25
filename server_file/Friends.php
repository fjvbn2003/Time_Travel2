<?php
  $con = mysqli_connect("localhost","fjvbn2003","Yj_102938","fjvbn2003");
  $result = mysqli_query($con, "SELECT * FROM FRIENDS ORDER BY userID ASC;");
  $response = array();

  while ($row = mysqli_fetch_array($result)) {
    array_push($response, array("userID"=>$row[0], "friendID"=>$row[1]));
  }

  echo json_encode(array("response"=>$response));
  mysqli_close($con);
?>
