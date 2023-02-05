package com.erebelo.springh2demo.exception;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class StandardException extends RuntimeException {

    private final ErrorEnum error;
    private final Object[] args;

    public StandardException(ErrorEnum error) {
        this.error = error;
        this.args = null;
    }

    public StandardException(ErrorEnum error, Object... args) {
        this.error = error;
        this.args = args;
    }

    public StandardException(ErrorEnum error, Throwable cause) {
        super(cause);
        this.error = error;
        this.args = null;
    }

    public StandardException(ErrorEnum error, Throwable cause, Object... args) {
        super(cause);
        this.error = error;
        this.args = args;
    }
}
