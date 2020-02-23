/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation.xml;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * ReaderUtils purpose.
 *
 * <p>This class is intended to be used as a library of XML relevant operation for the
 * XMLConfigReader class.
 *
 * <p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id$
 * @see XMLConfigReader
 */
class ReaderUtils {
    /** Used internally to create log information to detect errors. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ReaderUtils.class);

    /**
     * ReaderUtils constructor.
     *
     * <p>Static class, this should never be called.
     */
    private ReaderUtils() {}

    /**
     * loadConfig purpose.
     *
     * <p>Parses the specified file into a DOM tree.
     *
     * @param configFile The file to parse int a DOM tree.
     * @return the resulting DOM tree
     */
    public static Element loadConfig(Reader configFile)
            throws IOException, ParserConfigurationException, SAXException {
        LOGGER.finest("loading configuration file " + configFile);

        InputSource in = new InputSource(configFile);
        DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
        dfactory.setNamespaceAware(true);

        // TODO turn on validation
        dfactory.setValidating(false);
        dfactory.setIgnoringComments(true);
        dfactory.setCoalescing(true);
        dfactory.setIgnoringElementContentWhitespace(true);

        Document serviceDoc = dfactory.newDocumentBuilder().parse(in);
        Element configElem = serviceDoc.getDocumentElement();

        return configElem;
    }

    /**
     * initFile purpose.
     *
     * <p>Checks to ensure the file is valid. Returns the file passed in to allow this to wrap file
     * creations.
     *
     * @param f A file Handle to test.
     * @param isDir true when the File passed in is expected to be a directory, false when the
     *     handle is expected to be a file.
     * @return the File handle passed in
     * @throws IOException When the file does not exist or is not the type specified.
     */
    public static File initFile(File f, boolean isDir) throws IOException {
        if (!f.exists()) {
            throw new IOException("Path specified does not have a valid file.\n" + f + "\n\n");
        }

        if (isDir && !f.isDirectory()) {
            throw new IOException("Path specified does not have a valid file.\n" + f + "\n\n");
        }

        if (!isDir && !f.isFile()) {
            throw new IOException("Path specified does not have a valid file.\n" + f + "\n\n");
        }

        LOGGER.fine("File is valid: " + f);

        return f;
    }

    /**
     * getChildElement purpose.
     *
     * <p>Used to help with XML manipulations. Returns the first child element of the specified
     * name. An exception occurs when the node is required and not found.
     *
     * @param root The root element to look for children in.
     * @param name The name of the child element to look for.
     * @param mandatory true when an exception should be thrown if the child element does not exist.
     * @return The child element found, null if not found.
     * @throws SAXException When a child element is required and not found.
     */
    public static Element getChildElement(Element root, String name, boolean mandatory)
            throws SAXException {
        Node child = root.getFirstChild();

        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (name.equals(child.getNodeName())) {
                    return (Element) child;
                }
            }

