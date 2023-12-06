package main;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.util.Properties;

import javax.swing.JFrame;

import graphics.Propiedades;
import input.KeyBoard;
import input.MouseInput;
import states.*;
import Menu.Juego;
import Menu.Jugador;

public class Juego1943 extends Juego implements Runnable {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 700;

    private Canvas canvas;
    private Thread thread;
    private boolean running = false;

    private BufferStrategy bs;
    private Graphics g;

    private final int FPS = 60;
    private final double TARGETTIME = 1000000000.0 / FPS;
    private double delta = 0;
    private int averageFPS = FPS;

    private KeyBoard keyBoard;
    private MouseInput mouseInput;

    private JFrame frame;
    private boolean pausado = false, sonido_activado = true;

    public Juego1943() { //le debemos mandar el jugador
        canvas = new Canvas();
        keyBoard = new KeyBoard();
        mouseInput = new MouseInput();

        canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        canvas.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        canvas.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        canvas.setFocusable(true);

        canvas.addKeyListener(keyBoard);
        canvas.addMouseListener(mouseInput);
        canvas.addMouseMotionListener(mouseInput);

        frame = new JFrame("1943: Batalla de Midway");
    }

    public void cargarConfiguracion() {
        try {
            Properties propiedades = new Properties();

            // Abre el archivo de entrada para cargar los valores
            InputStream archivoEntrada = new FileInputStream("jgame.properties");

            // Carga los valores desde el archivo
            propiedades.load(archivoEntrada);

            // Cierra el archivo
            archivoEntrada.close();
           System.out.println("CONFIG: " + propiedades);
            // Obtén los valores cargados del archivo .properties
            boolean pantallaCompleta = Boolean.parseBoolean(propiedades.getProperty("pantallaCompleta"));
            boolean sonidoActivado = Boolean.parseBoolean(propiedades.getProperty("sonidoActivado"));
            String avionSeleccionado = propiedades.getProperty("avionSeleccionado");
            int volumen = Integer.parseInt(propiedades.getProperty("volumen"));
            String pistaMusicalSeleccionada = propiedades.getProperty("pistaMusicalSeleccionada");
            int pause = Integer.parseInt(propiedades.getProperty("teclaPausa"));
            int shoot = Integer.parseInt(propiedades.getProperty("teclaDisparo"));
            int ataque_especial = Integer.parseInt(propiedades.getProperty("teclaAtaqueEspecial"));

            System.out.println(propiedades);
            this.setPantallaCompleta(pantallaCompleta);
            //this.setSonidoActivado(sonidoActivado);
            Propiedades.set_plane(avionSeleccionado);
            keyBoard.cargar_teclas(pause, shoot, ataque_especial);
            Nivel1.set_sonido(sonidoActivado);
           

            /*
            juego1943.setAvionSeleccionado(avionSeleccionado);
            juego1943.setVolumen(volumen);
            juego1943.setPistaMusicalSeleccionada(pistaMusicalSeleccionada);
            */

        } catch (IOException e) {
            e.printStackTrace();
            // Si ocurre algún error al cargar el archivo o leer los valores, puedes establecer valores por defecto o manejar la situación según lo desees.
        }
    }

    public void setJugador(Jugador jugador) {
        super.jugador = jugador;
    }

    public void pausarJuego(){
        pausado = !pausado;
        Nivel1.pausarSonidoFondo();
    }
    

    private void update(float dt) {
        keyBoard.update();
        if (KeyBoard.getSpacePressCount()% 2 == 1) {
            pausarJuego();
        }else{
            State.getCurrentState().update(dt);
        }
    }

    private void draw() {
        bs = canvas.getBufferStrategy();

        if (bs == null) {
            canvas.createBufferStrategy(3);
            return;
        }

        g = bs.getDrawGraphics();
        State.getCurrentState().draw(g);
        
        if(pausado){
            g.setColor(Color.WHITE);
            g.setFont(Propiedades.fontBig);
            g.drawString("PAUSA", WIDTH / 2-100, HEIGHT / 2 );
        }

            if (State.getCurrentState() instanceof Nivel1 || State.getCurrentState() instanceof Nivel2) {
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Arial", Font.PLAIN, 20));
                    g.drawString(MenuState.getSecondsElapsed() + "seg", 10, 40);
                
            }  
        g.dispose();
        bs.show();
    }

    private void init() {
        Thread loadingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Propiedades.init();
            }
        });
        State.changeName(jugador.getNombreUsuario());
        State.changeState(new LoadingState(loadingThread));
    }

    public void start() {
        cargarConfiguracion();
        init();
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    /*
    public void setSonidoActivado(boolean sonidoActivado){
        if(!sonidoActivado){
            sonido_juego.stop();
        }
    }
    */

    public void setSonidoActivado(boolean sonidoActivado){
        sonido_activado = sonidoActivado; 
    }

    public void setPantallaCompleta(boolean pantallaCompleta) {
        // JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(canvas);
        frame.dispose();

        if (pantallaCompleta) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Establece la ventana en pantalla completa
            // canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // Ajusta el tamaño del canvas para que coincida con el tamaño de la ventana
            // frame.pack(); // Hace que el JFrame ajuste su tamaño para que todos sus componentes se ajusten correctamente.

            frame.add(canvas);
            // frame.setLayout(null);
            int canvasX = (frame.getWidth() - canvas.getWidth()) / 2;
            int canvasY = (frame.getHeight() - canvas.getHeight()) / 2;
            canvas.setLocation(canvasX, canvasY);

            frame.setVisible(true);
        } else {
            frame.setUndecorated(false); // Vuelve a agregar los bordes y decoraciones a la ventana
            frame.setSize(WIDTH, HEIGHT); // Restablece el tamaño original de la ventana
            frame.add(canvas);
        }
    }

    private void stop() {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // setPantallaCompleta(pantallaCompleta, frame);
        frame.setVisible(true);
        long now;
        long lastTime = System.nanoTime();
        int frames = 0;
        long time = 0;
        long startTime = System.currentTimeMillis();  // Se establece el tiempo inicial
        int secondsElapsed = 0;  // Se establece el tiempo transcurrido a cero

        while (running) {
            now = System.nanoTime();
            delta += (now - lastTime) / TARGETTIME;
            time += now - lastTime;
            lastTime = now;

            if (delta >= 1) {
                update((float) (delta * TARGETTIME * 0.000001f));
                draw();
                delta--;
                frames++;
            }

            
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            secondsElapsed = (int) (elapsedTime / 1000);
            
            //para que si pasa mas de x cantidad de tiempo el juego finalice solo
            if (secondsElapsed >= 200) {
                secondsElapsed = 0;
                startTime = currentTime;
                Propiedades.backgroundMusic.stop();
                State.changeState(new MenuState());
            }
            
            if (time >= 1000000000) {
                averageFPS = frames;
                frames = 0;
                time = 0;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        frame.dispose();
    }   
    
    public boolean getPausado(){
        return pausado;
    }
}
