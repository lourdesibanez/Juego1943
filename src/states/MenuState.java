package states;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import graphics.Propiedades;
import ui.Action;
import ui.Button;

public class MenuState extends State{
	
	public static final int WIDTH = 600;//Ancho de la ventana
	public static final int HEIGHT = 700;//largo de la ventana

	//Menu
	public static final String PLAY = "JUGAR";	
	public static final String EXIT = "SALIR";	
	public static final String HIGH_SCORES = "RANKING";	

	private ArrayList<Button> buttons;
	private static long startTime;
	private static int secondsElapsed;
	
	public MenuState() {

		buttons = new ArrayList<Button>();
		
		buttons.add(new Button(
				WIDTH / 2 - 100,
				HEIGHT / 2 - 50,
				PLAY,
				new Action() {
					@Override
					public void doAction() {
						startTime=System.currentTimeMillis();
						//no deberian contarse desde aca los segundos
						State.changeState(new GameState());
					}
				}
				));
		
		buttons.add(new Button(
				WIDTH / 2 - 100,
				HEIGHT / 2 + 50,
				EXIT,
				new Action() {
					@Override
					public void doAction() {
						System.exit(0);
					}
				}
				));
		
		buttons.add(new Button(
				WIDTH / 2 - 100,
				HEIGHT / 2,
				HIGH_SCORES,
				new Action() {
					@Override
					public void doAction() {
						State.changeState(new ScoreState());
					}
				}
				));
		
		
	}
	
	@Override
	public void update(float dt) {
		for(Button b: buttons) {
			b.update();
		}
	}

	public static int getSecondsElapsed() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        secondsElapsed = (int) (elapsedTime / 1000);
        return secondsElapsed;
    }
	

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLUE); 
    	g.fillRect(0, 0, WIDTH, HEIGHT); 

		int imageX = (WIDTH - Propiedades.titulo.getWidth()) / 2;
    	int imageY = 50;
    	g.drawImage(Propiedades.titulo, imageX, imageY, null);

		for(Button b: buttons) {
			b.draw(g);
		}
	}
	

}
