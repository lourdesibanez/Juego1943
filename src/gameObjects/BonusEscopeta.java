package gameObjects;

import java.awt.image.BufferedImage;

import math.Vector2D;
import states.Nivel1;
import ui.Action;

public class BonusEscopeta extends PowerUp{

    public BonusEscopeta(Vector2D position, BufferedImage texture, Action action, Nivel1 gameState) {
        super(position, texture, action, gameState);
    }

    @Override
    public void executeAction() {
        gameState.getPlayer().setEscopeta();
    }
    
}
