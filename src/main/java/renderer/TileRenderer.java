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

    private final Color treeColor = new Color(80, 48, 26);
    private final Color stockpileColor = new Color(99, 99, 99);
    private final Color wallColor = new Color(182, 97, 50);
    private final Color builtWallColor = new Color(5, 52, 239);

    private int getVariant(Tile tile, int variantCount) {
        int hash = tile.getX() * 73856093 ^ tile.getY() * 19349663;
        return Math.floorMod(hash, variantCount);
    }

    public void render(Tile tile, Camera camera, Graphics2D g) {
        int worldX = tile.getX() * World.TILE_SIZE;
        int worldY = tile.getY() * World.TILE_SIZE;

        int screenX = worldX - (int)camera.getX();
        int screenY = worldY - (int)camera.getY();

        Color color = null;
        switch (tile.getType()) {
            case GRASS -> color = grassColors[getVariant(tile, grassColors.length)];
            case STOCKPILE -> color = stockpileColor;
            case WALL -> color = wallColor;
            case BUILT_WALL -> color = builtWallColor;
        }

        color = tile.hasTree() ? treeColor : color;

        g.setColor(color);
        g.fillRect(screenX, screenY, World.TILE_SIZE, World.TILE_SIZE);
    }
}
