<?

function _credit_confirm(){
	switch($_REQUEST['do']){
		
		case 'confirm' : 
			if(!$ID = $_REQUEST['ID']){
				echo "err - ".__LINE__;
			} else if(!$rs = dbq(" SELECT * FROM `tbl_credit` WHERE `ID`='$ID' AND `Status`='0' LIMIT 1 ")){
				echo "err - ".__LINE__;
			} else if(!$rw = mysql_fetch_assoc($rs)){
				echo "err - ".__LINE__;
			} else if(!dbq(" UPDATE `tbl_credit` SET `Status`=1 WHERE `ID`='$ID' LIMIT 1 ")){
				echo "err - ".__LINE__;
			} else {
				if($rw['paytype']=='activation'){
					if(!dbq(" UPDATE `tbl_tour` SET `enable`=1 WHERE `ID`='".$rw['TourID']."' LIMIT 1 ")){
						echo "err - ".__LINE__;
					}
				} else {
					if(!dbq(" UPDATE `tbl_tour` SET `Credit`=`Credit`+".$rw['Credit']." WHERE `ID`='".$rw['TourID']."' LIMIT 1 ")){
						echo "err - ".__LINE__;
					}
				}
			}
			break;
			
		case 'remove' : 
			if(!$ID = $_REQUEST['ID']){
				echo "err - ".__LINE__;
			} else if(!dbq(" DELETE FROM `tbl_credit` WHERE `ID`='$ID' LIMIT 1 ")){
				echo "err - ".__LINE__;
			} else {
				;// OK
			}
			break;
	}
	echo "
	<table width=80% align=center dir=rtl border=0 cellspacing=0 cellpadding=0 >
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	<tr height=30 >
		<td bgcolor=#f4f4f4 style='font-size:16px;'>&nbsp; &nbsp; <a style='font-size:16px;' href='./' >مدیریت</a> » تایید اعتبار</td>
		<td bgcolor=#f4f4f4 align=left ><span style='color:#aaaaaa; cursor:default;'>شما با نام کاربری ".mysql_result(dbq(" SELECT `UserName` FROM `tbl_agency` WHERE `ID`='".$_SESSION['ID']."' LIMIT 1 "),0,0)." وارد سایت شده اید</span><span style='cursor:default;'>&nbsp; /</span><a style='color:#aa4444;font-size:12px;padding:10px;' href=./?do=logout >خروج</a></td>
	</tr>
	<tr height=30 ><td colspan=2 ><hr color=#cccccc size=1px style='height:1px; border:1px solid #eee;' ></td></tr>
	</table>
	";
	?>
	<br><br>
	<table width=80% bgcolor=#cccccc cellspacing=1 cellpadding=10 align=center dir=rtl >
	<tr height=30 ><th bgcolor=#f0f0f0 align=center colspan=7 >تایید اعتبار</th></tr>
		<?
		$p = intval($_REQUEST['p']);
		$tdd = 10;
		$stt = $tdd * $p;
		if(!$rs = dbq(" SELECT * FROM `tbl_credit` WHERE 1 ORDER BY `Status` ASC, `ID` DESC LIMIT $stt,$tdd ")){
			echo "err - ".__LINE__;
			echo mysql_error();
		} else if(!mysql_num_rows($rs)){
			echo "<tr bgcolor=white height=30 ><th colspan=7 >هنوز موردی ثبت نشده است!</th></tr>";
		} else while($rw = mysql_fetch_assoc($rs)){
			$tour = @mysql_result(dbq(" SELECT `Name` FROM `tbl_tour` WHERE `ID`='".$rw['TourID']."' LIMIT 1 "), 0, 0);
			if($tour==''){
				$tour = "<font color=red >تور حذف شده</font>";
			}
			?>
			<tr bgcolor=<?=($rw['Status']==0?"white":"#ffeeee")?> >
				<td><?=number_format($rw['Credit'])?> ریال</td>
				<th><?=$tour?></th>
				<td align=center ><?=($rw['paytype']=='activation'?"فعالسازی":"شارژ")?></td>
				<td align=center ><?=$rw['Serial']?></td>
				<td align=center ><?=$rw['date']?></td>
				<td align=center ><?=$rw['time']?></td>
				<th width=80 >
					<? if($rw['Status']==0){ ?>
					<a href='./?cp=<?=$_REQUEST['cp']?>&ID=<?=$rw['ID']?>&do=confirm'>تایید</a>
					-
					<a onclick="if(!confirm('آیا اطمینان به حذف این ردیف دارید؟'))return false;" href='./?cp=<?=$_REQUEST['cp']?>&ID=<?=$rw['ID']?>&do=remove'>حذف</a>
					<? } ?>
				</th>
			</tr>
			<?
		}
		?>
		<tr height=30 bgcolor=#f0f0f0 ><td colspan=7 ></td></tr>
	</table>
	<br><br><br><br><br><br>
	<?
}

