package shootingspaceship;

import java.awt.Graphics;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

class BHC extends Boss {

    private int Cooltime = 0;
    private int frameIndex = 0;
    private double frameCooltime = 0;
    private int selectedSkill;              //골라진 스킬
    private Random rand = new Random();
    private int BulletYspeed = 0;
    private int BulletXspeed = 0;
    private int ImageXpixel = 401;
    private int ImageYpixel = 406;
    private int ImageYpos;
    private int ImageXpos;
    private boolean skillOn = false;
    private int rgb = 0;//보스 그림의 rgb
    private BufferedImage[] NormalImage = new BufferedImage[3];
    private BufferedImage[] AttackImage = new BufferedImage[4];

    public BHC(int x_pos, int y_pos, int max_x, int max_y) {
        this.x_pos = x_pos;
        this.y_pos = y_pos;
        this.max_x = max_x;
        this.max_y = max_y;

        for (int i = 0; i < 4; ++i) {
            try {
                if (i < 3) {
                    NormalImage[i] = ImageIO.read(new File("BHC/JJH_" + i + "_NOR.png"));
                    AttackImage[i] = ImageIO.read(new File("BHC/JJH_" + i + ".png"));
                } else {
                    AttackImage[i] = ImageIO.read(new File("BHC/JJH_" + i + ".png"));
                }

            } catch (IOException error) {
                System.out.println("BHC 사진이 없습니다.");
            }
        }
        blood = 40;
        maxBlood = blood;
        speed_y = 2;
        speed_x = 0;
        collision_distance = 200;
    }

    public int selectedSkill() {
        ++Cooltime;
        if (Cooltime == 500) {
            Cooltime = 0;
            selectedSkill = rand.nextInt(3);                 //랜덤으로 보스 스킬 고르기
        }
        if (selectedSkill == 2) {
            skillOn = true;
        } else {
            skillOn = false;
        }
        return selectedSkill;
    }

    public int ShotTerm() {
        return ShotTerm;
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
                    if (skillOn) {
                        rgb = AttackImage[frameIndex].getRGB(ImageXpos, ImageYpos);
                    } else {
                        rgb = NormalImage[frameIndex].getRGB(ImageXpos, ImageYpos);
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
                BulletYspeed = 5;
                BulletXspeed = 10;
                shot = new BossShot[8];
                for (int i = 0; i < 8; i++) {
                    shot[i] = new BossShot("BHC/BRK_HOTLIL1.png", BulletXspeed, false);
                }
                return shot;
            }
            case 1: {                //스킬1
                BulletYspeed = 5;
                BulletXspeed = 6;
                shot = new BossShot[5];
                shot[0] = new BossShot("BHC/BRK_HOTLIL1.png", BulletXspeed, BulletYspeed - 1, false);
                shot[1] = new BossShot("BHC/BRK_HOTLIL1.png", BulletXspeed, BulletYspeed - 3, false);
                shot[2] = new BossShot("BHC/BRK_HOTLIL1.png", BulletXspeed, false);
                shot[3] = new BossShot("BHC/BRK_HOTLIL1.png", BulletXspeed, -(BulletYspeed - 1), false);
                shot[4] = new BossShot("BHC/BRK_HOTLIL1.png", BulletXspeed, -(BulletYspeed - 3), false);
                return shot;
            }
            default: {               //스킬2
                BulletYspeed = 5;
                BulletXspeed = 6;
                shot = new BossShot[12];
                for (int i = 0; i < 12; i++) {
                    shot[i] = new BossShot("BHC/BRK_0_SHOT.png", BulletXspeed - 5, BulletYspeed, 30 * i, false);
                }
                return shot;
            }
        }
    }

    public void FrameCoolTime() {
        frameCooltime++;

        if (skillOn == true) {
            if (frameCooltime >= 12) {
                frameCooltime = 0;
                if (frameIndex < 3) {
                    ++frameIndex;
                } else {
                    frameIndex = 0;
                }

            }

        } else {
            if (frameIndex == 3) {
                frameIndex = 0;
            }
            if (frameCooltime >= 12) {
                frameCooltime = 0;

                if (frameIndex < 2) {
                    ++frameIndex;
                } else {
                    frameIndex = 0;
                }

            }
        }
    }

    @Override
    public void draw(Graphics g) {
        FrameCoolTime();
        if (skillOn == false) {
            ImageXpixel = NormalImage[frameIndex].getWidth();
            ImageYpixel = NormalImage[frameIndex].getHeight();
            g.drawImage(NormalImage[frameIndex], (int) x_pos - (ImageXpixel / 2), (int) y_pos - (ImageYpixel / 2), null);
        } else {
            ImageXpixel = AttackImage[frameIndex].getWidth();
            ImageYpixel = AttackImage[frameIndex].getHeight();
            g.drawImage(AttackImage[frameIndex], (int) x_pos - ((ImageXpixel / 2) - 30), (int) y_pos - (ImageYpixel / 2), null);
        }
    }
}
