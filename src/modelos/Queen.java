package modelos;

public class Queen extends Pieza {
    public Queen(ColorPieza color, Posicion posicion) {
        super(color, posicion);
    }

    @Override
    public boolean esMovimientoValido(Posicion nuevaPosicion, Pieza[][] tablero) {
        int diferenciaFila = Math.abs(nuevaPosicion.getFila() - posicion.getFila());
        int diferenciaColumna = Math.abs(nuevaPosicion.getColumna() - posicion.getColumna());

        // Movimiento horizontal, vertical o diagonal
        if (diferenciaFila == 0 || diferenciaColumna == 0 || diferenciaFila == diferenciaColumna) {
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
        // Movimiento horizontal
        if (origen.getFila() == destino.getFila()) {
            int direccionColumna = destino.getColumna() > origen.getColumna() ? 1 : -1;
            for (int columna = origen.getColumna() + direccionColumna; columna != destino.getColumna(); columna += direccionColumna) {
                if (columna < 0 || columna > 7) {
                    return true; // Fuera de límites
                }
                if (tablero[origen.getFila()][columna] != null) {
                    return true; // Hay una pieza en el camino
                }
            }
        }
        // Movimiento vertical
        else if (origen.getColumna() == destino.getColumna()) {
            int direccionFila = destino.getFila() > origen.getFila() ? 1 : -1;
            for (int fila = origen.getFila() + direccionFila; fila != destino.getFila(); fila += direccionFila) {
                if (fila < 0 || fila > 7) {
                    return true; // Fuera de límites
                }
                if (tablero[fila][origen.getColumna()] != null) {
                    return true; // Hay una pieza en el camino
                }
            }
        }
        // Movimiento diagonal
        else if (Math.abs(destino.getFila() - origen.getFila()) == Math.abs(destino.getColumna() - origen.getColumna())) {
            int direccionFila = destino.getFila() > origen.getFila() ? 1 : -1;
            int direccionColumna = destino.getColumna() > origen.getColumna() ? 1 : -1;

            int fila = origen.getFila() + direccionFila;
            int columna = origen.getColumna() + direccionColumna;

            while (fila != destino.getFila() && columna != destino.getColumna()) {
                if (fila < 0 || fila > 7 || columna < 0 || columna > 7) {
                    return true; // Fuera de límites
                }
                if (tablero[fila][columna] != null) {
                    return true; // Hay una pieza en el camino
                }
                fila += direccionFila;
                columna += direccionColumna;
            }
        }
        return false; // No hay piezas en el camino
    }
}