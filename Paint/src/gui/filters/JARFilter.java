package gui.filters;

import java.io.File;


import javax.swing.filechooser.FileFilter;

public class JARFilter extends FileFilter{

	 private String description;
	 public final static String jar = "jar";
	 public JARFilter(String description) {
	      this.description = description;
	      
	    }
	@Override
	public boolean accept(File f) {
		// TODO Auto-generated method stub
		if (f.isDirectory())
			return true;
			if (extension(f).equals(jar)){
			 return true;	
			 }
			return false;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return description;
	}

	public String extension(final File f){
		String fileName = f.getName().toLowerCase();
		int dotIndex= fileName.lastIndexOf('.');
		if (dotIndex>0 && dotIndex<fileName.length()-1){
			return fileName.substring(dotIndex+1);
		}
		else{
			return"";
		}
	}
	public String beforeExtension(final File f){
		String fileName = f.getName().toLowerCase();
		int dotIndex= fileName.lastIndexOf('.');
		if (dotIndex>0 && dotIndex<fileName.length()-1){
			return fileName.substring(0,dotIndex);
		}
		else{
			return"";
		}
	}
}
