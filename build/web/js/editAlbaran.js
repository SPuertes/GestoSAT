/*  This file is part of GestoSAT.
*
*    GestoSAT is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    GestoSAT is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU Affero General Public License
*    along with GestoSAT.  If not, see <http://www.gnu.org/licenses/>.
* 
*    Salvador Puertes Aleixandre, July 2016
*
*/


if(typeof materialesAsignados === 'undefined' || typeof trabajadoresAsignados === 'undefined'){
    materialesAsignados = "";
    trabajadoresAsignados = "";
}

if(typeof edit === "undefined")
    edit = true;

$('#select_stock').multiSelect({
    type:"contable",
    selectableHeader: "<input type='text' class='form-control search-input' autocomplete='off' placeholder=''>",
    selectionHeader: "<input type='text' class='form-control search-input' autocomplete='off' placeholder=''>",
    afterInit: function (ms) {
        var that = this,
            $selectableSearch = that.$selectableUl.prev(),
            $selectionSearch = that.$selectionUl.prev(),
            selectableSearchString = '#' + that.$container.attr('id') + ' .ms-elem-selectable:not(.ms-selected)',
            selectionSearchString = '#' + that.$container.attr('id') + ' .ms-elem-selection.ms-selected';

        that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
            .on('keydown', function (e) {
                if (e.which === 40) {
                    that.$selectableUl.focus();
                    return false;
                }
            });
        that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
            .on('keydown', function (e) {
                if (e.which == 40) {
                    that.$selectionUl.focus();
                    return false;
                }
            });
        this.select(materialesAsignados,"");
        if(!edit)
            this.disable();
    },
    afterSelect: function () {
        this.qs1.cache();
        this.qs2.cache();
    },
    afterDeselect: function () {
        this.qs1.cache();
        this.qs2.cache();
    }
});

$('#select_empleados').multiSelect({
    type:"time",
    selectableHeader: "<input type='text' class='form-control search-input' autocomplete='off' placeholder=''>",
    selectionHeader: "<input type='text' class='form-control search-input' autocomplete='off' placeholder=''>",
    afterInit: function (ms) {
        var that = this,
            $selectableSearch = that.$selectableUl.prev(),
            $selectionSearch = that.$selectionUl.prev(),
            selectableSearchString = '#' + that.$container.attr('id') + ' .ms-elem-selectable:not(.ms-selected)',
            selectionSearchString = '#' + that.$container.attr('id') + ' .ms-elem-selection.ms-selected';

        that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
            .on('keydown', function (e) {
                if (e.which === 40) {
                    that.$selectableUl.focus();
                    return false;
                }
            });

        that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
            .on('keydown', function (e) {
                if (e.which == 40) {
                    that.$selectionUl.focus();
                    return false;
                }
            });
        this.select(trabajadoresAsignados,"");
        if(!edit)
            this.disable();
    },
    afterSelect: function () {
        this.qs1.cache();
        this.qs2.cache();
    },
    afterDeselect: function () {
        this.qs1.cache();
        this.qs2.cache();
    }
}); 

$(document).on('click','#cancel',function(){ window.location.href = "home.jsp"; });

// Calcular albaran
var precioVenta;

calcularPresupuesto = function(){

    precioVenta = 0;
    precioManoObra = 0;
    
    $('#select_empleados').multiSelect('getSelecteds');
    tempEmpleados = $.parseJSON(returnMultiSelect);
    $('#select_stock').multiSelect('getSelecteds');
    tempStock = $.parseJSON(returnMultiSelect);
    
    $.each(tempEmpleados,function(id){
        precioManoObra+= vecEmpleados[id]*(this["h"]+this["m"]/60)*(1+iva); // Precio hora trabajador (Bruto)
    });
    
    $.each(tempStock,function(id){
        precioVenta+= vecStock[id][0]*this; // Precio venta (con beneficios)
    });
    
    $("#precioAlbaran").html((Math.round((((1+iva)*precioVenta+precioManoObra)*100).toFixed(2))/100).toFixed(2)); // Ajustar per a no pedre centims
};

// Actualizar precio albaran
$(document).on('keyup','.ms-select-numeric',calcularPresupuesto);
$(document).on('click','.ms-list',calcularPresupuesto);

