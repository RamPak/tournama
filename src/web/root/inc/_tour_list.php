<?

function _tour_list(){
	switch($_REQUEST['op']){
		case 'remove' : 
			if(!$ID = intval($_REQUEST['ID'])){
				echo "err - ".__LINE__;
			} else if(!dbq(" DELETE FROM `tbl_tour` WHERE `ID`='$ID' LIMIT 1 ")){
				echo "err - ".__LINE__;
			} else {
				;//
			}
			break;
			
		case 'enable' : 
			dbq(" UPDATE `tbl_tour` SET `enable`=MOD(`enable`+1,2) WHERE `ID`='".intval($_REQUEST['ID'])."' LIMIT 1 ");
			break;
			
		case 'edit' : 
			_tour_edit();
			return true;
		
		case 'addcredit' : 
			_credit_add();
			return true;
			
		case 'addcreditForActivation' : 
			_credit_addcreditForActivation();
			return true;
	}
	echo "
	<table width=80% align=center dir=rtl border=0 cellspacing=0 cellpadding=0 >
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	<tr height=30 >
		<td bgcolor=#f4f4f4 style='font-size:16px;'>&nbsp; &nbsp; <a style='font-size:16px;' href='./' >مدیریت</a> » لیست تورها</td>
		<td bgcolor=#f4f4f4 align=left ><span style='color:#aaaaaa; cursor:default;'>شما با نام کاربری ".mysql_result(dbq(" SELECT `UserName` FROM `tbl_agency` WHERE `ID`='".$_SESSION['ID']."' LIMIT 1 "),0,0)." وارد سایت شده اید</span><span style='cursor:default;'>&nbsp; /</span><a style='color:#aa4444;font-size:12px;padding:10px;' href=./?do=logout >خروج</a></td>
	</tr>
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	</table>
	";
	echo "<table dir=rtl width=80% cellpadding=10 cellspacing=1 bgcolor=#cccccc align=center >";
	echo "
	<tr bgcolor=#eeeeee >
		<th>آژانس</th>
		<th>عنوان تور</th>
		<th>دسته</th>
		<th>مبدا</th>
		<th>مقصد</th>
		<th>هتل</th>
		<th>هزینه (ریال)</th>
		<th>تاریخ</th>
		<th>مدت</th>
		<th>اعتبار</th>
		<th title='تعداد کلیک باقی مانده' >کلیک</th>
		<th>وضعیت</th>
		<th>--</th>
	</tr>
	";
	$tdd = 20;
	$p = intval($_REQUEST['p']);
	$stt = $tdd * $p;
	if(!$rs = dbq(" SELECT * FROM `tbl_tour` WHERE 1 ".($_SESSION['ID']==1?"":" AND `AgencyID`='".$_SESSION['ID']."' ")." ".($_REQUEST['AgencyID']?" AND `AgencyID`='".$_REQUEST['AgencyID']."' ":"")." ORDER BY Date LIMIT $stt,$tdd ")){
		echo "err - ".__LINE__;
	} else if(!mysql_num_rows($rs)){
		echo "<tr height=50 bgcolor=white ><th colspan=13 >موردی یافت نشد</th></tr>";
	} else while($rw = mysql_fetch_assoc($rs)){
		echo "
		<tr bgcolor=".($rw['On_Sale']==1?"#ffffcc":"#ffffff")." >
			<td><a href='./?cp=".$_REQUEST['cp']."&AgencyID=".$rw['AgencyID']."' >".@mysql_result(dbq(" SELECT `Name` FROM `tbl_agency` WHERE `ID`='".$rw['AgencyID']."' LIMIT 1 "),0,0)."</a></td>
			<td>".$rw['Name']."</td>
			<td align=center >".@mysql_result(dbq(" SELECT `Name` FROM `tbl_tourgroup` WHERE `ID`='".$rw['GroupID']."' LIMIT 1 "),0,0)."</td>
			<td align=center >".@mysql_result(dbq(" SELECT `Name` FROM `tbl_city` WHERE `ID`='".$rw['SourceID']."' LIMIT 1 "),0,0)."</td>
			<td align=center >".@mysql_result(dbq(" SELECT `Name` FROM `tbl_city` WHERE `ID`='".$rw['DestinationID']."' LIMIT 1 "),0,0)."</td>
			<td align=center >".@mysql_result(dbq(" SELECT `Name` FROM `tbl_hotel` WHERE `ID`='".$rw['HotelID']."' LIMIT 1 "),0,0)."</td>
			<td>".number_format($rw['StartPrice'])."</td>
			<td>".$rw['Date']."</td>
			<td>".$rw['Duration']."</td>
			<td>".number_format($rw['Credit'])."</td>
			<td align=center >".round($rw['Credit']/mysql_result(dbq(" SELECT `clickprice` FROM `tbl_setting` "),0,0))."</td>
			<td align=center ><a ".($_SESSION['ID']==1?"href='./?cp=".$_REQUEST['cp']."&op=enable&ID=".$rw['ID']."'":"")." >".($rw['enable']==1?"<font color=green>فعال</font>":"<font color=red>غیرفعال</font>")."</a></td>
			<td align=center >
				<a onclick='if(!confirm(\"آیا مایل به حذف این تور هستید؟\"))return false;' href='./?cp=".$_REQUEST['cp']."&op=remove&ID=".$rw['ID']."'>حذف</a>
				-
				<a href='./?cp=".$_REQUEST['cp']."&op=edit&ID=".$rw['ID']."'>ویرایش</a>
				-
				".($rw['enable']==1?"<a href='./?cp=".$_REQUEST['cp']."&op=addcredit&ID=".$rw['ID']."'>ثبت اعتبار</a>":(mysql_result(dbq(" SELECT COUNT(*) FROM `tbl_credit` WHERE `TourID`='".$rw['ID']."' AND `paytype`='activation' AND `Status`=0 LIMIT 1 "),0,0)==0?"<a href='./?cp=".$_REQUEST['cp']."&op=addcreditForActivation&ID=".$rw['ID']."'>فعالسازی</a>":"<i style='color:green; cursor:default;'>در انتظار تایید</i>"))."
			</td>
		</tr>
		<tr bgcolor=#eeeeee ><td colspan=13 ></td></tr>
		";
	}
	if(!$rs = dbq(" SELECT COUNT(*) FROM `tbl_tour` WHERE 1 ".($_REQUEST['AgencyID']?" AND `AgencyID`='".$_REQUEST['AgencyID']."' ":"")." ")){
		echo "err - ".__LINE__;
	} else if(!$nm = mysql_result($rs,0,0)){
		;//echo "err - ".__LINE__;
	} else {
		$pg = ceil($nm / $tdd);
		if($pg>1){
			echo "<tr><td colspan=13 bgcolor=#dddddd > صفحه ";
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

