package josie.blockgamekeyvalue;

import static org.junit.jupiter.api.Assertions.*;

import josie.blockgamekeyvalue.exceptions.ParseException;
import org.junit.jupiter.api.Test;

public class BlockGameKeyValueTest {
    @Test
    public void testSingleEntry() throws ParseException {
        var input = "key\tvalue";
        var result = BlockGameKeyValue.parse(input);
        assertEquals(1, result.size());
        assertEquals("value", result.get("key"));
    }

    @Test
    public void testMultipleEntries() throws ParseException {
        var input = "a\t1\nb\t2\nc\t3";
        var result = BlockGameKeyValue.parse(input);
        assertEquals(3, result.size());
        assertEquals("1", result.get("a"));
        assertEquals("2", result.get("b"));
        assertEquals("3", result.get("c"));
    }

    @Test
    public void testEscapedNewline() throws ParseException {
        var input = "note\tline1\\nline2";
        var result = BlockGameKeyValue.parse(input);
        assertEquals("line1\nline2", result.get("note"));
    }

    @Test
    public void testEscapedTab() throws ParseException {
        var input = "note\titem1\\titem2";
        var result = BlockGameKeyValue.parse(input);
        assertEquals("item1\titem2", result.get("note"));
    }

    @Test
    public void testMultipleEscapes() throws ParseException {
        var input = "data\tline\\nwith\\tmany\\nescapes";
        var result = BlockGameKeyValue.parse(input);
        assertEquals("line\nwith\tmany\nescapes", result.get("data"));
    }

    @Test
    public void testEmptyValue() throws ParseException {
        var input = "empty\t";
        var result = BlockGameKeyValue.parse(input);
        assertEquals("", result.get("empty"));
    }

    @Test
    public void testEmptyPayload() {
        var input = "";
        assertThrows(ParseException.class, () -> BlockGameKeyValue.parse(input));
    }

    @Test
    public void testMissingSeparator() {
        var input = "no_separator_line";
        assertThrows(ParseException.class, () -> BlockGameKeyValue.parse(input));
    }

    @Test
    public void testEmptyKey() {
        var input = "\tvalue";
        assertThrows(ParseException.class, () -> BlockGameKeyValue.parse(input));
    }

    @Test
    public void testNonOverZealousEscaping() throws ParseException {
        var result = BlockGameKeyValue.parse("key\tyepitsvalid\\x");

        assertEquals("yepitsvalid\\x", result.get("key"));
    }

    @Test
    public void testBackslashAtEnd() throws ParseException {
        var result = BlockGameKeyValue.parse("key\tending with a backslash works too\\");
        assertEquals("ending with a backslash works too\\", result.get("key"));
    }

    @Test
    public void testKeyCollisionLastWins() throws ParseException {
        var result = BlockGameKeyValue.parse("dup\tone\ndup\ttwo");
        assertEquals(1, result.size());
        assertEquals("two", result.get("dup"));
    }

    @Test
    public void testRejectBlankLines() {
        var input = "a\t1\nb\t2\n";
        assertThrows(ParseException.class, () -> BlockGameKeyValue.parse(input));
    }

    @Test
    public void testOnlyTabLine() {
        var input = "\t";
        assertThrows(ParseException.class, () -> BlockGameKeyValue.parse(input));
    }

    @Test
    public void testUnicodeContent() throws ParseException {
        var input = "emoji\t👍\nchinese\t你好";
        var result = BlockGameKeyValue.parse(input);
        assertEquals("👍", result.get("emoji"));
        assertEquals("你好", result.get("chinese"));
    }

    @Test
    public void testValidUnicodeKey() throws ParseException {
        var input = "你好\tvalue";
        var result = BlockGameKeyValue.parse(input);
        assertEquals("value", result.get("你好"));
    }

    @Test
    public void testInvalidUnicodeKey() {
        var input = "👍\tvalue";
        assertThrows(ParseException.class, () -> BlockGameKeyValue.parse(input));
    }

    @Test
    public void testStressLotsOfLines() throws ParseException {
        var sb = new StringBuilder();
        for (var i = 0; i < 10000; i++) {
            sb.append("k").append(i).append("\tv").append(i).append("\n");
        }
        sb.setLength(sb.length() - 1);
        var result = BlockGameKeyValue.parse(sb.toString());
        assertEquals(10000, result.size());
        assertEquals("v0", result.get("k0"));
        assertEquals("v9999", result.get("k9999"));
    }

    @Test
    public void testStressVeryLongLines() throws ParseException {
        var longString1 = "k".repeat(2000);
        var longString2 = "v".repeat(2000);
        var sb = new StringBuilder();
        for (var i = 0; i < 4; i++) {
            sb.append(longString1)
                    .append(i)
                    .append("\t")
                    .append(longString2)
                    .append(i)
                    .append("\n");
        }
        sb.setLength(sb.length() - 1);
        var result = BlockGameKeyValue.parse(sb.toString());
        assertEquals(4, result.size());
        assertEquals(longString2 + "0", result.get(longString1 + "0"));
        assertEquals(longString2 + "3", result.get(longString1 + "3"));
    }

    @Test
    public void testWeirdBackslashes() throws ParseException {
        var result = BlockGameKeyValue.parse("weird\ttext\\\\n");
        assertEquals("text\\\\n", result.get("weird"));
    }

    @Test
    public void testWhitespaceKey() {
        var input = " \tvalue";
        assertThrows(ParseException.class, () -> BlockGameKeyValue.parse(input));
    }

    @Test
    public void testWhitespaceValue() throws ParseException {
        var result = BlockGameKeyValue.parse("key\t ");
        assertEquals(" ", result.get("key"));
    }

    @Test
    public void testRepeatedEscapedSequences() throws ParseException {
        var result = BlockGameKeyValue.parse("x\t\\n\\t\\n\\t");
        assertEquals("\n\t\n\t", result.get("x"));
    }

    @Test
    public void testEntryWithMultipleTabs() {
        var input = "a\tb\tc";
        assertThrows(ParseException.class, () -> BlockGameKeyValue.parse(input));
    }

    @Test
    public void testMultilineEscapedPayload() throws ParseException {
        var result = BlockGameKeyValue.parse("k\tline1\\nline2\\nline3");
        assertEquals("line1\nline2\nline3", result.get("k"));
    }
}
