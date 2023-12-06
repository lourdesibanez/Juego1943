package gameObjects;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graphics.Propiedades;
import graphics.Sound;
import math.Vector2D;
import states.GameState;

public abstract class ObjetoGrafico{
	
	protected Vector2D velocity;
	protected AffineTransform at;//ayudar a rotar la nave
	protected double angle;//representa donde esta mirando, apuntando
	protected double maxVel;
	protected int width;
	protected int height;
	protected GameState gameState;//en gamestate dice crear una instancia en jugador para que pueda moverlos
	protected Vector2D position;
	protected BufferedImage texture;
	
	public static Sound explosion;
	
	protected boolean Dead;

	private Yamato yamato;

	public static int barcosDestruidos = 0;
	public static int avionesDestruidos = 0;
	protected int rojosDestruidos = 0;

	public ObjetoGrafico(Vector2D position, Vector2D velocity, double maxVel, BufferedImage texture, GameState gameState) {
		this.velocity = velocity;
		this.maxVel = maxVel;
		this.gameState = gameState;
		this.position = position;
		this.texture = texture;
		width = texture.getWidth();
		height = texture.getHeight();
		angle = 0;
		explosion = new Sound(Propiedades.explosion, gameState.getSonido());
		Dead = false;
	}

	
	/* se encarga de detectar colisiones entre el objeto actual y otros objetos en movimiento en el juego, y luego llama al método objectCollision
	para que depende que objetos sean pasen distintas cosas
	 */
	protected void collidesWith(){
		ArrayList<ObjetoGrafico> movingObjects = gameState.getMovingObjects(); 
		for(int i = 0; i < movingObjects.size(); i++){
			ObjetoGrafico m  = movingObjects.get(i);
			if(m.equals(this))
				continue;
			double distance = m.getCenter().subtract(getCenter()).getMagnitude();
			if(distance < m.width/2 + width/2 && movingObjects.contains(this) && !m.Dead && !Dead){
				objectCollision(this, m);
			}
		}
	}
	
	protected void objectCollision(ObjetoGrafico a, ObjetoGrafico b) {
		AvionP38 p = null;
		if(a instanceof AvionP38)
			p = (AvionP38)a;
		else if(b instanceof AvionP38)
			p = (AvionP38)b;
		
		if(p != null && p.isSpawning()) 
			return;

		if(a instanceof MunicionP38 && b instanceof Ayako){
			((Ayako) b).incrementarColisiones();
		}
		
		if (a instanceof AvionEnemigoRojo && a.isDead() || b instanceof AvionEnemigoRojo && b.isDead()) {
			rojosDestruidos++;
			//a.Destroy();
		}
		if(a instanceof MunicionP38 && b instanceof Yamato){
			((Yamato) b).incrementarColisionesYamato();
		}

		if(a instanceof AvionEnemigo && b instanceof Ayako)
			return;
			
		if(a instanceof AvionEnemigo && b instanceof AvionEnemigo)
			return;
		
		if(b instanceof MunicionEnemigos && a instanceof AvionEnemigo)
			return;

		if(a instanceof MunicionEnemigos && b instanceof AvionEnemigo)
			return;

		if ((this instanceof Yamato && a instanceof BarcoChico) || (this instanceof BarcoChico && a instanceof Yamato)) {
			// Manejar el caso específico en el que Yamato y BarcoChico no deben colisionar
			// Puedes agregar lógica específica aquí si es necesario
			return;
		}
			
		if(a instanceof MunicionEnemigos && b instanceof Yamato)
			return;
		if(a instanceof MunicionEnemigos && b instanceof BarcoChico)
			return;
		
		if(a instanceof MunicionP38 && b instanceof AvionEnemigoVerde || a instanceof MunicionP38 && b instanceof AvionEnemigoNegro || a instanceof MunicionP38 && b instanceof AvionEnemigoRojo){
			gameState.addScore(20, getPosition());
			++avionesDestruidos;
		}
		if(a instanceof Laser && b instanceof AvionEnemigoVerde || a instanceof Laser && b instanceof AvionEnemigoNegro || a instanceof Laser && b instanceof AvionEnemigoRojo || a instanceof Laser && b instanceof Ayako){
			gameState.addScore(60, getPosition());
		}
		 
		if(a instanceof MunicionP38 && b instanceof BarcoChico || a instanceof MunicionP38 && b instanceof Yamato){
			gameState.addScore(30, getPosition());
			++barcosDestruidos;
		}
		if(a instanceof Laser && b instanceof BarcoChico || a instanceof Laser && b instanceof Yamato){
			gameState.addScore(60, getPosition());
			++barcosDestruidos;
		}

		if(a instanceof MunicionP38 && b instanceof Item || b instanceof MunicionP38 && a instanceof Item)
			gameState.getItem().executeAction();
		
		if(a instanceof AvionRefuerzo && b instanceof MunicionEnemigos){
			a.Destroy();
		}
		if(b instanceof AvionRefuerzo && a instanceof MunicionEnemigos){
			b.Destroy();
		}

		if(!(a instanceof PowerUp || b instanceof PowerUp)){
			a.Destroy();
			b.Destroy();
			return;
		}
		
		if(p != null){
			if(a instanceof AvionP38){
				((PowerUp)b).executeAction();
				b.Destroy();
			}else if(b instanceof AvionP38){
				((PowerUp)a).executeAction();
				a.Destroy();
			}
		}

	}

	
	protected void Destroy(){
		Dead = true;
		if(!(this instanceof Municion) && !(this instanceof PowerUp))
			explosion.play();
	}
	
	protected Vector2D getCenter(){
		return new Vector2D(position.getX() + width/2, position.getY() + height/2);
	}
	
	public boolean isDead() {return Dead;}
	
	public abstract void update(float dt);
	
	public abstract void draw(Graphics g);

	//public abstract void silenciar_sonido();
	
	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(Vector2D position) {
		this.position = position;
	}
}

