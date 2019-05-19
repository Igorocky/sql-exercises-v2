package org.igye.sqlexercises.exceptions;

public class ExerciseException extends RuntimeException {
    public ExerciseException(String message) {
        super(message);
    }

    public ExerciseException(Throwable cause) {
        super(cause);
    }
}
