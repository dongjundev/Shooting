package shootingspaceship;

import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class Shop {

    private boolean[] Selected = new boolean[4];
    private boolean[] PurchaseAble = new boolean[4];
    private BufferedImage[] Product;
    private int ProductIndex;
    private int WeponPrice = 1500;
    private String[] names = new String[4];
    private BufferedImage Lock;
    private BufferedImage Select;

    public Shop() {
        ProductIndex = 0;
        try {
            Lock = ImageIO.read(new File("Shop/Lock.png"));
            Select = ImageIO.read(new File("Shop/Selected.png"));
        } catch (IOException error) {
            System.out.println("상점 이미지 파일 존재하지 않음");
        }
        Product = new BufferedImage[4];

        PurchaseAble[0] = false;
        PurchaseAble[1] = true;
        PurchaseAble[2] = true;
        PurchaseAble[3] = false;
        for (int i = 0; i < 4; i++) {
            try {
                Selected[i] = false;
                Product[i] = ImageIO.read(new File("Shop/shop" + (i + 1) + ".png"));
            } catch (IOException error) {
                System.out.println("상점 이미지 파일 존재하지 않음");
            }
        }
        Selected[0] = true;
    }

    public int Price() {
        return WeponPrice;
    }

    public int What() {
        return ProductIndex;
    }

    public String Buy(int Gold) {
        if (PurchaseAble[ProductIndex]) {
            if (Gold >= 1500) {
                PurchaseAble[ProductIndex] = false;
                return "구매완료";
            } else {
                System.out.println("돈이 부족합니다.");
                return "구매실패";
            }
        } else {
            for (int i = 0; i < 4; i++) {
                Selected[i] = false;
            }
            Selected[ProductIndex] = true;
        }
        return "선택됨";
    }

    public void changeProductNum(boolean right) {
        if (right) {
            if (ProductIndex < 3) {
                ++ProductIndex;
            } else {
                ProductIndex = 0;
            }
        } else {
            if (ProductIndex > 0) {
                --ProductIndex;
            } else {
                ProductIndex = 3;
            }
        }
    }

    public void Show(Graphics g) {
        g.drawImage(Product[ProductIndex], 0, 0, null);
        if (PurchaseAble[ProductIndex]) {
            g.drawImage(Lock, 480, 423, null);
        } else if (Selected[ProductIndex]) {
            g.drawImage(Select, 320, 722, null);
        }
    }
}
