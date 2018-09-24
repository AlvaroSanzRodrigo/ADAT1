import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConnectionManagement connectionManagement = new ConnectionManagement();
        FileManagement fileManagement = new FileManagement();
        String fileName = "";
        String ne = "";
        Coche b;
        Coche c;

        Scanner in = new Scanner(System.in);


        Boolean repetirMenu = true;

        while (repetirMenu) {
            System.out.println(".:MENU:.");
            System.out.println("1. De consola a archivo");
            System.out.println("2. De archivo a consola");
            System.out.println("3. De BD a consola");
            System.out.println("4. De consola a BD");
            System.out.println("5. De archivo a DB");
            System.out.println("6. De DB a archivo");
            System.out.println("7. Eliminar de la BD");
            System.out.println("8. Modificar de la BD");
            System.out.println("9. Salirwertyut65");
            try {
                switch (Integer.parseInt(in.nextLine())) {

                    case 1:
                        System.out.println("¿Fichero nuevo o existente? N/E");
                        ne = in.nextLine();
                        if (ne.toLowerCase().equals("e")) {
                            System.out.println("En que fichero:");
                            fileName = in.nextLine();
                            System.out.println("¿Conservar datos anteriores? Y/N");
                            String conserveData = in.nextLine();
                            Boolean b_ConserveData = false;
                            if (conserveData.toLowerCase().equals("y"))
                                b_ConserveData = true;
                            c = new Coche();
                            System.out.println("Introduzca datos");
                            System.out.println("Marca:");
                            c.setMarca(in.nextLine());
                            System.out.println("Modelo:");
                            c.setModelo(in.nextLine());
                            System.out.println("Caballaje");
                            c.setCavallaje(Integer.parseInt(in.nextLine()));
                            System.out.println("Color:");
                            c.setColor(in.nextLine());

                            fileManagement.writeToFileFromConsole(fileName, c, b_ConserveData);
                        } else if (ne.toLowerCase().equals("n")) {
                            System.out.println("Nombre fichero:");
                            fileName = in.nextLine();
                            fileManagement.createNewFile(fileName);

                            c = new Coche();
                            System.out.println("Introduzca datos");
                            System.out.println("Marca:");
                            c.setMarca(in.nextLine());
                            System.out.println("Modelo:");
                            c.setModelo(in.nextLine());
                            System.out.println("Caballaje");
                            c.setCavallaje(Integer.parseInt(in.nextLine()));
                            System.out.println("Color:");
                            c.setColor(in.nextLine());

                            fileManagement.writeToFileFromConsole(fileName, c, true);

                        } else {
                            System.err.println("No ha escrito ni N ni E");
                        }

                        break;
                    case 2:
                        System.out.println("¿De que archivo?");
                        try {
                            fileName = in.nextLine();
                            fileManagement.readFromFileToWriteInConsole(fileName);
                        } catch (Exception e) {
                            System.err.println("Por favor Jaime, no intentes romper mi programa");
                            System.err.println("Archivo no encontrado.");
                        }
                        break;
                    case 3:
                        connectionManagement.readFromDBToPrintOnConsole();
                        break;
                    case 4:
                        b = new Coche();
                        System.out.println("Introduzca datos");
                        System.out.println("Marca:");
                        b.setMarca(in.nextLine());
                        System.out.println("Modelo:");
                        b.setModelo(in.nextLine());
                        System.out.println("Caballaje");
                        b.setCavallaje(Integer.parseInt(in.nextLine()));
                        System.out.println("Color:");
                        b.setColor(in.nextLine());
                        connectionManagement.writeOnDBFromConsole(b);
                        break;
                    case 5:
                        System.out.println("¿De que archivo?");
                        fileName = in.nextLine();
                        if (!fileManagement.isFileCorrupted(fileName)) {
                            ArrayList<Coche> cars = fileManagement.getAllCarsInFile(fileName);
                            for (Coche car : cars) {
                                System.out.println(car.toString());
                                connectionManagement.writeOnDBFromConsole(car);
                            }
                        }else {
                            System.err.println("Archivo corrupto o inexistente");
                        }
                        break;
                    case 6:
                        System.out.println("¿Fichero nuevo o existente? N/E");
                        ne = in.nextLine();
                        ArrayList<Coche> carsForTheFile = connectionManagement.getAllCarsInDB();
                        if (ne.toLowerCase().equals("e")) {
                            System.out.println("¿En que archivo? (Si no existe se creara uno nuevo)");
                            fileName = in.nextLine();
                            System.out.println("¿Conservar datos anteriores? Y/N");
                            String conserveData = in.nextLine();
                            Boolean b_ConserveData = false;
                            fileManagement.writeToFileFromDB(fileName, carsForTheFile, b_ConserveData);
                        } else if (ne.toLowerCase().equals("n")) {
                            System.out.println("Nombre fichero:");
                            fileName = in.nextLine();
                            fileManagement.createNewFile(fileName);
                            fileManagement.writeToFileFromDB(fileName, carsForTheFile, true);
                        } else {
                            System.err.println("No ha escrito ni N ni E");
                        }
                        break;
                    case 7:
                        System.out.println("A continuacion se imprimira la lista de coches, despúes introduzca el ID para borrarlo.");
                        System.out.println("Introduzca cualquier tecla para continuar");
                        in.nextLine();
                        connectionManagement.readFromDBToPrintOnConsole();
                        System.out.println("Introduzca ID:");
                        try{
                            connectionManagement.removeACarFromDB(Integer.parseInt(in.nextLine()));
                        } catch (Exception e){
                            System.err.println("Fallo al introducir ID");
                        }
                        break;
                    case 8:
                        c = new Coche();
                        System.out.println("A continuacion se imprimira la lista de coches, despúes introduzca el ID para borrarlo.");
                        System.out.println("Introduzca cualquier tecla para continuar");
                        in.nextLine();
                        connectionManagement.readFromDBToPrintOnConsole();
                        System.out.println("Introduzca ID:");
                        int carID = Integer.parseInt(in.nextLine());
                        System.out.println("Introduzca datos");
                        System.out.println("Marca:");
                        c.setMarca(in.nextLine());
                        System.out.println("Modelo:");
                        c.setModelo(in.nextLine());
                        System.out.println("Caballaje");
                        c.setCavallaje(Integer.parseInt(in.nextLine()));
                        System.out.println("Color:");
                        c.setColor(in.nextLine());
                        try{
                        connectionManagement.updateACarFromDB(c, carID);
                        }catch (Exception e){
                            System.err.println("Fallo al introducir ID");
                        }
                        break;
                    case 9:
                        repetirMenu = false;
                        break;

                }
            } catch (Exception e) {
                System.err.println("Por favor Jaime, no intentes romper mi programa");
            }

        }
    }
}
