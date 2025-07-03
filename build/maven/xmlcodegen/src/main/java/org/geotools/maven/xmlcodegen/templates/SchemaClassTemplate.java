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
import java.io.*;
import javax.xml.transform.*;
import javax.xml.transform.sax.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*; 
import org.geotools.xml.*;
import org.geotools.maven.xmlcodegen.*;
import org.geotools.api.feature.type.Schema;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.Name;
import org.geotools.feature.NameImpl;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.feature.type.PropertyType;
import org.eclipse.xsd.*;

public class SchemaClassTemplate
{
  protected static String nl;
  public static synchronized SchemaClassTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    SchemaClassTemplate result = new SchemaClassTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + NL + "import java.util.ArrayList;" + NL + "import java.util.Collections;" + NL + "import java.util.List;" + NL + "" + NL + "import org.geotools.api.feature.type.AttributeType;" + NL + "import org.geotools.api.feature.type.ComplexType;" + NL + "import org.geotools.api.feature.type.PropertyDescriptor;" + NL + "import org.geotools.api.filter.Filter;" + NL + "" + NL + "import org.geotools.feature.NameImpl;" + NL + "import org.geotools.feature.type.AttributeDescriptorImpl;" + NL + "import org.geotools.feature.type.AttributeTypeImpl;" + NL + "import org.geotools.feature.type.ComplexTypeImpl;" + NL + "import org.geotools.feature.type.SchemaImpl;" + NL;
  protected final String TEXT_3 = NL + "import ";
  protected final String TEXT_4 = ";";
  protected final String TEXT_5 = NL + NL + "public class ";
  protected final String TEXT_6 = "Schema extends SchemaImpl {" + NL;
  protected final String TEXT_7 = NL + "    /**" + NL + "     * <p>" + NL + "     *  <pre>" + NL + "     *   <code>";
  protected final String TEXT_8 = NL + "     *  ";
  protected final String TEXT_9 = NL + "     *" + NL + "     *    </code>" + NL + "     *   </pre>" + NL + "     * </p>" + NL + "     *" + NL + "     * @generated" + NL + "     */";
  protected final String TEXT_10 = NL + "    public static final ComplexType ";
  protected final String TEXT_11 = "_TYPE = build_";
  protected final String TEXT_12 = "_TYPE();" + NL + "    " + NL + "    private static ComplexType build_";
  protected final String TEXT_13 = "_TYPE() {" + NL + "        ComplexType builtType;";
  protected final String TEXT_14 = NL + "    public static final AttributeType ";
  protected final String TEXT_15 = "_TYPE = build_";
  protected final String TEXT_16 = "_TYPE();" + NL + "     " + NL + "    private static AttributeType build_";
  protected final String TEXT_17 = "_TYPE() {" + NL + "        AttributeType builtType;";
  protected final String TEXT_18 = NL + "        List<PropertyDescriptor> schema = new ArrayList<PropertyDescriptor>();";
  protected final String TEXT_19 = NL + "        schema.add(" + NL + "            new AttributeDescriptorImpl(";
  protected final String TEXT_20 = NL + "                ";
  protected final String TEXT_21 = ", ";
  protected final String TEXT_22 = ", ";
  protected final String TEXT_23 = ", ";
  protected final String TEXT_24 = ", ";
  protected final String TEXT_25 = ", null" + NL + "            )" + NL + "        );";
  protected final String TEXT_26 = NL + "        builtType = new ComplexTypeImpl(" + NL + "            new NameImpl(\"";
  protected final String TEXT_27 = "\",\"";
  protected final String TEXT_28 = "\"), schema, ";
  protected final String TEXT_29 = ",";
  protected final String TEXT_30 = NL + "            ";
  protected final String TEXT_31 = ", ";
  protected final String TEXT_32 = ", ";
  protected final String TEXT_33 = ", ";
  protected final String TEXT_34 = NL + "        );";
  protected final String TEXT_35 = NL + "        builtType = new ComplexTypeImpl(" + NL + "            new NameImpl(\"";
  protected final String TEXT_36 = "\",\"";
  protected final String TEXT_37 = "\"), Collections.<PropertyDescriptor>emptyList(), ";
  protected final String TEXT_38 = ",";
  protected final String TEXT_39 = NL + "            ";
  protected final String TEXT_40 = ", ";
  protected final String TEXT_41 = ", ";
  protected final String TEXT_42 = ", ";
  protected final String TEXT_43 = NL + "        );";
  protected final String TEXT_44 = NL + "        builtType = new AttributeTypeImpl(" + NL + "            new NameImpl(\"";
  protected final String TEXT_45 = "\",\"";
  protected final String TEXT_46 = "\"), ";
  protected final String TEXT_47 = ", ";
  protected final String TEXT_48 = ",";
  protected final String TEXT_49 = NL + "            ";
  protected final String TEXT_50 = ", ";
  protected final String TEXT_51 = ", ";
  protected final String TEXT_52 = ", ";
  protected final String TEXT_53 = NL + "        );";
  protected final String TEXT_54 = NL + "           builtType.put(";
  protected final String TEXT_55 = ",";
  protected final String TEXT_56 = ");";
  protected final String TEXT_57 = NL + "        return builtType;" + NL + "    }" + NL;
  protected final String TEXT_58 = NL + NL + "    public ";
  protected final String TEXT_59 = "Schema() {" + NL + "        super(\"";
  protected final String TEXT_60 = "\");" + NL;
  protected final String TEXT_61 = NL + "        put(new NameImpl(\"";
  protected final String TEXT_62 = "\",\"";
  protected final String TEXT_63 = "\"),";
  protected final String TEXT_64 = "_TYPE);";
  protected final String TEXT_65 = NL + "    }" + NL + "    " + NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
      
