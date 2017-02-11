package shapes;

import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public abstract class Shape {

	/*
	 * public Shape (){
	 * 
	 * }
	 */
	protected Color layoutClr;
	protected Color fillClr;
	protected int pointsNum;
	protected BasicStroke thickness;
	public static final int CN = 0;
	public static final int CS = 1;
	public static final int CW = 2;
	public static final int CE = 3;
	protected Point[] vertices;
	/*
	 * float[] dash = { 5F, 5F }; protected Stroke dashedStroke;
	 */
	/*	*/

	public abstract void drawShape(Graphics2D g);

	// which box was selected
	public abstract int getCursor(Point position);

	// drawing small boxs
	public abstract void drawCursors(Graphics2D g);

	// resize Algorthims
	public abstract void resizeShape(int resizeMode, Point Position);

	// moving cursors
	public abstract void moveCursors();

	public abstract void updateVertices(ArrayList<Point> points, int ind1, int ind2);

	public abstract boolean contains(int x, int y);

	public int getPointsNum() {
		return pointsNum;
	}

	public void setLayoutClr(Color layoutClr) {
		this.layoutClr = layoutClr;
	}

	public void setFillClr(Color fillClr) {
		this.fillClr = fillClr;
	}

	public void setThickness(BasicStroke thickness) {
		this.thickness = thickness;
	}

	public abstract Point[] getVertices();

	public abstract Color getLayoutClr();

	public abstract Color getFillClr();

	public abstract BasicStroke getThickness();

	public abstract void move(int deltaX, int deltaY);

	public final void setPointsNum(int pointsNum) {
		this.pointsNum = pointsNum;
	}

	public final void setVertices(Point[] vertices) {
		this.vertices = vertices;
	}

}
