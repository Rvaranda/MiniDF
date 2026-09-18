package main;

import items.Item;
import items.ItemType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    public static final int TILE_SIZE = 16;
    public static final int WORLD_WIDTH = 128;
    public static final int WORLD_HEIGHT = 128;

    private Tile[] tiles = new Tile[WORLD_WIDTH * WORLD_HEIGHT];
    private List<Dwarf> dwarves = new ArrayList<>();
    private List<Item> items = new ArrayList<>();

    Random random = new Random();

    // TODO: TESTE - apagar depois
    public int[][] treesPos = {
            {15, 15}, {16, 16}, {17, 17},
            {18, 18}, {19, 19}, {20, 20},
            {21, 21}, {22, 22}, {23, 23},
            {24, 24}, {25, 25}, {26, 26},
            {30, 10}, {30, 12}, {30, 14},
            {36, 10}, {36, 12}, {36, 14},
            {40, 23}, {40, 25}, {40, 27},
            {50, 50}, {51, 51}, {52, 52},
            {53, 53}, {54, 54}, {55, 55},
            {56, 56}, {57, 57}, {58, 58},
    };

    public World() {
        for (int i = 0; i < tiles.length; i++) {
            int x = i % WORLD_WIDTH;
            int y = i / WORLD_WIDTH;
            tiles[i] = new Tile(x, y, TileType.GRASS);
        }

        spawnTrees(100);
        spawnDwarf(3, 3);
    }

    // TODO: TESTE - apagar depois
    private void testes() {
        for (int[] p : treesPos) {
            spawnTree(p[0], p[1]);
        }

        spawnDwarf(2, 2);
        spawnDwarf(2, 4);
        spawnDwarf(2, 6);
        spawnDwarf(2, 8);
        spawnDwarf(2, 10);
        spawnDwarf(2, 12);
        spawnDwarf(2, 14);
        spawnDwarf(2, 16);
        spawnDwarf(2, 18);
        spawnDwarf(2, 20);
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

    public Tile[] getNeighbors(int x, int y, boolean includeNonTraversable) {
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

            Tile tile = getTile(nx, ny);
            if (includeNonTraversable)
                neighbors.add(getTile(nx, ny));
            else if (tile.isTraversable())
                neighbors.add(getTile(nx, ny));
        }

        return neighbors.toArray(Tile[]::new);
    }

    public Tile[] getNeighbors(int x, int y) {
        return getNeighbors(x, y, false);
    }

    public Tile getRandomNeighbor(int x, int y) {
        Tile[] neighbors = getNeighbors(x, y);
        if (neighbors.length == 0) return getTile(x, y);
        return neighbors[random.nextInt(neighbors.length)];
    }

    public void spawnTree(int x, int y) {
        getTile(x, y).spawnTree();
    }

    public void spawnTrees(int amount) {
        for (int i = 0; i < amount; i++) {
            tiles[random.nextInt(tiles.length)].spawnTree();
        }
    }

    public void spawnItem(ItemType type, int x, int y) {
        items.add(new Item(type, x, y));
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

    public List<Item> getItems() {
        return items;
    }
}
