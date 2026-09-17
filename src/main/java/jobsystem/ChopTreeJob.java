package jobsystem;

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

    }
}
