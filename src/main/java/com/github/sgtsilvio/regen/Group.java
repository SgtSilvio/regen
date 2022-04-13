package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
class Group implements RegexPart {

    private final int index;
    private final @NotNull String name;
    private final @NotNull RegexPart part;

    Group(final int index, final @NotNull String name, final @NotNull RegexPart part) {
        this.index = index;
        this.part = part;
        this.name = name;
    }

    @Override
    public int getQuantity() {
        return part.getQuantity();
    }
}
