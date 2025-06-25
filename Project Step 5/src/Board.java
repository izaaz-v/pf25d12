
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    public static final int ROWS = 3;
    public static final int COLS = 3;
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;
    public static final int Y_OFFSET = 1;


    Cell[][] cells;
    public int[] winningLine = null;

    public Board() {
        initGame();
    }

    public void initGame() {
        cells = new Cell[ROWS][COLS];
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    public void newGame() {
        winningLine = null;
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].newGame();
            }
        }
    }

    public int[] findRandomAIMove() {
        List<int[]> emptyCells = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (cells[r][c].content == Seed.NO_SEED) {
                    emptyCells.add(new int[]{r, c});
                }
            }
        }
        if (!emptyCells.isEmpty()) {
            return emptyCells.get(new Random().nextInt(emptyCells.size()));
        }
        return null;
    }

    public State stepGame(Seed player, int selectedRow, int selectedCol) {
        cells[selectedRow][selectedCol].content = player;

        if (cells[selectedRow][0].content == player && cells[selectedRow][1].content == player && cells[selectedRow][2].content == player) {
            winningLine = new int[]{selectedRow, 0, selectedRow, 2};
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }
        if (cells[0][selectedCol].content == player && cells[1][selectedCol].content == player && cells[2][selectedCol].content == player) {
            winningLine = new int[]{0, selectedCol, 2, selectedCol};
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }
        if (selectedRow == selectedCol && cells[0][0].content == player && cells[1][1].content == player && cells[2][2].content == player) {
            winningLine = new int[]{0, 0, 2, 2};
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }
        if (selectedRow + selectedCol == 2 && cells[0][2].content == player && cells[1][1].content == player && cells[2][0].content == player) {
            winningLine = new int[]{0, 2, 2, 0};
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col].content == Seed.NO_SEED) {
                    return State.PLAYING;
                }
            }
        }
        return State.DRAW;
    }

    public void paint(Graphics g) {
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; ++row) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH, GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; ++col) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, Y_OFFSET,
                    GRID_WIDTH, CANVAS_HEIGHT - 1, GRID_WIDTH, GRID_WIDTH);
        }

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                cells[row][col].paint(g);
            }
        }

        if (winningLine != null) {
            g.setColor(new Color(255, 255, 0, 200));
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(10, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int x1 = winningLine[1] * Cell.SIZE + Cell.SIZE / 2;
            int y1 = winningLine[0] * Cell.SIZE + Cell.SIZE / 2;
            int x2 = winningLine[3] * Cell.SIZE + Cell.SIZE / 2;
            int y2 = winningLine[2] * Cell.SIZE + Cell.SIZE / 2;
            g2d.drawLine(x1, y1, x2, y2);
        }
    }
}