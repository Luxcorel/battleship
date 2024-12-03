package org.example.Model;

import java.util.Objects;
import java.util.Random;

/**
 * @author Johannes Rosengren Systemutvecklare HT22
 * @author Victor Pirojoc Systemutvecklare HT22
 * <p>
 * This class is responsible for the game board.
 */
public class GameBoard {

    private final GenericShip[][] gameBoard;
    private int nbrOfShipsRemaining;
    private int nbrOfMoves;
    private long currentSeed;

    /**
     * Constructor for GameBoard with random seed. Initializes the number of ships, number of moves,
     * and the game board array.
     */
    public GameBoard() {
        nbrOfShipsRemaining = 5;
        nbrOfMoves = 0;
        gameBoard = new GenericShip[10][10];
        populateBoard();
    }

    /**
     * Constructor for GameBoard with specified seed. Initializes the number of ships, number of
     * moves, and the game board array.
     *
     * @param seed the seed to be used in the ship generation/placement method
     */
    public GameBoard(long seed) {
        nbrOfShipsRemaining = 5;
        nbrOfMoves = 0;
        gameBoard = new GenericShip[10][10];
        performPseudoRandomShipGenerationAndSubsequentlyPopulationOfGameBoard(seed);
    }

    /**
     * Returns the current game board's seed
     *
     * @return the current seed
     */
    public long getCurrentSeed() {
        return currentSeed;
    }

    /**
     * Returns the number of moves on the game board
     *
     * @return the number of moves that has occurred on the game board
     */
    public int getNbrOfMoves() {
        return nbrOfMoves;
    }

    /**
     * Increments the game board's move count by one
     */
    public void incrementMoves() {
        nbrOfMoves++;
    }

    /**
     * Delivers one hit to a ship. Returns true if the hit has sunk the ship else false. Method
     * requires coordinates containing a ship.
     *
     * @param row the row coordinate of the ship
     * @param col the col coordinate of the ship
     * @return returns true if ship hit has been sunk else false
     */
    //if GUI button is pressed that isn't mapped to null, run this method
    public boolean hitShip(int row, int col) {
        gameBoard[row][col].hit();
        if (gameBoard[row][col].isSunk()) {
            sinkOneShip();
            return true;
        }
        System.out.println("Hit ship at: " + row + " " + col);
        return false;
    }

    /**
     * Checks if a specified legal position contains a sunken ship
     *
     * @param row the row coordinate of the position to check
     * @param col the col coordinate of the position to check
     * @return returns true if a sunken ship exists at position else false
     */
    public boolean positionContainSunkenShip(int row, int col) {
        return gameBoard[row][col].isSunk();
    }

    /**
     * Returns the number of ships remaining on the game board
     *
     * @return the number of ships remaining
     */
    public int getNbrOfShipsRemaining() {
        return nbrOfShipsRemaining;
    }

    /**
     * Subtracts the number of ships remaining on the game board
     */
    public void sinkOneShip() {
        System.out.println("A SHIP HAS BEEN SUNK!");
        this.nbrOfShipsRemaining--;
    }

    /**
     * Runs the method
     * {@link GameBoard#performPseudoRandomShipGenerationAndSubsequentlyPopulationOfGameBoard} with
     * a null seed
     */
    public void populateBoard() {
        performPseudoRandomShipGenerationAndSubsequentlyPopulationOfGameBoard(null);
    }

