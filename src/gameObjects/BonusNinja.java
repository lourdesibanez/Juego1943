package gameObjects;

import java.awt.image.BufferedImage;
import math.Vector2D;
import states.Nivel1;
import ui.Action;

public class BonusNinja extends PowerUp{

    public BonusNinja(Vector2D position, BufferedImage texture, Action action, Nivel1 gameState) {
        super(position, texture, action, gameState);
    }

    //sobreescribo el metodo abstracto de PowerUp para que implemente solo la funcionalidad de ninja
    //y lo busco desde el gamestate, a una instancia del avionp38 que es
    //quien tiene el metodo de setNinja()
    
    /* Esto asegura que el power-up Ninja actúe sobre el jugador actual y no cree una nueva instancia de AvionP38 */
    @Override
    public void executeAction() {
        gameState.getPlayer().setNinja();
        //ninja.activarNinja();
    }
    
    public BufferedImage getTexture() {
		return texture;
	}

}