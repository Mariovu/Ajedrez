package modelos;

public abstract class Pieza {
    protected ColorPieza color;
    protected Posicion posicion;

    public Pieza(ColorPieza color, Posicion posicion) {
        this.color = color;
        this.posicion = posicion;
    }

    public ColorPieza getColor() {
        return color;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion nuevaPosicion) {
        this.posicion = nuevaPosicion;
    }

    public abstract boolean esMovimientoValido(Posicion nuevaPosicion, Pieza[][] tablero);

    protected boolean puedeCapturar(Pieza piezaObjetivo) {
        // Verifica si la pieza objetivo es del mismo color
        return piezaObjetivo != null && piezaObjetivo.getColor() != this.color;
    }
}