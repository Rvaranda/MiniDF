package main;

import jobsystem.Job;
import jobsystem.JobManager;

import java.util.List;

public class Dwarf {
    private int x, y;
    private Tile[] path;
    private Job assignedJob;
    private int pathIndex;

    public Dwarf(int x, int y) {
        this.x = x;
        this.y = y;
        pathIndex = 0;
        path = null;
        assignedJob = null;
    }

    private void updatePath() {
        if (path == null) return;
        if (path.length <= 1) {
            path = null;
            pathIndex = 0;
            return;
        }

        pathIndex++;
        x = path[pathIndex].getX();
        y = path[pathIndex].getY();

        if (x == path[path.length-1].getX() && y == path[path.length-1].getY()) {
            pathIndex = 0;
            path = null;
        }
    }

    private void updateJob() {
        if (assignedJob != null) return;

        List<Job> jobs = JobManager.getAvailableJobs();
        if (!jobs.isEmpty()) assignJob(jobs.getFirst());
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setPath(Tile[] path) {
        this.path = path;
        pathIndex = 0;
    }

    public void clearJob() { assignedJob = null; }
    public void assignJob(Job job) {
        assignedJob = job;
        job.assignDwarf(this);
    }

    public void update() {
        updatePath();
        updateJob();
    }
}
