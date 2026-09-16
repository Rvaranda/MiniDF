package renderer;

import main.Dwarf;
import main.World;

import java.awt.*;

public class DwarfRenderer {
    private Color dwarfColor = new Color(244, 6, 6);

    public void render(Dwarf dwarf, Camera camera, Graphics2D g) {
        int worldX = dwarf.getX() * World.TILE_SIZE;
        int worldY = dwarf.getY() * World.TILE_SIZE;

        int screenX = worldX - (int)camera.getX();
        int screenY = worldY - (int)camera.getY();

        g.setColor(dwarfColor);
        g.fillRect(screenX, screenY, World.TILE_SIZE, World.TILE_SIZE);
    }
}
