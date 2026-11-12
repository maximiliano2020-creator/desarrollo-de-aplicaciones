import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ClienteTCP {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;
    private static final int CANTIDAD_TRANSACCIONES = 1000;

    public static void main(String[] args) {

        List<Transaccion> transacciones = generarTransacciones();

        System.out.println("Transacciones generadas: "
                + transacciones.size());

        try (Socket socket = new Socket(HOST, PUERTO);
             DataOutputStream salida = new DataOutputStream(
                     socket.getOutputStream())) {

            System.out.println("Conectado al servidor TCP.");

            // Enviar lote JSON
            enviarJSON(salida, transacciones);

            // Enviar lote binario
            enviarBinario(salida, transacciones);

            salida.flush();

            System.out.println("\nTodos los lotes fueron enviados.");

        } catch (IOException e) {
            System.err.println("Error en el cliente: " + e.getMessage());
        }
    }

    // GENERAR 1000 TRANSACCIONES SIMULADAS
    private static List<Transaccion> generarTransacciones() {

        List<Transaccion> lista = new ArrayList<>();

        long timestampActual = System.currentTimeMillis();

        for (int i = 1; i <= CANTIDAD_TRANSACCIONES; i++) {

            Transaccion t = new Transaccion(
                    i,
                    "Nodo" + (i % 5 + 1),
                    1000.0 + (i * 15.75),
                    timestampActual + i
            );

            lista.add(t);
        }

        return lista;
    }

    // ENVIAR TRANSACCIONES EN JSON
    private static void enviarJSON(
            DataOutputStream salida,
            List<Transaccion> transacciones) throws IOException {

        long bytesTotales = 0;

        long inicio = System.nanoTime();

        // Identificador del formato JSON
        salida.writeChar('J');

        // Cantidad de transacciones
        salida.writeInt(transacciones.size());

        for (Transaccion t : transacciones) {

            String json = ParserMensajes.serializarJSON(t);

            byte[] datos = json.getBytes(StandardCharsets.UTF_8);

            // Longitud del mensaje y contenido
            salida.writeInt(datos.length);
            salida.write(datos);

            bytesTotales += datos.length;
        }

        salida.flush();

        long fin = System.nanoTime();

        double tiempoMs = (fin - inicio) / 1_000_000.0;

        System.out.println("\n--- ENVÍO JSON ---");
        System.out.println("Transacciones enviadas: " + transacciones.size());
        System.out.println("Carga útil: " + bytesTotales + " bytes");
        System.out.printf("Tiempo de serialización y envío: %.3f ms%n",
                tiempoMs);
    }

    // ENVIAR TRANSACCIONES EN BINARIO
    private static void enviarBinario(
            DataOutputStream salida,
            List<Transaccion> transacciones) throws IOException {

        long bytesTotales = 0;

        long inicio = System.nanoTime();

        // Identificador del formato binario
        salida.writeChar('B');

        // Cantidad de transacciones
        salida.writeInt(transacciones.size());

        for (Transaccion t : transacciones) {

            byte[] datos = ParserMensajes.serializarBinario(t);

            // Longitud del mensaje y contenido
            salida.writeInt(datos.length);
            salida.write(datos);

            bytesTotales += datos.length;
        }

        salida.flush();

        long fin = System.nanoTime();

        double tiempoMs = (fin - inicio) / 1_000_000.0;

        System.out.println("\n--- ENVÍO BINARIO ---");
        System.out.println("Transacciones enviadas: " + transacciones.size());
        System.out.println("Carga útil: " + bytesTotales + " bytes");
        System.out.printf("Tiempo de serialización y envío: %.3f ms%n",
                tiempoMs);
    }
}