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

    public Tile[] getNeighbors(int x, int y) {
        int[][] directions = {
                {0, -1}, { 0, 1},
                {1,  0}, {-1, 0},
                {1, -1}, {-1, -1},
                {-1, 1}, {1, 1}
        };

        List<Tile> neighbors = new ArrayList<>();

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];

            int nx = x + dx;
            int ny = y + dy;

            neighbors.add(getTile(nx, ny));
        }

        return neighbors.toArray(Tile[]::new);
    }

    public void spawnTree(int x, int y) {
        getTile(x, y).spawnTree();
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
            if (spawnX > WORLD_WIDTH || spawnY > WORLD_HEIGHT) return;
        }

        dwarves.add(new Dwarf(this, spawnX, spawnY));
    }

    public List<Dwarf> getDwarves() {
        return dwarves;
    }
}
