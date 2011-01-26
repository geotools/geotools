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
package org.geotools.xml;

import junit.framework.TestCase;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.geotools.ml.MLConfiguration;
import org.geotools.ml.Mail;
import org.geotools.ml.bindings.MLSchemaLocationResolver;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;


public class ParserTest extends TestCase {
    public void testParse() throws Exception {
        Parser parser = new Parser(new MLConfiguration());
        List mails = (List) parser.parse(MLSchemaLocationResolver.class.getResourceAsStream(
                    "mails.xml"));

        assertEquals(2, mails.size());

        Mail mail = (Mail) mails.get(0);
        assertEquals(0, mail.getId().intValue());

        mail = (Mail) mails.get(1);
        assertEquals(1, mail.getId().intValue());
    }

    public void testParseValid() throws Exception {
        Parser parser = new Parser(new MLConfiguration());
        parser.setValidating(true);
        parser.parse(MLSchemaLocationResolver.class.getResourceAsStream("mails.xml"));

        assertEquals(0, parser.getValidationErrors().size());
    }

    public void testParseInValid() throws Exception {
        Parser parser = new Parser(new MLConfiguration());
        parser.setValidating(true);
        parser.parse(MLSchemaLocationResolver.class.getResourceAsStream("mails-invalid.xml"));

        assertFalse(0 == parser.getValidationErrors().size());
        
        //test immeediate failure case
        parser.setFailOnValidationError(true);
        try {
            parser.parse(MLSchemaLocationResolver.class.getResourceAsStream("mails-invalid.xml"));    
            fail( "should have thrown an error with setFailOnValidationError set");
        }
        catch( SAXException e ) {
        }
    }
    
    public void testValidate() throws Exception {
        Parser parser = new Parser(new MLConfiguration());
        parser.validate(MLSchemaLocationResolver.class.getResourceAsStream("mails-invalid.xml"));

        assertFalse(0 == parser.getValidationErrors().size());
        
        //test immeediate failure case
        parser.setFailOnValidationError(true);
        try {
            parser.validate(MLSchemaLocationResolver.class.getResourceAsStream("mails-invalid.xml"));    
            fail( "should have thrown an error with setFailOnValidationError set");
        }
        catch( SAXException e ) {
        }
    }
    
    public void testParserDelegate() throws Exception {
        MLConfiguration config = new MLConfiguration();
        
        MyParserDelegate delegate = new MyParserDelegate();
        assertFalse( delegate.foo );
        assertFalse( delegate.bar );
        
        config.getContext().registerComponentInstance(  delegate );
       
        Parser parser = new Parser(config);
        Object o = parser.parse( ParserTest.class.getResourceAsStream( "parserDelegate.xml") );
    
        assertTrue( delegate.foo );
        assertTrue( delegate.bar );
    }
    
    static class MyParserDelegate implements ParserDelegate {

        boolean foo = false;
        boolean bar = false;
        
        public boolean canHandle(QName elementName) {
            return "parserDelegateElement".equals( elementName.getLocalPart() );
        }
        
        public void initialize(QName elementName) {
        }

        public Object getParsedObject() {
            return null;
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            if ( !bar && "bar".equals( new String( ch, start, length ) ) ) {
                bar = true;
            }
        }

        public void endDocument() throws SAXException {
        }

        public void endElement(String uri, String localName, String name)
                throws SAXException {
        }

        public void endPrefixMapping(String prefix) throws SAXException {
        }

        public void ignorableWhitespace(char[] ch, int start, int length)
                throws SAXException {
        }

        public void processingInstruction(String target, String data)
                throws SAXException {
        }

        public void setDocumentLocator(Locator locator) {
        }

        public void skippedEntity(String name) throws SAXException {
        }

        public void startDocument() throws SAXException {
        }

        public void startElement(String uri, String localName, String name,
                Attributes atts) throws SAXException {
            if ( !foo && "foo".equals( localName ) ) {
                foo = true;
            }
        }

        public void startPrefixMapping(String prefix, String uri)
                throws SAXException {
        }
        
    }
    
    public void testMixedContent() throws Exception {
        final StringBuffer sb = new StringBuffer();
        XSD xsd = new XSD() {
            @Override
            public String getSchemaLocation() {
                return ParserTest.class.getResource("mixed.xsd").getFile();
            }
            
            @Override
            public String getNamespaceURI() {
                return "http://geotools.org/test";
            }
        };
        Configuration cfg = new Configuration(xsd) {
            @Override
            protected void registerBindings(Map bindings) {
                bindings.put(new QName("http://geotools.org/test", "MixedType"), 
                        new MixedTypeBinding(sb));
            }

            @Override
            protected void configureParser(Parser parser) {
                parser.setHandleMixedContent(true);
            }
        };
    
        Parser p = new Parser(cfg);
        
        p.parse(getClass().getResourceAsStream("mixed1.xml"));
        assertEquals("Hello 'there' how are 'you'?", sb.toString());
        
        sb.setLength(0);
        p.parse(getClass().getResourceAsStream("mixed2.xml"));
        assertEquals("Hello 'there' how are 'you' ?", sb.toString());
    }
    
    public static class MixedTypeBinding extends AbstractComplexBinding {
        StringBuffer sb = new StringBuffer();
        
        public MixedTypeBinding(StringBuffer sb) {
            this.sb = sb;
        }
        
        public QName getTarget() {
            return new QName("http://geotools.org/test", "MixedType");
        }

        public Class getType() {
           return Object.class;
        }
        
        @Override
        public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
            for (Node n : ((List<Node>)node.getChildren())) {
                if (n.getValue() instanceof Text) {
                    sb.append(((Text)n.getValue()).getValue());
                }
                else {
                    sb.append("'").append(n.getValue()).append("'");
                }
            }
            return value;
        }
    }
}
