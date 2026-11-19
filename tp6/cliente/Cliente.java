package tp6.cliente;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.rmi.RemoteException;

import tp6.middleware.Estadisticas;
import tp6.middleware.InterfazRemota;

public class Cliente {

    public static void main(String[] args) {

        String ipServidor = args.length > 0
                ? args[0]
                : "localhost";

        try (Scanner scanner = new Scanner(System.in)) {

            // Conectarse al registro RMI del servidor.
            Registry registro = LocateRegistry.getRegistry(
                    ipServidor, 1099
            );

            // Obtener la referencia al objeto remoto.
            InterfazRemota procesador =
        (InterfazRemota) registro.lookup("Procesador");

            // Informar al servidor que el cliente se conectó.
            procesador.registrarConexion();

            System.out.println("Conectado al servidor RMI.");
            System.out.println("Servidor: " + ipServidor);

            int opcion;

            do {
                System.out.println("\n===== PROCESADOR REMOTO =====");
                System.out.println("1. Calcular estadísticas");
                System.out.println("2. Validar DNI");
                System.out.println("3. Filtrar contenido");
                System.out.println("4. Salir");
                System.out.print("Seleccione una opción: ");

                try {
                    opcion = Integer.parseInt(scanner.nextLine());

                    switch (opcion) {

                        case 1:
                            System.out.print(
                                    "Ingrese números separados por espacios: "
                            );

                            String entrada = scanner.nextLine().trim();

                            if (entrada.isEmpty()) {
                                System.out.println("No ingresó números.");
                                break;
                            }

                            String[] partes = entrada.split("\\s+");
                            double[] numeros = new double[partes.length];

                            for (int i = 0; i < partes.length; i++) {
                                numeros[i] = Double.parseDouble(partes[i]);
                            }

                            Estadisticas resultado =
                                    procesador.calcularEstadisticas(numeros);

                            System.out.println("\n--- RESULTADOS ---");
                            System.out.println(
                                    "Promedio: " + resultado.getPromedio()
                            );
                            System.out.println(
                                    "Máximo: " + resultado.getMaximo()
                            );
                            System.out.println(
                                    "Mínimo: " + resultado.getMinimo()
                            );
                            System.out.println(
                                    "Desviación estándar: "
                                    + resultado.getDesviacionEstandar()
                            );
                            break;

                        case 2:
                            System.out.print("Ingrese un DNI: ");
                            String dni = scanner.nextLine();

                            boolean valido = procesador.validarDatos(dni);

                            System.out.println(
                                    valido ? "DNI válido en formato."
                                           : "DNI inválido en formato."
                            );
                            break;

                        case 3:
                            System.out.print(
                                    "¿Cuántos textos desea ingresar?: "
                            );

                            int cantidad = Integer.parseInt(
                                    scanner.nextLine()
                            );

                            if (cantidad < 0) {
                                System.out.println(
                                        "La cantidad no puede ser negativa."
                                );
                                break;
                            }

                            String[] textos = new String[cantidad];

                            for (int i = 0; i < cantidad; i++) {
                                System.out.print(
                                        "Texto " + (i + 1) + ": "
                                );
                                textos[i] = scanner.nextLine();
                            }

                            System.out.print("Patrón de búsqueda: ");
                            String patron = scanner.nextLine();

                            String[] filtrados =
                                    procesador.filtrarContenido(textos, patron);

                            System.out.println("\n--- RESULTADOS ---");

                            if (filtrados.length == 0) {
                                System.out.println(
                                        "No se encontraron coincidencias."
                                );
                            } else {
                                for (String texto : filtrados) {
                                    System.out.println("- " + texto);
                                }
                            }
                            break;

                        case 4:
                            System.out.println("Cerrando cliente...");
                            break;

                        default:
                            System.out.println("Opción no válida.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println(
                            "Error: ingrese un número con formato válido."
                    );
                    opcion = 0;

                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                    opcion = 0;
                }

            } while (opcion != 4);

        } catch (RemoteException e) {

    System.err.println("\nERROR DE COMUNICACIÓN REMOTA");
    System.err.println(
            "No se pudo establecer comunicación con el servidor."
    );
    System.err.println("Detalle: " + e.getMessage());

    } catch (Exception e) {

    System.err.println("\nERROR INESPERADO");
    System.err.println("Detalle: " + e.getMessage());
    }
    }
}