package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import graphics.Propiedades;
import graphics.Loader;
import graphics.Text;
import math.Vector2D;

public class LoadingState extends State{

	public static final int WIDTH = 600;//Ancho de la ventana
	public static final int HEIGHT = 700;//largo de la ventana

	//Menu	
	public static final int LOADING_BAR_WIDTH = 500;
	public static final int LOADING_BAR_HEIGHT = 50;	

	private Thread loadingThread;
	
	public LoadingState(Thread loadingThread) {
		this.loadingThread = loadingThread;
		this.loadingThread.start();
		Font font = Loader.loadFont("/fonts/PressStart2P.ttf", 38);
	}
	
	@Override
	public void update(float dt) {
		if(Propiedades.loaded) {
			State.changeState(new MenuState());
			try {
				loadingThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void draw(Graphics g) {

		g.setColor(Color.BLUE); 
    	g.fillRect(0, 0, WIDTH, HEIGHT); 
		GradientPaint gp = new GradientPaint(
				WIDTH / 2 - LOADING_BAR_WIDTH / 2,
				HEIGHT / 2 - LOADING_BAR_HEIGHT / 2,
				Color.WHITE,
				WIDTH / 2 + LOADING_BAR_WIDTH / 2,
				HEIGHT / 2 + LOADING_BAR_HEIGHT / 2,
				Color.BLUE
				);
		
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setPaint(gp);
		
		float percentage = (Propiedades.count / Propiedades.MAX_COUNT);
		
		g2d.fillRect(WIDTH / 2 - LOADING_BAR_WIDTH / 2,
				HEIGHT / 2 - LOADING_BAR_HEIGHT / 2,
				(int)(LOADING_BAR_WIDTH * percentage),
				LOADING_BAR_HEIGHT);
		
		g2d.drawRect(WIDTH / 2 - LOADING_BAR_WIDTH / 2,
				HEIGHT / 2 - LOADING_BAR_HEIGHT / 2,
				LOADING_BAR_WIDTH,
				LOADING_BAR_HEIGHT);

		Font font = Loader.loadFont("/fonts/PressStart2P.ttf", 28);
		
		Text.drawText(g2d, "BATALLA DE MIDWAY", new Vector2D(WIDTH / 2, HEIGHT / 2 - 50),
				true, Color.WHITE, font);
		
		
		Text.drawText(g2d, "CARGANDO...", new Vector2D(WIDTH / 2, HEIGHT / 2 + 40),
				true, Color.WHITE, font);
		
	}
}
