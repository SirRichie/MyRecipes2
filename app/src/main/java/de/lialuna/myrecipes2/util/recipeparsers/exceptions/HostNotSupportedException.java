package de.lialuna.myrecipes2.util.recipeparsers.exceptions;

public class HostNotSupportedException extends IllegalArgumentException {
    public HostNotSupportedException() {
    }

    public HostNotSupportedException(String s) {
        super(s);
    }

    public HostNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public HostNotSupportedException(Throwable cause) {
        super(cause);
    }
}
