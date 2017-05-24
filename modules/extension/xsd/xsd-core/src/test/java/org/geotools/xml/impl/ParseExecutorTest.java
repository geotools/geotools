/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import org.geotools.ml.MLConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.geotools.xml.SchemaLocator;
import org.geotools.xml.XSD;
import org.junit.Test;
import org.picocontainer.MutablePicoContainer;
import org.xml.sax.SAXException;

/**
 * @author ian
 *
 */
public class ParseExecutorTest {
    String data = "<ComplexData mimeType=\"text/csv\"><![CDATA[State,inc1980,inc1990,inc1995,inc2000,inc2003,inc2006,inc2009,inc2012\n"
            + "Alabama,7465,14899,19683,23521,26338,30894,33096,35625\n"
            + "Alaska,13007,20887,25798,29642,33568,38138,42603,46778\n"
            + "Arizona,8854,16262,20634,24988,26838,31936,32935,35979\n"
            + "Arkansas,7113,13779,18546,21995,24289,28473,31946,34723\n"
            + "California,11021,20656,24496,32149,33749,39626,42325,44980\n"
            + "Colorado,10143,18818,24865,32434,34283,39491,41344,45135\n"
            + "Connecticut,11532,25426,31947,40702,43173,50762,54397,58908\n"
            + "Delaware,10059,19719,25391,31012,32810,39131,39817,41940\n"
            + "District of Columbia,12251,24643,33045,38838,48342,57746,66000,74710\n"
            + "Florida,9246,18785,23512,27764,30446,36720,37780,40344\n]]></ComplexData>";

    /**
     * Test method for {@link org.geotools.xml.impl.ParseExecutor#preParse(org.geotools.xml.InstanceComponent)}.
     * @throws ParserConfigurationException 
     * @throws SAXException 
     * @throws IOException 
     */
    @Test
    public void testPreParse() throws IOException, SAXException, ParserConfigurationException {

        Configuration configuration = new MLConfiguration();
        Parser parser = new Parser(configuration);
        parser.setValidating(true);

        HashMap<String,String> parsed;

        InputStream reader = new ByteArrayInputStream(data.getBytes());
        parsed = (HashMap<String, String>) parser.parse(reader );
        for(Entry<String, String> e:parsed.entrySet()) {
            System.out.println(e);
            if(e.getKey()!=null && e.getKey().equals("mimeType")) {
                assertEquals("text/csv", e.getValue());
            }else {
                assertTrue(e.getValue().contains("\n"));
            }
        }
    }

}
