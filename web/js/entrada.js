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


id_Cliente = 0;
id_Aparato = 0;
exist_Aparato = 0;
id_Cita = 0;
exist_Cita = 0;

$(document).on("click","#selec-existent-cliente",function(){
    $("#clientes").modal("show");
});

$(document).on("change","#menu-cliente",function(){
    aux_id_Cliente = parseInt($(this).val());
    if(aux_id_Cliente == 0){
        $("#modal-persona").text("");
        $("#modal-lugar").text("");
        $("#modal-direccion").text("");
        $("#modal-contacto").text("");
        id_Aparato = 0; 
        aux_id_Aparato = 0;    
    }else{
        $("#modal-persona").text(clientes[aux_id_Cliente]["nombre"]+" "+clientes[aux_id_Cliente]["apellidos"]+" "+clientes[aux_id_Cliente]["NIF"]);
        $("#modal-lugar").text(clientes[aux_id_Cliente]["provincia"]+" "+clientes[aux_id_Cliente]["poblacion"]);
        if(clientes[aux_id_Cliente]["CP"] != "0")
            $("#modal-lugar").append(" "+clientes[aux_id_Cliente]["CP"]);
        $("#modal-direccion").text(clientes[aux_id_Cliente]["calle"]+" "+clientes[aux_id_Cliente]["numero"]+" "+clientes[aux_id_Cliente]["escalera"]);
            if(clientes[aux_id_Cliente]["piso"] != "0")
                $("#modal-direccion").append(" "+clientes[aux_id_Cliente]["piso"]);
        $("#modal-direccion").append(" "+clientes[aux_id_Cliente]["puerta"]);
        $("#modal-contacto").text(clientes[aux_id_Cliente]["tlfFijo"]+" "+clientes[aux_id_Cliente]["tlfMovil"]+" "+clientes[aux_id_Cliente]["email"]);
    }
});

$(document).on("click","#aceptar-cliente",function(){
    id_Cliente = parseInt($("#menu-cliente").val());
    if(id_Cliente > 0){
        $("#nombre").val(clientes[id_Cliente]["nombre"]);
        $("#apellidos").val(clientes[id_Cliente]["apellidos"]);
        $("#dni").val(clientes[id_Cliente]["NIF"]);
        $("#prov").val(clientes[id_Cliente]["provincia"]);
        $("#pob").val(clientes[id_Cliente]["poblacion"]);
        if(clientes[id_Cliente]["CP"] != "0")
            $("#cp").val(clientes[id_Cliente]["CP"]);
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
        if($("#selec-existent-aparato").length == 0)
            $(".Aparato").children("span").append("<a class='btn btn-primary btn-exist' id='selec-existent-aparato'> Existente </a>");
        if($("#selec-existent-cita").length == 0)
            $(".Cita").children("span").append("<a class='btn btn-primary btn-exist' id='selec-existent-cita'> Existente </a>");
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
        $("#selec-existent-aparato").remove();
        $("#selec-existent-cita").remove();
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
    }
        $("#clientes").modal("hide");
        id_Aparato = 0; 
        exist_Aparato = 0;
        aux_id_Aparato = 0;
        id_Cita = 0; 
        exist_Cita = 0;
        aux_id_Cita = 0;
        
        $("#menu-aparato").empty();
        $("#menu-aparato").append("<option value='0'>Ninguno</option>");
        $("#tipo").val("");
        $("#marca").val("");
        $("#modelo").val("");
        $("#color").val("");
        $("#nSerie").val("");
        $("#lugar").val("");
        $("#motivoAveria").val("");
        $("#observacionesAparato").val("");
        $("#modal-tipo").text("");
        $("#modal-marca").text("");
        $("#modal-modelo").text("");
        $("#modal-color").text("");
        $("#modal-serie").text("");
        $("#modal-observaciones").text("");
        
        $("#menu-cita").empty();
        $("#menu-cita").append("<option value='0'>Ninguno</option>");
        $("#provCita").val("");
        $("#pobCita").val("");
        $("#cpCita").val("");
        $("#calleCita").val("");
        $("#numCita").val("");
        $("#escaleraCita").val("");
        $("#puertaCita").val("");
        $("#observacionesCita").val("");
        $("#observacionesLugar").val("");
        
        $("#provCita").prop("disabled",false);
        $("#pobCita").prop("disabled",false);
        $("#cpCita").prop("disabled",false);
        $("#calleCita").prop("disabled",false);
        $("#numCita").prop("disabled",false);
        $("#escaleraCita").prop("disabled",false);
        $("#puertaCita").prop("disabled",false);
        $("#observacionesCita").prop("disabled",false);
        $("#observacionesLugar").prop("disabled",false);
        $("#tipo").prop("disabled",false);
        $("#marca").prop("disabled",false);
        $("#modelo").prop("disabled",false);
        $("#color").prop("disabled",false);
        $("#nSerie").prop("disabled",false);
        $("#lugar").prop("disabled",false);
        $("#motivoAveria").prop("disabled",false);
        $("#observacionesAparato").prop("disabled",false);
        
        $("#modal-direccionCita").text("");
        $("#modal-observacionesCita").text("");
        
        $("#menu-aparato").select2({});
        $("#menu-cita").select2({});
    });

