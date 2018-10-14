package Controllers;

import Models.Brand;
import Models.Coche;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
                writer.append("\n" + c.getMarca().getBrandName());
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
        HashMap<String, Brand> brandsHashMap;
        Coche c;


        try {
            // crea los objetos necesarios
            archivo = new File(S_Archivo);
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            brandsHashMap = this.readBrands();

            // Lee el archivo de coches
            String linea;
            while ((linea = br.readLine()) != null) {
                c = new Coche();
                while (!linea.equals("#")) {
                    c.setID(Integer.parseInt(linea));
                    linea = br.readLine();
                    c.setMarca(brandsHashMap.get(linea));
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

    @Override
    public HashMap<String, Brand> readBrands() {

        File archivoMarcas;
        FileReader frMarcas;
        BufferedReader brMarcas;
        HashMap<String, Brand> brandsHashMap = new HashMap<>();
        Brand brand;
        try {
            archivoMarcas = new File("brands.txt");
            frMarcas = new FileReader(archivoMarcas);
            brMarcas = new BufferedReader(frMarcas);
            // Lee el archivo de marcas
            String lineaMarcas;
            while ((lineaMarcas = brMarcas.readLine()) != null){
                brand = new Brand();
                while (!lineaMarcas.equals("#")){
                    brand.setIdBrand(Integer.parseInt(lineaMarcas));
                    lineaMarcas = brMarcas.readLine();
                    brand.setBrandName(lineaMarcas);
                    lineaMarcas = brMarcas.readLine();
                    brand.setBrandCountry(lineaMarcas);
                    lineaMarcas = brMarcas.readLine();
                    brand.setBrandYearOfFundation(Integer.parseInt(lineaMarcas));
                    lineaMarcas = brMarcas.readLine();
                }
                brandsHashMap.put(brand.getBrandName(), brand);
            }
        } catch (IOException e) {
            System.err.println("Error en el archivo marcas");
        }
        return brandsHashMap;
    }

    @Override
    public void addBrand(String brandName, String brandCountry, int yearOfFundation) {
        try {
            FileWriter fileWriter = new FileWriter("brands.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append(String.valueOf(this.getnextBrandIDFromConfig()));
            bufferedWriter.append("\n").append(brandName);
            bufferedWriter.append("\n").append(brandCountry);
            bufferedWriter.append("\n").append(String.valueOf(yearOfFundation));
            bufferedWriter.append("\n#\n");
            bufferedWriter.close();
            setLastBrandIDToConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteBrand(String brandName) {
        HashMap<String, Brand> brandHashSet= this.readBrands();
        HashMap<String, Coche> cochesHashMap = new HashMap<>();
        for (Coche coche: this.read()) {
            cochesHashMap.put(coche.getMarca().getBrandName(), coche);
        }
        if (brandHashSet.containsKey(brandName)) {
            brandHashSet.remove(brandName);
            cochesHashMap.remove(brandName);
            HashMap<Integer, Coche> cochesHashMapToDelete = new HashMap<>();
            for (Coche coche : cochesHashMap.values()) {
                cochesHashMapToDelete.put(coche.getID(), coche);
            }
            saveChangesOnFile(cochesHashMapToDelete);
            saveChangesOnFileBrand(brandHashSet);
        }else {
            System.out.println("No existe esa marca");
        }
    }

    @Override
    public void updateBrand(String brandName, Brand brand) {
        HashMap<String, Brand> brandHashSet= this.readBrands();
        HashMap<String, Coche> cochesHashMap = new HashMap<>();
        for (Coche coche: this.read()) {
            cochesHashMap.put(coche.getMarca().getBrandName(), coche);
        }
        if (brandHashSet.containsKey(brandName)) {
            brand.setIdBrand(brandHashSet.get(brandName).getIdBrand());
            brandHashSet.replace(brandName, brand);
            HashMap<Integer, Coche> cochesHashMapToUpdate = new HashMap<>();
            for (Coche coche : cochesHashMap.values()) {
                if (coche.getMarca().getBrandName().equals(brandName))
                    coche.setMarca(brand);
                cochesHashMapToUpdate.put(coche.getID(), coche);
            }
            saveChangesOnFile(cochesHashMapToUpdate);
            saveChangesOnFileBrand(brandHashSet);
        }else {
            System.out.println("No existe esa marca");
        }
    }

    private void saveChangesOnFileBrand(HashMap<String, Brand> brandHashMap){
        try {
            FileWriter fileWriter = new FileWriter("brands.txt", false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (Brand brand : brandHashMap.values()) {
                bufferedWriter.append(String.valueOf(brand.getIdBrand()));
                bufferedWriter.append("\n").append(brand.getBrandName());
                bufferedWriter.append("\n").append(brand.getBrandCountry());
                bufferedWriter.append("\n").append(String.valueOf(brand.getBrandYearOfFundation()));
                bufferedWriter.append("\n#\n");
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveChangesOnFile(HashMap<Integer, Coche> cochesHashMap) {
        try {
            FileWriter fwe = new FileWriter(S_Archivo, false);
            BufferedWriter writere = new BufferedWriter(fwe);
            for (Coche c : cochesHashMap.values()) {
                writere.append(String.valueOf(c.getID()));
                writere.append("\n" + c.getMarca().getBrandName());
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

    public int getnextBrandIDFromConfig(){
        Properties propiedades = new Properties();
        readConfig(propiedades);
        return Integer.parseInt(propiedades.getProperty("lastBrandID"))+1;
    }

    public void setLastBrandIDToConfig(){
        Properties propiedades = new Properties();
        readConfig(propiedades);
        propiedades.replace("lastBrandID", String.valueOf(getnextBrandIDFromConfig()));
        saveConfig(propiedades);
    }

    private void saveConfig(Properties propiedades) {
        try {
            FileWriter fwconfig = new FileWriter("config.ini", false);
            Writer writerconfig = new BufferedWriter(fwconfig);
            propiedades.store(writerconfig, "");
        } catch (IOException e) {
            System.err.println("Escoja otro archivo, este no existe");
        }
    }


    public void setLastIDToConfig(){
        Properties propiedades = new Properties();
        readConfig(propiedades);
        propiedades.replace("lastID", String.valueOf(getnextIDFromConfig()));
        saveConfig(propiedades);
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
}