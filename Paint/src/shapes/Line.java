package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Line extends Shape {

	
	private Point[] vertices = new Point[2];
	protected Rectangle2D[] cursors = new Rectangle2D[2];
	public Line (){
		
	}
	public Line(Color fillColor,BasicStroke thickness, Color LayoutColor, Point[] dim) {
		pointsNum = 2;
		for (int i = 0; i < 2; i++) {
			if (dim!=null){
			vertices[i] = new Point(dim[i]);
			}else{
				vertices[i] = new Point(0,0);	
			}
		}
		for (int i = 0; i < 2; i++) {
			cursors [i] = new Rectangle2D.Double(0, 0,
                    10,
                    10);
		}
		moveCursors();
		this.setLayoutClr(LayoutColor);
		this.setFillClr(fillColor);
		this.setThickness(thickness);
	}

	@Override
	public void drawShape(Graphics2D g) {
		g.setColor(layoutClr);
		g.setStroke(thickness);
		if (vertices[0].x!=0 && vertices[0].y!=0 && vertices[1].x!=0 && vertices[1].y!=0){
		g.drawLine(vertices[0].x, vertices[0].y, vertices[1].x, vertices[1].y);
		}
	}

	@Override
	public void updateVertices(ArrayList<Point> points, int ind1, int ind2) {
		vertices[0].x = points.get(ind1).x;
		vertices[0].y = points.get(ind1).y;
		vertices[1].x = points.get(ind2).x;
		vertices[1].y = points.get(ind2).y;
		moveCursors();

	}

	@Override
	public boolean contains(int x, int y) {
		// calculate delta
		double deltaX = Math.abs(vertices[0].x - vertices[1].x);
		double deltaY = Math.abs(vertices[0].y - vertices[1].y);
		// calculate sub delta
		double deltaX1 = Math.abs(x - vertices[0].x);
		double deltaY1 = Math.abs(y - vertices[0].y);
		double deltaX2 = Math.abs(x - vertices[1].x);
		double deltaY2 = Math.abs(y - vertices[1].y);	
		// calculate total length & sub lengths
		double len  = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		double len1 = Math.sqrt(deltaX1 * deltaX1 + deltaY1 * deltaY1);
		double len2 = Math.sqrt(deltaX2 * deltaX2 + deltaY2 * deltaY2);
		if(Math.abs(len - (len1 + len2)) <= 5) {
			return true;
		}
		return false;
	}

	@Override
	public int getCursor(Point position) {
		int posDrag = -1;
		for (int i = 0; i < 2; i++) {
			if (cursors[i].contains(position)) {
				posDrag = i;
				break;
			}
		}
		return posDrag;
	
	}

	@Override
	public void drawCursors(Graphics2D g) {
		Color temp = g.getColor();
		g.setColor(Color.WHITE);
		for (int i = 0; i < 2; i++) {
			g.setColor(Color.black);
			g.draw(cursors[i]);
			g.setColor(Color.WHITE);
			g.fill(cursors[i]);
		}
		g.setColor(temp);
	}

	@Override
	public void resizeShape(int resizeMode, Point position) {
		// TODO Auto-generated method stub
		switch (resizeMode) {
		case CN: {
			vertices[0].x = position.x;
			vertices[0].y =position.y;
			
			moveCursors();
			break;
		}

		case CS: {
			vertices[1].x = position.x;
			vertices[1].y =position.y;
			moveCursors();
			break;
		}
	  }
	}

	@Override
	public void moveCursors() {
		
		cursors[CN] = new Rectangle2D.Double(vertices[0].x - 5, vertices[0].y - 5, 10, 10);
		cursors[CS] = new Rectangle2D.Double(vertices[1].x - 5, vertices[1].y - 5, 10, 10);
	}

	@Override
	public Point[] getVertices() {
		// TODO Auto-generated method stub
		return vertices;
	}

	@Override
	public Color getLayoutClr() {
		// TODO Auto-generated method stub
		return layoutClr;
	}

	@Override
	public Color getFillClr() {
		// TODO Auto-generated method stub
		return fillClr;
	}

	@Override
	public BasicStroke getThickness() {
		// TODO Auto-generated method stub
		return thickness;
	}

	@Override
	public void move(int deltaX, int deltaY) {
		// TODO Auto-generated method stub
				for (int i = 0; i < pointsNum; i++) {
					vertices[i].x += deltaX;
					vertices[i].y += deltaY;
				}
		moveCursors();
	}
}
