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
<!DOCTYPE html>
<%@page import="GestoSAT.Oficina"%>
<%@page import="GestoSAT.GestoSAT"%>
<%@page import="GestoSAT.Empleado"%>
<%@page import="GestoSAT.Gerente"%>
<%@page session='true'%>

<html lang="es">
<%@include file="cabecera.jsp" %>
    
    <head>
    <meta charset="utf-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Apliación GestoSAt para la gestión de empresas SAT de electrónica">
    <meta name="author" content="Puertes Aleixandre, Salvador">
    <link rel="shortcut icon" href="images/favicon.png">
    
    <title>Perfil usuario - GestoSAT</title>

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
                <li><a href="confApp.jsp"><i class="fa fa-cog"></i> Configurar aplicacion</a></li>
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
                        <h3>Perfil personal</h3>
                    </header>
                    <div class="panel-body">
                        Para que los cambios san permanente debes guardar las modificaciones.
                    </div>
                    
                    <%
                        Empleado usuario;
                        if((Boolean)ses.getAttribute("gerente"))
                            usuario = (Gerente)ses.getAttribute("usuario");
                        else
                            usuario = (Empleado)ses.getAttribute("usuario");
                    %>
                    
                    <div class="categoria-conf">
                        <span class="title-cat-conf">Datos personales</span>
                        <div>    
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Nombre:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="nombre" value="<% if(!usuario.getNombre().equals("0")){ out.print(usuario.getNombre()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Apellidos:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="apellidos" value="<% if(!usuario.getApellidos().equals("0")){ out.print(usuario.getApellidos()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>DNI:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="dni" value="<% if(!usuario.getDni().equals("0")){ out.print(usuario.getDni()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Provincia:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="prov" value="<% if(!usuario.getProvincia().equals("0")){ out.print(usuario.getProvincia()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Población:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="pob" value="<% if(!usuario.getPoblacion().equals("0")){ out.print(usuario.getPoblacion()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>CP:</span>
                                </div>
                                <input class="input-cat-conf" type="number" id="cp" <% if(usuario.getCp() != 0){ out.print("value='"+usuario.getCp()+"'"); } %>/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Calle:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="calle" value="<% if(!usuario.getCalle().equals("0")){ out.print(usuario.getCalle()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Número:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="num" value="<% if(!usuario.getNumero().equals("0")){ out.print(usuario.getNumero()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div  class="name-cat-conf">
                                    <span>Escalera:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="escalera" value="<% if(!usuario.getEscalera().equals("0")){ out.print(usuario.getEscalera()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Piso:</span>
                                </div>
                                <input class="input-cat-conf num" type="number" id="piso" <% if(usuario.getPiso() != 0){ out.print("value='"+usuario.getPiso()+"'"); } %>/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Puerta:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="puerta" value="<% if(!usuario.getPuerta().equals("0")){ out.print(usuario.getPuerta()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Teléfono casa:</span>
                                </div>
                                <input class="input-cat-conf" type="number" id="tlfFijo" <% if(usuario.getTlfFijo()!= 0){ out.print("value='"+usuario.getTlfFijo()+"'"); } %>/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Teléfono móvil:</span>
                                </div>
                                <input class="input-cat-conf" type="number" id="tlfMovil" <% if(usuario.getTlfMovil()!= 0){ out.print("value='"+usuario.getTlfMovil()+"'"); } %>/>
                            </div>
                            <% if((Boolean)ses.getAttribute("gerente")){ %>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Sueldo base:</span>
                                </div>
                                <input class="input-cat-conf decimal" type="number" id="base" step="0.01" <% if(usuario.getSueldoBase()!= 0){ out.print("value='"+usuario.getSueldoBase()+"'"); } %>/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Precio hora:</span>
                                </div>
                                <input class="input-cat-conf decimal num" type="number" id="hora" step="0.01" <% if(usuario.getPrecioHora()!= 0){ out.print("value='"+usuario.getPrecioHora()+"'"); } %>/>
                            </div>
                            <% } %>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Correo electrónico:</span>
                                </div>
                                <input class="input-cat-conf" type="email" id="email" value="<% if(!usuario.getEmail().equals("0")){ out.print(usuario.getEmail()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Contrsaeña:</span>
                                </div>
                                <input class="input-cat-conf" type="password" id="pass1"/>
                            </div>
                           <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Repite la contrsaeña:</span>
                                </div>
                                <input class="input-cat-conf" type="password" id="pass2"/>
                            </div>
                        </div>
                    </div>
                    <% if((Boolean)ses.getAttribute("gerente")){ 
                        Oficina o = usuario.getOficina();
                    %>
                    <div class="categoria-conf">
                        <span class="title-cat-conf">Datos Empresa</span>
                        <div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Nombre:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="nombreEmp" value="<% out.print(o.getNombre()); %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>NIF:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="nif" value="<% if(!o.getNif().equals("0")){ out.print(o.getNif()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Provincia:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="provEmp" value="<% if(!o.getProvincia().equals("0")){ out.print(o.getProvincia()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Población:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="pobEmp" value="<% if(!o.getPoblacion().equals("0")){ out.print(o.getPoblacion()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>CP:</span>
                                </div>
                                <input class="input-cat-conf" type="number" id="cpEmp" <% if(o.getCp() != 0){ out.print("value='"+o.getCp()+"'"); } %>/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Calle:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="calleEmp" value="<% if(!o.getCalle().equals("0")){ out.print(o.getCalle()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Número:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="numEmp" value="<%if(!o.getNumero().equals("0")){ out.print(o.getNumero()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Teléfono fijo:</span>
                                </div>
                                <input class="input-cat-conf" type="number" id="tlfFijoEmp" <% if(o.getTlfFijo()!= 0){ out.print("value='"+o.getTlfFijo()+"'"); } %>/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Teléfono móvil:</span>
                                </div>
                                <input class="input-cat-conf" type="number" id="tlfMovilEmp" <% if(o.getTlfMovil()!= 0){ out.print("value='"+o.getTlfMovil()+"'"); } %>/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Correo electrónico:</span>
                                </div>
                                <input class="input-cat-conf" type="email" id="emailEmp" value="<% if(!o.getEmail().equals("0")){ out.print(o.getEmail()); } %>"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Fax:</span>
                                </div>
                                <input class="input-cat-conf" type="number" id="faxEmp" <% if(o.getFax() != 0){ out.print("value='"+o.getFax()+"'"); } %>/>
                            </div>
                        </div>
                        <% } %>
                    </div>
                    <div class="btn-container">
                        <a class="btn btn-primary" id="save-change">Guardar cambios</a>
                    </div>
                </section>
            </div>
        </div>
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

        <div aria-hidden="true" aria-labelledby="camposImportantesVacios" role="dialog" tabindex="-1" id="correcte" class="modal fade">
              <div class="modal-dialog">
                  <div class="modal-content">
                      <div class="modal-header">
                          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                      </div>
                      <div class="modal-body">
                          <p>Cambios efectuados con éxito </p>
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
                          <p>No se han podido efectuar los cambios, asegurate que los datos insertados son correctos </p>
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
<script src="js/profileChange.js"></script>

<!-- My JS -->
<script src="js/selector.js"></script>
</body>
</html>
