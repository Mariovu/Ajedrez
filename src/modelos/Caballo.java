package modelos;

public class Caballo extends Pieza {
    public Caballo(ColorPieza color, Posicion posicion) {
        super(color, posicion);
    }

    @Override
    public boolean esMovimientoValido(Posicion nuevaPosicion, Pieza[][] tablero) {
        int diferenciaFila = Math.abs(nuevaPosicion.getFila() - posicion.getFila());
        int diferenciaColumna = Math.abs(nuevaPosicion.getColumna() - posicion.getColumna());

        // El caballo se mueve en forma de "L": 2 en una dirección y 1 en la otra
        if ((diferenciaFila == 2 && diferenciaColumna == 1) || (diferenciaFila == 1 && diferenciaColumna == 2)) {
            // Verificar si la casilla de destino está ocupada por una pieza del mismo color
            if (tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()] == null ||
                    tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()].getColor() != this.color) {
                return true; // Movimiento válido o captura válida
            }
        }
        return false; // Movimiento inválido
    }
}