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
<%@page import="GestoSAT.Cliente"%>
<%@page import="java.util.Vector"%>
<%@page import="GestoSAT.GestoSAT"%>
<%@page import="GestoSAT.Empleado"%>
<%@page import="GestoSAT.Gerente"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page session='true'%>
<%@include file="cabecera.jsp" %>

<html lang="es">
    <head>
    <meta charset="utf-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Apliación GestoSAt para la gestión de empresas SAT de electrónica">
    <meta name="author" content="Puertes Aleixandre, Salvador">
    <link rel="shortcut icon" href="images/favicon.png">
    
    <title>Cliente - GestoSAT</title>

    <!--Core CSS -->
    <link href="bs3/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-reset.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="js/bootstrap-datetimepicker/css/datetimepicker.css" />
    <link rel="stylesheet" type="text/css" href="js/jquery-multi-select/css/multi-select.css" />
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
                <span class="username"><%
                Empleado e = (Empleado)ses.getAttribute("usuario");
                out.print(e.getApellidos() + ", "+ e.getNombre());
                %></span>
                <b class="caret"></b>
            </a>
            <ul class="dropdown-menu extended logout">
                <li><a href="profile.jsp"><i class=" fa fa-suitcase"></i>Editar datos personales</a></li>
                <% if(((Boolean)ses.getAttribute("gerente"))){ %>
                <li><a href="confApp.jsp"><i class="fa fa-cog"></i> Configurar aplicación</a></li>
                <% } %>
                <li><a href="closeSession.jsp"><i class="fa fa-key"></i> Cerrar</a></li>
            </ul>
        </li>
        <!-- user login dropdown end -->
        <li>
            <div class="toggle-right-box">
                <div class="fa fa-bars"></div>
            </div>
        </li>
    </ul>
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
                        <h3>Cliente</h3>
                    </header>
                    <div class="panel-body">
                        <% 
                            GestoSAT gesto = (GestoSAT)ses.getAttribute("gestor");
                            Cliente cli = gesto.getCliente(Integer.parseInt(request.getParameter("id")));
                        %>
                        <div class='form-entradas'>
                            <h4>Datos Cliente</h4>
                            <%
                                if(cli == null)
                                    request.getRequestDispatcher("home.jsp").forward(request, response);
                                
                                // Datos del cliente
                            %>
                                <div class='conf-attr'><span class='attr-entries'>Nombre:</span><label class="my-label dynamic"><% out.print(cli.getNombre());%></label></div>
                                <div class='conf-attr'><span class='attr-entries'>Apellidos:</span> <label class="my-label dynamic"><% out.print(cli.getApellidos());%></label></div>
                                <div class='conf-attr'><span class='attr-entries'>DNI/NIF:</span> <label class="my-label dynamic"><% out.print(cli.getNif());%></label></div>
                                <div class='conf-attr'><span class='attr-entries'>Teléfonos:</span><div>
                                    <%  out.print("<label class='my-label dynamic'>"); 
                                        if(cli.getTlfFijo() != 0)
                                            out.print(cli.getTlfFijo());
                                        out.print("</label> <label class='my-label dynamic'>"); 
                                        if(cli.getTlfMovil() != 0)
                                            out.print(cli.getTlfMovil());
                                        out.print("</label>"); 
                                    %>
                                </div></div>
                                <div class='conf-attr'><span class='attr-entries'>Correo electrónico:</span> <label class="my-label dynamic"><%out.print(cli.getEmail());%></label></div>
                                <div class='conf-attr'><span class='attr-entries'>Observaciones:</span><label class="my-label dynamic"> <%out.print(cli.getObservaciones());%></label></div>
                        </div>
                        
                        <div class='form-entradas'>
                            <h4>Dirección</h4>
                            <div class='conf-attr'><span class='attr-entries'>Provincia:</span><label class="my-label dynamic"><% out.print(cli.getProvincia());%></label></div>
                            <div class='conf-attr'><span class='attr-entries'>Población:</span><label class="my-label dynamic"><% out.print(cli.getPoblacion());%></label></div>
                            <div class='conf-attr'><span class='attr-entries'>CP:</span><label class="my-label dynamic"><% out.print(cli.getCp());%></label></div>
                            <div class='conf-attr'><span class='attr-entries'>Calle:</span><label class="my-label dynamic"><% out.print(cli.getCalle());%></label></div>
                            <div class='conf-attr'><span class='attr-entries'>Número:</span><label class="my-label dynamic"><% out.print(cli.getNumero());%></label></div>
                            <div class='conf-attr'><span class='attr-entries'>Escalera:</span><label class="my-label dynamic"><% out.print(cli.getEscalera());%></label></div>
                            <div class='conf-attr'><span class='attr-entries'>Piso:</span><label class="my-label dynamic"><%
                                if(cli.getPiso() != 0)
                                        out.print(" "+cli.getPiso());
                            %></label></div>
                            <div class='conf-attr'><span class='attr-entries'>Puerta:</span><label class="my-label dynamic"><% out.print(cli.getPuerta());%></label></div>
                        </div>
                        
                        <div class="button-div">
                            <button type='button' class='btn btn-primary sep' id="crearFactura">Crear Factura</button>
                        </div>
                        <div class="button-div">
                            <button type='button' class='btn btn-primary sep' data-mode="editar" id="btn-mod">Editar</button>
                        </div>
                    </div>
                </section>
            </div>
        </div>
        <!-- page end-->
        </section>
    </section>
    <!--main content end-->
<!--right sidebar start-->
<div class="right-sidebar">
<div class="right-stat-bar">
<%@include file="recordatorio.jsp" %>
</div>
</div>
<!--right sidebar end-->

</section>

<div aria-hidden="true" aria-labelledby="empty" role="dialog" tabindex="-1" id="emptyValues" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <p>Debes rellenar todos los campos importantes</p>
            </div>
            <div class="modal-footer">
                <button data-dismiss="modal" class="btn btn-default" type="button">Cerrar</button>
            </div>
        </div>
      </div>
</div>

<div aria-hidden="true" aria-labelledby="error" role="dialog" tabindex="-1" id="errorSave" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <p>Se ha producido un error, vuelva a empezar</p>
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

<!--common script init for all pages-->
<script src="js/scripts.js"></script>

<!-- My Scripts -->
<script type='text/javascript'>
    id = <%out.print(request.getParameter("id"));%>;
</script>
<script src="js/editCliente.js"></script>
<script src="js/selector.js"></script>

</body>
</html>
