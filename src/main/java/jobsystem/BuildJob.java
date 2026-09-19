package jobsystem;

import items.Item;
import items.ItemType;
import main.Dwarf;
import main.Tile;
import main.TileType;
import main.World;
import pathfinding.Pathfinder;

public class BuildJob extends Job implements JobObserver {
    private int progress = 80;

    private Item item;
    private HaulJob haulJob;

    // TODO: GAMBIARRA - resolver quando possível
    private Tile[] bestPathToThisJob = null;

    public BuildJob(Tile target, World world) {
        super(target);
        changeState(JobState.WAITING);
        Item[] stoneItemsInStockpile = world.getItems().stream()
                .filter(i -> {
                    Tile tile = world.getTile(i.getX(), i.getY());
                    return i.getType() == ItemType.STONE
                            && tile.getType() == TileType.STOCKPILE;
                }).toArray(Item[]::new);
        // TODO: se nao tiver pedra disponivel, fazer o job procurar periodicamente
        if (stoneItemsInStockpile.length > 0) {
            item = stoneItemsInStockpile[0];
            haulJob = new HaulJob(
                    world.getTile(item.getX(), item.getY()),
                    target,
                    item
            );
            haulJob.addJobObserver(this);
            JobManager.addJob(haulJob);
        }
    }

    private Tile[] findPathNextToTarget(Dwarf dwarf) {
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
    public boolean canDwarfExecute(Dwarf dwarf) {
        bestPathToThisJob = findPathNextToTarget(dwarf);
        return bestPathToThisJob != null;
    }

    @Override
    public void onComplete(World world) {
        getTarget().buildWall();
        world.removeItem(item);
    }

    @Override
    public void execute(World world) {
        if (getAssignedDwarf().isMoving()) return;

        if (progress > 0) {
            progress--;
            return;
        }

        complete(world);
    }

    @Override
    public void notifyObserver() {
        changeState(JobState.AVAILABLE);
    }
}