    /**
     * Randomly calculates legal positions of ships and places them on the game board if there is
     * room. Can take null as an input seed for randomly generated seed to be used
     *
     * @param seedIn the seed to be used in generation of ships
     */
    private void performPseudoRandomShipGenerationAndSubsequentlyPopulationOfGameBoard(
        Long seedIn) {
        Random randomObject;

        if (Objects.nonNull(seedIn)) {
            randomObject = new Random(seedIn);
            this.currentSeed = seedIn;
            System.out.println("Using imported seed");
        } else {
            Random seedMachine = new Random();
            long randomSeed = seedMachine.nextLong();
            randomObject = new Random(randomSeed);
            this.currentSeed = randomSeed;
            System.out.println("Using random seed");
        }

        boolean ship1Created = false;
        boolean ship2Created = false;
        boolean ship3Created = false;
        boolean ship4Created = false;
        boolean ship5Created = false;

        //0 = starting with vertical, 1 = starting with horizontal
        int randomOrientation = randomObject.nextInt(2);
        ShipOrientation currentOrientation = randomOrientation == 0 ?
            ShipOrientation.VERTICAL :
            ShipOrientation.HORIZONTAL;
        int row;
        int col;

        while (!ship1Created) {
            row = randomObject.nextInt(10);
            col = randomObject.nextInt(10);
            //check if the position is empty
            if (Objects.isNull(gameBoard[row][col])) {
                gameBoard[row][col] = new Ship1();
                ship1Created = true;
            }
        }

        while (!ship2Created) {
            row = randomObject.nextInt(10);
            col = randomObject.nextInt(10);
            //check if start position is empty
            if (Objects.isNull(gameBoard[row][col])) {
                //figure whether to attempt vertical or horizontal placement
                if (currentOrientation == ShipOrientation.VERTICAL) {
                    //do validity check of offsets and check if they are empty
                    if (validityCheck(row, col, currentOrientation, 2)) {
                        if (Objects.isNull(gameBoard[row][col + 1])) {
                            GenericShip tmpHolderOfShip = new Ship2();
                            gameBoard[row][col] = tmpHolderOfShip;
                            gameBoard[row][col + 1] = tmpHolderOfShip;
                            currentOrientation = ShipOrientation.HORIZONTAL;
                            ship2Created = true;
                        }
                    }
                } else {
                    if (validityCheck(row, col, currentOrientation, 2)) {
                        if (Objects.isNull(gameBoard[row + 1][col])) {
                            GenericShip tmpHolderOfShip = new Ship2();
                            gameBoard[row][col] = tmpHolderOfShip;
                            gameBoard[row + 1][col] = tmpHolderOfShip;
                            currentOrientation = ShipOrientation.VERTICAL;
                            ship2Created = true;
                        }
                    }
                }
            }
        }

        while (!ship3Created) {
            row = randomObject.nextInt(10);
            col = randomObject.nextInt(10);
            //check if start position is empty
            if (Objects.isNull(gameBoard[row][col])) {
                //figure whether to attempt vertical or horizontal placement
                if (currentOrientation == ShipOrientation.VERTICAL) {
                    //do validity check of offsets and check if they are empty
                    if (validityCheck(row, col, currentOrientation, 3)) {
                        if (Objects.isNull(gameBoard[row][col + 1]) && Objects.isNull(
                            gameBoard[row][col + 2])) {
                            GenericShip tmpHolderOfShip = new Ship3();
                            gameBoard[row][col] = tmpHolderOfShip;
                            gameBoard[row][col + 1] = tmpHolderOfShip;
                            gameBoard[row][col + 2] = tmpHolderOfShip;
                            ship3Created = true;
                            currentOrientation = ShipOrientation.HORIZONTAL;
                        }
                    }
                } else {
                    //do validity check of offsets and check if they are empty
                    if (validityCheck(row, col, currentOrientation, 3)) {
                        if (Objects.isNull(gameBoard[row + 1][col]) && Objects.isNull(
                            gameBoard[row + 2][col])) {
                            GenericShip tmpHolderOfShip = new Ship3();
                            gameBoard[row][col] = tmpHolderOfShip;
                            gameBoard[row + 1][col] = tmpHolderOfShip;
                            gameBoard[row + 2][col] = tmpHolderOfShip;
                            ship3Created = true;
                            currentOrientation = ShipOrientation.VERTICAL;
                        }
                    }
                }
            }
        }

        while (!ship4Created) {
            row = randomObject.nextInt(10);
            col = randomObject.nextInt(10);
            //check if start position is empty
            if (Objects.isNull(gameBoard[row][col])) {
                //figure whether to attempt vertical or horizontal placement
                if (currentOrientation == ShipOrientation.VERTICAL) {
                    //do validity check of offsets and check if they are empty
                    if (validityCheck(row, col, currentOrientation, 4)) {
                        if (Objects.isNull(gameBoard[row][col + 1]) && Objects.isNull(
                            gameBoard[row][col + 2]) && Objects.isNull(gameBoard[row][col + 3])) {
                            GenericShip tmpHolderOfShip = new Ship4();
                            gameBoard[row][col] = tmpHolderOfShip;
                            gameBoard[row][col + 1] = tmpHolderOfShip;
                            gameBoard[row][col + 2] = tmpHolderOfShip;
                            gameBoard[row][col + 3] = tmpHolderOfShip;
                            currentOrientation = ShipOrientation.HORIZONTAL;
                            ship4Created = true;
                        }
                    }
                } else {
                    //do validity check of offsets and check if they are empty
                    if (validityCheck(row, col, currentOrientation, 4)) {
                        if (Objects.isNull(gameBoard[row + 1][col]) && Objects.isNull(
                            gameBoard[row + 2][col]) && Objects.isNull(gameBoard[row + 3][col])) {
                            GenericShip tmpHolderOfShip = new Ship4();
                            gameBoard[row][col] = tmpHolderOfShip;
                            gameBoard[row + 1][col] = tmpHolderOfShip;
                            gameBoard[row + 2][col] = tmpHolderOfShip;
                            gameBoard[row + 3][col] = tmpHolderOfShip;
                            currentOrientation = ShipOrientation.VERTICAL;
                            ship4Created = true;
                        }
                    }
                }
            }
        }

        while (!ship5Created) {
            row = randomObject.nextInt(10);
            col = randomObject.nextInt(10);
            //check if start position is empty
            if (Objects.isNull(gameBoard[row][col])) {
                //figure whether to attempt vertical or horizontal placement
                if (currentOrientation == ShipOrientation.VERTICAL) {
                    //do validity check of offsets and check if they are empty
                    if (validityCheck(row, col, currentOrientation, 5)) {
                        if (Objects.isNull(gameBoard[row][col + 1]) && Objects.isNull(
                            gameBoard[row][col + 2]) && Objects.isNull(gameBoard[row][col + 3])
                            && Objects.isNull(gameBoard[row][col + 4])) {
                            GenericShip tmpHolderOfShip = new Ship5();
                            gameBoard[row][col] = tmpHolderOfShip;
                            gameBoard[row][col + 1] = tmpHolderOfShip;
                            gameBoard[row][col + 2] = tmpHolderOfShip;
                            gameBoard[row][col + 3] = tmpHolderOfShip;
                            gameBoard[row][col + 4] = tmpHolderOfShip;
                            currentOrientation = ShipOrientation.HORIZONTAL;
                            ship5Created = true;
                        }
                    }
                } else {
                    //do validity check of offsets and check if they are empty
                    if (validityCheck(row, col, currentOrientation, 5)) {
                        if (Objects.isNull(gameBoard[row + 1][col]) && Objects.isNull(
                            gameBoard[row + 2][col]) && Objects.isNull(gameBoard[row + 3][col])
                            && Objects.isNull(gameBoard[row + 4][col])) {
                            GenericShip tmpHolderOfShip = new Ship5();
                            gameBoard[row][col] = tmpHolderOfShip;
                            gameBoard[row + 1][col] = tmpHolderOfShip;
                            gameBoard[row + 2][col] = tmpHolderOfShip;
                            gameBoard[row + 3][col] = tmpHolderOfShip;
                            gameBoard[row + 4][col] = tmpHolderOfShip;
                            currentOrientation = ShipOrientation.VERTICAL;
                            ship5Created = true;
                        }
                    }
                }
            }
        }


    }

