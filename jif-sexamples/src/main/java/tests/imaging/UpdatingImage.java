package tests.imaging;

import java.awt.*;
import java.awt.image.*;
import javax.swing.JFrame;


public class UpdatingImage extends JFrame {
    ImageSequence seq;

    public void init() {
        seq = new ImageSequence(new ColorPanCycle(100, 100), 10);
        setLayout(null);
        add(new ImageCanvas(seq, 50, 50));
        add(new ImageCanvas(seq, 100, 100));
        seq.start();
        this.setVisible(true);
    }

    public void stop() {
        if (seq != null) {
            seq.stop();
            seq = null;
        }
    }
    
    public static void main(String[] args) {
      (new UpdatingImage()).init();
    }
}


class ImageCanvas extends Canvas {
    Image img;
    ImageProducer source;

    ImageCanvas(ImageProducer p, int w, int h) {
        source = p;
        setSize(w, h);
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        if (img == null) {
            img = createImage(source);
        }
        g.drawImage(img, 0, 0, getSize().width, getSize().height, this);
    }
}
