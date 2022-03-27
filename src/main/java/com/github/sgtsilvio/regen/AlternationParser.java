package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;

/**
 * @author Silvio Giebl
 */
class AlternationParser {

    static @NotNull Alternation parse(final @NotNull RegexPart part, final @NotNull ByteBuffer byteBuffer) {
        final LinkedList<RegexPart> alternatives = new LinkedList<>();
        alternatives.add(part);
        while (continueAlternation(byteBuffer)) {
            byteBuffer.position(byteBuffer.position() + 1);
            final RegexPart alternative = ConcatenationParser.parseWithoutAlternation(byteBuffer);
            if (alternative instanceof Alternation) { // flatten alternation
                Collections.addAll(alternatives, ((Alternation) alternative).alternatives);
            } else {
                alternatives.add(alternative);
            }
        }
        if (alternatives.size() < 2) {
            throw new IllegalArgumentException("alternation must have at least 2 alternatives"); // TODO message
        }
        return new Alternation(alternatives.toArray(new RegexPart[0]));
    }

    static boolean continueAlternation(final @NotNull ByteBuffer byteBuffer) {
        return byteBuffer.hasRemaining() && (byteBuffer.get(byteBuffer.position()) == '|');
    }
}
