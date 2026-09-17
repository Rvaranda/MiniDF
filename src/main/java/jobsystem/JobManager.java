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
}
