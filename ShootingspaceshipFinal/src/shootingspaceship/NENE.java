package shootingspaceship;

import java.awt.Graphics;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

class NENE extends Boss {

    private int Cooltime = 500;
    private int frameIndex = 0;
    private double frameCooltime = 0;
    private int selectedSkill;              //골라진 스킬
    private Random rand = new Random(1);
    int ran = (int) (Math.random() * 100);
    private int BulletYspeed = 5;
    private int BulletXspeed = 10;
    private int ImageXpixel = 246;
    private int ImageYpixel = 481;
    private int ImageYpos;
    private int ImageXpos;
    private int rgb = 0;//보스 그림의 rgb
    private BufferedImage NormalImage;
    private BufferedImage[] AttackImage = new BufferedImage[2];
    private int OpenCoolTime = 600;
    private int OpenLastingTime = 300;
    private int OpenReadyTime = 200;
    private boolean OpenOn;

    public NENE(int x_pos, int y_pos, int max_x, int max_y) {
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.max_x = max_x;
        this.max_y = max_y;

        for (int i = 0; i < 2; i++) {
            try {
                if (i < 1) {
                    NormalImage = ImageIO.read(new File("NENE/UJS_NOR.png"));
                }
                AttackImage[i] = ImageIO.read(new File("NENE/UJS_OPEN" + (i + 1) + ".png"));
            } catch (IOException error) {
                System.out.println("네네 이미지 읽을수 없음");
            }
        }
        blood = 100;
        maxBlood = blood;
        speed_y = 2;
        speed_x = 0;
        collision_distance = 200;
        selectedSkill = 1;
        OpenOn = false;
    }

    public void move() {
        y_pos += speed_y;
        if (y_pos < 100) {
            y_pos = 100;
            speed_y = -speed_y;
        } else if (y_pos > max_y - 100) {
            y_pos = max_y - 100;
            speed_y = -speed_y;
        }
    }

    public boolean isDeathWithShot(Shot[] shots) {
        for (Shot shot : shots) {
            if (shot == null) {
                continue;
            }
            if ((-((ImageYpixel / 2) - 1) < (y_pos - shot.getY())) && ((y_pos - shot.getY()) < ((ImageYpixel / 2) - 1))) {
                if (-((ImageXpixel / 2) - 1) < (x_pos - shot.getX()) && (x_pos - shot.getX() < ((ImageXpixel / 2) - 1))) {
                    ImageXpos = ImageXpixel / 2 - ((int) x_pos - shot.getX());       //총알이 위치한 이 이미지에 대한 X좌표
                    ImageYpos = ImageYpixel / 2 - ((int) y_pos - shot.getY());       //총알이 위치한 이 이미지에 대한 Y좌표
                    if (OpenOn == false) {
                        rgb = NormalImage.getRGB(ImageXpos, ImageYpos);
                    } else {
                        if (OpenReadyTime != 0) {
                            rgb = AttackImage[0].getRGB(ImageXpos, ImageYpos);
                        } else {
                            rgb = AttackImage[1].getRGB(ImageXpos, ImageYpos);
                        }
                    }
                    rgb = rgb >> 24;          //RGB값의 투명도 추출
                    if (rgb != 0) {             //투명하지 않다면
                        blood--;
                        if (blood <= 0) {
                            return true;
                        }
                        shot.collided();
                    }
                }
            }
        }
        return false;
    }

    public BossShot[] generateShot(int select) {
        BossShot[] shot;
        switch (select) {                              //각 스킬의 랜덤한 보스 총알 속성
            case 0: {                //일반공격
                shot = new BossShot[1];
                shot[0] = new BossShot("NENE/EYEBULLET.png", BulletXspeed, false);
                return shot;
            }
            case 1: {                //스킬1
                shot = new BossShot[3];
                shot[0] = new BossShot("NENE/EYEBULLET.png", BulletXspeed, BulletYspeed, false);
                shot[1] = new BossShot("NENE/EYEBULLET.png", BulletXspeed, false);
                shot[2] = new BossShot("NENE/EYEBULLET.png", BulletXspeed, -BulletYspeed, false);
                return shot;
            }
            default: {               //스킬2
                shot = new BossShot[20];
                for (int i = 0; i < 20; i++) {
                    shot[i] = new BossShot("NENE/NENEBullet.png", BulletXspeed - 5, BulletYspeed, i * 30, false);

                }
                return shot;
            }
        }
    }

    public int selectedSkill() {
        --Cooltime;
        if (OpenOn == false) {
            --OpenCoolTime;
            if (OpenCoolTime == 0) {
                OpenOn = true;
            }
        } else {
            if (OpenReadyTime != 0) {
                --OpenReadyTime;
            } else {
                OpenCoolTime = 600;
                --OpenLastingTime;
                if (OpenLastingTime == 0) {
                    OpenLastingTime = 300;
                    OpenReadyTime = 100;
                    OpenOn = false;
                }
            }
        }
        if (Cooltime == 0) {
            Cooltime = 500;
            selectedSkill = rand.nextInt(3);                 //랜덤으로 보스 스킬 고르기
        }
        return selectedSkill;
    }

    public int ShotTerm() {
        return ShotTerm;
    }

    public void FrameCoolTime() {
        frameCooltime++;
        if (frameCooltime >= 12) {
            frameCooltime = 0;
            if (frameIndex < 2) {
                ++frameIndex;
            } else {
                frameIndex = 0;
            }
        }
    }

    public void draw(Graphics g) {
        FrameCoolTime();

        if (OpenOn == false) {
            ImageXpixel = NormalImage.getWidth();
            ImageYpixel = NormalImage.getHeight();
            g.drawImage(NormalImage, (int) x_pos - ImageXpixel / 2, (int) y_pos - ImageYpixel / 2, null);
        } else {
            if (OpenReadyTime != 0) {
                ImageXpixel = AttackImage[0].getWidth();
                ImageYpixel = AttackImage[0].getHeight();
                g.drawImage(AttackImage[0], (int) x_pos - ImageXpixel / 2, (int) y_pos - ImageYpixel / 2, null);
            } else {
                ImageXpixel = AttackImage[1].getWidth();
                ImageYpixel = AttackImage[1].getHeight();
                g.drawImage(AttackImage[1], (int) x_pos - ImageXpixel / 2, (int) y_pos - ImageYpixel / 2, null);
            }
        }
    }

    public boolean SpecialSkill(int x, int y) {
        return OpenOn;
    }
}
