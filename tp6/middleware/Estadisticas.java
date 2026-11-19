package tp6.middleware;
import java.io.Serializable;


public class Estadisticas implements Serializable {

    private double promedio;
    private double maximo;
    private double minimo;
    private double desviacionEstandar;

    public Estadisticas(double promedio, double maximo,
                        double minimo, double desviacionEstandar) {
        this.promedio = promedio;
        this.maximo = maximo;
        this.minimo = minimo;
        this.desviacionEstandar = desviacionEstandar;
    }

    public double getPromedio() {
        return promedio;
    }

    public double getMaximo() {
        return maximo;
    }

    public double getMinimo() {
        return minimo;
    }

    public double getDesviacionEstandar() {
        return desviacionEstandar;
    }
}