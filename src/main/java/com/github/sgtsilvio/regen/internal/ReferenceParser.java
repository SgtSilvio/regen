package com.github.sgtsilvio.regen.internal;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author Silvio Giebl
 */
class ReferenceParser {

    static @NotNull Reference parse(final @NotNull ByteBuffer byteBuffer) {
        if (!byteBuffer.hasRemaining() || byteBuffer.get() != '\\') {
            throw new IllegalArgumentException("reference must start with '\\'"); // TODO message
        }
        if (!byteBuffer.hasRemaining() || byteBuffer.get() != 'k') {
            throw new IllegalArgumentException("reference must start with '\\k'"); // TODO message
        }
        if (!byteBuffer.hasRemaining() || byteBuffer.get() != '<') {
            throw new IllegalArgumentException("reference must start with '\\k<'"); // TODO message
        }
        final int nameStart = byteBuffer.position();
        do {
            if (!byteBuffer.hasRemaining()) {
                throw new IllegalArgumentException("reference must end with '>'"); // TODO message
            }
        } while (byteBuffer.get() != '>');
        final byte[] nameBytes = new byte[byteBuffer.position() - 1 - nameStart];
        byteBuffer.position(nameStart);
        byteBuffer.get(nameBytes);
        byteBuffer.position(byteBuffer.position() + 1);
        final String name = new String(nameBytes, StandardCharsets.UTF_8);
        return new Reference(name);
    }
}
