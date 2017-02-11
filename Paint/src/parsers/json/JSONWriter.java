package parsers.json;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import shapes.Shape;

public class JSONWriter {
	
	 JSONObject obj = new JSONObject();
	 JSONArray list = new JSONArray();
	
	@SuppressWarnings("unchecked")
	public void toJSON(ArrayList<Shape> shapes) {
		for (int i = 0; i < shapes.size(); i++) {
			 JSONObject obj = new JSONObject();
			 Shape sh = shapes.get(i);
			 
			 obj.put("type", sh.getClass().getSimpleName());
			 obj.put("layoutClr", String.valueOf(sh.getLayoutClr()));
			 obj.put("fillClr", String.valueOf(sh.getFillClr()));
			 obj.put("thickness", Float.toString(sh.getThickness().getLineWidth()));
			 obj.put("pointsNum", Integer.toString(sh.getPointsNum()));
			 for (int j = 0; j < sh.getPointsNum(); j++) {
				String x = "x" + Integer.toString(j);
				String y = "y" + Integer.toString(j);
				obj.put(x, Integer.toString(sh.getVertices()[j].x));
				obj.put(y, Integer.toString(sh.getVertices()[j].y));
				
			 }
			 list.add(obj);
		 }
	}
	
	@SuppressWarnings("unchecked")
	public void write(final File newSheet) {
		try {
			
			FileWriter fw = new FileWriter(newSheet);
			obj.put("list", list);
			fw.write(obj.toJSONString());
			fw.flush();
			fw.close();
		} catch (Exception e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, "Cannot save to file!", "Warning", JOptionPane.PLAIN_MESSAGE);
		}
	}
	public static void main(String[] args){
		/*LinkedHashMap obj =  new LinkedHashMap();
        obj.put("zew", "hi");
        obj.put("salm", value);
		/*JSONObject obj1 = new JSONObject();
		JSONObject obj2 = new JSONObject();
		JSONObject obj3 = new JSONObject();
		 //ArrayList<String> list = new ArrayList<String>();
		JSONArray list = new JSONArray();
     //   obj1.put("zew", "yasso");
         obj2.put("hallo","shosho");
         obj3.put("ali", obj2);
        // list.add(obj1);
         //list.add(obj2);
         list.add(obj3);*/
		/*String temp = list.toJSONString();
		System.out.println(temp);*/

	}
	
}
