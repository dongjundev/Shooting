package shootingspaceship;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Enemy {

    protected float x_pos;
    protected float y_pos;
    protected float speed_x;
    protected float speed_y;
    protected int max_x;
    protected int max_y;
    protected int collision_distance = 20;
    private int frameIndex = 0;
    private double frameCooltime = 0;
    private int ImageXpixel;
    private int ImageYpixel;
    private int ImageYpos;
    private int ImageXpos;
    private int rgb = 0;
    private BufferedImage[] EnemyImage = new BufferedImage[2];

    public Enemy() {
    }

    public Enemy(int x, int y, float speed_x, float speed_y, int max_x, int max_y) {
        x_pos = x;
        y_pos = y;
        this.speed_x = speed_x;
        this.speed_y = speed_y;
        this.max_x = max_x;
        this.max_y = max_y;
        for (int i = 0; i < 2; i++) {
            try {
                EnemyImage[i] = ImageIO.read(new File("BHC/Enemy_" + i + ".png"));
            } catch (IOException error) {
            }
        }
        ImageXpixel = EnemyImage[1].getWidth();
        ImageYpixel = EnemyImage[1].getHeight();
    }

    public void move() {
        x_pos += speed_x;
        y_pos += speed_y;

        if (y_pos < 0) {
            y_pos = 0;
            speed_y = -speed_y;
        } else if (y_pos > max_y) {
            y_pos = max_y;
            speed_y = -speed_y;
        }
        if (x_pos > max_x) {
            x_pos = max_x;
            speed_x = -speed_x;
        }
        if (x_pos < 0) {
            x_pos = max_x;
        }
    }

    public double collisionD(int distance) {
        return Math.sqrt((x_pos * x_pos) + ((y_pos * y_pos) / 4));
    }

    public boolean isDeathWithShot(Shot[] shots) {
        for (Shot shot : shots) {
            if (shot == null) {
                continue;
            }
            if ((-((ImageYpixel / 2) - 1) < (y_pos - shot.getY())) && ((y_pos - shot.getY()) < ((ImageYpixel / 2) - 1))) {
                if (-((ImageXpixel / 2) - 1) < (x_pos - shot.getX()) && (x_pos - shot.getX() < ((ImageXpixel / 2) - 1))) {
                    ImageXpos = (ImageXpixel / 2) - ((int) x_pos - shot.getX());       //총알이 위치한 이 이미지에 대한 X좌표
                    ImageYpos = (ImageYpixel / 2) - ((int) y_pos - shot.getY());       //총알이 위치한 이 이미지에 대한 Y좌표
                    rgb = EnemyImage[frameIndex].getRGB(ImageXpos, ImageYpos);
                    rgb = rgb >> 24;          //RGB값의 투명도 추출
                    if (rgb != 0) {             //투명하지 않다면
                        shot.collided();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCollidedWithPlayer(Player player) {
        if (-player.getSize() <= (y_pos - player.getY()) && (y_pos - player.getY() <= player.getSize())) {
            if (-player.getSize() <= (x_pos - player.getX()) && (x_pos - player.getX() <= player.getSize())) {
                player.SetAlive(false);
                return true;
            }
        }
        return false;
    }

    public void FrameCoolTime() {
        frameCooltime++;
        if (frameCooltime >= 12) {
            frameCooltime = 0;
            if (frameIndex < 1) {
                ++frameIndex;
            } else {
                frameIndex = 0;
            }
        }

    }

    public void draw(Graphics g) {
        FrameCoolTime();
        ImageXpixel = EnemyImage[frameIndex].getWidth();
        ImageYpixel = EnemyImage[frameIndex].getHeight();
        g.drawImage(EnemyImage[frameIndex], (int) x_pos - (ImageXpixel / 2), (int) y_pos - (ImageYpixel / 2), null);
    }
}
