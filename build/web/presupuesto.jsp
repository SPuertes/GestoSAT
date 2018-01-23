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
<%@page import="java.text.DecimalFormat"%>
<%@page import="GestoSAT.MaterialTrabajos"%>
<%@page import="GestoSAT.Trabajo"%>
<%@page import="GestoSAT.Presupuesto"%>
<%@page import="GestoSAT.Aparato"%>
<%@page import="GestoSAT.Cliente"%>
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
    
    <title>Presupuesto - GestoSAT</title>

    <!--Core CSS -->
    <link href="bs3/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap-reset.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="js/bootstrap-datetimepicker/css/datetimepicker.css" />
    <link rel="stylesheet" type="text/css" href="js/jquery-multi-select/css/multi-select.css" />
    <link href="js/iCheck/skins/flat/green.css" rel="stylesheet">
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
                        <h3>Presupuesto</h3>
                    </header>
                    <div class="panel-body">
                        <div class='form-entradas'>
                            <h4>Datos Cliente</h4>
                            <%
                                Presupuesto presupuesto = ((GestoSAT)ses.getAttribute("gestor")).getPresupuesto(Integer.parseInt(request.getParameter("id"))); 
                                
                                if(presupuesto == null)
                                    request.getRequestDispatcher("home.jsp").forward(request, response);
                                
                                Entrada entrada = presupuesto.getEntrada();
                                Cliente cli = entrada.getCliente();
                                // Datos del cliente
                                out.print("<div class='conf-attr'><span class='attr-entries'>Nombre:</span> "+cli.getNombre()+"</div>"
                                        + "<div class='conf-attr'><span class='attr-entries'>Apellidos:</span> "+cli.getApellidos()+"</div>"
                                        + "<div class='conf-attr'><span class='attr-entries'>Dirección:</span> "+cli.getCalle()+" "+cli.getNumero()
                                        + " "+cli.getEscalera());
                                if(cli.getPiso() != 0)
                                    out.print(" "+cli.getPiso());
                                out.print(cli.getPuerta()+" "+cli.getPoblacion()+" "+cli.getProvincia()+"</div>"
                                        + "<div class='conf-attr'><span class='attr-entries'>Teléfono:</span> ");
                                        if(cli.getTlfFijo() != 0)
                                            out.print(" "+cli.getTlfFijo());
                                        if(cli.getTlfMovil() != 0)
                                        out.print(" "+cli.getTlfMovil());
                                out.print("</div>"
                                        + "<div class='conf-attr'><span class='attr-entries'>Correo electrónico:</span> "+cli.getEmail()+"</div>"
                                        + "</div>");
                                if(entrada.getIncidencia().getClass().getName().equals("GestoSAT.Averia")){
                                    out.print("<script type='text/javascript'>flag='averia'; id="+request.getParameter("id")+";idCliente="+((GestoSAT)ses.getAttribute("gestor")).getIdCliente(cli)+";</script>");
                                    Averia av = (Averia)entrada.getIncidencia();
                                    Aparato ap = av.getAparato();
                                    out.print("<div class='form-entradas'>"
                                            + "<h4>Aparato</h4>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Tipo:</span> "+ap.getTipo()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Marca:</span> "+ap.getMarca()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Modelo:</span> "+ap.getModelo()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Color:</span> "+ap.getColor()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Nº Serie:</span> "+ap.getNumSerie()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Lugar:</span> "+entrada.getLugar()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Observaciones aparato: </span> "+ap.getObservaciones()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Motivo:</span> "+av.getMotivo()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Observaciones entrada:</span> "+entrada.getObservaciones()+"</div>"
                                            + "</div>");
                                }else{
                                    out.print("<script type='text/javascript'>flag='cita';id="+request.getParameter("id")+";idCliente="+((GestoSAT)ses.getAttribute("gestor")).getIdCliente(cli)+";</script>");
                                    Cita cit = (Cita)entrada.getIncidencia();
                                    String mes="";
                                    switch(cit.getFchCita().getMonth()){
                                    case 0:
                                        mes = "Enero";
                                        break;
                                    case 1:
                                        mes = "Febrero";
                                        break;
                                    case 2:
                                        mes = "Marzo";
                                        break;
                                    case 3:
                                        mes = "Abril";
                                        break;
                                    case 4:
                                        mes = "Mayo";
                                        break;
                                    case 5:
                                        mes = "Junio";
                                        break;
                                    case 6:
                                        mes = "Julio";
                                        break;
                                    case 7:
                                        mes = "Agosto";
                                        break;
                                    case 8:
                                        mes = "Septiembre";
                                        break;
                                    case 9:
                                        mes = "Octubre";
                                        break;
                                    case 10:
                                        mes = "Noviembre";
                                        break;
                                    case 11:
                                        mes = "Diciembre";
                                        break;
                                    }
                                    String fecha = cit.getFchCita().getDate()+" "+mes+" "+(cit.getFchCita().getYear()+1900)+" - "+cit.getFchCita().getHours()+":"+cit.getFchCita().getMinutes();

                                    out.print("<div class='form-entradas'>"
                                            + "<h4>Lugar cita</h4>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Fecha cita:</span> "+fecha+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Provincia:</span> "+cit.getProvincia()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Población:</span> "+cit.getPoblacion()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Calle:</span> "+cit.getCalle()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Número:</span> "+cit.getNumero()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Escalera:</span> "+cit.getEscalera()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Piso:</span> ");
                                    if(cit.getPiso()!= 0)
                                        out.print(" "+cit.getPiso());
                                    out.print("</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Puerta:</span> "+cit.getPuerta()+"</label></div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Observaciones lugar:</span> "+cit.getObservacionesDirrecion()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Observaciones cita:</span> "+cit.getObservaciones()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Motivo:</span> "+cit.getMotivo()+"</div>"
                                            + "<div class='conf-attr'><span class='attr-entries'>Observaciones entrada:</span>"+entrada.getObservaciones()+"</div>"
                                            + "</div>");
                                }
                            %>
                        <div class="form-entradas">
                                <h4>Características presupuesto</h4>
                                <div class='conf-attr'><span class='attr-entries'>Concepto:</span> <label class="my-label"><% out.print(presupuesto.getConcepto()); %></label></div>
                                <div class='conf-attr'><span class='attr-entries'>Fecha de validez:</span>
                                    <%
                                    String mesPresupuesto="";
                                    switch(presupuesto.getValidez().getMonth()){
                                        case 0:
                                            mesPresupuesto = "Enero";
                                            break;
                                        case 1:
                                            mesPresupuesto = "Febrero";
                                            break;
                                        case 2:
                                            mesPresupuesto = "Marzo";
                                            break;
                                        case 3:
                                            mesPresupuesto = "Abril";
                                            break;
                                        case 4:
                                            mesPresupuesto = "Mayo";
                                            break;
                                        case 5:
                                            mesPresupuesto = "Junio";
                                            break;
                                        case 6:
                                            mesPresupuesto = "Julio";
                                            break;
                                        case 7:
                                            mesPresupuesto = "Agosto";
                                            break;
                                        case 8:
                                            mesPresupuesto = "Septiembre";
                                            break;
                                        case 9:
                                            mesPresupuesto = "Octubre";
                                            break;
                                        case 10:
                                            mesPresupuesto = "Noviembre";
                                            break;
                                        case 11:
                                            mesPresupuesto = "Diciembre";
                                            break;
                                    }
                                    String fechaPresupuesto = presupuesto.getValidez().getDate()+" "+mesPresupuesto+" "+(presupuesto.getValidez().getYear()+1900);
                                    %>
                                    <div data-date="" class="input-group date form_datetime-budget">   
                                        <input class='input-cat-conf' type="text" id="fecha" readonly="" disabled value="<% out.print(fechaPresupuesto);%>" size="25"/>
                                        <div class='my-input-group-btn'>
                                            <button type="button" class="btn btn-warning date-set"><i class="fa fa-calendar"></i></button>
                                        </div>
                                    </div>
                                </div>
                                <div class='conf-attr'><span class='attr-entries'>Aceptado:</span>
                                    <div class="flat-green">
                                        <input class="input-cat-conf" type="checkbox" <% if(presupuesto.isAceptado()){ out.print("checked"); } %> disabled id="aceptado">
                                    </div>
                                </div>
                                <div class='conf-attr'><span class='attr-entries'>Adelanto:</span> <label class="my-label"><% out.print(presupuesto.getAdelanto()); %></label></div>
                                <div class='conf-attr'><span class='attr-entries'>Forma de pago:</span> <label class="my-label"><% out.print(presupuesto.getFormaPago()); %></label></div>
                                <div class='conf-attr'><span class='attr-entries'>Tiempo estimado [Días]:</span> <label class="my-label"><% out.print(presupuesto.getPlazo()); %></label></div>
                                <div class='conf-attr'><span class='attr-entries'>Condiciones:</span> <label class="my-label"><% out.print(presupuesto.getCondiciones()); %></label></div>
                                <div class='conf-attr'><span class='attr-entries'>Seguro:</span> <label class="my-label"><% out.print(presupuesto.getSeguro()); %></label></div>
                                <div class='conf-attr'><span class='attr-entries'>Garantías:</span> <label class="my-label"><% out.print(presupuesto.getGarantia()); %></label></div>
                                <div class='conf-attr'><span class='attr-entries'>Observaciones:</span> <label class="my-label"><% out.print(presupuesto.getObservaciones()); %></label></div>
                        </div>

                                <%
                                    GestoSAT gesto = (GestoSAT)ses.getAttribute("gestor");
                                    Map stock = gesto.getStock();
                                    Map emps = gesto.getEmpleados();
                                    Iterator itEmpleado = emps.entrySet().iterator();
                                    Iterator itStock = stock.entrySet().iterator();
                                %>
                        <div class="form-entradas">
                            <div class ="cat-conf">
                                <h4>Materiales a utilizar</h4>
                                <div>
                                    <div class="select-list">
                                        <select name="stocklist" class="multi-select" multiple='' id="select_stock" >
                                            <%
                                                String jsonStock ="{";
                                                while(itStock.hasNext()){
                                                    Map.Entry aux = (Map.Entry)itStock.next();
                                                    out.print("<option value='"+aux.getKey()+"'>"
                                                        +((Stock)aux.getValue()).getNombre()+ "</option>"); 
                                                    jsonStock = jsonStock.concat(aux.getKey()+":["+((Stock)aux.getValue()).getPrecioUnidad()+"],");
                                                }
                                                if(jsonStock.length()!=1)
                                                    jsonStock = jsonStock.substring(0, jsonStock.length()-1);
                                                jsonStock = jsonStock.concat("}");
                                            %>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="cat-conf">
                                <h4>Trabajadores</h4>
                                <div>
                                    <div class="select-list">
                                        <select name="workerslist" class="multi-select" multiple='' id="select_empleados" >
                                            <%
                                                String jsonEmpleados = "{";
                                                while(itEmpleado.hasNext()){
                                                    Map.Entry aux = (Map.Entry)itEmpleado.next();
                                                    if(((Empleado)aux.getValue()).isActivo()){
                                                        out.print("<option value='"+aux.getKey()+"'>"
                                                            +((Empleado)aux.getValue()).getNombre()+" "+((Empleado)aux.getValue()).getApellidos()
                                                            + "</option>"); 
                                                        jsonEmpleados=jsonEmpleados.concat(aux.getKey()+":'"+((Empleado)aux.getValue()).getPrecioHora()+"',");
                                                    }
                                                }
                                                if(jsonEmpleados.length()!=1)
                                                    jsonEmpleados = jsonEmpleados.substring(0, jsonEmpleados.length()-1);
                                                jsonEmpleados = jsonEmpleados.concat("}");
                                            %>
                                        </select>
                                    </div>
                                </div>
                            </div>
                             
                            <div class="cat-conf">
                                <h4>Precio definitivo</h4>
                                <p><span id="precioPresupuesto"><% DecimalFormat df = new DecimalFormat("0.00");
                                                out.print(df.format(presupuesto.getTotal()).replace(",",".")); %></span><span>&euro;</span></p>
                            </div>
                        </div>
                                        
                        <div class="button-div">
                            <%
                                    Map mapDocs = presupuesto.cargarAlbaran(Integer.parseInt(request.getParameter("id")));
                                    Iterator itDocs = mapDocs.entrySet().iterator();
                                    Map.Entry auxDocs;
                                    boolean delete = false;
                                    
                                    switch(mapDocs.size()){
                                        default:
                                            out.print("<button type='button' id='crearAlbaran' class='btn btn-primary sep'>Crear Albarán</button>");
                                            out.print("<button type='button' id='crearFactura' class='btn btn-primary sep'>Crear Factura</button>");
                                            delete = true;
                                            break;
                                        case 1:
                                            auxDocs = (Map.Entry)itDocs.next();
                                            out.print("<script type='text/javascript'>idAlbaran="+auxDocs.getKey().toString().split(";")[0]+";</script>");
                                            out.print("<button type='button' id='editarAlbaran' class='btn btn-primary sep'>Ver Albarán</button>");
                                            out.print("<button type='button' id='crearFactura' class='btn btn-primary sep'>Crear Factura</button>");
                                            break;
                                        case 2:
                                            auxDocs = (Map.Entry)itDocs.next();
                                            out.print("<script type='text/javascript'>id"+auxDocs.getKey().toString().split(";")[1]+"="+auxDocs.getKey().toString().split(";")[0]+";");
                                            auxDocs = (Map.Entry)itDocs.next();
                                            out.print("id"+auxDocs.getKey().toString().split(";")[1]+"="+auxDocs.getKey().toString().split(";")[0]+";</script>");
                                            out.print("<script type='text/javascript'>idAlbaran="+auxDocs.getKey().toString().split(";")[0]+";idFactrua="+auxDocs.getKey().toString().split(";")[0]+";</script>");
                                            out.print("<button type='button' id='editarAlbaran' class='btn btn-primary sep'>Ver Albarán</button>");
                                            out.print("<button type='button' id='editarFactura' class='btn btn-primary sep'>Ver Factura</button>");
                                            break;
                                    }
                            %>
                        </div>
                                        
                        <div class="button-div">
                            
                            <button type='button' class='btn btn-primary sep' data-mode="editar" id="edit">Editar</button>
                            <%
                               if(delete)
                                    out.print("<button type='button' class='btn btn-primary sep' id='delete'>Eliminar</button>");
                            %>
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

