package com.github.sgtsilvio.regen.internal;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * @author Silvio Giebl
 */
class NumberRangeParser {

    static @NotNull NumberRange parse(final @NotNull ByteBuffer byteBuffer) {
        if (!byteBuffer.hasRemaining() || (byteBuffer.get() != '<')) {
            throw new IllegalArgumentException("number range must start with '<'"); // TODO message
        }
        int from = 0;
        boolean leadingZeros = false;
        while (true) {
            if (!byteBuffer.hasRemaining()) {
                throw new IllegalArgumentException("number range not complete"); // TODO message
            }
            final byte b = byteBuffer.get();
            if (b == ',') {
                break;
            }
            if ((b < '0') || (b > '9')) {
                throw new IllegalArgumentException("number range from number must not contain '" + (char) b + "'"); // TODO message
            }
            if ((from == 0) && (b == '0')) {
                if (leadingZeros) {
                    throw new IllegalArgumentException("number range from number must not start with '00'"); // TODO message
                } else {
                    leadingZeros = true;
                }
            }
            from = (from * 10) + (b - '0');
        }
        int to = 0;
        while (true) {
            if (!byteBuffer.hasRemaining()) {
                throw new IllegalArgumentException("number range not complete"); // TODO message
            }
            final byte b = byteBuffer.get();
            if (b == '>') {
                break;
            }
            if ((b < '0') || (b > '9')) {
                throw new IllegalArgumentException("number range to number must not contain '" + (char) b + "'"); // TODO message
            }
            if ((to == 0) && (b == '0')) {
                throw new IllegalArgumentException("number range to number must not start with '0'"); // TODO message
            }
            to = (to * 10) + (b - '0');
        }
        if (from > to) {
            throw new IllegalArgumentException("number range from number (" + from + ") must be smaller than to number (" + to + ")"); // TODO message
        }
        return new NumberRange(from, to, leadingZeros);
    }
}
