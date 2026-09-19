package jobsystem;

import items.ItemType;
import main.Dwarf;
import main.Tile;
import main.World;
import pathfinding.Pathfinder;

public class ChopTreeJob extends Job {
    private Tile moveTarget;
    private int progress;

    // TODO: GAMBIARRA - resolver quando possível
    private Tile[] bestPathToThisJob = null;

    public ChopTreeJob(Tile target) {
        super(target);
        progress = 40;
    }

    private Tile[] findPathNextToTree(Dwarf dwarf) {
        int dwarfX = dwarf.getX();
        int dwarfY = dwarf.getY();
        int targetX = getTarget().getX();
        int targetY = getTarget().getY();
        Tile[] neighbors = dwarf.getWorld().getNeighbors(targetX, targetY);
        Tile[] shortestPath = null;
        for (Tile neighbor : neighbors) {
            //if (!neighbor.isTraversable()) continue;
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

        if (shortestPath == null) return null;

        return shortestPath;
    }

    @Override
    public void assignDwarf(Dwarf dwarf) {
        super.assignDwarf(dwarf);
        if (bestPathToThisJob != null) {
            dwarf.setPath(bestPathToThisJob);
        }
        else {
            dwarf.setPath(findPathNextToTree(dwarf));
        }
    }

    @Override
    public boolean canDwarfExecute(Dwarf dwarf) {
        bestPathToThisJob = findPathNextToTree(dwarf);
        return bestPathToThisJob != null;
    }

    @Override
    public void onComplete(World world) {
        getTarget().chopTree();
        Tile tile = world.getRandomNeighbor(getTarget().getX(), getTarget().getY());
        world.spawnItem(ItemType.WOOD, tile.getX(), tile.getY());
    }

    @Override
    public void execute(World world) {
        if (getAssignedDwarf().isMoving()) return;

        if (!getTarget().hasTree()) {
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
