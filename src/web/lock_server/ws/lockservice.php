<?php 

if(isset($_GET['system']))
{
	$message = "nothing";
    $system = trim($_GET['system']);
	
	if($system == "tournama")
		$message = "true";

		

    header('Content-type: application/json');
    echo json_encode($message);


}
 ?> 