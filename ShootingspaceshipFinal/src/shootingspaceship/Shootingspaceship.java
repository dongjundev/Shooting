package shootingspaceship;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

public class Shootingspaceship extends JPanel implements Runnable {

    private Thread th;
    private Player player;
    private boolean playerDeath;
    private Shot[] shots;
    private Vector slaves;
    private Boss currentBoss;
    private final int shotSpeed = 20;
    private final int playerLeftUpSpeed = -6;
    private final int playerRightDownSpeed = 6;
    private int SkullSize = 0;
    private int maxSkullSize = 10;
    private int width = 1920;
    private int height = 1080;
    private final int playerMargin = 10;
    private final int enemyMaxYSpeed = 3;
    private final int enemyMaxHorizonSpeed = -3;
    private int selectedSkill;
    private boolean playerMoveLeft;
    private boolean playerMoveRight;
    private boolean playerMoveUp;
    private boolean playerMoveDown;
    private Random rand;
    private int maxShotNum = 30;
    private int playerShotTimer = 0;
    private boolean playerShoted = false;
    private int BackGroundXpos = 0;
    private BufferedImage[] Background = new BufferedImage[3];
    private BufferedImage SelectStage;
    private boolean BossZone;
    private boolean playerSelect;
    private Vector<BossShot[]>[] Skill;
    private int BossShotTerm;
    private boolean shotAble;
    private BufferedImage BossHealthGauge;
    private BufferedImage BossHealthBar;
    private BufferedImage[] Opening;
    private BufferedImage BOORClose;
    private BufferedImage NENEClose;
    private BufferedImage BHCClose;
    private boolean menu;
    private BufferedImage pause;
    private int slavecount;
    private int slaveMax;
    private int threadSpeed;
    private int makeTerm;
    private boolean BOORAble;
    private boolean BHCAble;
    private boolean NENEAble;
    private boolean Open;
    private int SceneTime;
    private int ImageIndex;
    private BufferedImage Title;
    private int ImageYpos = 0;
    private boolean TitleMenu;
    private Shop shop;
    private boolean inShop;
    private int Gold = 15000;
    private boolean Release;
    private SoundManager sm;
    private BufferedImage[] Ending;
    private BufferedImage EndingCredit;
    private boolean End;
    private int EndCount = 800;
    private BufferedImage GameOver;
    private BufferedImage PlayerLife;

    public Shootingspaceship(int width, int height) {
        shots = new Shot[maxShotNum];
        inShop = false;
        Release = false;
        this.width = width;
        this.height = height;
        player = new Revolver((int) (width * 0.1), height * 8 / 9, playerMargin, width - playerMargin, playerMargin, height - playerMargin);
        for (int i = 0; i < shots.length; i++) {
            shots[i] = player.generateShot();
        }       //직선 총알
        SceneTime = 100;
        ImageIndex = 0;
        TitleMenu = true;
        Open = true;
        playerDeath = false;
        BOORAble = true;
        End = false;
        BHCAble = true;
        NENEAble = true;
        menu = false;
        shotAble = true;
        rand = new Random(1);
        slavecount = 0;
        threadSpeed = 9;
        addMouseListener(new MenuControl());
        addKeyListener(new ShipControl());
        setFocusable(true);
        BossZone = false;
        try {
            GameOver = ImageIO.read(new File("Ending/GAMEOVER.jpg"));
            EndingCredit = ImageIO.read(new File("Ending/Credit.jpg"));
            BOORClose = ImageIO.read(new File("SelectStage/BOORClose.png"));
            BHCClose = ImageIO.read(new File("SelectStage/BHCClose.png"));
            NENEClose = ImageIO.read(new File("SelectStage/NENEClose.png"));
            Title = ImageIO.read(new File("Opening/Title.jpg"));
            BossHealthGauge = ImageIO.read(new File("BossHealthGauge/HEALTH_GAUGE.png"));
            BossHealthBar = ImageIO.read(new File("BossHealthGauge/HEALTH_BAR.png"));
            SelectStage = ImageIO.read(new File("SelectStage/SelectStage.jpg"));
            PlayerLife = ImageIO.read(new File("Character/Life.png"));
            pause = ImageIO.read(new File("Menu/Pause.png"));
        } catch (IOException error) {
        }
        Opening = new BufferedImage[7];
        Ending = new BufferedImage[4];
        for (int i = 0; i < 7; i++) {
            try {
                if (i < 4) {
                    Ending[i] = ImageIO.read(new File("Ending/ENDING" + (i + 1) + ".jpg"));
                }
                Opening[i] = ImageIO.read(new File("Opening/OPENING_" + (i + 1) + ".jpg"));
            } catch (IOException error) {
            }
        }
        shop = new Shop();
        sm = new SoundManager();
    }

