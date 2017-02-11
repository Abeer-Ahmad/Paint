package parsers.xml;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import shapes.Shape;

public class XMLWriter {

	XStream stream = new XStream(new DomDriver());

	public void writeXML(File newSheet, ArrayList<Shape> shapes) {

		try {

			FileWriter pw = new FileWriter(newSheet);
			pw.write(stream.toXML(shapes));
			pw.close();
		} catch (Exception e) {

			JOptionPane.showMessageDialog(null, "Error in xml writing");
		}
	}
}
