package planificador;

import java.util.List;

public class Fcfs extends Planificador {
    public Fcfs(List<Proceso> procesos) { super(procesos); }
    @Override public String getNombre() { return "FCFS"; }
}