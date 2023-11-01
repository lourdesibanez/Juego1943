package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import graphics.Propiedades;
import graphics.Sound;
import math.Vector2D;
import states.GameState;



public abstract class AvionEnemigo extends ObjetoGrafico{
	public static final int WIDTH = 600;//Ancho de la ventana
	public static final int HEIGHT = 700;//largo de la ventana

	public static final double AYAKO_MASS = 60;
    public static long BARCO_FIRE_RATE = 2000;
    public static double BARCO_ANGLE_RANGE = Math.PI / 2;
    public static final int BARCO_SCORE = 40;
    public static final int NODE_RADIUS = 160;
    public static final double LASER_VEL = 35.0; // VELOCIDAD DEL LASER

	// Propiedades del enemigo
	public static final int ENEMIGO_SCORE = 20;
	public static final double ENEMIGO_MAX_VEL = 36.0;

	private long fireRate;
	protected Sound shoot;
	
	public AvionEnemigo(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState) {
		super(position, velocity, maxVel, texture, gameState);
		this.velocity = velocity.scale(maxVel);
		fireRate = 0;
		shoot = new Sound(Propiedades.enemigoShoot);
	}

	@Override
	public void update(float dt) {
		if(velocity.getMagnitude() >= this.maxVel) {
			Vector2D reversedVelocity = new Vector2D(-velocity.getX(), -velocity.getY());
			velocity = velocity.add(reversedVelocity.normalize().scale(0.09f));
		}
		//disparo
		if (fireRate > BARCO_FIRE_RATE) {
			// La dirección de disparo será hacia abajo (270 grados en radianes)
			double currentAngle = Math.toRadians(90);
			// Crear el vector de dirección del disparo
			Vector2D toPlayer = new Vector2D(Math.cos(currentAngle), Math.sin(currentAngle));
			Municion laser = new MunicionEnemigos(getCenter().add(toPlayer.scale(width)), toPlayer, LASER_VEL,
					currentAngle + Math.PI / 2, Propiedades.municion_ayako, gameState);
			gameState.getMovingObjects().add(0, laser);
			fireRate = 0;
			shoot.play();
    	}
    	// Incrementar el tiempo entre disparos
    	fireRate += dt;
		velocity = velocity.limit(ENEMIGO_MAX_VEL);
		position = position.add(velocity);
		if(position.getX() > WIDTH)
			position.setX(-width);
		if(position.getY() > HEIGHT)
			position.setY(-height);
		if(position.getX() < -width)
			position.setX(WIDTH);
		if(position.getY() < -height)
			position.setY(HEIGHT);
	}

	protected boolean canShoot(float dt) {
        if (fireRate >= BARCO_FIRE_RATE) {
            fireRate = 0; // Resetear el tiempo entre disparos
            return true;
        }
        return false;
    }
	
	@Override
	public void Destroy(){
		gameState.playExplosion(position);
		//gameState.addScore(ENEMIGO_SCORE, position);
		super.Destroy();
	}
	
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
        AffineTransform originalTransform = g2d.getTransform();
        g2d.translate(position.getX(), position.getY());
        g2d.drawImage(texture, 0, 0, null);
        g2d.setTransform(originalTransform);
	}
}
