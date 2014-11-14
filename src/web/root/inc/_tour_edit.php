<?

function _tour_edit(){
	switch($_REQUEST['op2']){
		case 'save' : 
			if(!dbq(" UPDATE `tbl_tour` SET 
			 `Name`='".$_REQUEST['Name']."'
			,`GroupID`='".$_REQUEST['GroupID']."'
			,`SourceID`='".$_REQUEST['SourceID']."'
			,`DestinationID`='".$_REQUEST['DestinationID']."'
			,`HotelID`='".$_REQUEST['HotelID']."'
			,`StartPrice`='".str_replace(",","",$_REQUEST['StartPrice'])."'
			,`Date`='".$_REQUEST['Date']."'
			,`Duration`='".$_REQUEST['Duration']."'
			,`On_Sale`='".decbin($_REQUEST['On_Sale'])."'
			,`Description`='".$_REQUEST['Description']."'
			WHERE `ID`='".intval($_REQUEST['ID'])."' ".($_SESSION['ID']==1?"":" AND `AgencyID`='".$_SESSION['ID']."' ")." LIMIT 1
			")){
				echo "err - ".__LINE__;
			} else {
				echo "<script>location.href='./?cp=".$_REQUEST['cp']."'</script>";
			}
			return true;
	}
	echo "
	<table width=80% align=center dir=rtl border=0 cellspacing=0 cellpadding=0 >
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	<tr height=30 >
		<td bgcolor=#f4f4f4 style='font-size:16px;'>&nbsp; &nbsp; <a style='font-size:16px;' href='./' >مدیریت</a> » ویرایش تور</td>
		<td bgcolor=#f4f4f4 align=left ><span style='color:#aaaaaa; cursor:default;'>شما با نام کاربری ".mysql_result(dbq(" SELECT `UserName` FROM `tbl_agency` WHERE `ID`='".$_SESSION['ID']."' LIMIT 1 "),0,0)." وارد سایت شده اید</span><span style='cursor:default;'>&nbsp; /</span><a style='color:#aa4444;font-size:12px;padding:10px;' href=./?do=logout >خروج</a></td>
	</tr>
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	</table>
	";
	if(!$ID = $_REQUEST['ID']){
		die();
	}
	$rw = mysql_fetch_assoc(dbq(" SELECT * FROM `tbl_tour` WHERE `ID`='$ID' ".($_SESSION['ID']==1?"":" AND `AgencyID`='".$_SESSION['ID']."' ")." LIMIT 1 "));
	?>
	<br><br>
	<table width=80% bgcolor=#cccccc cellspacing=1 cellpadding=0 align=center dir=rtl >
	<tr height=30 ><th bgcolor=#f8f8f8 align=center >ویرایش تور</th></tr>
	<tr><td bgcolor=white >
	<table width=80% bgcolor=white cellpadding=10 align=center >
	<form name=form0 method=post action="./?cp=<?=$_REQUEST['cp']?>&op=<?=$_REQUEST['op']?>&ID=<?=$rw['ID']?>&op2=save" onsubmit="
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
			<td><input type=text name=Name value="<?=$rw['Name']?>" /></td>
		</tr>
		<tr>
			<td>دسته : </td>
			<td>
				<select name="GroupID" id="GroupID" >
				<?
				if(!$rs0 = dbq(" SELECT * FROM `tbl_tourgroup` WHERE 1 ")){
					echo "err - ".__LINE__;
				} else if(!mysql_num_rows($rs0)){
					echo "err - ".__LINE__;
				} else while($rw0 = mysql_fetch_assoc($rs0)){
					echo "<option value='".$rw0['ID']."' >".$rw0['Name']."</option>";
				}
				?>
				</select>
				<script>document.getElementById('GroupID').value='<?=$rw['GroupID']?>';</script>
			</td>
		</tr>
		<tr>
			<td>مبدا : </td>
			<td>
                <? echo "کشور : " ?>
				<select style="width: 150px" onchange="wget('./?do=get_city_list&CountryID='+this.value+'&select=<?=$rw['SourceID']?>', 'SourceID', '')" id="SourceCountry" >
				<?
				if(!$rs0 = dbq(" SELECT * FROM `tbl_country` WHERE 1  ORDER BY Name")){
					echo "err - ".__LINE__;
				} else if(!mysql_num_rows($rs0)){
					echo "err - ".__LINE__;
				} else while($rw0 = mysql_fetch_assoc($rs0)){
					echo "<option value='".$rw0['ID']."' >".$rw0['Name']."</option>";
				}
				?>
				</select>
				<? $SourceCountry = mysql_result(dbq(" SELECT `CountryID` FROM `tbl_city` WHERE `ID`='".$rw['SourceID']."' LIMIT 1 "),0,0);?>
				<script>document.getElementById('SourceCountry').value='<?=$SourceCountry?>';</script>
				<? echo "شهر : " ?>
				<select name="SourceID" id="SourceID" style="width: 150px" ></select>
				<script>
				wget('./?do=get_city_list&CountryID=<?=$SourceCountry?>&select=<?=$rw['SourceID']?>', 'SourceID', '');
				document.getElementById('SourceID').value='<?=$rw['SourceID']?>';
				</script>
				
			</td>
		</tr>
		<tr>
			<td>مقصد : </td>
			<td>
                <? echo "کشور : " ?>
				<select style="width: 150px" onchange="wget('./?do=get_city_list&CountryID='+this.value+'&select=<?=$rw['SourceID']?>', 'DestinationID', '')" id="DestinationCountry" >
				<?
				if(!$rs0 = dbq(" SELECT * FROM `tbl_country` WHERE 1  ORDER BY Name")){
					echo "err - ".__LINE__;
				} else if(!mysql_num_rows($rs0)){
					echo "err - ".__LINE__;
				} else while($rw0 = mysql_fetch_assoc($rs0)){
					echo "<option value='".$rw0['ID']."' >".$rw0['Name']."</option>";
				}
				?>
				</select>
				<? $DestinationCountry = mysql_result(dbq(" SELECT `CountryID` FROM `tbl_city` WHERE `ID`='".$rw['DestinationID']."' LIMIT 1 "),0,0);?>
				<script>document.getElementById('DestinationCountry').value='<?=$DestinationCountry?>';</script>
				
                <? echo "شهر : " ?>
				<select style="width: 150px" onchange="wget('./?do=get_hotel_list&CityID='+this.value, 'HotelID', '')" name="DestinationID" id="DestinationID" ></select>
				<script>
				wget('./?do=get_city_list&CountryID=<?=$DestinationCountry?>&select=<?=$rw['DestinationID']?>', 'DestinationID', '');
				document.getElementById('DestinationID').value='<?=$rw['DestinationID']?>';
				</script>
				<? echo "هتل : " ?>
				<select name="HotelID" id="HotelID" style="width: 150px" ></select>
				<script>
				wget('./?do=get_hotel_list&CityID=<?=$rw['DestinationID']?>&select=<?=$rw['HotelID']?>', 'HotelID', '');
				document.getElementById('HotelID').value='<?=$rw['HotelID']?>';
				</script>
				
			</td>
		</tr>
		<tr>
			<td>هزینه : </td>
			<td><input type=text name=StartPrice dir=ltr value="<?=number_format($rw['StartPrice'])?>" onkeyup="this.value=number_format(this.value);" /> (ریال) </td>
		</tr>
		<tr>
			<td>تاریخ : </td>
			<td>
				<input type=text dir=ltr name=Date id="Date" value="<?=$rw['Date']?>" />
				<script  type="text/javascript">$(document).ready(function(){$("#Date").datepicker({dateFormat: 'yy/mm/dd'});});</script>
			</td>
		</tr>
		<tr>
			<td>مدت : </td>
			<td><input type=text name=Duration value="<?=$rw['Duration']?>" /></td>
		</tr>
		<tr>
			<td>تخفیف (تور ویژه) : </td>
			<td><input type=checkbox name=On_Sale value=1 <?=($rw['On_Sale']==1?"checked":"")?> /></td>
		</tr>
		<tr>
			<td>توضیحات : </td>
			<td><textarea name=Description style="width:250px; height:80px;" ><?=$rw['Description']?></textarea></td>
		</tr>
		<tr height=30 ><td colspan=2 ></td></tr>
		<tr>
			<td></td>
			<td><input type=submit value="ویرایش تور" style="width:160px;" /></td>
		</tr>
		<tr height=30 ><td colspan=2 ></td></tr>
	</form>
	</table>
	</td></tr>
	</table>
	<br><br><br><br><br><br>
	<?
}

