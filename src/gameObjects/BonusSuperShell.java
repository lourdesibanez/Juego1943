package gameObjects;

import java.awt.image.BufferedImage;

import math.Vector2D;
import states.GameState;
import ui.Action;

public class BonusSuperShell extends PowerUp{

    public BonusSuperShell(Vector2D position, BufferedImage texture, Action action, GameState gameState) {
        super(position, texture, action, gameState);
    }
    
    /* Esto asegura que el power-up Super Shell actúe sobre el jugador actual y no cree una nueva instancia de AvionP38 */
    @Override
    public void executeAction() {
        gameState.getPlayer().setSuperShell();
        //superShell.activarSuperShell();
    }
	

}
