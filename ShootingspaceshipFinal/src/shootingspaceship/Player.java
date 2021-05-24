package shootingspaceship;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

abstract class Player {

    private int x_pos;
    private int y_pos;
    private int min_x;
    private int max_x;
    private int min_y;
    private int max_y;
    private int fireCount = 5;
    private boolean selecting;
    private boolean stop;
    private boolean forward;
    private boolean backward;
    private boolean upDown;
    private int imageXpos;
    private int imageYpos;
    public BufferedImage[] StopImage = new BufferedImage[3];
    public BufferedImage[] ForwardImage = new BufferedImage[3];
    public BufferedImage[] BackwardImage = new BufferedImage[3];
    public BufferedImage[] SelectImage = new BufferedImage[2];
    public BufferedImage[] UpDownImage = new BufferedImage[3];
    private int rgb = 0;
    private final int CharSize = 30;
    private int frameCooltime = 5;
    private int frameIndex = 0;
    protected int ShotTerm;
    protected BufferedImage Death;
    protected boolean alive;
    private int ImageXpixel;
    private int ImageYpixel;
    private int invincibility = 0;
    private int life = 3;

    public Player(int x, int y, int min_x, int max_x, int min_y, int max_y) {
        selecting = true;
        x_pos = x;
        y_pos = y;
        this.min_x = min_x;
        this.max_x = max_x;
        this.min_y = min_y;
        this.max_y = max_y;
        this.stop = true;
        this.forward = false;
        this.backward = false;
        this.upDown = false;
        alive = true;
        for (int i = 0; i < 2; i++) {
            try {
                SelectImage[i] = ImageIO.read(new File("Character/NormalWalking" + (i + 1) + ".png"));
            } catch (IOException error) {
                System.out.println("걷기 이미지 읽을 수 없음");
            }
        }
    }

    public int Life() {
        return life;
    }

    abstract int ShotTerm();

    public void playerSet(int x, int y) {
        x_pos = x;
        y_pos = y;
    }

    public void moveX(int speed) {
        x_pos += speed;
        if (x_pos < min_x) {
            x_pos = min_x;
        }
        if (x_pos > max_x) {
            x_pos = max_x;
        }
        forward = false;
        backward = false;
        if (speed > 0) {
            this.forward = true;
            this.stop = false;
            upDown = false;
        } else {
            this.backward = true;
            this.stop = false;
            upDown = false;
        }
    }

    public void moveY(int speed) {
        y_pos += speed;
        if (y_pos < min_y) {
            y_pos = min_y;
        }
        if (y_pos > max_y) {
            y_pos = max_y;
        }

        if (speed > 0) {
            upDown = true;
            backward = true;
            stop = false;
        } else {
            upDown = true;
            forward = true;
            stop = false;
        }
    }

    public int getX() {
        return x_pos;
    }

    public int getY() {
        return y_pos;
    }

    public int getSize() {
        return CharSize;
    }

    public int Invincibility() {
        return invincibility;
    }

    public void SetAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean Death() {
        if (!alive) {
            if (y_pos < max_y) {
                moveY(8);
                moveX(-3);
            } else {
                playerSet((int) (max_x * 0.1), max_y / 2);
                invincibility = 50;
                --life;
                alive = true;
            }
            return true;
        }
        if (invincibility != 0) {
            --invincibility;
        }
        return false;
    }

    public Shot generateShot() {
        Shot shot = new Shot(9999, 9999, 20);
        return shot;
    }

    public void FrameCoolTime() {
        --frameCooltime;
        if (frameCooltime == 0) {
            frameCooltime = 5;
            if (!selecting) {
                if (frameIndex < 2) {
                    ++frameIndex;
                } else {
                    frameIndex = 0;
                }
            } else {
                if (frameIndex < 1) {
                    ++frameIndex;
                } else {
                    frameIndex = 0;
                }
            }
        }
    }

    public void selectSwitch() {
        if (selecting == true) {
            selecting = false;
        } else {
            selecting = true;
        }
    }

    public void drawPlayer(Graphics g) {
        FrameCoolTime();
        if (alive) {
            if (upDown == true) {
                if (!selecting) {
                    ImageXpixel = UpDownImage[frameIndex].getWidth() / 2;
                    ImageYpixel = UpDownImage[frameIndex].getHeight() / 2;
                    g.drawImage(UpDownImage[frameIndex], x_pos - (ImageXpixel / 2), y_pos - (ImageYpixel / 2), null);
                }
            } else if (stop == true) {
                if (!selecting) {
                    ImageXpixel = StopImage[frameIndex].getWidth() / 2;
                    ImageYpixel = StopImage[frameIndex].getHeight() / 2;
                    g.drawImage(StopImage[frameIndex], x_pos - (ImageXpixel / 2), y_pos - (ImageYpixel / 2), null);
                } else {
                    g.drawImage(SelectImage[1], x_pos - 60, y_pos - 100, null);
                }
            } else if (forward == true) {
                if (!selecting) {
                    ImageXpixel = ForwardImage[frameIndex].getWidth() / 2;
                    ImageYpixel = ForwardImage[frameIndex].getHeight() / 2;
                    g.drawImage(ForwardImage[frameIndex], x_pos - (ImageXpixel / 2), y_pos - (ImageYpixel / 2), null);
                } else {
                    g.drawImage(SelectImage[frameIndex], x_pos - 60, y_pos - 100, null);
                }
            } else if (backward == true) {
                if (!selecting) {
                    ImageXpixel = BackwardImage[frameIndex].getWidth() / 2;
                    ImageYpixel = BackwardImage[frameIndex].getHeight() / 2;
                    g.drawImage(BackwardImage[frameIndex], x_pos - (ImageXpixel / 2), y_pos - (ImageYpixel / 2), null);
                } else {
                    try {
                        g.drawImage(SelectImage[frameIndex], x_pos - 60, y_pos - 100, null);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        g.drawImage(SelectImage[0], x_pos - 60, y_pos - 100, null);
                    }
                }
            }

            upDown = false;
            stop = true;
            forward = false;
            backward = false;
        } else {
            ImageXpixel = Death.getWidth() / 2;
            ImageYpixel = Death.getHeight() / 2;
            g.drawImage(Death, x_pos - (ImageXpixel / 2), y_pos - (ImageYpixel / 2), null);
        }
    }

    public boolean isCollidedWithShot(BossShot[] shots) {
        for (BossShot shot : shots) {
            if (-((ImageYpixel / 2) - 1) < (y_pos - shot.getY()) && (y_pos - shot.getY() < ((ImageYpixel / 2) - 1))) {
                if (-((ImageXpixel / 2) - 1) < (x_pos - shot.getX()) && (x_pos - shot.getX() < ((ImageXpixel / 2) - 1))) {
                    if (alive) {
                        int xPos = ImageXpixel / 2 - ((int) x_pos - shot.getX());
                        int yPos = ImageYpixel / 2 - ((int) y_pos - shot.getY());
                        if (stop) {
                            rgb = StopImage[frameIndex].getRGB(xPos, yPos);
                        } else if (forward) {
                            rgb = ForwardImage[frameIndex].getRGB(xPos, yPos);
                        } else if (backward) {
                            rgb = BackwardImage[frameIndex].getRGB(xPos, yPos);
                        } else if (upDown) {
                            rgb = UpDownImage[frameIndex].getRGB(xPos, yPos);
                        }
                        rgb = rgb >> 24;
                        if (rgb != 0) {
                            shot.collided();
                            if (invincibility == 0) {
                                alive = false;
                                return true;
                            }
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
