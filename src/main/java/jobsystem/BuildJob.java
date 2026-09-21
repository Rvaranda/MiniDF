package jobsystem;

import buildings.Building;
import buildings.BuildingRecipe;
import buildings.BuildingType;
import items.Item;
import main.Dwarf;
import main.Tile;
import main.TileType;
import main.World;
import pathfinding.Pathfinder;

public class BuildJob extends Job implements JobObserver {
    private int work;

    private BuildingRecipe recipe;
    private BuildingType buildingType;
    private Item item = null;
    private HaulJob haulJob = null;

    // TODO: GAMBIARRA - resolver quando possível
    private Tile[] bestPathToThisJob = null;

    public BuildJob(Tile target, World world, BuildingType buildingType, BuildingRecipe recipe) {
        super(target);
        work = recipe.work();
        this.recipe = recipe;
        this.buildingType = buildingType;
        changeState(JobState.WAITING);
        searchResources(world);
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

    public void searchResources(World world) {
        if (item != null) return;
        Item[] itemsInStockpile = world.getAvailableItems().stream()
                .filter(i -> {
                    Tile tile = world.getTile(i.getX(), i.getY());
                    return i.getType() == recipe.itemType()
                            && tile.getType() == TileType.STOCKPILE;
                }).toArray(Item[]::new);
        // TODO: se nao tiver pedra disponivel, fazer o job procurar periodicamente
        if (itemsInStockpile.length > 0) {
            item = itemsInStockpile[0];
            world.reserveItem(item);
            haulJob = new HaulJob(
                    world.getTile(item.getX(), item.getY()),
                    getTarget(),
                    item
            );
            haulJob.addJobObserver(this);
            world.getJobManager().addJob(haulJob);
        }
    }

    public Item getItem() {
        return item;
    }

    @Override
    public void assignDwarf(Dwarf dwarf) {
        super.assignDwarf(dwarf);
        if (bestPathToThisJob != null) {
            dwarf.setPath(bestPathToThisJob);
        }
        else {
            dwarf.setPath(findPathNextToTarget(dwarf));
        }
    }

    @Override
    public boolean canDwarfExecute(Dwarf dwarf) {
        bestPathToThisJob = findPathNextToTarget(dwarf);
        return bestPathToThisJob != null;
    }

    @Override
    public void onComplete(World world) {
        getTarget().setBuilding(new Building(buildingType));

        world.removeItem(item);
        if (getTarget().getType() == TileType.STOCKPILE) {
            getTarget().setType(TileType.GRASS);
        }
    }

    @Override
    public void execute(World world) {
        if (getAssignedDwarf().isMoving()) return;

        if (work > 0) {
            work--;
            return;
        }

        complete(world);
    }

    @Override
    public void onJobComplete() {
        if (isWaiting())
            changeState(JobState.AVAILABLE);
    }
}
