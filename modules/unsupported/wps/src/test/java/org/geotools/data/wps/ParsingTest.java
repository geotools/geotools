/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import javax.xml.parsers.ParserConfigurationException;
import net.opengis.wps10.ExecuteResponseType;
import net.opengis.wps10.OutputDataType;
import net.opengis.wps10.ProcessDescriptionsType;
import net.opengis.wps10.UOMsType;
import net.opengis.wps10.WPSCapabilitiesType;
import net.opengis.wps10.impl.Wps10FactoryImpl;
import org.geotools.TestData;
import org.geotools.wps.WPSConfiguration;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.EMFUtils;
import org.geotools.xsd.Parser;
import org.junit.Test;
import org.xml.sax.SAXException;
import si.uom.SI;

public class ParsingTest {

    private static final boolean DISABLE =
            "true".equalsIgnoreCase(System.getProperty("disableTest", "true"));

    private static final boolean ONLINE = false; // to do check -o maven option?

    /**
     * We want to test our ability to parse a DesscribeProcess document.
     *
     * <p>The OGC provide a reference document here:
     *
     * <ul>
     *   <li>http://schemas.opengis.net/wps/1.0.0/examples/40_wpsDescribeProcess_response.xml <br>
     *       referenceProcessDescriptions.xml
     * </ul>
     */
    @Test
    public void testDescribeProcessParsing() throws Exception {

        if (DISABLE) {
            return;
        }

        Object object;
        BufferedReader in = null;
        try {
            Configuration config = new WPSConfiguration();

            URL url;
            if (ONLINE) {
                url =
                        new URL(
                                "http://schemas.opengis.net/wps/1.0.0/examples/40_wpsDescribeProcess_response.xml");
            } else {
                url = TestData.url(this, "referenceProcessDescriptions.xml");
            }

            Parser parser = new Parser(config);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            object = parser.parse(in);

            ProcessDescriptionsType processDesc = (ProcessDescriptionsType) object;
            assertNotNull(processDesc);
        } finally {
            in.close();
        }
    }

    @Test
    public void testDegree3CapabilitiesParsing() throws Exception {
        if (DISABLE) {
            return;
        }

        Object object;
        BufferedReader in = null;
        try {
            Configuration config = new WPSConfiguration();

            URL url;
            url = TestData.url(this, "deegree3Capabilities.xml");

            Parser parser = new Parser(config);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            object = parser.parse(in);

            assertNotNull("parsed", object);

            WPSCapabilitiesType capabiliites = (WPSCapabilitiesType) object;
            assertEquals("1.0.0", capabiliites.getVersion());

        } finally {
            in.close();
        }
    }

    @Test
    public void testGeoServer3CapabilitiesParsing() throws Exception {
        if (DISABLE) {
            return;
        }

        Object object;
        BufferedReader in = null;
        try {
            Configuration config = new WPSConfiguration();

            URL url;
            url = TestData.url(this, "geoserverCapabilities.xml");

            Parser parser = new Parser(config);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            object = parser.parse(in);

            assertNotNull("parsed", object);

            WPSCapabilitiesType capabiliites = (WPSCapabilitiesType) object;
            assertEquals("1.0.0", capabiliites.getVersion());

        } finally {
            in.close();
        }
    }

    @Test
    public void testDegree3DescribeProcessParsing() throws Exception {
        if (DISABLE) {
            return;
        }

        Object object;
        BufferedReader in = null;
        try {
            Configuration config = new WPSConfiguration();

            URL url;
            url = TestData.url(this, "deegree3ProcessDescriptions.xml");

            Parser parser = new Parser(config);
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            object = parser.parse(in);

            ProcessDescriptionsType processDesc = (ProcessDescriptionsType) object;
            assertNotNull(processDesc);
        } finally {
            in.close();
        }
    }

    @Test
    public void testExeResponseLiteralDataType()
            throws IOException, SAXException, ParserConfigurationException {
        if (DISABLE) {
            return;
        }

        File file = TestData.file(this, "LiteralDataTypeTestFile.xml");
        BufferedReader in = new BufferedReader(new FileReader(file));
        Configuration config = new WPSConfiguration();
        Parser parser = new Parser(config);

        Object object = parser.parse(in);

        // try casting the response
        ExecuteResponseType exeResponse = null;
        if (object instanceof ExecuteResponseType) {
            exeResponse = (ExecuteResponseType) object;
        }

        // try to get the output datatype
        OutputDataType odt = (OutputDataType) exeResponse.getProcessOutputs().getOutput().get(0);
        String dataType = odt.getData().getLiteralData().getDataType();

        assertNotNull(dataType);
    }

    @Test
    public void testUOMsList() {
        if (DISABLE) {
            return;
        }

        UOMsType uoMsType = Wps10FactoryImpl.eINSTANCE.createUOMsType();
        Unit<Length> newValue = SI.METRE;
        EMFUtils.add(uoMsType, "UOM", newValue);

        // uoMsType.eSet(Wps10Package.UO_MS_TYPE__UOM, newValue);
    }
}
