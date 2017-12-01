package ir.pint.soltoon.services.scheduler;

public interface ScheduledJob {
    /**
     * @return Returns true if object is ready to run job.
     */
    boolean isReady();

    /**
     * @return Returns true if object job finished.
     */
    boolean runJob();
}
