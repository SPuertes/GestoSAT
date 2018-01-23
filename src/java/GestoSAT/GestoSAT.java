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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import sun.misc.BASE64Decoder;
import java.io.BufferedInputStream;
import java.util.Vector;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import java.util.Date;
import java.util.Iterator;
import javax.imageio.ImageIO;

@Stateless
public class GestoSAT {
	private Map documento = new HashMap();
	private Map cliente = new HashMap();
	private Empleado empleado;
        private Map empleados = new HashMap();
	private Map proveedor = new HashMap();
        private Connection con;
        private Vector<String> confSeg = new Vector<String>() ;
        private Vector<String> mySQL = new Vector<String>() ;
        private int iva;
        
        
	public GestoSAT(){
            
            File f = new File("confGestoSAT");
            if(f.exists()){
                FileReader file;
                try {
                    file = new FileReader("confGestoSAT");
                    BufferedReader b = new BufferedReader(file);
                    String cadena;
                    cadena = b.readLine();
                    String[] valores = cadena.split(";");
                                        
                    mySQL.add(valores[0]);
                    mySQL.add(Math.abs(Integer.parseInt(valores[1]))+"");
                    mySQL.add(valores[2]);
                    mySQL.add(valores[3]);
                    
                    Class.forName("com.mysql.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://"+mySQL.elementAt(0)+":"+Math.abs(Integer.parseInt(mySQL.elementAt(1)))+"/gestosat?user="+mySQL.elementAt(2)+"&password="+mySQL.elementAt(3));
                
                    confSeg.add(valores[4]);
                    confSeg.add(Math.abs(Integer.parseInt(valores[5]))+"");
                    confSeg.add(valores[6]);    
                    confSeg.add(valores[7]);
                   
                    iva = Math.abs(Integer.parseInt(valores[8]));
                    
                    file.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                }
        
            }
        }
        
        @Schedule(dayOfWeek = "Sun-Sat", month = "*", hour = "22", dayOfMonth = "*", year = "*", minute = "0", second = "0", persistent = true)
        public void generarCopia() {
            
            // http://chuwiki.chuidiang.org/index.php?title=Backup_de_MySQL_con_Java
            try {
                Process p = Runtime.getRuntime().exec("mysqldump -u "+mySQL.elementAt(2)+" -p"+mySQL.elementAt(3)+" -h "+mySQL.elementAt(0)+" -P "+mySQL.elementAt(1)+" gestosat");

                InputStream is = p.getInputStream();
                FileOutputStream fos = new FileOutputStream("backupGestoSAT.sql");
                byte[] buffer = new byte[1000];
                
                int leido = is.read(buffer);
                while (leido > 0) {
                   fos.write(buffer, 0, leido);
                   leido = is.read(buffer);
                }
                fos.close();
                
                //http://www.javamexico.org/blogs/lalo200/pequeno_ejemplo_para_subir_un_archivo_ftp_con_la_libreria_commons_net
                FTPClient clienteFTP = new FTPClient();
                
                // Lanza excepción si el servidor no existe
                clienteFTP.connect(confSeg.elementAt(0),Integer.parseInt(confSeg.elementAt(1)));
                
                if(clienteFTP.login(confSeg.elementAt(2),confSeg.elementAt(3))){
                    
                    clienteFTP.setFileType(FTP.BINARY_FILE_TYPE);
                    BufferedInputStream buffIn = new BufferedInputStream(new FileInputStream("backupGestoSAT.sql"));
                    clienteFTP.enterLocalPassiveMode();
                    clienteFTP.storeFile("backupGestoSAT.sql", buffIn);
                    buffIn.close();
                    clienteFTP.logout();
                    clienteFTP.disconnect();
                }else
                    System.out.println("Error de conexión FTP");
                
             } catch (Exception e) {
                e.printStackTrace();
             }
        }

	public int iniciarSesion(String correo, String pass) {
            File f = new File("confGestoSAT");
                if(f.exists()){
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        Statement st = con.createStatement();
                        ResultSet res = st.executeQuery("Select * From usuarios u INNER JOIN entidades e ON u.id_Entidad = e.id_Entidad Where u.Correo_electronico ='"+correo+"' AND u.Activo = 1");
                        
                        if(res.next()){
                            
                            String p =  (String) res.getObject("u.Password");
                            
                            MessageDigest sha256=MessageDigest.getInstance("SHA-256");
                            sha256.update(pass.getBytes("UTF-8"));
                            byte[] digest = sha256.digest();
                            StringBuffer sb=new StringBuffer();
                            for(int i=0;i<digest.length;i++){
                                sb.append(String.format("%02x", digest[i]));
                            }
            
                            String password = sb.toString();
                
                            
                            if(p.equals(password)){
                                if((res.getString("u.Gerente")).equals("1"))
                                    empleado = new Gerente(res.getString("u.Nombre"),res.getString("u.Apellidos"),res.getString("u.DNI"),
                                    res.getString("u.Poblacion"), res.getString("u.Provincia"),res.getInt("u.CP"),
                                    res.getString("u.Calle"), res.getString("u.Numero"), res.getString("u.Escalera"), res.getInt("u.Piso"),
                                    res.getString("u.Puerta"), res.getInt("u.tlf_Fijo"), res.getInt("u.tlf_Movil"),
                                    res.getFloat("u.Sueldo_base"), res.getFloat("u.Precio_hora"),correo,res.getString("e.nombre"),
                                    res.getString("e.nif"), res.getString("e.provincia"), res.getString("e.poblacion"), res.getInt("e.CP"),
                                    res.getString("e.Calle"), res.getString("e.numero"), res.getString("e.correo_electronico"),
                                    res.getInt("e.tlf_fijo"), res.getInt("e.tlf_movil"), res.getInt("e.fax"));
                                else
                                    empleado = new Empleado(res.getString("u.Nombre"),res.getString("u.Apellidos"),res.getString("u.DNI"),
                                    res.getString("u.Poblacion"), res.getString("u.Provincia"),res.getInt("u.CP"),
                                    res.getString("u.Calle"), res.getString("u.Numero"), res.getString("u.Escalera"), res.getInt("u.Piso"),
                                    res.getString("u.Puerta"), res.getInt("u.tlf_Fijo"), res.getInt("u.tlf_Movil"),
                                    res.getFloat("u.Sueldo_base"), res.getFloat("u.Precio_hora"),correo,res.getString("e.nombre"),
                                    res.getString("e.nif"), res.getString("e.provincia"), res.getString("e.poblacion"), res.getInt("e.CP"),
                                    res.getString("e.Calle"), res.getString("e.numero"), res.getString("e.correo_electronico"),
                                    res.getInt("e.tlf_fijo"), res.getInt("e.tlf_movil"), res.getInt("e.fax"));
                                    
                                empleado.setGestoSAT(this);
                                
                                int devolver = res.getInt("u.id_Usuario");
                                res.close();
                                this.empleados.put(devolver,empleado);
                                this.getEmpleados(devolver);
                                return devolver;
                            }else{
                                res.close();
                                return 0;
                            }
                        }else
                            return 0;
                    } catch (SQLException ex) {
                        Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                        return 0;
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                        return 0;
                    } catch (NoSuchAlgorithmException ex) {
                        Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                        return 0;
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                        return 0;
                    }
                }else{
                    String defaultUser = "admin@default.es";
                    String defaultPass = "admin";
                                        
                    if(defaultUser.equals(correo) && defaultPass.equals(pass))
                        return 1;
                    else
                        return 0;
                }
	}
        
         public boolean primerAcceso(){
            File f = new File("confGestoSAT");
            if(!f.exists())
                return true;
            else
                return false; 
        }
        
        public Gerente configuracionInicial(String nombre, String apellidos, String dni,
                String poblacion, String provincia, int cp, String calle, String numero,
                String escalera, int piso, String puerta, int tlfFijo, int tlfMovil,float sueldo,
                float precioHora, String email, String pass, String nombreEmp, String nif,
                String poblacionEmp, String provinciaEmp, int cpEmp, String calleEmp,
                String numeroEmp, String emailEmp, int tlfFijoEmp, int tlfMovilEmp,
                int faxEmp, String ipMySQL, int puertoMySQL,
                String userMySQL, String passMySQL, String ipFTP, int puertoFTP,
                String userFTP,String passFTP,int iva){
            
            File archConf = new File("confGestoSAT");
            File dir = new File("docsGSAT");
            dir.mkdir();
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(archConf));
                bw.write(ipMySQL+";"+Math.abs(puertoMySQL)+";"+userMySQL+";"+passMySQL+";"
                +ipFTP+";"+Math.abs(puertoFTP)+";"+userFTP+";"+passFTP+";"+iva);
                bw.close();
                this.iva = Math.abs(iva);
                
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://"+ipMySQL+":"+puertoMySQL+"/?user="+userMySQL+"&password="+passMySQL);
                
                Statement statement = con.createStatement();
                
                statement.executeUpdate("Create Database IF NOT EXISTS gestosat");
                con.close();
                
                con = DriverManager.getConnection("jdbc:mysql://"+ipMySQL+":"+puertoMySQL+"/gestosat?user="+userMySQL+"&password="+passMySQL);
                statement = con.createStatement();
                
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS albaranes (" +
"   id_Albaran     INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
"    fch_Creacion   DATETIME NOT NULL ," +
"    Concepto       VARCHAR (1000) NOT NULL ," +
"    fch_Entrega    DATETIME ," +
"    Provincia      VARCHAR (50) NOT NULL ," +
"    Poblacion      VARCHAR (255) NOT NULL ," +
"    CP             INTEGER NOT NULL ," +
"    Calle          VARCHAR (255) NOT NULL ," +
"    Numero         VARCHAR (10) NOT NULL ," +
"    Escalera       VARCHAR (10) ," +
"    Piso           INTEGER ," +
"    Puerta         VARCHAR (5) ," +
"    Total          DECIMAL(10,2) NOT NULL," +
"    id_Factura     INTEGER ," +
"    id_Presupuesto INTEGER ," +
"    id_Cliente     INTEGER NOT NULL ," +
"    Observaciones  VARCHAR (1000)" +
"   )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS aparatos" +
"  (" +
"    id_Aparato    INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
"    Tipo          VARCHAR (50) NOT NULL ," +
"    Marca         VARCHAR (100) NOT NULL ," +
"    Modelo        VARCHAR (100) NOT NULL ," +
"    Color         VARCHAR (50) NOT NULL ," +
"    Numero_serie  VARCHAR (50) NOT NULL ," +
"    Observaciones VARCHAR (1000) ," +
"    id_Cliente    INTEGER NOT NULL )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS averias" +
"  (id_Aparato INTEGER NOT NULL ," +
"    id_Entrada INTEGER NOT NULL ," +
"    Motivo      VARCHAR (1000) NOT NULL," +
"    Descripcion VARCHAR (1000) NOT NULL)");

statement.executeUpdate("ALTER TABLE averias ADD CONSTRAINT PRIMARY KEY ( id_Aparato, id_Entrada )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS citas" +
"  (id_Cita      INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
"    fch_Cita     DATETIME NOT NULL ," +
"    Direccion   INTEGER NOT NULL ," +
"    Motivo       VARCHAR (1000) NOT NULL ," +
"    Observaciones VARCHAR (1000) ," +
"    id_Cliente   INTEGER NOT NULL ," +
"    id_Entrada   INTEGER NOT NULL" +
"  )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS citas_empleados" +
"  (" +
"    id_Cita INTEGER NOT NULL ," +
"    id_Usuario INTEGER NOT NULL" +
"  )");

statement.executeUpdate("ALTER TABLE citas_empleados ADD CONSTRAINT PRIMARY KEY ( id_Cita, id_Usuario )");
statement.executeUpdate("CREATE TABLE IF NOT EXISTS clientes" +
"  (" +
"    id_Cliente         INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
"    Nombre             VARCHAR (150) NOT NULL ," +
"    Apellidos          VARCHAR (250) NOT NULL ," +
"    NIF                VARCHAR (12) NOT NULL ," +
"    Provincia          VARCHAR (50) NOT NULL ," +
"    Poblacion          VARCHAR (255) NOT NULL ," +
"    CP                 INTEGER NOT NULL ," +
"    Calle              VARCHAR (255) NOT NULL ," +
"    Numero             VARCHAR (5) NOT NULL ," +
"    Escalera           VARCHAR (10) ," +
"    Piso               INTEGER ," +
"    Puerta             VARCHAR (5) ," +
"    tlf_Contacto       INTEGER NOT NULL ," +
"    tlf_Auxiliar       INTEGER ," +
"    Correo_electronico VARCHAR (255) ," +
"    Observaciones      VARCHAR (1000)" +
"  ) ");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS direccion_citas" +
"  (id_Direccion      INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
"    Provincia    VARCHAR (50) NOT NULL ," +
"    Poblacion    VARCHAR (255) NOT NULL ," +
"    CP           INTEGER NOT NULL ," +
"    Calle        VARCHAR (255) NOT NULL ," +
"    Numero       VARCHAR (5) NOT NULL ," +
"    Escalera     VARCHAR (10) ," +
"    Piso         INTEGER ," +
"    Puerta       VARCHAR (5) ," +
"    Observaciones VARCHAR (1000) " +
"  )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS entidades" +
"  (" +
"    id_Entidad         INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
"    Nombre             VARCHAR (255) NOT NULL ," +
"    NIF                VARCHAR (12) NOT NULL ," +
"    Provincia          VARCHAR (50) NOT NULL ," +
"    Poblacion          VARCHAR (255) NOT NULL ," +
"    CP                 INTEGER NOT NULL ," +
"    Calle              VARCHAR (255) NOT NULL ," +
"    Numero             VARCHAR (5) NOT NULL ," +
"    Central            CHAR (1) NOT NULL ," +
"    Correo_electronico VARCHAR (100) NOT NULL ," +
"    tlf_Fijo  INTEGER ," +
"    tlf_Movil INTEGER NOT NULL ," +
"    Fax       INTEGER" +
"  )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS entradas" +
"  (" +
"    id_Entrada    INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
"    fch_Entrada   DATETIME NOT NULL ," +
"    Lugar         VARCHAR (150) ," +
"    Observaciones VARCHAR (1000) ," +
"    id_Usuario    INTEGER NOT NULL" +
"  )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS facturas" +
"  (" +
"    id_Factura    INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
"    fch_Creacion  DATETIME NOT NULL ," +
"    Concepto      VARCHAR (1000) NOT NULL ," +
"    Observaciones VARCHAR (1000) ," +
"    Total         DECIMAL(10,2) NOT NULL, " +
"    Forma_pago    VARCHAR (50) NOT NULL " +
"  )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS materiales_presupuestos" +
"  (" +
"    id_Presupuesto INTEGER NOT NULL ," +
"    id_Stock INTEGER NOT NULL ," +
"    Cantidad FLOAT NOT NULL" +
"  )");
statement.executeUpdate("ALTER TABLE materiales_presupuestos ADD CONSTRAINT PRIMARY KEY ( id_Presupuesto, id_Stock )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS materiales_utilizados" +
"  (" +
"    id_Albaran INTEGER NOT NULL ," +
"    id_Stock INTEGER NOT NULL ," +
"    Cantidad FLOAT" +
"  ) ");

statement.executeUpdate("ALTER TABLE materiales_utilizados ADD CONSTRAINT PRIMARY KEY ( id_Albaran, id_Stock )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS presupuestos" +
"  (" +
"    id_Presupuesto INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
"    fch_Creacion DATETIME NOT NULL ," +
"    Concepto     VARCHAR (1000) NOT NULL ," +
"    fch_Validez  DATETIME NOT NULL ," +
"    Aceptado     CHAR (1) NOT NULL ," +
"    Forma_pago   VARCHAR (150) ," +
"    Adelanto     DECIMAL(10,2) ," +
"    Plazo        INTEGER NOT NULL ," +
"    Condiciones  VARCHAR (1000) ," +
"    Seguro       VARCHAR (500) ," +
"    Garantia     VARCHAR (1000) ," +
"    Observaciones VARCHAR (1000) ," +
"    Total        DECIMAL (10,2) NOT NULL," +
"    id_Entrada   INTEGER NOT NULL" +
"  )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS proveedores" +
"  (" +
"    id_Proveedor       INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
"    Nombre             VARCHAR (255) NOT NULL ," +
"    NIF                VARCHAR (12) NOT NULL ," +
"    Provincia          VARCHAR (50) NOT NULL ," +
"    Poblacion          VARCHAR (255) NOT NULL ," +
"    CP                 INTEGER NOT NULL ," +
"    Calle              VARCHAR (255) NOT NULL ," +
"    Numero             VARCHAR (5) NOT NULL ," +
"    Piso               INTEGER ," +
"    Escalera           VARCHAR (50) ," +
"    Puerta             VARCHAR (5) ," +
"    tlf_Fijo           INTEGER NOT NULL ," +
"    tlf_Movil          INTEGER ," +
"    Fax                INTEGER ," +
"    Correo_electronico VARCHAR (255) NOT NULL" +
"  )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS recibos" +
"  (" +
"    id_Recibo      INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
"    fch_Creacion   DATETIME NOT NULL ," +
"    Provincia      VARCHAR (50) NOT NULL ," +
"    Poblacion      VARCHAR (255) NOT NULL ," +
"    CP             INTEGER NOT NULL ," +
"    Calle          VARCHAR (255) NOT NULL ," +
"    Numero         VARCHAR (5) NOT NULL ," +
"    Escalera       VARCHAR (10) ," +
"    Piso           INTEGER ," +
"    Puerta         VARCHAR (5) ," +
"    Observaciones  VARCHAR (1000) ," +
"    id_Factura     INTEGER NOT NULL " +
"  )" );

statement.executeUpdate("CREATE TABLE IF NOT EXISTS stock" +
"  (" +
"    id_Stock      INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
"    Nombre        VARCHAR (255) NOT NULL ," +
"    Descripcion   VARCHAR (1000) ," +
"    Precio_compra DECIMAL(10,2) NOT NULL ," +
"    Unidades FLOAT NOT NULL ," +
"    Precio_venta DECIMAL(10,2) NOT NULL ," +
"    num_Alertar  FLOAT NOT NULL ," +
"    id_Proveedor INTEGER NOT NULL" +
"  )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS trabajos_presupuestados" +
"  (" +
"    id_Usuario INTEGER NOT NULL ," +
"    id_Presupuesto INTEGER NOT NULL ," +
"    Horas DECIMAL(10,2) NOT NULL ," +
"    Descripcion VARCHAR (1000)" +
"  )");

statement.executeUpdate("ALTER TABLE trabajos_presupuestados ADD CONSTRAINT PRIMARY KEY ( id_Usuario, id_Presupuesto )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS trabajos_realizados" +
"  (" +
"    id_Usuario INTEGER NOT NULL ," +
"    id_Albaran INTEGER NOT NULL ," +
"    Horas       DECIMAL(10,2) ," +
"    Descripcion VARCHAR(1000)" +
"  )");

statement.executeUpdate("ALTER TABLE trabajos_realizados ADD CONSTRAINT PRIMARY KEY ( id_Usuario, id_Albaran )");

statement.executeUpdate("CREATE TABLE IF NOT EXISTS usuarios" +
"  (" +
"    id_Usuario INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY," +
"    Nombre     VARCHAR (100) NOT NULL ," +
"    Apellidos VARCHAR (100) NOT NULL ," +
"    Gerente    CHAR (1) NOT NULL ," +
"    DNI        VARCHAR (12) NOT NULL ," +
"    id_Entidad INTEGER NOT NULL ," +
"    Provincia          VARCHAR (50) NOT NULL ," +
"    Poblacion          VARCHAR (150) NOT NULL ," +
"    CP                 INTEGER  NOT NULL ," +
"    Calle              VARCHAR (255) NOT NULL ," +
"    Numero             VARCHAR (5) NOT NULL ," +
"    Escalera           VARCHAR (10) ," +
"    Piso               INTEGER ," +
"    Puerta             VARCHAR (5) ," +
"    tlf_Fijo           INTEGER ," +
"    tlf_Movil          INTEGER NOT NULL ," +
"    Correo_electronico VARCHAR (255) NOT NULL UNIQUE," +
"    Password    VARCHAR (100) NOT NULL ," +
"    Precio_hora DECIMAL(10,2) NOT NULL ," +
"    Sueldo_base DECIMAL(10,2) NOT NULL ," +
"    Activo      CHAR (1) NOT NULL" +
"  )");


