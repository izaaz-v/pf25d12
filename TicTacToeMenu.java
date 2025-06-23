import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToeMenu extends JFrame {
    public TicTacToeMenu() {
        setTitle("Tic-Tac-Toe: Ayo Main Rek!!!");
        setSize(360, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Panel utama
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(244, 244, 244)); // #f4f4f4
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Judul
        JLabel title = new JLabel("Tic-Tac-Toe: Ayo Main Rek!!!", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(33, 33, 33)); // hitam abu
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(title);

        // Tombol menu
        String[] labels = {"Mulai Rek!", "Skor", "Pengaturan", "Keluar Rek!"};
        for (String label : labels) {
            JButton button = new JButton(label);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(250, 40));
            button.setFocusPainted(false);
            button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            button.setBackground(new Color(76, 175, 80)); // #4CAF50
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            // Hover effect
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(56, 142, 60)); // #388E3C
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(new Color(76, 175, 80)); // back to original
                }
            });

            // Aksi tombol
            button.addActionListener(e -> {
                switch (label) {
                    case "Mulai Rek!":
                        // Membuka GameMain dalam window baru
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

            panel.add(Box.createRigidArea(new Dimension(0, 10))); // Jarak antar tombol
            panel.add(button);
        }

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        // Gunakan thread GUI
        SwingUtilities.invokeLater(() -> new TicTacToeMenu());
    }
}
