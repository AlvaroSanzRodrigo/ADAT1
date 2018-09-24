import java.io.*;
import java.util.ArrayList;


public class FileManagement{

    private FileWriter fw;
    private BufferedWriter writer;
    private String S_Archivo = "";

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

    public void write(ArrayList<Coche> coches) {

        try {

            for (Coche c : coches) {
                writer.append(c.getMarca());
                writer.append("\n" + c.getModelo());
                writer.append("\n" + String.valueOf(c.getCavallaje()));
                writer.append("\n" + c.getColor());
                writer.append("\n#\n");

            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Jaime, que no me lo rompes esto");
        }


    }

    public void readFromFileToWriteInConsole(String S_Archivo) {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            // crea los objetos necesarios
            archivo = new File(S_Archivo);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lee el archivo
            String linea;
            while ((linea = br.readLine()) != null)
                if (linea.equals("#"))
                    System.out.print("\n Siguiente coche \n");
                else
                    System.out.println(linea);
        } catch (Exception e) {
            System.err.println("Archivo no encontrado");
        } finally {
            // cierra el archivo
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                System.err.print("Fallo en el archivo, seleccione otro.");
            }
        }
    }

    public ArrayList<Coche> getAllCars() {
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
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
                System.out.println(carList.size() + c.toString());
                System.out.print("\n Siguiente coche \n");
            }


        } catch (Exception e) {
            System.err.println("Archivo no encontrado");
        }
        return carList;
    }

    public boolean isFileCorrupted(String S_Archivo) {
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
            if ((hastags*5) != lines)
                isCorrupted = true;
        } catch (Exception e) {
            System.err.println("Archivo inexistente o corrupto");
        }
        return isCorrupted;
    }
}

