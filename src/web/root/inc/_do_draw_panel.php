<?

function _do_draw_panel(){
	switch(intval($_REQUEST['cp'])){
		case 1 : 
			_country_city_hotel();
			break;
			
		case 2 : 
			_tour_new();
			break;
			
		case 3 : 
			_tour_list();
			break;
			
		case 4 : 
			_agency_edit();
			break;
			
		case 21 : 
			_contact();
			break;
			
		case 5 : 
			if($_SESSION['ID']!=1)die();
			_credit_confirm();
			break;
			
		case 6 : 
			if($_SESSION['ID']!=1)die();
			_agency_new();
			break;
			
		case 7 : 
			if($_SESSION['ID']!=1)die();
			_agency_list();
			break;
			
		case 8 : 
			if($_SESSION['ID']!=1)die();
			_tour_cat();
			break;
			
		case 9 : 
			if($_SESSION['ID']!=1)die();
			_ads();
			break;
			
		case 10 : 
			if($_SESSION['ID']!=1)die();
			_setting();
			break;
			
		DEFAULT : 
			_draw_cp_framework();
			break;
	}
}

