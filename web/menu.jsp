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
<ul class="sidebar-menu" id="nav-accordion">
            <% if(((Boolean)ses.getAttribute("gerente"))){ %>
            <li class="sub-menu">
                <a href="javascript:;">
                    <i class="fa fa-users"></i>
                    <span>Administrar Usuarios</span>
                </a>
                <ul class="sub">
                    <li><a href="addUser.jsp"><i class="fa fa-plus"></i> Añadir Usuario</a></li>
                    <li><a href="listUsers.jsp"><i class="fa fa-eye"></i> Ver Usuarios</a></li>
                </ul>
            </li>
            <% } %>
            <li>
                <a href="proveedores.jsp">
                    <i class="fa fa-user"></i>
                    <span>Proveedores</span>
                </a>
            </li>
            <li>
                <a href="stock.jsp">
                    <i class="fa fa-puzzle-piece"></i>
                    <span>Material</span>
                </a>
            </li>
            <li class="sub-menu">
                <a href="javascript:;">
                    <i class="fa fa-file"></i>
                    <span>Documentos</span>
                </a>
                <ul class="sub">
                    <li><a href="crearEntrada.jsp">Nueva Entrada</a></li>
                    <li><a href="crearAlbaran.jsp">Nueva Venta</a></li>
                </ul>
            </li>
        <% if(!request.getRequestURL().substring(request.getRequestURL().lastIndexOf("/")+1).equals("buscador.jsp")){ %>
            <li >
                <form class="menu-search" onSubmit="return false;">
                    <input type="text" class="form-control search" id="inp-search" placeholder=" Buscar">
                    </br>
                    <button id="buscar" class="btn btn-primary my-search">Buscar</button>
                </form>
            </li>
        <% } %>
        </ul>
