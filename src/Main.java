import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Los tiempos se miden SOLO del procesamiento,
 */
public class Main {

    //parámetros de búsqueda
    private static final String PALABRA_BUSCADA = "Quijote";
    private static final String SUBCADENA_BUSCADA = "ción";
    private static final int NUM_REPETICIONES = 5;
    private static final int FACTOR_AMPLIACION = 100;

    public static void main(String[] args) {
        // cargar el archivo en memoria
        String rutaArchivo = "data/quijote.txt";
        String textoOriginal;

        System.out.println("===========================================");
        System.out.println("  PRÁCTICA 1 - Procesamiento de Textos");
        System.out.println("  Computación Paralela");
        System.out.println("===========================================\n");

        try {
            textoOriginal = new String(Files.readAllBytes(Paths.get(rutaArchivo)), "UTF-8");
            System.out.println("archivo cargado exitosamente: " + rutaArchivo);
            System.out.println("tamaño del texto original: " + textoOriginal.length() + " caracteres\n");
        } catch (IOException e) {
            System.err.println("error al leer el archivo: " + rutaArchivo);
            System.err.println("asegurar que el archivo exista en la carpeta 'data/'");
            e.printStackTrace();
            return;
        }

        // generar la versión ampliada x100
        System.out.println("generando versión ampliada (x" + FACTOR_AMPLIACION + ")...");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < FACTOR_AMPLIACION; i++) {
            sb.append(textoOriginal);
            if (i < FACTOR_AMPLIACION - 1) {
                sb.append(" "); // espacio entre copias para no pegar palabras
            }
        }
        String textoAmpliado = sb.toString();
        System.out.println("tamaño del texto ampliado: " + textoAmpliado.length() + " caracteres\n");

        // verificar que los resultados coinciden
        System.out.println("verificación de resultados\n");
        System.out.println("palabra buscada: \"" + PALABRA_BUSCADA + "\"");
        System.out.println("subcadena buscada: \"" + SUBCADENA_BUSCADA + "\"\n");

        verificarResultados(textoOriginal);

        // ejecutar pruebas de rendimiento
        System.out.println("pruebas de rendimiento\n");
        System.out.println("cada prueba se repite " + NUM_REPETICIONES + " veces para calcular el promedio.\n");

        //resultados para la tabla final
        // [conjuntoDatos][numHilos] -> tiempoPromedio en ms
        double[][] tiempos = new double[2][3]; // 2 conjuntos × 3 configs (1, 2, 4 hilos)

        // texto original
        System.out.println("========== TEXTO ORIGINAL ==========\n");
        tiempos[0][0] = ejecutarPruebaSecuencial(textoOriginal, "original");
        tiempos[0][1] = ejecutarPruebaParalela(textoOriginal, 2, "original");
        tiempos[0][2] = ejecutarPruebaParalela(textoOriginal, 4, "original");

        // texto ampliado
        System.out.println("\n========== TEXTO AMPLIADO (x" + FACTOR_AMPLIACION + ") ==========\n");
        tiempos[1][0] = ejecutarPruebaSecuencial(textoAmpliado, "ampliado");
        tiempos[1][1] = ejecutarPruebaParalela(textoAmpliado, 2, "ampliado");
        tiempos[1][2] = ejecutarPruebaParalela(textoAmpliado, 4, "ampliado");

        // mostrar tabla comparativa
        System.out.println("\n============================================");
        System.out.println("        TABLA COMPARATIVA DE RESULTADOS");
        System.out.println("============================================\n");

        System.out.printf("%-15s %-20s %-25s %-15s%n",
                "conjunto", "hilos", "tiempo promedio (ms)", "aceleración");
        System.out.println("------------------------------------------------------------------------");

        String[] conjuntos = {"original", "ampliado"};
        int[] hilosConfig = {1, 2, 4};

        for (int i = 0; i < 2; i++) {
            double tiempoSecuencial = tiempos[i][0];
            for (int j = 0; j < 3; j++) {
                String hilosStr = hilosConfig[j] == 1 ? "1 (secuencial)" : String.valueOf(hilosConfig[j]);
                double speedup = tiempoSecuencial / tiempos[i][j];
                System.out.printf("%-15s %-20s %-25.4f %-15.4f%n",
                        conjuntos[i], hilosStr, tiempos[i][j], speedup);
            }
            System.out.println("------------------------------------------------------------------------");
        }

