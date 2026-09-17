package jobsystem;

import main.Dwarf;
import main.Tile;
import main.World;
import pathfinding.Pathfinder;

public class ChopTreeJob extends Job {
    private Tile moveTarget;
    private int progress;

    public ChopTreeJob(Tile target) {
        super(target);
        progress = 40;
    }

    private Tile[] findPathNextToTree(World world) {
        int dwarfX = getAssignedDwarf().getX();
        int dwarfY = getAssignedDwarf().getY();
        int targetX = getTarget().getX();
        int targetY = getTarget().getY();
        Tile[] neighbors = world.getNeighbors(targetX, targetY);
        Tile[] shortestPath = null;
        for (Tile neighbor : neighbors) {
            if (!neighbor.isTraversable()) continue;
            Tile[] path = Pathfinder.findPath(
                    world,
                    world.getTile(dwarfX, dwarfY),
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
        dwarf.setPath(findPathNextToTree(dwarf.getWorld()));
    }

    @Override
    public void onComplete() {
        getTarget().chopTree();
    }

    @Override
    public void execute(World world) {
        if (getAssignedDwarf().isMoving()) return;

        if (progress > 0) {
            progress--;
            return;
        }

        complete();
    }
}
