package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graphics.Propiedades;
import graphics.Sound;
import math.Vector2D;
import states.Nivel2;

public class Yamato extends BarcoEnemigo{
    private int cantColisiones = 0;
    private boolean destruido;
    private long fireRate;
    private Nivel2 nivel2;
    public static final double YAMATO_MASS = 60;

    public Yamato(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture,
            ArrayList<Vector2D> path, Nivel2 nivel2){
        super(position, velocity, maxVel, texture, nivel2);
        shoot = new Sound(Propiedades.enemigoShoot);
        destruido = false;
        this.nivel2 = nivel2;

        // Establecer la posición inicial en el centro de la pantalla en la parte superior
        position.setX(0); // Centro horizontal
        position.setY(-height); // Límite superior
    }

    public void update(float dt){
        fireRate += dt;
        if (position.getY() < HEIGHT) {
            // ayako no está completamente en la pantalla, sigue avanzando
            velocity = new Vector2D(0, 55); // Otra dirección de movimiento
        } else {
            // ayako está completamente en la pantalla, detén el movimiento
            velocity = new Vector2D(); 
        }
        if (position.getY() > HEIGHT/22) {
            velocity = new Vector2D(0, 0); 
        }
        velocity = velocity.scale(1 / YAMATO_MASS);
        velocity = velocity.limit(maxVel);
        position = position.add(velocity);
        // Lógica de disparo
        if (fireRate > BARCO_FIRE_RATE) {
            int numShots = 5; // Número de disparos simultáneos que deseas
            
            for (int i = 0; i < numShots; i++) {
                Vector2D toPlayer = nivel2.getPlayer().getCenter().subtract(getCenter());
                toPlayer = toPlayer.normalize();
                
                double currentAngle = toPlayer.getAngle();
                currentAngle += Math.random() * BARCO_ANGLE_RANGE - BARCO_ANGLE_RANGE / 2;
                
                if (toPlayer.getX() < 0)
                    currentAngle = -currentAngle + Math.PI;
                
                toPlayer = toPlayer.setDirection(currentAngle);
                
                // Introduce una pequeña desviación aleatoria en la posición de inicio
                double positionVariation = Math.random() * 10.0; // Ajusta este valor según tu necesidad
                Vector2D startPosition = getCenter().add(toPlayer.scale(width + positionVariation));
                
                Municion laser = new MunicionEnemigos(startPosition, toPlayer, LASER_VEL,
                    currentAngle + Math.PI / 2, Propiedades.municion_ayako, nivel2);
                
                nivel2.getMovingObjects().add(0, laser);
            }
            fireRate = 0;
            shoot.play();
        }
        if (shoot.getFramePosition() > 8500) {
            shoot.stop();
        }
        angle += 0.05;
        collidesWith();
    }

    public void incrementarColisionesYamato(){
        cantColisiones++;
    }

    @Override
    public void Destroy(){
        nivel2.playExplosion(new Vector2D(WIDTH/2, 80));
        if(cantColisiones == 5){
            //poner mas de una explosion para que se note que murio
            nivel2.playExplosion(position);
            //nivel2.addScore(BARCO_SCORE, position); //lo hago en objeto grafico
            nivel2.finNivel2();
            nivel2.gameOver();
            super.Destroy();
            destruido = true;
        }
    }
  
    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        g2d.drawImage(texture, at, null);
    }
}
