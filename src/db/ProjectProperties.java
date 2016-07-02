package db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ProjectProperties {

	public ProjectProperties(){}
	private static Properties props = new Properties(); 
	static{
		try {
			props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("project.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getValue(String key){
		return props.getProperty(key);
	}

    public static void setValue(String key,String value) {    
        props.setProperty(key, value); 
    } 
}
