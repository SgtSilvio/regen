package com.github.sgtsilvio.regen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Silvio Giebl
 */
class ReGenParseCharacterSetTest {

    @Test
    void parse_characterSetWithUnescapedBracketAfterOpeningBracket() {
        final ReGen reGen = ReGen.parse("[]]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals(']', root.fromCodePoint);
        assertEquals(']', root.toCodePoint);
    }

    @Test
    void parse_characterSetWithEscapedBracketAfterOpeningBracket() {
        final ReGen reGen = ReGen.parse("[\\]]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals(']', root.fromCodePoint);
        assertEquals(']', root.toCodePoint);
    }

    @Test
    void parse_characterSetWithEscapedBracketAfterFirstCharacter() {
        final ReGen reGen = ReGen.parse("[a\\]]");
        assertTrue(reGen.root instanceof Alternation);
        final Alternation root = (Alternation) reGen.root;
        assertEquals(2, root.alternatives.length);
        assertTrue(root.alternatives[0] instanceof CharacterRange);
        final CharacterRange alternative0 = (CharacterRange) root.alternatives[0];
        assertEquals(']', alternative0.fromCodePoint);
        assertEquals(']', alternative0.toCodePoint);
        assertTrue(root.alternatives[1] instanceof CharacterRange);
        final CharacterRange alternative1 = (CharacterRange) root.alternatives[1];
        assertEquals('a', alternative1.fromCodePoint);
        assertEquals('a', alternative1.toCodePoint);
    }

    @Test
    void parse_characterSetWithEscapedCaretAfterOpeningBracket() {
        final ReGen reGen = ReGen.parse("[\\^]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals('^', root.fromCodePoint);
        assertEquals('^', root.toCodePoint);
    }

    @Test
    void parse_characterSetWithUnescapedCaretAfterFirstCharacter() {
        final ReGen reGen = ReGen.parse("[a^]");
        assertTrue(reGen.root instanceof Alternation);
        final Alternation root = (Alternation) reGen.root;
        assertEquals(2, root.alternatives.length);
        assertTrue(root.alternatives[0] instanceof CharacterRange);
        final CharacterRange alternative0 = (CharacterRange) root.alternatives[0];
        assertEquals('^', alternative0.fromCodePoint);
        assertEquals('^', alternative0.toCodePoint);
        assertTrue(root.alternatives[1] instanceof CharacterRange);
        final CharacterRange alternative1 = (CharacterRange) root.alternatives[1];
        assertEquals('a', alternative1.fromCodePoint);
        assertEquals('a', alternative1.toCodePoint);
    }

    @Test
    void parse_characterSetWithEscapedCaretAfterFirstCharacter() {
        final ReGen reGen = ReGen.parse("[a\\^]");
        assertTrue(reGen.root instanceof Alternation);
        final Alternation root = (Alternation) reGen.root;
        assertEquals(2, root.alternatives.length);
        assertTrue(root.alternatives[0] instanceof CharacterRange);
        final CharacterRange alternative0 = (CharacterRange) root.alternatives[0];
        assertEquals('^', alternative0.fromCodePoint);
        assertEquals('^', alternative0.toCodePoint);
        assertTrue(root.alternatives[1] instanceof CharacterRange);
        final CharacterRange alternative1 = (CharacterRange) root.alternatives[1];
        assertEquals('a', alternative1.fromCodePoint);
        assertEquals('a', alternative1.toCodePoint);
    }

    @Test
    void parse_characterSetWithUnescapedDashAfterOpeningBracket() {
        final ReGen reGen = ReGen.parse("[-]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals('-', root.fromCodePoint);
        assertEquals('-', root.toCodePoint);
    }

    @Test
    void parse_characterSetWithEscapedDashAfterOpeningBracket() {
        final ReGen reGen = ReGen.parse("[\\-]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals('-', root.fromCodePoint);
        assertEquals('-', root.toCodePoint);
    }

    @Test
    void parse_characterSetWithUnescapedDashBeforeClosingBracket() {
        final ReGen reGen = ReGen.parse("[a-]");
        assertTrue(reGen.root instanceof Alternation);
        final Alternation root = (Alternation) reGen.root;
        assertEquals(2, root.alternatives.length);
        assertTrue(root.alternatives[0] instanceof CharacterRange);
        final CharacterRange alternative0 = (CharacterRange) root.alternatives[0];
        assertEquals('-', alternative0.fromCodePoint);
        assertEquals('-', alternative0.toCodePoint);
        assertTrue(root.alternatives[1] instanceof CharacterRange);
        final CharacterRange alternative1 = (CharacterRange) root.alternatives[1];
        assertEquals('a', alternative1.fromCodePoint);
        assertEquals('a', alternative1.toCodePoint);
    }

    @Test
    void parse_characterSetWithEscapedDashBeforeClosingBracket() {
        final ReGen reGen = ReGen.parse("[a\\-]");
        assertTrue(reGen.root instanceof Alternation);
        final Alternation root = (Alternation) reGen.root;
        assertEquals(2, root.alternatives.length);
        assertTrue(root.alternatives[0] instanceof CharacterRange);
        final CharacterRange alternative0 = (CharacterRange) root.alternatives[0];
        assertEquals('-', alternative0.fromCodePoint);
        assertEquals('-', alternative0.toCodePoint);
        assertTrue(root.alternatives[1] instanceof CharacterRange);
        final CharacterRange alternative1 = (CharacterRange) root.alternatives[1];
        assertEquals('a', alternative1.fromCodePoint);
        assertEquals('a', alternative1.toCodePoint);
    }

    @Test
    void parse_characterSetWithUnescapedDashAfterRange() {
        final ReGen reGen = ReGen.parse("[a-b-d]");
        assertTrue(reGen.root instanceof Alternation);
        final Alternation root = (Alternation) reGen.root;
        assertEquals(3, root.alternatives.length);
        assertTrue(root.alternatives[0] instanceof CharacterRange);
        final CharacterRange alternative0 = (CharacterRange) root.alternatives[0];
        assertEquals('-', alternative0.fromCodePoint);
        assertEquals('-', alternative0.toCodePoint);
        assertTrue(root.alternatives[1] instanceof CharacterRange);
        final CharacterRange alternative1 = (CharacterRange) root.alternatives[1];
        assertEquals('a', alternative1.fromCodePoint);
        assertEquals('b', alternative1.toCodePoint);
        assertTrue(root.alternatives[2] instanceof CharacterRange);
        final CharacterRange alternative2 = (CharacterRange) root.alternatives[2];
        assertEquals('d', alternative2.fromCodePoint);
        assertEquals('d', alternative2.toCodePoint);
    }

    @Test
    void parse_characterSetWithEscapedDashAfterRange() {
        final ReGen reGen = ReGen.parse("[a-b\\-d]");
        assertTrue(reGen.root instanceof Alternation);
        final Alternation root = (Alternation) reGen.root;
        assertEquals(3, root.alternatives.length);
        assertTrue(root.alternatives[0] instanceof CharacterRange);
        final CharacterRange alternative0 = (CharacterRange) root.alternatives[0];
        assertEquals('-', alternative0.fromCodePoint);
        assertEquals('-', alternative0.toCodePoint);
        assertTrue(root.alternatives[1] instanceof CharacterRange);
        final CharacterRange alternative1 = (CharacterRange) root.alternatives[1];
        assertEquals('a', alternative1.fromCodePoint);
        assertEquals('b', alternative1.toCodePoint);
        assertTrue(root.alternatives[2] instanceof CharacterRange);
        final CharacterRange alternative2 = (CharacterRange) root.alternatives[2];
        assertEquals('d', alternative2.fromCodePoint);
        assertEquals('d', alternative2.toCodePoint);
    }

    @Test
    void parse_characterSetWithUnescapedDashInRangeAsFrom() {
        final ReGen reGen = ReGen.parse("[--a]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals('-', root.fromCodePoint);
        assertEquals('a', root.toCodePoint);
    }

    @Test
    void parse_characterSetWithEscapedDashInRangeAsFrom() {
        final ReGen reGen = ReGen.parse("[\\--a]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals('-', root.fromCodePoint);
        assertEquals('a', root.toCodePoint);
    }

    @Test
    void parse_characterSetWithUnescapedDashInRangeAsTo() {
        final ReGen reGen = ReGen.parse("[#--]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals('#', root.fromCodePoint);
        assertEquals('-', root.toCodePoint);
    }

    @Test
    void parse_characterSetWithEscapedDashInRangeAsTo() {
        final ReGen reGen = ReGen.parse("[#-\\-]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals('#', root.fromCodePoint);
        assertEquals('-', root.toCodePoint);
    }

    @Test
    void parse_characterSetWith2ByteCharacter() {
        final ReGen reGen = ReGen.parse("[ä]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals('ä', root.fromCodePoint);
        assertEquals('ä', root.toCodePoint);
    }

    @Test
    void parse_characterSetWith3ByteCharacter() {
        final ReGen reGen = ReGen.parse("[€]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals('€', root.fromCodePoint);
        assertEquals('€', root.toCodePoint);
    }

    @Test
    void parse_characterSetWith4ByteCharacter() {
        final ReGen reGen = ReGen.parse("[\uD808\uDC00]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals("\uD808\uDC00".codePointAt(0), root.fromCodePoint);
        assertEquals("\uD808\uDC00".codePointAt(0), root.toCodePoint);
    }

    @Test
    void parse_characterSetWithMultiByteCharacterInRangeAsTo() {
        final ReGen reGen = ReGen.parse("[a-€]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals('a', root.fromCodePoint);
        assertEquals('€', root.toCodePoint);
    }

    @Test
    void parse_characterSetWithMultiByteCharacterInRangeAsFromAndTo() {
        final ReGen reGen = ReGen.parse("[ä-€]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals('ä', root.fromCodePoint);
        assertEquals('€', root.toCodePoint);
    }

    @Test
    void parse_mergeCharacterRangesToOne() {
        final ReGen reGen = ReGen.parse("[a-bcd-z]");
        assertTrue(reGen.root instanceof CharacterRange);
        final CharacterRange root = (CharacterRange) reGen.root;
        assertEquals('a', root.fromCodePoint);
        assertEquals('z', root.toCodePoint);
    }

    @Test
    void parse_mergeConsecutiveCharacterRanges() {
        final ReGen reGen = ReGen.parse("[a-b0-1c2d-z3-9]");
        assertTrue(reGen.root instanceof Alternation);
        final Alternation root = (Alternation) reGen.root;
        assertEquals(2, root.alternatives.length);
        assertTrue(root.alternatives[0] instanceof CharacterRange);
        final CharacterRange alternative0 = (CharacterRange) root.alternatives[0];
        assertEquals('0', alternative0.fromCodePoint);
        assertEquals('9', alternative0.toCodePoint);
        assertTrue(root.alternatives[1] instanceof CharacterRange);
        final CharacterRange alternative1 = (CharacterRange) root.alternatives[1];
        assertEquals('a', alternative1.fromCodePoint);
        assertEquals('z', alternative1.toCodePoint);
    }

    @Test
    void parse_mergeOverlappingCharacterRanges() {
        final ReGen reGen = ReGen.parse("[a-e0-4c2d-z3-9]");
        assertTrue(reGen.root instanceof Alternation);
        final Alternation root = (Alternation) reGen.root;
        assertEquals(2, root.alternatives.length);
        assertTrue(root.alternatives[0] instanceof CharacterRange);
        final CharacterRange alternative0 = (CharacterRange) root.alternatives[0];
        assertEquals('0', alternative0.fromCodePoint);
        assertEquals('9', alternative0.toCodePoint);
        assertTrue(root.alternatives[1] instanceof CharacterRange);
        final CharacterRange alternative1 = (CharacterRange) root.alternatives[1];
        assertEquals('a', alternative1.fromCodePoint);
        assertEquals('z', alternative1.toCodePoint);
    }

    @Test
    void parse_characterSetWithoutClosingBracket_throws() {
        assertThrows(IllegalArgumentException.class, () -> ReGen.parse("[")); // TODO check message
    }

    @Test
    void parse_characterSetWithoutClosingBracketAfterCharacter_throws() {
        assertThrows(IllegalArgumentException.class, () -> ReGen.parse("[a")); // TODO check message
    }

    @Test
    void parse_characterSetWithoutClosingBracketAfterDash_throws() {
        assertThrows(IllegalArgumentException.class, () -> ReGen.parse("[a-")); // TODO check message
    }

    @Test
    void parse_characterSetWithoutClosingBracketAfterCharacterRange_throws() {
        assertThrows(IllegalArgumentException.class, () -> ReGen.parse("[a-b")); // TODO check message
    }

    @Test
    void parse_characterSetWithMissingEscapedCharacter_throws() {
        assertThrows(IllegalArgumentException.class, () -> ReGen.parse("[\\")); // TODO check message
    }

    @Test
    void parse_characterSetWithUnexpectedEscapedCharacter_throws() {
        assertThrows(IllegalArgumentException.class, () -> ReGen.parse("[\\a]")); // TODO check message
    }

    @Test
    void parse_negatedCharacterSet_throws() {
        assertThrows(IllegalArgumentException.class, () -> ReGen.parse("[^a]")); // TODO check message
    }
}
