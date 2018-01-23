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

import GestoSAT.Cita;
import GestoSAT.Cliente;
import GestoSAT.Entrada;
import GestoSAT.GestoSAT;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author S
 */
public class addEntradaCita extends HttpServlet {

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
            String[] fch_aux = request.getParameter("fecha").split(" ");
            String[] temps = fch_aux[4].split(":");
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
            
            int id_Cliente,id_Entrada,id_Dir;
            Cita cita;
            Entrada e;
            Cliente c;
            if(request.getParameter("id_Cliente").equals("0")){
                c = new Cliente(request.getParameter("nombre"),
                        request.getParameter("apellidos"), request.getParameter("dni"),
                        request.getParameter("provincia"), request.getParameter("poblacion"),
                        Integer.parseInt(request.getParameter("cp")), request.getParameter("calle"),
                        request.getParameter("numero"), request.getParameter("escalera"),
                        Integer.parseInt(request.getParameter("piso")), request.getParameter("puerta"),
                        Integer.parseInt(request.getParameter("tlfCasa")), 
                        Integer.parseInt(request.getParameter("tlfMovil")),
                        request.getParameter("correo"), request.getParameter("observaciones"),gestor);
                id_Cliente=c.crearCliente();
                
                e = new Entrada(request.getParameter("observacionesEntrada"),gestor,c);
                id_Dir = gestor.guardarDireccion(request.getParameter("provinciaCita"),
                        request.getParameter("poblacionCita"),Integer.parseInt(request.getParameter("cpCita")),
                        request.getParameter("calleCita"),request.getParameter("numeroCita"),
                        request.getParameter("escaleraCita"),Integer.parseInt(request.getParameter("pisoCita")),
                        request.getParameter("puertaCita"),request.getParameter("observacionesLugar"));
                
                cita = new Cita(new Date(Integer.parseInt(fch_aux[2])-1900,
                        mes,Integer.parseInt(fch_aux[0]),Integer.parseInt(temps[0]),
                        Integer.parseInt(temps[1])),request.getParameter("provinciaCita"),
                        request.getParameter("poblacionCita"),Integer.parseInt(request.getParameter("cpCita")),
                        request.getParameter("calleCita"),request.getParameter("numeroCita"),
                        request.getParameter("escaleraCita"),Integer.parseInt(request.getParameter("pisoCita")),
                        request.getParameter("puertaCita"),new HashMap(),request.getParameter("motivo"),
                        request.getParameter("observacionesCita"),request.getParameter("observacionesLugar"),e);
            }else{
                id_Cliente = Integer.parseInt(request.getParameter("id_Cliente"));
                c = gestor.getCliente(id_Cliente);
                e = new Entrada(request.getParameter("observacionesEntrada"),gestor,c);
                if(request.getParameter("id_Direccion").equals("0")){
                    id_Dir = gestor.guardarDireccion(request.getParameter("provinciaCita"),
                        request.getParameter("poblacionCita"),Integer.parseInt(request.getParameter("cpCita")),
                        request.getParameter("calleCita"),request.getParameter("numeroCita"),
                        request.getParameter("escaleraCita"),Integer.parseInt(request.getParameter("pisoCita")),
                        request.getParameter("puertaCita"),request.getParameter("observacionesLugar"));
                }else
                    id_Dir = Integer.parseInt(request.getParameter("id_Direccion"));
                
                cita = new Cita(new Date(Integer.parseInt(fch_aux[2])-1900,
                        mes,Integer.parseInt(fch_aux[0]),Integer.parseInt(temps[0]),
                        Integer.parseInt(temps[1])),request.getParameter("provinciaCita"),
                        request.getParameter("poblacionCita"),Integer.parseInt(request.getParameter("cpCita")),
                        request.getParameter("calleCita"),request.getParameter("numeroCita"),
                        request.getParameter("escaleraCita"),Integer.parseInt(request.getParameter("pisoCita")),
                        request.getParameter("puertaCita"), new HashMap() ,request.getParameter("motivo"),
                        request.getParameter("observacionesCita"),request.getParameter("observacionesLugar"),e);
            }
            id_Entrada = gestor.guardarEntrada(e,(Integer)ses.getAttribute("id"));
            gestor.guardarCita(cita ,id_Cliente, id_Entrada, id_Dir);
            out.print(id_Entrada);
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
