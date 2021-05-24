package shootingspaceship;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class GatlingGun extends Player {

    private int OverHit;
    private int OverHitCount;
    private int OverHitCoolTime;
    private int ReCharge;

    public GatlingGun(int x, int y, int min_x, int max_x, int min_y, int max_y) {
        super(x, y, min_x, max_x, min_y, max_y);
        for (int i = 0; i < 3; i++) {
            try {
                StopImage[i] = ImageIO.read(new File("Character/GatlingGun/GAT_STAND" + (i + 1) + ".png"));
                ForwardImage[i] = ImageIO.read(new File("Character/GatlingGun/GAT_FORWARD" + (i + 1) + ".png"));
                BackwardImage[i] = ImageIO.read(new File("Character/GatlingGun/GAT_BACKWARD" + (i + 1) + ".png"));
                UpDownImage[i] = ImageIO.read(new File("Character/GatlingGun/GAT_UPDOWN" + (i + 1) + ".png"));
            } catch (IOException e) {
                System.out.println("개틀링 이미지 읽을수 없음");
            }
        }
        try {
            Death = ImageIO.read(new File("Character/GatlingGun/GAT_DEATH.png"));
        } catch (IOException e) {
            System.out.println("개틀링 이미지 읽을수 없음");
        }
        OverHitCoolTime = 50;
        ShotTerm = 2;
        OverHit = 200;
        OverHitCount = 0;
        ReCharge = 2;
    }

    public int ShotTerm() {
        if (OverHitCoolTime == 0) {
            return ShotTerm;
        } else {
            return 9999;
        }
    }

    public void Charge(boolean Released) {
        if (OverHitCoolTime == 0) {
            if (OverHitCount == OverHit) {
                OverHitCount = 0;
                OverHitCoolTime = 50;
            } else {
                ++OverHitCount;
            }
        } else {
            if (Released) {
                OverHitCoolTime--;
                if (ReCharge < 20) {
                    ReCharge = 20;
                }
            } else {
                if (ReCharge == 0) {
                    --OverHitCount;
                    if (OverHitCoolTime != 50) {
                        ++OverHitCoolTime;
                    }
                } else {
                    --ReCharge;
                }
            }
        }
    }
}
