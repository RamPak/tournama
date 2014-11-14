<?

function _setting(){
	echo "
	<table width=80% align=center dir=rtl border=0 cellspacing=0 cellpadding=0 >
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	<tr height=30 >
		<td bgcolor=#f4f4f4 style='font-size:16px;'>&nbsp; &nbsp; <a style='font-size:16px;' href='./' >مدیریت</a> » تنظیمات</td>
		<td bgcolor=#f4f4f4 align=left ><span style='color:#aaaaaa; cursor:default;'>شما با نام کاربری ".mysql_result(dbq(" SELECT `UserName` FROM `tbl_agency` WHERE `ID`='".$_SESSION['ID']."' LIMIT 1 "),0,0)." وارد سایت شده اید</span><span style='cursor:default;'>&nbsp; /</span><a style='color:#aa4444;font-size:12px;padding:10px;' href=./?do=logout >خروج</a></td>
	</tr>
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	</table>
	";
	switch($_REQUEST['do']){
		case 'save' : 
			if(!$_POST)break;
			dbq(" UPDATE `tbl_setting` SET `SystemName`='".$_REQUEST['SystemName']."',`activationprice`='".str_replace(",","",$_REQUEST['activationprice'])."',`clickprice`='".str_replace(",","",$_REQUEST['clickprice'])."',`ActivationStatus`='".$_REQUEST['ActivationStatus']."' ");
			break;
	}
	$rw = mysql_fetch_assoc(dbq(" SELECT * FROM `tbl_setting` "));
	echo "
	<br>
	<br>
	<form method=post action='./?cp=".$_REQUEST['cp']."&do=save'>
	<table width=80% dir=rtl bgcolor=#cccccc align=center cellspacing=1 cellpadding=10>
	<tr bgcolor=white >
		<td>نام سیستم</td>
		<td><input name=SystemName value='".$rw['SystemName']."' /></td>
	</tr>
	<tr bgcolor=white >
		<td>هزینه فعال سازی</td>
		<td><input dir=ltr name=activationprice onkeyup='this.value=number_format(this.value)' value='".number_format($rw['activationprice'])."' /></td>
	</tr>
	<tr bgcolor=white >
		<td>هزینه هر کلیک</td>
		<td><input dir=ltr name=clickprice onkeyup='this.value=number_format(this.value)' value='".number_format($rw['clickprice'])."' /></td>
	</tr>
	<tr bgcolor=white >
		<td>بخش مالی</td>
		<td><select name='ActivationStatus' id=ActivationStatus ><option value=0>فعال</option><option value=1>غیرفعال</option></select></td>
		<script>document.getElementById('ActivationStatus').value='".$rw['ActivationStatus']."'</script>
	</tr>
	<tr height=50 bgcolor=white >
		<td></td>
		<td><input type=submit value='ثبت تغییرات' /></td>
	</tr>
	</table>
	</form>
	";
}

?>
