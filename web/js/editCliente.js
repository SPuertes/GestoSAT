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


$(document).on("click","#crearFactura",function(){
   window.location.href= "crearFactura.jsp?id="+id; 
});

$(document).on("click","#btn-mod",function(){
    aux = this;
    if($(this).data("mode")==="editar"){
        $(this).html("Guardar");
        $(this).data("mode","guardar");
        
        labels=$("label.dynamic");
        label = $.each($("label.dynamic"),function(){});
        
        for(i = 0; i < label.length; i++){
            if(i === 3 || i === 4 || i === 9 || i === 13){
                if(i !== 13)
                    $(label[i]).replaceWith(("<input type='number' class='dynamic' value='"+$(label[i]).text()+"'/>"));
                else    
                    $(label[i]).replaceWith(("<input type='number' class='dynamic num' value='"+$(label[i]).text()+"'/>"));
            }else
                $(label[i]).replaceWith(("<input type='text' class='dynamic' value='"+$(label[i]).text()+"'/>"));
        }
        
        inputs = $.each($(".dynamic"),function(){$(this);});
        
    }else{
        if($(inputs[0]).val()!=="" && $(inputs[1]).val()!=="" && $(inputs[2]).val()!=="" && ($(inputs[3]).val()!=="" || $(inputs[4]).val()!=="" ) && $(inputs[5]).val()!=="" 
                && $(inputs[7]).val()!=="" && $(inputs[8]).val()!=="" && $(inputs[9]).val()!=="" && $(inputs[10]).val()!==""){
           
            piso ="0";
            
            if($(inputs[13]).val()!=="")
                piso=$(inputs[13]).val();
            
            param = {
               'id':id,
               'nombre':$(inputs[0]).val(),
               'apellidos':$(inputs[1]).val(),
               'nif':$(inputs[2]).val(),
               'tlf_Contacto':$(inputs[3]).val(),
               'tlf_Auxiliar':$(inputs[4]).val(),
               'email':$(inputs[5]).val(),
               'observaciones':$(inputs[6]).val(),
               'provincia':$(inputs[7]).val(),
               'poblacion':$(inputs[8]).val(),
               'cp':$(inputs[9]).val(),
               'calle':$(inputs[10]).val(),
               'numero':$(inputs[11]).val(),
               'escalera':$(inputs[12]).val(),
               'piso':piso,
               'puerta':$(inputs[14]).val()
           };
           
           $.ajax({
               url:'saveChangeCliente',
               data:param,
               type:'POST',
               success:function(res){
                   if(res==='true'){
                        for(i = 0; i < inputs.length; i++)
                            $(inputs[i]).replaceWith($("<label class='my-label'>"+$(inputs[i]).val()+"</label>"));
                        $(aux).html("Editar");
                        $(aux).data("mode","editar");
                   }else
                       $("#errorSave").modal("show")
               }
           });
           
        }else{
            $("#emptyValues").modal("show");
            $(inputs[0]).addClass("error");
            $(inputs[1]).addClass("error");
            $(inputs[2]).addClass("error");
            $(inputs[3]).addClass("error");
            $(inputs[4]).addClass("error");
            $(inputs[5]).addClass("error");
            $(inputs[7]).addClass("error");
            $(inputs[8]).addClass("error");
            $(inputs[9]).addClass("error");
            $(inputs[10]).addClass("error");
        }
    }
});