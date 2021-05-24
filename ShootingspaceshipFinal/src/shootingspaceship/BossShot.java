package shootingspaceship;

import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

class BossShot extends Shot {

    int angle = 0;
    Random rand = new Random();
    int ran = rand.nextInt(100);
    int Xspeed;
    int Yspeed;
    int constXspeed;
    int constYspeed;
    int num = 0;
    double angleGap;
    int angleCount;
    int returnAngle;
    int speedCount;
    boolean angleShot;
    int wideCount;
    int aiTerm = 100;
    boolean ai;

    public BossShot() {
        super();
    }

    public BossShot(String imageName, int Xspeed, int Yspeed, int angleGap, boolean ai) {
        super();
        constXspeed = Xspeed;
        constYspeed = Yspeed;
        this.angleGap = angleGap;
        this.Xspeed = Xspeed;
        this.Yspeed = Yspeed;
        angleShot = true;
        angleCount = 30;
        angle = angleGap;
        returnAngle = angle;
        speedCount = 20;
        wideCount = 50;
        try {
            bullet = ImageIO.read(new File(imageName));
        } catch (IOException error) {
            System.out.println("총알 이미지가 없습니다");
        }
        this.ai = ai;
    }

    public BossShot(String imageName, int Xspeed, int Yspeed, boolean ai) {
        super();
        this.Xspeed = Xspeed;
        this.Yspeed = Yspeed;
        angleShot = false;
        try {
            bullet = ImageIO.read(new File(imageName));
        } catch (IOException error) {
        }
        this.ai = ai;
    }

    public BossShot(String imageName, int Xspeed, boolean ai) {
        super();
        this.Xspeed = Xspeed;
        this.Yspeed = 0;
        angleShot = false;
        try {
            bullet = ImageIO.read(new File(imageName));
        } catch (IOException error) {
        }
        this.ai = ai;
    }

    public void notFire() {
        shoted = false;
        x_pos = 9999;
        y_pos = 9999;
        if (angleShot) {
            angle = returnAngle;
            Xspeed = constXspeed;
            Yspeed = constYspeed;
        }
        if (ai) {
            aiTerm = 400;
        }
    }

    public void moveShot(int Char_x, int Char_y) {
        if (shoted) {
            if (angleShot == true) {
                if (ai) {
                    if (aiTerm > 0) {
                        if (speedCount == 0) {
                            speedCount = 20;
                            Yspeed++;
                            Xspeed++;
                        }
                        angle++;
                        angle = angle % 360;
                        --angleCount;
                        double dDegree = Math.toRadians(angle);
                        double cosd = Math.cos(dDegree);
                        double sind = Math.sin(dDegree);
                        speedCount--;
                        x_pos -= cosd * Xspeed - sind * Yspeed;
                        y_pos -= sind * Xspeed + cosd * Yspeed;
                        --aiTerm;
                    }
                    if (aiTerm == 0) {
                        x_pos -= Xspeed;
                        if (x_pos >= Char_x) {
                            double dDegree = Math.toRadians(angle);
                            double cosd = Math.cos(dDegree);
                            double sind = Math.sin(dDegree);
                            if (y_pos < Char_y) {
                                y_pos += sind * Xspeed + cosd * Yspeed;
                            } else if (y_pos > Char_y) {
                                y_pos -= sind * Xspeed + cosd * Yspeed;
                            }
                        }
                    }
                } else {
                    if (speedCount == 0) {
                        speedCount = 20;
                        Yspeed++;
                        Xspeed++;
                    }
                    angle++;
                    angle = angle % 360;
                    --angleCount;
                    double dDegree = Math.toRadians(angle);
                    double cosd = Math.cos(dDegree);
                    double sind = Math.sin(dDegree);
                    speedCount--;
                    x_pos -= cosd * Xspeed - sind * Yspeed;
                    y_pos -= sind * Xspeed + cosd * Yspeed;
                    return;
                }
            } else {
                if (ai) {
                    if (aiTerm > 0) {
                        x_pos -= Xspeed;
                        y_pos -= Yspeed;
                        --aiTerm;
                    }
                    if (aiTerm == 0) {
                        x_pos -= Xspeed;
                        if (x_pos >= Char_x) {
                            if (y_pos < Char_y) {
                                y_pos += 4;
                            } else if (y_pos > Char_y) {
                                y_pos -= 4;
                            }
                        }
                    }
                } else {
                    x_pos -= Xspeed;
                    y_pos -= Yspeed;
                }
            }
        }
    }
}