$(document).on('click','#save',function(){
    // Mirar que entrega relleno
    if(!$.grep($(".entrega"),function(obj,id){ if(id<5) return $(obj).val()==="";}).length && $("#conceptoAlbaran").val()!=""){
         param = {
                'concepto':$("#conceptoAlbaran").val(),
                'observaciones':$("#observacionesAlbaran").val()
            };

            switch($("#lugarEntrega").val()){
                case 'Domicilio':
                    param['entrega'] = entrega["cliente"];
                    break;
                case 'Otro':
                    param['entrega']['provEntrega'] = $("#provEntrega").val();
                    param['entrega']['pobEntrega'] = $("#pobEntrega").val();
                    param['entrega']['cpEntrega'] = $("#cpEntrega").val();
                    param['entrega']['calleEntrega'] =$ ("#calleEntrega").val();
                    param['entrega']['numEntrega'] = $("#numEntrega").val();
                    param['entrega']['escaleraEntrega'] = $("#escaleraEntrega").val();
                    param['entrega']['pisoEntrega'] = $("#pisoEntrega").val();
                    param['entrega']['puertaEntrega'] = $("#puertaEntrega").val();
                    break;
                case 'Tienda':
                    param['entrega'] = entrega["tienda"];
                    break;
            };
            
            if(param['entrega']['pisoEntrega'] === "")
                param['entrega']['pisoEntrega']=0;
            
            if(venta){
                $('#select_stock').multiSelect('getSelecteds');
                param["material"]=returnMultiSelect;
                    
                    if(id_Cliente != 0){
                        param["id"]=id_Cliente;
                        $.ajax({
                            url:"crearVenta",
                            data:param,
                            type:"POST",
                            success:function(res){
                                if(parseInt(res)>0)
                                    window.location.href = "albaran.jsp?id="+res;
                                else
                                    $("#errorSave").modal("show")// Error
                            }
                        });
                    }else{
                        if($("#nombre").val() === "" || $("#apellidos").val() === "" || $("#dni").val() === "" || ($("#tlfFijo").val() === "" && $("#tlfMovil").val() === "")){
                            $("#nombre").addClass("error");
                            $("#apellidos").addClass("error");
                            $("#dni").addClass("error");
                            $("#tlfFijo").addClass("error");
                            $("#emptyValues").modal("show");
                        }else{
                            nuevoCliente = {
                                "nombre":$("#nombre").val(),
                                "apellidos":$("#apellidos").val(),
                                "NIF":$("#dni").val(),
                                "provincia":$("#prov").val(),
                                "poblacion":$("#pob").val(),
                                "cp":$("#cp").val(),
                                "calle":$("#calle").val(), 
                                "numero":$("#num").val(),
                                "escalera":$("#escalera").val(),
                                "piso":$("#piso").val(),
                                "puerta":$("#puerta").val(),
                                "tlfFijo":$("#tlfFijo").val(),
                                "tlfMovil":$("#tlfMovil").val(),
                                "email":$("#email").val(),
                                "observaciones":$("#observaciones").val()
                            };
                            
                            if(nuevoCliente["piso"]=="")
                                nuevoCliente["piso"] = 0;
                            
                            if(nuevoCliente["tlfMovil"]=="")
                                nuevoCliente["tlfMovil"] = 0;
                            
                            param["id"] = 0;
                            param["cliente"] = nuevoCliente;
                            
                            $.ajax({
                                url:"crearVenta",
                                data:param,
                                type:"POST",
                                success:function(res){
                                    if(parseInt(res)>0)
                                        window.location.href = "albaran.jsp?id="+res;
                                    else
                                        $("#errorSave").modal("show")// Error
                                }
                            });
                        }
                    }
            }else{
                param["id"]=idTipo;
                param["flag"]=tipo;
                
                $('#select_stock').multiSelect('getSelecteds');
                param["material"]=returnMultiSelect;
                
                $('#select_empleados').multiSelect('getSelecteds');
                param["trabajadores"]=returnMultiSelect;
                
                $.ajax({
                    url:"crearAlbaran",
                    data:param,
                    type:"POST",
                    success:function(res){
                        if(parseInt(res)>0)
                            window.location.href = "albaran.jsp?id="+res;
                        else
                            $("#errorSave").modal("show")// Error
                    }
                });
            }
        }else{
            $("#emptyValues").modal("show");
            $("#conceptoAlbaran").addClass("error");
            $.each($(".entrega"),function(id,obj){if(id<5) $(obj).addClass("error");});
        }
});

