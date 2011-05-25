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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
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
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDUtil;
import org.geotools.feature.FeatureCollection;
import org.geotools.xml.impl.BindingFactoryImpl;
import org.geotools.xml.impl.BindingLoader;
import org.geotools.xml.impl.BindingPropertyExtractor;
import org.geotools.xml.impl.BindingVisitorDispatch;
import org.geotools.xml.impl.BindingWalker;
import org.geotools.xml.impl.BindingWalkerFactoryImpl;
import org.geotools.xml.impl.ElementEncoder;
import org.geotools.xml.impl.GetPropertyExecutor;
import org.geotools.xml.impl.MismatchedBindingFinder;
import org.geotools.xml.impl.NamespaceSupportWrapper;
import org.geotools.xml.impl.SchemaIndexImpl;
import org.picocontainer.MutablePicoContainer;
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
import org.xml.sax.helpers.NamespaceSupport;


/**
 * Encodes objects as xml based on a schema.
 * <p>
 * The function of the encoder is to traverse a tree of objects seializing them
 * out as xml as it goes. Navigation and serialization of the tree is performed by
 * instances of {@link org.geotools.xml.Binding} which are bound to types in the
 * schema.
 * </p>
 * <br>
 * <p>
 * To execute the encoder, one must have 3 bits of information:
 * <ol>
 *         <li>The root object in the tree to be encoded
 *         <li>The schema / configuration of the intsance document being encoded.
 *         <li>A name of the element defined in the schema which corresponds to the
 *         root object in the tree.
 * </ol>
 * </p>
 * <br>
 * <p>
 * As an exmaple, consider the encoding of a {@link org.opengis.filter.Filter}
 * instance.
 * <pre>
 *         <code>
 *  //instantiate hte configuration for the filter schmea
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
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class Encoder {
    /**
     * Special name recognized by the encoder as a comment.
     * <p>
     * Bindings can return this name in {@link ComplexBinding#getProperties(Object)}
     * to provide comments to be encoded.
     * </p>
     */
    public static final QName COMMENT = new QName("http://www.geotools.org", "comment");

    /** the schema + index **/
    private XSDSchema schema;
    private SchemaIndex index;

    /** binding factory + context **/
    private BindingLoader bindingLoader;
    private MutablePicoContainer context;

    /** binding walker */
    private BindingWalker bindingWalker;

    /** property extractors */
    private List propertyExtractors;

    /** element encoder */
    private ElementEncoder encoder;

    /** factory for creating nodes **/
    private Document doc;

    /** namespaces */
    private NamespaceSupport namespaces;

    /** document serializer **/
    private ContentHandler serializer;

    /** schema location */
    private HashMap schemaLocations;

    /** output format */
    private OutputFormat outputFormat;

    /** namespace aware */
    private boolean namespaceAware = true;

    /** true if we are encoding a full document */
    private boolean inline = false;

    /**
     * Logger logger;
     */
    private Logger logger;

    /**
     * Creates an encoder from a configuration.
     * <p>
     * This constructor calls through to {@link #Encoder(Configuration, XSDSchema)}
     * obtaining the schema instance from {@link Configuration#schema()}.
     * </p>
     * @param configuration The encoder configuration.
     */
    public Encoder(Configuration configuration) {
        this(configuration, configuration.schema());
    }

    /**
     * Creates an encoder from a configuration and a specific schema
     * instance.
     *
     * @param configuration The encoder configuration.
     * @param schema The schema instance.
     */
    public Encoder(Configuration configuration, XSDSchema schema) {
        this.schema = schema;

        index = new SchemaIndexImpl(new XSDSchema[] { schema });

        bindingLoader = new BindingLoader(configuration.setupBindings());
        bindingWalker = new BindingWalker(bindingLoader);

        //create the context
        context = new DefaultPicoContainer();

        //register the binding factory in the context
        BindingFactory bindingFactory = new BindingFactoryImpl(bindingLoader);
        context.registerComponentInstance(bindingFactory);

        //register the element encoder in the context
        encoder = new ElementEncoder(bindingWalker, context);
        context.registerComponentInstance(encoder);

        //register the schema index
        context.registerComponentInstance(index);

        //bindign walker support
        context.registerComponentInstance(new BindingWalkerFactoryImpl(bindingLoader, context));

        //pass the context off to the configuration
        context = configuration.setupContext(context);
        encoder.setContext(context);

        //schema location setup
        schemaLocations = new HashMap();

        // get a logger from the context
        logger = (Logger) context.getComponentInstanceOfType(Logger.class);

        if (logger == null) {
            //create a default
            logger = org.geotools.util.logging.Logging.getLogger("org.geotools.xml");
            context.registerComponentInstance(logger);
        }

        encoder.setLogger(logger);

        //namespaces
        namespaces = new NamespaceSupport();
        context.registerComponentInstance(namespaces);
        context.registerComponentInstance(new NamespaceSupportWrapper(namespaces));

        //add configuration to context;
        context.registerComponentInstance(configuration);

        //property extractors
        propertyExtractors = Schemas.getComponentInstancesOfType(context, PropertyExtractor.class);

        //add the property extractor for bindings as first
        propertyExtractors.add(0, new BindingPropertyExtractor(this, context));

        outputFormat = new OutputFormat();
        
        configuration.setupEncoder(this);
    }
    
    /**
     * Sets the charset encoding scheme to be used in encoding XML content.
     * <p>
     * This encoding will determine the resulting character encoding for the XML
     * content generated by this Encoder and will be reflected in the XML
     * declaration tag.
     * </p>
     * 
     * @param charset
     *            the (non null) charset to encode XML content accordingly to
     */
    public void setEncoding(final Charset charset) {
        final String charsetName = charset.name();
        outputFormat.setEncoding(charsetName);
    }

    /**
     * Returns the Charset defining the character encoding scheme this Encoder
     * uses to encode XML content.
     * <p>
     * If not otherwise set through {@link #setEncoding(Charset)},
     * <code>UTF-8</code> is used.
     * </p>
     * 
     * @return the character set used for encoding
     */
    public Charset getEncoding() {
        final String charsetName = outputFormat.getEncoding();
        final Charset charset = Charset.forName(charsetName);
        return charset;
    }

    /**
     * Sets XML declaration omitting on and off.
     * 
     * @param ommitXmlDeclaration
     *            <code>true</code> if XML declaration should be omitted
     */
    public void setOmitXMLDeclaration(final boolean ommitXmlDeclaration) {
        outputFormat.setOmitXMLDeclaration(ommitXmlDeclaration);
    }

    /**
     * Returns true if the XML document declaration should be omitted. The
     * default is false.
     * 
     * @return whether the xml declaration is omitted, defaults to false.
     */
    public boolean isOmitXMLDeclaration() {
        return outputFormat.getOmitXMLDeclaration();
    }

    /**
     * Sets the indentation on and off.
     * <p>
     * When set on, the default indentation level and default line wrapping is
     * used (see {@link #getIndentSize()} and {@link #getLineWidth()}). To
     * specify a different indentation level or line wrapping, use
     * {@link #setIndent(int)} and {@link #setLineWidth(int)}).
     * </p>
     * 
     * @param doIndent
     *            <code>true</code> if indentation should be on
     */
    public void setIndenting(final boolean doIndent) {
        outputFormat.setIndenting(doIndent);
    }

    /**
     * Returns whether this Encoder produces indented XML.
     * <p>
     * Defaults to <code>false</code>.
     * 
     * @return <code>true</code> if indentation was specified
     * @see #setIndentSize(int)
     */
    public boolean isIndenting() {
        return outputFormat.getIndenting();
    }

    /**
     * Sets the indentation level in number of spaces used.
     * <p>
     * The document will not be indented if the indentation is set to zero.
     * Calling <code>setIndenting(false)</code> will reset this value to zero,
     * calling it with <code>true</code> will reset this value to the default.
     * </p>
     * 
     * @param indentSize
     *            the number, greater or equal than zero, of characters used to
     *            indent, zero for no indentation.
     */
    public void setIndentSize(final int indentSize) {
        if (indentSize < 0) {
            throw new IllegalArgumentException("indentSize shall be >= 0: " + indentSize);
        }
        outputFormat.setIndent(indentSize);
    }

    /**
     * Returns the indentation specified.
     * <p>
     * If no indentation was specified, zero is returned and the document should
     * not be indented.
     * </p>
     * <p>
     * Defaults to <code>4</code>
     * </p>
     * 
     * @return zero if not indenting, the number of white space characters used
     *         for indentation otherwise.
     * @see #setIndenting(boolean)
     */
    public int getIndentSize() {
        return outputFormat.getIndent();
    }

    /**
     * Sets the line width.
     * <p>
     * If zero then no line wrapping will occur. Calling
     * <code>setIndenting(false)</code> will reset this value to zero, calling
     * <code>setIndenting(true)</code> will set this value to the default.
     * </p>
     * 
     * @param lineWidth
     *            a number >= 0 used to limit line widths
     */
    public void setLineWidth(final int lineWidth) {
        if (lineWidth < 0) {
            throw new IllegalArgumentException("lineWidth shall be >= 0: " + lineWidth);
        }
        outputFormat.setLineWidth(lineWidth);
    }

    /**
     * Returns the line width for breaking up long lines.
     * <p>
     * When indenting, and only when indenting, long lines will be broken at
     * space boundaries based on this line width. No line wrapping occurs if
     * this value is zero.
     * </p>
     * <p>
     * Defaults to <code>72</code> characters per line.
     * </p>
     * 
     * @return the number of characters at which line wrapping happens, or zero
     *         for no line wrapping
     * @see #isIndenting()
     */
    public int getLineWidth() {
        return outputFormat.getLineWidth();
    }
    
    /**
     * Sets wether the encoder should be namespace aware.
     * <p>
     * Warning that setting this to <code>false</code> will result in no
     * namespace prefixes on encoded elements and attributes, and no schema
     * declarations on the root element.document;
     * </p>
     * @param namespaces
     */
    public void setNamespaceAware(boolean namespaceAware) {
        this.namespaceAware = namespaceAware;
    }

    /**
     * Returns the namespace mappings maintained by the encoder.
     * <p>
     * Clients may register additional namespace mappings. This is useful when
     * an application whishes to provide some "default" namespace mappings.
     * </p>
     * <p>
     * Clients should register namespace mappings in the current "context", ie
     * do not call {@link NamespaceSupport#pushContext()}. Example:
     * <code>
     * Encoder parser = new Encoder( ... );
     * encoder.getNamespaces().declarePrefix( "foo", "http://www.foo.com" );
     * ...
     * </code>
     * </p>
     *
     * @return The namespace support containing prefix to uri mappings.
     * @since 2.5
     */
    public NamespaceSupport getNamespaces() {
        return namespaces;
    }
    
    /**
     * True if we are encoding a full document, false if the xml headers should be omitted
     * (the encoder is used to generate part of a large document)
     * @param encodeFullDocument
     * 
     * @deprecated use {@link #setInline(boolean)}.
     * 
     */
    public void setEncodeFullDocument(boolean encodeFullDocument) {
        this.inline = !encodeFullDocument;
    }

    /**
     * Sets the encoder to "inline" mode.
     * <p>
     * When this flag is set {@link #encode(Object, QName, ContentHandler)} should
     * be used to encode.
     * </p>
     */
    public void setInline(boolean inline) {
        this.inline = inline;
    }
    
    /**
     * Sets the schema location for a particular namespace uri.
     * <p>
     * Registering a schema location will include it on the "schemaLocation" attribute of the
     * root element of the encoding.
     * </p>
     * @param namespaceURI A namespace uri.
     * @param location A schema location.
     *
     */
    public void setSchemaLocation(String namespaceURI, String location) {
        schemaLocations.put(namespaceURI, location);
    }

    /**
     * Sets hte output format to be used by the encoder.
     *
     * @param outputFormat The output format.
     * @deprecated use the various setters instead (setEncoding, setIndentation, etc)
     */
    public void setOutputFormat(OutputFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    /**
     * @return The walker used to traverse bindings, this method is for internal use only.
     */
    public BindingWalker getBindingWalker() {
        return bindingWalker;
    }

    /**
     * @return The index of schema components, this method is for internal use only.
     */
    public SchemaIndex getSchemaIndex() {
        return index;
    }

    /**
     * @return the schema.
     */
    public XSDSchema getSchema() {
        return schema;
    }

    /**
     * @deprecated use {@link #encode(Object, QName, OutputStream)}.
     *
     */
    public void write(Object object, QName name, OutputStream out)
        throws IOException, SAXException {
        encode(object, name, out);
    }

    /**
     * @return The document used as a factory to create dom nodes.
     */
    public Document getDocument() {
        return doc;
    }

    /**
     * Encodes an object.
     * <p>
     * An object is encoded as an object, name pair, where the name is the name
     * of an element declaration in a schema.
     * </p>
     * @param object The object being encoded.
     * @param name The name of the element being encoded in the schema.
     * @param out The output stream.
     *
     * @throws IOException
     */
    public void encode(Object object, QName name, OutputStream out)
        throws IOException {
        if ( inline ) {
            String msg = "Must use 'encode(Object,QName,ContentHandler)' when inline flag is set";
            throw new IllegalStateException( msg );
        }
        
        //create the document seriaizer
        XMLSerializer xmls = new XMLSerializer(out, outputFormat);

        xmls.setNamespaces(namespaceAware);
        try {
            encode(object, name, xmls);
        } 
        catch (SAXException e) {
            // SAXException does not sets initCause(). Instead, it holds its own
            // "exception" field.
            if (e.getException() != null && e.getCause() == null) {
                e.initCause(e.getException());
            }
            throw (IOException) new IOException().initCause(e); 
        }
    }

    public void encode(Object object, QName name, ContentHandler handler)
        throws IOException, SAXException {
        
        //maintain a stack of (encoding,element declaration pairs)
        Stack encoded = null;
        
        try {
        serializer = handler;

        if (!inline) {
            serializer.startDocument();
        }

        if (namespaceAware) {
            //write out all the namespace prefix value mappings
            for ( Enumeration e = namespaces.getPrefixes(); e.hasMoreElements(); ) {
                String prefix = (String) e.nextElement();
                String uri = namespaces.getURI( prefix );
                
                if ( "xml".equals( prefix ) ) {
                    continue;
                }
                serializer.startPrefixMapping( prefix , uri );
            }
            for (Iterator itr = schema.getQNamePrefixToNamespaceMap().entrySet().iterator();
                    itr.hasNext();) {
                Map.Entry entry = (Map.Entry) itr.next();
                String pre = (String) entry.getKey();
                String ns = (String) entry.getValue();

                if (XSDUtil.SCHEMA_FOR_SCHEMA_URI_2001.equals(ns)) {
                    continue;
                }

                serializer.startPrefixMapping(pre, ns);
                serializer.endPrefixMapping(pre);

                namespaces.declarePrefix((pre != null) ? pre : "", ns);
            }

            //ensure a default namespace prefix set
            if (namespaces.getURI("") == null) {
                namespaces.declarePrefix("", schema.getTargetNamespace());
            }
        }

        //create the document
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

        try {
            doc = docFactory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            new IOException().initCause(e);
        }

        encoded = new Stack();

        //add the first entry
        XSDElementDeclaration root = index.getElementDeclaration(name);

        if (root == null) {
            //check for context hint, this is only used when running the encoder
            // in test mode
            QName typeDefintion = (QName) context.getComponentInstance(
                    "http://geotools.org/typeDefinition");

            if (typeDefintion != null) {
                XSDTypeDefinition type = index.getTypeDefinition(typeDefintion);

                if (type == null) {
                    throw new NullPointerException();
                }

                //create a mock element declaration
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

        encoded.add(new EncodingEntry(object, root, null));

        while (!encoded.isEmpty()) {
            EncodingEntry entry = (EncodingEntry) encoded.peek();

            if (entry.encoding != null) {
                //element has been started, get the next child
                if (!entry.children.isEmpty()) {
                    Object[] child = (Object[]) entry.children.get(0);
                    XSDElementDeclaration element = (XSDElementDeclaration) child[0];
                    Iterator itr = (Iterator) child[1];

                    if (itr.hasNext()) {
                        Object next = itr.next();
                        if ( next == null ) {
                            logger.warning( "Iterator returned null for " + element.getName() );
                        }
                        
                        //here we check for instanceof EncoderDelegate
                        if ( next instanceof EncoderDelegate ) {
                            //do not add entry to the stack, just delegate to encode
                            try {
                                ((EncoderDelegate) next).encode(handler);
                            } 
                            catch (Exception e) {
                                throw new RuntimeException( e );
                            }
                        }
                        else {
                            //add the next object to be encoded to the stack
                            encoded.push(new EncodingEntry(next, element,entry));                            
                        }
                    } else {
                        //iterator done, close it
                        Object source = child[2];
                        closeIterator(itr,source);
                        
                        //this child is done, remove from child list
                        entry.children.remove(0);
                    }
                } else {
                    // no more children, finish the element
                    end(entry.encoding);
                    encoded.pop();
                    
                    //clean up the entry
                    entry.object = null;
                    entry.element = null;
                    entry.encoding = null;
                    entry.children = null;
                    entry.parent = null;
                }
            } else {
                //start the encoding of the entry

                //first make sure the element is not abstract
                if (entry.element.isAbstract()) {
                    // look for a non abstract substitute - substitution groups are subject to
                    // changes over time, so we make a copy to avoid being hit with a ConcurrentModificationException
                    List sub = safeCopy(entry.element.getSubstitutionGroup());


                    if (sub.size() > 0) {
                        //match up by type
                        List matches = new ArrayList();

                        for (Iterator s = sub.iterator(); s.hasNext();) {
                            XSDElementDeclaration e = (XSDElementDeclaration) s.next();

                            if (e == null || e.equals(entry.element)) {
                                continue;
                            }

                            if (e.getName() == null) {
                                continue;
                            }

                            //look up hte binding
                            Binding binding = bindingLoader.loadBinding(new QName(
                                        e.getTargetNamespace(), e.getName()), context);

                            if (binding == null) {
                                //try the type
                                XSDTypeDefinition type = e.getType();

                                if (type == null || type.getName() == null) {
                                    continue;
                                }

                                binding = bindingLoader.loadBinding(new QName(
                                            type.getTargetNamespace(), type.getName()), context);
                            }

                            if (binding == null) {
                                continue;
                            }

                            if (binding.getType() == null) {
                                logger.warning( "Binding: " + binding.getTarget() + " returns null type.");
                                continue;
                            }

                            //match up the type
                            if (binding.getType().isAssignableFrom(entry.object.getClass())) {
                                //we have a match, store as an (element,binding) tuple
                                matches.add(new Object[] { e, binding });
                            }
                        }

                        //if one, we are gold
                        if (matches.size() == 1) {
                            entry.element = (XSDElementDeclaration) ((Object[]) matches.get(0))[0];
                        }
                        //if multiple we have a problem
                        else if (matches.size() > 0) {
                            if (logger.isLoggable(Level.FINE)) {
                                StringBuffer msg = new StringBuffer(
                                        "Found multiple non-abstract bindings for ");
                                msg.append(entry.element.getName()).append(": ");

                                for (Iterator m = matches.iterator(); m.hasNext();) {
                                    msg.append(m.next().getClass().getName());
                                    msg.append(", ");
                                }

                                logger.fine(msg.toString());
                            }

                            //try sorting by the type of the binding
                            Collections.sort(matches,
                                new Comparator() {
                                    public int compare(Object o1, Object o2) {
                                        Object[] match1 = (Object[]) o1;
                                        Object[] match2 = (Object[]) o2;

                                        Binding b1 = (Binding) match1[1];
                                        Binding b2 = (Binding) match2[1];

                                        if ( b1.getType() != b2.getType() ) {
                                            if (b2.getType().isAssignableFrom(b1.getType())) {
                                                return -1;
                                            }
    
                                            if (b1.getType().isAssignableFrom(b2.getType())) {
                                                return 1;
                                            }
                                        }
                                        
                                        //use binding comparability
                                        if (b1 instanceof Comparable) {
                                            return ((Comparable) b1).compareTo(b2);
                                        }

                                        if (b2 instanceof Comparable) {
                                            return -1 * ((Comparable) b2).compareTo(b1);
                                        }

                                        return 0;
                                    }
                                });
                        }

                        if (matches.size() > 0) {
                            entry.element = (XSDElementDeclaration) ((Object[]) matches.get(0))[0];
                        }

                        //if zero, just use the abstract element
                    }
                }

                if (entry.element.isAbstract()) {
                    logger.fine(entry.element.getName() + " is abstract");
                }

                entry.encoding = entry.parent != null ? 
                        (Element) encode(entry.object, entry.element, entry.parent.element.getType()) :
                        (Element) encode(entry.object, entry.element);

                //add any more attributes
                List attributes = index.getAttributes(entry.element);

                for (Iterator itr = attributes.iterator(); itr.hasNext();) {
                    XSDAttributeDeclaration attribute = (XSDAttributeDeclaration) itr.next();

                    //do not encode the attribute if it has already been 
                    // encoded by the parent
                    String ns = attribute.getTargetNamespace();
                    String local = attribute.getName();

                    if ((entry.encoding.getAttributeNS(ns, local) != null)
                            && !"".equals(entry.encoding.getAttributeNS(ns, local))) {
                        continue;
                    }

                    //get the object(s) for this attribute
                    GetPropertyExecutor executor = new GetPropertyExecutor(entry.object, attribute);
                    
                    BindingVisitorDispatch.walk(object, bindingWalker, entry.element, executor, context);

                    if (executor.getChildObject() != null) {
                        //encode the attribute
                        Attr attr = (Attr) encode(executor.getChildObject(), attribute);

                        if (attr != null) {
                            entry.encoding.setAttributeNodeNS(attr);
                        }
                    }
                }

                //write out the leading edge of the element
                if (schemaLocations != null) {
                    //root element, add schema locations if set
                    if (!schemaLocations.isEmpty()) {
                        //declare the schema instance mapping
                        serializer.startPrefixMapping("xsi", XSDUtil.SCHEMA_INSTANCE_URI_2001);
                        serializer.endPrefixMapping("xsi");
                        namespaces.declarePrefix("xsi", XSDUtil.SCHEMA_INSTANCE_URI_2001);

                        StringBuffer schemaLocation = new StringBuffer();

                        for (Iterator e = schemaLocations.entrySet().iterator(); e.hasNext();) {
                            Map.Entry tuple = (Map.Entry) e.next();
                            String namespaceURI = (String) tuple.getKey();
                            String location = (String) tuple.getValue();

                            schemaLocation.append(namespaceURI + " " + location);

                            if (e.hasNext()) {
                                schemaLocation.append(" ");
                            }
                        }

                        entry.encoding.setAttributeNS(XSDUtil.SCHEMA_INSTANCE_URI_2001,
                            "xsi:schemaLocation", schemaLocation.toString());
                    }

                    schemaLocations = null;
                }

                start(entry.encoding);

                //TODO: this method of getting at properties wont maintain order very well, need
                // to come up with a better system that is capable of hanlding feature types
                for (Iterator pe = propertyExtractors.iterator(); pe.hasNext();) {
                    PropertyExtractor propertyExtractor = (PropertyExtractor) pe.next();

                    if (propertyExtractor.canHandle(entry.object)) {
                        List extracted = propertyExtractor.properties(entry.object, entry.element);
O: 
                        for (Iterator e = extracted.iterator(); e.hasNext();) {
                            Object[] tuple = (Object[]) e.next();
                            XSDParticle particle = (XSDParticle) tuple[0];
                            XSDElementDeclaration child = (XSDElementDeclaration) particle
                                .getContent();

                            //check for a comment
                            if ((child != null)
                                    && (COMMENT.getNamespaceURI().equals(child.getTargetNamespace()))
                                    && COMMENT.getLocalPart().equals(child.getName())) {
                                comment(child.getElement());

                                continue;
                            }

                            if (child.isElementDeclarationReference()) {
                                child = child.getResolvedElementDeclaration();
                            }

                            //do not encode the element if the parent has already 
                            // been encoded by the parent
                            String ns = child.getTargetNamespace();
                            String local = child.getName();

                            for (int i = 0; i < entry.encoding.getChildNodes().getLength(); i++) {
                                Node node = entry.encoding.getChildNodes().item(i);

                                if (node instanceof Element) {
                                    if (ns != null) {
                                        if (ns.equals(node.getNamespaceURI())
                                                && local.equals(node.getLocalName())) {
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

                            if (obj == null) {
                                if (particle.getMinOccurs() == 0) {
                                    //cool
                                } else {
                                    //log an error
                                    logger.fine("Property " + ns + ":" + local
                                        + " not found but minoccurs > 0 ");
                                }

                                //skip this regardless
                                continue;
                            }

                            //figure out the maximum number of occurences
                            int maxOccurs = 1;

                            if (particle.isSetMaxOccurs()) {
                                maxOccurs = particle.getMaxOccurs();
                            } else {
                                //look the containing group
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

                            if ((maxOccurs == -1) || (maxOccurs > 1)) {
                                //may have a collection or array, unwrap it
                                Iterator iterator = null;

                                if (obj instanceof Iterator) {
                                    iterator = (Iterator) obj;
                                } else if (obj.getClass().isArray()) {
                                    Object[] array = (Object[]) obj;
                                    iterator = Arrays.asList(array).iterator();
                                } else if (obj instanceof Collection) {
                                    Collection collection = (Collection) obj;
                                    iterator = collection.iterator();
                                } else if (obj instanceof FeatureCollection) {
                                    FeatureCollection collection = (FeatureCollection) obj;
                                    iterator = collection.iterator();
                                } else {
                                    iterator = new SingleIterator(obj);
                                }

                                entry.children.add(new Object[] { child, iterator, obj });
                            } else {
                                //only one, just add the object
                                entry.children.add(new Object[] { child, new SingleIterator(obj), obj });
                            }
                        }
                    }
                }
            }
        }

        if (!inline) {
            serializer.endDocument();
        }
        
        }
        finally {
            //cleanup
            index.destroy();
            
            //close any iterators still present in the stack, this will only occur in an exception
            // case
            if (encoded != null) {
                while(!encoded.isEmpty()) {
                    EncodingEntry entry = (EncodingEntry) encoded.pop();
                    if (!entry.children.isEmpty()) {
                        Object[] child = (Object[]) entry.children.get(0);
                        Iterator itr = (Iterator) child[1];
                        try {
                            closeIterator(itr,child[2]);
                        }
                        catch( Exception e ) {
                            //ignore, we are already in an error case.
                        }
                    }
                }
            }
            //TODO: there are probably other refences to elements of XSDScheam objects, we should
            // kill them too
        }
        
    }
    
    /**
     * Makes a defensive copy of an e-list handling the eventual issues due to concurrent modifications
     * @param substitutionGroup
     * @return
     */
    private List safeCopy(EList substitutionGroup) {
        while(true) {
            try {
                return new ArrayList(substitutionGroup);
            } catch(ArrayIndexOutOfBoundsException e) {
                // ok, the list was modified just during the copy...
            }
        }
    }

    /**
     * Encodes an object directly to a dom.
     * <p>
     * Note that this method should be used for testing or convenience since
     * it does not stream and loads the entire encoded result into memory.
     * </p>
     */
    public Document encodeAsDOM( Object object, QName name ) throws IOException, SAXException, TransformerException  {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encode( object, name, out );
        
        ByteArrayInputStream in = new ByteArrayInputStream( out.toByteArray() );
        DOMResult result = new DOMResult();
        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.transform( new StreamSource( in ), result );
        return (Document) result.getNode();
    }
    
    protected void closeIterator(Iterator itr, Object source) {
        //special case check here for feature collection
        // we need to ensure the iterator is closed properly
        if ( source instanceof FeatureCollection ) {
            //only close the iterator if not just a wrapping one
            if ( !( itr instanceof SingleIterator ) ) {
                ((FeatureCollection)source).close( itr );
            }
        }
    }
    protected Node encode(Object object, XSDNamedComponent component) {
        return encode( object, component, null );
    }
    
    protected Node encode(Object object, XSDNamedComponent component, XSDTypeDefinition container ) {
        if (component instanceof XSDElementDeclaration) {
            XSDElementDeclaration element = (XSDElementDeclaration) component;

            return encoder.encode(object, element, doc, container );
        } else if (component instanceof XSDAttributeDeclaration) {
            XSDAttributeDeclaration attribute = (XSDAttributeDeclaration) component;

            return encoder.encode(object, attribute, doc, container);
        }

        return null;
    }

    protected void start(Element element) throws SAXException {
        String uri = element.getNamespaceURI();
        String local = element.getLocalName();

        String qName = element.getLocalName();

        NamespaceSupport namespaces = this.namespaces;

        if (namespaceAware) {
            uri = (uri != null) ? uri : namespaces.getURI("");
            qName = namespaces.getPrefix(uri) + ":" + qName;
        } else {
            uri = "";
            namespaces = null;
        }

        DOMAttributes atts = new DOMAttributes(element.getAttributes(), namespaces);
        serializer.startElement(uri, local, qName, atts);

        //write out any text
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            Node node = (Node) element.getChildNodes().item(i);

            if (node instanceof Text) {
                char[] ch = ((Text) node).getData().toCharArray();
                serializer.characters(ch, 0, ch.length);
            }
        }

        //write out any child elements
        for (int i = 0; i < element.getChildNodes().getLength(); i++) {
            Node node = (Node) element.getChildNodes().item(i);

            if (node instanceof Element) {
                start((Element) node);
                end((Element) node);
            }
        }

        //push a new context for children, declaring the default prefix to be the one of this 
        // element
        this.namespaces.pushContext();

        if (uri != null) {
            this.namespaces.declarePrefix("", uri);
        }
    }

    protected void comment(Element element) throws SAXException, IOException {
        if (serializer instanceof XMLSerializer) {
            NodeList children = element.getChildNodes();

            for (int i = 0; i < children.getLength(); i++) {
                Node text = (Node) children.item(i);
                ((XMLSerializer) serializer).comment(text.getNodeValue());
            }
        }
    }

    protected void end(Element element) throws SAXException {
        //push off last context
        namespaces.popContext();

        String uri = element.getNamespaceURI();
        String local = element.getLocalName();

        String qName = element.getLocalName();

        if ((element.getPrefix() != null) && !"".equals(element.getPrefix())) {
            qName = element.getPrefix() + ":" + qName;
        }

        serializer.endElement(uri, local, qName);
    }

    private static class NullIterator implements Iterator {
        public void remove() {
            // do nothing
        }

        public boolean hasNext() {
            return false;
        }

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

        public void remove() {
            //unsupported
        }

        public boolean hasNext() {
            return more;
        }

        public Object next() {
            more = false;

            return object;
        }
    }

    /**
     * Encoding stack entries.
     */
    private static class EncodingEntry {
        public Object object;
        public XSDElementDeclaration element;
        public Element encoding;
        public List children; //list of (element,iterator) tuples
        public EncodingEntry parent;
        
        public EncodingEntry(Object object, XSDElementDeclaration element, EncodingEntry parent) {
            this.object = object;
            this.element = element;
            this.parent = parent;
            
            children = new ArrayList();
        }
    }

    private static class DOMAttributes implements Attributes {
        NamedNodeMap atts;
        NamespaceSupport namespaces;

        public DOMAttributes(NamedNodeMap atts, NamespaceSupport namespaces) {
            this.atts = atts;
            this.namespaces = namespaces;
        }

        public int getLength() {
            return atts.getLength();
        }

        public String getLocalName(int index) {
            return atts.item(index).getLocalName();
        }

        public String getQName(int index) {
            Node n = atts.item(index);

            if (namespaces != null) {
                String uri = n.getNamespaceURI();
                String prefix = (uri != null) ? namespaces.getPrefix(uri) : null;

                if (prefix != null) {
                    return prefix + ":" + n.getLocalName();
                }
            }

            return n.getLocalName();
        }

        public String getType(int index) {
            return "CDATA"; //TODO: this properly
        }

        public String getURI(int index) {
            return atts.item(index).getNamespaceURI();
        }

        public String getValue(int index) {
            return atts.item(index).getNodeValue();
        }

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
                Node att = (Node) atts.item(i);

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

        public String getType(String qName) {
            return getType(getIndex(qName));
        }

        public String getValue(String qName) {
            return getValue(getIndex(qName));
        }

        public int getIndex(String uri, String localName) {
            if ((uri == null) || uri.equals("")) {
                return getIndex(localName);
            }

            return getIndex(uri + ":" + localName);
        }

        public String getType(String uri, String localName) {
            return getType(getIndex(uri, localName));
        }

        public String getValue(String uri, String localName) {
            return getValue(getIndex(uri, localName));
        }
    }
}
