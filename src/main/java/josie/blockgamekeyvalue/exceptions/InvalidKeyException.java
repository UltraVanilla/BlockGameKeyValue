package josie.blockgamekeyvalue.exceptions;

/**
 * Indicates that a key was invalid while parsing Mojang's weird form format.
 */
public class InvalidKeyException extends ParseException {
    /**
     * Construct the exception.
     */
    public InvalidKeyException(String key) {
        super("Encountered invalid key: \"" + key + "\" while parsing Mojang's custom form format");
    }
}
