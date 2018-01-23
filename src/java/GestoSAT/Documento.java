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

public abstract class Documento {
	private Date fchCreacion;
	private String observaciones="";
	private Cliente cliente;
	private GestoSAT gestoSAT;
        
        public Documento(String observaciones, Cliente cliente, GestoSAT gestoSAT){
            
            this.fchCreacion = new Date();
            if(!observaciones.equals("0"))
                this.observaciones = observaciones;
            this.cliente = cliente;
            this.gestoSAT = gestoSAT;
        }

        public Documento(String observaciones, Date creacion, Cliente cliente, GestoSAT gestoSAT){
            this.fchCreacion = creacion;
            if(!observaciones.equals("0"))
                this.observaciones = observaciones;
            this.cliente = cliente;
            this.gestoSAT = gestoSAT;
        }
        
    public Date getFchCreacion() {
        return fchCreacion;
    }

    public void setFchCreacion(Date fchCreacion) {
        this.fchCreacion = fchCreacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public GestoSAT getGestoSAT() {
        return gestoSAT;
    }

    public void setGestoSAT(GestoSAT gestoSAT) {
        this.gestoSAT = gestoSAT;
    }
}