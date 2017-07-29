import javax.swing.*;
import java.awt.*;

public class BlankPanel extends JPanel {

    public BlankPanel() {
        this.setBackground(new Color(127, 174, 252));
        JLabel label = new JLabel();
        label.setText("                          ");
        this.add(label);
        this.setVisible(true);
    }
}
