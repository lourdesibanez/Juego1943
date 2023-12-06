package gameObjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.Propiedades;
import math.Vector2D;
import states.GameState;

public class AvionEnemigoNegro extends AvionEnemigo{

    public AvionEnemigoNegro(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState){
        super(position, velocity, maxVel, Propiedades.enemigo_negro, gameState);
    }

    @Override
	public void update(float dt) {
        super.update(dt);
        if(position.getX() > WIDTH - 50)
			position.setX(WIDTH-50);
		if(position.getY() > HEIGHT)
			position.setY(-height);
		if(position.getX() < -width)
			position.setX(WIDTH);
		if(position.getY() < -height)
			position.setY(HEIGHT);
    }
    @Override
	public void Destroy(){
        super.Destroy();
    }
    @Override
	public void draw(Graphics g) {
        super.draw(g);
    }
    @Override
	public void silenciar_sonido() {
        super.silenciar_sonido();
    }
}

