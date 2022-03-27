package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

/**
 * @author Silvio Giebl
 */
class ConstantParser {

    static @NotNull Constant parse(final @NotNull ByteBuffer byteBuffer) {
        int position = byteBuffer.position();
        int prevPosition = position;
        int escapedCharactersCount = 0;
        outer:
        while (position < byteBuffer.limit()) {
            final byte b = byteBuffer.get(position);
            int characterBytes = 1;
            if ((b & 0b1_0000000) == 0b0_0000000) { // 1 byte
                switch (b) {
                    // prefixes of standalone regex parts
                    case '(': // prefix of group
                    case '[': // prefix of character class
                    case '<': // prefix of number range
                        // postfixes of nestable standalone regex parts
                    case ')': // postfix of group
                        // infix operators
                    case '|': // alternation
                        break outer;
                    // multi character prefixes of standalone regex parts
                    case '\\':
                        if (position >= byteBuffer.limit()) {
                            throw new IllegalArgumentException("unexpected '\\' without following character"); // TODO message
                        }
                        switch (byteBuffer.get(position + 1)) {
                            case 'k': // prefix of reference
                                break outer;
                            case '\\':
                            case '(':
                            case ')':
                            case '[':
                            case '<':
                            case '|':
                            case '?':
                            case '*':
                            case '+':
                            case '{':
                            case '^': // no metacharacter in ReGen but in usual regex so allow to be compatible
                            case '$': // no metacharacter in ReGen but in usual regex so allow to be compatible
                            case '.': // no metacharacter in ReGen but in usual regex so allow to be compatible
                                escapedCharactersCount++;
                                characterBytes++; // skip next character because it is escaped
                                break;
                            default:
                                throw new IllegalArgumentException("unexpected escaped character"); // TODO message
                        }
                        break;
                    // prefixes of suffix regex parts
                    case '?': // prefix of repetition
                    case '*': // prefix of repetition
                    case '+': // prefix of repetition
                    case '{': // prefix of repetition
                        if (prevPosition > byteBuffer.position()) { // the constant consists of multiple characters
                            position = prevPosition; // end the constant before the last character which will then be parsed as repeated
                        } // else the constant only consists of one character that is repeated
                        break outer;
                }
            } else if ((b & 0b11_000000) == 0b10_000000) { // continuation byte
                throw new IllegalArgumentException("malformed UTF-8: continuation byte without starting byte"); // TODO message
            } else if ((b & 0b111_00000) == 0b110_00000) { // 2 bytes
                checkUtf8ContinuationByte(byteBuffer, position + 1);
                characterBytes += 1;
            } else if ((b & 0b1111_0000) == 0b1110_0000) { // 3 bytes
                checkUtf8ContinuationByte(byteBuffer, position + 1);
                checkUtf8ContinuationByte(byteBuffer, position + 2);
                characterBytes += 2;
            } else if ((b & 0b11111_000) == 0b11110_000) { // 4 bytes
                checkUtf8ContinuationByte(byteBuffer, position + 1);
                checkUtf8ContinuationByte(byteBuffer, position + 2);
                checkUtf8ContinuationByte(byteBuffer, position + 3);
                characterBytes += 3;
            } else {
                throw new IllegalArgumentException("malformed UTF-8 byte prefix"); // TODO message
            }
            prevPosition = position;
            position += characterBytes;
        }
        if (position == byteBuffer.position()) {
            return Constant.EMPTY;
        }
        final byte[] bytes = new byte[position - byteBuffer.position() - escapedCharactersCount];
        if (escapedCharactersCount == 0) {
            byteBuffer.get(bytes);
        } else { // filter escaped characters
            for (int i = 0; i < bytes.length; i++) {
                byte b = byteBuffer.get();
                if (b == '\\') {
                    b = byteBuffer.get();
                }
                bytes[i] = b;
            }
        }
        return new Constant(bytes);
    }

    private static void checkUtf8ContinuationByte(final @NotNull ByteBuffer byteBuffer, final int index) {
        if ((index >= byteBuffer.limit()) || ((byteBuffer.get(index) & 0b11_000000) != 0b10_000000)) {
            throw new IllegalArgumentException("malformed UTF-8: continuation byte missing"); // TODO message
        }
    }
}
