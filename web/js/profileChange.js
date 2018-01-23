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


$(document).on("click","#save-change",function(){
    if($("#pass1").val() !== $("#pass2").val()){
        $("#pass1").addClass("error");
        $("#pass2").addClass("error");
    }else{
        if((typeof $("#nif").val())!=="undefined"){
            if($("#email").val() === "" || $("#nif").val() === "" || ($("#tlfFijo").val()==="" && $("#tlfMovil").val()==="") ||
                    $("#nombre").val()==="" || $("#apellidos").val()==="" || $("#dni").val()==="" || $("#prov").val()===""||
                    $("#pob").val()==="" || $("#cp").val()==="" || $("#calle").val()==="" || $("#base").val()==="" ||
                    $("#hora").val()==="" || $("#email").val()===""|| $("#nif").val()==="" || $("#provEmp").val()==="" ||   
                    $("#pobEmp").val()==="" || $("#cpEmp").val()==="" ||$("#calleEmp").val()==="" || ($("#tlfFijoEmp").val()==="" &&
                    $("#tlfMovilEmp").val()==="") || $("#emailEmp").val()==="" || $("#nombreEmp").val()===""){
                $("#blancs").modal("show");
                $("#email").addClass("error");
                $("#nif").addClass("error");
                $("#tlfFijo").addClass("error");
                $("#tlfMovil").addClass("error");
                $("#dni").addClass("error");
                $("#prov").addClass("error");
                $("#pob").addClass("error");
                $("#cp").addClass("error");
                $("#calle").addClass("error");
                $("#base").addClass("error");
                $("#hora").addClass("error");
                $("#email").addClass("error");
                $("#nombreEmp").addClass("error");
                $("#nif").addClass("error");
                $("#provEmp").addClass("error");
                $("#pobEmp").addClass("error");
                $("#cpEmp").addClass("error");
                $("#calleEmp").addClass("error");
                $("#tlfFijoEmp").addClass("error");
                $("#tlfMovilEmp").addClass("error");
                $("#emailEmp").addClass("error");
            }else{
                param = {
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
                    "sueldo":$("#base").val(),
                    "hora":$("#hora").val(),
                    "correo":$("#email").val(),
                    "nombreEmp":$("#nombreEmp").val(),
                    "nif":$("#nif").val(),
                    "provinciaEmp":$("#provEmp").val(),
                    "poblacionEmp":$("#pobEmp").val(),
                    "cpEmp":$("#cpEmp").val(),
                    "calleEmp":$("#calleEmp").val(),
                    "numeroEmp":$("#numEmp").val(),
                    "tlfCasaEmp":$("#tlfFijoEmp").val(),
                    "tlfMovilEmp":$("#tlfMovilEmp").val(),
                    "faxEmp":$("#faxEmp").val(),
                    "correoEmp":$("#emailEmp").val()
                };
                if($("#pass1").val() != "")
                   param["pass"] = $("#pass1").val(); 

                $.each(param,function(campo,valor){
                    if(valor=="")
                        param[campo]=0;
                });
                $.ajax({
                    url:"saveProfileChange",
                    type:"POST",
                    data:param,
                    success:function(resp){
                        if(resp=="true")
                            $("#correcte").modal("show");
                        else
                            $("#error").modal("show");
                    }
                });
            }
        }else{
            if($("#email").val() === "" || $("#nif").val() === "" || ($("#tlfFijo").val()==="" && $("#tlfMovil").val()==="") ||
                    $("#nombre").val()==="" || $("#apellidos").val()==="" || $("#dni").val()==="" || $("#prov").val()===""||
                    $("#pob").val()==="" || $("#cp").val()==="" || $("#calle").val()==="" || $("#base").val()==="" ||
                    $("#hora").val()==="" || $("#email").val()===""){
                $("#blancs").modal("show");
                $("#email").addClass("error");
                $("#nif").addClass("error");
                $("#tlfFijo").addClass("error");
                $("#tlfMovil").addClass("error");
                $("#dni").addClass("error");
                $("#prov").addClass("error");
                $("#pob").addClass("error");
                $("#cp").addClass("error");
                $("#calle").addClass("error");
                $("#base").addClass("error");
                $("#hora").addClass("error");
                $("#email").addClass("error");
            }else{
                param = {
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
                    "sueldo":$("#base").val(),
                    "hora":$("#hora").val(),
                    "correo":$("#email").val(),
                    "nombreEmp":$("#nombreEmp").val(),
                    "nif":$("#nif").val(),
                    "provinciaEmp":$("#provEmp").val(),
                    "poblacionEmp":$("#pobEmp").val(),
                    "cpEmp":$("#cpEmp").val(),
                    "calleEmp":$("#calleEmp").val(),
                    "numeroEmp":$("#numEmp").val(),
                    "tlfCasaEmp":$("#tlfFijoEmp").val(),
                    "tlfMovilEmp":$("#tlfMovilEmp").val(),
                    "faxEmp":$("#faxEmp").val(),
                    "correoEmp":$("#emailEmp").val()
                };
                if($("#pass1").val() != "")
                   param["pass"] = $("#pass1").val(); 

                $.each(param,function(campo,valor){
                    if(valor=="")
                        param[campo]=0;
                });
                $.ajax({
                    url:"saveProfileChange",
                    type:"POST",
                    data:param,
                    success:function(resp){
                        if(resp=="true")
                            $("#correcte").modal("show");
                        else
                            $("#error").modal("show");
                    }
                });
            }
        }
    }
});


