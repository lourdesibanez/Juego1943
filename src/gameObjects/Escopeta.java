package gameObjects;

import math.Vector2D;
import states.Nivel1;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Escopeta extends MunicionP38{
    private long escopetaTime;
    private boolean escopetaOn = false; 

    public Escopeta(Vector2D position, Vector2D velocity, double maxVel, double angle, BufferedImage texture, Nivel1 gameState) {
		super(position, velocity, maxVel, angle, texture, gameState);
        escopetaTime = 0;
	}

    //hacemos una clase Laser para que cuando el jugador la utilice podamos identificarla para asignarle mas puntaje
    //cada vez que el jugador dispare con este laser le otorgamos 60 pts en objetografico

    public boolean isEscopetaOn() {
        return escopetaOn;
    }

    public void desactivarEscopeta() {
        if(escopetaOn){
            escopetaOn = false;
			escopetaTime = 0;
        }
    }

    public void activarEscopeta() {
        if(escopetaOn)
			escopetaTime = 0;
		escopetaOn = true;
    }

    @Override
	public void update(float dt) {
		super.update(dt);
        if(escopetaOn)
            escopetaTime += dt;

        if(escopetaTime > 12000)
			desactivarEscopeta();
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
