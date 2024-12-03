package org.example.View;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import org.example.Controller.Controller;
import org.example.Model.AlgoMode;
import org.example.Model.Player;

/**
 * @author Johannes Rosengren Systemutvecklare HT22
 * @author Victor Pirojoc Systemutvecklare HT22
 * <p>
 * This class is responsible for managing the GUI part of the game board
 */
public class PlayerBoard extends JFrame implements ActionListener {

    private final Controller controller;

    private final Random random;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel centerPanel;
    private JButton save;
    private JButton quit;
    private JButton stats;

    private Player currentPlayer = Player.PLAYER1;
    private final boolean isMultiplayer;
    private boolean isGameWon;

    // Arrays for the left and right board
    private final JButton[][] leftBoardButtons = new JButton[10][10];
    private final boolean[][] leftBoardState = new boolean[10][10];
    private final JButton[][] rightBoardButtons = new JButton[10][10];
    private final boolean[][] rightBoardState = new boolean[10][10];

    // Variables for the algorithm
    boolean isAlgoEnabled;
    private int verticalHuntOffset = 0;
    private int horizontalHuntOffset = 0;
    private AlgoMode algoMode = AlgoMode.HUNT;
    private int firstShipPartHitRow = -1;
    private int firstShipPartHitCol = -1;
    private int secondShipPartHitRow = -1;
    private int secondShipPartHitCol = -1;
    private boolean ship1Left = true;
    private boolean ship2Left = true;
    private boolean ship3Left = true;
    private boolean ship4Left = true;
    private boolean ship5Left = true;

    /**
     * Constructor for launching new game
     *
     * @param controller    an instance of the Controller class
     * @param isMultiplayer whether the game will be multiplayer
     * @param isAlgoEnabled whether to use algo or not
     */
    public PlayerBoard(Controller controller, boolean isMultiplayer, boolean isAlgoEnabled) {
        this.random = new Random();
        this.controller = controller;
        this.isMultiplayer = isMultiplayer;
        this.isAlgoEnabled = isAlgoEnabled;

        this.setLayout(null);
        this.setUndecorated(true);
        this.setSize(1040, 600);
        this.setLocationRelativeTo(null);
        this.setTitle("BattleShip");
        this.setResizable(false);
        getContentPane().setBackground(new Color(34, 37, 43));
        this.setVisible(true);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents(isMultiplayer);
        setupLeftBoard();
        setupRightBoard();
    }

    /**
     * Constructor for starting from a previous game session
     *
     * @param controller        an instance of the Controller class
     * @param movesOnLeftPanel  a boolean array containing the pressed buttons on the left board
     * @param movesOnRightPanel a boolean array containing the pressed buttons on the right board
     */
    //constructor for loading a saved game
    public PlayerBoard(Controller controller, boolean[][] movesOnLeftPanel,
        boolean[][] movesOnRightPanel) {
        this.random = new Random();
        this.controller = controller;
        // Determine if the game is multiplayer or not based on if there are moves on both boards
        boolean isMultiplayer = !Arrays.deepEquals(movesOnRightPanel, new boolean[10][10]);
        this.isMultiplayer = isMultiplayer;

        this.setLayout(null);
        this.setUndecorated(true);
        this.setSize(1040, 600);
        this.setLocationRelativeTo(null);
        this.setTitle("BattleShip");
        this.setResizable(false);
        getContentPane().setBackground(new Color(34, 37, 43));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents(isMultiplayer);
        setupLeftBoard();
        setupRightBoard();

        if (isMultiplayer) {
            loadMovesOnBoard(movesOnLeftPanel, movesOnRightPanel);
        } else {
            loadMovesOnBoard(movesOnLeftPanel);
        }
    }

