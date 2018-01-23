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

public class Gerente extends Empleado {

        public Gerente(String nombre, String apellidos, String dni,
                String poblacion, String provincia, int cp, String calle, String numero,
                String escalera, int piso, String puerta, int tlfFijo, int tlfMovil,
                float sueldo, float precioHora, String email){
           
            super(nombre, apellidos, dni, poblacion, provincia, cp, calle, numero,
                escalera, piso, puerta, tlfFijo, tlfMovil, sueldo, precioHora,
                email);
        }
    
        public Gerente(String nombre, String apellidos, String dni,
                String poblacion, String provincia, int cp, String calle, String numero,
                String escalera, int piso, String puerta, int tlfFijo, int tlfMovil,
                float sueldo, float precioHora, String email, boolean activo){
           
            super(nombre, apellidos, dni, poblacion, provincia, cp, calle, numero,
                escalera, piso, puerta, tlfFijo, tlfMovil, sueldo, precioHora,
                email, activo);
        }
        
        public Gerente(String nombre, String apellidos, String dni,
                String poblacion, String provincia, int cp, String calle, String numero,
                String escalera, int piso, String puerta, int tlfFijo, int tlfMovil,
                float sueldo, float precioHora, String email,String nombreEmp,String nif, String provinciaEmp,
                String poblacionEmp, int cpEmp, String calleEmp,
                String numeroEmp, String emailEmp, int tlfFijoEmp,
                int tlfMovilEmp, int faxEmp){
            
            super(nombre, apellidos, dni, poblacion, provincia, cp, calle, numero,
                escalera, piso, puerta, tlfFijo, tlfMovil, sueldo, precioHora,
                 email,nombreEmp, nif, provinciaEmp, poblacionEmp, cpEmp, calleEmp, numeroEmp,
                 emailEmp, tlfFijoEmp, tlfMovilEmp, faxEmp);
        }
        
        public boolean modificarUsuario(float newSueldoBase, float newPrecioHora, boolean ascenso, Empleado usuarioSeleccionado,int id) {
		
            usuarioSeleccionado.setSueldoBase(newSueldoBase);
            usuarioSeleccionado.setPrecioHora(newPrecioHora);
            
            if(usuarioSeleccionado.getClass().getName().equals("GestoSAT.Empleado") && ascenso ||
                    usuarioSeleccionado.getClass().getName().equals("GestoSAT.Gerente") && !ascenso)
                    usuarioSeleccionado = gestoSAT.cambiarUsuario(id);
                    
                return gestoSAT.guardarEmpleado(usuarioSeleccionado,id);
        }

	public boolean eliminarUsuario(int id, Empleado usuario) {
            if(usuario.isActivo()){
                usuario.setActivo(false);
                return this.getGestoSAT().desactivar(id);
            }else{
               return this.getGestoSAT().deleteUser(id);
            }
            
	}

        public boolean activarUsuario(int id, Empleado usuario) {
           usuario.setActivo(true);
           this.getGestoSAT().activar(id);
           return true;
	}
        
        public boolean actualizarOficina(){
            return gestoSAT.actualizarOficina(oficina);
        }
        
        public boolean modificarDatosPersonales(int id, String nombre, String apellidos, String dni, int cp,
                String provincia, String poblacion, String calle, String numero, String escalera,
                int piso, String puerta, int tlfFijo, int tlfMovil, String email, String newPass,
                float sueldo,float hora) {
            
            this.nombre = nombre;
            this.apellidos = apellidos;
            this.dni = dni;
            this.poblacion = poblacion;
            this.provincia = provincia;
            this.cp = Math.abs(cp);
            this.calle = calle;
            this.numero = numero;
            this.escalera = escalera;
            this.piso = Math.abs(piso);
            this.puerta = puerta;
            this.tlfFijo = Math.abs(tlfFijo);
            this.tlfMovil = Math.abs(tlfMovil);
            this.sueldoBase = Math.abs(sueldo);
            this.precioHora = Math.abs(hora);
            this.email = email;
        
            return gestoSAT.actualizarUsuario(this,newPass,id);
        }
}