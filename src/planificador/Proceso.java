package planificador;

public class Proceso {
    private final String nombre;
    private final int llegada;
    private final int rafaga;
    private final int ordenLlegada;

    private int restante;
    private EstadoProceso estado;
    private int fin;
    private int primeraCPU;

    public Proceso(String nombre, int llegada, int rafaga, int ordenLlegada) {
        this.nombre = nombre;
        this.llegada = llegada;
        this.rafaga = rafaga;
        this.ordenLlegada = ordenLlegada;
        reiniciar();
    }


    public void reiniciar() {
        this.restante = rafaga;
        this.estado = EstadoProceso.NUEVO;
        this.fin = -1;
        this.primeraCPU = -1;
    }

    public String getNombre()    { return nombre; }
    public int getLlegada()      { return llegada; }
    public int getRafaga()       { return rafaga; }
    public int getOrdenLlegada() { return ordenLlegada; }

    public int getRestante()              { return restante; }
    public void setRestante(int r)        { this.restante = r; }
    public EstadoProceso getEstado()      { return estado; }
    public void setEstado(EstadoProceso e){ this.estado = e; }
    public int getFin()                   { return fin; }
    public void setFin(int f)             { this.fin = f; }
    public int getPrimeraCPU()            { return primeraCPU; }
    public void setPrimeraCPU(int t)      { this.primeraCPU = t; }

    public int getRetorno()   { return fin - llegada; }
    public int getEspera()    { return getRetorno() - rafaga; }
    public int getRespuesta() { return primeraCPU - llegada; }

    @Override public String toString() { return nombre; }
}