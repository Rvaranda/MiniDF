import java.awt.*;

public class Tile {
    private final Color color;
    private final int x;
    private final int y;

    public Tile(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public int getScreenX() { return x * World.TILE_SIZE; }
    public int getScreenY() { return y * World.TILE_SIZE; }
}
