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
package org.geotools.gml3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.eclipse.xsd.XSDSchema;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.gml3.bindings.TEST;
import org.geotools.gml3.bindings.TestConfiguration;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class GML3EncodingOnlineTest {

    @Before
    public void setUp() throws Exception {

        Map<String, String> namespaces = new HashMap<>();
        namespaces.put("test", TEST.TestFeature.getNamespaceURI());
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
    }

    boolean isOffline() throws Exception {
        // this test will only run if network is available
        URL url = new URL("http://schemas.opengis.net");

        try {
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(2000);
            conn.getInputStream().read();
        } catch (Exception e) {
            return true;
        }

        return false;
    }

    @Test
    public void testWithConfiguration() throws Exception {
        if (isOffline()) {
            return;
        }

        TestConfiguration configuration = new TestConfiguration();

        // first parse in test data
        Parser parser = new Parser(configuration);
        SimpleFeatureCollection fc =
                (SimpleFeatureCollection)
                        parser.parse(TestConfiguration.class.getResourceAsStream("test.xml"));
        Assert.assertNotNull(fc);

        XSDSchema schema = TEST.getInstance().getSchema();
        Assert.assertNotNull(schema);

        Encoder encoder = new Encoder(configuration, schema);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        encoder.encode(fc, TEST.TestFeatureCollection, output);

        validate(output.toByteArray(), configuration);
    }

    @Test
    public void testWithApplicationSchemaConfiguration() throws Exception {
        if (isOffline()) {
            return;
        }

        // The schema location needs to be a well formed URI/URL, a file path is not sufficient.
        String schemaLocation = TestConfiguration.class.getResource("test.xsd").toString();

        ApplicationSchemaConfiguration configuration =
                new ApplicationSchemaConfiguration(TEST.NAMESPACE, schemaLocation);

        // first parse in test data
        Parser parser = new Parser(configuration);
        SimpleFeatureCollection fc =
                (SimpleFeatureCollection)
                        parser.parse(TestConfiguration.class.getResourceAsStream("test.xml"));
        Assert.assertNotNull(fc);

        XSDSchema schema = TEST.getInstance().getSchema();
        Assert.assertNotNull(schema);

        Encoder encoder = new Encoder(configuration, schema);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        encoder.encode(fc, TEST.TestFeatureCollection, output);

        validate(output.toByteArray(), configuration);
    }

    void validate(byte[] data, Configuration configuration) throws Exception {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Schema s = sf.newSchema(new URI(configuration.getXSD().getSchemaLocation()).toURL());

        Validator v = s.newValidator();

        final ArrayList<SAXParseException> errors = new ArrayList<>();
        DefaultHandler handler =
                new DefaultHandler() {
                    @Override
                    public void error(SAXParseException e) throws SAXException {
                        // System.out.println(e.getMessage());
                        errors.add(e);
                    }

                    @Override
                    public void fatalError(SAXParseException e) throws SAXException {
                        // System.out.println(e.getMessage());
                        errors.add(e);
                    }
                };

        v.setErrorHandler(handler);
        v.validate(new StreamSource(new ByteArrayInputStream(data)));

        Assert.assertTrue(errors.isEmpty());
    }
}
