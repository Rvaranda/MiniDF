package main;

public class Tile {
    private final int x;
    private final int y;
    private TileType type;

    private boolean hasTree = false;

    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void spawnTree() {
        if (type != TileType.WALL)
            hasTree = true;
    }

    public void chopTree() {
        hasTree = false;
    }

    public void mine() {
        if (type == TileType.WALL)
            type = TileType.GRASS;
    }

    public boolean hasTree() {
        return hasTree;
    }

    public void createStockpile() {
        type = TileType.STOCKPILE;
    }

    public void placeWall() {
        type = TileType.WALL;
        hasTree = false;
    }

    public TileType getType() {
        return type;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isTraversable() {
        return !hasTree && type != TileType.WALL;
    }
}
