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
package org.geotools.validation.attributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.geotools.validation.DefaultFeatureValidation;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 * GazetteerNameValidation purpose.
 * 
 * <p>
 * Description of GazetteerNameValidation ...
 * </p>
 * 
 * <p>
 * Capabilities:
 * </p>
 * 
 * <ul>
 * <li>
 * Feature: description
 * </li>
 * </ul>
 * 
 * <p>
 * Example Use:
 * </p>
 * <pre><code>
 * GazetteerNameValidation x = new GazetteerNameValidation(...);
 * </code></pre>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class GazetteerNameValidation extends DefaultFeatureValidation {
    /** used to store the gazetteer url */
    private URL gazetteer;

    /** used to store the Feature Attribute name to test for existance */
    private String attributeName;

    /**
     * GazetteerNameValidation constructor.
     * 
     * <p>
     * Does nothing
     * </p>
     */
    public GazetteerNameValidation() {
        super();

        try {
            gazetteer = new URL(
                    "http://cgdi-dev.geoconnections.org/cgi-bin/prototypes/cgdigaz/cgdigaz.cgi?version=1.0&request=GetPlacenameGeometry&wildcards=false&geomtype=bbox");
        } catch (MalformedURLException e) {
        }
    }

    /**
     * Implementation of validate.
     *
     * @param feature
     * @param type
     * @param results
     *
     *
     * @see org.geotools.validation.FeatureValidation#validate(org.geotools.feature.Feature,
     *      org.geotools.feature.FeatureType,
     *      org.geotools.validation.ValidationResults)
     */
    public boolean validate(SimpleFeature feature, SimpleFeatureType type,
        ValidationResults results) { // throws Exception {

        String place = (String) feature.getAttribute(attributeName);
        URL gazetteerURL = null;

        try {
            gazetteerURL = new URL(gazetteer.toString() + "&placename=" + place);
        } catch (MalformedURLException e) {
            results.error(feature, e.toString());

            return false;
        }

        InputStream gazetteerInputStream = null;

        try {
            HttpURLConnection gazetteerConnection = (HttpURLConnection) gazetteerURL
                .openConnection();

            if (!("OK".equals(gazetteerConnection.getResponseMessage()))) {
                results.error(feature,
                    "An error occured creating the connection to the Gazetteer.");
            }

            gazetteerInputStream = gazetteerConnection.getInputStream();
        } catch (IOException e) {
            results.error(feature, e.toString());

            return false;
        }

        InputStreamReader gazetteerInputStreamReader = new InputStreamReader(gazetteerInputStream);
        BufferedReader gazetteerBufferedReader = new BufferedReader(gazetteerInputStreamReader);

        InputSource gazetteerInputSource = new InputSource(gazetteerBufferedReader);
        DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
        dfactory.setNamespaceAware(true);

        // TODO turn on validation
        dfactory.setValidating(false);
        dfactory.setIgnoringComments(true);
        dfactory.setCoalescing(true);
        dfactory.setIgnoringElementContentWhitespace(true);

        Document serviceDoc = null;

        try {
            serviceDoc = dfactory.newDocumentBuilder().parse(gazetteerInputSource);
        } catch (Exception e) {
            results.error(feature, e.toString());
        }

        Element elem = serviceDoc.getDocumentElement();

        // elem == SimpleFeatureCollection at this point
        elem = getChildElement(elem, "queryInfo");

        if (elem == null) {
            results.error(feature, "Invalid DOM tree returned by gazetteer.");
        }

        // this number is the number of instances found.
        int number = Integer.parseInt(getChildText(elem, "numberOfResults"));

        return number > 0;
    }

    /**
     * Implementation of getPriority.
     *
     *
     * @see org.geotools.validation.Validation#getPriority()
     */
    public int getPriority() {
        return 5;
    }

    /**
     * Access attributeName property.
     *
     * @return Returns the attributeName.
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Set attributeName to attributeName.
     *
     * @param attrName The attributeName to set.
     */
    public void setAttributeName(String attrName) {
        this.attributeName = attrName;
    }

    /**
     * Access gazetteer property.
     *
     * @return Returns the gazetteer.
     */
    public URL getGazetteer() {
        return gazetteer;
    }

    /**
     * Set gazetteer to gazetteer.
     *
     * @param gazetteer The gazetteer to set.
     */
    public void setGazetteer(URL gazetteer) {
        this.gazetteer = gazetteer;
    }

    /**
     * getChildElement purpose.
     * 
     * <p>
     * Used to help with XML manipulations. Returns the first child element of
     * the specified name.
     * </p>
     *
     * @param root The root element to look for children in.
     * @param name The name of the child element to look for.
     *
     * @return The child element found, null if not found.
     *
     * @see getChildElement(Element,String,boolean)
     */
    private static Element getChildElement(Element root, String name) {
        Node child = root.getFirstChild();

        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (name.equals(child.getNodeName())) {
                    return (Element) child;
                }
            }

            child = child.getNextSibling();
        }

        return null;
    }

    /**
     * getChildText purpose.
     * 
     * <p>
     * Used to help with XML manipulations. Returns the first child text value
     * of the specified element name.
     * </p>
     *
     * @param root The root element to look for children in.
     * @param childName The name of the attribute to look for.
     *
     * @return The value if the child was found, the null otherwise.
     */
    private static String getChildText(Element root, String childName) {
        Element elem = getChildElement(root, childName);

        if (elem != null) {
            Node child;
            NodeList childs = elem.getChildNodes();
            int nChilds = childs.getLength();

            for (int i = 0; i < nChilds; i++) {
                child = childs.item(i);

                if (child.getNodeType() == Node.TEXT_NODE) {
                    return child.getNodeValue();
                }
            }
        }

        return null;
    }
}
