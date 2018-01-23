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

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Entrada extends Documento {
	private String lugar = "";
	private Incidencia incidencia;
	private Presupuesto presupuesto = null;
        
        public Entrada(String observaciones, String lugar, GestoSAT gestoSAT, Cliente cliente) {
            super(observaciones, cliente, gestoSAT);
            this.lugar = lugar;
            this.presupuesto = null;
        }
        
        public Entrada(String observaciones, GestoSAT gestoSAT, Cliente cliente) {
            super(observaciones, cliente, gestoSAT);
            this.presupuesto = null;
        }
        
        public Entrada(String observaciones, String lugar, Date creacion, GestoSAT gestoSAT,Cliente cliente, Incidencia incidencia) {
            super(observaciones, creacion, cliente, gestoSAT);
            this.lugar = lugar;
            this.incidencia = incidencia;
            this.presupuesto = null;
        }
        
        public void setLugar(String lugar){
            this.lugar = lugar;
        }
        
        public void setIncidencia(Incidencia incidencia){
            this.incidencia = incidencia;
        }

        public String getLugar() {
            return lugar;
        }


        public Incidencia getIncidencia() {
            return incidencia;
        }

        public Presupuesto getPresupuesto() {
            return presupuesto;
        }

        public void setPresupuesto(Presupuesto presupuesto) {
            this.presupuesto = presupuesto;
        }
        
        public Map cargarPresupuesto(int idEntrada){
            Map mapPresupuesto = this.getGestoSAT().recuperarPresupuesto(idEntrada,this);
            Iterator it  = mapPresupuesto.entrySet().iterator();
            if(it.hasNext()){
                Map.Entry aux = (Map.Entry)it.next();
                this.presupuesto=(Presupuesto)aux.getValue();
                mapPresupuesto.putAll(this.presupuesto.cargarAlbaran(Integer.parseInt(aux.getKey().toString().split(";")[0])));
            }
            return mapPresupuesto;
        }
        
        public void actualizar(String observaciones, String lugar, String motivo){
            this.lugar = lugar;
            this.getIncidencia().setMotivo(motivo);
            this.setObservaciones(observaciones);
            
        }
        
        public void actualizar(String observacionesEntrada, Date cita, String motivo,
                String observacionesCita){
            this.setObservaciones(observacionesEntrada);
            this.getIncidencia().setMotivo(motivo);
            this.getIncidencia().setObservaciones(observacionesCita);
            ((Cita)this.getIncidencia()).setFchCita(cita);
        };
        
        public void actualizar(String observacionesEntrada, Date cita, String motivo,
                String observacionesCita, Map empleados){
            this.setObservaciones(observacionesEntrada);
            this.getIncidencia().setMotivo(motivo);
            this.getIncidencia().setObservaciones(observacionesCita);
            ((Cita)this.getIncidencia()).setFchCita(cita);
            ((Cita)this.getIncidencia()).setEmpleados(empleados);
        };
        
        public String entrada2PDF(){
            try {
                String arch = "Entrada"+(new Date()).getTime()+".pdf";
                Document doc = new Document();
                PdfWriter.getInstance(doc,new FileOutputStream("/TomEE/webapps/ROOT/descargables/"+arch));
                doc.open();
                File f = new File("logo");
                
                // http://www.java2s.com/Tutorial/Java/0280__SWT/UseImageDatatocreateanewimage.htm
                if(f.exists()){
                    Image img = Image.getInstance("logo");
                    img.setAbsolutePosition(50, 735);
                    img.scaleAbsolute(50, 50);
                    doc.add(img);
                    Paragraph p = new Paragraph(" ");
                    p.setLeading((float)75);
                    doc.add(p);
                }
                doc.add(new Paragraph("Cliente: "+this.getCliente().getNombre()+" "+this.getCliente().getApellidos()));
                if(this.incidencia.getClass().getName().equals("GestoSAT.Averia")){
                    doc.add(new Paragraph(" "));
                    doc.add(new Paragraph("APARATO"));
                    doc.add(new Paragraph(" "));
                    Averia av = (Averia)this.incidencia;
                    doc.add(new Paragraph("Problema: "+av.getMotivo()));
                    doc.add(new Paragraph(" "));
                    doc.add(new Paragraph("Tipo: "+av.getAparato().getTipo()));
                    doc.add(new Paragraph("Marca: "+av.getAparato().getMarca()));
                    doc.add(new Paragraph("Modelo: "+av.getAparato().getModelo()));
                    doc.add(new Paragraph("NºSerie: "+av.getAparato().getNumSerie()));
                    doc.add(new Paragraph("Color: "+av.getAparato().getColor()));
                    doc.add(new Paragraph("Observaciones: "+av.getAparato().getObservaciones()));
                }else{
                    Cita cita = (Cita)this.incidencia;
                    doc.add(new Paragraph(" "));
                    doc.add(new Paragraph("CITA"));
                    doc.add(new Paragraph("Problema:"+cita.getMotivo()));
                    doc.add(new Paragraph("Dirección:"+cita.getPoblacion()+" "+cita.getCalle()+" "
                    +cita.getNumero()+" "+cita.getEscalera()+" "+cita.getPiso()+" "
                    +cita.getPuerta()));
                    doc.add(new Paragraph("Obsevaciones:"+cita.getObservacionesDirrecion()));
                    doc.add(new Paragraph(cita.getObservaciones()));
                }
                doc.close();
                return "descargables/"+arch;
            } catch (Exception ex) {
                Logger.getLogger(Entrada.class.getName()).log(Level.SEVERE, null, ex);
                return "";
            }
        }
        
        public String entrada2XLSX(){
            String archivo = "Entrada"+(new Date()).getTime()+".xlsx";
            try {
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("Entrada");
                // Create a row and put some cells in it. Rows are 0 based.
                Row row;
                Cell cell;
                row =sheet.createRow((short) 0);
                cell = row.createCell(0);
                cell.setCellValue("Datos cliente");
                row =sheet.createRow((short) 1);
                cell = row.createCell(0);
                cell.setCellValue("Nombre");
                cell = row.createCell(1);
                cell.setCellValue(this.getCliente().getNombre());
                row =sheet.createRow((short) 2);
                cell = row.createCell(0);
                cell.setCellValue("Apellidos");
                cell = row.createCell(1);
                cell.setCellValue(this.getCliente().getApellidos());
                
                row =sheet.createRow((short) 4);
                cell = row.createCell(0);
                cell.setCellValue("Valores entrada");
                
                row =sheet.createRow((short) 5);
                cell = row.createCell(0);
                cell.setCellValue("Motivo");
                cell = row.createCell(1);
                cell.setCellValue(this.incidencia.getMotivo());
                
                row =sheet.createRow((short) 6);
                cell = row.createCell(0);
                cell.setCellValue("Observaciones");
                cell = row.createCell(1);
                cell.setCellValue(this.getObservaciones());
                
                if(this.incidencia.getClass().getName().equals("GestoSAT.Cita")){
                    Cita cita = (Cita)this.incidencia;
                    row =sheet.createRow((short) 8);
                    cell = row.createCell(0);
                    cell.setCellValue("Observaciones dirección");
                    cell = row.createCell(1);
                    cell.setCellValue(cita.getObservacionesDirrecion());
                    row =sheet.createRow((short) 9);
                    cell = row.createCell(0);
                    cell.setCellValue("Observaciones cita");
                    cell = row.createCell(1);
                    cell.setCellValue(cita.getObservaciones());
                    row =sheet.createRow((short) 11);
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
                    row =sheet.createRow((short) 12);
                    cell = row.createCell(0);
                    cell.setCellValue(cita.getProvincia());
                    cell = row.createCell(1);
                    cell.setCellValue(cita.getPoblacion());
                    cell = row.createCell(2);
                    cell.setCellValue(cita.getCalle());
                    cell = row.createCell(3);
                    cell.setCellValue(cita.getNumero());
                    cell = row.createCell(4);
                    cell.setCellValue(cita.getEscalera());
                    cell = row.createCell(5);
                    cell.setCellValue(cita.getPiso());
                    cell = row.createCell(6);
                    cell.setCellValue(cita.getPuerta());
                }else{
                    Averia averia = (Averia)this.incidencia;
                    row = sheet.createRow((short) 8);
                    cell = row.createCell(0);
                    cell.setCellValue("Observaciones");
                    cell = row.createCell(1);
                    cell.setCellValue(averia.getObservaciones());
                    row = sheet.createRow((short) 9);
                    cell = row.createCell(0);
                    cell.setCellValue("Observaciones aparato");
                    cell = row.createCell(1);
                    cell.setCellValue(averia.getAparato().getObservaciones());
                    row = sheet.createRow((short) 11);
                    cell = row.createCell(0);
                    cell.setCellValue("Tipo");
                    cell = row.createCell(1);
                    cell.setCellValue("Modelo");
                    cell = row.createCell(2);
                    cell.setCellValue("Marca");
                    cell = row.createCell(3);
                    cell.setCellValue("N Seria");
                    cell = row.createCell(4);
                    cell.setCellValue("Color");
                    row = sheet.createRow((short) 12);
                    cell = row.createCell(0);
                    cell.setCellValue(averia.getAparato().getTipo());
                    cell = row.createCell(1);
                    cell.setCellValue(averia.getAparato().getModelo());
                    cell = row.createCell(2);
                    cell.setCellValue(averia.getAparato().getMarca());
                    cell = row.createCell(3);
                    cell.setCellValue(averia.getAparato().getNumSerie());
                    cell = row.createCell(4);
                    cell.setCellValue(averia.getAparato().getColor());
                }
                
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