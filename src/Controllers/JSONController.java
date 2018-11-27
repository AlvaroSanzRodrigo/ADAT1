package Controllers;

import Models.Brand;
import Models.Coche;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public class JSONController implements MagementInterface {

    ApiRequests encargadoPeticiones;
    private String SERVER_PATH, GET_CAR, SET_CAR, GET_BRAND, DELETE_CAR, SET_BRAND; // Datos de la conexion
    private String DELETE_BRAND;
    private String UPDATE_CAR;
    private String UPDATE_BRAND;
    private File archivo = null;
    private FileReader fr = null;
    private BufferedReader br = null;

    public JSONController() {

        encargadoPeticiones = new ApiRequests();
        Properties propiedades = new Properties();
        try {

            archivo = new File("jsonconfig.ini");
            if (archivo.exists()) {
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                propiedades.load(fr);
                // obtenemos las propiedades y las imprimimos
                SERVER_PATH = propiedades.getProperty("SERVER_PATH");
                GET_CAR = propiedades.getProperty("GET_CAR");
                SET_CAR = propiedades.getProperty("SET_CAR");
                GET_BRAND = propiedades.getProperty("GET_BRAND");
                DELETE_CAR = propiedades.getProperty("DELETE_CAR");
                GET_BRAND = propiedades.getProperty("GET_BRAND");
                SET_BRAND = propiedades.getProperty("SET_BRAND");
                UPDATE_CAR = propiedades.getProperty("UPDATE_CAR");
                DELETE_BRAND = propiedades.getProperty("DELETE_BRAND");
                UPDATE_BRAND = propiedades.getProperty("UPDATE_BRAND");


            } else
                System.err.println("Fichero no encontrado");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

                modificationResponse(respuesta);
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
                            marca = this.readBrandsById().get(Integer.parseInt(row.get("idBrand").toString()));
                            //marca = new Brand();

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
        try {
            JSONObject objCoche = new JSONObject();
            JSONObject objPeticion = new JSONObject();

            objCoche.put("idCoche", ID);

            // Lo transformamos a string y llamamos al
            // encargado de peticiones para que lo envie al PHP
            objPeticion.put("petition", "delete");
            objPeticion.put("idCoche", objCoche);

            String json = objPeticion.toJSONString();

            System.out.println("Lanzamos peticion JSON para almacenar un jugador");

            String url = SERVER_PATH + DELETE_CAR;

            System.out.println("La url a la que lanzamos la petici�n es " + url);
            System.out.println("El json que enviamos es: ");
            System.out.println(json);
            //System.exit(-1);

            String response = encargadoPeticiones.postRequest(url, json);

            System.out.println("El json que recibimos es: ");

            System.out.println(response); // Traza para pruebas


            // Parseamos la respuesta y la convertimos en un JSONObject
            JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

            modificationResponse(respuesta);

        } catch (Exception e) {
            System.out.println(
                    "Excepcion desconocida. Traza de error comentada en el mtodo 'annadirJugador' de la clase JSON REMOTO");
            // e.printStackTrace();
            System.out.println("Fin ejecucion");
            System.exit(-1);
        }
    }

    private void modificationResponse(JSONObject respuesta) {
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
    }

    @Override
    public void update(Coche c, int ID) {
        try {
            JSONObject objCoche = new JSONObject();
            JSONObject objPeticion = new JSONObject();

            objCoche.put("idCoche", ID);
            objCoche.put("modelo", c.getModelo());
            objCoche.put("cavallaje", c.getCavallaje());
            objCoche.put("color", c.getColor());
            objCoche.put("idBrand", c.getMarca().getIdBrand());

            // Tenemos el jugador como objeto JSON. Lo a�adimos a una peticion
            // Lo transformamos a string y llamamos al
            // encargado de peticiones para que lo envie al PHP
            objPeticion.put("petition", "update");
            objPeticion.put("coche", objCoche);

            String json = objPeticion.toJSONString();

            System.out.println("Lanzamos peticion JSON para updatear un coche");

            String url = SERVER_PATH + UPDATE_CAR;

            System.out.println("La url a la que lanzamos la petici�n es " + url);
            System.out.println("El json que enviamos es: ");
            System.out.println(json);
            //System.exit(-1);

            String response = encargadoPeticiones.postRequest(url, json);

            System.out.println("El json que recibimos es: ");

            System.out.println(response); // Traza para pruebas


            // Parseamos la respuesta y la convertimos en un JSONObject
            JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

            modificationResponse(respuesta);
        } catch (Exception e) {
            System.out.println(
                    "Excepcion desconocida. Traza de error comentada en el mtodo 'updateCar()' de la clase JSON REMOTO");
            // e.printStackTrace();
            System.out.println("Fin ejecucion");
            System.exit(-1);
        }
    }

    @Override
    public HashMap<String, Brand> readBrands() {
        HashMap<String, Brand> marcasHasMap = new HashMap<>();

        try {

            System.out.println("---------- Leemos datos de JSON --------------------");


            System.out.println("Lanzamos peticion JSON para coches");
            String url = SERVER_PATH + GET_BRAND; // Sacadas de configuracion

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
                    JSONArray array = (JSONArray) respuesta.get("marcas");

                    if (array.size() > 0) {

                        // Declaramos variables
                        Brand marca;

                        for (int i = 0; i < array.size(); i++) {
                            JSONObject row = (JSONObject) array.get(i);
                            marca = new Brand(Integer.parseInt(row.get("idBrand").toString()), row.get("brandName").toString(), row.get("brandCountry").toString(), Integer.parseInt(row.get("brandYearOfFundation").toString()));
                            marcasHasMap.put(row.get("brandName").toString(), marca);
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
        return marcasHasMap;
    }

    public HashMap<Integer, Brand> readBrandsById() {
        HashMap<Integer, Brand> marcasHasMap = new HashMap<>();

        try {

            System.out.println("---------- Leemos datos de JSON --------------------");


            System.out.println("Lanzamos peticion JSON para coches");
            String url = SERVER_PATH + GET_BRAND; // Sacadas de configuracion

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
                    JSONArray array = (JSONArray) respuesta.get("marcas");

                    if (array.size() > 0) {
                        Brand marca;

                        for (int i = 0; i < array.size(); i++) {
                            JSONObject row = (JSONObject) array.get(i);
                            marca = new Brand(Integer.parseInt(row.get("idBrand").toString()), row.get("brandName").toString(), row.get("brandCountry").toString(), Integer.parseInt(row.get("brandYearOfFundation").toString()));
                            marcasHasMap.put(Integer.parseInt(row.get("idBrand").toString()), marca);
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
        return marcasHasMap;
    }

    @Override
    public void addBrand(String brandName, String brandCountry, int yearOfFundation) {
        try {
            JSONObject objCoche = new JSONObject();
            JSONObject objPeticion = new JSONObject();

            objCoche.put("brandName", brandName);
            objCoche.put("brandCountry", brandCountry);
            objCoche.put("brandYearOfFundation", yearOfFundation);

            // Tenemos el jugador como objeto JSON. Lo a�adimos a una peticion
            // Lo transformamos a string y llamamos al
            // encargado de peticiones para que lo envie al PHP
            objPeticion.put("petition", "add");
            objPeticion.put("brand", objCoche);

            String json = objPeticion.toJSONString();

            System.out.println("Lanzamos peticion JSON para almacenar un jugador");

            String url = SERVER_PATH + SET_BRAND;

            System.out.println("La url a la que lanzamos la petici�n es " + url);
            System.out.println("El json que enviamos es: ");
            System.out.println(json);
            //System.exit(-1);

            String response = encargadoPeticiones.postRequest(url, json);

            System.out.println("El json que recibimos es: ");

            System.out.println(response); // Traza para pruebas


            // Parseamos la respuesta y la convertimos en un JSONObject
            JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

            modificationResponse(respuesta);
        } catch (Exception e) {
            System.out.println(
                    "Excepcion desconocida. Traza de error comentada en el mtodo 'annadirBrand' de la clase JSON REMOTO");
            // e.printStackTrace();
            System.out.println("Fin ejecucion");
            System.exit(-1);
        }

    }

    @Override
    public void deleteBrand(String brandName) {
        try {
            JSONObject objCoche = new JSONObject();
            JSONObject objPeticion = new JSONObject();

            objCoche.put("idBrand", brandName);

            // Lo transformamos a string y llamamos al
            // encargado de peticiones para que lo envie al PHP
            objPeticion.put("petition", "delete");
            objPeticion.put("idBrand", objCoche);

            String json = objPeticion.toJSONString();

            System.out.println("Lanzamos peticion JSON para eliminar una marca");

            String url = SERVER_PATH + DELETE_BRAND;

            System.out.println("La url a la que lanzamos la petici�n es " + url);
            System.out.println("El json que enviamos es: ");
            System.out.println(json);
            //System.exit(-1);

            String response = encargadoPeticiones.postRequest(url, json);

            System.out.println("El json que recibimos es: ");

            System.out.println(response); // Traza para pruebas


            // Parseamos la respuesta y la convertimos en un JSONObject
            JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

            modificationResponse(respuesta);

        } catch (Exception e) {
            System.out.println(
                    "Excepcion desconocida. Traza de error comentada en el mtodo 'annadirJugador' de la clase JSON REMOTO");
            // e.printStackTrace();
            System.out.println("Fin ejecucion");
            System.exit(-1);
        }
    }

    @Override
    public void updateBrand(String brandName, Brand brand) {
        try {
            JSONObject objCoche = new JSONObject();
            JSONObject objPeticion = new JSONObject();
            objCoche.put("brandNameToUpdate", brandName);
            objCoche.put("brandName", brand.getBrandName());
            objCoche.put("brandCountry", brand.getBrandCountry());
            objCoche.put("BrandYearOfFundation", brand.getBrandYearOfFundation());

            // Tenemos el jugador como objeto JSON. Lo a�adimos a una peticion
            // Lo transformamos a string y llamamos al
            // encargado de peticiones para que lo envie al PHP
            objPeticion.put("petition", "update");
            objPeticion.put("brand", objCoche);

            String json = objPeticion.toJSONString();

            System.out.println("Lanzamos peticion JSON para updatear un coche");

            String url = SERVER_PATH + UPDATE_BRAND;

            System.out.println("La url a la que lanzamos la petici�n es " + url);
            System.out.println("El json que enviamos es: ");
            System.out.println(json);
            //System.exit(-1);

            String response = encargadoPeticiones.postRequest(url, json);

            System.out.println("El json que recibimos es: ");

            System.out.println(response); // Traza para pruebas


            // Parseamos la respuesta y la convertimos en un JSONObject
            JSONObject respuesta = (JSONObject) JSONValue.parse(response.toString());

            modificationResponse(respuesta);
        } catch (Exception e) {
            System.out.println(
                    "Excepcion desconocida. Traza de error comentada en el mtodo 'updateCar()' de la clase JSON REMOTO");
            // e.printStackTrace();
            System.out.println("Fin ejecucion");
            System.exit(-1);
        }
    }

    public static void main(String[] args) {
        JSONController jsonController = new JSONController();
        ArrayList<Coche> cocheArrayList = jsonController.read();
        Brand ds = new Brand(3, "DS", "France", 2009);
        jsonController.updateBrand("Masserati", ds);

//        cocheArrayList.add(coche);
        //  HashMap<String, Brand> readmarcas;
        //readmarcas = jsonController.readBrands();
        //for (Brand brand : readmarcas.values()) {
        //    System.out.println(brand.toString());
        //}

        for (Coche coche : cocheArrayList) {
            System.out.println(coche.toString());
        }

        //jsonController.delete(5);
        //jsonController.deleteBrand("Issss");
    }
}