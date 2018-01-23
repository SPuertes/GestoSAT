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

import com.google.gson.Gson;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Albaran extends Documento {
	private String concepto;
	private Date fchEntrega = null;
	private String provincia;
	private String poblacion;
	private int cp;
	private String calle;
	private String numero;
	private String escalera;
	private int piso;
	private String puerta;
        private float total;
	private Presupuesto presupuesto;
	private Map materialUtilizado = new HashMap();
	private Map trabajoRealizado = new HashMap();
	private Factura factura;

    public Albaran(String concepto, String provincia, String poblacion, int cp ,
            String calle, String numero, String escalera, int piso, String puerta,
            Map material, String observaciones, Cliente cliente, GestoSAT gestoSAT) {
        super(observaciones, cliente, gestoSAT);
        
        this.concepto = concepto;
        this.provincia = provincia;
        this.poblacion = poblacion;
        this.cp = Math.abs(cp);
        this.calle = calle;
        this.numero = numero;
        this.escalera = escalera;
        if(piso > 0)
            this.piso = piso;
        else
            this.piso = 0;
        this.puerta = puerta;
        this.factura = null;
        
        Map stockGestor = gestoSAT.getStock();
                
                Iterator itStock = material.entrySet().iterator();
                
                float precioVenta = 0;
                
                while(itStock.hasNext()){ 
                    // auxStock.getKey() -> ID Stock
                    // auxStock.getValue() -> Cantidad
                    Map.Entry auxStock = (Map.Entry)itStock.next();
                    
                    Stock auxMat = (Stock)stockGestor.get(Integer.parseInt(auxStock.getKey().toString()));
                    
                    precioVenta+= Math.abs(auxMat.getPrecioUnidad()*Float.parseFloat(auxStock.getValue().toString()));
                    
                    materialUtilizado.put(auxStock.getKey(),new MaterialTrabajos(auxMat,Float.parseFloat(auxStock.getValue().toString()))); // Stock, Float
                }
            
                // Calcular total
                synchronized(this){
                    this.total= (1+(gestoSAT.getIva()/(float)100))*precioVenta;
                    notifyAll();
                }
    }
    
    public Albaran(String concepto, String provincia, String poblacion, int cp ,
            String calle, String numero, String escalera, int piso, String puerta,
            Map material, Map trabajos, String observaciones, Presupuesto presupuesto,
            Cliente cliente, GestoSAT gestoSAT) {
        super(observaciones, cliente, gestoSAT);
        this.concepto = concepto;
        this.provincia = provincia;
        this.poblacion = poblacion;
        this.cp = Math.abs(cp);
        this.calle = calle;
        this.numero = numero;
        this.escalera = escalera;
        if(piso > 0)
            this.piso = piso;
        else
            this.piso = 0;
        this.puerta = puerta;
        this.factura = null;
        this.presupuesto = presupuesto;
        
        Gson gJsonTime = new Gson();
                Map time; // Parse JSON
                Iterator itEmp = trabajos.entrySet().iterator();
                Iterator itTime;
                
                float precioManoObra = 0;
                while(itEmp.hasNext()){ 
                    // auxEmp.getKey() -> ID Empleado
                    Map.Entry auxEmp = (Map.Entry)itEmp.next();
                    time = (HashMap)gJsonTime.fromJson(auxEmp.getValue().toString(), HashMap.class);
                    itTime = time.entrySet().iterator();
                    Empleado empleado = gestoSAT.getEmpleado(Integer.parseInt(auxEmp.getKey().toString()));
                    float horas = 0;
                    String desc = "";
                    
                    while(itTime.hasNext()){
                        Map.Entry auxTime = (Map.Entry)itTime.next();
                        if(!auxTime.getKey().toString().equals("desc")) // Hour or Minute
                            if(auxTime.getKey().toString().equals("m"))
                               horas+= Math.abs(Float.parseFloat(auxTime.getValue().toString())/60); // int minutos
                            else
                               horas+= Math.abs(Float.parseFloat(auxTime.getValue().toString())); // int Horas
                        else
                            desc =  auxTime.getValue().toString();
                    }
                    precioManoObra+= Math.abs(empleado.getPrecioHora()*horas);
                    
                    trabajoRealizado.put(auxEmp.getKey(), new Trabajo(horas,desc,empleado)); // Float, String, Empleado
                }
                
                Map stockGestor = gestoSAT.getStock();
                
                Iterator itStock = material.entrySet().iterator();
                
                float precioVenta = 0;
                
                while(itStock.hasNext()){ 
                    // auxStock.getKey() -> ID Stock
                    // auxStock.getValue() -> Cantidad
                    Map.Entry auxStock = (Map.Entry)itStock.next();
                    
                    Stock auxMat = (Stock)stockGestor.get(Integer.parseInt(auxStock.getKey().toString()));
                    
                    precioVenta+= Math.abs(auxMat.getPrecioUnidad()*Float.parseFloat(auxStock.getValue().toString()));
                    
                    materialUtilizado.put(auxStock.getKey(),new MaterialTrabajos(auxMat,Float.parseFloat(auxStock.getValue().toString()))); // Stock, Float
                }
            
                // Calcular total
                synchronized(this){
                    this.total= (precioVenta+precioManoObra)*(gestoSAT.getIva()/(float)100+1);
                    notifyAll();
                }
    }
    public Albaran(String concepto, String provincia, String poblacion, int cp ,
            String calle, String numero, String escalera, int piso, String puerta,
            Map material, Map trabajos, String observaciones, float total, Date fch_Creacion,
            Date fch_Entrega, Presupuesto presupuesto, Cliente cliente, GestoSAT gestoSAT) {
        super(observaciones, fch_Creacion, cliente, gestoSAT);
        
        this.concepto = concepto;
        this.provincia = provincia;
        this.poblacion = poblacion;
        this.cp = Math.abs(cp);
        this.calle = calle;
        this.numero = numero;
        this.escalera = escalera;
        if(piso > 0)
            this.piso = piso;
        else
            this.piso = 0;
        this.puerta = puerta;
        this.factura = null;
        if(fch_Entrega!=null)
            this.fchEntrega = fch_Entrega;
        this.presupuesto = presupuesto;
        this.trabajoRealizado = trabajos;
        this.materialUtilizado = material;
        
        synchronized(this){
            this.total= total;
            notifyAll();
        }
    }
    
    public Albaran(String concepto, String provincia, String poblacion, int cp ,
            String calle, String numero, String escalera, int piso, String puerta,
            Map material, String observaciones, float total, Date fch_Creacion, Cliente cliente, GestoSAT gestoSAT) {
        super(observaciones, fch_Creacion, cliente, gestoSAT);
        
        this.concepto = concepto;
        this.provincia = provincia;
        this.poblacion = poblacion;
        this.cp = Math.abs(cp);
        this.calle = calle;
        this.numero = numero;
        this.escalera = escalera;
        if(piso > 0)
            this.piso = piso;
        else
            this.piso = 0;
        this.puerta = puerta;
        this.factura = null;
        this.presupuesto = null;
        this.materialUtilizado = material;
        
        synchronized(this){
            this.total= total;
            notifyAll();
        }
    }
    
    public Albaran(String concepto, String provincia, String poblacion, int cp ,
            String calle, String numero, String escalera, int piso, String puerta,
            Map material, String observaciones, float total, Date fch_Creacion, Date fch_Entrega, Cliente cliente, GestoSAT gestoSAT) {
        super(observaciones, fch_Creacion, cliente, gestoSAT);
        
        this.concepto = concepto;
        this.provincia = provincia;
        this.poblacion = poblacion;
        this.cp = Math.abs(cp);
        this.calle = calle;
        this.numero = numero;
        this.escalera = escalera;
        if(piso > 0)
            this.piso = piso;
        else
            this.piso = 0;
        this.puerta = puerta;
        this.factura = null;
        this.presupuesto = null;
        this.materialUtilizado = material;
        this.fchEntrega = fch_Entrega;
        
        synchronized(this){
            this.total= total;
            notifyAll();
        }
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
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
        this.piso = piso;
    }

    public String getPuerta() {
        return puerta;
    }

    public void setPuerta(String puerta) {
        this.puerta = puerta;
    }

    public synchronized float getTotal() {
        return total;
    }

    public synchronized void setTotal(float total) {
        this.total = total;
    }

    public Presupuesto getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Map getMaterialUtilizado() {
        return materialUtilizado;
    }

    public void setMaterialUtilizado(Map materialUtilizado) {
        Iterator itMaterial = materialUtilizado.entrySet().iterator();
            
            float precioManoObra = 0;
            float precioVenta = 0;
            
            if(itMaterial.hasNext()){
                Map.Entry auxMaterial = (Map.Entry)itMaterial.next();
                if(!auxMaterial.getValue().getClass().getName().equals("GestoSAT.MaterialTrabajos")){
                    Map stockGestor = this.getGestoSAT().getStock();
                    Stock auxMat = (Stock)stockGestor.get(Integer.parseInt(auxMaterial.getKey().toString()));
                    precioVenta+= auxMat.getPrecioUnidad()*Float.parseFloat(auxMaterial.getValue().toString());
                    this.materialUtilizado.clear();
                    this.materialUtilizado.put(Integer.parseInt(auxMaterial.getKey().toString()),new MaterialTrabajos(auxMat,Float.parseFloat(auxMaterial.getValue().toString()))); // Stock, Float
                    while(itMaterial.hasNext()){
                        auxMat = (Stock)stockGestor.get(Integer.parseInt(auxMaterial.getKey().toString()));
                        precioVenta+= Math.abs(auxMat.getPrecioUnidad()*Float.parseFloat(auxMaterial.getValue().toString()));
                        this.materialUtilizado.put(Integer.parseInt(auxMaterial.getKey().toString()),new MaterialTrabajos(auxMat,Float.parseFloat(auxMaterial.getValue().toString()))); // Stock, Float
                    }
                    Iterator itEmpleados = this.materialUtilizado.entrySet().iterator();
                    while(itEmpleados.hasNext()){
                        Map.Entry auxEmpleado = (Map.Entry)itEmpleados.next();
                        precioManoObra+= Math.abs(((Trabajo)auxEmpleado.getValue()).getEmpleado().getPrecioHora()*((Trabajo)auxEmpleado.getValue()).getHoras());
                    }
                }else{
                    this.materialUtilizado = materialUtilizado;
                    
                    precioVenta += Math.abs(((Stock)auxMaterial.getValue()).getPrecioUnidad()*((Stock)auxMaterial.getValue()).getCantidad());
                    
                    while(itMaterial.hasNext()){
                        auxMaterial = (Map.Entry)itMaterial.next();
                        precioVenta += Math.abs(((Stock)auxMaterial.getValue()).getPrecioUnidad()*((Stock)auxMaterial.getValue()).getCantidad());
                    }
                    
                    Iterator itEmpleados = this.trabajoRealizado.entrySet().iterator();
                    while(itEmpleados.hasNext()){
                        Map.Entry auxEmpleado = (Map.Entry)itEmpleados.next();
                        precioManoObra+= Math.abs(((Trabajo)auxEmpleado.getValue()).getEmpleado().getPrecioHora()*((Trabajo)auxEmpleado.getValue()).getHoras());
                    }
                }
            }else{
                this.materialUtilizado.clear();
                
                Iterator itEmpleados = this.trabajoRealizado.entrySet().iterator();
                
                while(itEmpleados.hasNext()){
                    Map.Entry auxEmpleado = (Map.Entry)itEmpleados.next();
                    precioManoObra+= Math.abs(((Trabajo)auxEmpleado.getValue()).getEmpleado().getPrecioHora()*((Trabajo)auxEmpleado.getValue()).getHoras());
                }
            }
            
            synchronized(this){
                this.total= (precioVenta+precioManoObra)*(this.getGestoSAT().getIva()/(float)100+1);
                notifyAll();
            }
    }

    public Map getTrabajoRealizado() {
        return trabajoRealizado;
    }

    public void setTrabajoRealizado(Map trabajoRealizado) {
        Iterator itTrabajo = trabajoRealizado.entrySet().iterator();
            
            float precioManoObra = 0;
            float precioVenta = 0;
            
            if(itTrabajo.hasNext()){
                Map.Entry auxTrabajo = (Map.Entry)itTrabajo.next();
                if(!auxTrabajo.getValue().getClass().getName().equals("GestoSAT.Trabajo")){
                    Gson gJsonTime = new Gson();
                    Map time;
                    time = (HashMap)gJsonTime.fromJson(auxTrabajo.getValue().toString(), HashMap.class);
                    Iterator itTime = time.entrySet().iterator();
                    
                    Empleado empleado = this.getGestoSAT().getEmpleado(Integer.parseInt(auxTrabajo.getKey().toString()));
                    float horas = 0;
                    String desc = "";
                    
                    while(itTime.hasNext()){
                        
                        Map.Entry auxTime = (Map.Entry)itTime.next();
                        if(!auxTime.getKey().toString().equals("desc")) // Hour or Minute
                            if(auxTime.getKey().toString().equals("m"))
                               horas+= Math.abs(Float.parseFloat(auxTime.getValue().toString())/60); // int minutos
                            else
                               horas+= Math.abs(Float.parseFloat(auxTime.getValue().toString())); // int Horas
                        else
                            desc =  auxTime.getValue().toString();
                    }
                    
                    this.trabajoRealizado.clear();
                    precioManoObra+= Math.abs(empleado.getPrecioHora()*horas);
                    this.trabajoRealizado.put(Integer.parseInt(auxTrabajo.getKey().toString()), new Trabajo(horas, desc, empleado));
                    
                    while(itTrabajo.hasNext()){
                        auxTrabajo = (Map.Entry)itTrabajo.next();
                        time = (HashMap)gJsonTime.fromJson(auxTrabajo.getValue().toString(), HashMap.class);
                        itTime = time.entrySet().iterator();

                        empleado = this.getGestoSAT().getEmpleado(Integer.parseInt(auxTrabajo.getKey().toString()));
                        horas = 0;
                        desc = "";

                        while(itTime.hasNext()){
                            Map.Entry auxTime = (Map.Entry)itTime.next();
                            if(!auxTime.getKey().toString().equals("desc")) // Hour or Minute
                                if(auxTime.getKey().toString().equals("m"))
                                   horas+= Math.abs(Float.parseFloat(auxTime.getValue().toString())/60); // int minutos
                                else
                                   horas+= Math.abs(Float.parseFloat(auxTime.getValue().toString())); // int Horas
                            else
                                desc =  auxTime.getValue().toString();
                        }
                        precioManoObra+= Math.abs(empleado.getPrecioHora()*horas);
                        this.trabajoRealizado.put(Integer.parseInt(auxTrabajo.getKey().toString()), new Trabajo(horas, desc, empleado));
                    
                        Iterator itMaterial = this.materialUtilizado.entrySet().iterator();
                        while(itMaterial.hasNext()){
                            Map.Entry auxMaterial = (Map.Entry)itMaterial.next();
                            precioVenta += Math.abs(((Stock)auxMaterial.getValue()).getPrecioUnidad()*((Stock)auxMaterial.getValue()).getCantidad());
                        }
                    }
                }else{
                    this.trabajoRealizado = trabajoRealizado;
                    precioManoObra+= Math.abs(((Trabajo)auxTrabajo.getValue()).getEmpleado().getPrecioHora()*((Trabajo)auxTrabajo.getValue()).getHoras());
                    while(itTrabajo.hasNext()){
                        auxTrabajo = (Map.Entry)itTrabajo.next();
                        precioManoObra+= Math.abs(((Trabajo)auxTrabajo.getValue()).getEmpleado().getPrecioHora()*((Trabajo)auxTrabajo.getValue()).getHoras());
                    }
                    Iterator itMaterial = this.materialUtilizado.entrySet().iterator();
                        while(itMaterial.hasNext()){
                            Map.Entry auxMaterial = (Map.Entry)itMaterial.next();
                            precioVenta += Math.abs(((Stock)auxMaterial.getValue()).getPrecioUnidad()*((Stock)auxMaterial.getValue()).getCantidad());
                    }
                }
            }else{
                this.trabajoRealizado.clear();
                Iterator itMaterial = this.materialUtilizado.entrySet().iterator();
                while(itMaterial.hasNext()){
                    Map.Entry auxMaterial = (Map.Entry)itMaterial.next();
                    precioVenta += Math.abs(((Stock)auxMaterial.getValue()).getPrecioUnidad()*((Stock)auxMaterial.getValue()).getCantidad());
                }
            }
                // Calcular total
            synchronized(this){
                this.total= (precioVenta+precioManoObra)*(this.getGestoSAT().getIva()/(float)100+1);
                notifyAll();
            }
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Date getFchEntrega() {
        return fchEntrega;
    }
  
    public void setTrabajo_N_Material(Map trabajos, Map materiales){
            float precioManoObra = 0;
            float precioVenta = 0;
            
            Iterator itMaterial = materiales.entrySet().iterator();
            if(itMaterial.hasNext()){
                Map.Entry auxMaterial = (Map.Entry)itMaterial.next();
                
                if(!auxMaterial.getValue().getClass().getName().equals("GestoSAT.MaterialTrabajos")){
                    Map stockGestor = this.getGestoSAT().getStock();
                    Stock auxMat = (Stock)stockGestor.get(Integer.parseInt(auxMaterial.getKey().toString()));
                    
                    precioVenta+= Math.abs(auxMat.getPrecioUnidad()*Float.parseFloat(auxMaterial.getValue().toString()));
                    
                    this.materialUtilizado.clear();
                    this.materialUtilizado.put(Integer.parseInt(auxMaterial.getKey().toString()),new MaterialTrabajos(auxMat,Float.parseFloat(auxMaterial.getValue().toString()))); // Stock, Float
                    while(itMaterial.hasNext()){
                        auxMat = (Stock)stockGestor.get(Integer.parseInt(auxMaterial.getKey().toString()));
                        
                        precioVenta+= Math.abs(auxMat.getPrecioUnidad()*Float.parseFloat(auxMaterial.getValue().toString()));
                    
                        this.materialUtilizado.put(Integer.parseInt(auxMaterial.getKey().toString()),new MaterialTrabajos(auxMat,Float.parseFloat(auxMaterial.getValue().toString()))); // Stock, Float
                    }
                }else{
                    this.materialUtilizado = materiales;
                    
                    precioVenta += ((Stock)auxMaterial.getValue()).getPrecioUnidad()*((Stock)auxMaterial.getValue()).getCantidad();
                    
                    while(itMaterial.hasNext()){
                        auxMaterial = (Map.Entry)itMaterial.next();
                        precioVenta += Math.abs(((Stock)auxMaterial.getValue()).getPrecioUnidad()*((Stock)auxMaterial.getValue()).getCantidad());
                    }
                }
            }else{
                this.materialUtilizado.clear();
            }
            
            Iterator itTrabajo = trabajos.entrySet().iterator();
            
            if(itTrabajo.hasNext()){
                Map.Entry auxTrabajo = (Map.Entry)itTrabajo.next();
                if(!auxTrabajo.getValue().getClass().getName().equals("GestoSAT.Trabajo")){
                    Gson gJsonTime = new Gson();
                    Map time;
                    time = (HashMap)gJsonTime.fromJson(auxTrabajo.getValue().toString(), HashMap.class);
                    Iterator itTime = time.entrySet().iterator();
                    
                    Empleado empleado = this.getGestoSAT().getEmpleado(Integer.parseInt(auxTrabajo.getKey().toString()));
                    float horas = 0;
                    String desc = "";
                    
                    while(itTime.hasNext()){
                        Map.Entry auxTime = (Map.Entry)itTime.next();
                        if(!auxTime.getKey().toString().equals("desc")) // Hour or Minute
                            if(auxTime.getKey().toString().equals("m"))
                               horas+= Math.abs(Float.parseFloat(auxTime.getValue().toString())/60); // int minutos
                            else
                               horas+= Math.abs(Float.parseFloat(auxTime.getValue().toString())); // int Horas
                        else
                            desc =  auxTime.getValue().toString();
                    }
                    
                    precioManoObra+= empleado.getPrecioHora()*horas;

                    this.trabajoRealizado.clear();
                    this.trabajoRealizado.put(Integer.parseInt(auxTrabajo.getKey().toString()), new Trabajo(horas, desc, empleado));
                    
                    while(itTrabajo.hasNext()){
                        auxTrabajo = (Map.Entry)itTrabajo.next();
                        time = (HashMap)gJsonTime.fromJson(auxTrabajo.getValue().toString(), HashMap.class);
                        itTime = time.entrySet().iterator();

                        empleado = this.getGestoSAT().getEmpleado(Integer.parseInt(auxTrabajo.getKey().toString()));
                        horas = 0;
                        desc = "";

                        while(itTime.hasNext()){
                            Map.Entry auxTime = (Map.Entry)itTime.next();
                            if(!auxTime.getKey().toString().equals("desc")) // Hour or Minute
                                if(auxTime.getKey().toString().equals("m"))
                                   horas+= Math.abs(Float.parseFloat(auxTime.getValue().toString())/60); // int minutos
                                else
                                   horas+= Math.abs(Float.parseFloat(auxTime.getValue().toString())); // int Horas
                            else
                                desc =  auxTime.getValue().toString();
                        }
                        
                        precioManoObra+= empleado.getPrecioHora()*horas;
                        
                        this.trabajoRealizado.put(Integer.parseInt(auxTrabajo.getKey().toString()), new Trabajo(horas, desc, empleado));
                    }
                }else{
                    this.trabajoRealizado = trabajos;
                    precioManoObra+= ((Trabajo)auxTrabajo.getValue()).getEmpleado().getPrecioHora()*((Trabajo)auxTrabajo.getValue()).getHoras();
                    while(itTrabajo.hasNext()){
                        auxTrabajo = (Map.Entry)itTrabajo.next();
                        precioManoObra+= Math.abs(((Trabajo)auxTrabajo.getValue()).getEmpleado().getPrecioHora()*((Trabajo)auxTrabajo.getValue()).getHoras());
                    }
                }
            }else{
                this.trabajoRealizado.clear();
            }
            
            synchronized(this){
                this.total= (precioVenta+precioManoObra)*(this.getGestoSAT().getIva()/(float)100+1);
                notifyAll();
            }
        }
    
        public void actualizarVenta(String concepto, String provincia, String poblacion, int cp ,
            String calle, String numero, String escalera, int piso, String puerta,
            Map material, String observaciones, float total) {
        
            this.setObservaciones(observaciones);
            this.concepto = concepto;
            this.provincia = provincia;
            this.poblacion = poblacion;
            this.cp = Math.abs(cp);
            this.calle = calle;
            this.numero = numero;
            this.escalera = escalera;
            if(piso > 0)
                this.piso = piso;
            else
                this.piso = 0;
            this.puerta = puerta;
            this.materialUtilizado = material;

            synchronized(this){
                this.total= total;
                notifyAll();
            }
        }
        
        public void actualizarAlbaran(String concepto, String provincia, String poblacion, int cp ,
            String calle, String numero, String escalera, int piso, String puerta,
            Map material, Map trabajos, String observaciones, float total) {
        
            this.setObservaciones(observaciones);
            this.concepto = concepto;
            this.provincia = provincia;
            this.poblacion = poblacion;
            this.cp = Math.abs(cp);
            this.calle = calle;
            this.numero = numero;
            this.escalera = escalera;
            if(piso > 0)
                this.piso = piso;
            else
                this.piso = 0;
            this.puerta = puerta;
            this.materialUtilizado = material;

            synchronized(this){
                this.total= total;
                notifyAll();
            }
        }
        
        
        public boolean actualizar(int id, String concepto, String provincia, 
                String poblacion, int cp ,String calle, String numero,
                String escalera, int piso, String puerta,Map material,
                Map trabajos, String observaciones) {
        
            this.setObservaciones(observaciones);
            this.concepto = concepto;
            this.provincia = provincia;
            this.poblacion = poblacion;
            this.cp = cp;
            this.calle = calle;
            this.numero = numero;
            this.escalera = escalera;
            if(piso > 0)
                this.piso = piso;
            else
                this.piso = 0;
            this.puerta = puerta;
            
            this.setTrabajo_N_Material(trabajos, material);
            
            return this.getGestoSAT().guardarAlbaran(id, this);
        }
        
        public Map cargarFactura(int idAlbaran){    
            Map mapFactura = this.getGestoSAT().recuperarFactura(idAlbaran);
                Iterator it  = mapFactura.entrySet().iterator();
                if(it.hasNext()){
                    Map.Entry aux = (Map.Entry)it.next();
                    this.factura=(Factura)aux.getValue();
                    mapFactura.putAll(this.factura.cargarRecibo(Integer.parseInt(aux.getKey().toString().split(";")[0])));
                }
            return mapFactura;
        }
        
        public String albaran2PDF(int id){
            try {
                String arch = "Albaran"+(new Date()).getTime()+".pdf";
                Document doc = new Document();
                PdfWriter.getInstance(doc,new FileOutputStream("/TomEE/webapps/ROOT/descargables/"+arch));
                DecimalFormat df = new DecimalFormat("0.00");
                
                doc.open();
                File f = new File("logo");
                if(f.exists()){
                    Image img = Image.getInstance("logo");
                    img.setAbsolutePosition(50, 735);
                    img.scaleAbsolute(50, 50);
                    doc.add(img);
                    Paragraph p = new Paragraph(" ");
                    p.setLeading((float)75);
                    doc.add(p);
                }               
                doc.add(new Paragraph("ALBARÁN"));
                doc.add(new Paragraph(" "));
                doc.add(new Paragraph("Datos empresa"));
                Oficina ofi = this.getGestoSAT().getEmpleado().getOficina();
                doc.add(new Paragraph(ofi.getProvincia()+" "+ofi.getPoblacion()+" "+ofi.getCalle()
                +" "+ofi.getNumero()));
                doc.add(new Paragraph(ofi.getNombre()));
                doc.add(new Paragraph("NIF: "+ofi.getNif()));
                doc.add(new Paragraph("Tlf: "+ofi.getTlfFijo()+" "+ofi.getTlfMovil()));
                doc.add(new Paragraph("Fax: "+ofi.getFax()));
                doc.add(new Paragraph(" "));
                doc.add(new Paragraph(" "));
                doc.add(new Paragraph("Datos de interés"));
                doc.add(new Paragraph("Cliente: "+this.getCliente().getNombre()+" "+this.getCliente().getApellidos()));
                doc.add(new Paragraph("Concepto: "+this.concepto));
                doc.add(new Paragraph(" "));
                doc.add(new Paragraph("Lugar de entrga:"+this.provincia+" "+this.poblacion+" "+this.calle+" "+this.escalera+" "
                +this.piso+" "+this.puerta));

                doc.add(new Paragraph(" "));
                
                float auxPrecio=0;
                PdfPTable table = new PdfPTable(5);
                
                table.addCell("#");
                table.addCell("Nombre");
                table.addCell("Precio Ud / H");
                table.addCell("Uds / H");
                table.addCell("Total");

                Iterator itTabla = this.materialUtilizado.entrySet().iterator();
                while(itTabla.hasNext()){
                    Map.Entry aux = (Map.Entry)itTabla.next();
                    table.addCell(aux.getKey().toString());
                    table.addCell(((MaterialTrabajos)aux.getValue()).getStock().getNombre());
                    table.addCell(((MaterialTrabajos)aux.getValue()).getStock().getPrecioUnidad()+"");
                    table.addCell(((MaterialTrabajos)aux.getValue()).getCantidad()+"");
                    auxPrecio+=((MaterialTrabajos)aux.getValue()).getCantidad()*((MaterialTrabajos)aux.getValue()).getStock().getPrecioUnidad();
                    table.addCell((((MaterialTrabajos)aux.getValue()).getCantidad()*((MaterialTrabajos)aux.getValue()).getStock().getPrecioUnidad())+"");
                }
                
                itTabla = this.trabajoRealizado.entrySet().iterator();
                while(itTabla.hasNext()){
                    Map.Entry aux = (Map.Entry)itTabla.next();
                    table.addCell("*");
                    Trabajo tra = (Trabajo)aux.getValue();
                    table.addCell(tra.getEmpleado().getNombre()+" "+tra.getEmpleado().getApellidos());
                    table.addCell(tra.getEmpleado().getPrecioHora()+"");
                    table.addCell(tra.getHoras()+"");
                    auxPrecio += tra.getEmpleado().getPrecioHora()*tra.getHoras();
                    table.addCell((tra.getEmpleado().getPrecioHora()*tra.getHoras())+"");
                }
                doc.add(table);
                doc.add(new Paragraph("I.V.A: "+this.getGestoSAT().getIva()+"%"));
                doc.add(new Paragraph("Total I.V.A: "+df.format((this.getGestoSAT().getIva()/(float)100)*auxPrecio)));
                doc.add(new Paragraph("Total: "+df.format(((this.getGestoSAT().getIva()/(float)100)+1)*auxPrecio)));
                
                doc.close();
                if(this.fchEntrega == null){
                    this.fchEntrega = new Date();
                    if(this.getGestoSAT().entregaAlbaran(id, this))
                        return "descargables/"+arch;
                    else
                        return "";
                }else
                    return "descargables/"+arch;
                
                
            } catch (IOException ex) {
                Logger.getLogger(Entrada.class.getName()).log(Level.SEVERE, null, ex);
                return "";
            } catch (BadElementException ex) {
                Logger.getLogger(Entrada.class.getName()).log(Level.SEVERE, null, ex);
                return "";
            } catch (DocumentException ex) {
                Logger.getLogger(Entrada.class.getName()).log(Level.SEVERE, null, ex);
                return "";
            }
        }
        
        public String albaran2XLSX(){
            String archivo = "Albaran"+(new Date()).getTime()+".xlsx";
            try {
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("Albarán");
                Row row;
                Cell cell;
                
                Oficina o = this.getGestoSAT().getEmpleado().getOficina();
                
                row =sheet.createRow((short) 0);
                cell = row.createCell(0);
                cell.setCellValue("Empresa");
                cell = row.createCell(1);
                cell.setCellValue(o.getNombre());
                row =sheet.createRow((short) 2);
                cell = row.createCell(0);
                cell.setCellValue("Dirección");
                row =sheet.createRow((short) 3);
                cell = row.createCell(0);
                cell.setCellValue(o.getProvincia());
                cell = row.createCell(1);
                cell.setCellValue(o.getPoblacion());
                cell = row.createCell(2);
                cell.setCellValue(o.getCalle());
                cell = row.createCell(3);
                cell.setCellValue(o.getNumero());
                
                row =sheet.createRow((short) 5);
                cell = row.createCell(0);
                cell.setCellValue("Datos cliente");
                row =sheet.createRow((short) 7);
                cell = row.createCell(0);
                cell.setCellValue("Nombre");
                cell = row.createCell(1);
                cell.setCellValue(this.getCliente().getNombre());
                
                row =sheet.createRow((short) 9);
                cell = row.createCell(0);
                cell.setCellValue("Datos Albarán");
                
                row =sheet.createRow((short) 10);
                cell = row.createCell(0);
                cell.setCellValue("Concepto");
                cell = row.createCell(1);
                cell.setCellValue(this.concepto);
                row =sheet.createRow((short) 11);
                cell = row.createCell(0);
                cell.setCellValue("Observaciones");
                cell = row.createCell(1);
                cell.setCellValue(this.getObservaciones());
                
                row =sheet.createRow((short) 13);
                cell = row.createCell(0);
                cell.setCellValue("Provincia");
                cell = row.createCell(1);
                cell.setCellValue("Población");
                cell = row.createCell(2);
                cell.setCellValue("Calle");
                cell = row.createCell(3);
                cell.setCellValue("Número");
                cell = row.createCell(4);
                cell.setCellValue("Escalera");
                cell = row.createCell(5);
                cell.setCellValue("Piso");
                cell = row.createCell(6);
                cell.setCellValue("Puerta");
                
                row =sheet.createRow((short) 14);
                cell = row.createCell(0);
                cell.setCellValue(this.provincia);
                cell = row.createCell(1);
                cell.setCellValue(this.poblacion);
                cell = row.createCell(2);
                cell.setCellValue(this.calle);
                cell = row.createCell(3);
                cell.setCellValue(this.numero);
                cell = row.createCell(4);
                cell.setCellValue(this.escalera);
                cell = row.createCell(5);
                cell.setCellValue(this.piso);
                cell = row.createCell(6);
                cell.setCellValue(this.puerta);
                
                float total=0;
                DecimalFormat df = new DecimalFormat("0.00");
                    
                if(!this.trabajoRealizado.isEmpty()){
                    row = sheet.createRow((short) 16);
                    cell = row.createCell(0);
                    cell.setCellValue("Trabajos presupuestados");
                    
                    row = sheet.createRow((short) 18);
                    cell = row.createCell(0);
                    cell.setCellValue("Nombre");
                    cell = row.createCell(1);
                    cell.setCellValue("Precio h");
                    cell = row.createCell(2);
                    cell.setCellValue("Horas");
                    cell = row.createCell(3);
                    cell.setCellValue("Total");
                    
                    Iterator itTrabajos = this.trabajoRealizado.entrySet().iterator();
                    for(int index = 19;itTrabajos.hasNext();index++){
                        Map.Entry aux = (Map.Entry)itTrabajos.next();
                        Trabajo trabajo = (Trabajo)aux.getValue();
                        row = sheet.createRow((short) index);
                        cell = row.createCell(0);
                        cell.setCellValue(trabajo.getEmpleado().getNombre()+" "+trabajo.getEmpleado().getApellidos());
                        cell = row.createCell(1);
                        cell.setCellValue(trabajo.getEmpleado().getPrecioHora());
                        cell = row.createCell(2);
                        cell.setCellValue(trabajo.getHoras());
                        cell = row.createCell(3);
                        cell.setCellValue(df.format(trabajo.getEmpleado().getPrecioHora()*trabajo.getHoras()));
                        total+=trabajo.getEmpleado().getPrecioHora()*trabajo.getHoras();
                    }
                }
                int valueIndex = row.getRowNum();
                
                if(!this.materialUtilizado.isEmpty()){
                    valueIndex++;valueIndex++;
                    row = sheet.createRow((short) valueIndex);
                    cell = row.createCell(0);
                    cell.setCellValue("Materiales presupuestados");
                    
                    valueIndex++;valueIndex++;
                    row = sheet.createRow((short) valueIndex);
                    cell = row.createCell(0);
                    cell.setCellValue("#");
                    cell = row.createCell(1);
                    cell.setCellValue("Nombre");
                    cell = row.createCell(2);
                    cell.setCellValue("Precio Ud");
                    cell = row.createCell(3);
                    cell.setCellValue("Cantidad");
                    cell = row.createCell(4);
                    cell.setCellValue("Total");
                    
                    Iterator itMateriales = this.materialUtilizado.entrySet().iterator();
                    valueIndex++;
                    for(int index=valueIndex;itMateriales.hasNext();index++){
                        Map.Entry aux = (Map.Entry)itMateriales.next();
                        MaterialTrabajos material = (MaterialTrabajos)aux.getValue();
                        row = sheet.createRow((short) index);
                        cell = row.createCell(0);
                        cell.setCellValue(aux.getKey().toString());
                        cell = row.createCell(1);
                        cell.setCellValue(material.getStock().getNombre());
                        cell = row.createCell(2);
                        cell.setCellValue(material.getStock().getPrecioUnidad());
                        cell = row.createCell(3);
                        cell.setCellValue(material.getCantidad());
                        cell = row.createCell(4);
                        cell.setCellValue(df.format(material.getStock().getPrecioUnidad()*material.getCantidad()));
                        total+=material.getStock().getPrecioUnidad()*material.getCantidad();
                        valueIndex = index;
                    }
                }
                
                float iva = this.getGestoSAT().getIva()/(float)100;
                valueIndex++; valueIndex++;
                row = sheet.createRow((short) valueIndex);
                cell = row.createCell(0);
                cell.setCellValue("I.V.A");
                cell = row.createCell(1);
                cell.setCellValue(df.format(iva*total));
                valueIndex++;
                row = sheet.createRow((short) valueIndex);
                cell = row.createCell(0);
                cell.setCellValue("Total sin I.V.A");
                cell = row.createCell(1);
                cell.setCellValue(df.format(total));
                valueIndex++;
                row = sheet.createRow((short) valueIndex+1);
                cell = row.createCell(0);
                cell.setCellValue("Total");
                cell = row.createCell(1);
                cell.setCellValue(df.format(this.total));
                
                FileOutputStream fileOut = new FileOutputStream("/TomEE/webapps/ROOT/descargables/"+archivo);
                wb.write(fileOut);
                fileOut.close();
                // Devolver Archivo
                return "descargables/"+archivo;
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Entrada.class.getName()).log(Level.SEVERE, null, ex);
                return "";
            } catch (IOException ex) {
                Logger.getLogger(Entrada.class.getName()).log(Level.SEVERE, null, ex);
                return "";
            }
        }
}