package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import javax.swing.JPanel;

import shapes.Shape;

public class PaintArea extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9133783297462055235L;
	private ArrayList<Shape> shapes = new ArrayList<Shape>();
	private ArrayList<Shape> tempShape = new ArrayList<Shape>();
	private int selectedInd = -1;

	PaintArea() {
		this.setBackground(Color.WHITE);
		this.setBounds(80, 0, 850, 800);
	}

	public void drawGrid(ArrayList<Shape> elements, ArrayList<Shape> dragElements, int index) {
		shapes = new ArrayList<Shape>(elements);
		tempShape = new ArrayList<Shape>(dragElements);
		selectedInd = index;
		this.repaint();

	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D grid = (Graphics2D) g;
		super.paintComponent(g);
		grid.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// already painted shapes
		for (int i = 0; i < shapes.size(); i++) {

			if (i != selectedInd) {
				shapes.get(i).drawShape(grid);
			}
		}
		// current shape
		if (tempShape.size() != 0) {

			if (tempShape.size() != 1) {
				for (int i = 1; i < tempShape.size(); i++) {
					tempShape.get(i).drawShape(grid); // lines in polygons
				}
			} else {
				tempShape.get(0).drawShape(grid);
				if (selectedInd > -1) {
					tempShape.get(0).drawCursors(grid);
				}
			}
		}
	}
}