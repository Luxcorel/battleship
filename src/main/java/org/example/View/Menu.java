package org.example.View;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.example.Controller.Controller;

/**
 * @author Johannes Rosengren Systemutvecklare HT22
 * @author Victor Pirojoc Systemutvecklare HT22
 * <p>
 * The main menu of the GUI part of the program
 */
public class Menu extends JFrame implements ActionListener {

    private final JPanel mainPanel;
    private JButton pvp;
    private JButton singlePlayer;
    private JButton ai;
    private JButton stats;
    private JButton loadGame;
    private JButton quit;
    private final Controller controller;

    /**
     * Constructor for initializing the main menu
     *
     * @param controller an instance of a Controller class object
     */
    public Menu(Controller controller) {
        this.controller = controller;
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        setSize(480, 570);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setContentPane(mainPanel);
        this.setTitle("Menu");
        this.setUndecorated(true);
        this.setResizable(false);
        this.setBackground(Color.BLACK);
        mainPanel.setBackground(Color.BLACK);
        buttonsLayout();
        this.setVisible(true);
    }

    /**
     * Initialization of the button objects and subsequently population of the mainPanel
     */
    private void buttonsLayout() {
        singlePlayer = new JButton("Single Player");
        singlePlayer.setFocusable(false);
        singlePlayer.setFont(new Font("Copperplate", Font.BOLD, 45));
        singlePlayer.setSize(270 * 2, 45 * 2);
        singlePlayer.setLocation(-30, 90);
        singlePlayer.setBorderPainted(false);
        singlePlayer.setBackground(Color.BLACK);
        singlePlayer.setForeground(new Color(0, 180, 255));
        singlePlayer.addActionListener(this);
        mainPanel.add(singlePlayer);

        pvp = new JButton("Player vs Player");
        pvp.setFocusable(false);
        pvp.setFont(new Font("Copperplate", Font.BOLD, 48));
        pvp.setSize(270 * 2, 45 * 2);
        pvp.setLocation(-30, 0);
        pvp.setBorderPainted(false);
        pvp.setBackground(Color.BLACK);
        pvp.setForeground(new Color(0, 155, 255));
        pvp.addActionListener(this);
        mainPanel.add(pvp);

        ai = new JButton("Player vs AI");
        ai.setFocusable(false);
        ai.setFont(new Font("Copperplate", Font.BOLD, 45));
        ai.setSize(270 * 2, 45 * 2);
        ai.setLocation(-30, 190);
        ai.setBackground(Color.BLACK);
        ai.setBorderPainted(false);
        ai.setForeground(new Color(0, 137, 194));
        ai.addActionListener(this);
        mainPanel.add(ai);

        loadGame = new JButton("Load Game");
        loadGame.setFocusable(false);
        loadGame.setFont(new Font("Copperplate", Font.BOLD, 45));
        loadGame.setSize(270 * 2, 45 * 2);
        loadGame.setLocation(-30, 290);
        loadGame.setBorderPainted(false);
        loadGame.setBackground(Color.BLACK);
        loadGame.setForeground(new Color(0, 100, 194));
        loadGame.addActionListener(this);
        mainPanel.add(loadGame);

        stats = new JButton("Stats");
        stats.setFocusable(false);
        stats.setFont(new Font("Copperplate", Font.BOLD, 45));
        stats.setSize(270 * 2, 55 * 2);
        stats.setLocation(-30, 380);
        stats.setBorderPainted(false);
        stats.setBackground(Color.BLACK);
        stats.setForeground(new Color(0, 80, 194));
        stats.addActionListener(this);
        mainPanel.add(stats);

        quit = new JButton("Quit");
        quit.setFocusable(false);
        quit.setFont(new Font("Copperplate", Font.BOLD, 45));
        quit.setSize(270 * 2, 45 * 2);
        quit.setLocation(-30, 480);
        quit.setBorderPainted(false);
        quit.setBackground(Color.BLACK);
        quit.setForeground(new Color(0, 40, 194));
        quit.addActionListener(this);
        mainPanel.add(quit);

    }

    /**
     * Listener for the main menu
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pvp) {
            controller.initNewGameBoards();
            new PlayerBoard(controller, true, false);
        } else if (e.getSource() == singlePlayer) {
            controller.initNewGameBoards();
            new PlayerBoard(controller, false, false);
        } else if (e.getSource() == ai) {
            controller.initNewGameBoards();
            new PlayerBoard(controller, true, true);
        } else if (e.getSource() == stats) {
            new ShowStats(controller.getScoreObjectsFromDiskAsStrings().toArray(new String[0]));
        } else if (e.getSource() == loadGame) {
            controller.loadGameFromDisk();
        } else if (e.getSource() == quit) {
            System.exit(0);
        }

    }

    /**
     * Shows a message using a JOptionPane
     *
     * @param output the message to be shown
     */
    public void showMessage(String output) {
        JOptionPane.showMessageDialog(null, output);
    }
}

