package jobsystem;

import main.Dwarf;
import main.Tile;
import main.World;

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

        int dx = dwarfX - targetX;
        int dy = dwarfY - targetY;

        if (Math.abs(dx) >= Math.abs(dy)) {
            return world.getTile(targetX + Integer.signum(dx), targetY);
        }
        else {
            return world.getTile(targetX, targetY + Integer.signum(dy));
        }
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
