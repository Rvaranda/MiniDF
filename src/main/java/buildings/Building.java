package buildings;

public class Building {
    private int x, y;
    private final BuildingType type;
    private boolean open;

    public Building(int x, int y, BuildingType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        open = false;
    }

    public BuildingType getType() { return type; }
    public int getX() { return x; }
    public int getY() { return y; }

    public void setOpen(boolean value) { open = value; }

    public boolean isTraversable() {
        if (type == BuildingType.DOOR) {
            return open;
        }

        return false;
    }
}
