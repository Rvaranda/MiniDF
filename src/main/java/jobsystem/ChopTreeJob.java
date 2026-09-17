package jobsystem;

import main.Dwarf;
import main.Tile;

public class ChopTreeJob extends Job {
    private Tile moveTarget;

    public ChopTreeJob(Tile target) {
        super(target);
    }

    @Override
    public void onComplete() {
        getTarget().chopTree();
    }

    @Override
    public void execute() {
        Dwarf dwarf = getAssignedDwarf();
        if (moveTarget == null) {
            // TODO: achar o melhor tile mais proximo da arvore para o anao se mover
            // moveTarget = tile proximo da arvore
            dwarf.moveTo(moveTarget);
            return;
        }

        if (dwarf.isMoving()) return;

        complete();
    }
}
