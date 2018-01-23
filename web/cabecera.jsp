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
<%@page import="java.util.HashMap"%>
<%@page import="GestoSAT.GestoSAT"%>
<%@page import="GestoSAT.Empleado"%>
<%@page import="GestoSAT.Gerente"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Iterator"%>
<%@page session='true'%>
<%
        HttpSession ses = request.getSession();
        
        if(ses.getAttribute("gestor")!= null){
            GestoSAT gesto = (GestoSAT)ses.getAttribute("gestor");
            if(gesto.primerAcceso()){
                response.sendRedirect("primerAcceso.jsp");
            }else{
                if(ses.getAttribute("usuario")== null){
                   ses.setAttribute("usuario",gesto.getEmpleado());
                   if(gesto.getEmpleado().getClass().getName().equals("GestoSAT.Gerente"))
                       ses.setAttribute("gerente",true);
                   else
                       ses.setAttribute("gerente",false);
                }
            }
            Map rightAppointment = gesto.getCitasPendientes();
            Map right = gesto.getStock();
            ses.setAttribute("stockAllert", right);
            ses.setAttribute("appointment", rightAppointment);
        }else
            request.getRequestDispatcher("index.jsp").forward(request, response);
        
        
%>