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
import java.util.Iterator;
import java.util.Map;

public class Cliente {
	private String nombre="";
	private String apellidos="";
	private String nif="";
	private String provincia="";
	private String poblacion="";
	private int cp;
	private String calle="";
	private String numero="";
	private String escalera="";
	private int piso;
	private String puerta="";
	private int tlfFijo;
	private int tlfMovil;
	private String email="";
	private String observaciones="";
	private Map aparato = new HashMap();
	private GestoSAT gestoSAT;
	private Map documento = new HashMap();

        public Cliente(String nombre, String apellidos, String nif, String provincia,
                String poblacion, int cp, String calle, String numero, String escalera,
                int piso, String puerta, int tlfFijo, int tlfMovil, String email,
                String observaciones, GestoSAT gestor) {
            if(!nombre.equals("0"))
                this.nombre = nombre;
            if(!apellidos.equals("0"))
                this.apellidos = apellidos;
            if(!nif.equals("0"))
                this.nif = nif;
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
            this.piso = piso;
            if(!puerta.equals("0"))
                this.puerta = puerta;
            if(tlfFijo > 0)
                this.tlfFijo = tlfFijo;
            if(tlfMovil > 0)
                this.tlfMovil = tlfMovil;
            if(!email.equals("0"))
                this.email = email;
            if(!observaciones.equals("0"))
                this.observaciones = observaciones;
            this.gestoSAT = gestor;
        }

        public Cliente(String nombre, String apellidos, String nif, String provincia,
                String poblacion, int cp, String calle, String numero, String escalera,
                int piso, String puerta, int tlfFijo, int tlfMovil, String email,
                String observaciones, Map aparatos ,GestoSAT gestor) {
            if(!nombre.equals("0"))
                this.nombre = nombre;
            if(!apellidos.equals("0"))
                this.apellidos = apellidos;
            if(!nif.equals("0"))
                this.nif = nif;
            if(!provincia.equals("0"))
                this.provincia = provincia;
            if(!poblacion.equals("0"))
                this.poblacion = poblacion;
            if(cp >0)
                this.cp = cp;
            if(!calle.equals("0"))
                this.calle = calle;
            if(!numero.equals("0"))
                this.numero = numero;
            if(!escalera.equals("0"))
                this.escalera = escalera;
            this.piso = piso;
            if(!puerta.equals("0"))
                this.puerta = puerta;
            if(tlfFijo > 0)
                this.tlfFijo = tlfFijo;
            if(tlfMovil > 0)
                this.tlfMovil = tlfMovil;
            if(!email.equals("0"))
                this.email = email;
            if(!observaciones.equals("0"))
                this.observaciones = observaciones;
            this.gestoSAT = gestor;
            this.aparato = aparatos;
            Iterator itAparatos = aparato.entrySet().iterator();
            while(itAparatos.hasNext()){
                Map.Entry auxAparatos = (Map.Entry)itAparatos.next();
                ((Aparato)auxAparatos.getValue()).setCliente(this);
            }
        }
        
