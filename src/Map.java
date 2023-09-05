import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Random;

public class Map {
    private final PImage[][] map = new PImage[50][40];
    private final ArrayList<PVector> empty_cells = new ArrayList<>();
    private final Game parent;
    private PVector door_coords;
    private PVector key_coords;
    public Map(Game parent) {
        this.parent = parent;

        loadMap();
    }

    public void drawMap() {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 40; j++) {
                parent.image(map[i][j], i * 16, j * 16);
            }
        }
    }

    private void loadMap() {
        generateTerrain();

        placeDoor();

        placeKey();

        while (keyDoorDistance() < 20) {
            setBgAt((int) key_coords.x, (int) key_coords.y);

            placeKey();
        }

        placeConsumables();

        generateEnemies();
    }
    private void generateEnemies() {
        int counter = 0;

        while (counter < 10) {
            var vec = getRandomEmptyCell();
            parent.addEnemy(new Enemy((int) vec.x, (int) vec.y, parent.getAsset("ghost")));
            counter++;
        }
    }

    private void generateTerrain() {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 40; j++) {
                map[i][j] = parent.getAsset("wall");
            }
        }

        int x = parent.width / 2 / 16;
        int y = parent.height / 2 / 16;

        setBgAt(x, y);

        int counter = 0;

        while (counter < 1000) {
            int randomNum = (int) Math.floor(Math.random() * 4);

            switch (randomNum) {
                case 0 -> x++;
                case 1 -> x--;
                case 2 -> y++;
                case 3 -> y--;
            }

            if (x < 0 || x >= 50 || y < 0 || y >= 40) {
                var vec = getRandomEmptyCell();
                x = (int) vec.x;
                y = (int) vec.y;
                continue;
            }

            if (map[x][y] == parent.getAsset("wall")) {
                setBgAt(x, y);

                counter++;
            }
        }
    }

    private void placeDoor() {
        door_coords = getRandomEmptyCell();

        map[(int) door_coords.x][(int) door_coords.y] = parent.getAsset("door");
    }

    private void placeKey() {
        key_coords = getRandomEmptyCell();

        map[(int) key_coords.x][(int) key_coords.y] = parent.getAsset("keyAtGround");
    }
    private void placeConsumables() {
        int counter = 0;

        while (counter < 10) {
            var vec1 = getRandomEmptyCell();
            var vec2 = getRandomEmptyCell();
            map[(int) vec1.x][(int) vec1.y] = parent.getAsset("potion");
            map[(int) vec2.x][(int) vec2.y] = parent.getAsset("poison");
            counter++;
        }
    }


    private PVector getRandomEmptyCell() {
        Random random = new Random();

        int randomNum = random.nextInt(empty_cells.size());

        var cell = empty_cells.get(randomNum);

        empty_cells.remove(randomNum);

        return cell;
    }
    public boolean isWallAt(int x, int y) {
        if (x < 0 || x >= 50 || y < 0 || y >= 40) return true;
        return map[x][y] == parent.getAsset("wall");
    }

    public boolean isDoorAt(int x, int y) {
        return map[x][y] == parent.getAsset("door");
    }
    public boolean isPotionAt(int x, int y) {
        return map[x][y] == parent.getAsset("potion");
    }

    public boolean isPoisonAt(int x, int y) {
        return map[x][y] == parent.getAsset("poison");
    }
    public boolean isKeyAt(int x, int y) {
        return map[x][y] == parent.getAsset("keyAtGround");
    }
    public boolean isEnemyAt(int x, int y) {
        for (var enemy : parent.getEnemies()) {
            if (enemy.x == x && enemy.y == y) return true;
        }
        return false;
    }
    private float keyDoorDistance() {
        return PApplet.dist(key_coords.x, key_coords.y, door_coords.x, door_coords.y);
    }
    public void setBgAt(int x, int y) {
        map[x][y] = parent.getAsset("bg");
        empty_cells.add(new PVector(x, y));
    }
}