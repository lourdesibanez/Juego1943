package gameObjects;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import states.Nivel2;

public class Tsunami extends AtaqueEspecial {

    private List<ObjetoGrafico> barcosEnemigosADestruir;

    Nivel2 nivel2;

    public Tsunami(BufferedImage texture, Nivel2 nivel2) {
        super(texture, nivel2);
        this.typeTexture = texture;
        this.barcosEnemigosADestruir = new ArrayList<>();
        this.nivel2 = nivel2;
    }

    public void ejecutarAtaque() {
        if (ataqueTime == 0) {
            ataqueTime = System.currentTimeMillis(); // Registra el tiempo de inicio 
        }
        //verifica su duración
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - ataqueTime;
        if (elapsedTime > 2000) {
            ataqueTime = 0;
        }

        // Marcar barcos enemigos para destrucción
        if (nivel2 != null) {
             System.out.println("Nivel 2 es igual a null");
            for (ObjetoGrafico objeto : nivel2.getMovingObjects()) {
                if (objeto instanceof BarcoChico) {
                    barcosEnemigosADestruir.add(objeto);
                }
            }
        }

        // Destruir los barcos enemigos marcados
        for (ObjetoGrafico barco : barcosEnemigosADestruir) {
            barco.Destroy();
            System.out.println("Destruyendo barcos con ataque especial");
        }
        barcosEnemigosADestruir.clear();
    }

    public void draw(Graphics g) {
        super.draw(g);
    }
}
