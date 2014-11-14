<?

function _contact(){
	echo "
	<table width=80% align=center dir=rtl border=0 cellspacing=0 cellpadding=0 >
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	<tr height=30 >
		<td bgcolor=#f4f4f4 style='font-size:16px;'>&nbsp; &nbsp; <a style='font-size:16px;' href='./' >مدیریت</a> » ارتباط با ما </td>
		<td bgcolor=#f4f4f4 align=left ><span style='color:#aaaaaa; cursor:default;'>شما با نام کاربری ".mysql_result(dbq(" SELECT `UserName` FROM `tbl_agency` WHERE `ID`='".$_SESSION['ID']."' LIMIT 1 "),0,0)." وارد سایت شده اید</span><span style='cursor:default;'>&nbsp; /</span><a style='color:#aa4444;font-size:12px;padding:10px;' href=./?do=logout >خروج</a></td>
	</tr>
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	</table>
	";
	echo '<br>
	<center dir=rtl >
	<fieldset style="width:80% ">
	<legend align="right" dir=rtl style="font-size:14px;color:#aaaaff; font-weight:bold;"> ارتباط با ما </legend>
	<br><br>';
	echo "<div style='width:95%; text-align:justify; '>";
	echo "<hr style='width:100%; border:1px dotted #888888'><br>";
	echo implode("<br>",file("docs/contact.txt"));
	echo "<br><br><hr style='width:100%; border:1px dotted #888888'><br><br><br>";
	echo "</div></center>";
	echo "</fieldset><br><br><br>";
}

