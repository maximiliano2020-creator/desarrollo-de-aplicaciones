package tp6.servidor;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor {

    public static void main(String[] args) {

        try {
            // Dirección IP con la que los clientes encontrarán el servidor.
            String ipServidor = args.length > 0
                    ? args[0]
                    : "localhost";

            System.setProperty(
                    "java.rmi.server.hostname",
                    ipServidor
            );

            // Crear el registro RMI en el puerto 1099.
            Registry registro = LocateRegistry.createRegistry(1099);

            // Crear el objeto que implementa las operaciones remotas.
            ProcesadorImpl procesador = new ProcesadorImpl();

            // Publicar el servicio con el nombre "Procesador".
            registro.rebind("Procesador", procesador);

            System.out.println("Servidor RMI iniciado correctamente.");
            System.out.println("IP del servidor: " + ipServidor);
            System.out.println("Puerto del registro: 1099");
            System.out.println("Servicio publicado: Procesador");

        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor:");
            e.printStackTrace();
        }
    }
}