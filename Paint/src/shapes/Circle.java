package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Circle extends Oval {

	protected int diameter = 0;
	public Circle(){
		super();
	}
	public Circle(Color fillColor, BasicStroke thickness, Color LayoutColor, Point[] dim) {
		super(fillColor, thickness, LayoutColor, dim);
		diameter = Math.min(major, minor);
		major=diameter;
		minor=diameter;
		vertices[1].x = vertices[0].x+major;
		vertices[1].y = vertices[0].y+minor;
		moveCursors();
		
	}

@Override
	
	public void updateVertices(ArrayList<Point> points, int ind1, int ind2) {
		
		super.updateVertices(points, ind1, ind2);
		diameter = Math.min(major, minor);
		major=diameter;
		minor=diameter;
		vertices[0].x = start.x + g1 * major;
		vertices[0].y = start.y + g2 * minor;
		vertices[1].x = vertices[0].x+major;
		vertices[1].y = vertices[0].y+minor;
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
			minor = vertices[1].y - vertices[0].y;
			major=minor;
			vertices[0].x=vertices[1].x-major;
			moveCursors();
			break;
		}

		case CS: {
			vertices[1].y = position.y;
			minor = vertices[1].y - vertices[0].y;
			major=minor;
			vertices[1].x=vertices[0].x+major;
			
			moveCursors();
			break;
		}

		case CW: {
			vertices[0].x = position.x;
			major = vertices[1].x - vertices[0].x;
			minor=major;
			vertices[0].y=vertices[1].y-minor;
			moveCursors();
			break;
		}

		case CE: {
			vertices[1].x = position.x;
			major = vertices[1].x - vertices[0].x;
			minor=major;
			vertices[1].y=vertices[0].y+minor;
			moveCursors();
			break;
		}
		}
	}
}
