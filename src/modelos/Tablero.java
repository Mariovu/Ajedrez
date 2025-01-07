package modelos;

import Exceptions.MovimientoInvalidoException;
import javax.swing.JOptionPane;

public class Tablero implements Juego{
    private Pieza[][] tablero;

    public Tablero() {
        tablero = new Pieza[8][8];
        inicializarTablero();
    }

    //Upcasting Convertimos las diferentes subclases Piezas a Piezas(padre)
    private void inicializarTablero() {
        // Inicializar las piezas en sus posiciones iniciales
        for (int i = 0; i < 8; i++) {
            tablero[1][i] = new Peon(ColorPieza.BLANCO, new Posicion(1, i));
            tablero[6][i] = new Peon(ColorPieza.NEGRO, new Posicion(6, i));
        }

        tablero[0][0] = new Torre(ColorPieza.BLANCO, new Posicion(0, 0));
        tablero[0][1] = new Caballo(ColorPieza.BLANCO, new Posicion(0, 1));
        tablero[0][2] = new Alfil(ColorPieza.BLANCO, new Posicion(0, 2));
        tablero[0][3] = new Queen(ColorPieza.BLANCO, new Posicion(0, 3));
        tablero[0][4] = new Rey(ColorPieza.BLANCO, new Posicion(0, 4));
        tablero[0][5] = new Alfil(ColorPieza.BLANCO, new Posicion(0, 5));
        tablero[0][6] = new Caballo(ColorPieza.BLANCO, new Posicion(0, 6));
        tablero[0][7] = new Torre(ColorPieza.BLANCO, new Posicion(0, 7));

        tablero[7][0] = new Torre(ColorPieza.NEGRO, new Posicion(7, 0));
        tablero[7][1] = new Caballo(ColorPieza.NEGRO, new Posicion(7, 1));
        tablero[7][2] = new Alfil(ColorPieza.NEGRO, new Posicion(7, 2));
        tablero[7][3] = new Queen(ColorPieza.NEGRO, new Posicion(7, 3));
        tablero[7][4] = new Rey(ColorPieza.NEGRO, new Posicion(7, 4));
        tablero[7][5] = new Alfil(ColorPieza.NEGRO, new Posicion(7, 5));
        tablero[7][6] = new Caballo(ColorPieza.NEGRO, new Posicion(7, 6));
        tablero[7][7] = new Torre(ColorPieza.NEGRO, new Posicion(7, 7));
    }

    @Override
    public boolean moverPieza(Posicion origen, Posicion destino) throws MovimientoInvalidoException {
        // Verificar que las posiciones estén dentro de los límites
        if (origen.getFila() < 0 || origen.getFila() > 7 || origen.getColumna() < 0 || origen.getColumna() > 7 ||
                destino.getFila() < 0 || destino.getFila() > 7 || destino.getColumna() < 0 || destino.getColumna() > 7) {
            throw new MovimientoInvalidoException("Las filas y columnas deben estar entre 0 y 7.");
        }

        Pieza pieza = tablero[origen.getFila()][origen.getColumna()];

        if (pieza == null) {
            throw new MovimientoInvalidoException("No hay pieza en la posición de origen.");
        }

        if (!pieza.esMovimientoValido(destino, tablero)) {
            throw new MovimientoInvalidoException("Movimiento no válido.");
        }

        Pieza piezaDestino = tablero[destino.getFila()][destino.getColumna()];
        if (piezaDestino != null && !pieza.puedeCapturar(piezaDestino)) {
            throw new MovimientoInvalidoException("No puedes capturar tus propias piezas.");
        }

        // Mover la pieza
        tablero[destino.getFila()][destino.getColumna()] = pieza;
        tablero[origen.getFila()][origen.getColumna()] = null;

        // Actualizar la posición de la pieza
        pieza.setPosicion(destino);

        // Verificar el estado del rey después del movimiento
        if (isInCheck(obtenerRey(pieza.getColor()))) {
            // Revertir el movimiento si el rey está en jaque
            tablero[origen.getFila()][origen.getColumna()] = pieza;
            tablero[destino.getFila()][destino.getColumna()] = null;
            pieza.setPosicion(origen); // Restaurar la posición de la pieza
            throw new MovimientoInvalidoException("Movimiento no válido: el rey está en jaque.");
        }

        // Verificar si hay jaque mate
        verificarEstadoRey();

        return true; // Movimiento exitoso
    }


    private void verificarEstadoRey() {
        Rey reyBlanco = obtenerRey(ColorPieza.BLANCO);
        Rey reyNegro = obtenerRey(ColorPieza.NEGRO);

        if (isInCheck(reyBlanco)) {
            JOptionPane.showMessageDialog(null, "¡El rey blanco está en jaque!");
        }
        if (isInCheck(reyNegro)) {
            JOptionPane.showMessageDialog(null, "¡El rey negro está en jaque!");
        }

        if (isCheckmate(reyBlanco)) {
            JOptionPane.showMessageDialog(null, "¡Jaque mate! El rey blanco no tiene movimientos legales.");
        }
        if (isCheckmate(reyNegro)) {
            JOptionPane.showMessageDialog(null, "¡Jaque mate! El rey negro no tiene movimientos legales.");
        }
    }
//downcasting Obtenemos una estancia Pieza y la convertimos en Rey
    private Rey obtenerRey(ColorPieza color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (tablero[i][j] instanceof Rey && tablero[i][j].getColor() == color) {
                    return (Rey) tablero[i][j];
                }
            }
        }
        return null; // No se encontró el rey
    }

    @Override
    public boolean isInCheck(Rey rey) {
        Posicion posRey = rey.getPosicion();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Pieza pieza = tablero[i][j];
                if (pieza != null && pieza.getColor() != rey.getColor() && pieza.esMovimientoValido(posRey, tablero)) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public boolean isCheckmate(Rey rey) {
        if (!isInCheck(rey)) {
            return false;
        }
        Posicion posRey = rey.getPosicion();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int nuevaFila = posRey.getFila() + i;
                int nuevaColumna = posRey.getColumna() + j;
                if (nuevaFila >= 0 && nuevaFila <= 7 && nuevaColumna >= 0 && nuevaColumna <= 7) {
                    Posicion nuevaPos = new Posicion(nuevaFila, nuevaColumna);
                    Pieza piezaDestino = tablero[nuevaFila][nuevaColumna];
                    if (piezaDestino == null || piezaDestino.getColor() != rey.getColor()) {
                        if (!causariaJaque(rey, nuevaPos)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean causariaJaque(Rey rey, Posicion nuevaPos) {
        Posicion posActual = rey.getPosicion();
        Pieza piezaTemporal = tablero[nuevaPos.getFila()][nuevaPos.getColumna()];

        // Mover rey temporalmente
        tablero[posActual.getFila()][posActual.getColumna()] = null;
        tablero[nuevaPos.getFila()][nuevaPos.getColumna()] = rey;
        rey.setPosicion(nuevaPos);

        boolean resultado = isInCheck(rey);

        // Revertir movimiento
        tablero[nuevaPos.getFila()][nuevaPos.getColumna()] = piezaTemporal;
        tablero[posActual.getFila()][posActual.getColumna()] = rey;
        rey.setPosicion(posActual);

        return resultado;
    }




    @Override
    public Pieza[][] getTablero() {
        return tablero; // Método para obtener el estado del tablero
    }


}