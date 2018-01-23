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


$(document).on("click","#submit-add",function(){

    if($("#pass1").val() != $("#pass2").val() || $("#pass1").val()==""){
        $("#difPass").modal("show");
    }else{
        if($("#email").val() === "" || $("#nombre").val()==="" || $("#apellidos").val()==="" ||
                $("#dni").val()==="" || $("#prov").val()==="" || $("#pob").val()==="" ||  $("#cp").val()==="" ||
                $("#calle").val()===""||($("#tlfFijo").val()==="" &&$("#tlfMovil").val()==="") || 
                $("#base").val()==="" || $("#hora").val()===""){
            $("#blancs").modal("show");
            $("#email").addClass("error");
            $("#pass1").addClass("error");
            $("#pass2").addClass("error");
            $("#nombre").addClass("error");
            $("#apellidos").addClass("error");
            $("#dni").addClass("error");
            $("#prov").addClass("error");
            $("#pob").addClass("error");
            $("#cp").addClass("error");
            $("#calle").addClass("error");
            $("#tlfFijo").addClass("error");
            $("#tlfMovil").addClass("error");
            $("#base").addClass("error");
            $("#hora").addClass("error");
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
                "pass":$("#pass1").val()
            };
                
            if($("#gerente").is(":checked"))
                param["gerente"]= 1;
            else
                param["gerente"]= 0;
            
            $.each(param,function(campo,valor){
                if(valor=="")
                    param[campo]=0;
            });
            $.ajax({
                url:"addUser",
                type:"POST",
                data:param,
                success:function(resp){
                    if(resp=="true")
                        window.location.href="home.jsp";
                    else
                        $("#error").modal("show");
                }
            });
        }
    }
});

