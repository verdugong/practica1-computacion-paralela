/**
 * SequentialProcessor.java
 * 
 * procesamiento secuencial del texto.
 * usa un solo flujo de ejecución (el hilo principal) y le pasa todo el texto al TextProcessor y listo.
 * 
 * aquí se compara el rendimiento paralelo.
 */
public class SequentialProcessor {

    /**
     * procesa todo el texto de forma secuencial.
     * 
     * @param texto          todo el contenido del archivo en un solo String
     * @param palabraBuscada palabra exacta a contar
     * @param subcadena      subcadena a buscar en cada palabra
     * @return un arreglo con los 3 resultados: [totalPalabras, apariciones, palabrasConSubcadena]
     */
    public int[] procesar(String texto, String palabraBuscada, String subcadena) {
        TextProcessor processor = new TextProcessor();
        processor.procesar(texto, palabraBuscada, subcadena);

        return new int[]{
            processor.getTotalPalabras(),
            processor.getAparicionesPalabra(),
            processor.getPalabrasConSubcadena()
        };
    }
}
