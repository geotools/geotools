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

import org.xml.sax.SAXException;
import java.io.InputStream;
import java.lang.reflect.Constructor;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.geotools.xml.impl.ElementNameStreamingParserHandler;
import org.geotools.xml.impl.StreamingParserHandler;
import org.geotools.xml.impl.TypeStreamingParserHandler;

/**
 * XML parser capable of streaming.
 * <p>
 * Performs the same task as {@link org.geotools.xml.Parser}, with the addition
 * that objects are streamed back to the client. Streaming can occur in a
 * number of different modes.
 * </p>
 * <p>
 * As an example consider the following gml document:
 * <pre>
 * &lt;test:TestFeatureCollection xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *        xmlns:gml="http://www.opengis.net/gml"
 *        xmlns:test="http://www.geotools.org/test"
 *        xsi:schemaLocation="http://www.geotools.org/test test.xsd"&gt;
 *
 *   &lt;gml:featureMember&gt;
 *      &lt;test:TestFeature fid="0"&gt;
 *              ...
 *      &lt;/test:TestFeature&gt;
 *   &lt;/gml:featureMember&gt;
 *
 *   &lt;gml:featureMember&gt;
 *      &lt;test:TestFeature fid="1"&gt;
 *              ...
 *      &lt;/test:TestFeature&gt;
 *   &lt;/gml:featureMember&gt;
 *
 *   &lt;gml:featureMember&gt;
 *      &lt;test:TestFeature fid="2"&gt;
 *              ....
 *      &lt;/test:TestFeature&gt;
 *   &lt;/gml:featureMember&gt;
 *
 * &lt;/test:TestFeatureCollection&gt;
 * </pre>
 * And suppose we want to stream back each feature as it is parsed.
 * </p>
 * <p>
 *        <h3>1. Element Name</h3>
 *        Objects are streamed back when an element of a particular name has been
 *        parsed.
 *        <pre>
 *    Configuration configuration = new GMLConfiguration();
 *    QName elementName = new QName( "http://www.geotools.org/test", "TestFeature" );
 *
 *    StreamingParser parser = new StreamingParser( configuration, elementName );
 *
 *    Feature f = null;
 *    while ( ( f = parser.parse() ) != null ) {
 *       ...
 *    }
 *  </pre>
 * </p>
 * <p>
 *         <h3>2. Type</h3>
 *         Objects are streamed back when an element has been parsed into an object
 *         of a particular type.
 *  <pre>
 *    Configuration configuration = new GMLConfiguration();
 *    StreamingParser parser = new StreamingParser( configuration, Feature.class );
 *
 *    Feature f = null;
 *    while ( ( f = parser.parse() ) != null ) {
 *       ...
 *    }
 *  </pre>
 * </p>
 * <p>
 *         <h3>3. Xpath Expression</h3>
 *         Objects are streamed back when an element has been parsed which matches
 *         a particular xpath expression.
 *  <pre>
 *    Configuration configuration = new GMLConfiguration();
 *    String xpath = "//TestFeature";
 *    StreamingParser parser = new StreamingParser( configuration, xpath );
 *
 *    Feature f = null;
 *    while ( ( f = parser.parse() ) != null ) {
 *       ...
 *    }
 *  </pre>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL$
 */
public class StreamingParser {
    /**
     * The sax driver / handler.
     */
    private StreamingParserHandler handler;

    /**
     * The sax parser.
     */
    private SAXParser parser;

    /**
     * The xml input.
     */
    private InputStream input;

    /**
     * The parsing thread.
     */
    private Thread thread;

    /**
     * Creates a new instance of the type based streaming parser.
     *
     * @param configuration Object representing the configuration of the parser.
     * @param input The input stream representing the instance document to be parsed.
     * @param type The type of parsed objects to stream back.
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public StreamingParser(Configuration configuration, InputStream input, Class type)
        throws ParserConfigurationException, SAXException {
        this(configuration, input, new TypeStreamingParserHandler(configuration, type));
    }

    /**
     * Creates a new instance of the element name based streaming parser.
     *
     * @param configuration Object representing the configuration of the parser.
     * @param input The input stream representing the instance document to be parsed.
     * @param elementName The name of elements to stream back.
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public StreamingParser(Configuration configuration, InputStream input, QName elementName)
        throws ParserConfigurationException, SAXException {
        this(configuration, input, new ElementNameStreamingParserHandler(configuration, elementName));
    }

    /**
     * Creates a new instance of the xpath based streaming parser.
     *
     * @param configuration Object representing the configuration of the parser.
     * @param input The input stream representing the instance document to be parsed.
     * @param xpath An xpath expression which dictates how the parser streams
     * objects back to the client.
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public StreamingParser(Configuration configuration, InputStream input, String xpath)
        throws ParserConfigurationException, SAXException {
        this(configuration, input, createJXpathStreamingParserHandler(configuration,xpath));
    }

    /**
     * Method for dynamic creation of the xpath streaming parser handler.
     * <p>
     * We do this to allow the jxpath component to be removed... and avoid its 
     * dependencies.
     * </p>
     * @param configuration
     * @param xpath
     * @return
     */
    static StreamingParserHandler createJXpathStreamingParserHandler(Configuration configuration, String xpath)
        throws ParserConfigurationException {
        
        Class clazz;
        try {
            clazz = Class.forName( "org.geotools.xml.impl.jxpath.JXPathStreamingParserHandler");
        } catch (ClassNotFoundException e) {
            throw (ParserConfigurationException) new ParserConfigurationException().initCause(e);
        }
        
        Constructor c;
        try {
            c = clazz.getConstructor(new Class[]{Configuration.class,String.class});
            return (StreamingParserHandler) c.newInstance(new Object[]{configuration,xpath});
        }
        catch( Exception e ) {
            //shoudl not happen
            throw new RuntimeException( e );
        }
        
        //return new JXPathStreamingParserHandler(configuration, xpath)    
    }
    
    /**
     * Internal constructor.
     */
    protected StreamingParser(Configuration configuration, InputStream input,
        StreamingParserHandler handler) throws ParserConfigurationException, SAXException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        parser = spf.newSAXParser();

        this.handler = handler;
        this.input = input;
    }

    /**
     * Streams the parser to the next element in the instance document which
     * matches the xpath query specified in the contstructor. This method
     * returns null when there are no more objects to stream.
     *
     * @return The next object in the stream, or null if no such object is
     * available.
     */
    public Object parse() {
        if (thread == null) {
            Runnable runnable = new Runnable() {
                    public void run() {
                        try {
                            parser.parse(input, handler);
                        } catch (Exception e) {
                            //close the buffer
                            handler.getBuffer().close();
                            throw new RuntimeException(e);
                        }
                    }
                    ;
                };

            thread = new Thread(runnable);
            thread.start();
        }

        return handler.getBuffer().get();
    }
}
