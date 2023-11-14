package states;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.xml.stream.XMLStreamException;
import Menu.Jugador;
import gameObjects.*;
import gameObjects.PowerUp.PowerUpTypes;
import graphics.*;
import input.KeyBoard;
import io.Ranking;
import math.Vector2D;
import ui.Action;

public class Nivel2 extends GameState{

	public static final int WIDTH = 600;//Ancho de la ventana
	public static final int HEIGHT = 700;//largo de la ventana

	public static final double PLAYER_MAX_VEL = 7.0;
	public static final long GAME_OVER_TIME = 3000;

	// Propiedades Barco Enemigo
	public static final int BARCO_MAX_VEL = 1;	
	public static final long BARCO_SPAWN_RATE = 10000;


	// Propiedades del enemigo
	public static final double ENEMIGO_INIT_VEL = 2.0;
	public static final int ENEMIGO_SCORE = 20;
	public static final double ENEMIGO_MAX_VEL = 6.0;
    public static final int ENEMIGO_WIDTH = 50;
    public static final int ENEMIGO_HEIGHT = 50;
    public static final long ENEMIGO_SPAWN_INTERVAL = 100;
    public static final int ENEMIGO_SPAWN_COUNT = 10;

	public static final Vector2D PLAYER_START_POSITION = new Vector2D(WIDTH/2 - Propiedades.player.getWidth()/2, HEIGHT/2 - Propiedades.player.getHeight()/2);
	
	private AvionP38 player;
	
	private ArrayList<ObjetoGrafico> movingObjects = new ArrayList<ObjetoGrafico>();// dibujar todos lo objetos que se muevan en el juego
	//para que player pueda acceder a array tiene que tener una instancia de game state
	private ArrayList<Animation> explosions = new ArrayList<Animation>();
	private ArrayList<Mensaje> messages = new ArrayList<Mensaje>();
	
	//para escribir en el archivo json
	File archivo = new File("/Users/Luly/OneDrive/Documentos/ProyectoFinal/data.json");
	
	private int enemigos;
	private boolean nivelIniciado = false;
	
	private Sound backgroundMusic;
	private long gameOverTimer;
	private boolean gameOver, fin_nivel;
	
	private long powerUpSpawner;
	private Tsunami tsunami;
	private boolean ataqueEspecialActivo = false;
	private long tiempoAtaqueEspecial =0;
	private boolean detenerDesplazamiento = false;
	private long tiempoInicioDetencion;

	private boolean yamatoSpawned = false;

	Ranking ranking = new Ranking();

	Jugador jugador;
	
	public Nivel2(int gameStateScore){
		//this se refiere a esta clase
		player = new AvionP38(PLAYER_START_POSITION, new Vector2D(),
				PLAYER_MAX_VEL, Propiedades.player, this);	
		gameOver = false;
		movingObjects.add(player);
		enemigos = 1;
		empezarNivel();
		backgroundMusic = new Sound(Propiedades.backgroundMusic);
		backgroundMusic.loop();
		backgroundMusic.changeVolume(-10.0f);
		gameOverTimer = 0;
		powerUpSpawner = 0;
		yamatoSpawned = false;
		gameOver = false;
		fin_nivel = false;
		lives = 10;
		tsunami = new Tsunami(Propiedades.tsunami, this);
	}
	
	
	private void empezarNivel() {	
		if (nivelIniciado == false) {
			messages.add(new Mensaje(new Vector2D(WIDTH / 2, HEIGHT / 2), false,
					"NIVEL 2", Color.WHITE, true, Propiedades.fontBig));
			nivelIniciado = true;
		}
		double x, y;
    double direction = 0.0; // Variable para alternar direcciones
    
    for (int i = 0; i < enemigos; i++) {
        double randomDirection = direction; // Usa la dirección actual
        direction += Math.PI / 4; // Incrementa la dirección para el siguiente barco
        
        if (i % 3 == 0) {
            x = (int) (Math.random() * WIDTH);
            y = -ENEMIGO_HEIGHT;
            movingObjects.add(new BarcoChico(
                new Vector2D(x, y),
                new Vector2D(Math.cos(randomDirection), Math.sin(randomDirection)), // Dirección asignada
                ENEMIGO_INIT_VEL * Math.random() + 1,
                Propiedades.barco_enemigo,
                this
            ));
        } else if (i % 3 == 1) {
            x = Math.random() * (WIDTH / 2 - ENEMIGO_WIDTH);
            y = Math.random() * HEIGHT;
            movingObjects.add(new BarcoChico(
                new Vector2D(x, y),
                new Vector2D(Math.cos(randomDirection), Math.sin(randomDirection)), // Dirección asignada
                ENEMIGO_INIT_VEL * Math.random() + 1,
                Propiedades.barco_enemigo,
                this
            ));
        }
    }
    enemigos++;
	}
	
