package planificador;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public abstract class Planificador {

    protected final List<Proceso> procesos;

    public Planificador(List<Proceso> procesos) { this.procesos = procesos; }

    public abstract String getNombre();

    /** Por defecto FIFO (sirve para FCFS y RR). SJF lo sobrescribe. */
    protected Proceso seleccionar(Deque<Proceso> cola) {
        return cola.pollFirst();
    }

    /** ¿Desalojar por agotar quantum? Por defecto no (FCFS/SJF no apropiativos). */
    protected boolean debeDesalojarPorQuantum(Proceso enCPU, int qUsado, Deque<Proceso> cola) {
        return false;
    }

    /** ¿Renovar quantum sin desalojar? (Regla 4, solo RR). */
    protected boolean debeRenovarQuantum(Proceso enCPU, int qUsado) {
        return false;
    }

    public Resultado simular() {
        // ¡Clave! Reiniciar todos los procesos para no arrastrar estado previo
        for (Proceso p : procesos) p.reiniciar();

        Resultado r = new Resultado(getNombre());
        Deque<Proceso> cola = new ArrayDeque<>();
        Proceso enCPU = null;
        Proceso ultimoEnCPU = null;
        int qUsado = 0;
        int t = 0;

        while (hayPendientes()) {

            // ---- Paso 1: llegadas en t (orden de fichero, Regla 2) ----
            for (Proceso p : procesos) {
                if (p.getEstado() == EstadoProceso.NUEVO && p.getLlegada() == t) {
                    p.setEstado(EstadoProceso.LISTO);
                    cola.addLast(p);
                    r.traza(t, p, EstadoProceso.NUEVO, EstadoProceso.LISTO, "llega al sistema");
                }
            }

            // ---- Paso 2: desalojo por quantum (Regla 3 ya aplicada en el paso 1) ----
            if (enCPU != null) {
                if (debeDesalojarPorQuantum(enCPU, qUsado, cola)) {
                    enCPU.setEstado(EstadoProceso.LISTO);
                    cola.addLast(enCPU);
                    r.traza(t, enCPU, EstadoProceso.EJECUCION, EstadoProceso.LISTO, "agota el quantum");
                    enCPU = null;
                    qUsado = 0;
                } else if (debeRenovarQuantum(enCPU, qUsado)) {
                    // Regla 4: nadie esperando → quantum nuevo, sin cambio de contexto
                    qUsado = 0;
                }
            }

            // ---- Paso 3: si CPU libre, elegir ----
            if (enCPU == null && !cola.isEmpty()) {
                Proceso elegido = seleccionar(cola);
                enCPU = elegido;
                enCPU.setEstado(EstadoProceso.EJECUCION);
                if (enCPU.getPrimeraCPU() < 0) enCPU.setPrimeraCPU(t);

                if (ultimoEnCPU != null && ultimoEnCPU != enCPU) r.incCambiosContexto();
                ultimoEnCPU = enCPU;

                r.traza(t, enCPU, EstadoProceso.LISTO, EstadoProceso.EJECUCION, "el planificador lo elige");
            }

            // ---- Paso 4: ejecutar una unidad ----
            if (enCPU != null) {
                r.getGantt().add(enCPU);
                enCPU.setRestante(enCPU.getRestante() - 1);
                qUsado++;
                if (enCPU.getRestante() == 0) {
                    enCPU.setEstado(EstadoProceso.TERMINADO);
                    enCPU.setFin(t + 1);     // Regla 1
                    r.traza(t + 1, enCPU, EstadoProceso.EJECUCION, EstadoProceso.TERMINADO, "completa su ráfaga");
                    enCPU = null;
                    qUsado = 0;
                }
            } else {
                r.getGantt().add(null);       // CPU ociosa
            }

            t++;
            if (t > 1_000_000) throw new IllegalStateException("Simulación demasiado larga.");
        }

        r.setProcesos(procesos);
        return r;
    }

    private boolean hayPendientes() {
        for (Proceso p : procesos) if (p.getEstado() != EstadoProceso.TERMINADO) return true;
        return false;
    }
}