package org.geotools.maven.xmlcodegen.templates;

import java.util.*;
import java.io.*;
import org.eclipse.xsd.*;
import org.geotools.xml.*;
import org.geotools.maven.xmlcodegen.*;

public class XSDTemplate
{
  protected static String nl;
  public static synchronized XSDTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    XSDTemplate result = new XSDTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = NL + "import java.util.Set;" + NL + "import javax.xml.namespace.QName;" + NL + "import org.geotools.xml.XSD;" + NL + "" + NL + "/**" + NL + " * This interface contains the qualified names of all the types,elements, and " + NL + " * attributes in the ";
  protected final String TEXT_2 = " schema." + NL + " *" + NL + " * @generated" + NL + " */" + NL + "public final class ";
  protected final String TEXT_3 = " extends XSD {" + NL + "" + NL + "    /** singleton instance */" + NL + "    private static final ";
  protected final String TEXT_4 = " instance = new ";
  protected final String TEXT_5 = "();" + NL + "    " + NL + "    /**" + NL + "     * Returns the singleton instance." + NL + "     */" + NL + "    public static final ";
  protected final String TEXT_6 = " getInstance() {" + NL + "       return instance;" + NL + "    }" + NL + "    " + NL + "    /**" + NL + "     * private constructor" + NL + "     */" + NL + "    private ";
  protected final String TEXT_7 = "() {" + NL + "    }" + NL + "    " + NL + "    protected void addDependencies(Set dependencies) {" + NL + "       //TODO: add dependencies here" + NL + "    }" + NL + "    " + NL + "    /**" + NL + "     * Returns '";
  protected final String TEXT_8 = "'." + NL + "     */" + NL + "    public String getNamespaceURI() {" + NL + "       return NAMESPACE;" + NL + "    }" + NL + "    " + NL + "    /**" + NL + "     * Returns the location of '";
  protected final String TEXT_9 = ".'." + NL + "     */" + NL + "    public String getSchemaLocation() {" + NL + "       return getClass().getResource(\"";
  protected final String TEXT_10 = "\").toString();" + NL + "    }" + NL + "    " + NL + "    /** @generated */" + NL + "    public static final String NAMESPACE = \"";
  protected final String TEXT_11 = "\";" + NL + "    " + NL + "    /* Type Definitions */";
  protected final String TEXT_12 = NL + "    /** @generated */" + NL + "    public static final QName ";
  protected final String TEXT_13 = " = " + NL + "        new QName(\"";
  protected final String TEXT_14 = "\",\"";
  protected final String TEXT_15 = "\");";
  protected final String TEXT_16 = NL + NL + "    /* Elements */";
  protected final String TEXT_17 = NL + "    /** @generated */" + NL + "    public static final QName ";
  protected final String TEXT_18 = " = " + NL + "        new QName(\"";
  protected final String TEXT_19 = "\",\"";
  protected final String TEXT_20 = "\");";
  protected final String TEXT_21 = NL + NL + "    /* Attributes */";
  protected final String TEXT_22 = NL + "    /** @generated */" + NL + "    public static final QName ";
  protected final String TEXT_23 = " = " + NL + "        new QName(\"";
  protected final String TEXT_24 = "\",\"";
  protected final String TEXT_25 = "\");";
  protected final String TEXT_26 = NL + NL + "}";
  protected final String TEXT_27 = NL + "    ";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
      
    XSDSchema schema = (XSDSchema)argument ;
    String ns = schema.getTargetNamespace();
    String prefix = Schemas.getTargetPrefix( schema );
    
    String file = new File( schema.eResource().getURI().toFileString() ).getName();
    

    stringBuffer.append(TEXT_1);
    stringBuffer.append(schema.getTargetNamespace());
    stringBuffer.append(TEXT_2);
    stringBuffer.append(prefix.toUpperCase());
    stringBuffer.append(TEXT_3);
    stringBuffer.append(prefix.toUpperCase());
    stringBuffer.append(TEXT_4);
    stringBuffer.append(prefix.toUpperCase());
    stringBuffer.append(TEXT_5);
    stringBuffer.append(prefix.toUpperCase());
    stringBuffer.append(TEXT_6);
    stringBuffer.append(prefix.toUpperCase());
    stringBuffer.append(TEXT_7);
    stringBuffer.append(ns);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(file);
    stringBuffer.append(TEXT_9);
    stringBuffer.append(file);
    stringBuffer.append(TEXT_10);
    stringBuffer.append( ns );
    stringBuffer.append(TEXT_11);
    
    List types = GeneratorUtils.allTypes( schema );
    for (Iterator itr = types.iterator(); itr.hasNext();) {
        XSDTypeDefinition type = (XSDTypeDefinition)itr.next();
        if (type.getName() == null) continue;
        if (!ns.equals(type.getTargetNamespace())) continue;
        

    stringBuffer.append(TEXT_12);
    stringBuffer.append(type.getName());
    stringBuffer.append(TEXT_13);
    stringBuffer.append(ns);
    stringBuffer.append(TEXT_14);
    stringBuffer.append(type.getName());
    stringBuffer.append(TEXT_15);
    
    }

    stringBuffer.append(TEXT_16);
    
    List elements = schema.getElementDeclarations();
    for (Iterator itr = elements.iterator(); itr.hasNext();) {
        XSDElementDeclaration element = (XSDElementDeclaration)itr.next();
        if (element.getName() == null) continue;
        if (!ns.equals(element.getTargetNamespace())) continue;

    stringBuffer.append(TEXT_17);
    stringBuffer.append(element.getName());
    stringBuffer.append(TEXT_18);
    stringBuffer.append(ns);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(element.getName());
    stringBuffer.append(TEXT_20);
    
    }

    stringBuffer.append(TEXT_21);
    
    List attributes = schema.getAttributeDeclarations();
    for (Iterator itr = attributes.iterator(); itr.hasNext();) {
        XSDAttributeDeclaration attribute = (XSDAttributeDeclaration)itr.next();
        if (attribute.getName() == null) continue;
        if (!ns.equals(attribute.getTargetNamespace())) continue;

    stringBuffer.append(TEXT_22);
    stringBuffer.append(attribute.getName());
    stringBuffer.append(TEXT_23);
    stringBuffer.append(ns);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(attribute.getName());
    stringBuffer.append(TEXT_25);
    
    }

    stringBuffer.append(TEXT_26);
    stringBuffer.append(TEXT_27);
    return stringBuffer.toString();
  }
}
