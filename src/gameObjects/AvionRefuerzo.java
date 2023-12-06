package gameObjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import math.Vector2D;
import states.Nivel1;

public class AvionRefuerzo extends AvionP38{

    private long refuerzoTime;
    private boolean refuerzoOn;

    public AvionRefuerzo(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, Nivel1 gameState){
        super(position, velocity, maxVel, texture, gameState, gameState.getSonido());
        refuerzoTime = 0;
    }

    public boolean isRefuerzoOn() {
        return refuerzoOn;
    }

    public long getRefuerzoTime(){
        return refuerzoTime;
    }

    public void desactivarRefuerzo() {
        if(refuerzoOn){
            refuerzoOn = false;
		    refuerzoTime = 0;
        }
    }

    public void activarRefuerzo() {
        if(refuerzoOn)
            refuerzoTime = 0;
        refuerzoOn = true;
    }

    @Override
	public void update(float dt) {
        super.update(dt);
        if(refuerzoOn)
			refuerzoTime += dt;

        if(refuerzoTime > 12000) {
            desactivarRefuerzo();
        }
    }

    @Override
	public void Destroy() {
		spawning = true;
		gameState.playExplosion(position);
		spawnTime = 0;
		loose.play();
        resetValues();
		destruido = true;
    }

    @Override
	public void draw(Graphics g) {
        super.draw(g);
    }

    public int getWidth() {
        return texture.getWidth();
    }
    public int getHeight() {
        return texture.getHeight();
    }
    
    
}