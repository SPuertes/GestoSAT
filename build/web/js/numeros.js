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
    aux = $(this).parent();
    cant = aux.find(".input-cat-conf").val();
    id = aux.parent().attr("id");
    actual = parseFloat(aux.parent().find(".conf-attr > span > span").text());
    
    if(aux != ""){
        $.ajax({
            url:"addCantidad",
            type:"POST",
            data:{"id":id,"cantidad":cant},
            success:function(resp){
                if(resp == "true"){
                    aux.find("input").val("");
                    aux.parent().find(".conf-attr > span > span").text((actual+parseFloat(cant)));
                }   
            }
        });   
    }
});