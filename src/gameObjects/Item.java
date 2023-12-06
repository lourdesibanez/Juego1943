package gameObjects;

import java.awt.image.BufferedImage;
import math.Vector2D;
import states.GameState;
import ui.Action;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Graphics2D;

import graphics.Propiedades;
import graphics.Sound;

public class Item extends ObjetoGrafico{

    private ArrayList <PowerUp> powerUps; // Lista de instancias de power-ups
    private int currentPowerUpIndex; // Índice de la instancia de power-up actual

    private long duration;
	protected Action action;
    private Sound powerUpPick;
    protected BufferedImage typeTexture;

    public Item(Vector2D position, BufferedImage texture, Action action, GameState gameState, boolean sonido_activado) {
        super(position, new Vector2D(), 0, texture, gameState);
        initializePowerUps(); // Inicializa la lista de instancias de power-ups
        currentPowerUpIndex = 0; // Establece el índice inicial
        updatePowerUpProperties(); // Actualiza las propiedades iniciales

        this.action = action;
		this.typeTexture = texture;
		duration = 0;
		powerUpPick = new Sound(Propiedades.powerUp, sonido_activado);
    }

    public void executeAction() {
        action.doAction();
		powerUpPick.play();
        switchPowerUp(); // Cambia a la siguiente instancia de power-up
        System.out.println("abajo de switchpowerup");
        updatePowerUpProperties(); // Actualiza las propiedades para reflejar el cambio
    }

    private void initializePowerUps() {
        // Crea instancias de las clases de power-up que deseas representar
        powerUps = new ArrayList<>();
        powerUps.add(new BonusNinja(position, Propiedades.ninja, action, gameState));
        powerUps.add(new BonusAuto(position, Propiedades.auto, action, gameState));
        // ...
    }

    private void switchPowerUp() {
        currentPowerUpIndex = (currentPowerUpIndex + 1) % powerUps.size();
    }

    private void updatePowerUpProperties() {
        // Obtén la instancia de power-up actual
        PowerUp currentPowerUp = powerUps.get(currentPowerUpIndex);

        // Actualiza las propiedades del Item para reflejar la instancia de power-up actual
        // Por ejemplo, actualiza la textura del Item
        typeTexture = currentPowerUp.getTexture();
        // ...
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

    @Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(typeTexture, (int)position.getX(), (int)position.getY(), null);
	}
}
