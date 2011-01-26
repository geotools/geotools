package org.geotools.maven.xmlcodegen;

/**
 * Bean for a constructor argument.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class BindingConstructorArgument {

    String name;
	
	String type;
	
	String mode = "member";
	
	public Class clazz;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public String getMode() {
        return mode;
    }
	
	public void setMode(String mode) {
        this.mode = mode;
    }
}
