package states;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import Menu.Jugador;
import gameObjects.*;
import gameObjects.PowerUp.PowerUpTypes;
import graphics.*;
import input.KeyBoard;
import io.Ranking;
import math.Vector2D;
import ui.Action;

public class GameState extends State{

	public static final int WIDTH = 600;//Ancho de la ventana
	public static final int HEIGHT = 700;//largo de la ventana

	public static final double PLAYER_MAX_VEL = 7.0;
	public static final long GAME_OVER_TIME = 3000;


	// Propiedades del enemigo
	public static final double ENEMIGO_INIT_VEL = 2.0;
	public static final int ENEMIGO_SCORE = 20;
	public static final double ENEMIGO_MAX_VEL = 6.0;
    public static final int ENEMIGO_WIDTH = 50;
    public static final int ENEMIGO_HEIGHT = 50;

	public static final Vector2D PLAYER_START_POSITION = new Vector2D(WIDTH/2 - Propiedades.player.getWidth()/2, HEIGHT/2 - Propiedades.player.getHeight()/2);
	
	private AvionP38 player;
	protected static boolean passedLevel2 = false;
	
	private ArrayList<ObjetoGrafico> movingObjects = new ArrayList<ObjetoGrafico>();// dibujar todos lo objetos que se muevan en el juego
	//para que player pueda acceder a array tiene que tener una instancia de game state
	private ArrayList<Animation> explosions = new ArrayList<Animation>();
	private ArrayList<Mensaje> messages = new ArrayList<Mensaje>();

	//para escribir en el archivo json
	File archivo = new File("/Users/Luly/OneDrive/Documentos/ProyectoFinal/data.json");

	protected static int score = 0;
	protected static int lives;
	
	private int enemigos;
	private boolean nivelIniciado = false;
	private int rojosDestruidos = 0;
	
	private static Sound backgroundMusic;
	private long gameOverTimer;
	private boolean gameOver, fin_nivel;
	
	protected long powerUpSpawner;
	private Item item;
	private List<AvionRefuerzo> avionesRefuerzo; 
	BonusRefuerzo BonusRefuerzo;

	private float tiempoAvionesRefuerzo = 30.0f; // Tiempo en segundos que los aviones de refuerzo estarán en pantalla 
	
	private Relampago relampago;
	private boolean ataqueEspecialActivo = false;
	private long tiempoAtaqueEspecial =0;

	private boolean ayakoSpawned = false;

	Ranking ranking = new Ranking();

	Jugador jugador;
	
	protected int backgroundY = 0;
	protected int backgroundSpeed = 1;

	private static boolean sonido_activado = true;
	
	public GameState(){
		//this se refiere a esta clase
		player = new AvionP38(PLAYER_START_POSITION, new Vector2D(),
				PLAYER_MAX_VEL, Propiedades.player, this);	
		gameOver = false;
		movingObjects.add(player);
		enemigos = 1;
		empezarNivel();
		cargar_sonido();
		/*backgroundMusic = new Sound(Propiedades.backgroundMusic);
		backgroundMusic.loop();
		backgroundMusic.changeVolume(-10.0f);*/
		gameOverTimer = 0;
		powerUpSpawner = 0;
		ayakoSpawned = false;
		gameOver = false;
		fin_nivel = false;
		lives = 10;
		avionesRefuerzo = new ArrayList<>();
		relampago = new Relampago(Propiedades.relampago, this);
		System.out.println("instancia de game state");
	}

	private void cargar_sonido(){
		backgroundMusic = new Sound(Propiedades.backgroundMusic);
		if (sonido_activado){
			backgroundMusic.loop();
			backgroundMusic.changeVolume(-10.0f);
		} else {
			AvionEnemigo.silenciarSonido();
		}
		//backgroundMusic.turnOffVolume();
		/*sonido_activado = sonido_on;
		System.out.println("sonido activado: "+sonido_on);
		if (sonido_activado){
			backgroundMusic.turnOffVolume();
		}*/
	}
	
