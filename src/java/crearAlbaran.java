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
import GestoSAT.GestoSAT;
import GestoSAT.Cliente;
import GestoSAT.Entrada;
import GestoSAT.Presupuesto;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
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
public class crearAlbaran extends HttpServlet {

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
            
            int id = Integer.parseInt(request.getParameter("id"));
            String flag = request.getParameter("flag");
            
            Entrada entrada;
            Presupuesto presupuesto;
            Gson gJson = new Gson();
            
            Map empleados = (HashMap)gJson.fromJson(request.getParameter("trabajadores"), HashMap.class); // Parse JSON
            Map material = (HashMap)gJson.fromJson(request.getParameter("material"), HashMap.class); // Parse JSON
            
            if(flag.equals("E")){
                entrada = (Entrada)gestor.getEntrada(id);
                // Crear presupost en default values
                id = gestor.crearPresupuesto(id, new Presupuesto(request.getParameter("concepto"), new Date(),
                        true,"", 0, 0, "", "", "",entrada,request.getParameter("observaciones")+" | Documento auxiliar", empleados, material, gestor));
                presupuesto = gestor.getPresupuesto(id);
            }else{
                presupuesto = (Presupuesto)gestor.getPresupuesto(id);
                entrada = presupuesto.getEntrada();
            }
            
            Cliente cli = entrada.getCliente();
            
            out.print(gestor.crearAlbaran(id, new Albaran(request.getParameter("concepto"),
                    request.getParameter("entrega[provEntrega]"), request.getParameter("entrega[pobEntrega]"),
                    Integer.parseInt(request.getParameter("entrega[cpEntrega]")),
                    request.getParameter("entrega[calleEntrega]"), request.getParameter("entrega[numEntrega]"),
                    request.getParameter("entrega[escaleraEntrega]"), 
                    Integer.parseInt(request.getParameter("entrega[pisoEntrega]")),
                    request.getParameter("entrega[puertaEntrega]"),
                    material, empleados, request.getParameter("observaciones"), presupuesto, cli, gestor)));
                        
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
