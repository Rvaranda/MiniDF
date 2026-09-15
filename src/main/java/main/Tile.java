package main;

public class Tile {
    private final int x;
    private final int y;
    private TileType type;
    private int visualVariant;

    public Tile(int x, int y, TileType type, int visualVariant) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.visualVariant = visualVariant;
    }

    public TileType getType() {
        return type;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getVisualVariant() { return visualVariant; }
}
