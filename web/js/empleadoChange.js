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
    
    if($("#base").val()==="" || $("#hora").val()){
        $("#base").addClass("error");
        $("#hora").addClass("error");
    }else{
        $("#base").removeClass("error");
        $("#hora").removeClass("error");
        
        param={
            'suelfo':$("#base").val(),
            'hora':$("#hora").val()
            };
        
        if($("#gerente").is(":checked"))
            param["gerente"]= 1;
        else
            param["gerente"]= 0;
        
        param["sueldo"]=$("#base").val();
        param["hora"]=$("#hora").val();
    
        $.ajax({
            url:"actualizarEmpleado",
            type:"POST",
            data:param,
            success:function(resp){
                if(resp == "true" )
                    $("#correcte").modal("show");
                else
                    $("#error").modal("show");
            }
        });
    }
});