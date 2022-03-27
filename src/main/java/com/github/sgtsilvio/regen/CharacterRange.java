package com.github.sgtsilvio.regen;

/**
 * @author Silvio Giebl
 */
class CharacterRange implements RegexPart {

    final int fromCodePoint;
    final int toCodePoint;

    CharacterRange(final int fromCodePoint, final int toCodePoint) {
        this.fromCodePoint = fromCodePoint;
        this.toCodePoint = toCodePoint;
    }

    @Override
    public int getQuantity() {
        return toCodePoint - fromCodePoint + 1;
    }
}
