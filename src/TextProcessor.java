/**
 * TextProcessor.java
 * 
 * la lógica de procesamiento de texto.
 * recibe un bloque de texto (String) y calcula:
 *  total de palabras
 *  pariciones de una palabra exacta
 *  palabras que contienen una subcadena
 * 
 * esta clase no maneja hilos, solo procesa texto puro.
 * la usan tanto la versión secuencial como cada hilo en la versión paralela.
 */
public class TextProcessor {

    private int totalPalabras;
    private int aparicionesPalabra;
    private int palabrasConSubcadena;

    /**
     * 
     * @param texto          el bloque de texto a procesar
     * @param palabraBuscada la palabra exacta a contar
     * @param subcadena      la subcadena a buscar dentro de cada palabra
     */
    public void procesar(String texto, String palabraBuscada, String subcadena) {
        // reiniciamos los contadores
        totalPalabras = 0;
        aparicionesPalabra = 0;
        palabrasConSubcadena = 0;

        // si el texto está vacío, retorna
        if (texto == null || texto.isEmpty()) {
            return;
        }

        // convertimos la palabra buscada y subcadena a minúsculas una sola vez para no hacerlo en cada iteración
        String palabraBuscadaLower = palabraBuscada.toLowerCase();
        String subcadenaLower = subcadena.toLowerCase();

        // separamos el texto en palabras usando expresión regular
        // \\s+ significa "uno o más espacios en blanco"
        String[] palabras = texto.split("\\s+");

        for (String palabra : palabras) {
            // ignoramos tokens vacíos que pueden aparecer al hacer split
            if (palabra.isEmpty()) {
                continue;
            }

            totalPalabras++;

            // convertimos a minúsculas para comparación case-insensitive
            String palabraLower = palabra.toLowerCase();

            // limpiamos signos de puntuación al inicio y final de la palabra
            String palabraLimpia = palabraLower.replaceAll("^[^a-záéíóúüñ]+|[^a-záéíóúüñ]+$", "");

            // 1. verificamos si es la palabra exacta buscada
            if (palabraLimpia.equals(palabraBuscadaLower)) {
                aparicionesPalabra++;
            }

            // 2. verificamos si contiene la subcadena
            if (palabraLower.contains(subcadenaLower)) {
                palabrasConSubcadena++;
            }
        }
    }

    // getters para obtener los resultados
    public int getTotalPalabras() {
        return totalPalabras;
    }

    public int getAparicionesPalabra() {
        return aparicionesPalabra;
    }

    public int getPalabrasConSubcadena() {
        return palabrasConSubcadena;
    }
}
