package gameObjects;

import math.Vector2D;
import states.GameState;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Ametralladora extends MunicionP38{
    private long ametralladoraTime;
    private boolean ametralladoraOn = false; 

    public Ametralladora(Vector2D position, Vector2D velocity, double maxVel, double angle, BufferedImage texture, GameState gameState) {
		super(position, velocity, maxVel, angle, texture, gameState);
        ametralladoraTime = 0;
	}

    //hacemos una clase Laser para que cuando el jugador la utilice podamos identificarla para asignarle mas puntaje
    //cada vez que el jugador dispare con este laser le otorgamos 60 pts en objetografico

    public boolean isAmetralladoraOn() {
        return ametralladoraOn;
    }

    public void desactivarAmetralladora() {
        if(ametralladoraOn){
            ametralladoraOn = false;
			ametralladoraTime = 0;
        }
    }

    public void activarAmetralladora() {
        if(ametralladoraOn)
			ametralladoraTime = 0;
		ametralladoraOn = true;
    }

    @Override
	public void update(float dt) {
		super.update(dt);
        if(ametralladoraOn)
            ametralladoraTime += dt;

        if(ametralladoraTime > 15000)
			desactivarAmetralladora();
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
