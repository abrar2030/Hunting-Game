package hunter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class MainApp extends PrimaryWindow {
    private ArrayList<BoardWindow> gameWindows;

    public MainApp() {
        gameWindows = new ArrayList<>();
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        setupButtons();
    }

    private void setupButtons() {
        JButton small = new JButton("3x3");
        JButton medium = new JButton("5x5");
        JButton large = new JButton("7x7");

        small.addActionListener(e -> spawnGameWindow(3));
        medium.addActionListener(e -> spawnGameWindow(5));
        large.addActionListener(e -> spawnGameWindow(7));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(small);
        buttonPanel.add(medium);
        buttonPanel.add(large);

        getContentPane().add(buttonPanel);
    }

    private void spawnGameWindow(int dimension) {
        BoardWindow windowInstance = new BoardWindow(dimension, this);
        windowInstance.setVisible(true);
        registerWindow(windowInstance);
    }

    public void registerWindow(BoardWindow windowInstance) {
        gameWindows.add(windowInstance);
    }

    public void deregisterWindow(BoardWindow windowInstance) {
        gameWindows.remove(windowInstance);
    }
}
