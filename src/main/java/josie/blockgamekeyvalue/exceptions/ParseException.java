package josie.blockgamekeyvalue.exceptions;

/**
 * Error while parsing Mojang's weird form format.
 */
public class ParseException extends Exception {
    /**
     * Construct the exception.
     */
    public ParseException(String message) {
        super(message);
    }

    /**
     * Construct the exception.
     */
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
