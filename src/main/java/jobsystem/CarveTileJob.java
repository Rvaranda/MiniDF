package jobsystem;

import items.ItemType;
import main.Dwarf;
import main.Tile;
import main.TileType;
import main.World;
import pathfinding.Pathfinder;

public class CarveTileJob extends Job {
    private boolean mustSpawnItem = false;
    private int progress;

    private ItemType itemTypeToSpawn = null;

    // TODO: GAMBIARRA - resolver quando possível
    private Tile[] bestPathToThisJob = null;

    public CarveTileJob(Tile target) {
        super(target);

        if (target.getType() == TileType.WALL) {
            mustSpawnItem = true;
            itemTypeToSpawn = ItemType.STONE;
            progress = 60;
        }
        else if (target.hasTree()) {
            mustSpawnItem = true;
            itemTypeToSpawn = ItemType.WOOD;
            progress = 40;
        }
    }

    @Override
    public void assignDwarf(Dwarf dwarf) {
        super.assignDwarf(dwarf);
        if (bestPathToThisJob != null) {
            dwarf.setPath(bestPathToThisJob);
        }
        else {
            dwarf.setPath(
                    Pathfinder.findPathNextTo(
                            dwarf.getWorld(),
                            dwarf.getWorld().getTile(dwarf.getX(), dwarf.getY()),
                            getTarget()
                    ).toArray(Tile[]::new)
            );
        }
    }

    @Override
    public boolean canDwarfExecute(Dwarf dwarf) {
        bestPathToThisJob = Pathfinder.findPathNextTo(
                dwarf.getWorld(),
                dwarf.getWorld().getTile(dwarf.getX(), dwarf.getY()),
                getTarget()
        ).toArray(Tile[]::new);
        return bestPathToThisJob != null;
    }

    @Override
    public void onComplete(World world) {
        if (getTarget().getType() == TileType.WALL)
            getTarget().mine();
        else if (getTarget().hasTree())
            getTarget().chopTree();

        if (mustSpawnItem) {
            Tile tile = null;
            switch (itemTypeToSpawn) {
                case WOOD -> tile = world.getRandomNeighbor(getTarget().getX(), getTarget().getY());
                case STONE -> tile = getTarget();
            }
            world.spawnItem(itemTypeToSpawn, tile.getX(), tile.getY());
        }
    }

    @Override
    public void execute(World world) {
        if (getAssignedDwarf().isMoving()) return;

        boolean shouldCancelJob = getTarget().getType() != TileType.WALL && !getTarget().hasTree();

        if (shouldCancelJob) {
            getAssignedDwarf().clearJob();
            assignDwarf(null);
            return;
        }

        if (progress > 0) {
            progress--;
            return;
        }

        complete(world);
    }
}
