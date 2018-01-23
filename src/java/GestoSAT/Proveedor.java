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

public class Proveedor {
	private String nombre;
	private String nif;
	private String provincia;
	private String poblacion;
	private int cp;
	private String calle;
	private String numero;
	private String escalera;
	private int piso;
	private String puerta;
	private String email;
	private int fax;
	private int tlfFijo;
	private int tlfMovil;
	private GestoSAT gestoSAT;
	private Map material = new HashMap();

        public Proveedor(String nombre, String nif, String provincia, String poblacion,
                int cp, String calle, String numero, String escalera, int piso, 
                String puerta, int tlfFijo, int tlfMovil, String email, int fax, GestoSAT gestoSAT) {
            this.nombre = nombre;
            this.nif = nif;
            this.provincia = provincia;
            this.poblacion = poblacion;
            this.cp = Math.abs(cp);
            this.calle = calle;
            this.numero = numero;
            this.escalera = escalera;
            this.piso = piso;
            this.puerta = puerta;
            this.email = email;
            this.fax = Math.abs(fax);
            this.tlfFijo = Math.abs(tlfFijo);
            this.tlfMovil = Math.abs(tlfMovil);
            this.gestoSAT = gestoSAT;
        }
        
        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getNif() {
            return nif;
        }

        public void setNif(String nif) {
            this.nif = nif;
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
        
        public boolean guardarCambios(int id,String nombre, String nif, String provincia, String poblacion,
                int cp, String calle, String numero, String escalera, int piso, String puerta,
                int tlfFijo, int tlfMovil, String email, int fax) {
            this.nombre = nombre;
            this.nif = nif;
            this.provincia = provincia;
            this.poblacion = poblacion;
            this.cp = Math.abs(cp);
            this.calle = calle;
            this.numero = numero;
            this.escalera = escalera;
            this.piso = piso;
            this.puerta = puerta;
            this.email = email;
            this.fax = Math.abs(fax);
            this.tlfFijo = Math.abs(tlfFijo);
            this.tlfMovil = Math.abs(tlfMovil);
            
            return gestoSAT.modificarProveedor(id, this);
        }
        
        public void actualizar(String nombre, String nif, String provincia, String poblacion,
                int cp, String calle, String numero, String escalera, int piso, String puerta,
                int tlfFijo, int tlfMovil, String email, int fax) {
            this.nombre = nombre;
            this.nif = nif;
            this.provincia = provincia;
            this.poblacion = poblacion;
            this.cp = Math.abs(cp);
            this.calle = calle;
            this.numero = numero;
            this.escalera = escalera;
            this.piso = piso;
            this.puerta = puerta;
            this.email = email;
            this.fax = Math.abs(fax);
            this.tlfFijo = Math.abs(tlfFijo);
            this.tlfMovil = Math.abs(tlfMovil);
        }
        
        public Stock setStock(int id,Stock stock){
            if(material.putIfAbsent(id, stock)!=null)
                ((Stock)material.get(id)).actualizar(stock.getNombre(),stock.getCantidad(),stock.getPrecioUnidad(),stock.getDescripcion(),stock.getPrecioCompra(), stock.getAlerta());
            return (Stock)material.get(id);
                        
        }
        
        public Stock getStock(int id){
            return (Stock)material.get(id);
        }
}