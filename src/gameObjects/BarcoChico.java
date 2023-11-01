package gameObjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import math.Vector2D;
import states.Nivel2;

public class BarcoChico extends BarcoEnemigo{

    public BarcoChico(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, Nivel2 nivel2){
        super(position, velocity, maxVel, texture, nivel2);
    }

    @Override
    public void update(float dt){
        super.update(dt);
    }
    @Override
    public void Destroy(){
        nivel2.playExplosion(position);
        super.Destroy();
        //nivel2.addScore(BARCO_SCORE, position); //lo hago en objetografico
    }
    @Override
    public void draw(Graphics g){
        super.draw(g);
    }
}
