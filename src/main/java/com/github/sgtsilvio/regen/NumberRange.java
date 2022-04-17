package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
class NumberRange implements RegexPart {

    private final int fromNumber;
    private final int toNumber;
    private final int generatedSize;

    NumberRange(final int fromNumber, final int toNumber, final boolean leadingZeros) {
        this.fromNumber = fromNumber;
        this.toNumber = toNumber;
        this.generatedSize = leadingZeros ? generatedSizeOfNumber(toNumber) : 0;
    }

    @Override
    public int getQuantity() {
        return toNumber - fromNumber + 1;
    }

    @Override
    public int generatedSize(final int index) {
        final int number = fromNumber + index;
        if (number > toNumber) {
            throw new IllegalArgumentException("index too big"); // TODO message
        }
        return (generatedSize == 0) ? generatedSizeOfNumber(number) : generatedSize;
    }

    @Override
    public int generate(final int index, final byte @NotNull [] bytes, int end) {
        int number = fromNumber + index;
        if (number > toNumber) {
            throw new IllegalArgumentException("index too big"); // TODO message
        }
        int i = 0;
        while (number > 0) {
            bytes[end - i] = (byte) ('0' + number % 10);
            number /= 10;
            i++;
        }
        while (i < generatedSize) {
            bytes[end - i] = '0';
            i++;
        }
        return end - i;
    }

    private static int generatedSizeOfNumber(final int number) {
        int characterCount = 1;
        for (int rest = number / 10; rest > 0; rest /= 10) {
            characterCount++;
        }
        return characterCount;
    }
}
