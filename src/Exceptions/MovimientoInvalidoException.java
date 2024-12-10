package Exceptions;

public class MovimientoInvalidoException extends Exception {
    public MovimientoInvalidoException(String mensaje) {
        super(mensaje);
    }
}