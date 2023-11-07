package gameObjects;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

import math.Vector2D;
import states.GameState;

public class AvionEnemigoRojo extends AvionEnemigo {

    public AvionEnemigoRojo(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState) {
        super(position, velocity, maxVel, texture, gameState);   
    }
    //sobreescribo el metodo para que no dispare como los aviones enemigos
    @Override
    public void update(float dt) {
        if (velocity.getMagnitude() >= this.maxVel) {
            Vector2D reversedVelocity = new Vector2D(-velocity.getX(), -velocity.getY());
            velocity = velocity.add(reversedVelocity.normalize().scale(0.09f));
        }

        velocity = velocity.limit(ENEMIGO_MAX_VEL);
        position = position.add(velocity);

        if (position.getX() > WIDTH)
            position.setX(-width);
        if (position.getY() > HEIGHT - 200)
            position.setY(HEIGHT - 200);

        if (position.getX() < -width)
            position.setX(WIDTH);
        if (position.getY() < -height)
            position.setY(HEIGHT - 200);
            
        //para que vaya recto hacia la izquierda
       velocity.setX(-1); 
        velocity.setY(0);
    }

    @Override
	public void Destroy(){
        super.Destroy();
        gameState.incrementarRojosDestruidos();
        if(gameState.getRojosDestruidos() == 5){
            gameState.spawnItem();
            System.out.println("Destruidos los 5 aviones rojos y lanzado y power up");
            gameState.reiniciarRojosDestruidos();
        }
    }

    @Override
	public void draw(Graphics g) {
        super.draw(g);
    }
}