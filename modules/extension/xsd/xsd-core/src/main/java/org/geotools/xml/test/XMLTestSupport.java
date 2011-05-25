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
package org.geotools.xml.test;

import junit.framework.TestCase;
import org.eclipse.xsd.XSDSchema;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.NamespaceSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.geotools.xml.Binding;
import org.geotools.xml.Configuration;
import org.geotools.xml.DOMParser;
import org.geotools.xml.Encoder;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.XSD;
import org.geotools.xml.impl.BindingFactoryImpl;
import org.geotools.xml.impl.BindingLoader;
import org.geotools.xml.impl.BindingWalkerFactoryImpl;
import org.geotools.xml.impl.NamespaceSupportWrapper;
import org.geotools.xml.impl.SchemaIndexImpl;


/**
 * Abstract test class to be used to unit test bindings.
 * <p>
 * Subclasses must implement the {@link #createConfiguration()} method. It must
 * return a new instance of {@link Configuration}. Example:
 *
 * <pre>
 *         <code>
 *  public MailTypeBindingTest extends XMLTestSupport {
 *
 *      protected Configuration createConfiguration() {
 *         return new MLConfiguration();
 *      }
 *  }
 *         </code>
 * </pre>
 *
 * </p>
 * <p>
 * The {@link #parse()} method is used to test binding parsing. Subclasses
 * should call this from test methods after building up an instance document
 * with {@link #document}. Example
 *
 * <pre>
 *         <code>
 *  public void testParsing() throws Exception {
 *      //build up an instance document
 *
 *      //the root element
 *      Element mailElement = document.createElementNS( ML.NAMESPACE, "mail" );
 *      document.appendChild( mailElement );
 *
 *      mailElement.setAttribute( "id", "someId" );
 *      ....
 *
 *      //call parse
 *      Mail mail = (Mail) parse();
 *
 *      //make assertions
 *      assertEquals( "someId", mail.getId() );
 *  }
 *         </code>
 * </pre>
 * </p>
 *
 * <p>
 * The {@link #encode(Object, QName)} method is used to test binding encoding.
 * Subclasses should call this method from test methods after creating an
 * object to be encoded. Example:
 *
 * <pre>
 *         <code>
 * public void testEncoding() throws Exception {
 *    //create the mail object
 *    Mail mail = new Mail( "someId" );
 *    mail.setEnvelope( ... );
 *    ....
 *
 *    //call encode
 *    Document document = encode( mail, new QName( ML.NAMESPACE, "mail" );
 *
 *    //make assertions
 *    assertEquals( "mail", document.getDocumentElement().getNodeName() );
 *    assertEquals( "someId", document.getDocumentElement().getAttribute( "id" ) );
 * }
 *         </code>
 * </pre>
 * </p>
 *
 * <p>
 * The {@link #binding(QName)} method is used to obtain an instance of a
 * particular binding. Subclasses should call this method to assert other
 * properties of the binding, such as type mapping and execution mode. Example:
 *
 * <pre>
 *         <code>
 *  public void testType() {
 *     //get an instance of the binding
 *     Binding binding = binding( new QName( ML.NAMESPACE, "MailType" ) );
 *
 *     //make assertions
 *     assertEquals( Mail.class, binding.getType() );
 *  }
 *
 *  public void testExecutionMode() {
 *    //get an instance of the binding
 *    Binding binding = binding( new QName( ML.NAMESPACE, "MailType" ) );
 *
 *    //make assertions
 *    assertEquals( Binding.OVERRIDE, binding.getExecutionMode() );
 *  }
 *         </code>
 * </pre>
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 *
 * @source $URL$
 */
public abstract class XMLTestSupport extends TestCase {
    /**
     * Logging instance
     */
    protected static Logger logger = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.xml.test");

    /**
     * the instance document
     */
    protected Document document;

