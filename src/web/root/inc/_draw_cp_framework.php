<?

function _draw_cp_framework(){
	?>
	<table background="images/pannel-main-bg.png" width="100%" height="100%" dir="rtl" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#DADADA" width="1"><img src="s" width="1" height="1"></td>
		<td align="center" valign=top >
		<table width=80% align=center dir=rtl border=0 cellspacing=0 cellpadding=0 >
			<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
			<tr height=30 >
				<td bgcolor=#f4f4f4 style='font-size:16px;'>&nbsp; &nbsp; <a style='font-size:16px;' href='./' >مدیریت</a></td>
		<td bgcolor=#f4f4f4 align=left ><span style='color:#aaaaaa; cursor:default;'>شما با نام کاربری <?=mysql_result(dbq(" SELECT `UserName` FROM `tbl_agency` WHERE `ID`='".$_SESSION['ID']."' LIMIT 1 "),0,0)?> وارد سایت شده اید</span><span style='cursor:default;'>&nbsp; /</span><a style='color:#aa4444;font-size:12px;padding:10px;' href=./?do=logout >خروج</a></td>
			</tr>
			<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
		</table>
		<table dir="rtl" cellpadding="20" cellspacing="0" width=80%>
			<tr>
				<td align=center ><a href="./?cp=<?=++$i?>"><img src="images/cp_icon.png" border="none" /><br>کشور/شهر/هتل</a></td>
				<td align=center ><a href="./?cp=<?=++$i?>"><img src="images/cp_icon.png" border="none" /><br>ثبت تور</a></td>
				<td align=center ><a href="./?cp=<?=++$i?>"><img src="images/cp_icon.png" border="none" /><br>لیست تور ها</a></td>
				<td align=center ><a href="./?cp=<?=++$i?>"><img src="images/cp_icon.png" border="none" /><br>تغییر مشخصات</a></td>
				<td align=center ><a href="./?cp=21"><img src="images/cp_icon.png" border="none" /><br>ارتباط با ما</a></td>
				<td align=center ></td>
			</tr>
		‌	<?
			if($_SESSION['ID']==1){
			?>
			<tr>
				<td align=center ><a href="./?cp=<?=++$i?>"><img src="images/cp_icon.png" border="none" /><br>تایید پرداخت ها</a></td>
				<td align=center ><a href="./?cp=<?=++$i?>"><img src="images/cp_icon.png" border="none" /><br>ثبت آژانس</a></td>
				<td align=center ><a href="./?cp=<?=++$i?>"><img src="images/cp_icon.png" border="none" /><br>لیست آژانس ها</a></td>
				<td align=center ><a href="./?cp=<?=++$i?>"><img src="images/cp_icon.png" border="none" /><br>گروهبندی تور ها</a></td>
				<td align=center ><a href="./?cp=<?=++$i?>"><img src="images/cp_icon.png" border="none" /><br>تبلیغات</a></td>
				<td align=center ><a href="./?cp=<?=++$i?>"><img src="images/cp_icon.png" border="none" /><br>تنظیمات</a></td>
			</tr>
			<?
			}
			?>
		</table>
		</td>
		<td  bgcolor="#DADADA" width="1"><img src="s" width="1" height="1"></td>
	</tr>
	<tr height="20">
		<td  bgcolor="#DADADA" width="1"><img src="s" width="1" height="1"></td>
		<td></td>
		<td  bgcolor="#DADADA" width="1"><img src="s" width="1" height="1"></td>
	</tr>
	</table>
	<?
}

