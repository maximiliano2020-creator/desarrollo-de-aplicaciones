public class Main {

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== INICIO DEL SISTEMA DE ACTORES ===\n");

        // 1. Crear el ActorProcesador
        ActorProcesador procesador = new ActorProcesador();

        System.out.println("[SPAWN] ActorProcesador creado.");

        // 2. Crear e iniciar el hilo del procesador
        Thread hiloProcesador = new Thread(procesador);
        hiloProcesador.start();

        // 3. Crear los sensores
        int cantidadSensores = 3;
        int mensajesPorSensor = 200;

        ActorSensor[] sensores = new ActorSensor[cantidadSensores];
        Thread[] hilosSensores = new Thread[cantidadSensores];

        for (int i = 0; i < cantidadSensores; i++) {

            sensores[i] = new ActorSensor(
                "Sensor-" + (i + 1),
                procesador,
                mensajesPorSensor
            );

            hilosSensores[i] = new Thread(sensores[i]);

            System.out.println(
                "[SPAWN] Sensor-" + (i + 1) + " creado."
            );
        }

        // 4. Iniciar los sensores
        System.out.println("\n=== INICIANDO SENSORES ===\n");

        for (Thread hilo : hilosSensores) {
            hilo.start();
        }

        // 5. Esperar a que todos los sensores terminen
        for (Thread hilo : hilosSensores) {
            hilo.join();
        }

        System.out.println("\n=== TODOS LOS SENSORES FINALIZARON ===");

        // 6. Indicar al procesador que debe detenerse
        procesador.detener();

        // 7. Esperar a que procese todos los mensajes
        hiloProcesador.join();

        System.out.println("\n=== SISTEMA FINALIZADO ===");
    }
}