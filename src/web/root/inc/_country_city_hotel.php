<?

function _country_city_hotel(){
	if($_REQUEST['cityID']){
		_country_city_hotel__hotel();
	} else if($_REQUEST['countryID']){
		_country_city_hotel__city();
	} else {
		_country_city_hotel__country();
	}
}

