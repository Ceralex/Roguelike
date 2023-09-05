import processing.core.PApplet;
import processing.core.PImage;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class Game extends PApplet {

    private final HashMap<String, PImage> assets = new HashMap<>();
    private Map map;

    private Player player;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private int alpha = 0;
    private int current_frame = 0;

    @Override
    public void settings() {
        size(800, 640);
    }

    @Override
    public void setup() {
        frameRate(30);
        loadAssets();

        player = new Player(width / 16 / 2, height / 16 / 2, assets.get("player"));
        map = new Map(this);
    }

    @Override
    public void draw() {
        map.drawMap();

        player.draw(this.getGraphics());

        enemies.forEach(enemy -> enemy.draw(this.getGraphics()));

        drawHealthBar();

        if (player.hasKey()) {
            drawKey();
        }

        current_frame = (current_frame + 2) % 30;

        moveEnemies();

        checkInteraction();

        if (player.getHealth() <= 0) {
            gameOver();
        }
    }
    public static void main(String[] args) {
        PApplet.main("Game");
    }
    private void checkInteraction() {
        if (map.isDoorAt(player.x, player.y)) {
            if (player.hasKey()) {
                loadNextLevel();
            } else {
                textSize(24);
                fill(255, 255, 255);
                text("You need a key to open the door!", 10, height - 20);
            }
        } else if (map.isPotionAt(player.x, player.y)) {
            map.setBgAt(player.x, player.y);
            player.modifyHealth(10f);
        } else if (map.isPoisonAt(player.x, player.y)) {
            map.setBgAt(player.x, player.y);
            player.modifyHealth(-10f);
        } else if (map.isKeyAt(player.x, player.y)) {
            map.setBgAt(player.x, player.y);
            player.setHasKey(true);
        } else if (map.isEnemyAt(player.x, player.y)) {
            if (current_frame == 0) {
                player.modifyHealth(-15f);
            }
        }
    }
    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
            exit();
        }

        switch (event.getKeyCode()) {
            case java.awt.event.KeyEvent.VK_UP -> {
                if (map.isWallAt(player.x, player.y - 1)) return;

                player.moveUp();
            }
            case java.awt.event.KeyEvent.VK_DOWN -> {
                if (map.isWallAt(player.x, player.y + 1)) return;

                player.moveDown();
            }
            case java.awt.event.KeyEvent.VK_LEFT -> {
                if (map.isWallAt(player.x - 1, player.y)) return;

                player.moveLeft();
            }
            case java.awt.event.KeyEvent.VK_RIGHT -> {
                if (map.isWallAt(player.x + 1, player.y)) return;

                player.moveRight();
            }
        }
    }
    private void loadAssets() {
        assets.put("bg", loadImage("assets/bg.png"));
        assets.put("wall", loadImage("assets/wall.png"));
        assets.put("player", loadImage("assets/player.png"));
        assets.put("ghost", loadImage("assets/ghost.png"));
        assets.put("door", loadImage("assets/door.png"));
        assets.put("potion", loadImage("assets/potion.png"));
        assets.put("poison", loadImage("assets/poison.png"));
        assets.put("keyAtGround", loadImage("assets/keyAtGround.png"));
        assets.put("keyIcon", loadImage("assets/keyIcon.png"));
    }
    public PImage getAsset(String name) {
        return assets.get(name);
    }
    private void loadNextLevel() {
        fill(0, 0, 0, alpha);
        rect(0,0, width, height);

        if (alpha < 255) {
            alpha += 5;
        }

        if (alpha >= 255) {
            delay(500);
            alpha = 0;
            player.x = width / 16 / 2;
            player.y = height / 16 / 2;
            enemies.clear();

            map = new Map(this);
            player.setHasKey(false);
        }
    }
    private void drawHealthBar() {
        fill(92, 179, 11);
        float barWidth = player.getHealth();
        int barHeight = 15;
        rect(width - barWidth - 10, height - barHeight - 10, barWidth, barHeight);
    }
    private void drawKey() {
        var keyIcon = assets.get("keyIcon");
        keyIcon.resize(48, 48);
        image(keyIcon, 10, height - 58);
    }
    private void gameOver() {
        alpha += 5;
        fill(0, 0, 0, alpha);
        rect(0, 0, width, height);
        textSize(32);
        fill(255, 255, 255);
        text("You died!", width * 0.5f - 50, height * 0.5f);
        if (alpha >= 255) {
            exit();
        }
    }
    private void moveEnemies() {
        if (current_frame == 0) { // Muove i nemici massimo 2 volte al secondo
            enemies.forEach(enemy -> enemy.move(map, player));
        }
    }
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }
}
