package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
class Reference implements RegexPart {

    private final @NotNull String name;

    Reference(final @NotNull String name) {
        this.name = name;
    }

//    private final @NotNull Group group;
//
//    Reference(final @NotNull Group group) {
//        this.group = group;
//    }

    @Override
    public int getQuantity() {
        return 1;
    }
}
