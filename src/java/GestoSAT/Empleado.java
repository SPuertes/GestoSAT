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

import java.util.HashMap;
import java.util.Map;

public class Empleado {
	protected String nombre;
	protected String apellidos;
	protected String dni;
	protected String provincia;
	protected String poblacion;
	protected int cp;
	protected String calle;
        protected String numero;
	protected String escalera;
	protected int piso;
	protected String puerta;
	protected int tlfFijo;
	protected int tlfMovil;
	protected String email;
	private boolean activo;
	protected float sueldoBase;
	protected float precioHora;
	protected Oficina oficina;
	private Map cita = new HashMap();
	protected GestoSAT gestoSAT;
	
	public Empleado(String nombre, String apellidos, String dni,
                String poblacion, String provincia, int cp, String calle, String numero,
                String escalera, int piso, String puerta, int tlfFijo, int tlfMovil,
                float sueldo, float precioHora, String email){
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
            this.precioHora = Math.abs(precioHora);
            this.email = email;
            this.activo = true;
        }
        
        public Empleado(String nombre, String apellidos, String dni,
                String poblacion, String provincia, int cp, String calle, String numero,
                String escalera, int piso, String puerta, int tlfFijo, int tlfMovil,
                float sueldo, float precioHora, String email, boolean activo){
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
            this.precioHora = Math.abs(precioHora);
            this.email = email;
            this.activo = activo;
        }
        
        public Empleado(String nombre, String apellidos, String dni,
                String poblacion, String provincia, int cp, String calle, String numero,
                String escalera, int piso, String puerta, int tlfFijo, int tlfMovil,
                float sueldo, float precioHora, String email,String nomEmp, String nif, String provinciaEmp,
                String poblacionEmp, int cpEmp, String calleEmp,
                String numeroEmp, String emailEmp, int tlfFijoEmp,int tlfMovilEmp,
                int faxEmp){
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
            this.precioHora = Math.abs(precioHora);
            this.email = email;
            this.activo = true;
            
            oficina = new  Oficina(nomEmp,nif,provinciaEmp, poblacionEmp, cpEmp, calleEmp,
            numeroEmp, emailEmp, tlfFijoEmp, tlfMovilEmp, faxEmp, true);
        
            oficina.setEmpleado(this);
        }
        
        public boolean modificarDatosPersonales(int id,String nombre, String apellidos, String dni, int cp,
                String provincia, String poblacion, String calle, String numero, String escalera,
                int piso, String puerta, int tlfFijo, int tlfMovil, String email, String newPass) {
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
            this.email = email;
            
            return gestoSAT.actualizarUsuario(this,newPass,id);
        }
        
        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getApellidos() {
            return apellidos;
        }

        public void setApellidos(String apellidos) {
            this.apellidos = apellidos;
        }

        public String getDni() {
            return dni;
        }

        public void setDni(String dni) {
            this.dni = dni;
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

        public int getCp() {
            return cp;
        }

        public void setCp(int cp) {
            this.cp = Math.abs(cp);
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

        public String getEscalera() {
            return escalera;
        }

        public void setEscalera(String escalera) {
            this.escalera = escalera;
        }

        public int getPiso() {
            return piso;
        }

        public void setPiso(int piso) {
            this.piso = Math.abs(piso);
        }

        public String getPuerta() {
            return puerta;
        }

        public void setPuerta(String puerta) {
            this.puerta = puerta;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public float getSueldoBase() {
            return sueldoBase;
        }

        public void setSueldoBase(float sueldoBase) {
            this.sueldoBase = Math.abs(sueldoBase);
        }

        public float getPrecioHora() {
            return precioHora;
        }

        public void setPrecioHora(float precioHora) {
            this.precioHora = Math.abs(precioHora);
        }

        public Oficina getOficina() {
            return oficina;
        }

        public void setOficina(Oficina oficina) {
            this.oficina = oficina;
        }

        public GestoSAT getGestoSAT() {
            return gestoSAT;
        }

        public void setGestoSAT(GestoSAT gestoSAT) {
            this.gestoSAT = gestoSAT;
        }

        public boolean isActivo() {
            return activo;
        }

        public void setActivo(boolean activo) {
            this.activo = activo;
        }

        public void setCita(int id, Cita cita){
            this.cita.put(id, cita);
        }
        
        public Cita getCita(int id){
            return (Cita)cita.get(id);
        }
        
        public Map getCitas(){
            return cita;
        }
        
        public void removeCita(int id){
            cita.remove(id);
        }
}