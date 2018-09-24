import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class ConnectionManagement{

    //Atributos de la clase
    private String bd;
    private String login;
    private String pwd;
    private String url;
    private File archivo = null;
    private FileReader fr = null;
    private BufferedReader br = null;
    private Connection conexion;

    //retrieve configurations for .ini file to make the connection possible.
    public ConnectionManagement() {
        Properties propiedades = new Properties();
        try {

            archivo = new File("config.ini");
            if (archivo.exists()) {
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                propiedades.load(fr);
// obtenemos las propiedades y las imprimimos
                bd = propiedades.getProperty("basedatos");
                login = propiedades.getProperty("usuario");
                pwd = propiedades.getProperty("clave");
                url = "jdbc:mysql://localhost/" + bd + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

            } else
                System.err.println ("Fichero no encontrado");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //conexion
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(url, login, pwd);
            System.out.println(" - Conexión con MySQL establecida -");
        } catch (Exception e) {
            System.out.println(" – Error de Conexión con MySQL -");
            e.printStackTrace();
        }
    }

    public void readFromDBToPrintOnConsole() {

        try {
            PreparedStatement pstmt = conexion.prepareStatement("SELECT * from coches_adat.coches");
            ResultSet rset = pstmt.executeQuery();
            // We get the metadata so we can handle the writing of the said data
            ResultSetMetaData rsmd = rset.getMetaData();
            while (rset.next())
                System.out.println("| " +rsmd.getColumnName(1) + " - " + rset.getString(1) + "\t | \t" + rsmd.getColumnName(2) + " - " + rset.getString(2) + "\t | \t" + rsmd.getColumnName(3) + " - " + rset.getString(3) + "\t | \t" + rsmd.getColumnName(4) + " - " + rset.getString(4) + " cv \t | \t" + rsmd.getColumnName(5) + " - " + rset.getString(5) + " \t | \t");
            rset.close();
            pstmt.close();
        } catch (SQLException s) {
            s.printStackTrace();
        }

    }

    public ArrayList<Coche> getAllCarsInDB(){
        ArrayList<Coche> carsInDB = new ArrayList<>();
        Coche c;
        try {
            PreparedStatement pstmt = conexion.prepareStatement("SELECT * from coches_adat.coches");
            ResultSet rset = pstmt.executeQuery();

            while (rset.next()){
                c = new Coche();
                c.setMarca(rset.getString(2));
                c.setModelo( rset.getString(3));
                c.setCavallaje(Integer.parseInt(rset.getString(4)));
                c.setColor(rset.getString(5));
                carsInDB.add(c);
            }
            rset.close();
            pstmt.close();
        } catch (SQLException s) {
            s.printStackTrace();
        }
        return carsInDB;
    }

    public void writeOnDBFromConsole(Coche coche){
        try {
            PreparedStatement pstmt = conexion.prepareStatement("INSERT INTO coches VALUES (null ,?, ? , ?, ?)");
            pstmt.setString(1, coche.getMarca());
            pstmt.setString(2,coche.getModelo());
            pstmt.setInt(3, coche.getCavallaje());
            pstmt.setString(4, coche.getColor());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

    public void removeACarFromDB(int carID){
        try {
            PreparedStatement pstmt = conexion.prepareStatement("DELETE FROM coches WHERE coches.ID = ?");
            pstmt.setInt(1,carID);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException s) {
            System.err.println("Error en la consulta sql, porfavor vuelve a intentarlo");
        }
    }
    public void closeConnection(){
        try {
            conexion.close();
        } catch (SQLException e) {
           System.err.println("no se puede cerrar conexion.");
        }
    }

    public void updateACarFromDB(Coche coche, int carID){
        try {
            PreparedStatement pstmt = conexion.prepareStatement("UPDATE coches SET ID = ?, marca = ?, modelo = ?, cavallaje = ?, color = ? WHERE coches.ID = ?");
            pstmt.setInt(1, carID);
            pstmt.setString(2, coche.getMarca());
            pstmt.setString(3,coche.getModelo());
            pstmt.setInt(4, coche.getCavallaje());
            pstmt.setString(5, coche.getColor());
            pstmt.setInt(6, carID);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException s) {
            System.err.println("Error en la consulta sql, porfavor vuelve a intentarlo");
        }
    }
}
