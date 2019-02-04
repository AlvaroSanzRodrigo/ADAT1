package Views;

import Controllers.*;
import Models.Brand;
import Models.Coche;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Vista {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Boolean repeat = true;
        Controller controller = new Controller();
        MagementInterface emisor = null;
        MagementInterface receptor = null;
        Vista yomismo = new Vista();
        while (repeat) {
            System.out.println(".:MENU:.");
            System.out.println("¿De donde quiere obtener los datos?");
            System.out.println("1. Fichero");
            System.out.println("2. BBDD");
            System.out.println("3. Hibernate");
            System.out.println("4. JSON");

            try {
                int option = Integer.parseInt(in.nextLine());
                switch (option) {
                    case 1:
                        System.out.println("¿Sobre que fichero?");
                        emisor = new FileManagement(in.nextLine(), true);
                        break;
                    case 2:
                        System.out.println("Conectando...");
                        emisor = new ConnectionManagement();
                        break;
                    case 3:
                        System.out.println(".:Hibernate:.");
                        try {
                            emisor = new HibernateManager();
                        } catch (Exception e) {
                            System.err.println("Error de conexion");
                        }
                        break;
                    case 4:
                        System.out.println("JSON");
                        try {
                            emisor = new JSONController();
                        } catch (Exception e) {
                            System.err.println("Error");
                        }
                        break;
                    case 5:
                        System.out.println("MONGODB");
                        try {
                            emisor = new MongoController();
                        } catch (Exception e) {
                            System.err.println("Error");
                        }
                        break;
                    //   default:
                    //     System.err.println("ERROR, SALIENDO");
                    //   System.exit(1);
                }
                try {

                    Boolean repeatCase1 = true;
                    while (repeatCase1) {
                        System.out.println(".:Menu:.");
                        System.out.println("1. Imprimir");
                        System.out.println("2. Pasar datos a otro almacenamiento");
                        System.out.println("3. Eliminar dato");
                        System.out.println("4. Añadir dato");
                        System.out.println("5. Actualizar dato");
                        System.out.println("6. Marcas");
                        System.out.println("7. Salir");

                        try {
                            int optionCase1 = Integer.parseInt(in.nextLine());
                            switch (optionCase1) {
                                case 1:
                                    yomismo.print(controller.print(emisor));
                                    break;
                                case 2:
                                    System.out.println("¿Sobre que almacenamiento?");
                                    System.out.println("1. A BBDD");
                                    System.out.println("2. A Fichero");
                                    System.out.println("3. A Hibernate");
                                    System.out.println("4. A JSON");
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
                                                break;
                                            case 2:
                                                System.out.println("¿Nombre del archivo?");
                                                try {
                                                    String nombreDelArchivo = in.nextLine();
                                                    System.out.println("¿Borrar datos anteriores?");
                                                    System.out.println("1. Si");
                                                    System.out.println("2. No");
                                                    Boolean borrarDatosAnteriores = false;
                                                    if (Integer.parseInt(in.nextLine()) == 2) {
                                                        borrarDatosAnteriores = true;
                                                    }
                                                    receptor = new FileManagement(nombreDelArchivo, borrarDatosAnteriores);
                                                    controller.put(emisor, receptor);
                                                } catch (Exception e) {
                                                    System.err.println("Escoge si o no");
                                                }
                                                break;
                                            case 3:
                                                receptor = new HibernateManager();
                                                controller.put(emisor, receptor);
                                                break;
                                            case 4:
                                                receptor = new JSONController();
                                                controller.put(emisor, receptor);
                                                break;
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
                                        if (emisor.getClass() == FileManagement.class) {
                                            System.out.println("¿Cuantos coches desea añadir?");
                                            controller.add(emisor, yomismo.getCarsFromKB(Integer.parseInt(in.nextLine()), in, (FileManagement) emisor));
                                        } else if (emisor.getClass() == ConnectionManagement.class) {
                                            System.out.println("¿Cuantos coches desea añadir?");
                                            controller.add(emisor, yomismo.getCarsFromKB(Integer.parseInt(in.nextLine()), in, (ConnectionManagement) emisor));
                                        } else if (emisor.getClass() == JSONController.class) {
                                            System.out.println("¿Cuantos coches desea añadir?");
                                            int cochesParaAnnadirAhora = Integer.parseInt(in.nextLine());
                                            ArrayList<Coche> cochesListos = new ArrayList<>();
                                            for (int i = 0; i < cochesParaAnnadirAhora; i++) {
                                                cochesListos.add(yomismo.getCarFromKB(in, emisor));
                                            }
                                            controller.add(emisor, cochesListos);
                                        } else if (emisor.getClass() == MongoController.class) {
                                            System.out.println("¿Cuantos coches desea añadir?");
                                            int cochesParaAnnadirAhora = Integer.parseInt(in.nextLine());
                                            ArrayList<Coche> cochesListos = new ArrayList<>();
                                            for (int i = 0; i < cochesParaAnnadirAhora; i++) {
                                                cochesListos.add(yomismo.getCarFromKB(in, emisor));
                                            }
                                            controller.add(emisor, cochesListos);
                                        }
                                    } catch (Exception e) {
                                        System.err.println("Error");
                                    }
                                    break;
                                case 5:
                                    yomismo.print(controller.print(emisor));
                                    System.out.println("Introduce ID del dato a actualizar");
                                    try {
                                        int n = Integer.parseInt(in.nextLine());
                                        controller.update(emisor, yomismo.getCarFromKB(in, emisor), n);

                                    } catch (Exception e) {
                                        System.err.println("Fallo en la entrada");
                                    }
                                    break;
                                case 6:
                                    Boolean repeatBrandsMenu = true;
                                    while (repeatBrandsMenu) {
                                        System.out.println("1. añadir marca");
                                        System.out.println("2. borrar marca");
                                        System.out.println("3. actualizar marca");
                                        System.out.println("4. leer marcas");
                                        System.out.println("5. salir");
                                        int brandOption = Integer.parseInt(in.nextLine());
                                        try {
                                            switch (brandOption) {
                                                case 1:
                                                    try {

                                                        System.out.println("Nombre de la marca:");
                                                        String brandName = in.nextLine();
                                                        System.out.println("Nacionalidad de la marca:");
                                                        String brandCountry = in.nextLine();
                                                        System.out.println("Año de la marca:");
                                                        int yearOfFundation = Integer.parseInt(in.nextLine());
                                                        emisor.addBrand(brandName, brandCountry, yearOfFundation);
                                                    } catch (Exception e) {
                                                        System.err.println("Porfavor introduzca numeros o letras segun se le especifique");
                                                    }
                                                    break;
                                                case 2:
                                                    System.out.println("ATENCION: SE BORRARAN TODOS LOS COCHES QUE ESTEN RELACIONADOS CON ESA MARCA");
                                                    System.out.println("Introduzca nombre de la marca");
                                                    emisor.deleteBrand(in.nextLine());
                                                    break;
                                                case 3:
                                                    try {
                                                        System.out.println("No podrás cambiar el nombre de una marca");
                                                        System.out.println("Introduzca marca a actualizar:");
                                                        String brandName = in.nextLine();
                                                        Brand brand = new Brand();
                                                        System.out.println("Introduzca nacionalidad de la marca:");
                                                        brand.setBrandCountry(in.nextLine());
                                                        System.out.println("Introduzca año de fundaciond e la marca:");
                                                        brand.setBrandYearOfFundation(Integer.valueOf(in.nextLine()));
                                                        emisor.updateBrand(brandName, brand);
                                                    } catch (Exception e) {
                                                        System.err.println("Porfavor introduzca numeros o letras segun se le especifique");
                                                    }
                                                    break;
                                                case 4:
                                                    yomismo.printBrand(emisor.readBrands());
                                                    break;
                                                case 5:
                                                    repeatBrandsMenu = false;
                                                    break;
                                            }
                                        } catch (Exception e) {
                                            System.err.println("Porfavor introduce una de las opciones");
                                        }
                                    }
                                    break;
                                case 7:
                                    repeatCase1 = false;
                                    break;
                            }
                        } catch (Exception e) {
                            System.err.println("Porfavor introduce un numero");
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error qeneral");
                }
            } catch (Exception e) {
                System.err.println("Porfavor introduce un numero");
            }

        }
    }

    public void print(ArrayList<Coche> coches) {
        for (Coche coch : coches) {
            System.out.println(coch.getID());
            System.out.println(coch.getMarca().getBrandName());
            System.out.println(coch.getModelo());
            System.out.println(coch.getCavallaje() + "CV");
            System.out.println(coch.getColor());

            System.out.println("####################\n");
        }
    }

    public void printBrand(HashMap<String, Brand> brandHashMap) {
        for (Brand brand : brandHashMap.values()) {
            System.out.println(brand.getIdBrand());
            System.out.println(brand.getBrandName());
            System.out.println(brand.getBrandCountry());
            System.out.println(brand.getBrandYearOfFundation());
        }
    }

    public ArrayList<Coche> getCarsFromKB(int n, Scanner in, FileManagement emisor) {
        ArrayList<Coche> carsArrayList = new ArrayList<>();
        Coche coche;
        HashMap<String, Brand> brandHashMap = emisor.readBrands();
        for (int i = 0; i < n; i++) {
            coche = new Coche();
            coche.setID(emisor.getnextIDFromConfig());
            System.out.println("Introduzca Marca");
            coche.setMarca(brandHashMap.get(in.nextLine()));
            System.out.println("Introduzca Modelo");
            coche.setModelo(in.nextLine());
            System.out.println("Introduzca Cavallaje");
            coche.setCavallaje(Integer.parseInt(in.nextLine()));
            System.out.println("Introduzca Color");
            coche.setColor(in.nextLine());
            carsArrayList.add(coche);
            emisor.setLastIDToConfig();
        }
        return carsArrayList;
    }

    public ArrayList<Coche> getCarsFromKB(int n, Scanner in, ConnectionManagement emisor) {
        ArrayList<Coche> carsArrayList = new ArrayList<>();
        Coche coche;
        HashMap<String, Brand> brandHashMap = emisor.readBrands();
        for (int i = 0; i < n; i++) {
            coche = new Coche();
            System.out.println("Introduzca Marca");
            coche.setMarca(brandHashMap.get(in.nextLine()));
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

    public Coche getCarFromKB(Scanner in, MagementInterface emisor) {
        ArrayList<Coche> carsArrayList = new ArrayList<>();
        Coche coche;
        coche = new Coche();
        System.out.println("Introduzca Marca");
        coche.setMarca(emisor.readBrands().get(in.nextLine()));
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
