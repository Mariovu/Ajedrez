package modelos;

public class Rey extends Pieza {
    public Rey(ColorPieza color, Posicion posicion) {
        super(color, posicion);
    }

    @Override
    public boolean esMovimientoValido(Posicion nuevaPosicion, Pieza[][] tablero) {
        int diferenciaFila = Math.abs(nuevaPosicion.getFila() - posicion.getFila());
        int diferenciaColumna = Math.abs(nuevaPosicion.getColumna() - posicion.getColumna());

        // El rey se puede mover una casilla en cualquier dirección
        if ((diferenciaFila <= 1 && diferenciaColumna <= 1) && !(diferenciaFila == 0 && diferenciaColumna == 0)) {
            // Verificar si la casilla de destino está ocupada por una pieza del mismo color
            if (tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()] == null ||
                    tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()].getColor() != this.color) {
                return true; // Movimiento válido o captura válida
            }
        }
        return false; // Movimiento inválido
    }
}