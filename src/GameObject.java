import processing.core.PGraphics;
import processing.core.PImage;

public class GameObject {
    protected int x;
    protected int y;

    protected PImage sprite;

    public GameObject(int x, int y, PImage sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public void draw(PGraphics g) {
        g.image(sprite, x * 16, y * 16);
    }
}
