package renderer;

import buildings.Building;
import buildings.BuildingType;
import main.World;

import java.awt.*;

public class BuildingRenderer {

    private final Color wallColor = new Color(5, 52, 239);
    private final Color doorColor = new Color(246, 214, 2);

    public void render(Building building, Camera camera, Graphics2D g) {
        int worldX = building.getX() * World.TILE_SIZE;
        int worldY = building.getY() * World.TILE_SIZE;

        int screenX = worldX - (int)camera.getX();
        int screenY = worldY - (int)camera.getY();

        Color color = null;
        switch (building.getType()) {
            case WALL -> color = wallColor;
            case DOOR -> color = doorColor;
        }

        g.setColor(color);

        if (building.getType() == BuildingType.DOOR && building.isTraversable()) {
            g.fillRect(screenX, screenY, World.TILE_SIZE / 8, World.TILE_SIZE);
        }
        else {
            g.fillRect(screenX, screenY, World.TILE_SIZE, World.TILE_SIZE);
        }
    }
}