$(document).on("change","#menu-aparato",function(){ // Actualisar als atributos de Aparato
    aux_id_Aparato = parseInt($(this).val());
    if(aux_id_Aparato == 0){
        $("#modal-tipo").text("");
        $("#modal-marca").text("");
        $("#modal-modelo").text("");
        $("#modal-color").text("");
        $("#modal-serie").text("");
        $("#modal-observaciones").text("");
    }else{
        $("#modal-tipo").text(aparato[aux_id_Aparato].tipo);
        $("#modal-marca").text(aparato[aux_id_Aparato].marca);
        $("#modal-modelo").text(aparato[aux_id_Aparato].modelo);
        $("#modal-color").text(aparato[aux_id_Aparato].color);
        $("#modal-serie").text(aparato[aux_id_Aparato].numSerie);
        $("#modal-observaciones").text(aparato[aux_id_Aparato].observaciones);
    }
});

$(document).on("change","#menu-cita",function(){ // Actualisar als atributos de Cita
    aux_id_Cita = parseInt($(this).val());
    if(aux_id_Cita == 0){
        $("#modal-direccionCita").text("");
        $("#modal-observacionesCita").text("");
    }else{
        $("#modal-direccionCita").text(cita[aux_id_Cita].provincia+" "+cita[aux_id_Cita].poblacion+" "+cita[aux_id_Cita].calle+" "+cita[aux_id_Cita].numero+" "+cita[aux_id_Cita].escalera+" "+cita[aux_id_Cita].piso+" "+cita[aux_id_Cita].puerta);
        $("#modal-observacionesCita").text(cita[aux_id_Cita].observaciones);
    }
});

$(document).on("click","#aceptar-aparato",function(){
    id_Aparato = parseInt($("#menu-aparato").val()); 
    if(id_Aparato === 0){
        $("#tipo").val("");
        $("#marca").val("");
        $("#modelo").val("");
        $("#color").val("");
        $("#nSerie").val("");
        $("#motivoAveria").val("");
        $("#observacionesAparato").attr("");
        
        // Desbloquear
        $("#tipo").prop("disabled",false);
        $("#marca").prop("disabled",false);
        $("#modelo").prop("disabled",false);
        $("#color").prop("disabled",false);
        $("#nSerie").prop("disabled",false);
        $("#motivoAveria").prop("disabled",false);
        $("#observacionesAparato").prop("disabled",false);
    }else{
        $("#tipo").val(aparato[id_Aparato].tipo);
        $("#marca").val(aparato[id_Aparato].marca);
        $("#modelo").val(aparato[id_Aparato].modelo);
        $("#color").val(aparato[id_Aparato].color);
        $("#nSerie").val(aparato[id_Aparato].numSerie);
        $("#motivoAveria").val("");
        $("#observacionesAparato").val(aparato[id_Aparato].observaciones);
        // Bloquear
        $("#tipo").prop("disabled",true);
        $("#marca").prop("disabled",true);
        $("#modelo").prop("disabled",true);
        $("#color").prop("disabled",true);
        $("#nSerie").prop("disabled",true);
        $("#motivoAveria").prop("disabled",true);
        $("#observacionesAparato").prop("disabled",true);
    }
    $("#aparatos").modal("hide");
});