statement.executeUpdate("ALTER TABLE albaranes ADD CONSTRAINT FOREIGN KEY ( id_Cliente ) REFERENCES clientes ( id_Cliente ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE albaranes ADD CONSTRAINT FOREIGN KEY ( id_Factura ) REFERENCES facturas ( id_Factura ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE albaranes ADD CONSTRAINT FOREIGN KEY ( id_Presupuesto ) REFERENCES presupuestos ( id_Presupuesto ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE aparatos ADD CONSTRAINT FOREIGN KEY ( id_Cliente ) REFERENCES clientes ( id_Cliente ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE averias ADD CONSTRAINT FOREIGN KEY ( id_Aparato ) REFERENCES aparatos ( id_Aparato ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE averias ADD CONSTRAINT FOREIGN KEY ( id_Entrada ) REFERENCES entradas ( id_Entrada ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE citas ADD CONSTRAINT FOREIGN KEY ( id_Cliente ) REFERENCES clientes ( id_Cliente ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE citas ADD CONSTRAINT FOREIGN KEY ( Direccion ) REFERENCES direccion_citas ( id_Direccion ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE citas_empleados ADD FOREIGN KEY ( id_Cita ) REFERENCES citas ( id_Cita ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE citas_empleados ADD FOREIGN KEY ( id_Usuario ) REFERENCES usuarios ( id_Usuario ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE citas ADD CONSTRAINT FOREIGN KEY ( id_Entrada ) REFERENCES entradas ( id_Entrada ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE entradas ADD CONSTRAINT FOREIGN KEY ( id_Usuario ) REFERENCES usuarios ( id_Usuario ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE materiales_presupuestos ADD CONSTRAINT FOREIGN KEY ( id_Presupuesto ) REFERENCES presupuestos ( id_Presupuesto ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE materiales_presupuestos ADD CONSTRAINT FOREIGN KEY ( id_Stock ) REFERENCES stock ( id_Stock ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE materiales_utilizados ADD CONSTRAINT FOREIGN KEY ( id_Albaran ) REFERENCES albaranes ( id_Albaran ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE materiales_utilizados ADD CONSTRAINT FOREIGN KEY ( id_Stock ) REFERENCES stock ( id_Stock ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE presupuestos ADD CONSTRAINT FOREIGN KEY ( id_Entrada ) REFERENCES entradas ( id_Entrada ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE recibos ADD CONSTRAINT FOREIGN KEY ( id_Factura ) REFERENCES facturas ( id_Factura ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE stock ADD CONSTRAINT FOREIGN KEY ( id_Proveedor ) REFERENCES proveedores ( id_Proveedor ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE trabajos_presupuestados ADD CONSTRAINT FOREIGN KEY ( id_Presupuesto ) REFERENCES presupuestos ( id_Presupuesto ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE trabajos_presupuestados ADD CONSTRAINT FOREIGN KEY ( id_Usuario ) REFERENCES usuarios ( id_Usuario ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE trabajos_realizados ADD CONSTRAINT FOREIGN KEY ( id_Albaran ) REFERENCES albaranes ( id_Albaran ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE trabajos_realizados ADD CONSTRAINT FOREIGN KEY ( id_Usuario ) REFERENCES usuarios ( id_Usuario ) ON DELETE CASCADE");
statement.executeUpdate("ALTER TABLE usuarios ADD CONSTRAINT FOREIGN KEY ( id_Entidad ) REFERENCES entidades ( id_Entidad ) ON DELETE CASCADE");


    MessageDigest sha256=MessageDigest.getInstance("SHA-256");
                sha256.update(pass.getBytes("UTF-8"));
                byte[] digest = sha256.digest();
                StringBuffer sb=new StringBuffer();
                for(int i=0;i<digest.length;i++){
                    sb.append(String.format("%02x", digest[i]));
                }

                pass = sb.toString();
                
                Gerente gerente = new Gerente(nombre, apellidos, dni, poblacion,
                        provincia, cp, calle, numero, escalera, piso, puerta,
                        tlfFijo, tlfMovil, sueldo, precioHora, email, nombreEmp, nif, provinciaEmp,
                        poblacionEmp, cpEmp, calleEmp, numeroEmp, emailEmp,
                        tlfFijoEmp, tlfMovilEmp, faxEmp);
           
                // Guardar en la base de datos entidad y gerente
                statement.executeUpdate("INSERT INTO entidades (Nombre,NIF, Provincia, Poblacion, "
                        + "CP, Calle, Numero, Central, Correo_electronico, tlf_Fijo, "
                        + "tlf_Movil, Fax) VALUES('"+nombreEmp+"','"+nif+"','"+provinciaEmp+"', '"+poblacionEmp+"', '"
                        + Math.abs(cpEmp)+"', '"+calleEmp+"', '"+numeroEmp+"', '"+1+"', '"+emailEmp+"','"
                        + Math.abs(tlfFijoEmp)+"','"+Math.abs(tlfMovilEmp)+"','"+Math.abs(faxEmp)+"')");
                
                statement.executeUpdate("INSERT INTO usuarios (Nombre, Apellidos, "
                        + "Gerente, DNI, id_Entidad, Provincia, Poblacion, CP, "
                        + "Calle, Numero, Escalera, Piso, Puerta, tlf_Fijo, tlf_Movil, "
                        + "Correo_electronico, Password, Precio_hora, Sueldo_base,"
                        + "Activo) VALUES ('"+nombre+"', '"+apellidos+"', '"+1+"',"
                        + " '"+dni+"','"+1+"','"+provincia+"','"+poblacion+"'"
                        + ",'"+Math.abs(cp)+"','"+calle+"','"+numero+"','"+escalera+"',"
                        + "'"+Math.abs(piso)+"', '"+puerta+"', '"+Math.abs(tlfFijo)+"','"+Math.abs(tlfMovil)+"','"
                        + email+"','"+pass+"','"+Math.abs(precioHora)+"','"
                        + Math.abs(sueldo)+"','"+1+"' )");
                
                statement.executeUpdate("INSERT INTO proveedores (Nombre, "
                        + " NIF, Provincia, Poblacion, CP, "
                        + "Calle, Numero, Escalera, Piso, Puerta, tlf_Fijo, tlf_Movil, "
                        + "Correo_electronico, Fax) VALUES ('Autoservicio',"
                        + " '"+nif+"','"+provinciaEmp+"','"+poblacionEmp+"',"
                        + "'"+cpEmp+"','"+calleEmp+"','"+numeroEmp+"','0',"
                        + "'0', '0', '"+Math.abs(tlfFijoEmp)+"','"+Math.abs(tlfMovilEmp)+"','"
                        + emailEmp+"','"+Math.abs(faxEmp)+"')");
                
                con.close();
                empleado = gerente;
                return (Gerente)empleado;
                
            } catch (IOException ex) {
                System.out.println("Error Archivo");
                System.out.println(ex.toString());
                return null;
            } catch (SQLException ex) {
                System.out.println(ex.toString());
                
                return null;
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }

           public Empleado getEmpleado(){
               return empleado;
           }
           
           public boolean actualizarUsuario(Empleado empleado, String newPass, int id){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Statement statement = con.createStatement();
                
                int respuesta;
                String consulta = "Update usuarios SET "
                        + "nombre='"+empleado.getNombre()+"',"
                        + "apellidos='"+empleado.getApellidos()+"',"
                        + "dni='"+empleado.getDni()+"',"
                        + "provincia='"+empleado.getProvincia()+"',"
                        + "poblacion='"+empleado.getPoblacion()+"',"
                        + "cp='"+empleado.getCp()+"',"
                        + "calle='"+empleado.getCalle()+"',"
                        + "numero='"+empleado.getNumero()+"',"
                        + "escalera='"+empleado.getEscalera()+"',"
                        + "piso='"+empleado.getPiso()+"',"
                        + "puerta='"+empleado.getPuerta()+"',"
                        + "correo_electronico='"+empleado.getEmail()+"',"
                        + "precio_hora='"+empleado.getPrecioHora()+"',"
                        + "sueldo_base='"+empleado.getSueldoBase()+"',"
                        + "tlf_Fijo='"+empleado.getTlfFijo()+"',"
                        + "tlf_Movil='"+empleado.getTlfMovil()+"'";
                        if(!newPass.equals("")){
                        // Calcular el nou Hash
                            MessageDigest sha256=MessageDigest.getInstance("SHA-256");
                            sha256.update(newPass.getBytes("UTF-8"));
                            byte[] digest = sha256.digest();
                            StringBuffer sb=new StringBuffer();
                            for(int i=0;i<digest.length;i++){
                                sb.append(String.format("%02x", digest[i]));
                            }

                            consulta += " , password='"+sb.toString()+"'";
                        
                        }
                        consulta += " Where id_usuario='"+id+"'"; 
                        
                respuesta = statement.executeUpdate(consulta);
                
                if(respuesta > 0)
                    return true;
                else
                    return false;
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }    
           }
           
           public boolean actualizarOficina(Oficina oficina){
            try {
                
                Class.forName("com.mysql.jdbc.Driver");
                Statement statement = con.createStatement();
                int respuesta = statement.executeUpdate("Update entidades SET "
                        + "Nombre='"+oficina.getNombre()+"',"
                        + "Nif='"+oficina.getNif()+"',"
                        + "Provincia='"+oficina.getProvincia()+"',"
                        + "Poblacion='"+oficina.getPoblacion()+"',"
                        + "CP='"+oficina.getCp()+"',"
                        + "Calle='"+oficina.getCalle()+"',"
                        + "Numero='"+oficina.getNumero()+"',"
                        + "Correo_electronico='"+oficina.getEmail()+"',"
                        + "tlf_Fijo='"+oficina.getTlfFijo()+"',"
                        + "tlf_Movil='"+oficina.getTlfMovil()+"',"
                        + "Fax='"+oficina.getFax()+"'"
                        + "  Where id_Entidad='1'");
                
                if(respuesta > 0)
                    return true;
                else
                 return false;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
           }
 
           
        public int getIva(){
            return iva;
        }
        

        public boolean actualizarConfiguracion(Vector<String> mySQL, Vector<String> confSeg, int iva, String logo) throws Exception{
            FileReader file;
                try{
                    this.iva = Math.abs(iva);
                    
                    BufferedImage image = null;
                    byte[] imageByte;

                    BASE64Decoder decoder = new BASE64Decoder();
                    imageByte = decoder.decodeBuffer(logo.split(",")[1]);
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
                    image = ImageIO.read(bis);
                    bis.close();

                    // write the image to a file
                    File outputfile = new File("logo");
                    String formato =logo.split("/")[1].split(";")[0];
                    ImageIO.write(image, formato, outputfile);
                    
                    // MySQL
                    if(mySQL.elementAt(0).equals(this.mySQL.elementAt(0))){
                       if(!mySQL.elementAt(1).equals(this.mySQL.elementAt(1)) &&
                               !mySQL.elementAt(2).equals(this.mySQL.elementAt(2)) &&
                               (!mySQL.elementAt(3).equals(this.mySQL.elementAt(3)) ||
                                       !mySQL.elementAt(0).equals(""))){
                            Class.forName("com.mysql.jdbc.Driver");
                            this.con.close();
                            this.con = DriverManager.getConnection("jdbc:mysql://"+mySQL.elementAt(0)+":"+Math.abs(Integer.parseInt(mySQL.elementAt(1)))+"/gestosat?user="+mySQL.elementAt(2)+"&password="+mySQL.elementAt(3));
                            
                            this.mySQL.set(0, mySQL.elementAt(0));
                            this.mySQL.set(1, Math.abs(Integer.parseInt(mySQL.elementAt(1)))+"");
                            this.mySQL.set(2, mySQL.elementAt(2));
                            this.mySQL.set(3, mySQL.elementAt(3));
                        }
                    }else{
                       // Comprobar que pass != ""
                        Process pGet = Runtime.getRuntime().exec("mysqldump -u "+this.mySQL.elementAt(2)+" -p"+this.mySQL.elementAt(3)+" -h "+this.mySQL.elementAt(0)+" -P "+this.mySQL.elementAt(1)+" gestosat");

                        InputStream is = pGet.getInputStream();
                        FileOutputStream fos = new FileOutputStream("backupGestoSAT.sql");
                        byte[] bufferOut = new byte[1000];

                        int leido = is.read(bufferOut);
                        while (leido > 0) {
                           fos.write(bufferOut, 0, leido);
                           leido = is.read(bufferOut);
                        }
                        fos.close();
                       
                        Class.forName("com.mysql.jdbc.Driver");
                        this.con.close();
                        this.con = DriverManager.getConnection("jdbc:mysql://"+mySQL.elementAt(0)+":"+Math.abs(Integer.parseInt(mySQL.elementAt(1)))+"/gestosat?user="+mySQL.elementAt(2)+"&password="+mySQL.elementAt(3));
                            
                        this.mySQL.set(0, mySQL.elementAt(0));
                        this.mySQL.set(1, Math.abs(Integer.parseInt(mySQL.elementAt(1)))+"");
                        this.mySQL.set(2, mySQL.elementAt(2));
                        this.mySQL.set(3, mySQL.elementAt(3));
                        
                        Process pPut = Runtime.getRuntime().exec("mysql -u "+mySQL.elementAt(2)+" -p"+mySQL.elementAt(3)+" -h "+mySQL.elementAt(0)+" -P "+Math.abs(Integer.parseInt(mySQL.elementAt(1)))+" gestosat");

                        OutputStream os = pPut.getOutputStream();
                        FileInputStream fis = new FileInputStream("backupGestoSAT.sql");
                        byte[] bufferIn = new byte[1000];

                        int escrito = fis.read(bufferIn);
                        while (escrito > 0) {
                           os.write(bufferIn, 0, leido);
                           escrito = fis.read(bufferIn);
                        }

                        os.flush();
                        os.close();
                        fis.close();
                   }
                   
                   // FTP
                
                   FTPClient cliente = new FTPClient();
                   if(!confSeg.elementAt(3).equals("")){
                        cliente.connect(confSeg.elementAt(0),Integer.parseInt(confSeg.elementAt(1)));
                        
                        if(cliente.login(confSeg.elementAt(2),confSeg.elementAt(3))){
                            cliente.setFileType(FTP.BINARY_FILE_TYPE);
                            BufferedInputStream buffIn = new BufferedInputStream(new FileInputStream("backupGestoSAT.sql"));
                            cliente.enterLocalPassiveMode();
                            cliente.storeFile("backupGestoSAT.sql", buffIn);
                            buffIn.close();
                            cliente.logout();
                            cliente.disconnect();
                            
                            this.confSeg = confSeg;
                        }else
                            return false;
                    }
                   
                    File archConf = new File("confGestoSAT");
                    BufferedWriter bw = new BufferedWriter(new FileWriter(archConf));
                    bw.write(this.mySQL.elementAt(0)+";"+Math.abs(Integer.parseInt(this.mySQL.elementAt(1)))+";"+this.mySQL.elementAt(2)+";"+this.mySQL.elementAt(3)+";"
                    +this.confSeg.elementAt(0)+";"+Math.abs(Integer.parseInt(this.confSeg.elementAt(1)))+";"+this.confSeg.elementAt(2)+";"+this.confSeg.elementAt(3)+";"+Math.abs(iva));
                    bw.close();
                   
                    return true;
                } catch (Exception ex) {
                    file = new FileReader("confGestoSAT");
                    BufferedReader b = new BufferedReader(file);
                    String cadena;
                    cadena = b.readLine();
                    String[] valores = cadena.split(";");
                                        
                    this.mySQL.add(valores[0]);
                    this.mySQL.add(Math.abs(Integer.parseInt(valores[1]))+"");
                    this.mySQL.add(valores[2]);
                    this.mySQL.add(valores[3]);
                    con.close();
                    Class.forName("com.mysql.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://"+this.mySQL.elementAt(0)+":"+this.mySQL.elementAt(1)+"/gestosat?user="+this.mySQL.elementAt(2)+"&password="+this.mySQL.elementAt(3));
                
                    this.confSeg.add(valores[4]);
                    this.confSeg.add(Math.abs(Integer.parseInt(valores[5]))+"");
                    this.confSeg.add(valores[6]);
                    this.confSeg.add(valores[7]);
                    
                    file.close();
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
        }
        
        public Vector<String> getMySQLConf(){
            Vector<String> aux = new Vector<String>();
            aux.add(mySQL.elementAt(0));
            aux.add(mySQL.elementAt(1));
            aux.add(mySQL.elementAt(2));
            return aux;
        }
        
        public Vector<String> getConfSeg(){
            Vector<String> aux = new Vector<String>();
            aux.add(confSeg.elementAt(0));
            aux.add(confSeg.elementAt(1));
            aux.add(confSeg.elementAt(2));
            return aux;
        }
        
        public boolean crearEmpleado(Empleado e, String pass){
            try {
                Statement st = con.createStatement();
                
                MessageDigest sha256=MessageDigest.getInstance("SHA-256");
                sha256.update(pass.getBytes("UTF-8"));
                byte[] digest = sha256.digest();
                StringBuffer sb=new StringBuffer();
                for(int i=0;i<digest.length;i++){
                    sb.append(String.format("%02x", digest[i]));
                }
                
                if(st.executeUpdate("INSERT INTO usuarios (Nombre, Apellidos, "
                        + "Gerente, DNI, id_Entidad, Provincia, Poblacion, CP, "
                        + "Calle, Numero, Escalera, Piso, Puerta, tlf_Fijo, tlf_Movil, "
                        + "Correo_electronico, Password, Precio_hora, Sueldo_base,"
                        + "Activo) VALUES ('"+e.getNombre()+"', '"+e.getApellidos()+"', '"+0+"',"
                        + " '"+e.getDni()+"','"+1+"','"+e.getProvincia()+"','"+e.getPoblacion()+"'"
                        + ",'"+e.getCp()+"','"+e.getCalle()+"','"+e.getNumero()+"','"+e.getEscalera()+"',"
                        + "'"+e.getPiso()+"', '"+e.getPuerta()+"', '"+e.getTlfFijo()+"','"+e.getTlfMovil()+"','"
                        + e.getEmail()+"','"+sb.toString()+"','"+e.getPrecioHora()+"','"
                        + e.getSueldoBase()+"','"+1+"' )")>0)
                    return true;
                else
                    return false;
                
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        
        public boolean crearGerente(Gerente g, String pass){
            try {
                Statement st = con.createStatement();
                
                MessageDigest sha256=MessageDigest.getInstance("SHA-256");
                sha256.update(pass.getBytes("UTF-8"));
                byte[] digest = sha256.digest();
                StringBuffer sb=new StringBuffer();
                for(int i=0;i<digest.length;i++){
                    sb.append(String.format("%02x", digest[i]));
                }
                
                if(st.executeUpdate("INSERT INTO usuarios (Nombre, Apellidos, "
                        + "Gerente, DNI, id_Entidad, Provincia, Poblacion, CP, "
                        + "Calle, Numero, Escalera, Piso, Puerta, tlf_Fijo, tlf_Movil, "
                        + "Correo_electronico, Password, Precio_hora, Sueldo_base,"
                        + "Activo) VALUES ('"+g.getNombre()+"', '"+g.getApellidos()+"', '"+1+"',"
                        + " '"+g.getDni()+"','"+1+"','"+g.getProvincia()+"','"+g.getPoblacion()+"'"
                        + ",'"+g.getCp()+"','"+g.getCalle()+"','"+g.getNumero()+"','"+g.getEscalera()+"',"
                        + "'"+g.getPiso()+"', '"+g.getPuerta()+"', '"+g.getTlfFijo()+"','"+g.getTlfMovil()+"','"
                        + g.getEmail()+"','"+sb.toString()+"','"+g.getPrecioHora()+"','"
                        + g.getSueldoBase()+"','"+1+"' )")>0)
                    return true;
                else
                    return false;
                
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        
        public Map getEmpleados(int id){
            try {
                    Statement st = con.createStatement();
                    ResultSet res = st.executeQuery("Select * From usuarios Where id_Usuario !='"+id+"'");
                    boolean activo;
                    while(res.next()){
                        if((res.getString("activo")).equals("1"))
                            activo = true;
                        else
                            activo = false;
                        
                        if((res.getString("Gerente")).equals("0"))
                            empleados.put(res.getInt("id_Usuario"),new Empleado(res.getString("Nombre"), res.getString("Apellidos"), res.getString("DNI"),
                                    res.getString("Poblacion"), res.getString("Provincia"), res.getInt("CP"),
                                    res.getString("Calle"), res.getString("Numero"), res.getString("Escalera"), res.getInt("Piso"),
                                    res.getString("Puerta"), res.getInt("tlf_Fijo"), res.getInt("tlf_Movil"),
                                    res.getFloat("Sueldo_base"), res.getFloat("Precio_hora"),
                                    res.getString("Correo_electronico"), activo));
                        else
                            empleados.put(res.getInt("id_Usuario"),new Gerente(res.getString("Nombre"), res.getString("Apellidos"), res.getString("DNI"),
                                    res.getString("Poblacion"), res.getString("Provincia"), res.getInt("CP"),
                                    res.getString("Calle"), res.getString("Numero"), res.getString("Escalera"), res.getInt("Piso"),
                                    res.getString("Puerta"), res.getInt("tlf_Fijo"), res.getInt("tlf_Movil"),
                                    res.getFloat("Sueldo_base"), res.getFloat("Precio_hora"),
                                    res.getString("Correo_electronico"), activo));
                        }
                return empleados;
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        
        public Map getEmpleados(){
            try {
                    Statement st = con.createStatement();
                    ResultSet res = st.executeQuery("Select * From usuarios Where activo=1");
                    while(res.next()){
                        if((res.getString("Gerente")).equals("0"))
                            empleados.put(res.getInt("id_Usuario"),new Empleado(res.getString("Nombre"), res.getString("Apellidos"), res.getString("DNI"),
                                    res.getString("Poblacion"), res.getString("Provincia"), res.getInt("CP"),
                                    res.getString("Calle"), res.getString("Numero"), res.getString("Escalera"), res.getInt("Piso"),
                                    res.getString("Puerta"), res.getInt("tlf_Fijo"), res.getInt("tlf_Movil"),
                                    res.getFloat("Sueldo_base"), res.getFloat("Precio_hora"),
                                    res.getString("Correo_electronico"), res.getBoolean("activo")));
                        else
                            empleados.put(res.getInt("id_Usuario"),new Gerente(res.getString("Nombre"), res.getString("Apellidos"), res.getString("DNI"),
                                    res.getString("Poblacion"), res.getString("Provincia"), res.getInt("CP"),
                                    res.getString("Calle"), res.getString("Numero"), res.getString("Escalera"), res.getInt("Piso"),
                                    res.getString("Puerta"), res.getInt("tlf_Fijo"), res.getInt("tlf_Movil"),
                                    res.getFloat("Sueldo_base"), res.getFloat("Precio_hora"),
                                    res.getString("Correo_electronico"), res.getBoolean("activo")));
                        }
                return empleados;
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        
        public Empleado getEmpleado(int id){
            return (Empleado)empleados.get(id);
        }
        
        public Empleado cambiarUsuario(int id){
            Empleado aux = (Empleado)empleados.get(id);
            
            if(aux.getClass().getName().equals("GestoSAT.Empleado"))
                empleados.put(id,new Gerente(aux.getNombre(), aux.getApellidos(), aux.getDni(),
                aux.getPoblacion(), aux.getProvincia(), aux.getCp(), aux.getCalle(), aux.getNumero(),
                aux.getEscalera(), aux.getPiso(), aux.getPuerta(), aux.getTlfFijo(), aux.getTlfMovil(),
                aux.getSueldoBase(), aux.getPrecioHora(), aux.getEmail(), aux.isActivo()));
            else
                empleados.put(id, new Empleado(aux.getNombre(), aux.getApellidos(), aux.getDni(),
                aux.getPoblacion(), aux.getProvincia(), aux.getCp(), aux.getCalle(), aux.getNumero(),
                aux.getEscalera(), aux.getPiso(), aux.getPuerta(), aux.getTlfFijo(), aux.getTlfMovil(),
                aux.getSueldoBase(), aux.getPrecioHora(), aux.getEmail(), aux.isActivo()));
        
            return (Empleado)(empleados.get(id));
        }
        
        public boolean guardarEmpleado(Empleado emp, int id){
            try {
                Statement st = con.createStatement();
                int rango;
                if(emp.getClass().getName().equals("GestoSAT.Empleado"))
                    rango = 0;
                else
                    rango = 1;
                Class.forName("com.mysql.jdbc.Driver");
                if(st.executeUpdate("Update usuarios SET Precio_hora='"+emp.getPrecioHora()+ "',"
                        + " Sueldo_base='"+emp.getSueldoBase()+"',"
                        + "Gerente='"+rango+"' Where id_Usuario='"+id+"'")>0)
                    return true;
                else
                    return false;
                
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        
        public boolean deleteUser(int id){
            try {
                Statement st = con.createStatement();
                Class.forName("com.mysql.jdbc.Driver");
                
                if(st.executeUpdate("DELETE FROM usuarios Where id_Usuario='"+id+"'")>0){
                    empleados.remove(id);
                    return true;
                }else
                    return false;
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        
        public boolean desactivar(int id){
            try {
                Statement st = con.createStatement();
                Class.forName("com.mysql.jdbc.Driver");
                
                if(st.executeUpdate("Update usuarios SET Activo='0' Where id_Usuario='"+id+"'")>0)
                    return true;
                else
                    return false;
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        
        public boolean activar(int id){
            try {
                Statement st = con.createStatement();
                Class.forName("com.mysql.jdbc.Driver");
                
                if(st.executeUpdate("Update usuarios SET Activo='1' Where id_Usuario='"+id+"'")>0)
                    return true;
                else
                    return false;
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        
        public boolean crearProveedor(Proveedor p){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Statement st = con.createStatement();
                
                if(st.executeUpdate("INSERT INTO proveedores (Nombre, "
                        + " NIF, Provincia, Poblacion, CP, "
                        + "Calle, Numero, Escalera, Piso, Puerta, tlf_Fijo, tlf_Movil, "
                        + "Correo_electronico, Fax) VALUES ('"+p.getNombre()+"',"
                        + " '"+p.getNif()+"','"+p.getProvincia()+"','"+p.getPoblacion()+"'"
                        + ",'"+p.getCp()+"','"+p.getCalle()+"','"+p.getNumero()+"','"+p.getEscalera()+"',"
                        + "'"+p.getPiso()+"', '"+p.getPuerta()+"', '"+p.getTlfFijo()+"','"+p.getTlfMovil()+"','"
                        + p.getEmail()+"','"+p.getFax()+"')")>0)
                    return true;
                else
                    return false;
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        
        public Map getProveedores(){
            try {
                    Statement st = con.createStatement();
                    ResultSet res = st.executeQuery("Select * From proveedores");
                    while(res.next())
                        if(this.proveedor.putIfAbsent(res.getInt("id_Proveedor"),new Proveedor(
                            res.getString("Nombre"), res.getString("NIF"),
                            res.getString("Provincia"), res.getString("Poblacion"), 
                            res.getInt("CP"), res.getString("Calle"),
                            res.getString("Numero"), res.getString("Escalera"),
                            res.getInt("Piso"), res.getString("Puerta"),
                            res.getInt("tlf_Fijo"), res.getInt("tlf_Movil"),
                            res.getString("Correo_electronico"), res.getInt("Fax"),this))!= null)
                                ((Proveedor)this.proveedor.get(res.getInt("id_Proveedor"))).actualizar(
                                    res.getString("Nombre"), res.getString("NIF"),
                                    res.getString("Provincia"), res.getString("Poblacion"), 
                                    res.getInt("CP"), res.getString("Calle"),
                                    res.getString("Numero"), res.getString("Escalera"),
                                    res.getInt("Piso"), res.getString("Puerta"),
                                    res.getInt("tlf_Fijo"), res.getInt("tlf_Movil"),
                                    res.getString("Correo_electronico"), res.getInt("Fax"));
                    st.close();
                    return this.proveedor;
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return new HashMap();
            }
        }
        
        public boolean eliminarProveedor(int id){
            try {
                Statement st = con.createStatement();
                Class.forName("com.mysql.jdbc.Driver");
                
                if(st.executeUpdate("DELETE FROM proveedores Where id_Proveedor='"+id+"'")>0){
                    proveedor.remove(id);
                    return true;
                }else
                    return false;
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        
        public Proveedor getProveedor(int id){
            return (Proveedor)proveedor.get(id);
        }
        
        //Modificar proveedor
        public boolean modificarProveedor(int id, Proveedor prov){
            try {
                Statement st = con.createStatement();
                Class.forName("com.mysql.jdbc.Driver");
                if(st.executeUpdate("Update proveedores SET "
                        + "Nombre='"+prov.getNombre()+"',"
                        + "NIF='"+prov.getNif()+"',"
                        + "Provincia='"+prov.getProvincia()+"',"
                        + "Poblacion='"+prov.getPoblacion()+"',"
                        + "CP='"+prov.getCp()+"',"
                        + "Calle='"+prov.getCalle()+"',"
                        + "Numero='"+prov.getNumero()+"',"
                        + "Escalera='"+prov.getEscalera()+"',"
                        + "Piso='"+prov.getPiso()+"',"
                        + "Puerta='"+prov.getPuerta()+"',"
                        + "tlf_Fijo='"+prov.getTlfFijo()+"',"
                        + "tlf_Movil='"+prov.getTlfMovil()+"',"
                        + "Correo_electronico='"+prov.getEmail()+"',"
                        + "Fax='"+prov.getFax()+"'"
                        + " Where id_Proveedor='"+id+"'")>0)
                    return true;
                else
                    return false;
                
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        
        public boolean addStock(int id, Stock s){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Statement st = con.createStatement();
                Statement stStock = con.createStatement();
                
                if(st.executeUpdate("INSERT INTO stock (Nombre, Descripcion, Precio_compra,"
                        + "Unidades, Precio_venta, num_Alertar,id_Proveedor) VALUES ("
                        + "'"+s.getNombre()+"','"+s.getDescripcion()+"','"+s.getPrecioCompra()+"',"
                        + "'"+Math.abs(s.getCantidad())+"','"+s.getPrecioUnidad()+"','"+Math.abs(s.getAlerta())+"',"
                        + "'"+id+"')")>0){
                    // Rcuperar datos
                    ResultSet res = stStock.executeQuery("Select id_Stock FROM stock WHERE id_Proveedor='"+id+"' AND Nombre ='"+s.getNombre()+"' ORDER BY id_Proveedor DESC");
                    res.next();
                    this.getProveedor(id).setStock(res.getInt("id_Stock"), s);
                    return true;
            }else
                    return false;
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        
        public Map getStock(){
        
            try {
                Map stock = new HashMap();
                Class.forName("com.mysql.jdbc.Driver");
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("Select * From stock S INNER JOIN proveedores P ON S.id_proveedor = P.id_Proveedor");
                Proveedor p;
                    this.getProveedores();
                    while(res.next()){
                        p=(Proveedor)this.proveedor.get(res.getInt("P.id_Proveedor"));
                        Stock s = new Stock(res.getString("S.Nombre"),
                                res.getFloat("S.Unidades"),res.getFloat("S.Precio_venta"),
                                (String)res.getObject("S.Descripcion"),res.getFloat("S.Precio_compra"),
                                res.getFloat("S.num_Alertar"),p);
                        s = p.setStock(res.getInt("s.id_Stock"), s);
                        stock.putIfAbsent(res.getInt("S.id_Stock"), s);
                    }
                        return stock;
                    } catch (Exception ex) {
                        Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                        return new HashMap();
                    }    
        }
        
        public boolean actualizarStock(int id, float cantidad){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("Select Unidades From stock Where id_Stock='"+id+"'");
                        
                if(res.next()){
                    if(st.executeUpdate("Update stock SET Unidades='"+(Math.abs(cantidad)+res.getFloat("Unidades"))+"' Where id_Stock='"+id+"'")> 0)
                        return true;
                    else
                        return false;
                }else
                    return false;
                
            } catch (Exception ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
            }
        }
        
        public boolean actualizarStock(Integer id, String nombre, String desc,
                float precioCompra, float precioVenta, float alerta){
            try {
                Statement st = con.createStatement();
                Class.forName("com.mysql.jdbc.Driver");
                if(st.executeUpdate("Update stock SET "
                        + "Nombre='"+nombre+"',"
                        + "Descripcion='"+desc+"',"
                        + "Precio_compra='"+Math.abs(precioCompra)+"',"
                        + "Precio_venta='"+Math.abs(precioVenta)+"',"
                        + "num_Alertar='"+Math.abs(alerta)+"'"
                        + " Where id_Stock='"+id+"'")>0)
                    return true;
                else
                    return false;
                
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        
        public int crearCliente(Cliente cliente){
            try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Statement st = con.createStatement();

                    st.executeUpdate("INSERT INTO clientes (Nombre, Apellidos,"
                            + " NIF, Provincia, Poblacion, CP, "
                            + "Calle, Numero, Escalera, Piso, Puerta, tlf_Contacto, tlf_Auxiliar, "
                            + "Correo_electronico, Observaciones) VALUES ('"+cliente.getNombre()+"',"
                            + "'"+cliente.getApellidos()+"',"
                            + " '"+cliente.getNif()+"','"+cliente.getProvincia()+"','"+cliente.getPoblacion()+"'"
                            + ",'"+cliente.getCp()+"','"+cliente.getCalle()+"','"+cliente.getNumero()+"','"+cliente.getEscalera()+"',"
                            + "'"+cliente.getPiso()+"', '"+cliente.getPuerta()+"', '"+cliente.getTlfFijo()+"','"+cliente.getTlfMovil()+"','"
                            + cliente.getEmail()+"','"+cliente.getObservaciones()+"')");
                    
                    ResultSet res = st.executeQuery("SELECT * FROM clientes Order BY id_Cliente DESC");
                    if(res.next()){
                        int id = res.getInt("id_Cliente");
                        this.cliente.putIfAbsent(id, cliente);
                        st.close();
                        return id;
                    }else{ st.close();  return 0;}
            } catch (Exception ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                    return 0;
            }
        }
        
        public int guardarAparato(Aparato aparato, int id){
            try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Statement st = con.createStatement();

                    st.executeUpdate("INSERT INTO aparatos (Tipo, Marca,"
                            + " Modelo, Color, Numero_serie, Observaciones, id_Cliente)"
                            + " VALUES ('"+aparato.getTipo()+"',"
                            + "'"+aparato.getMarca()+"',"
                            + " '"+aparato.getModelo()+"','"+aparato.getColor()+"','"+aparato.getNumSerie()+"'"
                            + ",'"+aparato.getObservaciones()+"','"+id+"')");
                    st.close();
                    st = con.createStatement();
                    ResultSet res = st.executeQuery("SELECT * FROM aparatos Order BY id_Aparato DESC");
                    if(res.next()){
                        int id_Aparato = res.getInt("id_Aparato");
                        st.close();
                        aparato.getCliente().setAparato(aparato, id_Aparato);
                        return id_Aparato;
                    }else{
                        st.close();
                        return 0;
                    }
            } catch (Exception ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                    return 0;
            }
        }

        public int guardarEntrada(Entrada entrada, int id_Usuario){
            try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Statement st = con.createStatement();
                    
                    st.executeUpdate("INSERT INTO entradas (fch_Entrada, Lugar,"
                            + " Observaciones, id_Usuario)"
                            + " VALUES ('"+((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(entrada.getFchCreacion()))+"',"
                            + "'"+entrada.getLugar()+"',"
                            + " '"+entrada.getObservaciones()+"','"+id_Usuario+"')");
                    st.close();
                    st = con.createStatement();
                    ResultSet res = st.executeQuery("SELECT * FROM entradas Order BY id_Entrada DESC");
                    if(res.next()){
                        int id_Entrada = res.getInt("id_Entrada");
                        st.close();
                        entrada.getCliente().setEntrada(id_Entrada, entrada);
                        this.documento.put(id_Entrada+";Entrada",entrada);
                        return id_Entrada;
                    }else{ 
                        st.close();
                        return 0;
                    }
            } catch (Exception ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                    return 0;
            }
        }
        
        public void guardarAveria(Averia averia, int id_Entrada, int id_Aparato){
            try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Statement st = con.createStatement();

                    st.executeUpdate("INSERT INTO averias (id_Entrada, id_Aparato,"
                            + " Motivo, Descripcion)"
                            + " VALUES ('"+id_Entrada+"',"
                            + "'"+id_Aparato+"',"
                            + " '"+averia.getMotivo()+"','"+averia.getObservaciones()+"')");
                    st.close();
            } catch (Exception ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public Map getCliente(){
            try {
                    Statement st = con.createStatement();
                    ResultSet res = st.executeQuery("Select C.*, GROUP_CONCAT(REPLACE(A.Observaciones,',','~')) Observaciones_Aparato,"
                            + " GROUP_CONCAT(A.id_Aparato) id_Aparato, GROUP_CONCAT(A.Marca) Marca,"
                            + " GROUP_CONCAT(A.Tipo) Tipo, GROUP_CONCAT(A.Modelo) Modelo,"
                            + " GROUP_CONCAT(A.Color) Color, GROUP_CONCAT(A.Numero_Serie) Numero_Serie"
                            + " From clientes C LEFT JOIN aparatos A ON C.id_Cliente = A.id_Cliente GROUP BY C.id_Cliente");
                    String[] ids_Aparato;
                    String[] tipos;
                    String[] marcas;
                    String[] modelos;
                    String[] colores;
                    String[] numeros_Serie;
                    String[] observaciones;
                    Map aparatos;
                    while(res.next()){
                        aparatos = new HashMap();
                        if(res.getString("id_Aparato")!= null){ // Clients que tenen aparatos
                            ids_Aparato = res.getString("id_Aparato").split(",");
                            tipos = res.getString("Tipo").replaceAll(","," , ").split(",");
                            marcas = res.getString("Marca").replaceAll(","," , ").split(",");
                            modelos = res.getString("Modelo").replaceAll(","," , ").split(",");
                            colores = res.getString("Color").replaceAll(","," , ").split(",");
                            numeros_Serie = res.getString("Numero_Serie").replaceAll(","," , ").split(",");
                            observaciones = res.getString("Observaciones_Aparato").replaceAll(","," , ").split(",");
                            for(int i =0; i < ids_Aparato.length;i++)
                                aparatos.put(Integer.parseInt(ids_Aparato[i]), new Aparato(tipos[i].replaceFirst(" ","")
                                        ,marcas[i].replaceFirst(" ",""),modelos[i].replaceFirst(" ","")
                                        ,colores[i].replaceFirst(" ",""),numeros_Serie[i].replaceFirst(" ",""),
                                        observaciones[i].replaceFirst(" ","")));
                        }
                        cliente.putIfAbsent(res.getInt("id_Cliente"),new Cliente(res.getString("C.Nombre"),res.getString("C.Apellidos"),res.getString("C.NIF"),
                                    res.getString("C.Provincia"), res.getString("C.Poblacion"), res.getInt("C.CP"),
                                    res.getString("C.Calle"),res.getString("C.Numero"), res.getString("C.Escalera"), res.getInt("C.Piso"),
                                    res.getString("C.Puerta"), res.getInt("C.tlf_Contacto"), res.getInt("C.tlf_Auxiliar"),
                                    res.getString("C.Correo_electronico"),res.getString("C.Observaciones"),aparatos,this));
                    }
                   
                    return cliente;
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        
        public Cliente getCliente(int id){
            
            if(cliente.get(id)==null){
                Statement st;
                try {
                    st = con.createStatement();
                    ResultSet res = st.executeQuery("SELECT * FROM clientes WHERE id_cliente="+id);
                    if(res.next())
                        this.cliente.put(id, new Cliente(res.getString("nombre"), res.getString("apellidos"), res.getString("nif"),
                                res.getString("provincia"), res.getString("poblacion"), res.getInt("cp"), res.getString("calle"),
                                res.getString("numero"), res.getString("escalera"), res.getInt("piso"), res.getString("puerta"),
                                res.getInt("tlf_contacto"), res.getInt("tlf_auxiliar"), res.getString("correo_electronico"),
                                res.getString("observaciones"), this));
                    
                 } catch (SQLException ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                }
           }
            
            return (Cliente)cliente.get(id);
        }
        
        public int guardarDireccion(String provincia, String poblacion, int cp, String calle,
                String numero, String escalera, int piso, String puerta, String observaciones){
            try{    
                Class.forName("com.mysql.jdbc.Driver");
                    Statement st = con.createStatement();

                    st.executeUpdate("INSERT INTO direccion_citas (Provincia, Poblacion,"
                            + "CP, Calle, Numero, Escalera, Piso, Puerta, Observaciones)"
                            + " VALUES ('"+provincia+"','"+poblacion+"','"+Math.abs(cp)+"',"
                            + "'"+calle+"','"+numero+"','"+escalera+"','"+Math.abs(piso)+"',"
                            + "'"+puerta+"','"+observaciones+"')");
                    ResultSet res = st.executeQuery("SELECT * FROM direccion_citas Order by id_Direccion DESC");
                    res.next();
                    int id = res.getInt("id_Direccion");
                    st.close();
                    return id;
            }catch (Exception ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                    return 0;
            }
        }
        
        public void guardarCita(Cita cita, int id_Cliente, int id_Entrada, int direccion){
            try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Statement st = con.createStatement();

                    st.executeUpdate("INSERT INTO citas (fch_Cita, Motivo, Direccion, "
                            + "Observaciones, id_Cliente, id_Entrada)"
                            + " VALUES ('"+((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(cita.getFchCita()))+"', "
                            + "'"+cita.getMotivo()+"', '"+direccion+"', '"+cita.getObservaciones()+"', "
                            + "'"+id_Cliente+"','"+id_Entrada+"')");
                    st.close();
                    
            } catch (Exception ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public Map getAparatosCliente(int idCliente){
            return ((Cliente)this.cliente.get(idCliente)).getAparatos();
        }
        
        public Map getCitas(int id_Cliente){
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("Select id_Direccion, IF(DC.Provincia <> '0', DC.Provincia,'') Provincia,"
                        + "IF(DC.Poblacion <>'0',DC.Poblacion,'') Poblacion, "
                        + "IF(DC.CP>0,DC.CP,'') CP, IF(DC.Calle<>'0',DC.Calle,'') Calle, "
                        + "IF(DC.Numero<>'0',DC.Numero,'') Numero, IF(DC.Escalera<>'0',DC.Escalera,'') Escalera,"
                        + "IF(DC.Piso>0,DC.Piso,'') Piso, IF(DC.Puerta<>'0',DC.Puerta,'') Puerta, "
                        + "if(DC.Observaciones<>'0',DC.Observaciones,'') Observaciones "
                        + "From citas C INNER JOIN direccion_citas DC ON C.Direccion = DC.id_Direccion "
                        + "Where C.id_Cliente="+id_Cliente+" GROUP BY DC.id_Direccion");
                Map citas = new HashMap();
                Map valores;
                while(res.next()){
                    valores = new HashMap();
                    valores.put("provincia", res.getString("Provincia"));
                    valores.put("poblacion", res.getString("Poblacion"));
                    valores.put("cp", res.getString("CP"));
                    valores.put("calle", res.getString("Calle"));
                    valores.put("numero", res.getString("Numero"));
                    valores.put("escalera", res.getString("Escalera"));
                    valores.put("piso", res.getString("Piso"));
                    valores.put("puerta", res.getString("Puerta"));
                    valores.put("observaciones", res.getString("Observaciones"));
                    citas.put(res.getInt("id_Direccion"), valores);
                }
                return citas;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        
    public Map entradasPendientes(){
        try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("Select e.*, cli.*,av.*, ap.* FROM (entradas e LEFT JOIN (presupuestos p "
                        + "LEFT JOIN albaranes a ON p.id_Presupuesto = a.id_Presupuesto) ON e.id_Entrada = p.id_Entrada ) INNER JOIN "
                        + "((clientes cli INNER JOIN aparatos ap ON cli.id_Cliente = ap.id_Cliente) INNER JOIN averias av ON "
                        + "ap.id_Aparato = av.id_Aparato) ON av.id_Entrada = e.id_Entrada WHERE a.id_albaran IS NULL ORDER BY fch_Entrada DESC ");
                Map entradas = new HashMap();
                Cliente cli;
                Entrada entrada;
                while(res.next()){
                    if(this.documento.get(res.getString("id_Entrada")+";Entrada")== null){
                        if(this.cliente.get(res.getInt("cli.id_Cliente"))==null){
                            cli = new Cliente(res.getString("cli.nombre"),res.getString("cli.apellidos"),res.getString("cli.nif"),
                                        res.getString("cli.provincia"),res.getString("cli.poblacion"),
                                        res.getInt("cli.cp"),res.getString("cli.calle"),res.getString("cli.numero"),
                                        res.getString("cli.escalera"),res.getInt("cli.piso"),res.getString("cli.puerta"),
                                        res.getInt("cli.tlf_Contacto"), res.getInt("cli.tlf_Auxiliar"),
                                        res.getString("cli.correo_electronico"),res.getString("cli.observaciones"),this);

                            this.cliente.putIfAbsent(res.getInt("cli.id_Cliente"), cli);
                        }else{
                            cli = (Cliente)this.cliente.get(res.getInt("cli.id_Cliente"));
                        }
                        
                        entrada = new Entrada(res.getString("e.observaciones"), res.getString("e.lugar"),
                                res.getTimestamp("e.fch_Entrada"),this,cli,
                                new Averia(res.getString("av.motivo"),res.getString("av.descripcion"),
                                    new Aparato(res.getString("ap.tipo"), res.getString("ap.marca"),
                                            res.getString("ap.modelo"), res.getString("ap.color"),
                                            res.getString("ap.numero_serie"),res.getString("ap.observaciones"),cli,res.getInt("ap.id_Aparato")),null));
                        
                        cli.setEntrada(res.getInt("id_Entrada"), entrada);
                        
                        entradas.put(res.getString("id_Entrada")+";Entrada",entrada);
                    }else{
                        entrada = (Entrada)this.documento.get(res.getString("id_Entrada")+";Entrada");
                        entrada.actualizar(res.getString("e.observaciones"), res.getString("e.lugar"),res.getString("av.motivo"));
                        entradas.put(res.getString("id_Entrada")+";Entrada", entrada);
                    }
                }
                
                this.documento.putAll(entradas);
                
                return entradas;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
    }
    
    public Map getProximasCitas(){
        try {
                Statement st = con.createStatement(); // Recuperar citas y no averias
                ResultSet res = st.executeQuery("Select e.*, cli.*,cita.*, dir.*, GROUP_CONCAT(c_e.id_Usuario) empleados FROM (entradas e LEFT JOIN"
                        + " (presupuestos p LEFT JOIN albaranes a ON p.id_Presupuesto = a.id_Presupuesto) ON e.id_Entrada = p.id_Entrada) INNER JOIN "
                        + "((direccion_citas dir INNER JOIN ((citas cita LEFT JOIN citas_empleados c_e ON cita.id_Cita = c_e.id_Cita)"
                        + " LEFT JOIN usuarios u ON u.id_Usuario = c_e.id_Usuario) ON cita.Direccion = dir.id_Direccion )"
                        + " INNER JOIN clientes cli ON cli.id_Cliente = cita.id_Cliente) ON e.id_entrada = cita.id_Entrada"
                        + " WHERE a.id_albaran IS NULL GROUP BY cita.id_Cita ORDER BY fch_Entrada DESC");
                Map citas = new HashMap();
                Cliente cli;
                Entrada entrada;
                while(res.next()){
                    Map aux_empleados = new HashMap();
                    if(res.getString("empleados") != null){
                        String[] aux = res.getString("empleados").split(",");

                        for(int i = 0;i < aux.length; i++)
                            aux_empleados.put(aux[i],this.getEmpleado(Integer.parseInt(aux[i])));
                    }
                        
                    if(this.documento.get(res.getString("id_Entrada")+";Entrada")== null){
                        if(this.cliente.get(res.getInt("cli.id_Cliente"))==null){
                            cli = new Cliente(res.getString("cli.nombre"),res.getString("cli.apellidos"),res.getString("cli.nif"),
                                        res.getString("cli.provincia"),res.getString("cli.poblacion"),
                                        res.getInt("cli.cp"),res.getString("cli.calle"),res.getString("cli.numero"),
                                        res.getString("cli.escalera"),res.getInt("cli.piso"),res.getString("cli.puerta"),
                                        res.getInt("cli.tlf_Contacto"), res.getInt("cli.tlf_Auxiliar"),
                                        res.getString("cli.correo_electronico"),res.getString("cli.observaciones"),this);

                            this.cliente.putIfAbsent(res.getInt("cli.id_Cliente"), cli);
                        }else{
                            cli = (Cliente)this.cliente.get(res.getInt("cli.id_Cliente"));
                        }
                        entrada = new Entrada(res.getString("e.observaciones"), res.getString("e.lugar"),
                            res.getTimestamp("e.fch_Entrada"),this,cli,
                        new Cita(res.getTimestamp("cita.fch_Cita"), res.getString("dir.provincia"),
                        res.getString("dir.poblacion"), res.getInt("dir.cp"), res.getString("dir.calle"),res.getString("dir.numero"),
                        res.getString("dir.escalera"), res.getInt("dir.piso"), res.getString("dir.puerta"), 
                        aux_empleados ,res.getString("cita.motivo"),
                        res.getString("cita.observaciones"), res.getString("dir.observaciones"),null));
                        
                        cli.setEntrada(res.getInt("id_Entrada"), entrada);
                        
                        citas.put(res.getString("id_Entrada")+";Entrada", entrada);
                    }else{
                        entrada = (Entrada)this.documento.get(res.getString("id_Entrada")+";Entrada");
                        entrada.actualizar(res.getString("e.observaciones"), res.getTimestamp("cita.fch_Cita"), res.getString("cita.motivo"),res.getString("cita.observaciones"),aux_empleados);
                        citas.put(res.getString("id_Entrada")+";Entrada", entrada);
                    }
                    
                }
                
                this.documento.putAll(citas);
                
                return citas;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
    }
    
        public Map getCitasPendientes(){ // Problemes en allbaran
        try {
            Calendar next7Days = Calendar.getInstance();
            next7Days.setTime(new Date());
            next7Days.add(Calendar.DAY_OF_YEAR,7);
                Statement st = con.createStatement(); // Recuperar citas hoy
                ResultSet res = st.executeQuery("Select e.*, cli.*,cita.*, dir.*, GROUP_CONCAT(c_e.id_Usuario) empleados FROM (entradas e LEFT JOIN "
                        + " (presupuestos p LEFT JOIN albaranes a ON p.id_Presupuesto = a.id_Presupuesto) ON e.id_Entrada = p.id_Entrada) INNER JOIN "
                        + " ((direccion_citas dir INNER JOIN ((citas cita LEFT JOIN citas_empleados c_e ON "
                        + " c_e.id_Cita = cita.id_Cita) LEFT JOIN usuarios u ON c_e.id_Usuario = u.id_Usuario)"
                        + " ON cita.Direccion = dir.id_Direccion )"
                        + " INNER JOIN clientes cli ON cli.id_Cliente = cita.id_Cliente) ON e.id_entrada = cita.id_Entrada"
                        + " Where a.id_albaran IS NULL AND cita.fch_Cita <= '"+((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(next7Days.getTime()))+"'" // Next 7 days
                        + " GROUP BY cita.id_Cita ORDER BY cita.fch_Cita DESC"); 
                Map citas = new HashMap();
                Cliente cli;
                Entrada entrada;
                while(res.next()){ // Cambiar por citas
                    Map aux_empleados = new HashMap();

                    if(res.getString("empleados") != null){
                        String[] aux = res.getString("empleados").split(",");

                        for(int i = 0;i < aux.length; i++)
                            aux_empleados.put(aux[i],this.getEmpleado(Integer.parseInt(aux[i])));
                    }
                    
                    if(this.documento.get(res.getString("id_Entrada")+";Entrada")== null){
                        
                        if(this.cliente.get(res.getInt("cli.id_Cliente"))==null){
                            cli = new Cliente(res.getString("cli.nombre"),res.getString("cli.apellidos"),res.getString("cli.nif"),
                                        res.getString("cli.provincia"),res.getString("cli.poblacion"),
                                        res.getInt("cli.cp"),res.getString("cli.calle"),res.getString("cli.numero"),
                                        res.getString("cli.escalera"),res.getInt("cli.piso"),res.getString("cli.puerta"),
                                        res.getInt("cli.tlf_Contacto"), res.getInt("cli.tlf_Auxiliar"),
                                        res.getString("cli.correo_electronico"),res.getString("cli.observaciones"),this);

                            this.cliente.putIfAbsent(res.getInt("cli.id_Cliente"), cli);
                        }else{
                            cli = (Cliente)this.cliente.get(res.getInt("cli.id_Cliente"));
                        }
                        
                        Entrada e = new Entrada(res.getString("e.observaciones"), res.getString("e.lugar"),
                                res.getTimestamp("e.fch_Entrada"),this,cli,
                            new Cita(res.getTimestamp("cita.fch_Cita"), res.getString("dir.provincia"),
                            res.getString("dir.poblacion"), res.getInt("dir.cp"), res.getString("dir.calle"),res.getString("dir.numero"),
                            res.getString("dir.escalera"), res.getInt("dir.piso"), res.getString("dir.puerta"),
                            aux_empleados, res.getString("cita.motivo"),res.getString("cita.observaciones"),
                            res.getString("dir.observaciones"),null));

                            cli.setEntrada(res.getInt("id_Entrada"), e);
                        
                        if(((Entrada)this.documento.get(res.getString("id_Entrada")+";Entrada"))!= null)
                            if(((Entrada)this.documento.get(res.getString("id_Entrada")+";Entrada")).getPresupuesto() != null){
                                e.setPresupuesto(((Entrada)this.documento.get(res.getString("id_Entrada")+";Entrada")).getPresupuesto());
                                e.getPresupuesto().setEntrada(e);
                            }

                        citas.put(res.getString("id_Entrada")+";Entrada",e);
                    }else{
                        entrada = (Entrada)this.documento.get(res.getString("id_Entrada")+";Entrada");
                        entrada.actualizar(res.getString("e.observaciones"), res.getTimestamp("cita.fch_Cita"), res.getString("cita.motivo"),res.getString("cita.observaciones"),aux_empleados);
                        citas.put(res.getString("id_Entrada")+";Entrada", entrada);
                    }
                }
                
                this.documento.putAll(citas);
                
                return citas;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
    }
    
    public Map getEntradasPendientes(){
        Map entradas = new HashMap();
        entradas.putAll(this.getProximasCitas());
        entradas.putAll(this.entradasPendientes());
        return entradas;
    } 
    
    public Entrada getEntrada(int idEntrada){
        if(this.documento.get(idEntrada+";Entrada")!=null)
            return (Entrada)this.documento.get(idEntrada+";Entrada");
        else{
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("Select e.*, cli.*,av.*, ap.* FROM (entradas e LEFT JOIN (presupuestos p "
                        + "LEFT JOIN albaranes a ON p.id_Presupuesto = a.id_Presupuesto) ON e.id_Entrada = p.id_Entrada ) INNER JOIN "
                        + "((clientes cli INNER JOIN aparatos ap ON cli.id_Cliente = ap.id_Cliente) INNER JOIN averias av ON "
                        + "ap.id_Aparato = av.id_Aparato) ON av.id_Entrada = e.id_Entrada WHERE e.id_Entrada="+idEntrada+" ORDER BY fch_Entrada DESC");
                Entrada entrada = null;
                Cliente cli;
                    
                if(res.next()){
                    if(this.cliente.get(res.getInt("cli.id_Cliente"))==null){
                            cli = new Cliente(res.getString("cli.nombre"),res.getString("cli.apellidos"),res.getString("cli.nif"),
                                        res.getString("cli.provincia"),res.getString("cli.poblacion"),
                                        res.getInt("cli.cp"),res.getString("cli.calle"),res.getString("cli.numero"),
                                        res.getString("cli.escalera"),res.getInt("cli.piso"),res.getString("cli.puerta"),
                                        res.getInt("cli.tlf_Contacto"), res.getInt("cli.tlf_Auxiliar"),
                                        res.getString("cli.correo_electronico"),res.getString("cli.observaciones"),this);

                            this.cliente.putIfAbsent(res.getInt("cli.id_Cliente"), cli);
                        }else{
                            cli = (Cliente)this.cliente.get(res.getInt("cli.id_Cliente"));
                        }
                        
                        entrada = new Entrada(res.getString("e.observaciones"), res.getString("e.lugar"),
                                res.getTimestamp("e.fch_Entrada"),this,cli,
                                new Averia(res.getString("av.motivo"),res.getString("av.descripcion"),
                                    new Aparato(res.getString("ap.tipo"), res.getString("ap.marca"),
                                            res.getString("ap.modelo"), res.getString("ap.color"),
                                            res.getString("ap.numero_serie"),res.getString("ap.observaciones"),cli,res.getInt("ap.id_Aparato")),null));
                        
                        cli.setEntrada(res.getInt("id_Entrada"), entrada);
                }else{
                    res = st.executeQuery("Select e.*, cli.*,cita.*, dir.*, GROUP_CONCAT(c_e.id_Usuario) empleados FROM (entradas e LEFT JOIN "
                        + " (presupuestos p LEFT JOIN albaranes a ON p.id_Presupuesto = a.id_Presupuesto) ON e.id_Entrada = p.id_Entrada) INNER JOIN "
                        + " ((direccion_citas dir INNER JOIN ((citas cita LEFT JOIN citas_empleados c_e ON "
                        + " c_e.id_Cita = cita.id_Cita) LEFT JOIN usuarios u ON c_e.id_Usuario = u.id_Usuario)"
                        + " ON cita.Direccion = dir.id_Direccion )"
                        + " INNER JOIN clientes cli ON cli.id_Cliente = cita.id_Cliente) ON e.id_entrada = cita.id_Entrada"
                        + " Where e.id_Entrada="+idEntrada+" GROUP BY cita.id_Cita ORDER BY cita.fch_Cita DESC");
                    res.next();
                        
                    if(this.documento.get(res.getString("id_Entrada")+";Entrada")== null){
                        if(this.cliente.get(res.getInt("cli.id_Cliente"))==null){
                            cli = new Cliente(res.getString("cli.nombre"),res.getString("cli.apellidos"),res.getString("cli.nif"),
                                        res.getString("cli.provincia"),res.getString("cli.poblacion"),
                                        res.getInt("cli.cp"),res.getString("cli.calle"),res.getString("cli.numero"),
                                        res.getString("cli.escalera"),res.getInt("cli.piso"),res.getString("cli.puerta"),
                                        res.getInt("cli.tlf_Contacto"), res.getInt("cli.tlf_Auxiliar"),
                                        res.getString("cli.correo_electronico"),res.getString("cli.observaciones"),this);

                            this.cliente.putIfAbsent(res.getInt("cli.id_Cliente"), cli);
                        }else{
                            cli = (Cliente)this.cliente.get(res.getInt("cli.id_Cliente"));
                        }
                        Map aux_empleados = new HashMap();
                        
                        if(res.getString("empleados") != null){
                            String[] aux = res.getString("empleados").split(",");

                            for(int i = 0;i < aux.length; i++)
                                aux_empleados.put(aux[i],this.getEmpleado(Integer.parseInt(aux[i])));
                        }
                        
                        entrada = new Entrada(res.getString("e.observaciones"), res.getString("e.lugar"),
                            res.getTimestamp("e.fch_Entrada"),this,cli,
                        new Cita(res.getTimestamp("cita.fch_Cita"), res.getString("dir.provincia"),
                        res.getString("dir.poblacion"), res.getInt("dir.cp"), res.getString("dir.calle"),res.getString("dir.numero"),
                        res.getString("dir.escalera"), res.getInt("dir.piso"), res.getString("dir.puerta"), 
                        aux_empleados ,res.getString("cita.motivo"),
                        res.getString("cita.observaciones"), res.getString("dir.observaciones"),null));
                        
                        cli.setEntrada(res.getInt("id_Entrada"), entrada);
                        
                    }
                }
                return entrada;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }
    
    public boolean editarAveria(String id, String obs, String lugar, String motivo){
        try {
            getEntrada(Integer.parseInt(id)).actualizar(obs, lugar, motivo);
                Class.forName("com.mysql.jdbc.Driver");
                Statement st = con.createStatement();
                if(st.executeUpdate("UPDATE entradas e INNER JOIN averias av ON "
                        + " av.id_Entrada = e.id_Entrada "
                        + " SET e.Observaciones='"+obs+"', "
                        + "e.lugar='"+lugar+"',"
                        + "av.motivo='"+motivo+"' Where e.id_Entrada='"+id+"'")> 0)
                    return true;
                else
                    return false;
            } catch (Exception ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
            }
    }
    
    public boolean editarCita(String id, String obs_Entrada, Date fecha, String motivo, String obs_Cita, String empleados){
        try {   // Fer us de la funcio actualisar
                
                Entrada entrada = this.getEntrada(Integer.parseInt(id));
                Cita cita = (Cita)entrada.getIncidencia();
                Class.forName("com.mysql.jdbc.Driver");
                Statement st = con.createStatement();
                Statement stWorkers = con.createStatement();
                
                String[] idEmpleados = empleados.split(",");
                boolean[] existentes;
                //ResultSet auxRes;
                String[] workers;
                
                ResultSet allWorkers = stWorkers.executeQuery("SELECT c.id_Cita id_Cita,GROUP_CONCAT(c_e.id_Usuario) empleados FROM "
                        + "((citas_empleados c_e RIGHT JOIN citas c ON c_e.id_Cita = c.id_Cita)"
                        + " INNER JOIN entradas e on c.id_Entrada = e.id_Entrada) Where e.id_Entrada ="+id);
                
                 // Buscar diferencia de empeados true = está, false = no está
                if(allWorkers.next()){
                    int idCita = allWorkers.getInt("id_Cita");
                    if(allWorkers.getString("empleados") != null){
                        workers = allWorkers.getString("empleados").split(",");
                        existentes = new boolean[workers.length];
                        for(int x=0; x<workers.length; x++){
                            for(int y=0; y<idEmpleados.length && !existentes[x] ;y++)
                                if(workers[x].equals(idEmpleados[y]))
                                    existentes[x]=true;
                            if(!existentes[x]){ // Delete unused workers
                                stWorkers.executeUpdate("DELETE FROM citas_empleados"
                                    + " Where id_Cita="+idCita+" AND"
                                    + " id_Usuario="+workers[x]);
                                ((Empleado)this.empleados.get(Integer.parseInt(workers[x]))).removeCita(idCita);
                                cita.removeEmpleado(Integer.parseInt(workers[x]));
                            }
                        }
                    }
                    // Put new workers
                    if(!empleados.equals(""))
                        for(int i =0;i<idEmpleados.length;i++){
                            stWorkers.executeUpdate("INSERT IGNORE INTO citas_empleados"
                                    + " (id_Cita,id_Usuario) VALUES ("+idCita+","+idEmpleados[i]+")");
                            ((Empleado)this.empleados.get(Integer.parseInt(idEmpleados[i]))).setCita(idCita,cita);
                            cita.setEmpleado(Integer.parseInt(idEmpleados[i]),(Empleado)this.empleados.get(idEmpleados[i]));
                        }
                }
                
                // Multiple upload
                if(st.executeUpdate("UPDATE entradas e INNER JOIN citas c ON "
                        + " c.id_Entrada = e.id_Entrada "
                        + " SET e.Observaciones='"+obs_Entrada+"',"
                        + " c.fch_cita='"+((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(fecha))+"',"
                        + " c.Observaciones='"+obs_Cita+"',"
                        + " c.Motivo='"+motivo+"'"
                        + " Where e.id_Entrada='"+id+"'")> 0){
                    entrada.actualizar(obs_Entrada, fecha, motivo, obs_Cita);
                    return true;
                }else
                    return false;
            } catch (Exception ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
            }
    }
    
    public boolean deleteEntrada(int id){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Statement stDelete = con.createStatement();
                Statement stIdCita = con.createStatement();
                ResultSet res = stIdCita.executeQuery("SELECT c.id_Cita FROM entradas e LEFT JOIN citas c ON e.id_Entrada = c.id_Entrada");
                Statement stIdPresupost = con.createStatement();
                ResultSet resPresupost = stIdPresupost.executeQuery("SELECT p.id_Presupuesto FROM entradas e INNER JOIN presupuestos p ON e.id_Entrada = p.id_Entrada WHERE e.id_Entrada ="+id);
                
                if(resPresupost.next())
                    this.deletePresupuesto(resPresupost.getInt("p.id_Presupuesto"));
                
                if(res.next()){
                    int idCita = res.getInt("id_Cita");
                    if(stDelete.executeUpdate("DELETE FROM entradas WHERE id_Entrada="+id) > 0){
                        if((((Entrada)this.documento.get(id+";Entrada")).getIncidencia().getClass().getName()).equals("GestoSAT.Cita")){
                            Map empleados = ((Cita)((Entrada)this.documento.get(id+";Entrada")).getIncidencia()).getEmpleados();
                            Iterator itEmp = empleados.entrySet().iterator();
                            while(itEmp.hasNext()){
                                Map.Entry aux = (Map.Entry)itEmp.next();
                                ((Empleado)aux.getValue()).removeCita(idCita);
                            }
                        } 
                        ((Entrada)this.documento.get(id+";Entrada")).getCliente().deleteEntrada(id);
                        this.documento.remove(id+";Entrada");
                        
                        return true;
                    }else
                        return false;
                }else
                    return false;
            } catch (Exception ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
                
    }
    
    public int crearPresupuesto(int idEntrada,Presupuesto presupuesto){
        try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Statement st = con.createStatement();
                    String query = "INSERT INTO presupuestos (fch_Creacion, Concepto,"
                            + " fch_Validez, Aceptado, Forma_Pago, Adelanto, Plazo,"
                            + " Condiciones, Seguro, Garantia, Observaciones, Total, id_Entrada)"
                            + " VALUES ('"+((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(presupuesto.getFchCreacion()))+"',"
                            + " '"+presupuesto.getConcepto()+"','"+((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(presupuesto.getValidez()))+"',";
                        if(presupuesto.isAceptado())
                            query+= "'1',";
                        else
                            query+= "'0',";
                        query+= " '"+presupuesto.getFormaPago()+"','"+presupuesto.getAdelanto()+"',"
                                + " '"+presupuesto.getPlazo()+"','"+presupuesto.getCondiciones()+"',"
                                + " '"+presupuesto.getSeguro()+"', '"+presupuesto.getGarantia()+"',"
                                + " '"+presupuesto.getObservaciones()+"',"
                                + " '"+presupuesto.getTotal()+"', '"+idEntrada+"')";
                            st.executeUpdate(query);
                    ResultSet res = st.executeQuery("SELECT id_Presupuesto FROM presupuestos ORDER BY id_Presupuesto DESC");
                    res.next();
                    int idPresupuesto = res.getInt("id_Presupuesto");
                    this.documento.put(idPresupuesto+";Presupuesto", presupuesto);
                    Iterator it =  presupuesto.getTrabajo().entrySet().iterator();
                    while(it.hasNext()){ // Crear TrabajoPresupuesto
                        Map.Entry auxTrabajo = (Map.Entry)it.next();
                        st.executeUpdate("INSERT INTO trabajos_presupuestados ("
                                + "id_Usuario,id_Presupuesto,Horas,Descripcion)"
                                + " VALUES ('"+auxTrabajo.getKey()+"','"+idPresupuesto+"',"
                                + "'"+((Trabajo)auxTrabajo.getValue()).getHoras()+"',"
                                + "'"+((Trabajo)auxTrabajo.getValue()).getDescripcion()+"')");
                    }
                    
                    it = presupuesto.getMaterial().entrySet().iterator();
                    while(it.hasNext()){ // Crear MaterialPresupuesto
                        Map.Entry auxMaterial = (Map.Entry)it.next();
                        st.executeUpdate("INSERT INTO materiales_presupuestos (id_Stock,"
                                + "id_Presupuesto,Cantidad) VALUES ('"+auxMaterial.getKey()+"',"
                        + "'"+idPresupuesto+"','"+((MaterialTrabajos)auxMaterial.getValue()).getCantidad()+"')");
                    }
                    st.close();
                    
                    presupuesto.getEntrada().getCliente().setPresupuesto(idPresupuesto, presupuesto);
                    
                    return idPresupuesto;
        
            } catch (Exception ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                    return 0;
            }
    }
    
    public Map recuperarPresupuesto(int id_Entrada, Entrada entrada){
        int id_Presupuesto = 0;
        this.getStock();
        try{
            Map presupuesto = new HashMap();
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("Select * FROM presupuestos WHERE id_Entrada='"+id_Entrada+"' ORDER BY id_Presupuesto DESC ");
                if(res.next()){
                  id_Presupuesto = res.getInt("id_Presupuesto");
                  
                  Statement stMaps = con.createStatement();
                  
                  ResultSet resMaps = stMaps.executeQuery("Select m_p.*,s.id_Proveedor FROM materiales_presupuestos m_p INNER JOIN stock s ON m_p.id_Stock = s.id_Stock WHERE m_p.id_Presupuesto='"+id_Presupuesto+"' ORDER BY m_p.id_Presupuesto DESC ");
                  Map materiales = new HashMap();
                  while(resMaps.next()){ // Materiales
                      materiales.put(resMaps.getInt("m_p.id_Stock"),new MaterialTrabajos(((Proveedor)this.proveedor.get(resMaps.getInt("s.id_Proveedor"))).getStock(resMaps.getInt("m_p.id_Stock")),resMaps.getFloat("m_p.Cantidad")));
                    }
                  
                  Map trabajos = new HashMap();
                  resMaps = stMaps.executeQuery("SELECT * FROM trabajos_presupuestados t_p INNER JOIN presupuestos p ON t_p.id_Presupuesto = p.id_Presupuesto Where p.id_Presupuesto = '"+id_Presupuesto+"'");
                  while(resMaps.next()){
                      trabajos.put(resMaps.getInt("t_p.id_Usuario"),new Trabajo(resMaps.getFloat("t_p.horas"),resMaps.getString("t_p.Descripcion"),(Empleado)empleados.get(resMaps.getInt("t_p.id_Usuario"))));
                  }
                  stMaps.close();
                  
                  Presupuesto p = new Presupuesto(res.getTimestamp("fch_Creacion"),res.getString("Concepto"),res.getTimestamp("fch_Validez"),
                          res.getBoolean("Aceptado"),res.getString("Forma_Pago"),res.getFloat("Adelanto"),res.getInt("Plazo"),
                          res.getString("Condiciones"),res.getString("Seguro"),res.getString("Garantia"),entrada,res.getString("Observaciones"),
                          trabajos,materiales,this,res.getFloat("Total"));
                  
                  if(this.documento.putIfAbsent(id_Presupuesto+";Presupuesto",p)==null){
                    entrada.getCliente().setPresupuesto(id_Presupuesto,p);
                    presupuesto.put(id_Presupuesto+";Presupuesto",p);
                  }else{
                      presupuesto.put(id_Presupuesto+";Presupuesto",this.documento.get(id_Presupuesto+";Presupuesto"));
                      ((Presupuesto)this.documento.get(id_Presupuesto+";Presupuesto")).actualizar(res.getString("Concepto"),res.getTimestamp("fch_Validez"),
                          res.getBoolean("Aceptado"),res.getString("Forma_Pago"),res.getFloat("Adelanto"),res.getInt("Plazo"),
                          res.getString("Condiciones"),res.getString("Seguro"),res.getString("Garantia"),entrada,res.getString("Observaciones"),
                          trabajos,materiales,res.getFloat("Total"));
                  }
                  
                  st.close();
                }
                return presupuesto;
        } catch (Exception ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                    return new HashMap();
        }
    }
    
    public Presupuesto getPresupuesto(int id_Presupuesto){
        return (Presupuesto)this.documento.get(id_Presupuesto+";Presupuesto");
    }
    
    public boolean deletePresupuesto(int id_Presupuesto){
        try {
            Statement st = con.createStatement();
            
            /*Statement stAlbaran = con.createStatement();
            ResultSet res = stAlbaran.executeQuery("SELECT a.id_Albaran FROM presupuestos p INNER JOIN albaranes a ON p.id_Presupuesto= a.id_Presupuesto WHERE p.id_Presupuesto="+id_Presupuesto);
            if(res.next())
               this.deleteAlbaran(res.getInt("a.id_Albaran"));
            */
            if(st.executeUpdate("Delete FROM presupuestos WHERE id_Presupuesto ='"+id_Presupuesto+"'")>0){
                ((Presupuesto)this.documento.get(id_Presupuesto+";Presupuesto")).getEntrada().setPresupuesto(null);
                ((Presupuesto)this.documento.get(id_Presupuesto+";Presupuesto")).getEntrada().getCliente().deletePresupuesto(id_Presupuesto);
                this.documento.remove(id_Presupuesto+";Presupuesto");
                
                return true;
            }else
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean guardarPresupuesto(int id, Presupuesto presupuesto){
            try {
                Statement st = con.createStatement();
                int aceptado = 0;
                if(presupuesto.isAceptado())
                    aceptado = 1;
                if(st.executeUpdate("UPDATE presupuestos SET Concepto='"+presupuesto.getConcepto()+"',"
                        + " fch_Validez='"+((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(presupuesto.getValidez()))+"',"
                        + " Aceptado='"+aceptado+"',"
                        + " Forma_pago='"+presupuesto.getFormaPago()+"',"
                        + " Adelanto='"+presupuesto.getAdelanto()+"',"
                        + " Plazo='"+presupuesto.getPlazo()+"',"
                        + " Condiciones='"+presupuesto.getCondiciones()+"',"
                        + " Seguro='"+presupuesto.getSeguro()+"',"
                        + " Garantia='"+presupuesto.getGarantia()+"',"
                        + " Observaciones='"+presupuesto.getObservaciones()+"',"
                        + " Total='"+presupuesto.getTotal()+"'"
                        + " WHERE id_Presupuesto='"+id+"'") > 0){
                    st.close();
                    // Trabajo
                    Statement stWorkers = con.createStatement();
                    ResultSet resWorkers = stWorkers.executeQuery("Select GROUP_CONCAT(id_Usuario) Empleados FROM trabajos_presupuestados WHERE id_Presupuesto='"+id+"' GROUP BY id_Presupuesto");
                    
                    String[] idUsuarios;
                    boolean[] empleadosExistentes;
                    while(resWorkers.next()){ // Comprobar els que esxistisen i el que no
                        idUsuarios = resWorkers.getString("Empleados").split(",");
                        empleadosExistentes = new boolean[idUsuarios.length];
                        Statement stDelUsuarios = con.createStatement();
                        
                        for(int x=0; x<idUsuarios.length; x++){
                            for(int y=0; y<presupuesto.getTrabajo().size() && !empleadosExistentes[x] ;y++)
                                if(idUsuarios[x].equals(presupuesto.getTrabajo().entrySet().toArray()[y].toString().split("=")[0]))
                                    empleadosExistentes[x]=true;
                            if(!empleadosExistentes[x]) // Delete unused workers
                                stDelUsuarios.executeUpdate("DELETE FROM trabajos_presupuestados WHERE id_Usuario='"+idUsuarios[x]+"' AND id_Presupuesto='"+id+"'"); // Borrar de la base de datos
                            else
                               stDelUsuarios.executeUpdate("UPDATE trabajos_presupuestados SET horas='"+((Trabajo)presupuesto.getTrabajo().get(Integer.parseInt(idUsuarios[x]))).getHoras()+"', descripcion='"+((Trabajo)presupuesto.getTrabajo().get(Integer.parseInt(idUsuarios[x]))).getDescripcion()+"' WHERE id_Usuario='"+idUsuarios[x]+"' AND id_Presupuesto='"+id+"'");
                        }
                        stDelUsuarios.close();
                    }
                        
                    // Add els nous treballs
                    Iterator itTrabajo = presupuesto.getTrabajo().entrySet().iterator();
                    while(itTrabajo.hasNext()){
                        Map.Entry auxTrabajo = (Map.Entry)itTrabajo.next();
                        stWorkers.executeUpdate("INSERT IGNORE INTO trabajos_presupuestados"
                                + " (id_Presupuesto, id_Usuario, horas, descripcion) VALUES ("
                                + " '"+id+"','"+auxTrabajo.getKey()+"',"
                                + " '"+((Trabajo)auxTrabajo.getValue()).getHoras()+"',"
                                + " '"+((Trabajo)auxTrabajo.getValue()).getDescripcion()+"')");
                    }
                    stWorkers.close();
                    
                    // Material    
                    Statement stStock = con.createStatement();
                    ResultSet resStock = stStock.executeQuery("Select GROUP_CONCAT(id_Stock) Materiales FROM materiales_presupuestos WHERE id_Presupuesto='"+id+"' GROUP BY id_Presupuesto");
                    
                    String[] idStock;
                    boolean[] stockExistentes;
                    if(resStock.next()){ // Comprobar els que esxistisen i el que no
                        idStock = resStock.getString("Materiales").split(",");
                        stockExistentes = new boolean[idStock.length];
                        Statement stDelStock = con.createStatement();
                        
                        for(int x=0; x<idStock.length; x++){
                            for(int y=0; y<presupuesto.getMaterial().size() && !stockExistentes[x] ;y++)
                                if(idStock[x].equals(presupuesto.getMaterial().entrySet().toArray()[y].toString().split("=")[0]))
                                    stockExistentes[x]=true;
                            if(!stockExistentes[x]) // Delete unused workers
                                stDelStock.executeUpdate("DELETE FROM materiales_presupuestos WHERE id_Stock='"+idStock[x]+"' AND id_Presupuesto='"+id+"'"); // Borrar de la base de datos
                            else
                                stDelStock.executeUpdate("UPDATE materiales_presupuestos SET cantidad='"+((MaterialTrabajos)presupuesto.getMaterial().get(Integer.parseInt(idStock[x]))).getCantidad()+"' WHERE id_Stock='"+idStock[x]+"' AND id_Presupuesto='"+id+"'");
                        }
                        stDelStock.close();
                    }
                    
                    // Add els nous treballs
                    Iterator itStock = presupuesto.getMaterial().entrySet().iterator();
                    while(itStock.hasNext()){
                        Map.Entry auxStock = (Map.Entry)itStock.next();
                        stStock.executeUpdate("INSERT IGNORE INTO materiales_presupuestos"
                                + " (id_Presupuesto, id_Stock, cantidad) VALUES ("
                                + " '"+id+"','"+auxStock.getKey()+"',"
                                + " '"+((MaterialTrabajos)auxStock.getValue()).getCantidad()+"')");
                    }    
                    
                    resStock.close();
                    return true;
                }else
                    return false;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
    }
    
    public int crearVenta(int idCliente, Albaran albaran){
            try {
                // Recuperar datos del presupost i guardar
                Statement st = con.createStatement();
                st.executeUpdate("INSERT INTO albaranes (fch_Creacion,Concepto,Provincia,Poblacion,CP,Calle,Numero,"
                        + "Escalera,Piso,Puerta,Total,id_Cliente,Observaciones) VALUES ("
                        + "'"+((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(albaran.getFchCreacion()))+"',"
                        + "'"+albaran.getConcepto()+"','"+albaran.getProvincia()+"','"+albaran.getPoblacion()+"',"
                        + "'"+albaran.getCp()+"','"+albaran.getCalle()+"','"+albaran.getNumero()+"',"
                        + "'"+albaran.getEscalera()+"','"+albaran.getPiso()+"','"+albaran.getPuerta()+"',"
                        + "'"+albaran.getTotal()+"','"+idCliente+"','"+albaran.getObservaciones()+"')");
                
                ResultSet res = st.executeQuery("SELECT id_Albaran FROM albaranes ORDER BY id_Albaran DESC");
                    res.next();
                    int idAlbaran = res.getInt("id_Albaran");
                    this.documento.put(idAlbaran+";Albaran", albaran);
                    Iterator it = albaran.getMaterialUtilizado().entrySet().iterator();
                    
                    Map stock = this.getStock();
                    Statement stStock = con.createStatement();
                    
                    while(it.hasNext()){ // Crear Material
                        Map.Entry auxMaterial = (Map.Entry)it.next();
                        ((Stock)stock.get(Integer.parseInt(auxMaterial.getKey().toString()))).quitar(((MaterialTrabajos)auxMaterial.getValue()).getCantidad());
                        st.executeUpdate("INSERT INTO materiales_utilizados (id_Stock,"
                                + "id_Albaran,Cantidad) VALUES ('"+auxMaterial.getKey()+"',"
                        + "'"+idAlbaran+"','"+((MaterialTrabajos)auxMaterial.getValue()).getCantidad()+"')");
                        stStock.executeUpdate("UPDATE stock set Unidades='"+((Stock)stock.get(Integer.parseInt(auxMaterial.getKey().toString()))).getCantidad()+"' WHERE id_Stock ="+auxMaterial.getKey());
                    }
                    stStock.close();
                    st.close();
                    
                    albaran.getCliente().setAlbaran(idAlbaran, albaran);
                    
                    return idAlbaran;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return 0;
            }
    }
    
    public int getIdCliente(Cliente cliente){
        
            this.getCliente();
                                                                                    
        Iterator itCliente = this.cliente.entrySet().iterator();
        int idCliente =0;
        while(itCliente.hasNext()){
            Map.Entry auxCliente = (Map.Entry)itCliente.next();
            if(cliente == auxCliente.getValue()){
                idCliente = Integer.parseInt(auxCliente.getKey().toString());
                break;
            }
        }
        return idCliente;
    }
    
    public int crearAlbaran(int idPresupuesto, Albaran albaran){
        try {
                this.getStock();
                int idCliente = this.getIdCliente(albaran.getCliente());
                albaran.getPresupuesto().setAlbaran(albaran);
                // Recuperar datos del presupost i guardar
                Statement st = con.createStatement();
                st.executeUpdate("INSERT INTO albaranes (fch_Creacion,Concepto,Provincia,Poblacion,CP,Calle,Numero,"
                        + "Escalera,Piso,Puerta,Total,id_Cliente,id_Presupuesto,Observaciones) VALUES ("
                        + "'"+((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(albaran.getFchCreacion()))+"',"
                        + "'"+albaran.getConcepto()+"','"+albaran.getProvincia()+"','"+albaran.getPoblacion()+"',"
                        + "'"+albaran.getCp()+"','"+albaran.getCalle()+"','"+albaran.getNumero()+"',"
                        + "'"+albaran.getEscalera()+"','"+albaran.getPiso()+"','"+albaran.getPuerta()+"',"
                        + "'"+albaran.getTotal()+"','"+idCliente+"','"+idPresupuesto+"','"+albaran.getObservaciones()+"')");
                
                ResultSet res = st.executeQuery("SELECT id_Albaran FROM albaranes ORDER BY id_Albaran DESC");
                    res.next();
                    int idAlbaran = res.getInt("id_Albaran");
                    this.documento.put(idAlbaran+";Albaran", albaran);
                    
                    Iterator it = albaran.getMaterialUtilizado().entrySet().iterator();
                    Map stock = this.getStock();
                    Statement stStock = con.createStatement();
                    
                    while(it.hasNext()){ // Crear Material
                        Map.Entry auxMaterial = (Map.Entry)it.next();
                        ((Stock)stock.get(Integer.parseInt(auxMaterial.getKey().toString()))).quitar(((MaterialTrabajos)auxMaterial.getValue()).getCantidad());
                        st.executeUpdate("INSERT INTO materiales_utilizados (id_Stock,"
                                + "id_Albaran,Cantidad) VALUES ('"+auxMaterial.getKey()+"',"
                        + "'"+idAlbaran+"','"+((MaterialTrabajos)auxMaterial.getValue()).getCantidad()+"')");
                        stStock.executeUpdate("UPDATE stock set Unidades='"+((Stock)stock.get(Integer.parseInt(auxMaterial.getKey().toString()))).getCantidad()+"' WHERE id_Stock ="+auxMaterial.getKey());
                    }
                    stStock.close();
                    it = albaran.getTrabajoRealizado().entrySet().iterator();
                    
                    while(it.hasNext()){
                        Map.Entry auxTrabajo = (Map.Entry)it.next();
                        st.executeUpdate("INSERT INTO trabajos_realizados ("
                                + "id_Usuario,id_Albaran,Horas,Descripcion)"
                                + " VALUES ('"+auxTrabajo.getKey()+"','"+idAlbaran+"',"
                                + "'"+((Trabajo)auxTrabajo.getValue()).getHoras()+"',"
                                + "'"+((Trabajo)auxTrabajo.getValue()).getDescripcion()+"')");
                    }
                    st.close();
                    
                    albaran.getCliente().setAlbaran(idAlbaran, albaran);
                    
                    return idAlbaran;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return 0;
            }
    }
    
    public Map recuperarAlbaran(int id_Presupuesto){
        int id_Albaran = 0;
        this.getStock();
        try{
            Map mapAlbaran = new HashMap();
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("Select * FROM albaranes WHERE id_Presupuesto='"+id_Presupuesto+"' ORDER BY id_Presupuesto DESC ");
                if(res.next()){
                  id_Albaran = res.getInt("id_Albaran");
                  
                  Statement stMaps = con.createStatement();
                  
                  ResultSet resMaps = stMaps.executeQuery("Select m_u.*,s.id_Proveedor FROM materiales_utilizados m_u INNER JOIN stock s ON m_u.id_Stock = s.id_Stock WHERE m_u.id_Albaran='"+id_Albaran+"' ORDER BY m_u.id_Albaran DESC ");
                  Map materiales = new HashMap();
                  while(resMaps.next()){ // Materiales
                      materiales.put(resMaps.getInt("m_u.id_Stock"),new MaterialTrabajos(((Proveedor)this.proveedor.get(resMaps.getInt("s.id_Proveedor"))).getStock(resMaps.getInt("m_u.id_Stock")),resMaps.getFloat("m_u.Cantidad")));
                    }
                  
                  Map trabajos = new HashMap();
                  resMaps = stMaps.executeQuery("SELECT * FROM trabajos_realizados t_r INNER JOIN albaranes a ON t_r.id_Albaran = a.id_Albaran Where a.id_Albaran = '"+id_Albaran+"'");
                  while(resMaps.next()){
                      trabajos.put(resMaps.getInt("t_r.id_Usuario"),new Trabajo(resMaps.getFloat("t_r.horas"),resMaps.getString("t_r.Descripcion"),(Empleado)empleados.get(resMaps.getInt("t_r.id_Usuario"))));
                  }
                  stMaps.close();
                  
                  Albaran albaran = new Albaran(res.getString("Concepto"),res.getString("Provincia"),res.getString("Poblacion"),
                          res.getInt("CP"),res.getString("calle"),res.getString("numero"),res.getString("escalera"),res.getInt("piso"),
                          res.getString("Puerta"),materiales,trabajos,res.getString("Observaciones"),res.getFloat("Total"),
                          (res.getTimestamp("fch_Creacion")),(res.getTimestamp("fch_Entrega")),this.getPresupuesto(id_Presupuesto),this.getCliente(res.getInt("id_Cliente")),this);
                  
                    if(this.documento.putIfAbsent(id_Albaran+";Albaran",albaran)==null){
                        ((Presupuesto)this.documento.get(id_Presupuesto+";Presupuesto")).setAlbaran(albaran);
                        albaran.getCliente().setAlbaran(id_Albaran, albaran);
                        mapAlbaran.put(id_Albaran+";Albaran",albaran);
                    }else{
                        ((Albaran)this.documento.get(id_Albaran+";Albaran")).actualizarAlbaran(res.getString("Concepto"),res.getString("Provincia"),res.getString("Poblacion"),
                            res.getInt("CP"),res.getString("calle"),res.getString("numero"),res.getString("escalera"),res.getInt("piso"),
                            res.getString("Puerta"),materiales,trabajos,res.getString("Observaciones"),res.getFloat("Total"));
                        //((Albaran)this.documento.get(id_Albaran+";Albaran")).setPresupuesto((Presupuesto)this.documento.get(id_Presupuesto+";Presupuesto"));
                        mapAlbaran.put(id_Albaran+";Albaran",this.documento.get(id_Albaran+";Albaran"));
                    }
                        
                  st.close();
                }
                return mapAlbaran;
        } catch (Exception ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                    return new HashMap();
        }
    }
    
    public Albaran getAlbaran(int idAlbaran){
        return (Albaran)this.documento.get(idAlbaran+";Albaran");
    }
    
    public Factura getFactura(int idFactura){
        return (Factura)this.documento.get(idFactura+";Factura");
    }
    
    public Recibo getRecibo(int idRecibo){
        return (Recibo)this.documento.get(idRecibo+";Recibo");
    }
    
    /*public boolean deleteAlbaran(int id_Albaran){ // No se puede eliminar un albaran
        try {
            Statement st = con.createStatement();
            if(st.executeUpdate("Delete FROM albaranes WHERE id_Albaran ='"+id_Albaran+"'")>0){
                ((Albaran)this.documento.get(id_Albaran+";Albaran")).getPresupuesto().setAlbaran(null);
                ((Albaran)this.documento.get(id_Albaran+";Albaran")).getCliente().deleteAlbaran(id_Albaran);
                this.documento.remove(id_Albaran+";Albaran");
                return true;
            }else
                return false;
        } catch (SQLException ex) {
            Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }*/
    
    public boolean guardarAlbaran(int idAlbaran, Albaran albaran){
        try {
                Map stock = this.getStock();
                Statement st = con.createStatement();
                if(st.executeUpdate("UPDATE albaranes SET Concepto='"+albaran.getConcepto()+"',"
                        + " Provincia='"+albaran.getProvincia()+"',"
                        + " Poblacion='"+albaran.getPoblacion()+"',"
                        + " CP='"+albaran.getCp()+"',"
                        + " Calle='"+albaran.getCalle()+"',"
                        + " Numero='"+albaran.getNumero()+"',"
                        + " Escalera='"+albaran.getEscalera()+"',"
                        + " Piso='"+albaran.getPiso()+"',"
                        + " Puerta='"+albaran.getPuerta()+"',"
                        + " Observaciones='"+albaran.getObservaciones()+"',"
                        + " Total='"+albaran.getTotal()+"'"
                        + " WHERE id_Albaran='"+idAlbaran+"'") > 0){
                    st.close();
                    // Trabajo
                    Statement stWorkers = con.createStatement();
                    ResultSet resWorkers = stWorkers.executeQuery("Select GROUP_CONCAT(id_Usuario) Empleados FROM trabajos_realizados WHERE id_Albaran='"+idAlbaran+"' GROUP BY id_Albaran");
                    String[] idUsuarios;
                    boolean[] empleadosExistentes;
                    while(resWorkers.next()){ // Comprobar els que esxistisen i el que no
                        idUsuarios = resWorkers.getString("Empleados").split(",");
                        empleadosExistentes = new boolean[idUsuarios.length];
                        Statement stDelUsuarios = con.createStatement();
                        
                        for(int x=0; x<idUsuarios.length; x++){
                            for(int y=0; y<albaran.getTrabajoRealizado().size() && !empleadosExistentes[x] ;y++)
                                if(idUsuarios[x].equals(albaran.getTrabajoRealizado().entrySet().toArray()[y].toString().split("=")[0]))
                                    empleadosExistentes[x]=true;
                            if(!empleadosExistentes[x]) // Delete unused workers
                                stDelUsuarios.executeUpdate("DELETE FROM trabajos_realizados WHERE id_Usuario='"+idUsuarios[x]+"' AND id_Albaran='"+idAlbaran+"'"); // Borrar de la base de datos
                            else
                               stDelUsuarios.executeUpdate("UPDATE trabajos_realizados SET horas='"+((Trabajo)albaran.getTrabajoRealizado().get(Integer.parseInt(idUsuarios[x]))).getHoras()+"', descripcion='"+((Trabajo)albaran.getTrabajoRealizado().get(Integer.parseInt(idUsuarios[x]))).getDescripcion()+"' WHERE id_Usuario='"+idUsuarios[x]+"' AND id_Albaran='"+idAlbaran+"'");
                        }
                        stDelUsuarios.close();
                    }
                        
                    // Add els nous treballs
                    Iterator itTrabajo = albaran.getTrabajoRealizado().entrySet().iterator();
                    
                    while(itTrabajo.hasNext()){
                        Map.Entry auxTrabajo = (Map.Entry)itTrabajo.next();
                        stWorkers.executeUpdate("INSERT IGNORE INTO trabajos_realizados"
                                + " (id_Albaran, id_Usuario, horas, descripcion) VALUES ("
                                + " '"+idAlbaran+"','"+auxTrabajo.getKey()+"',"
                                + " '"+((Trabajo)auxTrabajo.getValue()).getHoras()+"',"
                                + " '"+((Trabajo)auxTrabajo.getValue()).getDescripcion()+"')");
                    }
                    stWorkers.close();
                    
                    // Material    
                    Statement stStock = con.createStatement();
                    ResultSet resStock = stStock.executeQuery("Select GROUP_CONCAT(id_Stock) Materiales FROM materiales_utilizados WHERE id_Albaran='"+idAlbaran+"' GROUP BY id_Albaran");
                    
                    String[] idStock = null;
                    boolean[] stockExistentes = null;
                    float aux_Cantidad;
                    
                    if(resStock.next()){ // Comprobar els que esxistisen i el que no
                        idStock = resStock.getString("Materiales").split(",");
                        stockExistentes = new boolean[idStock.length];
                        Statement stDelStock = con.createStatement();
                        ResultSet resActStock;
                        
                        for(int x=0; x<idStock.length; x++){
                            for(int y=0; y<albaran.getMaterialUtilizado().size() && !stockExistentes[x] ;y++)
                                if(idStock[x].equals(albaran.getMaterialUtilizado().entrySet().toArray()[y].toString().split("=")[0]))
                                    stockExistentes[x]=true;
                            if(!stockExistentes[x]){ // Delete unused workers                                                                                                                                                                                                                                                                                                                        {
                                resActStock = stDelStock.executeQuery("SELECT cantidad FROM materiales_utilizados WHERE id_Stock='"+idStock[x]+"' AND id_Albaran='"+idAlbaran+"'");
                                resActStock.next();
                                aux_Cantidad = resActStock.getFloat("cantidad");
                                resActStock = stDelStock.executeQuery("SELECT unidades FROM stock WHERE id_Stock="+idStock[x]);
                                resActStock.next();
                                stDelStock.executeUpdate("UPDATE stock SET unidades='"+(aux_Cantidad+resActStock.getFloat("unidades"))+"' WHERE id_Stock="+idStock[x]);
                                ((Stock)stock.get(Integer.parseInt(idStock[x]))).anyadir(aux_Cantidad);
                                stDelStock.executeUpdate("DELETE FROM materiales_utilizados WHERE id_Stock='"+idStock[x]+"' AND id_Albaran='"+idAlbaran+"'"); // Borrar de la base de datos
                            }else{
                                resActStock = stDelStock.executeQuery("SELECT cantidad FROM materiales_utilizados WHERE id_Stock='"+idStock[x]+"' AND id_Albaran='"+idAlbaran+"'");
                                resActStock.next();
                                aux_Cantidad = resActStock.getFloat("cantidad");
                                resActStock = stDelStock.executeQuery("SELECT unidades FROM stock WHERE id_Stock="+idStock[x]);
                                resActStock.next();
                                stDelStock.executeUpdate("UPDATE stock SET unidades='"+((aux_Cantidad-((MaterialTrabajos)albaran.getMaterialUtilizado().get(Integer.parseInt(idStock[x]))).getCantidad())+resActStock.getFloat("unidades"))+"' WHERE id_Stock="+idStock[x]);
                                if(aux_Cantidad > ((MaterialTrabajos)albaran.getMaterialUtilizado().get(Integer.parseInt(idStock[x]))).getCantidad())
                                    ((Stock)stock.get(Integer.parseInt(idStock[x]))).anyadir((aux_Cantidad-((MaterialTrabajos)albaran.getMaterialUtilizado().get(Integer.parseInt(idStock[x]))).getCantidad()));
                                else
                                    ((Stock)stock.get(Integer.parseInt(idStock[x]))).quitar(-(aux_Cantidad-((MaterialTrabajos)albaran.getMaterialUtilizado().get(Integer.parseInt(idStock[x]))).getCantidad()));
                                
                                stDelStock.executeUpdate("UPDATE materiales_utilizados SET cantidad='"+((MaterialTrabajos)albaran.getMaterialUtilizado().get(Integer.parseInt(idStock[x]))).getCantidad()+"' WHERE id_Stock='"+idStock[x]+"' AND id_Albaran='"+idAlbaran+"'");
                            }
                        }
                        stDelStock.close();
                    }
                    
                    // Add els nous treballs
                    Iterator itStock = albaran.getMaterialUtilizado().entrySet().iterator();
                    
                    while(itStock.hasNext()){
                        Map.Entry auxStock = (Map.Entry)itStock.next();
                        
                        if(idStock!= null && stockExistentes != null){
                            for(int x=0; x<idStock.length; x++)
                                for(int y=0; y<albaran.getMaterialUtilizado().size() && !stockExistentes[x];y++)
                                    if(idStock[x].equals(albaran.getMaterialUtilizado().entrySet().toArray()[y].toString().split("=")[0])){
                                        resStock = stStock.executeQuery("SELECT unidades FROM stock WHERE id_Stock="+auxStock.getKey());
                                        resStock.next();
                                        aux_Cantidad = resStock.getFloat("unidades");
                                        ((Stock)stock.get(Integer.parseInt(idStock[x]))).quitar(aux_Cantidad);
                                        stStock.executeUpdate("UPDATE stock SET unidades='"+(aux_Cantidad-((MaterialTrabajos)auxStock.getValue()).getCantidad())+"' WHERE id_Stock="+auxStock.getKey());
                                }
                        }else{
                            resStock = stStock.executeQuery("SELECT unidades FROM stock WHERE id_Stock="+auxStock.getKey());
                            resStock.next();
                            aux_Cantidad = resStock.getFloat("unidades");
                            ((Stock)stock.get(Integer.parseInt(auxStock.getKey().toString()))).quitar(aux_Cantidad);
                            stStock.executeUpdate("UPDATE stock SET unidades='"+(aux_Cantidad-((MaterialTrabajos)auxStock.getValue()).getCantidad())+"' WHERE id_Stock="+auxStock.getKey());
                        }
                        stStock.executeUpdate("INSERT IGNORE INTO materiales_utilizados"
                                + " (id_Albaran, id_Stock, cantidad) VALUES ("
                                + " '"+idAlbaran+"','"+auxStock.getKey()+"',"
                                + " '"+((MaterialTrabajos)auxStock.getValue()).getCantidad()+"')");
                    }
                    
                    resStock.close();
                    return true;
                }else
                    return false;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
    }
    
    public void cargarVentasPendientes(int idCliente){
        int id_Albaran = 0;
        this.getStock();
        try{
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("Select * FROM albaranes WHERE id_Cliente='"+idCliente+"' AND id_Factura IS NULL AND id_Presupuesto IS NULL ORDER BY id_Albaran DESC ");
            Statement stMaps;    
            ResultSet resMaps;
            while(res.next()){
                  id_Albaran = res.getInt("id_Albaran");
                  stMaps = con.createStatement();
                  resMaps = stMaps.executeQuery("Select m_u.*,s.id_Proveedor FROM materiales_utilizados m_u INNER JOIN stock s ON m_u.id_Stock = s.id_Stock WHERE m_u.id_Albaran='"+id_Albaran+"' ORDER BY m_u.id_Albaran DESC ");
                  Map materiales = new HashMap();
                  while(resMaps.next()){ // Materiales
                      materiales.put(resMaps.getInt("m_u.id_Stock"),new MaterialTrabajos(((Proveedor)this.proveedor.get(resMaps.getInt("s.id_Proveedor"))).getStock(resMaps.getInt("m_u.id_Stock")),resMaps.getFloat("m_u.Cantidad")));
                  }
                        
                  Albaran albaran = new Albaran(res.getString("Concepto"),res.getString("Provincia"),res.getString("Poblacion"),
                          res.getInt("CP"),res.getString("calle"),res.getString("numero"),res.getString("escalera"),res.getInt("piso"),
                          res.getString("Puerta"),materiales,res.getString("Observaciones"),res.getFloat("Total"),
                          (res.getTimestamp("fch_Creacion")),this.getCliente(idCliente),this);
                        
                        if(this.documento.putIfAbsent(id_Albaran+";Albaran",albaran) == null)
                            albaran.getCliente().setAlbaran(id_Albaran, albaran);
                        else
                        ((Albaran)this.documento.get(id_Albaran+";Albaran")).actualizarVenta(res.getString("Concepto"),res.getString("Provincia"),
                                res.getString("Poblacion"),res.getInt("CP"),res.getString("calle"),res.getString("numero"),
                                res.getString("escalera"),res.getInt("piso"),res.getString("Puerta"),materiales,
                                res.getString("Observaciones"),res.getFloat("Total"));
                    stMaps.close();
                }
            st.close();
            
        } catch (Exception ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarAlbaranesPendientes(int idCliente){
        // Codig agafat de buscar albaranes, agafar tot y adaptar
        try {
            Statement st = con.createStatement();
            
            // Albaran de Cita
            String consulta ="Select e.*, cli.*,cita.*, dir.*, a.*, GROUP_CONCAT(c_e.id_Usuario) empleados FROM (entradas e INNER JOIN"
                        + " (presupuestos p INNER JOIN albaranes a ON p.id_Presupuesto = a.id_Presupuesto) ON e.id_Entrada = p.id_Entrada) INNER JOIN "
                        + " ((direccion_citas dir INNER JOIN ((citas cita LEFT JOIN citas_empleados c_e ON cita.id_Cita = c_e.id_Cita)"
                        + " LEFT JOIN usuarios u ON u.id_Usuario = c_e.id_Usuario) ON cita.Direccion = dir.id_Direccion )"
                        + " INNER JOIN clientes cli ON cli.id_Cliente = cita.id_Cliente) ON e.id_entrada = cita.id_Entrada"
                        + " WHERE a.id_Factura IS NULL AND a.id_Cliente="+idCliente+" GROUP BY cita.id_Cita ORDER BY a.fch_Creacion DESC";
            
            ResultSet res = st.executeQuery(consulta);
            
            Entrada entrada;
            Cliente cli;
                
                while(res.next()){
                    Map aux_empleados = new HashMap();
                    if(res.getString("empleados") != null){
                        String[] aux = res.getString("empleados").split(",");

                        for(int i = 0;i < aux.length; i++)
                            aux_empleados.put(aux[i],this.getEmpleado(Integer.parseInt(aux[i])));
                    }
                        
                    if(this.documento.get(res.getString("id_Entrada")+";Entrada")== null){
                        if(this.cliente.get(res.getInt("cli.id_Cliente"))==null){
                            cli = new Cliente(res.getString("cli.nombre"),res.getString("cli.apellidos"),res.getString("cli.nif"),
                                        res.getString("cli.provincia"),res.getString("cli.poblacion"),
                                        res.getInt("cli.cp"),res.getString("cli.calle"),res.getString("cli.numero"),
                                        res.getString("cli.escalera"),res.getInt("cli.piso"),res.getString("cli.puerta"),
                                        res.getInt("cli.tlf_Contacto"), res.getInt("cli.tlf_Auxiliar"),
                                        res.getString("cli.correo_electronico"),res.getString("cli.observaciones"),this);

                            this.cliente.putIfAbsent(res.getInt("cli.id_Cliente"), cli);
                        }else{
                            cli = (Cliente)this.cliente.get(res.getInt("cli.id_Cliente"));
                        }
                        entrada = new Entrada(res.getString("e.observaciones"), res.getString("e.lugar"),
                            res.getTimestamp("e.fch_Entrada"),this,cli,
                        new Cita(res.getTimestamp("cita.fch_Cita"), res.getString("dir.provincia"),
                        res.getString("dir.poblacion"), res.getInt("dir.cp"), res.getString("dir.calle"),res.getString("dir.numero"),
                        res.getString("dir.escalera"), res.getInt("dir.piso"), res.getString("dir.puerta"), 
                        aux_empleados ,res.getString("cita.motivo"),
                        res.getString("cita.observaciones"), res.getString("dir.observaciones"),null));
                        
                        cli.setEntrada(res.getInt("id_Entrada"), entrada);
                        
                        this.documento.putIfAbsent(res.getInt("e.id_Entrada")+";Entrada",entrada);
                    }else{
                            entrada = (Entrada)this.documento.get(res.getInt("e.id_Entrada")+";Entrada");
                        }

                        entrada.cargarPresupuesto(res.getInt("e.id_Entrada"));
            }
            
            // Obtener albaranes averias
                consulta ="Select e.*, cli.*,av.*, ap.*, a.* FROM (entradas e INNER JOIN (presupuestos p "
                        + "INNER JOIN albaranes a ON p.id_Presupuesto = a.id_Presupuesto) ON e.id_Entrada = p.id_Entrada ) INNER JOIN "
                        + "((clientes cli INNER JOIN aparatos ap ON cli.id_Cliente = ap.id_Cliente) INNER JOIN averias av ON "
                        + "ap.id_Aparato = av.id_Aparato) ON av.id_Entrada = e.id_Entrada"
                        + " WHERE a.id_Factura IS NULL AND a.id_Cliente="+idCliente+" ORDER BY a.fch_Creacion DESC";
                
                res = st.executeQuery(consulta);
                while(res.next()){
                        if(this.documento.get(res.getString("id_Entrada")+";Entrada")== null){
                            if(this.cliente.get(res.getInt("cli.id_Cliente"))==null){
                                cli = new Cliente(res.getString("cli.nombre"),res.getString("cli.apellidos"),res.getString("cli.nif"),
                                            res.getString("cli.provincia"),res.getString("cli.poblacion"),
                                            res.getInt("cli.cp"),res.getString("cli.calle"),res.getString("cli.numero"),
                                            res.getString("cli.escalera"),res.getInt("cli.piso"),res.getString("cli.puerta"),
                                            res.getInt("cli.tlf_Contacto"), res.getInt("cli.tlf_Auxiliar"),
                                            res.getString("cli.correo_electronico"),res.getString("cli.observaciones"),this);

                                this.cliente.putIfAbsent(res.getInt("cli.id_Cliente"), cli);
                            }else{
                                cli = (Cliente)this.cliente.get(res.getInt("cli.id_Cliente"));
                            }

                            entrada = new Entrada(res.getString("e.observaciones"), res.getString("e.lugar"),
                                    res.getTimestamp("e.fch_Entrada"),this,cli,
                                    new Averia(res.getString("av.motivo"),res.getString("av.descripcion"),
                                        new Aparato(res.getString("ap.tipo"), res.getString("ap.marca"),
                                                res.getString("ap.modelo"), res.getString("ap.color"),
                                                res.getString("ap.numero_serie"),res.getString("ap.observaciones"),cli,res.getInt("ap.id_Aparato")),null));

                            cli.setEntrada(res.getInt("id_Entrada"), entrada);

                            this.documento.putIfAbsent(res.getInt("e.id_Entrada")+";Etrada", entrada);
                        }else{
                            entrada = (Entrada)this.documento.get(res.getInt("e.id_Entrada")+";Entrada");
                        }

                        entrada.cargarPresupuesto(res.getInt("e.id_Entrada"));
                }    
        } catch (SQLException ex) {
            Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int crearFactura(Factura factura){
        try {
            Statement st = con.createStatement();
            int idFactura = 0;
            st.executeUpdate("INSERT INTO facturas (fch_Creacion,Concepto,Observaciones,Forma_pago,Total) VALUES("
                    + "'"+((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(factura.getFchCreacion()))+"',"
                    + "'"+factura.getConcepto()+"','"+factura.getObservaciones()+"','"+factura.getFormaPago()+"',"
                    + "'"+factura.getTotal()+"')");
            
            ResultSet res = st.executeQuery("SELECT id_Factura from facturas ORDER BY id_Factura DESC");
            if(res.next()){
                    idFactura = res.getInt("id_Factura");

                Iterator itAlbaranes = factura.getAlbaranes().entrySet().iterator();
                while(itAlbaranes.hasNext()){
                    Map.Entry auxAlbaranes = (Map.Entry)itAlbaranes.next();
                    st.executeUpdate("UPDATE albaranes SET id_Factura='"+idFactura+"' WHERE id_Albaran ='"+auxAlbaranes.getKey()+"'");
                }

                st.close();

                this.documento.put(idFactura+";Factura",factura);
                factura.getCliente().setFactura(idFactura, factura);
            }
            return idFactura;
            
        } catch (SQLException ex) {
            Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }
    
    public Map buscarCliente(String buscar){
            try {
                Map mapClientes = new HashMap();
                Statement st = con.createStatement();
                
                ResultSet res = st.executeQuery("SELECT * FROM clientes WHERE CONCAT(Nombre,' ',Apellidos) LIKE '%"+buscar+"%' "
                        + " OR Observaciones LIKE '%"+buscar+"%' OR NIF LIKE'%"+buscar+"%' "
                        + " OR Provincia LIKE '%"+buscar+"%' OR Poblacion LIKE '%"+ buscar+"%' "
                        + " OR CP LIKE '%"+buscar+"%' OR Calle LIKE '%"+buscar+"%' "
                        + " OR Numero LIKE '%"+buscar+"%' OR Escalera LIKE '%"+buscar+"%' "
                        + " OR Piso LIKE '%"+buscar+"%' OR Puerta LIKE '%"+buscar+"%' "
                        + " OR Correo_electronico LIKE '%"+buscar+"%' OR tlf_Contacto LIKE '%"+buscar+"%' "
                        + " OR tlf_Auxiliar LIKE '%"+buscar+"%'");
                
                this.getCliente();
                while(res.next()){
                    mapClientes.put(res.getString("id_Cliente")+";Cliente",this.getCliente(res.getInt("id_Cliente")));
                }
                st.close();
                
                return mapClientes;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return new HashMap();
            }
    }
    
    public Map buscarEntrada(String buscar, boolean pendiente){
            try {
                Statement st = con.createStatement();
                
                Map entradas = new HashMap();
                
                String consulta = "Select e.*, cli.*,av.*, ap.* FROM (entradas e LEFT JOIN (presupuestos p "
                        + "LEFT JOIN albaranes a ON p.id_Presupuesto = a.id_Presupuesto) ON e.id_Entrada = p.id_Entrada ) INNER JOIN "
                        + "((clientes cli INNER JOIN aparatos ap ON cli.id_Cliente = ap.id_Cliente) INNER JOIN averias av ON "
                        + "ap.id_Aparato = av.id_Aparato) ON av.id_Entrada = e.id_Entrada WHERE ( "
                        + "CONCAT(cli.nombre,' ',cli.apellidos) LIKE '%"+buscar+"%'"
                        + " OR cli.Observaciones LIKE '%"+buscar+"%' OR cli.NIF LIKE'%"+buscar+"%' "
                        + " OR cli.Provincia LIKE '%"+buscar+"%' OR cli.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR cli.CP LIKE '%"+buscar+"%' OR cli.Calle LIKE '%"+buscar+"%' "
                        + " OR cli.Numero LIKE '%"+buscar+"%' OR cli.Escalera LIKE '%"+buscar+"%' "
                        + " OR cli.Piso LIKE '%"+buscar+"%' OR cli.Puerta LIKE '%"+buscar+"%' "
                        + " OR cli.Correo_electronico LIKE '%"+buscar+"%' OR cli.tlf_Contacto LIKE '%"+buscar+"%' "
                        + " OR cli.tlf_Auxiliar LIKE '%"+buscar+"%' OR e.lugar LIKE '%"+buscar+"%'"
                        + " OR e.observaciones LIKE '%"+buscar+"%' OR av.motivo LIKE '%"+buscar+"%'"
                        + " OR av.descripcion LIKE '%"+buscar+"%' OR ap.tipo LIKE '%"+buscar+"%'"
                        + " OR ap.marca LIKE '%"+buscar+"%' OR ap.modelo LIKE '%"+buscar+"%'"
                        + " OR ap.color LIKE '%"+buscar+"%' OR ap.numero_serie LIKE '"+buscar+"'"
                        + " OR ap.observaciones LIKE '%"+buscar+"%')";
                // Comprobar averies
                if(pendiente)
                    consulta += " AND a.id_albaran IS NULL ";
                
                consulta +=" ORDER BY fch_Entrada DESC";
                
                ResultSet res = st.executeQuery(consulta);
                Entrada entrada;
                Cliente cli;
                while(res.next()){
                    if(this.documento.get(res.getString("id_Entrada")+";Entrada")== null){
                        if(this.cliente.get(res.getInt("cli.id_Cliente"))==null){
                            cli = new Cliente(res.getString("cli.nombre"),res.getString("cli.apellidos"),res.getString("cli.nif"),
                                        res.getString("cli.provincia"),res.getString("cli.poblacion"),
                                        res.getInt("cli.cp"),res.getString("cli.calle"),res.getString("cli.numero"),
                                        res.getString("cli.escalera"),res.getInt("cli.piso"),res.getString("cli.puerta"),
                                        res.getInt("cli.tlf_Contacto"), res.getInt("cli.tlf_Auxiliar"),
                                        res.getString("cli.correo_electronico"),res.getString("cli.observaciones"),this);

                            this.cliente.putIfAbsent(res.getInt("cli.id_Cliente"), cli);
                        }else{
                            cli = (Cliente)this.cliente.get(res.getInt("cli.id_Cliente"));
                        }
                        
                        entrada = new Entrada(res.getString("e.observaciones"), res.getString("e.lugar"),
                                res.getTimestamp("e.fch_Entrada"),this,cli,
                                new Averia(res.getString("av.motivo"),res.getString("av.descripcion"),
                                    new Aparato(res.getString("ap.tipo"), res.getString("ap.marca"),
                                            res.getString("ap.modelo"), res.getString("ap.color"),
                                            res.getString("ap.numero_serie"),res.getString("ap.observaciones"),cli,res.getInt("ap.id_Aparato")),null));
                        
                        cli.setEntrada(res.getInt("id_Entrada"), entrada);
                        
                        entradas.put(res.getString("id_Entrada")+";Entrada",entrada);
                        this.documento.putIfAbsent(res.getInt("e.id_Entrada")+";Etrada", entrada);
                        
                    }else{
                        entrada = (Entrada)this.documento.get(res.getString("id_Entrada")+";Entrada");
                        entrada.actualizar(res.getString("e.observaciones"), res.getString("e.lugar"),res.getString("av.motivo"));
                        entradas.put(res.getString("id_Entrada")+";Entrada", entrada);
                    }
                    entrada.cargarPresupuesto(res.getInt("id_Entrada"));
                }
                
                // Comprobar citas
                consulta="Select e.*, cli.*,cita.*, dir.*, GROUP_CONCAT(c_e.id_Usuario) empleados FROM (entradas e LEFT JOIN"
                        + " (presupuestos p LEFT JOIN albaranes a ON p.id_Presupuesto = a.id_Presupuesto) ON e.id_Entrada = p.id_Entrada) INNER JOIN "
                        + "((direccion_citas dir INNER JOIN ((citas cita LEFT JOIN citas_empleados c_e ON cita.id_Cita = c_e.id_Cita)"
                        + " LEFT JOIN usuarios u ON u.id_Usuario = c_e.id_Usuario) ON cita.Direccion = dir.id_Direccion )"
                        + " INNER JOIN clientes cli ON cli.id_Cliente = cita.id_Cliente) ON e.id_entrada = cita.id_Entrada"
                        + " WHERE ( CONCAT(cli.nombre,' ',cli.apellidos) LIKE '%"+buscar+"%'"
                        + " OR cli.Observaciones LIKE '%"+buscar+"%' OR cli.NIF LIKE'%"+buscar+"%' "
                        + " OR cli.Provincia LIKE '%"+buscar+"%' OR cli.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR cli.CP LIKE '%"+buscar+"%' OR cli.Calle LIKE '%"+buscar+"%' "
                        + " OR cli.Numero LIKE '%"+buscar+"%' OR cli.Escalera LIKE '%"+buscar+"%' "
                        + " OR cli.Piso LIKE '%"+buscar+"%' OR cli.Puerta LIKE '%"+buscar+"%' "
                        + " OR cli.Correo_electronico LIKE '%"+buscar+"%' OR cli.tlf_Contacto LIKE '%"+buscar+"%' "
                        + " OR cli.tlf_Auxiliar LIKE '%"+buscar+"%' OR e.lugar LIKE '%"+buscar+"%'"
                        + " OR e.observaciones LIKE '%"+buscar+"%' OR cita.motivo LIKE '%"+buscar+"%'"
                        + " OR cita.observaciones LIKE '%"+buscar+"%' OR dir.provincia LIKE '%"+buscar+"%'"
                        + " OR dir.poblacion LIKE '%"+buscar+"%' OR dir.CP LIKE '%"+buscar+"%'"
                        + " OR dir.Calle LIKE '%"+buscar+"%' OR dir.Numero LIKE '%"+buscar+"%'"
                        + " OR dir.Escalera LIKE '%"+buscar+"%' OR dir.Piso LIKE '%"+buscar+"%'"
                        + " OR dir.Puerta LIKE '%"+buscar+"%')";
                if(pendiente)
                    consulta += " AND a.id_albaran IS NULL ";
                    
                consulta+=" GROUP BY cita.id_Cita ORDER BY fch_Entrada DESC";
                res = st.executeQuery(consulta);
                
                while(res.next()){
                    Map aux_empleados = new HashMap();
                    if(res.getString("empleados") != null){
                        String[] aux = res.getString("empleados").split(",");

                        for(int i = 0;i < aux.length; i++)
                            aux_empleados.put(aux[i],this.getEmpleado(Integer.parseInt(aux[i])));
                    }
                        
                    if(this.documento.get(res.getString("id_Entrada")+";Entrada")== null){
                        if(this.cliente.get(res.getInt("cli.id_Cliente"))==null){
                            cli = new Cliente(res.getString("cli.nombre"),res.getString("cli.apellidos"),res.getString("cli.nif"),
                                        res.getString("cli.provincia"),res.getString("cli.poblacion"),
                                        res.getInt("cli.cp"),res.getString("cli.calle"),res.getString("cli.numero"),
                                        res.getString("cli.escalera"),res.getInt("cli.piso"),res.getString("cli.puerta"),
                                        res.getInt("cli.tlf_Contacto"), res.getInt("cli.tlf_Auxiliar"),
                                        res.getString("cli.correo_electronico"),res.getString("cli.observaciones"),this);

                            this.cliente.putIfAbsent(res.getInt("cli.id_Cliente"), cli);
                        }else{
                            cli = (Cliente)this.cliente.get(res.getInt("cli.id_Cliente"));
                        }
                        entrada = new Entrada(res.getString("e.observaciones"), res.getString("e.lugar"),
                            res.getTimestamp("e.fch_Entrada"),this,cli,
                        new Cita(res.getTimestamp("cita.fch_Cita"), res.getString("dir.provincia"),
                        res.getString("dir.poblacion"), res.getInt("dir.cp"), res.getString("dir.calle"),res.getString("dir.numero"),
                        res.getString("dir.escalera"), res.getInt("dir.piso"), res.getString("dir.puerta"), 
                        aux_empleados ,res.getString("cita.motivo"),
                        res.getString("cita.observaciones"), res.getString("dir.observaciones"),null));
                        
                        cli.setEntrada(res.getInt("id_Entrada"), entrada);
                        
                        entradas.put(res.getString("id_Entrada")+";Entrada", entrada);
                        this.documento.putIfAbsent(res.getInt("e.id_Entrada")+";Entrada",entrada);
                    }else{
                        entrada = (Entrada)this.documento.get(res.getString("id_Entrada")+";Entrada");
                        entrada.actualizar(res.getString("e.observaciones"), res.getTimestamp("cita.fch_Cita"), res.getString("cita.motivo"),res.getString("cita.observaciones"),aux_empleados);
                        entradas.put(res.getString("id_Entrada")+";Entrada", entrada);
                    }
                    entrada.cargarPresupuesto(res.getInt("id_Entrada"));
                }
                st.close();
                return entradas;
                
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return new HashMap();
            }
        }
   
    public Map buscarPresupuesto(String buscar, boolean pendiente){
            try {
                Map presupuestos = new HashMap(); 
                Statement st = con.createStatement();
                
                // Presupuestos citas
                String consulta ="Select e.*, cli.*,cita.*, dir.*, p.*, GROUP_CONCAT(c_e.id_Usuario) empleados FROM (entradas e LEFT JOIN"
                        + " (presupuestos p LEFT JOIN albaranes a ON p.id_Presupuesto = a.id_Presupuesto) ON e.id_Entrada = p.id_Entrada) INNER JOIN "
                        + " ((direccion_citas dir INNER JOIN ((citas cita LEFT JOIN citas_empleados c_e ON cita.id_Cita = c_e.id_Cita)"
                        + " LEFT JOIN usuarios u ON u.id_Usuario = c_e.id_Usuario) ON cita.Direccion = dir.id_Direccion )"
                        + " INNER JOIN clientes cli ON cli.id_Cliente = cita.id_Cliente) ON e.id_entrada = cita.id_Entrada"
                        + " WHERE (CONCAT(cli.nombre,' ',cli.apellidos) LIKE '%"+buscar+"%'"
                        + " OR cli.Observaciones LIKE '%"+buscar+"%' OR cli.NIF LIKE'%"+buscar+"%' "
                        + " OR cli.Provincia LIKE '%"+buscar+"%' OR cli.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR cli.CP LIKE '%"+buscar+"%' OR cli.Calle LIKE '%"+buscar+"%' "
                        + " OR cli.Numero LIKE '%"+buscar+"%' OR cli.Escalera LIKE '%"+buscar+"%' "
                        + " OR cli.Piso LIKE '%"+buscar+"%' OR cli.Puerta LIKE '%"+buscar+"%' "
                        + " OR cli.Correo_electronico LIKE '%"+buscar+"%' OR cli.tlf_Contacto LIKE '%"+buscar+"%' "
                        + " OR cli.tlf_Auxiliar LIKE '%"+buscar+"%' OR e.lugar LIKE '%"+buscar+"%'"
                        + " OR e.observaciones LIKE '%"+buscar+"%' OR cita.motivo LIKE '%"+buscar+"%'"
                        + " OR cita.observaciones LIKE '%"+buscar+"%' OR dir.provincia LIKE '%"+buscar+"%'"
                        + " OR dir.poblacion LIKE '%"+buscar+"%' OR dir.CP LIKE '%"+buscar+"%'"
                        + " OR dir.Calle LIKE '%"+buscar+"%' OR dir.Numero LIKE '%"+buscar+"%'"
                        + " OR dir.Escalera LIKE '%"+buscar+"%' OR dir.Piso LIKE '%"+buscar+"%'"
                        + " OR dir.Puerta LIKE '%"+buscar+"%' OR p.concepto LIKE '%"+buscar+"%'"
                        + " OR p.forma_pago LIKE '%"+buscar+"%' OR p.condiciones LIKE '%"+buscar+"%'"
                        + " OR p.seguro LIKE '%"+buscar+"%' OR p.garantia LIKE '%"+buscar+"%'"
                        + " OR p.observaciones LIKE '%"+buscar+"%') AND p.Observaciones NOT LIKE '%| Documento auxiliar%' ";
                
                if(pendiente)
                    consulta +=" AND a.id_albaran IS NULL ";
                
                consulta += " GROUP BY cita.id_Cita ORDER BY p.fch_Creacion DESC";
                
                ResultSet res = st.executeQuery(consulta);
                
                Entrada entrada;
                Cliente cli;
                
                while(res.next()){
                    Map aux_empleados = new HashMap();
                    if(res.getString("empleados") != null){
                        String[] aux = res.getString("empleados").split(",");

                        for(int i = 0;i < aux.length; i++)
                            aux_empleados.put(aux[i],this.getEmpleado(Integer.parseInt(aux[i])));
                    }
                        
                    if(this.documento.get(res.getString("id_Entrada")+";Entrada")== null){
                        if(this.cliente.get(res.getInt("cli.id_Cliente"))==null){
                            cli = new Cliente(res.getString("cli.nombre"),res.getString("cli.apellidos"),res.getString("cli.nif"),
                                        res.getString("cli.provincia"),res.getString("cli.poblacion"),
                                        res.getInt("cli.cp"),res.getString("cli.calle"),res.getString("cli.numero"),
                                        res.getString("cli.escalera"),res.getInt("cli.piso"),res.getString("cli.puerta"),
                                        res.getInt("cli.tlf_Contacto"), res.getInt("cli.tlf_Auxiliar"),
                                        res.getString("cli.correo_electronico"),res.getString("cli.observaciones"),this);

                            this.cliente.putIfAbsent(res.getInt("cli.id_Cliente"), cli);
                        }else{
                            cli = (Cliente)this.cliente.get(res.getInt("cli.id_Cliente"));
                        }
                        entrada = new Entrada(res.getString("e.observaciones"), res.getString("e.lugar"),
                            res.getTimestamp("e.fch_Entrada"),this,cli,
                        new Cita(res.getTimestamp("cita.fch_Cita"), res.getString("dir.provincia"),
                        res.getString("dir.poblacion"), res.getInt("dir.cp"), res.getString("dir.calle"),res.getString("dir.numero"),
                        res.getString("dir.escalera"), res.getInt("dir.piso"), res.getString("dir.puerta"), 
                        aux_empleados ,res.getString("cita.motivo"),
                        res.getString("cita.observaciones"), res.getString("dir.observaciones"),null));
                        
                        cli.setEntrada(res.getInt("id_Entrada"), entrada);
                        
                        this.documento.putIfAbsent(res.getInt("e.id_Entrada")+";Entrada",entrada);
                    }else{
                            entrada = (Entrada)this.documento.get(res.getInt("e.id_Entrada")+";Entrada");
                        }

                        entrada.cargarPresupuesto(res.getInt("e.id_Entrada"));
                        presupuestos.put(res.getInt("p.id_Presupuesto")+";Presupuesto",this.documento.get(res.getInt("p.id_Presupuesto")+";Presupuesto"));
                    
                }
                
                // Obtener presupuestos averias
                consulta ="Select e.*, cli.*,av.*, ap.*, p.* FROM (entradas e LEFT JOIN (presupuestos p "
                        + "LEFT JOIN albaranes a ON p.id_Presupuesto = a.id_Presupuesto) ON e.id_Entrada = p.id_Entrada ) INNER JOIN "
                        + "((clientes cli INNER JOIN aparatos ap ON cli.id_Cliente = ap.id_Cliente) INNER JOIN averias av ON "
                        + "ap.id_Aparato = av.id_Aparato) ON av.id_Entrada = e.id_Entrada WHERE ("
                        + " CONCAT(cli.nombre,' ',cli.apellidos) LIKE '%"+buscar+"%'"
                        + " OR cli.Observaciones LIKE '%"+buscar+"%' OR cli.NIF LIKE'%"+buscar+"%' "
                        + " OR cli.Provincia LIKE '%"+buscar+"%' OR cli.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR cli.CP LIKE '%"+buscar+"%' OR cli.Calle LIKE '%"+buscar+"%' "
                        + " OR cli.Numero LIKE '%"+buscar+"%' OR cli.Escalera LIKE '%"+buscar+"%' "
                        + " OR cli.Piso LIKE '%"+buscar+"%' OR cli.Puerta LIKE '%"+buscar+"%' "
                        + " OR cli.Correo_electronico LIKE '%"+buscar+"%' OR cli.tlf_Contacto LIKE '%"+buscar+"%' "
                        + " OR cli.tlf_Auxiliar LIKE '%"+buscar+"%' OR e.lugar LIKE '%"+buscar+"%'"
                        + " OR e.observaciones LIKE '%"+buscar+"%' OR av.motivo LIKE '%"+buscar+"%'"
                        + " OR av.descripcion LIKE '%"+buscar+"%' OR ap.tipo LIKE '%"+buscar+"%'"
                        + " OR ap.marca LIKE '%"+buscar+"%' OR ap.modelo LIKE '%"+buscar+"%'"
                        + " OR ap.color LIKE '%"+buscar+"%' OR ap.numero_serie LIKE '%"+buscar+"%'"
                        + " OR ap.observaciones LIKE '%"+buscar+"%' OR p.concepto LIKE '%"+buscar+"%'"
                        + " OR p.forma_pago LIKE '%"+buscar+"%' OR p.condiciones LIKE '%"+buscar+"%'"
                        + " OR p.seguro LIKE '%"+buscar+"%' OR p.garantia LIKE '%"+buscar+"%'"
                        + " OR p.observaciones LIKE '%"+buscar+"%') AND p.Observaciones NOT LIKE '%| Documento auxiliar%' ";
                
                if(pendiente)
                    consulta +=" AND a.id_albaran IS NULL ";
                
                consulta += " ORDER BY p.fch_Creacion DESC";
                
                res = st.executeQuery(consulta);
                while(res.next()){
                        if(this.documento.get(res.getString("id_Entrada")+";Entrada")== null){
                            if(this.cliente.get(res.getInt("cli.id_Cliente"))==null){
                                cli = new Cliente(res.getString("cli.nombre"),res.getString("cli.apellidos"),res.getString("cli.nif"),
                                            res.getString("cli.provincia"),res.getString("cli.poblacion"),
                                            res.getInt("cli.cp"),res.getString("cli.calle"),res.getString("cli.numero"),
                                            res.getString("cli.escalera"),res.getInt("cli.piso"),res.getString("cli.puerta"),
                                            res.getInt("cli.tlf_Contacto"), res.getInt("cli.tlf_Auxiliar"),
                                            res.getString("cli.correo_electronico"),res.getString("cli.observaciones"),this);

                                this.cliente.putIfAbsent(res.getInt("cli.id_Cliente"), cli);
                            }else{
                                cli = (Cliente)this.cliente.get(res.getInt("cli.id_Cliente"));
                            }

                            entrada = new Entrada(res.getString("e.observaciones"), res.getString("e.lugar"),
                                    res.getTimestamp("e.fch_Entrada"),this,cli,
                                    new Averia(res.getString("av.motivo"),res.getString("av.descripcion"),
                                        new Aparato(res.getString("ap.tipo"), res.getString("ap.marca"),
                                                res.getString("ap.modelo"), res.getString("ap.color"),
                                                res.getString("ap.numero_serie"),res.getString("ap.observaciones"),cli,res.getInt("ap.id_Aparato")),null));

                            cli.setEntrada(res.getInt("id_Entrada"), entrada);

                            this.documento.putIfAbsent(res.getInt("e.id_Entrada")+";Etrada", entrada);
                        }else{
                            entrada = (Entrada)this.documento.get(res.getInt("e.id_Entrada")+";Entrada");
                        }

                        entrada.cargarPresupuesto(res.getInt("e.id_Entrada"));
                        presupuestos.put(res.getInt("p.id_Presupuesto")+";Presupuesto",this.documento.get(res.getInt("p.id_Presupuesto")+";Presupuesto"));
                }
                
                st.close();
                return presupuestos;
            
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return new HashMap();
            }
    }
    
    public Map buscarAlbaran(String buscar, boolean pendiente){
        try {
            Statement st = con.createStatement();
            Map albaranes = new HashMap();
            
            // Albaran de Cita
            String consulta ="Select e.*, cli.*,cita.*, dir.*, a.*, GROUP_CONCAT(c_e.id_Usuario) empleados FROM (entradas e INNER JOIN"
                        + " (presupuestos p INNER JOIN albaranes a ON p.id_Presupuesto = a.id_Presupuesto) ON e.id_Entrada = p.id_Entrada) INNER JOIN "
                        + " ((direccion_citas dir INNER JOIN ((citas cita LEFT JOIN citas_empleados c_e ON cita.id_Cita = c_e.id_Cita)"
                        + " LEFT JOIN usuarios u ON u.id_Usuario = c_e.id_Usuario) ON cita.Direccion = dir.id_Direccion )"
                        + " INNER JOIN clientes cli ON cli.id_Cliente = cita.id_Cliente) ON e.id_entrada = cita.id_Entrada"
                        + " WHERE (CONCAT(cli.nombre,' ',cli.apellidos) LIKE '%"+buscar+"%'"
                        + " OR cli.Observaciones LIKE '%"+buscar+"%' OR cli.NIF LIKE'%"+buscar+"%' "
                        + " OR cli.Provincia LIKE '%"+buscar+"%' OR cli.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR cli.CP LIKE '%"+buscar+"%' OR cli.Calle LIKE '%"+buscar+"%' "
                        + " OR cli.Numero LIKE '%"+buscar+"%' OR cli.Escalera LIKE '%"+buscar+"%' "
                        + " OR cli.Piso LIKE '%"+buscar+"%' OR cli.Puerta LIKE '%"+buscar+"%' "
                        + " OR cli.Correo_electronico LIKE '%"+buscar+"%' OR cli.tlf_Contacto LIKE '%"+buscar+"%' "
                        + " OR cli.tlf_Auxiliar LIKE '%"+buscar+"%' OR e.lugar LIKE '%"+buscar+"%'"
                        + " OR e.observaciones LIKE '%"+buscar+"%' OR cita.motivo LIKE '%"+buscar+"%'"
                        + " OR cita.observaciones LIKE '%"+buscar+"%' OR dir.provincia LIKE '%"+buscar+"%'"
                        + " OR dir.poblacion LIKE '%"+buscar+"%' OR dir.CP LIKE '%"+buscar+"%'"
                        + " OR dir.Calle LIKE '%"+buscar+"%' OR dir.Numero LIKE '%"+buscar+"%'"
                        + " OR dir.Escalera LIKE '%"+buscar+"%' OR dir.Piso LIKE '%"+buscar+"%'"
                        + " OR dir.Puerta LIKE '%"+buscar+"%' OR a.concepto LIKE '%"+buscar+"%'"
                        + " OR a.total LIKE '%"+buscar+"%' OR a.observaciones LIKE '%"+buscar+"%'"
                        + " OR a.Provincia LIKE '%"+buscar+"%' OR a.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR a.CP LIKE '%"+buscar+"%' OR a.Calle LIKE '%"+buscar+"%' "
                        + " OR a.Numero LIKE '%"+buscar+"%' OR a.Escalera LIKE '%"+buscar+"%' "
                        + " OR a.Piso LIKE '%"+buscar+"%' OR a.Puerta LIKE '%"+buscar+"%')";
            
            if(pendiente)
                consulta+=" AND a.id_Factura IS NULL ";
        
            consulta+=" GROUP BY cita.id_Cita ORDER BY a.fch_Creacion DESC";
            
            ResultSet res = st.executeQuery(consulta);
            
            Entrada entrada;
            Cliente cli;
                
                while(res.next()){
                    Map aux_empleados = new HashMap();
                    if(res.getString("empleados") != null){
                        String[] aux = res.getString("empleados").split(",");

                        for(int i = 0;i < aux.length; i++)
                            aux_empleados.put(aux[i],this.getEmpleado(Integer.parseInt(aux[i])));
                    }
                        
                    if(this.documento.get(res.getString("id_Entrada")+";Entrada")== null){
                        if(this.cliente.get(res.getInt("cli.id_Cliente"))==null){
                            cli = new Cliente(res.getString("cli.nombre"),res.getString("cli.apellidos"),res.getString("cli.nif"),
                                        res.getString("cli.provincia"),res.getString("cli.poblacion"),
                                        res.getInt("cli.cp"),res.getString("cli.calle"),res.getString("cli.numero"),
                                        res.getString("cli.escalera"),res.getInt("cli.piso"),res.getString("cli.puerta"),
                                        res.getInt("cli.tlf_Contacto"), res.getInt("cli.tlf_Auxiliar"),
                                        res.getString("cli.correo_electronico"),res.getString("cli.observaciones"),this);

                            this.cliente.putIfAbsent(res.getInt("cli.id_Cliente"), cli);
                        }else{
                            cli = (Cliente)this.cliente.get(res.getInt("cli.id_Cliente"));
                        }
                        entrada = new Entrada(res.getString("e.observaciones"), res.getString("e.lugar"),
                            res.getTimestamp("e.fch_Entrada"),this,cli,
                        new Cita(res.getTimestamp("cita.fch_Cita"), res.getString("dir.provincia"),
                        res.getString("dir.poblacion"), res.getInt("dir.cp"), res.getString("dir.calle"),res.getString("dir.numero"),
                        res.getString("dir.escalera"), res.getInt("dir.piso"), res.getString("dir.puerta"), 
                        aux_empleados ,res.getString("cita.motivo"),
                        res.getString("cita.observaciones"), res.getString("dir.observaciones"),null));
                        
                        cli.setEntrada(res.getInt("id_Entrada"), entrada);
                        
                        this.documento.putIfAbsent(res.getInt("e.id_Entrada")+";Entrada",entrada);
                    }else{
                            entrada = (Entrada)this.documento.get(res.getInt("e.id_Entrada")+";Entrada");
                        }

                        entrada.cargarPresupuesto(res.getInt("e.id_Entrada"));
                        albaranes.put(res.getInt("a.id_Albaran")+";Albaran",this.documento.get(res.getInt("a.id_Albaran")+";Albaran"));
            }
            
            // Obtener albaranes averias
                consulta ="Select e.*, cli.*,av.*, ap.*, a.* FROM (entradas e INNER JOIN (presupuestos p "
                        + "INNER JOIN albaranes a ON p.id_Presupuesto = a.id_Presupuesto) ON e.id_Entrada = p.id_Entrada ) INNER JOIN "
                        + "((clientes cli INNER JOIN aparatos ap ON cli.id_Cliente = ap.id_Cliente) INNER JOIN averias av ON "
                        + "ap.id_Aparato = av.id_Aparato) ON av.id_Entrada = e.id_Entrada WHERE ("
                        + " CONCAT(cli.nombre,' ',cli.apellidos) LIKE '%"+buscar+"%'"
                        + " OR cli.Observaciones LIKE '%"+buscar+"%' OR cli.NIF LIKE'%"+buscar+"%' "
                        + " OR cli.Provincia LIKE '%"+buscar+"%' OR cli.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR cli.CP LIKE '%"+buscar+"%' OR cli.Calle LIKE '%"+buscar+"%' "
                        + " OR cli.Numero LIKE '%"+buscar+"%' OR cli.Escalera LIKE '%"+buscar+"%' "
                        + " OR cli.Piso LIKE '%"+buscar+"%' OR cli.Puerta LIKE '%"+buscar+"%' "
                        + " OR cli.Correo_electronico LIKE '%"+buscar+"%' OR cli.tlf_Contacto LIKE '%"+buscar+"%' "
                        + " OR cli.tlf_Auxiliar LIKE '%"+buscar+"%' OR e.lugar LIKE '%"+buscar+"%'"
                        + " OR e.observaciones LIKE '%"+buscar+"%' OR av.motivo LIKE '%"+buscar+"%'"
                        + " OR av.descripcion LIKE '%"+buscar+"%' OR ap.tipo LIKE '%"+buscar+"%'"
                        + " OR ap.marca LIKE '%"+buscar+"%' OR ap.modelo LIKE '%"+buscar+"%'"
                        + " OR ap.color LIKE '%"+buscar+"%' OR ap.numero_serie LIKE '%"+buscar+"%'"
                        + " OR ap.observaciones LIKE '%"+buscar+"%' OR a.concepto LIKE '%"+buscar+"%'"
                        + " OR a.total LIKE '%"+buscar+"%' OR a.observaciones LIKE '%"+buscar+"%'"
                        + " OR a.Provincia LIKE '%"+buscar+"%' OR a.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR a.CP LIKE '%"+buscar+"%' OR a.Calle LIKE '%"+buscar+"%' "
                        + " OR a.Numero LIKE '%"+buscar+"%' OR a.Escalera LIKE '%"+buscar+"%' "
                        + " OR a.Piso LIKE '%"+buscar+"%' OR a.Puerta LIKE '%"+buscar+"%')";
                
                if(pendiente)
                    consulta +=" AND a.id_Factura IS NULL ";
                
                consulta += " ORDER BY a.fch_Creacion DESC";
                
                res = st.executeQuery(consulta);
                while(res.next()){
                        if(this.documento.get(res.getString("id_Entrada")+";Entrada")== null){
                            if(this.cliente.get(res.getInt("cli.id_Cliente"))==null){
                                cli = new Cliente(res.getString("cli.nombre"),res.getString("cli.apellidos"),res.getString("cli.nif"),
                                            res.getString("cli.provincia"),res.getString("cli.poblacion"),
                                            res.getInt("cli.cp"),res.getString("cli.calle"),res.getString("cli.numero"),
                                            res.getString("cli.escalera"),res.getInt("cli.piso"),res.getString("cli.puerta"),
                                            res.getInt("cli.tlf_Contacto"), res.getInt("cli.tlf_Auxiliar"),
                                            res.getString("cli.correo_electronico"),res.getString("cli.observaciones"),this);

                                this.cliente.putIfAbsent(res.getInt("cli.id_Cliente"), cli);
                            }else{
                                cli = (Cliente)this.cliente.get(res.getInt("cli.id_Cliente"));
                            }

                            entrada = new Entrada(res.getString("e.observaciones"), res.getString("e.lugar"),
                                    res.getTimestamp("e.fch_Entrada"),this,cli,
                                    new Averia(res.getString("av.motivo"),res.getString("av.descripcion"),
                                        new Aparato(res.getString("ap.tipo"), res.getString("ap.marca"),
                                                res.getString("ap.modelo"), res.getString("ap.color"),
                                                res.getString("ap.numero_serie"),res.getString("ap.observaciones"),cli,res.getInt("ap.id_Aparato")),null));

                            cli.setEntrada(res.getInt("id_Entrada"), entrada);

                            this.documento.putIfAbsent(res.getInt("e.id_Entrada")+";Etrada", entrada);
                        }else{
                            entrada = (Entrada)this.documento.get(res.getInt("e.id_Entrada")+";Entrada");
                        }

                        entrada.cargarPresupuesto(res.getInt("e.id_Entrada"));
                        albaranes.put(res.getInt("a.id_Albaran")+";Albaran",this.documento.get(res.getInt("a.id_Albaran")+";Albaran"));
                }    
                
                // Buscar en ventas
                consulta = "SELECT a.*, cli.* FROM (((albaranes a INNER JOIN clientes cli ON a.id_Cliente = cli.id_Cliente)"
                        + " INNER JOIN materiales_utilizados mat ON mat.id_Albaran=a.id_Albaran) INNER JOIN stock s ON s.id_stock = mat.id_stock)"
                        + " WHERE (CONCAT(cli.nombre,' ',cli.apellidos) LIKE '%"+buscar+"%'"
                        + " OR cli.Observaciones LIKE '%"+buscar+"%' OR cli.NIF LIKE'%"+buscar+"%' "
                        + " OR cli.Provincia LIKE '%"+buscar+"%' OR cli.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR cli.CP LIKE '%"+buscar+"%' OR cli.Calle LIKE '%"+buscar+"%' "
                        + " OR cli.Numero LIKE '%"+buscar+"%' OR cli.Escalera LIKE '%"+buscar+"%' "
                        + " OR cli.Piso LIKE '%"+buscar+"%' OR cli.Puerta LIKE '%"+buscar+"%' "
                        + " OR cli.Correo_electronico LIKE '%"+buscar+"%' OR cli.tlf_Contacto LIKE '%"+buscar+"%' "
                        + " OR cli.tlf_Auxiliar LIKE '%"+buscar+"%' OR a.concepto LIKE '%"+buscar+"%'"
                        + " OR a.total LIKE '%"+buscar+"%' OR a.observaciones LIKE '%"+buscar+"%'"
                        + " OR a.Provincia LIKE '%"+buscar+"%' OR a.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR a.CP LIKE '%"+buscar+"%' OR a.Calle LIKE '%"+buscar+"%' "
                        + " OR a.Numero LIKE '%"+buscar+"%' OR a.Escalera LIKE '%"+buscar+"%' "
                        + " OR a.Piso LIKE '%"+buscar+"%' OR a.Puerta LIKE '%"+buscar+"%'"
                        + " OR s.nombre LIKE '%"+buscar+"%' OR s.descripcion LIKE '"+buscar+"')";
                
                if(pendiente)
                    consulta +=" AND a.id_Factura IS NULL ";
                
                consulta += " ORDER BY a.fch_Creacion DESC";
                
                res = st.executeQuery(consulta);
                int id_Albaran;
                ResultSet resMaps;
                Statement stMaps;
                this.getStock();
                while(res.next()){
                    id_Albaran = res.getInt("id_Albaran");
                    stMaps = con.createStatement();
                    resMaps = stMaps.executeQuery("Select m_u.*,s.id_Proveedor FROM materiales_utilizados m_u INNER JOIN stock s ON m_u.id_Stock = s.id_Stock WHERE m_u.id_Albaran='"+id_Albaran+"' ORDER BY m_u.id_Albaran DESC ");
                    Map materiales = new HashMap();
                    while(resMaps.next()){ // Materiales
                        materiales.put(resMaps.getInt("m_u.id_Stock"),new MaterialTrabajos(((Proveedor)this.proveedor.get(resMaps.getInt("s.id_Proveedor"))).getStock(resMaps.getInt("m_u.id_Stock")),resMaps.getFloat("m_u.Cantidad")));
                    }
                    stMaps.close();
                            
                    Albaran albaran = new Albaran(res.getString("Concepto"),res.getString("Provincia"),res.getString("Poblacion"),
                          res.getInt("CP"),res.getString("calle"),res.getString("numero"),res.getString("escalera"),res.getInt("piso"),
                          res.getString("Puerta"),materiales,res.getString("Observaciones"),res.getFloat("Total"),
                          res.getTimestamp("fch_Creacion"),this.getCliente(res.getInt("id_Cliente")),this);
                        
                        if(this.documento.putIfAbsent(id_Albaran+";Albaran",albaran) == null)
                            albaran.getCliente().setAlbaran(id_Albaran, albaran);
                        else
                            ((Albaran)this.documento.get(id_Albaran+";Albaran")).actualizarVenta(res.getString("Concepto"),res.getString("Provincia"),
                                res.getString("Poblacion"),res.getInt("CP"),res.getString("calle"),res.getString("numero"),
                                res.getString("escalera"),res.getInt("piso"),res.getString("Puerta"),materiales,
                                res.getString("Observaciones"),res.getFloat("Total"));
                        
                        albaranes.put(id_Albaran+";Albaran",albaran);
                }
                
            return albaranes;
             
        } catch (SQLException ex) {
            Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
            return new HashMap();    
        }
    }
    
    public Map recuperarFactura(int idAlbaran){
            try {
                Map mapFactura = new HashMap();
                // Torna un map de la facuta actual
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT f.*, a.id_Cliente id_Cliente,GROUP_CONCAT(concat(IF(e.id_entrada is null,'',e.id_entrada),';',id_Albaran)) EntradaAlbaran"
                        + " FROM (((albaranes a INNER JOIN facturas f ON a.id_factura = f.id_factura) LEFT JOIN presupuestos p ON a.id_Presupuesto=p.id_Presupuesto)"
                        + " LEFT JOIN entradas e ON e.id_entrada=p.id_entrada) WHERE a.id_Factura=(SELECT id_Factura FROM albaranes"
                        + " WHERE id_Albaran="+idAlbaran+") AND a.id_Factura IS NOT NULL GROUP BY a.id_Factura");
                Factura factura;
                String[] vecEntradasAlbaranes;
                if(res.next()){
                    vecEntradasAlbaranes = res.getString("EntradaAlbaran").split(",");
                    factura = new Factura(res.getString("f.concepto"),res.getString("f.forma_pago"),res.getString("f.observaciones"),
                        res.getTimestamp("f.fch_creacion"),this.getCliente(res.getInt("id_Cliente")),this);
                    if(this.documento.putIfAbsent(res.getInt("f.id_Factura")+";Factura",factura)==null){
                        mapFactura.putIfAbsent(res.getInt("f.id_Factura")+";Factura",factura);
                        ((Cliente)this.cliente.get(res.getInt("id_Cliente"))).setFactura(res.getInt("f.id_Factura"), factura);
                        
                        ((Albaran)this.documento.get(idAlbaran+";Albaran")).setFactura(factura);
                        factura.setAlbaran(idAlbaran,(Albaran)this.documento.get(idAlbaran+";Albaran"));
                        Map auxMap;
                        for(int i=0; i < vecEntradasAlbaranes.length;i++){
                            if(vecEntradasAlbaranes[i].split(";")[0].isEmpty()){
                                auxMap=this.recuperarVenta(Integer.parseInt(vecEntradasAlbaranes[i].split(";")[1]));
                                ((Albaran)auxMap.get(Integer.parseInt(vecEntradasAlbaranes[i].split(";")[1]))).setFactura(factura);
                                factura.setAlbaran(Integer.parseInt(vecEntradasAlbaranes[i].split(";")[1]),(Albaran)auxMap.get(Integer.parseInt(vecEntradasAlbaranes[i].split(";")[1])));
                            }else{
                                Entrada entrada =this.getEntrada(Integer.parseInt(vecEntradasAlbaranes[i].split(";")[0]));
                                entrada.cargarPresupuesto(Integer.parseInt(vecEntradasAlbaranes[i].split(";")[0]));
                            }
                        }
                    }else{
                        factura = (Factura)this.documento.get(res.getInt("f.id_Factura")+";Factura");
                        factura.actualizar(res.getString("f.concepto"),res.getString("f.forma_pago"),res.getString("f.observaciones"));
                        factura.setAlbaran(idAlbaran,(Albaran)this.documento.get(idAlbaran+";Albaran"));
                        mapFactura.putIfAbsent(res.getInt("f.id_Factura")+";Factura",factura);
                    }
                    ((Cliente)this.cliente.get(res.getInt("id_Cliente"))).setFactura(res.getInt("f.id_Factura"), factura);
                    st.close();
                }
                return mapFactura;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return new HashMap();
            }
    }
    
    public void reuperarVentasCliente(int idCliente){
        int id_Albaran = 0;
        this.getStock();
        try{
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("Select * FROM albaranes WHERE id_Cliente='"+idCliente+"' AND id_Presupuesto IS NULL ORDER BY id_Albaran DESC ");
            Statement stMaps;    
            ResultSet resMaps;
            
            while(res.next()){
                id_Albaran = res.getInt("id_Albaran");
                stMaps = con.createStatement();
                resMaps = stMaps.executeQuery("Select m_u.*,s.id_Proveedor FROM materiales_utilizados m_u INNER JOIN stock s ON m_u.id_Stock = s.id_Stock WHERE m_u.id_Albaran='"+id_Albaran+"' ORDER BY m_u.id_Albaran DESC ");
                Map materiales = new HashMap();
                while(resMaps.next()){ // Materiales
                    materiales.put(resMaps.getInt("m_u.id_Stock"),new MaterialTrabajos(((Proveedor)this.proveedor.get(resMaps.getInt("s.id_Proveedor"))).getStock(resMaps.getInt("m_u.id_Stock")),resMaps.getFloat("m_u.Cantidad")));
                }
                        
                Albaran albaran = new Albaran(res.getString("Concepto"),res.getString("Provincia"),res.getString("Poblacion"),
                          res.getInt("CP"),res.getString("calle"),res.getString("numero"),res.getString("escalera"),res.getInt("piso"),
                          res.getString("Puerta"),materiales,res.getString("Observaciones"),res.getFloat("Total"),
                          (res.getTimestamp("fch_Creacion")),this.getCliente(idCliente),this);
                        
                if(this.documento.putIfAbsent(id_Albaran+";Albaran",albaran) == null){
                    albaran.getCliente().setAlbaran(id_Albaran, albaran);
                }else
                    ((Albaran)this.documento.get(id_Albaran+";Albaran")).actualizarVenta(res.getString("Concepto"),res.getString("Provincia"),
                        res.getString("Poblacion"),res.getInt("CP"),res.getString("calle"),res.getString("numero"),
                        res.getString("escalera"),res.getInt("piso"),res.getString("Puerta"),materiales,
                        res.getString("Observaciones"),res.getFloat("Total"));
                stMaps.close();
                ((Cliente)this.cliente.get(idCliente)).setAlbaran(id_Albaran,(Albaran)this.documento.get(id_Albaran+";Albaran"));
            }
            st.close();
        } catch (Exception ex) {
                    Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Map recuperarVenta(int idAlbaran){
        this.getStock();
        try{
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery("Select * FROM albaranes WHERE id_Albaran='"+idAlbaran+"' AND id_Presupuesto IS NULL ORDER BY id_Albaran DESC ");
            Statement stMaps;    
            ResultSet resMaps;
            
            Map albaranes = new HashMap();
            
            if(res.next()){
                  stMaps = con.createStatement();
                  resMaps = stMaps.executeQuery("Select m_u.*,s.id_Proveedor FROM materiales_utilizados m_u INNER JOIN stock s ON m_u.id_Stock = s.id_Stock WHERE m_u.id_Albaran='"+idAlbaran+"' ORDER BY m_u.id_Albaran DESC ");
                  Map materiales = new HashMap();
                  while(resMaps.next()){ // Materiales
                      materiales.put(resMaps.getInt("m_u.id_Stock"),new MaterialTrabajos(((Proveedor)this.proveedor.get(resMaps.getInt("s.id_Proveedor"))).getStock(resMaps.getInt("m_u.id_Stock")),resMaps.getFloat("m_u.Cantidad")));
                    }
                        
                  Albaran albaran = new Albaran(res.getString("Concepto"),res.getString("Provincia"),res.getString("Poblacion"),
                          res.getInt("CP"),res.getString("calle"),res.getString("numero"),res.getString("escalera"),res.getInt("piso"),
                          res.getString("Puerta"),materiales,res.getString("Observaciones"),res.getFloat("Total"),
                          (res.getTimestamp("fch_Creacion")),this.getCliente(res.getInt("id_Cliente")),this);
                        
                        if(this.documento.putIfAbsent(idAlbaran+";Albaran",albaran) == null){
                            albaran.getCliente().setAlbaran(idAlbaran, albaran);
                            albaranes.put(idAlbaran,albaran);
                        }else{
                            ((Albaran)this.documento.get(idAlbaran+";Albaran")).actualizarVenta(res.getString("Concepto"),res.getString("Provincia"),
                                res.getString("Poblacion"),res.getInt("CP"),res.getString("calle"),res.getString("numero"),
                                res.getString("escalera"),res.getInt("piso"),res.getString("Puerta"),materiales,
                                res.getString("Observaciones"),res.getFloat("Total"));
                            albaranes.put(idAlbaran,this.documento.get(idAlbaran+";Albaran"));
                        }
                        //((Cliente)this.cliente.get(res.getInt("id_cliente"))).setAlbaran(idAlbaran,(Albaran)this.documento.get(idAlbaran+";Albaran"));
                    stMaps.close();
                }
            st.close();
            return albaranes;
        } catch (Exception ex) {
            Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
            return new HashMap();
        }
    }
    
    public Map buscarFactura(String buscar, boolean pendiente){
            try {
                Statement st = con.createStatement();
                Map facturas = new HashMap();
                
                String consulta = "SELECT f.*,GROUP_CONCAT(if(e.id_entrada is null,'',e.id_entrada),';',"
                        + " a.id_albaran) EntradasAlbaranes, a.* FROM ((((albaranes a INNER JOIN (facturas f LEFT JOIN recibos r ON"
                        + " f.id_factura=r.id_factura) ON f.id_Factura=a.id_Factura)"
                        + " INNER JOIN clientes cli ON a.id_cliente=cli.id_cliente)"
                        + " LEFT JOIN presupuestos p ON a.id_Presupuesto=p.id_Presupuesto)"
                        + " LEFT JOIN ((entradas e LEFT JOIN (citas cita INNER JOIN direccion_citas dir"
                        + " ON cita.direccion = dir.id_direccion) ON cita.id_Entrada = e.id_entrada) LEFT JOIN"
                        + " (citas_empleados c_e INNER JOIN usuarios u ON c_e.id_Usuario = u.id_Usuario)"
                        + " ON cita.id_Cita=c_e.id_Cita) LEFT JOIN (averias av INNER JOIN aparatos ap"
                        + " ON av.id_aparato = ap.id_aparato) ON e.id_Entrada=e.id_Entrada ON e.id_entrada=p.id_entrada)"
                        + " WHERE (CONCAT(cli.nombre,' ',cli.apellidos) LIKE '%"+buscar+"%'"
                        + " OR cli.Observaciones LIKE '%"+buscar+"%' OR cli.NIF LIKE'%"+buscar+"%' "
                        + " OR cli.Provincia LIKE '%"+buscar+"%' OR cli.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR cli.CP LIKE '%"+buscar+"%' OR cli.Calle LIKE '%"+buscar+"%' "
                        + " OR cli.Numero LIKE '%"+buscar+"%' OR cli.Escalera LIKE '%"+buscar+"%' "
                        + " OR cli.Piso LIKE '%"+buscar+"%' OR cli.Puerta LIKE '%"+buscar+"%' "
                        + " OR cli.Correo_electronico LIKE '%"+buscar+"%' OR cli.tlf_Contacto LIKE '%"+buscar+"%' "
                        + " OR cli.tlf_Auxiliar LIKE '%"+buscar+"%' OR e.lugar LIKE '%"+buscar+"%'"
                        + " OR e.observaciones LIKE '%"+buscar+"%' OR av.motivo LIKE '%"+buscar+"%'"
                        + " OR av.descripcion LIKE '%"+buscar+"%' OR ap.tipo LIKE '%"+buscar+"%'"
                        + " OR ap.marca LIKE '%"+buscar+"%' OR ap.modelo LIKE '%"+buscar+"%'"
                        + " OR ap.color LIKE '%"+buscar+"%' OR ap.numero_serie LIKE '%"+buscar+"%'"
                        + " OR ap.observaciones LIKE '%"+buscar+"%' OR a.concepto LIKE '%"+buscar+"%'"
                        + " OR a.total LIKE '%"+buscar+"%' OR a.observaciones LIKE '%"+buscar+"%'"
                        + " OR a.Provincia LIKE '%"+buscar+"%' OR a.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR a.CP LIKE '%"+buscar+"%' OR a.Calle LIKE '%"+buscar+"%' "
                        + " OR a.Numero LIKE '%"+buscar+"%' OR a.Escalera LIKE '%"+buscar+"%' "
                        + " OR a.Piso LIKE '%"+buscar+"%' OR a.Puerta LIKE '%"+buscar+"%'"
                        + " OR cita.motivo LIKE '%"+buscar+"%' OR cita.observaciones LIKE '%"+buscar+"%'"
                        + " OR dir.provincia LIKE '%"+buscar+"%' OR dir.poblacion LIKE '%"+buscar+"%'"
                        + " OR dir.CP LIKE '%"+buscar+"%' OR dir.Calle LIKE '%"+buscar+"%'"
                        + " OR dir.Numero LIKE '%"+buscar+"%'OR dir.Escalera LIKE '%"+buscar+"%'"
                        + " OR dir.Piso LIKE '%"+buscar+"%' OR dir.Puerta LIKE '%"+buscar+"%'"
                        + " OR f.concepto LIKE '%"+buscar+"%' OR f.observaciones LIKE '%"+buscar+"%'"
                        + " OR f.forma_pago LIKE '%"+buscar+"%') ";
                
                if(pendiente)
                    consulta+= " AND r.id_Recibo IS NULL ";
                        
                consulta += " GROUP BY f.id_Factura";
 
                ResultSet res = st.executeQuery(consulta);
                while(res.next()){ // agafar id Albaran i anar a carregar facturas
                    String[] idsEntAlb = res.getString("EntradasAlbaranes").split(",")[0].split(";");
                    if(idsEntAlb[0].isEmpty()){ // Venta
                        this.recuperarVenta(Integer.parseInt(idsEntAlb[1]));
                    }else{ // Entrada
                        int id_Albaran = Integer.parseInt(idsEntAlb[1]);
                        Statement stMaps = con.createStatement();
                        ResultSet resMaps = stMaps.executeQuery("Select m_u.*,s.id_Proveedor FROM materiales_utilizados m_u INNER JOIN stock s ON m_u.id_Stock = s.id_Stock WHERE m_u.id_Albaran='"+id_Albaran+"' ORDER BY m_u.id_Albaran DESC ");
                        Map materiales = new HashMap();
                        while(resMaps.next()){ // Materiales
                            materiales.put(resMaps.getInt("m_u.id_Stock"),new MaterialTrabajos(((Proveedor)this.proveedor.get(resMaps.getInt("s.id_Proveedor"))).getStock(resMaps.getInt("m_u.id_Stock")),resMaps.getFloat("m_u.Cantidad")));
                          }

                        Map trabajos = new HashMap();
                        resMaps = stMaps.executeQuery("SELECT * FROM trabajos_realizados t_r INNER JOIN albaranes a ON t_r.id_Albaran = a.id_Albaran Where a.id_Albaran = '"+id_Albaran+"'");
                        while(resMaps.next()){
                            trabajos.put(resMaps.getInt("t_r.id_Usuario"),new Trabajo(resMaps.getFloat("t_r.horas"),resMaps.getString("t_r.Descripcion"),(Empleado)empleados.get(resMaps.getInt("t_r.id_Usuario"))));
                        }
                        stMaps.close();
                        Albaran albaran = new Albaran(res.getString("a.Concepto"),res.getString("a.Provincia"),res.getString("a.Poblacion"),
                                res.getInt("a.CP"),res.getString("a.calle"),res.getString("a.numero"),res.getString("a.escalera"),res.getInt("a.piso"),
                                res.getString("a.Puerta"),materiales,trabajos,res.getString("a.Observaciones"),res.getFloat("a.Total"),
                                (res.getTimestamp("a.fch_Creacion")),(res.getTimestamp("a.fch_Entrega")),null,this.getCliente(res.getInt("a.id_Cliente")),this);
                          if(this.documento.putIfAbsent(id_Albaran+";Albaran",albaran)==null){
                              albaran.getCliente().setAlbaran(id_Albaran, albaran);
                          }else{
                              ((Albaran)this.documento.get(id_Albaran+";Albaran")).actualizarAlbaran(res.getString("a.Concepto"),res.getString("a.Provincia"),res.getString("a.Poblacion"),
                                  res.getInt("a.CP"),res.getString("a.calle"),res.getString("a.numero"),res.getString("a.escalera"),res.getInt("a.piso"),
                                  res.getString("a.Puerta"),materiales,trabajos,res.getString("a.Observaciones"),res.getFloat("a.Total"));
                          }
                          stMaps.close();
                    }
                    facturas.putAll(this.recuperarFactura(Integer.parseInt(idsEntAlb[1])));
                }
                st.close();
                return facturas;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return new HashMap();
            }
    }
    
    public boolean guardarFactura(int idFactura, Factura factura){
            try {
                Statement st = con.createStatement();
                
                if(st.executeUpdate("UPDATE facturas set concepto='"+factura.getConcepto()+"',"
                        + "forma_pago='"+factura.getFormaPago()+"',observaciones='"+factura.getObservaciones()+"',"
                        + "total="+factura.getTotal()+" WHERE id_factura="+idFactura)>0){
                    Iterator itALbaranes = factura.getAlbaranes().entrySet().iterator();
                    String condicion="";
                    while(itALbaranes.hasNext()){
                        Map.Entry aux = (Map.Entry)itALbaranes.next();
                        if(condicion.isEmpty())
                            condicion += " id_Albaran = "+aux.getKey();
                        else
                            condicion += " OR id_Albaran = "+aux.getKey();
                    }
                    
                    if(st.executeUpdate("UPDATE albaranes set id_factura=null WHERE id_Factura ="+idFactura)>0){
                        if(st.executeUpdate("UPDATE albaranes set id_factura="+idFactura+" WHERE "+condicion)>0){
                            st.close();
                            return true;
                        }else{
                            st.close();
                            return false;
                        }
                    }else{
                        st.close();
                        return false;
                    }
                }else{
                    st.close();
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
    }
    
    public boolean guardarCliente(int idCliente, Cliente cli){
            try {
                Statement st = con.createStatement();
                if(st.executeUpdate("UPDATE clientes set nombre='"+cli.getNombre()+"',apellidos='"+cli.getApellidos()+"',"
                        + "nif='"+cli.getNif()+"',provincia='"+cli.getProvincia()+"',poblacion='"+cli.getPoblacion()+"',"
                        + "cp='"+cli.getCp()+"',calle='"+cli.getCalle()+"',numero='"+cli.getNumero()+"',"
                        + "escalera='"+cli.getEscalera()+"',piso='"+cli.getPiso()+"',puerta='"+cli.getPuerta()+"',"
                        + "tlf_contacto='"+cli.getTlfFijo()+"',tlf_auxiliar='"+cli.getTlfMovil()+"',"
                        + "correo_electronico='"+cli.getEmail()+"',observaciones='"+cli.getObservaciones()+"' "
                        + " WHERE id_Cliente="+idCliente)>0){
                    st.close();
                    return true;
                }else{
                    st.close();
                    return false;
                }
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
    }
    
        
    public int crearRecibo(int idFactura, Recibo recibo){
        try {
                Statement st = con.createStatement();
                st.executeUpdate("INSERT INTO recibos(fch_creacion,provincia,poblacion,cp,calle,"
                        + "numero,escalera,piso,puerta,observaciones,id_Factura) VALUES("
                        + "'"+((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(recibo.getFchCreacion()))+"',"
                        + "'"+recibo.getProvincia()+"','"+recibo.getPoblacion()+"','"+recibo.getCp()+"',"
                        + "'"+recibo.getCalle()+"','"+recibo.getNumero()+"','"+recibo.getEscalera()+"',"
                        + "'"+recibo.getPiso()+"','"+recibo.getPuerta()+"','"+recibo.getObservaciones()+"','"+idFactura+"')");
                
                ResultSet res = st.executeQuery("SELECT id_Recibo FROM recibos WHERE fch_creacion='"+((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(recibo.getFchCreacion()))+"' ORDER BY id_factura DESC");
                int id_Recibo=0;    
                if(res.next()){
                    id_Recibo = res.getInt("id_Recibo");
                    this.documento.put(id_Recibo+";Recibo",recibo);
                    recibo.getFactura().getCliente().setRecibo(id_Recibo, recibo);
                }
                return id_Recibo;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return 0;
            }
    }
    
    public Map recuperarRecibo(int idFactura){
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery("SELECT * FROM recibos WHERE id_Factura ="+idFactura);
                
                Map mapRecibo = new HashMap();
                Recibo recibo;
                if(res.next()){
                    recibo = new Recibo(res.getString("provincia"),
                    res.getString("poblacion"),res.getInt("cp"),res.getString("calle"), res.getString("numero"),
                    res.getString("escalera"),res.getInt("piso"),res.getString("puerta"),res.getString("observaciones"),
                    this.getFactura(res.getInt("id_Factura")),this);
                    if(this.documento.putIfAbsent(res.getInt("id_Recibo")+";Recibo",recibo) ==null){
                        mapRecibo.put(res.getInt("id_Recibo")+";Recibo",recibo);
                    }else{
                        mapRecibo.put(res.getInt("id_Recibo")+";Recibo",this.documento.get(res.getInt("id_Recibo")+";Recibo"));
                    }
                    recibo.getCliente().setRecibo(res.getInt("id_Recibo"), recibo);
                }
                st.close();
                return mapRecibo;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return new HashMap();
            }
    }
    
    public Map buscarRecibo(String buscar){
            try {
                Statement st = con.createStatement();
                Map recibos = new HashMap();
                Iterator facturas;
                
                String consulta = "SELECT r.*,f.*,GROUP_CONCAT(if(e.id_entrada is null,'',e.id_entrada),';',"
                        + " a.id_albaran) EntradasAlbaranes, a.* FROM ((((albaranes a INNER JOIN (facturas f INNER JOIN recibos r ON"
                        + " f.id_factura=r.id_factura) ON f.id_Factura=a.id_Factura)"
                        + " INNER JOIN clientes cli ON a.id_cliente=cli.id_cliente)"
                        + " LEFT JOIN presupuestos p ON a.id_Presupuesto=p.id_Presupuesto)"
                        + " LEFT JOIN ((entradas e LEFT JOIN (citas cita INNER JOIN direccion_citas dir"
                        + " ON cita.direccion = dir.id_direccion) ON cita.id_Entrada = e.id_entrada) LEFT JOIN"
                        + " (citas_empleados c_e INNER JOIN usuarios u ON c_e.id_Usuario = u.id_Usuario)"
                        + " ON cita.id_Cita=c_e.id_Cita) LEFT JOIN (averias av INNER JOIN aparatos ap"
                        + " ON av.id_aparato = ap.id_aparato) ON e.id_Entrada=e.id_Entrada ON e.id_entrada=p.id_entrada)"
                        + " WHERE (CONCAT(cli.nombre,' ',cli.apellidos) LIKE '%"+buscar+"%'"
                        + " OR cli.Observaciones LIKE '%"+buscar+"%' OR cli.NIF LIKE'%"+buscar+"%' "
                        + " OR cli.Provincia LIKE '%"+buscar+"%' OR cli.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR cli.CP LIKE '%"+buscar+"%' OR cli.Calle LIKE '%"+buscar+"%' "
                        + " OR cli.Numero LIKE '%"+buscar+"%' OR cli.Escalera LIKE '%"+buscar+"%' "
                        + " OR cli.Piso LIKE '%"+buscar+"%' OR cli.Puerta LIKE '%"+buscar+"%' "
                        + " OR cli.Correo_electronico LIKE '%"+buscar+"%' OR cli.tlf_Contacto LIKE '%"+buscar+"%' "
                        + " OR cli.tlf_Auxiliar LIKE '%"+buscar+"%' OR e.lugar LIKE '%"+buscar+"%'"
                        + " OR e.observaciones LIKE '%"+buscar+"%' OR av.motivo LIKE '%"+buscar+"%'"
                        + " OR av.descripcion LIKE '%"+buscar+"%' OR ap.tipo LIKE '%"+buscar+"%'"
                        + " OR ap.marca LIKE '%"+buscar+"%' OR ap.modelo LIKE '%"+buscar+"%'"
                        + " OR ap.color LIKE '%"+buscar+"%' OR ap.numero_serie LIKE '%"+buscar+"%'"
                        + " OR ap.observaciones LIKE '%"+buscar+"%' OR a.concepto LIKE '%"+buscar+"%'"
                        + " OR a.total LIKE '%"+buscar+"%' OR a.observaciones LIKE '%"+buscar+"%'"
                        + " OR a.Provincia LIKE '%"+buscar+"%' OR a.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR a.CP LIKE '%"+buscar+"%' OR a.Calle LIKE '%"+buscar+"%' "
                        + " OR a.Numero LIKE '%"+buscar+"%' OR a.Escalera LIKE '%"+buscar+"%' "
                        + " OR a.Piso LIKE '%"+buscar+"%' OR a.Puerta LIKE '%"+buscar+"%'"
                        + " OR cita.motivo LIKE '%"+buscar+"%' OR cita.observaciones LIKE '%"+buscar+"%'"
                        + " OR dir.provincia LIKE '%"+buscar+"%' OR dir.poblacion LIKE '%"+buscar+"%'"
                        + " OR dir.CP LIKE '%"+buscar+"%' OR dir.Calle LIKE '%"+buscar+"%'"
                        + " OR dir.Numero LIKE '%"+buscar+"%'OR dir.Escalera LIKE '%"+buscar+"%'"
                        + " OR dir.Piso LIKE '%"+buscar+"%' OR dir.Puerta LIKE '%"+buscar+"%'"
                        + " OR f.concepto LIKE '%"+buscar+"%' OR f.observaciones LIKE '%"+buscar+"%'"
                        + " OR f.forma_pago LIKE '%"+buscar+"%' OR r.observaciones LIKE '%"+buscar+"%'"
                        + " OR r.Provincia LIKE '%"+buscar+"%' OR r.Poblacion LIKE '%"+ buscar+"%' "
                        + " OR r.CP LIKE '%"+buscar+"%' OR r.Calle LIKE '%"+buscar+"%' "
                        + " OR r.Numero LIKE '%"+buscar+"%' OR r.Escalera LIKE '%"+buscar+"%' "
                        + " OR r.Piso LIKE '%"+buscar+"%' OR r.Puerta LIKE '%"+buscar+"%') GROUP BY f.id_Factura";
 
                ResultSet res = st.executeQuery(consulta);
                while(res.next()){ // agafar id Albaran i anar a carregar facturas
                    String[] idsEntAlb = res.getString("EntradasAlbaranes").split(",")[0].split(";");
                    if(idsEntAlb[0].isEmpty()){ // Venta
                        this.recuperarVenta(Integer.parseInt(idsEntAlb[1]));
                    }else{ // Entrada
                        int id_Albaran = Integer.parseInt(idsEntAlb[1]);
                        Statement stMaps = con.createStatement();
                        ResultSet resMaps = stMaps.executeQuery("Select m_u.*,s.id_Proveedor FROM materiales_utilizados m_u INNER JOIN stock s ON m_u.id_Stock = s.id_Stock WHERE m_u.id_Albaran='"+id_Albaran+"' ORDER BY m_u.id_Albaran DESC ");
                        Map materiales = new HashMap();
                        while(resMaps.next()){ // Materiales
                            materiales.put(resMaps.getInt("m_u.id_Stock"),new MaterialTrabajos(((Proveedor)this.proveedor.get(resMaps.getInt("s.id_Proveedor"))).getStock(resMaps.getInt("m_u.id_Stock")),resMaps.getFloat("m_u.Cantidad")));
                          }

                        Map trabajos = new HashMap();
                        resMaps = stMaps.executeQuery("SELECT * FROM trabajos_realizados t_r INNER JOIN albaranes a ON t_r.id_Albaran = a.id_Albaran Where a.id_Albaran = '"+id_Albaran+"'");
                        while(resMaps.next()){
                            trabajos.put(resMaps.getInt("t_r.id_Usuario"),new Trabajo(resMaps.getFloat("t_r.horas"),resMaps.getString("t_r.Descripcion"),(Empleado)empleados.get(resMaps.getInt("t_r.id_Usuario"))));
                        }
                        stMaps.close();
                        Albaran albaran = new Albaran(res.getString("a.Concepto"),res.getString("a.Provincia"),res.getString("a.Poblacion"),
                                res.getInt("a.CP"),res.getString("a.calle"),res.getString("a.numero"),res.getString("a.escalera"),res.getInt("a.piso"),
                                res.getString("a.Puerta"),materiales,trabajos,res.getString("a.Observaciones"),res.getFloat("a.Total"),
                                (res.getTimestamp("a.fch_Creacion")),(res.getTimestamp("a.fch_Entrega")),null,this.getCliente(res.getInt("a.id_Cliente")),this);
                          if(this.documento.putIfAbsent(id_Albaran+";Albaran",albaran)==null){
                              albaran.getCliente().setAlbaran(id_Albaran, albaran);
                          }else{
                              ((Albaran)this.documento.get(id_Albaran+";Albaran")).actualizarAlbaran(res.getString("a.Concepto"),res.getString("a.Provincia"),res.getString("a.Poblacion"),
                                  res.getInt("a.CP"),res.getString("a.calle"),res.getString("a.numero"),res.getString("a.escalera"),res.getInt("a.piso"),
                                  res.getString("a.Puerta"),materiales,trabajos,res.getString("a.Observaciones"),res.getFloat("a.Total"));
                          }
                          stMaps.close();
                    }
                    facturas = this.recuperarFactura(Integer.parseInt(idsEntAlb[1])).entrySet().iterator();
                    while(facturas.hasNext()){
                        Map.Entry aux = (Map.Entry)facturas.next();
                        if(this.documento.putIfAbsent(res.getInt("r.id_Recibo")+";Recibo", ((Factura)aux.getValue()).getRecibo())==null)
                            ((Factura)aux.getValue()).getCliente().setRecibo(res.getInt("r.id_recibo"), ((Factura)aux.getValue()).getRecibo());
                        
                        recibos.put(res.getInt("r.id_Recibo")+";Recibo", ((Factura)aux.getValue()).getRecibo());
                    }
                }
                st.close();
                return recibos;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return new HashMap();
            }
    }
    
    public boolean entregaAlbaran(int idAlbaran, Albaran albaran){
        try {
                Statement st = con.createStatement();
                if(st.executeUpdate("UPDATE albaranes SET fch_Entrega='"+((new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(albaran.getFchEntrega()))+"' "
                        + " WHERE id_Albaran='"+idAlbaran+"'") > 0){
                    st.close();
                    return true;
                }else
                    return false;
            } catch (SQLException ex) {
                Logger.getLogger(GestoSAT.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
    }
}   