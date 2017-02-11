package gui.filters;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ExtensionFilter extends FileFilter{

	private String extensions[];

    private String description;
   
	public ExtensionFilter(String description, String extensions[]) {
	      this.description = description;
	      this.extensions = (String[]) extensions.clone();
	    }
	public final static String xml = "xml";
	public final static String json = "json";
	public boolean accept(File newFile) {
		
		if (newFile.isDirectory())
		return true;
		if (extension(newFile).equals(xml)||extension(newFile).equals(json)){
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
	
}
