package main;

import buildings.Building;
import buildings.BuildingType;

public class Tile {
    private final int x;
    private final int y;
    private TileType type;
    private Building building;

    private boolean hasTree = false;

    public Tile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.building = null;
    }

    public void spawnTree() {
        if (type != TileType.WALL)
            hasTree = true;
    }

    public void chopTree() {
        hasTree = false;
    }

    public void mine() {
        if (type == TileType.WALL)
            type = TileType.GRASS;
    }

    public boolean hasTree() {
        return hasTree;
    }

    public void createStockpile() {
        type = TileType.STOCKPILE;
    }

    public void placeWall() {
        type = TileType.WALL;
        hasTree = false;
    }

    public TileType getType() {
        return type;
    }
    public void setType(TileType type) { this.type = type; }

    public Building getBuilding() { return building; }
    public void setBuilding(Building building) {
        if (isTraversable()) this.building = building;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public boolean isTraversable() {
        return !hasTree
                && type != TileType.WALL
                && (building == null || building.isTraversable());
    }

    public boolean canPlanThrough() {
        if (hasTree || type == TileType.WALL)
            return false;

        if (building == null)
            return true;

        return building.isTraversable()
                || building.getType() == BuildingType.DOOR;
    }

    public boolean isCarveable() {
        return hasTree || type == TileType.WALL;
    }
}
