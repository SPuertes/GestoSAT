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
<%@page import="GestoSAT.GestoSAT"%>
<html lang="es">
   <% 
       HttpSession ses = request.getSession();
        if(ses.getAttribute("gestor") == null || ses.getAttribute("user") != null){
            response.sendRedirect("home.jsp");
        }
        
   %>
    <head>
    <meta charset="utf-8">

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="Apliación GestoSAt para la gestión de empresas SAT de electrónica">
    <meta name="author" content="Puertes Aleixandre, Salvador">
    <link rel="shortcut icon" href="images/favicon.png">

    <title>GestoSAT - Configuración inicial</title>

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

    <a href="#" class="logo">
        <img src="images/logo.png" alt="GestoSAT">
    </a>
</div>
<!--logo end-->

<div class="nav notify-row" id="top_menu">
    <!--  notification start -->
    
    <!--  notification end -->
</div>
<div class="top-nav clearfix">
</div>
</header>
<!--header end-->
<aside>
    <div id="sidebar" class="nav-collapse">
        <!-- sidebar menu start-->
        <div class="leftside-navigation">
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
                        <h3>Bienvenido a GestoSAT</h3>
                    </header>
                    <div class="panel-body">
                        A continuación pordras configurar tu acceso como gerente (usuario privilegiado) y los datos necesarios para que la aplicación funcione corréctamente.
                    </div>
                    <div class="categoria-conf">
                        <span class="title-cat-conf">Cuenta Gerente</span>
                        <div>    
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
                                    <span>Sueldo base:</span>
                                </div>
                                <input class="input-cat-conf decimal" type="number" id="base" step="0.01"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Precio hora:</span>
                                </div>
                                <input class="input-cat-conf decimal num" type="number" id="hora" step="0.01"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Correo electrónico:</span>
                                </div>
                                <input class="input-cat-conf" type="email" id="email"/>
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
                    <div class="categoria-conf">
                        <span class="title-cat-conf">Datos Empresa</span>
                        <div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Nombre:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="nombreEmp"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>NIF:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="nif"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Provincia:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="provEmp"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Población:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="pobEmp"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>CP:</span>
                                </div>
                                <input class="input-cat-conf" type="number" id="cpEmp"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Calle:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="calleEmp"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Número:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="numEmp"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Teléfono fijo:</span>
                                </div>
                                <input class="input-cat-conf" type="number" id="tlfFijoEmp"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Teléfono móvil:</span>
                                </div>
                                <input class="input-cat-conf" type="number" id="tlfMovilEmp"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Correo electrónico:</span>
                                </div>
                                <input class="input-cat-conf" type="email" id="emailEmp"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Fax:</span>
                                </div>
                                <input class="input-cat-conf" type="number" id="faxEmp"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>IVA General:</span>
                                </div>
                                <input class="input-cat-conf num" type="number" value="21" id="iva"/>
                            </div>
                        </div>
                    </div>
                    <div class="categoria-conf">
                        <span class="title-cat-conf">Conexión con MySQL</span>
                        <div>    
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Dirección:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="ipMySQL" value="localhost"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Puerto:</span>
                                </div>
                                <input class="input-cat-conf num" type="number" id="puertoMySQL" value="3306"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Usuario:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="userMySQL"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Contraseña:</span>
                                </div>
                                <input class="input-cat-conf" type="password" id="passMySQL"/>
                            </div>
                        </div>
                    </div>
                    <div class="categoria-conf">
                        <span class="title-cat-conf">Copias de seguridad (FTP)</span>
                        <div>    
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Dirección:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="ipFTP" value="localhost"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Puerto:</span>
                                </div>
                                <input class="input-cat-conf num" type="number" id="puertoFTP" value="21"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Usuario:</span>
                                </div>
                                <input class="input-cat-conf" type="text" id="userFTP" value="anonimo"/>
                            </div>
                            <div class="conf-attr">
                                <div class="name-cat-conf">
                                    <span>Contraseña:</span>
                                </div>
                                <input class="input-cat-conf" type="password" id="passFTP" value="anonimo"/>
                            </div>
                        </div>
                    </div>
                    <div class="btn-container">
                        <a class="btn btn-primary" id="submit-conf">Configurar</a>
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
</div>
</div>
<!--right sidebar end-->

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
                          <p>Los campos son erroneos. Asegúrate que el usuario y contraseña del MySQL es correcta (Es sensible a mayúsculas).</p>
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
<script src="js/initialConf.js"></script>

</body>
</html>
