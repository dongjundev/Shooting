package shootingspaceship;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Revolver extends Player {

    public Revolver(int x, int y, int min_x, int max_x, int min_y, int max_y) {
        super(x, y, min_x, max_x, min_y, max_y);
        ShotTerm = 10;
        for (int i = 0; i < 3; i++) {
            try {
                StopImage[i] = ImageIO.read(new File("Character/Revolver/REVOLVER_STAND" + (i + 1) + ".png"));
                ForwardImage[i] = ImageIO.read(new File("Character/Revolver/REVOLVER_FORWARD" + (i + 1) + ".png"));
                BackwardImage[i] = ImageIO.read(new File("Character/Revolver/REVOLVER_BACKWARD" + (i + 1) + ".png"));
                UpDownImage[i] = ImageIO.read(new File("Character/Revolver/REVOLVER_UPDOWN" + (i + 1) + ".png"));
            } catch (IOException e) {
                System.out.println("리볼버 이미지 읽을수 없음");
            }
        }
        try {
            Death = ImageIO.read(new File("Character/Revolver/REVOLVER_DEATH.png"));
        } catch (IOException e) {
            System.out.println("리볼버 이미지 읽을수 없음");
        }

    }

    int ShotTerm() {
        return ShotTerm;
    }
}
