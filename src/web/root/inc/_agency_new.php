<?

function _agency_new(){
	echo "
	<table width=80% align=center dir=rtl border=0 cellspacing=0 cellpadding=0 >
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	<tr height=30 >
		<td bgcolor=#f4f4f4 style='font-size:16px;'>&nbsp; &nbsp; <a style='font-size:16px;' href='./' >مدیریت</a> » ثبت آژانس</td>
		<td bgcolor=#f4f4f4 align=left ><span style='color:#aaaaaa; cursor:default;'>شما با نام کاربری ".mysql_result(dbq(" SELECT `UserName` FROM `tbl_agency` WHERE `ID`='".$_SESSION['ID']."' LIMIT 1 "),0,0)." وارد سایت شده اید</span><span style='cursor:default;'>&nbsp; /</span><a style='color:#aa4444;font-size:12px;padding:10px;' href=./?do=logout >خروج</a></td>
	</tr>
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	</table>
	";
	switch($_REQUEST['op']){
		case 'add' : 
			if(!$rs = dbq(" SELECT * FROM `tbl_agency` WHERE `Name`='".$_REQUEST['Name']."' OR `UserName`='".$_REQUEST['UserName']."' OR `Email`='".$_REQUEST['Email']."' LIMIT 1 ")){
				echo "err - ".__LINE__;
			} else if(mysql_num_rows($rs)!=0){
				echo "<script>alert('این آژانس قبلا ثبت شده است')</script>";
			} else if(!dbq(" INSERT INTO `tbl_agency` (`Name`,`Tel`,`Address`,`UserName`,`Password`,`Email`) VALUES ('".$_REQUEST['Name']."','".$_REQUEST['Tel']."','".$_REQUEST['Address']."','".$_REQUEST['UserName']."','".$_REQUEST['Password']."','".$_REQUEST['Email']."') ")){
				echo "err - ".__LINE__;
			} else {
				$ID = mysql_insert_id();
				$logo_name = "logo/".$ID.strrchr($_FILES['Logo']['name'],".");
				if($_FILES['Logo']['size']<=0){
					;// no file attached.
				} else if($_FILES['Logo']['size']>30*1024){
					echo "<script>alert('حداکثر حجم فایل لوگو 30KB میباشد')</script>";
				} else if(substr($_FILES['Logo']['type'],0,10)!='image/jpeg'){
					echo "<script>alert('نوع فایل لوگو مجاز نیست.')</script>";
				} else if(!$size = getimagesize($_FILES['Logo']['tmp_name'])){
					echo "err - ".__LINE__;
				} else if( ($size[0]!=90) or ($size[1]!=90) ){
					echo "<script>alert('سایز عکس ضمیمه شده مجاز نیست!')</script>";
				} else if(!copy($_FILES['Logo']['tmp_name'], $logo_name)){
					echo "err - ".__LINE__;
				} else if(!dbq(" UPDATE `tbl_agency` SET `Logo`='$logo_name' WHERE `ID`='$ID' ")){
					echo "err - ".__LINE__;
				}
				echo "<br><br><br><br><center dir=rtl >ثبت آژانس انجام شد.<br><br><a href='./?cp=".$_REQUEST['cp']."' >بازگشت</a></center>";
				return true;
			}
			break;
	}
	?>
	<br><br>
	<table width=80% bgcolor=#cccccc cellspacing=1 cellpadding=0 align=center dir=rtl >
	<tr height=30 ><th bgcolor=#f8f8f8 align=center >ثبت آژانس جدید</th></tr>
	<tr><td bgcolor=white >
	<table width=80% bgcolor=white cellpadding=10 align=center >
	<form method=post name=naForm enctype="multipart/form-data" action="./?cp=<?=$_REQUEST['cp']?>&op=add" onsubmit="
	if(naForm.Name.value==''){
		alert('نام بطور درست وارد نشده است.');
		return false;
	} else if(naForm.Tel.value==''){
		alert('شماره تماس درست وارد نشده است.');
		return false;
	} else if(naForm.Address.value==''){
		alert('آدرس درست وارد نشده است.');
		return false;
	} else if(naForm.UserName.value==''){
		alert('نام کاربری درست وارد نشده است.');
		return false;
	} else if(naForm.Password.value==''){
		alert('کلمه عبور درست وارد نشده است.');
		return false;
	} else if(naForm.Password.value!=naForm.Password2.value){
		alert('کلمه عبور درست وارد نشده است.');
		return false;
	} else if(naForm.Email.value==''){
		alert('آدرس ایمیل درست وارد نشده است.');
		return false;
	}
	" >
		<tr height=30 ><td colspan=2 ></td></tr>
		<tr>
			<td>نام کامل : </td>
			<td><input type=text name=Name /></td>
		</tr>
		<tr>
			<td>شماره تماس : </td>
			<td><input type=text name=Tel dir=ltr /></td>
		</tr>
		<tr>
			<td>آدرس : </td>
			<td><input type=text name=Address style="width:400px; text-align:right;" /></td>
		</tr>
		<tr>
			<td>لوگو : </td>
			<td><input type=file name=Logo /> (90px در 90px)  - (حداکثر 30 کیلوبایت)</td>
		</tr>
		<tr>
			<td>نام کاربری : </td>
			<td><input dir=ltr type=text name=UserName /></td>
		</tr>
		<tr>
			<td>کلمه عبور : </td>
			<td><input dir=ltr type=password name=Password /></td>
		</tr>
		<tr>
			<td>تکرار کلمه عبور : </td>
			<td><input dir=ltr type=password name=Password2 /></td>
		</tr>
		<tr>
			<td>آدرس ایمیل : </td>
			<td><input type=text name=Email dir=ltr /></td>
		</tr>
		<tr height=30 ><td colspan=2 ></td></tr>
		<tr>
			<td></td>
			<td><input type=submit value="ثبت آژانس" style="width:160px;" /></td>
		</tr>
		<tr height=30 ><td colspan=2 ></td></tr>
	</table>
	</td></tr>
	</table>
	<br><br><br><br><br><br>
	<?
}

