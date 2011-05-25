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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.parsers.SAXParser;
import org.eclipse.xsd.XSDSchema;
import org.geotools.xml.impl.ParserHandler;
import org.geotools.xs.XS;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.NamespaceSupport;


/**
 * GeoTools XML parser.
 * <p>
 * This parser uses a sax based driver to parse an input stream into a single object. For streaming
 * look at {@link StreamingParser}. If the source document being parsed as already been parsed into
 * a {@link Document} the {@link DOMParser} class may be used.
 * </p>
 * <p>
 * <h3>Schema Resolution</h3>
 * See {@link org.geotools.xml.Configuration} javadocs for instructions on how
 * to customize schema resolution. This is often desirable in the case that
 * the instance document being parsed contains invalid uri's in schema imports
 * and includes.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class Parser {
    /** sax handler which maintains the element stack */
    private ParserHandler handler;

    /** the sax parser driving the handler */
    private SAXParser parser;

    /** the instance document being parsed */
    private InputStream input;

    /**
     * Creats a new instance of the parser.
     *
     * @param configuration The parser configuration, bindings and context,
     *         must never be <code>null</code>.
     *
     */
    public Parser(Configuration configuration) {
        if (configuration == null) {
            throw new NullPointerException("configuration");
        }

        handler = new ParserHandler(configuration);
        configuration.setupParser(this);
    }

    /**
     * Creates a new instance of the parser.
     *
     * @param configuration Object representing the configuration of the parser.
     * @param input A uri representing the instance document to be parsed.
     *
     * @throws ParserConfigurationException
     * @throws SAXException If a sax parser can not be created.
     * @throws URISyntaxException If <code>input</code> is not a valid uri.
     *
     * @deprecated use {@link #Parser(Configuration)} and {@link #parse(InputStream)}.
     */
    public Parser(Configuration configuration, String input)
        throws IOException, URISyntaxException {
        this(configuration, new BufferedInputStream(new FileInputStream(new File(new URI(input)))));
    }

    /**
     * Creates a new instance of the parser.
     *
     * @param configuration Object representing the configuration of the parser.
     * @param input The stream representing the instance document to be parsed.
     *
     * @deprecated use {@link #Parser(Configuration)} and {@link #parse(InputStream)}.
     */
    public Parser(Configuration configuration, InputStream input) {
        this(configuration);
        this.input = input;
    }

    /**
     * @return The underlying parser handler.
     */
    ParserHandler getParserHandler() {
        return handler;
    }
    
    /**
     * Signals the parser to parse the entire instance document. The object
     * returned from the parse is the object which has been bound to the root
     * element of the document. This method should only be called once for
     * a single instance document.
     *
     * @return The object representation of the root element of the document.
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     *
     * @deprecated use {@link #parse(InputStream)}
     */
    public Object parse() throws IOException, SAXException, ParserConfigurationException {
        return parse(input);
    }

    /**
     * Parses an instance documented defined by an input stream.
     * <p>
     * The object returned from the parse is the object which has been bound to the root
     * element of the document. This method should only be called once for a single instance document.
     * </p>
     *
     * @return The object representation of the root element of the document.
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public Object parse(InputStream input)
        throws IOException, SAXException, ParserConfigurationException {
        return parse(new InputSource(input));
    }

    /**
     * Parses an instance documented defined by a reader.
     * <p>
     * The object returned from the parse is the object which has been bound to the root
     * element of the document. This method should only be called once for a single instance document.
     * </p>
     *
     * @return The object representation of the root element of the document.
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public Object parse(Reader reader)
        throws IOException, SAXException, ParserConfigurationException {
        return parse(new InputSource(reader));
    }

    /**
     * Parses an instance document defined by a transformer source.
     * <p>
     * Note: Currently this method reads the entire source into memory in order to validate
     * it. If large documents must be parsed one of {@link #
     * </p>
     * @param source THe source of the instance document.
     *
     * @return @return The object representation of the root element of the document.
     * 
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * 
     * @since 2.6
     */
    public Object parse(Source source) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        //TODO: use SAXResult to stream, need to figure out how to enable 
        // validation with transformer api
        //SAXResult result = new SAXResult( handler );
        StreamResult result = new StreamResult( new ByteArrayOutputStream() );
        
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer tx = tf.newTransformer();
        
        tx.transform( source, result );
        
        return parse( new ByteArrayInputStream( ((ByteArrayOutputStream)result.getOutputStream()).toByteArray() ) );
    }
    

    /**
     * Parses an instance documented defined by a sax input source.
     * <p>
     * The object returned from the parse is the object which has been bound to the root
     * element of the document. This method should only be called once for a single instance document.
     * </p>
     *
     * @return The object representation of the root element of the document.
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public Object parse(InputSource source)
        throws IOException, SAXException, ParserConfigurationException {
        parser = parser();
        parser.setContentHandler(handler);
        parser.setErrorHandler(handler);

        parser.parse(source);

        return handler.getValue();
    }

    /**
     * Sets the strict parsing flag.
     * <p>
     * When set to <code>true</code>, this will cause the parser to operate in
     * a strict mode, which means that xml being parsed must be exactly correct
     * with respect to the schema it references.
     * </p>
     * <p>
     * Some examples of cases in which the parser will throw an exception while
     * operating in strict mode:
     * <ul>
     *  <li>no 'schemaLocation' specified, or specified incorrectly
     *  <li>element found which is not declared in the schema
     * </ul>
     * </p>
     * @param strict The strict flag.
     */
    public void setStrict(boolean strict) {
        handler.setStrict(strict);
    }

    /**
     * Sets the flag controlling wether the parser should validate or not.
     *
     * @param validating Validation flag, <code>true</code> to validate, otherwise <code>false</code>
     */
    public void setValidating(boolean validating) {
        handler.setValidating(validating);
    }

    /**
     * @return Flag determining if the parser is validatin or not.
     */
    public boolean isValidating() {
        return handler.isValidating();
    }
    
    /**
     * Sets the flag which controls how the parser handles validation errors.
     * <p>
     * When this flag is set, the parser will throw an exception when it encounters 
     * a validation error. Otherise the error will be stored, retreivable from 
     * {@link #getValidationErrors()}.
     * </p>
     * <p>
     * The default behavior is to set this flag to <code>false</code>. So client
     * code should explicitly set this flag if it is desired that the exception 
     * be thrown when the validation error occurs.
     * </p>
     * @param fail failure flag, <code>true</code> to fail, otherwise <code>false</code>
     */
    public void setFailOnValidationError( boolean fail ) {
        handler.setFailOnValidationError( fail );
    }
    
    /**
     * @return The flag determining how the parser deals with validation errors.
     */
    public boolean isFailOnValidationError() {
        return handler.isFailOnValidationError();
    }
    
    /**
     * Sets flag that controls whether the parser will process mixed content in a way 
     * that preserves order of child elements and text.
     * 
     * @since 2.7
     */
    public void setHandleMixedContent(boolean handleMixedContent) {
        handler.setHandleMixedContent(handleMixedContent);
    }
    
    /**
     * Flag that controls whether the parser will process mixed content in a way 
     * that preserves order of child elements and text.
     * <p>
     * By default the parser will simply concatenate blindly all child text and not preserve order
     * with respect to other elements within a mixed content type.
     * </p>
     * 
     * @since 2.7
     */
    public boolean isHandleMixedContent() {
        return handler.isHandleMixedContent();
    }
    
    /**
     * Returns a list of any validation errors that occured while parsing.
     *
     * @return A list of errors, or an empty list if none.
     */
    public List getValidationErrors() {
        return handler.getValidationErrors();
    }

    /**
     * Validates an instance document defined by a input stream.
     * <p>
     * Clients should call {@link #getValidationErrors()} after this method to 
     * retrieve any validation errors that occurred. Clients do not need to call 
     * {@link #setValidating(boolean)} when using this method to validate. 
     * </p>
     * <p>
     * This method does not do any of the work done by {@link #parse(InputSource)}, it
     * only validates. 
     * </p>
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void validate( InputStream in ) throws IOException, SAXException, ParserConfigurationException {
        validate( new InputSource( in ) );
    }

    /**
     * Validates an instance document defined by a reader.
     * <p>
     * Clients should call {@link #getValidationErrors()} after this method to 
     * retrieve any validation errors that occurred. Clients do not need to call 
     * {@link #setValidating(boolean)} when using this method to validate. 
     * </p>
     * <p>
     * This method does not do any of the work done by {@link #parse(InputSource)}, it
     * only validates. 
     * </p>
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void validate( Reader reader ) throws IOException, SAXException, ParserConfigurationException {
        validate( new InputSource( reader ) );
    }
    
    /**
     * Validates an instance document defined by a input source.
     * <p>
     * Clients should call {@link #getValidationErrors()} after this method to 
     * retrieve any validation errors that occurred. Clients do not need to call 
     * {@link #setValidating(boolean)} when using this method to validate. 
     * </p>
     * <p>
     * This method does not do any of the work done by {@link #parse(InputSource)}, it
     * only validates. 
     * </p>
     *
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public void validate( InputSource source ) throws IOException, SAXException, ParserConfigurationException {
        SAXParser parser = parser( true );
        parser.setContentHandler( handler.getValidator() );
        parser.setErrorHandler( handler.getValidator() );
        parser.parse( source );
    }

    /**
     * Returns the schema objects referenced by the instance document being
     * parsed. This method can only be called after a successful parse has
     * begun.
     *
     * @return The schema objects used to parse the document, or null if parsing
     * has not commenced.
     */
    public XSDSchema[] getSchemas() {
        if (handler != null) {
            return handler.getSchemas();
        }

        return null;
    }

    /**
     * Returns the namespace mappings maintained by the parser.
     * <p>
     * Clients may register additional namespace mappings. This is useful when
     * an application whishes to provide some "default" namespace mappings.
     * </p>
     * <p>
     * Clients should register namespace mappings in the current "context", ie
     * do not call {@link NamespaceSupport#pushContext()}. Example:
     * <code>
     * Parser parser = new Parser( ... );
     * parser.getNamespaces().declarePrefix( "foo", "http://www.foo.com" );
     * ...
     * </code>
     * </p>
     *
     * @return The namespace support containing prefix to uri mappings.
     * @since 2.4
     */
    public NamespaceSupport getNamespaces() {
        return handler.getNamespaceSupport();
    }

    protected SAXParser parser() throws ParserConfigurationException, SAXException {
        return parser( isValidating() );
    }
    
    protected SAXParser parser(boolean validate) throws ParserConfigurationException, SAXException {
        //JD: we use xerces directly here because jaxp does seem to allow use to 
        // override all the namespaces to validate against
        SAXParser parser = new SAXParser();

        //set the appropriate features
        parser.setFeature("http://xml.org/sax/features/namespaces", true);

        if (validate) {
            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setFeature("http://apache.org/xml/features/validation/schema", true);
            parser.setFeature("http://apache.org/xml/features/validation/schema-full-checking", true);
        }

        //set the schema sources of this configuration, and all dependent ones
        StringBuffer schemaLocation = new StringBuffer();

        for (Iterator d = handler.getConfiguration().allDependencies().iterator(); d.hasNext();) {
            Configuration dependency = (Configuration) d.next();

            //ignore xs namespace
            if (XS.NAMESPACE.equals(dependency.getNamespaceURI())) {
                continue;
            }

            //seperate entries by space
            if (schemaLocation.length() > 0) {
                schemaLocation.append(" ");
            }

            //add the entry
            schemaLocation.append(dependency.getNamespaceURI());
            schemaLocation.append(" ");
            schemaLocation.append(dependency.getSchemaFileURL());
        }

        //set hte property to map namespaces to schema locations
        parser.setProperty("http://apache.org/xml/properties/schema/external-schemaLocation",
            schemaLocation.toString());

        //set the default location
        parser.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation",
            handler.getConfiguration().getSchemaFileURL());

        return parser;
    }

    /**
     * Properties used to control the parser behaviour.
     * <p>
     * Parser properties are set in the configuration of a parser.
     * <pre>
     * Configuration configuration = new ....
     * configuration.getProperties().add( Parser.Properties.PARSE_UNKNOWN_ELEMENTS );
     * configuration.getProperties().add( Parser.Properties.PARSE_UNKNOWN_ATTRIBUTES );
     * </pre>
     * </p>
     * @author Justin Deoliveira, The Open Planning Project
     * @deprecated
     */
    public static interface Properties {
        /**
         * If set, the parser will continue to parse when it finds an element
         * and cannot determine its type.
         *
         * @deprecated use {@link Parser#setStrict(boolean)}
         */
        QName PARSE_UNKNOWN_ELEMENTS = new QName("http://www.geotools.org", "parseUnknownElements");

        /**
         * If set, the parser will continue to parse when it finds an attribute
         * and cannot determine its type.
         *
         * @deprecated use {@link Parser#setStrict(boolean)}
         */
        QName PARSE_UNKNOWN_ATTRIBUTES = new QName("http://www.geotools.org",
                "parseUnknownAttributes");

        /**
         * If set, the parser will ignore the schemaLocation attribute of an
         * instance document.
         *
         * @deprecated use {@link Parser#setStrict(boolean)}
         */
        QName IGNORE_SCHEMA_LOCATION = new QName("http://www.geotools.org", "ignoreSchemaLocation");
    }
}
