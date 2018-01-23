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


// Global variables to edti filter

// Both
aux_obs = "";

// Averia
aux_lugar = "";
aux_motivo = "";

// Cita
aux_fecha= "";
aux_obs_cita = "";

$(document).on("click","#btn-mod",function(){
    var obj = $(this);
    if($(this).data("mode") === "editar"){
        $(this).data("mode","guardar");
        $(this).text("Guardar");
        label = $.each($("label.dynamic"),function(){});
        for(i = 0; i < label.length; i++){
            $(label[i]).replaceWith(("<input type='text' class='dynamic' value='"+$(label[i]).text()+"'/>"));
        }
        
        inputs = $.each($("input.dynamic"),function(){ $(this);});
        
        $(".my-input-group-btn").css("display","inline-block");
        
        if(flag==='averia'){
            aux_obs = $(label[2]).text();
            aux_lugar = $(label[0]).text();
            aux_motivo = $(label[1]).text();
        }else{
            aux_fecha = $("#fecha").val();
            aux_obs_cita = $(label[0]).text();
            aux_motivo = $(label[1]).text();
            aux_obs = $(label[2]).text();
            $('#select_empleados').multiSelect('getSelecteds');
            empleados = returnMultiSelect;
            $("#fecha").prop("disabled",false);
            $("#select_empleados").multiSelect('enable');
        }
    }else{
        if(flag==='averia'){
            if(aux_obs !== $(inputs[2]).val() || aux_lugar !== $(inputs[0]).val() || aux_motivo !== $(inputs[1]).val())
                $.ajax({
                    url:"saveChangeAveria",
                    type:"POST",
                    data:{"id":id,"observaciones":$(inputs[2]).val(),"lugar":$(inputs[0]).val(),"motivo":$(inputs[1]).val()},
                    success:function(resp){
                        if(resp === 'true'){
                            obj.data("mode","editar");
                            obj.text("Editar");

                            for(i = 0; i < inputs.length; i++)
                                $(inputs[i]).replaceWith($("<label class='my-label'>"+$(inputs[i]).val()+"</label>"));

                            $(".my-input-group-btn").hide();
                        }
                    }
                });
        }else{
            $('#select_empleados').multiSelect('getSelecteds');
            console.log(inputs);
            if(aux_obs !== $(inputs[2]).val() || aux_fecha !== $("#fecha").val() || aux_obs_cita !== $(inputs[0]).val() || empleados !== returnMultiSelect || aux_motivo !== $(inputs[1]).val())
                $.ajax({
                    url:"saveChangeCita",
                    type:"POST",
                    data:{"id":id,"observacionesEntrada":$(inputs[2]).val(),
                        "fecha":$("#fecha").val(),
                        "observacionesCita":$(inputs[0]).val(),
                        "empleados":returnMultiSelect,
                        "motivo":$(inputs[1]).val()},
                    success:function(resp){
                        if(resp === 'true'){
                            obj.data("mode","editar");
                            obj.text("Editar");
                            for(i = 1; i < inputs.length; i++)
                                $(inputs[i]).replaceWith($("<label class='my-label'>"+$(inputs[i]).val()+"</label>"));

                            $(".my-input-group-btn").hide();
                            $("#fecha").prop("disabled",true);
                            $("#select_empleados").multiSelect('disable');
                            
                            fecha = $("#fecha").val().split(" ");
                            var mes = 0;
                            switch(fecha[1]){
                                case "Enero":
                                    mes = 0;
                                    break;
                                case "Febrero":
                                    mes = 1;
                                    break;
                                case "Marzo":
                                    mes = 2;
                                    break;
                                case "Abril":
                                    mes = 3;
                                    break;
                                case "Mayo":
                                    mes = 4;
                                    break;
                                case "Junio":
                                    mes = 5;
                                    break;
                                case "Julio":
                                    mes = 6;
                                    break;
                                case "Agosto":
                                    mes = 7;
                                    break;
                                case "Septiembre":
                                    mes = 8;
                                    break;
                                case "Octubre":
                                    mes = 9;
                                    break;
                                case "Noviembre":
                                    mes = 00;
                                    break;
                                case "Diciembre":
                                    mes = 11;
                                    break;
                            };
                            
                            var fechaCita = new Date(fecha[2],mes,fecha[0]);
                            var fechaActual = new Date();
                            fechaActual.setDate(fechaActual.getDate()+7); // Fecha limite de visualizacion
                            citaActual = null;
                            $.each($("li .appointments").children(),function(){
                                if($(this).data("idcita")===id)
                                    citaActual = $(this);
                            });
                            if(fechaCita < fechaActual){
                                if(citaActual !== null){ // Editar existent
                                    $($(citaActual.find("div")[4]).children()[1]).text(fecha[4]+" "+fechaCita.getDate()+"/"+(fechaCita.getMonth()+1)+"/"+fechaCita.getFullYear());
                                    $($(citaActual.find("div")[3]).children()[1]).text($(inputs[2]).val());
                                }else // Anyadir si no existeix
                                    $("li .appointments").prepend("<div class='col-md-11 cita-div' data-idCita='"+id+"'><div class='rightCita'><div><span class='attr-entries'>Cliente: </span>"+cliente[0]+"</div><div><span class='attr-entries'>Direccion: </span>"+cliente[1]+"</div><div><span class='attr-entries'>Motivo: </span><span>"+$(inputs[2]).val()+"</span></div><div><span class='attr-entries'>Fecha: </span><span>"+fecha[4]+" "+fechaCita.getDate()+"/"+(fechaCita.getMonth()+1)+"/"+fechaCita.getFullYear()+"</span></div></div></div>");
                            }else
                                if(citaActual !== null)
                                    citaActual.remove();
                        }
                    }
                });
        }
    }
});

$(document).on("click","#delete-entry",function(){
    $.ajax({
        url:"deleteEntrada",
        type:"POST",
        data:{"id":id},
        success:function(respuesta){
            if(respuesta==="true")
                window.location.href = "home.jsp";
        }
    });
});

$(document).on("click","#crearPresupuesto",function(){
    window.location.href = "crearPresupuesto.jsp?id="+id;
});

$(document).on("click","#editarPresupuesto",function(){
    window.location.href = "presupuesto.jsp?id="+idPresupuesto;
});

$(document).on("click","#crearAlbaran",function(){
    if(typeof idPresupuesto === "undefined")
        idPresupuesto=0;
    window.location.href = "crearAlbaran.jsp?id="+id+"E&idP="+idPresupuesto;
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

$('#select_empleados').multiSelect({
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
        this.select(asignados,"");
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

$(document).on("click","#exportarPDF",function(){
   $.ajax({
       url:'entrada2PDF',
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
       url:'entrada2XLSX',
       type:'POST',
       data:{'id':id},
       success:function(dir){
            if(dir!==""){
               window.open(dir);
            }
       }
   }); 
});