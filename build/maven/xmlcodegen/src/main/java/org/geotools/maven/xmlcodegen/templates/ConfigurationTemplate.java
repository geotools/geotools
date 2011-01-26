package org.geotools.maven.xmlcodegen.templates;

import java.util.*;
import java.io.*;
import org.eclipse.xsd.*;
import org.geotools.xml.*;

public class ConfigurationTemplate
{
  protected static String nl;
  public static synchronized ConfigurationTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    ConfigurationTemplate result = new ConfigurationTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "import org.eclipse.xsd.util.XSDSchemaLocationResolver;\t" + NL + "import org.geotools.xml.Configuration;" + NL + "import org.picocontainer.MutablePicoContainer;" + NL + "" + NL + "/**" + NL + " * Parser configuration for the ";
  protected final String TEXT_2 = " schema." + NL + " *" + NL + " * @generated" + NL + " */" + NL + "public class ";
  protected final String TEXT_3 = "Configuration extends Configuration {" + NL + "" + NL + "    /**" + NL + "     * Creates a new configuration." + NL + "     * " + NL + "     * @generated" + NL + "     */     " + NL + "    public ";
  protected final String TEXT_4 = "Configuration() {" + NL + "       super(";
  protected final String TEXT_5 = ".getInstance());" + NL + "       " + NL + "       //TODO: add dependencies here" + NL + "    }" + NL + "    " + NL + "    /**" + NL + "     * Registers the bindings for the configuration." + NL + "     *" + NL + "     * @generated" + NL + "     */" + NL + "    protected final void registerBindings( MutablePicoContainer container ) {";
  protected final String TEXT_6 = NL + "        //Types";
  protected final String TEXT_7 = NL + "        container.registerComponentImplementation(";
  protected final String TEXT_8 = ",";
  protected final String TEXT_9 = ");";
  protected final String TEXT_10 = NL;
  protected final String TEXT_11 = NL + "        //Elements";
  protected final String TEXT_12 = NL + "        container.registerComponentImplementation(";
  protected final String TEXT_13 = ",";
  protected final String TEXT_14 = ");";
  protected final String TEXT_15 = NL;
  protected final String TEXT_16 = NL + "        //Attributes";
  protected final String TEXT_17 = NL + "        container.registerComponentImplementation(";
  protected final String TEXT_18 = ",";
  protected final String TEXT_19 = ");";
  protected final String TEXT_20 = NL + "    " + NL + "    }" + NL + "} ";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
     	
    Object[] arguments = (Object[])argument;
	XSDSchema schema = (XSDSchema)arguments[0];
	List components = (List)arguments[1];
	
	String namespace = schema.getTargetNamespace();
	String prefix = Schemas.getTargetPrefix( schema ).toUpperCase();
	
	List types = new ArrayList();
    List elements = new ArrayList();
    List attributes = new ArrayList();
    
    for (Iterator itr = components.iterator(); itr.hasNext();) {
        XSDNamedComponent component = (XSDNamedComponent)itr.next();
        if (component instanceof XSDTypeDefinition) {
            types.add(component);
        }
        else if (component instanceof XSDElementDeclaration) {
            elements.add(component);
        }
        else if (component instanceof XSDAttributeDeclaration) {
            attributes.add(component);
        }
    }

    stringBuffer.append(TEXT_1);
    stringBuffer.append(namespace);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(prefix);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(prefix);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(prefix);
    stringBuffer.append(TEXT_5);
    
    if (!types.isEmpty()) {

    stringBuffer.append(TEXT_6);
    
        for (Iterator itr = types.iterator(); itr.hasNext();) {
                XSDTypeDefinition type = (XSDTypeDefinition)itr.next();
                if (type.getName() == null) continue;
                
                String typeQName = prefix.toUpperCase()+"."+type.getName();
                String binding = type.getName().substring(0,1).toUpperCase() + 
                    type.getName().substring(1) + "Binding.class";

    stringBuffer.append(TEXT_7);
    stringBuffer.append(typeQName);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(binding);
    stringBuffer.append(TEXT_9);
    
        }
    }

    stringBuffer.append(TEXT_10);
    
    if (!elements.isEmpty()) {

    stringBuffer.append(TEXT_11);
    
        for (Iterator itr = elements.iterator(); itr.hasNext();) {
                XSDNamedComponent named = (XSDNamedComponent)itr.next();
                if (named.getName() == null) continue;
                
                String nQName = prefix.toUpperCase()+"."+named.getName();
                String binding = named.getName().substring(0,1).toUpperCase() + 
                    named.getName().substring(1) + "Binding.class";

    stringBuffer.append(TEXT_12);
    stringBuffer.append(nQName);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(binding);
    stringBuffer.append(TEXT_14);
    
        }
    }

    stringBuffer.append(TEXT_15);
    
    if (!attributes.isEmpty()) {

    stringBuffer.append(TEXT_16);
    
        for (Iterator itr = attributes.iterator(); itr.hasNext();) {
                XSDNamedComponent named = (XSDNamedComponent)itr.next();
                if (named.getName() == null) continue;
                
                String nQName = prefix.toUpperCase()+"."+named.getName();
                String binding = named.getName().substring(0,1).toUpperCase() + 
                    named.getName().substring(1) + "Binding.class";

    stringBuffer.append(TEXT_17);
    stringBuffer.append(nQName);
    stringBuffer.append(TEXT_18);
    stringBuffer.append(binding);
    stringBuffer.append(TEXT_19);
    
        }
    }

    stringBuffer.append(TEXT_20);
    return stringBuffer.toString();
  }
}
