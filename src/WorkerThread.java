/**
 * WorkerThread.java
 * 
 * esta clase extiende Thread. Cada instancia representa un hilo que procesa un bloque (fragmento) del texto.
 * extendemos Thread porque el enunciado pide usar directamente la clase Thread.
 * 
 * cada hilo:
 *  recibe un fragmento de texto en el constructor
 *  en run(), procesa ese fragmento con TextProcessor
 *  guarda los resultados parciales para que el hilo principal los recoja
 */
public class WorkerThread extends Thread {

    private final String fragmentoTexto;
    private final String palabraBuscada;
    private final String subcadena;

    // resultados parciales de este hilo
    private int totalPalabras;
    private int aparicionesPalabra;
    private int palabrasConSubcadena;

    /**
     * constructor del hilo trabajador.
     * 
     * @param nombre         nombre del hilo
     * @param fragmento      el fragmento de texto que este hilo va a procesar
     * @param palabraBuscada palabra exacta a contar
     * @param subcadena      subcadena a buscar
     */
    public WorkerThread(String nombre, String fragmento, String palabraBuscada, String subcadena) {
        super(nombre); 
        this.fragmentoTexto = fragmento;
        this.palabraBuscada = palabraBuscada;
        this.subcadena = subcadena;
    }

    /**
     * método run() — se ejecuta cuando llamamos a start().
     * aquí es donde el hilo hace su trabajo real.
     */
    @Override
    public void run() {
        TextProcessor processor = new TextProcessor();
        processor.procesar(fragmentoTexto, palabraBuscada, subcadena);

        // guardamos los resultados parciales
        this.totalPalabras = processor.getTotalPalabras();
        this.aparicionesPalabra = processor.getAparicionesPalabra();
        this.palabrasConSubcadena = processor.getPalabrasConSubcadena();
    }

    // el hilo principal usa getters despues de join() para obtener los resultados
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
