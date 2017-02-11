package shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;

public class Triangle extends Polygon {

	private static int nVertices = 3;

	public Triangle(Color FillColor, BasicStroke Thickness, Color LayoutClr, Point[] dim) {
		super(nVertices, FillColor, Thickness, LayoutClr, dim);

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

}
