import java.util.ArrayList;
import java.util.Scanner;

public class Vista {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Boolean repeat = true;
        Controller controller = new Controller();
        MagementInterface emisor;
        MagementInterface receptor;
        Vista yomismo = new Vista();
        while (repeat) {
            System.out.println(".:MENU:.");
            System.out.println("¿De donde quiere obtener los datos?");
            System.out.println("1. Fichero");
            System.out.println("2. BBDD");
            System.out.println("3. Crear archivo nuevo");
            try {
                int option = Integer.parseInt(in.nextLine());
                switch (option) {
                    case 1:
                        Boolean repeatCase1 = true;
                        System.out.println("¿Sobre que fichero?");
                        emisor = new FileManagement(in.nextLine(), true);
                        while (repeatCase1) {
                            System.out.println(".:Menu:.");
                            System.out.println("1. Imprimir");
                            System.out.println("2. Pasar datos a otro almacenamiento");
                            System.out.println("3. Eliminar dato");
                            System.out.println("4. Añadir dato");
                            System.out.println("5. Actualizar dato");
                            System.out.println("5. Salir");
                            try {
                                int optionCase1 = Integer.parseInt(in.nextLine());
                                switch (optionCase1) {
                                    case 1:
                                        yomismo.print(controller.print(emisor));
                                        break;
                                    case 2:
                                        System.out.println("¿Sobre que almacenamiento?");
                                        System.out.println("1. A BBDD");
                                        int optionFilesToOther = 0;
                                        try {
                                            optionFilesToOther = Integer.parseInt(in.nextLine());
                                        } catch (Exception e) {
                                            System.err.println("Porfavor introduce el numero de opción");
                                        }

                                        try {
                                            switch (optionFilesToOther) {
                                                case 1:
                                                    receptor = new ConnectionManagement();
                                                    controller.put(emisor, receptor);
                                            }
                                        } catch (Exception e) {
                                            System.err.println("Error al introducir opción");
                                        }
                                        break;
                                    case 3:
                                        try {
                                            yomismo.print(controller.print(emisor));
                                            System.out.println("Introduce ID del dato a eleminar");
                                            controller.delete(emisor, Integer.parseInt(in.nextLine()));
                                        } catch (Exception e) {
                                            System.err.println("Por favor introduzca ID válido ");
                                        }
                                        break;
                                    case 4:
                                        try {
                                            System.out.println("Cuantos coches desea añadir");
                                            controller.add(emisor, yomismo.getCarsFromKB(Integer.parseInt(in.nextLine()), in, (FileManagement) emisor));
                                        }catch (Exception e){
                                            System.err.println("Error");
                                        }
                                        break;
                                    case 5:
                                        yomismo.print(controller.print(emisor));
                                        System.out.println("Introduce ID del dato a actualizar");
                                        try {
                                            int n = Integer.parseInt(in.nextLine());
                                            controller.update(emisor, yomismo.getCarFromKB(in), n);

                                        } catch (Exception e){
                                            System.err.println("Fallo en la entrada");
                                        }
                                }
                            } catch (Exception e) {
                                System.err.println("Porfavor introduce un numero");
                            }
                        }
                        break;
                    case 2:
                        break;
                }
            } catch (Exception e) {
                System.err.println("Porfavor introduce un numero");
            }

        }
    }

    public void print(ArrayList<Coche> coches) {
        for (Coche coch : coches) {
            System.out.println(coch.getID());
            System.out.println(coch.getMarca());
            System.out.println(coch.getModelo());
            System.out.println(coch.getCavallaje() + "CV");
            System.out.println(coch.getColor());

            System.out.println("####################\n");
        }
    }

    public ArrayList<Coche> getCarsFromKB(int n, Scanner in, FileManagement emisor) {
        ArrayList<Coche> carsArrayList = new ArrayList<>();
        Coche coche;
        for (int i = 0; i < n; i++) {
            coche = new Coche();
            coche.setID(emisor.getnextIDFromConfig());
            System.out.println("Introduzca Marca");
            coche.setMarca(in.nextLine());
            System.out.println("Introduzca Modelo");
            coche.setModelo(in.nextLine());
            System.out.println("Introduzca Cavallaje");
            coche.setCavallaje(Integer.parseInt(in.nextLine()));
            System.out.println("Introduzca Color");
            coche.setColor(in.nextLine());
            carsArrayList.add(coche);
        }
        return carsArrayList;
    }

    public Coche getCarFromKB(Scanner in) {
        ArrayList<Coche> carsArrayList = new ArrayList<>();
        Coche coche;
        coche = new Coche();
        System.out.println("Introduzca Marca");
        coche.setMarca(in.nextLine());
        System.out.println("Introduzca Modelo");
        coche.setModelo(in.nextLine());
        System.out.println("Introduzca Cavallaje");
        coche.setCavallaje(Integer.parseInt(in.nextLine()));
        System.out.println("Introduzca Color");
        coche.setColor(in.nextLine());
        carsArrayList.add(coche);
        return coche;
    }
}
