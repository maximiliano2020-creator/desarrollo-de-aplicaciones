import java.util.concurrent.ConcurrentLinkedQueue;

public class ActorProcesador implements Runnable {

    // Buzon de mensajes FIFO
    private final ConcurrentLinkedQueue<Mensaje> buzon;

    // Estado privado del actor
    private int contador;
    private double sumaTemperaturas;

    private volatile boolean activo;

    public ActorProcesador() {
        this.buzon = new ConcurrentLinkedQueue<>();
        this.contador = 0;
        this.sumaTemperaturas = 0;
        this.activo = true;
    }

    // Recibe un mensaje y lo coloca en el buzon
    public void enviarMensaje(Mensaje mensaje) {
        buzon.offer(mensaje);
    }

    // Procesa los mensajes secuencialmente
    @Override
    public void run() {

        while (activo || !buzon.isEmpty()) {

            Mensaje mensaje = buzon.poll();

            if (mensaje != null) {

                contador++;
                sumaTemperaturas += mensaje.getTemperatura();

                System.out.println(
                    "[PROCESADOR] Mensaje #" + contador
                    + " | Sensor: " + mensaje.getSensorId()
                    + " | Temperatura: " + mensaje.getTemperatura()
                );
            }
        }

        System.out.println("\n=== RESULTADO FINAL ===");
        System.out.println("Mensajes procesados: " + contador);

        if (contador > 0) {
            double promedio = sumaTemperaturas / contador;

            System.out.printf(
                "Promedio de temperatura: %.2f °C%n",
                promedio
            );
        }
    }

    // Finaliza el actor cuando termina el envio de mensajes
    public void detener() {
        activo = false;
    }
}