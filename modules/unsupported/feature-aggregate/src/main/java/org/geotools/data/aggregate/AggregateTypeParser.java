package org.geotools.data.aggregate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.geotools.feature.NameImpl;
import org.opengis.feature.type.Name;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class AggregateTypeParser {

    protected static Validator VALIDATOR;
    static {

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        URL url = AggregateTypeParser.class.getResource("/aggregatetypes_1.0.xsd");
        Schema schema = null;
        try {
            schema = factory.newSchema(url);
        } catch (SAXException e) {
            // should not happen
            throw new RuntimeException(e);
        }
        VALIDATOR = schema.newValidator();
    }

    List<AggregateTypeConfiguration> parseConfigurations(InputStream is) throws IOException {
        try {
            Document doc = null;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setNamespaceAware(true);
            factory.setIgnoringElementContentWhitespace(true);
    
            try {
                DocumentBuilder db = factory.newDocumentBuilder();
                doc = db.parse(is);
                VALIDATOR.validate(new DOMSource(doc));
            } catch (Exception e) {
                throw new IOException(e.getMessage());
            }
            Element root = doc.getDocumentElement();
            String version = root.getAttribute("version");
            if (version != null && !"1.0".equals(version)) {
                throw new IOException("Unrecognized version " + version
                        + ", the only valid value now is 1.0");
            }
    
            return parseConfigurations(root);
        } finally {
            if(is != null) {
                is.close();
            }
        }
    }

    List<AggregateTypeConfiguration> parseConfigurations(Node root) {
        List<AggregateTypeConfiguration> result = new ArrayList<AggregateTypeConfiguration>();
        NodeList configNodes = root.getChildNodes();
        for (int i = 0; i < configNodes.getLength(); i++) {
            // build the config object
            Node configNode = configNodes.item(i);
            if(!"AggregateType".equals(configNode.getLocalName())) {
                continue;
            }
            
            String name = getAttributeValue(configNode, "name");
            AggregateTypeConfiguration config = new AggregateTypeConfiguration(name);
            
            // populate with the sources
            NodeList sourceNodes = configNode.getChildNodes();
            for (int j = 0; j < sourceNodes.getLength(); j++) {
                Node sourceNode = sourceNodes.item(j);
                if(!"Source".equals(sourceNode.getLocalName())) {
                    continue;
                }
                String store = getAttributeValue(sourceNode, "store");
                String type = getAttributeValue(sourceNode, "type");
                config.addStore(buildName(store), type);
            }
            
            result.add(config);
        }

        return result;
    }
    
    /**
     * Builds a qualified name from a name containing the ":" separator, otherwise the given name
     * will be used as the local part
     * 
     * @param name
     * @return
     */
    Name buildName(String name) {
        int idx = name.indexOf(":");
        if (idx == -1) {
            return new NameImpl(name);
        } else {
            String ns = name.substring(0, idx);
            String local = name.substring(idx + 1);
            return new NameImpl(ns, local);
        }
    }

    private String getAttributeValue(Node node, String attribute) {
        NamedNodeMap attributes = node.getAttributes();
        if(attributes == null) {
            return null;
        }
        Node namedItem = attributes.getNamedItem(attribute);
        if(namedItem == null) {
            return null;
        } else {
            return namedItem.getTextContent();
        }
    }
}
