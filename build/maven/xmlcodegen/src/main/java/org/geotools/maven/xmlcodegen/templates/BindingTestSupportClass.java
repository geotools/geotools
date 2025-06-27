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

import org.eclipse.xsd.XSDSchema;
import org.geotools.xsd.Schemas;

public class BindingTestSupportClass
{
  protected static String nl;
  public static synchronized BindingTestSupportClass create(String lineSeparator)
  {
    nl = lineSeparator;
    BindingTestSupportClass result = new BindingTestSupportClass();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? System.getProperties().getProperty("line.separator") : nl;
  protected final String TEXT_1 = NL + "import org.geotools.xsd.Configuration;" + NL + "import org.geotools.xml.test.XMLTestSupport;" + NL + "" + NL + "/**" + NL + " * Base test class for the ";
  protected final String TEXT_2 = " schema." + NL + " *" + NL + " * @generated" + NL + " */" + NL + "public class ";
  protected final String TEXT_3 = "TestSupport extends XMLTestSupport {" + NL + "" + NL + "    protected Configuration createConfiguration() {" + NL + "       return new ";
  protected final String TEXT_4 = "Configuration();" + NL + "    }" + NL + "  " + NL + "} ";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
      
    Object[] arguments = (Object[])argument;
    XSDSchema schema = (XSDSchema)arguments[0];
    
    String namespace = schema.getTargetNamespace();
    String prefix = Schemas.getTargetPrefix( schema ).toUpperCase();
 
    stringBuffer.append(TEXT_1);
    stringBuffer.append(namespace);
    stringBuffer.append(TEXT_2);
    stringBuffer.append(prefix);
    stringBuffer.append(TEXT_3);
    stringBuffer.append(prefix);
    stringBuffer.append(TEXT_4);
    return stringBuffer.toString();
  }
}