	public void addScore(int value, Vector2D position) {	
		Color c = Color.WHITE;
		String text = "+" + value + " puntos";
		score += value;
		messages.add(new Mensaje(position, true, text, c, true, Propiedades.fontMed));
	}
	
	private void empezarNivel() {	
		if (nivelIniciado == false) {
			messages.add(new Mensaje(new Vector2D(WIDTH / 2, HEIGHT / 2), false,
					"NIVEL 1", Color.WHITE, true, Propiedades.fontBig));
			nivelIniciado = true;
		}
		double x, y;
		for (int i = 0; i < enemigos; i++) {
			if (i % 3 == 0) {
				// Enemigo negro
				x = (int) (Math.random() * WIDTH);
				y = -ENEMIGO_HEIGHT;
				BufferedImage texture = Propiedades.enemigo_negro;
				movingObjects.add(new AvionEnemigoNegro(
						new Vector2D(x, y),
						new Vector2D(0, 1),
						ENEMIGO_INIT_VEL * Math.random() + 1,
						texture,
						this
				));
			} else if (i % 3 == 1) {
				// Enemigo verde en forma de "V"
				x = Math.random() * (WIDTH / 2 - ENEMIGO_WIDTH);
				y = Math.random() * HEIGHT;
				BufferedImage texture = Propiedades.enemigo_v_chico;
				movingObjects.add(new AvionEnemigoVerde(
						new Vector2D(x, y),
						new Vector2D(1, 1),
						ENEMIGO_INIT_VEL * Math.random() + 1,
						texture,
						this
				));
				x = WIDTH - x - ENEMIGO_WIDTH;
				movingObjects.add(new AvionEnemigoVerde(
						new Vector2D(x, y),
						new Vector2D(-1, 1),
						ENEMIGO_INIT_VEL * Math.random() + 1,
						texture,
						this
				));
			} else {
				// Aviones enemigos rojos en formación recta
				double spaceBetweenPlanes = (double) WIDTH / (5 + 10);
				
				int minY = 10;  // Define la distancia mínima desde el borde inferior
				int maxY = Nivel2.HEIGHT - minY;
				
				int p = minY + (int)(Math.random() * (maxY - minY)); // Altura aleatoria en la pantalla
				for (int j = 0; j < 5; j++) {
    				x = WIDTH - (j + 1) * spaceBetweenPlanes - ENEMIGO_WIDTH;
    				movingObjects.add(new AvionEnemigoRojo(
            		new Vector2D(x, p),
            		new Vector2D(-1, 0),
            		ENEMIGO_INIT_VEL * Math.random() + 1,
            		Propiedades.enemigo_rojo,
            		this
    				));
				}
			}
		}
		enemigos++;
	}

	public void playExplosion(Vector2D position){
		explosions.add(new Animation(
				Propiedades.exp,
				50,
				position.subtract(new Vector2D(Propiedades.exp[0].getWidth()/2, Propiedades.exp[0].getHeight()/2))
				));
	}
	
	private void spawnAyako(){
		double x = Math.random() * WIDTH;
		double y = -HEIGHT;
		ArrayList<Vector2D> path = new ArrayList<Vector2D>();
		path.add(new Vector2D(0, 0));
		path.add(new Vector2D(0, HEIGHT));
		movingObjects.add(new Ayako(
				new Vector2D(x, y),
				new Vector2D(),
				1,
				Propiedades.ayako,
				path,
				this
				));
	}

