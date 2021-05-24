package shootingspaceship;

import java.awt.Toolkit;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class ShotGun extends Player {

    private final int radius = 3;
    private int bulletSize;
    private Image bulletHg = Toolkit.getDefaultToolkit().getImage("HANDGUN_BULLET.png");

    public ShotGun(int x, int y, int min_x, int max_x, int min_y, int max_y) {
        super(x, y, min_x, max_x, min_y, max_y);
        for (int i = 0; i < 3; i++) {
            try {
                StopImage[i] = ImageIO.read(new File("Character/ShotGun/SHOTGUN_STAND" + (i + 1) + ".png"));
                ForwardImage[i] = ImageIO.read(new File("Character/ShotGun/SHOTGUN_FORWARD" + (i + 1) + ".png"));
                BackwardImage[i] = ImageIO.read(new File("Character/ShotGun/SHOTGUN_BACKWARD" + (i + 1) + ".png"));
                UpDownImage[i] = ImageIO.read(new File("Character/ShotGun/SHOTGUN_UPDOWN" + (i + 1) + ".png"));
            } catch (IOException e) {
                System.out.println("이미지 읽을수 없음");
            }
        }
        try {
            Death = ImageIO.read(new File("Character/ShotGun/SHOTGUN_DEATH.png"));
        } catch (IOException e) {
            System.out.println("이미지 읽을수 없음");
        }
        ShotTerm = 50;
    }

    int ShotTerm() {
        return ShotTerm;
    }
}
