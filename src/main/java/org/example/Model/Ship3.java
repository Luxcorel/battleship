package org.example.Model;

/**
 * @author Johannes Rosengren Systemutvecklare HT22
 * @author Victor Pirojoc Systemutvecklare HT22
 * <p>
 * Implementation of GenericShip with ship length 3
 */
public class Ship3 implements GenericShip {

    private final ShipType shipType = ShipType.JAGARE;
    private int parts = 3;

    /**
     * Subtracts one "part" from the ship
     */
    @Override
    public void hit() {
        parts--;
    }

    /**
     * Checks whether there are more parts of the ship remaining
     *
     * @return true if no parts remain else false
     */
    @Override
    public boolean isSunk() {
        return parts < 1;
    }

    /**
     * Returns the ship's type
     *
     * @return the type of ship
     */
    @Override
    public ShipType getType() {
        return shipType;
    }


}