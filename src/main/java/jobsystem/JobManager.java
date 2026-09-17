package jobsystem;

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

    public static Job pollFirstAvailableJob() {
        List<Job> availableJobs = getAvailableJobs();
        if (availableJobs.isEmpty()) return null;

        Job job = availableJobs.getFirst();
        jobs.remove(job);
        return job;
    }
}
