package Controllers;

import Models.Brand;
import Models.Coche;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.HashMap;

public class JSONController implements MagementInterface {

    ApiRequests encargadoPeticiones;
    private String SERVER_PATH, GET_CAR, SET_CAR; // Datos de la conexion

    public JSONController() {

        encargadoPeticiones = new ApiRequests();

        SERVER_PATH = "http://localhost/Sanz/ADAT_1_JSON/";
        GET_CAR = "readCars.php";
        SET_CAR = "addCar.php";

    }

    @Override
    public void write(ArrayList<Coche> coches) {
        for (Coche coch : coches) {

            try {
                JSONObject objCoche = new JSONObject();
                JSONObject objPeticion = new JSONObject();

                objCoche.put("modelo", coch.getModelo());
                objCoche.put("cavallaje", coch.getCavallaje());
                objCoche.put("color", coch.getColor());
                objCoche.put("idBrand", coch.getMarca().getIdBrand());

                // Tenemos el jugador como objeto JSON. Lo a�adimos a una peticion
                // Lo transformamos a string y llamamos al
                // encargado de peticiones para que lo envie al PHP
                objPeticion.put("petition", "add");
                objPeticion.put("coche", objCoche);

                String json = objPeticion.toJSONString();

                System.out.println("Lanzamos peticion JSON para almacenar un jugador");

                String url = SERVER_PATH + SET_CAR;

                System.out.println("La url a la que lanzamos la petici�n es " + url);
                System.out.println("El json que enviamos es: ");
                System.out.println(json);
                //System.exit(-1);

                String response = encargadoPeticiones.postRequest(url, json);

                System.out.println("El json que recibimos es: ");

                System.out.println(response); // Traza para pruebas


                // Parseamos la respuesta y la convertimos en un JSONObject
                JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

                if (respuesta == null) { // Si hay alg�n error de parseo (json
                    // incorrecto porque hay alg�n caracter
                    // raro, etc.) la respuesta ser� null
                    System.out.println("El json recibido no es correcto. Finaliza la ejecuci�n");
                    System.exit(-1);
                } else { // El JSON recibido es correcto

                    // Sera "ok" si todo ha ido bien o "error" si hay alg�n problema
                    String estado = (String) respuesta.get("estado");
                    if (estado.equals("ok")) {
                        System.out.println("Almacenado jugador enviado por JSON Remoto");

                    } else { // Hemos recibido el json pero en el estado se nos
                        // indica que ha habido alg�n error

                        System.out.println("Acceso JSON REMOTO - Error al almacenar los datos");
                        System.out.println("Error: " + (String) respuesta.get("error"));
                        System.out.println("Consulta: " + (String) respuesta.get("query"));

                        System.exit(-1);

                    }
                }
            } catch (Exception e) {
                System.out.println(
                        "Excepcion desconocida. Traza de error comentada en el mtodo 'annadirJugador' de la clase JSON REMOTO");
                // e.printStackTrace();
                System.out.println("Fin ejecucion");
                System.exit(-1);
            }
        }
    }

    @Override
    public ArrayList<Coche> read() {
        ArrayList<Coche> cocheArrayList = new ArrayList<>();

        try {

            System.out.println("---------- Leemos datos de JSON --------------------");


            System.out.println("Lanzamos peticion JSON para coches");
            String url = SERVER_PATH + GET_CAR; // Sacadas de configuracion

            System.out.println("La url a la que lanzamos la petici�n es " +
                    url); // Traza para pruebas

            String response = encargadoPeticiones.getRequest(url);

            // System.out.println(response); // Traza para pruebas

            // Parseamos la respuesta y la convertimos en un JSONObject
            JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

            if (respuesta == null) { // Si hay alg�n error de parseo (json
                // incorrecto porque hay alg�n caracter
                // raro, etc.) la respuesta ser� null
                System.out.println("El json recibido no es correcto");
            } else { // El JSON recibido es correcto
                // Sera "ok" si todo ha ido bien o "error" si hay alg�n problema
                String estado = (String) respuesta.get("state");

                if (estado.equals("ok")) {
                    JSONArray array = (JSONArray) respuesta.get("coches");

                    if (array.size() > 0) {

                        // Declaramos variables
                        Coche newCoche;
                        int id, cavallaje;
                        Brand marca;
                        String modelo, color;

                        for (int i = 0; i < array.size(); i++) {
                            JSONObject row = (JSONObject) array.get(i);

                            modelo = row.get("modelo").toString();
                            cavallaje = Integer.parseInt(row.get("cavallaje").toString());
                            id = Integer.parseInt(row.get("ID").toString());
                            color = row.get("color").toString();
                            //marca = this.readBrands().get(row.get("idBrand").toString());
                            marca = new Brand();

                            newCoche = new Coche(id, marca, modelo, cavallaje, color);

                            cocheArrayList.add(newCoche);
                        }

                        System.out.println("Acceso JSON Remoto - Leidos datos correctamente y generado ArrayList");
                        System.out.println();

                    } else { // El array de jugadores est� vac�o
                        System.out.println("Acceso JSON Remoto - No hay datos que tratar");
                        System.out.println();
                    }

                } else { // Hemos recibido el json pero en el estado se nos
                    // indica que ha habido alg�n error

                    System.out.println("Ha ocurrido un error en la busqueda de datos");
                    System.out.println("Error: " + (String) respuesta.get("error"));
                    System.out.println("Consulta: " + (String) respuesta.get("query"));

                    System.exit(-1);

                }
            }

        } catch (Exception e) {
            System.out.println("Ha ocurrido un error en la busqueda de datos");

            e.printStackTrace();

            System.exit(-1);
        }
        return cocheArrayList;
    }

    @Override
    public void delete(int ID) {

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

    public static void main(String[] args) {
        ArrayList<Coche> cocheArrayList = new ArrayList<>();
        JSONController jsonController = new JSONController();
        Brand ds = new Brand(3, "DS", "France", 2009);
        Coche coche = new Coche(1, ds, "Pruebita", 2, "Rojo");

        cocheArrayList.add(coche);

        jsonController.write(cocheArrayList);
        for (Coche c : jsonController.read()) {
            System.out.println(c.toString());
        }

    }
}
