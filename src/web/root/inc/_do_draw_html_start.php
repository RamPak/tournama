<?

function _do_draw_html_start(){
	?>
	<html>
	<head>
		<title>.:: <?=mysql_result(dbq(" SELECT `SystemName` FROM `tbl_setting` "),0,0)?> ::.</title>
		<link rel="shortcut icon" href="images/favicon.ico" >
		<META http-equiv=Content-Type content="text/html; charset=utf-8">
		<link rel="stylesheet" href="styles/main.css" type="text/css">
		<script language="javascript1.1" src="js/jquery-1.7.2.min.js"></script>
		<script language="javascript1.1" src="js/main.js"></script>
		
		<link type="text/css" href="modules/jquery.ui.datepicker1.8.14-cc/styles/jquery-ui-1.8.14.css" rel="stylesheet" />
		<script type="text/javascript" src="modules/jquery.ui.datepicker1.8.14-cc/scripts/jquery.ui.datepicker-cc.all.min.js"></script>
	</head>
	<body>
	<?
}


