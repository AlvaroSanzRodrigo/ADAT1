import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


public class FileManagement implements MagementInterface {

    private FileWriter fw;
    private BufferedWriter writer;
    private String S_Archivo = "";
    private int lastID;
    private File archivo;
    private FileReader fr;
    private BufferedReader br;

    public FileManagement(String S_Archivo, Boolean append) {
        this.S_Archivo = S_Archivo;
        try {
            this.fw = new FileWriter(S_Archivo, append);
            this.writer = new BufferedWriter(fw);
        } catch (IOException e) {
            System.err.println("Escoja otro archivo, este no existe");
        }

    }


    public String createNewFile(String fileName) {
        File file = new File(fileName);
        return fileName;
    }

    @Override
    public void write(ArrayList<Coche> coches) {

        try {

            for (Coche c : coches) {
                writer.append(String.valueOf(c.getID()));
                writer.append("\n" + c.getMarca());
                writer.append("\n" + c.getModelo());
                writer.append("\n" + String.valueOf(c.getCavallaje()));
                writer.append("\n" + c.getColor());
                writer.append("\n#\n");

            }
            writer.close();
            setLastIDToConfig();
        } catch (IOException e) {
            System.err.println("Jaime, que no me lo rompes esto");
        }


    }

    @Override
    public ArrayList<Coche> read() {

        ArrayList<Coche> carList = new ArrayList<>();
        Coche c;

        try {
            // crea los objetos necesarios
            archivo = new File(S_Archivo);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lee el archivo
            String linea;
            while ((linea = br.readLine()) != null) {
                c = new Coche();
                while (!linea.equals("#")) {
                    c.setID(Integer.parseInt(linea));
                    linea = br.readLine();
                    c.setMarca(linea);
                    linea = br.readLine();
                    c.setModelo(linea);
                    linea = br.readLine();
                    c.setCavallaje(Integer.parseInt(linea));
                    linea = br.readLine();
                    c.setColor(linea);
                    linea = br.readLine();
                }
                c.toString();
                carList.add(c);
            }
        } catch (Exception e) {
            System.err.println("Archivo no encontrado");
        }
        return carList;
    }

    @Override
    public void delete(int ID) {
        HashMap<Integer, Coche> cochesHashMap = new HashMap<>();
        for (Coche coche: this.read()) {
            cochesHashMap.put(coche.getID(), coche);
        }
        cochesHashMap.remove(ID);
        saveChangesOnFile(cochesHashMap);
    }

    @Override
    public void update(Coche c, int ID) {
        HashMap<Integer, Coche> cochesHashMap = new HashMap<>();
        for (Coche coche: this.read()) {
            cochesHashMap.put(coche.getID(), coche);
        }
        c.setID(ID);
        cochesHashMap.replace(ID, c);
        saveChangesOnFile(cochesHashMap);
    }

    private void saveChangesOnFile(HashMap<Integer, Coche> cochesHashMap) {
        try {
            FileWriter fwe = new FileWriter(S_Archivo, false);
            BufferedWriter writere = new BufferedWriter(fwe);
            for (Coche c : cochesHashMap.values()) {
                writere.append(String.valueOf(c.getID()));
                writere.append("\n" + c.getMarca());
                writere.append("\n" + c.getModelo());
                writere.append("\n" + String.valueOf(c.getCavallaje()));
                writere.append("\n" + c.getColor());
                writere.append("\n#\n");
            }
            writere.close();
            setLastIDToConfig();
        } catch (IOException e) {
            System.err.println("Jaime, que no me lo rompes esto");
        }
    }

    public boolean isFileCorrupted() {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        Boolean isCorrupted = false;

        try {
            // crea los objetos necesarios
            archivo = new File(S_Archivo);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            int hastags = 0;
            int lines = 0;
            // Lee el archivo
            String linea;
            while ((linea = br.readLine()) != null) {
                lines++;
                if (linea.equals("#"))
                    hastags++;
            }
            if ((hastags*6) != lines)
                isCorrupted = true;
        } catch (Exception e) {
            System.err.println("Archivo inexistente o corrupto");
        }
        return isCorrupted;
    }

    public int getnextIDFromConfig(){
        Properties propiedades = new Properties();
        readConfig(propiedades);
        this.lastID = Integer.parseInt(propiedades.getProperty("lastID"));
        return this.lastID + 1;
    }


    private void setLastIDToConfig(){
        Properties propiedades = new Properties();
        readConfig(propiedades);
        propiedades.replace("lastID", String.valueOf(getnextIDFromConfig()));
        try {
            FileWriter fwconfig = new FileWriter("config.ini", false);
            Writer writerconfig = new BufferedWriter(fwconfig);
            propiedades.store(writerconfig, "");
        } catch (IOException e) {
            System.err.println("Escoja otro archivo, este no existe");
        }
    }

    private void readConfig(Properties propiedades) {
        try {

            archivo = new File("config.ini");
            if (archivo.exists()) {
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                propiedades.load(fr);
            } else
                System.err.println("Fichero no encontrado");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        MagementInterface emisor = new FileManagement("coches.txt", true);
        System.out.println(((FileManagement) emisor).isFileCorrupted());
        ArrayList<Coche> cochesasd = new ArrayList<Coche>();
        emisor.delete(23);
    }
}