package gameObjects;

import java.awt.image.BufferedImage;
import math.Vector2D;
import states.GameState;
import ui.Action;

public class BonusLaser extends PowerUp{

    public BonusLaser(Vector2D position, BufferedImage texture, Action action, GameState gameState) {
        super(position, texture, action, gameState);
    }

    @Override
    public void executeAction() {
        gameState.getPlayer().setLaser();
    }
}