package states;

import java.awt.Color;
import java.awt.Graphics;

import graphics.Propiedades;
import ui.Action;
import ui.Button;

public class MenuNivel2 extends State { //si ponemos extends MenuState
    public static final int WIDTH = 600;//Ancho de la ventana
	public static final int HEIGHT = 700;

    private Button jugar_nivel2, boton2, boton_salir;
	private static long startTime;
	private static int secondsElapsed;
	private static int score = 0;
	private int gameStateScore; 

    public MenuNivel2(){
        jugar_nivel2 = new Button(WIDTH / 2 - 100,
				HEIGHT / 2 - 50,
				"JUGAR NIVEL 2",
				new Action() {
					@Override
					public void doAction() {
						startTime = System.currentTimeMillis();
        				secondsElapsed = 0;
						long currentTime = System.currentTimeMillis();
        				long elapsedTime = currentTime - startTime;
        				secondsElapsed = (int) (elapsedTime / 1000);

						int puntajeActual = getScore(); // Obtiene el puntaje actual de MenuNivel2
            			Nivel2.setScore(puntajeActual); // Establece el puntaje en Nivel2
						State.changeState(new Nivel2(gameStateScore)); //aca deberia ser new Nivel2()
					}
				}
			    );
        boton2 = new Button(WIDTH / 2 - 100,
				HEIGHT / 2 - 100,
				"AYAKO ELIMINADO",
				null
				);
		boton_salir = new Button(
					WIDTH / 2 - 100,
					HEIGHT / 2 + 100,
					"SALIR",
					new Action() {
						@Override
						public void doAction() {
							System.exit(0);
						}
					}
					);
    }

	public MenuNivel2(int score) {
        this.gameStateScore = score; // Inicializa la variable de instancia con el puntaje de GameState
    }

    @Override
    public void update(float dt) {
        jugar_nivel2.update();
        boton2.update();
		boton_salir.update();
    }

	public static int getSecondsElapsed() {
        return secondsElapsed;
    }

	public static void setScore(int newScore) {
        score = newScore;
    }

	public static int getScore(){
		return score;
	}


    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE); 
    	g.fillRect(0, 0, WIDTH, HEIGHT); 

		int imageX = (WIDTH - Propiedades.titulo.getWidth()) / 2;
    	int imageY = 50;
    	g.drawImage(Propiedades.titulo, imageX, imageY, null);
        jugar_nivel2.draw(g);
        boton2.draw(g);
		boton_salir.draw(g);
    }
    
}
