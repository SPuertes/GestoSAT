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

public class Presupuesto extends Documento {
	private String concepto;
	private Date validez;
	private boolean aceptado = false;
	private String formaPago;
	private float adelanto;
	private int plazo; // Duración de los trabajos
	private String condiciones;
	private String seguro;
	private String garantia;
        private float total;
	private Map trabajoPresupuestado = new HashMap(); // Map de trabajo
        private Map materialPresupuestado = new HashMap(); // Map del material presupost
	private Entrada entrada;
	private Albaran albaran;
       
       public Presupuesto(String concepto, Date validez, boolean aceptado, 
                String formaPago, float adelanto, int plazo, String condiciones,
                String seguro, String garantia, Entrada entrada, 
                String observaciones, Map empleados, Map material,GestoSAT gestoSAT){
            super(observaciones,entrada.getCliente(),gestoSAT);
            this.concepto = concepto;
            this.validez = validez;
            this.aceptado = aceptado;
            this.formaPago = formaPago;
            this.adelanto = Math.abs(adelanto);
            this.plazo = Math.abs(plazo);
            this.condiciones = condiciones;
            this.seguro = seguro;
            this.garantia = garantia;
            this.entrada = entrada;
            
            entrada.setPresupuesto(this);
            
                Gson gJsonTime = new Gson();
                Map time; // Parse JSON
                Iterator itEmp = empleados.entrySet().iterator();
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
                    precioManoObra+= empleado.getPrecioHora()*horas;
                    
                    trabajoPresupuestado.put(auxEmp.getKey(), new Trabajo(horas,desc,empleado)); // Float, String, Empleado
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
                    
                    materialPresupuestado.put(auxStock.getKey(),new MaterialTrabajos(auxMat,Float.parseFloat(auxStock.getValue().toString()))); // Stock, Float
                }
            
