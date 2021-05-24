package shootingspaceship;

import java.awt.Graphics;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

class NENEOIL extends Enemy {

    private BufferedImage[] OILImage = new BufferedImage[2];
    private int frameIndex = 0;
    private int frameCooltime = 0;
    private int ImageXpixel;
    private int ImageYpixel;
    private int rgb;

    public NENEOIL(int x, int y, float speed_x, float speed_y, int max_x, int max_y) {
        super(x, y, speed_x, speed_y, max_x, max_y);
        for (int i = 0; i < 2; i++) {
            try {
                OILImage[i] = ImageIO.read(new File("NENE/NENEOIL" + i + ".png"));
            } catch (IOException error) {
                System.out.println("오일 이미지가 없습니다");
            }
        }
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

            speed_x = -speed_x;
        }
    }

    public double collisionD(int distance) {
        return Math.sqrt((x_pos * x_pos) + ((y_pos * y_pos)));
    }

    public boolean isCollidedWithShot(Shot[] shots) {
        for (Shot shot : shots) {
            if (shot == null) {
                continue;
            }
            if (-((ImageYpixel / 2) - 1) < (y_pos - shot.getY()) && (y_pos - shot.getY() < (ImageYpixel / 2) - 1)) {
                if (-((ImageXpixel / 2) - 1) < (x_pos - shot.getX()) && (x_pos - shot.getX() < ((ImageXpixel / 2) - 1))) {
                    int ImageXpos = ImageXpixel / 2 - ((int) x_pos - shot.getX());
                    int ImageYpos = ImageYpixel / 2 - ((int) y_pos - shot.getY());
                    rgb = OILImage[frameIndex].getRGB(ImageXpos, ImageYpos);
                    rgb = rgb >> 24;
                    if (rgb != 0) {
                        shot.collided();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void FrameCoolTime() {
        frameCooltime++;
        if (frameCooltime >= 20) {
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
        ImageXpixel = OILImage[frameIndex].getWidth();
        ImageYpixel = OILImage[frameIndex].getHeight();
        g.drawImage(OILImage[frameIndex], (int) x_pos - (ImageXpixel / 2), (int) y_pos - (ImageYpixel / 2), null);
    }
}
