package shootingspaceship;

import java.awt.image.BufferedImage;

abstract class Boss extends Enemy {

    protected int blood;
    protected int maxBlood;
    protected BufferedImage[] NormalImage;
    protected int ShotTerm = 60;

    public void move() {
        y_pos += speed_y;
        if (y_pos < 0) {
            y_pos = 0;
            speed_y = -speed_y;
        } else if (y_pos > max_y - 100) {
            y_pos = max_y - 100;
            speed_y = -speed_y;
        }
    }

    abstract public BossShot[] generateShot(int select);

    abstract public int selectedSkill();

    abstract public int ShotTerm();

    public int Health() {
        return blood;
    }

    public int MaxHealth() {
        return maxBlood;
    }

    public int getX() {
        return (int) x_pos;
    }

    public int getY() {
        return (int) y_pos;
    }

    public boolean SpecialSkill(int x, int y) {
        return false;
    }

    public boolean isDeathWithShot(Shot[] shots) {
        for (Shot shot : shots) {
            if (shot == null) {
                continue;
            }
            if (-collision_distance <= (y_pos - shot.getY()) && (y_pos - shot.getY() <= collision_distance)) {
                if (-collision_distance <= (x_pos - shot.getX()) && (x_pos - shot.getX() <= collision_distance)) {
                    blood--;
                    if (blood <= 0) {
                        return true;
                    }
                    shot.collided();
                }
            }
        }
        return false;
    }

    public boolean isCollidedWithPlayer(Player player) {
        if (-player.getSize() <= (y_pos - player.getY()) && (y_pos - player.getY() <= player.getSize())) {
            if (-player.getSize() <= (x_pos - player.getX()) && (x_pos - player.getX() <= player.getSize())) {
                return true;
            }
        }
        return false;
    }
}
