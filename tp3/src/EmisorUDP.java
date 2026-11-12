import java.net.*;
import java.nio.charset.StandardCharsets;

public class EmisorUDP {

    public static void main(String[] args) {

        String host = args.length > 0 ? args[0] : "localhost";

        try (DatagramSocket socket = new DatagramSocket()) {

            InetAddress address = InetAddress.getByName(host);

            System.out.println("Emisor UDP iniciado.");

            int contador = 1;

            while (true) {

                String message = "ALERTA: Telemetria numero " + contador;

                byte[] data = message.getBytes(StandardCharsets.UTF_8);

                DatagramPacket packet = new DatagramPacket(
                    data,
                    data.length,
                    address,
                    6000
                );

                socket.send(packet);

                System.out.println("Enviado: " + message);

                contador++;

                Thread.sleep(2000);
            }

        } catch (Exception e) {

            System.out.println("Error en emisor: " + e.getMessage());
        }
    }
}