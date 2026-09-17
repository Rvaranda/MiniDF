package main;

public class Dwarf {
    private int x, y;
    private Tile[] path;
    private int pathIndex;

    public Dwarf(int x, int y) {
        this.x = x;
        this.y = y;
        pathIndex = 0;
        path = null;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setPath(Tile[] path) {
        this.path = path;
        pathIndex = 0;
    }

    public void update() {
        if (path == null) return;
        if (path.length <= 1) {
            path = null;
            pathIndex = 0;
            return;
        }

        pathIndex++;
        x = path[pathIndex].getX();
        y = path[pathIndex].getY();

        if (x == path[path.length-1].getX() && y == path[path.length-1].getY()) {
            pathIndex = 0;
            path = null;
        }
    }
}
