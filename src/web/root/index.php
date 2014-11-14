<?
include_once('inc.php');

switch($_REQUEST['do']){
	
	case 'register' : 
		_do_draw_html_start();
		_do_draw_header();
		echo '<br>
		<center dir=rtl >
		<fieldset style="width:80% ">
		<legend align="right" dir=rtl style="font-size:14px;color:#aaaaff; font-weight:bold;"> قوانین سایت </legend>
		<br><br>';
		echo "<div style='width:95%; text-align:justify; '>";
		echo "<hr style='width:100%; border:1px dotted #888888'>";
		echo implode("<br>",file("docs/role.txt"));
		echo "<br><br><hr style='width:100%; border:1px dotted #888888'><br><br><center>
		<input type=button onclick='location.href=\"./?do=register2\"' value='قوانین را میپذیرم'></center><br><br>";
		echo "</div></center>";
		echo "</fieldset><br><br><br>";
		_do_draw_footer();
		_do_draw_html_end();
		die();
	
	case 'register2' : 
		_do_draw_html_start();
		_do_draw_header();
		_agency_register();
		_do_draw_footer();
		_do_draw_html_end();
		die();
	
	case 'logout' : 
		session_destroy();
		echo "<script>location.href='./'</script>";
		break;
		
	case 'security_number' :
		$rand=rand(100000,999999);
		$_SESSION['security_number'] = $rand;
		$im = imagecreatefrompng('images/SECPHOTOBG'.rand(1,4).'.png');
		$cl = imagecolorallocate($im, rand(50,200), rand(50,200), rand(50,200));
		imagestring($im,24,10,2,$rand,$cl);
		echo imagepng($im);
		imagedestroy($im);
		die();
		
	case 'get_city_list' : 
		if(!$_REQUEST['select']){
			echo "<option></option>";
		}
		if(!$_REQUEST['CountryID']){
			echo "err - ".__LINE__;
		} else if(!$rs = dbq(" SELECT * FROM `tbl_city` WHERE `CountryID`='".$_REQUEST['CountryID']."' ORDER BY Name")){
			echo "err - ".__LINE__;
		} else if(!mysql_num_rows($rs)){
			echo "err - ".__LINE__;
		} else while($rw = mysql_fetch_assoc($rs)){
			if($_REQUEST['select']==$rw['ID']){
				echo "<option value='".$rw['ID']."' selected >".$rw['Name']."</option>";
			} else {
				echo "<option value='".$rw['ID']."' >".$rw['Name']."</option>";
			}
		}
		die();
		
	case 'get_hotel_list' : 
		if(!$_REQUEST['select']){
			echo "<option></option>";
		}
		if(!$_REQUEST['CityID']){
			echo "err - ".__LINE__;
		} else if(!$rs = dbq(" SELECT * FROM `tbl_hotel` WHERE `CityID`='".$_REQUEST['CityID']."' ORDER BY Name")){
			echo "err - ".__LINE__;
		} else if(!mysql_num_rows($rs)){
			echo "err - ".__LINE__;
		} else while($rw = mysql_fetch_assoc($rs)){
			echo "<option value='".$rw['ID']."' >".$rw['Name']."</option>";
		}
		die();
}

_do_draw_html_start();
_do_draw_header();
if(_do_login()){
	_do_draw_panel();
}
_do_draw_footer();
_do_draw_html_end();

?>
