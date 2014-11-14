<?

function _tour_new(){
	echo "
	<table width=80% align=center dir=rtl border=0 cellspacing=0 cellpadding=0 >
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	<tr height=30 >
		<td bgcolor=#f4f4f4 style='font-size:16px;'>&nbsp; &nbsp; <a style='font-size:16px;' href='./' >مدیریت</a> » ثبت تور</td>
		<td bgcolor=#f4f4f4 align=left ><span style='color:#aaaaaa; cursor:default;'>شما با نام کاربری ".mysql_result(dbq(" SELECT `UserName` FROM `tbl_agency` WHERE `ID`='".$_SESSION['ID']."' LIMIT 1 "),0,0)." وارد سایت شده اید</span><span style='cursor:default;'>&nbsp; /</span><a style='color:#aa4444;font-size:12px;padding:10px;' href=./?do=logout >خروج</a></td>
	</tr>
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	</table>
	";
	switch($_REQUEST['op']){
		case 'add' : 
			$ActivationStatus = mysql_result(dbq(" SELECT `ActivationStatus` FROM `tbl_setting` "),0,0);
			dbq(" INSERT INTO `tbl_tour` (
			 `AgencyID`,`Name`,`SourceID`,`DestinationID`
			,`HotelID`,`StartPrice`,`Date`,`Duration`
			,`Description`,`GroupID`,`On_Sale`,`enable`
			) VALUES (
			'".$_SESSION['ID']."','".$_REQUEST['Name']."','".$_REQUEST['SourceID']."','".$_REQUEST['DestinationID']."'
			,'".$_REQUEST['HotelID']."','".str_replace(",","",$_REQUEST['StartPrice'])."','".$_REQUEST['Date']."','".$_REQUEST['Duration']."'
			,'".$_REQUEST['Description']."','".$_REQUEST['GroupID']."','".intval($_REQUEST['On_Sale'])."','$ActivationStatus'
			) ");
			$ActivationStatus = mysql_result(dbq(" SELECT `ActivationStatus` FROM `tbl_setting` "),0,0);
			echo "<br><br><br><center>تور جدید ثبت شد<br>".($ActivationStatus?"":"برای فعال سازی تور از بخش لیست تور ها بر روی لینک فعال سازی تور مورد نظر کلیک کنید")."
			<br><a href='./?cp=3' >لیست تور ها</a>   ---   <a href='./?cp=".$_REQUEST['cp']."' >بازگشت</a></center>";
			return true;
	}
	?>
	<br><br>
	<table width=80% bgcolor=#cccccc cellspacing=1 cellpadding=0 align=center dir=rtl >
	<tr height=30 ><th bgcolor=#f8f8f8 align=center >ثبت تور جدید</th></tr>
	<tr><td bgcolor=white >
	<table width=80% bgcolor=white cellpadding=10 align=center >
	<form name=form0 method=post action="./?cp=<?=$_REQUEST['cp']?>&op=add" onsubmit="
	if(form0.Name.value==''){
		alert('عنوان وارد نشده است');
	} else if(form0.GroupID.value==''){
		alert('دسته وارد نشده است');
	} else if(form0.SourceID.value==''){
		alert('مبدا وارد نشده است');
	} else if(form0.DestinationID.value==''){
		alert('شهر مقصد وارد نشده است');
	} else if(form0.StartPrice.value==''){
		alert('هزینه وارد نشده است');
	} else if(form0.Date.value==''){
		alert('تاریخ وارد نشده است');
	} else if(form0.Duration.value==''){
		alert('مدت وارد نشده است');
	} else {
		return true;
	}
	return false;
	" >
	
		<tr height=30 ><td colspan=2 ></td></tr>
		<tr>
			<td>عنوان تور : </td>
			<td><input type=text name=Name /></td>
		</tr>
		<tr>
			<td>دسته : </td>
			<td>
				<select name="GroupID" >
				<option></option>
				<?
				if(!$rs = dbq(" SELECT * FROM `tbl_tourgroup` WHERE 1 ")){
					echo "err - ".__LINE__;
				} else if(!mysql_num_rows($rs)){
					echo "err - ".__LINE__;
				} else while($rw = mysql_fetch_assoc($rs)){
					echo "<option value='".$rw['ID']."' >".$rw['Name']."</option>";
				}
				?>
				</select>
			</td>
		</tr>
		<tr>
			<td>مبدا : </td>
			<td>
                <? echo "کشور : " ?>
				<select style="width: 150px" onchange="wget('./?do=get_city_list&CountryID='+this.value, 'SourceID', ''); " >
				<option></option>
				<?
				if(!$rs = dbq(" SELECT * FROM `tbl_country` WHERE 1 ORDER BY Name")){
					echo "err - ".__LINE__;
				} else if(!mysql_num_rows($rs)){
					echo "err - ".__LINE__;
				} else while($rw = mysql_fetch_assoc($rs)){
					echo "<option value='".$rw['ID']."' >".$rw['Name']."</option>";
				}
				?>
				</select>
                <? echo "شهر : " ?>
				<select name="SourceID" id="SourceID" style="width: 150px">
				</select>
		  </td>
		</tr>
		<tr>
			<td>مقصد : </td>
			<td>
            	<? echo "کشور : " ?>
				<select style="width: 150px" onchange="wget('./?do=get_city_list&CountryID='+this.value, 'DestinationID', '')" >
				<option></option>
				<?
				if(!$rs = dbq(" SELECT * FROM `tbl_country` WHERE 1  ORDER BY Name")){
					echo "err - ".__LINE__;
				} else if(!mysql_num_rows($rs)){
					echo "err - ".__LINE__;
				} else while($rw = mysql_fetch_assoc($rs)){
					echo "<option value='".$rw['ID']."' >".$rw['Name']."</option>";
				}
				?>
				</select>
                <? echo "شهر : " ?>
				<select style="width: 150px" onchange="wget('./?do=get_hotel_list&CityID='+this.value, 'HotelID', '')" name="DestinationID" id="DestinationID" ></select>
				<? echo "هتل : " ?>
                <select name="HotelID" id="HotelID" style="width: 150px" ></select>
			</td>
		</tr>
		<tr>
			<td>هزینه : </td>
			<td><input type=text dir=ltr name=StartPrice onkeyup="this.value=number_format(this.value);" /> (ریال) </td>
		</tr>
		<tr>
			<td>تاریخ : </td>
			<td>
				<input type=text dir=ltr name=Date id="Date" />
				<script type="text/javascript">$(document).ready(function(){$("#Date").datepicker({dateFormat: 'yy/mm/dd'});});</script>
			</td>
		</tr>
		<tr>
			<td>مدت : </td>
			<td><input type=text name=Duration /></td>
		</tr>
		<tr>
			<td>تخفیف (تور ویژه) : </td>
			<td><input type=checkbox name=On_Sale value=1 /></td>
		</tr>
		<tr>
			<td>توضیحات : </td>
			<td><textarea name=Description style="width:250px; height:80px;" ></textarea></td>
		</tr>
		<tr height=30 ><td colspan=2 ></td></tr>
		<tr>
			<td></td>
			<td><input type=submit value="ثبت تور جدید" style="width:160px;" /></td>
		</tr>
		<tr height=30 ><td colspan=2 ></td></tr>
	</table>
	</td></tr>
	</table>
	<br><br><br><br><br><br>
	<?
}

