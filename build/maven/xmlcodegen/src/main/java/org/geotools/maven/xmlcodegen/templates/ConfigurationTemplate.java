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

import java.util.*;

import org.eclipse.xsd.*;
import org.geotools.xsd.Schemas;

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

  public final String NL = nl == null ? System.getProperties().getProperty("line.separator") : nl;
  protected final String TEXT_1 = "import org.eclipse.xsd.util.XSDSchemaLocationResolver;\t" + NL + "import org.geotools.xsd.Configuration;" + NL + "import org.picocontainer.MutablePicoContainer;" + NL + "" + NL + "/**" + NL + " * Parser configuration for the ";
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
	
	List<XSDTypeDefinition> types = new ArrayList<>();
    List<XSDTypeDefinition> elements = new ArrayList<>();
    List<XSDAttributeDeclaration> attributes = new ArrayList<>();

      for (Object o : components) {
          XSDNamedComponent component = (XSDNamedComponent) o;
          if (component instanceof XSDTypeDefinition) {
              types.add((XSDTypeDefinition) component);
          } else if (component instanceof XSDTypeDefinition) {
              elements.add((XSDTypeDefinition) component);
          } else if (component instanceof XSDAttributeDeclaration) {
              attributes.add((XSDAttributeDeclaration) component);
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

        for (XSDTypeDefinition type : types) {
            if (type.getName() == null) continue;

            String typeQName = prefix.toUpperCase() + "." + type.getName();
            String binding = type.getName().substring(0, 1).toUpperCase() +
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

        for (XSDTypeDefinition element : elements) {
            XSDNamedComponent named = element;
            if (named.getName() == null) continue;

            String nQName = prefix.toUpperCase() + "." + named.getName();
            String binding = named.getName().substring(0, 1).toUpperCase() +
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

        for (XSDAttributeDeclaration attribute : attributes) {
            XSDNamedComponent named = attribute;
            if (named.getName() == null) continue;

            String nQName = prefix.toUpperCase() + "." + named.getName();
            String binding = named.getName().substring(0, 1).toUpperCase() +
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
