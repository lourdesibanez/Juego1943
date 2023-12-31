package gameObjects;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import states.Nivel1;

public class Relampago extends AtaqueEspecial {

    private Nivel1 gameState;
    private List<ObjetoGrafico> avionesEnemigosADestruir;

    public Relampago(BufferedImage texture, Nivel1 gameState) {
        super(texture, gameState);
        this.typeTexture = texture;
        this.gameState = gameState;
        this.avionesEnemigosADestruir = new ArrayList<>();
    }

    public void ejecutarAtaque() {
        if (ataqueTime == 0) {
            ataqueTime = System.currentTimeMillis(); // Registra el tiempo de inicio del relámpago
        }
        // El relámpago ya está activo, verifica su duración
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - ataqueTime;
        if (elapsedTime > 2000) {
            ataqueTime = 0;
        }

        // Marcar aviones enemigos para destrucción
        if (gameState != null) {
            for (ObjetoGrafico objeto : gameState.getMovingObjects()) {
                if (objeto instanceof AvionEnemigoNegro || objeto instanceof AvionEnemigoVerde || objeto instanceof AvionEnemigoRojo) {
                    avionesEnemigosADestruir.add(objeto);
                }
            }
        }

        // Destruir los aviones enemigos marcados
        for (ObjetoGrafico avion : avionesEnemigosADestruir) {
            avion.Destroy();
        }
        avionesEnemigosADestruir.clear();
    }

    public void draw(Graphics g){
        super.draw(g);
    }
}
