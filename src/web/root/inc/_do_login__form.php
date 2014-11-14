<?

function _do_login__form(){
	switch($_REQUEST['op']){
		case 'login' : 
			if($_REQUEST['security_number']!=$_SESSION['security_number'])
			{				
				echo "<script>alert('Invalid security number.')</script>";
			} else if(!$rs = dbq(" SELECT * FROM `tbl_agency` WHERE `UserName`='".$_REQUEST['UserName']."' AND `Password`='".$_REQUEST['Password']."' LIMIT 1 ")){
				echo "err - ".__LINE__;
			} else if(!$rw = mysql_fetch_assoc($rs)){
				echo "<script>alert('Invalid UserName/password.')</script>";
			} else {
				$_SESSION['ID'] = $rw['ID'];
				echo "<script>location.href='./'</script>";
			}
			break;
	}
	?>
	<br><br><br><br><br><br><br><br><br>
<table dir=rtl align="center" width="50%" class="tx2">
	<tr><td>
	<form method="post" action="./?op=login" >
	<fieldset style="width:100% ">
	<legend align="right" dir=rtl style="font-size:14px;color:#aaaaff; font-weight:bold;">ورود </legend>
	<table dir=rtl width="100%" height="100%" class="TX2" cellpadding="5">
		<tr height="30"><td colspan="2">&nbsp;</td></tr>
		<tr>
			<td width="30"></td>
			<td>نام کاربری : </td>
			<td><input type="text" name="UserName" dir=ltr style="text-align:center; width:160px" class="TX2" ></td>
		</tr>
		<tr>
			<td></td>
			<td>کلمه عبور : </td>
			<td>
			<input type="password" name="Password" dir=ltr style="text-align:center; width:160px" class="TX2">
			</td>
		</tr>
		<tr>
			<td></td>
			<td>کد امنیتی : </td>
			<td>
				<input type="text" dir=ltr style="text-align:center; width:80px;" name="security_number" class="TX2">
				<img dir=rtl title="این قسمت برای مقابله با روبوت های brute force در نظر گرفته شده، با وارد کردن شماره در فرم ادامه دهید" src="./?do=security_number&nocache=<?=rand(10000000,99999999)?>" style="position:relative; top:5px; border:1px solid #ccc;" width="74" height="20" >
			</td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td>
				<input type="submit" value="ورود" style="font-family:tahoma; font-size:12px; background-color:#f9f9f9; width:78px; border-width:1px; ">
				<input type="button" onclick="location.href='./?do=register'" value="ثبت نام" style="font-family:tahoma; font-size:12px; background-color:#f9f9f9; width:78px; border-width:1px; ">
			</td>
		</tr>
		<tr height="20"><td colspan="3"></td></tr>
	</table>
	</fieldset>
	</form>
	</td></tr>
</table>
    
<p>&nbsp;</p>      
    <table width="222" align="center" dir=rtl>
	<tr>
	  <td width="96"><img src="release/android/logo.png" width="96" height="96" /></td>
	  <td width="104"><a href="release/android/NojaTour.apk">دانلود نسخه آندروید</a></td>
	</tr>
	</table>
    
<center class="TX2">&nbsp;<?=$_REQUEST['REPORT'] ?></center>
	<?
}


