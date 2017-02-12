package parsers.xml;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import gui.Paint;
import shapes.Shape;

public class XMLReader {

	private XStream stream;
	private Paint p;

	public XMLReader(Paint p) {
		stream = new XStream(new DomDriver());
		this.p = p;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Shape> readXML(File loadSheet) {

		try {

			InputStream is = new FileInputStream(loadSheet);

			if (p.downloaded) {
				
				
				stream.setClassLoader(p.loadClasses().getClassLoader());
			}
			
			ArrayList<Shape> shapes = (ArrayList<Shape>) stream.fromXML(is);
			
			is.close();
			return shapes;
		} catch (Exception e) {
			System.out.println("Exception");
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, "File not Found");
			return null;
		}
	}

}
