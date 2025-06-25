//Sebagai tempat seluruh permainan Tic-Tac-Toe ditampilkan, serta mengelola alur permainan, giliran pemain, skor permainan

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;
import java.util.Random;

public class GameMain extends JPanel implements MouseListener {

    public static final String TITLE = "Tic Tac Toe";
    public static final Color COLOR_BG = Color.WHITE; // Warna latar cadangan
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216); // Warna abu-abu status bar
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    //Variabel untuk simpn semua data dan state permainan saat ini
    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private GameMode gameMode;
    private int difficultyLevel;
    private AIPlayer aiPlayer;
    private int scoreCross;
    private int scoreNought;
    private int roundsPlayed;
    private final int MAX_ROUNDS = 5;
    private boolean isSeriesOver;

    //Untuk atribut pemain
    private String nameX;
    private String nameO;

    //Untuk tampilan layar
    private JLabel statusBar;
    private Image backgroundImage;

    //Constructor untuk class GameMain dg menginisiasi, mengatur mode dan nama pmain
    public GameMain(GameMode mode, int difficulty, String player1Name, String player2Name) {
        this.gameMode = mode;
        this.difficultyLevel = difficulty;
        this.nameX = player1Name;
        this.nameO = player2Name;

        try {
            java.net.URL imgUrl = getClass().getClassLoader().getResource("images/background.png");
            if (imgUrl != null) {
                this.backgroundImage = new ImageIcon(imgUrl).getImage();
            } else {
                System.err.println("GAGAL (Game): File 'images/background.png' tidak ditemukan.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupUI();
        initGame();
        newGame();
    }

    //Method untuk kontrol dari pengguna
    public void setDifficultyLevel(int difficulty) {
        this.difficultyLevel = difficulty;
        JOptionPane.showMessageDialog(this, "Ok Boz!", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public void repaintBoard() {
        this.repaint();
    }

    public String getNameX() {
        return this.nameX;
    }

    public String getNameO() {
        return this.nameO;
    }

    //Method untuk mengatur alur game
    private void setupUI() {
        addMouseListener(this);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        statusBar = new JLabel(" ");
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.CENTER);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        add(statusBar, BorderLayout.PAGE_END);
    }

    public void initGame() {
        board = new Board();
        if (gameMode == GameMode.PVE) {
            aiPlayer = new AIPlayer(Seed.NOUGHT);
        }
    }

    public void newGame() {
        board.newGame();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
    }

    public void newSeries() {
        scoreCross = 0;
        scoreNought = 0;
        roundsPlayed = 0;
        isSeriesOver = false;
        newGame();
    }

    //Method untuk menunjukkan elemen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Gambar background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            setBackground(COLOR_BG);
        }

        //gambar papan
        board.paint(g);

        //gambar teks skor
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setFont(new Font("Georgia", Font.BOLD, 16));

        String scoreText = String.format("%s: %d   |   %s: %d", nameX, scoreCross, nameO, scoreNought);
        String roundText = isSeriesOver ? "SERI SELESAI" : String.format("RONDE %d / %d", Math.min(roundsPlayed + 1, MAX_ROUNDS), MAX_ROUNDS);

        g2d.setColor(Color.BLACK);
        g2d.drawString(scoreText, 21, 26);
        g2d.drawString(roundText, 21, 51);
        g2d.setColor(Color.WHITE);
        g2d.drawString(scoreText, 20, 25);
        g2d.drawString(roundText, 20, 50);

        updateStatusBar();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isSeriesOver) {
            return;
        }

        if (currentState != State.PLAYING) {
            newGame();
            repaint();
            return;
        }

        if (currentState == State.PLAYING) {
            int row = e.getY() / Cell.SIZE;
            int col = e.getX() / Cell.SIZE;

            if (row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS && board.cells[row][col].content == Seed.NO_SEED) {
                makeMove(row, col);

                if (currentState != State.PLAYING) { // Jika langkah pemain mengakhiri ronde
                    endOfRound();
                } else if (gameMode == GameMode.PVE && currentPlayer == Seed.NOUGHT) { // Jika sekarang giliran AI
                    // Gunakan Timer agar gerakan AI tidak terasa instan
                    Timer timer = new Timer(500, ae -> {
                        aiTurn();
                        if (currentState != State.PLAYING) { // Jika langkah AI mengakhiri ronde
                            endOfRound();
                        }
                        repaint();
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        }
        repaint();
    }

    private void endOfRound() {
        updateScore();
        roundsPlayed++;

        //Cek apakah permainan sudah sampai akhir
        if (scoreCross >= 3 || scoreNought >= 3 || roundsPlayed >= MAX_ROUNDS) {
            isSeriesOver = true;
            GameRecord record = new GameRecord(nameX, nameO, scoreCross, scoreNought, gameMode, new Date());
            HistoryManager.saveRecord(record);

            Timer timer = new Timer(500, e -> showEndOfSeriesDialog());
            timer.setRepeats(false);
            timer.start();
        }
    }

    // Muncul pop up di akhir game
    private void showEndOfSeriesDialog() {
        String winnerMsg;
        if (scoreCross > scoreNought) {
            winnerMsg = String.format("Selamat, bro %s memenangkan seri ini!", nameX);
        } else if (scoreNought > scoreCross) {
            winnerMsg = String.format("Selamat, bro %s memenangkan seri ini!", nameO);
        } else {
            winnerMsg = "Permainan Seri!";
        }
        String finalScore = String.format("Skor akhir: %d - %d", scoreCross, scoreNought);
        String message = winnerMsg + "\n" + finalScore;

        Object[] options = {"Main Lagi Lah Rek!", "Balik ke Menu"};
        int choice = JOptionPane.showOptionDialog(this, message, "Seri Selesai",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            newSeries();
        } else {
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (topFrame != null) {
                topFrame.dispose();
            }
        }
    }

    //Memproses langkah di papan
    private void makeMove(int row, int col) {
        currentState = board.stepGame(currentPlayer, row, col);
        if (currentState == State.PLAYING) {
            SoundEffect.MOVE.play();
            currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
        } else {
            SoundEffect.WIN.play();
        }
    }

    //Untuk menentukan langkah dari AI berdasarkan tingkat kesulitan
    private void aiTurn() {
        int[] aiMove;
        switch (difficultyLevel) {
            case 0:
                aiMove = board.findRandomAIMove();
                break;
            case 1:
                aiMove = (new Random().nextBoolean()) ? board.findRandomAIMove() : aiPlayer.findBestMove(board);
                break;
            case 2:
            default:
                aiMove = aiPlayer.findBestMove(board);
                break;
        }
        if (aiMove != null) {
            makeMove(aiMove[0], aiMove[1]);
        }
    }

    //Untuk nambah skor pemain yang menang
    private void updateScore() {
        if (currentState == State.CROSS_WON) {
            scoreCross++;
        } else if (currentState == State.NOUGHT_WON) {
            scoreNought++;
        }
    }

    //Teks di status bar bawah sesuai kondisi permainan
    private void updateStatusBar() {
        if (isSeriesOver) {
            statusBar.setForeground(Color.BLUE.darker());
            String winnerMsg;
            if (scoreCross > scoreNought) {
                winnerMsg = String.format("PEMENANG SERI: %s!", nameX);
            } else if (scoreNought > scoreCross) {
                winnerMsg = String.format("PEMENANG SERI: %s!", nameO);
            } else {
                winnerMsg = "HASIL SERI KESELURUHAN!";
            }
            statusBar.setText(winnerMsg);
            return;
        }
        if (currentState == State.PLAYING) {
            statusBar.setForeground(Color.BLACK);
            String turnName = (currentPlayer == Seed.CROSS) ? nameX : nameO;
            statusBar.setText("Giliran: " + turnName);
        } else if (currentState == State.DRAW) {
            statusBar.setForeground(Color.DARK_GRAY);
            statusBar.setText("RONDE SERI! Klik untuk melanjutkan.");
        } else if (currentState == State.CROSS_WON) {
            statusBar.setForeground(new Color(19, 120, 38));
            statusBar.setText(String.format("'%s' MENANG RONDE INI! Klik untuk melanjutkan.", nameX));
        } else if (currentState == State.NOUGHT_WON) {
            statusBar.setForeground(new Color(19, 120, 38));
            statusBar.setText(String.format("'%s' MENANG RONDE INI! Klik untuk melanjutkan.", nameO));
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}