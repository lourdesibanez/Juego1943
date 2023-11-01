package graphics;
//almacenar recursos externos
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;

public class Propiedades {
	public static boolean loaded = false;
	public static float count = 0;
	public static float MAX_COUNT = 57;

	//imagenes de los distintos juegos que tenemos
	public static BufferedImage batalla_midway, pacman, tetris;
	
	//jugador avion
	public static BufferedImage player, avion_refuerzo;
	//fondo
	public static BufferedImage fondo;
	public static BufferedImage fondo2;

	//titulo
	public static BufferedImage titulo;

	//efecto del avion
	public static BufferedImage speed;

	// explosion
	public static BufferedImage[] exp = new BufferedImage[9];
	
	// municiones
	public static BufferedImage laser2, municion_ayako;

	//enemigos
	public static BufferedImage enemigo_negro;
	public static BufferedImage enemigo_v_chico;
	public static BufferedImage enemigo_rojo;
	public static BufferedImage ayako;
	public static BufferedImage yamato;
	public static BufferedImage barco_enemigo;

	
	// numbers
	public static BufferedImage[] numbers = new BufferedImage[11];
	
	//fuentes
	public static Font fontBig;
	public static Font fontMed;
	
	//sonido
	public static Clip backgroundMusic, explosion, playerLoose, playerShoot, enemigoShoot, powerUp;
	
	// power ups
	public static BufferedImage superShell, ninja, auto, pow, item, refuerzo, escopeta, ametralladora, laser;

	//ataques especiales
	public static BufferedImage relampago, tsunami;
	
	public static void init()//un metodo static. se llama solo cuando el juego se arranque
	{
		tsunami = loadImage("/AtaquesEspeciales/tsunami.png");
		relampago = loadImage("/AtaquesEspeciales/relampago.png");
		
		fondo = loadImage("/fondo.jpeg");
		fondo2 = loadImage("/fondo2.jpg");

		titulo = loadImage("/titulo.png");
		
		player = loadImage("/ships/player1943(1).png");

		avion_refuerzo = loadImage("/ships/refuerzos.png");

		enemigo_negro = loadImage("/ships/enemigo-negro.png");
		enemigo_v_chico = loadImage("/ships/enemigo-verde-chico.png");
		enemigo_rojo = loadImage("/ships/enemigo-rojo.png");
		ayako = loadImage("/ships/ayako.png");
		yamato = loadImage("/ships/yamato.png");
		barco_enemigo = loadImage("/ships/barco_enemigo.png");

		speed = loadImage("/effects/fire08.png");
		
		laser2 = loadImage("/lasers/laser2.png");
		
		municion_ayako = loadImage("/lasers/municion-ayako.png");
		
		ayako = loadImage("/ships/ayako.png");
		
		fontBig = loadFont("/fonts/PressStart2P.ttf", 42);
		
		fontMed = loadFont("/fonts/PressStart2P.ttf", 20);
		
		for(int i = 0; i < exp.length; i++)
			exp[i] = loadImage("/explosion/"+i+".png");
		
		for(int i = 0; i < numbers.length; i++)
			numbers[i] = loadImage("/numbers/"+i+".png");
		
		backgroundMusic = loadSound("/sounds/backgroundMusic.wav");
		explosion = loadSound("/sounds/explosion.wav");
		playerLoose = loadSound("/sounds/playerLoose.wav");
		playerShoot = loadSound("/sounds/playerShoot.wav");
		enemigoShoot = loadSound("/sounds/enemigoShoot.wav");
		powerUp = loadSound("/sounds/pow.wav");
	
		

		superShell = loadImage("/powers/doubleScore.png");
		ninja = loadImage("/powers/ninja.png");
		auto = loadImage("/powers/auto.png");
		pow = loadImage("/powers/pow.png");
		item = loadImage("/powers/item.png");
		refuerzo = loadImage("/powers/pow-refuerzo.png");
		laser = loadImage("/powers/laser1.png");
		escopeta = loadImage("/powers/escopeta.png");
		ametralladora = loadImage("/powers/ametralladora.png");
		// ===========================================================
		
		loaded = true;
		
	}
/* 
	public static BufferedImage loadImage(String path) {
		count ++;
		return Loader.ImageLoader(path);
	}
*/

	public static BufferedImage loadImage(String path) {
    	count++;
    	BufferedImage image = Loader.ImageLoader(path);
    	if (image == null) {
        	throw new RuntimeException("No se pudo cargar la imagen: " + path);
    }
    	return image;
	}
	public static Font loadFont(String path, int size) {
		count ++;
		Font font = Loader.loadFont(path, size);
        if (font == null) {
            throw new RuntimeException("No se pudo cargar la fuente: " + path);
        }
        return font;
	}
	public static Clip loadSound(String path) {
		count ++;
		Clip clip = Loader.loadSound(path);
        if (clip == null) {
            throw new RuntimeException("No se pudo cargar el sonido: " + path);
        }
        return clip;
	}
	
}