<!-- Modal -->

<div aria-hidden="true" aria-labelledby="empty" role="dialog" tabindex="-1" id="emptyValues" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <p>Debes rellenar todos los campos</p>
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

<div aria-hidden="true" aria-labelledby="errorDelete" role="dialog" tabindex="-1" id="errorDelete" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
            </div>
            <div class="modal-body">
                <p>Se ha producido un error, este presupuesto no se puede borrar en este momento, intentelo más tarde</p>
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
<script type="text/javascript" src="js/bootstrap-datepicker/js/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="js/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="js/bootstrap-timepicker/js/bootstrap-timepicker.js"></script>

<script type="text/javascript" src="js/jquery-multi-select/js/jquery.multi-select.js"></script>
<script type="text/javascript" src="js/jquery-multi-select/js/jquery.quicksearch.js"></script>
<script src="js/iCheck/jquery.icheck.js"></script>
<script src="js/icheck-init.js"></script>
<script src="js/advanced-form.js"></script>

<!--common script init for all pages-->
<script src="js/scripts.js"></script>

<!-- My Scripts -->
<script type='text/javascript'>
    vecEmpleados = <% out.print(jsonEmpleados); %>;
    vecStock = <% out.print(jsonStock); %>;
    iva = <% out.print(gesto.getIva()); %>/100;
