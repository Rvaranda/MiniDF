package main;

import jobsystem.Job;
import jobsystem.JobManager;
import pathfinding.Pathfinder;

import java.util.List;

public class Dwarf {
    private int x, y;
    private World world;
    private Tile[] path;
    private Job assignedJob;
    private int pathIndex;
    private boolean moving;

    public Dwarf(World world, int x, int y) {
        this.x = x;
        this.y = y;
        pathIndex = 0;
        path = null;
        assignedJob = null;
        this.world = world;
    }

    private void updatePath() {
        if (path == null || !moving) return;
        if (path.length <= 1) {
            path = null;
            pathIndex = 0;
            moving = false;
            return;
        }

        pathIndex++;
        x = path[pathIndex].getX();
        y = path[pathIndex].getY();

        if (x == path[path.length-1].getX() && y == path[path.length-1].getY()) {
            pathIndex = 0;
            path = null;
            moving = false;
        }
    }

    private void updateJob() {
        if (assignedJob != null) {
            assignedJob.execute(world);
        }
        else {
            List<Job> jobs = JobManager.getAvailableJobs();
            if (!jobs.isEmpty()) assignJob(jobs.getFirst());
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Tile[] getPath() { return path; }
    public boolean isMoving() { return moving; }

    public void setPath(Tile[] path) {
        this.path = path;
        pathIndex = 0;
    }

    public void clearJob() { assignedJob = null; }
    public void assignJob(Job job) {
        assignedJob = job;
        job.assignDwarf(this);
    }

    public void moveTo(Tile target) {
        List<Tile> path = Pathfinder.findPath(world, world.getTile(x, y), target);
        if (path.isEmpty()) return;

        moving = true;
        setPath(path.toArray(Tile[]::new));
    }

    public void update() {
        updatePath();
        updateJob();
    }
}
