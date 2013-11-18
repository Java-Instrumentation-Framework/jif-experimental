package edu.mbl.jif.stage;

import java.awt.image.BufferedImage;

/**
 * Defines a Site on an XY-plane, including a Z or set of Zs, and whether 'active'...
 * @author GBH
 */
public class Site {

    private int x;
    private int y;
    private int z;
    private int[] zS;
    private String name;
    private boolean active;
    private BufferedImage image;

    /** Creates a new instance of Site */    // @todo break Z's out...
    public Site() {
    }

    public Site(int x, int y, BufferedImage image) {
        this(x, y, new int[]{}, image);
    }

    public Site(int x, int y, int z, BufferedImage image) {
        this(x, y, new int[]{z}, image);
    }

    public Site(int x, int y, int[] zS, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.zS = zS;
        this.image = image;
        this.name = generateName();
    }

    private String generateName() {
        return "UniqueName";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int[] getZS() {
        return zS;
    }

    public void setZS(int[] zS) {
        this.zS = zS;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
