package ir.pint.soltoon.services.game;

public interface Game {
    String getId();
    GameState getGameState();
    GameResult getGameResult();
}
