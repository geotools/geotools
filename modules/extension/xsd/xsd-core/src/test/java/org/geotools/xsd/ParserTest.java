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
package org.geotools.xsd;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.xml.namespace.QName;
import org.geotools.ml.MLConfiguration;
import org.geotools.ml.Mail;
import org.geotools.ml.bindings.MLSchemaLocationResolver;
import org.geotools.xsd.impl.Handler;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.EntityResolver2;

public class ParserTest {
    @Test
    public void testParseEntityResolver() throws Exception {
        Parser parser = new Parser(new XMLConfiguration());
        AtomicBoolean resolverUsed = new AtomicBoolean(false);
        parser.setEntityResolver(
                new EntityResolver2() {
                    @Override
                    public InputSource getExternalSubset(String name, String baseURI)
                            throws SAXException, IOException {
                        return null;
                    }

                    @Override
                    public InputSource resolveEntity(
                            String name, String publicId, String baseURI, String systemId)
                            throws SAXException, IOException {
                        if (systemId.equals("./mails.xsd")) resolverUsed.set(true);
                        return null;
                    }

                    @Override
                    public InputSource resolveEntity(String publicId, String systemId)
                            throws SAXException, IOException {
                        if (systemId.equals("./mails.xsd")) resolverUsed.set(true);
                        return null;
                    }
                });
        parser.parse(MLSchemaLocationResolver.class.getResourceAsStream("mails-local-schema.xml"));
        assertTrue("The resolver was not used?", resolverUsed.get());
    }

    @Test
    public void testParse() throws Exception {
        Parser parser = new Parser(new MLConfiguration());
        List mails =
                (List)
                        parser.parse(
                                MLSchemaLocationResolver.class.getResourceAsStream("mails.xml"));

        Assert.assertEquals(2, mails.size());

        Mail mail = (Mail) mails.get(0);
        Assert.assertEquals(0, mail.getId().intValue());

        mail = (Mail) mails.get(1);
        Assert.assertEquals(1, mail.getId().intValue());
    }

    @Test
    public void testParseValid() throws Exception {
        Parser parser = new Parser(new MLConfiguration());
        parser.setValidating(true);
        parser.parse(MLSchemaLocationResolver.class.getResourceAsStream("mails.xml"));

        Assert.assertEquals(0, parser.getValidationErrors().size());
    }

    @Test
    public void testParseNull() throws Exception {
        Parser parser = new Parser(new MLConfiguration());
        parser.setValidating(true);
        List mails =
                (List)
                        parser.parse(
                                MLSchemaLocationResolver.class.getResourceAsStream(
                                        "null-mail.xml"));

        Assert.assertEquals(0, parser.getValidationErrors().size());
        Assert.assertEquals(1, mails.size());

        Mail mail = (Mail) mails.get(0);
        Assert.assertNull(mail.getBody());
    }

    @Test
    public void testParseInValid() throws Exception {
        Parser parser = new Parser(new MLConfiguration());
        parser.setValidating(true);
        parser.parse(MLSchemaLocationResolver.class.getResourceAsStream("mails-invalid.xml"));

        Assert.assertNotEquals(0, parser.getValidationErrors().size());

        // test immeediate failure case
        parser.setFailOnValidationError(true);
        try {
            parser.parse(MLSchemaLocationResolver.class.getResourceAsStream("mails-invalid.xml"));
            Assert.fail("should have thrown an error with setFailOnValidationError set");
        } catch (SAXException e) {
        }
    }

    @Test
    public void testValidate() throws Exception {
        Parser parser = new Parser(new MLConfiguration());
        parser.validate(MLSchemaLocationResolver.class.getResourceAsStream("mails-invalid.xml"));

        Assert.assertNotEquals(0, parser.getValidationErrors().size());

        // test immeediate failure case
        parser.setFailOnValidationError(true);
        try {
            parser.validate(
                    MLSchemaLocationResolver.class.getResourceAsStream("mails-invalid.xml"));
            Assert.fail("should have thrown an error with setFailOnValidationError set");
        } catch (SAXException e) {
        }
    }

    @Test
    public void testParserDelegate() throws Exception {
        MLConfiguration config = new MLConfiguration();

        MyParserDelegate delegate = new MyParserDelegate();
        Assert.assertFalse(delegate.foo);
        Assert.assertFalse(delegate.bar);

        config.getContext().registerComponentInstance(delegate);

        Parser parser = new Parser(config);
        parser.parse(ParserTest.class.getResourceAsStream("parserDelegate.xml"));

        assertTrue(delegate.foo);
        assertTrue(delegate.bar);
    }

