package com.github.sgtsilvio.regen.internal;

import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * @author Silvio Giebl
 */
public class ConcatenationParser {

    public static @NotNull RegexPart parse(final @NotNull ByteBuffer byteBuffer) {
        final RegexPart part = parseWithoutAlternation(byteBuffer);
        if (AlternationParser.continueAlternation(byteBuffer)) {
            return AlternationParser.parse(part, byteBuffer);
        }
        return part;
    }

    static @NotNull RegexPart parseWithoutAlternation(final @NotNull ByteBuffer byteBuffer) {
        final LinkedList<RegexPart> parts = new LinkedList<>();
        while (continueConcatenation(byteBuffer)) {
            final RegexPart part = parseNext(byteBuffer);
            if (part instanceof Concatenation) { // flatten concatenation
                for (final RegexPart nestedPart : ((Concatenation) part).parts) {
                    add(parts, nestedPart);
                }
            } else {
                add(parts, part);
            }
        }
        switch (parts.size()) {
            case 0:
                return Constant.EMPTY;
            case 1:
                return parts.get(0);
        }
        return new Concatenation(parts.toArray(new RegexPart[0]));
    }

    private static boolean continueConcatenation(final @NotNull ByteBuffer byteBuffer) {
        if (!byteBuffer.hasRemaining()) {
            return false;
        }
        switch (byteBuffer.get(byteBuffer.position())) {
            case ')':
            case '|':
                return false;
        }
        return true;
    }

    private static @NotNull RegexPart parseNext(final @NotNull ByteBuffer byteBuffer) {
        return parseSuffixIfPresent(parseStandalone(byteBuffer), byteBuffer);
    }

    private static @NotNull RegexPart parseStandalone(final @NotNull ByteBuffer byteBuffer) {
        switch (byteBuffer.get(byteBuffer.position())) {
            case '(':
                return GroupParser.parse(byteBuffer);
            case '[':
                return CharacterSetParser.parse(byteBuffer);
            case '<':
                return NumberRangeParser.parse(byteBuffer);
            case '\\':
                if (byteBuffer.get(byteBuffer.position() + 1) == 'k') {
                    return ReferenceParser.parse(byteBuffer);
                }
                break;
            case '?':
            case '*':
            case '+':
            case '{':
                throw new IllegalArgumentException("unexpected repetition of empty regex part"); // TODO message
        }
        return ConstantParser.parse(byteBuffer);
    }

    private static @NotNull RegexPart parseSuffixIfPresent(
            final @NotNull RegexPart part, final @NotNull ByteBuffer byteBuffer) {

        if (byteBuffer.hasRemaining()) {
            switch (byteBuffer.get(byteBuffer.position())) {
                case '?':
                    byteBuffer.position(byteBuffer.position() + 1);
                    return Repetition.neverOrOnce(part);
                case '*':
                    byteBuffer.position(byteBuffer.position() + 1);
                    return Repetition.neverOrMore(part);
                case '+':
                    byteBuffer.position(byteBuffer.position() + 1);
                    return Repetition.onceOrMore(part);
                case '{':
                    return RepetitionParser.parse(part, byteBuffer);
            }
        }
        return part;
    }

    private static void add(final @NotNull LinkedList<@NotNull RegexPart> parts, final @NotNull RegexPart part) {
        if (part instanceof Constant) {
            final RegexPart prevPart = parts.peekLast();
            if (prevPart instanceof Constant) { // merge constants
                parts.removeLast();
                final byte[] value = ((Constant) part).value;
                final byte[] prevValue = ((Constant) prevPart).value;
                final byte[] mergedValue = new byte[value.length + prevValue.length];
                System.arraycopy(prevValue, 0, mergedValue, 0, prevValue.length);
                System.arraycopy(value, 0, mergedValue, prevValue.length, value.length);
                parts.add(new Constant(mergedValue));
                return;
            }
        }
        parts.add(part);
    }
}
