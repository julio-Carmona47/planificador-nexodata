package planificador;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LectorCSV {

    public static List<Proceso> leer(Path fichero) throws IOException {
        List<String> lineas = Files.readAllLines(fichero);
        List<Proceso> procesos = new ArrayList<>();
        int orden = 0;

        for (int i = 0; i < lineas.size(); i++) {
            String linea = lineas.get(i).trim();
            if (linea.isEmpty() || linea.startsWith("#")) continue;

            String[] campos = linea.split(";");
            if (campos.length != 3)
                throw new IllegalArgumentException(
                        "Línea " + (i + 1) + " mal formada (3 campos esperados): " + linea);

            String nombre = campos[0].trim();
            if (nombre.isEmpty())
                throw new IllegalArgumentException("Línea " + (i + 1) + ": nombre vacío.");

            int llegada, rafaga;
            try {
                llegada = Integer.parseInt(campos[1].trim());
                rafaga  = Integer.parseInt(campos[2].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Línea " + (i + 1) + ": llegada/ráfaga no son enteros: " + linea);
            }
            if (llegada < 0)
                throw new IllegalArgumentException("Línea " + (i + 1) + ": llegada negativa (" + llegada + ").");
            if (rafaga <= 0)
                throw new IllegalArgumentException("Línea " + (i + 1) + ": ráfaga debe ser > 0 (era " + rafaga + ").");

            procesos.add(new Proceso(nombre, llegada, rafaga, orden++));
        }
        return procesos;
    }
}