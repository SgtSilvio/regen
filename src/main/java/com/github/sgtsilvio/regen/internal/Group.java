package com.github.sgtsilvio.regen.internal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Silvio Giebl
 */
public class Group implements RegexPart {

    private final @NotNull RegexPart @NotNull [] parts;
    private final @Nullable String name;
    private final int size;

    public Group(final @NotNull RegexPart @NotNull [] parts, final @Nullable String name) {
        this.parts = parts;
        this.name = name;
        this.size = getSize(parts);
    }

    public static int getSize(final @NotNull RegexPart @NotNull [] parts) {
        int size = 0;
        for (final RegexPart part : parts) {
            size += part.getSize();
        }
        return size;
    }

    @Override
    public int getSize() {
        return size;
    }
}