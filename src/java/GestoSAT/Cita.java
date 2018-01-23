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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Cita extends Incidencia {
	private Date fchCita;
	private String provincia="";
	private String poblacion="";
	private int cp;
	private String calle="";
	private String numero="";
	private String escalera="";
	private int piso;
	private String puerta="";
	private Map empleado = new HashMap();
        private String observacionesDireccion="";

    public Cita(Date fchCita, String provincia, String poblacion, int cp,
            String calle, String numero, String escalera, int piso, String puerta,
            Map empleado , String motivo, String observaciones, String observacionesDir, Entrada entrada) {
        
        super(motivo, observaciones,entrada);
        this.empleado = empleado;
        
        this.fchCita = fchCita;
        if(!provincia.equals("0"))
            this.provincia = provincia;
        
        if(!poblacion.equals("0"))
            this.poblacion = poblacion;
        if(cp > 0)
            this.cp = cp;
        if(!calle.equals("0"))
            this.calle = calle;
        if(!numero.equals("0"))
            this.numero = numero;
        if(!escalera.equals("0"))
            this.escalera = escalera;
        if(piso > 0)
            this.piso = piso;
        if(!puerta.equals("0"))
            this.puerta = puerta;
        if(!observacionesDir.equals("0"))
            this.observacionesDireccion = observacionesDir;
    }
    
    public Date getFchCita() {
        return fchCita;
    }

    public void setFchCita(Date fchCita) {
        this.fchCita = fchCita;
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

    public Map getEmpleados() {
        return empleado;
    }
    
    public Empleado getEmpleado(int id) {
        return (Empleado)empleado.get(id);
    }

    public void setEmpleados(Map empleado) {
        this.empleado = empleado;
    }
    
    public void setEmpleado(int id, Empleado empleado) {
        this.empleado.put(id, empleado);
    }

    public void removeEmpleado(int id){
        empleado.remove(id);
    }
    
    public String getObservacionesDirrecion() {
        return observacionesDireccion;
    }

    public void setObservacionesDirrecion(String observacionesDireccion) {
        this.observacionesDireccion = observacionesDireccion;
    }
}