<%
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
%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="GestoSAT.GestoSAT"%>
<%@page import="GestoSAT.Empleado"%>
<%@page import="GestoSAT.Gerente"%>
<%@page import="GestoSAT.Cliente"%>
<%@page session='true'%>
<%@include file="cabecera.jsp" %>
<html lang="es">
    <head>
        <meta charset="utf-8">

        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content="Apliación GestoSAt para la gestión de empresas SAT de electrónica">
        <meta name="author" content="Puertes Aleixandre, Salvador">
        <link rel="shortcut icon" href="images/favicon.png">

        <title>Creador de entradas - GestoSAT</title>

        <!--Core CSS -->
        <link href="bs3/css/bootstrap.min.css" rel="stylesheet">
        <link href="css/bootstrap-reset.css" rel="stylesheet">
        <link href="font-awesome/css/font-awesome.css" rel="stylesheet" />                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          

        <link rel="stylesheet" type="text/css" href="js/bootstrap-datetimepicker/css/datetimepicker.css" />
        <link rel="stylesheet" type="text/css" href="js/select2/select2.css" />
        <!-- Custom styles for this template -->
        <link href="css/style.css" rel="stylesheet">
        <link href="css/style-responsive.css" rel="stylesheet" />

        <!-- Just for debugging purposes. Don't actually copy this line! -->
        <!--[if lt IE 9]>
        <script src="js/ie8-responsive-file-warning.js"></script><![endif]-->

        <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]-->
    </head>

    <body>

        <section id="container" >
            <!--header start-->
            <header class="header fixed-top clearfix">
                <!--logo start-->
                <div class="brand">

                    <a href="home.jsp" class="logo">
                        <img src="images/logo.png" alt="GestoSAT">
                    </a>
                    <div class="sidebar-toggle-box toogle-left">
                        <div class="fa fa-bars"></div>
                    </div>
                </div>
                <!--logo end-->

                <div class="nav notify-row" id="top_menu">

                </div>
                <div class="top-nav clearfix">
                    <!--search & user info start-->
                    <ul class="nav pull-right top-menu">
                        <!-- user login dropdown start-->
                        <li class="dropdown">
                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="username"><% Empleado e = (Empleado) ses.getAttribute("usuario");
                                    out.print(e.getApellidos() + ", " + e.getNombre());
                                    %></span>
                                <b class="caret"></b>
                            </a>
                            <ul class="dropdown-menu extended logout">
                                <li><a href="profile.jsp"><i class=" fa fa-suitcase"></i>Editar datos personales</a></li>
                                    <% if (((Boolean) ses.getAttribute("gerente"))) { %>
                                <li><a href="confApp.jsp"><i class="fa fa-cog"></i> Configurar aplicación</a></li>
                                    <% }
                                    %>
                                <li><a href="closeSession.jsp"><i class="fa fa-key"></i> Cerrar</a></li>
                            </ul>
                        </li>
                        <li>
                            <div class="toggle-right-box">
                                <div class="fa fa-bars"></div>
                            </div>
                        </li>
                    </ul>
                    <!--search & user info end-->
                </div>
            </header>
            <!--header end-->
            <aside>
                <div id="sidebar" class="nav-collapse">
                    <!-- sidebar menu start-->
                    <div class="leftside-navigation">
                        <%@include file="menu.jsp" %>
                    </div>        
                    <!-- sidebar menu end-->
                </div>
            </aside>
            <!--sidebar end-->
            <!--main content start-->
            <section id="main-content">
                <section class="wrapper">
                    <!-- page start-->

                    <div class="row">
                        <div class="col-sm-12">
                            <section class="panel">
                                <header class="panel-heading">
                                    <h3> Nueva entrada </h3>
                                </header>
                                <div class="panel-body">
                                    <div class="categoria-conf">
                                        <span class="title-cat-conf">Cuenta Cliente <a class="btn btn-primary" id="selec-existent-cliente">Existente</a></span>
                                        <div class="cliente">    
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>Nombre:</span>
                                                </div>
                                                <input class="input-cat-conf" type="text" id="nombre"/>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>Apellidos:</span>
                                                </div>
                                                <input class="input-cat-conf" type="text" id="apellidos"/>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>DNI:</span>
                                                </div>
                                                <input class="input-cat-conf" type="text" id="dni"/>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>Provincia:</span>
                                                </div>
                                                <input class="input-cat-conf" type="text" id="prov"/>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>Población:</span>
                                                </div>
                                                <input class="input-cat-conf" type="text" id="pob"/>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>CP:</span>
                                                </div>
                                                <input class="input-cat-conf" type="number" id="cp"/>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>Calle:</span>
                                                </div>
                                                <input class="input-cat-conf" type="text" id="calle"/>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>Número:</span>
                                                </div>
                                                <input class="input-cat-conf" type="text" id="num"/>
                                            </div>
                                            <div class="conf-attr">
                                                <div  class="name-cat-conf">
                                                    <span>Escalera:</span>
                                                </div>
                                                <input class="input-cat-conf" type="text" id="escalera"/>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>Piso:</span>
                                                </div>
                                                <input class="input-cat-conf num" type="number" id="piso"/>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>Puerta:</span>
                                                </div>
                                                <input class="input-cat-conf" type="text" id="puerta"/>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>Teléfono casa:</span>
                                                </div>
                                                <input class="input-cat-conf" type="number" id="tlfFijo"/>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>Teléfono móvil:</span>
                                                </div>
                                                <input class="input-cat-conf" type="number" id="tlfMovil"/>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>Correo electrónico:</span>
                                                </div>
                                                <input class="input-cat-conf" type="email" id="email"/>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>Observaciones:</span>
                                                </div>
                                                <input class="input-cat-conf" type="text" id="observaciones"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div><div>Avería <input type="radio" checked="" name="clase" value="averia"/></div>
                                        <div>Cita <input type="radio" name="clase" value="cita"/></div></div>
                                    <div class="categoria-conf">
                                        <div class="Aparato">
                                            <span class="title-cat-conf">Aparato </span>
                                            <div>    
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Tipo:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="tipo"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Marca:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="marca"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Modelo:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="modelo"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Color:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="color"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Nº Serie:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="nSerie"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Lugar almacenaje:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="lugar"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Motivo:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="motivoIncidencia"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Observaciones:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="observacionesAparato"/>
                                                </div>         
                                            </div>
                                        </div>
                                        <div class="Cita">
                                            <span class="title-cat-conf">Cita </span>
                                            <div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf-dia">
                                                        <span>Día:</span>
                                                    </div>
                                                    <div class="conf-attr-in">
                                                        <div data-date="" class="input-group date form_datetime-adv">   
                                                            <input type="text" id="fecha" readonly="" size="23">
                                                            <div class="input-group-btn">
                                                                <button type="button" class="btn btn-warning date-set"><i class="fa fa-calendar"></i></button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Provincia:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="provCita"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Población:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="pobCita"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>CP:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="number" id="cpCita"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Calle:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="calleCita"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Número:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="numCita"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div  class="name-cat-conf">
                                                        <span>Escalera:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="escaleraCita"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Piso:</span>
                                                    </div>
                                                    <input class="input-cat-conf num" type="number" id="pisoCita"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Puerta:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="puertaCita"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Motivo:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="motivoCita"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Observaciones dirección:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="observacionesLugar"/>
                                                </div>
                                                <div class="conf-attr">
                                                    <div class="name-cat-conf">
                                                        <span>Observaciones cita:</span>
                                                    </div>
                                                    <input class="input-cat-conf" type="text" id="observacionesCita"/>
                                                </div>
                                            </div>
                                        </div>

                                        <div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span>Observaciones entrada:</span>
                                                </div>
                                                <input class="input-cat-conf" type="text" id="observacionesEntrada"/>
                                            </div>
                                        </div>
                                        <div class="btn-container">
                                            <a class="btn btn-primary" id="submit-add">Añadir</a>
                                        </div>    
                                    </div>
                            </section>
                        </div>
                    </div>
                    <!-- page end-->
                </section>
            </section>
            <!--main content end-->

        </section>
