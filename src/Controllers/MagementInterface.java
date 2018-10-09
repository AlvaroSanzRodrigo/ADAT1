package Controllers;

import Models.Brand;
import Models.Coche;

import java.util.ArrayList;
import java.util.HashMap;

public interface MagementInterface {

    void write(ArrayList<Coche> coches);

    ArrayList<Coche> read();

    void delete(int ID);

    void  update(Coche c, int ID);

    HashMap<String, Brand> readBrands();

    void addBrand(String brandName, String brandCountry, int yearOfFundation);
}
