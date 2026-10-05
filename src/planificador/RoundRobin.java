package planificador;

import java.util.Deque;
import java.util.List;

public class RoundRobin extends Planificador {
    private final int quantum;

    public RoundRobin(List<Proceso> procesos, int quantum) {
        super(procesos);
        if (quantum <= 0) throw new IllegalArgumentException("El quantum debe ser > 0.");
        this.quantum = quantum;
    }

    @Override public String getNombre() { return "Round Robin (q=" + quantum + ")"; }

    @Override
    protected boolean debeDesalojarPorQuantum(Proceso enCPU, int qUsado, Deque<Proceso> cola) {
        return qUsado >= quantum && !cola.isEmpty();
    }

    @Override
    protected boolean debeRenovarQuantum(Proceso enCPU, int qUsado) {
        return qUsado >= quantum;
    }
}
