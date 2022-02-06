package com.github.sgtsilvio.regen.internal;

/**
 * @author Silvio Giebl
 */
public class NumberRange implements RegexPart {

    private final int fromNumber;
    private final int toNumber;
    private final boolean leadingZeros;

    public NumberRange(final int fromNumber, final int toNumber, final boolean leadingZeros) {
        this.fromNumber = fromNumber;
        this.toNumber = toNumber;
        this.leadingZeros = leadingZeros;
    }

    @Override
    public int getQuantity() {
        return toNumber - fromNumber + 1;
    }
}
