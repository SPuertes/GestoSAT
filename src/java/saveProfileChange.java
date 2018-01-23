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

import GestoSAT.Empleado;
import GestoSAT.Gerente;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author salvador
 */
public class saveProfileChange extends HttpServlet {

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
            String pass;
            if(request.getParameter("pass")!= null)
                pass = request.getParameter("pass");
            else
                pass = "";
            
            float sueldo = 0;
            float hora = 0;
            
            
            if(request.getParameter("sueldo") != null)
                sueldo = Float.parseFloat(request.getParameter("sueldo"));
            
            if(request.getParameter("hora") != null)
                hora = Float.parseFloat(request.getParameter("hora"));
            
            if((Boolean)ses.getAttribute("gerente")){
                Gerente e = (Gerente)ses.getAttribute("usuario");
                if( e.getOficina().actualizarOficina(request.getParameter("nombreEmp"),request.getParameter("nif"),request.getParameter("provinciaEmp"), request.getParameter("poblacionEmp"),Integer.parseInt(request.getParameter("cpEmp")),
                        request.getParameter("calleEmp"), request.getParameter("numeroEmp"), request.getParameter("correoEmp"),
                        Integer.parseInt(request.getParameter("tlfCasaEmp")), Integer.parseInt(request.getParameter("tlfMovilEmp")),
                        Integer.parseInt(request.getParameter("faxEmp"))) && e.modificarDatosPersonales((Integer)ses.getAttribute("id"),request.getParameter("nombre"), request.getParameter("apellidos"), request.getParameter("dni"),
                        Integer.parseInt(request.getParameter("cp")), request.getParameter("provincia"), request.getParameter("poblacion"), 
                        request.getParameter("calle"), request.getParameter("numero"), request.getParameter("escalera"),
                        Integer.parseInt(request.getParameter("piso")), request.getParameter("puerta"), Integer.parseInt(request.getParameter("tlfCasa")),
                        Integer.parseInt(request.getParameter("tlfMovil")), request.getParameter("correo"), pass, sueldo, hora))
                    out.print(true);
                else
                    out.print(false);
            }else{
                Empleado e = (Empleado)ses.getAttribute("usuario");
                
                if(e.modificarDatosPersonales((Integer)ses.getAttribute("id"),request.getParameter("nombre"), request.getParameter("apellidos"), request.getParameter("dni"),
                        Integer.parseInt(request.getParameter("cp")), request.getParameter("provincia"), request.getParameter("poblacion"), 
                        request.getParameter("calle"), request.getParameter("numero"), request.getParameter("escalera"),
                        Integer.parseInt(request.getParameter("piso")), request.getParameter("puerta"), Integer.parseInt(request.getParameter("tlfCasa")),
                        Integer.parseInt(request.getParameter("tlfMovil")), request.getParameter("correo"), pass))
                    out.print(true);
                else
                    out.print(false);
            }
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
        response.sendRedirect("index.jsp");
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
        return "Access Denied";
    }// </editor-fold>

}
