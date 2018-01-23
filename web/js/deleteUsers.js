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

$(document).on("click",".fa-times",function(){
    var aux = $(this).parent(); 
    $.ajax({
        url:"deleteUser",
        type:"POST",
        data:{"id":$(this).parent().attr("id")},
        success:function(respuesta){
            if(respuesta=="true")
                if(aux.find(".fa-eject").length > 0)
                    aux.remove();
                else{
                    aux.append("<i class='fa fa-eject sep'></i>");
                    aux.find(".empleado").addClass("borrado");
                 }
        }
    });
});

$(document).on("click",".fa-eject",function(){
    var aux = $(this);
    $.ajax({
        url:"activate",
        type:"POST",
        data:{"id":$(this).parent().attr("id")},
        success:function(respuesta){
            if(respuesta=="true"){
                aux.parent().find(".borrado").removeClass("borrado");
                aux.remove();
            }
        }
    });
});