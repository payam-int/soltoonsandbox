package ir.pint.soltoon.services.scheduler;

import ir.pint.soltoon.services.logger.ExternalExceptionLogger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public abstract class Scheduler extends Thread {
    protected long sleepTime;

    @Autowired
    protected ExternalExceptionLogger externalExceptionLogger;

    protected Scheduler() {
    }

    public Scheduler(String name, long sleepTime) {
        super(name);
        this.sleepTime = sleepTime;
    }

    protected Scheduler(String name) {
        super(name);
    }


    protected abstract void runStep();


    @PostConstruct
    void init() {
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                runStep();
                sleep(sleepTime);
            } catch (Exception e) {
                externalExceptionLogger.log(e);
            }
        }
    }
}
