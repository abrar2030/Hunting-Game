package hunter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

public class BoardModel {
    private final int dimension;
    private final Participant[][] grid;
    private Participant currentParticipant;
    private int movesMade;
    private Point runnerPosition;
    private final ArrayList<Point> chaserPositions = new ArrayList<>();
    private boolean isChosen;
    private Point chosenPosition;

    public BoardModel(int dimension) {
        this.dimension = dimension;
        this.grid = new Participant[dimension][dimension];
        startBoard();
    }

    private void startBoard() {
        resetGrid();
        setupParticipants();
        positionParticipantsOnGrid();
    }

    private void resetGrid() {
        for (int i = 0; i < dimension; i++) {
            Arrays.fill(grid[i], Participant.NONE);
        }
    }

    private void setupParticipants() {
        currentParticipant = Participant.FUGITIVE;
        runnerPosition = new Point(dimension / 2, dimension / 2);
        chaserPositions.addAll(Arrays.asList(
                new Point(0, 0),
                new Point(0, dimension - 1),
                new Point(dimension - 1, 0),
                new Point(dimension - 1, dimension - 1)
        ));
    }

    private void positionParticipantsOnGrid() {
        positionParticipant(runnerPosition, Participant.FUGITIVE);
        chaserPositions.forEach(position -> positionParticipant(position, Participant.HUNTER));
    }

    private void positionParticipant(Point position, Participant participant) {
        grid[position.x][position.y] = participant;
    }

    public void makeMove(int row, int col) {
        Point targetPosition = new Point(row, col);

        if (isRunnerMove()) {
            executeRunnerAction(targetPosition);
        } else {
            executeChaserAction(targetPosition);
        }
    }

    private boolean isRunnerMove() {
        return currentParticipant == Participant.FUGITIVE;
    }

    private void executeRunnerAction(Point target) {
        if (validateMove(runnerPosition, target)) {
            shiftParticipant(runnerPosition, target);
            switchParticipant();
        }
    }

    private void executeChaserAction(Point target) {
        if (isChosen) {
            executeChosenChaserMove(target);
        } else if (isChaserAtPosition(target)) {
            selectChaser(target);
        }
    }

    private void executeChosenChaserMove(Point target) {
        if (validateMove(chosenPosition, target)) {
            shiftParticipant(chosenPosition, target);
            updateChaserPosition(chosenPosition, target);
            deselectChaser();
            incrementMoves();
            switchParticipant();
        }
    }

   private boolean validateMove(Point from, Point to) {
        return isAdjacent(from, to) && isCellEmpty(to);
    }

    private void shiftParticipant(Point from, Point to) {
        grid[from.x][from.y] = Participant.NONE;
        grid[to.x][to.y] = currentParticipant;
        if (currentParticipant == Participant.FUGITIVE) {
            runnerPosition = to;
        }
    }

    private void switchParticipant() {
        currentParticipant = (currentParticipant == Participant.FUGITIVE) ? Participant.HUNTER : Participant.FUGITIVE;
    }

    private boolean isChaserAtPosition(Point position) {
    if (!isWithinGridBounds(position)) {
        return false; // Out of bounds is not occupied by a chaser
    }
    return grid[position.x][position.y] == Participant.HUNTER;
}

    private void selectChaser(Point position) {
        isChosen = true;
        chosenPosition = position;
    }

    private void deselectChaser() {
        isChosen = false;
    }

    private void incrementMoves() {
        movesMade++;
    }

    private boolean isAdjacent(Point p, Point q) {
        return Math.abs(p.x - q.x) <= 1 && Math.abs(p.y - q.y) <= 1 && !p.equals(q);
    }

    private boolean isCellEmpty(Point position) {
        return grid[position.x][position.y] == Participant.NONE;
    }
    
    private void updateChaserPosition(Point oldPosition, Point newPosition) {
        chaserPositions.remove(oldPosition);
        chaserPositions.add(newPosition);
    }

    public Participant identifyWinner() {
    System.out.println("Checking for winner...");
    refreshChaserPositions();

    if (isFugitiveTrapped()) {
        System.out.println("Fugitive is trapped.");
        return Participant.HUNTER;
    }

    if (movesMade > 4 * dimension) {
        System.out.println("Move limit exceeded.");
        return Participant.FUGITIVE;
    }

    return Participant.NONE;
}


private boolean isFugitiveTrapped() {
    for (int dx = -1; dx <= 1; dx++) {
        for (int dy = -1; dy <= 1; dy++) {
            if (dx == 0 && dy == 0) continue; // Skip the fugitive's current position

            Point adjacent = new Point(runnerPosition.x + dx, runnerPosition.y + dy);
            if (isWithinGridBounds(adjacent) && isCellEmpty(adjacent)) {
                return false; // Found an escape route
            }
        }
    }
    return true; // No escape routes available
}

    
    
    private void refreshChaserPositions() {
        chaserPositions.clear();
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (grid[i][j] == Participant.HUNTER) {
                    chaserPositions.add(new Point(i, j));
                }
            }
        }
    }

    private boolean allNearbyCellsOccupied(Point position) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue; // Skip the current position of the fugitive
                }
                Point adjacent = new Point(position.x + i, position.y + j);
                // Check if adjacent cell is either out of bounds or not occupied by a hunter
                if (!isWithinGridBounds(adjacent) || isCellEmpty(adjacent)) {
                    return false; // An adjacent cell is either out of bounds or not occupied by a hunter, fugitive is not trapped
                }
            }
        }
        return true; // All adjacent cells are occupied by hunters, fugitive is trapped
    }
    
    


    private boolean isWithinGridBounds(Point position) {
        return position.x >= 0 && position.x < dimension && position.y >= 0 && position.y < dimension;
    }

    public Participant getCurrentParticipant() {
        return currentParticipant;
    }

    public int getDimension() {
        return dimension;
    }

    public int movesMade() {
        return movesMade;
    }

    public Participant getCell(int x, int y) {
        return grid[x][y];
    }

    public Point getRunnerPosition() {
        return new Point(runnerPosition);
    }

    public ArrayList<Point> getChaserPositions() {
        return new ArrayList<>(chaserPositions);
    }

    public Point getChosenPosition() {
        return isChosen ? new Point(chosenPosition) : null;
    }
}