package gameObjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import math.Vector2D;
import states.Nivel1;

public class MunicionEnemigos extends Municion{

	public static final int WIDTH = 600;//Ancho de la ventana
	public static final int HEIGHT = 700;//largo de la ventana
//velocidad es igual al heading de player
	public MunicionEnemigos(Vector2D position, Vector2D velocity, double maxVel, double angle, BufferedImage texture, Nivel1 gameState) {
		super(position, velocity, maxVel, angle, texture, gameState);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
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