    private void algoFireOnLeftBoard() {
        if (firstShipPartHitRow != -1 && firstShipPartHitCol != -1) {
            boolean sunkShipLastRound = controller.positionContainSunkenShip(firstShipPartHitRow,
                firstShipPartHitCol, false);
            if (sunkShipLastRound) {
                switch (controller.getShipTypeAt(firstShipPartHitRow, firstShipPartHitCol, false)) {
                    case UBAT -> ship1Left = false;
                    case TORPED -> ship2Left = false;
                    case JAGARE -> ship3Left = false;
                    case KRYSSARE -> ship4Left = false;
                    case SLAGSKEPP -> ship5Left = false;
                }
                firstShipPartHitRow = -1;
                firstShipPartHitCol = -1;
                secondShipPartHitRow = -1;
                secondShipPartHitCol = -1;
                algoMode = AlgoMode.HUNT;
                System.out.println("BACK TO HUNT MODE");
            }
        }

        switch (algoMode) {
            case HUNT -> {
                System.out.println("HUNT MODE ACTIVE");
                int row;
                int col;
                int attempts = 0;
                boolean commit;
                do {
                    commit = false;
                    row = random.nextInt(10);
                    col = random.nextInt(10);
                    if (ship5Left) {
                        if ((row + col) % 5 == 0) {
                            commit = true;
                        }
                    } else if (ship4Left) {
                        if ((row + col) % 4 == 0) {
                            commit = true;
                        }
                    } else if (ship3Left) {
                        if ((row + col) % 3 == 0) {
                            commit = true;
                        }
                    } else if (ship2Left) {
                        if ((row + col) % 2 == 0) {
                            commit = true;
                        }
                    } else if (ship1Left) {
                        commit = true;
                    }

                    if (attempts++ > 100) {
                        commit = true;
                        System.out.println("Too many attempts: allowing random position");
                    }
                } while (leftBoardState[row][col] || !commit);

                if (leftBoardButtons[row][col].isEnabled()) {
                    if (leftButtonListener(row, col, true)) {
                        firstShipPartHitRow = row;
                        firstShipPartHitCol = col;
                        System.out.println("ALGO FOUND A SHIP!");
                        algoMode = AlgoMode.TARGET;
                    }
                } else {
                    System.out.println("TRIED TO PRESS SAME BUTTON SECOND TIME");
                }
            }
            case TARGET -> {
                System.out.println("TARGET MODE ACTIVE");
                if (firstShipPartHitRow != -1 && secondShipPartHitRow != -1) {
                    if (controller.getShipTypeAt(firstShipPartHitRow, firstShipPartHitCol, false)
                        .equals(
                            controller.getShipTypeAt(secondShipPartHitRow, secondShipPartHitCol,
                                false))) {
                        System.out.println("SHIP ORIENTATION LOCK ACTIVE");

                        if (firstShipPartHitCol == secondShipPartHitCol) {
                            System.out.println("VERTICAL SHIP");
                            int rowOffset = firstShipPartHitRow;
                            int colOffset = firstShipPartHitCol;
                            int offsetVertical = 0;

                            do {
                                switch (verticalHuntOffset) {
                                    case 0 -> {
                                        verticalHuntOffset += 1;
                                        if (firstShipPartHitRow + offsetVertical < 10) {
                                            rowOffset = firstShipPartHitRow + offsetVertical;
                                            System.out.println("GOING DOWN: OFFSET: " + rowOffset);
                                        }
                                    }
                                    case 1 -> {
                                        verticalHuntOffset += 1;
                                        if ((firstShipPartHitRow - offsetVertical) >= 0) {
                                            rowOffset = firstShipPartHitRow - offsetVertical;
                                            System.out.println("GOING UP: OFFSET: " + rowOffset);
                                        }
                                    }
                                    case 2 -> {
                                        verticalHuntOffset = 0;
                                        if (offsetVertical < 5) {
                                            offsetVertical += 1;
                                        } else {
                                            System.out.println(
                                                "PROBLEM WITH OFFSET UP AND DOWN LOGIC");
                                        }
                                    }
                                    default -> System.out.println("SOMETHING WENT WRONG");
                                }
                            } while (leftBoardState[rowOffset][colOffset]);

                            if (leftBoardButtons[rowOffset][colOffset].isEnabled()) {
                                if (leftButtonListener(rowOffset, colOffset, true)) {
                                    firstShipPartHitRow = rowOffset;
                                    firstShipPartHitCol = colOffset;
                                }
                                System.out.println("PRESSED BUTTON 2!");
                            } else {
                                JOptionPane.showMessageDialog(null,
                                    "TRIED TO PRESS SAME BUTTON SECOND TIME 2!");
                            }

                        } else if (firstShipPartHitRow == secondShipPartHitRow) {
                            System.out.println("HORIZONTAL SHIP");
                            int rowOffset = firstShipPartHitRow;
                            int colOffset = firstShipPartHitCol;
                            int offsetHorizontal = 0;

                            do {
                                switch (horizontalHuntOffset) {
                                    case 0 -> {
                                        horizontalHuntOffset += 1;
                                        if (firstShipPartHitCol + offsetHorizontal < 10) {
                                            colOffset = firstShipPartHitCol + offsetHorizontal;
                                            System.out.println("GOING RIGHT: OFFSET: " + colOffset);
                                        }
                                    }
                                    case 1 -> {
                                        horizontalHuntOffset += 1;
                                        if ((firstShipPartHitCol - offsetHorizontal) >= 0) {
                                            colOffset = firstShipPartHitCol - offsetHorizontal;
                                            System.out.println("GOING LEFT: OFFSET: " + colOffset);
                                        }
                                    }
                                    case 2 -> {
                                        horizontalHuntOffset = 0;
                                        if (offsetHorizontal < 5) {
                                            offsetHorizontal += 1;
                                        } else {
                                            System.out.println(
                                                "PROBLEM WITH OFFSET LEFT AND RIGHT LOGIC");
                                        }
                                    }
                                    default -> System.out.println("SOMETHING WENT WRONG");
                                }
                            } while (leftBoardState[rowOffset][colOffset]);

                            if (leftBoardButtons[rowOffset][colOffset].isEnabled()) {
                                if (leftButtonListener(rowOffset, colOffset, true)) {
                                    firstShipPartHitRow = rowOffset;
                                    firstShipPartHitCol = colOffset;
                                }
                                System.out.println("PRESSED BUTTON 3!");
                            } else {
                                JOptionPane.showMessageDialog(null,
                                    "TRIED TO PRESS SAME BUTTON SECOND TIME 3!");
                            }
                        }

                    } else {
                        System.out.println("Hit different ship... target it instead");
                        secondShipPartHitRow = -1;
                        secondShipPartHitCol = -1;
                    }
                }
                if (secondShipPartHitRow == -1) {
                    System.out.println("SHIP ORIENTATION HUNT ACTIVE");
                    int tryDir = 0;
                    int row;
                    int col;
                    do {
                        row = firstShipPartHitRow;
                        col = firstShipPartHitCol;
                        switch (tryDir++) {
                            case 0 -> {
                                if (row + 1 != 10) {
                                    row += 1;
                                }
                            }
                            case 1 -> {
                                if (row - 1 > -1) {
                                    row -= 1;
                                }
                            }
                            case 2 -> {
                                if (col - 1 > -1) {
                                    col -= 1;
                                }
                            }
                            case 3 -> {
                                if (col + 1 < 10) {
                                    col += 1;
                                }
                            }
                        }
                    } while (leftBoardState[row][col]);

                    if (leftBoardButtons[row][col].isEnabled()) {
                        if (leftButtonListener(row, col, true)) {
                            System.out.println("FOUND DIRECTION!");
                            secondShipPartHitRow = row;
                            secondShipPartHitCol = col;
                        }
                        System.out.println("PRESSED BUTTON 4!");
                    } else {
                        JOptionPane.showMessageDialog(null,
                            "TRIED TO PRESS SAME BUTTON SECOND TIME 4!");
                    }
                }
            }
        }
    }

