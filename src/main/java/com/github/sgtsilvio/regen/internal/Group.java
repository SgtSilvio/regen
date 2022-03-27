package com.github.sgtsilvio.regen.internal;

import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
public class Group implements RegexPart {

    private final @NotNull RegexPart part;
    private final @NotNull String name;

    public Group(final @NotNull RegexPart part, final @NotNull String name) {
        this.part = part;
        this.name = name;
    }

    @Override
    public int getQuantity() {
        return part.getQuantity();
    }
}
