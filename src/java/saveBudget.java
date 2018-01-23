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


import GestoSAT.GestoSAT;
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
public class saveBudget extends HttpServlet {

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
            
            Gson gJson = new Gson();
            
            String[] fch_aux = request.getParameter("1").split(" ");
            int mes = 0;
            switch(fch_aux[1]){
                case "Febrero":
                    mes = 1;
                    break;
                case "Marzo":
                    mes = 2;
                    break;
                case "Abril":
                    mes = 3;
                    break;
                case "Mayo":
                    mes = 4;
                    break;
                case "Junio":
                    mes = 5;
                    break;
                case "Julio":
                    mes = 6;
                    break;
                case "Agosto":
                    mes = 7;
                    break;
                case "Semptiembre":
                    mes = 8;
                    break;
                case "Octubre":
                    mes = 9;
                    break;
                case "Noviembre":
                    mes = 10;
                    break;
                case "Diciembre":
                    mes = 11;
                    break;
            };
            
            Map empleados = (HashMap)gJson.fromJson(request.getParameter("10"), HashMap.class); // Parse JSON
            Map stock = (HashMap)gJson.fromJson(request.getParameter("11"), HashMap.class); // Parse JSON
                
            out.print(gestor.crearPresupuesto(Integer.parseInt(request.getParameter("12").toString()),
                    new Presupuesto(request.getParameter("0"), new Date(Integer.parseInt(fch_aux[2])-1900,
                        mes,Integer.parseInt(fch_aux[0])), Boolean.parseBoolean(request.getParameter("2")),
                        request.getParameter("4"), Float.parseFloat(request.getParameter("3")),
                        Integer.parseInt(request.getParameter("5")), request.getParameter("6"),
                        request.getParameter("7"), request.getParameter("8"),
                        gestor.getEntrada(Integer.parseInt(request.getParameter("12").toString())),
                        request.getParameter("9"), empleados, stock, gestor)));
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
