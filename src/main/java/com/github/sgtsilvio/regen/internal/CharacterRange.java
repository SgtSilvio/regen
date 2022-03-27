package com.github.sgtsilvio.regen.internal;

/**
 * @author Silvio Giebl
 */
public class CharacterRange implements RegexPart {

    final int fromCodePoint;
    final int toCodePoint;

    public CharacterRange(final int fromCodePoint, final int toCodePoint) {
        this.fromCodePoint = fromCodePoint;
        this.toCodePoint = toCodePoint;
    }

    @Override
    public int getQuantity() {
        return toCodePoint - fromCodePoint + 1;
    }
}
