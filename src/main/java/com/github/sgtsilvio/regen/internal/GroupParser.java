package com.github.sgtsilvio.regen.internal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author Silvio Giebl
 */
class GroupParser {

    static @NotNull RegexPart parse(final @NotNull ByteBuffer byteBuffer) {
        if (!byteBuffer.hasRemaining() || (byteBuffer.get() != '(')) {
            throw new IllegalArgumentException("group must start with '('"); // TODO message
        }
        final String name = parseName(byteBuffer);
        final RegexPart part = ConcatenationParser.parse(byteBuffer);
        if (!byteBuffer.hasRemaining() || (byteBuffer.get() != ')')) {
            throw new IllegalArgumentException("group must end with ')'"); // TODO message
        }
        return (name == null) ? part : new Group(part, name);
    }

    private static @Nullable String parseName(final @NotNull ByteBuffer byteBuffer) {
        if (byteBuffer.hasRemaining() && (byteBuffer.get(byteBuffer.position()) == '?')) {
            byteBuffer.position(byteBuffer.position() + 1);
            if (!byteBuffer.hasRemaining()) {
                throw new IllegalArgumentException("group must end with ')'"); // TODO message
            }
            final byte b = byteBuffer.get();
            switch (b) {
                case '<': // named group
                    final int nameStart = byteBuffer.position();
                    do {
                        if (!byteBuffer.hasRemaining()) {
                            throw new IllegalArgumentException("group name must end with '>'"); // TODO message
                        }
                    } while (byteBuffer.get() != '>');
                    final byte[] nameBytes = new byte[byteBuffer.position() - 1 - nameStart];
                    byteBuffer.position(nameStart);
                    byteBuffer.get(nameBytes);
                    byteBuffer.position(byteBuffer.position() + 1);
                    return new String(nameBytes, StandardCharsets.UTF_8);
                case ':': // non capturing group
                    return null;
                default:
                    throw new IllegalArgumentException("group must not start with '?" + (char) b + "'"); // TODO message
            }
        }
        return null;
    }
}
