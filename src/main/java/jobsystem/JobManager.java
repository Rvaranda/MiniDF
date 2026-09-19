package jobsystem;

import items.Item;
import main.Tile;

import java.util.ArrayList;
import java.util.List;

public class JobManager {
    private static final List<Job> jobs = new ArrayList<>();

    public static void addJob(Job job) {
        jobs.add(job);
    }

    public static List<Job> getAvailableJobs() {
        return jobs.stream().filter(Job::isAvailable).toList();
    }

    public static Job getFirstAvailableJob() {
        List<Job> availableJobs = getAvailableJobs();
        if (availableJobs.isEmpty()) return null;

        Job job = availableJobs.getFirst();
        return job;
    }

    public static boolean isPositionAssignedToHaul(int x, int y) {
        return jobs.stream()
                .filter(j -> j instanceof HaulJob)
                .anyMatch(j -> ((HaulJob) j).getDestinationX() == x && ((HaulJob) j).getDestinationY() == y);
    }

    public static boolean hasJobFor(Tile tile) {
        return jobs.stream()
                .filter(j -> j.isAvailable() || j.isAssigned())
                .anyMatch(j -> j.getTarget() == tile);
    }

    public static boolean hasHaulJobFor(Item item) {
        return jobs.stream()
                .filter(j -> j.isAvailable() || j.isAssigned())
                .filter(j -> j instanceof HaulJob)
                .anyMatch(j -> ((HaulJob) j).getItem() == item);
    }
}
