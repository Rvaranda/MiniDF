package renderer;

import java.awt.*;
import main.Tile;
import main.World;

public class TileRenderer {
    private final Color[] grassColors = new Color[] {
            new Color(96, 194, 89),
            new Color(111, 219, 61),
            new Color(168, 222, 102),
            new Color(48, 150, 42),
            new Color(101, 191, 112),
            new Color(99, 214, 32),
    };

    public void render(Tile tile, Graphics2D g) {
        //int variant = hash(tile.getY(), tile.getY());
        int screenX = tile.getX() * World.TILE_SIZE;
        int screenY = tile.getY() * World.TILE_SIZE;

        g.setColor(grassColors[0]);
        g.fillRect(screenX, screenY, World.TILE_SIZE, World.TILE_SIZE);
    }
}