    /**
     * additional namespace mappings
     */
    protected HashMap namespaceMappings;
    /**
     * Creates an empty xml document.
     */
    protected void setUp() throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);

        document = docFactory.newDocumentBuilder().newDocument();
        namespaceMappings = new HashMap();
    }

    /**
     * Template method for subclasses to register namespace mappings on the
     * root element of an instance document.
     * <p>
     * Namespace mappings should be set as follows:
     * <pre>
     * <code>
     *        root.setAttribute( "xmlns:gml", http://www.opengis.net/gml" );
     * </code>
     * </pre>
     * </p>
     * <p>
     * Subclasses of this method should register the default namespace, the
     * default namesapce is the one returned by the configuration.
     * </p>
     * <p>
     * This method is intended to be extended or overiden. This implementation
     * registers the <code>xsi,http://www.w3.org/2001/XMLSchema-instance</code>
     * namespace.
     * </p>
     * @param root The root node of the instance document.
     * @deprecated use {@link #registerNamespaceMapping(String, String)}
     *
     */
    protected void registerNamespaces(Element root) {
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    }

    /**
     * Registers a namespace mapping.
     * <p>
     * This mapping will be included in the "namespace context" of both the 
     * parser and the encoder.
     * </p>
     * @param prefix The prefix of the namespace, not <code>null</code>.
     * @param uri The uri of the namespace, not <code>null</code>.
     */
    protected void registerNamespaceMapping( String prefix, String uri ) {
        namespaceMappings.put( prefix, uri );
    }
    
    /**
     * Tempalte method for subclasses to create the configuration to be used by
     * the parser.
     *
     * @return A parser configuration.
     */
    protected abstract Configuration createConfiguration();

    /**
     * Parses the build document, explicity specifying the type of the root
     * element.
     * <p>
     * This method should be called after building the entire document.
     *</p>
     * @param type The name of the type of the root element of the build document.
     */
    protected Object parse(QName type) throws Exception {
        Element root = document.getDocumentElement();

        if (root == null) {
            throw new IllegalStateException("Document has no root element");
        }

        Configuration config = createConfiguration();

        if (type != null) {
            config.getContext().registerComponentInstance("http://geotools.org/typeDefinition", type);
        }

        //register additional namespaces
        registerNamespaces(root);
        for ( Iterator e = namespaceMappings.entrySet().iterator(); e.hasNext(); ) {
            Map.Entry mapping = (Map.Entry) e.next();
            String prefix = (String) mapping.getKey();
            String uri = (String) mapping.getValue();
            
            root.setAttribute("xmlns:" + prefix, uri );
        }
        
        //default
        root.setAttribute("xsi:schemaLocation",
            config.getNamespaceURI() + " " + config.getSchemaFileURL());

        DOMParser parser = new DOMParser(config, document);

        return parser.parse();
    }

    /**
     * Parses the built document.
     * <p>
     * This method should be called after building the entire document.
     *</p>
     */
    protected Object parse() throws Exception {
        return parse(null);
    }

    /**
     * Encodes an object, element name pair explicitly specifying the type of
     * the root element.
     *
     * @param object The object to encode.
     * @param element The name of the element to encode.
     * @param type The type of the element
     *
     * @return The object encoded.
     * @throws Exception
     */
    protected Document encode(Object object, QName element, QName type)
        throws Exception {
        Configuration configuration = createConfiguration();

        if (type != null) {
            //set the hint
            configuration.getContext()
                         .registerComponentInstance("http://geotools.org/typeDefinition", type);
        }

        XSDSchema schema = configuration.getXSD().getSchema();

        Encoder encoder = new Encoder(configuration, schema);
        
        //additional namespaces
        for ( Iterator e = namespaceMappings.entrySet().iterator(); e.hasNext(); ) {
            Map.Entry mapping = (Map.Entry) e.next();
            String prefix = (String) mapping.getKey();
            String uri = (String) mapping.getValue();
            
            encoder.getNamespaces().declarePrefix( prefix, uri );
        }
        
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        encoder.write(object, element, output);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        
        return dbf.newDocumentBuilder().parse(new ByteArrayInputStream(output.toByteArray()));
    }

    /**
     * Encodes an object, element name pair.
     *
     * @param object The object to encode.
     * @param element The name of the element to encode.
     *
     * @return The object encoded.
     * @throws Exception
     */
    protected Document encode(Object object, QName element)
        throws Exception {
        return encode(object, element, null);
    }

    /**
     * Convenience method to dump the contents of the document to stdout.
     *
     *
     */
    protected void print(Node dom) throws Exception {
        TransformerFactory txFactory = TransformerFactory.newInstance();
        Transformer tx = txFactory.newTransformer();
        tx.setOutputProperty(OutputKeys.INDENT, "yes");
        
        tx.transform(new DOMSource(dom), new StreamResult(System.out));
    }

    /**
     * Convenience method for obtaining an instance of a binding.
     *
     * @param name The qualified name of the element,attribute,or type the
     * binding "binds" to, the key of the binding in the container.
     *
     * @return The binding.
     */
    protected Binding binding(QName name) {
        Configuration configuration = createConfiguration();

        //create the context
        MutablePicoContainer context = new DefaultPicoContainer();
        context = configuration.setupContext(context);

        //create the binding container
        Map bindings = configuration.setupBindings();
        BindingLoader bindingLoader = new BindingLoader(bindings);
//        MutablePicoContainer container = bindingLoader.getContainer();
//        container = configuration.setupBindings(container);
//        bindingLoader.setContainer(container);

        //register cmponents available to bindings at runtime
        context.registerComponentInstance(new BindingFactoryImpl(bindingLoader));

        //binding walker support
        context.registerComponentInstance(new BindingWalkerFactoryImpl(bindingLoader, context));

        //logger
        context.registerComponentInstance(logger);

        //setup the namespace support
        NamespaceSupport namespaces = new NamespaceSupport();
        HashMap mappings = new HashMap();

        try {
            for (Iterator d = configuration.getXSD().getDependencies().iterator(); d.hasNext();) {
                XSD xsd = (XSD) d.next();
                XSDSchema schema = xsd.getSchema();

                mappings.putAll(schema.getQNamePrefixToNamespaceMap());
            }

            mappings.putAll(configuration.getXSD().getSchema().getQNamePrefixToNamespaceMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Iterator m = mappings.entrySet().iterator(); m.hasNext();) {
            Map.Entry mapping = (Map.Entry) m.next();
            String key = (String) mapping.getKey();

            if (key == null) {
                key = "";
            }

            namespaces.declarePrefix(key, (String) mapping.getValue());
        }

        context.registerComponentInstance(namespaces);
        context.registerComponentInstance(new NamespaceSupportWrapper(namespaces));
        
        SchemaIndex index = new SchemaIndexImpl( new XSDSchema[]{configuration.schema()} );
        context.registerComponentInstance(index);
        
        context.registerComponentInstance(configuration);
        
        return bindingLoader.loadBinding(name, context);
    }

    /**
     * Convenience method which parses the specified string into a dom and sets
     * the built document which is to be parsed.
     *
     * @param xml A string of xml
     */
    protected void buildDocument(String xml) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);

        document = docFactory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes()));
    }

    /**
     * Convenience method for finding a node in a document which matches the
     * specified name.
     *
     */
    protected Element getElementByQName(Document dom, QName name) {
        return getElementByQName(dom.getDocumentElement(), name);
    }

    /**
     * Convenience method for finding a single descendant of a particular node
     * which matches the specified name.
     *
     */
    protected Element getElementByQName(Element parent, QName name) {
        NodeList nodes = parent.getElementsByTagNameNS(name.getNamespaceURI(), name.getLocalPart());

        if (nodes.getLength() == 0) {
            return null;
        }

        return (Element) nodes.item(0);
    }

    /**
     * Convenience method for finding nodes in a document which matche the
     * specified name.
     *
     */
    protected NodeList getElementsByQName(Document dom, QName name) {
        return getElementsByQName(dom.getDocumentElement(), name);
    }

    /**
     * Convenience method for finding decendants of a particular node which match
     * the specified name.
     *
     */
    protected NodeList getElementsByQName(Element parent, QName name) {
        return parent.getElementsByTagNameNS(name.getNamespaceURI(), name.getLocalPart());
    }
}
