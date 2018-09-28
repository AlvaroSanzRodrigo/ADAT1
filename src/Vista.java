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
            System.out.println("1. Base de datos");
            System.out.println("1. Fichero");
            try {
                int option = Integer.parseInt(in.nextLine());
                switch (option) {
                    case 1:
                        Boolean repeatCase1 = true;
                        while (repeatCase1) {
                            try {
                                System.out.println("¿Sobre que archivo?");
                                emisor = new FileManagement(in.nextLine(), true);
                                System.out.println(".:Menu BBDD:.");
                                System.out.println("1. Imprimir");
                                System.out.println("2. Pasar datos a otro almacenamiento");
                                System.out.println("3. Eliminar dato");
                                System.out.println("4. Actualizar dato");
                                System.out.println("5. Salir");
                                int optionCase1 = Integer.parseInt(in.nextLine());
                                switch (optionCase1) {
                                    case 1:
                                        yomismo.print(controller.print(emisor));
                                        break;
                                    case 2:
                                        System.out.println("¿Sobre que almacenamiento?");

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
}
