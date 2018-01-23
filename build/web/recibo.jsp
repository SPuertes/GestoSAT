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
<%@page import="GestoSAT.Recibo"%>
<%@page import="GestoSAT.Factura"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="GestoSAT.MaterialTrabajos"%>
<%@page import="GestoSAT.Albaran"%>
<%@page import="GestoSAT.Trabajo"%>
<%@page import="GestoSAT.Cliente"%>
<%@page import="java.util.Vector"%>
<%@page import="GestoSAT.GestoSAT"%>
<%@page import="GestoSAT.Empleado"%>
<%@page import="GestoSAT.Gerente"%>
<%@page import="GestoSAT.Entrada"%>
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
    
    <title>Recibo - GestoSAT</title>
    
    <!--Core CSS -->
    <link href="bs3/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-reset.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="js/bootstrap-datetimepicker/css/datetimepicker.css" />
    <link href="js/iCheck/skins/flat/green.css" rel="stylesheet">
    <link href="css/table-responsive.css" rel="stylesheet">
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
                        <h3>Recibo</h3>
                    </header>
                    <div class="panel-body">
                            <%
                                GestoSAT gestoSAT = (GestoSAT)ses.getAttribute("gestor");
                                Recibo recibo = gestoSAT.getRecibo(Integer.parseInt(request.getParameter("id")));
                                Factura factura = recibo.getFactura(); 
                                Cliente cli = factura.getCliente();
                                
                                // Datos del cliente
                                %>
                                <div class='form-entradas'>
                                        <h4>Datos Cliente</h4>
                                        <div class="cliente">    
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span class='attr-entries'>Nombre:</span>
                                                </div>
                                                <% out.print(cli.getNombre());%>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span class='attr-entries'>Apellidos:</span>
                                                </div>
                                                <% out.print(cli.getApellidos());%>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span class='attr-entries'>DNI:</span>
                                                </div>
                                                <% out.print(cli.getNif());%>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span class='attr-entries'>Provincia:</span>
                                                </div>
                                                <% out.print(cli.getProvincia());%>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span class='attr-entries'>Población:</span>
                                                </div>
                                                <% out.print(cli.getPoblacion());%>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span class='attr-entries'>CP:</span>
                                                </div>
                                                <% out.print(cli.getCp());%>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span class='attr-entries'>Calle:</span>
                                                </div>
                                                <% out.print(cli.getCalle());%>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span class='attr-entries'>Número:</span>
                                                </div>
                                                <% out.print(cli.getNumero());%>
                                            </div>
                                            <div class="conf-attr">
                                                <div  class="name-cat-conf">
                                                    <span class='attr-entries'>Escalera:</span>
                                                </div>
                                                <% out.print(cli.getEscalera());%>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span class='attr-entries'>Piso:</span>
                                                </div>
                                                <% if(cli.getPiso()!=0)
                                                    out.print(cli.getNombre());%>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span class='attr-entries'>Puerta:</span>
                                                </div>
                                                    <% out.print(cli.getPuerta());%>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span class='attr-entries'>Teléfono casa:</span>
                                                </div>
                                                    <% if(cli.getTlfFijo()!=0)
                                                        out.print(cli.getTlfFijo());%>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span class='attr-entries'>Teléfono móvil:</span>
                                                </div>
                                                <% if(cli.getTlfMovil()!=0)
                                                        out.print(cli.getTlfMovil());%>
                                            </div>
                                            <div class="conf-attr">
                                                <div class="name-cat-conf">
                                                    <span class='attr-entries'>Correo electrónico:</span>
                                                </div>
                                                <% out.print(cli.getEmail());%>
                                            </div>
                                        </div>
                                </div>
                        <div class="form-entradas"> 
                            <h4>Entrega del recibo</h4>
                            <div class='conf-attr'><span class='attr-entries'>Provincia:</span> <% out.print(recibo.getProvincia());%></div>
                            <div class='conf-attr'><span class='attr-entries'>Población:</span> <% out.print(recibo.getPoblacion());%></div>
                            <div class='conf-attr'><span class='attr-entries'>CP:</span> <% out.print(recibo.getCp());%></div>
                            <div class='conf-attr'><span class='attr-entries'>Calle:</span> <% out.print(recibo.getCalle());%></div>
                            <div class='conf-attr'><span class='attr-entries'>Número:</span> <% out.print(recibo.getNumero());%></div>
                            <div class='conf-attr'><span class='attr-entries'>Escalera:</span> <% out.print(recibo.getEscalera());%></div>
                            <div class='conf-attr'><span class='attr-entries'>Piso:</span> <% if(recibo.getPiso()!=0) out.print(recibo.getPiso());%></div>
                            <div class='conf-attr'><span class='attr-entries'>Puerta:</span>  <% out.print(recibo.getPuerta());%></div>
                            <div class='conf-attr'><span class='attr-entries'>Observaciones:</span>  <% out.print(recibo.getObservaciones());%></div>
                        </div>
                        <div class="form-entradas">
                            <div class="form-entradas">
                                <h4>Facturas contenidas</h4>
                                <div class='conf-attr'><span>Concepto:</span> <% out.print(factura.getConcepto()); %></div>
                                <div class='conf-attr'><span>Forma de pago:</span> <% out.print(factura.getFormaPago()); %></div>
                                <div class='conf-attr'><span>Observaciones:</span> <% out.print(factura.getObservaciones()); %></div>
                        
                            <div class="panel-albaranes-facturas">
                                <%  Map albaranes = factura.getAlbaranes();
                                    
                                    Map auxAlbaranes = new HashMap();
                                    auxAlbaranes.putAll(cli.cargarAlbaranesPendientes(gestoSAT.getIdCliente(cli)));
                                    
                                    auxAlbaranes.putAll(albaranes);
                                    
                                    Iterator itAlbaranes = albaranes.entrySet().iterator();
                                    
                                    Map stock = gestoSAT.getStock();
                                    
                                    float iva = gestoSAT.getIva()/(float)100, precioIVAUd, precioMO, horas, total = 0;
                                    
                                    DecimalFormat df = new DecimalFormat("0.00");
                                    
                                    Iterator itContenido;
                                    while(itAlbaranes.hasNext()){ 
                                        Map.Entry auxAlbaran = (Map.Entry)itAlbaranes.next();
                                    %>
                                        <div class="row albaran">
                                            <div class="col-md-12">
                                                <div class="my-panel my-panel-warning">
                                                    <div class="panel-heading">
                                                        <div class="toogle-portlet">
                                                            <span><% out.print(((Albaran)auxAlbaran.getValue()).getConcepto()); %></span>
                                                            <span class="obs-albaran"> <% out.print(((Albaran)auxAlbaran.getValue()).getFchCreacion().getDate()+"/"
                                                                    + (1+((Albaran)auxAlbaran.getValue()).getFchCreacion().getMonth())+"/"
                                                                    + (1900+((Albaran)auxAlbaran.getValue()).getFchCreacion().getYear())); %> </span>
                                                            <span class="obs-albaran"><% out.print(((Albaran)auxAlbaran.getValue()).getObservaciones()); %></span>
                                                            <div class="pull-right">
                                                                <span class="pull-left precio-albaran"><span><%total += ((Albaran)auxAlbaran.getValue()).getTotal(); out.print(((Albaran)auxAlbaran.getValue()).getTotal()); %></span><span>&euro;</span></span>
                                                                <span class="tools pull-right">
                                                                    <a class="fa fa-chevron-down" ></a>
                                                                </span>
                                                            </div>
                                                        </div>
                                                    </div>
                                                        <%if(!((Albaran)auxAlbaran.getValue()).getMaterialUtilizado().isEmpty() || !((Albaran)auxAlbaran.getValue()).getTrabajoRealizado().isEmpty()){
                                                            itContenido = ((Albaran)auxAlbaran.getValue()).getMaterialUtilizado().entrySet().iterator();
                                                        %>
                                                    <div class="panel-body">
                                                        <section id="flip-scroll">                
                                                            <table class="table table-bordered table-striped table-condensed cf">
                                                                <thead class="cf">
                                                                    <tr>
                                                                        <th>#</th>
                                                                        <th>Nombre</th>
                                                                        <th class="numeric">Precio unidad/hora</th>
                                                                        <th class="numeric">Uds/Horas</th>
                                                                        <th class="numeric">Total (IVA incl.)</th>
                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <% while(itContenido.hasNext()){
                                                                        Map.Entry auxContenido = (Map.Entry)itContenido.next();
                                                                    %>
                                                                        <tr>
                                                                            <td><% out.print(auxContenido.getKey()); %></td>
                                                                            <td><% out.print(((Stock)stock.get(Integer.parseInt(auxContenido.getKey().toString()))).getNombre()); %></td>
                                                                            <% precioIVAUd = ((Stock)stock.get(Integer.parseInt(auxContenido.getKey().toString()))).getPrecioUnidad()*(1+iva);%>
                                                                            <td class="numeric"><% out.print(df.format(precioIVAUd).replace(",",".")); %></td>
                                                                            <td class="numeric"><% out.print(((MaterialTrabajos)auxContenido.getValue()).getCantidad()); %></td>
                                                                            <td class="numeric"><% out.print(df.format(((MaterialTrabajos)auxContenido.getValue()).getCantidad()*precioIVAUd).replace(",",".")); %></td>
                                                                        </tr>
                                                                    <% } 
                                                                    itContenido = ((Albaran)auxAlbaran.getValue()).getTrabajoRealizado().entrySet().iterator();
                                                                    precioMO =0;
                                                                    horas =0;

                                                                    while(itContenido.hasNext()){
                                                                        Map.Entry auxMO = (Map.Entry)itContenido.next();
                                                                        precioMO += gestoSAT.getEmpleado(Integer.parseInt(auxMO.getKey().toString())).getPrecioHora()*(1+iva)*((Trabajo)auxMO.getValue()).getHoras();
                                                                        horas += ((Trabajo)auxMO.getValue()).getHoras();
                                                                    %>
                                                                        <tr>
                                                                            <td>*</td>
                                                                            <td><% Empleado empleado = gestoSAT.getEmpleado(Integer.parseInt(auxMO.getKey().toString()));
                                                                            out.print(empleado.getNombre()+" "+empleado.getApellidos());
                                                                            %></td>
                                                                            <td class="numeric"><% out.print(gestoSAT.getEmpleado(Integer.parseInt(auxMO.getKey().toString())).getPrecioHora()); %></td>
                                                                            <td class="numeric"><% out.print(horas); %></td>
                                                                            <td class="numeric"><% out.print(df.format(precioMO).toString().replace(",",".")); %></td>
                                                                        </tr>
                                                                    <% } %>
                                                                </tbody>
                                                            </table>
                                                        </section>
                                                    </div>
                                                    <% } %>
                                                </div>
                                            </div>
                                        </div>
                                    <% } %>
                                </div>
                        </div>
                        <div class="form-entradas">
                            <div class="cat-conf">
                                <h4>Precio definitivo</h4>
                                <p><span><% out.print(total); %></span><span>&euro;</span></p>
                            </div>
                        </div>
                        <div class="button-div">
                            <button type='button' class='btn btn-primary sep' id="exportarPDF">Exportar PDF</button>
                            <button type='button' class='btn btn-primary sep' id="exportarXLSX">Exportar XLSX</button>
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
<!-- Placed js at the end of the document so the pages load faster -->

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
<script src="js/iCheck/jquery.icheck.js"></script>
<script src="js/icheck-init.js"></script>
<script src="js/advanced-form.js"></script>

<!--common script init for all pages-->
<script src="js/scripts.js"></script>
<!-- My Scripts -->
<script>
    idRecibo = <%out.print(request.getParameter("id"));%>;
</script>
<script src="js/recibo.js"></script>
<script src="js/selector.js"></script>
</body>
</html>
