import java.io.File;
import java.sql.SQLOutput;
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

                ejercicio3();
                System.out.println();
                menuEjercicios();

            } else if (input1.equals("4")){

                ejercicio4();
                System.out.println();
                menuEjercicios();

            } else if (input1.equals("5")) {

                ejercicio5();
                System.out.println();
                menuEjercicios();

            } else if (input1.equals("6")){

                //ejercicio6();
                System.out.println();
                menuEjercicios();

            } else if (input1.equals("7")){

                //ejercicio7();
                System.out.println();
                menuEjercicios();


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

    public static void ejercicio3(){

        //Acceso directo a slots de 4 bytes

        long tam_slot = 4L; //cada entero ocupa 4 bytes

        try (RandomAccessFile raf = new RandomAccessFile("slots.bin", "rw")){

            raf.setLength(0);

            int [] favoritos= {22, 34, 45, 67, 267}; //para hacerlo más rápido, vamos a establecer los 5 números sin pedir completar el ejercicio 2

            System.out.println("Números a escribir en el fichero: ");
            for (int fav : favoritos){
                System.out.print(fav + " | ");
            }

            System.out.println();
            System.out.println();
            System.out.println("Escribiendo en el fichero...");
            System.out.println();


            for (int i = 0; i < favoritos.length; i++){

                long offset = tam_slot * i;
                raf.seek(offset); //colocamos el puntero en la posición con el offset para escribir el número
                raf.writeInt(favoritos[i]);
            }

            System.out.println("Números escritos correctamente");


            System.out.println("Calculamos la posición del segundo slot para leer el número utilizando el puntero y calculando que cada entero ocupa 4 Bytes. \n El segundo entero empezará en el Byte número 4 ya que: \n El primer entero se encuentra del byte 0 al byte 3, ocupando 4 bytes.");

            raf.seek(tam_slot);

            int valorSegPos = raf.readInt();

            System.out.println();
            System.out.println("Lectura del segundo valor desde el fichero binario: " + valorSegPos);


        }catch (IOException e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static void ejercicio4(){


        try (RandomAccessFile raf = new RandomAccessFile("nombres.bin", "rw")){

            raf.setLength(0);

            String [] nombres= {"Ana", "Jesús", "Álvaro"};

            System.out.println("\n Al utilizar writeUTF y readUTF vamos a tener que leer todo el fichero hasta llegar a la posición buscada. " +
                    "\n Es muy cómodo y sabe dónde acaba y empieza cada registro, pero no nos permite acceder con puntero, sólo sirve para Acceso Secuencial \n" +
                    "\n Vamos a escribir los siguientes nombres de manera secuencial en un documento binario utilizando writeUTF:");

            for (String nombre : nombres){

                System.out.print(nombre + " ");

                raf.writeUTF(nombre);
            }

            System.out.println("\n");
            System.out.println("Nombres escritos en el fichero correctamente.");

            raf.seek(0); //devolvemos el puntero al comienzo del fichero

            System.out.println();
            System.out.println("Ahora leemos los nombres secuencialmente desde el fichero: ");

            while (raf.getFilePointer()< raf.length()){

                System.out.print(raf.readUTF() + " ");
            }

        }catch (IOException e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static void ejercicio5(){

        //Registro: Definir un registro con id (int, 4 bytes) y un nombre (10 caracteres, cada char 2 bytes)

        try (RandomAccessFile raf = new RandomAccessFile("registros.bin", "rw")){

            raf.setLength(0);

            int [] ids = {23, 34, 56};
            String [] nombres= {"Ana", "Jesús", "Álvaro"};

            System.out.println("Vamos a introducir los siguientes registros en un fichero binario: ");
            System.out.println();

            for (int i = 0; i < 3; i++){
                System.out.println("ID empleado: " + ids[i] + " | Nombre empleado: " + nombres[i]);
            }

            //Definimos la longitud del nombre: 10 caracteres

            int longNombre = 10;

            int tam_registro = 4 + longNombre * 2; // int -> 4 bytes; char -> 2 bytes. TAM_REGISTRO = 24 BYTES

            System.out.println();
            System.out.println("Seguiremos la posición del puntero mientras escribe los registros en el fichero binario, para ilustrar cuántos bytes van ocupando el id, el nombre y el total del registro: ");
            System.out.println();

            raf.seek(0);

            for(int i = 0; i < ids.length; i++){

                System.out.println("Comienzo registro " + (i+1) + " | Posición puntero: " + raf.getFilePointer());

                raf.writeInt(ids[i]);

                System.out.println("Comienzo del nombre. Posición puntero: " + raf.getFilePointer());

                StringBuffer sBuffer = new StringBuffer(nombres[i]);
                sBuffer.setLength(longNombre);
                // ^ lo recortamos a la longitud establecida
                raf.writeChars(sBuffer.toString());

                System.out.println("Aquí acaba el registro " + (i+1) + " | Posición puntero: " + raf.getFilePointer());
                System.out.println();

            }

            raf.seek(0); //devolvemos el puntero al comienzo del fichero

            System.out.println("Registros escritos correctamente en el fichero");
            System.out.println();

            System.out.println("Para leer el nombre del tercer registro tendremos que colocar el puntero al comienzo del registro 3, pasados los 4 bytes que ocupa el id.");
            System.out.println();

            System.out.println("Lectura del nombre en el tercer registro... ");
            System.out.println();

            int posicionRegistro3 = 24*2; //comienzo del 3er registro -> Byte 48

            raf.seek(posicionRegistro3 + 4); //colocamos el puntero para leer el nombre en el tercer registro (tras el id que ocupa 4 bytes)

            char[] caracteres = new char[longNombre];

            for (int i = 0; i < longNombre; i++) {
                caracteres[i] = raf.readChar();
            }

            String nombre = new String(caracteres).trim(); //Usamos trim para eliminar los espacios al final del nombre

            System.out.println("Nombre del registro 3: " + nombre);

        }catch (IOException e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static void ejercicio6(){

        try (RandomAccessFile raf = new RandomAccessFile("personas.dat", "rw")){

            int [] ids = {1, 2, 3};

        }catch (IOException e){
            System.out.println("Error: " + e.getMessage());
        }


    }
}

