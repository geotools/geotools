/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.SQLXML;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.geotools.factory.Hints;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * ConverterFactory for handling XML W3C DOM Document conversions.
 * <p>
 * Supported conversions:
 * <ul>
 *      <li> java.sql.SQLXML to Node
 *      <li> String to Document
 *      <li> String to DocumentFragment
 *      <li> Node to String
 * 
 * </ul>
 * </p>
 *
 * @author Torben Barsballe (Boundless)
 *
 * @source $URL$
 */
public class XMLDocumentConverterFactory implements ConverterFactory {

    public Converter createConverter(Class source, Class target, Hints hints) {
        //To XML
        if (source.equals(SQLXML.class)) {
            if (Node.class.isAssignableFrom(target)) {
                return new Converter() {
                    @Override
                    public <T> T convert(Object source, Class<T> target)
                            throws Exception {
                        SQLXML sqlxml = (SQLXML)source;
                        DOMSource domSource = sqlxml.getSource(DOMSource.class);
                        return (T) domSource.getNode();
                    }
                };
            }
        } else if (source.equals(String.class)) {
            if (target.equals(Document.class)) {
                return new StringToDocumentConverter();
            } else if (target.equals(DocumentFragment.class)) {
                return new StringToDocumentFragmentConverter();
            }
        //From XML
        } else if (Node.class.isAssignableFrom(source)) {
            if (target.equals(String.class)) {
                return new NodeToStringConverter();
            }
        }
        return null;
    }
    
    public static class StringToDocumentConverter implements Converter {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            String string = (String)source;
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return (T)builder.parse(new InputSource(new StringReader(string)));
        }
    }
    
    public static class StringToDocumentFragmentConverter implements Converter {
        StringToDocumentConverter delegate = new StringToDocumentConverter();
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
          //Wrap the fragment in a top-level tag
            String string = "<fragment>"+source+"</fragment>";
            Document d = (Document)delegate.convert(string, Document.class);
            
            return (T)stripRootNode(d);
        }
    }
    
    public static class NodeToStringConverter implements Converter {
        @Override
        public <T> T convert(Object source, Class<T> target) throws Exception {
            Node node = (Node)source;
            
            DOMSource domSource = new DOMSource(node);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            if (source instanceof DocumentFragment) {
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            }
            transformer.transform(domSource, result);
            return (T)writer.toString();
        }
    }
    
    public static DocumentFragment stripRootNode(Document d) {
        Node node = d.getDocumentElement();
        
        node = d.importNode(node, true);
        DocumentFragment fragment = d.createDocumentFragment();
        NodeList children = node.getChildNodes();
        
        //Copy the children of the top-level node to the fragment
        while (children.getLength() > 0) {
            fragment.appendChild(children.item(0));
        }
        return fragment;
    }
}