                // Calcular total
                synchronized(this){
                    this.total= (precioVenta+precioManoObra)*(gestoSAT.getIva()/(float)100+1);
                    notifyAll();
                }
        }
       
       public Presupuesto(Date creacion, String concepto, Date validez, boolean aceptado, 
                String formaPago, float adelanto, int plazo, String condiciones,
                String seguro, String garantia, Entrada entrada, 
                String observaciones, Map empleados, Map material,GestoSAT gestoSAT, float total){
            super(observaciones,creacion,entrada.getCliente(),gestoSAT);
            this.concepto = concepto;
            this.validez = validez;
            this.aceptado = aceptado;
            this.formaPago = formaPago;
            this.adelanto = Math.abs(adelanto);
            this.plazo = Math.abs(plazo);
            this.condiciones = condiciones;
            this.seguro = seguro;
            this.garantia = garantia;
            this.entrada = entrada;
            this.materialPresupuestado = material;
            this.trabajoPresupuestado = empleados;
            this.total= total;
        }
       
       synchronized public float getTotal(){
           return this.total;
       }
       
        public String getConcepto() {
            return concepto;
        }

        public void setConcepto(String concepto) {
            this.concepto = concepto;
        }

        public Date getValidez() {
            return validez;
        }

        public void setValidez(Date validez) {
            this.validez = validez;
        }

        public boolean isAceptado() {
            return aceptado;
        }

        public void setAceptado(boolean aceptado) {
            this.aceptado = aceptado;
        }

        public String getFormaPago() {
            return formaPago;
        }

        public void setFormaPago(String formaPago) {
            this.formaPago = formaPago;
        }

        public float getAdelanto() {
            return adelanto;
        }

        public void setAdelanto(float adelanto) {
            this.adelanto = Math.abs(adelanto);
        }

        public int getPlazo() {
            return plazo;
        }

        public void setPlazo(int plazo) {
            this.plazo = Math.abs(plazo);
        }

        public String getCondiciones() {
            return condiciones;
        }

        public void setCondiciones(String condiciones) {
            this.condiciones = condiciones;
        }

        public String getSeguro() {
            return seguro;
        }

        public void setSeguro(String seguro) {
            this.seguro = seguro;
        }

        public String getGarantia() {
            return garantia;
        }

        public void setGarantia(String garantia) {
            this.garantia = garantia;
        }

        public void setTrabajo(Map trabajoPresupuestado) {
            Iterator itTrabajo = trabajoPresupuestado.entrySet().iterator();
            
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
                    
                    this.trabajoPresupuestado.clear();
                    precioManoObra+= empleado.getPrecioHora()*horas;
                    this.trabajoPresupuestado.put(Integer.parseInt(auxTrabajo.getKey().toString()), new Trabajo(horas, desc, empleado));
                    
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
                        this.trabajoPresupuestado.put(Integer.parseInt(auxTrabajo.getKey().toString()), new Trabajo(horas, desc, empleado));
                    
                        Iterator itMaterial = this.materialPresupuestado.entrySet().iterator();
                        while(itMaterial.hasNext()){
                            Map.Entry auxMaterial = (Map.Entry)itMaterial.next();
                            precioVenta += Math.abs(((Stock)auxMaterial.getValue()).getPrecioUnidad()*((Stock)auxMaterial.getValue()).getCantidad());
                        }
                    }
                }else{
                    this.trabajoPresupuestado = trabajoPresupuestado;
                    precioManoObra+= ((Trabajo)auxTrabajo.getValue()).getEmpleado().getPrecioHora()*((Trabajo)auxTrabajo.getValue()).getHoras();
                    while(itTrabajo.hasNext()){
                        auxTrabajo = (Map.Entry)itTrabajo.next();
                        precioManoObra+= Math.abs(((Trabajo)auxTrabajo.getValue()).getEmpleado().getPrecioHora()*((Trabajo)auxTrabajo.getValue()).getHoras());
                    }
                    Iterator itMaterial = this.materialPresupuestado.entrySet().iterator();
                        while(itMaterial.hasNext()){
                            Map.Entry auxMaterial = (Map.Entry)itMaterial.next();
                            precioVenta += Math.abs(((Stock)auxMaterial.getValue()).getPrecioUnidad()*((Stock)auxMaterial.getValue()).getCantidad());
                    }
                }
            }else{
                this.trabajoPresupuestado.clear();
                Iterator itMaterial = this.materialPresupuestado.entrySet().iterator();
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
        
        public Map getTrabajo() {
            return this.trabajoPresupuestado;
        }

        public Map getMaterial(){
            return materialPresupuestado;
        }

        public void setMaterial(Map materialPresupuestado) {
            Iterator itMaterial = materialPresupuestado.entrySet().iterator();
            
            float precioManoObra = 0;
            float precioVenta = 0;
            
            if(itMaterial.hasNext()){
                Map.Entry auxMaterial = (Map.Entry)itMaterial.next();
                if(!auxMaterial.getValue().getClass().getName().equals("GestoSAT.MaterialTrabajos")){
                    Map stockGestor = this.getGestoSAT().getStock();
                    Stock auxMat = (Stock)stockGestor.get(Integer.parseInt(auxMaterial.getKey().toString()));
                    precioVenta+= Math.abs(auxMat.getPrecioUnidad()*Float.parseFloat(auxMaterial.getValue().toString()));
                    this.materialPresupuestado.clear();
                    this.materialPresupuestado.put(Integer.parseInt(auxMaterial.getKey().toString()),new MaterialTrabajos(auxMat,Float.parseFloat(auxMaterial.getValue().toString()))); // Stock, Float
                    while(itMaterial.hasNext()){
                        auxMat = (Stock)stockGestor.get(Integer.parseInt(auxMaterial.getKey().toString()));
                        precioVenta+= Math.abs(auxMat.getPrecioUnidad()*Float.parseFloat(auxMaterial.getValue().toString()));
                        this.materialPresupuestado.put(Integer.parseInt(auxMaterial.getKey().toString()),new MaterialTrabajos(auxMat,Float.parseFloat(auxMaterial.getValue().toString()))); // Stock, Float
                    }
                    Iterator itEmpleados = this.trabajoPresupuestado.entrySet().iterator();
                    while(itEmpleados.hasNext()){
                        Map.Entry auxEmpleado = (Map.Entry)itEmpleados.next();
                        precioManoObra+= Math.abs(((Trabajo)auxEmpleado.getValue()).getEmpleado().getPrecioHora()*((Trabajo)auxEmpleado.getValue()).getHoras());
                    }
                }else{
                    this.materialPresupuestado = materialPresupuestado;
                    
                    precioVenta += Math.abs(((Stock)auxMaterial.getValue()).getPrecioUnidad()*((Stock)auxMaterial.getValue()).getCantidad());
                    
                    while(itMaterial.hasNext()){
                        auxMaterial = (Map.Entry)itMaterial.next();
                        precioVenta += Math.abs(((Stock)auxMaterial.getValue()).getPrecioUnidad()*((Stock)auxMaterial.getValue()).getCantidad());
                    }
                    
                    Iterator itEmpleados = this.trabajoPresupuestado.entrySet().iterator();
                    while(itEmpleados.hasNext()){
                        Map.Entry auxEmpleado = (Map.Entry)itEmpleados.next();
                        precioManoObra+= Math.abs(((Trabajo)auxEmpleado.getValue()).getEmpleado().getPrecioHora()*((Trabajo)auxEmpleado.getValue()).getHoras());
                    }
                }
            }else{
                this.materialPresupuestado.clear();
                
                Iterator itEmpleados = this.trabajoPresupuestado.entrySet().iterator();
                
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

        public Entrada getEntrada() {
            return entrada;
        }

        public void setEntrada(Entrada entrada) {
            this.entrada = entrada;
        }

        public Albaran getAlbaran() {
            return albaran;
        }

        public void setAlbaran(Albaran albaran) {
            this.albaran = albaran;
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
                    this.materialPresupuestado.clear();
                    this.materialPresupuestado.put(Integer.parseInt(auxMaterial.getKey().toString()),new MaterialTrabajos(auxMat,Float.parseFloat(auxMaterial.getValue().toString()))); // Stock, Float
                    while(itMaterial.hasNext()){
                        auxMat = (Stock)stockGestor.get(Integer.parseInt(auxMaterial.getKey().toString()));
                        
                        precioVenta+= Math.abs(auxMat.getPrecioUnidad()*Float.parseFloat(auxMaterial.getValue().toString()));
                    
                        this.materialPresupuestado.put(Integer.parseInt(auxMaterial.getKey().toString()),new MaterialTrabajos(auxMat,Float.parseFloat(auxMaterial.getValue().toString()))); // Stock, Float
                    }
                }else{
                    this.materialPresupuestado = materiales;
                    precioVenta += Math.abs(((Stock)auxMaterial.getValue()).getPrecioUnidad()*((Stock)auxMaterial.getValue()).getCantidad());
                    while(itMaterial.hasNext()){
                        auxMaterial = (Map.Entry)itMaterial.next();
                        precioVenta += Math.abs(((Stock)auxMaterial.getValue()).getPrecioUnidad()*((Stock)auxMaterial.getValue()).getCantidad());
                    }
                }
            }else{
                this.materialPresupuestado.clear();
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

                    this.trabajoPresupuestado.clear();
                    this.trabajoPresupuestado.put(Integer.parseInt(auxTrabajo.getKey().toString()), new Trabajo(horas, desc, empleado));
                    
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
                        
                        this.trabajoPresupuestado.put(Integer.parseInt(auxTrabajo.getKey().toString()), new Trabajo(horas, desc, empleado));
                    }
                }else{
                    this.trabajoPresupuestado = trabajos;
                    precioManoObra+= ((Trabajo)auxTrabajo.getValue()).getEmpleado().getPrecioHora()*((Trabajo)auxTrabajo.getValue()).getHoras();
                    while(itTrabajo.hasNext()){
                        auxTrabajo = (Map.Entry)itTrabajo.next();
                        precioManoObra+= Math.abs(((Trabajo)auxTrabajo.getValue()).getEmpleado().getPrecioHora()*((Trabajo)auxTrabajo.getValue()).getHoras());
                    }
                }
            }else{
                this.trabajoPresupuestado.clear();
            }
            
            synchronized(this){
                this.total= (precioVenta+precioManoObra)*(this.getGestoSAT().getIva()/(float)100+1);
                notifyAll();
            }
        }
        
        public Map cargarAlbaran(int idPresupuesto){
            Map mapAlbaran = this.getGestoSAT().recuperarAlbaran(idPresupuesto);
            Iterator it  = mapAlbaran.entrySet().iterator();
            if(it.hasNext()){
                Map.Entry aux = (Map.Entry)it.next();
                this.albaran=(Albaran)aux.getValue();
                mapAlbaran.putAll(this.albaran.cargarFactura(Integer.parseInt(aux.getKey().toString().split(";")[0])));
            }
            return mapAlbaran;
        }
        
        public void actualizar(String concepto, Date validez, boolean aceptado, 
                String formaPago, float adelanto, int plazo, String condiciones,
                String seguro, String garantia, Entrada entrada, 
                String observaciones, Map empleados, Map material, float total){
            this.setObservaciones(observaciones);
            this.concepto = concepto;
            this.validez = validez;
            this.aceptado = aceptado;
            this.formaPago = formaPago;
            this.adelanto = Math.abs(adelanto);
            this.plazo = Math.abs(plazo);
            this.condiciones = condiciones;
            this.seguro = seguro;
            this.garantia = garantia;
            this.entrada = entrada;
            this.materialPresupuestado = material;
            this.trabajoPresupuestado = empleados;
            this.total= total;
        }
        
        public boolean actualizar(int id,String concepto, Date validez, boolean aceptado, 
                String formaPago, float adelanto, int plazo, String condiciones,
                String seguro, String garantia,String observaciones,
                Map empleados, Map material){
            this.setObservaciones(observaciones);
            this.concepto = concepto;
            this.validez = validez;
            this.aceptado = aceptado;
            this.formaPago = formaPago;
            this.adelanto = Math.abs(adelanto);
            this.plazo = Math.abs(plazo);
            this.condiciones = condiciones;
            this.seguro = seguro;
            this.garantia = garantia;
            this.setTrabajo_N_Material(empleados, material);
            
            return this.getGestoSAT().guardarPresupuesto(id, this);
        }
        
        public String presupuesto2PDF(){
            try {
                String arch = "Presupeusto"+(new Date()).getTime()+".pdf";
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
                doc.add(new Paragraph("PRESUPUESTO"));
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
                doc.add(new Paragraph("Fecha validez: "+this.validez));
                if(this.aceptado)
                    doc.add(new Paragraph("Aceptado: SI"));
                else
                    doc.add(new Paragraph("Aceptado: NO"));
                doc.add(new Paragraph("Adelanto: "+this.adelanto));
                doc.add(new Paragraph("Forma pago: "+this.formaPago));
                doc.add(new Paragraph("Tiempo estimado: "+this.plazo));
                doc.add(new Paragraph("Condiciones: "+this.condiciones));
                doc.add(new Paragraph("Seguro: "+this.seguro));
                doc.add(new Paragraph("Garantia: "+this.garantia));
                doc.add(new Paragraph(" "));
                
                float auxPrecio=0;
                PdfPTable table = new PdfPTable(5);
        
                table.addCell("#");
                table.addCell("Nombre");
                table.addCell("Precio Ud / H");
                table.addCell("Uds / H");
                table.addCell("Total");

                Iterator itTabla = this.materialPresupuestado.entrySet().iterator();
                while(itTabla.hasNext()){
                    Map.Entry aux = (Map.Entry)itTabla.next();
                    table.addCell(aux.getKey().toString());
                    table.addCell(((MaterialTrabajos)aux.getValue()).getStock().getNombre());
                    table.addCell(((MaterialTrabajos)aux.getValue()).getStock().getPrecioUnidad()+"");
                    table.addCell(((MaterialTrabajos)aux.getValue()).getCantidad()+"");
                    auxPrecio+=((MaterialTrabajos)aux.getValue()).getCantidad()*((MaterialTrabajos)aux.getValue()).getStock().getPrecioUnidad();
                    table.addCell((((MaterialTrabajos)aux.getValue()).getCantidad()*((MaterialTrabajos)aux.getValue()).getStock().getPrecioUnidad())+"");
                }
                
                itTabla = this.trabajoPresupuestado.entrySet().iterator();
                while(itTabla.hasNext()){
                    Map.Entry aux = (Map.Entry)itTabla.next();
                    table.addCell("*");
                    Trabajo tra = (Trabajo)aux.getValue();
                    table.addCell(tra.getEmpleado().getNombre()+" "+tra.getEmpleado().getApellidos());
                    table.addCell(tra.getEmpleado().getPrecioHora()+"");
                    table.addCell(tra.getHoras()+"");
                    auxPrecio+= tra.getEmpleado().getPrecioHora()*tra.getHoras();
                    table.addCell((tra.getEmpleado().getPrecioHora()*tra.getHoras())+"");
                }
                doc.add(table);
                doc.add(new Paragraph("I.V.A: "+this.getGestoSAT().getIva()+"%"));
                doc.add(new Paragraph("Total I.V.A: "+df.format((this.getGestoSAT().getIva()/(float)100)*auxPrecio)));
                doc.add(new Paragraph("Total: "+df.format((this.getGestoSAT().getIva()/(float)100+1)*auxPrecio)));
                
                doc.close();
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
        
        public String presupuesto2XLSX(){
            String archivo = "Presupuesto"+(new Date()).getTime()+".xlsx";
            try {
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("Presuepuesto");
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
                cell.setCellValue("Datos presupuesto");
                
                row =sheet.createRow((short) 10);
                cell = row.createCell(0);
                cell.setCellValue("Concepto");
                cell = row.createCell(1);
                cell.setCellValue(this.concepto);
                row =sheet.createRow((short) 11);
                cell = row.createCell(0);
                cell.setCellValue("Fecha de validez");
                cell = row.createCell(1);
                cell.setCellValue(this.validez.toString());
                row =sheet.createRow((short) 12);
                cell = row.createCell(0);
                cell.setCellValue("Aceptado");
                cell = row.createCell(1);
                if(this.aceptado)
                    cell.setCellValue("SI");
                else
                    cell.setCellValue("NO");
                
                row = sheet.createRow((short) 13);
                cell = row.createCell(0);
                cell.setCellValue("Forma de pago");
                cell = row.createCell(1);
                cell.setCellValue(this.formaPago);
                row = sheet.createRow((short) 14);
                cell = row.createCell(0);
                cell.setCellValue("Adelanto");
                cell = row.createCell(1);
                cell.setCellValue(this.adelanto);
                row = sheet.createRow((short) 15);
                cell = row.createCell(0);
                cell.setCellValue("Plazo de trabajo");
                cell = row.createCell(1);
                cell.setCellValue(this.plazo);
                row = sheet.createRow((short) 16);
                cell = row.createCell(0);
                cell.setCellValue("Condiciones");
                cell = row.createCell(1);
                cell.setCellValue(this.condiciones);
                row = sheet.createRow((short) 17);
                cell = row.createCell(0);
                cell.setCellValue("Seguro");
                cell = row.createCell(1);
                cell.setCellValue(this.seguro);
                row = sheet.createRow((short) 18);
                cell = row.createCell(0);
                cell.setCellValue("Garantia");
                cell = row.createCell(1);
                cell.setCellValue(this.garantia);
                
                float total=0;
                DecimalFormat df = new DecimalFormat("0.00");
                    
                if(!this.trabajoPresupuestado.isEmpty()){
                    row = sheet.createRow((short) 20);
                    cell = row.createCell(0);
                    cell.setCellValue("Trabajos presupuestados");
                    
                    row = sheet.createRow((short) 22);
                    cell = row.createCell(0);
                    cell.setCellValue("Nombre");
                    cell = row.createCell(1);
                    cell.setCellValue("Precio h");
                    cell = row.createCell(2);
                    cell.setCellValue("Horas");
                    cell = row.createCell(3);
                    cell.setCellValue("Total");
                    
                    Iterator itTrabajos = this.trabajoPresupuestado.entrySet().iterator();
                    for(int index = 23;itTrabajos.hasNext();index++){
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
                
                if(!this.materialPresupuestado.isEmpty()){
                    valueIndex++;valueIndex++;
                    row = sheet.createRow((short) valueIndex);
                    cell = row.createCell(0);
                    cell.setCellValue("Materiales presupuestados");
                    
                    valueIndex++;
                    row = sheet.createRow((short) valueIndex);
                    cell = row.createCell(0);
                    cell.setCellValue("Nombre");
                    cell = row.createCell(1);
                    cell.setCellValue("Precio Ud");
                    cell = row.createCell(2);
                    cell.setCellValue("Cantidad");
                    cell = row.createCell(3);
                    cell.setCellValue("Total");
                    
                    Iterator itMateriales = this.materialPresupuestado.entrySet().iterator();
                    valueIndex++;
                    for(int index=valueIndex;itMateriales.hasNext();index++){
                        Map.Entry aux = (Map.Entry)itMateriales.next();
                        MaterialTrabajos material = (MaterialTrabajos)aux.getValue();
                        row = sheet.createRow((short) index);
                        cell = row.createCell(0);
                        cell.setCellValue(material.getStock().getNombre());
                        cell = row.createCell(1);
                        cell.setCellValue(material.getStock().getPrecioUnidad());
                        cell = row.createCell(2);
                        cell.setCellValue(material.getCantidad());
                        cell = row.createCell(3);
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