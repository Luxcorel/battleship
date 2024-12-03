package org.example.View;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * @author Johannes Rosengren Systemutvecklare HT22
 * @author Victor Pirojoc Systemutvecklare HT22
 * <p>
 * This class is responsible for displaying score objects as strings
 */
public class ShowStats extends JFrame {

    private final JPanel mainPanel;

    /**
     * Constructor for ShowStats. Requires Score objects to be converted to string array
     *
     * @param dataToShow the string array to show
     */
    public ShowStats(String[] dataToShow) {
        mainPanel = new JPanel();
        this.setSize(250, 300);
        this.setContentPane(mainPanel);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setTitle("Scoreboard");
        setupListOfStats(dataToShow);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setVisible(true);
    }

    /**
     * Initializes the JList containing the score list
     *
     * @param dataToShow the string array to display in the JList
     */
    private void setupListOfStats(String[] dataToShow) {
        JList<String> scoreList = new JList<>();
        scoreList.setBackground(Color.BLACK);
        scoreList.setLocation(0, 0);
        scoreList.setSize(250, 300);
        scoreList.setFont(new Font("Impact", Font.ITALIC, 19));
        scoreList.setForeground(new Color(35, 182, 182));
        mainPanel.add(scoreList);
        scoreList.setListData(dataToShow);
    }

}
