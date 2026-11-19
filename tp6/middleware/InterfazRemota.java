package tp6.middleware;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfazRemota extends Remote {

    void registrarConexion() throws RemoteException;

    Estadisticas calcularEstadisticas(double[] numeros)
            throws RemoteException;

    boolean validarDatos(String dato)
            throws RemoteException;

    String[] filtrarContenido(String[] textos, String patron)
            throws RemoteException;
}