package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import graphics.Propiedades;
import graphics.Text;
import input.MouseInput;
import math.Vector2D;

public class Button {

	//private boolean mouseIn;
	private Rectangle boundingBox;
	private Action action;
	private String text;
	
	public Button(
			int x, int y,
			String text,
			Action action
			) {
		this.text = text;
		boundingBox = new Rectangle(x, y, 200, 50);
		this.action = action;
	}
	
	public void update() {
		if (boundingBox.contains(MouseInput.X, MouseInput.Y) && MouseInput.MLB) {
			action.doAction();
		}
	}
	
	public void draw(Graphics g) {
		Text.drawText(
				g,
				text,
				new Vector2D(
						boundingBox.getX() + boundingBox.getWidth() / 2,
						boundingBox.getY() + boundingBox.getHeight()),
				true,
				Color.BLACK,
				Propiedades.fontMed);
		
	}
	
}
