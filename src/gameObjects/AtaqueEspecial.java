package gameObjects;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import states.GameState;

public abstract class AtaqueEspecial {
    protected long ataqueTime;
    protected BufferedImage typeTexture;
    
    public AtaqueEspecial(BufferedImage texture, GameState gameState) {
    }

    public abstract void ejecutarAtaque();
    
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(typeTexture, 0, 0, null);
    }
}
