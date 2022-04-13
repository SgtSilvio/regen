package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author Silvio Giebl
 */
public class ReGen {

    public static @NotNull ReGen parse(final @NotNull String regex) {
        final byte[] regexBytes = regex.getBytes(StandardCharsets.UTF_8);
        final ByteBuffer byteBuffer = ByteBuffer.wrap(regexBytes);
        final ParseContext context = new ParseContext();
        final RegexPart part = ConcatenationParser.parse(byteBuffer, context);
        if (byteBuffer.hasRemaining()) {
            throw new IllegalArgumentException("unexpected character '" + byteBuffer.get() + "'"); // TODO message
        }
        context.validate();
        return new ReGen(part);
    }

    final @NotNull RegexPart root;

    private ReGen(final @NotNull RegexPart root) {
        this.root = root;
    }

//    public byte @NotNull [] generate(final int index) {
//        final int size = root.generatedSize(index);
//        final byte[] bytes = new byte[size];
//        root.generate(index, bytes, 0);
//        return bytes;
//    }

    // generate size, save group size information
    // generate bytes, save group position information, if necessary save reference position information
}
