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

public class Stock {
	private String nombre;
	private float cantidad;
	private float precioUnidad;
	private String descripcion;
	private float precioCompra;
	private float alerta;
	private Proveedor proveedor;

        public Stock(String nombre, float cantidad, float precioUnidad, String descripcion, 
	float precioCompra, float alerta, Proveedor proveedor){
            this.nombre = nombre;
            this.cantidad = Math.abs(cantidad);
            this.precioUnidad = Math.abs(precioUnidad);
            this.descripcion = descripcion;
            this.precioCompra = Math.abs(precioCompra);
            this.alerta = Math.abs(alerta);
            this.proveedor = proveedor;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public float getCantidad() {
            return cantidad;
        }

        public void setCantidad(float cantidad) {
            this.cantidad = Math.abs(cantidad);
        }

        public float getPrecioUnidad() {
            return precioUnidad;
        }

        public void setPrecioUnidad(float precioUnidad) {
            this.precioUnidad = Math.abs(precioUnidad);
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public float getPrecioCompra() {
            return precioCompra;
        }

        public void setPrecioCompra(float precioCompra) {
            this.precioCompra = Math.abs(precioCompra);
        }

        public float getAlerta() {
            return alerta;
        }

        public void setAlerta(float alerta) {
            this.alerta = Math.abs(alerta);
        }
        
        public Proveedor getProveedor(){
            return proveedor;
        }
        
	public boolean lanzarRecordatorio() {
            if(this.cantidad < this.alerta)
                return true;
            else
                return false;
        }
        
        public void quitar(float cantidad){
            this.cantidad = Math.abs(this.cantidad - cantidad);
        }
        
        public void anyadir(float cantidad){
            this.cantidad = Math.abs(this.cantidad + cantidad);
        }
        
        public void actualizar(String nombre, float cantidad, float precioUnidad, String descripcion, 
                float precioCompra, float alerta){
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.precioUnidad = precioUnidad;
            this.descripcion = descripcion;
            this.precioCompra = precioCompra;
            this.alerta = alerta;
        }
}