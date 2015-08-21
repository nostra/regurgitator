package no.api.regurgitator.exception;

/**
 *
 */
public class RegurgitatorException extends RuntimeException {

    public RegurgitatorException(String message) {
        super(message);
    }

    public RegurgitatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