// Eliminar albaran (NO PODER BORRAR ALBARANES)
/*$(document).on("click","#delete",function(){
    $.ajax({
        url:"deleteAlbaran",
        data:{"id":idAlbaran},
        type:"POST",
        success:function(respuesta){
            if(respuesta === 'true'){
                window.location.href = "home.jsp";
            }else{
                $("errorDelete").modal("show");
            }
        }
    });
});*/

// Modo edicion presupuesto
$(document).on("click","#edit",function(){
    auxGuardar = $(this);
    if($(this).data("mode") === "editar"){ // Activar edicion 
        $(".my-input-group-btn").css("display","inline-block");
        $(this).data("mode","guardar");
        $(this).text("Guardar");
        $(".entrega-opt").show();
        label = $.each($("label"),function(){});
        
        for(i = 0; i < label.length; i++){
            if(i === 2 || i === 6 ){
                if(i === 2)
                    $(label[i]).replaceWith(("<input type='number' class='dynamic entrega' value='"+$(label[i]).text()+"'/>"));
                else    
                    $(label[i]).replaceWith(("<input type='number' class='dynamic num entrega' value='"+$(label[i]).text()+"'/>"));
            }else
                if(i < 8)
                    $(label[i]).replaceWith(("<input type='text' class='dynamic entrega' value='"+$(label[i]).text()+"'/>"));
                else    
                    $(label[i]).replaceWith(("<input type='text' class='dynamic' value='"+$(label[i]).text()+"'/>"));
        }
        
        inputs = $.each($(".dynamic"),function(){$(this);});
        
        $("#select_empleados").multiSelect('enable');
        $("#select_stock").multiSelect('enable');
    }else{ // Guardar
        param = [];
        $.each($('.dynamic'),function(id){param[id] = $(this)[0].value;});
        
        $("#select_empleados").multiSelect('getSelecteds');
        param[10]=returnMultiSelect; // Trabajadores Presupuesto
        $("#select_stock").multiSelect('getSelecteds');
        param[11]=returnMultiSelect; // Materiales Presupuesto
        
        param[12]=idAlbaran;
        if(!$.grep($(".entrega"),function(obj,id){ if(id<5) return $(obj).val()==="";}).length && $(inputs[8]).val()!==""){ // Si no hay huecos vacios
            // Datos i enviar param mes stock y treballadors
            if(param[6]==="")
                param[6]=0;
            
            $.ajax({
                url:'saveChangeAlbaran',
                type:'POST',
                data:$.extend({},param),
                success:function(resp){
                    if(resp === 'true'){
                        for(i = 0; i < inputs.length; i++)
                            $(inputs[i]).replaceWith($("<label class='my-label'>"+$(inputs[i]).val()+"</label>"));

                        auxGuardar.data("mode","editar");
                        auxGuardar.text("Editar");
                        $(".entrega-opt").hide();
                        $("#select_empleados").multiSelect('disable');
                        $("#select_stock").multiSelect('disable');
                    }else
                        $("#errorSave").modal("show");
                }
            });
        }else{
            $("#emptyValues").modal("show");
            $(inputs[0]).addClass("error");
            $(inputs[1]).addClass("error");
            $(inputs[2]).addClass("error");
            $(inputs[3]).addClass("error");
            $(inputs[4]).addClass("error");
            $(inputs[8]).addClass("error");
        }
    }
});

$(document).on("click","#crearFactura",function(){
    window.location.href = "crearFactura.jsp?id="+idCliente;
});

$(document).on("click","#editarFactura",function(){
    window.location.href = "factura.jsp?id="+idFactura;
});

$(document).on("click","#selec-existent-cliente",function(){
    $("#clientes").modal("show");
});

