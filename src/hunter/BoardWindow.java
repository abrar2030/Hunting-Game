package hunter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class BoardWindow extends PrimaryWindow {

    private final BoardModel boardModel;
    private final JButton[][] gridButtons;
    private final JLabel statusLabel;
    private final JLabel movesLabel;
    private final JPanel gridPanel;
    private final JPanel footer;
    private final MainApp mainApp;

    public BoardWindow(int dimension, MainApp mainApp) {
        this.mainApp = mainApp;
        this.mainApp.registerWindow(this);

        this.boardModel = new BoardModel(dimension);
        this.footer = new JPanel();
        JPanel header = new JPanel();

        this.statusLabel = new JLabel();
        this.movesLabel = new JLabel();
        refreshStatusText();

        JButton restartGame = initiateRestartGameButton();

        this.gridPanel = new JPanel(new GridLayout(dimension, dimension));
        this.gridButtons = new JButton[dimension][dimension];
        setupGridButtons(dimension);

        header.add(statusLabel);
        header.add(restartGame);
        footer.add(movesLabel);

        setupLayout(header, gridPanel, footer);
    }

    private void setupLayout(JPanel header, JPanel gridPanel, JPanel footer) {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(gridPanel, BorderLayout.CENTER);
        getContentPane().add(footer, BorderLayout.SOUTH);

        this.pack();
    }

    private JButton initiateRestartGameButton() {
        JButton restartGame = new JButton("Restart Game");
        restartGame.addActionListener(e -> restartTheGame());
        return restartGame;
    }

    private void setupGridButtons(int dimension) {
        for (int i = 0; i < dimension; ++i) {
            for (int j = 0; j < dimension; ++j) {
                gridButtons[i][j] = initiateButton(i, j);
            }
        }
    }

    private JButton initiateButton(int x, int y) {
        JButton button = new JButton();
        setDefaultButtonText(button, x, y);
        button.addActionListener(produceButtonActionListener(x, y));
        button.setPreferredSize(new Dimension(100, 40));
        gridPanel.add(button);
        return button;
    }

    private void setDefaultButtonText(JButton button, int x, int y) {
        if (isMiddlePosition(x, y) || isEdgePosition(x, y)) {
            button.setText(retrieveParticipantAtCell(x, y).name());
        }
    }

    private boolean isMiddlePosition(int x, int y) {
        int middle = boardModel.getDimension() / 2;
        return x == middle && y == middle;
    }

    private boolean isEdgePosition(int x, int y) {
        int dimensionMinusOne = boardModel.getDimension() - 1;
        return (x == 0 || x == dimensionMinusOne) && (y == 0 || y == dimensionMinusOne);
    }

    private Participant retrieveParticipantAtCell(int x, int y) {
        return boardModel.getCell(x, y);
    }

    private ActionListener produceButtonActionListener(int x, int y) {
    return e -> {
        boardModel.makeMove(x, y);
        refreshButtonLabels();
        Participant victor = boardModel.identifyWinner();

        if (victor != Participant.NONE) {
            gameEnds(victor); // Displays the message
            disableAllButtons();
        }

        refreshStatusText();
    };
}
    
    private void disableAllButtons() {
        for (int i = 0; i < boardModel.getDimension(); ++i) {
            for (int j = 0; j < boardModel.getDimension(); ++j) {
                gridButtons[i][j].setEnabled(false);
            }
        }
    }

    private void refreshButtonLabels() {
        for (int i = 0; i < boardModel.getDimension(); ++i) {
            for (int j = 0; j < boardModel.getDimension(); ++j) {
                refreshButtonLabel(gridButtons[i][j], boardModel.getCell(i, j));
            }
        }
    }

    private void refreshButtonLabel(JButton button, Participant participant) {
        button.setText(participant != Participant.NONE ? participant.name() : "");
    }

    private void restartTheGame() {
        BoardWindow newBoardWindow = new BoardWindow(boardModel.getDimension(), mainApp);
        newBoardWindow.setVisible(true);
        this.dispose();
        mainApp.deregisterWindow(this);
    }

    private void refreshStatusText() {
        statusLabel.setText("Current Participant: " + boardModel.getCurrentParticipant().name());
        movesLabel.setText("Moves Made: " + boardModel.movesMade());
    }

    private void gameEnds(Participant victor) {
    JOptionPane.showMessageDialog(this, "Game Over! The victor is: " + victor.name());
}
}