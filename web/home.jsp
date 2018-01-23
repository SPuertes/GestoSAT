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
<%@page import="java.util.Vector"%>
<%@page import="GestoSAT.GestoSAT"%>
<%@page import="GestoSAT.Empleado"%>
<%@page import="GestoSAT.Gerente"%>
<%@page import="GestoSAT.Entrada"%>
<%@page import="GestoSAT.Averia"%>
<%@page import="GestoSAT.Cita"%>
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
    
    <title>Inicio - GestoSAT</title>

    <!--Core CSS -->
    <link href="bs3/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-reset.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet" />

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
                        Entradas pendientes
                    </header>
                    <div class="panel-body">
                        <%
                        Map allEntradas = ((GestoSAT)ses.getAttribute("gestor")).getEntradasPendientes();
                        
                        Iterator itEntradas = allEntradas.entrySet().iterator();
                        int size = allEntradas.size();
                        Vector<Map.Entry> indices = new Vector<Map.Entry>(size); 
                        
                        while(itEntradas.hasNext()){
                            indices.add((Map.Entry)itEntradas.next());
                        }
                        
                        for(int i = indices.size()-1; i>=0;i--){
                            Map.Entry aux_it = indices.get(i);
                            String idEntrada = aux_it.getKey().toString().split(";")[0];
                            Entrada e_aux = (Entrada)aux_it.getValue();   
                            Object auxiliar = e_aux.getIncidencia();
                            if(auxiliar.getClass().getName().equals("GestoSAT.Averia")){
                                    Averia aux_av = (Averia)auxiliar;
                                    out.print("<div class='col-md-4  col-sm-12 col-xs-12' data-idEntrada='"+idEntrada+"'>"
                                         + "<div class='entradasPortada'>"
                                         + "<div><span class='attr-entries'>Cliente:</span> "+e_aux.getCliente().getNombre()+" "+e_aux.getCliente().getApellidos()+"</div>"
                                         + "<div><span class='attr-entries'>Aparato:</span> "+aux_av.getAparato().getTipo()+" "
                                         +aux_av.getAparato().getModelo()+" "
                                         +aux_av.getAparato().getMarca()+"</div>"
                                         + "<div><span class='attr-entries'>Motivo: </span>"+e_aux.getIncidencia().getMotivo()+"</div>"
                                         + "</div></div>");
                                }else{
                                    Cita aux_cit = (Cita)auxiliar;
                                    Date aux_date = aux_cit.getFchCita();
                                    out.print("<div class='col-md-4  col-sm-12 col-xs-12' data-idEntrada='"+idEntrada+"'>"
                                        + "<div class='entradasPortada'>"
                                        + "<div><span class='attr-entries'>Cliente: </span>"+e_aux.getCliente().getNombre()+" "+e_aux.getCliente().getApellidos()+"</div>"
                                        + "<div><span class='attr-entries'>Dirección: </span>"+aux_cit.getPoblacion()+" "
                                        +aux_cit.getCalle()+" "+aux_cit.getNumero()+" "+aux_cit.getEscalera()+" ");
                                               
                                    if(aux_cit.getPiso() != 0)
                                        out.print(aux_cit.getPiso()+" ");
                                    
                                    String minutos;
                                    if(aux_date.getMinutes()<10)
                                        minutos = "0"+aux_date.getMinutes();
                                    else
                                        minutos = aux_date.getMinutes()+"";
                                    out.print(aux_cit.getPuerta()+"</div>"
                                        + "<div><span class='attr-entries'>Motivo: </span>"+aux_cit.getMotivo()+"</div>"
                                        + "<div><span class='attr-entries'>Fecha: </span>"+aux_date.getHours()+":"+minutos
                                        + " "+aux_date.getDate()+"/"+(aux_date.getMonth()+1)+"/"+(aux_date.getYear()+1900)+"</div>"
                                        + "</div></div>");
                                    
                                }
                            }%>
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
<div aria-hidden="true" aria-labelledby="empty" role="dialog" tabindex="-1" id="noAlbaranes" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <p>No se puede crear la factura, el cliente no tiene albaranes.</p>
                <p>Crea los albaranes y vuelve a realizar la acción.</p>
            </div>
            <div class="modal-footer">
                <button data-dismiss="modal" class="btn btn-default" type="button">Cerrar</button>
            </div>
        </div>
      </div>
</div>
<!-- Placed js at the end of the document so the pages load faster -->

<!--Core js-->
<script src="js/jquery.js"></script>
<script src="bs3/js/bootstrap.min.js"></script>
<script class="include" type="text/javascript" src="js/jquery.dcjqaccordion.2.7.js"></script>
<script src="js/jquery.scrollTo.min.js"></script>
<script src="js/jQuery-slimScroll-1.3.0/jquery.slimscroll.js"></script>
<script src="js/jquery.nicescroll.js"></script>

<!--common script init for all pages-->
<script src="js/scripts.js"></script>

<!-- My JS -->
<script src="js/selector.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        if(<%out.print(request.getParameter("factura"));%>)
            $("#noAlbaranes").modal("show");
    });
</script>
</body>
</html>