        public int crearCliente(){
            return gestoSAT.crearCliente(this);
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

        public String getObservaciones() {
            return observaciones;
        }

        public void setObservaciones(String observaciones) {
            this.observaciones = observaciones;
        }

        public Map getAparatos() {
            return aparato;
        }

        public Aparato getAparato(int id) {
            return (Aparato)aparato.get(id);
        }
        
        public void setAparato(Aparato aparato, int id) {
            this.aparato.put(id,aparato);
        }

        public void setAparatos(Map aparatos){
            this.aparato = aparatos;
            Iterator itAparato = aparato.entrySet().iterator();
            while(itAparato.hasNext()){
                Map.Entry auxAparatos = (Map.Entry)itAparato.next();
                ((Aparato)auxAparatos.getValue()).setCliente(this);
            }
        }
        
        public GestoSAT getGestoSAT() {
            return gestoSAT;
        }

        public  void setGestoSAT(GestoSAT gestoSAT) {
            this.gestoSAT = gestoSAT;
        }

        public  void setEntrada(int id, Entrada entrada){
            this.documento.putIfAbsent(id+";Entrada", entrada);
        }
        
        public  void setPresupuesto(int id, Presupuesto presupuesto){
            this.documento.putIfAbsent(id+";Presupuesto", presupuesto);
        }
        
        public  void setAlbaran(int id, Albaran albaran){
            this.documento.putIfAbsent(id+";Albaran", albaran);
        }
        
        public  void setFactura(int id, Factura factura){
            this.documento.putIfAbsent(id+";Factura", factura);
        }
        
        public  void setRecibo(int id, Recibo recibo){
            this.documento.putIfAbsent(id+";Recibo", recibo);
        }
        
        public  void deleteEntrada(int id){
            this.documento.remove(id+";Entrada");
        }
        
        public  void deletePresupuesto(int id){
            this.documento.remove(id+";Presupuesto");
        }
        
        public  void deleteAlbaran(int id){
            this.documento.remove(id+";Albaran");
        }
        
        public  void deleteFactura(int id){
            this.documento.remove(id+";Factura");
        }
        
        public  void deleteRecibo(int id){
            this.documento.remove(id+";Recibo");
        }

        public Map getDocumentos() {
            return documento;
        }
        
        private Map getAlbaranesPendientes(){
            Iterator itDocs = this.documento.entrySet().iterator();
            Map albaranes = new HashMap();
            
            while(itDocs.hasNext()){
                Map.Entry auxDocs = (Map.Entry)itDocs.next();
                if(auxDocs.getValue().getClass().getName().equals("GestoSAT.Albaran")){
                    if(((Albaran)auxDocs.getValue()).getFactura() == null)
                        albaranes.put(auxDocs.getKey(),auxDocs.getValue());
                }
            }
            return albaranes;
        }
        
        public Map getAlbaranes(){
            Iterator itDocs = this.documento.entrySet().iterator();
            Map albaranes = new HashMap();
            
            while(itDocs.hasNext()){
                Map.Entry auxDocs = (Map.Entry)itDocs.next();
                if(auxDocs.getValue().getClass().getName().equals("GestoSAT.Albaran")){
                    albaranes.put(auxDocs.getKey(),auxDocs.getValue());
                }
            }
            return albaranes;
        }
        
        public Map cargarAlbaranesPendientes(int idCliente){
            Map auxMap = new HashMap();
            auxMap.putAll(this.documento);
            Iterator itDocs = auxMap.entrySet().iterator();
           
            while(itDocs.hasNext()){
                Map.Entry auxDocs = (Map.Entry)itDocs.next();
                if(auxDocs.getValue().getClass().getName().equals("GestoSAT.Entrada"))
                    ((Entrada)auxDocs.getValue()).cargarPresupuesto(Integer.parseInt(auxDocs.getKey().toString().split(";")[0]));
            }
            this.gestoSAT.cargarVentasPendientes(idCliente);
            this.gestoSAT.cargarAlbaranesPendientes(idCliente);
            
            return this.getAlbaranesPendientes();
        }
        
        public boolean actualizarDatos(int id,String nombre, String apellidos, String nif, String provincia, String poblacion, int cp, String calle,
                String numero, String escalera, int piso, String puerta, int tlfFijo, int tlfMovil, String email, String observaciones) {
            if(!nombre.equals("0"))
                this.nombre = nombre;
            if(!apellidos.equals("0"))
                this.apellidos = apellidos;
            if(!nif.equals("0"))
                this.nif = nif;
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
            this.piso = Math.abs(piso);
            if(!puerta.equals("0"))
                this.puerta = puerta;
            if(tlfFijo != 0)
                this.tlfFijo = tlfFijo;
            if(tlfMovil != 0)
                this.tlfMovil = tlfMovil;
            if(!email.equals("0"))
                this.email = email;
            if(!observaciones.equals("0"))
                this.observaciones = observaciones;
            
            return gestoSAT.guardarCliente(id, this);
        }
}