    static class MyParserDelegate implements ParserDelegate, ParserDelegate2 {

        boolean foo = false;
        boolean bar = false;

        public boolean canHandle(QName elementName) {
            return "parserDelegateElement".equals(elementName.getLocalPart());
        }

        public void initialize(QName elementName) {}

        @Override
        public Object getParsedObject() {
            return null;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (!bar && "bar".equals(new String(ch, start, length))) {
                bar = true;
            }
        }

        @Override
        public void endDocument() throws SAXException {}

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {}

        @Override
        public void endPrefixMapping(String prefix) throws SAXException {}

        @Override
        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}

        @Override
        public void processingInstruction(String target, String data) throws SAXException {}

        @Override
        public void setDocumentLocator(Locator locator) {}

        @Override
        public void skippedEntity(String name) throws SAXException {}

        @Override
        public void startDocument() throws SAXException {}

        @Override
        public void startElement(String uri, String localName, String name, Attributes atts)
                throws SAXException {
            if (!foo && "foo".equals(localName)) {
                foo = true;
            }
        }

        @Override
        public void startPrefixMapping(String prefix, String uri) throws SAXException {}

        @Override
        public boolean canHandle(
                QName elementName, Attributes attributes, Handler handler, Handler parent) {
            return canHandle(elementName);
        }
    }

    @Test
    public void testMixedContent() throws Exception {
        final StringBuffer sb = new StringBuffer();
        XSD xsd =
                new XSD() {
                    @Override
                    public String getSchemaLocation() {
                        return ParserTest.class.getResource("mixed.xsd").getFile();
                    }

                    @Override
                    public String getNamespaceURI() {
                        return "http://geotools.org/test";
                    }
                };
        Configuration cfg =
                new Configuration(xsd) {
                    @Override
                    protected void registerBindings(Map<QName, Object> bindings) {
                        bindings.put(
                                new QName("http://geotools.org/test", "MixedType"),
                                new MixedTypeBinding(sb));
                    }

                    @Override
                    protected void configureParser(Parser parser) {
                        parser.setHandleMixedContent(true);
                    }
                };

        Parser p = new Parser(cfg);

        p.parse(getClass().getResourceAsStream("mixed1.xml"));
        Assert.assertEquals("Hello 'there' how are 'you'?", sb.toString());

        sb.setLength(0);
        p.parse(getClass().getResourceAsStream("mixed2.xml"));
        Assert.assertEquals("Hello 'there' how are 'you' ?", sb.toString());
    }

    public static class MixedTypeBinding extends AbstractComplexBinding {
        StringBuffer sb = new StringBuffer();

        public MixedTypeBinding(StringBuffer sb) {
            this.sb = sb;
        }

        @Override
        public QName getTarget() {
            return new QName("http://geotools.org/test", "MixedType");
        }

        @Override
        public Class getType() {
            return Object.class;
        }

        @Override
        public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
            for (Node n : node.getChildren()) {
                if (n.getValue() instanceof Text) {
                    sb.append(((Text) n.getValue()).getValue());
                } else {
                    sb.append("'").append(n.getValue()).append("'");
                }
            }
            return value;
        }
    }

    /**
     * Test Parser with an XML document containing an external entity: <!ENTITY c SYSTEM
     * "file:///this/file/does/not/exist">
     */
    @Test
    public void testParseWithEntityResolver() throws Exception {
        Parser parser = new Parser(new MLConfiguration());

        try {
            parser.parse(
                    MLSchemaLocationResolver.class.getResourceAsStream(
                            "mails-external-entities.xml"));
            Assert.fail("parsing should throw an exception since referenced file does not exist");
        } catch (FileNotFoundException e) {
        }
        try {
            parser.validate(
                    MLSchemaLocationResolver.class.getResourceAsStream(
                            "mails-external-entities.xml"));
            Assert.fail(
                    "validating should throw an exception since referenced file does not exist");
        } catch (FileNotFoundException e) {
        }

        // Set an EntityResolver implementation to prevent usage of external entities.
        // When parsing an XML entity, the empty InputSource returned by this resolver provokes
        // a java.net.MalformedURLException
        parser.setEntityResolver(
                new EntityResolver2() {
                    @Override
                    public InputSource resolveEntity(String publicId, String systemId)
                            throws SAXException, IOException {
                        return new InputSource();
                    }

                    @Override
                    public InputSource getExternalSubset(String name, String baseURI)
                            throws SAXException, IOException {
                        return new InputSource();
                    }

                    @Override
                    public InputSource resolveEntity(
                            String name, String publicId, String baseURI, String systemId)
                            throws SAXException, IOException {
                        return new InputSource();
                    }
                });

        try {
            parser.parse(
                    MLSchemaLocationResolver.class.getResourceAsStream(
                            "mails-external-entities.xml"));
            Assert.fail(
                    "parsing an XML with external entities should throw a MalformedURLException");
        } catch (MalformedURLException e) {
        }
        try {
            parser.validate(
                    MLSchemaLocationResolver.class.getResourceAsStream(
                            "mails-external-entities.xml"));
            Assert.fail(
                    "validating an XML with external entities should throw a MalformedURLException");
        } catch (MalformedURLException e) {
        }

        // Set another EntityResolver
        parser.setEntityResolver(
                new EntityResolver2() {
                    @Override
                    public InputSource resolveEntity(String publicId, String systemId)
                            throws SAXException, IOException {
                        if ("file:///this/file/does/not/exist".equals(systemId)) {
                            return new InputSource(new StringReader("hello"));
                        } else {
                            return new InputSource();
                        }
                    }

                    @Override
                    public InputSource getExternalSubset(String name, String baseURI)
                            throws SAXException, IOException {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public InputSource resolveEntity(
                            String name, String publicId, String baseURI, String systemId)
                            throws SAXException, IOException {
                        if ("file:///this/file/does/not/exist".equals(systemId)) {
                            return new InputSource(new StringReader("hello"));
                        } else {
                            return new InputSource();
                        }
                    }
                });

        // parsing shouldn't throw an exception
        parser.parse(
                MLSchemaLocationResolver.class.getResourceAsStream("mails-external-entities.xml"));
        parser.validate(
                MLSchemaLocationResolver.class.getResourceAsStream("mails-external-entities.xml"));
    }

    /** Tests returned exception caused by entity expansion limit configuration on Parser. */
    @Test
    public void testEntityExpansionLimitException() throws Exception {
        final StringBuffer sb = new StringBuffer();
        XSD xsd =
                new XSD() {
                    @Override
                    public String getSchemaLocation() {
                        return ParserTest.class.getResource("mixed.xsd").getFile();
                    }

                    @Override
                    public String getNamespaceURI() {
                        return "http://geotools.org/test";
                    }
                };
        Configuration cfg =
                new Configuration(xsd) {
                    @Override
                    protected void registerBindings(Map<QName, Object> bindings) {
                        bindings.put(
                                new QName("http://geotools.org/test", "MixedType"),
                                new MixedTypeBinding(sb));
                    }

                    @Override
                    protected void configureParser(Parser parser) {
                        parser.setHandleMixedContent(true);
                    }
                };

        Parser p = new Parser(cfg);
        p.setEntityExpansionLimit(1);
        SAXParseException expected = null;
        try {
            p.parse(getClass().getResourceAsStream("entityExpansionLimit.xml"));
        } catch (SAXParseException ex) {
            expected = ex;
        }
        Assert.assertNotNull(expected);
        // check for the entity expansion limit error code in exception message
        assertTrue(expected.getMessage().contains("JAXP00010001"));
    }

    /** Tests entity expansion limit configuration on Parser. */
    @Test
    public void testEntityExpansionLimitAllowed() throws Exception {
        final StringBuffer sb = new StringBuffer();
        XSD xsd =
                new XSD() {
                    @Override
                    public String getSchemaLocation() {
                        return ParserTest.class.getResource("mixed.xsd").getFile();
                    }

                    @Override
                    public String getNamespaceURI() {
                        return "http://geotools.org/test";
                    }
                };
        Configuration cfg =
                new Configuration(xsd) {
                    @Override
                    protected void registerBindings(Map<QName, Object> bindings) {
                        bindings.put(
                                new QName("http://geotools.org/test", "MixedType"),
                                new MixedTypeBinding(sb));
                    }

                    @Override
                    protected void configureParser(Parser parser) {
                        parser.setHandleMixedContent(true);
                    }
                };

        Parser p = new Parser(cfg);
        p.setEntityExpansionLimit(100);
        SAXParseException unexpected = null;
        try {
            p.parse(getClass().getResourceAsStream("entityExpansionLimit.xml"));
        } catch (SAXParseException ex) {
            unexpected = ex;
        }
        Assert.assertNull(unexpected);
    }
}
