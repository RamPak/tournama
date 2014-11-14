<?

function _country_city_hotel__hotel(){
	switch($_REQUEST['do']){
		case 'add' : 
			dbq(" INSERT INTO `tbl_hotel` (`CityID`,`Name`,`userid`) VALUES ('".$_REQUEST['cityID']."','".$_REQUEST['Name']."','".$_SESSION['ID']."') ");
			break;
		case 'edit' : 
			dbq(" UPDATE `tbl_hotel` SET `Name`='".$_REQUEST['Name']."' WHERE `ID`='".$_REQUEST['ID']."' ".($_SESSION['ID']==1?"":"AND `userid`='".$_SESSION['ID']."'")." LIMIT 1 ");
			break;
		case 'remove' : 
			if(mysql_result(dbq(" SELECT COUNT(*) FROM `tbl_tour` WHERE `HotelID`='".$_REQUEST['ID']."' "),0,0)!=0){
				echo "هنوز توری در این هتل به ثبت رسیده است";
			} else {
				dbq(" DELETE FROM `tbl_hotel` WHERE `ID`='".$_REQUEST['ID']."' ".($_SESSION['ID']==1?"":"AND `userid`='".$_SESSION['ID']."'")." LIMIT 1 ");
			}
			break;
	}
	
	echo 
	"<table height=100% width=80% align=center dir=rtl border=0 cellspacing=0 cellpadding=0 >
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	<tr height=30 >
		<td bgcolor=#f4f4f4 style='font-size:16px;'>
			&nbsp; &nbsp; 
			<a style='font-size:16px;' href='./' >مدیریت</a>
			» <a style='font-size:16px;' href='./?cp=".$_REQUEST['cp']."' >کشورها</a>
			» <a style='font-size:16px;' href='./?cp=".$_REQUEST['cp']."&countryID=".$_REQUEST['countryID']."' >شهرها</a> » مدیریت هتل ها
			</td>
		<td bgcolor=#f4f4f4 align=left ><span style='color:#aaaaaa; cursor:default;'>شما با نام کاربری ".mysql_result(dbq(" SELECT `UserName` FROM `tbl_agency` WHERE `ID`='".$_SESSION['ID']."' LIMIT 1 "),0,0)." وارد سایت شده اید</span><span style='cursor:default;'>&nbsp; /</span><a style='color:#aa4444;font-size:12px;padding:10px;' href=./?do=logout >خروج</a></td>
	</tr>
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	";
	if(!$rs = dbq(" SELECT * FROM `tbl_hotel` WHERE `CityID`='".$_REQUEST['cityID']."' ORDER BY Name")){
		echo "err - ".__LINE__;	
	} else if(mysql_num_rows($rs)==0){
		echo "<tr height=100px ><td align=center >هنوز هتلی ثبت نشده است!</td></tr>";
	} else while($rw = mysql_fetch_assoc($rs)){
		if( ($rw['userid']!=$_SESSION['ID']) and ($_SESSION['ID']!=1) ){
			$dontDisplayControl = "1";
		} else {
			$dontDisplayControl = "";
		}
		echo "
		<tr>
		<td align=right style='padding-right:100px;' >
		<fo".$dontDisplayControl."rm method=post action='./?cp=".$_REQUEST['cp']."&countryID=".$_REQUEST['countryID']."&cityID=".$_REQUEST['cityID']."&do=edit&ID=".$rw['ID']."' > 
		".($dontDisplayControl?"<img src='images/transparent.gif' width=12px height=1px border='none' />":"<a onclick='if(!confirm(\"آیا مایل به حذف این شهر هستید؟\"))return false;' href='./?cp=".$_REQUEST['cp']."&countryID=".$_REQUEST['countryID']."&cityID=".$_REQUEST['cityID']."&do=remove&ID=".$rw['ID']."' ><img src='images/delete_2.png' border='none' /></a>")."
		<input type=text name=Name value='".$rw['Name']."' >
		".($dontDisplayControl?"<img src='images/transparent.gif' width=70px height=1px border='none' />":"<input type=submit value='ویرایش' style='width:70px;' />")."
		‌</fo".$dontDisplayControl."rm>
		</td>
		</tr>
		";
	}
	echo "
	<form method=post action='./?cp=".$_REQUEST['cp']."&countryID=".$_REQUEST['countryID']."&cityID=".$_REQUEST['cityID']."&do=add' > 
	<tr height=30 ><td><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	<tr><td align=center >
	<img src='images/transparent.gif' width=12 height=12 border='none' />
	<input type=text name=Name value='هتل جدید' onclick='value=\"\"' >
	<input type=submit value='ثبت' style='width:70px;' />
	</td></tr>
	<tr height=30 ><td><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	‌</form>
	";
	echo "
	<tr height=100% ><td></td></tr>
	</table>";
}