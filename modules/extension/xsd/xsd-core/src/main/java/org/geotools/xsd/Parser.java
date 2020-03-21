/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.xsd.XSDSchema;
import org.geotools.util.logging.Logging;
import org.geotools.xs.XS;
import org.geotools.xsd.impl.ParserHandler;
import org.geotools.xsd.impl.ParserHandler.ContextCustomizer;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * GeoTools XML parser.
 *
 * <p>This parser uses a sax based driver to parse an input stream into a single object. For
 * streaming look at {@link StreamingParser}. If the source document being parsed as already been
 * parsed into a {@link Document} the {@link DOMParser} class may be used.
 *
 * <p>
 *
 * <h3>Schema Resolution</h3>
 *
 * See {@link Configuration} javadocs for instructions on how to customize schema resolution. This
 * is often desirable in the case that the instance document being parsed contains invalid uri's in
 * schema imports and includes.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class Parser {

    private static final Logger LOGGER = Logging.getLogger(Parser.class);

    private static final String LEXICAL_HANDLER_PROPERTY = "lexical-handler";

    private static final String SAX_PROPERTY_PREFIX = "http://xml.org/sax/properties/";

    private static final String JAXP_PROPERTY_PREFIX = "http://www.oracle.com/xml/jaxp/properties/";
    private static final String JDK_ENTITY_EXPANSION_LIMIT =
            JAXP_PROPERTY_PREFIX + "entityExpansionLimit";
    private static final Integer DEFAULT_ENTITY_EXPANSION_LIMIT = 100;

    /** sax handler which maintains the element stack */
    private ParserHandler handler;

    /** the sax parser driving the handler */
    private SAXParser parser;

    /** Entity expansion limit configuration, set to null by default */
    private Integer entityExpansionLimit;

    /**
     * Creates a new instance of the parser.
     *
     * @param configuration The parser configuration, bindings and context, must never be <code>null
     *     </code>.
     */
    public Parser(Configuration configuration) {
        if (configuration == null) {
            throw new NullPointerException("configuration");
        }

        handler = new ParserHandler(configuration);

        configuration.setupParser(this);
    }

    /** @return The underlying parser handler. */
    ParserHandler getParserHandler() {
        return handler;
    }

    /** Allows the caller to customize the Pico context used for parsing */
    public void setContextCustomizer(ContextCustomizer contextCustomizer) {
        handler.setContextCustomizer(contextCustomizer);
    }

    /**
     * Parses an instance documented defined by an input stream.
     *
     * <p>The object returned from the parse is the object which has been bound to the root element
     * of the document. This method should only be called once for a single instance document.
     *
     * @return The object representation of the root element of the document.
     */
    public Object parse(InputStream input)
            throws IOException, SAXException, ParserConfigurationException {
        return parse(new InputSource(input));
    }

    /**
     * Parses an instance documented defined by a reader.
     *
     * <p>The object returned from the parse is the object which has been bound to the root element
     * of the document. This method should only be called once for a single instance document.
     *
     * @return The object representation of the root element of the document.
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
     *
     * @since 2.6
     */
    public Object parse(Source source)
            throws IOException, SAXException, ParserConfigurationException, TransformerException {
        // TODO: use SAXResult to stream, need to figure out how to enable
        // validation with transformer api
        // SAXResult result = new SAXResult( handler );
        StreamResult result = new StreamResult(new ByteArrayOutputStream());

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer tx = tf.newTransformer();

        tx.transform(source, result);

        return parse(
                new ByteArrayInputStream(
                        ((ByteArrayOutputStream) result.getOutputStream()).toByteArray()));
    }

    /**
     * Parses an instance documented defined by a sax input source.
     *
     * <p>The object returned from the parse is the object which has been bound to the root element
     * of the document. This method should only be called once for a single instance document.
     *
     * @return The object representation of the root element of the document.
     */
    public Object parse(InputSource source)
            throws IOException, SAXException, ParserConfigurationException {
        parser = parser();

        parser.parse(source, handler);

        return handler.getValue();
    }

    /**
     * Sets the strict parsing flag.
     *
     * <p>When set to <code>true</code>, this will cause the parser to operate in a strict mode,
     * which means that xml being parsed must be exactly correct with respect to the schema it
     * references.
     *
     * <p>Some examples of cases in which the parser will throw an exception while operating in
     * strict mode:
     *
     * <ul>
     *   <li>no 'schemaLocation' specified, or specified incorrectly
     *   <li>element found which is not declared in the schema
     * </ul>
     *
     * @param strict The strict flag.
     */
    public void setStrict(boolean strict) {
        handler.setStrict(strict);
    }

    /**
     * Sets the flag controlling wether the parser should validate or not.
     *
     * @param validating Validation flag, <code>true</code> to validate, otherwise <code>false
     *     </code>
     */
    public void setValidating(boolean validating) {
        handler.setValidating(validating);
    }

    /** @return Flag determining if the parser is validatin or not. */
    public boolean isValidating() {
        return handler.isValidating();
    }

    /**
     * Sets the flag which controls how the parser handles validation errors.
     *
     * <p>When this flag is set, the parser will throw an exception when it encounters a validation
     * error. Otherwise the error will be stored, retrievable from {@link #getValidationErrors()}.
     *
     * <p>The default behavior is to set this flag to <code>false</code>. So client code should
     * explicitly set this flag if it is desired that the exception be thrown when the validation
     * error occurs.
     *
     * @param fail failure flag, <code>true</code> to fail, otherwise <code>false</code>
     */
    public void setFailOnValidationError(boolean fail) {
        handler.setFailOnValidationError(fail);
    }

    /** @return The flag determining how the parser deals with validation errors. */
    public boolean isFailOnValidationError() {
        return handler.isFailOnValidationError();
    }

    /**
     * Sets flag that controls whether the parser will process mixed content in a way that preserves
     * order of child elements and text.
     *
     * @since 2.7
     */
    public void setHandleMixedContent(boolean handleMixedContent) {
        handler.setHandleMixedContent(handleMixedContent);
    }

    /**
     * Flag that controls whether the parser will process mixed content in a way that preserves
     * order of child elements and text.
     *
     * <p>By default the parser will simply concatenate blindly all child text and not preserve
     * order with respect to other elements within a mixed content type.
     *
     * @since 2.7
     */
    public boolean isHandleMixedContent() {
        return handler.isHandleMixedContent();
    }

    /**
     * Sets Flag that forces of the check for {@link ParserDelegate} even in cases where an element
     * can be parsed normally.
     *
     * @since 8.0
     * @see Parser#isForceParserDelegate()
     */
    public void setForceParserDelegate(boolean forceParserDelegate) {
        handler.setForceParserDelegate(forceParserDelegate);
    }

    /**
     * Flag that forces of the check for {@link ParserDelegate} even in cases where an element can
     * be parsed normally.
     *
     * <p>By default the parser will only lookup parser delegates when the element is unrecognized
     * with regard to the schema and can't be parsed normally.
     *
     * @since 8.0
     */
    public boolean isForceParserDelegate() {
        return handler.isForceParserDelegate();
    }

    /** Set EntityResolver */
    public void setEntityResolver(EntityResolver entityResolver) {
        handler.setEntityResolver(entityResolver);
    }

    /**
     * Get EntityResolver
     *
     * @return entityResolver
     */
    public EntityResolver getEntityResolver() {
        return handler.getEntityResolver();
    }

    /**
     * Informs the parser of the type of the root element to be used in cases where it can not be
     * inferred.
     *
     * <p>This method is used in cases where the element being parsed is not declared as global in
     * the schema.
     *
     * @param typeName The type name of the root element.
     * @since 8.0
     */
    public void setRootElementType(QName typeName) {
        handler.setRootElementType(typeName);
    }

    /**
     * The type name of the root element being parsed.
     *
     * @see Parser#setRootElementType(QName)
     */
    public QName getRootElementType() {
        return handler.getRootElementType();
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
     *
     * <p>Clients should call {@link #getValidationErrors()} after this method to retrieve any
     * validation errors that occurred. Clients do not need to call {@link #setValidating(boolean)}
     * when using this method to validate.
     *
     * <p>This method does not do any of the work done by {@link #parse(InputSource)}, it only
     * validates.
     */
    public void validate(InputStream in)
            throws IOException, SAXException, ParserConfigurationException {
        validate(new InputSource(in));
    }

    /**
     * Validates an instance document defined by a reader.
     *
     * <p>Clients should call {@link #getValidationErrors()} after this method to retrieve any
     * validation errors that occurred. Clients do not need to call {@link #setValidating(boolean)}
     * when using this method to validate.
     *
     * <p>This method does not do any of the work done by {@link #parse(InputSource)}, it only
     * validates.
     */
    public void validate(Reader reader)
            throws IOException, SAXException, ParserConfigurationException {
        validate(new InputSource(reader));
    }

    /**
     * Validates an instance document defined by a input source.
     *
     * <p>Clients should call {@link #getValidationErrors()} after this method to retrieve any
     * validation errors that occurred. Clients do not need to call {@link #setValidating(boolean)}
     * when using this method to validate.
     *
     * <p>This method does not do any of the work done by {@link #parse(InputSource)}, it only
     * validates.
     */
    public void validate(InputSource source)
            throws IOException, SAXException, ParserConfigurationException {
        SAXParser parser = parser(true);
        parser.parse(source, handler.getValidator());
    }

    /**
     * Returns the schema objects referenced by the instance document being parsed. This method can
     * only be called after a successful parse has begun.
     *
     * @return The schema objects used to parse the document, or null if parsing has not commenced.
     */
    public XSDSchema[] getSchemas() {
        if (handler != null) {
            return handler.getSchemas();
        }

        return null;
    }

    /**
     * Returns the namespace mappings maintained by the parser.
     *
     * <p>Clients may register additional namespace mappings. This is useful when an application
     * wishes to provide some "default" namespace mappings.
     *
     * <p>Clients should register namespace mappings in the current "context", ie do not call {@link
     * NamespaceSupport#pushContext()}. Example: <code>
     * Parser parser = new Parser( ... );
     * parser.getNamespaces().declarePrefix( "foo", "http://www.foo.com" );
     * ...
     * </code>
     *
     * @return The namespace support containing prefix to uri mappings.
     * @since 2.4
     */
    public ParserNamespaceSupport getNamespaces() {
        return handler.getNamespaceSupport();
    }

    /**
     * Returns the list of {@link URIHandler} used when parsing schemas.
     *
     * <p>URI handlers are invoked to handle external references that occur during parsing.
     *
     * @since 2.7
     */
    public List<URIHandler> getURIHandlers() {
        return handler.getURIHandlers();
    }

    protected SAXParser parser() throws ParserConfigurationException, SAXException {
        return parser(isValidating());
    }

    protected SAXParser parser(boolean validate) throws ParserConfigurationException, SAXException {
        // JD: we use xerces directly here because jaxp does seem to allow use to
        // override all the namespaces to validate against
        SAXParserFactory pFactory = SAXParserFactory.newInstance();

        // set the appropriate features
        pFactory.setFeature("http://xml.org/sax/features/namespaces", true);

        if (validate) {
            pFactory.setFeature("http://xml.org/sax/features/validation", true);
            pFactory.setFeature("http://apache.org/xml/features/validation/schema", true);
            pFactory.setFeature(
                    "http://apache.org/xml/features/validation/schema-full-checking", true);
        }

        SAXParser parser = pFactory.newSAXParser();

        // set the schema sources of this configuration, and all dependent ones
        StringBuffer schemaLocation = new StringBuffer();

        for (Iterator d = handler.getConfiguration().allDependencies().iterator(); d.hasNext(); ) {
            Configuration dependency = (Configuration) d.next();

            // ignore xs namespace
            if (XS.NAMESPACE.equals(dependency.getNamespaceURI())) {
                continue;
            }

            // Separate entries by space
            if (schemaLocation.length() > 0) {
                schemaLocation.append(" ");
            }

            // add the entry
            schemaLocation.append(dependency.getNamespaceURI());
            schemaLocation.append(" ");
            schemaLocation.append(dependency.getXSD().getSchemaLocation());
        }

        // set the property to map namespaces to schema locations
        parser.setProperty(
                "http://apache.org/xml/properties/schema/external-schemaLocation",
                schemaLocation.toString());
        // add the handler as a LexicalHandler too.
        parser.setProperty(SAX_PROPERTY_PREFIX + LEXICAL_HANDLER_PROPERTY, handler);
        // set Entity expansion limit
        setupEntityExpansionLimit(parser);

        //
        // return builded parser
        return parser;
    }

    private void setupEntityExpansionLimit(final SAXParser parser) throws SAXNotSupportedException {
        try {
            parser.setProperty(
                    JDK_ENTITY_EXPANSION_LIMIT,
                    entityExpansionLimit != null
                            ? entityExpansionLimit
                            : DEFAULT_ENTITY_EXPANSION_LIMIT);
        } catch (SAXNotRecognizedException ex) {
            LOGGER.warning(
                    "Sax parser property '"
                            + JDK_ENTITY_EXPANSION_LIMIT
                            + "' not recognized.  "
                            + "Xerces version is incompatible.");
        }
    }

    public Optional<Integer> getEntityExpansionLimit() {
        return Optional.ofNullable(entityExpansionLimit);
    }

    public void setEntityExpansionLimit(Integer entityExpansionLimit) {
        this.entityExpansionLimit = entityExpansionLimit;
    }
}
