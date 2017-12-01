package ir.pint.soltoon.services.scheduler;

import java.util.concurrent.ConcurrentLinkedDeque;

public interface TimeManagedObject extends SortableScheduledJob {

    void watch();

    boolean isDead();

    @Override
    default boolean isReady() {
        return isDead();
    }

    default boolean remove() {
        return true;
    }

    @Override
    default boolean runJob() {
        return remove();
    }
}
