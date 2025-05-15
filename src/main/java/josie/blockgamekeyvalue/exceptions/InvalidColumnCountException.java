package josie.blockgamekeyvalue.exceptions;

public class InvalidColumnCountException extends ParseException {
    public InvalidColumnCountException(String line) {
        super("Invalid number of columns in line: \"" + line + "\" while parsing Mojang's custom form format");
    }
}
