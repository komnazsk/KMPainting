package org.github.komnazsk.kmpainting;

import org.bukkit.Art;

/**
 * Class for sequentially acquiring Enum type Art
 */
public class ArtCycler {

    /**
     * Returns the next Art value in the sequence.
     * If the current value is the last in the sequence, it wraps around to the first value.
     *
     * @param currentArt the current enum Art
     * @return the next enum Art in the sequence
     */
    public static Art getNext(Art currentArt) {
        Art[] values = Art.values();
        int nextIndex = (currentArt.ordinal() + 1) % values.length;
        return values[nextIndex];
    }

    /**
     * Returns the previous Art value in the sequence.
     * If the current value is the first in the sequence, it wraps around to the last value.
     *
     * @param currentArt the current Art value
     * @return the previous Art value in the sequence
     */
    public static Art getPrev(Art currentArt) {
        Art[] values = Art.values();
        int nextIndex = (currentArt.ordinal() - 1 + values.length) % values.length;
        return values[nextIndex];
    }

}
