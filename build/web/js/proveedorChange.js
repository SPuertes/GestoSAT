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
    if($("#num").val()==="")
        param["numero"]=0;
    else
        param["numero"]=$("#num").val();
    if($("#escalera").val()==="")
        param["escalera"]=0;
    else
        param["escalera"]=$("#escalera").val();
    if($("#piso").val()==="")
        param["piso"]=0;
    else
        param["piso"]=$("#piso").val();
    if($("#puerta").val()==="")
        param["puerta"]=0;
    else
        param["puerta"]=$("#puerta").val();
    
    if($("#tlfFijo").val()==="")
        param["tlfFijo"]=0;
    else
        param["tlfFijo"]=$("#tlfFijo").val();
    
    if($("#tlfMovil").val()==="")
        param["tlfMovil"]=0;
    else
        param["tlfMovil"]=$("#tlfMovil").val();
    if($("#fax").val()==="")
        param["fax"]=0;
    else
        param["fax"]=$("#fax").val();
    
    if((param["tlfFijo"]==="" && param["tlfMovil"]==="") || $("#nombre").val()==="" ||
        $("#nif").val()==="" || $("#prov").val()==="" || $("#pob").val()==="" ||
        $("#cp").val()==="" || $("#calle").val()==="" || $("#email").val()===""){
        
        $("#nombre").addClass("error");
        $("#nif").addClass("error");
        $("#prov").addClass("error");
        $("#pob").addClass("error");
        $("#cp").addClass("error");
        $("#calle").addClass("error");
        $("#tlfFijo").addClass("error");
        $("#tlfMovil").addClass("error");
        $("#email").addClass("error");
    }else{
        param["nombre"]=$("#nombre").val();
        param["nif"]=$("#nif").val();
        param["provincia"]=$("#prov").val();
        param["poblacion"]=$("#pob").val();
        param["cp"]=$("#cp").val();
        param["calle"]=$("#calle").val();
        param["email"]=$("#email").val();
        
        $.ajax({
            url:"actualizarProveedor",
            type:"POST",
            data:param,
            success:function(resp){
                if(resp === "true" ){
                    $("#correcte").modal("show");
                    $("#nombre").removeClass("error");
                    $("#nif").removeClass("error");
                    $("#prov").removeClass("error");
                    $("#pob").removeClass("error");
                    $("#cp").removeClass("error");
                    $("#calle").removeClass("error");
                    $("#tlfFijo").removeClass("error");
                    $("#tlfMovil").removeClass("error");
                    $("#email").removeClass("error");
                }else{
                    $("#error").modal("show");
                    $("#nombre").addClass("error");
                    $("#nif").addClass("error");
                    $("#prov").addClass("error");
                    $("#pob").addClass("error");
                    $("#cp").addClass("error");
                    $("#calle").addClass("error");
                    $("#tlfFijo").addClass("error");
                    $("#tlfMovil").addClass("error");
                    $("#email").addClass("error");
                }
            }
        });
    }
});