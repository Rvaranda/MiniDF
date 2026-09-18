package jobsystem;

import items.Item;
import main.Dwarf;
import main.Tile;
import main.World;
import pathfinding.Pathfinder;

public class HaulJob extends Job {
    private int destinationX, destinationY;
    private Item item;

    private Tile[] pathFromItemToDest = null;

    // TODO: GAMBIARRA - resolver quando possível
    private Tile[] bestPathToThisJob = null;

    public HaulJob(Tile target, Item item) {
        super(target);
        destinationX = 0;
        destinationY = 0;
        this.item = item;
    }

    public HaulJob(Tile target, Tile destination, Item item) {
        super(target);
        destinationX = destination.getX();
        destinationY = destination.getY();
        this.item = item;
    }

    @Override
    public void assignDwarf(Dwarf dwarf) {
        super.assignDwarf(dwarf);
        if (dwarf != null) dwarf.setPath(bestPathToThisJob);
    }

    @Override
    public boolean canDwarfExecute(Dwarf dwarf) {
        World world = dwarf.getWorld();
        Tile origin = world.getTile(dwarf.getX(), dwarf.getY());
        bestPathToThisJob = Pathfinder.findPath(world, origin, getTarget()).toArray(Tile[]::new);
        return bestPathToThisJob.length > 0;
    }

    @Override
    public void onComplete(World world) {
        //getAssignedDwarf().removeItem();
        item.setX(destinationX);
        item.setY(destinationY);
        world.spawnItem(item);
    }

    @Override
    public void execute(World world) {
        // Indo até o item
        Dwarf dwarf = getAssignedDwarf();
        if (dwarf.isMoving()) return;

        // Chegou no item
        if (!dwarf.hasItem()) {
            dwarf.takeItem(item);
            return;
        }

        // Com o item na mao, calcula rota até o destino
        if (pathFromItemToDest == null) {
            pathFromItemToDest = Pathfinder.findPath(
                    world,
                    world.getTile(item.getX(), item.getY()),
                    world.getTile(destinationX, destinationY)
            ).toArray(Tile[]::new);
            return;
        }

        // Não há caminho do item até o destino, cancela job
        if (pathFromItemToDest.length == 0) {
            world.spawnItem(item);
            dwarf.removeItem();
            dwarf.clearJob();
            assignDwarf(null);
            return;
        }

        // Anao comeca a levar o item até o destino
        if (!dwarf.isMoving() && (
                dwarf.getX() != destinationX || dwarf.getY() != destinationY
                )) {
            dwarf.setPath(pathFromItemToDest);
            return;
        }

        // Anao chegou ao destino com o item
        dwarf.removeItem();
        complete(world);
    }
}
