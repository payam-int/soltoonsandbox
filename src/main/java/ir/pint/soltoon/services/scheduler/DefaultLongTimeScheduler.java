package ir.pint.soltoon.services.scheduler;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

@Service
public class DefaultLongTimeScheduler extends Scheduler implements LongTimeScheduler {
    public static TemporalAmount defaultLifetime = Duration.ofDays(1);
    private SortedSet<SortableScheduledJob> sortedScheduledJobs = new ConcurrentSkipListSet<>(new Comparator<SortableScheduledJob>() {
        @Override
        public int compare(SortableScheduledJob o1, SortableScheduledJob o2) {
            return (int) (o1.getScheduledTime() - o2.getScheduledTime());
        }
    });

    public DefaultLongTimeScheduler() {
        super("LongTimeScheduler", TimeUnit.HOURS.toMillis(1));
    }

    public DefaultLongTimeScheduler(String name, long sleepTime, TimeUnit timeUnit) {
        super(name);
        this.sleepTime = timeUnit.toMillis(sleepTime);
    }

    @Override
    public void addJob(SortableScheduledJob scheduledJob) {
        this.sortedScheduledJobs.add(scheduledJob);
    }

    @Override
    protected void runStep() {
        LinkedList<SortableScheduledJob> updatedJobs = new LinkedList<>();


        Iterator<SortableScheduledJob> iterator = sortedScheduledJobs.iterator();
        while (iterator.hasNext()) {
            SortableScheduledJob j = iterator.next();
            if (j.isReady()) {
                boolean remove = j.runJob();
                iterator.remove();
                if (!remove) {
                    updatedJobs.add(j);
                }
            } else {
                break;
            }
        }

        sortedScheduledJobs.addAll(updatedJobs);
    }
}