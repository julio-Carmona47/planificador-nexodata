package planificador;

import java.util.List;
import java.util.Locale;

public class Informe {

    public static void imprimir(Resultado r, boolean mostrarTraza) {
        List<Proceso> procesos = r.getProcesos();
        System.out.println("=== " + r.getNombre() + " ===");
        imprimirGantt(r);
        System.out.println();
        imprimirTabla(procesos);
        System.out.println();
        System.out.printf(Locale.US, "Medias: retorno %.2f | espera %.2f | respuesta %.2f%n",
                media(procesos, 'r'), media(procesos, 'e'), media(procesos, 'p'));
        System.out.println("Cambios de contexto: " + r.getCambiosContexto());

        if (mostrarTraza) {
            System.out.println();
            System.out.println("Traza de estados:");
            for (String l : r.getTrazas()) System.out.println(l);
        }
    }

    private static void imprimirGantt(Resultado r) {
        List<Proceso> gantt = r.getGantt();
        int n = gantt.size();
        if (n == 0) { System.out.println("Gantt: (vacío)"); return; }

        int w = 4;
        for (Proceso p : gantt) if (p != null) w = Math.max(w, p.getNombre().length() + 1);
        w = Math.min(w, 10);

        StringBuilder h = new StringBuilder(String.format("%-4s", "t"));
        StringBuilder c = new StringBuilder(String.format("%-4s", "CPU"));
        for (int i = 0; i < n; i++) {
            String etiq = gantt.get(i) == null ? "-" : gantt.get(i).getNombre();
            if (etiq.length() > w) etiq = etiq.substring(0, w);
            h.append(String.format("%-" + w + "s", i));
            c.append(String.format("%-" + w + "s", etiq));
        }
        System.out.println("Gantt:");
        System.out.println(h);
        System.out.println(c);
    }

    private static void imprimirTabla(List<Proceso> procesos) {
        System.out.printf("%-10s %8s %8s %6s %8s %8s %10s%n",
                "Proceso", "Llegada", "Ráfaga", "Fin", "Retorno", "Espera", "Respuesta");
        for (Proceso p : procesos)
            System.out.printf("%-10s %8d %8d %6d %8d %8d %10d%n",
                    p.getNombre(), p.getLlegada(), p.getRafaga(),
                    p.getFin(), p.getRetorno(), p.getEspera(), p.getRespuesta());
    }

    private static double media(List<Proceso> procesos, char c) {
        return procesos.stream().mapToInt(p -> switch (c) {
            case 'r' -> p.getRetorno();
            case 'e' -> p.getEspera();
            default  -> p.getRespuesta();
        }).average().orElse(0);
    }
}