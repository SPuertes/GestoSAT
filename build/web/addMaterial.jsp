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
<%@page import="GestoSAT.Proveedor"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<!DOCTYPE html>
<%@page import="GestoSAT.GestoSAT"%>
<html lang="es">
   <%@include file="cabecera.jsp" %>
    <head>
    <meta charset="utf-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Apliación GestoSAt para la gestión de empresas SAT de electrónica">
    <meta name="author" content="Puertes Aleixandre, Salvador">
    <link rel="shortcut icon" href="images/favicon.png">

    <title>Añadir Material - GestoSAT</title>
    <!--Core CSS -->
    <link href="bs3/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-reset.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet" />

    <!--responsive table-->
    <link href="css/table-responsive.css" rel="stylesheet" />
    
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
    <!--  notification start -->
    
    <!--  notification end -->
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
                        <h3>Añadir Material</h3>
                    </header>
                    <div class="categoria-conf">
                        <span class="title-cat-conf">Cuenta Proveedor</span>
                        <div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Proveedor:</span>
                                </div>
                                <select id="proveedor">
                                    <!--<option value="1">Ninguno</option>-->
                                        <% Map prov = ((GestoSAT)ses.getAttribute("gestor")).getProveedores();  // Extruare els proveedors (uno que siga huit per a YouSelf)
                                            Iterator it = prov.entrySet().iterator();
                                                while(it.hasNext()){
                                                   Map.Entry aux = (Map.Entry)it.next(); 
                                                   out.print("<option value='"+aux.getKey()+"'>"+((Proveedor)aux.getValue()).getNombre()+"</option>");
                                                }
                                        %>
                                </select>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Nombre:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="nombre"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Descripción:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="descripcion"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Precio compra:</span>
                                </div>
                                <input class="input-cat-conf decimal" type="number" step="0.01" id="compra"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Unidades:</span>
                                </div>
                                <input class="input-cat-conf decimal" type="number" step="0.01" id="unidades"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Precio venta (Bruto):</span>
                                </div>
                                <input class="input-cat-conf decimal" type="number" step="0.01" id="venta"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Cantidad de alerta:</span>
                                </div>
                                <input class="input-cat-conf decimal" type="number" step="0.01" id="alerta"/>
                            </div>
                        </div>
                        
                    </div>
                    <div class="btn-container">
                        <a class="btn btn-primary" id="submit-add">Añadir</a>
                    </div>
                </section>
            </div>
        </div>
        <!-- page end-->
        </section>
    </section>
    <!--main content end-->

</section>

    <!-- Modal -->
          <div aria-hidden="true" aria-labelledby="difPass" role="dialog" tabindex="-1" id="difPass" class="modal fade">
              <div class="modal-dialog">
                  <div class="modal-content">
                      <div class="modal-header">
                          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                      </div>
                      <div class="modal-body">
                          <p>Las contraseñas no coinciden</p>
                      </div>
                      <div class="modal-footer">
                          <button data-dismiss="modal" class="btn btn-default" type="button">Cerrar</button>
                      </div>
                  </div>
              </div>
          </div>
        <div aria-hidden="true" aria-labelledby="camposImportantesVacios" role="dialog" tabindex="-1" id="blancs" class="modal fade">
              <div class="modal-dialog">
                  <div class="modal-content">
                      <div class="modal-header">
                          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                      </div>
                      <div class="modal-body">
                          <p>Debes rellenar correctamente los campos marcados en rojo </p>
                      </div>
                      <div class="modal-footer">
                          <button data-dismiss="modal" class="btn btn-default" type="button">Cerrar</button>
                      </div>
                  </div>
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
          <!-- modal -->
    
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
<script src="js/addMaterial.js"></script>

<!-- My JS -->
<script src="js/selector.js"></script>

</body>
</html>
