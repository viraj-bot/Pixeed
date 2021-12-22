package com.example.softablitz;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import javax.imageio.*;
import javax.swing.*;


class ImagePanel extends JPanel {
    private Image image;

    public ImagePanel(Image image) {
        this.image = image;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Insets insets = getInsets(); //draw within border
        int w = getWidth() - insets.left - insets.right;
        int h = getHeight() - insets.top - insets.bottom;
        renderImage(g, image, insets.left, insets.top, w, h);
    }

    protected void renderImage(Graphics g, Image image, int x, int y, int w, int h){
        g.drawImage(image, x, y, w, h, this);
    }

}

class AveragingImagePanel extends ImagePanel {
    public AveragingImagePanel(Image image) {
        super(image);
    }

    protected void renderImage(Graphics g, Image image, int x, int y, int w, int h){
        Image rescaled = image.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);
        g.drawImage(rescaled, x, y, w, h, this);
    }

}

public class ImagePanelTest {
    public static void main(String[] args) throws IOException {
        String url = "https://i.ibb.co/zV1xCBT/pic1.jpg";
        BufferedImage image = ImageIO.read(new URL(url));
        Dimension size = new Dimension(image.getWidth()/3, image.getHeight()/3);
        JComponent panel1 = new ImagePanel(image);
        panel1.setPreferredSize(size);
        panel1.setBorder(BorderFactory.createTitledBorder("Using basic drawImage"));
        JComponent panel2 = new AveragingImagePanel(image);
        panel2.setPreferredSize(size);
        panel2.setBorder(BorderFactory.createTitledBorder("Using SCALE_AREA_AVERAGING"));
        JPanel cp = new JPanel(new GridLayout(1,0));
        cp.add(panel1);
        cp.add(panel2);
        JFrame.setDefaultLookAndFeelDecorated(true);
        final JFrame f = new JFrame("ImagePanelTest");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setContentPane(cp);
        EventQueue.invokeLater(new Runnable(){
            public void run() {
                f.pack();
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }
}
