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


public class HibernateManager implements MagementInterface{
    Session session;

    public HibernateManager() {

        HibernateUtil util = new HibernateUtil();

        session = util.getSessionFactory().openSession();

    }

    public static void main(String[] args) {
        HibernateManager hbm= new HibernateManager();
        Vista vista = new Vista();
        Brand masserati = new Brand(3,"Masserati", "Italy", 1914);
        Coche granTurismo = new Coche(3, masserati, "GranTurismo", 460, "Plata");
        ArrayList<Coche> coches = new ArrayList<>();
        coches.add(granTurismo);
        hbm.write(coches);
    }

    @Override
    public void write(ArrayList<Coche> coches) {
        session.beginTransaction();
        for (Coche coch : coches) {
            session.save(coch.getMarca());
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

    }

    @Override
    public void update(Coche c, int ID) {

    }

    @Override
    public HashMap<String, Brand> readBrands() {
        return null;
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
}
