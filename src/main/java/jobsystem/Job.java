package jobsystem;

import main.Dwarf;
import main.Tile;

public abstract class Job {
    private final Tile target;
    private Dwarf assignedDwarf;
    private JobState state;

    public Job(Tile target) {
        this.target = target;
        state = JobState.AVAILABLE;
    }

    public Tile getTarget() { return target; }
    public Dwarf getAssignedDwarf() { return assignedDwarf; }
    public boolean isAvailable() { return state == JobState.AVAILABLE; }

    public void assignDwarf(Dwarf dwarf) {
        this.assignedDwarf = dwarf;
        state = JobState.ASSIGNED;
    }

    public void complete() {
        state = JobState.DONE;
        assignedDwarf.clearJob();
        assignedDwarf = null;
        onComplete();
    }

    public abstract void onComplete();
    public abstract void execute();
}
