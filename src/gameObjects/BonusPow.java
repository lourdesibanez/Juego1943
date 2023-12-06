package gameObjects;

import java.awt.image.BufferedImage;

import math.Vector2D;
import states.Nivel1;
import ui.Action;

public class BonusPow extends PowerUp{

    public BonusPow(Vector2D position, BufferedImage texture, Action action, Nivel1 gameState) {
        super(position, texture, action, gameState);
    }

    //aumenta el puntaje del jugador
    @Override
    public void executeAction() {
        if(gameState.getLives() <= 6){
            gameState.addLife(4);
        }
        if(gameState.getLives() == 7){
            gameState.addLife(3);
        }
        if(gameState.getLives() == 8){
            gameState.addLife(2);
        }
        if(gameState.getLives() == 9){
            gameState.addLife(1);
        }
    }
}
