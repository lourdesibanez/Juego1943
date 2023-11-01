package gameObjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import math.Vector2D;
import states.GameState;

public class Ninja extends MunicionP38{

    private long ninjaTime;
    private boolean ninjaOn = false; 

    public Ninja(Vector2D position, Vector2D velocity, double maxVel, double angle, BufferedImage texture, GameState gameState) {
		super(position, velocity, maxVel, angle, texture, gameState);
        ninjaTime = 0;
	}

    //hacemos una clase Laser para que cuando el jugador la utilice podamos identificarla para asignarle mas puntaje
    //cada vez que el jugador dispare con este laser le otorgamos 60 pts en objetografico

    public boolean isNinjaOn() {
        return ninjaOn;
    }

    public void desactivarNinja() {
        if(ninjaOn){
            ninjaOn = false;
		    ninjaTime = 0;
        }
    }

    public void activarNinja() {
        if(ninjaOn)
            ninjaTime = 0;
        ninjaOn = true;
    }

    @Override
	public void update(float dt) {
		super.update(dt);
        if(ninjaOn)
            ninjaTime += dt;

        if(ninjaTime > 12000)
			desactivarNinja();
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
	}
	
	@Override
	public Vector2D getCenter(){
		return new Vector2D(position.getX() + width/2, position.getY() + width/2);
	}
}
