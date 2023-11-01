package gameObjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import math.Vector2D;
import states.GameState;

public class SuperShell extends MunicionP38{

    private long superShellTime;
    private boolean superShellOn = false; 

    public SuperShell(Vector2D position, Vector2D velocity, double maxVel, double angle, BufferedImage texture, GameState gameState) {
		super(position, velocity, maxVel, angle, texture, gameState);
        superShellTime = 0;
	}

    //hacemos una clase Laser para que cuando el jugador la utilice podamos identificarla para asignarle mas puntaje
    //cada vez que el jugador dispare con este laser le otorgamos 60 pts en objetografico

    public boolean isSuperShellOn() {
        return superShellOn;
    }

    public void desactivarSuperShell() {
        if(superShellOn){
            superShellOn = false;
		    superShellTime = 0;
        }
    }

    public void activarSuperShell() {
        if(superShellOn)
            superShellTime = 0;
        superShellOn = true;
    }

    @Override
	public void update(float dt) {
		super.update(dt);
        if(!superShellOn)
            superShellTime += dt;

            if(superShellTime > 12000)
			    desactivarSuperShell();
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

