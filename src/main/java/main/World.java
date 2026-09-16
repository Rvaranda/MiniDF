package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    public static final int TILE_SIZE = 16;
    public static final int WORLD_WIDTH = 128;
    public static final int WORLD_HEIGHT = 128;

    private Tile[] tiles = new Tile[WORLD_WIDTH * WORLD_HEIGHT];
    private List<Dwarf> dwarves = new ArrayList<>();

    Random random = new Random();

    public World() {
        for (int i = 0; i < tiles.length; i++) {
            int x = i % WORLD_WIDTH;
            int y = i / WORLD_WIDTH;
            tiles[i] = new Tile(x, y, TileType.GRASS);
        }

        spawnTrees(100);
        spawnDwarf(10, 10);
    }

    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < WORLD_WIDTH &&
                y >= 0 && y < WORLD_HEIGHT;
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public Tile getTile(int x, int y) {
        return isValidPosition(x, y) ? tiles[y * WORLD_WIDTH + x] : null;
    }

    public void spawnTrees(int amount) {
        for (int i = 0; i < amount; i++) {
            tiles[random.nextInt(tiles.length)].spawnTree();
        }
    }

    public void spawnDwarf(int x, int y) {
        int spawnX = x;
        int spawnY = y;

        while (getTile(spawnX, spawnY).hasTree()) {
            spawnX++;
            spawnY++;
            if (spawnX > 127 || spawnY > 127) return;
        }

        dwarves.add(new Dwarf(spawnX, spawnY));
    }

    public List<Dwarf> getDwarves() {
        return dwarves;
    }
}
