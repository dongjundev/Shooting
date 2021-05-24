package shootingspaceship;

import java.awt.Graphics;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

class BOOR extends Boss {

    private int Cooltime = 50;
    private int frameIndex = 0;
    private double frameCooltime = 0;
    private int selectedSkill;              //골라진 스킬
    private Random rand = new Random(0);
    private int BulletYspeed = 5;
    private int BulletXspeed = 10;
    private int LaserCoolTime = 600;
    private int LaserCoolTimeMax = 600;
    private int LaserLastingTime = 300;
    private int LaserLastingTimeMax = 300;
    private int LaserReadyTime = 200;
    private int LaserReadyTimeMax = 200;
    private boolean LaserOn;
    private int ImageXpixel = 834;
    private int ImageYpixel = 522;
    private int ImageYpos;
    private int ImageXpos;
    private int LaserImageXpos;
    private int LaserImageYpos;
    private int rgb = 0;//보스 그림의 rgb
    private int LaserRGB = 0;
    private BufferedImage[] NormalImage = new BufferedImage[5];
    private BufferedImage[] AttackImage = new BufferedImage[6];
    private BufferedImage Laser;

    public BOOR(int x_pos, int y_pos, int max_x, int max_y) {
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.max_x = max_x;
        this.max_y = max_y;

        for (int i = 0; i < 6; i++) {
            try {
                if (i < 5) {
                    NormalImage[i] = ImageIO.read(new File("BOOR/BOOR" + (i + 1) + ".png"));
                }
                AttackImage[i] = ImageIO.read(new File("BOOR/BOORLaser" + (i + 1) + ".png"));
            } catch (IOException error) {
            }
        }
        try {
            Laser = ImageIO.read(new File("BOOR/BOORLaser.png"));
        } catch (IOException error) {
        }
        blood = 100;
        maxBlood = blood;
        speed_y = 2;
        speed_x = 0;
        selectedSkill = 1;
        LaserOn = false;
    }

    public boolean isDeathWithShot(Shot[] shots) {
        for (Shot shot : shots) {
            if ((-((ImageYpixel / 2) - 1) < (y_pos - shot.getY())) && ((y_pos - shot.getY()) < ((ImageYpixel / 2) - 1))) {
                if (-((ImageXpixel / 2) - 1) < (x_pos - shot.getX()) && (x_pos - shot.getX() < ((ImageXpixel / 2) - 1))) {
                    ImageXpos = (ImageXpixel / 2) - ((int) x_pos - shot.getX());       //총알이 위치한 이 이미지에 대한 X좌표
                    ImageYpos = (ImageYpixel / 2) - ((int) y_pos - shot.getY());       //총알이 위치한 이 이미지에 대한 Y좌표
                    if (LaserOn == false) {
                        rgb = NormalImage[frameIndex].getRGB(ImageXpos, ImageYpos);
                    } else {
                        if (LaserReadyTime == 0) {
                            rgb = AttackImage[5].getRGB(ImageXpos, ImageYpos);
                        } else {
                            rgb = AttackImage[frameIndex].getRGB(ImageXpos, ImageYpos);
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

    public int selectedSkill() {
        --Cooltime;
        --LaserCoolTime;
        if (Cooltime == 0) {
            Cooltime = 1000;
            selectedSkill = rand.nextInt(3);                 //랜덤으로 보스 스킬 고르기
        }
        return selectedSkill;
    }

    public int ShotTerm() {
        if (blood < 30) {
            ShotTerm = 45;
            LaserCoolTimeMax = 100;
            LaserReadyTimeMax = 150;
        }
        return ShotTerm;
    }

    public BossShot[] generateShot(int select) {
        BossShot[] shot;
        switch (select) {                              //각 스킬의 랜덤한 보스 총알 속성
            case 0: {                //일반공격
                shot = new BossShot[1];
                shot[0] = new BossShot("BOOR/BOORBullet.png", BulletXspeed, false);
                return shot;
            }
            case 1: {                //스킬1
                shot = new BossShot[3];
                shot[0] = new BossShot("BOOR/BOORBullet.png", BulletXspeed - 3, BulletYspeed, false);
                shot[1] = new BossShot("BOOR/BOORBullet.png", BulletXspeed - 3, false);
                shot[2] = new BossShot("BOOR/BOORBullet.png", BulletXspeed - 3, -BulletYspeed, false);
                return shot;
            }
            default: {               //스킬2
                shot = new BossShot[6];
                for (int i = 0; i < 6; i++) {
                    shot[i] = new BossShot("BOOR/BOORBullet.png", BulletXspeed - 7, BulletYspeed - 2, 60 * i, false);
                }
                return shot;
            }
        }
    }

    public boolean SpecialSkill(int Char_x, int Char_y) {
        if (LaserReadyTime == 0 && LaserOn) {
            if (Char_y <= y_pos - 440 || Char_x >= x_pos + 140) {
                return false;
            }
            LaserImageXpos = 140 - (-Char_x);
            LaserImageYpos = 440 - ((int) y_pos - Char_y);
            try {
                LaserRGB = Laser.getRGB(LaserImageXpos, LaserImageYpos);
            } catch (ArrayIndexOutOfBoundsException error) {
                return false;
            }
            LaserRGB = (LaserRGB >> 24);
            if (LaserRGB != 0) {
                return true;
            }
        }
        return false;
    }

    public void FrameCoolTime() {
        if (frameIndex % 2 == 1) {
            frameCooltime += 1;
        } else {
            frameCooltime += 1.5;
        }
        if (blood > 30) {
            if (frameCooltime >= 12) {
                frameCooltime = 0;

                if (frameIndex < 4) {
                    ++frameIndex;
                } else {
                    frameIndex = 0;
                }

            }
        } else {
            if (frameCooltime >= 5) {
                frameCooltime = 0;

                if (frameIndex < 4) {
                    ++frameIndex;
                } else {
                    frameIndex = 0;
                }

            }
        }
    }

    public void draw(Graphics g) {
        FrameCoolTime();
        if (LaserOn == false) {
            g.drawImage(NormalImage[frameIndex], (int) x_pos - (ImageXpixel / 2), (int) y_pos - (ImageYpixel / 2), null);
            if (LaserCoolTime == 0) {
                LaserOn = true;
            }
        } else {
            if (LaserReadyTime != 0) {
                g.drawImage(AttackImage[frameIndex], (int) x_pos - 417, (int) y_pos - 261, null);
                --LaserReadyTime;
            } else {
                g.drawImage(AttackImage[5], (int) x_pos - 417, (int) y_pos - 261, null);
                g.drawImage(Laser, (int) -140, (int) y_pos - 440, null);
                --LaserLastingTime;
                if (LaserLastingTime == 0) {
                    LaserCoolTime = LaserCoolTimeMax;
                    LaserLastingTime = LaserLastingTimeMax;
                    LaserReadyTime = LaserReadyTimeMax;
                    LaserOn = false;
                }
            }
        }

    }
}
