import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LinkGameFrame extends JFrame {
    private static final int DEF_WIDTH = 830;
    private static final int DEF_HEIGHT = 600;
    private  JPanel gameInfo = null;
    private  JPanel game = null;

    public LinkGameFrame() throws HeadlessException {
        
        this.setPreferredSize(new Dimension(DEF_WIDTH, DEF_HEIGHT));
        this.setLocation(100, 50);
        this.setTitle("连连看");
        this.setResizable(false);

        gameInfo = new JPanel();




        Box box = Box.createHorizontalBox();

        // 疯狂的java的logo
        JPanel crazy = new JPanel();
        crazy.setBorder(new EtchedBorder());
        Image crazyItLogoImage = null;
        try {
            crazyItLogoImage = ImageIO.read(new File("image/crazyItLogo.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        crazy.setBackground(new Color(127, 174, 252));
        assert crazyItLogoImage != null;
        crazy.add(new JLabel(new ImageIcon(crazyItLogoImage)));



        // 疯狂的java的logo
        JPanel imagePanel = new JPanel();
        imagePanel.setBorder(new EtchedBorder());
        Image imageIco = null;
        try {
            imageIco = ImageIO.read(new File("image/logo.gif"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        imagePanel.setBackground(new Color(127, 174, 252));
        assert imageIco != null;
        imagePanel.add(new JLabel(new ImageIcon(imageIco)));



        JPanel scorePanel = new JPanel();
        scorePanel.setBorder(new EtchedBorder());
        scorePanel.setBackground(new Color(127, 174, 252));
        scorePanel.add(new JLabel("分         数" ));

        JPanel score = new JPanel();
        score.setBorder(new EtchedBorder());
        score.setBackground(new Color(123, 229, 252));
        score.add(new JLabel("         0              "));

        JPanel timePanel = new JPanel();
        timePanel.setBorder(new EtchedBorder());
        timePanel.setBackground(new Color(127, 174, 252));
        timePanel.add(new JLabel("时              间 "));

        JPanel time = new JPanel();
        time.setBorder(new EtchedBorder());
        time.setBackground(new Color(123, 229, 252));
        time.add(new JLabel("         0              "));


        JPanel gameControl = new JPanel();
        gameControl.setBorder(new EtchedBorder());
        gameControl.setLayout(new BoxLayout(gameControl, BoxLayout.X_AXIS));
        gameControl.setBackground(new Color(127, 174, 252));
        Box b = Box.createHorizontalBox();


        Button btnStart = new Button("start");
        btnStart.setBackground(new Color(127, 174, 252));

        Button btnEnd = new Button("end");
        btnStart.setBackground(new Color(127, 174, 252));
        b.add(btnStart);
        b.add(Box.createHorizontalStrut(30));
        b.add(btnEnd);
        gameControl.add(b);

        gameInfo.add(crazy);
        gameInfo.add(imagePanel);
        gameInfo.add(scorePanel);
        gameInfo.add(score);
        gameInfo.add(timePanel);
        gameInfo.add(time);
        gameInfo.add(gameControl);

        gameInfo.setLayout( new BoxLayout(gameInfo, BoxLayout.Y_AXIS));
        gameInfo.setBorder(new EtchedBorder());


        game = new LinkGamePanel();
        this.add(game, BorderLayout.CENTER);
        this.add(gameInfo, BorderLayout.EAST);
        this.pack();
    }
}
