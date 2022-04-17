package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
class CharacterRange implements RegexPart {

    final int fromCodePoint;
    final int toCodePoint;

    CharacterRange(final int fromCodePoint, final int toCodePoint) {
        this.fromCodePoint = fromCodePoint;
        this.toCodePoint = toCodePoint;
    }

    @Override
    public int getQuantity() {
        return toCodePoint - fromCodePoint + 1;
    }

    @Override
    public int generatedSize(final int index) {
        final int codePoint = fromCodePoint + index;
        if (codePoint > toCodePoint) {
            throw new IllegalArgumentException("index too big"); // TODO message
        }
        if (codePoint < 0x80) {
            return 1;
        }
        if (codePoint < 0x800) {
            return 2;
        }
        if (codePoint < 0x10000) {
            return 3;
        }
        return 4;
    }

    @Override
    public int generate(final int index, final byte @NotNull [] bytes, final int end) {
        final int codePoint = fromCodePoint + index;
        if (codePoint > toCodePoint) {
            throw new IllegalArgumentException("index too big"); // TODO message
        }
        if (codePoint < 0x80) {
            bytes[end] = (byte) codePoint;
            return end - 1;
        }
        if (codePoint < 0x800) {
            bytes[end - 1] = (byte) (0b110_00000 | (codePoint >> 6));
            bytes[end] = (byte) (0b10_000000 | (codePoint & 0b111111));
            return end - 2;
        }
        if (codePoint < 0x10000) {
            bytes[end - 2] = (byte) (0b1110_0000 | (codePoint >> 12));
            bytes[end - 1] = (byte) (0b10_000000 | ((codePoint >> 6) & 0b111111));
            bytes[end] = (byte) (0b10_000000 | (codePoint & 0b111111));
            return end - 3;
        }
        bytes[end - 3] = (byte) (0b11110_000 | (codePoint >> 18));
        bytes[end - 2] = (byte) (0b10_000000 | ((codePoint >> 12) & 0b111111));
        bytes[end - 1] = (byte) (0b10_000000 | ((codePoint >> 6) & 0b111111));
        bytes[end] = (byte) (0b10_000000 | (codePoint & 0b111111));
        return end - 4;
    }
}