$(document).on("click","#aceptar-cliente",function(){
    id_Cliente = parseInt($("#menu-cliente").val());
    if(id_Cliente > 0){
        $("#nombre").val(clientes[id_Cliente]["nombre"]);
        $("#apellidos").val(clientes[id_Cliente]["apellidos"]);
        $("#dni").val(clientes[id_Cliente]["NIF"]);
        $("#prov").val(clientes[id_Cliente]["provincia"]);
        $("#pob").val(clientes[id_Cliente]["poblacion"]);
        if(clientes[id_Cliente]["cp"] != "0")
            $("#cp").val(clientes[id_Cliente]["cp"]);
        $("#calle").val(clientes[id_Cliente]["calle"]);
        $("#num").val(clientes[id_Cliente]["numero"]);
        $("#escalera").val(clientes[id_Cliente]["escalera"]);
        if(clientes[id_Cliente]["piso"] != "0")
            $("#piso").val(clientes[id_Cliente]["piso"]);
        $("#puerta").val(clientes[id_Cliente]["puerta"]);
        $("#tlfFijo").val(clientes[id_Cliente]["tlfFijo"]);
        $("#tlfMovil").val(clientes[id_Cliente]["tlfMovil"]);
        $("#email").val(clientes[id_Cliente]["email"]);
        $("#observaciones").val(clientes[id_Cliente]["observaciones"]);
        
        entrega["cliente"]['provEntrega']=clientes[id_Cliente]["provincia"];
        entrega["cliente"]['pobEntrega']=clientes[id_Cliente]["poblacion"];
        entrega["cliente"]['cpEntrega']=clientes[id_Cliente]["cp"];
        entrega["cliente"]['calleEntrega']=clientes[id_Cliente]["calle"];
        entrega["cliente"]['numEntrega']=clientes[id_Cliente]["num"];
        entrega["cliente"]['escaleraEntrega']=clientes[id_Cliente]["escalera"];
        entrega["cliente"]['pisoEntrega']=clientes[id_Cliente]["piso"];
        entrega["cliente"]['puertaEntrega']=clientes[id_Cliente]["puerta"];
        
        // Bloquear inputs
        $("#nombre").prop("disabled",true);
        $("#apellidos").prop("disabled",true);
        $("#dni").prop("disabled",true);
        $("#prov").prop("disabled",true);
        $("#pob").prop("disabled",true);
        $("#cp").prop("disabled",true);
        $("#calle").prop("disabled",true); 
        $("#num").prop("disabled",true);
        $("#escalera").prop("disabled",true);
        $("#piso").prop("disabled",true);
        $("#puerta").prop("disabled",true);
        $("#tlfFijo").prop("disabled",true);
        $("#tlfMovil").prop("disabled",true);
        $("#email").prop("disabled",true);
        $("#observaciones").prop("disabled",true);
        
        if($("#lugarEntrega").val()==="Domicilio"){
            $("#provEntrega").val(entrega["cliente"]['provEntrega']);
            $("#pobEntrega").val(entrega["cliente"]['pobEntrega']);
            $("#cpEntrega").val(entrega["cliente"]['cpEntrega']);
            $("#calleEntrega").val(entrega["cliente"]['calleEntrega']);
            $("#numEntrega").val(entrega["cliente"]['numEntrega']);
            $("#escaleraEntrega").val(entrega["cliente"]['escaleraEntrega']);
            $("#pisoEntrega").val(entrega["cliente"]['pisoEntrega']);
            $("#puertaEntrega").val(entrega["cliente"]['puertaEntrega']);
        }
    }else{
        $("#nombre").val("");
        $("#apellidos").val("");
        $("#dni").val("");
        $("#prov").val("");
        $("#pob").val("");
        $("#cp").val("");
        $("#calle").val(""); 
        $("#num").val("");
        $("#escalera").val("");
        $("#piso").val("");
        $("#puerta").val("");
        $("#tlfFijo").val("");
        $("#tlfMovil").val("");
        $("#email").val("");
        $("#observaciones").val("");
        
        entrega["cliente"]['provEntrega']="";
        entrega["cliente"]['pobEntrega']="";
        entrega["cliente"]['cpEntrega']="";
        entrega["cliente"]['calleEntrega']="";
        entrega["cliente"]['numEntrega']="";
        entrega["cliente"]['escaleraEntrega']="";
        entrega["cliente"]['pisoEntrega']="";
        entrega["cliente"]['puertaEntrega']="";
        
        // Desbloquear inputs
        $("#nombre").prop("disabled",false);
        $("#apellidos").prop("disabled",false);
        $("#dni").prop("disabled",false);
        $("#prov").prop("disabled",false);
        $("#pob").prop("disabled",false);
        $("#cp").prop("disabled",false);
        $("#calle").prop("disabled",false);
        $("#calle").prop("disabled",false); 
        $("#num").prop("disabled",false);
        $("#escalera").prop("disabled",false);
        $("#piso").prop("disabled",false);
        $("#puerta").prop("disabled",false);
        $("#tlfFijo").prop("disabled",false);
        $("#tlfMovil").prop("disabled",false);
        $("#email").prop("disabled",false);
        $("#observaciones").prop("disabled",false);
        
        if($("#lugarEntrega").val()==="Domicilio"){
            $("#lugarEntrega").val("Otro");
            $("#lugarEntrega").change();
        }
    }
    $("#clientes").modal("hide");
});

