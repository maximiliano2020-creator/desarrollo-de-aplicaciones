import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ParserMensajes {

    // SERIALIZACIÓN JSON
    public static String serializarJSON(Transaccion t) {

        return "{\"id\":" + t.getIdTransaccion()
                + ",\"origen\":\"" + escaparJSON(t.getOrigen())
                + "\",\"monto\":" + Double.toString(t.getMonto())
                + ",\"timestamp\":" + t.getTimestamp()
                + "}";
    }

    // DESERIALIZACIÓN JSON
    public static Transaccion deserializarJSON(String json) {

        String contenido = json.trim();

        if (!contenido.startsWith("{") || !contenido.endsWith("}")) {
            throw new IllegalArgumentException("JSON inválido");
        }

        String idTexto = extraerCampo(contenido, "id");
        String origen = extraerCampo(contenido, "origen");
        String montoTexto = extraerCampo(contenido, "monto");
        String timestampTexto = extraerCampo(contenido, "timestamp");

        int id = Integer.parseInt(idTexto);
        double monto = Double.parseDouble(montoTexto);
        long timestamp = Long.parseLong(timestampTexto);

        return new Transaccion(id, origen, monto, timestamp);
    }

    // SERIALIZACIÓN BINARIA
    public static byte[] serializarBinario(Transaccion t) throws IOException {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream salida = new DataOutputStream(bytes);

        salida.writeInt(t.getIdTransaccion());

        byte[] origenBytes = t.getOrigen().getBytes(StandardCharsets.UTF_8);
        salida.writeInt(origenBytes.length);
        salida.write(origenBytes);

        salida.writeDouble(t.getMonto());
        salida.writeLong(t.getTimestamp());

        salida.flush();

        return bytes.toByteArray();
    }

    // DESERIALIZACIÓN BINARIA
    public static Transaccion deserializarBinario(byte[] datos) throws IOException {

        DataInputStream entrada = new DataInputStream(
                new ByteArrayInputStream(datos)
        );

        int id = entrada.readInt();

        int longitudOrigen = entrada.readInt();

        if (longitudOrigen < 0 || longitudOrigen > datos.length - 24) {
            throw new IOException("Longitud de origen inválida");
        }

        byte[] origenBytes = new byte[longitudOrigen];
        entrada.readFully(origenBytes);

        String origen = new String(origenBytes, StandardCharsets.UTF_8);

        double monto = entrada.readDouble();
        long timestamp = entrada.readLong();

        if (entrada.available() != 0) {
            throw new IOException("Hay bytes adicionales en la transacción");
        }

        return new Transaccion(id, origen, monto, timestamp);
    }

    // ESCAPAR CARACTERES ESPECIALES EN JSON
    private static String escaparJSON(String texto) {

        return texto.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "\\r")
                    .replace("\t", "\\t");
    }

    // EXTRAER UN CAMPO DEL JSON
    private static String extraerCampo(String json, String campo) {

        String clave = "\"" + campo + "\"";
        int inicio = json.indexOf(clave);

        if (inicio == -1) {
            throw new IllegalArgumentException("Falta el campo: " + campo);
        }

        inicio = json.indexOf(':', inicio + clave.length()) + 1;

        while (inicio < json.length() && Character.isWhitespace(json.charAt(inicio))) {
            inicio++;
        }

        if (inicio >= json.length()) {
            throw new IllegalArgumentException("Valor vacío: " + campo);
        }

        if (json.charAt(inicio) == '"') {

            StringBuilder resultado = new StringBuilder();
            boolean escape = false;

            for (int i = inicio + 1; i < json.length(); i++) {

                char c = json.charAt(i);

                if (escape) {
                    switch (c) {
                        case '"': resultado.append('"'); break;
                        case '\\': resultado.append('\\'); break;
                        case 'n': resultado.append('\n'); break;
                        case 'r': resultado.append('\r'); break;
                        case 't': resultado.append('\t'); break;
                        default:
                            throw new IllegalArgumentException("Escape JSON inválido");
                    }
                    escape = false;

                } else if (c == '\\') {
                    escape = true;

                } else if (c == '"') {
                    return resultado.toString();

                } else {
                    resultado.append(c);
                }
            }

            throw new IllegalArgumentException("Cadena JSON sin cerrar");

        } else {

            int fin = inicio;

            while (fin < json.length()
                    && json.charAt(fin) != ','
                    && json.charAt(fin) != '}') {
                fin++;
            }

            return json.substring(inicio, fin).trim();
        }
    }
}
