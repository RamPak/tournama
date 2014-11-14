<?php

session_start();
ob_start();
include_once('config.php');

/********************************************  f u n c t i o n s  *********************************************/
include_dir("inc");
function include_dir($dir_name){
	if(!$dp=opendir($dir_name)){
		die("Error in opening directory");
	} // else 
	while($dir=readdir($dp)){
		if(strrchr($dir,'.')!='.php'){
			continue;
		} else {
			include_once($dir_name."/".$dir);
		}
	}
	closedir($dp);
}
/**************************/

$pagedata = ob_get_contents();
ob_end_clean();

/**********************************************  U R L  *************************************************************/
# making the _URL define.
$URLFOTDEFINE = "http://".str_replace("/","",$_SERVER['HTTP_HOST']);
$SUBFOLDER = substr( dirname($_SERVER['SCRIPT_NAME']),1,strlen(dirname($_SERVER['SCRIPT_NAME'])) );
if(strlen($SUBFOLDER)!=0) $URLFOTDEFINE .= "/".$SUBFOLDER;
define('_URL',$URLFOTDEFINE);
/********************************************************************************************************************/

?>
