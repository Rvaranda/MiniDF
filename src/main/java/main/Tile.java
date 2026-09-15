package main;

public class Tile {
    private final int x;
    private final int y;
    private TileType type;

    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public TileType getType() {
        return type;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}
