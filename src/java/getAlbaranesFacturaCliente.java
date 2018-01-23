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
import GestoSAT.Factura;
import GestoSAT.GestoSAT;
import GestoSAT.MaterialTrabajos;
import GestoSAT.Trabajo;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author salvador
 */
public class getAlbaranesFacturaCliente extends HttpServlet {

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
            
            Factura factura = gestor.getFactura(Integer.parseInt(request.getParameter("id")));
            
            Map albaranes = new HashMap();
            albaranes.putAll(factura.getAlbaranes());
            albaranes.putAll(factura.getCliente().cargarAlbaranesPendientes(gestor.getIdCliente(factura.getCliente())));
            
            String vecAlbaranes = "{";
            
            Iterator itAlbaranes = albaranes.entrySet().iterator();
            Iterator itContenido;
            Albaran albaran;
            boolean depurar;
            while(itAlbaranes.hasNext()){
                Map.Entry aux = (Map.Entry)itAlbaranes.next();
                albaran = (Albaran)aux.getValue();
                vecAlbaranes+="'"+aux.getKey().toString().split(";")[0]+"':{"
                        + "'concepto':'"+albaran.getConcepto()+"','creacion':'"+albaran.getFchCreacion()+"',"
                        + "'observaciones':'"+albaran.getObservaciones()+"','total':'"+albaran.getTotal()+"','trabajo':{";
                itContenido = albaran.getTrabajoRealizado().entrySet().iterator();
                depurar =false;
                while(itContenido.hasNext()){
                    Map.Entry auxCont = (Map.Entry)itContenido.next();
                    Trabajo trabajo = (Trabajo)auxCont.getValue();
                    vecAlbaranes+="'"+auxCont.getKey()+"':{'nombre':'"+trabajo.getEmpleado().getNombre()+" "+trabajo.getEmpleado().getApellidos()+"',"
                            + "'precioH':'"+trabajo.getEmpleado().getPrecioHora()+"','cantidad':'"+trabajo.getHoras()+"'},";
                    depurar = true;
                }
                if(depurar)
                    vecAlbaranes = vecAlbaranes.substring(0, vecAlbaranes.length()-1);
                vecAlbaranes+="},'material':{";
                itContenido = albaran.getMaterialUtilizado().entrySet().iterator();
                depurar = false;
                while(itContenido.hasNext()){
                    Map.Entry auxCont = (Map.Entry)itContenido.next();
                    MaterialTrabajos material = (MaterialTrabajos)auxCont.getValue();
                    vecAlbaranes+="'"+auxCont.getKey()+"':{'nombre':'"+material.getStock().getNombre()+"',"
                            + "'precioU':'"+material.getStock().getPrecioUnidad()+"','cantidad':'"+material.getCantidad()+"'},";
                depurar = true;
                }
                if(depurar)
                    vecAlbaranes = vecAlbaranes.substring(0, vecAlbaranes.length()-1);
                
                vecAlbaranes+="}},";
            }
            vecAlbaranes = vecAlbaranes.substring(0, vecAlbaranes.length()-1);
            vecAlbaranes=vecAlbaranes.replace("'","\"");
            
            
            out.print(vecAlbaranes+"}");
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
