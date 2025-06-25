
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public enum Seed {
    CROSS("X", "images/cross_default.png"),
    NOUGHT("O", "images/nought_default.png"),
    NO_SEED(" ", null);

    private String displayName;
    private ImageIcon icon;

    Seed(String name, String imageFilename) {
        this.displayName = name;
        if (imageFilename != null) {
            setImage(imageFilename);
        }
    }

    public void setImage(String imageFilename) {

        URL imgURL = getClass().getResource(imageFilename);
        if (imgURL != null) {
            this.icon = new ImageIcon(imgURL);
        } else {
            System.err.println("Gagal memuat skin: " + imageFilename);
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public Image getImage() {
        return (icon != null) ? icon.getImage() : null;
    }
}