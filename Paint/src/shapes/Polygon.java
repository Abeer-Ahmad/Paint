package shapes;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public abstract class Polygon extends Shape {

	protected int[] xVertices;
	protected int[] yVertices;
	protected Point[] vertices;
	protected Rectangle2D[] cursors; 

	public Polygon(){
		
	}
	public Polygon(int x,Color fillColor,BasicStroke thickness, Color LayoutColor, Point[] dim) {
		pointsNum = x;
		xVertices = new int[pointsNum];
		yVertices = new int[pointsNum];
		vertices = new Point[pointsNum];
		 cursors=new Rectangle2D[pointsNum];
		 
		 
		for (int i = 0; i < pointsNum; i++) {
			if (dim !=null){
			vertices[i] = new Point(dim[i]);
			xVertices[i]=dim[i].x;
			yVertices[i]=dim[i].y;
			}
			else{
				vertices[i] = new Point(0,0);
				xVertices[i]=0;
				yVertices[i]=0;
			}
			cursors [i] = new Rectangle2D.Double(0, 0,10,10);
		}
		this.setLayoutClr(LayoutColor);
		this.setFillClr(fillColor);
		this.setThickness(thickness);
		moveCursors();
	}

	public void drawShape(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(layoutClr);
		g.setStroke(thickness);
		g.drawPolygon(xVertices, yVertices, pointsNum);
		g.setColor(fillClr);
		g.fillPolygon(xVertices, yVertices, pointsNum);
		
	}

	@Override
	public void updateVertices(ArrayList<Point> points, int ind1, int ind2) {
		for (int i = 0; i < pointsNum; i++) {
			xVertices[i] = points.get(i).x;
			yVertices[i] = points.get(i).y;
			vertices[i].x = xVertices[i];
			vertices[i].y = yVertices[i];
		}
		moveCursors();
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO Auto-generated method stub
		boolean found = false;
		int j = pointsNum - 1;
		for (int i = 0; i < pointsNum; j = i++) {
			if (((vertices[i].y >= y) != (vertices[j].y >= y)) &&
					(x <= (vertices[j].x - vertices[i].x) * (y - vertices[i].y)
							/ (vertices[j].y - vertices[i].y) + vertices[i].x))
				found = !found;
		}
		return found;
	}
	@Override
	public void move(int deltaX, int deltaY) {
		// TODO Auto-generated method stub
				for (int i = 0; i < pointsNum; i++) {
					vertices[i].x += deltaX;
					vertices[i].y += deltaY;
					xVertices[i] = vertices[i].x;
					yVertices[i] = vertices[i].y;
				}
				moveCursors();
	}
	@Override
	public int getCursor(Point position) {
		// TODO Auto-generated method stub
		int posDrag = -1;
		for (int i = 0; i < pointsNum; i++) {
			if (cursors[i].contains(position)) {
				posDrag = i;
				break;
			}
		}
		return posDrag;
	}

	@Override
	public void drawCursors(Graphics2D g) {
		// TODO Auto-generated method stub
		Color temp = g.getColor();
		g.setColor(Color.WHITE);
		for (int i = 0; i < pointsNum; i++) {
			g.setColor(Color.black);
			g.draw(cursors[i]);
			g.setColor(Color.WHITE);
			g.fill(cursors[i]);
		}
		g.setColor(temp);
	}

	@Override
	public void resizeShape(int resizeMode, Point Position) {
		// TODO Auto-generated method stub
		if (resizeMode>=0){
			vertices[resizeMode]= new Point(Position);
			xVertices[resizeMode] = Position.x;
			yVertices[resizeMode] = Position.y;
		}
		moveCursors();
	}

	@Override
	public void moveCursors() {
		// TODO Auto-generated method stub
		for (int i=0;i<pointsNum;i++){
			cursors[i] = new Rectangle2D.Double(vertices[i].x - 5, vertices[i].y - 5, 10, 10);
		}
	}
}
