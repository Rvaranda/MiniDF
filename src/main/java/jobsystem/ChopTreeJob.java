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

    private Tile findTileNextToTree(World world) {
        int dwarfX = getAssignedDwarf().getX();
        int dwarfY = getAssignedDwarf().getY();
        int targetX = getTarget().getX();
        int targetY = getTarget().getY();
        Tile[] neighbors = world.getNeighbors(targetX, targetY);
        Tile[][] possiblePaths = new Tile[neighbors.length][];
        for (int i = 0; i < neighbors.length; i++) {
            possiblePaths[i] =
                    Pathfinder.findPath(world, world.getTile(dwarfX, dwarfY), neighbors[i]).toArray(Tile[]::new);
        }

        Tile[] shortestPath = possiblePaths[0];
        for (Tile[] path : possiblePaths) {
            if (shortestPath.length == 0)
                shortestPath = path;
            else if (path.length < shortestPath.length)
                shortestPath = path;
        }

        return shortestPath[shortestPath.length - 1];
    }

    @Override
    public void onComplete() {
        getTarget().chopTree();
    }

    @Override
    public void execute(World world) {
        Dwarf dwarf = getAssignedDwarf();
        if (moveTarget == null) {
            moveTarget = findTileNextToTree(world);
            dwarf.moveTo(moveTarget);
            return;
        }

        if (dwarf.isMoving()) return;

        if (progress > 0) {
            progress--;
            return;
        }

        complete();
    }
}
