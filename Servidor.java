import java.io.*;
import java.net.*;

public class Servidor {

    public static void main(String[] args) {

        int puerto = 5500;

        try (ServerSocket servidor = new ServerSocket(puerto)) {

            System.out.println("Servidor iniciado.");
            System.out.println("Esperando conexión en el puerto " + puerto + "...");

            try (Socket cliente = servidor.accept()) {

                System.out.println("Cliente conectado.");

                BufferedReader entrada = new BufferedReader(
                        new InputStreamReader(cliente.getInputStream())
                );

                PrintWriter salida = new PrintWriter(
                        cliente.getOutputStream(), true
                );

                // Recibir los datos del cliente
                String mensaje = entrada.readLine();

                System.out.println("Petición recibida: " + mensaje);

                // Separar los datos
                String[] datos = mensaje.split(";");

                int numero1 = Integer.parseInt(datos[0]);
                String operacion = datos[1];
                int numero2 = Integer.parseInt(datos[2]);

                String resultado;

                // Realizar la operación
                switch (operacion) {

                    case "+":
                        resultado = String.valueOf(numero1 + numero2);
                        break;

                    case "-":
                        resultado = String.valueOf(numero1 - numero2);
                        break;

                    case "*":
                        resultado = String.valueOf(numero1 * numero2);
                        break;

                    case "/":
                        if (numero2 == 0) {
                            resultado = "ERROR: Division por cero";
                        } else {
                            resultado = String.valueOf((double) numero1 / numero2);
                        }
                        break;

                    default:
                        resultado = "ERROR: Operacion no valida";
                }

                // Enviar resultado al cliente
                salida.println(resultado);

                System.out.println("Resultado enviado: " + resultado);
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}