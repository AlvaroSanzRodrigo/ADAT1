import java.util.ArrayList;

public class Controller {

    public ArrayList print(MagementInterface emisor){
        return emisor.read();
    }

    public void put(MagementInterface emisor,MagementInterface receptor){
        receptor.write(emisor.read());
    }

    public void update(MagementInterface emisor, Coche c, int ID){
        emisor.update(c,ID);
    }

    public void delete(MagementInterface emisor, int ID){
        emisor.delete(ID);
    }

}
