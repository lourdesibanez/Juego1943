package states;

import java.awt.Graphics;

public abstract class State {
	
	private static State currentState = null;
	protected static String nombre;

	private static long startTime;
	private static int secondsElapsed;

	
	public static State getCurrentState() {return currentState;}
	public static void changeState(State newState) {
		currentState = newState;
	}
	public static void changeName(String n) {
		nombre = n;
	}
		
	public abstract void update(float dt);
	public abstract void draw(Graphics g);
	
	public static int getSecondsElapsed(){
		long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        secondsElapsed = (int) (elapsedTime / 1000);
        return secondsElapsed;
	}
	
}
