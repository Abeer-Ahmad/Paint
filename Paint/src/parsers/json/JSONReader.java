package parsers.json;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import gui.Paint;
import paint.main.Main;
import shapes.Shape;

public class JSONReader {

	private JSONParser parser;
	private Scanner sc;
	private Paint p;
	public JSONReader (Paint p){
		this.p=p;
		parser = new JSONParser();
	}
	public ArrayList<Shape> read(File loadSheet) {
		try {
			ArrayList<Shape> shapes = new ArrayList<Shape>();
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(loadSheet));
			JSONObject jObj = (JSONObject) obj;
			String attribute = new String();
			JSONArray list = (JSONArray) jObj.get("list");
			
			for (int i = 0; i < list.size(); i++) {
				jObj = (JSONObject) list.get(i);
				String name = (String) jObj.get("type");
				
				attribute = (String) jObj.get("layoutClr");
				sc = new Scanner(attribute);
				sc.useDelimiter("\\D+");
				Color layoutClr = new Color(sc.nextInt(), sc.nextInt(), sc.nextInt());
				sc.close();
				
				attribute = (String) jObj.get("fillClr");
				sc = new Scanner(attribute);
				sc.useDelimiter("\\D+");
				Color fillClr = new Color(sc.nextInt(), sc.nextInt(), sc.nextInt());
				sc.close();
				
				attribute = (String) jObj.get("thickness");
				float thickness = Float.parseFloat(attribute);
				
				attribute = (String) jObj.get("pointsNum");
				int pointsNum = Integer.parseInt(attribute);
				
				Point[] vertices = new Point[pointsNum];
				for (int j = 0; j < pointsNum; j++) {
					vertices[j] = new Point(0, 0);
					String x = "x" + Integer.toString(j);
					String y = "y" + Integer.toString(j);
					attribute = (String) jObj.get(x);
					vertices[j].x = Integer.parseInt(attribute);
					attribute = (String) jObj.get(y);
					vertices[j].y = Integer.parseInt(attribute);
				}
				
				if ((p.downloaded && name.equals("Triangle"))||(!name.equals("Triangle"))) {
				shapes.add(p.chooseShape(fillClr, new BasicStroke(thickness), layoutClr, vertices, name));
				}
				else {
					JOptionPane.showMessageDialog(null, "Shape not supported"); ///
				}
			}
			return shapes;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}

