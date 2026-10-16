import java.io.File;
import java.util.*;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Main {

    public static void main(String [] args) throws IOException {

        Scanner scanner = new Scanner (System.in);

        menuEjercicios();

        while(true) {

            String input1 = scanner.nextLine();

            if (input1.equals("1")) {

                ejercicio1();
                System.out.println();
                menuEjercicios();

            } else if (input1.equals("2")) {

                ejercicio2(scanner);
                System.out.println();
                menuEjercicios();

            } else if (input1.equals("3")){

                //ejercicio3();

            } else if (input1.equals("4")){

                //ejercicio4();



            } else if (input1.equalsIgnoreCase("exit")) {

                System.out.println("Bye-bye!");
                break;
            } else {

                System.out.println("Inténtalo de nuevo o escribe 'exit'");
                System.out.println();
                menuEjercicios();
            }
        }

    }

    public static void menuEjercicios(){
        System.out.println("---------------------------------------");
        System.out.println("--TAREA 2 RA1 -Random Access File (RAF)--");
        System.out.println("---------------------------------------");

        System.out.println("Ejercicios: 1, 2, 3, 4, 5, 6, 7");
        System.out.println();
        System.out.println("Introduce el número del ejercicio y presiona 'Enter'. \n \nTeclea 'exit' para finalizar el programa");
        System.out.println("-----------");
    }

    public static void ejercicio1 () throws IOException {

    //Empleados Binarios

        //Introducimos los datos a guardar en el fichero
        String[] apellEmpleados= {"Herrera", "López", "Sánchez", "Fernández"};
        int longitudApell = 10;
        int[] departamentos = {2, 12, 14, 12};
        double[] salarios = {1000.0, 3600.2, 2030.0, 2000.5};
        int tam_registro = 4 + (2*longitudApell) + 4 + 8; // 36 Bytes

        /*id (int -> 4 Bytes) + apellido (char*longitud -> 2 Bytes * 10) + dept (int -> 4 Bytes) + salario (double -> 8 Bytes) => Tamaño registro = 4 + 20 + 4 + 8 = 36 Bytes*/


        //Creamos la ruta para el fichero
        String rutaAbsoluta = ("C:" + File.separator + "AD" + File.separator + "Ejercicios");
        File miDirectorio= new File (rutaAbsoluta, "miDirectorio");
        File ficheroEmpleados = new File(miDirectorio, "Empleados.dat");

        //RAF crea el fichero, pero tenemos que crear la carpeta que lo contiene:
        if (miDirectorio.mkdirs()) {
            System.out.println("Directorio creado con éxito");
        }

        /* Creamos el RAF para lectura y escritura.
        El modo "rf" abre el fichero para lectura y escritura y, si no existe, lo crea.
        Escribimos los empleados en el fichero con un bucle for: */

        try (RandomAccessFile raf = new RandomAccessFile(ficheroEmpleados, "rw")){


            raf.setLength(0); //Borra el contenido anterior

            /* ESCRITURA EN EL FICHERO */

            for(int i = 0; i < 4; i++){


                int id = i + 1;

                //Creamos un string buffer para almacenar de uno a uno los apellidos de los empleados y establecemos la longitud a 10 caracteres:

                StringBuffer sBuffer = new StringBuffer(apellEmpleados[i]);
                // ^ guarda el apellido de la posición actual

                sBuffer.setLength(10);
                // ^ lo recorta a 10 caracteres

                raf.writeInt(id);                        //id -> int -> 4 Bytes
                raf.writeChars(sBuffer.toString());     //apellido -> 10xchar -> 20 bytes
                raf.writeInt(departamentos[i]);         //departamento -> int -> 4 Bytes
                raf.writeDouble(salarios[i]);           //salario -> double -> 8 Bytes

                //Cada registro ocupa 36 Bytes

            }

            System.out.println("Empleados registrados en el fichero binario");

            /* LECTURA DEL FICHERO */

            raf.seek(0); //Colocamos el puntero al inicio

            while (raf.getFilePointer() < raf.length()){

                int id = raf.readInt();

                char[] caracteres = new char[longitudApell];
                for (int i = 0; i < longitudApell; i++) {
                    caracteres[i] = raf.readChar();
                }
                String apellido = new String(caracteres).trim(); //Usamos trim para eliminar los espacios al final del apellido

                int dpto = raf.readInt();
                double salario = raf.readDouble();

                System.out.println("ID: " + id + " | Apellido: " + apellido + " | Departamento: " + dpto + " | Salario: " + salario);

            }

        }catch (IOException e){
            System.out.println("Error: " + e);
        }

    }

    public static void ejercicio2(Scanner scanner) throws IOException{

        try (RandomAccessFile raf = new RandomAccessFile("nums.bin", "rw")){

            raf.setLength(0);

            System.out.println("Vamos a guardar tus 5 números favoritos en un fichero. Escribe cada uno y pulsa Enter: ");
            int [] favoritos= new int[5];

            for (int i = 0; i < 5; i++){
                favoritos[i] = scanner.nextInt();
            }

            for (int i = 0; i < 5; i++){
                raf.writeInt(favoritos[i]);
            }

            scanner.nextLine(); //lectura del último salto de línea

            System.out.println();
            System.out.println("Ahora vamos a leerlos desde el fichero binario.");
            System.out.println("Tus números favoritos son: ");

            raf.seek(0); //devolvemos el puntero al inicio del fichero

            while (raf.getFilePointer() < raf.length()){
                int numero = raf.readInt();
                System.out.print(numero + " | ");
            }



        }catch (IOException e){
            System.out.println("Error: " + e.getMessage());
        }



    }
}

