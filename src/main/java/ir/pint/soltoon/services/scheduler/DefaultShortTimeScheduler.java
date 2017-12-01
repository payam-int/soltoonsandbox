package ir.pint.soltoon.services.scheduler;

import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

@Service
public class DefaultShortTimeScheduler extends Scheduler implements ShortTimeScheduler {
    private ConcurrentLinkedDeque<ScheduledJob> scheduledJobs = new ConcurrentLinkedDeque<>();

    public DefaultShortTimeScheduler() {
        super("ShortTimeScheduler", TimeUnit.SECONDS.toMillis(1));
    }

    public DefaultShortTimeScheduler(String name, long sleepTime) {
        super(name, sleepTime);
    }

    public DefaultShortTimeScheduler(String name) {
        super(name, TimeUnit.SECONDS.toMillis(1));
    }

    @Override
    public void addJob(ScheduledJob job) {
        scheduledJobs.add(job);
    }

    @Override
    protected void runStep() {
        Iterator<ScheduledJob> iterator = scheduledJobs.iterator();

        while (iterator.hasNext()) {
            ScheduledJob j = iterator.next();
            if (j.isReady()) {
                boolean r = j.runJob();
                if (r)
                    iterator.remove();
            }
        }
    }
}
