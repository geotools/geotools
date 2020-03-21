/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.xs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import junit.framework.TestCase;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.util.XSDParser;
import org.geotools.util.URLs;
import org.geotools.xsd.Binding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Schemas;
import org.geotools.xsd.SimpleBinding;
import org.geotools.xsd.impl.BindingLoader;
import org.geotools.xsd.impl.ElementImpl;
import org.geotools.xsd.impl.PicoMap;
import org.picocontainer.defaults.DefaultPicoContainer;

public abstract class TestSchema extends TestCase {
    public static URL url;
    public static XSDSchema schema;
    public static XSDSchema xsd;
    public static BindingLoader factory;

    static {
        url = TestSchema.class.getResource("sample.xsd");

        try {
            schema = Schemas.parse(url.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        xsd = schema.getSchemaForSchema();

        Map bindings = new HashMap();

        new XSConfiguration().registerBindings(new PicoMap(bindings));
        factory = new BindingLoader(bindings);
    }

    protected XSDSimpleTypeDefinition typeDef;
    protected SimpleBinding strategy;
    protected QName qname;

    public TestSchema() {}

    public TestSchema(String name) {
        super(name);
    }

    /**
     * Limited to a search of simple types, no QName required.
     *
     * @return XSDSimpleTypeDefinition
     */
    public XSDSimpleTypeDefinition xsdSimple(String name) {
        Map simpleTypes = xsd.getSimpleTypeIdMap();

        // System.out.println( simpleTypes );
        return (XSDSimpleTypeDefinition) simpleTypes.get(name);
    }

    public QName xs(String name) throws Exception {
        Class xs = XS.class;
        Field[] fields = xs.getFields();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            if (field.getName().equalsIgnoreCase(name)) {
                return (QName) field.get(null);
            }
        }

        throw new IllegalArgumentException(name);
    }

    public Binding stratagy(QName qname) throws Exception {
        return factory.loadBinding(qname, new DefaultPicoContainer());
    }

    public Binding stratagy(String name) throws Exception {
        return factory.loadBinding(xs(name), new DefaultPicoContainer());
    }

    public ElementInstance element(String text, QName qname) {
        // create a fake element declaration and element instance
        XSDElementDeclaration declaration = XSDFactory.eINSTANCE.createXSDElementDeclaration();
        declaration.setTypeDefinition(xsdSimple(qname.getLocalPart()));

        ElementInstance element = new ElementImpl(declaration);
        element.setText(text);

        return element;
    }

    public ElementInstance element(String text, QName original, String name) {
        try {
            File temp = File.createTempFile("name", "xsd");
            FileWriter file = new FileWriter(temp);
            BufferedWriter buff = new BufferedWriter(file);
            PrintWriter print = new PrintWriter(buff);
            print.println(
                    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                            + "  <xsd:schema xmlns:my=\"http://mails/refractions/net\""
                            + "              xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\""
                            + "              targetNamespace=\"http://localhost//test\">"
                            + "  <xsd:element name=\""
                            + name
                            + "\" type=\"xsd:"
                            + original.getLocalPart()
                            + "\"/>"
                            + "</xsd:schema>");

            URL url = URLs.fileToUrl(temp);
            XSDParser parser = new XSDParser(Collections.emptyMap());
            parser.parse(url.toString());

            XSDSchema schema = parser.getSchema();
            Map map = schema.getSimpleTypeIdMap();

            return (ElementInstance) map.get(name);
        } catch (Throwable t) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", t);

            return null;
        }
    }

    /**
     * Will call the parse method on the strategy object, passing it <code>given</code> to use as a
     * value. It will then perform <code>assertEquals(expected, result);</code>
     *
     * @param given the value to pass to the parse method
     * @param expected used to compare against the result of the parse method
     */
    public void validateValues(String given, Object expected) throws Exception {
        Object result = strategy.parse(element(given, qname), given);
        assertEquals(expected, result);
    }

    /** Each subclass must indicate which kind of QName they wish to operate against. */
    protected abstract QName getQName();

    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();

        qname = getQName();

        if (qname != null) {
            typeDef = xsdSimple(qname.getLocalPart());
            strategy = (SimpleBinding) stratagy(qname);
        }
    }

    public void testSetUp() {
        if (getQName() != null) {
            assertNotNull("XSD typedef", typeDef);
            assertNotNull(strategy);
        }
    }
}