</script>)
<script type="text/javascript">
    <% Iterator itTrabajo = presupuesto.getTrabajo().entrySet().iterator();
    Iterator itMaterial = presupuesto.getMaterial().entrySet().iterator(); %>
    trabajadoresAsignados = {
        <%  
            String trabajos = "";
            int horas, minutos;
            
            while(itTrabajo.hasNext()){
                Map.Entry auxTrabajo = (Map.Entry)itTrabajo.next();
                Trabajo trabajo = (Trabajo)auxTrabajo.getValue();
                horas = (int)Math.floor(trabajo.getHoras());
                minutos = (int)((trabajo.getHoras()%1)*60);
                trabajos+="'"+auxTrabajo.getKey()+"':{'h':'"+horas+"','m':'"+minutos+"','desc':'"+trabajo.getDescripcion()+"'},";
        } 
        if(!trabajos.isEmpty())
            out.print(trabajos.substring(0, trabajos.length()-1));
        %>
    };
    
    materialesAsignados = {
        <%  
            String materiales = "";
            while(itMaterial.hasNext()){
                Map.Entry auxMaterial = (Map.Entry)itMaterial.next();
                materiales+="'"+auxMaterial.getKey()+"':'"+((MaterialTrabajos)auxMaterial.getValue()).getCantidad()+"',";
        } 
        if(!materiales.isEmpty())
            out.print(materiales.substring(0, materiales.length()-1));
        %>
    }; 
</script>
<script src="js/editPresupuesto.js"></script>
<script src="js/selector.js"></script>
</body>
</html>
