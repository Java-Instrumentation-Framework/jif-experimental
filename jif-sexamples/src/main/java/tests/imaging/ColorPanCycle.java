package tests.imaging;

import java.awt.*;


class ColorPanCycle implements FrameARGBData {
    int frame = 0;
    int width;
    int height;
    private int[] pixels;

    ColorPanCycle(int w, int h) {
        width = w;
        height = h;
        pixels = new int[width * height];
        nextFrame();
    }

    public synchronized int[] getPixels() {
        return pixels;
    }

    public synchronized void nextFrame() {
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int red = (y * 255) / (height - 1);
                int green = (x * 255) / (width - 1);
                int blue = (frame * 10) & 0xff;
                pixels[index++] = (255 << 24) | (red << 16) | (green << 8) |
                    blue;
            }
        }
        frame++;
    }

    public Dimension size() {
        return new Dimension(width, height);
    }
}
