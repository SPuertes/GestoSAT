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
    
    if($("#iva").val() == "" || $("#userMySQL").val() == "" || $("#ipMySQL").val() == "" || $("#puertoMySQL").val() == "" || $("#userFTP").val() == ""|| $("#ipFTP").val() == "" || $("#puertoFTP").val() == ""){
            $("#error").modal("show");
            $("#userMySQL").addClass("error");
            $("#ipMySQL").addClass("error");
            $("#puertoMySQL").addClass("error");
            $("#userFTP").addClass("error");
            $("#ipFTP").addClass("error");
            $("#iva").addClass("error");    
            $("#puertoFTP").addClass("error");
    }else{
        param = {
            "iva":$("#iva").val(),
            "ipMySQL":$("#ipMySQL").val(),
            "puertoMySQL":$("#puertoMySQL").val(),
            "userMySQL":$("#userMySQL").val(),
            "ipFTP":$("#ipFTP").val(),
            "puertoFTP":$("#puertoFTP").val(),
            "userFTP":$("#userFTP").val(),
            "passMySQL":$("#passMySQL").val(),
            "passFTP":$("#passFTP").val()
        };
        if($("#logo").val()!=""){
            img = new FileReader();
            img.readAsDataURL($("#logo")[0].files[0]);
            img.onloadend = function(){
                param["logo"] = img.result;
                $.ajax({
                    url:"saveAppChange",
                    type:"POST",
                    data:param,
                    success:function(resp){
                        if(resp=="true")
                            $("#correcte").modal("show");
                        else
                            $("#error").modal("show");
                    }
                });
            }
            
        }else{
            param["logo"]="";
            $.ajax({
            url:"saveAppChange",
            type:"POST",
            data:param,
            success:function(resp){
                if(resp=="true")
                    $("#correcte").modal("show");
                else
                    $("#error").modal("show");
                }
            });
        }
    }
});


