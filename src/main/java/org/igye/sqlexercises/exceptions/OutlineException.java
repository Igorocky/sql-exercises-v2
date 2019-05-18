package org.igye.sqlexercises.exceptions;

public class OutlineException extends RuntimeException {
    public OutlineException(String message) {
        super(message);
    }

    public OutlineException(Throwable cause) {
        super(cause);
    }
}
