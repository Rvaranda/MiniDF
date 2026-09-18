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
        hasTree = true;
    }

    public void chopTree() {
        hasTree = false;
    }

    public boolean hasTree() {
        return hasTree;
    }

    public void createStockpile() {
        type = TileType.STOCKPILE;
    }

    public TileType getType() {
        return type;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isTraversable() {
        return !hasTree;
    }
}
