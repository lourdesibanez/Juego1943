package gameObjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import math.Vector2D;
import states.Nivel1;

public class SuperShell extends MunicionP38{

    private long superShellTime;
    private boolean superShellOn = false; 

    public SuperShell(Vector2D position, Vector2D velocity, double maxVel, double angle, BufferedImage texture, Nivel1 gameState) {
		super(position, velocity, maxVel, angle, texture, gameState);
        superShellTime = 0;
	}

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
        if(superShellOn)
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

