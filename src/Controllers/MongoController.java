package Controllers;

import Models.Brand;
import Models.Coche;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;

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

    }

    @Override
    public void deleteBrand(String brandName) {

    }

    @Override
    public void updateBrand(String brandName, Brand brand) {

    }

    public static void main(String[] args) {
        MongoController mongoController = new MongoController();
        mongoController.read();
        mongoController.readBrands();
        mongoController.delete(9);

    }
}
