package com.github.sgtsilvio.regen.internal;

import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
public class Reference implements RegexPart {

    private final @NotNull String name;

    public Reference(final @NotNull String name) {
        this.name = name;
    }

//    private final @NotNull Group group;
//
//    public Reference(final @NotNull Group group) {
//        this.group = group;
//    }

    @Override
    public int getQuantity() {
        return 1;
    }
}
