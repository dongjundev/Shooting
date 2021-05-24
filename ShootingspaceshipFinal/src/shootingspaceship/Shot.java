package shootingspaceship;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

class Shot {

    protected boolean shoted;
    protected boolean shotAble;
    protected int x_pos;
    protected int y_pos;
    protected boolean alive;
    protected final int radius = 3;
    protected int bulletSize;
    private int Xspeed;
    private int Yspeed;
    protected BufferedImage bullet = null;
    private BufferedImage RB;
    private BufferedImage GB;

    public Shot() {
        x_pos = 9999;
        y_pos = 9999;
    }

    public Shot(int x, int y, int speed) {
        shoted = false;
        x_pos = x + 30;
        y_pos = y;
        alive = true;
        this.Xspeed = speed;
        this.Yspeed = 0;
        try {
            RB = ImageIO.read(new File("Character/HANDGUN_BULLET.png"));
            GB = ImageIO.read(new File("Character/GAT_BULLET.png"));
        } catch (IOException error) {
        }
    }

    Shot(float x_pos, float y_pos) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void Fire(int x, int y) {
        x_pos = x + 30;
        y_pos = y;
        shoted = true;
    }

    public boolean isShoted() {
        return shoted;
    }

    public int getY() {
        return y_pos;
    }

    public int getX() {
        return x_pos;
    }

    public void moveShot() {
        if (shoted) {
            x_pos += Xspeed;
            y_pos += Yspeed;
        }
    }

    public void Set(int Xspeed, int Yspeed) {
        this.Xspeed = Xspeed;
        this.Yspeed = Yspeed;
    }

    public void drawShot(Graphics g, Player Type) {
        if (Type instanceof Revolver || Type instanceof ShotGun) {
            if (shoted) {
                g.drawImage(RB, x_pos - RB.getWidth() / 2, y_pos - RB.getHeight() / 2, null);
            }
        } else {
            if (shoted) {
                g.drawImage(GB, x_pos - GB.getWidth() / 2, y_pos - GB.getHeight() / 2, null);
            }
        }
    }

    public void drawShot(Graphics g) {
        if (shoted) {
            g.drawImage(bullet, x_pos - bullet.getWidth() / 2, y_pos - bullet.getHeight() / 2, null);
        }
    }

    public void collided() {
        x_pos = 9999;
        y_pos = 9999;
        Yspeed = 0;
        shoted = false;
    }
}
