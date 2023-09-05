import processing.core.PImage;

public class Enemy extends GameObject {
    public Enemy(int x, int y, PImage sprite) {
        super(x, y, sprite);
    }

    public void move(Map map, Player player) {
        if (Math.abs(player.x - x) + Math.abs(player.y - y) <= 10) {
            if (player.x > x && !map.isWallAt(x + 1, y) && !map.isEnemyAt(x + 1, y)) {
                x++;
            } else if (player.x < x && !map.isWallAt(x - 1, y) && !map.isEnemyAt(x - 1, y)) {
                x--;
            } else if (player.y > y && !map.isWallAt(x, y + 1) && !map.isEnemyAt(x, y + 1)) {
                y++;
            } else if (player.y < y && !map.isWallAt(x, y - 1) && !map.isEnemyAt(x, y - 1)) {
                y--;
            }
        }
    }
}
