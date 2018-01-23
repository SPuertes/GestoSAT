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
        if(materialesAsignados!=='')
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
        if(trabajadoresAsignados!=='')
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

$(document).on('click','#cancel',function(){ window.location.href = "entrada.jsp?id="+id; });

// Calcular presupuesto
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
    
    $("#precioPresupuesto").html((Math.round((((1+iva)*+precioVenta+precioManoObra)*100).toFixed(2))/100).toFixed(2)); // Ajustar per a no pedre centims
};

// Actualizar precio presupuesto
$(document).on('keyup','.ms-select-numeric',calcularPresupuesto);
$(document).on('click','.ms-list',calcularPresupuesto);

$(document).on('click','#save',function(){
    // calcularPresupuesto(); // Actualment innesesari
    var param=[];
     $.each($('.input-cat-conf'),function(id){
        if(id!=2)
            param[id] = $(this)[0].value;
        else
            param[id] = $(this)[0].checked;
     });
     
    if(param[9]==='')
         param[9]= 0;
     
    $('#select_empleados').multiSelect('getSelecteds');
    var selectedEmpleados = returnMultiSelect;
    $('#select_stock').multiSelect('getSelecteds');
    var selectedStock = returnMultiSelect;
    
    param[10] = selectedEmpleados;
    param[11] = selectedStock;
    param[12] = id; // Id entrada
    
    if(!$.grep(param,function(val){return val==="";}).length){ // Si no hay huecos vacios
        $.ajax({
            url:'saveBudget',
            type:'POST',
            data:$.extend({},param),
            success:function(resp){
                if(parseInt(resp) !== 0)
                    window.location.href = "presupuesto.jsp?id="+parseInt(resp);
                else
                    $("#errorSave").modal("show");
            }
        });
    }else
        $("#emptyValues").modal("show");
});

// Eliminar presupuesto
$(document).on("click","#delete",function(){
    $.ajax({
        url:"deletePresupuesto",
        data:{"id":id},
        type:"POST",
        success:function(respuesta){
            if(respuesta === 'true'){
                window.location.href = "home.jsp";
            }else{
                $("errorDelete").modal("show");
            }
        }
    });
});

// Modo edicion presupuesto
$(document).on("click","#edit",function(){
    auxGuardar = $(this);
    if($(this).data("mode")=== "editar"){ // Activar edicion 
        $(".my-input-group-btn").css("display","inline-block");
        $(this).data("mode","guardar");
        $(this).text("Guardar");
        label = $.each($("label"),function(){});
        for(i = 0; i < label.length; i++){
            if(i===1)
                $(label[i]).replaceWith(("<input type='number' class='dynamic decimal' step='0.01' value='"+$(label[i]).text()+"'/>"));
            else if(i===3)
                $(label[i]).replaceWith(("<input type='number' class='dynamic num' value='"+$(label[i]).text()+"'/>"));
            else
                $(label[i]).replaceWith(("<input type='text' class='dynamic' value='"+$(label[i]).text()+"'/>"));
        }
        $("#fecha").prop("disabled",false);
        $("#aceptado").prop("disabled",false);
        $(".icheckbox_flat-green").removeClass("disabled");
        inputs = $.each($(".dynamic"),function(){$(this);});
        $("#select_empleados").multiSelect('enable');
        $("#select_stock").multiSelect('enable');
    }else{ // Guardar
        param = [];
        $.each($('.dynamic  '),function(id){param[id] = $(this)[0].value;});
        
        param[8]=$("#fecha").val();
        param[9]=$("#aceptado").prop("checked");
        
        $("#select_empleados").multiSelect('getSelecteds');
        param[10]=returnMultiSelect; // Trabajadores Presupuesto
        $("#select_stock").multiSelect('getSelecteds');
        param[11]=returnMultiSelect; // Materiales Presupuesto
        
        param[12]=id;
        
        if(!$.grep(param,function(val){return val==="";}).length){ // Si no hay huecos vacios
            // Datos i enviar param mes stock y treballadors
            $.ajax({
                url:'saveChangeBudget',
                type:'POST',
                data:$.extend({},param),
                success:function(resp){
                    if(resp === 'true'){
                        for(i = 0; i < inputs.length; i++)
                            $(inputs[i]).replaceWith($("<label class='my-label'>"+$(inputs[i]).val()+"</label>"));

                        $(".my-input-group-btn").css("display","none");
                        auxGuardar.data("mode","editar");
                        auxGuardar.text("Editar");
                        $("#fecha").prop("disabled",true);
                        $("#aceptado").prop("disabled",true);
                        $(".icheckbox_flat-green").addClass("disabled");
                        $("#select_empleados").multiSelect('disable');
                        $("#select_stock").multiSelect('disable');
                    }else
                        $("#errorSave").modal("show");
                }
            });
        }else
            $("#emptyValues").modal("show");
    }
});

$(document).on("click","#crearAlbaran",function(){
    window.location.href = "crearAlbaran.jsp?id="+id+"P";
});

$(document).on("click","#editarAlbaran",function(){
    window.location.href = "albaran.jsp?id="+idAlbaran;
});

$(document).on("click","#crearFactura",function(){
    window.location.href = "crearFactura.jsp?id="+idCliente;
});

$(document).on("click","#editarFactura",function(){
    window.location.href = "factura.jsp?id="+idFactura;
});

$(document).on("click","#exportarPDF",function(){
   $.ajax({
       url:'presupuesto2PDF',
       type:'POST',
       data:{'id':id},
       success:function(dir){
            if(dir!==""){
               window.open(dir);
            }
       }
   }); 
});

$(document).on("click","#exportarXLSX",function(){
   $.ajax({
       url:'presupuesto2XLSX',
       type:'POST',
       data:{'id':id},
       success:function(dir){
            if(dir!==""){
               window.open(dir);
            }
       }
   }); 
});