	protected void spawnPowerUp() {
		final int x = (int) (Math.random() * WIDTH);
		final int y = (int) (Math.random() * HEIGHT);
		int index = (int) (Math.random() * (PowerUpTypes.values().length));
		PowerUpTypes p = PowerUpTypes.values()[index];
		final String text = p.text;
		Action action = null;
		Vector2D position = new Vector2D(x , y);
		switch(p) {
		case AUTO:
			action = new Action() {
				@Override
				public void doAction() {
					BonusAuto auto = new BonusAuto(position, Propiedades.auto, this, GameState.this);
					auto.executeAction();
					messages.add(new Mensaje(
						new Vector2D(WIDTH/2, HEIGHT/2),
							false,
							text,
							Color.BLACK,
							true,
							Propiedades.fontMed
							));
				}
			};
			break;
		case SUPER_SHELL:
			action = new Action() {
				@Override
				public void doAction() {
					BonusSuperShell superShell = new BonusSuperShell(position, Propiedades.superShell, this, GameState.this);
					superShell.executeAction();
					messages.add(new Mensaje(
							new Vector2D(WIDTH/2, HEIGHT/2),
							false,
							text,
							Color.BLACK,
							true,
							Propiedades.fontMed
							));
				}
			};
			break;
		case POW:
		action = new Action() {
			@Override
			public void doAction() {
				BonusPow pow = new BonusPow(position, Propiedades.pow, this, GameState.this);
				pow.executeAction();
				messages.add(new Mensaje(
						new Vector2D(WIDTH/2, HEIGHT/2),
						false,
						text,
						Color.BLACK,
						true,
						Propiedades.fontMed
						));
			}
		};
		break;
		case NINJA:
		action = new Action() {
			@Override
			public void doAction() {
				BonusNinja ninja = new BonusNinja(position, Propiedades.ninja, this, GameState.this);
				ninja.executeAction();
				messages.add(new Mensaje(
						new Vector2D(WIDTH/2, HEIGHT/2),
						false,
						text,
						Color.BLACK,
						true,
						Propiedades.fontMed
						));
			}
		};
		break;
		case REFUERZO:
		action = new Action() {
			@Override
			public void doAction() {
				BonusRefuerzo bonus_refuerzo = new BonusRefuerzo(position, Propiedades.refuerzo, this, GameState.this);
				bonus_refuerzo.executeAction();
				messages.add(new Mensaje(
						new Vector2D(WIDTH/2, HEIGHT/2),
						false,
						text,
						Color.BLACK,
						true,
						Propiedades.fontMed
						));
			}
		};
		break;
		case LASER:
		action = new Action() {
			@Override
			public void doAction() {
				BonusLaser laser = new BonusLaser(position, Propiedades.laser, this, GameState.this);
				laser.executeAction();
				messages.add(new Mensaje(
						new Vector2D(WIDTH/2, HEIGHT/2),
						false,
						text,
						Color.BLACK,
						true,
						Propiedades.fontMed
						));
			}
		};
		break;
		case ESCOPETA:
		action = new Action() {
			@Override
			public void doAction() {
				BonusEscopeta escopeta = new BonusEscopeta(position, Propiedades.escopeta, this, GameState.this);
				escopeta.executeAction();
				messages.add(new Mensaje(
						new Vector2D(WIDTH/2, HEIGHT/2),
						false,
						text,
						Color.BLACK,
						true,
						Propiedades.fontMed
						));
			}
		};
		break; 
		case AMETRALLADORA:
		action = new Action() {
			@Override
			public void doAction() {
				BonusAmetralladora bonusAmetralladora = new BonusAmetralladora(position, Propiedades.ametralladora, this, GameState.this);
				bonusAmetralladora.executeAction();
				messages.add(new Mensaje(
						new Vector2D(WIDTH/2, HEIGHT/2),
						false,
						text,
						Color.BLACK,
						true,
						Propiedades.fontMed
						));
			}
		};
		break;
	default:
		break;
	}
		this.movingObjects.add(new PowerUp(
				position,
				p.texture,
				action,
				this
				));
	}

	public void spawnItem() {
		// Obtén una posición aleatoria dentro de los límites del juego
		int x = (int) (Math.random() * WIDTH);
		int y = (int) (Math.random() * HEIGHT);
	
		Vector2D position = new Vector2D(x, y);
		Action action = new Action() {
				@Override
				public void doAction() {
					item = new Item(position, Propiedades.item, this, GameState.this);
					//item.executeAction();
					messages.add(new Mensaje(
						new Vector2D(WIDTH/2, HEIGHT/2),
							false,
							"ITEM",
							Color.BLACK,
							true,
							Propiedades.fontMed
							));
				}
			};
		// Agrega el Item a la lista de objetos móviles para que se actualice y dibuje
		this.movingObjects.add(new PowerUp(
				position,
				Propiedades.item,
				action,
				this
				));
	}

