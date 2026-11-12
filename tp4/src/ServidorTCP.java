import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServidorTCP {

    private static final int PUERTO = 5000;

    public static void main(String[] args) {

        System.out.println("Servidor TCP iniciado. Puerto: " + PUERTO);

        try (ServerSocket servidor = new ServerSocket(PUERTO)) {

            System.out.println("Esperando conexión del cliente...");

            try (Socket cliente = servidor.accept();
                 DataInputStream entrada = new DataInputStream(
                         cliente.getInputStream())) {

                System.out.println("Cliente conectado: "
                        + cliente.getInetAddress());

                // Recibir lote JSON
                recibirLote(entrada, 'J');

                // Recibir lote binario
                recibirLote(entrada, 'B');

                System.out.println("\nTodos los datos fueron recibidos correctamente.");

            }

        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static void recibirLote(DataInputStream entrada, char formato)
            throws IOException {

        char formatoRecibido = entrada.readChar();

        if (formatoRecibido != formato) {
            throw new IOException("Formato inesperado");
        }

        int cantidad = entrada.readInt();

        if (cantidad < 0 || cantidad > 10000) {
            throw new IOException("Cantidad de transacciones inválida");
        }

        long bytesTotales = 0;
        long inicio = System.nanoTime();

        for (int i = 0; i < cantidad; i++) {

            int longitud = entrada.readInt();

            if (longitud < 0 || longitud > 1_000_000) {
                throw new IOException("Tamaño de mensaje inválido");
            }

            byte[] datos = new byte[longitud];
            entrada.readFully(datos);

            bytesTotales += longitud;

            Transaccion transaccion;

            if (formato == 'J') {

                String json = new String(datos, StandardCharsets.UTF_8);
                transaccion = ParserMensajes.deserializarJSON(json);

            } else {

                transaccion = ParserMensajes.deserializarBinario(datos);
            }

            // Mostrar solamente algunas transacciones como ejemplo
            if (i < 3) {
                System.out.println("Recibida: " + transaccion);
            }
        }

        long fin = System.nanoTime();
        double tiempoMs = (fin - inicio) / 1_000_000.0;

        System.out.println("\n--- Lote " +
                (formato == 'J' ? "JSON" : "BINARIO") + " ---");

        System.out.println("Transacciones recibidas: " + cantidad);
        System.out.println("Carga útil recibida: " + bytesTotales + " bytes");
        System.out.printf("Tiempo de recepción y deserialización: %.3f ms%n",
                tiempoMs);
    }
}