package states;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

import javax.xml.stream.XMLStreamException;

import graphics.Propiedades;
import graphics.Text;
import io.Ranking;
import math.Vector2D;
import ui.Action;
import ui.Button;

public class ScoreState extends State{

	public static final int WIDTH = 600;//Ancho de la ventana
	public static final int HEIGHT = 700;//largo de la ventana

	//Menu	
	public static final String RETURN = "VOLVER";	
	public static final String SCORE = "PUNTAJE";
	public static final String DATE = "FECHA";
	public static final String NOMBRE = "JUGADOR";

	private Button returnButton;
	
	private PriorityQueue<Ranking.ScoreData> highScores;
	
	private Comparator<Ranking.ScoreData> scoreComparator;
	
	private Ranking.ScoreData[] auxArray;
	
	public ScoreState() {

		returnButton = new Button(
				WIDTH / 2 - 100,
				HEIGHT - 100,
				RETURN,
				new Action() {
					@Override
					public void doAction() {
						State.changeState(new MenuState());
					}
				}
			);
		
		scoreComparator = new Comparator<Ranking.ScoreData>() {
			@Override
			public int compare(Ranking.ScoreData e1, Ranking.ScoreData e2) {
				return e1.getScore() < e2.getScore() ? -1: e1.getScore() > e2.getScore() ? 1: 0;
			}
		};
		
		highScores = new PriorityQueue<Ranking.ScoreData>(10, scoreComparator);
		
		try {
			ArrayList<Ranking.ScoreData> dataList = Ranking.readFile();
			
			for(Ranking.ScoreData d: dataList) {
				highScores.add(d);
			}
			
			while(highScores.size() > 10) {
				highScores.poll();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(float dt) {
		returnButton.update();
	}

	@Override
	public void draw(Graphics g) {

		g.setColor(Color.BLUE); 
    	g.fillRect(0, 0, WIDTH, HEIGHT); 
		
		returnButton.draw(g);
		
		auxArray = highScores.toArray(new Ranking.ScoreData[highScores.size()]);
		
		Arrays.sort(auxArray, scoreComparator);
		
		
		Vector2D scorePos = new Vector2D(
				WIDTH / 2 - 18,
				100
				);
		Vector2D datePos = new Vector2D(
				WIDTH / 2 + 180,
				100
				);
		Vector2D namePos = new Vector2D(
				WIDTH / 2 - 180,
				100
				);
		
		Text.drawText(g, SCORE, scorePos, true, Color.BLACK, Propiedades.fontMed);
		Text.drawText(g, DATE, datePos, true, Color.BLACK, Propiedades.fontMed);
		Text.drawText(g, NOMBRE, namePos, true, Color.BLACK, Propiedades.fontMed);
		
		scorePos.setY(scorePos.getY() + 40);
		datePos.setY(datePos.getY() + 40);
		namePos.setY(namePos.getY()+ 40);
		
		for(int i = auxArray.length - 1; i > -1; i--) {
			
			Ranking.ScoreData d = auxArray[i];
			
			Text.drawText(g, d.getName(), namePos, true, Color.WHITE, Propiedades.fontMed);
			Text.drawText(g, Integer.toString(d.getScore()), scorePos, true, Color.WHITE, Propiedades.fontMed);
			Text.drawText(g, d.getDate(), datePos, true, Color.WHITE, Propiedades.fontMed);
			
			
			scorePos.setY(scorePos.getY() + 40);
			datePos.setY(datePos.getY() + 40);
			namePos.setY(namePos.getY()+ 40);
			
		}
		
	}
	
}
