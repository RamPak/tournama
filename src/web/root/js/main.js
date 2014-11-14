
//<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>__________________
function HighLight(id,status){
	if(status==1){
		document.getElementById(id).style.filter = "alpha(opacity=70, finishopacity=70, style=2, startx=0, starty=0, finishx=140, finishy=270);";
	} else {
		document.getElementById(id).style.filter = "";
	}	
}


//<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>__________________
function COLORATE(id,status){
	if(status==1){
		document.getElementById(id).style.filter = "";
	} else {
		document.getElementById(id).style.filter = "progid:DXImageTransform.Microsoft.BasicImage(grayscale=1)";	
	}	

}




//<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>__________________is_numeric
function is_numeric(field){
	var value = field;
	//alert(value);
	value++;value--;
	//alert(value);
	if(field==value){
		return true;
	} else {
		return false;
	}
}

//<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>__________________addZiro
function addZiro(Q){if(Q<10)return '0'+Q;else return Q;}


//<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>__________________
function str_replace(find,replace,string){
	var fndLng=find.length;
	for(i=0; i<string.length; i++){
		if(string.substr(i,fndLng)==find){
			string=string.substring(0,i)+replace+string.substring(i+fndLng,string.length);
		}
	}
	return string;
}


// -------------------------------------
function wget(urlpath, id, pars){ // jquery
	if(pars!=''){
		var mtd = 'POST';
	} else {
		var mtd = 'GET';
	}
	
	if(urlpath.indexOf("?")==-1){
		;//
	} else {
		if(pars=='') {
			pars = urlpath.substring(urlpath.indexOf("?")+1,urlpath.length);
		}
		urlpath = urlpath.substring(0,urlpath.indexOf("?"));
	}
	$.ajax({
		 type: mtd
		,url: urlpath
		,data: pars
		,cache: false
		,success: function(html){$("#"+id).html(html);}
	});
}


function inputFileExtensionChecker(id, ext){
	str=document.getElementById(id).value.toUpperCase();
	var valid = 0;
	var i = 0;
	var ext_str = "";
	for(i=0; i<ext.length; i++){
		if(ext_str==""){
			ext_str = "*."+ext[i];
		} else {
			ext_str = ext_str+", *."+ext[i];
		}
		ext[i] = ext[i].toUpperCase();
		if(str.indexOf(ext[i], str.length - ext[i].length) !== -1){
			valid = 1;
		}
	}
	if(valid==0){
		alert('نوع فایل مجاز نیست,\nنوع فایل مجاز: '+ext_str);
		document.getElementById(id).value='';
	}
}


function checkIfIsNumberPressed(event){
	//alert(event.keyCode);
	if((event.keyCode >= 48) && (event.keyCode <= 57))return true;
	if(event.keyCode == 8 )return true;
	if(event.keyCode == 9 )return true;
	if(event.keyCode ==116)return true;
	if(event.keyCode == 46)return true;
	if(event.keyCode == 13)return true;
	
	return false;
}



function explode(glue, text){
	var ary = [];
	var arC = 0;
	var lEn = 0;
	var lng = text.length;
	var glg = glue.length;
	for(i=0; i<lng; i++){
		if(text.substring(i, i+glg)==glue){
			ary[arC] = text.substring(lEn, i);
			lEn = i+glg;
			i = i+glg;
			arC++;
		}
	}
	ary[arC] = text.substring(lEn, i);
	return ary;
}

function number_format(n){
	n = str_replace(",","",n);
	if(!is_numeric(n))return n;
	var l=0,t='',r='';
	do {
		l = n.length;
		t = n.substring(l-3,l);
		n = n.substring(0,l-3);
		if(r!=''){
			r = t+","+r;
		} else {
			r = t;
		}
	} while(n.length>0);
	return r;
}

