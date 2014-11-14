<?

function _ads(){
	echo "
	<table width=80% align=center dir=rtl border=0 cellspacing=0 cellpadding=0 >
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	<tr height=30 >
		<td bgcolor=#f4f4f4 style='font-size:16px;'>&nbsp; &nbsp; <a style='font-size:16px;' href='./' >مدیریت</a> » مدیریت تبلیغات</td>
		<td bgcolor=#f4f4f4 align=left ><span style='color:#aaaaaa; cursor:default;'>شما با نام کاربری ".mysql_result(dbq(" SELECT `UserName` FROM `tbl_agency` WHERE `ID`='".$_SESSION['ID']."' LIMIT 1 "),0,0)." وارد سایت شده اید</span><span style='cursor:default;'>&nbsp; /</span><a style='color:#aa4444;font-size:12px;padding:10px;' href=./?do=logout >خروج</a></td>
	</tr>
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	</table>
	";
	switch($_REQUEST['do']){
		case 'saveNew' : 
			dbq(" INSERT INTO `tbl_advertise` (`name`,`image`,`link`,`startdate`,`enddate`) 
			VALUES ('".$_REQUEST['name']."','$image_addr','".$_REQUEST['link']."','".$_REQUEST['startdate']."','".$_REQUEST['enddate']."') ");
			$id = mysql_insert_id();
			$logo_name = "Ad/".$id.strrchr($_FILES['image']['name'],".");
			if($_FILES['image']['size']<=0){
				;// no file attached.
			} else if($_FILES['image']['size']>100*1024){
				echo "<script>alert('حداکثر حجم فایل لوگو 100KB میباشد')</script>";
			} else if(substr($_FILES['image']['type'],0,10)!='image/jpeg'){
				echo "<script>alert('invalid mime type')</script>";
			} else if(!copy($_FILES['image']['tmp_name'], $logo_name)){
				echo "err - ".__LINE__;
			} else if(!dbq(" UPDATE `tbl_advertise` SET `image`='$logo_name' WHERE `id`='$id' ")){
				echo "err - ".__LINE__;
			}
			break;
			
		case 'saveEdit' : 
			$id = intval($_REQUEST['id']);
			$image_old = mysql_result(dbq(" SELECT `image` FROM `tbl_advertise` WHERE `id`='$id' LIMIT 1 "),0,0);
			dbq(" UPDATE `tbl_advertise` SET
				 `name`='".$_REQUEST['name']."'
				,`link`='".$_REQUEST['link']."'
				,`startdate`='".$_REQUEST['startdate']."'
				,`enddate`='".$_REQUEST['enddate']."'
			WHERE `id`='$id' LIMIT 1 ");
			$logo_name = "Ad/".$id.strrchr($_FILES['image']['name'],".");
			if($_FILES['image']['size']<=0){
				;// no file attached.
			} else if($_FILES['image']['size']>100*1024){
				echo "<script>alert('حداکثر حجم فایل لوگو 100KB میباشد')</script>";
			} else if(substr($_FILES['image']['type'],0,10)!='image/jpeg'){
				echo "<script>alert('invalid mime type')</script>";

			} else if(!copy($_FILES['image']['tmp_name'], $logo_name)){
				echo "err - ".__LINE__;
			} else if(!dbq(" UPDATE `tbl_advertise` SET `image`='$logo_name' WHERE `id`='$id' ")){
				echo "err - ".__LINE__;
			}
			break;
		case 'remove' : 
			dbq(" DELETE FROM `tbl_advertise` WHERE `id`='".intval($_REQUEST['id'])."' LIMIT 1 ");
			break;
	}
	echo "
	<br><br>
	<table width=80% dir=rtl bgcolor=#cccccc align=center cellspacing=1 cellpadding=10>
	<tr bgcolor=#f0f0f0 >
		<th></th>
		<th>نام</th>
		<th>عکس</th>
		<th>لینک</th>
		<th>شروع</th>
		<th>پایان</th>
		<th></th>
	</tr>
	";
	if(!$rs = dbq(" SELECT * FROM `tbl_advertise` WHERE 1 ORDER BY name")){
		echo "err - ".__LINE__;
	} else if(!mysql_num_rows($rs)){
		echo "<tr bgcolor=#ffffff height=40 ><th colspan=6 >موردی یافت نشد</th></tr>";
	} else while($rw = mysql_fetch_assoc($rs)){
		$k++;
		echo "
		<form enctype='multipart/form-data' method='post' action='./?cp=".$_REQUEST['cp']."&do=saveEdit&id=".$rw['id']."' >
		<tr bgcolor='#ffffff'>
			<td><a onclick='if(!confirm(\"آیا مایل به حذف این تبلیغ هستید؟\"))return false;' href='./?cp=".$_REQUEST['cp']."&do=remove&id=".$rw['id']."' ><img border='none' src='images/delete_2.png' /></a></td>
			<td><input type=text name=name value='".$rw['name']."' style='width:140px' ></td>
			<td><a href='".$rw['image']."' target=_blank ><img border='none' src='".$rw['image']."' width=40 height=40 /></a><input type=file name=image ></td>
			<td><input type=text name=link value='".$rw['link']."' dir=ltr style='width:140px' ></td>
			<td><input type=text dir=ltr name=startdate value='".$rw['startdate']."' id='startdate".$k."' style='width:80px' ><script type=\"text/javascript\">\$(document).ready(function(){\$(\"#startdate".$k."\").datepicker({dateFormat: 'yy/mm/dd'});});</script></td>
			<td><input type=text dir=ltr name=enddate value='".$rw['enddate']."' id='enddate".$k."' style='width:80px' ><script type=\"text/javascript\">\$(document).ready(function(){\$(\"#enddate".$k."\").datepicker({dateFormat: 'yy/mm/dd'});});</script></td>
			<td><input type=submit value='ویرایش' style='width:80px;' /></td>
		</tr>
		</form>
		";
	}
	echo "
		<tr bgcolor=#f0f0f0 ><td colspan=7 ></td></tr>
		<form enctype='multipart/form-data' method='post' action='./?cp=".$_REQUEST['cp']."&do=saveNew' >
		<tr bgcolor='#ffffff'>
			<td></td>
			<td><input type=text name=name style='width:140px;' ></td>
			<td><img src='images/transparent.gif' width=20 height=20 border='none' /><input type=file name=image ></td>
			<td><input type=text name=link dir=ltr style='width:140px;' ></td>
			<td><input type=text dir=ltr name=startdate id=startdate style='width:80px;' ><script type=\"text/javascript\">\$(document).ready(function(){\$(\"#startdate\").datepicker({dateFormat: 'yy/mm/dd'});});</script></td>
			<td><input type=text dir=ltr name=enddate id=enddate style='width:80px;' ><script type=\"text/javascript\">\$(document).ready(function(){\$(\"#enddate\").datepicker({dateFormat: 'yy/mm/dd'});});</script></td>
			<td><input type=submit value='ثبت تبلیغ' style='width:80px;' /></td>
		</tr>
	</table>
	";
}

?>
