package jobsystem;

import main.Dwarf;
import main.Tile;
import main.World;

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
        if (dwarf != null)
            state = JobState.ASSIGNED;
        else
            state = JobState.CANCELED;
    }

    // TODO: PROVISORIO - provalmente será removido futuramente
    protected void changeState(JobState newState) {
        state = newState;
    }

    public void complete(World world) {
        state = JobState.DONE;
        assignedDwarf.clearJob();
        assignedDwarf = null;
        onComplete(world);
    }

    public abstract boolean canDwarfExecute(Dwarf dwarf);
    public abstract void onComplete(World world);
    public abstract void execute(World world);
}
