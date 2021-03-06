package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

/**
 * @author Silvio Giebl
 */
@Unmodifiable
interface RegexPart {

    /**
     * @return the amount of strings that can be generated from this regex, {@link Integer#MAX_VALUE} means that this regex is treated as infinite.
     */
    int getQuantity();

    int generatedSize(int index);

    int generate(int index, byte @NotNull [] bytes, int end);
}
