package com.example.budgetapp.exceptions;

public class CannotVerifyDataException extends RuntimeException{
    public CannotVerifyDataException() {
        super();
    }

    public CannotVerifyDataException(String message) {
        super(message);
    }

    public CannotVerifyDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotVerifyDataException(Throwable cause) {
        super(cause);
    }

    protected CannotVerifyDataException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
