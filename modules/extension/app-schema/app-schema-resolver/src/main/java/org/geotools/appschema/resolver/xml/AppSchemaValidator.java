/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.appschema.resolver.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.geotools.xml.resolver.SchemaCatalog;
import org.geotools.xml.resolver.SchemaResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.EntityResolver2;

/**
 * A class to perform XML schema validation against schemas found using an {@link SchemaResolver} .
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class AppSchemaValidator {

    /**
     * Pattern matching a string that starts with an XML declaration with an encoding, with a single group that contains
     * the encoding.
     */
    private static final Pattern XML_ENCODING_PATTERN = Pattern.compile("<\\?xml.*?encoding=[\"'](.+?)[\"'].*?\\?>.*");

    /** The resolver used to find XML schemas. */
    private final SchemaResolver resolver;

    /** Failures found during parsing of an XML instance document. */
    private final List<String> failures = new ArrayList<>();

    /** Are validation warnings considered failures? The default is true. */
    private boolean failOnWarning = true;

    /**
     * Construct an {@link AppSchemaValidator} that performs schema validation against schemas found on the classpath
     * using the convention described in {@link SchemaResolver#getSimpleHttpResourcePath(java.net.URI)}.
     */
    private AppSchemaValidator() {
        this(new SchemaResolver());
    }

    /**
     * Construct an {@link AppSchemaValidator} that performs schema validation against schemas found using an
     * {@link SchemaResolver}.
     *
     * @param resolver resolver used to locate XML schemas
     */
    private AppSchemaValidator(SchemaResolver resolver) {
        this.resolver = resolver;
    }

    /**
     * Construct an {@link AppSchemaValidator} that performs schema validation against schemas found using an
     * {@link SchemaResolver} with a {@link SchemaCatalog}.
     *
     * @param catalog SchemaCatalog
     */
    private AppSchemaValidator(SchemaCatalog catalog) {
        this(new SchemaResolver(catalog));
    }

    /** Return the list of failures found during parsing. */
    public List<String> getFailures() {
        return Collections.unmodifiableList(failures);
    }

    /** Are validation warnings considered failures? */
    public boolean isFailOnWarning() {
        return failOnWarning;
    }

    /** Should validation warnings be considered failures? */
    public void setFailOnWarning(boolean failOnWarning) {
        this.failOnWarning = failOnWarning;
    }

    /**
     * Parse an XML instance document read from an {@link InputStream}, recording any validation failures failures.
     *
     * @param input stream from which XML instance document is read
     */
    public void parse(InputStream input) {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        parserFactory.setValidating(true);
        XMLReader xmlReader;
        try {
            SAXParser parser = parserFactory.newSAXParser();
            // Validation is against XML Schema
            parser.setProperty(
                    "http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
            xmlReader = parser.getXMLReader();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        xmlReader.setEntityResolver(new AppSchemaEntityResolver());
        // We principally care about the failures themselves, but it is also possible to install a
        // ContentHandler to output annotated XML that identifies the precise location of failures.
        // That can be done with a serializer that implements both ContentHandler and ErrorHandler.
        // It should be installed here (and used for the error handler):
        // parser.setContentHandler(contentHandler);
        xmlReader.setErrorHandler(new AppSchemaValidatorErrorHandler());
        try {
            xmlReader.parse(new InputSource(input));
        } catch (RuntimeException e) {
            // Avoid gratuitous exception chaining.
            // Resolver failures pass through this block.
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Throw a {@link RuntimeException} if the validator has found any failures. The exception detail contains the
     * failure messages.
     */
    public void checkForFailures() {
        if (!failures.isEmpty()) {
            throw new RuntimeException(buildFailureMessage());
        }
    }

    /** Build an exception detail message that contains all the validation failure messages. */
    private String buildFailureMessage() {
        String newline = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder();
        builder.append("Schema validation failures: " + failures.size());
        for (String failure : failures) {
            builder.append(newline);
            builder.append(failure);
        }
        return builder.toString();
    }

    /**
     * Construct an {@link AppSchemaValidator} that performs schema validation against schemas found on the classpath
     * using the convention described in {@link SchemaResolver#getSimpleHttpResourcePath(java.net.URI)}.
     */
    public static AppSchemaValidator buildValidator() {
        return new AppSchemaValidator();
    }

    /**
     * Construct an {@link AppSchemaValidator} that performs schema validation against schemas found using an
     * {@link SchemaResolver}.
     *
     * @param resolver the resolver used to find schemas
     */
    public static AppSchemaValidator buildValidator(SchemaResolver resolver) {
        return new AppSchemaValidator(resolver);
    }

    /**
     * Construct an {@link AppSchemaValidator} that performs schema validation against schemas found using an
     * {@link SchemaResolver} with a {@link SchemaCatalog}.
     *
     * @param catalog SchemaCatalog
     */
    public static AppSchemaValidator buildValidator(SchemaCatalog catalog) {
        return new AppSchemaValidator(catalog);
    }

    /**
     * Perform schema validation of an XML instance document read from a classpath resource against schemas found on the
     * classpath using the convention described in {@link SchemaResolver#getSimpleHttpResourcePath(java.net.URI)}.
     *
     * <p>If validation fails, a {@link RuntimeException} is thrown containing details of all failures.
     *
     * @param name resource name of XML instance document
     * @param catalog SchemaCatalog to aide local schema resolution or null
     */
    public static void validateResource(String name, SchemaCatalog catalog) throws IOException {
        try (InputStream input = AppSchemaValidator.class.getResourceAsStream(name)) {
            validate(input, catalog);
        }
    }

    /**
     * Perform schema validation of an XML instance document in a string against schemas found on the classpath using
     * the convention described in {@link SchemaResolver#getSimpleHttpResourcePath(java.net.URI)}.
     *
     * <p>If validation fails, a {@link RuntimeException} is thrown containing details of all failures.
     *
     * @param xml string containing XML instance document
     * @param catalog SchemaCatalog to aide local schema resolution or null
     */
    public static void validate(String xml, SchemaCatalog catalog) {
        byte[] bytes = null;
        String encoding = getEncoding(xml);
        if (encoding != null) {
            try {
                bytes = xml.getBytes(encoding);
            } catch (UnsupportedEncodingException e) {
                // ignore, handled below
            }
        }
        if (bytes == null) {
            // no encoding in declaration or unsupported encoding
            // fall back to platform default
            bytes = xml.getBytes(StandardCharsets.UTF_8);
        }
        try (InputStream input = new ByteArrayInputStream(bytes)) {
            validate(input, catalog);
        } catch (IOException e) {
            // should not happen
        }
    }

    /**
     * Return the encoding from the XML declaration in an XML document, if present, or null if not found.
     *
     * @param xml string containing an XML document
     * @return declared encoding or null if not present
     */
    static String getEncoding(String xml) {
        Matcher m = XML_ENCODING_PATTERN.matcher(xml);
        if (m.matches()) {
            return m.group(1);
        } else {
            return null;
        }
    }

    /**
     * Perform schema validation of an XML instance document read from an input stream against schemas found on the
     * classpath using the convention described in {@link SchemaResolver#getSimpleHttpResourcePath(java.net.URI)}.
     *
     * <p>If validation fails, a {@link RuntimeException} is thrown containing details of all failures.
     *
     * @param input stream providing XML instance document
     * @param catalog SchemaCatalog file to aide local schema resolution or null
     */
    public static void validate(InputStream input, SchemaCatalog catalog) {
        AppSchemaValidator validator = buildValidator(catalog);
        validator.parse(input);
        validator.checkForFailures();
    }

    /**
     * An {@link EntityResolver2} that uses the enclosing instance's {@link SchemaResolver} to look up XML entities
     * (that is, XML schemas).
     */
    private class AppSchemaEntityResolver implements EntityResolver2 {

        /**
         * Always throws {@link UnsupportedOperationException}. The {@link EntityResolver2} interface must be used so
         * that relative URLs are resolved correctly. If this method is called, it means that the parser is probably
         * misconfigured.
         *
         * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
         */
        @Override
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
            throw new UnsupportedOperationException("Misconfigured parser: EntityResolver2 interface must be used "
                    + "so that relative URLs are resolved correctly");
        }
        /**
         * Always returns null to indicate that there is no external subset.
         *
         * @see org.xml.sax.ext.EntityResolver2#getExternalSubset(java.lang.String, java.lang.String)
         */
        @Override
        public InputSource getExternalSubset(String name, String baseURI) {
            return null;
        }

        /**
         * Return an {@link InputSource} for the resolved schema location. Note that the {@link EntityResolver2}
         * interface must be used because baseURI is needed to resolve relative URIs. The resolver uses baseURI to find
         * the original unresolved context (which it has stored); this is then used to construct the unresolved URI of
         * the schema. In the case of downloaded schemas, the original URI is used to download the schema into the
         * cache; the resolved URI is the location of the cached schema.
         *
         * @see org.xml.sax.ext.EntityResolver2#resolveEntity(java.lang.String, java.lang.String, java.lang.String,
         *     java.lang.String)
         */
        @Override
        public InputSource resolveEntity(String name, String publicId, String baseURI, String systemId)
                throws SAXException, IOException {
            return new InputSource(resolver.resolve(systemId, baseURI));
        }
    }

    /**
     * An {@link ErrorHandler} that appends validation failure messages to the failure list in the enclosing instance.
     */
    private class AppSchemaValidatorErrorHandler implements ErrorHandler {

        /** @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException) */
        @Override
        public void error(SAXParseException exception) throws SAXException {
            failures.add("ERROR: " + exception.getMessage());
        }

        /** @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException) */
        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            failures.add("FATAL ERROR: " + exception.getMessage());
        }

        /** @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException) */
        @Override
        public void warning(SAXParseException exception) throws SAXException {
            if (failOnWarning) {
                failures.add("WARNING: " + exception.getMessage());
            }
        }
    }
}
