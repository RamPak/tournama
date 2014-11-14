<?

function _agency_list(){
	switch($_REQUEST['op']){
		case 'remove' : 
			if(!$ID = intval($_REQUEST['ID'])){
				echo "err - ".__LINE__;
			} else if(!$rs = dbq(" SELECT COUNT(*) FROM `tbl_tour` WHERE `AgencyID`='$ID' ")){
				echo "err - ".__LINE__;
			} else if(mysql_result($rs,0,0)!=0){
				echo "<script>alert('هنوز از این آژانس تور موجود هست')</script>";
			} else {
				dbq(" DELETE FROM `tbl_agency` WHERE `ID`='$ID' LIMIT 1 ");
			}
			break;
		case 'editform' : 
			_agency_edit();
			return true;
		case 'edit' : 
			_agency_edit();
			return true;
	}
	echo "
	<table width=80% align=center dir=rtl border=0 cellspacing=0 cellpadding=0 >
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	<tr height=30 >
		<td bgcolor=#f4f4f4 style='font-size:16px;'>&nbsp; &nbsp; <a style='font-size:16px;' href='./' >مدیریت</a> » لیست آژانس ها</td>
		<td bgcolor=#f4f4f4 align=left ><span style='color:#aaaaaa; cursor:default;'>شما با نام کاربری ".mysql_result(dbq(" SELECT `UserName` FROM `tbl_agency` WHERE `ID`='".$_SESSION['ID']."' LIMIT 1 "),0,0)." وارد سایت شده اید</span><span style='cursor:default;'>&nbsp; /</span><a style='color:#aa4444;font-size:12px;padding:10px;' href=./?do=logout >خروج</a></td>
	</tr>
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	</table>
	";
	echo "<table dir=rtl width=80% cellpadding=10 cellspacing=1 bgcolor=#cccccc align=center >";
	echo "
	<tr bgcolor=#eeeeee >
		<th>نام</th>
		<th>تلفن</th>
		<th>آدرس</th>
		<th>نام کاربری</th>
		<th>کلمه عبور</th>
		<th>آدرس ایمیل</th>
		<th>--</th>
	</tr>
	";
	$tdd = 20;
	$p = intval($_REQUEST['p']);
	$stt = $tdd * $p;
	if(!$rs = dbq(" SELECT * FROM `tbl_agency` WHERE 1 ORDER BY Name LIMIT $stt,$tdd ")){
		echo "err - ".__LINE__;
	} else if(!mysql_num_rows($rs)){
		echo "err - ".__LINE__;
	} else while($rw = mysql_fetch_assoc($rs)){
		echo "
		<tr bgcolor=".($rw['ID']==1?"#ffffcc":"#ffffff")." >
			<td>".$rw['Name']."</td>
			<td dir=ltr >".$rw['Tel']."</td>
			<td>".$rw['Address']."</td>
			<td align=center >".$rw['UserName']."</td>
			<td align=center >".$rw['Password']."</td>
			<td align=center >".$rw['Email']."</td>
			<td align=center >
				<a ".($rw['ID']==1?"":" onclick='if(!confirm(\"آیا مایل به حذف این آژانس هستید؟\"))return false;' href='./?cp=".$_REQUEST['cp']."&op=remove&ID=".$rw['ID']."'")." >حذف</a>
				-
				<a href='./?cp=".$_REQUEST['cp']."&op=editform&ID=".$rw['ID']."'>ویرایش</a>
			</td>
		</tr>
		";
	}
	if(!$rs = dbq(" SELECT COUNT(*) FROM `tbl_agency` WHERE 1 ")){
		echo "err - ".__LINE__;
	} else if(!$nm = mysql_result($rs,0,0)){
		;//echo "err - ".__LINE__;
	} else {
		$pg = ceil($nm / $tdd);
		if($pg>1){
			echo "<tr><td colspan=7 bgcolor=#dddddd > صفحه ";
			for($i=0; $i<$pg; $i++){
				if($i == $p){
					echo "<b> ".($i+1)."&nbsp;</b>";
				} else {
					echo " <a href='./?cp=".$_REQUEST['cp']."&p=$i'>".($i+1)."</a>&nbsp;";
				}
			}
			echo "</td></tr>";
		}
	}
	echo "</table>";
}

