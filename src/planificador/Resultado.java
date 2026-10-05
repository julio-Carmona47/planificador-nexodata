package planificador;

import java.util.ArrayList;
import java.util.List;

public class Resultado {
    private final String nombre;
    private final List<Proceso> gantt = new ArrayList<>();
    private final List<String> trazas = new ArrayList<>();
    private int cambiosContexto = 0;
    private List<Proceso> procesos;

    public Resultado(String nombre) { this.nombre = nombre; }

    public String getNombre()          { return nombre; }
    public List<Proceso> getGantt()    { return gantt; }
    public List<String> getTrazas()    { return trazas; }
    public int getCambiosContexto()    { return cambiosContexto; }
    public void incCambiosContexto()   { cambiosContexto++; }
    public List<Proceso> getProcesos() { return procesos; }
    public void setProcesos(List<Proceso> p) { this.procesos = p; }

    public void traza(int t, Proceso p, EstadoProceso de, EstadoProceso a, String motivo) {
        trazas.add(String.format("t=%-3d %-10s %s -> %s (%s)",
                t, p.getNombre(), de, a, motivo));
    }
}