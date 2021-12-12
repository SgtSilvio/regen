package com.github.sgtsilvio.regen.internal;

/**
 * @author Silvio Giebl
 */
public class CharacterClass implements RegexPart {

    private final int fromCodePoint;
    private final int toCodePoint;

    public CharacterClass(final int fromCodePoint, final int toCodePoint) {
        this.fromCodePoint = fromCodePoint;
        this.toCodePoint = toCodePoint;
    }

    @Override
    public int getSize() {
        return toCodePoint - fromCodePoint + 1;
    }
}
