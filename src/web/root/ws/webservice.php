<?php 
include_once('../config.php');

if(isset($_GET['table']) && isset($_GET['select']))
{

    $table = trim($_GET['table']);
	$select = trim($_GET['select']);
	$where = (isset($_GET['where']) ? trim($_GET['where']) : "" );
	$order = (isset($_GET['order']) ? trim($_GET['order']) : "" );
   
    $conn = mysql_connect($GLOBALS['sqlhost'], $GLOBALS['sqluser'], $GLOBALS['sqlpass']);
    mysql_select_db($GLOBALS['dtbname'],$conn);

    $query = "SELECT ".$select." FROM ".$table." ".($where == "" ? "" : "where ".$where)." ".($order == "" ? "" : "order by ".$order);
    $result = mysql_query($query,$conn);
	
    $posts = array();
    if(mysql_num_rows($result)) 
	{
        while($post = mysql_fetch_assoc($result)) 
		{
            $posts[] = array('post'=>$post);
        }
    }

    header('Content-type: application/json');
    echo json_encode(array('posts'=>$posts));

    @mysql_close($conn);
}
 ?> 