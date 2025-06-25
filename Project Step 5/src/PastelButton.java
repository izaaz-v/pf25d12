import javax.swing.JButton;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;


//untuk warna dan bentuk Tombol
public class PastelButton extends JButton {

    private Color colorBackground = new Color(220, 220, 255);
    private Color colorHover = new Color(200, 200, 255);
    private Color colorPressed = new Color(180, 180, 255);
    private Color colorText = new Color(80, 80, 130);

    //Untuk menentukan seberapa melengkung sudutnya.
    private int arc = 25;

    public PastelButton(String text) {
        super(text);


        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(colorText);
        setFont(new Font("Trebuchet MS", Font.BOLD, 15));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Pilih warna berdasarkan state tombol
        if (getModel().isPressed()) {
            g2.setColor(colorPressed);
        } else if (getModel().isRollover()) {
            g2.setColor(colorHover);
        } else {
            g2.setColor(colorBackground);
        }


        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

        g2.dispose();

        super.paintComponent(g);
    }
}
