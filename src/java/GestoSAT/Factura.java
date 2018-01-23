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
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Factura extends Documento {
	private String concepto;
	private String formaPago;
        private float total;
	private Recibo recibo;
	private Map albaran = new HashMap();

    public Factura(String concepto, String formaPago, Map albaranes, String observaciones, Cliente cliente, GestoSAT gestoSAT) {
        super(observaciones, cliente, gestoSAT);
        this.recibo = null;
        this.concepto = concepto;
        this.formaPago = formaPago;
        this.albaran = albaranes;
        
        synchronized(this){
            Iterator itAlbaran = albaranes.entrySet().iterator();
            while(itAlbaran.hasNext()){
                Map.Entry auxAlbaran = (Map.Entry)itAlbaran.next();
                ((Albaran)auxAlbaran.getValue()).setFactura(this);
                this.total +=((Albaran)auxAlbaran.getValue()).getTotal(); 
            }
        }
    }
    
    public Factura(String concepto, String formaPago, String observaciones, Date creacion, Cliente cliente, GestoSAT gestoSAT) {
        super(observaciones, creacion, cliente, gestoSAT);
        this.recibo = null;
        this.concepto = concepto;
        this.formaPago = formaPago;
        
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public synchronized float getTotal() {
        return total;
    }

    public synchronized void setTotal(float total) {
        this.total = total;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public void setRecibo(Recibo recibo) {
        if(this.recibo==null)
            this.recibo = recibo;
    }

    public Map getAlbaranes() {
        return albaran;
    }

    public void setAlbaranes(Map albaran) {
        this.albaran = albaran;
        Iterator itAlbaran = this.albaran.entrySet().iterator();
            synchronized(this){
                this.total=0;
                while(itAlbaran.hasNext()){
                    Map.Entry auxAlbaran = (Map.Entry)itAlbaran.next();
                    ((Albaran)auxAlbaran.getValue()).setFactura(this);
                    this.total +=((Albaran)auxAlbaran.getValue()).getTotal(); 
                }
            }
    }
    
    public Albaran getAlbaran(int id) {
        return (Albaran)albaran.get(id);
    }

    public void setAlbaran(int id, Albaran albaran) {
        
        if(this.albaran.putIfAbsent(id,albaran)== null){
            synchronized(this){
                this.total +=albaran.getTotal(); 
            }
        }
    }
    
    public void actualizar(String concepto, String formaPago, String observaciones) {
        this.setObservaciones(observaciones);
        this.concepto = concepto;
        this.formaPago = formaPago;
        
    }
    
    public boolean actualizar(int id, String concepto, String formaPago, String observaciones) {
        this.setObservaciones(observaciones);
        this.concepto = concepto;
        this.formaPago = formaPago;
        
        return this.getGestoSAT().guardarFactura(id,this);
        
    }
    
    public Map cargarRecibo(int idFactura){
        Map mapRecibo = this.getGestoSAT().recuperarRecibo(idFactura);
                Iterator it  = mapRecibo.entrySet().iterator();
                if(it.hasNext()){
                    Map.Entry aux = (Map.Entry)it.next();
                    this.recibo=(Recibo)aux.getValue();
                }
            return mapRecibo;
    }
 
    public void eliminarAlbaranes(){
        synchronized(this){
            this.total=0;
        }
        
        Iterator itAlbaranes = albaran.entrySet().iterator();
        while(itAlbaranes.hasNext()){
            Map.Entry aux = (Map.Entry)itAlbaranes.next();
            ((Albaran)aux.getValue()).setFactura(null);
        }
        this.albaran = new HashMap();
    }
    
    public String factura2PDF(){
            try {
                String arch = "Factura"+(new Date()).getTime()+".pdf";
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
                doc.add(new Paragraph("FACTURA"));
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
                doc.add(new Paragraph("Forma pago: "+this.formaPago));
                doc.add(new Paragraph(" "));
                doc.add(new Paragraph("Albaranes"));
                
                Iterator itAlbaranes = this.albaran.entrySet().iterator();
                while(itAlbaranes.hasNext()){
                    Map.Entry auxAlbaranes = (Map.Entry)itAlbaranes.next();
                // Leer albaranes
                    float auxPrecio=0;
                    PdfPTable table = new PdfPTable(5);

                    table.addCell("#");
                    table.addCell("Nombre");
                    table.addCell("Precio Ud / H");
                    table.addCell("Uds / H");
                    table.addCell("Total");

                    Iterator itTabla = ((Albaran)auxAlbaranes.getValue()).getMaterialUtilizado().entrySet().iterator();
                    while(itTabla.hasNext()){
                        Map.Entry aux = (Map.Entry)itTabla.next();
                        table.addCell(aux.getKey().toString());
                        table.addCell(((MaterialTrabajos)aux.getValue()).getStock().getNombre());
                        table.addCell(((MaterialTrabajos)aux.getValue()).getStock().getPrecioUnidad()+"");
                        table.addCell(((MaterialTrabajos)aux.getValue()).getCantidad()+"");
                        auxPrecio+=((MaterialTrabajos)aux.getValue()).getCantidad()*((MaterialTrabajos)aux.getValue()).getStock().getPrecioUnidad();
                        table.addCell((((MaterialTrabajos)aux.getValue()).getCantidad()*((MaterialTrabajos)aux.getValue()).getStock().getPrecioUnidad())+"");
                    }

                    itTabla = ((Albaran)auxAlbaranes.getValue()).getTrabajoRealizado().entrySet().iterator();
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
                    doc.add(new Paragraph(" "));
                }
                
                doc.add(new Paragraph(" "));
                doc.add(new Paragraph("Tota factura: "+df.format(this.total)));

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
    
    public String factura2XLSX(){
            String archivo = "Factura"+(new Date()).getTime()+".xlsx";
            try {
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("Factura");
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
                cell.setCellValue("Datos Factura");
                
                row =sheet.createRow((short) 11);
                cell = row.createCell(0);
                cell.setCellValue("Concepto");
                cell = row.createCell(1);
                cell.setCellValue(this.concepto);
                row =sheet.createRow((short) 12);
                cell = row.createCell(0);
                cell.setCellValue("Forma pago");
                cell = row.createCell(1);
                cell.setCellValue(this.formaPago);
                row =sheet.createRow((short) 13);
                cell = row.createCell(0);
                cell.setCellValue("Observaciones");
                cell = row.createCell(1);
                cell.setCellValue(this.getObservaciones());
                
                row =sheet.createRow((short) 15);
                cell = row.createCell(0);
                cell.setCellValue("Albaranes");
                
                float total=0;
                DecimalFormat df = new DecimalFormat("0.00");
                int valueIndex = row.getRowNum()+2;
                
                Iterator itAlbaranes = this.albaran.entrySet().iterator();
                while(itAlbaranes.hasNext()){
                    Map.Entry auxAlb = (Map.Entry)itAlbaranes.next();
                    Albaran alb = (Albaran) auxAlb.getValue();
                    
                    row =sheet.createRow((short) valueIndex);
                    cell = row.createCell(0);
                    cell.setCellValue("Concepto Alabrán");
                    cell = row.createCell(1);
                    cell.setCellValue(alb.getConcepto());
                
                    if(!alb.getTrabajoRealizado().isEmpty()){
                        valueIndex = row.getRowNum()+2;
                        row = sheet.createRow((short) valueIndex);
                        cell = row.createCell(0);
                        cell.setCellValue("Trabajos presupuestados");
                        valueIndex = row.getRowNum()+2;
                        row = sheet.createRow((short) valueIndex);
                        cell = row.createCell(0);
                        cell.setCellValue("Nombre");
                        cell = row.createCell(1);
                        cell.setCellValue("Precio h");
                        cell = row.createCell(2);
                        cell.setCellValue("Horas");
                        cell = row.createCell(3);
                        cell.setCellValue("Total");
                        valueIndex =row.getRowNum()+2;
                        
                        Iterator itTrabajos = alb.getTrabajoRealizado().entrySet().iterator();
                        for(int i = valueIndex;itTrabajos.hasNext();i++){
                            Map.Entry aux = (Map.Entry)itTrabajos.next();
                            Trabajo trabajo = (Trabajo)aux.getValue();
                            row = sheet.createRow((short) i);
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
                    
                    valueIndex = row.getRowNum()+2;

                    if(!alb.getMaterialUtilizado().isEmpty()){
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

                        Iterator itMateriales = alb.getMaterialUtilizado().entrySet().iterator();
                        valueIndex++;
                        for(int i=valueIndex;itMateriales.hasNext();i++){
                            Map.Entry aux = (Map.Entry)itMateriales.next();
                            MaterialTrabajos material = (MaterialTrabajos)aux.getValue();
                            row = sheet.createRow((short) i);
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
                        }
                    }
                    valueIndex = row.getRowNum()+2;
                            
                }
                float iva = this.getGestoSAT().getIva()/(float)100;
                valueIndex++;
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