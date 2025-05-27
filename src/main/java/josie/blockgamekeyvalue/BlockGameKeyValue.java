package josie.blockgamekeyvalue;

import java.util.LinkedHashMap;
import java.util.Map;
import josie.blockgamekeyvalue.exceptions.DataTooLongException;
import josie.blockgamekeyvalue.exceptions.InvalidColumnCountException;
import josie.blockgamekeyvalue.exceptions.InvalidKeyException;
import josie.blockgamekeyvalue.exceptions.ParseException;

/**
 * Tools for working with Mojang's weird custom form format
 */
public class BlockGameKeyValue {
    /**
     * Deserialize one of Mojang's weird TSV strings into a {@link Map}
     *
     * @param payload      the string to parse
     * @param maximumSize  the maximum size of the string
     * @param maximumLines the maximum number of lines accepted
     */
    public static Map<String, String> parse(String payload, int maximumSize, int maximumLines) throws ParseException {
        if (payload.length() > maximumSize) {
            throw new DataTooLongException(maximumSize, maximumLines);
        }

        final var result = new LinkedHashMap<String, String>();
        final var lines = payload.split("\n", -1);

        var i = 0;
        for (final var line : lines) {
            final var sepIndex = line.indexOf('\t');
            if (sepIndex == -1) {
                throw new InvalidColumnCountException(line);
            }

            final var key = line.substring(0, sepIndex);
            final var rawValue = line.substring(sepIndex + 1);

            if (key.isEmpty() || !isValidVariableName(key)) {
                throw new InvalidKeyException(key);
            } else if (rawValue.indexOf('\t') != -1) {
                throw new InvalidColumnCountException(line);
            }

            result.put(key, unescape(rawValue));

            i++;
            if (i > maximumLines) {
                throw new DataTooLongException(maximumSize, maximumLines);
            }
        }

        return result;
    }

    /**
     * Deserialize one of Mojang's weird TSV strings into a {@link Map}. Uses a
     * default size limit of 131072 and a default line limit of 16384
     *
     * @param payload the string to parse
     */
    public static Map<String, String> parse(String payload) throws ParseException {
        return parse(payload, 0x20000, 0x4000);
    }

    private static String unescape(String input) {
        final var sb = new StringBuilder(input.length());
        for (var i = 0; i < input.length(); ) {
            final var character = input.charAt(i);
            if (character == '\\' && i + 1 < input.length()) {
                final var next = input.charAt(i + 1);
                if (next == 'n') {
                    sb.append('\n');
                    i += 2;
                } else if (next == 't') {
                    sb.append('\t');
                    i += 2;
                } else {
                    sb.append('\\');
                    sb.append(next);
                    i += 2;
                }
            } else {
                sb.append(character);
                i++;
            }
        }
        return sb.toString();
    }

    private static boolean isValidVariableName(String string) {
        for (var i = 0; i < string.length(); ++i) {
            final var character = string.charAt(i);
            if (Character.isLetterOrDigit(character) || character == '_') continue;
            return false;
        }
        return true;
    }
}