        System.out.println("\nprocesamiento completado.");
    }

    /**
     * verificar que los resultados secuenciales y paralelos (2 y 4 hilos) coincidan.
     * si no coinciden, hay un bug en la división del texto.
     */
    private static void verificarResultados(String texto) {
        SequentialProcessor secuencial = new SequentialProcessor();
        ParallelProcessor paralelo = new ParallelProcessor();

        int[] resultadoSec = secuencial.procesar(texto, PALABRA_BUSCADA, SUBCADENA_BUSCADA);
        int[] resultadoPar2 = paralelo.procesar(texto, 2, PALABRA_BUSCADA, SUBCADENA_BUSCADA);
        int[] resultadoPar4 = paralelo.procesar(texto, 4, PALABRA_BUSCADA, SUBCADENA_BUSCADA);

        System.out.println("resultados secuencial:");
        imprimirResultados(resultadoSec);

        System.out.println("resultados paralelo (2 hilos):");
        imprimirResultados(resultadoPar2);

        System.out.println("resultados paralelo (4 hilos):");
        imprimirResultados(resultadoPar4);

        // verificar coincidencia
        boolean coinciden = true;
        for (int i = 0; i < 3; i++) {
            if (resultadoSec[i] != resultadoPar2[i] || resultadoSec[i] != resultadoPar4[i]) {
                coinciden = false;
                break;
            }
        }

        if (coinciden) {
            System.out.println("✓ VERIFICACIÓN EXITOSA: todos los resultados coinciden.\n");
        } else {
            System.out.println("✗ ERROR: los resultados NO coinciden. Revisar la división del texto.\n");
        }
    }

    /**
     * se imprimen los resultados de un procesamiento.
     */
    private static void imprimirResultados(int[] resultados) {
        System.out.println("  total de palabras: " + resultados[0]);
        System.out.println("  apariciones de \"" + PALABRA_BUSCADA + "\": " + resultados[1]);
        System.out.println("  palabras con \"" + SUBCADENA_BUSCADA + "\": " + resultados[2]);
        System.out.println();
    }

    /**
     * se ejecuta la prueba secuencial NUM_REPETICIONES veces y retorna el tiempo promedio en ms.
     * 
     * usamos System.nanoTime() porque es más preciso que System.currentTimeMillis() para medir intervalos cortos.
     */
    private static double ejecutarPruebaSecuencial(String texto, String conjunto) {
        SequentialProcessor processor = new SequentialProcessor();
        long[] tiempos = new long[NUM_REPETICIONES];

        for (int i = 0; i < NUM_REPETICIONES; i++) {
            long inicio = System.nanoTime();
            processor.procesar(texto, PALABRA_BUSCADA, SUBCADENA_BUSCADA);
            long fin = System.nanoTime();
            tiempos[i] = fin - inicio;
        }

        double promedioMs = calcularPromedioMs(tiempos);
        System.out.printf("[%s] secuencial - tiempo promedio: %.4f ms%n", conjunto, promedioMs);
        return promedioMs;
    }

    /**
     * se ejecuta la prueba paralela NUM_REPETICIONES veces y retorna el tiempo promedio en ms.
     */
    private static double ejecutarPruebaParalela(String texto, int numHilos, String conjunto) {
        ParallelProcessor processor = new ParallelProcessor();
        long[] tiempos = new long[NUM_REPETICIONES];

        for (int i = 0; i < NUM_REPETICIONES; i++) {
            long inicio = System.nanoTime();
            processor.procesar(texto, numHilos, PALABRA_BUSCADA, SUBCADENA_BUSCADA);
            long fin = System.nanoTime();
            tiempos[i] = fin - inicio;
        }

        double promedioMs = calcularPromedioMs(tiempos);
        System.out.printf("[%s] paralelo (%d hilos) - tiempo promedio: %.4f ms%n",
                conjunto, numHilos, promedioMs);
        return promedioMs;
    }

    /**
     * se calcula el promedio de un arreglo de tiempos en nanosegundos y se convierte a milisegundos.
     */
    private static double calcularPromedioMs(long[] tiemposNano) {
        long suma = 0;
        for (long t : tiemposNano) {
            suma += t;
        }
        // dividimos entre 1_000_000.0 para convertir nanosegundos a milisegundos
        return (suma / (double) tiemposNano.length) / 1_000_000.0;
    }
}
