package tp6.servidor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import tp6.middleware.Estadisticas;
import tp6.middleware.InterfazRemota;

public class ProcesadorImpl extends UnicastRemoteObject
        implements InterfazRemota {

    public ProcesadorImpl() throws RemoteException {
        super(1100);
    }

    // Obtiene la dirección del cliente y registra la operación.
    private void registrarOperacion(String operacion) {

        try {
            String ipCliente = RemoteServer.getClientHost();

            System.out.println(
                    "[" + LocalDateTime.now() + "] "
                    + "[CLIENTE: " + ipCliente + "] "
                    + "[OPERACIÓN] " + operacion
            );

        } catch (ServerNotActiveException e) {
            System.out.println(
                    "[OPERACIÓN] " + operacion
                    + " (cliente no identificado)"
            );
        }
    }

    @Override
    public void registrarConexion() throws RemoteException {
        try {
            String ipCliente = RemoteServer.getClientHost();

            System.out.println(
                    "[" + LocalDateTime.now() + "] "
                    + "[CONEXIÓN] Cliente conectado desde: "
                    + ipCliente
            );

        } catch (ServerNotActiveException e) {
            System.out.println(
                    "[CONEXIÓN] Cliente conectado (IP no identificada)."
            );
        }
    }

    @Override
    public Estadisticas calcularEstadisticas(double[] numeros)
            throws RemoteException {

        registrarOperacion("Cálculo de estadísticas solicitado.");

        if (numeros == null || numeros.length == 0) {
            throw new IllegalArgumentException(
                    "La lista de números no puede estar vacía."
            );
        }

        double suma = 0;
        double maximo = numeros[0];
        double minimo = numeros[0];

        for (double numero : numeros) {
            suma += numero;

            if (numero > maximo) {
                maximo = numero;
            }

            if (numero < minimo) {
                minimo = numero;
            }
        }

        double promedio = suma / numeros.length;

        double sumaCuadrados = 0;

        for (double numero : numeros) {
            sumaCuadrados += Math.pow(numero - promedio, 2);
        }

        double desviacionEstandar =
                Math.sqrt(sumaCuadrados / numeros.length);

        return new Estadisticas(
                promedio,
                maximo,
                minimo,
                desviacionEstandar
        );
    }

    @Override
    public boolean validarDatos(String dato)
            throws RemoteException {

        registrarOperacion("Validación de DNI solicitada.");

        if (dato == null) {
            return false;
        }

        return dato.matches("\\d{7,8}");
    }

    @Override
    public String[] filtrarContenido(String[] textos, String patron)
            throws RemoteException {

        registrarOperacion("Filtrado de contenido solicitado.");

        if (textos == null || patron == null) {
            return new String[0];
        }

        ArrayList<String> resultados = new ArrayList<>();

        for (String texto : textos) {
            if (texto != null &&
                    texto.toLowerCase().contains(patron.toLowerCase())) {
                resultados.add(texto);
            }
        }

        return resultados.toArray(new String[0]);
    }
}