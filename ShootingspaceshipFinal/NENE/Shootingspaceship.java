package shootingspaceship;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.*;

public class Shootingspaceship extends JPanel implements Runnable {
    private static Scanner scanner;
    private Thread th;
    private Player player;
    private Shot[] shots;
    private ArrayList<BossShot[]> BossShots;            //보스 총알 묶음을 갖고있는 어레이 리스트
    private ArrayList enemies;
    private ArrayList neneoils;                     //*추가
    private ArrayList bosses;
    private Boss currentBoss;
    private final int bossMaxYSpeed=3;
    private final float bossYSpeedInc=0.3f;
    private final int shotSpeed = 20;
    private final int playerLeftUpSpeed = -6;
    private final int playerRightDownSpeed = 6;
    private final int width = 1920;
    private final int height = 800;
    private final int playerMargin = 10;
    private final int enemyMaxYSpeed = 3;
    private final int enemyMaxHorizonSpeed = -3;
    private final int enemyTimeGap = 2000; //unit: msec
    private final float enemyYSpeedInc = 0.3f;
    private final int maxEnemySize = 10;
    private final int maxBossSize = 1;
    private final int maxNeneoilSize = 5;     //*추가
    private int enemySize;
    private int bossSize;
    private int neneoilSize;        //*추가
    private javax.swing.Timer timer;
    private boolean playerMoveLeft;
    private boolean playerMoveRight;
    private boolean playerMoveUp;
    private boolean playerMoveDown;
    private Image dbImage;
    private Graphics dbg;
    private Random rand;
    private int maxShotNum = 100;
    private int shotTimer=0;
    private int playerShotTimer=0;
    private int shotTimerP=0;
    private static int selectedStage=0;
    private boolean playerShoted=false;
    int cooltime;

    public Shootingspaceship() {
        setBackground(Color.black);
        setPreferredSize(new Dimension(width, height));
        player = new Player((int) (width * 0.1), height / 2, playerMargin, width-playerMargin, playerMargin, height-playerMargin );
        shots = new Shot[ maxShotNum ];
        BossShots = new ArrayList();       //직선 총알
        enemies = new ArrayList();
        enemySize = 0;
        bosses=new ArrayList();
        bossSize=0;
        neneoils=new ArrayList();           //추가
        neneoilSize=0;                      //추가
        rand = new Random(1);
        timer = new javax.swing.Timer(enemyTimeGap, new addANewEnemy());
        timer.start();
        addKeyListener(new ShipControl());
        setFocusable(true);
        bosses.add(new BOOR(width/4*3,height/2,width,height));
        bosses.add(new NENE(width/4*3+150,height/2,width,height));
        scanner = new Scanner(System.in);
    }

    public void start() {
        th = new Thread(this);
        th.start();
    }

    private class addANewEnemy implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (++enemySize <= maxEnemySize) {
                float Yspeed;
                do {
                    Yspeed = rand.nextFloat() * 2 * enemyMaxYSpeed - enemyMaxYSpeed;
                } while (Yspeed == 0);

                float horspeed = rand.nextFloat() * enemyMaxHorizonSpeed+enemyMaxHorizonSpeed; 
                //System.out.println("enemySize=" + enemySize + " downspeed=" + downspeed + " horspeed=" + horspeed);

