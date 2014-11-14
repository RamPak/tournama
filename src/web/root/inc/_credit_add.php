<?

function _credit_add(){
	if(!$ID = $_REQUEST['ID'])die();
	echo "
	<table width=80% align=center dir=rtl border=0 cellspacing=0 cellpadding=0 >
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	<tr height=30 >
		<td bgcolor=#f4f4f4 style='font-size:16px;'>&nbsp; &nbsp; <a style='font-size:16px;' href='./' >مدیریت</a> » ثبت اعتبار</td>
		<td bgcolor=#f4f4f4 align=left ><span style='color:#aaaaaa; cursor:default;'>شما با نام کاربری ".mysql_result(dbq(" SELECT `UserName` FROM `tbl_agency` WHERE `ID`='".$_SESSION['ID']."' LIMIT 1 "),0,0)." وارد سایت شده اید</span><span style='cursor:default;'>&nbsp; /</span><a style='color:#aa4444;font-size:12px;padding:10px;' href=./?do=logout >خروج</a></td>
	</tr>
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	</table>
	";
	switch($_REQUEST['op2']){
		case 'add' : 
			dbq(" INSERT INTO `tbl_credit` (`TourID`,`Credit`,`Serial`,`date`,`time`,`cardnumber`) 
			VALUES ('$ID','".intval($_REQUEST['Credit'])."','".$_REQUEST['Serial']."','".$_REQUEST['date']."','".$_REQUEST['time']."','".$_REQUEST['cardnumber']."') ");
			echo "<br><br><br><br><center dir=rtl >ثبت پرداخت انجام شد.<br>اعتبار ثبت شده پس از تایید توسط مدیریت به تور شما اضافه خواهد شد<br><a href='./?cp=".$_REQUEST['cp']."' >بازگشت</a></center>";
			return true;
	}
	?>
	<br><br>
	<table width=80% bgcolor=#cccccc cellspacing=1 cellpadding=0 align=center dir=rtl >
	<tr height=30 ><th bgcolor=#f8f8f8 align=center >ثبت اعتبار</th></tr>
	<tr><td bgcolor=white >
	<table width=80% bgcolor=white cellpadding=10 align=center >
	<script>
	var selFlag = false;
	</script>
	<form method=post name=chForm enctype="multipart/form-data" action="./?cp=<?=$_REQUEST['cp']?>&op=<?=$_REQUEST['op']?>&ID=<?=$ID?>&op2=add" onsubmit="
	if(!selFlag){
		alert('لطفا مبلغ شارژ را انتخاب نمایید');
		return false;
	} else if(chForm.Serial.value==''){
		alert('لطفا سریال پرداخت را انتخاب نمایید');
		return false;
	} else if(chForm.date.value==''){
		alert('لطفا تاریخ پرداخت را انتخاب نمایید');
		return false;
	} else if( (chForm.time.value=='--:--') || (chForm.time.value=='')){
		alert('لطفا ساعت پرداخت را انتخاب نمایید');
		return false;
	}
	" >
		<tr height=30 ><td colspan=2 ></td></tr>
		<tr>
			<td colspan=2 >ثبت اعتبار بابت تور <b><?=mysql_result(dbq(" SELECT `Name` FROM `tbl_tour` WHERE `ID`='$ID' LIMIT 1 "),0,0)?></b></td>
			<td></td>
		</tr>
		<tr>
			<td></td>
			<td><label><input type=radio onclick="selFlag=true" name="Credit" value="10000" /> اعتبار به مبلغ ۱۰.۰۰۰ ریال</label></td>
		</tr>
		<tr>
			<td></td>
			<td><label><input type=radio onclick="selFlag=true" name="Credit" value="20000" /> اعتبار به مبلغ ۲۰.۰۰۰ ریال</label></td>
		</tr>
		<tr>
			<td></td>
			<td><label><input type=radio onclick="selFlag=true" name="Credit" value="50000" /> اعتبار به مبلغ ۵۰.۰۰۰ ریال</label></td>
		</tr>
		<tr>
			<td></td>
			<td><label><input type=radio onclick="selFlag=true" name="Credit" value="100000" /> اعتبار به مبلغ ۱۰۰.۰۰۰ ریال</label></td>
		</tr>
		<tr>
			<td></td>
			<td><label><input type=radio onclick="selFlag=true" name="Credit" value="1000000" /> اعتبار به مبلغ ۱.۰۰۰.۰۰۰ ریال</label></td>
		</tr>
		<tr height=30 ><td colspan=2 ></td></tr>
		<tr>
			<td>کد پیگیری/شماره فیش‌ : </td>
			<td><input type=text name="Serial" dir=ltr style="width:160px;" /></td>
		</tr>
		<tr>
			<td>تاریخ و ساعت پرداخت : </td>
			<td>
				<input type=text dir=ltr name="date" dir=ltr id="Date" style="width:80px;" />
				<script type="text/javascript">$(document).ready(function(){$("#Date").datepicker({dateFormat: 'yy/mm/dd'});});</script>
				<input type=text dir=ltr name="time" style="width:80px;" value="--:--" onclick="value=''" />
			</td>
		</tr>
		<tr>
			<td>۴ رقم آخر شماره کارت</td>
			<td>
				<input type=text name="cardnumber" dir=ltr style="width:80px;" maxlength=4 />
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type=submit value="ثبت اعتبار" style="width:160px;" /></td>
		</tr>
		<tr height=30 ><td colspan=2 ></td></tr>
	</table>
	</td></tr>
	</table>
	<br><br><br><br><br><br>
	<?
}

