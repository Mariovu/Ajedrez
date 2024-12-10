package modelos;

public class Posicion {
    private int fila;    // Fila en el tablero (0-7)
    private int columna; // Columna en el tablero (0-7)

    public Posicion(int fila, int columna) {
        if (fila < 0 || fila > 7 || columna < 0 || columna > 7) {
            throw new IllegalArgumentException("Las filas y columnas deben estar entre 0 y 7.");
        }
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    @Override
    public String toString() {
        return "Posicion{" +
                "fila=" + fila +
                ", columna=" + columna +
                '}';
    }
}