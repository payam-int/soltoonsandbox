package ir.pint.soltoon.container.ai;

import ir.pint.soltoon.container.GameInterface;

public interface SlaveGameInterface extends GameInterface {
    long getRemainingTime();
    boolean isExtraTime();
}