    /**
     * Perform a check to see if a coordinate and its offsets are within range of the game board
     * array
     *
     * @param row          the row start position
     * @param col          the col start position
     * @param orientation  the orientation of the ship
     * @param lengthOfShip the number of offsets to be checked
     * @return true if all positions are within range of the array else false
     */
    private boolean validityCheck(int row, int col, ShipOrientation orientation, int lengthOfShip) {
        int positionsThatPassedCheck = 0;

        if (orientation == ShipOrientation.HORIZONTAL) {
            for (int i = 0; i < lengthOfShip; i++) {
                if ((row + i) >= 0 && (row + i) <= 9) {
                    if (col >= 0 && col <= 9) {
                        positionsThatPassedCheck++;
                    }
                }
            }
        } else if (orientation == ShipOrientation.VERTICAL) {
            for (int i = 0; i < lengthOfShip; i++) {
                if (row >= 0 && row <= 9) {
                    if ((col + i) >= 0 && (col + i) <= 9) {
                        positionsThatPassedCheck++;
                    }
                }
            }
        }

        return positionsThatPassedCheck == lengthOfShip;
    }

    /**
     * Checks whether a position within the array contains a ship. Can only check values that are in
     * range of the array.
     *
     * @param row the row coordinate
     * @param col the col coordinate
     * @return true if the game board array at position is occupied else false
     */
    //can only check legal positions
    public boolean positionExists(int row, int col) {
        return (gameBoard[row][col] != null);
    }

    /**
     * Returns the type of ship that is occupying a certain spot on the array. Can only be run on
     * positions that contain a ship
     *
     * @param row the row coordinate
     * @param col the col coordinate
     * @return the shipType of a certain location
     */
    public ShipType getShipType(int row, int col) {
        return gameBoard[row][col].getType();
    }
}
