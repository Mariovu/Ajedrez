package modelos;

public class Alfil extends Pieza {
    public Alfil(ColorPieza color, Posicion posicion) {
        super(color, posicion);
    }

    @Override
    public boolean esMovimientoValido(Posicion nuevaPosicion, Pieza[][] tablero) {
        int diferenciaFila = Math.abs(nuevaPosicion.getFila() - posicion.getFila());
        int diferenciaColumna = Math.abs(nuevaPosicion.getColumna() - posicion.getColumna());

        // Movimiento diagonal
        if (diferenciaFila == diferenciaColumna) {
            // Verificar si hay piezas en el camino
            if (!hayPiezasEnCamino(posicion, nuevaPosicion, tablero)) {
                // Captura
                Pieza piezaDestino = tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()];
                if (piezaDestino != null && piezaDestino.getColor() != this.color) {
                    return true; // Captura válida
                }
                return true; // Movimiento válido
            }
        }
        return false; // Movimiento inválido
    }

    private boolean hayPiezasEnCamino(Posicion origen, Posicion destino, Pieza[][] tablero) {
        int direccionFila = destino.getFila() > origen.getFila() ? 1 : -1;
        int direccionColumna = destino.getColumna() > origen.getColumna() ? 1 : -1;

        int fila = origen.getFila() + direccionFila;
        int columna = origen.getColumna() + direccionColumna;

        // Iterar a través de las posiciones en el camino
        while (fila != destino.getFila() && columna != destino.getColumna()) {
            // Verificar que los índices estén dentro de los límites
            if (fila < 0 || fila > 7 || columna < 0 || columna > 7) {
                return true; // Fuera de límites, hay una pieza en el camino
            }

            if (tablero[fila][columna] != null) {
                return true; // Hay una pieza en el camino
            }
            fila += direccionFila;
            columna += direccionColumna;
        }
        return false; // No hay piezas en el camino
    }
}