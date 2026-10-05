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
| FCFS | | | | |
| SJF | | | | |
| RR q=1 | | | | |
| RR q=2 | | | | |
| RR q=4 | | | | |

1. [Respuesta 1]
2. [Respuesta 2]
3. [Respuesta 3]
4. [Respuesta 4]
5. [Respuesta 5]
6. [Respuesta 6, con la captura comentada de los estados de tu sistema]

### Recomendación
[De 5 a 10 líneas.]
