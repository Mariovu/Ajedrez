package modelos;

public class Peon extends Pieza {
    private boolean primerMovimiento;

    public Peon(ColorPieza color, Posicion posicion) {
        super(color, posicion);
        this.primerMovimiento = true; // Asumimos que el peón comienza en su posición inicial
    }

    @Override
    public boolean esMovimientoValido(Posicion nuevaPosicion, Pieza[][] tablero) {
        int direccion = (color == ColorPieza.BLANCO) ? 1 : -1; // Dirección del movimiento: 1 para blanco, -1 para negro
        int filaDestino = posicion.getFila() + direccion;

        // Movimiento hacia adelante
        if (nuevaPosicion.getColumna() == posicion.getColumna()) {
            // Movimiento de 1 casilla hacia adelante
            if (nuevaPosicion.getFila() == filaDestino && tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()] == null) {
                return true; // Movimiento válido
            }
            // Movimiento de 2 casillas hacia adelante desde la posición inicial
            if (primerMovimiento && nuevaPosicion.getFila() == filaDestino + direccion && tablero[filaDestino][nuevaPosicion.getColumna()] == null && tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()] == null) {
                return true; // Movimiento válido
            }
        }

        // Captura en diagonal
        if (Math.abs(nuevaPosicion.getColumna() - posicion.getColumna()) == 1 && nuevaPosicion.getFila() == filaDestino) {
            if (tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()] != null &&
                    tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()].getColor() != this.color) {
                return true; // Captura válida
            }
        }

        return false; // Movimiento inválido
    }

    // Método para marcar que el peón ha movido
    public void mover() {
        this.primerMovimiento = false;
    }
}