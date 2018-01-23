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
import GestoSAT.Cliente;
import GestoSAT.GestoSAT;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author salvador
 */
public class crearVenta extends HttpServlet {

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
            Cliente cli;
            
            int idCliente = Integer.parseInt(request.getParameter("id"));
            
            Gson gJson = new Gson();
            Map material = (HashMap)gJson.fromJson(request.getParameter("material"), HashMap.class); // Parse JSON
            
            if(idCliente>0){
                cli = gestor.getCliente(idCliente);
            }else{
                cli = new Cliente(request.getParameter("cliente[nombre]"),
                        request.getParameter("cliente[apellidos]"), request.getParameter("cliente[NIF]"),
                        request.getParameter("cliente[provincia]"), request.getParameter("cliente[poblacion]"),
                        Integer.parseInt(request.getParameter("cliente[cp]")), request.getParameter("cliente[calle]"),
                        request.getParameter("cliente[numero]"), request.getParameter("cliente[escalera]"),
                        Integer.parseInt(request.getParameter("cliente[piso]")), request.getParameter("cliente[puerta]"),
                        Integer.parseInt(request.getParameter("cliente[tlfFijo]")), 
                        Integer.parseInt(request.getParameter("cliente[tlfMovil]")),
                        request.getParameter("cliente[email]"), request.getParameter("cliente[observaciones]"),gestor);
                idCliente = gestor.crearCliente(cli);
            }
            
            out.print(gestor.crearVenta(idCliente,new Albaran(request.getParameter("concepto"),
                    request.getParameter("entrega[provEntrega]"), request.getParameter("entrega[pobEntrega]"),
                    Integer.parseInt(request.getParameter("entrega[cpEntrega]")),
                    request.getParameter("entrega[calleEntrega]"), request.getParameter("entrega[numEntrega]"),
                    request.getParameter("entrega[escaleraEntrega]"), 
                    Integer.parseInt(request.getParameter("entrega[pisoEntrega]")),
                    request.getParameter("entrega[puertaEntrega]"),
                    material, request.getParameter("observaciones"), cli, gestor)));
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
