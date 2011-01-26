package org.geotools.maven.xmlcodegen.templates;

import java.util.*;
import java.io.*;
import org.eclipse.xsd.*;
import org.geotools.xml.*;

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

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = NL + "import org.geotools.xml.Configuration;" + NL + "import org.geotools.xml.test.XMLTestSupport;" + NL + "" + NL + "/**" + NL + " * Base test class for the ";
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