	private void spawnYamato(){
		double x = Math.random() * WIDTH;
		double y = -HEIGHT;
		ArrayList<Vector2D> path = new ArrayList<Vector2D>();
		path.add(new Vector2D(0, 0));
		path.add(new Vector2D(0, HEIGHT));
		movingObjects.add(new Yamato(
				new Vector2D(x, y),
				new Vector2D(),
				BARCO_MAX_VEL,
				Propiedades.yamato,
				path,
				this
				));
	}

	public void update(float dt){
		passedLevel2 = true;

		if(KeyBoard.ATAQUE_ESPECIAL && !ataqueEspecialActivo){
	
			ataqueEspecialActivo = true;
			tiempoAtaqueEspecial = System.currentTimeMillis();
			
			tsunami.ejecutarAtaque();
		}

		// Verifica si el ataque especial está activo y controla su duración.
		if (ataqueEspecialActivo) {
			long tiempoActual = System.currentTimeMillis();
			if (tiempoActual - tiempoAtaqueEspecial > 50) {
				// El ataque especial ha terminado.
				ataqueEspecialActivo = false;
			}
			detenerDesplazamiento = true;
    		tiempoInicioDetencion = System.currentTimeMillis();
		}

		if (detenerDesplazamiento) {
			long tiempoActual = System.currentTimeMillis();
			if (tiempoActual - tiempoInicioDetencion > 3000) {
				detenerDesplazamiento = false;
			}
		}
		
		if (!detenerDesplazamiento) {
			backgroundY += backgroundSpeed;

			if (backgroundY >= HEIGHT) 
				backgroundY = 0;
		}

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
			guardarRankingNivel2();
			backgroundMusic.stop();
			State.changeState(new MenuState());
		}
		if(fin_nivel == true){
			guardarRankingNivel2();
		}

		/* 
		if(fin_nivel==true && ayako.isDestroy()){
			backgroundMusic.stop();
			State.changeState(new MenuNivel2());
		}
		*/
		
		if(powerUpSpawner > 2000) {
			spawnPowerUp();
			powerUpSpawner = 0;
		}
		if (ObjetoGrafico.barcosDestruidos >= 5 && yamatoSpawned == false) {
			spawnYamato();
			yamatoSpawned = true;
		}
		for(int i = 0; i < movingObjects.size(); i++)
			if(movingObjects.get(i) instanceof BarcoChico)
				return;
		empezarNivel();
	}

	public void guardarRankingNivel2(){
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

	public void spawnPowerUp() {
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
					BonusAuto auto = new BonusAuto(position, Propiedades.auto, this, Nivel2.this);
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
					BonusSuperShell superShell = new BonusSuperShell(position, Propiedades.superShell, this, Nivel2.this);
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
				BonusPow pow = new BonusPow(position, Propiedades.pow, this, Nivel2.this);
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
				BonusNinja ninja = new BonusNinja(position, Propiedades.ninja, this, Nivel2.this);
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
		case LASER:
		action = new Action() {
			@Override
			public void doAction() {
				BonusLaser laser = new BonusLaser(position, Propiedades.laser, this, Nivel2.this);
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
				BonusEscopeta escopeta = new BonusEscopeta(position, Propiedades.escopeta, this, Nivel2.this);
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
				BonusAmetralladora bonusAmetralladora = new BonusAmetralladora(position, Propiedades.ametralladora, this, Nivel2.this);
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
	public void draw(Graphics g){	

		if (ataqueEspecialActivo && tsunami != null) {
    	    tsunami.draw(g);
    	} else{
			g.drawImage(Propiedades.fondo2, 0, backgroundY, null);
        	g.drawImage(Propiedades.fondo2, 0, backgroundY - HEIGHT, null);
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

	
	@Override
	public void playExplosion(Vector2D position){
		explosions.add(new Animation(
				Propiedades.exp,
				50,
				position.subtract(new Vector2D(Propiedades.exp[0].getWidth()/2, Propiedades.exp[0].getHeight()/2))
				));
	}

	public void addScore(int value, Vector2D position) {	
		Color c = Color.WHITE;
		String text = "+" + value + " puntos";
		score += value;
		messages.add(new Mensaje(position, true, text, c, true, Propiedades.fontMed));
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
				"YAMATO ELIMINADO",
				Color.WHITE,
				true,
				Propiedades.fontBig);
		this.messages.add(gameOverMsg);
		gameOver = true;
		super.gameOver();
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

	public void addLife(int cant){
		lives += cant;
	}

	
	public static void setScore(int newScore) {
        score = newScore;
    }

    public int getScore() {
        return score;
    }

	public static boolean hasPassedLevel2(){
		return passedLevel2;
	}

	public void finNivel2(){
		fin_nivel = true;
		backgroundMusic.stop();
		State.changeState(new MenuState());
	}

	public boolean isGameOver() {
        return gameOver;
    }
}