import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {

        String host = "localhost";
        int puerto = 5500;

        Scanner scanner = new Scanner(System.in);

        try (Socket socket = new Socket(host, puerto)) {

            System.out.println("Conectado al servidor.");

            BufferedReader entrada = new BufferedReader(
                    new InputStreamReader(socket.getInputStream())
            );

            PrintWriter salida = new PrintWriter(
                    socket.getOutputStream(), true
            );

            // Pedir datos al usuario
            System.out.print("Ingrese el primer numero entero: ");
            int numero1 = scanner.nextInt();

            System.out.print("Ingrese la operacion (+, -, *, /): ");
            String operacion = scanner.next();

            System.out.print("Ingrese el segundo numero entero: ");
            int numero2 = scanner.nextInt();

            // Empaquetar los datos
            String mensaje = numero1 + ";" + operacion + ";" + numero2;

            // Enviar al servidor
            salida.println(mensaje);

            // Recibir resultado
            String resultado = entrada.readLine();

            System.out.println("Resultado recibido: " + resultado);

        } catch (IOException e) {
            System.out.println("No se pudo conectar con el servidor.");
            System.out.println("Error: " + e.getMessage());
        }
    }
}