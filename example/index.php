<?php
$otp = $_GET['otp'];
$now = new \DateTime('now');
$month = $now->format('m');
$year = $now->format('Y');
$hour = $now->format('g');
$day = $now->format('j');
$minute = $now->format('i');
$private = "test";

$math = $month + $hour + $year + $day + $minute + 1;
$math .= $private; 
$prevmath = $month + $hour + $year + $day + $minute;	
$prevmath .= $private; 
$mathhashed = md5($math);
$prevhashed = md5($prevmath);
if (substr($mathhashed, -6) === $otp || substr($prevhashed, -6) === $otp) {
  //Secure content goes here!!!
  echo "Valid!";
  
}
else {
	echo "Invalid!";
}
?>