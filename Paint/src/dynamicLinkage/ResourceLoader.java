package dynamicLinkage;

import java.io.File;
import java.io.InputStream;

final public class ResourceLoader {
 public static InputStream load(String path){
	 
	 InputStream input = ResourceLoader.class.getClassLoader().getResourceAsStream(File.separator+path);
	
	 
	/* if (input==null){
		 input= ResourceLoader.class.getResourceAsStream(path);
		
	 }*/
	 return input;
 }
}
