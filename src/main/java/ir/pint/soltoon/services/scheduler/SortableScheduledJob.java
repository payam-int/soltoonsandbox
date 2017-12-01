package ir.pint.soltoon.services.scheduler;

public interface SortableScheduledJob extends ScheduledJob {
    default long getScheduledTime() {
        return 0;
    }

    default int compareTo(SortableScheduledJob sortableScheduledJob) {
        return (int) (getScheduledTime() - sortableScheduledJob.getScheduledTime());
    }
}