$(document).on("change","#menu-cliente",function(){
    aux_id_Cliente = parseInt($(this).val());
    if(aux_id_Cliente == 0){
        $("#modal-persona").text("");
        $("#modal-lugar").text("");
        $("#modal-direccion").text("");
        $("#modal-contacto").text(""); 
    }else{
        $("#modal-persona").text(clientes[aux_id_Cliente]["nombre"]+" "+clientes[aux_id_Cliente]["apellidos"]+" "+clientes[aux_id_Cliente]["NIF"]);
        $("#modal-lugar").text(clientes[aux_id_Cliente]["provincia"]+" "+clientes[aux_id_Cliente]["poblacion"]);
        if(clientes[aux_id_Cliente]["cp"] != "0")
            $("#modal-lugar").append(" "+clientes[aux_id_Cliente]["cp"]);
        $("#modal-direccion").text(clientes[aux_id_Cliente]["calle"]+" "+clientes[aux_id_Cliente]["numero"]+" "+clientes[aux_id_Cliente]["escalera"]);
            if(clientes[aux_id_Cliente]["piso"] != "0")
                $("#modal-direccion").append(" "+clientes[aux_id_Cliente]["piso"]);
        $("#modal-direccion").append(" "+clientes[aux_id_Cliente]["puerta"]);
        $("#modal-contacto").text(clientes[aux_id_Cliente]["tlfFijo"]+" "+clientes[aux_id_Cliente]["tlfMovil"]+" "+clientes[aux_id_Cliente]["email"]);
    }
});

$(document).on("keyup",".cliente",function(){
    entrega["cliente"]['provEntrega']=$("#prov").val();
    entrega["cliente"]['pobEntrega']=$("#pob").val();
    entrega["cliente"]['cpEntrega']=$("#cp").val();
    entrega["cliente"]['calleEntrega']=$("#calle").val();
    entrega["cliente"]['numEntrega']=$("#num").val();
    entrega["cliente"]['escaleraEntrega']=$("#escalera").val();
    entrega["cliente"]['pisoEntrega']=$("#piso").val();
    entrega["cliente"]['puertaEntrega']=$("#puerta").val();  
    $("#lugarEntrega").change();
});

$(document).on("change","#lugarEntrega",function(){
    switch($(this).val()){
        case 'Domicilio':
            $("#provEntrega").val(entrega["cliente"]['provEntrega']);
            $("#provEntrega").prop("disabled",true);
            $("#pobEntrega").val(entrega["cliente"]['pobEntrega']);
            $("#pobEntrega").prop("disabled",true);
            $("#cpEntrega").val(entrega["cliente"]['cpEntrega']);
            $("#cpEntrega").prop("disabled",true);
            $("#calleEntrega").val(entrega["cliente"]['calleEntrega']);
            $("#calleEntrega").prop("disabled",true);
            $("#numEntrega").val(entrega["cliente"]['numEntrega']);
            $("#numEntrega").prop("disabled",true);
            $("#escaleraEntrega").val(entrega["cliente"]['escaleraEntrega']);
            $("#escaleraEntrega").prop("disabled",true);
            $("#pisoEntrega").val(entrega["cliente"]['pisoEntrega']);
            $("#pisoEntrega").prop("disabled",true);
            $("#puertaEntrega").val(entrega["cliente"]['puertaEntrega']);
            $("#puertaEntrega").prop("disabled",true);
            break;
        case 'Otro':
            $("#provEntrega").val("");
            $("#provEntrega").prop("disabled",false);
            $("#pobEntrega").val("");
            $("#pobEntrega").prop("disabled",false);
            $("#cpEntrega").val("");
            $("#cpEntrega").prop("disabled",false);
            $("#calleEntrega").val("");
            $("#calleEntrega").prop("disabled",false);
            $("#numEntrega").val("");
            $("#numEntrega").prop("disabled",false);
            $("#escaleraEntrega").val("");
            $("#escaleraEntrega").prop("disabled",false);
            $("#pisoEntrega").val("");
            $("#pisoEntrega").prop("disabled",false);
            $("#puertaEntrega").val("");
            $("#puertaEntrega").prop("disabled",false);
            break;
        case 'Tienda':
            $("#provEntrega").val(entrega["tienda"]['provEntrega']);
            $("#provEntrega").prop("disabled",true);
            $("#pobEntrega").val(entrega["tienda"]['pobEntrega']);
            $("#pobEntrega").prop("disabled",true);
            $("#cpEntrega").val(entrega["tienda"]['cpEntrega']);
            $("#cpEntrega").prop("disabled",true);
            $("#calleEntrega").val(entrega["tienda"]['calleEntrega']);
            $("#calleEntrega").prop("disabled",true);
            $("#numEntrega").val(entrega["tienda"]['numEntrega']);
            $("#numEntrega").prop("disabled",true);
            $("#escaleraEntrega").val(entrega["tienda"]['escaleraEntrega']);
            $("#escaleraEntrega").prop("disabled",true);
            $("#pisoEntrega").val(entrega["tienda"]['pisoEntrega']);
            $("#pisoEntrega").prop("disabled",true);
            $("#puertaEntrega").val(entrega["tienda"]['puertaEntrega']);
            $("#puertaEntrega").prop("disabled",true);
            break;
    };
});

