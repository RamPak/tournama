<?

function _do_login(){
	if(!$_SESSION['ID']){
		_do_login__form();
		return false;
	} else {
		return true;
	}
}
