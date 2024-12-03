package org.example.Controller;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;
import org.example.Model.GameBoard;
import org.example.Model.Score;
import org.example.Model.ShipType;
import org.example.View.Menu;
import org.example.View.PlayerBoard;
import org.example.View.ShowStats;

/**
 * @author Johannes Rosengren Systemutvecklare HT22
 * @author Victor Pirojoc Systemutvecklare HT22
 * <p>
 * Responsible for interaction between classes in the Model and View packages
 */
public class Controller {

    private GameBoard leftBoard;
    private GameBoard rightBoard;
    private final Menu mainMenu;

    /**
     * Instructor of the Controller class Creates a new instance of the Menu class
     */
    public Controller() {
        mainMenu = new Menu(this);
    }

    /**
     * Initializes/resets the two game boards
     */
    public void initNewGameBoards() {
        leftBoard = new GameBoard();
        rightBoard = new GameBoard();
    }

    /**
     * Saves a game-state to disk
     *
     * @param leftBoardMovesToSave  a boolean array that contains "pressed buttons" of the left
     *                              board
     * @param rightBoardMovesToSave boolean array that contains "pressed buttons" of the right
     *                              board
     * @return returns true if save is successful else false
     */
    public boolean saveGameToDisk(boolean[][] leftBoardMovesToSave,
        boolean[][] rightBoardMovesToSave) {
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter("savedLeftBoardMoves.gson");
            myWriter.write(new Gson().toJson(leftBoardMovesToSave));
            myWriter.close();
            System.out.println("Successfully wrote LEFT board moves to the file.");

            myWriter = new FileWriter("savedRightBoardMoves.gson");
            myWriter.write(new Gson().toJson(rightBoardMovesToSave));
            myWriter.close();
            System.out.println("Successfully wrote RIGHT board moves to the file.");

            myWriter = new FileWriter("savedLeftBoardSeed.gson");
            myWriter.write(new Gson().toJson(leftBoard.getCurrentSeed()));
            myWriter.close();
            System.out.println("Successfully wrote LEFT board seed to the file.");

            myWriter = new FileWriter("savedRightBoardSeed.gson");
            myWriter.write(new Gson().toJson(rightBoard.getCurrentSeed()));
            myWriter.close();
            System.out.println("Successfully wrote Right board seed to the file.");

            return true;
        } catch (IOException e) {
            System.out.println("Controller.saveGameToDisk() - An error occurred.");
            return false;
        } finally {
            try {
                if (myWriter != null) {
                    myWriter.close();
                }
            } catch (IOException e) {
                System.out.println("Controller.saveGameToDisk() - An error occurred.");
            }
        }
    }

    /**
     * Loads a game board state from disk
     */
    public void loadGameFromDisk() {
        try {
            //check if the file exists
            if ((new File("savedLeftBoardMoves.gson").isFile()) && (new File(
                "savedRightBoardMoves.gson").isFile()) && (new File(
                "savedLeftBoardSeed.gson").isFile()) && (new File(
                "savedRightBoardSeed.gson").isFile())) {
                File leftBoardFile = new File("savedLeftBoardMoves.gson");
                File rightBoardFile = new File("savedRightBoardMoves.gson");
                File leftBoardLayout = new File("savedLeftBoardSeed.gson");
                File rightBoardLayout = new File("savedRightBoardSeed.gson");

                boolean[][] leftMoves = new boolean[10][10];
                boolean[][] rightMoves = new boolean[10][10];
                Long leftPlayerSeed = null;
                Long rightPlayerSeed = null;

                Scanner reader = new Scanner(leftBoardFile);
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    leftMoves = new Gson().fromJson(data, boolean[][].class);
                }
                reader.close();

                reader = new Scanner(rightBoardFile);
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    rightMoves = new Gson().fromJson(data, boolean[][].class);
                }
                reader.close();

                reader = new Scanner(leftBoardLayout);
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    leftPlayerSeed = new Gson().fromJson(data, Long.class);
                }
                reader.close();

                reader = new Scanner(rightBoardLayout);
                while (reader.hasNextLine()) {
                    String data = reader.nextLine();
                    rightPlayerSeed = new Gson().fromJson(data, Long.class);
                }
                reader.close();

                leftBoard = new GameBoard(leftPlayerSeed);
                rightBoard = new GameBoard(rightPlayerSeed);
                PlayerBoard continuePlay = new PlayerBoard(this, leftMoves, rightMoves);
                continuePlay.setVisible(true);
            } else {
                mainMenu.showMessage("Could not load from file!");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Controller.loadGameFromDisk() - An error occurred.");
        }
    }

    /**
     * Writes the top ten score entries to disk
     *
     * @param scoreToPotentiallyAdd a score object to potentially add to the score list
     */
    public void potentiallyAddScoreToScoresList(Score scoreToPotentiallyAdd) {
        if (Objects.nonNull(scoreToPotentiallyAdd.name())) {
            ArrayList<Score> tmpSortList = getScoreObjectsFromDisk();
            tmpSortList.add(scoreToPotentiallyAdd);
            tmpSortList.sort(Comparator.comparingInt(Score::movesRequiredToWin));
            if (tmpSortList.size() == 11) {
                tmpSortList.remove(10);
            }
            writeSortedScoresListToDisk(tmpSortList);
        }
    }

    /**
     * Creates a new file for storing score objects
     */
    public void createNewScoresOnDisk() {
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter("scores.gson");
            myWriter.write("");
        } catch (IOException e) {
            System.out.println("Controller.createNewScoresOnDisk() - An error occurred.");
        } finally {
            try {
                if (myWriter != null) {
                    myWriter.close();
                }
            } catch (IOException e) {
                System.out.println("Controller.createNewScoresOnDisk() - An error occurred.");
            }
        }
    }

    /**
     * Writes Arraylist containing Score objects to disk
     *
     * @param scoresInput Score ArrayList to write to disk
     */
    public void writeSortedScoresListToDisk(ArrayList<Score> scoresInput) {
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter("scores.gson");
            for (Score x : scoresInput) {
                myWriter.write(new Gson().toJson(x) + "\n");
            }
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("Controller.writeSortedScoresListToDisk() - An error occurred.");
        } finally {
            try {
                if (myWriter != null) {
                    myWriter.close();
                }
            } catch (IOException e) {
                System.out.println("Controller.writeSortedScoresListToDisk() - An error occurred.");
            }
        }
    }

    /**
     * @return the Score objects saved on disk
     */
    private ArrayList<Score> getScoreObjectsFromDisk() {
        ArrayList<Score> scoresListOutput = new ArrayList<>();

        try {
            //check if scores file exists, if none exists create one
            if (!(new File("scores.gson").isFile())) {
                createNewScoresOnDisk();
            }
            File fileToRead = new File("scores.gson");
            Scanner reader = new Scanner(fileToRead);
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                scoresListOutput.add(new Gson().fromJson(data, Score.class));
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Controller.readScoresListFromDisk() - An error occurred.");
        }
        return scoresListOutput;
    }

    /**
     * @return a list with the Score objects string representation
     */
    public ArrayList<String> getScoreObjectsFromDiskAsStrings() {
        ArrayList<Score> inputList = getScoreObjectsFromDisk();

        ArrayList<String> outputList = new ArrayList<>();

        for (Score currentScore : inputList) {
            outputList.add(currentScore.toString());
        }

        return outputList;
    }

    /**
     * Delivers one hit to a ship. Returns true if the hit has sunk the ship else false
     *
     * @param isRightBoard if it's the right game board
     * @param row          the row coordinate of the ship
     * @param col          the col coordinate of the ship
     * @return returns true if ship hit has been sunk else false
     */
    public boolean hitShipSunk(boolean isRightBoard, int row, int col) {
        if (isRightBoard) {
            return rightBoard.hitShip(row, col);
        } else {
            return leftBoard.hitShip(row, col);
        }
    }

    /**
     * Checks whether ship objects exist at specified legal coordinates.
     *
     * @param isRightBoard if it's the right game board
     * @param row          the row coordinate of the position to check
     * @param col          the col coordinate of the position to check
     * @return returns true if ship exists at position else false
     */
    public boolean shipExistsAt(boolean isRightBoard, int row, int col) {
        if (isRightBoard) {
            return rightBoard.positionExists(row, col);
        } else {
            return leftBoard.positionExists(row, col);
        }
    }

    /**
     * Checks if a specified legal position contains a sunk ship
     *
     * @param row          the row coordinate of the position to check
     * @param col          the col coordinate of the position to check
     * @param isRightBoard if it's the right game board
     * @return returns true if a sunken ship exists at position else false
     */
    //
    public boolean positionContainSunkenShip(int row, int col, boolean isRightBoard) {
        if (isRightBoard) {
            return rightBoard.positionContainSunkenShip(row, col);
        } else {
            return leftBoard.positionContainSunkenShip(row, col);
        }
    }

    /**
     * Checks whether player 2 has won the game
     *
     * @return returns true if the right player has won
     */
    public boolean checkIfRightPlayerWon() {
        return leftBoard.getNbrOfShipsRemaining() == 0;
    }

    /**
     * Checks whether player 1 has won the game
     *
     * @return returns true if left player has won
     */
    public boolean leftPlayerWon() {
        return rightBoard.getNbrOfShipsRemaining() == 0;
    }

    /**
     * Takes a name from the winner of the game and adds it to the scoreboard if it is in the top
     * 10
     *
     * @param name            the name of the entry
     * @param nbrOfMovesToWin the number of moves required to win
     */
    public void letWinnerEnterNameToPotentiallyAddToScoreboard(String name, int nbrOfMovesToWin) {
        potentiallyAddScoreToScoresList(new Score(nbrOfMovesToWin, name));
    }

    /**
     * Returns the number of moves for specific player
     *
     * @param player2 if it is player 2 or not
     * @return returns the number of moves for the specified player
     */
    public int getMovesRequiredToWin(boolean player2) {
        if (player2) {
            return leftBoard.getNbrOfMoves();
        } else {
            return rightBoard.getNbrOfMoves();
        }
    }

    /**
     * Increments player 1's score by one
     */
    public void incrementPlayer1Score() {
        rightBoard.incrementMoves();
    }

    /**
     * Increments player 2's score by one
     */
    public void incrementPlayer2Score() {
        leftBoard.incrementMoves();
    }

    /**
     * Takes as input legal coordinates of a position that contains a ship object
     *
     * @param row          the row coordinate of the position to check a ship type of
     * @param col          the col coordinate of the position to check a ship type of
     * @param isRightBoard if it's the right game board
     * @return the shipType of the specified position
     */
    public ShipType getShipTypeAt(int row, int col, boolean isRightBoard) {
        if (isRightBoard) {
            return rightBoard.getShipType(row, col);
        } else {
            return leftBoard.getShipType(row, col);
        }
    }

    /**
     * Creates a new instance of the scoreboard class and populates it with score board data from
     * disk
     */
    public void showStats() {
        new ShowStats(getScoreObjectsFromDiskAsStrings().toArray(new String[0]));
    }
}
