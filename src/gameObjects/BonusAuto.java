package gameObjects;

import java.awt.image.BufferedImage;

import math.Vector2D;
import states.Nivel1;
import ui.Action;

public class BonusAuto extends PowerUp{

    private long autoTime;
    private boolean autoOn;

    public BonusAuto(Vector2D position, BufferedImage texture, Action action, Nivel1 gameState) {
        super(position, texture, action, gameState);
        autoTime = 0;
    }

    /* Esto asegura que el power-up Auto actúe sobre el jugador actual y no cree una nueva instancia de AvionP38 */
    @Override
    public void executeAction() {
        activarAuto();
    }

    public boolean isAutoOn() {
        return autoOn;
    }

    public void desactivarAuto() {
        if(autoOn){
            autoOn = false;
		    autoTime = 0;
        }
    }

    public void activarAuto() {
        if(autoOn)
			autoTime = 0;
		autoOn = true;
		gameState.addLife(10 - gameState.getLives());
    }


    @Override
	public void update(float dt) {
        if(autoOn) 
			autoTime += dt;

        if(autoTime > 12000) {
			desactivarAuto();
		}
    }

    public BufferedImage getTexture() {
		return texture;
	}
}
