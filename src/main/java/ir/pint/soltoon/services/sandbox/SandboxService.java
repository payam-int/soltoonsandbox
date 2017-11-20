package ir.pint.soltoon.services.sandbox;

import ir.pint.soltoon.services.game.Game;

/**
 * Depends handle docker service
 *
 * High level api to handle running storageentities handle docker
 */
public interface SandboxService {
    GameInfo startGame(Game game);

}
