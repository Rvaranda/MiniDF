package jobsystem;

import items.ItemType;
import main.Dwarf;
import main.Tile;
import main.TileType;
import main.World;
import pathfinding.Pathfinder;

public class MineJob extends Job {
    private int progress;

    // TODO: GAMBIARRA - resolver quando possível
    private Tile[] bestPathToThisJob = null;

    public MineJob(Tile target) {
        super(target);
        progress = 60;
    }

    private Tile[] findPathNextToWall(Dwarf dwarf) {
        int dwarfX = dwarf.getX();
        int dwarfY = dwarf.getY();
        int targetX = getTarget().getX();
        int targetY = getTarget().getY();
        Tile[] neighbors = dwarf.getWorld().getNeighbors(targetX, targetY);
        Tile[] shortestPath = null;
        for (Tile neighbor : neighbors) {
            Tile[] path = Pathfinder.findPath(
                    dwarf.getWorld(),
                    dwarf.getWorld().getTile(dwarfX, dwarfY),
                    neighbor
            ).toArray(Tile[]::new);

            if (path.length == 0) continue;

            if (shortestPath == null || path.length < shortestPath.length) {
                shortestPath = path;
            }
        }

        return shortestPath;
    }

    @Override
    public void assignDwarf(Dwarf dwarf) {
        super.assignDwarf(dwarf);
        if (bestPathToThisJob != null) {
            dwarf.setPath(bestPathToThisJob);
        }
        else {
            dwarf.setPath(findPathNextToWall(dwarf));
        }
    }

    @Override
    public boolean canDwarfExecute(Dwarf dwarf) {
        bestPathToThisJob = findPathNextToWall(dwarf);
        return bestPathToThisJob != null;
    }

    @Override
    public void onComplete(World world) {
        getTarget().mine();
        world.spawnItem(ItemType.STONE, getTarget().getX(), getTarget().getY());
    }

    @Override
    public void execute(World world) {
        if (getAssignedDwarf().isMoving()) return;

        if (getTarget().getType() != TileType.WALL) {
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
