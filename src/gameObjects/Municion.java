package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import math.Vector2D;
import states.GameState;

public abstract class Municion extends ObjetoGrafico{

	public static final int WIDTH = 600;//Ancho de la ventana
	public static final int HEIGHT = 700;//largo de la ventana
	//velocidad es igual al heading de player
	public Municion(Vector2D position, Vector2D velocity, double maxVel, double angle, BufferedImage texture, GameState gameState) {
		super(position, velocity, maxVel, texture, gameState);
		this.angle = angle;
		this.velocity = velocity.scale(maxVel);
	}

	@Override
	public void update(float dt) {
		 //la velocidad es constante
		position = position.add(velocity);
		if(position.getX() < 0 || position.getX() > WIDTH ||// Eliminar los laser cuando se pasa del limite de la ventana
				position.getY() < 0 || position.getY() > HEIGHT){//quiero eliminar este objeto, osea el laser cuando sale
			Destroy();
		}
		collidesWith();
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		//posicion inicial de la municion sea en frente de la nave
		at = AffineTransform.getTranslateInstance(position.getX() - width/2, position.getY());//Para centrarce con el avion
		at.rotate(angle, width/2, 0);
		g2d.drawImage(texture, at, null);
	}
	
	@Override
	public Vector2D getCenter(){
		return new Vector2D(position.getX() + width/2, position.getY() + width/2);
	}
	
}
