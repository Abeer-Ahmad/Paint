package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Square extends Rectangle {

	protected int length = 0;
 
	public Square(){
		super();
	}
	public Square(Color fillColor, BasicStroke thickness, Color LayoutColor, Point[] dim) {
		super(fillColor, thickness, LayoutColor, dim);
		length = Math.min(width, height);
		width=length;
		height=length;
		vertices[1].x = vertices[0].x+width;
		vertices[1].y = vertices[0].y+height;
		moveCursors();
		
	}
	
	@Override
	public void updateVertices(ArrayList<Point> points, int ind1, int ind2) {
		
		super.updateVertices(points, ind1, ind2);
		length = Math.min(width, height);
		width=length;
		height=length;
		vertices[0].x = start.x + g1 * width;
		vertices[0].y = start.y + g2 * height;
		vertices[1].x = vertices[0].x+width;
		vertices[1].y = vertices[0].y+height;
		moveCursors();
	}
	@Override
	public void drawShape(Graphics2D g) {
		// TODO Auto-generated method stub
		
		g.setColor(layoutClr);
		g.setStroke(thickness);
		super.drawShape(g);
	}
	@Override
	public void resizeShape(int resizeMode, Point position) {
		
		// TODO Auto-generated method stub
	
		switch (resizeMode) {
		case CN: {
			vertices[0].y =position.y;
			height = vertices[1].y - vertices[0].y;
			width=height;
			vertices[0].x=vertices[1].x-width;
			moveCursors();
			break;
		}

		case CS: {
			vertices[1].y = position.y;
			height = vertices[1].y - vertices[0].y;
			width=height;
			vertices[1].x=vertices[0].x+width;
			
			moveCursors();
			break;
		}

		case CW: {
			vertices[0].x = position.x;
			width = vertices[1].x - vertices[0].x;
			height=width;
			vertices[0].y=vertices[1].y-height;
			moveCursors();
			break;
		}

		case CE: {
			vertices[1].x = position.x;
			width = vertices[1].x - vertices[0].x;
			height=width;
			vertices[1].y=vertices[0].y+height;
			moveCursors();
			break;
		}
		}
	}

}
