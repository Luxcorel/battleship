package org.example.Model;

/**
 * @author Johannes Rosengren Systemutvecklare HT22
 * @author Victor Pirojoc Systemutvecklare HT22
 * <p>
 * This class is responsible for keeping track of one entry's score
 */
public record Score(int movesRequiredToWin, String name) {

    /**
     * @return returns a string representation of the Score class.
     */
    @Override
    public String toString() {
        return String.format("%s: %d", name, movesRequiredToWin);
    }

}
