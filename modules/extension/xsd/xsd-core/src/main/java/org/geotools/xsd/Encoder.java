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
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDForm;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.api.feature.ComplexAttribute;
import org.geotools.api.feature.Property;
import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureCollection;
import org.geotools.xml.XMLUtils;
import org.geotools.xml.transform.QNameValidatingHandler;
import org.geotools.xsd.impl.BindingFactoryImpl;
import org.geotools.xsd.impl.BindingLoader;
import org.geotools.xsd.impl.BindingPropertyExtractor;
import org.geotools.xsd.impl.BindingVisitorDispatch;
import org.geotools.xsd.impl.BindingWalker;
import org.geotools.xsd.impl.BindingWalkerFactoryImpl;
import org.geotools.xsd.impl.ElementEncoder;
import org.geotools.xsd.impl.GetPropertyExecutor;
import org.geotools.xsd.impl.NamespaceSupportWrapper;
import org.geotools.xsd.impl.SchemaIndexImpl;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.NamespaceSupport;

import static javax.xml.XMLConstants.DEFAULT_NS_PREFIX;
import static javax.xml.XMLConstants.NULL_NS_URI;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import static javax.xml.XMLConstants.XML_NS_PREFIX;

/**
 * Encodes objects as xml based on a schema.
 *
 * <p>The function of the encoder is to traverse a tree of objects serializing them out as xml as it goes. Navigation
 * and serialization of the tree is performed by instances of {@link Binding} which are bound to types in the schema.
 * <br>
 *
 * <p>To execute the encoder, one must have 3 bits of information:
 *
 * <ol>
 *   <li>The root object in the tree to be encoded
 *   <li>The schema / configuration of the instance document being encoded.
 *   <li>A name of the element defined in the schema which corresponds to the root object in the tree.
 * </ol>
 *
 * <br>
 *
 * <p>As an example, consider the encoding of a {@link org.geotools.api.filter.Filter} instance.
 *
 * <pre>
 *         <code>
 *  //instantiate the configuration for the filter schema
 *  Configuration configuration = new OGCConfiguration();
 *
 *  //create the encoder
 *  Encoder encoder = new Encoder( configuration );
 *
 *  //get a filter
 *  Filter filter = ...;
 *
 *  //get the name of the 'filter' element in the schema
 *  QName name = new QName( "http://www.opengis.net/ogc", "Filter" );
 *
 *  //encode
 *  encoder.encode( filter, name );
 *         </code>
 * </pre>
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class Encoder {
    /**
     * Special name recognized by the encoder as a comment.
     *
     * <p>Bindings can return this name in {@link ComplexBinding#getProperties(Object, XSDElementDeclaration)} to
     * provide comments to be encoded.
     */
    public static final QName COMMENT = new QName("http://www.geotools.org", "comment");

    static final String INDENT_AMOUNT_KEY = "{http://xml.apache.org/xslt}indent-amount";

    /** the schema + index * */
    private final XSDSchema schema;

    private final SchemaIndex index;

    /** binding factory + context * */
    private final BindingLoader bindingLoader;

    private MutablePicoContainer context;

    /** binding walker */
    private final BindingWalker bindingWalker;

    /** property extractors */
    private final List<PropertyExtractor> propertyExtractors;

    /** element encoder */
    private final ElementEncoder encoder;

    /** factory for creating nodes * */
    private Document doc;

    /** namespaces */
    private final NamespaceSupport namespaces;

    /** document serializer * */
    private ContentHandler serializer;

    /** schema location */
    private Map<String, String> schemaLocations;

    /** output format/properties */
    private final Properties outputProps;

    /** namespace aware */
    private boolean namespaceAware = true;

    /** true if we are encoding a full document */
    private boolean inline = false;

    /** name of type of root element to encode */
    private QName rootElementType;
    /** Logger logger; */
    private Logger logger;

    /** if true the encoder may encode complex features that map to a complex type that is not GML valid * */
    private final boolean relaxed = Boolean.parseBoolean(System.getProperty("encoder.relaxed", "true"));

    /** The configuration used by the encoder */
    private final Configuration configuration;

    /**
     * Creates an encoder from a configuration.
     *
     * <p>This constructor calls through to {@link #Encoder(Configuration, XSDSchema)} obtaining the schema instance
     * from {@link Configuration#getXSD()#getSchema()}).
     *
     * @param configuration The encoder configuration.
     */
    public Encoder(final Configuration configuration) {
        this(configuration, getSchema(configuration));
    }

    public static XSDSchema getSchema(final Configuration configuration) {
        try {
            return configuration.getXSD().getSchema();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates an encoder from a configuration and a specific schema instance.
     *
     * @param configuration The encoder configuration.
     * @param schema The schema instance.
     */
    public Encoder(final Configuration configuration, final XSDSchema schema) {
        this.configuration = configuration;
        this.schema = schema;

        index = new SchemaIndexImpl(new XSDSchema[] {schema});

        bindingLoader = new BindingLoader(configuration.setupBindings());
        bindingWalker = new BindingWalker(bindingLoader);

        // create the context
        context = new DefaultPicoContainer();
        context.registerComponentInstance(this);

        // register the binding factory in the context
        BindingFactory bindingFactory = new BindingFactoryImpl(bindingLoader);
        context.registerComponentInstance(bindingFactory);

        // register the element encoder in the context
        encoder = new ElementEncoder(bindingWalker, context);
        context.registerComponentInstance(encoder);

        // register the schema index
        context.registerComponentInstance(index);

        // binding walker support
        context.registerComponentInstance(new BindingWalkerFactoryImpl(bindingLoader, context));

        // pass the context off to the configuration
        context = configuration.setupContext(context);
        encoder.setContext(context);

        // schema location setup
        schemaLocations = new HashMap<>();

        // get a logger from the context
        logger = (Logger) context.getComponentInstanceOfType(Logger.class);

        if (logger == null) {
            // create a default
            logger = org.geotools.util.logging.Logging.getLogger(Encoder.class);
            context.registerComponentInstance(logger);
        }

        encoder.setLogger(logger);

        // namespaces
        namespaces = new NamespaceSupport();
        context.registerComponentInstance(namespaces);
        context.registerComponentInstance(new NamespaceSupportWrapper(namespaces));

        // add configuration to context;
        context.registerComponentInstance(configuration);

        // property extractors
        propertyExtractors = Schemas.getComponentInstancesOfType(context, PropertyExtractor.class);

        // add the property extractor for bindings as first
        propertyExtractors.add(0, new BindingPropertyExtractor(this, context));

        // create output properties with some defaults
        outputProps = new Properties();
        outputProps.setProperty(INDENT_AMOUNT_KEY, "2");

        configuration.setupEncoder(this);
    }

    /**
     * Sets the charset encoding scheme to be used in encoding XML content.
     *
     * <p>This encoding will determine the resulting character encoding for the XML content generated by this Encoder
     * and will be reflected in the XML declaration tag.
     *
     * @param charset the (non null) charset to encode XML content accordingly to
     */
    public void setEncoding(final Charset charset) {
        final String charsetName = charset.name();
        outputProps.put(OutputKeys.ENCODING, charsetName);
    }

    /**
     * Returns the Charset defining the character encoding scheme this Encoder uses to encode XML content.
     *
     * <p>If not otherwise set through {@link #setEncoding(Charset)}, <code>UTF-8</code> is used.
     *
     * @return the character set used for encoding
     */
    public Charset getEncoding() {
        final String charsetName = outputProps.getProperty(OutputKeys.ENCODING);
        return charsetName != null ? Charset.forName(charsetName) : null;
    }

    /**
     * Sets XML declaration omitting on and off.
     *
     * @param ommitXmlDeclaration <code>true</code> if XML declaration should be omitted
     */
    public void setOmitXMLDeclaration(final boolean ommitXmlDeclaration) {
        outputProps.put(OutputKeys.OMIT_XML_DECLARATION, ommitXmlDeclaration ? "yes" : "no");
    }

    /**
     * Returns true if the XML document declaration should be omitted. The default is false.
     *
     * @return whether the xml declaration is omitted, defaults to false.
     */
    public boolean isOmitXMLDeclaration() {
        return "yes".equals(outputProps.get(OutputKeys.OMIT_XML_DECLARATION));
    }

    /**
     * Sets the indentation on and off.
     *
     * <p>When set on, the default indentation level and default line wrapping is used (see {@link #getIndentSize()} and
     * {@link #getLineWidth()}). To specify a different indentation level or line wrapping, use
     * {@link #setIndentSize(int)} and {@link #setLineWidth(int)}).
     *
     * @param doIndent <code>true</code> if indentation should be on
     */
    public void setIndenting(final boolean doIndent) {
        outputProps.put(OutputKeys.INDENT, doIndent ? "yes" : "no");
    }

    /**
     * Returns whether this Encoder produces indented XML.
     *
     * <p>Defaults to <code>false</code>.
     *
     * @return <code>true</code> if indentation was specified
     * @see #setIndentSize(int)
     */
    public boolean isIndenting() {
        return "yes".equals(outputProps.get(OutputKeys.INDENT));
    }

    /**
     * Sets the indentation level in number of spaces used.
     *
     * <p>The document will not be indented if the indentation is set to zero. Calling <code>
     * setIndenting(false)</code> will reset this value to zero, calling it with <code>true</code> will reset this value
     * to the default.
     *
     * @param indentSize the number, greater or equal than zero, of characters used to indent, zero for no indentation.
     */
    public void setIndentSize(final int indentSize) {
        if (indentSize < 0) {
            throw new IllegalArgumentException("indentSize shall be >= 0: " + indentSize);
        }
        setIndenting(true);
        outputProps.setProperty(INDENT_AMOUNT_KEY, String.valueOf(indentSize));
    }

    /**
     * Returns the indentation specified.
     *
     * <p>If no indentation was specified, zero is returned and the document should not be indented.
     *
     * <p>Defaults to <code>4</code>
     *
     * @return zero if not indenting, the number of white space characters used for indentation otherwise.
     * @see #setIndenting(boolean)
     */
    public int getIndentSize() {
        if (outputProps.containsKey(INDENT_AMOUNT_KEY)) {
            return Integer.parseInt(outputProps.getProperty(INDENT_AMOUNT_KEY));
        }
        return 0;
    }

    /**
     * Sets the line width.
     *
     * <p>If zero then no line wrapping will occur. Calling <code>setIndenting(false)</code> will reset this value to
     * zero, calling <code>setIndenting(true)</code> will set this value to the default.
     *
     * @param lineWidth a number >= 0 used to limit line widths
     */
    public void setLineWidth(final int lineWidth) {
        if (lineWidth < 0) {
            throw new IllegalArgumentException("lineWidth shall be >= 0: " + lineWidth);
        }

        // TODO: it seems there is no magic key for line width...
    }

    /**
     * Returns the line width for breaking up long lines.
     *
     * <p>When indenting, and only when indenting, long lines will be broken at space boundaries based on this line
     * width. No line wrapping occurs if this value is zero.
     *
     * <p>Defaults to <code>72</code> characters per line.
     *
     * @return the number of characters at which line wrapping happens, or zero for no line wrapping
     * @see #isIndenting()
     */
    public int getLineWidth() {
        return 72;
    }

    /**
     * Sets wether the encoder should be namespace aware.
     *
     * <p>Warning that setting this to <code>false</code> will result in no namespace prefixes on encoded elements and
     * attributes, and no schema declarations on the root element.document;
     */
    public void setNamespaceAware(final boolean namespaceAware) {
        this.namespaceAware = namespaceAware;
    }

    /**
     * Returns the namespace mappings maintained by the encoder.
     *
     * <p>Clients may register additional namespace mappings. This is useful when an application wishes to provide some
     * "default" namespace mappings.
     *
     * <p>Clients should register namespace mappings in the current "context", ie do not call
     * {@link NamespaceSupport#pushContext()}. Example: <code>
     * Encoder parser = new Encoder( ... );
     * encoder.getNamespaces().declarePrefix( "foo", "http://www.foo.com" );
     * ...
     * </code>
     *
     * @return The namespace support containing prefix to uri mappings.
     * @since 2.5
     */
    public NamespaceSupport getNamespaces() {
        return namespaces;
    }

    /**
     * Sets the encoder to "inline" mode.
     *
     * <p>When this flag is set {@link #encode(Object, QName, ContentHandler)} should be used to encode.
     */
    public void setInline(final boolean inline) {
        this.inline = inline;
    }

    /**
     * Informs the encoder of the type of the root element to be used in cases where it can not be inferred.
     *
     * <p>This method is used in cases where the element being encoded is not declared as global in the schema.
     *
     * @param rootElementType The type name of the root element.
     * @since 8.0
     */
    public void setRootElementType(final QName rootElementType) {
        this.rootElementType = rootElementType;
    }

    /**
     * Sets the schema location for a particular namespace uri.
     *
     * <p>Registering a schema location will include it on the "schemaLocation" attribute of the root element of the
     * encoding.
     *
     * @param namespaceURI A namespace uri.
     * @param location A schema location.
     */
    public void setSchemaLocation(final String namespaceURI, final String location) {
        schemaLocations.put(namespaceURI, location);
    }

    /** @return The walker used to traverse bindings, this method is for internal use only. */
    public BindingWalker getBindingWalker() {
        return bindingWalker;
    }

    /** @return The index of schema components, this method is for internal use only. */
    public SchemaIndex getSchemaIndex() {
        return index;
    }

    /** @return the schema. */
    public XSDSchema getSchema() {
        return schema;
    }

    /** @return The document used as a factory to create dom nodes. */
    public Document getDocument() {
        return doc;
    }

    /**
     * Encodes an object.
     *
     * <p>An object is encoded as an object, name pair, where the name is the name of an element declaration in a
     * schema.
     *
     * @param object The object being encoded.
     * @param name The name of the element being encoded in the schema.
     * @param out The output stream.
     */
    public void encode(final Object object, final QName name, final OutputStream out) throws IOException {
        if (inline) {
            String msg = "Must use 'encode(Object,QName,ContentHandler)' when inline flag is set";
            throw new IllegalStateException(msg);
        }

        // create the document serializer
        SAXTransformerFactory txFactory = XMLUtils.newSaxTransformerFactory();

        TransformerHandler xmls;
        try {
            xmls = txFactory.newTransformerHandler();
        } catch (TransformerConfigurationException e) {
            throw new IOException(e);
        }
        xmls.getTransformer().setOutputProperties(outputProps);
        xmls.getTransformer().setOutputProperty(OutputKeys.METHOD, "xml");
        xmls.setResult(new StreamResult(out));

        xmls = new QNameValidatingHandler(xmls);
        try {
            encode(object, name, xmls);
        } catch (final SAXException e) {
            // SAXException does not sets initCause(). Instead, it holds its own "exception" field.
            if (e.getException() != null && e.getCause() == null) {
                e.initCause(e.getException());
            }
            throw (IOException) new IOException().initCause(e);
        }
    }

    /**
     * Helper method that checks if the complex feature we want to encode maps to a complex type that respects the GML
     * object-property model.
     */
    private boolean isNonStripedNestedElement(final Object next, final XSDElementDeclaration element) {
        if (!(next instanceof ComplexAttribute)) {
            return false;
        }
        final ComplexAttribute complex = (ComplexAttribute) next;
        // let's see if we need to encode this
        final Collection<Property> nestedProperties = complex.getProperties();
        if (nestedProperties == null || nestedProperties.isEmpty()) {
            return false;
        }
        // let's see if all the properties have the same type, and that the type is equal to the
        // current element type
        if (!nestedProperties.stream()
                .allMatch(property -> property == null
                        || property.getType().getName().equals(complex.getType().getName()))) {
            // different types which means we are not in the case of nested complex features
            return false;
        }
        // so let's see if the nested type is a reference
        for (final XSDParticle childParticle : Schemas.getChildElementParticles(element.getTypeDefinition(), true)) {
            XSDElementDeclaration childElement = (XSDElementDeclaration) childParticle.getContent();
            if (childElement.isElementDeclarationReference()) {
                childElement = childElement.getResolvedElementDeclaration();
                if (childElement
                        .getType()
                        .getName()
                        .equals(complex.getType().getName().getLocalPart())) {
                    // we are good this type respect the object-property model
                    return false;
                }
            }
        }
        // the mapped complex type doesn't respect the object-property model
        return true;
    }

    public void encode(final Object object, final QName name, final ContentHandler handler) throws IOException, SAXException {

        // maintain a stack of (encoding,element declaration pairs)
        Deque<EncodingEntry> encoded = null;

        // make sure the xs namespace is declared
        if (namespaces.getPrefix(W3C_XML_SCHEMA_NS_URI) == null) {
            namespaces.declarePrefix("xs", W3C_XML_SCHEMA_NS_URI);
        }

        try {
            serializer = handler instanceof QNameValidatingHandler ? handler : new QNameValidatingHandler(handler);

            if (!inline) {
                serializer.startDocument();
            }

            if (namespaceAware) {
                setupNamespaces();
            }

            // create the document
            try {
                doc = XMLUtils.newDocumentBuilder().newDocument();
            } catch (ParserConfigurationException e) {
                throw new IOException("Unable to create document", e);
            }

            encoded = new ArrayDeque<>();

            final XSDElementDeclaration root = getRootDeclaration(name);

            encoded.addLast(new EncodingEntry(object, root, null));

            while (!encoded.isEmpty()) {
                final EncodingEntry entry = encoded.peekFirst();

                if (entry.encoding != null) {
                    // element has been started, get the next child
                    if (!entry.children.isEmpty()) {
                        processChildren(serializer, encoded, entry);
                    } else {
                        // no more children, finish the element
                        finishElement(encoded, entry);
                    }
                } else {
                    // start the encoding of the entry
                    startEncoding(object, entry);
                }
            }

            if (!inline) {
                serializer.endDocument();
            }

        } finally {
            // cleanup
            index.destroy();

            // close any iterators still present in the stack, this will only occur in an exception
            // case
            if (encoded != null) {
                while (!encoded.isEmpty()) {
                    final EncodingEntry entry = encoded.removeFirst();
                    if (!entry.children.isEmpty()) {
                        try {
                            closeIterator(entry.children.get(0).iterator);
                        } catch (final Exception e) {
                            // ignore, we are already in an error case.
                        }
                    }
                }
            }
            // TODO: there are probably other references to elements of XSDSchema objects, we should
            // kill them too
        }
    }

    private void startEncoding(final Object object, final EncodingEntry entry) throws SAXException, IOException {
        // first make sure the element is not abstract
        if (entry.element.isAbstract()) {
            // look for a non abstract substitute - substitution groups are subject to
            // changes over time, so we make a copy to avoid being hit with a
            // ConcurrentModificationException
            final List<XSDElementDeclaration> sub = safeCopy(entry.element.getSubstitutionGroup());

            if (!sub.isEmpty()) {
                final XSDElementDeclaration substitute = getConcreteSubstitute(entry, sub);
                if (substitute != null) {
                    entry.element = substitute;
                }
                // otherwise just use the abstract element
            }
        }

        if (entry.element.isAbstract()) {
            logger.fine(entry.element.getName() + " is abstract");
        }

        entry.encoding = entry.parent != null
                ? (Element) encode(entry.object, entry.element, entry.parent.element.getType())
                : (Element) encode(entry.object, entry.element);

        // add any more attributes
        setupEntryAttributes(object, entry);
        setupSchemaLocations(entry);
        start(entry.encoding, entry.element);
        populateChildren(entry);
    }

    private void setupEntryAttributes(final Object object, final EncodingEntry entry) {
        final List<XSDAttributeDeclaration> attributes = index.getAttributes(entry.element);

        for (final XSDAttributeDeclaration attribute : attributes) {

            // do not encode the attribute if it has already been
            // encoded by the parent
            final String ns = attribute.getTargetNamespace();
            final String local = attribute.getName();

            if (!entry.encoding.getAttributeNS(ns, local).isEmpty()) {
                continue;
            }

            // get the object(s) for this attribute
            final GetPropertyExecutor executor = new GetPropertyExecutor(entry.object, attribute);

            BindingVisitorDispatch.walk(object, bindingWalker, entry.element, executor, context);

            if (executor.getChildObject() != null) {
                // encode the attribute
                final Attr attr = (Attr) encode(executor.getChildObject(), attribute);

                if (attr != null) {
                    entry.encoding.setAttributeNodeNS(attr);
                }
            }
        }
    }

    private void populateChildren(final EncodingEntry entry) throws SAXException, IOException {
        // TODO: this method of getting at properties won't maintain order very well,
        // need to come up with a better system that is capable of handling feature types
        for (final PropertyExtractor propertyExtractor : propertyExtractors) {
            if (propertyExtractor.canHandle(entry.object)) {
                final List<ExtractedProperty<?>> extractedProperties = propertyExtractor.properties(entry.object, entry.element);
                O:
                for (final ExtractedProperty<?> extractedProperty : extractedProperties) {
                    final XSDParticle particle = extractedProperty.particle;
                    XSDElementDeclaration child = (XSDElementDeclaration) particle.getContent();

                    if (child == null) {
                        continue;
                    }

                    // check for a comment
                    if (COMMENT.getNamespaceURI().equals(child.getTargetNamespace())
                            && COMMENT.getLocalPart().equals(child.getName())) {
                        comment(child.getElement());

                        continue;
                    }

                    if (child.isElementDeclarationReference()) {
                        child = child.getResolvedElementDeclaration();
                    }

                    // do not encode the element if the parent has already
                    // been encoded by the parent
                    final String ns = child.getTargetNamespace();
                    final String local = child.getName();

                    for (int i = 0; i < entry.encoding.getChildNodes().getLength(); i++) {
                        final Node node = entry.encoding.getChildNodes().item(i);

                        if (node instanceof Element) {
                            if (ns != null) {
                                if (ns.equals(node.getNamespaceURI()) && local.equals(node.getLocalName())) {
                                    continue O;
                                }
                            } else {
                                if (local.equals(node.getLocalName())) {
                                    continue O;
                                }
                            }
                        }
                    }

                    final Object obj = extractedProperty.value;

                    // if the value is null, can we skip it? Or do we have to go out
                    // with an non empty element with xs:nil?
                    if (obj == null) {
                        if (particle.getMinOccurs() == 0) {
                            // just skip it
                            continue;
                        } else if (!child.isNillable()) {
                            // log an error and skip the element, but we're encoding
                            // something invalid
                            logger.fine("Property " + ns + ":" + local + " not found but minoccurs > 0 ");
                            // skip this regardless
                            continue;
                        }
                        // minOccurs > 0 && nillable -> we'll encode an empty element
                        // with xs:nil
                    }

                    // figure out the maximum number of occurrences
                    int maxOccurs = 1;

                    if (particle.isSetMaxOccurs()) {
                        maxOccurs = particle.getMaxOccurs();
                    } else {
                        // look the containing group
                        if (particle.eContainer() instanceof XSDModelGroup) {
                            final XSDModelGroup group = (XSDModelGroup) particle.eContainer();

                            if (group.eContainer() instanceof XSDParticle) {
                                final XSDParticle cParticle = (XSDParticle) group.eContainer();

                                if (cParticle.isSetMaxOccurs()) {
                                    maxOccurs = cParticle.getMaxOccurs();
                                }
                            }
                        }
                    }

                    if (maxOccurs == -1 || maxOccurs > 1) {
                        // may have a collection or array, unwrap it
                        final Iterator<?> iterator;

                        if (obj instanceof final Iterator<?> iterator1) {
                            iterator = iterator1;

                        } else if (obj != null && obj.getClass().isArray()) {
                            final Object[] array = (Object[]) obj;
                            iterator = Arrays.asList(array).iterator();

                        } else if (obj instanceof final Collection<?> collection) {
                            iterator = collection.iterator();

                        } else if (obj instanceof final FeatureCollection<?, ?> collection) {
                            iterator = DataUtilities.iterator(collection.features());

                        } else {
                            iterator = new SingleIterator<>(obj);
                        }

                        entry.children.add(new EncodingEntryChild<>(child, iterator));
                    } else {
                        // only one, just add the object
                        entry.children.add(new EncodingEntryChild<>(child, new SingleIterator<>(obj)));
                    }
                }
            }
        }
    }

    private void setupSchemaLocations(final EncodingEntry entry) throws SAXException {
        // write out the leading edge of the element
        if (schemaLocations != null) {
            // root element, add schema locations if set
            if (!schemaLocations.isEmpty()) {
                // declare the schema instance mapping
                serializer.startPrefixMapping("xsi", W3C_XML_SCHEMA_INSTANCE_NS_URI);
                serializer.endPrefixMapping("xsi");
                namespaces.declarePrefix("xsi", W3C_XML_SCHEMA_INSTANCE_NS_URI);

                final StringBuilder schemaLocation = new StringBuilder();

                for (final Iterator<Map.Entry<String, String>> e = schemaLocations.entrySet().iterator(); e.hasNext(); ) {
                    final Map.Entry<String, String> tuple = e.next();
                    final String namespaceURI = tuple.getKey();
                    final String location = tuple.getValue();

                    schemaLocation.append(namespaceURI).append(' ').append(location);

                    if (e.hasNext()) {
                        schemaLocation.append(' ');
                    }
                }

                entry.encoding.setAttributeNS(
                        W3C_XML_SCHEMA_INSTANCE_NS_URI, "xsi:schemaLocation", schemaLocation.toString());
            }

            schemaLocations = null;
        }
    }

    private XSDElementDeclaration getConcreteSubstitute(final EncodingEntry entry, final List<XSDElementDeclaration> sub) {
        // match up by type
        final List<Object[]> matches = new ArrayList<>();

        for (final XSDElementDeclaration e : sub) {

            if (e == null || e.equals(entry.element)) {
                continue;
            }

            if (e.getName() == null) {
                continue;
            }

            // look up the binding
            Binding binding = bindingLoader.loadBinding(new QName(e.getTargetNamespace(), e.getName()), context);

            if (binding == null) {
                // try the type
                final XSDTypeDefinition type = e.getType();

                if (type == null || type.getName() == null) {
                    continue;
                }

                binding = bindingLoader.loadBinding(new QName(type.getTargetNamespace(), type.getName()), context);
            }

            if (binding == null) {
                continue;
            }

            if (binding.getType() == null) {
                logger.warning("Binding: " + binding.getTarget() + " returns null type.");
                continue;
            }

            // match up the type
            if (binding.getType().isAssignableFrom(entry.object.getClass())) {
                // we have a match, store as an (element,binding) tuple
                matches.add(new Object[] {e, binding});
            }
        }

        // if one, we are gold
        if (matches.size() == 1) {
            entry.element = (XSDElementDeclaration) matches.get(0)[0];
        } else if (!matches.isEmpty()) {
            // if multiple we have a problem
            if (logger.isLoggable(Level.FINE)) {
                final StringBuilder msg = new StringBuilder("Found multiple non-abstract bindings for ");
                msg.append(entry.element.getName()).append(": ");

                for (final Object[] match : matches) {
                    msg.append(match.getClass().getName());
                    msg.append(", ");
                }

                logger.fine(msg.toString());
            }

            // try sorting by the type of the binding
            matches.sort(new MatchComparator());
        }

        if (!matches.isEmpty()) {
            return (XSDElementDeclaration) matches.get(0)[0];
        }

        return null;
    }

    private void finishElement(final Deque<EncodingEntry> encoded, final EncodingEntry entry) throws SAXException {
        end(entry.encoding, entry.element);
        encoded.removeFirst();

        // clean up the entry
        entry.object = null;
        entry.element = null;
        entry.encoding = null;
        entry.children = null;
        entry.parent = null;
    }

    private void processChildren(final ContentHandler handler, final Deque<EncodingEntry> encoded, final EncodingEntry entry) {
        final EncodingEntryChild<?> child = entry.children.get(0);
        final XSDElementDeclaration element = child.elementDecl.getResolvedElementDeclaration();
        final Iterator<?> itr = child.iterator;

        if (itr.hasNext()) {
            final Object next = itr.next();

            // here we check for instanceof EncoderDelegate
            if (next instanceof EncoderDelegate delegate) {
                // do not add entry to the stack, just delegate to encode
                try {
                    delegate.encode(handler);
                } catch (Exception e) {
                    throw new RuntimeException("Error encoding object to xml-element", e);
                }
            } else {
                if (next instanceof ComplexAttribute attribute && relaxed && isNonStripedNestedElement(next, element)) {
                    for (final Property property : attribute.getProperties()) {
                        // add object sub properties, i.e. nested complex features
                        encoded.addFirst(new EncodingEntry(property, element, entry));
                    }
                } else {
                    // add the next object to be encoded to the stack
                    encoded.addFirst(new EncodingEntry(next, element, entry));
                }
            }
        } else {
            // iterator done, close it
            closeIterator(itr);

            // this child is done, remove from child list
            entry.children.remove(0);
        }
    }

    private XSDElementDeclaration getRootDeclaration(final QName name) {
        // add the first entry
        XSDElementDeclaration root = index.getElementDeclaration(name);

        if (root == null) {
            // check for context hint, this is only used when running the encoder
            // in test mode
            QName typeDefintion = rootElementType;

            if (typeDefintion == null) {
                typeDefintion = (QName) context.getComponentInstance("http://geotools.org/typeDefinition");
            }

            if (typeDefintion != null) {
                final XSDTypeDefinition type = Objects.requireNonNull(index.getTypeDefinition(typeDefintion));

                // create a mock element declaration
                root = XSDFactory.eINSTANCE.createXSDElementDeclaration();
                root.setName(name.getLocalPart());
                root.setTargetNamespace(name.getNamespaceURI());
                root.setTypeDefinition(type);
            }
        }

        if (root == null) {
            throw new IllegalArgumentException("Could not find element declaration for:" + name);
        }

        return root;
    }

    private void setupNamespaces() throws SAXException {
        // write out all the namespace prefix value mappings
        for (final Enumeration<String> e = namespaces.getPrefixes(); e.hasMoreElements(); ) {
            final String prefix = e.nextElement();
            final String uri = namespaces.getURI(prefix);

            if (XML_NS_PREFIX.equals(prefix)) {
                continue;
            }
            serializer.startPrefixMapping(prefix, uri);
        }

        for (final Map.Entry<String, String> prefixNsEntry : schema.getQNamePrefixToNamespaceMap().entrySet()) {
            String prefix = prefixNsEntry.getKey();
            if (prefix == null) {
                prefix = DEFAULT_NS_PREFIX;
            }
            final String ns = prefixNsEntry.getValue();

            if (W3C_XML_SCHEMA_INSTANCE_NS_URI.equals(ns)) {
                continue;
            }

            // skip ones already registered
            if (namespaces.getPrefix(ns) != null) {
                continue;
            }
            serializer.startPrefixMapping(prefix, ns);
            serializer.endPrefixMapping(prefix);

            namespaces.declarePrefix(prefix, ns);
        }

        // ensure a default namespace prefix set
        if (namespaces.getURI(DEFAULT_NS_PREFIX) == null) {
            namespaces.declarePrefix(DEFAULT_NS_PREFIX, schema.getTargetNamespace());
        }
    }

    /** Makes a defensive copy of an e-list handling the eventual issues due to concurrent modifications */
    private <T> List<T> safeCopy(final EList<T> substitutionGroup) {
        while (true) {
            try {
                return new ArrayList<>(substitutionGroup);
            } catch (final ArrayIndexOutOfBoundsException e) {
                // ok, the list was modified just during the copy...
            }
        }
    }

    /**
     * Encodes an object directly to a dom.
     *
     * <p>Note that this method should be used for testing or convenience since it does not stream and loads the entire
     * encoded result into memory.
     */
    public Document encodeAsDOM(final Object object, final QName name) throws IOException, SAXException, TransformerException {
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            encode(object, name, out);

            try (final ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray())) {
                final DOMResult result = new DOMResult();

                final Transformer tx = XMLUtils.newTransformer();
                tx.transform(new StreamSource(in), result);
                return (Document) result.getNode();
            }
        }
    }

    /**
     * Encodes an object directly to a string.
     *
     * <p>Note that this method should be used for testing or convenience since it does not stream and loads the entire
     * encoded result into memory.
     *
     * @since 8.0
     */
    public String encodeAsString(final Object object, final QName name) throws IOException {
        try (final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            encode(object, name, out);
            return new String(out.toByteArray(), StandardCharsets.UTF_8);
        }
    }

    protected void closeIterator(final Iterator<?> iterator) {
        DataUtilities.close(iterator);
    }

    protected Node encode(final Object object, final XSDNamedComponent component) {
        return encode(object, component, null);
    }

    protected Node encode(final Object object, final XSDNamedComponent component, final XSDTypeDefinition container) {
        if (component instanceof final XSDElementDeclaration element) {
            return encoder.encode(object, element, doc, container);

        } else if (component instanceof final XSDAttributeDeclaration attribute) {
            return encoder.encode(object, attribute, doc, container);
        }

        return null;
    }

    protected void start(final Element element, final XSDElementDeclaration declaration) throws SAXException {
        String uri, local;

        if (element.getLocalName() != null) {
            uri = element.getNamespaceURI();
            local = element.getLocalName();
        } else {
            // namespace unaware dom tree
            local = element.getNodeName();
            final int qnSepIx = local.indexOf(':');
            if (qnSepIx > -1) {
                final String prefix = local.substring(0, qnSepIx);
                uri = namespaces.getURI(prefix);
                local = local.substring(qnSepIx + 1);

            } else {
                uri = null;
            }
        }

        // declaration == null -> gml3 envelope encoding test failing
        // declaration.getSchema() == null -> wfs 2.0 feature collection encoding test failing
        final String qName;
        if (forceQualified(declaration)) {
            final String uriPrefix;
            if (uri == null) {
                uriPrefix = DEFAULT_NS_PREFIX;
                uri = namespaces.getURI(uriPrefix);
            } else {
                uriPrefix = namespaces.getPrefix(uri);
            }

            if (uriPrefix != null && !DEFAULT_NS_PREFIX.equals(uriPrefix)) {
                qName = uriPrefix + ":" + local;
            } else {
                qName = local;
            }
        } else {
            uri = NULL_NS_URI;
            qName = local;
        }

        final DOMAttributes atts = new DOMAttributes(element.getAttributes(), namespaces);
        serializer.startElement(uri, local, qName, atts);

        // write out any text
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            final Node node = element.getChildNodes().item(i);

            if (node instanceof Text text) {
                final String data = XMLUtils.removeXMLInvalidChars(text.getData());
                char[] ch = data.toCharArray();
                serializer.characters(ch, 0, ch.length);
            }
        }

        // write out any child elements
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            final Node node = element.getChildNodes().item(i);

            if (node instanceof Element child) {
                final QName childName = new QName(child.getNamespaceURI(), child.getNodeName());
                final XSDElementDeclaration childDecl =
                        declaration != null ? Schemas.getChildElementDeclaration(declaration, childName) : null;
                start(child, childDecl);
                end(child, childDecl);
            }
        }

        // push a new context for children, declaring the default prefix to be the one of this
        // element
        this.namespaces.pushContext();

        if (uri != null) {
            this.namespaces.declarePrefix(DEFAULT_NS_PREFIX, uri);
        }
    }

    boolean forceQualified(final XSDElementDeclaration e) {
        return namespaceAware
                && (e == null
                        || e.isGlobal()
                        || e.getSchema() == null
                        || e.getSchema().getElementFormDefault() == XSDForm.QUALIFIED_LITERAL);
    }

    protected void comment(final Element element) throws SAXException, IOException {
        if (serializer instanceof LexicalHandler handler) {
            final NodeList children = element.getChildNodes();

            for (int i = 0; i < children.getLength(); i++) {
                final Node text = children.item(i);
                final String str = text.getNodeValue();
                handler.comment(str.toCharArray(), 0, str.length());
            }
        }
    }

    protected void end(final Element element, final XSDElementDeclaration declaration) throws SAXException {
        // push off last context
        namespaces.popContext();

        String uri = element.getNamespaceURI();
        final String local = element.getLocalName();

        final String qName;
        if (element.getPrefix() != null && !DEFAULT_NS_PREFIX.equals(element.getPrefix())) {
            qName = element.getPrefix() + ":" + local;
        } else {
            if (forceQualified(declaration)) {
                final String uriPrefix;
                if (uri == null) {
                    uriPrefix = DEFAULT_NS_PREFIX;
                    uri = namespaces.getURI(uriPrefix);
                } else {
                    uriPrefix = namespaces.getPrefix(uri);
                }

                if (uriPrefix != null && !DEFAULT_NS_PREFIX.equals(uriPrefix)) {
                    qName = uriPrefix + ":" + local;
                } else {
                    qName = local;
                }
            } else {
                uri = NULL_NS_URI;
                qName = local;
            }
        }

        serializer.endElement(uri, local, qName);
    }

    private static class SingleIterator<T> implements Iterator<T> {
        private final T item;
        private boolean more;

        public SingleIterator(final T item) {
            this.item = item;
            this.more = true;
        }

        @Override
        public void remove() {
            // unsupported
        }

        @Override
        public boolean hasNext() {
            return more;
        }

        @Override
        public T next() {
            this.more = false;
            return item;
        }
    }

    /** Encoding stack entries. */
    private static class EncodingEntry {
        public Object object;
        public XSDElementDeclaration element;
        public Element encoding;
        public List<EncodingEntryChild> children;
        public EncodingEntry parent;

        public EncodingEntry(final Object object, final XSDElementDeclaration element, final EncodingEntry parent) {
            this.object = object;
            this.element = element;
            this.parent = parent;
            this.children = new ArrayList<>();
        }
    }

    private static class EncodingEntryChild<T> {
        final XSDElementDeclaration elementDecl;
        final Iterator<T> iterator;

        public EncodingEntryChild(final XSDElementDeclaration elementDecl, final Iterator<T> iterator) {
            this.elementDecl = elementDecl;
            this.iterator = iterator;
        }
    }

    private static class DOMAttributes implements Attributes {
        final NamedNodeMap atts;
        final NamespaceSupport namespaces;

        public DOMAttributes(final NamedNodeMap atts, final NamespaceSupport namespaces) {
            this.atts = atts;
            this.namespaces = namespaces;
        }

        @Override
        public int getLength() {
            return atts.getLength();
        }

        @Override
        public String getLocalName(final int index) {
            String local = atts.item(index).getLocalName();
            if (nullOrEmpty(local)) {
                // check the qname
                final String qName = getQName(index);
                if (!nullOrEmpty(qName)) {
                    final int qnSepIdx = qName.indexOf(':');
                    if (qnSepIdx > -1) {
                        local = qName.substring(qnSepIdx + 1);
                    }
                }
            }

            return emptyIfNull(local);
        }

        @Override
        public String getQName(final int index) {
            final Node n = atts.item(index);

            if (namespaces != null) {
                final String uri = n.getNamespaceURI();
                final String prefix = uri != null ? namespaces.getPrefix(uri) : null;

                if (prefix != null) {
                    return prefix + ":" + n.getLocalName();
                }
            }

            return n.getLocalName() != null ? n.getLocalName() : n.getNodeName();
        }

        @Override
        public String getType(final int index) {
            return "CDATA"; // TODO: this properly
        }

        @Override
        public String getURI(final int index) {
            String ns = atts.item(index).getNamespaceURI();
            if (ns == null) {
                ns = XMLUtils.qName(getQName(index), namespaces).getNamespaceURI();
            }

            return emptyIfNull(ns);
        }

        @Override
        public String getValue(final int index) {
            return atts.item(index).getNodeValue();
        }

        @Override
        public int getIndex(final String qName) {
            String prefix = null;
            String local = null;

            final int qnSepIdx = qName.lastIndexOf(':');
            if (qnSepIdx > -1) {
                prefix = qName.substring(0, qnSepIdx);
                local = qName.substring(qnSepIdx + 1);
            } else {
                prefix = DEFAULT_NS_PREFIX;
                local = qName;
            }

            for (int i = 0; i < atts.getLength(); i++) {
                final Node att = atts.item(i);

                if (att.getLocalName().equals(local)) {
                    String attPrefix = att.getPrefix();

                    if (attPrefix == null) {
                        attPrefix = DEFAULT_NS_PREFIX;
                    }

                    if (prefix.equals(attPrefix)) {
                        return i;
                    }
                }
            }

            return -1;
        }

        @Override
        public String getType(final String qName) {
            return getType(getIndex(qName));
        }

        @Override
        public String getValue(final String qName) {
            return getValue(getIndex(qName));
        }

        @Override
        public int getIndex(final String uri, final String localName) {
            if (uri == null || uri.equals(NULL_NS_URI)) {
                return getIndex(localName);
            }

            return getIndex(uri + ":" + localName);
        }

        @Override
        public String getType(final String uri, final String localName) {
            return getType(getIndex(uri, localName));
        }

        @Override
        public String getValue(final String uri, final String localName) {
            return getValue(getIndex(uri, localName));
        }

        boolean nullOrEmpty(final String val) {
            return val == null || val.isEmpty();
        }

        String emptyIfNull(final String val) {
            return val != null ? val : "";
        }
    }

    /**
     * Returns the configuration used by the encoder
     *
     * @return the configuration
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /** Returns the object used to load xml bindings in this encoder */
    public BindingLoader getBindingLoader() {
        return bindingLoader;
    }

    /** Returns the Pico context used by this encoder */
    public PicoContainer getContext() {
        return context;
    }

    @SuppressWarnings("unchecked")
    private static class MatchComparator implements Comparator<Object[]> {
        @Override
        public int compare(final Object[] match1, final Object[] match2) {
            final Binding b1 = (Binding) match1[1];
            final Binding b2 = (Binding) match2[1];

            if (b1.getType() != b2.getType()) {
                if (b2.getType().isAssignableFrom(b1.getType())) {
                    return -1;
                }

                if (b1.getType().isAssignableFrom(b2.getType())) {
                    return 1;
                }
            }

            // use binding comparability
            if (b1 instanceof final Comparable comparable) {
                return comparable.compareTo(b2);
            }

            if (b2 instanceof final Comparable comparable) {
                return -1 * comparable.compareTo(b1);
            }

            return 0;
        }
    }
}
