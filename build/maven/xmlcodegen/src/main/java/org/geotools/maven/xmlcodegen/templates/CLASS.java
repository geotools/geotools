package org.geotools.maven.xmlcodegen.templates;

import org.geotools.maven.xmlcodegen.*;
import java.util.*;
import org.apache.xml.serialize.*;
import org.eclipse.xsd.*;
import java.io.*;
import org.geotools.xml.*;

public class CLASS
{
  protected static String nl;
  public static synchronized CLASS create(String lineSeparator)
  {
    nl = lineSeparator;
    CLASS result = new CLASS();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = NL + "import org.geotools.xml.*;" + NL + "import ";
  protected final String TEXT_2 = ";" + NL;
  protected final String TEXT_3 = NL + "import ";
  protected final String TEXT_4 = ";\t\t";
  protected final String TEXT_5 = NL + NL + "import javax.xml.namespace.QName;" + NL + "" + NL + "/**" + NL + " * Binding object for the ";
  protected final String TEXT_6 = " ";
  protected final String TEXT_7 = ":";
  protected final String TEXT_8 = "." + NL + " *" + NL + " * <p>" + NL + " *\t<pre>" + NL + " *\t <code>";
  protected final String TEXT_9 = NL + " *  ";
  protected final String TEXT_10 = " " + NL + " *\t\t" + NL + " *\t  </code>" + NL + " *\t </pre>" + NL + " * </p>" + NL + " *" + NL + " * @generated" + NL + " */";
  protected final String TEXT_11 = NL + "public class ";
  protected final String TEXT_12 = " extends ";
  protected final String TEXT_13 = " {" + NL;
  protected final String TEXT_14 = NL + "\t";
  protected final String TEXT_15 = " ";
  protected final String TEXT_16 = ";\t";
  protected final String TEXT_17 = "\t" + NL + "\t";
  protected final String TEXT_18 = NL + "\t/**" + NL + "\t * @generated" + NL + "\t */" + NL + "\tpublic QName getTarget() {" + NL + "\t\treturn ";
  protected final String TEXT_19 = ".";
  protected final String TEXT_20 = ";" + NL + "\t}" + NL + "\t" + NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t *\t" + NL + "\t * @generated modifiable" + NL + "\t */\t" + NL + "\tpublic Class getType() {" + NL + "\t\treturn null;" + NL + "\t}" + NL + "\t";
  protected final String TEXT_21 = NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t *\t" + NL + "\t * @generated modifiable" + NL + "\t */\t" + NL + "\tpublic Object parse(InstanceComponent instance, Object value) " + NL + "\t\tthrows Exception {" + NL + "\t\t" + NL + "\t\t//TODO: implement and remove call to super" + NL + "\t\treturn super.parse(instance,value);" + NL + "\t}" + NL;
  protected final String TEXT_22 = NL + "\t/**" + NL + "\t * <!-- begin-user-doc -->" + NL + "\t * <!-- end-user-doc -->" + NL + "\t *\t" + NL + "\t * @generated modifiable" + NL + "\t */\t" + NL + "\tpublic Object parse(ElementInstance instance, Node node, Object value) " + NL + "\t\tthrows Exception {" + NL + "\t\t" + NL + "\t\t//TODO: implement and remove call to super" + NL + "\t\treturn super.parse(instance,node,value);" + NL + "\t}" + NL;
  protected final String TEXT_23 = NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    

	Object[] arguments = (Object[]) argument;
	XSDNamedComponent named = (XSDNamedComponent)arguments[0];
	BindingConstructorArgument[] constructorArgs = (BindingConstructorArgument[]) arguments[1];
	Class bindingBaseClass = (Class)arguments[2];
	
	XSDSchema schema = named.getSchema();
	XSDTypeDefinition type = null;
	String desc = null;
	if (named instanceof XSDTypeDefinition) {
		type = (XSDTypeDefinition)named;
		desc = "type";
	}
	if (named instanceof XSDElementDeclaration) {
		type = ((XSDElementDeclaration)named).getTypeDefinition();
		desc = "element";
	}
	if (named instanceof XSDAttributeDeclaration) {
		type = ((XSDAttributeDeclaration)named).getTypeDefinition();
		desc = "attribute";
	}
	
	String ns = schema.getTargetNamespace();
	String prefix = Schemas.getTargetPrefix( schema );

    stringBuffer.append(TEXT_1);
    stringBuffer.append(bindingBaseClass.getName());
    stringBuffer.append(TEXT_2);
    
	if ( constructorArgs != null ) {
		for ( int i = 0;i < constructorArgs.length; i++ ) {
			Class arg = constructorArgs[i].clazz;

    stringBuffer.append(TEXT_3);
    stringBuffer.append(arg.getName());
    stringBuffer.append(TEXT_4);
    		
		}
	}

    stringBuffer.append(TEXT_5);
    stringBuffer.append(desc);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(named.getTargetNamespace());
    stringBuffer.append(TEXT_7);
    stringBuffer.append(named.getName());
    stringBuffer.append(TEXT_8);
    
	
	OutputFormat output = new OutputFormat();
	output.setOmitXMLDeclaration(true);
	output.setIndenting(true);
	
	StringWriter writer = new StringWriter();
	XMLSerializer serializer = new XMLSerializer(writer,output);

	try {
		serializer.serialize(named.getElement());
	} 
	catch (IOException e) {
		e.printStackTrace();
		return null;
	}
	
	String[] lines = writer.getBuffer().toString().split("\n");
	for (int i = 0; i < lines.length; i++) {

    stringBuffer.append(TEXT_9);
    stringBuffer.append(lines[i].replaceAll("<","&lt;").replaceAll(">","&gt;"));
    
	}

    stringBuffer.append(TEXT_10);
    
	String className = named.getName().substring(0,1).toUpperCase() + 
		named.getName().substring(1) + "Binding";
	String baseClassName = bindingBaseClass.getName();
	baseClassName = baseClassName.substring(bindingBaseClass.getPackage().getName().length()+1); 

    stringBuffer.append(TEXT_11);
    stringBuffer.append(className);
    stringBuffer.append(TEXT_12);
    stringBuffer.append(baseClassName);
    stringBuffer.append(TEXT_13);
    
	if ( constructorArgs != null ) {
		List fieldNames = new ArrayList();
		
		StringBuffer constructor = new StringBuffer();
		constructor.append("public " + className + "( ");
		
		for ( int i = 0; i < constructorArgs.length; i++ ) {
		    
			String fieldName = constructorArgs[i].getName();
			Class arg = constructorArgs[i].clazz;
			
			String typeName = arg.getName();
			
			if ( typeName.lastIndexOf( '.' ) != -1 ) {
				typeName = typeName.substring( typeName.lastIndexOf( '.') + 1 );
			}
			
			fieldNames.add( fieldName );
			
			if ( "member".equals(constructorArgs[i].getMode() ) ) {

    stringBuffer.append(TEXT_14);
    stringBuffer.append(typeName);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(fieldName);
    stringBuffer.append(TEXT_16);
    
            }
            
			constructor.append( typeName + " " + fieldName );
			constructor.append( "," );
		}
		constructor.setLength( constructor.length()-1 );
		
		constructor.append( " ) {\n");
		constructor.append( "\t\tsuper(");
		boolean trim = false;
		for ( int i = 0; i < constructorArgs.length; i++ ) {
            String fieldName = (String) fieldNames.get(i);
            if ( "parent".equals( constructorArgs[i].getMode() ) ) {
              constructor.append(fieldName + ",");
              trim = true;
            }
        }
        if (trim) {
            constructor.setLength(constructor.length()-1);
        }
		constructor.append( ");\n");
		
		for ( int i = 0; i < constructorArgs.length; i++ ) {
            String fieldName = (String) fieldNames.get(i);
            if ( "member".equals( constructorArgs[i].getMode() ) ) {
                constructor.append( "\t\tthis." + fieldName + " = " + fieldName + ";\n");
            }
		}
		constructor.append( "\t}\n" );

    stringBuffer.append(TEXT_17);
    stringBuffer.append(constructor.toString());
    
	}

    stringBuffer.append(TEXT_18);
    stringBuffer.append(prefix.toUpperCase());
    stringBuffer.append(TEXT_19);
    stringBuffer.append(named.getName());
    stringBuffer.append(TEXT_20);
    
	if (type instanceof XSDSimpleTypeDefinition) {

    stringBuffer.append(TEXT_21);
    
	}
	else {

    stringBuffer.append(TEXT_22);
    
	}

    stringBuffer.append(TEXT_23);
    return stringBuffer.toString();
  }
}