	public void update(float dt){

		if(KeyBoard.ATAQUE_ESPECIAL && !ataqueEspecialActivo){
	
			ataqueEspecialActivo = true;
			tiempoAtaqueEspecial = System.currentTimeMillis();
			
			relampago.ejecutarAtaque();
		}

		// Verifica si el ataque especial está activo y controla su duración.
		if (ataqueEspecialActivo) {
			long tiempoActual = System.currentTimeMillis();
			if (tiempoActual - tiempoAtaqueEspecial > 50) {
				// El ataque especial ha terminado.
				ataqueEspecialActivo = false;
			}
		}
		
		// Verificar si los aviones de refuerzo están activos 

        if (tiempoAvionesRefuerzo > 0) {  //BonusRefuerzo.isRefuerzoOn()
            List<AvionRefuerzo> avionesRefuerzo = getAvionesRefuerzo(); 
            for (AvionRefuerzo avionRefuerzo : avionesRefuerzo) { 
                avionRefuerzo.update(dt); 
            } 
            tiempoAvionesRefuerzo -= 0.016; 
        } else { 
            // El tiempo de los aviones de refuerzo ha terminado, elimínalos 
            avionesRefuerzo.clear(); 
        } 

		backgroundY += backgroundSpeed;

		if (backgroundY >= HEIGHT) 
			backgroundY = 0;


		if(gameOver)
			gameOverTimer += dt;
		powerUpSpawner += dt;
		for(int i = 0; i < movingObjects.size(); i++) {
			ObjetoGrafico mo = movingObjects.get(i);
			mo.update(dt);
			if(mo.isDead()) {
				movingObjects.remove(i);
				i--;
			}
		}
		for(int i = 0; i < explosions.size(); i++){
			Animation anim = explosions.get(i);
			anim.update(dt);
			if(!anim.isRunning()){
				explosions.remove(i);
			}	
		}
		 
		if(gameOverTimer > GAME_OVER_TIME) {	
			guardarRanking();
			backgroundMusic.stop();
			State.changeState(new MenuState());
		}
		
		if (!avionesRefuerzo.isEmpty() && player.isDestroy()) {
			for (AvionRefuerzo avionRefuerzo : avionesRefuerzo) {
				avionRefuerzo.Destroy(); // Asumiendo que tienes un método destroy() en la clase AvionRefuerzo
			}
			avionesRefuerzo.clear(); // Limpia la lista de aviones de refuerzo
		}

		if(powerUpSpawner > 10000) {
			spawnPowerUp();
			powerUpSpawner = 0;
		}
		if (ObjetoGrafico.avionesDestruidos >= 20 && ayakoSpawned == false) {
			spawnAyako();
			ayakoSpawned = true;
		}

		for(int i = 0; i < movingObjects.size(); i++)
			if(movingObjects.get(i) instanceof AvionEnemigo)
				return;
		empezarNivel();
	}

