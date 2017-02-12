package dynamicLinkage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JOptionPane;

import shapes.Shape;

public class Plugin {
	
	 Constructor<?>[] constructor;
     URL url;
     URLClassLoader ucl;
     Class<?> loadedClass;
	public void loadPlugin(final File newJar) {
		try {
			 url = newJar.toURI().toURL();
			 ucl = new URLClassLoader(new URL[] { url });
			loadedClass = ucl.loadClass("shapes.Triangle");
			constructor = loadedClass.getConstructors();
			JOptionPane.showMessageDialog(null, "Class Loaded Successfully", "Warning", JOptionPane.PLAIN_MESSAGE);
		} catch (Exception e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, "Cannot find plugin!", "Warning", JOptionPane.PLAIN_MESSAGE);
		}
	}

	public Shape createTriangle(Color fillClr, BasicStroke thickness, Color layoutClr, Point[] dim) {
		
		try {
			Shape triangle = (Shape) constructor[0].newInstance(fillClr, thickness, layoutClr, dim);
			//System.out.println(constructor[0].getClass().getSimpleName());
			return triangle;
		} catch (Exception e) {
			System.out.println(e);
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(null, "Cannot create Triangle!", "Warning", JOptionPane.PLAIN_MESSAGE);
			return null;
		}
	}
	
	public Class<?> getLoadedClasses(){
		return loadedClass;
	}
}
