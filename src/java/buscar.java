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


import GestoSAT.Albaran;
import GestoSAT.Aparato;
import GestoSAT.Averia;
import GestoSAT.Cita;
import GestoSAT.Cliente;
import GestoSAT.Entrada;
import GestoSAT.Factura;
import GestoSAT.GestoSAT;
import GestoSAT.MaterialTrabajos;
import GestoSAT.Presupuesto;
import GestoSAT.Recibo;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author S
 */
public class buscar extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            HttpSession ses = request.getSession();
            GestoSAT gestor = (GestoSAT)ses.getAttribute("gestor");
            String buscar = request.getParameter("buscar");
            String jsonRespuesta ="{";
            
            DecimalFormat df = new DecimalFormat("0.00");
            
            boolean pendiente = Boolean.parseBoolean(request.getParameter("pendiente"));
            
            Map auxBusqueda;
            Iterator itBusquedas;
            if(Boolean.parseBoolean(request.getParameter("cliente"))){
                auxBusqueda= gestor.buscarCliente(buscar);
                itBusquedas = auxBusqueda.entrySet().iterator();
                Cliente cli;
                while(itBusquedas.hasNext()){
                    Map.Entry aux = (Map.Entry)itBusquedas.next();
                    cli=(Cliente)aux.getValue();
                    jsonRespuesta+="'"+aux.getKey()+"':{'nombre':'"+cli.getNombre()+" "+cli.getApellidos()+"',"
                            + "'NIF':'"+cli.getNif()+"','provincia':'"+cli.getProvincia()+"',"
                            + "'población':'"+cli.getPoblacion()+"','CP':'"+cli.getCp()+"',"
                            + "'calle':'"+cli.getCalle()+"','número':'"+cli.getNumero()+"',"
                            + "'escalera':'"+cli.getEscalera()+"','puerta':'"+cli.getPuerta()+"',"
                            + "'teléfonos':'"+cli.getTlfFijo()+" | "+cli.getTlfMovil()+"',"
                            + "'email':'"+cli.getEmail()+"'},";
                }
            }
            
            if(Boolean.parseBoolean(request.getParameter("entrada"))){
                auxBusqueda=gestor.buscarEntrada(buscar,pendiente);
                itBusquedas = auxBusqueda.entrySet().iterator();
                Cita cit;
                Aparato ap;
                while(itBusquedas.hasNext()){
                    Map.Entry aux = (Map.Entry)itBusquedas.next();
                    Entrada entrada=(Entrada)aux.getValue();
                    if(entrada.getIncidencia().getClass().getName().equals("GestoSAT.Cita")){
                        cit = (Cita)entrada.getIncidencia();
                        jsonRespuesta+="'"+aux.getKey()+"':{'creacion':'"+entrada.getFchCreacion()+"','nombre':'"+entrada.getCliente().getNombre()
                                +" "+entrada.getCliente().getApellidos()+"','provincia':'"+cit.getProvincia()+"',"
                                + "'población':'"+cit.getPoblacion()+"','calle':'"+cit.getCalle()+"','número':'"+cit.getNumero()+"',"
                                + "'escalera':'"+cit.getEscalera()+"','piso':'"+cit.getPiso()+"','puerta':'"+cit.getPuerta()+"',"
                                + "'motivo':'"+entrada.getIncidencia().getMotivo()+"'},";
                    }else{
                        ap = (Aparato)((Averia)entrada.getIncidencia()).getAparato();
                        jsonRespuesta+="'"+aux.getKey()+"':{'creacion':'"+entrada.getFchCreacion()+"','nombre':'"+entrada.getCliente().getNombre()+" "
                                + entrada.getCliente().getApellidos()+"','tipo':'"+ap.getTipo()+"','marca':'"+ap.getMarca()+"',"
                                + "'modelo':'"+ap.getModelo()+"','color':'"+ap.getColor()+"','número_serie':'"+ap.getNumSerie()+"',"
                                + "'motivo':'"+entrada.getIncidencia().getMotivo()+"'},";
                    }
                }
            }
            
            if(Boolean.parseBoolean(request.getParameter("presupuesto"))){
               auxBusqueda = gestor.buscarPresupuesto(buscar,pendiente);
               itBusquedas = auxBusqueda.entrySet().iterator();
               Presupuesto presupuesto;
               while(itBusquedas.hasNext()){
                   Map.Entry aux = (Map.Entry)itBusquedas.next();
                   presupuesto = (Presupuesto)aux.getValue();
                   jsonRespuesta+="'"+aux.getKey()+"':{'creacion':'"+presupuesto.getFchCreacion()+"','nombre':'"+presupuesto.getEntrada().getCliente().getNombre()+" "+presupuesto.getEntrada().getCliente().getApellidos()+"','concepto':'"+presupuesto.getConcepto()+"',"
                                + "'validez':'"+presupuesto.getValidez()+"','aceptado':'"+presupuesto.isAceptado()+"','total':'"+df.format(presupuesto.getTotal())+"'},";
               }
            }
            
            if(Boolean.parseBoolean(request.getParameter("albaran"))){
                auxBusqueda = gestor.buscarAlbaran(buscar,pendiente);
                itBusquedas = auxBusqueda.entrySet().iterator();
                Albaran albaran;
                while(itBusquedas.hasNext()){
                    Map.Entry aux = (Map.Entry)itBusquedas.next();
                    albaran = (Albaran)aux.getValue();
                    jsonRespuesta+="'"+aux.getKey()+"':{'creacion':'"+albaran.getFchCreacion()+"','nombre':'"+albaran.getCliente().getNombre()+" "
                            + albaran.getCliente().getApellidos()+"','concepto':'"+albaran.getConcepto()+"',"
                            + "'total':'"+df.format(albaran.getTotal())+"'";
                    
                    if(albaran.getPresupuesto()==null){
                        jsonRespuesta+=",'articulos':{";
                        Iterator itMat = albaran.getMaterialUtilizado().entrySet().iterator();
                        while(itMat.hasNext()){
                            Map.Entry auxMat = (Map.Entry)itMat.next();
                            jsonRespuesta+="'"+auxMat.getKey()+"':{'nombre':'"
                                    + ""+((MaterialTrabajos)auxMat.getValue()).getStock().getNombre()+"',"
                                    + "'cantidad':'"+((MaterialTrabajos)auxMat.getValue()).getCantidad()+"'},";
                        }
                        jsonRespuesta = jsonRespuesta.substring(0, jsonRespuesta.length()-1);
                        jsonRespuesta+="}";
                    }
                    
                    jsonRespuesta+= "},";
                }
            }
            
            if(Boolean.parseBoolean(request.getParameter("factura"))){
                auxBusqueda = gestor.buscarFactura(buscar,pendiente);
                itBusquedas = auxBusqueda.entrySet().iterator();
                Factura factura;
                while(itBusquedas.hasNext()){
                   Map.Entry aux = (Map.Entry)itBusquedas.next();
                   factura = (Factura)aux.getValue();
                   jsonRespuesta+="'"+aux.getKey()+"':{'creacion':'"+factura.getFchCreacion()+"','nombre':'"+factura.getCliente().getNombre()+" "
                                +factura.getCliente().getApellidos()+"','concepto':'"+factura.getConcepto()+"',"
                                + "'observaciones':'"+factura.getObservaciones()+"','total':'"+df.format(factura.getTotal())+"'},";
               }
            }
            
            if(Boolean.parseBoolean(request.getParameter("recibo"))){
                auxBusqueda = gestor.buscarRecibo(buscar);
                itBusquedas = auxBusqueda.entrySet().iterator();
                System.out.println(auxBusqueda);
                Recibo recibo;
                while(itBusquedas.hasNext()){
                    Map.Entry aux = (Map.Entry)itBusquedas.next();
                    recibo = (Recibo)aux.getValue();
                    jsonRespuesta+="'"+aux.getKey()+"':{'creacion':'"+recibo.getFchCreacion()+"','nombre':'"+recibo.getFactura().getCliente().getNombre()+" "
                            + recibo.getFactura().getCliente().getApellidos()+"','concepto':'"+recibo.getFactura().getConcepto()+"',"
                            + "'total':'"+df.format(recibo.getFactura().getTotal())+"'";
                    jsonRespuesta+= "},";
                }
            }
                
            
            if(jsonRespuesta.length()>1){
                jsonRespuesta = jsonRespuesta.substring(0,jsonRespuesta.length()-1);
                jsonRespuesta=jsonRespuesta.replace("'","\"");
            }
            out.print(jsonRespuesta+"}");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
