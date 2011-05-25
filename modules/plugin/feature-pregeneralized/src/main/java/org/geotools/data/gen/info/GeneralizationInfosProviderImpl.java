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

package org.geotools.data.gen.info;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Christian Mueller
 * 
 * The default implementation for GeneralizationInfosProvider, reading the info from an XML file.
 * 
 * The xml schema file is "/geninfos_1.0.xsd"
 * 
 * 
 *
 *
 * @source $URL$
 */
public class GeneralizationInfosProviderImpl implements GeneralizationInfosProvider {

    protected final static String GENERALIZATION_INFOS_TAG = "GeneralizationInfos";

    protected final static String GENERALIZATION_INFO_TAG = "GeneralizationInfo";

    protected final static String GENERALIZATION_TAG = "Generalization";

    protected final static String FEATURE_NAME_ATTR = "featureName";

    protected final static String BASE_FEATURE_NAME_ATTR = "baseFeatureName";

    protected final static String GEOM_PROPERTY_NAME_ATTR = "geomPropertyName";

    protected final static String DISTANCE_ATTR = "distance";

    protected final static String DATASOURCE_NAME_ATTR = "dataSourceName";

    protected final static String DATASOURCE_NAMESPACE_NAME_ATTR = "dataSourceNameSpace";

    protected final static String VERSION_ATTR = "version";

    protected static Validator VALIDATOR;
    static {

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        URL url = GeneralizationInfosProviderImpl.class.getResource("/geninfos_1.0.xsd");
        Schema schema = null;
        try {
            schema = factory.newSchema(url);
        } catch (SAXException e) {
            // should not happen
            throw new RuntimeException(e);
        }
        VALIDATOR = schema.newValidator();
    }

    public GeneralizationInfos getGeneralizationInfos(Object source) throws IOException {

        URL inputURL = deriveURLFromSourceObject(source);
        GeneralizationInfos infos = parseXML(inputURL);
        infos.validate();
        return infos;
    }

    protected URL deriveURLFromSourceObject(Object source) throws IOException {
        if (source == null)
            throw new IOException("Cannot read from null");

        if (source instanceof URL)
            return (URL) source;

        if (source instanceof String) {
            URL url = null;
            File f = new File((String) source);
            if (f.exists())
                url = f.toURI().toURL();
            else
                url = new URL((String) source);

            url = new URL(URLDecoder.decode(url.toExternalForm(), "UTF8"));
            return url;
        }

        if (source instanceof URI)
            return ((URI) source).toURL();
        return null;
    }

    protected GeneralizationInfos parseXML(URL url) throws IOException {

        Document doc = null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringComments(true);
        factory.setNamespaceAware(true);
        factory.setIgnoringElementContentWhitespace(true);

        try {
            DocumentBuilder db = factory.newDocumentBuilder();
            doc = db.parse(url.openStream());
            VALIDATOR.validate(new DOMSource(doc));
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        NodeList nl = doc.getElementsByTagName(GENERALIZATION_INFOS_TAG);
        GeneralizationInfos gInfos = new GeneralizationInfos();

        Node gInfosNode = nl.item(0);
        checkVersion(gInfosNode);

        NamedNodeMap attrMap = gInfosNode.getAttributes();
        if (attrMap.getNamedItem(DATASOURCE_NAME_ATTR) != null)
            gInfos.setDataSourceName(attrMap.getNamedItem(DATASOURCE_NAME_ATTR).getTextContent());
        if (attrMap.getNamedItem(DATASOURCE_NAMESPACE_NAME_ATTR) != null)
            gInfos.setDataSourceNameSpace(attrMap.getNamedItem(DATASOURCE_NAMESPACE_NAME_ATTR)
                    .getTextContent());
        parseGeneralizationInfoNodes(gInfosNode, gInfos);
        return gInfos;

    }

    protected void parseGeneralizationInfoNodes(Node parentNode, GeneralizationInfos gInfos) {

        NodeList nl = parentNode.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node generalisationInfoNode = nl.item(i);
            if (GENERALIZATION_INFO_TAG.equals(generalisationInfoNode.getNodeName()) == false)
                continue;

            NamedNodeMap attrMap = generalisationInfoNode.getAttributes();

            String baseFeatureName = attrMap.getNamedItem(BASE_FEATURE_NAME_ATTR).getTextContent();
            String featureName = attrMap.getNamedItem(FEATURE_NAME_ATTR).getTextContent();
            String geomPropertyName = null;
            if (attrMap.getNamedItem(GEOM_PROPERTY_NAME_ATTR) != null)
                geomPropertyName = attrMap.getNamedItem(GEOM_PROPERTY_NAME_ATTR).getTextContent();
            GeneralizationInfo gi = new GeneralizationInfo(baseFeatureName, featureName,
                    geomPropertyName, gInfos);

            if (attrMap.getNamedItem(DATASOURCE_NAME_ATTR) != null)
                gi.setDataSourceName(attrMap.getNamedItem(DATASOURCE_NAME_ATTR).getTextContent());

            if (attrMap.getNamedItem(DATASOURCE_NAMESPACE_NAME_ATTR) != null)
                gi.setDataSourceNameSpace(attrMap.getNamedItem(DATASOURCE_NAMESPACE_NAME_ATTR)
                        .getTextContent());

            parseDistanceInfoNodes(generalisationInfoNode, gi);
            gInfos.addGeneralizationInfo(gi);
        }

    }

    protected void parseDistanceInfoNodes(Node parentNode, GeneralizationInfo gInfo) {
        NodeList nl = parentNode.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node distanceInfoNode = nl.item(i);
            if (GENERALIZATION_TAG.equals(distanceInfoNode.getNodeName()) == false)
                continue;

            NamedNodeMap attrMap = distanceInfoNode.getAttributes();

            Double distance = new Double(attrMap.getNamedItem(DISTANCE_ATTR).getTextContent());
            String featureName = attrMap.getNamedItem(FEATURE_NAME_ATTR).getTextContent();
            String geomPropertyName = null;
            if (attrMap.getNamedItem(GEOM_PROPERTY_NAME_ATTR) != null)
                geomPropertyName = attrMap.getNamedItem(GEOM_PROPERTY_NAME_ATTR).getTextContent();
            Generalization di = new Generalization(distance, featureName, geomPropertyName, gInfo);

            if (attrMap.getNamedItem(DATASOURCE_NAME_ATTR) != null)
                di.setDataSourceName(attrMap.getNamedItem(DATASOURCE_NAME_ATTR).getTextContent());

            if (attrMap.getNamedItem(DATASOURCE_NAMESPACE_NAME_ATTR) != null)
                di.setDataSourceNameSpace(attrMap.getNamedItem(DATASOURCE_NAMESPACE_NAME_ATTR)
                        .getTextContent());

            gInfo.getGeneralizations().add(di);
        }

    }

    protected void checkVersion(Node gInfos) throws IOException {
        String version = gInfos.getAttributes().getNamedItem(VERSION_ATTR).getTextContent();
        if ("1.0".equals(version) == false) {
            throw new IOException(gInfos.getLocalName() + " " + version + " is not supported");
        }

    }

}
