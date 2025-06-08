/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.v1_x;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.opengis.wfs.FeatureTypeType;
import org.apache.commons.io.IOUtils;
import org.geotools.data.wfs.internal.WFSOperationType;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.xs.bindings.XSDoubleBinding;
import org.geotools.xs.bindings.XSIntegerBinding;
import org.geotools.xs.bindings.XSStringBinding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This strategy addresses a bug in most MapServer implementations where a filter is required in order for all the
 * features to be returned. So if the Filter is Filter.NONE or Query.ALL then a BBox Filter is constructed that is the
 * entire layer.
 */
public class MapServerWFSStrategy extends StrictWFS_1_x_Strategy {

    private String mapServerVersion = "";

    public MapServerWFSStrategy(Document capabilitiesDoc) {
        super();
        getMapServerVersion(capabilitiesDoc);
    }

    @Override
    public FeatureTypeType translateTypeInfo(FeatureTypeType typeInfo) {
        if ("wfs".equals(typeInfo.getName().getPrefix())
                || "http://www.opengis.net/wfs".equals(typeInfo.getName().getNamespaceURI())) {
            QName newName = new QName(
                    "http://mapserver.gis.umn.edu/mapserver", typeInfo.getName().getLocalPart(), "ms");
            typeInfo.setName(newName);
        }
        return typeInfo;
    }

    @Override
    public Map<QName, Class<?>> getFieldTypeMappings() {
        Map<QName, Class<?>> mappings = new HashMap<>();
        mappings.put(new QName("http://www.w3.org/2001/XMLSchema", "Character"), XSStringBinding.class);
        mappings.put(new QName("http://www.w3.org/2001/XMLSchema", "Integer"), XSIntegerBinding.class);
        mappings.put(new QName("http://www.w3.org/2001/XMLSchema", "Real"), XSDoubleBinding.class);
        return mappings;
    }

    @Override
    public InputStream getPostContents(WFSRequest request) throws IOException {
        InputStream in = super.getPostContents(request);

        if (request.getOperation().compareTo(WFSOperationType.GET_FEATURE) == 0
                && getVersion().compareTo("1.0.0") == 0
                && mapServerVersion != null) {
            try {
                // Pre-5.6.7 versions of MapServer do not support the following gml:Box coordinate
                // format:
                // <gml:coord><gml:X>-59.0</gml:X><gml:Y>-35.0</gml:Y></gml:coord><gml:coord><
                // gml:X>-58.0</gml:X><gml:Y>-34.0</gml:Y></gml:coord>
                // Rewrite the coordinates in the following format:
                // <gml:coordinates cs="," decimal="." ts=" ">-59,-35 -58,-34</gml:coordinates>
                String[] tokens = mapServerVersion.split("\\.");
                if (tokens.length == 3 && versionCompare(mapServerVersion, "5.6.7") < 0) {
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(in, writer, "UTF-8");
                    String pc = writer.toString();

                    boolean reformatted = false;
                    if (pc.contains("gml:Box")
                            && pc.contains("gml:coord")
                            && pc.contains("gml:X")
                            && pc.contains("gml:Y")) {

                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        factory.setNamespaceAware(true);
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        Document doc = builder.parse(new ByteArrayInputStream(pc.getBytes()));

                        NodeList boxes = doc.getElementsByTagName("gml:Box");
                        for (int b = 0; b < boxes.getLength(); b++) {
                            Element box = (Element) boxes.item(b);
                            NodeList coords = box.getElementsByTagName("gml:coord");
                            if (coords != null && coords.getLength() == 2) {
                                Element coord1 = (Element) coords.item(0);
                                Element coord2 = (Element) coords.item(1);
                                if (coord1 != null && coord2 != null) {
                                    Element coordX1 = (Element)
                                            coord1.getElementsByTagName("gml:X").item(0);
                                    Element coordY1 = (Element)
                                            coord1.getElementsByTagName("gml:Y").item(0);
                                    Element coordX2 = (Element)
                                            coord2.getElementsByTagName("gml:X").item(0);
                                    Element coordY2 = (Element)
                                            coord2.getElementsByTagName("gml:Y").item(0);
                                    if (coordX1 != null && coordY1 != null && coordX2 != null && coordY2 != null) {
                                        reformatted = true;
                                        String x1 = coordX1.getTextContent();
                                        String y1 = coordY1.getTextContent();
                                        String x2 = coordX2.getTextContent();
                                        String y2 = coordY2.getTextContent();

                                        box.removeChild(coord1);
                                        box.removeChild(coord2);

                                        Element coordinates = doc.createElement("gml:coordinates");
                                        coordinates.setAttribute("cs", ",");
                                        coordinates.setAttribute("decimal", ".");
                                        coordinates.setAttribute("ts", " ");
                                        coordinates.appendChild(
                                                doc.createTextNode(x1 + "," + y1 + " " + x2 + "," + y2));
                                        box.appendChild(coordinates);
                                    }
                                }
                            }
                        }

                        if (reformatted) {
                            DOMSource domSource = new DOMSource(doc);
                            StringWriter domsw = new StringWriter();
                            StreamResult result = new StreamResult(domsw);
                            TransformerFactory tf = TransformerFactory.newInstance();
                            Transformer transformer = tf.newTransformer();
                            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                            transformer.transform(domSource, result);
                            domsw.flush();
                            pc = domsw.toString();
                        }
                    }
                    in.close();
                    in = new ByteArrayInputStream(pc.getBytes());
                }
            } catch (SAXException | ParserConfigurationException | TransformerException | IOException ex) {
                LOGGER.log(
                        Level.FINE,
                        "Unexpected exception while rewriting 1.0.0 GETFEATURE request with BBOX filter",
                        ex.getMessage());
            }
        }

        return in;
    }

    //
    // Mapserver returns its version in the WFS 1.0.0 getcapabilities response
    //
    void getMapServerVersion(Document capabilitiesDoc) {
        if (getVersion().compareTo("1.0.0") == 0) {
            Element cap = capabilitiesDoc.getDocumentElement();
            NodeList childNodes = cap.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);
                if (child.getNodeType() == Element.COMMENT_NODE) {
                    String nodeValue = child.getNodeValue();
                    if (nodeValue != null && nodeValue.contains("MapServer version")) {
                        String[] tokens = nodeValue.split("\\s");
                        if (tokens.length >= 4) {
                            mapServerVersion = tokens[3];
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Compares two version strings.
     *
     * <p>Use this instead of String.compareTo() for a non-lexicographical comparison that works for version strings.
     * e.g. "1.10".compareTo("1.6").
     *
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2. The result is a positive
     *     integer if str1 is _numerically_ greater than str2. The result is zero if the strings are _numerically_
     *     equal.
     */
    // Code from Stack Overflow:
    // http://stackoverflow.com/questions/6701948/efficient-way-to-compare-version-strings-in-java
    //
    private Integer versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;
        // set index to first non-equal ordinal or length of shortest version string
        while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }
        // compare first non-equal ordinal number
        if (i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }
        // the strings are equal or one string is a substring of the other
        // e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
        else {
            return Integer.signum(vals1.length - vals2.length);
        }
    }

    @Override
    protected String encodePropertyName(String propertyName) {
        return "(" + propertyName + ")";
    }
}
