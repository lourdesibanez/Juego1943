package gameObjects;

import java.awt.image.BufferedImage;

import graphics.Propiedades;
import math.Vector2D;
import states.GameState;
import ui.Action;

public class BonusRefuerzo extends PowerUp{

    AvionP38 player = gameState.getPlayer();
    Vector2D playerPosition = player.getPosition();

    public BonusRefuerzo(Vector2D position, BufferedImage texture, Action action, GameState gameState) {
        super(position, texture, action, gameState);
    }

    @Override 
    public void executeAction() { 
        // Obtiene la posición del jugador 
        Vector2D playerPosition = gameState.getPlayer().getPosition(); 
        // Calcula las posiciones para los aviones de refuerzo 
        Vector2D refuerzoPositionLeft = playerPosition.subtract(new Vector2D(50, 0)); 
        Vector2D refuerzoPositionRight = playerPosition.add(new Vector2D(50, 0)); 
        // Crea instancias de AvionRefuerzo con las posiciones calculadas 
        AvionRefuerzo refuerzoIzquierdo = new AvionRefuerzo(refuerzoPositionLeft, new Vector2D(), 7.0, Propiedades.avion_refuerzo, gameState); 
        AvionRefuerzo refuerzoDerecho = new AvionRefuerzo(refuerzoPositionRight, new Vector2D(), 7.0, Propiedades.avion_refuerzo, gameState); 
        
        refuerzoDerecho.activarRefuerzo();
        refuerzoIzquierdo.activarRefuerzo();
        
        
        // Agrega las instancias de AvionRefuerzo a la lista de objetos móviles en gameState 
        gameState.agregarAvionRefuerzo(refuerzoIzquierdo); 
        gameState.agregarAvionRefuerzo(refuerzoDerecho); 

    } 
    
}
