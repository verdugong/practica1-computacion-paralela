/**
 * ParallelProcessor.java
 * 
 * la división del texto se hace sin cortar palabras.
 * si el punto de corte cae en medio de una palabra, se mueve al
 * siguiente espacio en blanco. Así cada hilo procesa palabras completas.
 */
public class ParallelProcessor {

    /**
     * se procesa el texto en paralelo usando el número de hilos indicado.
     * 
     * @param texto          todo el contenido del archivo
     * @param numHilos       número de hilos a usar (2 o 4, según la práctica)
     * @param palabraBuscada palabra exacta a contar
     * @param subcadena      subcadena a buscar
     * @return arreglo con [totalPalabras, apariciones, palabrasConSubcadena]
     */
    public int[] procesar(String texto, int numHilos, String palabraBuscada, String subcadena) {
        // se divide el texto en fragmentos sin cortar palabras
        String[] fragmentos = dividirTexto(texto, numHilos);

        // se crean los hilos
        WorkerThread[] hilos = new WorkerThread[fragmentos.length];
        for (int i = 0; i < fragmentos.length; i++) {
            hilos[i] = new WorkerThread("Hilo-" + (i + 1), fragmentos[i], palabraBuscada, subcadena);
        }

        // se arrancan todos los hilos
        // start() lanza la ejecución de run() en un hilo separado
        for (WorkerThread hilo : hilos) {
            hilo.start();
        }

        // se espera a que todos terminen
        // join() bloquea el hilo principal hasta que ese hilo termine
        // sin join(), podríamos intentar leer resultados antes de que estén listos
        for (WorkerThread hilo : hilos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                System.err.println("Hilo interrumpido: " + hilo.getName());
                e.printStackTrace();
            }
        }

        // se combinan los resultados parciales
        int totalPalabras = 0;
        int apariciones = 0;
        int palabrasConSubcadena = 0;

        for (WorkerThread hilo : hilos) {
            totalPalabras += hilo.getTotalPalabras();
            apariciones += hilo.getAparicionesPalabra();
            palabrasConSubcadena += hilo.getPalabrasConSubcadena();
        }

        return new int[]{totalPalabras, apariciones, palabrasConSubcadena};
    }

    /**
     * se divide el texto en N fragmentos sin cortar palabras a la mitad.
     * 
     * calculamos el tamaño aproximado de cada fragmento (longitud total / numHilos)
     * el punto de corte puede caer en medio de una palabra
     * se ajusta buscando el siguiente espacio en blanco
     * así cada fragmento empieza y termina en límites de palabra
     * 
     * @param texto    el texto completo
     * @param numParts número de fragmentos (= número de hilos)
     * @return arreglo de Strings con los fragmentos
     */
    private String[] dividirTexto(String texto, int numParts) {
        String[] fragmentos = new String[numParts];
        int longitud = texto.length();
        int tamanioPorParte = longitud / numParts;

        int inicio = 0;

        for (int i = 0; i < numParts; i++) {
            int fin;

            if (i == numParts - 1) {
                // el último fragmento toma todo lo que queda para no perder caracteres por el redondeo de la división entera
                fin = longitud;
            } else {
                // calculamos el punto de corte aproximado
                fin = inicio + tamanioPorParte;

                // ajustamos para no cortar una palabra y avanzamos hasta encontrar un espacio en blanco
                while (fin < longitud && !Character.isWhitespace(texto.charAt(fin))) {
                    fin++;
                }
            }

            fragmentos[i] = texto.substring(inicio, fin);
            inicio = fin;
        }

        return fragmentos;
    }
}
