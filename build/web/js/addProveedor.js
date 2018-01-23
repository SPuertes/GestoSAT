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
    if($("#nombre").val()==="" || $("#nif").val()==="" || $("#prov").val()==="" ||
                $("#pob").val() ==="" || $("#cp").val() ==="" || $("#calle").val() ==="" ||
                ($("#tlfFijo").val() ==="" && $("#tlfMovil").val() ==="" ) || $("#email").val()===""){
                
            $("#nombre").addclass("error");
            $("#nif").addclass("error");
            $("#prov").addclass("error");
            $("#pob").addclass("error");
            $("#cp").addclass("error");
            $("#calle").addclass("error");
            $("#tlfFijo").addclass("error");
            $("#tlfMovil").addclass("error");
            $("#email").addclass("error");
            $("#blancs").modal("show");
        }else{
            param = {
                    "nombre":$("#nombre").val(),
                    "nif":$("#nif").val(),
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
                    "fax":$("#fax").val()
                };

                $.each(param,function(campo,valor){
                    if(valor=="")
                        param[campo]=0;
                });

                $.ajax({
                    url:"addProveedor",
                    type:"POST",
                    data:param,
                    success:function(resp){
                        if(resp=="true")
                            window.location.href="proveedores.jsp";
                        else
                            $("#error").modal("show");
                    }
                });
    }
});

