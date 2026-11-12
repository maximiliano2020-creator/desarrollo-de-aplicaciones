import java.net.*;
import java.nio.charset.StandardCharsets;

public class ReceptorUDP {

    private static final int PORT = 6000;

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket(PORT)) {

            socket.setSoTimeout(5000);

            System.out.println("Receptor UDP iniciado.");
            System.out.println("Esperando datagramas en puerto " + PORT);

            while (true) {

                try {

                    byte[] buffer = new byte[1024];

                    DatagramPacket packet = new DatagramPacket(
                        buffer,
                        buffer.length
                    );

                    socket.receive(packet);

                    String message = new String(
                        packet.getData(),
                        packet.getOffset(),
                        packet.getLength(),
                        StandardCharsets.UTF_8
                    );

                    System.out.println(
                        "Alerta recibida: " + message
                    );

                } catch (SocketTimeoutException e) {

                    System.out.println(
                        "ADVERTENCIA: No se recibieron datagramas durante 5 segundos."
                    );
                }
            }

        } catch (Exception e) {

            System.out.println("Error en receptor: " + e.getMessage());
        }
    }
}