/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */

package org.geotools.maven.xmlcodegen.templates;

import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.xsd.Schemas;

public class BindingTestClass
{
  protected static String nl;
  public static synchronized BindingTestClass create(String lineSeparator)
  {
    nl = lineSeparator;
    BindingTestClass result = new BindingTestClass();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? System.getProperties().getProperty("line.separator") : nl;
  protected final String TEXT_1 = NL + "import org.geotools.xsd.Binding;" + NL + "" + NL + "/**" + NL + " * Binding test case for ";
  protected final String TEXT_2 = ":";
  protected final String TEXT_3 = "." + NL + " *" + NL + " * <p>" + NL + " *  <pre>" + NL + " *   <code>";
  protected final String TEXT_4 = NL + " *  ";
  protected final String TEXT_5 = " " + NL + " *      " + NL + " *    </code>" + NL + " *   </pre>" + NL + " * </p>" + NL + " *" + NL + " * @generated" + NL + " */";
  protected final String TEXT_6 = NL + "public class ";
  protected final String TEXT_7 = " extends ";
  protected final String TEXT_8 = " {" + NL + "" + NL + "    public void testType() {" + NL + "        assertEquals(  Object.class, binding( ";
  protected final String TEXT_9 = ".";
  protected final String TEXT_10 = " ).getType() );" + NL + "    }" + NL + "    " + NL + "    public void testExecutionMode() {" + NL + "        assertEquals( Binding.OVERRIDE, binding( ";
  protected final String TEXT_11 = ".";
  protected final String TEXT_12 = " ).getExecutionMode() );" + NL + "    }" + NL + "    " + NL + "    public void testParse() throws Exception {" + NL + "    " + NL + "    }" + NL + "    " + NL + "    public void testEncode() throws Exception {" + NL + "    " + NL + "    }" + NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    

    Object[] arguments = (Object[]) argument;
    XSDNamedComponent named = (XSDNamedComponent)arguments[0];
    
    XSDSchema schema = named.getSchema();
    XSDTypeDefinition type = null;
   
    String ns = schema.getTargetNamespace();
    String prefix = Schemas.getTargetPrefix( schema );

    stringBuffer.append(TEXT_1);
    stringBuffer.append(named.getTargetNamespace());
    stringBuffer.append(TEXT_2);
    stringBuffer.append(named.getName());
    stringBuffer.append(TEXT_3);
    
    StringWriter writer = new StringWriter();

    SAXTransformerFactory txFactory = 
            (SAXTransformerFactory) SAXTransformerFactory.newInstance();
    TransformerHandler xmls;
    try {
        xmls = txFactory.newTransformerHandler();
    } catch (TransformerConfigurationException e) {
        throw new RuntimeException(e);
    }
    xmls.getTransformer().setOutputProperty(OutputKeys.METHOD, "XML");
    xmls.getTransformer().setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "true");
    xmls.getTransformer().setOutputProperty(OutputKeys.INDENT, "true");

    try {
        xmls.getTransformer().transform(new DOMSource(named.getElement()), new StreamResult(writer));
    } 
    catch (Exception e) {
        e.printStackTrace();
        return null;
    }
    
    String[] lines = writer.getBuffer().toString().split("\n");
      for (String line : lines) {

          stringBuffer.append(TEXT_4);
          stringBuffer.append(line.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));

      }

    stringBuffer.append(TEXT_5);
    
    String className = named.getName().substring(0,1).toUpperCase() + 
        named.getName().substring(1) + "BindingTest";
    String baseClassName = prefix.toUpperCase() + "TestSupport";

    stringBuffer.append(TEXT_6);
    stringBuffer.append(className);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(baseClassName);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(prefix.toUpperCase());
    stringBuffer.append(TEXT_9);
    stringBuffer.append(named.getName());
    stringBuffer.append(TEXT_10);
    stringBuffer.append(prefix.toUpperCase());
    stringBuffer.append(TEXT_11);
    stringBuffer.append(named.getName());
    stringBuffer.append(TEXT_12);
    return stringBuffer.toString();
  }
}
