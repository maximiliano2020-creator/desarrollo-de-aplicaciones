import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

    private static final int PORT = 5000;

    private static final Set<ClientHandler> clients =
            ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {

        System.out.println("Iniciando servidor de chat...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            System.out.println("Servidor escuchando en puerto " + PORT);

            while (true) {

                Socket socket = serverSocket.accept();

                ClientHandler client = new ClientHandler(socket);

                clients.add(client);

                client.start();
            }

        } catch (IOException e) {
            System.out.println("Error del servidor: " + e.getMessage());
        }
    }

    public static void broadcast(String message, ClientHandler sender) {

        for (ClientHandler client : clients) {

            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    static class ClientHandler extends Thread {

        private final Socket socket;
        private PrintWriter output;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void sendMessage(String message) {

            if (output != null) {
                output.println(message);
            }
        }

        @Override
        public void run() {

            try {

                BufferedReader input = new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream(),
                        StandardCharsets.UTF_8
                    )
                );

                output = new PrintWriter(
                    socket.getOutputStream(),
                    true,
                    StandardCharsets.UTF_8
                );

                System.out.println("Cliente conectado: "
                        + socket.getInetAddress());

                String message;

                while ((message = input.readLine()) != null) {

                    System.out.println("Mensaje recibido: " + message);

                    broadcast(message, this);
                }

            } catch (IOException e) {

                System.out.println("Cliente desconectado.");

            } finally {

                clients.remove(this);

                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error cerrando conexión.");
                }
            }
        }
    }
}