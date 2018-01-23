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


total =0;
checks={};

calcularTotal = function(){
    total =0;
    $.each(checks,function(id,element){
        if($(element).prop("checked"))
            total+=parseFloat($($(element).parents(".panel-heading").find(".precio-albaran").children("span")[0]).html());
    });
    
    $("#precioFactura").html(total.toFixed(2));
};

$(document).ready(function(){
    checks = ($("input[type=checkbox]"));
    calcularTotal();
    $(".fa-chevron-down").click();

});

$(document).on("ifChanged","input",function(){
    calcularTotal();
});

$(document).on("click","#cancel",function(){
    window.location.href = "home.jsp";
});

$(document).on("click","#save",function(){
    var albaranes ="";
        
    if($("#conceptoFactura").val()!== "" && $("#formaPagoFactura").val() !== "" && checks.length >0){
        $.each(checks,function(id,element){
        if($(element).prop("checked"))
            albaranes+=$(element).val().trim()+",";
        });
        
        param = {
            "concepto":$("#conceptoFactura").val(),
            "formaPago":$("#formaPagoFactura").val(),
            "observaciones":$("#observacionesFactura").val(),
            "albaranes":albaranes.substr(0,albaranes.length-1),
            "idCliente":idCliente
        };
        
        $.ajax({
            url:"crearFactura",
            data:param,
            type:"POST",
            success:function(res){
                if(parseInt(res)>0)
                    window.location.href ="factura.jsp?id="+res;
                else
                    $("#errorSave").modal("show");
            }
        });
    }else{
        $("#emptyValues").modal("show");
        $("#conceptoFactura").addClass("error");
        $("#formaPagoFactura").addClass("error");
    }
});

$(document).on("click","#crearRecibo",function(){
    window.location.href = "crearRecibo.jsp?id="+id;
});

$(document).on("click","#editarRecibo",function(){
    window.location.href = "recibo.jsp?id="+idRecibo;
});

$(document).on("click","#btn-mod",function(){
    var vecAlbaranes;
    var albaranesAdd="";
    
    if($(this).data("mode")==="editar"){
        
        label = $.each($("label.dynamic"),function(){});
        for(i = 0; i < label.length; i++){
            $(label[i]).replaceWith(("<input type='text' class='dynamic' value='"+$(label[i]).text()+"'/>"));
        }
        
        inputs = $.each($("input.dynamic"),function(){ $(this);});
        
        $(this).data("mode","guardar");
        $(this).html("Guardar");
        $.ajax({
            url:'getAlbaranesFacturaCliente',
            data:{'id':id},
            type:'POST',
            success:function(res){
                vecAlbaranes = $.parseJSON(res);
                $.each($("input[type=checkbox]"),function(){$(this).prop("disabled",false);delete vecAlbaranes[parseInt($(this).val())];});
                $(".icheckbox_flat-green").removeClass("disabled");
                var fecha;
                $.each(vecAlbaranes,function(index){
                    fecha =this["creacion"].split(" ");
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
                    
                    albaranesAdd +="<div class='row albaran'><div class='col-md-12'><div class='my-panel my-panel-warning'><div class='panel-heading'><div class='flat-green'><input type='checkbox' value='"+index+"'/></div>"
                        +"<div class='toogle-portlet'><span> "+this["concepto"]+"</span><span class='obs-albaran'>"+fecha[2]+"/"+fecha[1]+"/"+fecha[0]+"</span><span class='obs-albaran'>"+this["observaciones"]+"</span><div class='pull-right'>"
                        +"<span class='pull-left precio-albaran'><span>"+this["total"]+"</span><span>&euro;</span></span><span class='tools pull-right'><a class='fa fa-chevron-down'></a></span></div></div></div><div class='panel-body'>"
                        +"<table class='table table-bordered table-striped table-condensed'><thead><tr><th>#</th><th>Nombre</th><th class='numeric'>Precio unidad/hora</th><th class='numeric'>Uds/Horas</th><th class='numeric'>Total (IVA incl.)</th></tr></thead><tbody>";
                    $.each(this["trabajo"],function(){
                            albaranesAdd+="<tr><td>*</td><td>"+this["nombre"]+"</td><td>"+this["precioH"]+"</td><td>"+this["cantidad"]+"</td><td>"+Math.round((parseFloat(this["precioH"])*parseFloat(this["cantidad"])*(iva+1))*100)/100+"</td></tr>";
                    });
                    $.each(this["material"],function(i){
                            albaranesAdd+="<tr><td>"+i+"</td><td>"+this["nombre"]+"</td><td>"+this["precioU"]+"</td><td>"+this["cantidad"]+"</td><td>"+Math.round((parseFloat(this["precioU"])*parseFloat(this["cantidad"])*(iva+1))*100)/100+"</td></tr>";
                    });
                    
                    albaranesAdd+="</tbody></table></div></div></div></div>";
                });
                
                $(".panel-albaranes-facturas").append(albaranesAdd);
                checks = ($("input[type=checkbox]"));
                $(".fa-chevron-down").click();
                
                $('.flat-green input').iCheck({
                    checkboxClass: 'icheckbox_flat-green',
                    radioClass: 'iradio_flat-green'
                });
            }
        });
        
    }else{
        if($("input:checked").length>0 && $(inputs[0]).val()!=="" && $(inputs[1]).val()!==""){
            $(this).data("mode","editar");
            $(this).html("Editar");
            
            param = {
                'ids':"",
                'id':id,
                'concepto':$(inputs[0]).val(),
                'formaPago':$(inputs[1]).val(),
                'observaciones':$(inputs[2]).val()
            };
            
            $.each($("input:checked"),function(){
                param["ids"]+=","+$(this).val().trim();
            });
            if(param["ids"].length>1)
                param["ids"] = param["ids"].substr(1);
                        
            $.ajax({
                url:'saveChangeFactura',
                data:param,
                type:'POST',
                success:function(res){
                    if(res==='true'){
                        $.each($("input[type=checkbox]"),function(){
                            if($(this).prop("checked"))
                                $(this).prop("disabled",true);
                            else
                                $(this).parents(".row.albaran").remove();    
                        });
                        $(inputs[0]).replaceWith($("<label class='my-label dynamic'>"+$(inputs[0]).val()+"</label>"));
                        $(inputs[1]).replaceWith($("<label class='my-label dynamic'>"+$(inputs[1]).val()+"</label>"));
                           $(inputs[2]).replaceWith($("<label class='my-label dynamic'>"+$(inputs[2]).val()+"</label>"));
                        $(".icheckbox_flat-green").addClass("disabled");
                    }else
                        $("errorSave").modal("show");
                }
            });
        }else
            $("#emptyValues").modal("show");
    }
});


$(document).on("click","#exportarPDF",function(){
   $.ajax({
       url:'factura2PDF',
       type:'POST',
       data:{'id':id},
       success:function(dir){
            if(dir!==""){
               window.open(dir);
            }
       }
   }); 
});

$(document).on("click","#exportarXLSX",function(){
   $.ajax({
       url:'factura2XLSX',
       type:'POST',
       data:{'id':id},
       success:function(dir){
            if(dir!==""){
               window.open(dir);
            }
       }
   }); 
});