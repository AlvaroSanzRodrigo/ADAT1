import java.util.ArrayList;

public interface MagementInterface {

    void write(ArrayList<Coche> coches);

    ArrayList<Coche> read();

    void delete(int ID);

    void  update(Coche c, int ID);
}