	public void guardarRanking(){
		if(!passedLevel2){
		try {
				ArrayList<Ranking.ScoreData> dataList = Ranking.readFile();
				dataList.add(new Ranking.ScoreData(score, nombre));
				ranking.writeJSONFile(archivo, dataList);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XMLStreamException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void draw(Graphics g){	

		g.drawImage(Propiedades.fondo, 0, backgroundY, null);
        g.drawImage(Propiedades.fondo, 0, backgroundY - HEIGHT, null);

		if (ataqueEspecialActivo && relampago != null) {
    	    relampago.draw(g);
    	}

		List<AvionRefuerzo> avionesRefuerzo = getAvionesRefuerzo();
        for (AvionRefuerzo avionRefuerzo : avionesRefuerzo) { 
            avionRefuerzo.draw(g); 
        } 
		
		//para que no se vea tan pixelado el jugador cuando rota
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		for(int i = 0; i < messages.size(); i++) {
			messages.get(i).draw(g2d);
			if(messages.get(i).isDead())
				messages.remove(i);
		}
		for(int i = 0; i < movingObjects.size(); i++)
			movingObjects.get(i).draw(g);
		for(int i = 0; i < explosions.size(); i++){
			Animation anim = explosions.get(i);
			g2d.drawImage(anim.getCurrentFrame(), (int)anim.getPosition().getX(), (int)anim.getPosition().getY(),
					null);
		}
		drawScore(g);
		drawLives(g);
		g.dispose();
	}
	
	protected void drawScore(Graphics g) {
		Vector2D pos = new Vector2D(500, 15);
		String scoreToString = Integer.toString(score);
		g.setColor(Color.WHITE);
		g.setFont(Propiedades.fontMed);
		pos.setY(pos.getY()+25);
		for(int i = 0; i < scoreToString.length(); i++) {
			String digit = scoreToString.substring(i,i+1);
			g.drawString(digit, (int) pos.getX(), (int) pos.getY());
			pos.setX(pos.getX()+20);			
		}
	}
	
	protected void drawLives(Graphics g){
		if (lives < 1)
        	return;
    	Vector2D livePosition = new Vector2D(25, 25);
    	// Dibujar el cartel "Energia"
    	g.setColor(Color.WHITE);
    	g.setFont(new Font("Arial", Font.PLAIN, 20));
    	g.drawString("Energia: ", (int)livePosition.getX() - 15, (int)livePosition.getY() + 40);
    	// Dibujar el número de energia al lado del cartel
    	g.drawString(Integer.toString(lives), (int)livePosition.getX() + 60, (int)livePosition.getY() + 40);
	}
	
	public ArrayList<ObjetoGrafico> getMovingObjects() {
		return movingObjects;
	}
	
	public ArrayList<Mensaje> getMessages() {
		return messages;
	}
	
	public AvionP38 getPlayer() {
		return player;
	}

	public int getLives(){
		return lives;
	}

	public static int getScore(){
		System.out.println("PUNTOS"+ score);
		return score;
	}

	public Item getItem(){
		return item;
	}

	public void addLife(int cant){
		lives += cant;
	}
	
	public void incrementarRojosDestruidos() {
		rojosDestruidos++;
	}

	public void reiniciarRojosDestruidos() {
		rojosDestruidos = 0;
	}
	
	public int getRojosDestruidos() {
		return rojosDestruidos;
	}

	public List<AvionRefuerzo> getAvionesRefuerzo(){
		return avionesRefuerzo;
	}

	public void agregarAvionRefuerzo(AvionRefuerzo avionRefuerzo) {
		avionesRefuerzo.add(avionRefuerzo);
	}	

	public boolean subtractLife(Vector2D position) {
		lives --;
		Mensaje lifeLostMesg = new Mensaje(
				position,
				false,
				"--ENERGIA",
				Color.RED,
				true,
				Propiedades.fontMed
				);
		messages.add(lifeLostMesg);
		return lives > 0;
	}
	
	public void gameOver() {
		Mensaje gameOverMsg = new Mensaje(
				PLAYER_START_POSITION,
				true,
				"FIN DEL JUEGO",
				Color.WHITE,
				true,
				Propiedades.fontBig);
		this.messages.add(gameOverMsg);
		gameOver = true;
	}

	public void finNivel(){
		fin_nivel = true;
		backgroundMusic.stop();
		int puntajeActual = getScore(); // Obtiene el puntaje actual de GameState
		MenuNivel2.setScore(puntajeActual); // Establece el puntaje en MenuNivel2
		State.changeState(new MenuNivel2());
	}
	
	public boolean isGameOver() {
        return gameOver;
    }

	public static void pausarSonidoFondo() {
		System.out.println("background pausar"+backgroundMusic);
        backgroundMusic.stop(); 
    }

    public static void reanudarSonidoFondo() {
		backgroundMusic.play();
    }

	public static void set_sonido(boolean sonido){
		sonido_activado = sonido;
	}
}