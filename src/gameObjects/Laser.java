package gameObjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import math.Vector2D;
import states.GameState;

public class Laser extends MunicionP38{

    private long laserTime;
    private boolean laserOn = false; 

    public Laser(Vector2D position, Vector2D velocity, double maxVel, double angle, BufferedImage texture, GameState gameState) {
		super(position, velocity, maxVel, angle, texture, gameState);
        laserTime = 0;
	}

    //hacemos una clase Laser para que cuando el jugador la utilice podamos identificarla para asignarle mas puntaje
    //cada vez que el jugador dispare con este laser le otorgamos 60 pts en objetografico

    public boolean isLaserOn() {
        return laserOn;
    }

    public void desactivarLaser() {
        if(laserOn){
            laserOn = false;
		    laserTime = 0;
        }
    }

    public void activarLaser() {
        if(laserOn)
            laserTime = 0;
        laserOn = true;
    }

    @Override
	public void update(float dt) {
		super.update(dt);
        if(laserOn)
            laserTime += dt;
            if(laserTime > 12000)
			desactivarLaser();
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
