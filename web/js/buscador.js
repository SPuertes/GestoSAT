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


buscar = function(str){
    $("#panel-results").empty();
    checks = $("input[type=checkbox]");
    
    param = {
        'buscar':str,
        'cliente':'',
        'entrada':'',
        'presupuesto':'',
        'albaran':'',
        'factura':'',
        'recibo':'',
        'pendiente':''
    };
    
    $.each(checks,function(){
       param[$(this).val()]=$(this).prop("checked");
    });
    if(param["buscar"] !=="")
        $.ajax({
            url:"buscar",
            data:param,
            type:"POST",
            success:function(res){
                resultados = $.parseJSON(res);
                var type;
                $.each(resultados,function(id,val){
                    var type = id.split(";");
                    var strAdd ="";
                    switch(type[1]){
                        case 'Cliente':
                            strAdd = "<div class='row'><div class='col-md-12'><div class='my-panel panel-primary'><div class='panel-heading'><button class='btn my-btn-see' id='"+id+"'><i class='fa fa-eye'></i></button><div class='toogle-portlet'>"+val['nombre']+"<span class='tools pull-right'><a class='fa fa-chevron-down'></a></span></div></div><div class='panel-body'>";
                            
                            $.each(val,function(name,content){
                                if(name!=='nombre')
                                    strAdd+="<div class='conf-attr'><span class='attr-entries'>"+name+":</span> "+content+"</div>";
                            });
                            
                            strAdd+="</div></div></div></div>";
                            $("#panel-results").append(strAdd);    
                            break;
                        case 'Entrada':
                            var fecha = val['creacion'].split(" ");
                            
                            if(fecha.length > 2 ){
                                var aux=[];
                                aux[0] = fecha[5];
                                switch(fecha[1]){
                                    case 'Jan':
                                        aux[1] = "01";
                                        break;
                                    case 'Feb':
                                        aux[1] = "02";
                                        break;
                                    case 'Mar':
                                        aux[1] = "03";
                                        break;
                                    case 'Apr':
                                        aux[1] = "04";
                                        break;
                                    case 'May':
                                        aux[1] = "05";
                                        break;
                                    case 'Jun':
                                        aux[1] = "06";
                                        break;
                                    case 'Jul':
                                        aux[1] = "07";
                                        break;
                                    case 'Aug':
                                        aux[1] = "08";
                                        break;
                                    case 'Sep':
                                        aux[1] = "09";
                                        break;
                                    case 'Oct':
                                        aux[1] = "10";
                                        break;
                                    case 'Nov':
                                        aux[1] = "11";
                                        break;
                                    case 'Dec':
                                        aux[1] = "12";
                                        break;
                                };
                                
                                aux[2] = fecha[2];
                                fecha = aux;
                            }else
                                fecha = fecha[0].split("-");
                            
                            strAdd = "<div class='row'><div class='col-md-12'><div class='my-panel panel-info'><div class='panel-heading'><button class='btn my-btn-see' id='"+id+"'><i class='fa fa-eye'></i></button><div class='toogle-portlet'>"+val['nombre']+"<span class='obs-albaran'>"+fecha[2]+"/"+fecha[1]+"/"+fecha[0]+"<span><span class='tools pull-right'><a class='fa fa-chevron-down'></a></span></div></div><div class='panel-body'>";
                            
                            $.each(val,function(name,content){
                                if(name!=='nombre' && name!=='creacion')
                                    strAdd+="<div class='conf-attr'><span class='attr-entries'>"+name.replace("_"," ")+":</span> "+content+"</div>";
                            });
                            
                            strAdd+="</div></div></div></div>";
                            $("#panel-results").append(strAdd);
                            break;
                        case 'Presupuesto':
                            var fecha = val['creacion'].split(" ");
                            if(fecha.length > 2 ){
                                var aux=[];
                                aux[0] = fecha[5];
                                switch(fecha[1]){
                                    case 'Jan':
                                        aux[1] = "01";
                                        break;
                                    case 'Feb':
                                        aux[1] = "02";
                                        break;
                                    case 'Mar':
                                        aux[1] = "03";
                                        break;
                                    case 'Apr':
                                        aux[1] = "04";
                                        break;
                                    case 'May':
                                        aux[1] = "05";
                                        break;
                                    case 'Jun':
                                        aux[1] = "06";
                                        break;
                                    case 'Jul':
                                        aux[1] = "07";
                                        break;
                                    case 'Aug':
                                        aux[1] = "08";
                                        break;
                                    case 'Sep':
                                        aux[1] = "09";
                                        break;
                                    case 'Oct':
                                        aux[1] = "10";
                                        break;
                                    case 'Nov':
                                        aux[1] = "11";
                                        break;
                                    case 'Dec':
                                        aux[1] = "12";
                                        break;
                                };
                                
                                aux[2] = fecha[2];
                                fecha = aux;
                            }else
                                fecha = fecha[0].split("-");
                            
                            strAdd = "<div class='row'><div class='col-md-12'><div class='my-panel panel-success'><div class='panel-heading'><button class='btn my-btn-see' id='"+id+"'><i class='fa fa-eye'></i></button><div class='toogle-portlet'>"+val['nombre']+"<span class='obs-albaran'>"+fecha[2]+"/"+fecha[1]+"/"+fecha[0]+"<span><span class='tools pull-right'><a class='fa fa-chevron-down'></a></span></div></div><div class='panel-body'>";
                            
                            $.each(val,function(name,content){
                                if(name!=='nombre' && name!=='creacion')
                                    strAdd+="<div class='conf-attr'><span class='attr-entries'>"+name.replace("_"," ")+":</span> "+content+"</div>";
                            });
                            
                            strAdd+="</div></div></div></div>";
                            $("#panel-results").append(strAdd);
                            break;
                        case 'Albaran':
                            var fecha = val['creacion'].split(" ");
                            
                            if(fecha.length > 2 ){
                                var aux=[];
                                aux[0] = fecha[5];
                                switch(fecha[1]){
                                    case 'Jan':
                                        aux[1] = "01";
                                        break;
                                    case 'Feb':
                                        aux[1] = "02";
                                        break;
                                    case 'Mar':
                                        aux[1] = "03";
                                        break;
                                    case 'Apr':
                                        aux[1] = "04";
                                        break;
                                    case 'May':
                                        aux[1] = "05";
                                        break;
                                    case 'Jun':
                                        aux[1] = "06";
                                        break;
                                    case 'Jul':
                                        aux[1] = "07";
                                        break;
                                    case 'Aug':
                                        aux[1] = "08";
                                        break;
                                    case 'Sep':
                                        aux[1] = "09";
                                        break;
                                    case 'Oct':
                                        aux[1] = "10";
                                        break;
                                    case 'Nov':
                                        aux[1] = "11";
                                        break;
                                    case 'Dec':
                                        aux[1] = "12";
                                        break;
                                };
                                
                                aux[2] = fecha[2];
                                fecha = aux;
                            }else
                                fecha = fecha[0].split("-");
                            
                            strAdd = "<div class='row'><div class='col-md-12'><div class='my-panel my-panel-warning'><div class='panel-heading'><button class='btn my-btn-see' id='"+id+"'><i class='fa fa-eye'></i></button><div class='toogle-portlet'>"+val['nombre']+"<span class='obs-albaran'>"+fecha[2]+"/"+fecha[1]+"/"+fecha[0]+"<span><span class='tools pull-right'><a class='fa fa-chevron-down'></a></span></div></div><div class='panel-body'>";
                            
                            $.each(val,function(name,content){
                                if(name!=='nombre' && name!=='creacion' && name!=='articulos')
                                    strAdd+="<div class='conf-attr'><span class='attr-entries'>"+name.replace("_"," ")+":</span> "+content+"</div>";
                                else if(name==='articulos'){
                                    strAdd+="<div><span class='attr-entries'>"+name+":</span></div><div class='col-md-4 col-sm-6 col-xs-12'><section class='flip-scroll'><table class='table table-bordered table-striped table-condensed cf'><thead class='cf'><tr><th>Nombre</th><th class='numeric'>Cantidad</th></tr></thead><tbody>";
                                    $.each(content,function(i,value){
                                        strAdd+="<tr><td>"+value['nombre']+"</td><td>"+value["cantidad"]+"</td></tr>";
                                    });
                                strAdd+="</tbody></table></section><div>";
                            }
                            });
                            
                            strAdd+="</div></div></div></div>";
                            $("#panel-results").append(strAdd);
                            break;
                        case 'Factura':
                            var fecha = val['creacion'].split(" ");
                            
                            if(fecha.length > 2 ){
                                var aux=[];
                                aux[0] = fecha[5];
                                switch(fecha[1]){
                                    case 'Jan':
                                        aux[1] = "01";
                                        break;
                                    case 'Feb':
                                        aux[1] = "02";
                                        break;
                                    case 'Mar':
                                        aux[1] = "03";
                                        break;
                                    case 'Apr':
                                        aux[1] = "04";
                                        break;
                                    case 'May':
                                        aux[1] = "05";
                                        break;
                                    case 'Jun':
                                        aux[1] = "06";
                                        break;
                                    case 'Jul':
                                        aux[1] = "07";
                                        break;
                                    case 'Aug':
                                        aux[1] = "08";
                                        break;
                                    case 'Sep':
                                        aux[1] = "09";
                                        break;
                                    case 'Oct':
                                        aux[1] = "10";
                                        break;
                                    case 'Nov':
                                        aux[1] = "11";
                                        break;
                                    case 'Dec':
                                        aux[1] = "12";
                                        break;
                                };
                                
                                aux[2] = fecha[2];
                                fecha = aux;
                            }else
                                fecha = fecha[0].split("-");
                            
                            strAdd = "<div class='row'><div class='col-md-12'><div class='my-panel panel-danger'><div class='panel-heading'><button class='btn my-btn-see' id='"+id+"'><i class='fa fa-eye'></i></button><div class='toogle-portlet'>"+val['nombre']+"<span class='obs-albaran'>"+fecha[2]+"/"+fecha[1]+"/"+fecha[0]+"<span><span class='tools pull-right'><a class='fa fa-chevron-down'></a></span></div></div><div class='panel-body'>";
                            
                            $.each(val,function(name,content){
                                if(name!=='nombre' && name!=='creacion')
                                    strAdd+="<div class='conf-attr'><span class='attr-entries'>"+name.replace("_"," ")+":</span> "+content+"</div>";
                            });
                            
                            strAdd+="</div></div></div></div>";
                            $("#panel-results").append(strAdd);
                            break;
                        case 'Recibo':
                            var fecha = val['creacion'].split(" ");
                            
                            if(fecha.length > 2 ){
                                var aux=[];
                                aux[0] = fecha[5];
                                switch(fecha[1]){
                                    case 'Jan':
                                        aux[1] = "01";
                                        break;
                                    case 'Feb':
                                        aux[1] = "02";
                                        break;
                                    case 'Mar':
                                        aux[1] = "03";
                                        break;
                                    case 'Apr':
                                        aux[1] = "04";
                                        break;
                                    case 'May':
                                        aux[1] = "05";
                                        break;
                                    case 'Jun':
                                        aux[1] = "06";
                                        break;
                                    case 'Jul':
                                        aux[1] = "07";
                                        break;
                                    case 'Aug':
                                        aux[1] = "08";
                                        break;
                                    case 'Sep':
                                        aux[1] = "09";
                                        break;
                                    case 'Oct':
                                        aux[1] = "10";
                                        break;
                                    case 'Nov':
                                        aux[1] = "11";
                                        break;
                                    case 'Dec':
                                        aux[1] = "12";
                                        break;
                                };
                                
                                aux[2] = fecha[2];
                                fecha = aux;
                            }else
                                fecha = fecha[0].split("-");
                            
                            strAdd = "<div class='row'><div class='col-md-12'><div class='my-panel panel-receipt'><div class='panel-heading'><button class='btn my-btn-see' id='"+id+"'><i class='fa fa-eye'></i></button><div class='toogle-portlet'>"+val['nombre']+"<span class='obs-albaran'>"+fecha[2]+"/"+fecha[1]+"/"+fecha[0]+"<span><span class='tools pull-right'><a class='fa fa-chevron-down'></a></span></div></div><div class='panel-body'>";
                            
                            $.each(val,function(name,content){
                                if(name!=='nombre' && name!=='creacion')
                                    strAdd+="<div class='conf-attr'><span class='attr-entries'>"+name.replace("_"," ")+":</span> "+content+"</div>";
                            });
                            
                            strAdd+="</div></div></div></div>";
                            $("#panel-results").append(strAdd);
                            break;
                    }
                });
                $(".toogle-portlet").click();
            }
        });
    
};

$(document).on("click","#buscar-page",function(){
    buscar($(".my-inp-search").val());
});

$(document).on("keypress",".my-inp-search",function(tecla){
    if(tecla.keyCode===13)
        buscar($(this).val());
});

$(document).on("click",".my-btn-see",function(){
    window.location.href= ($(this).attr("id")).split(";")[1].toLowerCase()+".jsp?id="+($(this).attr("id")).split(";")[0];
});

$(document).ready(function(){ if($(".my-inp-search").val()!=="") buscar($(".my-inp-search").val()); });