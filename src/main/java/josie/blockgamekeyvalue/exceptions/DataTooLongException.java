package josie.blockgamekeyvalue.exceptions;

/**
 * The input data exceeded the specified limit while parsing Mojang's custom
 * form format. Can either indicate it reached the maximum overall input size,
 * or the line limit.
 */
public class DataTooLongException extends ParseException {
    /**
     * Construct the error.
     *
     * @param maximumSize  the specified maximum size
     * @param maximumLines the specified maximum lines
     */
    public DataTooLongException(int maximumSize, int maximumLines) {
        super("Data exceeded limit (configured as %d bytes, %d lines) while parsing Mojang's custom form format"
                .formatted(maximumSize, maximumLines));
    }
}