// Aceptar citas
$(document).on("click","#aceptar-cita",function(){
    id_Cita = parseInt($("#menu-cita").val()); 
    if(id_Cita === 0){
        $("#provCita").val("");
        $("#pobCita").val("");
        $("#cpCita").val("");
        $("#calleCita").val("");
        $("#numCita").val("");
        $("#escaleraCita").val("");
        $("#pisoCita").val("");
        $("#puertaCita").val("");
        $("#observacionesLugar").val("");
        
        //Desbloquear
        $("#provCita").prop("disabled",false);
        $("#pobCita").prop("disabled",false);
        $("#cpCita").prop("disabled",false);
        $("#calleCita").prop("disabled",false);
        $("#numCita").prop("disabled",false);
        $("#escaleraCita").prop("disabled",false);
        $("#pisoCita").prop("disabled",false);
        $("#puertaCita").prop("disabled",false);
        $("#observacionesLugar").prop("disabled",false);
    }else{
        $("#provCita").val(cita[id_Cita].provincia);
        $("#pobCita").val(cita[id_Cita].poblacion);
        $("#cpCita").val(cita[id_Cita].cp);
        $("#calleCita").val(cita[id_Cita].calle);
        $("#numCita").val(cita[id_Cita].numero);
        $("#escaleraCita").val(cita[id_Cita].escalera);
        $("#pisoCita").val(cita[id_Cita].piso);
        $("#puertaCita").val(cita[id_Cita].puerta);
        $("#observacionesLugar").val(cita[id_Cita].observaciones);
        
        // Bloquear
        $("#provCita").prop("disabled",true);
        $("#pobCita").prop("disabled",true);
        $("#cpCita").prop("disabled",true);
        $("#calleCita").prop("disabled",true);
        $("#numCita").prop("disabled",true);
        $("#escaleraCita").prop("disabled",true);
        $("#pisoCita").prop("disabled",true);
        $("#puertaCita").prop("disabled",true);
        $("#observacionesLugar").prop("disabled",true);
    }
    $("#citas").modal("hide");
});

// Recuperar aparatos del usuario que han pasado por el registro
$(document).on("click","#selec-existent-aparato",function(){
   if(exist_Aparato === 0)
        $.ajax({
            url:"recuperarAparatos",
            type:"POST",
            data:{"id_Cliente":id_Cliente},
            success:function(aparatos){
                aparato = JSON.parse(aparatos);
                exist_Aparato = 1;
                if(!$.isEmptyObject(aparato)){   
                    $("#aparatos").modal("show");
                    $.each(aparato,function(id,a){
                        $("#menu-aparato").append("<option value='"+id+"'>"+a.tipo+" "+a.marca+" "+a.modelo+" "+a.numSerie+"</option>")
                    });
                }else{
                    $("#no-aparatos").modal("show");
                }
            }
        });
    else
        if(!$.isEmptyObject(aparato))   
            $("#aparatos").modal("show");
        else
            $("#no-aparatos").modal("show");
});

// Recuperar citas anteriores del usuario
$(document).on("click","#selec-existent-cita",function(){
    if(exist_Cita == 0)
        $.ajax({
            url:"recuperarCitas",
            type:"POST",
            data:{"id_Cliente":id_Cliente},
            success:function(citas){
                cita = JSON.parse(citas);
                exist_Cita = 1;
                if(!$.isEmptyObject(cita)){   
                    $("#citas").modal("show");
                    $.each(cita,function(id,c){
                        $("#menu-cita").append("<option value='"+id+"'>"+c.poblacion+" "+c.calle+" "+c.numero+" "+c.escalera+" "+c.piso+" "+c.puerta+"</option>")
                    });
                }else{
                    $("#no-citas").modal("show");
                }
            }
        });
    else
        if(!$.isEmptyObject(cita))   
            $("#citas").modal("show");
        else
            $("#no-citas").modal("show");
});

// Cambiar el motivo de la entrada
$(document).on("click","input[name=clase]",function(){
      if($(this).val()=='averia'){
        $(".Aparato").show();
        $(".Cita").hide();
      }else{
        $(".Aparato").hide();
        $(".Cita").show();
      }
  });

