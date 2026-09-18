package items;

public class Item {
    private int x, y;
    private final ItemType type;

    public Item(ItemType type, int x, int y) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public ItemType getType() { return type; }
}
