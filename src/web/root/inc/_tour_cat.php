<?

function _tour_cat(){
	switch($_REQUEST['do']){
		case 'add' : 
			dbq(" INSERT INTO `tbl_tourgroup` (`Name`) VALUES ('".$_REQUEST['Name']."') ");
			break;
		case 'edit' : 
			dbq(" UPDATE `tbl_tourgroup` SET `Name`='".$_REQUEST['Name']."' WHERE `ID`='".$_REQUEST['ID']."' LIMIT 1 ");
			break;
		case 'remove' : 
			if(mysql_result(dbq(" SELECT COUNT(*) FROM `tbl_tour` WHERE `GroupID`='".$_REQUEST['ID']."' "),0,0)!=0){
				echo "هنوز توری در این گروه به ثبت رسیده است";
			} else {
				dbq(" DELETE FROM `tbl_tourgroup` WHERE `ID`='".$_REQUEST['ID']."' LIMIT 1 ");
			}
			break;
	}
	echo "
	<table width=80% align=center dir=rtl border=0 cellspacing=0 cellpadding=0 >
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	<tr height=30 >
		<td bgcolor=#f4f4f4 style='font-size:16px;'>&nbsp; &nbsp; <a style='font-size:16px;' href='./' >مدیریت</a> » دسته بندی تورها</td>
		<td bgcolor=#f4f4f4 align=left ><span style='color:#aaaaaa; cursor:default;'>شما با نام کاربری ".mysql_result(dbq(" SELECT `UserName` FROM `tbl_agency` WHERE `ID`='".$_SESSION['ID']."' LIMIT 1 "),0,0)." وارد سایت شده اید</span><span style='cursor:default;'>&nbsp; /</span><a style='color:#aa4444;font-size:12px;padding:10px;' href=./?do=logout >خروج</a></td>
	</tr>
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	";
	if(!$rs = dbq(" SELECT * FROM `tbl_tourgroup` WHERE 1 ORDER BY Name")){
		echo "err - ".__LINE__;	
	} else if(mysql_num_rows($rs)==0){
		echo "<tr height=100px ><td align=center >هنوز دسته ای ثبت نشده است!</td></tr>";
	} else while($rw = mysql_fetch_assoc($rs)){
		echo "
		<tr>
		<td align=center >
		<form method=post action='./?cp=".$_REQUEST['cp']."&do=edit&ID=".$rw['ID']."' >
		<a onclick='if(!confirm(\"آیا مایل به حذف این دسته بندی هستید؟\"))return false;' href='./?cp=".$_REQUEST['cp']."&do=remove&ID=".$rw['ID']."' ><img src='images/delete_2.png' border='none' /></a>
		<input type=text name=Name value='".$rw['Name']."' >
		<input type=submit value='ویرایش' style='width:70px;' />
		‌</form>
		</td></tr>
		";
	}
	echo "
	<tr height=30 ><td><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	<tr><td align=center >
	<img src='images/transparent.gif' width=12 height=12 border='none' />
	<form method=post action='./?cp=".$_REQUEST['cp']."&do=add' > 
	<input type=text name=Name value='دسته جدید' onclick='value=\"\"' >
	<input type=submit value='ثبت' style='width:70px;' />
	‌</form>
	</td></tr>
	<tr height=30 ><td><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	";
	echo "</table>";
}

