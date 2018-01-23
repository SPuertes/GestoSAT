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


$(document).on('click','#cancel',function(){ window.location.href = "factura.jsp?id="+id; });


// Actualisar per a recibo
$(document).on('click','#save',function(){
    // Mirar que entrega relleno
    if(!$.grep($(".entrega"),function(obj,id){ if(id<5) return $(obj).val()==="";}).length){
         param = {'id':id,'observaciones':$("#observaciones").val()};

            switch($("#lugarEntrega").val()){
                case 'Domicilio':
                    param['entrega'] = entrega["cliente"];
                    break;
                case 'Otro':
                    param['entrega']['provEntrega'] = $("#provEntrega").val();
                    param['entrega']['pobEntrega'] = $("#pobEntrega").val();
                    param['entrega']['cpEntrega'] = $("#cpEntrega").val();
                    param['entrega']['calleEntrega'] =$ ("#calleEntrega").val();
                    param['entrega']['numEntrega'] = $("#numEntrega").val();
                    param['entrega']['escaleraEntrega'] = $("#escaleraEntrega").val();
                    param['entrega']['pisoEntrega'] = $("#pisoEntrega").val();
                    param['entrega']['puertaEntrega'] = $("#puertaEntrega").val();
                    break;
                case 'Tienda':
                    param['entrega'] = entrega["tienda"];
                    break;
            };
            
            if(param['entrega']['pisoEntrega'] === "")
                param['entrega']['pisoEntrega']=0;
            
                $.ajax({
                    url:"crearRecibo",
                    data:param,
                    type:"POST",
                    success:function(res){
                        if(parseInt(res)>0)
                            window.location.href = "recibo.jsp?id="+res;
                        else
                            $("#errorSave").modal("show")// Error
                    }
                });
        }else{
            $("#emptyValues").modal("show");
            $.each($(".entrega"),function(id,obj){if(id<5) $(obj).addClass("error");});
        }
});

$(document).on("change","#lugarEntrega",function(){
    switch($(this).val()){
        case 'Domicilio':
            $("#provEntrega").val(entrega["cliente"]['provEntrega']);
            $("#provEntrega").prop("disabled",true);
            $("#pobEntrega").val(entrega["cliente"]['pobEntrega']);
            $("#pobEntrega").prop("disabled",true);
            $("#cpEntrega").val(entrega["cliente"]['cpEntrega']);
            $("#cpEntrega").prop("disabled",true);
            $("#calleEntrega").val(entrega["cliente"]['calleEntrega']);
            $("#calleEntrega").prop("disabled",true);
            $("#numEntrega").val(entrega["cliente"]['numEntrega']);
            $("#numEntrega").prop("disabled",true);
            $("#escaleraEntrega").val(entrega["cliente"]['escaleraEntrega']);
            $("#escaleraEntrega").prop("disabled",true);
            $("#pisoEntrega").val(entrega["cliente"]['pisoEntrega']);
            $("#pisoEntrega").prop("disabled",true);
            $("#puertaEntrega").val(entrega["cliente"]['puertaEntrega']);
            $("#puertaEntrega").prop("disabled",true);
            break;
        case 'Otro':
            $("#provEntrega").val("");
            $("#provEntrega").prop("disabled",false);
            $("#pobEntrega").val("");
            $("#pobEntrega").prop("disabled",false);
            $("#cpEntrega").val("");
            $("#cpEntrega").prop("disabled",false);
            $("#calleEntrega").val("");
            $("#calleEntrega").prop("disabled",false);
            $("#numEntrega").val("");
            $("#numEntrega").prop("disabled",false);
            $("#escaleraEntrega").val("");
            $("#escaleraEntrega").prop("disabled",false);
            $("#pisoEntrega").val("");
            $("#pisoEntrega").prop("disabled",false);
            $("#puertaEntrega").val("");
            $("#puertaEntrega").prop("disabled",false);
            break;
        case 'Tienda':
            $("#provEntrega").val(entrega["tienda"]['provEntrega']);
            $("#provEntrega").prop("disabled",true);
            $("#pobEntrega").val(entrega["tienda"]['pobEntrega']);
            $("#pobEntrega").prop("disabled",true);
            $("#cpEntrega").val(entrega["tienda"]['cpEntrega']);
            $("#cpEntrega").prop("disabled",true);
            $("#calleEntrega").val(entrega["tienda"]['calleEntrega']);
            $("#calleEntrega").prop("disabled",true);
            $("#numEntrega").val(entrega["tienda"]['numEntrega']);
            $("#numEntrega").prop("disabled",true);
            $("#escaleraEntrega").val(entrega["tienda"]['escaleraEntrega']);
            $("#escaleraEntrega").prop("disabled",true);
            $("#pisoEntrega").val(entrega["tienda"]['pisoEntrega']);
            $("#pisoEntrega").prop("disabled",true);
            $("#puertaEntrega").val(entrega["tienda"]['puertaEntrega']);
            $("#puertaEntrega").prop("disabled",true);
            break;
    };
});

$(document).on("click","#exportarPDF",function(){
   $.ajax({
       url:'recibo2PDF',
       type:'POST',
       data:{'id':idRecibo},
       success:function(dir){
            if(dir!==""){
               window.open(dir);
            }
       }
   }); 
});

$(document).on("click","#exportarXLSX",function(){
   $.ajax({
       url:'recibo2XLSX',
       type:'POST',
       data:{'id':idRecibo},
       success:function(dir){
            if(dir!==""){
               window.open(dir);
            }
       }
   }); 
});