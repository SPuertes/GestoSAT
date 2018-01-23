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
<%@page import="GestoSAT.Stock"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="GestoSAT.GestoSAT"%>
<%@page import="GestoSAT.Entrada"%>
<%@page import="GestoSAT.Cita"%>

<ul class="right-side-accordion">
<li class="widget-collapsible">
<a href="#" class="head widget-head red-bg active clearfix">
        <span class="pull-left">Próximas citas</span>
        <span class="pull-right widget-collapse"><i class="ico-minus"></i></span>
    </a>
    <ul class="widget-container">
        <!-- Proximes cites -->
        <li class="recordatorio appointments">
            
            <%  if(ses.getAttribute("appointment")!= null){
                        Iterator itCitas = ((Map)ses.getAttribute("appointment")).entrySet().iterator();
                        while(itCitas.hasNext()){
                               Map.Entry aux_it = ((Map.Entry)itCitas.next());
                               String idCita = aux_it.getKey().toString().split(";")[0];
                               Entrada cita_aux = (Entrada)aux_it.getValue();
                               Cita aux = ((Cita)cita_aux.getIncidencia());
                               Date aux_date = ((Cita)cita_aux.getIncidencia()).getFchCita();
                               
                               out.print("<div class='col-md-11 cita-div' data-idCita='"+idCita+"'><div class='rightCita'>"
                                       + "<div><span class='attr-entries'>Cliente: </span>"+cita_aux.getCliente().getNombre()+" "+cita_aux.getCliente().getApellidos()+"</div>"
                                       + "<div><span class='attr-entries'>Direccion: </span>"+aux.getPoblacion()+" "
                                       +aux.getCalle()+" "+aux.getNumero()+" "+aux.getEscalera()+" ");
                               String minutos;
                               if(aux_date.getMinutes()<10)
                                    minutos = "0"+aux_date.getMinutes();
                                else
                                    minutos = aux_date.getMinutes()+"";
                               if(((Cita)cita_aux.getIncidencia()).getPiso() != 0)
                                    out.print(aux.getPiso()+" ");
                                       out.print(aux.getPuerta()+"</div>"
                                       + "<div><span class='attr-entries'>Motivo: </span><span>"+aux.getMotivo()+"</span></div>"
                                       + "<div><span class='attr-entries'>Fecha: </span><span>"+aux_date.getHours()+":"+minutos
                                       + " "+aux_date.getDate()+"/"+(aux_date.getMonth()+1)+"/"+(aux_date.getYear()+1900)+"</span></div>"
                                       + "</div></div>");
                        }
                }%>                
        </li>
    </ul>
</li>
<li class="widget-collapsible">
<a href="#" class="head widget-head purple-bg active clearfix">
        <span class="pull-left">Falta Material</span>
        <span class="pull-right widget-collapse"><i class="ico-minus"></i></span>
    </a>
    <ul class="widget-container">
        <li class="recordatorio">
            <%Iterator it = ((Map)ses.getAttribute("stockAllert")).entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry r_aux = (Map.Entry)it.next();
                    if(((Stock)r_aux.getValue()).lanzarRecordatorio())
                        out.print("<p>"+((Stock)r_aux.getValue()).getNombre()+" | "+((Stock)r_aux.getValue()).getProveedor().getNombre()+"</p>");
                }%>                
        </li>
    </ul>
</li>
</ul>