<div class="right-sidebar">
<div class="right-stat-bar">
<%@include file="recordatorio.jsp" %>
</div>
</div>
        <div aria-hidden="true" aria-labelledby="camposImportantesVacios" role="dialog" tabindex="-1" id="error" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    </div>
                    <div class="modal-body">
                        <p>Los campos son erroneos. Asegurate que son valores válidos.</p>
                    </div>
                    <div class="modal-footer">
                        <button data-dismiss="modal" class="btn btn-default" type="button">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>
        <div aria-hidden="true" aria-labelledby="Clientes" role="dialog" id="clientes" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    </div>
                    <div class="modal-body">
                        <div class="inf-cliente">
                            <div><b>Cliente</b>: <span id="modal-persona"></span></div>
                            <div><b>Población</b>: <span id="modal-lugar"></span></div>
                            <div><b>Dirección</b>: <span id="modal-direccion"></span></div>
                            <div><b>Contacto</b>: <span id="modal-contacto"></span></div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 col-sm-2 control-label">Clientes: </label>
                            <div>
                                <select id="menu-cliente" class="populate" style="width: 300px">
                                        <option class="opt-cliente" value="0" selected="selected">Ninguno</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button data-dismiss="modal" class="btn btn-default" type="button">Cerrar</button>
                        <button id="aceptar-cliente" class="btn btn-primary" type="button">Aceptar</button>
                    </div>
                </div>
            </div>
        </div>
        <div aria-hidden="true" aria-labelledby="Aparatos" role="dialog"  id="aparatos" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    </div>
                    <div class="modal-body">
                        <div id="inf-aparatos">
                            <div><b>Tipo</b>: <span id="modal-tipo"></span></div>
                            <div><b>Marca</b>: <span id="modal-marca"></span></div>
                            <div><b>Modelo</b>: <span id="modal-modelo"></span></div>
                            <div><b>Nº Serie</b>: <span id="modal-serie"></span></div>
                            <div><b>Color</b>: <span id="modal-color"></span></div>
                            <div><b>Observaciones</b>: <span id="modal-observaciones"></span></div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 col-sm-2 control-label">Aparato: </label>
                            <div class="col-lg-6">
                                <select id="menu-aparato" class="populate " style="width: 300px">
                                        <option value="0">Ninguno</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button data-dismiss="modal" class="btn btn-default" type="button">Cerrar</button>
                        <button id="aceptar-aparato" class="btn btn-primary" type="button">Aceptar</button>
                    </div>
                </div>
            </div>
        </div>
        <div aria-hidden="true" aria-labelledby="Citas" role="dialog" id="citas" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    </div>
                    <div class="modal-body">
                        <div id="inf-citas">
                            <div><b>Dirección</b>: <span id="modal-direccionCita"></span></div>
                            <div><b>Observaciones</b>: <span id="modal-observacionesCita"></span></div>
                        </div>
                        <div class="form-group">
                            <label class="col-lg-2 col-sm-2 control-label">Direcciones citas: </label>
                            <div class="col-lg-6">
                                <select id="menu-cita" class="populate " style="width: 300px">
                                        <option value="0">Ninguno</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button data-dismiss="modal" class="btn btn-default" type="button">Cerrar</button>
                        <button id="aceptar-cita" class="btn btn-primary" type="button">Aceptar</button>
                    </div>
                </div>
            </div>
        </div>
        <div aria-hidden="true" aria-labelledby="No Aparatos" role="dialog" tabindex="-1" id="no-aparatos" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    </div>
                    <div class="modal-body">
                        <span>No hay aparatos registrados para este cliente</span>
                    </div>
                    <div class="modal-footer">
                        <button data-dismiss="modal" class="btn btn-default" type="button">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>
        <div aria-hidden="true" aria-labelledby="No Citas" role="dialog" tabindex="-1" id="no-citas" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    </div>
                    <div class="modal-body">
                        <span>No hay citas registrados para este cliente</span>
                    </div>
                    <div class="modal-footer">
                        <button data-dismiss="modal" class="btn btn-default" type="button">Cerrar</button>
                    </div>
                </div>
            </div>
        </div>
        <!--Core js-->
        <script src="js/jquery.js"></script>
        <script src="bs3/js/bootstrap.min.js"></script>
        <script class="include" type="text/javascript" src="js/jquery.dcjqaccordion.2.7.js"></script>
        <script src="js/jquery.scrollTo.min.js"></script>
        <script src="js/jQuery-slimScroll-1.3.0/jquery.slimscroll.js"></script>
        <script src="js/jquery.nicescroll.js"></script>
        <script type="text/javascript" src="js/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
        <script type="text/javascript" src="js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"></script>
        <script type="text/javascript" src="js/bootstrap-timepicker/js/bootstrap-timepicker.js"></script>

        <!--common script init for all pages-->
        <script src="js/scripts.js"></script>

        <script src="js/entrada.js"></script>
        <script type="text/javascript" >
            clientes = {
            <%
                // Recuperar clientes
                Map clientes = ((GestoSAT) ses.getAttribute("gestor")).getCliente();

                Iterator itClientes = clientes.entrySet().iterator();
                while (itClientes.hasNext()) {
                    Map.Entry aux = (Map.Entry) itClientes.next();
                    Cliente c = (Cliente) aux.getValue();
                    out.print("'" + ((Integer) aux.getKey()) + "':{"
                            + "'nombre':'" + c.getNombre() + "',"
                            + "'apellidos':'" + c.getApellidos() + "',"
                            + "'NIF':'" + c.getNif() + "',"
                            + "'provincia':'" + c.getProvincia() + "',"
                            + "'poblacion':'" + c.getPoblacion() + "',"
                            + "'CP':'" + c.getCp() + "',"
                            + "'calle':'" + c.getCalle() + "',"
                            + "'numero':'" + c.getNumero() + "',"
                            + "'escalera':'" + c.getEscalera() + "',"
                            + "'piso':'" + c.getPiso() + "',"
                            + "'puerta':'" + c.getPuerta() + "',"
                            + "'tlfFijo':'" + c.getTlfFijo()+"',"
                            + "'tlfMovil':'" + c.getTlfMovil()+"',"
                            + "'email':'" + c.getEmail() + "',"
                            + "'observaciones':'" + c.getObservaciones() + "'"
                            + "},"
                    );
                }%>
            };
            $.each(clientes, function (id, c) {
                $("#menu-cliente").append("<option class='opt-cliente' value='" + id + "'>" + c["nombre"] + " " + c["apellidos"] + "</option>");
            });

            id_Cliente = 0;
            id_Aparato = 0;
        </script>
        <script src="js/select2/select2.js"></script>
        <script type="text/javascript">$("#menu-cliente").select2({});</script>
        <script src="js/advanced-form.js"></script>
        <!-- My JS -->
    <script src="js/selector.js"></script>

    </body>
</html>