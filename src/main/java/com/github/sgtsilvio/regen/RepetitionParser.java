package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * @author Silvio Giebl
 */
class RepetitionParser {

    static @NotNull Repetition parse(final @NotNull RegexPart part, final @NotNull ByteBuffer byteBuffer) {
        if (!byteBuffer.hasRemaining() || (byteBuffer.get() != '{')) {
            throw new IllegalArgumentException("repetition: expected '{'"); // TODO message
        }
        final int minRepetitions = parseMinRepetitions(byteBuffer);
        switch (byteBuffer.get()) {
            case '}':
                return Repetition.nTimes(part, minRepetitions, Integer.MAX_VALUE, false);
            case ',':
                final int maxRepetitions = parseMaxRepetitions(byteBuffer);
                switch (byteBuffer.get()) {
                    case '}':
                        return Repetition.nToMTimes(part, minRepetitions, maxRepetitions, Integer.MAX_VALUE);
                    case 'm':
                        final int maxQuantity = parseMaxQuantity(byteBuffer);
                        if (byteBuffer.get() == '}') {
                            return Repetition.nToMTimes(part, minRepetitions, maxRepetitions, maxQuantity);
                        }
                        throw new IllegalArgumentException("repetition: expected '}'"); // TODO message
                }
                throw new IllegalArgumentException("repetition: expected '}' or 'm'"); // TODO message
            case 'm':
                final int maxQuantity = parseMaxQuantity(byteBuffer);
                switch (byteBuffer.get()) {
                    case '}':
                        return Repetition.nTimes(part, minRepetitions, maxQuantity, false);
                    case 'r':
                        if (byteBuffer.get() == '}') {
                            return Repetition.nTimes(part, minRepetitions, maxQuantity, true);
                        }
                        throw new IllegalArgumentException("repetition: expected '}'"); // TODO message
                }
                throw new IllegalArgumentException("repetition: expected '}' or 'r'"); // TODO message
        }
        throw new IllegalArgumentException("repetition: expected '}', ',' or 'm'"); // TODO message
    }

    private static int parseMinRepetitions(final @NotNull ByteBuffer byteBuffer) {
        final int minRepetitions = parseNumber(byteBuffer);
        if (minRepetitions == -1) {
            throw new IllegalArgumentException("repetition: expected number for repetitions"); // TODO message
        }
        if (!byteBuffer.hasRemaining()) {
            throw new IllegalArgumentException("repetition: expected '}'"); // TODO message
        }
        return minRepetitions;
    }

    private static int parseMaxRepetitions(final @NotNull ByteBuffer byteBuffer) {
        int maxRepetitions = parseNumber(byteBuffer);
        if (maxRepetitions == -1) {
            maxRepetitions = Integer.MAX_VALUE;
        }
        if (!byteBuffer.hasRemaining()) {
            throw new IllegalArgumentException("repetition: expected '}'"); // TODO message
        }
        return maxRepetitions;
    }

    private static int parseMaxQuantity(final @NotNull ByteBuffer byteBuffer) {
        final int maxQuantity = parseNumber(byteBuffer);
        if (maxQuantity == -1) {
            throw new IllegalArgumentException("repetition: expected number for max quantity"); // TODO message
        }
        if (!byteBuffer.hasRemaining()) {
            throw new IllegalArgumentException("repetition: expected '}'"); // TODO message
        }
        return maxQuantity;
    }

    private static int parseNumber(final @NotNull ByteBuffer byteBuffer) {
        int number = -1;
        int i = byteBuffer.position();
        while (byteBuffer.hasRemaining()) {
            final byte b = byteBuffer.get(i);
            if ((b < '0') || (b > '9')) {
                break;
            }
            if (number == -1) {
                number = b - '0';
            } else {
                number = (number * 10) + (b - '0');
            }
            i++;
        }
        byteBuffer.position(i);
        return number;
    }
}