    public void start() {
        th = new Thread(this);
        th.start();
    }

    private class MenuControl extends MouseAdapter {

        public void mousePressed(MouseEvent e) {
            Point pos = e.getPoint();
            if (TitleMenu) {
                if (1502 < pos.x && pos.x < 1866) {
                    if (742 < pos.y && pos.y < 846) {
                        TitleMenu = false;
                    } else if (870 < pos.getY() && pos.getY() < 977) {
                        System.exit(0);
                    }
                }
            }
            if (menu) {
                if (794 < pos.x && pos.x < 1072) {
                    if (627 < pos.y && pos.y < 722) {
                        System.exit(0);
                    } else if (505 < pos.getY() && pos.getY() < 605) {
                        menu = false;
                    }
                }
            }
            if (inShop) {
                if (310 < pos.x && pos.x < 790) {
                    if (260 < pos.y && pos.y < 840) {
                        if (shop.Buy(Gold) == "구매완료") {
                            Gold -= shop.Price();
                        } else if (shop.Buy(Gold) == "선택됨") {
                            if (shop.What() == 0) {
                                if (!(player instanceof Revolver)) {
                                    player = null;
                                    player = new Revolver((int) (width * 0.1), height * 8 / 9, playerMargin, width - playerMargin, playerMargin, height - playerMargin);
                                }
                            } else if (shop.What() == 1) {
                                if (!(player instanceof ShotGun)) {
                                    player = null;
                                    player = new ShotGun((int) (width * 0.1), height * 8 / 9, playerMargin, width - playerMargin, playerMargin, height - playerMargin);
                                }
                            } else if (shop.What() == 2) {
                                if (!(player instanceof GatlingGun)) {
                                    player = null;
                                    player = new GatlingGun((int) (width * 0.1), height * 8 / 9, playerMargin, width - playerMargin, playerMargin, height - playerMargin);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private class ShipControl extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (inShop) {
                        shop.changeProductNum(false);
                    } else {
                        if (!playerDeath) {
                            playerMoveLeft = true;
                        } else {
                            playerMoveLeft = false;
                        }
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (Open) {
                        SceneTime = 0;
                    } else if (inShop) {
                        shop.changeProductNum(true);
                    } else if (!playerDeath) {
                        playerMoveRight = true;
                    } else {
                        playerMoveRight = false;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (!playerDeath) {
                        playerMoveUp = true;
                    } else {
                        playerMoveUp = false;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (!playerDeath) {
                        playerMoveDown = true;
                    } else {
                        playerMoveDown = false;
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    if (Open) {
                        Open = false;
                    } else if (inShop) {
                        inShop = false;
                    } else {
                        if (menu == true) {
                            menu = false;
                        } else {
                            menu = true;
                        }
                    }
                    break;
            }
            if (e.getKeyChar() == 'x') {
                if (BossZone) {
                    if (!playerDeath) {
                        if (!playerShoted) {
                            if (player instanceof ShotGun) {
                                sm.Sound("Sound/Shotgun.wav", false);
                                int stack = 4;
                                int Yspeed = 3;
                                for (int i = 0; i < shots.length; i++) {
                                    if (!shots[i].isShoted()) {
                                        --stack;
                                        shots[i].Fire(player.getX() + 60, player.getY() + 45);
                                        shots[i].Set(20, Yspeed);
                                        Yspeed -= 2;
                                        if (stack == 0) {
                                            break;
                                        }
                                    }
                                }
                            } else if (player instanceof Revolver) {
                                for (int i = 0; i < shots.length; i++) {
                                    if (!shots[i].isShoted()) {
                                        sm.Sound("Sound/Revolver.wav", false);
                                        shots[i].Fire(player.getX() + 70, player.getY() + 37);
                                        break;
                                    }
                                }
                            } else {
                                for (int i = 0; i < shots.length; i++) {
                                    if (!shots[i].isShoted()) {
                                        sm.Sound("Sound/GatlingGun.wav", false);
                                        shots[i].Fire(player.getX() + 130, player.getY() + 60);
                                        break;
                                    }
                                }
                            }
                            playerShoted = true;
                        }
                    }
                } else {
                    playerSelect = true;
                    if (inShop) {
                        if (shop.Buy(Gold) == "구매완료") {
                            Gold -= shop.Price();
                        } else if (shop.Buy(Gold) == "선택됨") {
                            if (shop.What() == 0) {
                                if (!(player instanceof Revolver)) {
                                    player = null;
                                    player = new Revolver((int) (width * 0.1), height * 8 / 9, playerMargin, width - playerMargin, playerMargin, height - playerMargin);
                                }
                            } else if (shop.What() == 1) {
                                if (!(player instanceof ShotGun)) {
                                    player = null;
                                    player = new ShotGun((int) (width * 0.1), height * 8 / 9, playerMargin, width - playerMargin, playerMargin, height - playerMargin);
                                }
                            } else if (shop.What() == 2) {
                                if (!(player instanceof GatlingGun)) {
                                    player = null;
                                    player = new GatlingGun((int) (width * 0.1), height * 8 / 9, playerMargin, width - playerMargin, playerMargin, height - playerMargin);
                                }
                            }
                        }
                    }
                }
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    playerMoveLeft = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    playerMoveRight = false;
                    break;
                case KeyEvent.VK_UP:
                    playerMoveUp = false;
                    break;
                case KeyEvent.VK_DOWN:
                    playerMoveDown = false;
                    break;
            }
        }

        public void keyTyped(KeyEvent e) {
            if (!playerDeath) {
                if (e.getKeyChar() == 'x') {
                    if (BossZone) {
                        if (player instanceof GatlingGun) {
                            ((GatlingGun) player).Charge(true);
                        }
                    }
                }
            }
        }
    }

    public void run() {

        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        sm.Sound("Sound/Intro.wav", true);
        do {//스테이지 선택
            if (!BOORAble & !NENEAble & !BHCAble) {
                sm.SoundClose(sm.clip3);
                sm.Sound("Sound/ENDING.wav", false);
                while (true) {
                    repaint();
                    if (End) {
                        if (--EndCount == 0) {
                            System.exit(0);
                        }
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                    }
                }
            }
            if (playerMoveLeft) {
                player.moveX(playerLeftUpSpeed);
            } else if (playerMoveRight) {
                player.moveX(playerRightDownSpeed);
            }
            if (menu) {
                while (true) {
                    repaint();
                    if (menu == false) {
                        break;
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                    }
                }
            }

            if (68 < player.getX() && player.getX() < 720) {
                if (playerSelect) {
                    inShop = true;
                    playerSelect = false;
                }
                while (true) {
                    repaint();
                    if (inShop == false) {
                        break;
                    }
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                    }
                }
            }

            if (1650 < player.getX() && player.getX() < 1820) {                     //부어치킨
                if (BOORAble) {
                    if (playerSelect) {
                        sm.SoundStop(sm.clip3);
                        sm.Sound("Sound/BOOR.wav", "Sound/BOORBGM.wav", true);
                        currentBoss = new BOOR(width / 4 * 3, height / 2, width, height);
                        BossZone = true;
                        for (int i = 0; i < 3; i++) {
                            try {
                                Background[i] = ImageIO.read(new File("Background/BOOR_BACK" + (i + 1) + ".jpg"));
                            } catch (IOException error) {
                            }
                        }
                    }
                }
            } else if (1260 < player.getX() && player.getX() < 1370) {                     //부어치킨
                if (NENEAble) {
                    if (playerSelect) {
                        sm.SoundStop(sm.clip3);
                        sm.Sound("Sound/JS.wav", "Sound/NENEBGM.wav", true);
                        currentBoss = new NENE(width / 4 * 3, height / 2, width, height);
                        slaves = new Vector<NENEOIL>();
                        BossZone = true;
                        slaveMax = 5;
                        for (int i = 0; i < 3; i++) {
                            try {
                                Background[i] = ImageIO.read(new File("Background/UJS_BACK" + (i + 1) + ".jpg"));
                            } catch (IOException error) {
                            }
                        }
                    }
                }
            } else if (850 < player.getX() && player.getX() < 1000) {
                if (BHCAble) {
                    if (playerSelect) {
                        sm.SoundStop(sm.clip3);
                        sm.Sound("Sound/JH.wav", "Sound/BHCBGM.wav", true);
                        currentBoss = new BHC(width / 4 * 3, height / 2, width, height);
                        BossZone = true;
                        slaves = new Vector<Enemy>();
                        makeTerm = 200;
                        for (int i = 0; i < 3; i++) {
                            try {
                                Background[i] = ImageIO.read(new File("Background/JJH_BACK" + (i + 1) + ".jpg"));
                            } catch (IOException error) {
                            }
                        }
                    }
                }
            }
            if (BossZone) {           //보스방 입장
                player.selectSwitch();
                playerSelect = false;
                player.playerSet((int) (width * 0.1), height / 2);
                selectedSkill = currentBoss.selectedSkill();
                BossShotTerm = currentBoss.ShotTerm();
                Skill = new Vector[3];
                for (int i = 0; i < shots.length; i++) {
                    shots[i].collided();
                }
                for (int i = 0; i < 3; i++) {
                    Skill[i] = new Vector<BossShot[]>();
                }
                for (int i = 0; i < 7; i++) {
                    Skill[0].add(currentBoss.generateShot(0));
                    Skill[1].add(currentBoss.generateShot(1));
                    Skill[2].add(currentBoss.generateShot(2));
                }
                shotAble = true;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                }
                while (true) {
                    if (currentBoss == null) {
                        player.SetAlive(true);
                        Skill = null;
                        player.playerSet((int) (width * 0.1), height * 8 / 9);
                        BossZone = false;
                        player.selectSwitch();
                        playerShoted = false;
                        if (slaves != null) {
                            slaves.clear();
                        }
                        for (int i = 0; i < 3; i++) {
                            Background[i] = null;
                        }
                        sm.SoundClose(sm.clip2);
                        sm.SoundStart(sm.clip3);
                        break;
                    }
                    if (menu) {
                        while (true) {
                            repaint();
                            if (menu == false) {
                                break;
                            }
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                            }
                        }
                    }

                    if (player instanceof GatlingGun) {
                        ((GatlingGun) player).Charge(false);
                    }
                    if (playerShoted == true) {
                        ++playerShotTimer;
                    }
                    if (playerShotTimer > player.ShotTerm()) {
                        playerShoted = false;
                        playerShotTimer = 0;
                    }
                    playerDeath = player.Death();
                    if (player.Life() == 0) {
                        while (true) {
                            repaint();
                            if (--EndCount == 0) {
                                System.exit(0);
                            }
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException ex) {
                            }
                        }
                    }
                    for (int i = 0; i < shots.length; i++) {
                        shots[i].moveShot();
                        if (shots[i].isShoted()) {
                            if (shots[i].getX() > 1920) {
                                shots[i].collided();
                            }
                        }
                    }
                    if (BossShotTerm++ > currentBoss.ShotTerm()) {
                        BossShotTerm = 0;
                        shotAble = true;
                    }
                    int stack; //쏴야될 총알 묶음 갯수만큼 다 못쐈는가
                    for (int i = 0; i < 3; i++) {
                        Iterator<BossShot[]> bullets = Skill[i].iterator();
                        while (bullets.hasNext()) {
                            BossShot[] bullet = bullets.next();
                            stack = 0;
                            for (int j = 0; j < bullet.length; j++) {
                                if (bullet[j].isShoted()) {
                                    if (bullet[j].getX() < 0) {
                                        bullet[j].notFire();
                                        ++stack;
                                    } else if (bullet[j].getY() < 0) {
                                        bullet[j].notFire();
                                        ++stack;
                                    } else {
                                        bullet[j].moveShot(player.getX(), player.getY());
                                    }
                                } else {
                                    ++stack;
                                }
                            }
                            if (shotAble) {
                                if (selectedSkill == i) {
                                    if (stack == bullet.length) {
                                        if (currentBoss instanceof BHC && selectedSkill == 0) {
                                            bullet[0].Fire(currentBoss.getX(), currentBoss.getY() + 90);
                                            bullet[1].Fire(currentBoss.getX(), currentBoss.getY() + 60);
                                            bullet[2].Fire(currentBoss.getX(), currentBoss.getY() + 30);
                                            bullet[3].Fire(currentBoss.getX(), currentBoss.getY() + 10);
                                            bullet[4].Fire(currentBoss.getX(), currentBoss.getY() - 10);
                                            bullet[5].Fire(currentBoss.getX(), currentBoss.getY() - 30);
                                            bullet[6].Fire(currentBoss.getX(), currentBoss.getY() - 60);
                                            bullet[7].Fire(currentBoss.getX(), currentBoss.getY() - 90);
                                        } else {
                                            for (int k = 0; k < bullet.length; k++) {
                                                bullet[k].Fire(currentBoss.getX(), currentBoss.getY());
                                            }
                                        }
                                        shotAble = false;
                                    }
                                }
                            }
                            player.isCollidedWithShot(bullet);

                        }
                        if (shotAble && selectedSkill == i) {
                            Skill[i].add(currentBoss.generateShot(i));
                            shotAble = false;
                        }
                    }
                    selectedSkill = currentBoss.selectedSkill();

                    if (currentBoss.SpecialSkill(player.getX(), player.getY())) {
                        if (currentBoss instanceof BOOR) {
                            player.SetAlive(false);
                        } else if (currentBoss instanceof NENE) {
                            double Yspeed;
                            do {
                                Yspeed = rand.nextFloat() * 2 * enemyMaxYSpeed - enemyMaxYSpeed;
                            } while (Yspeed == 0);
                            float horspeed = rand.nextFloat() * enemyMaxHorizonSpeed + enemyMaxHorizonSpeed;
                            if (++slavecount <= slaveMax) {
                                slaves.add(new NENEOIL((int) currentBoss.x_pos, (int) currentBoss.y_pos, horspeed, (int) Yspeed, width, height));
                            }
                        }
                    } else {
                        slavecount = 0;
                    }
                    if (SkullSize < maxSkullSize) {
                        if (--makeTerm == 0) {
                            makeTerm = 200;
                            if (currentBoss instanceof BHC) {
                                float Yspeed;
                                do {
                                    Yspeed = rand.nextFloat() * 2 * enemyMaxYSpeed - enemyMaxYSpeed;
                                } while (Yspeed == 0);
                                float horspeed = rand.nextFloat() * enemyMaxHorizonSpeed + enemyMaxHorizonSpeed;

                                slaves.add(new Enemy(width, rand.nextInt(1080), horspeed, Yspeed, width, height));
                                SkullSize++;
                            }
                        }
                    }
                    if (currentBoss instanceof NENE) {
                        Iterator neneoilList = slaves.iterator();
                        while (neneoilList.hasNext()) {
                            NENEOIL neneoil = (NENEOIL) neneoilList.next();
                            neneoil.move();
                            if (neneoil.isCollidedWithShot(shots)) {
                                neneoilList.remove();
                            }
                            if (neneoil.isCollidedWithPlayer(player)) {           //미니몹한테 플레이어 맞았을때
                                player.SetAlive(false);
                                neneoilList.remove();
                            }
                        }
                    } else if (currentBoss instanceof BHC) {
                        Iterator mini = slaves.iterator();
                        while (mini.hasNext()) {
                            Enemy skull = (Enemy) mini.next();
                            skull.move();
                            if (skull.isDeathWithShot(shots)) {
                                mini.remove();
                            }
                            if (skull.isCollidedWithPlayer(player)) {             //미니몹한테 플레이어 맞았을때
                                player.SetAlive(false);
                                mini.remove();
                            }
                        }
                    }
                    if (playerMoveDown) {
                        player.moveY(playerRightDownSpeed);
                    } else if (playerMoveUp) {
                        player.moveY(playerLeftUpSpeed);
                    }

                    if (playerMoveLeft) {
                        player.moveX(playerLeftUpSpeed);
                    } else if (playerMoveRight) {
                        player.moveX(playerRightDownSpeed);
                    }

                    currentBoss.move();
                    Release = false;
                    repaint();

                    try {
                        Thread.sleep(threadSpeed);
                    } catch (InterruptedException ex) {
                        // do nothing
                    }

                    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                }

            }
            playerSelect = false;
            repaint();

            try {
                Thread.sleep(threadSpeed);
            } catch (InterruptedException ex) {
                // do nothing
            }

            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        } while (true);
    }

    public void EnemyDraw(Graphics g, Vector slaves) {
        Iterator mini = slaves.iterator();
        try {
            while (mini.hasNext()) {
                Enemy skull = (Enemy) mini.next();
                skull.draw(g);
            }
        } catch (ConcurrentModificationException e) {
        }
    }

    public boolean Scene(Graphics g, int index, BufferedImage[] intro, BufferedImage title, int time) {
        if (SceneTime-- < 0) {
            if (ImageIndex < index - 1) {
                ++ImageIndex;
            } else {
                if (ImageYpos < -1080) {
                    return false;
                } else {
                    g.drawImage(intro[ImageIndex], 0, ImageYpos, null);
                    g.drawImage(title, 0, ImageYpos + height, null);
                    ImageYpos -= 20;
                    return true;
                }
            }
            SceneTime = time;
        }
        g.drawImage(intro[ImageIndex], 0, 0, null);
        return true;
    }

    public void initImage(Graphics g) {
        if (BossZone) {
            if (BackGroundXpos < -(width * 2)) {
                g.drawImage(Background[2], BackGroundXpos + (width * 2), 0, this);
                g.drawImage(Background[0], BackGroundXpos + (width * 3), 0, this);
                if (BackGroundXpos == -(width * 3)) {
                    BackGroundXpos = 0;
                }
            } else if (BackGroundXpos <= -(width)) {
                g.drawImage(Background[1], BackGroundXpos + width, 0, this);
                g.drawImage(Background[2], BackGroundXpos + (width * 2), 0, this);
            } else {
                g.drawImage(Background[0], BackGroundXpos, 0, this);
                g.drawImage(Background[1], BackGroundXpos + width, 0, this);
            }
            BackGroundXpos -= 4;
        } else {
            g.drawImage(SelectStage, 0, 0, this);
            if (!BOORAble) {
                g.drawImage(BOORClose, 0, 0, this);
            }
            if (!BHCAble) {
                g.drawImage(BHCClose, 0, 0, this);
            }
            if (!NENEAble) {
                g.drawImage(NENEClose, 0, 0, this);
            }
        }
    }

    public void paintComponent(Graphics g) {
        if (Open) {
            if (!Scene(g, 7, Opening, Title, 100)) {
                Open = false;
            }
            return;
        } else if (TitleMenu) {
            ImageIndex = 0;
            SceneTime = 300;
            ImageYpos = 0;
            g.drawImage(Title, 0, 0, null);
            return;
        } else if (inShop) {
            shop.Show(g);
            return;
        } else if (!BOORAble && !NENEAble && !BHCAble) {
            if (!Scene(g, 4, Ending, EndingCredit, 200)) {
                End = true;
            }
            return;
        } else if (player.Life() == 0) {
            g.drawImage(GameOver, 0, 0, this);
            return;
        }
        initImage(g);
        player.drawPlayer(g);

        if (BossZone) {
            if (currentBoss == null) {
                return;
            }

            if (currentBoss.isCollidedWithPlayer(player)) {
                return;
            }
            if (currentBoss instanceof NENE || currentBoss instanceof BHC) {
                EnemyDraw(g, slaves);
            }

            // draw shots
            for (int i = 0; i < shots.length; i++) {
                shots[i].drawShot(g, player);
            }
            for (int i = 0; i < 3; i++) {
                Iterator<BossShot[]> bullets = Skill[i].iterator();
                while (bullets.hasNext()) {
                    BossShot[] bullet = bullets.next();
                    for (int j = 0; j < bullet.length; j++) {
                        bullet[j].drawShot(g);
                    }
                }
            }
            currentBoss.draw(g);

            if (currentBoss.isDeathWithShot(shots)) {
                if (currentBoss instanceof BOOR) {
                    BOORAble = false;
                } else if (currentBoss instanceof NENE) {
                    NENEAble = false;
                } else if (currentBoss instanceof BHC) {
                    BHCAble = false;
                }
                currentBoss = null;
                return;
            }

            g.drawImage(BossHealthGauge.getSubimage(0, 0, currentBoss.Health() * (BossHealthGauge.getWidth() - 1) / currentBoss.MaxHealth(), BossHealthGauge.getHeight()), (width - BossHealthGauge.getWidth()) / 2, 900, this);
            g.drawImage(BossHealthBar, (width - BossHealthBar.getWidth()) / 2, 900, this);
            
            if(player.Life() == 3) {							//플레이어 피통
            g.drawImage(PlayerLife, 100, 900, this);
            g.drawImage(PlayerLife, 150, 900, this);
            g.drawImage(PlayerLife, 200, 900, this);
            }
            else if(player.Life() == 2){
            	g.drawImage(PlayerLife, 150, 900, this);
                g.drawImage(PlayerLife, 200, 900, this);
            }
            else 
            {
            	g.drawImage(PlayerLife, 200, 900, this);
            }
            
            //
        }

        if (menu) {
            g.drawImage(pause, 0, 0, this);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame("병아리 슈터");
        frame.setSize(1920, 1024);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Shootingspaceship ship = new Shootingspaceship(1920, 1024);
        //System.out.println(ScreenSize.width+"  "+ScreenSize.height);
        frame.getContentPane().add(ship);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true); //전체화면
        frame.pack();
        frame.setVisible(true);

        ship.start();
    }
}
