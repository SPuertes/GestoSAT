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

public class Aparato {
	private String tipo="";
	private String marca="";
	private String modelo="";
	private String color="";
	private String numSerie="";
	private String observaciones="";
        private Cliente cliente;
    
    public Aparato(String tipo, String marca, String modelo, String color, String numSerie, String observaciones) {
        if(!tipo.equals("0"))
            this.tipo = tipo;
        if(!marca.equals("0"))    
            this.marca = marca;
        if(!modelo.equals("0"))
            this.modelo = modelo;
        if(!color.equals("0"))
            this.color = color;
        if(!numSerie.equals("0"))    
            this.numSerie = numSerie;
        if(!observaciones.equals("0"))
            this.observaciones = observaciones.replaceAll("~", ",");
    }
    
    public Aparato(String tipo, String marca, String modelo, String color, String numSerie, String observaciones, Cliente cliente) {
        if(!tipo.equals("0"))
            this.tipo = tipo;
        if(!marca.equals("0"))    
            this.marca = marca;
        if(!modelo.equals("0"))
            this.modelo = modelo;
        if(!color.equals("0"))
            this.color = color;
        if(!numSerie.equals("0"))    
            this.numSerie = numSerie;
        if(!observaciones.equals("0"))
            this.observaciones = observaciones.replaceAll("~", ",");
        this.cliente = cliente;
    }
    
    public Aparato(String tipo, String marca, String modelo, String color, String numSerie, String observaciones, Cliente cliente, int id_Aparato) {
        if(!tipo.equals("0"))
            this.tipo = tipo;
        if(!marca.equals("0"))    
            this.marca = marca;
        if(!modelo.equals("0"))
            this.modelo = modelo;
        if(!color.equals("0"))
            this.color = color;
        if(!numSerie.equals("0"))    
            this.numSerie = numSerie;
        if(!observaciones.equals("0"))
            this.observaciones = observaciones.replaceAll("~", ",");
        this.cliente = cliente;
        cliente.setAparato(this,id_Aparato);
    }
    
    public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public void setMarca(String marca) {
            this.marca = marca;
        }

        public void setModelo(String modelo) {
            this.modelo = modelo;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public void setNumSerie(String numSerie) {
            this.numSerie = numSerie;
        }

        public void setObservaciones(String observaciones) {
            this.observaciones = observaciones;
        }

        public String getTipo() {
            return tipo;
        }

        public String getMarca() {
            return marca;
        }

        public String getModelo() {
            return modelo;
        }

        public String getColor() {
            return color;
        }

        public String getNumSerie() {
            return numSerie;
        }

        public String getObservaciones() {
            return observaciones;
        }
        
        public void setCliente(Cliente cliente){
            this.cliente = cliente;
            
        }
        
        public Cliente getCliente(){
            return cliente;
        }

}