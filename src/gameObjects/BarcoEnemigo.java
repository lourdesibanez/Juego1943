package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import graphics.Propiedades;
import graphics.Sound;
import math.Vector2D;
import states.Nivel1;
import states.Nivel2;

public abstract class BarcoEnemigo extends ObjetoGrafico {

    // Atributos comunes a todas las subclases de Barco pero cada subclase puede modificarlos a su necesidad
    public static final int WIDTH = 600; // Ancho de la ventana
    public static final int HEIGHT = 700; // Largo de la ventana
    public static final int NODE_RADIUS = 160;
    public static final double BARCO_MASS = 60;
    public static final double BARCO_MAX_VEL = 20.0;
    public static long BARCO_FIRE_RATE = 1000;
    public static double BARCO_ANGLE_RANGE = Math.PI / 2;
    public static final long BARCO_SPAWN_RATE = 10000;
    public static final double LASER_VEL = 35.0; // VELOCIDAD DEL LASER

    private long fireRate;
	protected Sound shoot;
    protected Nivel2 nivel2;

    public BarcoEnemigo(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, Nivel2 nivel2) {
        super(position, velocity, maxVel, texture, nivel2);
        this.nivel2 = nivel2;
		this.velocity = velocity.scale(maxVel);
		fireRate = 0;
		shoot = new Sound(Propiedades.enemigoShoot, Nivel1.getSonido());
    }

    public void update(float dt){
        if (velocity.getMagnitude() >= this.maxVel) {
            Vector2D reversedVelocity = new Vector2D(-velocity.getX(), -velocity.getY());
            velocity = velocity.add(reversedVelocity.normalize().scale(0.09f));
        }
		//disparo
		if (fireRate > BARCO_FIRE_RATE) {
			Vector2D toPlayer = gameState.getPlayer().getCenter().subtract(getCenter());
            toPlayer = toPlayer.normalize();
                
            double currentAngle = toPlayer.getAngle();
            currentAngle += Math.random() * BARCO_ANGLE_RANGE - BARCO_ANGLE_RANGE / 2;
                
            if (toPlayer.getX() < 0)
                currentAngle = -currentAngle + Math.PI;
            
                toPlayer = toPlayer.setDirection(currentAngle);
                
                // Introduce una pequeña desviación aleatoria en la posición de inicio
                double positionVariation = Math.random() * 10.0; 
                Vector2D startPosition = getCenter().add(toPlayer.scale(width + positionVariation));
                
                Municion laser = new MunicionEnemigos(startPosition, toPlayer, LASER_VEL,
                    currentAngle + Math.PI / 2, Propiedades.municion_ayako, gameState);
                
                gameState.getMovingObjects().add(0, laser);
			fireRate = 0;
			shoot.play();
    	}
        angle += 0.05;
    	// Incrementar el tiempo entre disparos
    	fireRate += dt;
        // Establecer la velocidad solo una vez fuera del bucle de actualización
        double scaledVerticalSpeed = BARCO_MAX_VEL * 0.1; // Puedes ajustar el factor 0.5 según tu necesidad
        velocity = new Vector2D(0, scaledVerticalSpeed);
		position = position.add(velocity);
		if(position.getX() > WIDTH - 50)
			position.setX(WIDTH - 50);
		if(position.getY() > HEIGHT)
			position.setY(-height);
		if(position.getX() < -width)
			position.setX(WIDTH);
		if(position.getY() < -height)
			position.setY(HEIGHT);
    }

    

    public void Destroy(){
        nivel2.playExplosion(position);
        super.Destroy();
    }

    public void draw(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        g2d.translate(position.getX(), position.getY());
        g2d.drawImage(texture, 0, 0, null);
        g2d.setTransform(originalTransform);
    }
}
