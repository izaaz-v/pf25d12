import javax.swing.*;
import java.awt.*;

public class TicTacToeMenu extends JFrame {

    // ✅ Inner class harus dideklarasikan sebelum digunakan
    class BackgroundPanel extends JPanel {
        private Image image;

        public BackgroundPanel() {
            try {
                // ✅ Memuat gambar dari folder resources
                java.net.URL imgUrl = getClass().getResource("/images/genshin.gif");
                if (imgUrl != null) {
                    image = new ImageIcon(imgUrl).getImage();
                } else {
                    System.err.println("Gagal: File /images/genshin.gif tidak ditemukan.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error saat memuat gambar background.");
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public TicTacToeMenu() {
        setTitle("Tic-Tac-Toe: Ayo Main Rek!!!");
        setSize(360, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // ✅ Gunakan BackgroundPanel setelah didefinisikan
        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        panel.setOpaque(false);

        // Judul
        JLabel title = new JLabel("Tic-Tac-Toe: Ayo Main Rek!!!", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);  // ✅ bukan setAlignme
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(title);

        // Tombol
        String[] labels = {"Mulai Rek!", "Skor", "Pengaturan", "Keluar Rek!"};
        for (String label : labels) {
            JButton button = new JButton(label);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(250, 40));
            button.setFocusPainted(false);
            button.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
            button.setBackground(new Color(158, 158, 158)); // abu-abu
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            // Hover effect
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(117, 117, 117)); // abu-abu gelap
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(110, 110, 110)); // abu-abu gelap
                }
            });

            // Action
            button.addActionListener(e -> {
                switch (label) {
                    case "Mulai Rek!":
                        JFrame gameFrame = new JFrame(GameMain.TITLE);
                        gameFrame.setContentPane(new GameMain());
                        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        gameFrame.pack();
                        gameFrame.setLocationRelativeTo(null);
                        gameFrame.setVisible(true);
                        break;
                    case "Skor":
                        JOptionPane.showMessageDialog(this, "Skor belum tersedia.");
                        break;
                    case "Pengaturan":
                        JOptionPane.showMessageDialog(this, "Fitur pengaturan masih dikembangkan.");
                        break;
                    case "Keluar Rek!":
                        System.exit(0);
                        break;
                }
            });

            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            panel.add(button);
        }

        setContentPane(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToeMenu());
    }
}
