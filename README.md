# Práctica 1 - Procesamiento Secuencial y Paralelo de Textos con Java Threads

## Descripción
Aplicación Java que analiza el contenido del libro *Don Quijote de la Mancha* mediante procesamiento secuencial y paralelo utilizando la clase `Thread`.

### Funcionalidades
- Conteo total de palabras del documento
- Conteo de apariciones de una palabra específica ("Quijote")
- Conteo de palabras que contienen una subcadena ("ción")
- Comparación de tiempos de ejecución: secuencial vs paralelo (2 y 4 hilos)

## Estructura del proyecto
```
practica1/
├── src/
│   ├── Main.java                 # Punto de entrada y medición de tiempos
│   ├── TextProcessor.java        # Lógica de procesamiento de texto
│   ├── SequentialProcessor.java  # Procesamiento secuencial
│   ├── ParallelProcessor.java    # Procesamiento paralelo (orquestador)
│   └── WorkerThread.java         # Hilo trabajador (extiende Thread)
├── data/
│   └── quijote.txt               # Texto del Quijote (descargar de Project Gutenberg)
└── README.md
```

## Requisitos
- Java JDK 8 o superior

## Instrucciones de ejecución

### 1. Descargar el texto
Descargar el archivo TXT de *Don Quijote de la Mancha* desde [Project Gutenberg](https://www.gutenberg.org/ebooks/2000) y colocarlo en la carpeta `data/` con el nombre `quijote.txt`.

### 2. Compilar
```bash
cd practica1
javac -d bin src/*.java
```

### 3. Ejecutar
```bash
java -cp bin Main
```

## Resultados esperados
El programa mostrará:
1. Verificación de que los resultados secuenciales y paralelos coinciden
2. Tiempos de ejecución promedio (5 repeticiones) para cada configuración
3. Tabla comparativa con el cálculo de aceleración (Speedup = Ts / Tp)

## Autor
Sebastian Verdugo - Universidad Politécnica Salesiana - 8vo Semestre
