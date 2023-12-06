package gameObjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import graphics.Propiedades;
import math.Vector2D;
import states.GameState;

public class AvionEnemigoVerde extends AvionEnemigo{

    public AvionEnemigoVerde(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState, boolean sonido_activado){
        super(position, velocity, maxVel, Propiedades.enemigo_v_chico, gameState, sonido_activado);
    }

    @Override
	public void update(float dt) {
        super.update(dt);
    }
    @Override
	public void Destroy(){
        super.Destroy();
    }
    @Override
	public void draw(Graphics g) {
        super.draw(g);
    }
    
}
