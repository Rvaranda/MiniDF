package renderer;

import items.Item;
import items.ItemType;
import main.World;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ItemRenderer {
    private final Map<ItemType, Color> itemsColor = new HashMap<>();

    public ItemRenderer() {
        itemsColor.put(ItemType.WOOD, new Color(238, 6, 246));
        itemsColor.put(ItemType.STONE, new Color(21, 230, 203));
    }

    public void render(Item item, Camera camera, Graphics2D g) {
        int worldX = item.getX() * World.TILE_SIZE;
        int worldY = item.getY() * World.TILE_SIZE;

        int screenX = worldX - (int)camera.getX();
        int screenY = worldY - (int)camera.getY();

        Color color = itemsColor.get(item.getType());

        g.setColor(color);
        g.fillRect(screenX, screenY, World.TILE_SIZE, World.TILE_SIZE);
    }
}
