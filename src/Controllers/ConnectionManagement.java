package Controllers;

import Models.Brand;
import Models.Coche;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class ConnectionManagement implements MagementInterface {

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
                System.err.println("Fichero no encontrado");
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
                System.out.println("| " + rsmd.getColumnName(1) + " - " + rset.getString(1) + "\t | \t" + rsmd.getColumnName(2) + " - " + rset.getString(2) + "\t | \t" + rsmd.getColumnName(3) + " - " + rset.getString(3) + "\t | \t" + rsmd.getColumnName(4) + " - " + rset.getString(4) + " cv \t | \t" + rsmd.getColumnName(5) + " - " + rset.getString(5) + " \t | \t");
            rset.close();
            pstmt.close();
        } catch (SQLException s) {
            s.printStackTrace();
        }

    }

    @Override
    public void write(ArrayList<Coche> coches) {
        for (Coche coche : coches) {
            try {
                PreparedStatement pstmt = conexion.prepareStatement("INSERT INTO coches VALUES (null ,?, ? , ?, ?)");
                pstmt.setString(1, coche.getMarca().getBrandName());
                pstmt.setString(2, coche.getModelo());
                pstmt.setInt(3, coche.getCavallaje());
                pstmt.setString(4, coche.getColor());
                pstmt.executeUpdate();
                pstmt.close();
            } catch (SQLException s) {
                s.printStackTrace();
            }
        }
    }

    @Override
    public ArrayList<Coche> read() {
        ArrayList<Coche> carsInDB = new ArrayList<>();
        Coche c;
        try {
            PreparedStatement pstmt = conexion.prepareStatement("SELECT * from coches_adat.coches");
            ResultSet rset = pstmt.executeQuery();
            HashMap<String, Brand> brands = readBrands();
            while (rset.next()) {
                c = new Coche();
                c.setID(rset.getInt(1));
                c.setMarca(brands.get(rset.getString(2)));
                c.setModelo(rset.getString(3));
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

    @Override
    public void delete(int ID) {
        try {
            PreparedStatement pstmt = conexion.prepareStatement("DELETE FROM coches WHERE coches.ID = ?");
            pstmt.setInt(1, ID);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException s) {
            System.err.println("Error en la consulta sql, porfavor vuelve a intentarlo");
        }
    }

    public void closeConnection() {
        try {
            conexion.close();
        } catch (SQLException e) {
            System.err.println("no se puede cerrar conexion.");
        }

    }

    @Override
    public void update(Coche c, int ID) {
        try {
            PreparedStatement pstmt = conexion.prepareStatement("UPDATE coches SET ID = ?, marca = ?, modelo = ?, cavallaje = ?, color = ? WHERE coches.ID = ?");
            pstmt.setInt(1, ID);
            pstmt.setString(2, c.getMarca().getBrandName());
            pstmt.setString(3, c.getModelo());
            pstmt.setInt(4, c.getCavallaje());
            pstmt.setString(5, c.getColor());
            pstmt.setInt(6, ID);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException s) {
            System.err.println("Error en la consulta sql, porfavor vuelve a intentarlo");
        }
    }

    @Override
    public HashMap<String, Brand> readBrands() {
        HashMap<String, Brand> brands = new HashMap<>();
        try {
            PreparedStatement brandspstmt = conexion.prepareStatement("select * from marca");
            ResultSet brandsrset = brandspstmt.executeQuery();
            while (brandsrset.next()) {
                brands.put(brandsrset.getString(2), new Brand(brandsrset.getInt(1), brandsrset.getString(2), brandsrset.getString(3), brandsrset.getInt(4)));
            }
            brandspstmt.close();
            brandsrset.close();
        } catch (SQLException e) {
            System.err.println("Error en la conexion de BBDD");
        }

        return brands;
    }
    @Override
    public void addBrand(String brandName, String brandCountry, int yearOfFundation){
        try {
            PreparedStatement pstmt = conexion.prepareStatement("INSERT INTO marca (idBrand, brandName, brandCountry, brandYearOfFundation) VALUES (NULL, ?, ?, ?)");
            pstmt.setString(1, brandName);
            pstmt.setString(2, brandCountry);
            pstmt.setInt(3, yearOfFundation);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException s) {
            System.err.println("Error en la consulta sql, porfavor vuelve a intentarlo");
        }

    }

    @Override
    public void deleteBrand(String brandName) {
        try {
            PreparedStatement pstmt = conexion.prepareStatement("DELETE FROM coches WHERE coches.marca = ?");
            PreparedStatement preparedStatementBrands = conexion.prepareStatement("DELETE FROM marca WHERE marca.brandName= ?");
            pstmt.setString(1, brandName);
            preparedStatementBrands.setString(1, brandName);
            pstmt.executeUpdate();
            preparedStatementBrands.executeUpdate();
            pstmt.close();
        } catch (SQLException s) {
            System.err.println("Error en la consulta sql, porfavor vuelve a intentarlo");
        }
    }

    @Override
    public void updateBrand(String brandName, Brand brand) {
        try{
            PreparedStatement preparedStatement = conexion.prepareStatement("UPDATE `marca` SET  `brandCountry` = ?, `brandYearOfFundation` = ? WHERE `marca`.brandName = ?");
            preparedStatement.setString(1,brand.getBrandCountry());
            preparedStatement.setInt(2, brand.getBrandYearOfFundation());
            preparedStatement.setString(3, brandName);
        } catch (SQLException e) {
            System.err.println("Error en la consulta SQL");
        }
    }
}
