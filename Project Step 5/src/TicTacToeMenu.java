import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class TicTacToeMenu extends JFrame {

    private int currentDifficulty = 0;
    //MusicPlayer
    private MusicPlayer musicPlayer;

    static class BackgroundPanel extends JPanel {
        private Image image;
        public BackgroundPanel() {
            try {
                java.net.URL imgUrl = getClass().getClassLoader().getResource("images/background.png");
                if (imgUrl != null) { image = new ImageIcon(imgUrl).getImage(); }
                else { System.err.println("GAGAL (Menu): File 'images/background.png' tidak ditemukan."); }
            } catch (Exception e) { e.printStackTrace(); }
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) { g.drawImage(image, 0, 0, getWidth(), getHeight(), this); }
        }
    }

    public TicTacToeMenu() {
        setTitle("Tic-Tac-Toe: Main Bareng Kelompok12");
        setSize(360, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        //MUSIK YANG DIMODIFIKASI
        musicPlayer = new MusicPlayer();
        musicPlayer.playMusicByIndex(0);

        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("Tic-Tac-Toe", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Georgia", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        panel.add(title);

        JLabel subtitle = new JLabel("Genshin Impact Edition", SwingConstants.CENTER);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("Georgia", Font.ITALIC, 18));
        subtitle.setForeground(Color.WHITE);
        subtitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));
        panel.add(subtitle);

        String[] labels = {"Mulai Rek!", "Riwayat Permainan"};
        for (String label : labels) {
            PastelButton button = new PastelButton(label);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(250, 45));
            button.addActionListener(e -> handleButtonClick(label));
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            panel.add(button);
        }

        //KONTROL MUSIK
        JPanel musicControlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        musicControlPanel.setOpaque(false);

        JLabel musicLabel = new JLabel("Musik:");
        musicLabel.setForeground(Color.WHITE);

        JComboBox<String> musicComboBox = new JComboBox<>(musicPlayer.getMusicTitles());
        musicComboBox.setPreferredSize(new Dimension(150, 25));

        musicComboBox.addActionListener(e -> {
            musicPlayer.playMusicByIndex(musicComboBox.getSelectedIndex());
        });

        musicControlPanel.add(musicLabel);
        musicControlPanel.add(musicComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(musicControlPanel);

        PastelButton exitButton = new PastelButton("Keluar");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setMaximumSize(new Dimension(250, 45));
        exitButton.addActionListener(e -> handleButtonClick("Keluar"));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(exitButton);

        setContentPane(panel);
        setVisible(true);
    }

    private void handleButtonClick(String label) {
        switch (label) {
            case "Mulai Rek!":
                Object[] modeOptions = {"Lawan Kanca (PvP)", "Lawan AI (PvE)"};
                int modeChoice = JOptionPane.showOptionDialog(this, "Lawan siapa?", "Pilih Mode", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, modeOptions, modeOptions[1]);
                if (modeChoice != JOptionPane.CLOSED_OPTION) {
                    GameMode selectedMode = (modeChoice == 0) ? GameMode.PVP : GameMode.PVE;
                    String nameX, nameO;
                    if (selectedMode == GameMode.PVE) {
                        showSettingsDialog();
                        nameX = JOptionPane.showInputDialog(this, "Masukkan Nama (X):", "Nama Kamu", JOptionPane.PLAIN_MESSAGE);
                        nameO = "Komputer";
                        Seed.CROSS.setImage("images/cross_default.png");
                        Seed.NOUGHT.setImage("images/nought_default.png");
                    } else {
                        nameX = JOptionPane.showInputDialog(this, "Masukkan nama Pemain 1 (X):", "Pemain 1", JOptionPane.PLAIN_MESSAGE);
                        nameO = JOptionPane.showInputDialog(this, "Masukkan nama Pemain 2 (O):", "Pemain 2", JOptionPane.PLAIN_MESSAGE);
                        selectSkinForPlayer(Seed.CROSS, nameX);
                        selectSkinForPlayer(Seed.NOUGHT, nameO);
                    }
                    if (nameX == null || nameX.trim().isEmpty()) { nameX = "Pemain X"; }
                    if (nameO == null || nameO.trim().isEmpty()) { nameO = "Pemain O"; }

                    JFrame gameFrame = new JFrame(GameMain.TITLE);
                    GameMain gamePanel = new GameMain(selectedMode, currentDifficulty, nameX, nameO);
                    gameFrame.setContentPane(gamePanel);
                    JMenuBar menuBar = createGameMenuBar(gameFrame, gamePanel, selectedMode);
                    gameFrame.setJMenuBar(menuBar);
                    gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    gameFrame.pack();
                    gameFrame.setLocationRelativeTo(this);
                    gameFrame.setVisible(true);
                }
                break;
            case "Riwayat Permainan":
                List<String> history = HistoryManager.loadHistory();
                if (history.isEmpty()) { JOptionPane.showMessageDialog(this, "Belum ada riwayat permainan.", "Riwayat", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Collections.reverse(history);
                    StringBuilder historyText = new StringBuilder("--- RIWAYAT PERMAINAN ---\n\n");
                    for (String line : history) {
                        String[] parts = line.split(",");
                        if (parts.length == 6) {
                            historyText.append(String.format("Waktu: %s (%s)\n", parts[0], parts[1]));
                            historyText.append(String.format("   %s [%s] vs %s [%s]\n", parts[2], parts[3], parts[4], parts[5]));
                            historyText.append("---------------------------------\n");
                        }
                    }
                    JTextArea textArea = new JTextArea(15, 40);
                    textArea.setText(historyText.toString());
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    JOptionPane.showMessageDialog(this, scrollPane, "Riwayat Permainan", JOptionPane.INFORMATION_MESSAGE);
                }
                break;
            case "Keluar":
                System.exit(0);
                break;
        }
        repaint();
    }

    private void selectSkinForPlayer(Seed playerSeed, String playerName) {
        Object[] skinOptions = {"Default", "Anemo", "Geo", "Electro", "Dendro", "Hydro", "Pyro", "Cryo"};
        String message = String.format("Pilih elemen untuk %s:", playerName);
        int choice = JOptionPane.showOptionDialog(this, message, "Pilih Elemen", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, skinOptions, skinOptions[0]);
        String imagePath;
        switch (choice) {
            case 1: imagePath = "images/anemo.png"; break;
            case 2: imagePath = "images/geo.png"; break;
            case 3: imagePath = "images/electro.png"; break;
            case 4: imagePath = "images/dendro.png"; break;
            case 5: imagePath = "images/hydro.png"; break;
            case 6: imagePath = "images/pyro.png"; break;
            case 7: imagePath = "images/cryo.png"; break;
            default: imagePath = (playerSeed == Seed.CROSS) ? "images/cross_default.png" : "images/nought_default.png"; break;
        }
        playerSeed.setImage(imagePath);
    }

    private JMenuBar createGameMenuBar(JFrame gameFrame, GameMain gamePanel, GameMode mode) {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");

        if (mode == GameMode.PVE) {
            JMenu difficultyMenu = new JMenu("Tingkat Kesulitan AI");
            ButtonGroup difficultyGroup = new ButtonGroup();
            JRadioButtonMenuItem easy = new JRadioButtonMenuItem("Ezz", currentDifficulty == 0);
            JRadioButtonMenuItem normal = new JRadioButtonMenuItem("Biasa", currentDifficulty == 1);
            JRadioButtonMenuItem hard = new JRadioButtonMenuItem("Ngeri", currentDifficulty == 2);
            easy.addActionListener(e -> gamePanel.setDifficultyLevel(0));
            normal.addActionListener(e -> gamePanel.setDifficultyLevel(1));
            hard.addActionListener(e -> gamePanel.setDifficultyLevel(2));
            difficultyGroup.add(easy); difficultyGroup.add(normal); difficultyGroup.add(hard);
            difficultyMenu.add(easy); difficultyMenu.add(normal); difficultyMenu.add(hard);
            gameMenu.add(difficultyMenu);
        }

        gameMenu.addSeparator();
        JMenuItem exitToMenu = new JMenuItem("Kembali ke Menu Utama");
        exitToMenu.addActionListener(e -> gameFrame.dispose());
        gameMenu.add(exitToMenu);

        menuBar.add(gameMenu);

        JMenu viewMenu = new JMenu("Tampilan");
        viewMenu.add(createSkinSubMenu("Pilih Skin untuk " + gamePanel.getNameX(), Seed.CROSS, gamePanel));
        viewMenu.add(createSkinSubMenu("Pilih Skin untuk " + gamePanel.getNameO(), Seed.NOUGHT, gamePanel));
        menuBar.add(viewMenu);

        return menuBar; // Return statement
    }

    private JMenu createSkinSubMenu(String title, Seed playerSeed, GameMain gamePanel) {
        JMenu skinMenu = new JMenu(title);
        String[] skinNames = {"Default", "Anemo", "Geo", "Electro", "Dendro", "Hydro", "Pyro", "Cryo"};
        String[] filePaths = {
                (playerSeed == Seed.CROSS) ? "images/cross_default.png" : "images/nought_default.png",
                "images/anemo.png", "images/geo.png", "images/electro.png", "images/dendro.png",
                "images/hydro.png", "images/pyro.png", "images/cryo.png"
        };
        for (int i = 0; i < skinNames.length; i++) {
            JMenuItem menuItem = new JMenuItem(skinNames[i]);
            final String path = filePaths[i];
            menuItem.addActionListener(e -> {
                playerSeed.setImage(path);
                gamePanel.repaintBoard();
            });
            skinMenu.add(menuItem);
        }
        return skinMenu; // Return statement
    }

    private boolean showSettingsDialog() {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 2, currentDifficulty);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("Ezz"));
        labelTable.put(1, new JLabel("Biasa"));
        labelTable.put(2, new JLabel("Ngeri"));
        slider.setLabelTable(labelTable);
        int result = JOptionPane.showConfirmDialog(this, slider, "Tingkat Kesulitan AI", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            currentDifficulty = slider.getValue();
            return true; // Return statement
        }
        return false; // Return statement
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeMenu::new);
    }
}