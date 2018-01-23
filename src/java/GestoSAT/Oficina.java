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

package GestoSAT;

import java.util.Vector;

public class Oficina {
	private String nombre;
	private boolean central;
	private String nif;
        private String calle;
	private String numero;
	private int cp;
	private String provincia;
	private String poblacion;
	private String email;
	private int tlfFijo;
	private int tlfMovil;
	private int fax;
	private Vector<Empleado> empleado = new Vector<Empleado>();

        public Oficina(String nombre, String nif, String provincia, String poblacion, int cp, String calle,
            String numero, String email, int tlfFijo, int tlfMovil, int fax, boolean central){
            
            this.nombre = nombre;
            this.nif = nif;
            this.central = central;
            this.calle = calle;
            this.numero = numero;
            this.cp = Math.abs(cp);
            this.provincia = provincia;
            this.poblacion = poblacion;
            this.email = email;
            this.tlfFijo = Math.abs(tlfFijo);
            this.tlfMovil = Math.abs(tlfMovil);
            this.fax = Math.abs(fax);
        }
        
        public void setEmpleado(Empleado empleado){
            this.empleado.add(empleado);
        }

        public boolean isCentral() {
            return central;
        }

        public void setCentral(boolean central) {
            this.central = central;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        
        public String getCalle() {
            return calle;
        }

        public void setCalle(String calle) {
            this.calle = calle;
        }

        public String getNumero() {
            return numero;
        }

        public void setNumero(String numero) {
            this.numero = numero;
        }

        public int getCp() {
            return cp;
        }

        public void setCp(int cp) {
            this.cp = Math.abs(cp);
        }

        public String getProvincia() {
            return provincia;
        }

        public void setProvincia(String provincia) {
            this.provincia = provincia;
        }

        public String getPoblacion() {
            return poblacion;
        }

        public void setPoblacion(String poblacion) {
            this.poblacion = poblacion;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getTlfFijo() {
            return tlfFijo;
        }

        public void setTlfFijo(int tlfFijo) {
            this.tlfFijo = Math.abs(tlfFijo);
        }

        public int getTlfMovil() {
            return tlfMovil;
        }

        public void setTlfMovil(int tlfMovil) {
            this.tlfMovil = Math.abs(tlfMovil);
        }

        public int getFax() {
            return fax;
        }

        public void setFax(int fax) {
            this.fax = Math.abs(fax);
        }

        public Vector<Empleado> getEmpleado() {
            return empleado;
        }
        
        public String getNif(){
            return nif;
        }
        
        public void setNif(String nif){
            this.nif = nif;
        }
        
        public boolean actualizarOficina(String nombre, String nif, String provincia, String poblacion, int cp,
               String calle, String numero, String email, int tlfFijo, int tlfMovil, int fax){
            
            this.nombre = nombre;
            this.nif = nif;
            this.calle = calle;
            this.numero = numero;
            this.cp = Math.abs(cp);
            this.provincia = provincia;
            this.poblacion = poblacion;
            this.email = email;
            this.tlfFijo = Math.abs(tlfFijo);
            this.tlfMovil = Math.abs(tlfMovil);
            this.fax = Math.abs(fax);
            
            if(empleado.get(0).getClass().getName().equals("GestoSAT.Gerente"))
                if(((Gerente)empleado.get(0)).actualizarOficina())
                    return true;
                else
                    return false;
            else
                return false;
        }
        
}