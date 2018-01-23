/*  This file is part of GestoSAT.
+
+    GestoSAT is free software: you can redistribute it and/or modify
+    it under the terms of the GNU General Public License as published by
+    the Free Software Foundation, either version 3 of the License, or
+    (at your option) any later version.
+
+    GestoSAT is distributed in the hope that it will be useful,
+    but WITHOUT ANY WARRANTY; without even the implied warranty of
+    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
+    GNU General Public License for more details.
+
+    You should have received a copy of the GNU Affero General Public License
+    along with GestoSAT.  If not, see <http://www.gnu.org/licenses/>.
+ 
*    Salvador Puertes Aleixandre, July 2016
*
*/

$(document).on("click","#submit-conf",function(){

    if($("#pass1").val() != $("#pass2").val() || $("#pass1").val()==""){
        $("#difPass").modal("show");
    }else{
        if( $("#nif").val() =="" || $("#passMySQL").val() =="" || $("#userMySQL").val() == "" || $("#ipMySQL").val() == "" || $("#puertoMySQL").val() == "" || $("#passFTP").val() == "" || $("#userFTP").val() == ""|| $("#ipFTP").val() == ""|| $("#puertoFTP").val() == "" || $("#email").val() == ""){
            $("#blancs").modal("show");
            $("#passMySQL").addClass("error");
            $("#userMySQL").addClass("error");
            $("#ipMySQL").addClass("error");
            $("#puertoMySQL").addClass("error");
            $("#passFTP").addClass("error");
            $("#userFTP").addClass("error");
            $("#ipFTP").addClass("error");
            $("#puertoFTP").addClass("error");
            $("#email").addClass("error");
            $("#pass1").addClass("error");
            $("#pass2").addClass("error");
            $("#nif").addClass("error");
        }else{
            if($("#iva").val()=="")
                $("#iva").val("21");
            param = {
                "nombre":$("#nombre").val(),
                "apellidos":$("#apellidos").val(),
                "dni":$("#dni").val(),
                "provincia":$("#prov").val(),
                "poblacion":$("#pob").val(),
                "cp":$("#cp").val(),
                "calle":$("#calle").val(),
                "numero":$("#num").val(),
                "escalera":$("#escalera").val(),
                "piso":$("#piso").val(),
                "puerta":$("#puerta").val(),
                "tlfCasa":$("#tlfFijo").val(),
                "tlfMovil":$("#tlfMovil").val(),
                "sueldo":$("#base").val(),
                "hora":$("#hora").val(),
                "correo":$("#email").val(),
                "nombreEmp":$("#nombreEmp").val(),
                "nif":$("#nif").val(),
                "provinciaEmp":$("#provEmp").val(),
                "poblacionEmp":$("#pobEmp").val(),
                "cpEmp":$("#cpEmp").val(),
                "calleEmp":$("#calleEmp").val(),
                "numeroEmp":$("#numEmp").val(),
                "tlfCasaEmp":$("#tlfFijoEmp").val(),
                "tlfMovilEmp":$("#tlfMovilEmp").val(),
                "faxEmp":$("#faxEmp").val(),
                "correoEmp":$("#emailEmp").val(),
                "iva":$("#iva").val(),
                "pass":$("#pass1").val(),
                "ipMySQL":$("#ipMySQL").val(),
                "puertoMySQL":$("#puertoMySQL").val(),
                "userMySQL":$("#userMySQL").val(),
                "passMySQL":$("#passMySQL").val(),
                "ipFTP":$("#ipFTP").val(),
                "puertoFTP":$("#puertoFTP").val(),
                "userFTP":$("#userFTP").val(),
                "passFTP":$("#passFTP").val()
            };
            
            $.each(param,function(campo,valor){
                if(valor=="")
                    param[campo]=0;
            });
            
            $.ajax({
                url:"initialConfiguration",
                type:"POST",
                data:param,
                success:function(resp){
                    if(resp=="true")
                        window.location.href="closeSession.jsp";
                    else
                        $("#error").modal("show");
                }
            });
        }
    }
});

