package modelos;

import Exceptions.MovimientoInvalidoException;

public interface Juego {
    boolean moverPieza(Posicion origen, Posicion destino) throws MovimientoInvalidoException;
    Pieza[][] getTablero();
    boolean isInCheck(Rey rey);
    boolean isCheckmate(Rey rey);
}