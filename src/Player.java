
import processing.core.PImage;

public class Player extends GameObject {
    private Float health;
    private boolean hasKey;
    public Player(int x, int y, PImage sprite) {
        super(x, y, sprite);
        health = 100f;
        hasKey = false;
    }

    public void moveUp() {
        y--;
    }

    public void moveDown() {
        y++;
    }

    public void moveLeft() {
        x--;
    }

    public void moveRight() {
        x++;
    }

    public Float getHealth() {
        return health;
    }
    public void modifyHealth(Float amount) {
        health += amount;
        if (health > 100f) health = 100f;
    }
    public boolean hasKey() {
        return hasKey;
    }
    public void setHasKey(boolean hasKey) {
        this.hasKey = hasKey;
    }
}
