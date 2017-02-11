package shapes;

import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
//import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Rectangle extends Shape {

	// first point considered top left / second point bottom right
	public Point[] vertices = new Point[2];
	protected Point start = new Point();
	protected int width = 0;
	protected int height = 0;
	protected int g1 , g2;
	protected  Rectangle2D[] cursors= new Rectangle2D [4];
	public Rectangle(){
		
	}
	
	public Rectangle(Color fillColor,BasicStroke thickness, Color LayoutColor, Point[] dim) {
		pointsNum = 2;
		for (int i = 0; i < 2; i++) {
			if (dim!=null){
			vertices[i] = new Point(dim[i]);
			}
			else{
				vertices[i] = new Point(0,0);
			}
		}
		width=vertices[1].x-vertices[0].x;
		height=vertices[1].y-vertices[0].y;
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
		g1 = (points.get(ind1).x > points.get(ind2).x ? -1 : 0);
		g2 = (points.get(ind1).y > points.get(ind2).y ? -1 : 0);
		start = new Point(points.get(ind1));
		width = Math.abs(points.get(ind1).x - points.get(ind2).x);
		height = Math.abs(points.get(ind1).y - points.get(ind2).y);
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
		if (vertices[0].x!=0 && vertices[0].y!=0 && width!=0 && height!=0){
		g.drawRect(vertices[0].x, vertices[0].y, width, height);
		g.setColor(fillClr);
		g.fillRect(vertices[0].x, vertices[0].y, width, height);
		}
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO Auto-generated method stub		
		if (x >  vertices[0].x && x <  vertices[1].x && y >  vertices[0].y && y <  vertices[1].y) {
			return true;
		}
		return false;
	}

	@Override
	public int getCursor(Point position) {
		
		int posDrag =-1;
		for(int i=0;i<4;i++){
			if (cursors[i].contains(position)){
				posDrag=i;
				break;
			}
		}
		return posDrag;
		
	}

	@Override
	public void drawCursors(Graphics2D g) {
		
		Color temp=g.getColor();
		g.setColor(Color.WHITE);
		for (int i=0;i<4;i++){
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
			vertices[0].y =position.y;
			height = vertices[1].y - vertices[0].y;
			moveCursors();
			break;
		}

		case CS: {
			vertices[1].y = position.y;
			height = vertices[1].y - vertices[0].y;
			moveCursors();
			break;
		}

		case CW: {
			vertices[0].x = position.x;
			width = vertices[1].x - vertices[0].x;
			moveCursors();
			break;
		}

		case CE: {
			vertices[1].x = position.x;
			width= vertices[1].x - vertices[0].x;
			moveCursors();
			break;
		}
		}
	}
	
	public void moveCursors(){
		cursors[CN].setRect(vertices[0].x+(width/2-5), vertices[0].y-5,10, 10);
		cursors[CS].setRect(vertices[0].x+(width/2-5), vertices[0].y+(height)-5,10, 10);
		cursors[CW].setRect(vertices[0].x-5, vertices[0].y+(height/2)-5,10, 10);
		cursors[CE].setRect(vertices[1].x-5, vertices[0].y+(height/2)-5,10, 10);
	}
  public void setRectangle (int x1, int y1, int width, int height){
	  vertices[0].x = x1;
	  vertices[0].y = y1;
	  vertices[1].x = x1+width;
	  vertices[10].y = y1+height;
  }

@Override
public Point[] getVertices() {
	// TODO Auto-generated method stub
	return vertices;
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

}
