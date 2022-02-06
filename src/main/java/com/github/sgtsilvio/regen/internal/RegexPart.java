package com.github.sgtsilvio.regen.internal;

import org.jetbrains.annotations.Unmodifiable;

/**
 * @author Silvio Giebl
 */
@Unmodifiable
public interface RegexPart {

    /**
     * @return the amount of strings that can be generated from this regex, {@link Integer#MAX_VALUE} means that this regex is treated as infinite.
     */
    int getQuantity();
}
