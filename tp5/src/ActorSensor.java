import java.util.Random;

public class ActorSensor implements Runnable {

    private final String sensorId;
    private final ActorProcesador procesador;
    private final int cantidadMensajes;

    public ActorSensor(String sensorId, ActorProcesador procesador,
                       int cantidadMensajes) {

        this.sensorId = sensorId;
        this.procesador = procesador;
        this.cantidadMensajes = cantidadMensajes;
    }

    @Override
    public void run() {

        Random random = new Random();

        for (int i = 0; i < cantidadMensajes; i++) {

            // Generar temperatura aleatoria entre 15 y 35 grados
            double temperatura = 15 + random.nextDouble() * 20;

            // Crear mensaje inmutable
            Mensaje mensaje = new Mensaje(sensorId, temperatura);

            // Enviar mensaje al procesador
            procesador.enviarMensaje(mensaje);
        }

        System.out.println(
            "[SENSOR] " + sensorId
            + " finalizó el envío de " + cantidadMensajes + " mensajes."
        );
    }
}