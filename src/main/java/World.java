public class World {
    public static final int TILE_SIZE = 16;
    public static final int WORLD_WIDTH = 128;
    public static final int WORLD_HEIGHT = 128;

    private Tile[] tiles = new Tile[WORLD_WIDTH * WORLD_HEIGHT];

    public World() {
        for (int i = 0; i < tiles.length; i++) {
            int x = i % WORLD_WIDTH;
            int y = i / WORLD_WIDTH;
            tiles[i] = new Tile(x, y, TileType.GRASS, 3);
        }
    }

    public Tile[] getTiles() {
        return tiles;
    }

    public Tile getTile(int x, int y) {
        return tiles[y * WORLD_WIDTH + x];
    }
}