                Enemy newEnemy = new Enemy(1920, (int)(rand.nextFloat()*height), horspeed, Yspeed, width, height);
                enemies.add(newEnemy);
            }
            else {
                timer.stop();
            }
        }
    }
    
    private class ShipControl implements KeyListener {
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    playerMoveLeft = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    playerMoveRight = true;
                    break;
                case KeyEvent.VK_UP:
                    playerMoveUp = true;
                    break;
                case KeyEvent.VK_DOWN:
                    playerMoveDown = true;
                    break;
            }
            if(e.getKeyChar()=='x'&&playerShoted==false){
                for (int i = 0; i < shots.length; i++) {
                        if (shots[i] == null) {
                            shots[i] = player.generateShot();
                            playerShoted=true;
                            break;
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
        }
    }

    public void run() {
        //int c=0;
        
        
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        
        
        do {//부어치킨 do-while 문
            //System.out.println( ++c );
            // do operations on shots in shots array
            if(selectedStage==0)
            currentBoss = (BOOR)bosses.get(selectedStage);
            if(selectedStage==0){       //부어치킨이 선택되면
                Sound("bgm.wav", true);         //bgm 추가
                while(true){
                    if(currentBoss==null){
                        break;
                    }

                    if(playerShoted==true){
                        ++playerShotTimer;
                    }
                    if(playerShotTimer==10){
                        playerShoted=false;
                        playerShotTimer=0;
                    }

                    for (int i = 0; i < shots.length; i++) {
                        if (shots[i] != null) {
                            // move shot
                            shots[i].moveXShot(shotSpeed);

                            // test if shot is out
                            if (shots[i].getX() > 1920) {
                                // remove shot from array
                                shots[i] = null;
                            }
                        }
                    }

                    Iterator<BossShot[]> it = BossShots.iterator();
                    int stack=0;
                    while(it.hasNext()){
                        BossShot[] currentBossShot=it.next();
                        if(player.isCollidedWithShot(currentBossShot)){
                            System.out.println("총알맞음");
                        }
                        if(currentBossShot != null){
                            for(int j=0; j<currentBossShot.length; j++){
                                if(currentBossShot[j]!=null){
                                    currentBossShot[j].moveShot();

                                    if(currentBossShot[j].getX()<0){    //총알이 왼쪽화면 밖으로 나가면 객체 사라짐
                                        currentBossShot[j]=null;
                                    }
                                }
                                else
                                    ++stack;    
                            }
                            if(stack==currentBossShot.length){
                                currentBossShot=null;
                            }
                        }
                        stack=0;
                    }

                    if(((BOOR)currentBoss).LaserHit(player.getX(), player.getY())){
                        System.out.println("레이저 맞음");
                    }

                    if (playerMoveDown){
                        player.moveY(playerRightDownSpeed);
                    } else if (playerMoveUp) {
                        player.moveY(playerLeftUpSpeed);
                    }

                    if (playerMoveLeft) {
                        player.moveX(playerLeftUpSpeed);
                    } else if (playerMoveRight) {
                        player.moveX(playerRightDownSpeed);
                    }    

                    ++shotTimer;
                    ((BOOR)currentBoss).move();   
                    if(shotTimer==30){
                        shotTimer=0;
                        BossShots.add(((BOOR)currentBoss).generateShot());
                    }

                    repaint();

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        // do nothing
                    }

                    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                }
            }
            
            if(selectedStage==1)
            currentBoss = (NENE)bosses.get(selectedStage);
            if(selectedStage==1){       //네네치킨이 선택되면
                Sound("bgm.wav", true);         //*bgm 추가
                while(true){
//                    Sound("bgm.wav", true);
                    float Yspeed; 

                    do {
                        Yspeed = rand.nextFloat() * 2 * enemyMaxYSpeed - enemyMaxYSpeed;
                     } while (Yspeed == 0);
                     float horspeed = rand.nextFloat() * enemyMaxHorizonSpeed+enemyMaxHorizonSpeed;
                     if(((NENE)currentBoss).open()==true){
                        if (++neneoilSize <= maxNeneoilSize){ 
                             neneoils.add(new NENEOIL((int)((NENE)currentBoss).x_pos, (int)((NENE)currentBoss).y_pos, horspeed, Yspeed, width, height));
                        }
                        
                     }
                     else neneoilSize=0;

//                    enemies.add(new NENEOIL(width/4*3,height/2, 1, 1,width,height));        //*
                    Iterator neneoilList = neneoils.iterator();                                           //*
                    while (neneoilList.hasNext()) {                                                   //*
                        NENEOIL neneoil = (NENEOIL) neneoilList.next();                                   //*
                        neneoil.move();                                                               //*
                    }                                                                               //*추가
                    if(currentBoss==null){
                        break;
                    }

                    if(playerShoted==true){
                        ++playerShotTimer;
                    }
                    if(playerShotTimer==10){
                        playerShoted=false;
                        playerShotTimer=0;
                    }

                    for (int i = 0; i < shots.length; i++) {
                        if (shots[i] != null) {
                            // move shot
                            shots[i].moveXShot(shotSpeed);

                            // test if shot is out
                            if (shots[i].getX() > 1920) {
                                // remove shot from array
                                shots[i] = null;
                            }
                        }
                    }

                    Iterator<BossShot[]> it = BossShots.iterator();
                    int stack=0;
                    while(it.hasNext()){
                        BossShot[] currentBossShot=it.next();
                        if(player.isCollidedWithShot(currentBossShot)){
                            System.out.println("총알맞음");
                        }
                        if(currentBossShot != null){
                            for(int j=0; j<currentBossShot.length; j++){
                                if(currentBossShot[j]!=null){
                                    currentBossShot[j].moveShot();      

                                    if(currentBossShot[j].getX()<0){    //총알이 왼쪽화면 밖으로 나가면 객체 사라짐
                                        currentBossShot[j]=null;
                                    }
                                }
                                else
                                    ++stack;    
                            }
                            if(stack==currentBossShot.length){
                                currentBossShot=null;
                            }
                        }
                        stack=0;
                    }

                    if (playerMoveDown){
                        player.moveY(playerRightDownSpeed);
                    } else if (playerMoveUp) {
                        player.moveY(playerLeftUpSpeed);
                    }

                    if (playerMoveLeft) {
                        player.moveX(playerLeftUpSpeed);
                    } else if (playerMoveRight) {
                        player.moveX(playerRightDownSpeed);
                    }    

                    ++shotTimer;
                    ((NENE)currentBoss).move();   
                    if(shotTimer==40){
                        shotTimer=0;
                        BossShots.add(((NENE)currentBoss).generateShot());
                    }

                    repaint();

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        // do nothing
                    }

                    Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
                }
            }
        }while(true);
    }
    

    
    public void initImage(Graphics g) {
        if (dbImage == null) {
            dbImage = createImage(width, height);
            dbg = dbImage.getGraphics();
        }

        dbg.setColor(Color.BLACK);
        dbg.fillRect(0, 0, width, height);

        dbg.setColor(getForeground());
        //paint (dbg);

        g.drawImage(dbImage, 0, 0, this);
    }

    public void paintComponent(Graphics g) {
        
        initImage(g);
        player.drawPlayer(g);
        if(selectedStage==0){
            // draw player
            
                
            
            Iterator enemyList = enemies.iterator();
            while (enemyList.hasNext()) {
                Enemy enemy = (Enemy) enemyList.next();
                enemy.draw(g);

                if (enemy.isCollidedWithShot(shots)) {
                    enemyList.remove();
                }
                if (enemy.isCollidedWithPlayer(player)) {
                    enemyList.remove();

                }
            }
            if(currentBoss!=null){
                ((BOOR)currentBoss).draw(g);
            }
            if(currentBoss.isDeathWithShot(shots)){
                bosses.remove(0);
                currentBoss=null;
            }
            if(currentBoss.isCollidedWithPlayer(player)){
                System.exit(0);
            }

            // draw shots
            for (int i = 0; i < shots.length; i++) {
                if (shots[i] != null) {
                    shots[i].drawShot(g);
                }
            }

            for (int i = 0; i < BossShots.size(); i++) {
                BossShot[] currentBossShot = BossShots.get(i);
                if (currentBossShot != null) {
                    for(int j=0; j<currentBossShot.length; j++){
                        if(currentBossShot[j] != null){
                            currentBossShot[j].drawShot(g);
                        }
                    }
                }
            }
        }
        if(selectedStage==1){                               //스테이지 1번
            Iterator neneoilList = neneoils.iterator();        //*수정함
            while (neneoilList.hasNext()) {
                NENEOIL neneoil = (NENEOIL) neneoilList.next();
                neneoil.draw(g);

                if (neneoil.isCollidedWithShot(shots)) {
                    neneoilList.remove();
                }
                if (neneoil.isCollidedWithPlayer(player)) {
                    neneoilList.remove();

                }
            }
            if(currentBoss!=null){
                ((NENE)currentBoss).draw(g);
            }
            if(currentBoss.isDeathWithShot(shots)){
                bosses.remove(0);
                currentBoss=null;
            }
            if(currentBoss.isCollidedWithPlayer(player)){
                System.exit(0);
            }


            // draw shots
            for (int i = 0; i < shots.length; i++) {
                if (shots[i] != null) {
                    shots[i].drawShot(g);
                }
            }

            for (int i = 0; i < BossShots.size(); i++) {
                BossShot[] currentBossShot = BossShots.get(i);
                if (currentBossShot != null) {
                    for(int j=0; j<currentBossShot.length; j++){
                        if(currentBossShot[j] != null){
                            currentBossShot[j].drawShot(g);
                        }
                    }
                }
            }
        }
    }
    public static void Sound(String file, boolean Loop){                   // *소리출력 메소드 (wav만 가능)
        Clip clip;
        try{
//            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(file));
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
            if(Loop) clip.loop(-1);         //true일때 무한재상 
        } catch(Exception e){
            e.printStackTrace();
        }
    }
  
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JFrame frame = new JFrame("병아리 슈터");
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        GraphicsDevice gd = ge.getDefaultScreenDevice();
//        frame.setUndecorated(true);
//        gd.setFullScreenWindow(frame);
//        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Shootingspaceship ship = new Shootingspaceship();
        frame.getContentPane().add(ship);
        frame.pack();
        selectedStage=scanner.nextInt();        //BOOR == 0  BHC == 1
        frame.setVisible(true);
        ship.start();
        
        
    }
}
