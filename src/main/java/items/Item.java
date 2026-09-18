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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
