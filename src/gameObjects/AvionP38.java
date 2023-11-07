package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import graphics.Propiedades;
import graphics.Sound;
import input.KeyBoard;
import math.Vector2D;
import states.GameState;

public class AvionP38 extends ObjetoGrafico{

	public static final int WIDTH = 600;//Ancho de la ventana
	public static final int HEIGHT = 700;//largo de la ventana

	public static int FIRERATE = 300;//velocidad del disparo

	//velocidad del avion
	public static final double DELTA_X = 5;
	public static final double DELTA_Y = 5;

	//public static final double ACC = 5;// constante de aceleracion
	public static final long FLICKER_TIME = 200;
	public static final long SPAWNING_TIME = 3000;

	public static final double LASER_VEL = 15.0;//VELOCIDAD DEL LASER
    
	
	private Vector2D heading;	//vector de direccion,represetnacion donde esta mirando la nave
	private Vector2D acceleration;//cambio en la velocidad con respecto al tiempo
	private long fireRate;
	protected boolean spawning, visible;
	protected long spawnTime, flickerTime;
	protected Sound shoot, loose;
	Laser laser;
	Ninja ninja;
	SuperShell superShell;
	BonusAuto BonusAuto;
	Escopeta escopeta;
	Ametralladora ametralladora;
	protected boolean destruido;
	private long fireSpeed;
	
	public AvionP38(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState) {
		super(position, velocity, maxVel, texture, gameState);
		heading = new Vector2D(0, 1);
		acceleration = new Vector2D();
		fireRate = 0;
		spawnTime = 0;
		flickerTime = 0;

		BonusAuto = new BonusAuto(position, texture, null, gameState);
		ninja = new Ninja(position, velocity, maxVel, maxVel, texture, gameState);
		laser = new Laser(position, velocity, maxVel, maxVel, texture, gameState);
		superShell = new SuperShell(position, velocity, maxVel, maxVel, texture, gameState);
		escopeta = new Escopeta(position, velocity, maxVel, maxVel, texture, gameState);
		ametralladora = new Ametralladora(position, velocity, maxVel, maxVel, texture, gameState);

		shoot = new Sound(Propiedades.playerShoot);
		loose = new Sound(Propiedades.playerLoose);
		visible = true;
	}
	