            child = child.getNextSibling();
        }

        if (mandatory && (child == null)) {
            throw new SAXException(
                    root.getNodeName() + " does not contains a child element named " + name);
        }

        return null;
    }

    /**
     * getChildElement purpose.
     *
     * <p>Used to help with XML manipulations. Returns the first child element of the specified
     * name.
     *
     * @param root The root element to look for children in.
     * @param name The name of the child element to look for.
     * @return The child element found, null if not found.
     * @see getChildElement(Element,String,boolean)
     */
    public static Element getChildElement(Element root, String name) {
        try {
            return getChildElement(root, name, false);
        } catch (SAXException e) {
            // will never be here.
            return null;
        }
    }

    /**
     * getIntAttribute purpose.
     *
     * <p>Used to help with XML manipulations. Returns the first child integer attribute of the
     * specified name. An exception occurs when the node is required and not found.
     *
     * @param elem The root element to look for children in.
     * @param attName The name of the attribute to look for.
     * @param mandatory true when an exception should be thrown if the attribute element does not
     *     exist.
     * @param defaultValue a default value to return incase the attribute was not found. mutually
     *     exclusive with the ConfigurationException thrown.
     * @return The int value if the attribute was found, the default otherwise.
     * @throws SAXException When a attribute element is required and not found.
     */
    public static int getIntAttribute(
            Element elem, String attName, boolean mandatory, int defaultValue) throws SAXException {
        String attValue = getAttribute(elem, attName, mandatory);

        if (!mandatory && (attValue == null)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(attValue);
        } catch (Exception ex) {
            if (mandatory) {
                throw new SAXException(
                        attName
                                + " attribute of element "
                                + elem.getNodeName()
                                + " must be an integer, but it's '"
                                + attValue
                                + "'");
            } else {
                return defaultValue;
            }
        }
    }

    /**
     * getIntAttribute purpose.
     *
     * <p>Used to help with XML manipulations. Returns the first child integer attribute of the
     * specified name. An exception occurs when the node is required and not found.
     *
     * @param elem The root element to look for children in.
     * @param attName The name of the attribute to look for.
     * @param mandatory true when an exception should be thrown if the attribute element does not
     *     exist.
     * @return The value if the attribute was found, the null otherwise.
     * @throws SAXException When a child attribute is required and not found.
     */
    public static String getAttribute(Element elem, String attName, boolean mandatory)
            throws SAXException {
        Attr att = elem.getAttributeNode(attName);

        String value = null;

        if (att != null) {
            value = att.getValue();
        }

        if (mandatory) {
            if (att == null) {
                throw new SAXException(
                        "element "
                                + elem.getNodeName()
                                + " does not contains an attribute named "
                                + attName);
            } else if ("".equals(value)) {
                throw new SAXException(
                        "attribute " + attName + "in element " + elem.getNodeName() + " is empty");
            }
        }

        return value;
    }

    /**
     * getBooleanAttribute purpose.
     *
     * <p>Used to help with XML manipulations. Returns the first child integer attribute of the
     * specified name. An exception occurs when the node is required and not found.
     *
     * @param elem The root element to look for children in.
     * @param attName The name of the attribute to look for.
     * @param mandatory true when an exception should be thrown if the attribute element does not
     *     exist.
     * @return The value if the attribute was found, the false otherwise.
     * @throws SAXException When a child attribute is required and not found.
     */
    public static boolean getBooleanAttribute(Element elem, String attName, boolean mandatory)
            throws SAXException {
        String value = getAttribute(elem, attName, mandatory);

        return Boolean.valueOf(value).booleanValue();
    }

    /**
     * getChildText purpose.
     *
     * <p>Used to help with XML manipulations. Returns the first child text value of the specified
     * element name.
     *
     * @param root The root element to look for children in.
     * @param childName The name of the attribute to look for.
     * @return The value if the child was found, the null otherwise.
     */
    public static String getChildText(Element root, String childName) {
        try {
            return getChildText(root, childName, false);
        } catch (SAXException ex) {
            return null;
        }
    }

    /**
     * getChildText purpose.
     *
     * <p>Used to help with XML manipulations. Returns the first child text value of the specified
     * element name. An exception occurs when the node is required and not found.
     *
     * @param root The root element to look for children in.
     * @param childName The name of the attribute to look for.
     * @param mandatory true when an exception should be thrown if the text does not exist.
     * @return The value if the child was found, the null otherwise.
     * @throws SAXException When a child attribute is required and not found.
     */
    public static String getChildText(Element root, String childName, boolean mandatory)
            throws SAXException {
        Element elem = getChildElement(root, childName, mandatory);

        if (elem != null) {
            return getElementText(elem, mandatory);
        } else {
            if (mandatory) {
                String msg = "Mandatory child " + childName + "not found in " + " element: " + root;

                throw new SAXException(msg);
            }

            return null;
        }
    }

    /**
     * getChildText purpose.
     *
     * <p>Used to help with XML manipulations. Returns the text value of the specified element name.
     *
     * @param elem The root element to look for children in.
     * @return The value if the text was found, the null otherwise.
     */
    public static String getElementText(Element elem) {
        try {
            return getElementText(elem, false);
        } catch (SAXException ex) {
            return null;
        }
    }

    /**
     * getChildText purpose.
     *
     * <p>Used to help with XML manipulations. Returns the text value of the specified element name.
     * An exception occurs when the node is required and not found.
     *
     * @param elem The root element to look for children in.
     * @param mandatory true when an exception should be thrown if the text does not exist.
     * @return The value if the text was found, the null otherwise.
     * @throws SAXException When text is required and not found.
     */
    public static String getElementText(Element elem, boolean mandatory) throws SAXException {
        String value = null;

        LOGGER.finest("getting element text for " + elem);

        if (elem != null) {
            Node child;

            NodeList childs = elem.getChildNodes();

            int nChilds = childs.getLength();

            for (int i = 0; i < nChilds; i++) {
                child = childs.item(i);

                if (child.getNodeType() == Node.TEXT_NODE) {
                    value = child.getNodeValue();

                    if (mandatory && "".equals(value.trim())) {
                        throw new SAXException(elem.getNodeName() + " text is empty");
                    }

                    break;
                }
            }

            if (mandatory && (value == null)) {
                throw new SAXException(elem.getNodeName() + " element does not contains text");
            }
        } else {
            throw new SAXException("Argument element can't be null");
        }

        return value;
    }

    /**
     * getKeyWords purpose.
     *
     * <p>Used to help with XML manipulations. Returns a list of keywords that were found.
     *
     * @param keywordsElem The root element to look for children in.
     * @return The list of keywords that were found.
     */
    public static String[] getKeyWords(Element keywordsElem) {
        NodeList klist = keywordsElem.getElementsByTagName("keyword");
        int kCount = klist.getLength();
        List keywords = new ArrayList(kCount);
        String kword;
        Element kelem;

        for (int i = 0; i < kCount; i++) {
            kelem = (Element) klist.item(i);
            kword = getElementText(kelem);

            if (kword != null) {
                keywords.add(kword);
            }
        }

        Object[] s = (Object[]) keywords.toArray();

        if (s == null) {
            return new String[0];
        }

        String[] ss = new String[s.length];

        for (int i = 0; i < ss.length; i++) ss[i] = s[i].toString();

        return ss;
    }

    /**
     * getFirstChildElement purpose.
     *
     * <p>Used to help with XML manipulations. Returns the element which represents the first child.
     *
     * @param root The root element to look for children in.
     * @return The element if a child was found, the null otherwise.
     */
    public static Element getFirstChildElement(Element root) {
        Node child = root.getFirstChild();

        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return (Element) child;
            }

            child = child.getNextSibling();
        }

        return null;
    }

    /**
     * getDoubleAttribute purpose.
     *
     * <p>Used to help with XML manipulations. Returns the first child integer attribute of the
     * specified name. An exception occurs when the node is required and not found.
     *
     * @param elem The root element to look for children in.
     * @param attName The name of the attribute to look for.
     * @param mandatory true when an exception should be thrown if the attribute element does not
     *     exist.
     * @return The double value if the attribute was found, the NaN otherwise.
     * @throws SAXException When a attribute element is required and not found.
     */
    public static double getDoubleAttribute(Element elem, String attName, boolean mandatory)
            throws SAXException {
        String value = getAttribute(elem, attName, mandatory);

        double d = Double.NaN;

        if (value != null) {
            try {
                d = Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                throw new SAXException(
                        "Illegal attribute value for "
                                + attName
                                + " in element "
                                + elem.getNodeName()
                                + ". Expected double, but was "
                                + value);
            }
        }

        return d;
    }
}