    Object[] arguments = (Object[]) argument;
    Schema schema = (Schema) arguments[0];
    String prefix = (String) arguments[1];
    prefix = prefix.toUpperCase();
    
    SchemaGenerator sg = (SchemaGenerator) arguments[2];
    List types = sg.sort();

    stringBuffer.append(TEXT_2);
    
    Map<String, String> ns2import = new HashMap<>();
      for (Schema imported : sg.getImports()) {
          String fullClassName = imported.getClass().getName();
          String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);

          ns2import.put(imported.getURI(), className);

          stringBuffer.append(TEXT_3);
          stringBuffer.append(fullClassName);
          stringBuffer.append(TEXT_4);

      }

    stringBuffer.append(TEXT_5);
    stringBuffer.append(prefix);
    stringBuffer.append(TEXT_6);
    
    Map<Name, String> typeBindings = sg.getTypeBindings();

      for (Object value : types) {
          AttributeType type = (AttributeType) value;
          Name name = type.getName();

          stringBuffer.append(TEXT_7);

          StringWriter writer = new StringWriter();

          XSDTypeDefinition xsdType = sg.getXSDType(type);
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
              xmls.getTransformer().transform(new DOMSource(xsdType.getElement()),
                      new StreamResult(writer));
          } catch (Exception e) {
              e.printStackTrace();
              return null;
          }

          String[] lines = writer.getBuffer().toString().split("\n");
          for (String line : lines) {

              stringBuffer.append(TEXT_8);
              stringBuffer.append(line.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));

          }

          stringBuffer.append(TEXT_9);

          if (type instanceof ComplexType && !typeBindings.containsKey(name)) {

              stringBuffer.append(TEXT_10);
              stringBuffer.append(name.getLocalPart().toUpperCase());
              stringBuffer.append(TEXT_11);
              stringBuffer.append(name.getLocalPart().toUpperCase());
              stringBuffer.append(TEXT_12);
              stringBuffer.append(name.getLocalPart().toUpperCase());
              stringBuffer.append(TEXT_13);

          } else {

              stringBuffer.append(TEXT_14);
              stringBuffer.append(name.getLocalPart().toUpperCase());
              stringBuffer.append(TEXT_15);
              stringBuffer.append(name.getLocalPart().toUpperCase());
              stringBuffer.append(TEXT_16);
              stringBuffer.append(name.getLocalPart().toUpperCase());
              stringBuffer.append(TEXT_17);

          }
          String uri = name.getNamespaceURI();
          String local = name.getLocalPart();

          String binding;
          if (typeBindings.containsKey(name)) {
              binding = typeBindings.get(name) + ".class";
          } else {
              binding = type.getBinding().getName() + ".class";
          }
          String isIdentified = type.isIdentified() ? "true" : "false";
          String isAbstract = type.isAbstract() ? "true" : "false";

          String restrictions = "Collections.<Filter>emptyList()";
          String superType = "null";

          if (type.getSuper() != null) {
              superType = type.getSuper().getName()
                      .getLocalPart().toUpperCase() + "_TYPE";
              String superURI = type.getSuper().getName().getNamespaceURI();
              if (!uri.equals(superURI)) {
                  superType = ns2import.get(superURI) + "." + superType;
              }
          }

          String description = "null";
          if (type instanceof ComplexType && !typeBindings.containsKey(name)) {
              ComplexType cType = (ComplexType) type;

              if (!cType.getDescriptors().isEmpty()) {

                  stringBuffer.append(TEXT_18);

                  for (PropertyDescriptor pd : cType.getDescriptors()) {
                      if (!(pd instanceof AttributeDescriptor)) {
                          continue;
                      }

                      AttributeDescriptor ad = (AttributeDescriptor) pd;

                      AttributeType adType = ad.getType();

                      String adTypeName = adType.getName().getLocalPart().toUpperCase() +
                              "_TYPE";
                      String adTypeURI = adType.getName().getNamespaceURI();
                      if (!uri.equals(adTypeURI)) {
                          adTypeName = ns2import.get(adTypeURI) + "." + adTypeName;
                      }
                      String adName = "new NameImpl(\"" + ad.getName().getNamespaceURI() +
                              "\",\"" + ad.getName().getLocalPart() + "\")";
                      String min = ad.getMinOccurs() + "";
                      String max = ad.getMaxOccurs() + "";
                      String isNillable = ad.isNillable() ? "true" : "false";

                      stringBuffer.append(TEXT_19);
                      stringBuffer.append(TEXT_20);
                      stringBuffer.append(adTypeName);
                      stringBuffer.append(TEXT_21);
                      stringBuffer.append(adName);
                      stringBuffer.append(TEXT_22);
                      stringBuffer.append(min);
                      stringBuffer.append(TEXT_23);
                      stringBuffer.append(max);
                      stringBuffer.append(TEXT_24);
                      stringBuffer.append(isNillable);
                      stringBuffer.append(TEXT_25);

                  }

                  stringBuffer.append(TEXT_26);
                  stringBuffer.append(uri);
                  stringBuffer.append(TEXT_27);
                  stringBuffer.append(local);
                  stringBuffer.append(TEXT_28);
                  stringBuffer.append(isIdentified);
                  stringBuffer.append(TEXT_29);
                  stringBuffer.append(TEXT_30);
                  stringBuffer.append(isAbstract);
                  stringBuffer.append(TEXT_31);
                  stringBuffer.append(restrictions);
                  stringBuffer.append(TEXT_32);
                  stringBuffer.append(superType);
                  stringBuffer.append(TEXT_33);
                  stringBuffer.append(description);
                  stringBuffer.append(TEXT_34);

              } else {

                  stringBuffer.append(TEXT_35);
                  stringBuffer.append(uri);
                  stringBuffer.append(TEXT_36);
                  stringBuffer.append(local);
                  stringBuffer.append(TEXT_37);
                  stringBuffer.append(isIdentified);
                  stringBuffer.append(TEXT_38);
                  stringBuffer.append(TEXT_39);
                  stringBuffer.append(isAbstract);
                  stringBuffer.append(TEXT_40);
                  stringBuffer.append(restrictions);
                  stringBuffer.append(TEXT_41);
                  stringBuffer.append(superType);
                  stringBuffer.append(TEXT_42);
                  stringBuffer.append(description);
                  stringBuffer.append(TEXT_43);

              }
          } else {

              stringBuffer.append(TEXT_44);
              stringBuffer.append(uri);
              stringBuffer.append(TEXT_45);
              stringBuffer.append(local);
              stringBuffer.append(TEXT_46);
              stringBuffer.append(binding);
              stringBuffer.append(TEXT_47);
              stringBuffer.append(isIdentified);
              stringBuffer.append(TEXT_48);
              stringBuffer.append(TEXT_49);
              stringBuffer.append(isAbstract);
              stringBuffer.append(TEXT_50);
              stringBuffer.append(restrictions);
              stringBuffer.append(TEXT_51);
              stringBuffer.append(superType);
              stringBuffer.append(TEXT_52);
              stringBuffer.append(description);
              stringBuffer.append(TEXT_53);

          }

          if (!type.getUserData().isEmpty()) {
              //attributes
              for (Map.Entry<Object, Object> objectObjectEntry : type.getUserData().entrySet()) {
                  PropertyDescriptor pd = (PropertyDescriptor) objectObjectEntry.getValue();
                  PropertyType pdType = pd.getType();

                  String pdTypeName = pdType.getName().getLocalPart().toUpperCase() +
                          "_TYPE";
                  if (ns2import.containsKey(pdType.getName().getNamespaceURI())) {
                      String importClassName = ns2import.get(pdType.getName().getNamespaceURI());
                      pdTypeName = importClassName + "." + pdTypeName;
                  }
                  String pdName = "new NameImpl(\"" + pd.getName().getNamespaceURI() +
                          "\",\"" + pd.getName().getLocalPart() + "\")";


                  stringBuffer.append(TEXT_54);
                  stringBuffer.append(pdName);
                  stringBuffer.append(TEXT_55);
                  stringBuffer.append(pdTypeName);
                  stringBuffer.append(TEXT_56);


              }
          }

          stringBuffer.append(TEXT_57);

      }

    stringBuffer.append(TEXT_58);
    stringBuffer.append(prefix);
    stringBuffer.append(TEXT_59);
    stringBuffer.append(schema.getURI());
    stringBuffer.append(TEXT_60);

      for (Object o : types) {
          AttributeType type = (AttributeType) o;
          Name name = type.getName();

          String local = name.getLocalPart();

          stringBuffer.append(TEXT_61);
          stringBuffer.append(schema.getURI());
          stringBuffer.append(TEXT_62);
          stringBuffer.append(local);
          stringBuffer.append(TEXT_63);
          stringBuffer.append(local.toUpperCase());
          stringBuffer.append(TEXT_64);

      }

    stringBuffer.append(TEXT_65);
    return stringBuffer.toString();
  }
}
