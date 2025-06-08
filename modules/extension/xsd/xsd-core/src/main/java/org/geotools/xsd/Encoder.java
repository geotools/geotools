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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
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
import org.eclipse.xsd.util.XSDUtil;
import org.geotools.api.feature.ComplexAttribute;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureCollection;
import org.geotools.xml.XMLUtils;
import org.geotools.xml.transform.QNameValidatingHandler;
import org.geotools.xs.XS;
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
    private XSDSchema schema;

    private SchemaIndex index;

    /** binding factory + context * */
    private BindingLoader bindingLoader;

    private MutablePicoContainer context;

    /** binding walker */
    private BindingWalker bindingWalker;

    /** property extractors */
    private List<PropertyExtractor> propertyExtractors;

    /** element encoder */
    private ElementEncoder encoder;

    /** factory for creating nodes * */
    private Document doc;

    /** namespaces */
    private NamespaceSupport namespaces;

    /** document serializer * */
    private ContentHandler serializer;

    /** schema location */
    private Map<String, String> schemaLocations;

    /** output format/properties */
    private Properties outputProps;

    /** namespace aware */
    private boolean namespaceAware = true;

    /** true if we are encoding a full document */
    private boolean inline = false;

    /** name of type of root element to encode */
    private QName rootElementType;
    /** Logger logger; */
    private Logger logger;

    /** if true the encoder may encode complex features that map to a complex type that is not GML valid * */
    private boolean relaxed = Boolean.parseBoolean(System.getProperty("encoder.relaxed", "true"));

    /** The configuration used by the encoder */
    private Configuration configuration;

    /**
     * Creates an encoder from a configuration.
     *
     * <p>This constructor calls through to {@link #Encoder(Configuration, XSDSchema)} obtaining the schema instance
     * from {@link Configuration#getXSD()#getSchema()}).
     *
     * @param configuration The encoder configuration.
     */
    public Encoder(Configuration configuration) {
        this(configuration, getSchema(configuration));
    }

    public static XSDSchema getSchema(Configuration configuration) {
        try {
            return configuration.getXSD().getSchema();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates an encoder from a configuration and a specific schema instance.
     *
     * @param configuration The encoder configuration.
     * @param schema The schema instance.
     */
    public Encoder(Configuration configuration, XSDSchema schema) {
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
    public void setNamespaceAware(boolean namespaceAware) {
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
    public void setInline(boolean inline) {
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
    public void setRootElementType(QName rootElementType) {
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
    public void setSchemaLocation(String namespaceURI, String location) {
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
    public void encode(Object object, QName name, OutputStream out) throws IOException {
        if (inline) {
            String msg = "Must use 'encode(Object,QName,ContentHandler)' when inline flag is set";
            throw new IllegalStateException(msg);
        }

        // create the document serializer
        SAXTransformerFactory txFactory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();

        TransformerHandler xmls;
        try {
            xmls = txFactory.newTransformerHandler();
        } catch (TransformerConfigurationException e) {
            throw new IOException(e);
        }
        xmls.getTransformer().setOutputProperties(outputProps);
        xmls.getTransformer().setOutputProperty(OutputKeys.METHOD, "xml");
        xmls.setResult(new StreamResult(out));

        // TODO
        // xmls.setNamespaces(namespaceAware);
        xmls = new QNameValidatingHandler(xmls);
        try {
            encode(object, name, xmls);
        } catch (SAXException e) {
            // SAXException does not sets initCause(). Instead, it holds its own
            // "exception" field.
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
    private boolean isNonStripedNestedElement(Object next, XSDElementDeclaration element) {
        if (!(next instanceof ComplexAttribute)) {
            return false;
        }
        ComplexAttribute complex = (ComplexAttribute) next;
        // let's see if we need to encode this
        Collection<Property> nestedProperties = complex.getProperties();
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
        for (XSDParticle childParticle : Schemas.getChildElementParticles(element.getTypeDefinition(), true)) {
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

    public void encode(Object object, QName name, ContentHandler handler) throws IOException, SAXException {

        // maintain a stack of (encoding,element declaration pairs)
        Stack<EncodingEntry> encoded = null;

        // make sure the xs namespace is declared
        if (namespaces.getPrefix(XS.NAMESPACE) == null) {
            namespaces.declarePrefix("xs", XS.NAMESPACE);
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
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

            try {
                doc = docFactory.newDocumentBuilder().newDocument();
            } catch (ParserConfigurationException e) {
                new IOException().initCause(e);
            }

            encoded = new Stack<>();

            XSDElementDeclaration root = getRootDeclaration(name);

            encoded.add(new EncodingEntry(object, root, null));

            while (!encoded.isEmpty()) {
                EncodingEntry entry = encoded.peek();

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
                    EncodingEntry entry = encoded.pop();
                    if (!entry.children.isEmpty()) {
                        Object[] child = entry.children.get(0);
                        Iterator itr = (Iterator) child[1];
                        try {
                            closeIterator(itr, child[2]);
                        } catch (Exception e) {
                            // ignore, we are already in an error case.
                        }
                    }
                }
            }
            // TODO: there are probably other references to elements of XSDSchema objects, we should
            // kill them too
        }
    }

    private void startEncoding(Object object, EncodingEntry entry) throws SAXException, IOException {
        // first make sure the element is not abstract
        if (entry.element.isAbstract()) {
            // look for a non abstract substitute - substitution groups are subject to
            // changes over time, so we make a copy to avoid being hit with a
            // ConcurrentModificationException
            List sub = safeCopy(entry.element.getSubstitutionGroup());

            if (!sub.isEmpty()) {
                XSDElementDeclaration substitute = getConcreteSubstitute(entry, sub);
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

    private void setupEntryAttributes(Object object, EncodingEntry entry) {
        List attributes = index.getAttributes(entry.element);

        for (Object value : attributes) {
            XSDAttributeDeclaration attribute = (XSDAttributeDeclaration) value;

            // do not encode the attribute if it has already been
            // encoded by the parent
            String ns = attribute.getTargetNamespace();
            String local = attribute.getName();

            if (entry.encoding.getAttributeNS(ns, local) != null
                    && !"".equals(entry.encoding.getAttributeNS(ns, local))) {
                continue;
            }

            // get the object(s) for this attribute
            GetPropertyExecutor executor = new GetPropertyExecutor(entry.object, attribute);

            BindingVisitorDispatch.walk(object, bindingWalker, entry.element, executor, context);

            if (executor.getChildObject() != null) {
                // encode the attribute
                Attr attr = (Attr) encode(executor.getChildObject(), attribute);

                if (attr != null) {
                    entry.encoding.setAttributeNodeNS(attr);
                }
            }
        }
    }

    private void populateChildren(EncodingEntry entry) throws SAXException, IOException {
        // TODO: this method of getting at properties won't maintain order very well,
        // need to come up with a better system that is capable of handling feature types
        for (PropertyExtractor propertyExtractor : propertyExtractors) {
            if (propertyExtractor.canHandle(entry.object)) {
                List extracted = propertyExtractor.properties(entry.object, entry.element);
                O:
                for (Object o : extracted) {
                    Object[] tuple = (Object[]) o;
                    XSDParticle particle = (XSDParticle) tuple[0];
                    XSDElementDeclaration child = (XSDElementDeclaration) particle.getContent();

                    if (child == null) {
                        continue;
                    }

                    // check for a comment
                    if (child != null
                            && COMMENT.getNamespaceURI().equals(child.getTargetNamespace())
                            && COMMENT.getLocalPart().equals(child.getName())) {
                        comment(child.getElement());

                        continue;
                    }

                    if (child.isElementDeclarationReference()) {
                        child = child.getResolvedElementDeclaration();
                    }

                    // do not encode the element if the parent has already
                    // been encoded by the parent
                    String ns = child.getTargetNamespace();
                    String local = child.getName();

                    for (int i = 0; i < entry.encoding.getChildNodes().getLength(); i++) {
                        Node node = entry.encoding.getChildNodes().item(i);

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

                    Object obj = tuple[1];

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
                            XSDModelGroup group = (XSDModelGroup) particle.eContainer();

                            if (group.eContainer() instanceof XSDParticle) {
                                XSDParticle cParticle = (XSDParticle) group.eContainer();

                                if (cParticle.isSetMaxOccurs()) {
                                    maxOccurs = cParticle.getMaxOccurs();
                                }
                            }
                        }
                    }

                    if (maxOccurs == -1 || maxOccurs > 1) {
                        // may have a collection or array, unwrap it
                        Iterator iterator = null;

                        if (obj instanceof Iterator) {
                            iterator = (Iterator) obj;
                        } else if (obj != null && obj.getClass().isArray()) {
                            Object[] array = (Object[]) obj;
                            iterator = Arrays.asList(array).iterator();
                        } else if (obj instanceof Collection) {
                            Collection collection = (Collection) obj;
                            iterator = collection.iterator();
                        } else if (obj instanceof FeatureCollection) {
                            @SuppressWarnings("unchecked")
                            FeatureCollection<FeatureType, Feature> collection = (FeatureCollection) obj;
                            iterator = DataUtilities.iterator(collection.features());
                        } else {
                            iterator = new SingleIterator(obj);
                        }

                        entry.children.add(new Object[] {child, iterator, obj});
                    } else {
                        // only one, just add the object
                        entry.children.add(new Object[] {child, new SingleIterator(obj), obj});
                    }
                }
            }
        }
    }

    private void setupSchemaLocations(EncodingEntry entry) throws SAXException {
        // write out the leading edge of the element
        if (schemaLocations != null) {
            // root element, add schema locations if set
            if (!schemaLocations.isEmpty()) {
                // declare the schema instance mapping
                serializer.startPrefixMapping("xsi", XSDUtil.SCHEMA_INSTANCE_URI_2001);
                serializer.endPrefixMapping("xsi");
                namespaces.declarePrefix("xsi", XSDUtil.SCHEMA_INSTANCE_URI_2001);

                StringBuffer schemaLocation = new StringBuffer();

                for (Iterator e = schemaLocations.entrySet().iterator(); e.hasNext(); ) {
                    Map.Entry tuple = (Map.Entry) e.next();
                    String namespaceURI = (String) tuple.getKey();
                    String location = (String) tuple.getValue();

                    schemaLocation.append(namespaceURI + " " + location);

                    if (e.hasNext()) {
                        schemaLocation.append(" ");
                    }
                }

                entry.encoding.setAttributeNS(
                        XSDUtil.SCHEMA_INSTANCE_URI_2001, "xsi:schemaLocation", schemaLocation.toString());
            }

            schemaLocations = null;
        }
    }

    private XSDElementDeclaration getConcreteSubstitute(EncodingEntry entry, List sub) {
        // match up by type
        List<Object[]> matches = new ArrayList<>();

        for (Object o : sub) {
            XSDElementDeclaration e = (XSDElementDeclaration) o;

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
                XSDTypeDefinition type = e.getType();

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
        }
        // if multiple we have a problem
        else if (!matches.isEmpty()) {
            if (logger.isLoggable(Level.FINE)) {
                StringBuffer msg = new StringBuffer("Found multiple non-abstract bindings for ");
                msg.append(entry.element.getName()).append(": ");

                for (Object[] match : matches) {
                    msg.append(match.getClass().getName());
                    msg.append(", ");
                }

                logger.fine(msg.toString());
            }

            // try sorting by the type of the binding
            Collections.sort(matches, new MatchComparator());
        }

        if (!matches.isEmpty()) {
            return (XSDElementDeclaration) matches.get(0)[0];
        }
        return null;
    }

    private void finishElement(Stack<EncodingEntry> encoded, EncodingEntry entry) throws SAXException {
        end(entry.encoding, entry.element);
        encoded.pop();

        // clean up the entry
        entry.object = null;
        entry.element = null;
        entry.encoding = null;
        entry.children = null;
        entry.parent = null;
    }

    private void processChildren(ContentHandler handler, Stack<EncodingEntry> encoded, EncodingEntry entry) {
        Object[] child = entry.children.get(0);
        XSDElementDeclaration element = ((XSDElementDeclaration) child[0]).getResolvedElementDeclaration();
        Iterator itr = (Iterator) child[1];

        if (itr.hasNext()) {
            Object next = itr.next();

            // here we check for instanceof EncoderDelegate
            if (next instanceof EncoderDelegate) {
                // do not add entry to the stack, just delegate to encode
                try {
                    ((EncoderDelegate) next).encode(handler);
                } catch (Exception e) {
                    throw new RuntimeException("Error encoding object to xml-element", e);
                }
            } else {
                if (next instanceof ComplexAttribute && relaxed && isNonStripedNestedElement(next, element)) {
                    for (Property property : ((ComplexAttribute) next).getProperties()) {
                        // add object sub properties, i.e. nested complex features
                        encoded.push(new EncodingEntry(property, element, entry));
                    }
                } else {
                    // add the next object to be encoded to the stack
                    encoded.push(new EncodingEntry(next, element, entry));
                }
            }
        } else {
            // iterator done, close it
            Object source = child[2];
            closeIterator(itr, source);

            // this child is done, remove from child list
            entry.children.remove(0);
        }
    }

    private XSDElementDeclaration getRootDeclaration(QName name) {
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
                XSDTypeDefinition type = index.getTypeDefinition(typeDefintion);

                if (type == null) {
                    throw new NullPointerException();
                }

                // create a mock element declaration
                root = XSDFactory.eINSTANCE.createXSDElementDeclaration();
                root.setName(name.getLocalPart());
                root.setTargetNamespace(name.getNamespaceURI());
                root.setTypeDefinition(type);
            }
        }

        if (root == null) {
            String msg = "Could not find element declaration for:" + name;
            throw new IllegalArgumentException(msg);
        }
        return root;
    }

    private void setupNamespaces() throws SAXException {
        // write out all the namespace prefix value mappings
        for (Enumeration e = namespaces.getPrefixes(); e.hasMoreElements(); ) {
            String prefix = (String) e.nextElement();
            String uri = namespaces.getURI(prefix);

            if ("xml".equals(prefix)) {
                continue;
            }
            serializer.startPrefixMapping(prefix, uri);
        }
        for (Map.Entry<String, String> stringStringEntry :
                schema.getQNamePrefixToNamespaceMap().entrySet()) {
            Map.Entry entry = stringStringEntry;
            String pre = (String) entry.getKey();
            String ns = (String) entry.getValue();

            if (XSDUtil.SCHEMA_FOR_SCHEMA_URI_2001.equals(ns)) {
                continue;
            }

            // skip ones already registered
            if (namespaces.getPrefix(ns) != null) {
                continue;
            }
            serializer.startPrefixMapping(pre != null ? pre : "", ns);
            serializer.endPrefixMapping(pre != null ? pre : "");

            namespaces.declarePrefix(pre != null ? pre : "", ns);
        }

        // ensure a default namespace prefix set
        if (namespaces.getURI("") == null) {
            namespaces.declarePrefix("", schema.getTargetNamespace());
        }
    }

    /** Makes a defensive copy of an e-list handling the eventual issues due to concurrent modifications */
    private <T> List<T> safeCopy(EList<T> substitutionGroup) {
        while (true) {
            try {
                return new ArrayList<>(substitutionGroup);
            } catch (ArrayIndexOutOfBoundsException e) {
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
    public Document encodeAsDOM(Object object, QName name) throws IOException, SAXException, TransformerException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encode(object, name, out);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        DOMResult result = new DOMResult();
        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.transform(new StreamSource(in), result);
        return (Document) result.getNode();
    }

    /**
     * Encodes an object directly to a string.
     *
     * <p>Note that this method should be used for testing or convenience since it does not stream and loads the entire
     * encoded result into memory.
     *
     * @since 8.0
     */
    public String encodeAsString(Object object, QName name) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encode(object, name, out);
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    protected void closeIterator(Iterator iterator, Object source) {
        DataUtilities.close(iterator);
    }

    protected Node encode(Object object, XSDNamedComponent component) {
        return encode(object, component, null);
    }

    protected Node encode(Object object, XSDNamedComponent component, XSDTypeDefinition container) {
        if (component instanceof XSDElementDeclaration) {
            XSDElementDeclaration element = (XSDElementDeclaration) component;

            return encoder.encode(object, element, doc, container);
        } else if (component instanceof XSDAttributeDeclaration) {
            XSDAttributeDeclaration attribute = (XSDAttributeDeclaration) component;

            return encoder.encode(object, attribute, doc, container);
        }

        return null;
    }

    protected void start(Element element, XSDElementDeclaration declaration) throws SAXException {
        String uri, local;

        if (element.getLocalName() != null) {
            uri = element.getNamespaceURI();
            local = element.getLocalName();
        } else {
            // namespace unaware dom tree
            local = element.getNodeName();
            if (local.contains(":")) {
                String[] split = local.split(":");
                local = split[1];
                uri = namespaces.getURI(split[0]);
            } else {
                uri = null;
            }
        }
        String qName = local;

        NamespaceSupport namespaces = this.namespaces;

        // declaration == null -> gml3 envelope encoding test failing
        // declaration.getSchema() == null -> wfs 2.0 feature collection encoding test failing
        if (forceQualified(declaration)) {
            uri = uri != null ? uri : namespaces.getURI("");
            qName = namespaces.getPrefix(uri) + ":" + qName;

        } else {
            uri = "";
        }

        DOMAttributes atts = new DOMAttributes(element.getAttributes(), namespaces);
        serializer.startElement(uri, local, qName, atts);

        // write out any text
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            Node node = element.getChildNodes().item(i);

            if (node instanceof Text) {
                String data = XMLUtils.removeXMLInvalidChars(((Text) node).getData());
                char[] ch = data.toCharArray();
                serializer.characters(ch, 0, ch.length);
            }
        }

        // write out any child elements
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            Node node = element.getChildNodes().item(i);

            if (node instanceof Element) {
                Element child = (Element) node;
                QName childName = new QName(child.getNamespaceURI(), child.getNodeName());
                XSDElementDeclaration childDecl =
                        declaration != null ? Schemas.getChildElementDeclaration(declaration, childName) : null;
                start(child, childDecl);
                end(child, childDecl);
            }
        }

        // push a new context for children, declaring the default prefix to be the one of this
        // element
        this.namespaces.pushContext();

        if (uri != null) {
            this.namespaces.declarePrefix("", uri);
        }
    }

    boolean forceQualified(XSDElementDeclaration e) {
        return namespaceAware
                && (e == null
                        || e.isGlobal()
                        || e.getSchema() == null
                        || e.getSchema().getElementFormDefault() == XSDForm.QUALIFIED_LITERAL);
    }

    protected void comment(Element element) throws SAXException, IOException {
        if (serializer instanceof LexicalHandler) {
            NodeList children = element.getChildNodes();

            for (int i = 0; i < children.getLength(); i++) {
                Node text = children.item(i);
                String str = text.getNodeValue();
                ((LexicalHandler) serializer).comment(str.toCharArray(), 0, str.length());
            }
        }
    }

    protected void end(Element element, XSDElementDeclaration declaration) throws SAXException {
        // push off last context
        namespaces.popContext();

        String uri = element.getNamespaceURI();
        String local = element.getLocalName();
        String qName = element.getLocalName();

        if (element.getPrefix() != null && !"".equals(element.getPrefix())) {
            qName = element.getPrefix() + ":" + qName;
        } else {
            if (forceQualified(declaration)) {
                uri = uri != null ? uri : namespaces.getURI("");
                if (uri != null) {
                    qName = namespaces.getPrefix(uri) + ":" + qName;
                } else {
                    uri = "";
                }
            }
        }

        serializer.endElement(uri, local, qName);
    }

    private static class NullIterator implements Iterator {
        @Override
        public void remove() {
            // do nothing
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    private static class SingleIterator implements Iterator {
        Object object;
        boolean more;

        public SingleIterator(Object object) {
            this.object = object;
            more = true;
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
        public Object next() {
            more = false;

            return object;
        }
    }

    /** Encoding stack entries. */
    private static class EncodingEntry {
        public Object object;
        public XSDElementDeclaration element;
        public Element encoding;
        public List<Object[]> children; // list of (element,iterator) tuples
        public EncodingEntry parent;

        public EncodingEntry(Object object, XSDElementDeclaration element, EncodingEntry parent) {
            this.object = object;
            this.element = element;
            this.parent = parent;

            children = new ArrayList<>();
        }
    }

    private static class DOMAttributes implements Attributes {
        NamedNodeMap atts;
        NamespaceSupport namespaces;

        public DOMAttributes(NamedNodeMap atts, NamespaceSupport namespaces) {
            this.atts = atts;
            this.namespaces = namespaces;
        }

        @Override
        public int getLength() {
            return atts.getLength();
        }

        @Override
        public String getLocalName(int index) {
            String local = atts.item(index).getLocalName();
            if (nullOrEmpty(local)) {
                // check the qname
                String qName = getQName(index);
                if (!nullOrEmpty(qName)) {
                    int dot = qName.indexOf(':');
                    if (dot > -1) {
                        local = qName.split(":")[1];
                    }
                }
            }

            return emptyIfNull(local);
        }

        @Override
        public String getQName(int index) {
            Node n = atts.item(index);

            if (namespaces != null) {
                String uri = n.getNamespaceURI();
                String prefix = uri != null ? namespaces.getPrefix(uri) : null;

                if (prefix != null) {
                    return prefix + ":" + n.getLocalName();
                }
            }

            return n.getLocalName() != null ? n.getLocalName() : n.getNodeName();
        }

        @Override
        public String getType(int index) {
            return "CDATA"; // TODO: this properly
        }

        @Override
        public String getURI(int index) {
            String ns = atts.item(index).getNamespaceURI();
            if (ns == null) {
                ns = XMLUtils.qName(getQName(index), namespaces).getNamespaceURI();
            }

            return emptyIfNull(ns);
        }

        @Override
        public String getValue(int index) {
            return atts.item(index).getNodeValue();
        }

        @Override
        public int getIndex(String qName) {
            String pre = null;
            String local = null;

            if (qName.lastIndexOf(':') != -1) {
                String[] split = qName.split(":");
                pre = split[0];
                local = split[1];
            } else {
                pre = "";
                local = qName;
            }

            for (int i = 0; i < atts.getLength(); i++) {
                Node att = atts.item(i);

                if (att.getLocalName().equals(local)) {
                    String apre = att.getPrefix();

                    if (apre == null) {
                        apre = "";
                    }

                    if (pre.equals(apre)) {
                        return i;
                    }
                }
            }

            return -1;
        }

        @Override
        public String getType(String qName) {
            return getType(getIndex(qName));
        }

        @Override
        public String getValue(String qName) {
            return getValue(getIndex(qName));
        }

        @Override
        public int getIndex(String uri, String localName) {
            if (uri == null || uri.equals("")) {
                return getIndex(localName);
            }

            return getIndex(uri + ":" + localName);
        }

        @Override
        public String getType(String uri, String localName) {
            return getType(getIndex(uri, localName));
        }

        @Override
        public String getValue(String uri, String localName) {
            return getValue(getIndex(uri, localName));
        }

        boolean nullOrEmpty(String val) {
            return val == null || "".equals(val);
        }

        String emptyIfNull(String val) {
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
        public int compare(Object[] match1, Object[] match2) {
            Binding b1 = (Binding) match1[1];
            Binding b2 = (Binding) match2[1];

            if (b1.getType() != b2.getType()) {
                if (b2.getType().isAssignableFrom(b1.getType())) {
                    return -1;
                }

                if (b1.getType().isAssignableFrom(b2.getType())) {
                    return 1;
                }
            }

            // use binding comparability
            if (b1 instanceof Comparable) {
                return ((Comparable) b1).compareTo(b2);
            }

            if (b2 instanceof Comparable) {
                return -1 * ((Comparable) b2).compareTo(b1);
            }

            return 0;
        }
    }
}
