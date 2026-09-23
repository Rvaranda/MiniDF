package main;

import items.Item;
import items.ItemType;
import jobsystem.HaulJob;
import jobsystem.JobManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class World {
    public static final int TILE_SIZE = 16;

    public final int WORLD_WIDTH;
    public final int WORLD_HEIGHT;

    private final JobManager jobManager = new JobManager();

    private Tile[] tiles;
    private List<Dwarf> dwarves = new ArrayList<>();
    private List<Item> items = new ArrayList<>();
    private List<Item> jobReservedItems = new ArrayList<>();

    private int scateredItemsCheckTimer = 20;
    private int scateredItemsCheckCounter = 0;

    Random random = new Random();

    public World() {
        WORLD_WIDTH = 128;
        WORLD_HEIGHT = 128;
        initializeTiles();

        //spawnTrees(100);
        //spawnDwarf(3, 3);
        //testes();
        placeWall(30, 30, 40, 40);
        placeWall(60, 30, 70, 40);
        spawnTrees(100);
        spawnDwarf(2, 2);
//        spawnDwarf(2, 4);
//        spawnDwarf(2, 6);
//        spawnDwarf(2, 8);
//        spawnDwarf(2, 10);
//        spawnDwarf(2, 12);
//        spawnDwarf(2, 14);
//        spawnDwarf(2, 16);
//        spawnDwarf(2, 18);
//        spawnDwarf(2, 20);
        createStockpileArea(44, 3, 53, 5);
    }

    public World(int width, int height) {
        WORLD_WIDTH = width;
        WORLD_HEIGHT = height;
        initializeTiles();
    }

    private void initializeTiles() {
        tiles = new Tile[WORLD_WIDTH * WORLD_HEIGHT];
        for (int i = 0; i < tiles.length; i++) {
            int x = i % WORLD_WIDTH;
            int y = i / WORLD_WIDTH;
            tiles[i] = new Tile(x, y, TileType.GRASS);
        }
    }

    // TODO: TESTE - apagar depois
    private void testes() {
        int[][] treesPos = {
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

    public void spawnItemsTeste(ItemType itemType, int amount) {
        Arrays.stream(tiles)
                .filter(t -> t.getType() == TileType.STOCKPILE)
                .filter(t -> getItem(t.getX(), t.getY()) == null)
                .limit(amount)
                .forEach(t -> spawnItem(itemType, t.getX(), t.getY()));
    }

    private void createStockpileArea(int x1, int y1, int x2, int y2) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                Tile tile = getTile(i, j);
                if (tile != null && tile.getType() != TileType.WALL)
                    tile.createStockpile();
            }
        }
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
            if (tile == null) continue;

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

    public Tile getFreeStockpileTile() {
        List<Tile> stockpileTiles = Arrays.stream(tiles).filter(t -> t.getType() == TileType.STOCKPILE).toList();
        List<Tile> freeStockpileTiles = stockpileTiles.stream()
                .filter(t -> {
                    Item item = items.stream().filter(i -> i.getX() == t.getX() && i.getY() == t.getY())
                            .findFirst().orElse(null);
                    return item == null;
                }).toList();
        return freeStockpileTiles.stream()
                .filter(t -> !jobManager.isPositionAssignedToHaul(t.getX(), t.getY()))
                .findFirst().orElse(null);
    }

    public Tile[] getAllTrees() {
        return Arrays.stream(tiles).filter(Tile::hasTree).toArray(Tile[]::new);
    }

    public void spawnTree(int x, int y) {
        Tile tile = getTile(x, y);
        if (tile != null && tile.getType() != TileType.WALL)
            tile.spawnTree();
    }

    public void spawnTrees(int amount) {
        for (int i = 0; i < amount; i++) {
            tiles[random.nextInt(tiles.length)].spawnTree();
        }
    }

    public void spawnItem(ItemType type, int x, int y) {
        Item item = new Item(type, x, y);
        items.add(item);
    }

    public void spawnItem(Item item) {
        items.add(item);
    }

    public Item getItem(int x, int y) {
        return items.stream().filter(i -> i.getX() == x && i.getY() == y).findFirst().orElse(null);
    }

    public Item removeItem(Item item) {
        jobReservedItems.remove(item);
        return items.remove(item) ? item : null;
    }

    public void reserveItem(Item item) {
        jobReservedItems.add(item);
    }

    public void placeWall(int x, int y) {
        Tile tile = getTile(x, y);
        if (tile != null && !tile.hasTree())
            tile.placeWall();
    }

    public void placeWall(int x1, int y1, int x2, int y2) {
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                placeWall(i, j);
            }
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

    private void checkScateredItems() {
        for (Item item : items) {
            if (jobManager.hasHaulJobFor(item)) continue;
            if (jobManager.hasBuildJobFor(item)) continue;

            Tile itemTile = getTile(item.getX(), item.getY());
            Tile destination = getFreeStockpileTile();
            if (destination != null && itemTile.getType() != TileType.STOCKPILE)
                jobManager.addJob(
                        new HaulJob(
                                itemTile,
                                destination,
                                item
                        )
                );
        }
    }

    public List<Dwarf> getDwarves() {
        return dwarves;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Item> getAvailableItems() {
        return items.stream()
                .filter(i -> !jobReservedItems.contains(i))
                .toList();
    }

    public JobManager getJobManager() { return jobManager; }

    public void update() {
        dwarves.forEach(Dwarf::update);

        scateredItemsCheckCounter++;
        if (scateredItemsCheckCounter >= scateredItemsCheckTimer) {
            scateredItemsCheckCounter = 0;
            checkScateredItems();
            jobManager.evaluateBuildJobs(this);
        }
    }
}