// Editar albaran entrega
$(document).on("change","#lugarEntregaEdit",function(){
    
    switch($(this).val()){
        case 'Domicilio':
            $(inputs[0]).val(entrega["cliente"]['provEntrega']);
            $(inputs[1]).val(entrega["cliente"]['pobEntrega']);
            $(inputs[2]).val(entrega["cliente"]['cpEntrega']);
            $(inputs[3]).val(entrega["cliente"]['calleEntrega']);
            $(inputs[4]).val(entrega["cliente"]['numEntrega']);
            $(inputs[5]).val(entrega["cliente"]['escaleraEntrega']);
            $(inputs[6]).val(entrega["cliente"]['pisoEntrega']);
            $(inputs[7]).val(entrega["cliente"]['puertaEntrega']);
            break;
        case 'Otro':
            $(inputs[0]).val(entrega["otro"]['provEntrega']);
            $(inputs[1]).val(entrega["otro"]['pobEntrega']);
            $(inputs[2]).val(entrega["otro"]['cpEntrega']);
            $(inputs[3]).val(entrega["otro"]['calleEntrega']);
            $(inputs[4]).val(entrega["otro"]['numEntrega']);
            $(inputs[5]).val(entrega["otro"]['escaleraEntrega']);
            $(inputs[6]).val(entrega["otro"]['pisoEntrega']);
            $(inputs[7]).val(entrega["otro"]['puertaEntrega']);
            break;
        case 'Tienda':
            $(inputs[0]).val(entrega["tienda"]['provEntrega']);
            $(inputs[1]).val(entrega["tienda"]['pobEntrega']);
            $(inputs[2]).val(entrega["tienda"]['cpEntrega']);
            $(inputs[3]).val(entrega["tienda"]['calleEntrega']);
            $(inputs[4]).val(entrega["tienda"]['numEntrega']);
            $(inputs[5]).val(entrega["tienda"]['escaleraEntrega']);
            $(inputs[6]).val(entrega["tienda"]['pisoEntrega']);
            $(inputs[7]).val(entrega["tienda"]['puertaEntrega']);
            break;
    };
});

$(document).on("keyup",".ms-select-numeric.decimal",function(){
    if(parseFloat($(this).attr("max")) < parseFloat($(this).val())){
        parseFloat($(this).val($(this).attr("max")));
        calcularPresupuesto();
    }
    
});

$(document).on("click","#exportarPDF",function(){
   $.ajax({
       url:'albaran2PDF',
       type:'POST',
       data:{'id':idAlbaran},
       success:function(dir){
            if(dir!==""){
               window.open(dir);
            }
       }
   }); 
});

$(document).on("click","#exportarXLSX",function(){
   $.ajax({
       url:'albaran2XLSX',
       type:'POST',
       data:{'id':idAlbaran},
       success:function(dir){
            if(dir!==""){
               window.open(dir);
            }
       }
   }); 
});