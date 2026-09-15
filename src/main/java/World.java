import java.awt.*;
import java.util.Random;

public class World {
    public static final int TILE_SIZE = 16;
    public static final int WORLD_WIDTH = GameWindow.WIDTH / TILE_SIZE;
    public static final int WORLD_HEIGHT = GameWindow.HEIGHT / TILE_SIZE;
    Random random = new Random();

    private final Color[] grassColors = new Color[] {
            new Color(96, 194, 89),
            new Color(111, 219, 61),
            new Color(168, 222, 102),
            new Color(48, 150, 42),
            new Color(101, 191, 112),
            new Color(99, 214, 32),
    };

    private Tile[] tiles = new Tile[WORLD_WIDTH * WORLD_HEIGHT];

    public World() {
        for (int i = 0; i < tiles.length; i++) {
            int x = i % WORLD_WIDTH;
            int y = i / WORLD_WIDTH;
            tiles[i] = new Tile(x, y, grassColors[random.nextInt(grassColors.length)]);
        }
    }

    public Tile[] getTiles() {
        return tiles;
    }
}
