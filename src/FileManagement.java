import java.io.*;
import java.util.ArrayList;


public class FileManagement implements MagementInterface {

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

    @Override
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

    @Override
    public ArrayList<Coche> read() {
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

    @Override
    public void delete(int ID) {

    }

    @Override
    public void update(Coche c, int ID) {

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