    /**
     * Clicks the buttons that map to a true value in the inputted boolean array
     *
     * @param movesOnLeftPanel  a boolean array containing which buttons should be pressed on the
     *                          left board
     * @param movesOnRightPanel a boolean array containing which buttons should be pressed on the
     *                          right board
     */
    private void loadMovesOnBoard(boolean[][] movesOnLeftPanel, boolean[][] movesOnRightPanel) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (movesOnLeftPanel[row][col]) {
                    leftButtonListener(row, col, false);
                    System.out.println("CLICK LEFT");
                }
                if (movesOnRightPanel[row][col]) {
                    rightButtonListener(row, col);
                    System.out.println("CLICK RIGHT");
                }
            }
        }
    }

    /**
     * Clicks the buttons that map to a true value in the inputted boolean array. Only used for
     * single player games
     *
     * @param movesOnLeftPanel a boolean array containing which buttons should be pressed on the
     *                         left board
     */
    private void loadMovesOnBoard(boolean[][] movesOnLeftPanel) {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (movesOnLeftPanel[row][col]) {
                    leftButtonListener(row, col, false);
                    System.out.println("CLICK");
                }
            }
        }
    }

    /**
     * Initializes the components of the game board based on whether multiplayer/single-player is
     * selected
     *
     * @param multiPlayer whether to multiplayer
     */
    private void initComponents(boolean multiPlayer) {
        leftPanel = new JPanel();
        leftPanel.setOpaque(true);
        leftPanel.setSize(500, 500);
        leftPanel.setLocation(0, 30);
        leftPanel.setLayout(new GridLayout(10, 10));
        this.add(leftPanel);

        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(10, 1));
        centerPanel.setBackground(new Color(34, 37, 43));
        centerPanel.setOpaque(true);
        centerPanel.setSize(40, 500);
        centerPanel.setLocation(500 + 10, 30);

        rightPanel = new JPanel();
        rightPanel.setOpaque(true);
        rightPanel.setSize(500, 500);
        rightPanel.setLocation(540, 30);
        rightPanel.setLayout(new GridLayout(10, 10));
        this.add(rightPanel);

        JPanel topLeftPanel = new JPanel();
        topLeftPanel.setOpaque(true);
        topLeftPanel.setSize(510, 40);
        topLeftPanel.setLocation(15, -5);
        topLeftPanel.setLayout(new GridLayout(1, 10));
        topLeftPanel.setBackground(new Color(34, 37, 43));
        topLeftPanel.add(createLetters("A"));
        topLeftPanel.add(createLetters("B"));
        topLeftPanel.add(createLetters("C"));
        topLeftPanel.add(createLetters("D"));
        topLeftPanel.add(createLetters("E"));
        topLeftPanel.add(createLetters("F"));
        topLeftPanel.add(createLetters("G"));
        topLeftPanel.add(createLetters("H"));
        topLeftPanel.add(createLetters("I"));
        topLeftPanel.add(createLetters("J"));
        this.add(topLeftPanel);
        topLeftPanel.setVisible(true);

        JPanel topRightPanel = new JPanel();
        topRightPanel.setOpaque(true);
        topRightPanel.setSize(510, 40);
        topRightPanel.setLocation(555, -5);
        topRightPanel.setLayout(new GridLayout(1, 10));
        topRightPanel.setBackground(new Color(34, 37, 43));
        topRightPanel.add(createLetters("A"));
        topRightPanel.add(createLetters("B"));
        topRightPanel.add(createLetters("C"));
        topRightPanel.add(createLetters("D"));
        topRightPanel.add(createLetters("E"));
        topRightPanel.add(createLetters("F"));
        topRightPanel.add(createLetters("G"));
        topRightPanel.add(createLetters("H"));
        topRightPanel.add(createLetters("I"));
        topRightPanel.add(createLetters("J"));
        topRightPanel.setVisible(true);
        this.add(topRightPanel);

        //setup labels on centerPanel
        for (int i = 0; i < 10; i++) {
            JLabel numbers = new JLabel(String.valueOf((i + 1)));
            numbers.setForeground(new Color(0, i + 15 * 10, 194));
            //letters.setLocation(515,(i*50)+20);
            numbers.setSize(10, 10);
            numbers.setVisible(true);
            centerPanel.add(numbers);
        }
        this.add(centerPanel);

        save = new JButton("SAVE");
        save.setOpaque(true);
        save.setBorderPainted(false);
        save.setBackground(new Color(49, 55, 67));
        save.setForeground(new Color(0, 150, 194));
        save.setSize(150, 50);
        save.setLocation(95, 540);
        save.setFocusable(false);
        save.addActionListener(this);
        this.add(save);

        stats = new JButton("STATISTICS");
        stats.setOpaque(true);
        stats.setBorderPainted(false);
        stats.setBackground(new Color(49, 55, 67));
        stats.setForeground(new Color(0, 150, 194));
        stats.setSize(150, 50);
        stats.setLocation(255, 540);
        stats.setFocusable(false);
        stats.addActionListener(this);
        this.add(stats);

        quit = new JButton("QUIT");
        quit.setOpaque(true);
        quit.setBorderPainted(false);
        quit.setBackground(new Color(49, 55, 67));
        quit.setForeground(new Color(0, 150, 194));
        quit.setSize(150, 50);
        quit.setLocation(870, 540);
        quit.setFocusable(false);
        quit.addActionListener(this);
        this.add(quit);

        //setup for single player
        if (!multiPlayer) {
            this.setSize(530, 600);
            this.setLocationRelativeTo(null);
            save.setLocation(20, 540);
            stats.setLocation(190, 540);
            quit.setLocation(360, 540);
        } else {
            // Make Java redraw the window - workaround to fix panels not visible on Windows.
            this.setSize(1040, 599);
            this.setSize(1040, 600);
        }

    }

    /**
     * Sets up buttons on the left board and adds an actionListener implementation specific to each
     * button
     */
    private void setupLeftBoard() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JButton tmpBtn = new JButton();
                tmpBtn.setSize(10, 10);
                int rowInput = row;
                int colInput = col;
                //custom implementation of actionListener is assigned to button
                tmpBtn.addActionListener((actionEvent) -> {
                    if (isMultiplayer) {
                        if (isAlgoEnabled) {
                            JOptionPane.showMessageDialog(null,
                                "This is your own board! You can't fire here!");
                        } else {
                            if (currentPlayer == Player.PLAYER2) {
                                leftButtonListener(rowInput, colInput, false);
                                currentPlayer = Player.PLAYER1;
                            } else {
                                JOptionPane.showMessageDialog(null,
                                    "It's not your turn! Player 1's turn.");
                            }
                        }
                    } else {
                        leftButtonListener(rowInput, colInput, false);
                    }

                });
                tmpBtn.setOpaque(true);
                tmpBtn.setBackground(new Color(49, 55, 67));
                Border border = new LineBorder(Color.BLACK, 1);
                tmpBtn.setBorder(border);
                leftBoardButtons[row][col] = tmpBtn;
                leftPanel.add(tmpBtn);
            }
        }
    }

    /**
     * Sets up buttons on the right board and adds an actionListener implementation specific to each
     * button
     */
    private void setupRightBoard() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JButton tmpBtn = new JButton();
                tmpBtn.setSize(10, 10);
                int rowInput = row;
                int colInput = col;
                //custom implementation of actionListener is assigned to button
                tmpBtn.addActionListener((event) -> {
                    if (isMultiplayer) {
                        if (isAlgoEnabled) {
                            rightButtonListener(rowInput, colInput);
                        } else {
                            if (currentPlayer == Player.PLAYER1) {
                                rightButtonListener(rowInput, colInput);
                                currentPlayer = Player.PLAYER2;
                            } else {
                                JOptionPane.showMessageDialog(null,
                                    "It's not your turn! Player 2's turn.");
                            }
                        }
                    } //else {/* The game is in single player mode, which doesn't have a right board. */}
                });
                tmpBtn.setOpaque(true);
                tmpBtn.setBackground(new Color(49, 55, 67));
                Border border = new LineBorder(Color.BLACK, 1);
                tmpBtn.setBorder(border);
                rightBoardButtons[row][col] = tmpBtn;
                rightPanel.add(tmpBtn);
            }
        }
    }

    /**
     * The listener method for the left board's buttons
     *
     * @param row the row of the button on the left board
     * @param col the column of the button on the left board
     * @return whether a ship was hit
     */
    private boolean leftButtonListener(int row, int col, boolean algoMove) {
        if (isAlgoEnabled && !algoMove) {
            JOptionPane.showMessageDialog(null, "This is your own board! You can't fire here!");
            return false;
        }

        System.out.println("Button pressed on left board at: row:" + row + " col:" + col);
        controller.incrementPlayer2Score();
        leftBoardButtons[row][col].setEnabled(false);
        leftBoardState[row][col] = true;

        // check if a ship was hit
        if (!controller.shipExistsAt(false, row, col)) {
            leftBoardButtons[row][col].setBackground(Color.DARK_GRAY);
            return false;
        }

        // check what type of ship was hit & color the game board accordingly
        switch (controller.getShipTypeAt(row, col, false)) {
            case UBAT -> {
                leftBoardButtons[row][col].setText("1");
                leftBoardButtons[row][col].setBackground(new Color(0, 100, 194));
            }
            case TORPED -> {
                leftBoardButtons[row][col].setText("2");
                leftBoardButtons[row][col].setBackground(new Color(0, 90, 194));
            }
            case JAGARE -> {
                leftBoardButtons[row][col].setText("3");
                leftBoardButtons[row][col].setBackground(new Color(0, 70, 194));
            }
            case KRYSSARE -> {
                leftBoardButtons[row][col].setText("4");
                leftBoardButtons[row][col].setBackground(new Color(0, 60, 194));
            }
            case SLAGSKEPP -> {
                leftBoardButtons[row][col].setText("5");
                leftBoardButtons[row][col].setBackground(new Color(0, 30, 194));
            }
            default -> leftBoardButtons[row][col].setBackground(Color.BLACK);
        }

        // check if the ship was sunk && whether anyone has won
        if (controller.hitShipSunk(false, row, col)) {
            refreshLeftPanel();
            if (!isGameWon && controller.checkIfRightPlayerWon()) {
                isGameWon = true;
                gameWonAnnouncement(true);
            }
        }

        return true;
    }

    /**
     * The listener method for the right board's buttons
     *
     * @param row the row of the button on the right board
     * @param col the column of the button on the right board
     */
    private void rightButtonListener(int row, int col) {
        System.out.println("Button pressed on right board at: row:" + row + " col:" + col);
        controller.incrementPlayer1Score();
        rightBoardButtons[row][col].setEnabled(false);
        rightBoardState[row][col] = true;

        if (!controller.shipExistsAt(true, row, col)) {
            rightBoardButtons[row][col].setBackground(Color.DARK_GRAY);
            if (isAlgoEnabled) {
                algoFireOnLeftBoard();
            }
            return;
        }

        switch (controller.getShipTypeAt(row, col, true)) {
            case UBAT -> {
                rightBoardButtons[row][col].setText("1");
                rightBoardButtons[row][col].setBackground(new Color(200, 0, 0));
            }
            case TORPED -> {
                rightBoardButtons[row][col].setText("2");
                rightBoardButtons[row][col].setBackground(new Color(170, 0, 0));
            }
            case JAGARE -> {
                rightBoardButtons[row][col].setText("3");
                rightBoardButtons[row][col].setBackground(new Color(130, 0, 0));
            }
            case KRYSSARE -> {
                rightBoardButtons[row][col].setText("4");
                rightBoardButtons[row][col].setBackground(new Color(100, 0, 0));
            }
            case SLAGSKEPP -> {
                rightBoardButtons[row][col].setText("5");
                rightBoardButtons[row][col].setBackground(new Color(50, 0, 0));
            }
            default -> rightBoardButtons[row][col].setBackground(Color.BLACK);
        }

        if (controller.hitShipSunk(true, row, col)) {
            refreshRightPanel();
            if (!isGameWon && controller.leftPlayerWon()) {
                isGameWon = true;
                gameWonAnnouncement(false);
            }
        }
        if (isAlgoEnabled) {
            algoFireOnLeftBoard();
        }
    }

    /**
     * Shows a message that announces that a specified player has won
     *
     * @param player2 whether player2 has won or not
     */
    public void gameWonAnnouncement(boolean player2) {
        if (player2) {
            if (Arrays.deepEquals(rightBoardState, new boolean[10][10])) {
                JOptionPane.showMessageDialog(this, "YOU WON!");
            } else if (isAlgoEnabled) {
                JOptionPane.showMessageDialog(this,
                    "COMPUTER WON IN: " + controller.getMovesRequiredToWin(true) + " MOVES! :O");
            } else {
                JOptionPane.showMessageDialog(this, "PLAYER 2 WON!");
            }
            if (!isAlgoEnabled) {
                controller.letWinnerEnterNameToPotentiallyAddToScoreboard(
                    JOptionPane.showInputDialog("Enter a name for the scoreboard!"),
                    controller.getMovesRequiredToWin(true));
            }
            controller.showStats();
        } else {
            JOptionPane.showMessageDialog(this, "PLAYER 1 WON!");
            controller.letWinnerEnterNameToPotentiallyAddToScoreboard(
                JOptionPane.showInputDialog("Enter a name for the scoreboard!"),
                controller.getMovesRequiredToWin(false));
            controller.showStats();
        }
    }

    /**
     * Creates a new JLabel with inputted string and returns it
     *
     * @param string the string to be shown in the JLabel
     * @return a JLabel with the specified string
     */
    private JLabel createLetters(String string) {
        JLabel letter = new JLabel(string);
        letter.setForeground(new Color(0, 131, 194));
        letter.setSize(10, 10);
        letter.setVisible(true);
        centerPanel.add(letter);
        return letter;
    }

    /**
     * Updates the left panel by checking for ships that have been sunk
     */
    private void refreshLeftPanel() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (controller.shipExistsAt(false, row, col)) {
                    if (controller.positionContainSunkenShip(row, col, false)) {
                        leftBoardButtons[row][col].setText("X");
                    }
                }
            }
        }
    }

    /**
     * Updates the right panel by checking for ships that have been sunk
     */
    private void refreshRightPanel() {
        System.out.println("RefreshRightPanel()");
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                if (controller.shipExistsAt(true, row, col)) {
                    if (controller.positionContainSunkenShip(row, col, true)) {
                        rightBoardButtons[row][col].setText("X");
                    }
                }
            }
        }
    }

    /**
     * The listener of the PlayerBoard class
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == save) {
            if (controller.saveGameToDisk(leftBoardState, rightBoardState)) {
                JOptionPane.showMessageDialog(this, "Saved game to disk!");
            } else {
                JOptionPane.showMessageDialog(this, "Could not save game to disk!");

            }
        } else if (e.getSource() == stats) {
            System.out.println("Pressed stats");
            new ShowStats(controller.getScoreObjectsFromDiskAsStrings().toArray(new String[0]));
        } else if (e.getSource() == quit) {
            if (JOptionPane.showConfirmDialog(this, "Do you really want to exit to main menu?",
                "Exit?",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                this.dispose();
            }
        }

    }

}
