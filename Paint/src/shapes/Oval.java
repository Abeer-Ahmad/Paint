package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Oval extends Shape {

	protected Point[] vertices = new Point[2];
	protected Point start = new Point();
	protected int major = 0;
	protected int minor = 0;
	protected int g1, g2;
	protected Rectangle2D[] cursors = new Rectangle2D[4];
     
	public Oval(){
		
	}
	public Oval(Color fillColor,BasicStroke thickness, Color LayoutColor, Point[] dim) {
		pointsNum = 2;
		for (int i = 0; i < 2; i++) {
			if (dim!=null){
			vertices[i] = new Point(dim[i]);
			}else{
				vertices[i] = new Point(0,0);
			}
		}
		major=vertices[1].x-vertices[0].x;
		minor=vertices[1].y-vertices[0].y;
		for (int i = 0; i < 4; i++) {
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
	public void updateVertices(ArrayList<Point> points, int ind1, int ind2) {
		// TODO Auto-generated method stub
		g1 = (points.get(ind1).x > points.get(ind2).x ? -1 : 0);
		g2 = (points.get(ind1).y > points.get(ind2).y ? -1 : 0);
		start = new Point(points.get(ind1));
		major = Math.abs(points.get(ind1).x - points.get(ind2).x);
		minor = Math.abs(points.get(ind1).y - points.get(ind2).y);
		vertices[0].x = start.x + g1 * major;
		vertices[0].y = start.y + g2 * minor;
		vertices[1].x = vertices[0].x + major;
		vertices[1].y = vertices[0].y + minor;
		moveCursors();
	}

	@Override
	public void drawShape(Graphics2D g) {
		g.setColor(layoutClr);
		g.setStroke(thickness);
		if (vertices[0].x!=0 && vertices[0].y!=0 && major!=0 && minor!=0){
		g.drawOval(vertices[0].x, vertices[0].y, major, minor);
		g.setColor(fillClr);
		g.fillOval(vertices[0].x, vertices[0].y, major, minor);
		}
		
	}

	@Override
	public boolean contains(int x, int y) {
		double h, k, term1, term2, term3;
		h = (vertices[0].x + vertices[1].x) / 2.0;
		k = (vertices[0].y + vertices[1].y) / 2.0;

		term1 = (2.0 * minor * (x - h)) * (2.0 * minor * (x - h));
		term2 = (2.0 * major * (y - k)) * (2.0 * major * (y - k));
		term3 = ((1.0 * major) * (1.0 * major)) * ((1.0 * minor) * (1.0 * minor));
		
		if (term1 + term2 <= term3) {
			return true;
		}
		return false;

	}


	@Override
	public int getCursor(Point position) {

		int posDrag = -1;
		for (int i = 0; i < 4; i++) {
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
		for (int i = 0; i < 4; i++) {
			g.setColor(Color.black);
			g.draw(cursors[i]);
			g.setColor(Color.WHITE);
			g.fill(cursors[i]);
		}
		g.setColor(temp);
	}

	@Override
	public void resizeShape(int resizeMode, Point position) {
		switch (resizeMode) {
		case CN: {
			vertices[0].y =position.y;
			minor = vertices[1].y - vertices[0].y;
			moveCursors();
			break;
		}

		case CS: {
			vertices[1].y = position.y;
			minor = vertices[1].y - vertices[0].y;
			moveCursors();
			break;
		}

		case CW: {
			vertices[0].x = position.x;
			major = vertices[1].x - vertices[0].x;
			moveCursors();
			break;
		}

		case CE: {
			vertices[1].x = position.x;
			major = vertices[1].x - vertices[0].x;
			moveCursors();
			break;
		}
		}
	}

	@Override
	public void moveCursors() {
		
		cursors[CN] = new Rectangle2D.Double(vertices[0].x + (major / 2 - 5), vertices[0].y - 5, 10, 10);
		cursors[CS] = new Rectangle2D.Double(vertices[0].x + (major / 2 - 5), vertices[0].y + (minor) - 5, 10, 10);
		cursors[CW] = new Rectangle2D.Double(vertices[0].x - 5, vertices[0].y + (minor / 2) - 5, 10, 10);
		cursors[CE] = new Rectangle2D.Double(vertices[1].x - 5, vertices[0].y + (minor / 2) - 5, 10, 10);

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
		for (int i = 0; i < pointsNum; i++) {
			vertices[i].x += deltaX;
			vertices[i].y += deltaY;
		}
		moveCursors();
		
	}

}
