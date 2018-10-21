package Controllers;

import Models.Brand;
import Models.Coche;
import Views.Vista;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class HibernateManager implements MagementInterface {
    Session session;

    public HibernateManager() {

        HibernateUtil util = new HibernateUtil();

        session = util.getSessionFactory().openSession();

    }

    public static void main(String[] args) {
        HibernateManager hbm = new HibernateManager();
        Vista vista = new Vista();
        Brand ds = new Brand(3, "DS", "France", 2009);
        hbm.deleteBrand("DS");
    }

    @Override
    public void write(ArrayList<Coche> coches) {
        session.beginTransaction();
        for (Coche coch : coches) {
            session.saveOrUpdate(coch.getMarca());
            session.save(coch);
        }
        session.getTransaction().commit();
    }


    @Override
    public ArrayList<Coche> read() {
        session.beginTransaction();
        Query query = session.createQuery("select c from Coche c");
        List<Coche> results = query.list();
        session.getTransaction();
        ArrayList<Coche> returnable = new ArrayList<>(results);
        return returnable;
    }

    @Override
    public void delete(int ID) {
        session.beginTransaction();
        ArrayList<Coche> cocheArrayList = new ArrayList<>(this.read());
        HashMap<Integer, Coche> cocheHashMap = new HashMap<>();
        for (Coche coche : cocheArrayList) {
            cocheHashMap.put(coche.getID(), coche);
        }
        session.delete(cocheHashMap.get(ID));
        session.getTransaction().commit();
    }

    @Override
    public void update(Coche c, int ID) {
        session.beginTransaction();
        session.update(c);
        session.getTransaction().commit();
    }

    @Override
    public HashMap<String, Brand> readBrands() {
        session.beginTransaction();
        Query query = session.createQuery("select b from Brand b");
        List<Brand> results = query.list();
        session.getTransaction();
        HashMap<String, Brand> brandHashMap = new HashMap<>();
        for (Brand brand : results) {
            brandHashMap.put(brand.getBrandName(), brand);
        }
        return brandHashMap;
    }

    @Override
    public void addBrand(String brandName, String brandCountry, int yearOfFundation) {
        Brand b = new Brand();
        b.setBrandName(brandName);
        b.setBrandCountry(brandCountry);
        b.setBrandYearOfFundation(yearOfFundation);
        session.beginTransaction();
        session.save(b);
        session.getTransaction().commit();
    }

    @Override
    public void deleteBrand(String brandName) {
        session.beginTransaction();
        HashMap<String, Brand> brandHashMap = this.readBrands();
        session.delete(brandHashMap.get(brandName));
        session.getTransaction().commit();
    }

    @Override
    public void updateBrand(String brandName, Brand brand) {
        session.beginTransaction();
        session.saveOrUpdate(brand);
        session.getTransaction().commit();
    }
}
