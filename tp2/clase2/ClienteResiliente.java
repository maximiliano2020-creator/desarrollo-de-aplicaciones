package clase2;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;

public class ClienteResiliente {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5001;
    private static final int TIMEOUT_MS = 2000;
    private static final int MAX_REINTENTOS = 4;

    public static void main(String[] args) {

        System.out.println("=== Iniciando Cliente Resiliente ===");

        boolean exito = false;
        int intento = 0;
        int esperaMs = 1000;

        // Generador de números aleatorios para el Jitter
        Random random = new Random();

        // Tiempo total de comunicación
        long tiempoTotal = 0;

        while (!exito && intento < MAX_REINTENTOS) {

            intento++;

            // Comenzamos a medir el tiempo del intento
            long inicioIntento = System.currentTimeMillis();

            System.out.println(
                    "\n[Cliente] Intento " + intento
                    + " de " + MAX_REINTENTOS + "..."
            );

            try (Socket socket = new Socket()) {

                // Timeout para establecer la conexión
                socket.connect(
                        new InetSocketAddress(HOST, PUERTO),
                        TIMEOUT_MS
                );

                // Timeout para recibir datos
                socket.setSoTimeout(TIMEOUT_MS);

                DataInputStream in =
                        new DataInputStream(socket.getInputStream());

                String respuesta = in.readUTF();

                System.out.println(
                        "[Cliente ÉXITO] Respuesta recibida: "
                        + respuesta
                );

                exito = true;

            } catch (SocketTimeoutException e) {

                System.err.println(
                        "[Cliente ERROR] Timeout alcanzado ("
                        + TIMEOUT_MS
                        + " ms sin respuesta)."
                );

            } catch (IOException e) {

                System.err.println(
                        "[Cliente ERROR] Error de comunicación/conexión: "
                        + e.getMessage()
                );
            }

            // Calculamos cuánto tardó este intento
            long finIntento = System.currentTimeMillis();

            tiempoTotal += finIntento - inicioIntento;

            // Si falló y todavía quedan intentos
            if (!exito && intento < MAX_REINTENTOS) {

                // Jitter aleatorio entre 0 y 500 ms
                int jitter = random.nextInt(501);

                // Backoff + Jitter
                int tiempoEspera = esperaMs + jitter;

                System.out.println(
                        "[Cliente] Backoff Exponencial + Jitter."
                );

                System.out.println(
                        "[Cliente] Esperando "
                        + tiempoEspera
                        + " ms antes de reintentar..."
                );

                try {

                    Thread.sleep(tiempoEspera);

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();
                }

                // Aumentamos el Backoff para el próximo intento
                esperaMs *= 2;
            }
        }

        // Resultado final
        if (!exito) {

            System.err.println(
                    "\n[Cliente FALLO DEFINITIVO] "
                    + "Se superó el máximo de reintentos. "
                    + "Operación cancelada."
            );
        }

        // ============================
        // MÉTRICAS DE RESILIENCIA
        // ============================

        System.out.println("\n=== MÉTRICAS DE RESILIENCIA ===");

        if (exito) {
            System.out.println("Estado final: Éxito");
        } else {
            System.out.println("Estado final: Fallo definitivo");
        }

        System.out.println(
                "Intentos realizados: " + intento
        );

        System.out.println(
                "Tiempo total transcurrido: "
                + tiempoTotal
                + " ms"
        );
    }
}