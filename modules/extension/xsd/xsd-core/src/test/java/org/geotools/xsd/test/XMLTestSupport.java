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
package org.geotools.xsd.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.eclipse.xsd.XSDSchema;
import org.geotools.test.xml.XmlTestSupport;
import org.geotools.xsd.Binding;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.DOMParser;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.SchemaIndex;
import org.geotools.xsd.XSD;
import org.geotools.xsd.impl.BindingFactoryImpl;
import org.geotools.xsd.impl.BindingLoader;
import org.geotools.xsd.impl.BindingWalkerFactoryImpl;
import org.geotools.xsd.impl.NamespaceSupportWrapper;
import org.geotools.xsd.impl.SchemaIndexImpl;
import org.junit.Before;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Abstract test class to be used to unit test bindings.
 *
 * <p>Subclasses must implement the {@link #createConfiguration()} method. It must return a new instance of
 * {@link Configuration}. Example:
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
 * <p>The {@link #parse()} method is used to test binding parsing. Subclasses should call this from test methods after
 * building up an instance document with {@link #document}. Example
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
 *
 * <p>The {@link #encode(Object, QName)} method is used to test binding encoding. Subclasses should call this method
 * from test methods after creating an object to be encoded. Example:
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
 *
 * <p>The {@link #binding(QName)} method is used to obtain an instance of a particular binding. Subclasses should call
 * this method to assert other properties of the binding, such as type mapping and execution mode. Example:
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
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
public abstract class XMLTestSupport extends XmlTestSupport {
    /** Logging instance */
    protected static Logger logger = org.geotools.util.logging.Logging.getLogger(XMLTestSupport.class);

    /** the instance document */
    protected Document document;

    /** Creates an empty xml document. */
    @Before
    public void setUp() throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);

        document = docFactory.newDocumentBuilder().newDocument();
    }

    /**
     * Tempalte method for subclasses to create the configuration to be used by the parser.
     *
     * @return A parser configuration.
     */
    protected abstract Configuration createConfiguration();

    /**
     * Parses the build document, explicity specifying the type of the root element.
     *
     * <p>This method should be called after building the entire document.
     *
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

        // register additional namespaces
        root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        for (Map.Entry<String, String> namespace : getNamespaces().entrySet()) {
            String prefix = namespace.getKey();
            String uri = namespace.getValue();

            root.setAttribute("xmlns:" + prefix, uri);
        }

        // process the schemaLocation, replace any schema locations that we know about
        if (root.hasAttribute("xsi:schemaLocation")) {
            XSD xsd = config.getXSD();
            List<XSD> deps = xsd.getAllDependencies();
            deps.add(xsd);

            String[] locations = root.getAttribute("xsi:schemaLocation").split(" +");
            for (int i = 0; i < locations.length; i += 2) {
                String uri = locations[i];
                for (XSD dep : deps) {
                    if (dep.getNamespaceURI().equals(uri)) {
                        locations[i + 1] = dep.getSchemaLocation();
                    }
                }
            }

            StringBuffer joined = new StringBuffer();
            for (String s : locations) {
                joined.append(s).append(" ");
            }
            joined.setLength(joined.length() - 1);
            root.setAttribute("xsi:schemaLocation", joined.toString());
        } else {
            // no schemaLocation attribute, add one for the schema for this config
            root.setAttribute(
                    "xsi:schemaLocation",
                    config.getNamespaceURI() + " " + config.getXSD().getSchemaLocation());
        }

        DOMParser parser = new DOMParser(config, document);

        return parser.parse();
    }

    /**
     * Parses the built document.
     *
     * <p>This method should be called after building the entire document.
     */
    protected Object parse() throws Exception {
        return parse(null);
    }

    /**
     * Encodes an object, element name pair explicitly specifying the type of the root element.
     *
     * @param object The object to encode.
     * @param element The name of the element to encode.
     * @param type The type of the element
     * @return The object encoded.
     */
    protected Document encode(Object object, QName element, QName type) throws Exception {
        Configuration configuration = createConfiguration();

        if (type != null) {
            // set the hint
            configuration.getContext().registerComponentInstance("http://geotools.org/typeDefinition", type);
        }

        XSDSchema schema = configuration.getXSD().getSchema();

        Encoder encoder = new Encoder(configuration, schema);

        // additional namespaces
        for (Map.Entry<String, String> namespace : getNamespaces().entrySet()) {
            String prefix = namespace.getKey();
            String uri = namespace.getValue();

            encoder.getNamespaces().declarePrefix(prefix, uri);
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        encoder.encode(object, element, output);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        return dbf.newDocumentBuilder().parse(new ByteArrayInputStream(output.toByteArray()));
    }

    /**
     * Encodes an object, element name pair.
     *
     * @param object The object to encode.
     * @param element The name of the element to encode.
     * @return The object encoded.
     */
    protected Document encode(Object object, QName element) throws Exception {
        return encode(object, element, null);
    }

    /** Convenience method to dump the contents of the document to stdout. */
    public static void print(Node dom) throws Exception {
        TransformerFactory txFactory = TransformerFactory.newInstance();
        Transformer tx = txFactory.newTransformer();
        tx.setOutputProperty(OutputKeys.INDENT, "yes");

        tx.transform(new DOMSource(dom), new StreamResult(System.out));
    }

    /**
     * Convenience method for obtaining an instance of a binding.
     *
     * @param name The qualified name of the element,attribute,or type the binding "binds" to, the key of the binding in
     *     the container.
     * @return The binding.
     */
    protected Binding binding(QName name) {
        Configuration configuration = createConfiguration();

        // create the context
        MutablePicoContainer context = new DefaultPicoContainer();
        context = configuration.setupContext(context);

        // create the binding container
        Map<QName, Object> bindings = configuration.setupBindings();
        BindingLoader bindingLoader = new BindingLoader(bindings);
        //        MutablePicoContainer container = bindingLoader.getContainer();
        //        container = configuration.setupBindings(container);
        //        bindingLoader.setContainer(container);

        // register cmponents available to bindings at runtime
        context.registerComponentInstance(new BindingFactoryImpl(bindingLoader));

        // binding walker support
        context.registerComponentInstance(new BindingWalkerFactoryImpl(bindingLoader, context));

        // logger
        context.registerComponentInstance(logger);

        // setup the namespace support
        NamespaceSupport namespaces = new NamespaceSupport();
        Map<String, String> mappings = new HashMap<>();

        try {
            for (XSD xsd : configuration.getXSD().getDependencies()) {
                XSDSchema schema = xsd.getSchema();

                mappings.putAll(schema.getQNamePrefixToNamespaceMap());
            }

            mappings.putAll(configuration.getXSD().getSchema().getQNamePrefixToNamespaceMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Map.Entry<String, String> mapping : mappings.entrySet()) {
            String key = mapping.getKey();

            if (key == null) {
                key = "";
            }

            namespaces.declarePrefix(key, mapping.getValue());
        }

        context.registerComponentInstance(namespaces);
        context.registerComponentInstance(new NamespaceSupportWrapper(namespaces));

        XSDSchema result;
        try {
            result = configuration.getXSD().getSchema();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SchemaIndex index = new SchemaIndexImpl(new XSDSchema[] {result});
        context.registerComponentInstance(index);

        context.registerComponentInstance(configuration);

        return bindingLoader.loadBinding(name, context);
    }

    /**
     * Convenience method which parses the specified string into a dom and sets the built document which is to be
     * parsed.
     *
     * @param xml A string of xml
     */
    public void buildDocument(String xml) throws Exception {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);

        document =
                docFactory.newDocumentBuilder().parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    /** Convenience method for finding a node in a document which matches the specified name. */
    protected Element getElementByQName(Document dom, QName name) {
        return getElementByQName(dom.getDocumentElement(), name);
    }

    /** Convenience method for finding a single descendant of a particular node which matches the specified name. */
    protected Element getElementByQName(Element parent, QName name) {
        NodeList nodes = parent.getElementsByTagNameNS(name.getNamespaceURI(), name.getLocalPart());

        if (nodes.getLength() == 0) {
            return null;
        }

        return (Element) nodes.item(0);
    }

    /** Convenience method for finding nodes in a document which matche the specified name. */
    protected NodeList getElementsByQName(Document dom, QName name) {
        return getElementsByQName(dom.getDocumentElement(), name);
    }

    /** Convenience method for finding decendants of a particular node which match the specified name. */
    protected NodeList getElementsByQName(Element parent, QName name) {
        return parent.getElementsByTagNameNS(name.getNamespaceURI(), name.getLocalPart());
    }
}
