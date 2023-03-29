function aplicarAlineacionesTablas(){
	var table = $('table'); //$("#list");

	if(table.length > 0){
		var th = table.find("thead th");
		
		var izq = th.find(".table-header-izquierda");
		if(izq.length > 0){
			izq.parent().parent().css("text-align", "left");
		}
		
		var der = th.find(".table-header-derecha");
		if(der.length > 0){
			der.parent().parent().css("text-align", "right");
		}
		
		var cen = th.find(".table-header-centrado");
		if(cen.length > 0){
			cen.parent().parent().css("text-align", "center");
		}
	}	
}

function marcarTabsInvalidos(id) {
    id = PrimeFaces.escapeClientId(id);
    
    $(id + ' ul > li.ui-tabs-header').each(function(index) {
    	//Obtengo el id del tab-panel
        d = $('a', this);
        // el id del href empieza por '#'. Lo obvio.
        tid = d.attr('href').substring(1); 
        tid = PrimeFaces.escapeClientId(tid);
        //Busco los campos no válidos
        d = $(tid + ' .ui-state-error');        
        if (d.length > 0) $(this).addClass('ui-state-error');
        else $(this).removeClass('ui-state-error');
    });
}

function zIndexFrente(){
	var max = -1;

	$("[style*='z-index']").each(function(i, e){
		//console.log(e)
		var str = $(e).css('z-index');
		var z = parseInt(str, 10);
		if(z > max){
			max = z;
		}
	});

	return max;
}

//********************************************

function abrirVistaPreviaDoc(urlVP, nombreVP) {
	var prefijoUrl = prefijoUrlAplicacion();
	const urlVisor = new URL(prefijoUrl + "/aplicacion/expedientes/expedientes/visorDocVP.xhtml");
	urlVisor.search = new URLSearchParams({url: urlVP, nombre: nombreVP});
	//console.log(urlVisor.toString());
	
	window.open(urlVisor.toString(), "_blank",
			'location=no,menubar=no,status=no,toolbar=no,resizable=yes'
			);
}

function prefijoUrlAplicacion(){
	var url = window.location.href;
	var p = url.indexOf(CONTEXT_PATH);
	return url.substr(0, p+CONTEXT_PATH.length);
}

//----------
//Obsoleto. EL visor pdf ahora se abre dinámicamente desde Java
/*function abrirDialogVisorPdf() {
	var pfDlg = PF('dialogVisorPdf');
	var jqDlg = pfDlg.jq;
	
	if('none' == jqDlg.css('display')){
		pfDlg.show();
	} else {
		jqDlg.css('z-index', 1 + zIndexFrente());
	}
}*/

function traerAlFrentePF(widgetDlg){
	//console.log(widgetDlg);	
	traerAlFrenteJQ(PF(widgetDlg).jq);
}

function traerAlFrente(idDlg){
	//console.log(idDlg);
	//Asumimos que sólo hay un diálogo con este id
	var jqDlg = $("#" + idDlg);	
	traerAlFrenteJQ(jqDlg);
}

function traerAlFrenteJQ(jqDlg){
	if(!jqDlg || jqDlg.length == 0){
		console.log("El objeto jQuery no existe");
		return;
	}
		
	if('none' == jqDlg.css('display')){
		//pfDlg.show();
	} else {
		jqDlg.css('z-index', 1 + zIndexFrente());
	}
}

function traerVisorAlFrente(id){
	var jq = PF('panelVisorPdf-' + id).jq.closest('.ui-dialog');
	traerAlFrenteJQ(jqDlg);
}

//************************************************

