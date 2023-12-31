package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graphics.Propiedades;
import graphics.Sound;
import math.Vector2D;
import states.Nivel1;

public class Ayako extends AvionEnemigo {
    private ArrayList<Vector2D> path;
    private Vector2D currentNode;
    private int index;
    private boolean following;
    private long fireRate;
    private Sound shoot;
    public static final int WIDTH = 600;//Ancho de la ventana
	public static final int HEIGHT = 700;//largo de la ventana
    private int cantColisiones = 0;
    private boolean destruido;

    public Ayako(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture,
            ArrayList<Vector2D> path, Nivel1 gameState, boolean sonido_activado) {
        super(position, velocity, maxVel, texture, gameState, sonido_activado);
        this.path = path;
        index = 0;
        following = true;
        fireRate = 0;
        shoot = new Sound(Propiedades.enemigoShoot, sonido_activado);

        // Establecer la posición inicial en el centro de la pantalla en la parte superior
        position.setX(WIDTH / 2 - width / 2); // Centro horizontal
        position.setY(-height); // Límite superior
    }

    @Override
    public void update(float dt) {
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
        velocity = velocity.scale(1 / AYAKO_MASS);
        velocity = velocity.limit(maxVel);
        position = position.add(velocity);
        // Lógica de disparo
        if (fireRate > BARCO_FIRE_RATE) {
            int numShots = 5; // Número de disparos simultáneos que deseas
            
            for (int i = 0; i < numShots; i++) {
                Vector2D toPlayer = gameState.getPlayer().getCenter().subtract(getCenter());
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
                    currentAngle + Math.PI / 2, Propiedades.municion_ayako, gameState);
                
                gameState.getMovingObjects().add(0, laser);
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


    @Override
    public void Destroy() {
        gameState.playExplosion(new Vector2D(WIDTH/2, 80));
        if(cantColisiones == 25){
            //poner mas de una explosion para que se note que murio
            //gameState.playExplosion(position);
            gameState.addScore(BARCO_SCORE, position); //que se le sume el puntaje una vez que lo destruye por completo
            gameState.finNivel();
            super.Destroy();
            destruido = true;
        }
    }

    public boolean isDestroy(){
		return destruido;
	}

    //para que el ayako no sea destruido antes de recibir determinada cantidad de disparos
    public void incrementarColisiones(){
        cantColisiones++;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
        g2d.drawImage(texture, at, null);
    }
}


