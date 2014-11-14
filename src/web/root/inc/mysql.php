<?php

$GLOBALS['DB_POINTER'] = false;
function db_connect($S=false){
	
#	if($GLOBALS['DB_POINTER']){
#		return $GLOBALS['DB_POINTER'];
#	}
	if($db= @ mysql_connect($GLOBALS['sqlhost'], $GLOBALS['sqluser'], $GLOBALS['sqlpass']))
	{
		mysql_query("SET CHARACTER SET utf8", $db);
		mysql_query("SET NAMES 'utf8'", $db);
		if(@ mysql_select_db($GLOBALS['dtbname']) ){
			$GLOBALS['DB_POINTER']=$db;
			return $db;
		}
	}
	if($S==true){
		;
	} else {
		echo "<center class=er1 >invalid mysql connection</center>";
	}
	return false;
}

function db_query($query=''){
	if($query==''){
		return false;
	} else {
		if(db_connect()){
			if($res = mysql_query($query)){
				return $res;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}

function dbq($query=''){
	if($query==''){
		return false;
	} else {
		if(db_connect()){
			if($res = mysql_query($query)){
				return $res;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}

