package ir.pint.soltoon.services.scheduler;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.AssertTrue;
import java.time.Instant;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShortTimeSchedulerTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private ShortTimeScheduler sts;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addJobTest() throws Exception {
        Semaphore lock = new Semaphore(-2);
        SimpleScheduledJob s1 = new SimpleScheduledJob(lock, "S1", 1000, TimeUnit.MILLISECONDS);
        SimpleScheduledJob s2 = new SimpleScheduledJob(lock, "S2", 2500, TimeUnit.MILLISECONDS);
        SimpleScheduledJob s3 = new SimpleScheduledJob(lock, "S3", 3500, TimeUnit.MILLISECONDS);

        sts.addJob(s1);
        sts.addJob(s2);
        sts.addJob(s3);

        boolean b = lock.tryAcquire(10, TimeUnit.SECONDS);
        Assert.assertTrue(b);
    }

    public static class SimpleScheduledJob implements ScheduledJob {
        private Instant startTime;
        private Semaphore lock;
        private String name;

        public SimpleScheduledJob(Semaphore lock, String name, long offset, TimeUnit timeUnit) {
            this.startTime = Instant.now().plus(offset, timeUnit.toChronoUnit());
            this.name = name;
            this.lock = lock;
        }

        @Override
        public boolean isReady() {
            return Instant.now().isAfter(startTime);
        }

        @Override
        public boolean runJob() {
            System.out.println(String.format("Object %s killed %d.", name, Instant.now().toEpochMilli() - startTime.toEpochMilli()));

            lock.release();
            return true;
        }
    }

}