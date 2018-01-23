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
    if($("#venta").val()=="" || $("#unidades").val()=="" || $("#nombre").val()==""){
        $("#venta").addClass("error");
        $("#unidades").addClass("error");
        $("#nombre").addClass("error");
        $("#error").modal("show");
    }else{
            param = {
                "proveedor":$("#proveedor").val(),
                "nombre":$("#nombre").val(),
                "descripcion":$("#descripcion").val(),
                "compra":$("#compra").val(),
                "unidades":$("#unidades").val(),
                "venta":$("#venta").val(),
                "alerta":$("#alerta").val()
            };
            
            $.each(param,function(campo,valor){
                if(valor=="")
                    param[campo]=0;
            });
            
            $.ajax({
                url:"addMaterial",
                type:"POST",
                data:param,
                success:function(resp){
                    if(resp=="true")
                        window.location.href="stock.jsp";
                    else
                        $("#error").modal("show");
                }
            });
        }
});