// Crear entrada
$(document).on("click","#submit-add",function(){
    if($("input[name=clase]:checked").val()=="averia"){
        if($("#nombre").val() == "" || $("#nSerie").val() == "" || $("#motivoIncidencia").val() == "" || ($("#tlfFijo").val() == "" && $("#tlfMovil").val() == "") || $("#dni").val() == "" || $("#apellidos").val() == ""){
            $("#nombre").addClass("error");
            $("#nSerie").addClass("error");
            $("#motivoIncidencia").addClass("error");
            $("#tlfFijo").addClass("error");
            $("#tlfMovil").addClass("error");
            $("#dni").addClass("error");
            $("#apellidos").addClass("error");
            $("#error").modal("show");
            
        }else{
            param={
                    "nombre":$("#nombre").val(),
                    "apellidos":$("#apellidos").val(),
                    "dni":$("#dni").val(),
                    "provincia":$("#prov").val(),
                    "poblacion":$("#pob").val(),
                    "cp":$("#cp").val(),
                    "calle":$("#calle").val(),
                    "numero":$("#num").val(),
                    "escalera":$("#escalera").val(),
                    "piso":$("#piso").val(),
                    "puerta":$("#puerta").val(),
                    "tlfCasa":$("#tlfFijo").val(),
                    "tlfMovil":$("#tlfMovil").val(),
                    "correo":$("#email").val(),
                    "observaciones":$("#observaciones").val(),
                    "tipo":$("#tipo").val(),
                    "marca":$("#marca").val(),
                    "modelo":$("#modelo").val(),
                    "color":$("#color").val(),
                    "lugar":$("#lugar").val(),
                    "observacionesAparato":$("#observacionesAparato").val(),
                    "nSerie":$("#nSerie").val(),
                    "motivo":$("#motivoIncidencia").val(),
                    "observacionesEntrada":$("#observacionesEntrada").val(),
                    "id_Cliente":id_Cliente,
                    "id_Aparato":id_Aparato
            };
            
            $.each(param,function(clave,valor){
                if(valor == "")
                    param[clave]=0;
            });
            
            $.ajax({
                url:"addEntradaAveria",
                type:"POST",
                data:param,
                success:function(respuesta){
                    if(parseInt(respuesta))
                        window.location.href = "entrada.jsp?id="+respuesta;
                    else
                    $("#error").modal("show");
                }
            });
        }
    }else{
        if($("#nombre").val() == "" || $("#fecha").val()== "" || $("#motivoCita").val() == ""
                || $("#calleCita").val() == "" || $("#pobCita").val()== "" || $("#tlfFijo").val() == "" || $("#dni").val() == "" || $("#apellidos").val() == ""){
            $("#nombre").addClass("error");
            $("#fecha").addClass("error");
            $("#motivoCita").addClass("error");
            $("#pobCita").addClass("error");
            $("#calleCita").addClass("error");
            $("#tlfFijo").addClass("error");
            $("#dni").addClass("error");
            $("#apellidos").addClass("error");
            $("#error").modal("show");
        }else{
            param={
                "nombre":$("#nombre").val(),
                "apellidos":$("#apellidos").val(),
                "dni":$("#dni").val(),
                "provincia":$("#prov").val(),
                "poblacion":$("#pob").val(),
                "cp":$("#cp").val(),
                "calle":$("#calle").val(),
                "numero":$("#num").val(),
                "escalera":$("#escalera").val(),
                "piso":$("#piso").val(),
                "puerta":$("#puerta").val(),
                "tlfCasa":$("#tlfFijo").val(),
                "tlfMovil":$("#tlfMovil").val(),
                "correo":$("#email").val(),
                "observaciones":$("#observaciones").val(),
                "fecha":$("#fecha").val(),
                "provinciaCita":$("#provCita").val(),
                "poblacionCita":$("#pobCita").val(),
                "cpCita":$("#cpCita").val(),
                "calleCita":$("#calleCita").val(),
                "numeroCita":$("#numCita").val(),
                "escaleraCita":$("#escaleraCita").val(),
                "pisoCita":$("#pisoCita").val(),
                "puertaCita":$("#puertaCita").val(),
                "motivo":$("#motivoCita").val(),
                "observacionesCita":$("#observacionesCita").val(),
                "observacionesLugar":$("#observacionesLugar").val(),
                "observacionesEntrada":$("#observacionesEntrada").val(),
                "id_Cliente":id_Cliente,
                "id_Direccion":id_Cita
            };
            
            $.each(param,function(clave,valor){
                if(valor == "")
                    param[clave]=0;
            });
            
            $.ajax({
                url:"addEntradaCita",
                type:"POST",
                data:param,
                success:function(respuesta){
                    if(parseInt(respuesta) > 0)
                        window.location.href = "entrada.jsp?id="+respuesta;
                    else
                    $("#error").modal("show");
                }
            });
        }
    }
});