/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.IOUtils;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xsd.Parser;
import org.geotools.xsd.StreamingParser;
import org.w3c.dom.Document;

/**
 * GML3 Parsing examples.
 *
 * @author Justin Deoliveira, OpenGeo
 */
public class GMLParsing {

    public static void main(String[] args) throws Exception {
        // parseGML3();
        // streamParseGML3();
        schemaParseGML3();
    }

    /** Parses GML3 without specifying a schema location. */
    public static void parseGML3() throws Exception {
        InputStream in = GMLParsing.class.getResourceAsStream("states.xml");
        GMLConfiguration gml = new GMLConfiguration();
        Parser parser = new Parser(gml);
        parser.setStrict(false);

        FeatureCollection features = (FeatureCollection) parser.parse(in);
        FeatureIterator i = features.features();

        int nfeatures = 0;
        while (i.hasNext()) {
            SimpleFeature f = (SimpleFeature) i.next();
            System.out.println(f.getID());
            nfeatures++;
        }

        System.out.println("Number of features: " + nfeatures);
    }

    /**
     * Parses GML3 without specifying a schema location, and illusrates the use of the streaming
     * parser.
     */
    public static void streamParseGML3() throws Exception {
        InputStream in = GMLParsing.class.getResourceAsStream("states.xml");
        GMLConfiguration gml = new GMLConfiguration();
        StreamingParser parser = new StreamingParser(gml, in, SimpleFeature.class);

        int nfeatures = 0;
        SimpleFeature f = null;
        while ((f = (SimpleFeature) parser.parse()) != null) {
            nfeatures++;
            System.out.println(f.getID());
        }

        System.out.println("Number of features: " + nfeatures);
    }

    /**
     * Parses GML3 by specifying the schema location.
     *
     * <p>This example first transforms the original file states.xml, and sets its schemaLocation to
     * the states.xsd file.
     */
    public static void schemaParseGML3() throws Exception {
        File xml = setSchemaLocation();
        InputStream in = new FileInputStream(xml);

        GMLConfiguration gml = new GMLConfiguration();
        Parser parser = new Parser(gml);
        parser.setStrict(false);

        FeatureCollection features = (FeatureCollection) parser.parse(in);
        FeatureIterator i = features.features();

        int nfeatures = 0;
        while (i.hasNext()) {
            SimpleFeature f = (SimpleFeature) i.next();
            System.out.println(f.getID());
            nfeatures++;
        }

        System.out.println("Number of features: " + nfeatures);
    }

    static File setSchemaLocation() throws Exception {
        File xsd = File.createTempFile("states", "xsd");
        IOUtils.copy(GMLParsing.class.getResourceAsStream("states.xsd"), new FileOutputStream(xsd));

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document d = db.parse(GMLParsing.class.getResourceAsStream("states.xml"));
        d.getDocumentElement()
                .setAttribute(
                        "xsi:schemaLocation",
                        "http://www.openplans.org/topp " + xsd.getCanonicalPath());

        File xml = File.createTempFile("states", "xml");
        TransformerFactory.newInstance()
                .newTransformer()
                .transform(new DOMSource(d), new StreamResult(xml));
        return xml;
    }
}
