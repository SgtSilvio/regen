package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * @author Silvio Giebl
 */
class CharacterSetParser {

    static @NotNull RegexPart parse(final @NotNull ByteBuffer byteBuffer) {
        if (!byteBuffer.hasRemaining() || (byteBuffer.get() != '[')) {
            throw new IllegalArgumentException("repetition: expected '['"); // TODO message
        }
        final TreeSet<CharacterRange> alternatives = new TreeSet<>(Comparator.comparingInt(o -> o.fromCodePoint));
        if (!byteBuffer.hasRemaining()) {
            throw new IllegalArgumentException("character class: expected at least one character and ']'"); // TODO message
        }
        int fromCodePoint = parseCodePoint(byteBuffer);
        switch (fromCodePoint) {
            case '^':
                throw new IllegalArgumentException("ReGen does not support negated character classes"); // TODO message
            case '\\':
                fromCodePoint = parseEscapedCharacter(byteBuffer);
                break;
        }
        outer:
        while (byteBuffer.hasRemaining()) {
            final int codePoint = parseCodePoint(byteBuffer);
            switch (codePoint) {
                case ']':
                    alternatives.add(new CharacterRange(fromCodePoint, fromCodePoint));
                    break outer;
                case '-':
                    if (!byteBuffer.hasRemaining()) {
                        throw new IllegalArgumentException("character class: expected ']'"); // TODO message
                    }
                    int toCodePoint = parseCodePoint(byteBuffer);
                    switch (toCodePoint) {
                        case ']':
                            alternatives.add(new CharacterRange(fromCodePoint, fromCodePoint));
                            alternatives.add(new CharacterRange('-', '-'));
                            break outer;
                        case '\\':
                            toCodePoint = parseEscapedCharacter(byteBuffer);
                            break;
                    }
                    alternatives.add(new CharacterRange(fromCodePoint, toCodePoint));
                    if (!byteBuffer.hasRemaining()) {
                        throw new IllegalArgumentException("character class: expected ']'"); // TODO message
                    }
                    fromCodePoint = parseCodePoint(byteBuffer);
                    switch (fromCodePoint) {
                        case ']':
                            break outer;
                        case '\\':
                            fromCodePoint = parseEscapedCharacter(byteBuffer);
                            break;
                    }
                    break;
                case '\\':
                    alternatives.add(new CharacterRange(fromCodePoint, fromCodePoint));
                    fromCodePoint = parseEscapedCharacter(byteBuffer);
                    break;
                default:
                    alternatives.add(new CharacterRange(fromCodePoint, fromCodePoint));
                    fromCodePoint = codePoint;
            }
        }
        if (alternatives.size() < 2) {
            return alternatives.first();
        }
        return mergeAlternatives(alternatives);
//        Pattern.compile("[\\(\\)\\[\\]\\|\\?\\*\\+\\{\\}\\\\\\^\\$\\.\\-]"); // TODO remove
    }

    private static int parseCodePoint(final @NotNull ByteBuffer byteBuffer) {
        final byte b = byteBuffer.get();
        if ((b & 0b1_0000000) == 0b0_0000000) { // 1 byte
            return b;
        } else if ((b & 0b11_000000) == 0b10_000000) { // continuation byte
            throw new IllegalArgumentException("malformed UTF-8: continuation byte without starting byte"); // TODO message
        } else if ((b & 0b111_00000) == 0b110_00000) { // 2 bytes
            return ((b & 0b000_11111) << 6) + parseUtf8ContinuationByte(byteBuffer);
        } else if ((b & 0b1111_0000) == 0b1110_0000) { // 3 bytes
            return ((b & 0b000_11111) << 12) +
                    (parseUtf8ContinuationByte(byteBuffer) << 6) +
                    parseUtf8ContinuationByte(byteBuffer);
        } else if ((b & 0b11111_000) == 0b11110_000) { // 4 bytes
            return ((b & 0b000_11111) << 18) +
                    (parseUtf8ContinuationByte(byteBuffer) << 12) +
                    (parseUtf8ContinuationByte(byteBuffer) << 6) +
                    parseUtf8ContinuationByte(byteBuffer);
        } else {
            throw new IllegalArgumentException("malformed UTF-8 byte prefix"); // TODO message
        }
    }

    private static int parseUtf8ContinuationByte(final @NotNull ByteBuffer byteBuffer) {
        final byte b = byteBuffer.get();
        if (!byteBuffer.hasRemaining() || ((b & 0b11_000000) != 0b10_000000)) {
            throw new IllegalArgumentException("malformed UTF-8: continuation byte missing"); // TODO message
        }
        return b & 0b00_111111;
    }

    private static int parseEscapedCharacter(final @NotNull ByteBuffer byteBuffer) {
        if (!byteBuffer.hasRemaining()) {
            throw new IllegalArgumentException("unexpected '\\' without following character"); // TODO message
        }
        final byte b = byteBuffer.get();
        switch (b) {
            case '\\':
            case ']':
            case '-':
            case '^':
                return b;
        }
        throw new IllegalArgumentException("unexpected escaped character"); // TODO message
    }

    private static @NotNull RegexPart mergeAlternatives(final @NotNull TreeSet<CharacterRange> alternatives) {
        final LinkedList<RegexPart> mergedAlternatives = new LinkedList<>();
        final Iterator<CharacterRange> iterator = alternatives.iterator();
        CharacterRange lastAlternative = iterator.next();
        while (iterator.hasNext()) {
            final CharacterRange alternative = iterator.next();
            if ((lastAlternative.toCodePoint + 1) >= alternative.fromCodePoint) {
                lastAlternative = new CharacterRange(
                        lastAlternative.fromCodePoint, Math.max(lastAlternative.toCodePoint, alternative.toCodePoint));
            } else {
                mergedAlternatives.add(lastAlternative);
                lastAlternative = alternative;
            }
        }
        mergedAlternatives.add(lastAlternative);
        // TODO replace single char CharacterRange with Constant
        if (mergedAlternatives.size() == 1) {
            return mergedAlternatives.getFirst();
        }
        return new Alternation(mergedAlternatives.toArray(new RegexPart[0]));
    }
}