	@Override
	public void update(float dt) {
		laser.update(dt);
		ametralladora.update(dt);
		BonusAuto.update(dt);
		superShell.update(dt);
		ninja.update(dt);
		escopeta.update(dt);
		/* 
		if(KeyBoard.ATAQUE_ESPECIAL && !ataqueEspecialActivo){
			if(!gameState.subtractLife(position)) {
				gameState.gameOver();
				super.Destroy();
			}
		}
		*/

		fireRate += dt;
		if(!superShell.isSuperShellOn())
            FIRERATE = 300; // Restaura la velocidad de disparo normal

		if(!BonusAuto.isAutoOn()) 
			fireSpeed = FIRERATE;

		if(spawning) {
			flickerTime += dt;
			spawnTime += dt;
			if(flickerTime > FLICKER_TIME) {
				visible = !visible;
				flickerTime = 0;
			}
			if(spawnTime > SPAWNING_TIME) {
				spawning = false;
				visible = true;
			}
		}
		if (KeyBoard.SHOOT &&  fireRate > fireSpeed && !spawning) {
			gameState.getMovingObjects().add(0,new MunicionP38( //ver desde donde sale el disparo
							getCenter().add(heading.scale(width)),//posicion del laser, verificar
							heading,// velocidad
							LASER_VEL,//velocidad maxima
							angle,//mismo angulo de rotacion que el jugador
							Propiedades.laser2,
							gameState
						));
			if(ninja.isNinjaOn()) {
				Vector2D leftGun = getCenter();
				Vector2D rightGun = getCenter();
				Vector2D temp = new Vector2D(heading);
				temp.normalize();
				temp = temp.setDirection(angle - 1.3f);
				temp = temp.scale(width);
				rightGun = rightGun.add(temp);
				temp = temp.setDirection(angle - 1.9f);
				leftGun = leftGun.add(temp);
				Municion l = new MunicionP38(leftGun, heading, LASER_VEL, angle, Propiedades.laser2, gameState);
				Municion r = new MunicionP38(rightGun, heading, LASER_VEL, angle, Propiedades.laser2, gameState);
				gameState.getMovingObjects().add(0, l);
				gameState.getMovingObjects().add(0, r);
			} 
			if (laser.isLaserOn()) {
					gameState.getMovingObjects().add(0, new Laser(
						getCenter().add(heading.scale(width)),
						heading,
						LASER_VEL,
						angle,
						Propiedades.laser, // Usar la nueva imagen del láser fuerte si está activada
						gameState
					));
			} 
				if (escopeta.isEscopetaOn()) {
						for (int i = 0; i < 4; i++) {
							// Genera un ángulo aleatorio en radianes
							double currentAngle = Math.random() * 2 * Math.PI;

							// Calcula un vector de dirección basado en el ángulo
							Vector2D direccion = new Vector2D(Math.cos(currentAngle), Math.sin(currentAngle));

							// Introduce una pequeña desviación aleatoria en la posición de inicio
							double positionVariation = Math.random() * 10.0; // Ajusta este valor según tu necesidad
							Vector2D startPosition = getCenter().add(direccion.scale(width + positionVariation));

							// Crea un objeto de MunicionEnemigos con la dirección y posición calculadas
							gameState.getMovingObjects().add(0, new Escopeta(
								startPosition, 
								direccion, 
								LASER_VEL, 
								currentAngle + Math.PI / 2, 
								Propiedades.municion_ayako, 
								gameState));
						}
				} 
				if (ametralladora.isAmetralladoraOn()){
							for (int i = 0; i < 3; i++) {
								gameState.getMovingObjects().add(0, new Ametralladora(
								getCenter().add(heading.scale(width)),
								heading,
								LASER_VEL,
								angle,
								Propiedades.municion_ayako, // Usar la nueva imagen del láser fuerte si está activada
								gameState
								));
							}
				} 
			fireRate = 0;
			shoot.play();
		}
		

		if(shoot.getFramePosition() > 8500) {
			shoot.stop();
		}
		if(KeyBoard.RIGHT)
			position.setX(position.getX() + DELTA_X); // Ajusta la velocidad horizontal hacia la derecha
		if(KeyBoard.LEFT)
			position.setX(position.getX() - DELTA_X); // Ajusta la velocidad horizontal hacia la izquierda
		if(KeyBoard.UP)
			position.setY(position.getY() - DELTA_Y); // Ajusta la velocidad vertical hacia arriba
		if(KeyBoard.DOWN)
			position.setY(position.getY() + DELTA_Y); // Ajusta la velocidad vertical hacia abajo
		
		velocity = velocity.add(acceleration);//velocidad le agrego la aceleracion
		velocity = velocity.limit(maxVel);
		//para que dispare para arriba?
		heading = heading.setDirection(angle - Math.PI/2);
		//cambio de posicion respecto altiempo
        //fotograma le sumamos vector posicion
		position = position.add(velocity);
		//verifica que el avion no sobrepase los limites de la pantalla
		if (position.getX() > WIDTH - width)
		    position.setX(WIDTH - width);
		if (position.getX() < 0)
            position.setX(0);
		if (position.getY() > HEIGHT - 70)
            position.setY(HEIGHT - 70);
	    if (position.getY() < 0)
            position.setY(0);
		collidesWith();
	}

	public void setSuperShell() {
		superShell.activarSuperShell();
		FIRERATE = 100; 
	}

	public void setNinja() {
		ninja.activarNinja();
	}

	public void setLaser() {
		laser.activarLaser();
	}


	public void setEscopeta(){
		escopeta.activarEscopeta();
	}

	public void setAmetralladora(){
		ametralladora.activarAmetralladora();
	}
	
	@Override
	public void Destroy() {
		if (!escopeta.isEscopetaOn() || !ametralladora.isAmetralladoraOn()) { 
			spawning = true;
			gameState.playExplosion(position);
			spawnTime = 0;
			loose.play();
			if(!gameState.subtractLife(position)) {
				gameState.gameOver();
				super.Destroy();
			}
			resetValues();
			destruido = true;
		}
	}

	public boolean isDestroy(){
		return destruido;
	}
	
	protected void resetValues() {
		angle = 0;
		velocity = new Vector2D();
		position = GameState.PLAYER_START_POSITION;
	}
	
	@Override
	public void draw(Graphics g) {
		if(!visible)
			return;
		Graphics2D g2d = (Graphics2D)g;
		at = AffineTransform.getTranslateInstance(position.getX(), position.getY());
		g2d.drawImage(texture, at, null);
	}
	
	public Vector2D getPosition() {
		return position; 
	}

	public boolean isSpawning() {return spawning;}

}