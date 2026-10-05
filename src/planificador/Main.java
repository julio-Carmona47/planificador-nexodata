package planificador;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso: java planificador.Main <fichero.csv> <fcfs|sjf|rr|todos> [quantum] [--traza]");
            System.exit(1);
        }
        Path fichero = Path.of(args[0]);
        String algoritmo = args[1].toLowerCase();
        boolean traza = List.of(args).contains("--traza");
        int quantum = 2;
        if (args.length >= 3 && !args[2].startsWith("--")) {
            try {
                quantum = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("El quantum debe ser un número entero: " + args[2]);
                System.exit(1);
            }
        }
        if (!Files.exists(fichero)) {
            System.err.println("No encuentro el fichero " + fichero.toAbsolutePath()
                    + "\nComprueba el «Working directory» de la configuración de ejecución.");
            System.exit(1);
        }
        if (!List.of("fcfs", "sjf", "rr", "todos").contains(algoritmo)) {
            System.err.println("Algoritmo desconocido: " + algoritmo + " (usa fcfs, sjf, rr o todos)");
            System.exit(1);
        }

        // --- A partir de aquí, código del alumno ---
        List<Proceso> procesos;
        try {
            procesos = LectorCSV.leer(fichero);
        } catch (Exception e) {
            System.err.println("Error leyendo " + fichero + ": " + e.getMessage());
            System.exit(1);
            return;
        }
        if (procesos.isEmpty()) {
            System.err.println("El fichero no contiene procesos válidos.");
            System.exit(1);
        }

        if (quantum <= 0) {
            System.err.println("El quantum debe ser > 0: " + quantum);
            System.exit(1);
        }

        List<Planificador> algoritmos = new java.util.ArrayList<>();
        switch (algoritmo) {
            case "fcfs" -> algoritmos.add(new Fcfs(procesos));
            case "sjf"  -> algoritmos.add(new Sjf(procesos));
            case "rr"   -> algoritmos.add(new RoundRobin(procesos, quantum));
            case "todos" -> {
                algoritmos.add(new Fcfs(procesos));
                algoritmos.add(new Sjf(procesos));
                algoritmos.add(new RoundRobin(procesos, quantum));
            }
        }

        boolean primero = true;
        for (Planificador alg : algoritmos) {
            if (!primero) System.out.println();
            primero = false;
            Informe.imprimir(alg.simular(), traza);
        }
    }
}