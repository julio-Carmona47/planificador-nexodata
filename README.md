# Simulador de planificación · NexoData

> PSP · Tema 2 · Procesos · 2.º A DAM · **[Tu nombre]**
> Sustituye todo lo que aparece entre corchetes y borra estas indicaciones cuando lo completes.

## 1. Qué hace

[Tres o cuatro líneas.]

## 2. Cómo compilar y ejecutar

**Desde la terminal**, en la carpeta del proyecto:

```bash
javac -encoding UTF-8 -d out src/planificador/*.java
java -cp out planificador.Main datos/ejemplo_clase.csv rr 2 --traza
```

**Desde IntelliJ IDEA:** [explica los pasos que has seguido: JDK, configuración de ejecución, argumentos y *working directory*.]

![Mi programa en ejecución](capturas/[nombre-de-tu-captura].png)

## 3. Diseño

[Qué clases hay, qué hace cada una y por qué las has organizado así.]

## 4. Verificación (tarea 4)

### 4.1 verificacion.csv resuelto a mano
[FCFS y Round Robin q=2: diagrama de Gantt, cola de listos en cada decisión (RR), métricas y cambios de contexto. Puedes usar tablas Markdown o una foto de tu hoja.]

### 4.2 Comparación con el programa
[¿Coincide? Si no coincidió en algún momento: quién se equivocaba, qué corregiste y enlace al commit.]
### 4.2 Comparación con el programa

Tras resolver el ejercicio a mano, ejecuté:

    java -cp out planificador.Main datos/verificacion.csv fcfs
    java -cp out planificador.Main datos/verificacion.csv rr 2

**FCFS**: coincide exactamente con mi resolución manual. Gantt
`P1 P1 P1 P1 P1 P1 P2 P2 P2 P2 P3 P3 P4 P4 P4 P4 P4 P5`, medias
retorno 10.00, espera 6.40, respuesta 6.40, cambios de contexto 4.

**RR q=2**: coincide exactamente con mi resolución manual. Gantt
`P1 P1 P2 P2 P3 P3 P1 P1 P4 P4 P2 P2 P5 P1 P1 P4 P4 P4`, medias
retorno 10.20, espera 6.60, respuesta 2.60, cambios de contexto 8.
Comprobé que se respeta la Regla 3: en t=2, P3 entra en la cola
ANTES de que P1 sea desalojado. Y la Regla 4: cuando un proceso
agota el quantum y la cola está vacía, sigue en CPU sin cambio
de contexto.
### 4.3 hueco.csv
[Qué ocurre entre los instantes 2 y 5 y cómo lo refleja tu diagrama.]

## 5. Análisis y recomendación a NexoData (tarea 5)

| nocturno.csv | Retorno medio | Espera media | Respuesta media | Cambios de contexto |
|---|---|---|---|---|
| FCFS         |      14.00    |    10.00     |      10.00      |        5            |
| SJF          |      11.67    |    7.67      |      7.67       |        5            |
| RR q=1       |      12.33    |    8.33      |      1.50       |        22           |
| RR q=2       |      12.17    |    8.17      |      2.83       |        11           |
| RR q=4       |      14.50    |    10.50     |      6.17       |        8            |

1. **¿Qué algoritmo da la menor espera media?** SJF, con 7.67 minutos.
   Pasa antes los trabajos cortos (LOGS=1, INFORME=2, AVISOS=2) y deja
   para el final los largos (BACKUP=6, RUTAS=4). Como esos cortos no
   tienen que esperar detrás de FACTURA, su espera cae en picado y tira
   de la media hacia abajo.

2. **En FCFS, LOGS espera 14 minutos.** LOGS solo necesita 1 minuto pero
   llega en t=3, cuando FACTURA (ráfaga 9) aún está en CPU y BACKUP
   (ráfaga 6) espera en cola. Efecto **convoy**: un trabajo muy largo
   bloquea la CPU y todos los que llegan detrás heredan su penalización
   en cadena. El proceso culpable es FACTURA.

3. **En SJF el peor parado es BACKUP** (espera 16, retorno 22). Si cada
   pocos minutos llegasen trabajos cortos nuevos, BACKUP nunca llegaría
   a ejecutarse: eso es **inanición (starvation)**. Se evita con
   **envejecimiento (aging)**: subir progresivamente la prioridad de los
   procesos que llevan mucho en cola.

4. **Al bajar el quantum de 4 a 1**, la respuesta media cae de 6.17 a 1.50
   (mejora enorme para el usuario interactivo) pero los cambios de
   contexto se disparan de 8 a 22. Un quantum de 1 minuto no sería
   realista en un SO real porque el cambio de contexto tiene coste: con
   quantum de 1 ms el sistema pasaría más tiempo cambiando de proceso
   que ejecutando.

5. **RR q=4 (10.50) sale ligeramente peor que FCFS (10.00)** en espera
   media. Sorprende, pero tiene sentido: RR reparte la penalización entre
   todos, así que FACTURA (que en FCFS esperaba 0) ahora espera 15. Los
   cortos mejoran algo, pero no lo suficiente para compensar. FCFS es
   mejor en media cuando los largos van primero; RR gana en el peor caso.

6. **El simulador nunca usa Bloqueado** porque solo modela ráfagas de CPU
   puras, sin E/S. En un SO real (`Administrador de tareas → Detalles` en
   Windows, o `ps -eo pid,stat,comm` en Linux) aparecen estados como
   `Running` (equivalente a mi LISTO+EJECUCION), `Suspended` (equivalente
   a mi BLOQUEADO, no simulado) y `Stopped`. Mi modelo es un subconjunto
   simplificado: solo me interesa el reparto de CPU, no el ciclo completo
   de vida de un proceso con E/S.

![Estados de procesos en el SO](capturas/estados_so.png)

### Recomendación

Para los trabajos nocturnos por lotes de NexoData usaría **SJF con
envejecimiento**: da la menor espera media (7.67) y la menor media de
retorno (11.67), y el envejecimiento evita que BACKUP se quede sin
ejecutar cuando lleguen trabajos cortos nuevos. Si el servidor tuviera
usuarios conectados (interactivos), cambiaría a **Round Robin con
quantum ≈ 4** o al CFS de Linux: sacrifica algo de espera media pero
reduce la respuesta media a unos pocos minutos, que es lo que percibe
el usuario.
