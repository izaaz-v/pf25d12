
import java.awt.*;

public class Cell {

    public static final int SIZE = 120; // Ukuran sel dalam pixel
    public static final int PADDING = SIZE / 5; // Jarak gambar dari tepi sel
    public static final int SEED_SIZE = SIZE - PADDING * 2; // Ukuran gambar bidak

    Seed content;
    int row, col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED;
    }

    public void newGame() {
        content = Seed.NO_SEED;
    }

    public void paint(Graphics g) {

        if (content == Seed.CROSS || content == Seed.NOUGHT) {
            int x1 = col * SIZE + PADDING;
            int y1 = row * SIZE + PADDING;
            g.drawImage(content.getImage(), x1, y1, SEED_SIZE, SEED_SIZE, null);
        }
    }
}