package gameObjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.Sound;
import math.Vector2D;
import states.GameState;

public class AvionRefuerzo extends AvionP38{

    private long refuerzoTime;
    private boolean refuerzoOn;

    public AvionRefuerzo(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState){
        super(position, velocity, maxVel, texture, gameState);
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
}