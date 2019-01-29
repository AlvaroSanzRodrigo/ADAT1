package Controllers;

import Models.Brand;
import Models.Coche;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static com.mongodb.client.model.Filters.eq;

public class MongoController implements MagementInterface {

    MongoClient mongoClient;
    MongoDatabase database;

    public MongoController() {
        mongoClient = new MongoClient();
        database = mongoClient.getDatabase("ADAT_1");

    }

    @Override
    public void write(ArrayList<Coche> coches) {
        for (Coche coche : coches) {
            Document cocheDocument = new Document();
            cocheDocument.append("ID", coche.getID());
            cocheDocument.append("idBrand", coche.getMarca().getIdBrand());
            cocheDocument.append("cavallaje", coche.getCavallaje());
            cocheDocument.append("color", coche.getColor());
            cocheDocument.append("modelo", coche.getModelo());
            database.getCollection("coches").insertOne(cocheDocument);
        }
    }

    @Override
    public ArrayList<Coche> read() {
        ArrayList<Coche> cocheArrayList = new ArrayList<>();
        MongoCollection<Document> collection = database.getCollection("coches");
        for (Document document : collection.find()) {
            Coche coche = new Coche();
            coche.setModelo(document.getString("modelo"));
            coche.setColor(document.getString("color"));
            coche.setCavallaje(Integer.parseInt(document.getString("cavallaje")));
            Brand brand = new Brand();
            Document brandDocument = database.getCollection("marcas").find(eq("idBrand", document.getString("idBrand"))).first();
            brand.setIdBrand(Integer.parseInt(brandDocument.getString("idBrand")));
            brand.setBrandYearOfFundation(Integer.parseInt(brandDocument.getString("brandYearOfFundation")));
            brand.setBrandCountry(brandDocument.getString("brandCountry"));
            brand.setBrandName(brandDocument.getString("brandName"));
            coche.setMarca(brand);
            coche.setID(Integer.parseInt(document.getString("ID")));
            cocheArrayList.add(coche);
        }
        return cocheArrayList;
    }

    @Override
    public void delete(int ID) {
        database.getCollection("coches").deleteOne(eq("ID", String.valueOf(ID)));
    }

    @Override
    public void update(Coche c, int ID) {
        Document cocheDocument = new Document();
        cocheDocument.append("ID", c.getID());
        cocheDocument.append("idBrand", c.getMarca().getIdBrand());
        cocheDocument.append("cavallaje", c.getCavallaje());
        cocheDocument.append("color", c.getColor());
        cocheDocument.append("modelo", c.getModelo());
        database.getCollection("coches").updateOne(eq("ID", String.valueOf(ID)), cocheDocument);
    }

    @Override
    public HashMap<String, Brand> readBrands() {
        HashMap<String, Brand> brandHashMap = new HashMap<>();
        for (Document brandDocument :database.getCollection("marcas").find()) {
            Brand brand = new Brand();
            brand.setIdBrand(Integer.parseInt(brandDocument.getString("idBrand")));
            brand.setBrandYearOfFundation(Integer.parseInt(brandDocument.getString("brandYearOfFundation")));
            brand.setBrandCountry(brandDocument.getString("brandCountry"));
            brand.setBrandName(brandDocument.getString("brandName"));
            brandHashMap.put(brand.getBrandName(), brand);
        }
        return brandHashMap;
    }

    @Override
    public void addBrand(String brandName, String brandCountry, int yearOfFundation) {
        Document brandDocument = new Document();
        Scanner scanner = new Scanner(System.in);
        brandDocument.append("brandName", brandName);
        brandDocument.append("brandCountry", brandCountry);
        brandDocument.append("brandYearOfFundation", yearOfFundation);
        System.out.println("Introduce un ID para la marca");
        brandDocument.append("idBrand", scanner.nextLine());
        database.getCollection("marcas").insertOne(brandDocument);
    }

    @Override
    public void deleteBrand(String brandName) {
        database.getCollection("marcas").deleteOne(eq("brandName", String.valueOf(brandName)));
    }

    @Override
    public void updateBrand(String brandName, Brand brand) {
        Document brandDocument = new Document();
        Scanner scanner = new Scanner(System.in);
        brandDocument.append("brandName", brand.getBrandName());
        brandDocument.append("brandCountry", brand.getBrandCountry());
        brandDocument.append("brandYearOfFundation", brand.getBrandYearOfFundation());
        brandDocument.append("idBrand", brand.getIdBrand());
        database.getCollection("marcas").updateOne(eq("brandName",brandName),brandDocument);
    }

    public static void main(String[] args) {
        MongoController mongoController = new MongoController();
        mongoController.read();
        mongoController.readBrands();
        mongoController.delete(9);
    }
}
