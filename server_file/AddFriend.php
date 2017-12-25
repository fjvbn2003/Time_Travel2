<?php
      $con = mysqli_connect("localhost","fjvbn2003","Yj_102938","fjvbn2003");

      $userID = $_POST["userID"];
      $friendID = $_POST["friendID"];
      mysqli_stmt_execute("USE FRIENDS");

      $statement = mysqli_prepare($con, "INSERT INTO FRIENDS SELECT ?,? FROM DUAL WHERE NOT EXISTS (SELECT * FROM FRIENDS WHERE userID=? and friendID=?)");
      mysqli_stmt_bind_param($statement,"ssss",$userID,$friendID,$userID,$friendID);
      mysqli_stmt_execute($statement);

      $response = array();
      $response["success"] = true;

      echo json_encode($response);
 ?>
