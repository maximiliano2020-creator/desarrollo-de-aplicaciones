import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ChatClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {

        try (
            Socket socket = new Socket(HOST, PORT);

            PrintWriter output = new PrintWriter(
                socket.getOutputStream(),
                true,
                StandardCharsets.UTF_8
            );

            Scanner scanner = new Scanner(System.in)
        ) {

            BufferedReader input = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream(),
                    StandardCharsets.UTF_8
                )
            );

            System.out.println("Conectado al servidor.");
            System.out.println("Escribí tus mensajes. Usa /salir para desconectarte.");

            Thread receiver = new Thread(() -> {

                try {

                    String message;

                    while ((message = input.readLine()) != null) {
                        System.out.println("\n" + message);
                    }

                } catch (IOException e) {
                    System.out.println("Conexión interrumpida.");
                }
            });

            receiver.setDaemon(true);
            receiver.start();

            while (scanner.hasNextLine()) {

                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("/salir")) {
                    break;
                }

                output.println(message);
            }

        } catch (IOException e) {

            System.out.println("No se pudo conectar: " + e.getMessage());
        }
    }
}