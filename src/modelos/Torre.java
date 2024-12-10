package modelos;

public class Torre extends Pieza {
    public Torre(ColorPieza color, Posicion posicion) {
        super(color, posicion);
    }

    @Override
    public boolean esMovimientoValido(Posicion nuevaPosicion, Pieza[][] tablero) {
        int diferenciaFila = Math.abs(nuevaPosicion.getFila() - posicion.getFila());
        int diferenciaColumna = Math.abs(nuevaPosicion.getColumna() - posicion.getColumna());

        // Movimiento horizontal o vertical
        if (diferenciaFila == 0 || diferenciaColumna == 0) {
            if (!hayPiezasEnCamino(posicion, nuevaPosicion, tablero)) {
                // Captura
                if (tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()] != null &&
                        tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()].getColor() != this.color) {
                    return true; // Captura válida
                }
                return true; // Movimiento válido
            }
        }
        return false; // Movimiento inválido
    }

    private boolean hayPiezasEnCamino(Posicion origen, Posicion destino, Pieza[][] tablero) {
        if (origen.getFila() == destino.getFila()) { // Movimiento horizontal
            int direccionColumna = destino.getColumna() > origen.getColumna() ? 1 : -1;
            for (int columna = origen.getColumna() + direccionColumna; columna != destino.getColumna(); columna += direccionColumna) {
                if (tablero[origen.getFila()][columna] != null) {
                    return true; // Hay una pieza en el camino
                }
            }
        } else { // Movimiento vertical
            int direccionFila = destino.getFila() > origen.getFila() ? 1 : -1;
            for (int fila = origen.getFila() + direccionFila; fila != destino.getFila(); fila += direccionFila) {
                if (tablero[fila][origen.getColumna()] != null) {
                    return true; // Hay una pieza en el camino
                }
            }
        }
        return false; // No hay piezas en el camino
    }
}