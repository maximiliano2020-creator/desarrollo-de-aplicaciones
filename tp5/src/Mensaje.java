import java.time.LocalDateTime;

public final class Mensaje {

    private final String sensorId;
    private final double temperatura;
    private final LocalDateTime timestamp;

    public Mensaje(String sensorId, double temperatura) {
        this.sensorId = sensorId;
        this.temperatura = temperatura;
        this.timestamp = LocalDateTime.now();
    }

    public String getSensorId() {
        return sensorId;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}