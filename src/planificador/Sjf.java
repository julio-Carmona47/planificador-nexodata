package planificador;

import java.util.Deque;
import java.util.List;

public class Sjf extends Planificador {
    public Sjf(List<Proceso> procesos) { super(procesos); }
    @Override public String getNombre() { return "SJF (sin desalojo)"; }

    @Override
    protected Proceso seleccionar(Deque<Proceso> cola) {
        Proceso mejor = null;
        for (Proceso p : cola) if (mejor == null || esMenor(p, mejor)) mejor = p;
        if (mejor != null) cola.remove(mejor);
        return mejor;
    }

    private boolean esMenor(Proceso a, Proceso b) {
        if (a.getRafaga() != b.getRafaga())     return a.getRafaga() < b.getRafaga();
        if (a.getLlegada() != b.getLlegada())   return a.getLlegada() < b.getLlegada();
        return a.getOrdenLlegada() < b.getOrdenLlegada();
    }
}
