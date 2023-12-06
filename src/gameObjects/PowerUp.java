package gameObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import graphics.Propiedades;
import graphics.Sound;
import math.Vector2D;
import states.GameState;
import ui.Action;

public class PowerUp extends ObjetoGrafico {

	private long duration;
	protected Action action;
	private Sound powerUpPick;
	protected BufferedImage typeTexture;
	private static PowerUpTypes[] powerUpTypes = PowerUpTypes.values();
	private int currentTypeIndex = 0;

	
	public PowerUp(Vector2D position, BufferedImage texture, Action action, GameState gameState) {
		super(position, new Vector2D(), 0, texture, gameState);

		this.action = action;
		this.typeTexture = texture;
		duration = 0;
		powerUpPick = new Sound(Propiedades.powerUp, GameState.getSonido());
	}
	

	public enum PowerUpTypes {
		AUTO("AUTO: LLENA TANQUE", Propiedades.auto),
		SUPER_SHELL("SUPER SHELL: DISPAROS CONTINUOS", Propiedades.superShell),
		POW("POW: +4 ENERGIA", Propiedades.pow),
		NINJA("ESTRELLA NINJA: DISPARO RAFAGA", Propiedades.ninja),
		REFUERZO("REFUERZOS", Propiedades.refuerzo),
		LASER("LASER", Propiedades.laser),
		ESCOPETA("ESCOPETA", Propiedades.escopeta),
		AMETRALLADORA("AMETRALLADORA", Propiedades.ametralladora);
		
	
		public String text;
		public BufferedImage texture;
	
		private PowerUpTypes(String text, BufferedImage texture){
			this.text = text;
			this.texture = texture;
		}
	}
	
	void executeAction() {
		action.doAction();
		powerUpPick.play();
		currentTypeIndex++;
		if (currentTypeIndex >= powerUpTypes.length) {
    		currentTypeIndex = 0;
		}

	}

	@Override
	public void update(float dt) {
		angle += 0.1;
		duration += dt;
		if(duration > 10000) {
			this.Destroy();
		}
		collidesWith();
	}

	//dibujo la textura de cada power up
	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(typeTexture, (int)position.getX(), (int)position.getY(), null);
	}

	public BufferedImage getTexture() {
		return powerUpTypes[currentTypeIndex].texture;
	}

}
