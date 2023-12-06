package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyBoard implements KeyListener{
	
	private boolean[] keys = new boolean[256];
	
	public static boolean UP, LEFT, RIGHT, DOWN, SHOOT, ATAQUE_ESPECIAL;
	public boolean PAUSE, PAUSE_KEY_PRESSED = false;
	private static int spacePressCount = 0, key_pause = KeyEvent.VK_SPACE, key_shoot = KeyEvent.VK_X, key_ataque_especial = KeyEvent.VK_Z;

	public KeyBoard()
	{
		UP = false;
		LEFT = false;
		RIGHT = false;
		DOWN = false;
		SHOOT = false;
		ATAQUE_ESPECIAL = false;
		PAUSE = false;
	}

	public void cargar_teclas(int pause, int shoot, int ataque_especial) {
		key_pause = pause;
		key_shoot = shoot;
		key_ataque_especial = ataque_especial;
	}
	
	public void update()
	{
		UP = keys[KeyEvent.VK_UP];//codigo flecha arriba
		DOWN = keys[KeyEvent.VK_DOWN];
		LEFT = keys[KeyEvent.VK_LEFT];
		RIGHT = keys[KeyEvent.VK_RIGHT];
		SHOOT = keys[key_shoot];// aca digo que letra oprimir
		ATAQUE_ESPECIAL = keys[key_ataque_especial];
		/*SHOOT = keys[KeyEvent.VK_X];// aca digo que letra oprimir
		ATAQUE_ESPECIAL = keys[KeyEvent.VK_Z];*/
		PAUSE = !PAUSE_KEY_PRESSED;
	}

	public static int getSpacePressCount(){
		return spacePressCount;
	}
	
	@Override//apretas tecla y se almacena en e de tipo keyEvent
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true; 
		if (e.getKeyCode() == key_pause) {
			spacePressCount++;
            PAUSE_KEY_PRESSED = !PAUSE_KEY_PRESSED;
        }
	}

	@Override//suelte tecla
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	public int getPlayerMovementX() {
		int movementX = 0;
        if (LEFT) {
            movementX -= 1;
        }
        if (RIGHT) {
            movementX += 1;
        }
        return movementX;
	}

    public int getPlayerMovementY() {
        int movementY = 0;
        if (UP) {
            movementY -= 1;
        }
        if (DOWN) {
            movementY += 1;
        }
        return movementY;
    }
	
}
