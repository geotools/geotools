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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import junit.framework.TestCase;
import net.opengis.wps10.ProcessBriefType;
import net.opengis.wps10.ProcessDescriptionType;
import net.opengis.wps10.ProcessDescriptionsType;
import net.opengis.wps10.ProcessOfferingsType;
import net.opengis.wps10.WPSCapabilitiesType;

import org.eclipse.emf.common.util.EList;
import org.geotools.data.wps.request.DescribeProcessRequest;
import org.geotools.data.wps.response.DescribeProcessResponse;
import org.geotools.ows.ServiceException;
import org.geotools.process.Process;
import org.geotools.process.ProcessException;
import org.geotools.test.OnlineTestCase;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Test requests using the WPSFactory and WPSProcess objects
 * 
 * @author GDavis
 * 
 * 
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/wps/src/test/java/org/geotools
 *         /data/wps/OnlineWPSFactoryTest.java $
 */
public class OnlineWPSFactoryTest extends OnlineTestCase {

    private WebProcessingService wps;

    private URL url;

    private String processIden;

    /**
     * The wps.geoserver fixture consisting of service and processId.
     */
    @Override
    protected String getFixtureId() {
        return "wps";
    }

    protected Properties createExampleFixture() {
        Properties example = new Properties();
        example.put("service", "http://localhost:8080/geoserver/ows?service=wps&version=1.0.0&request=GetCapabilities");
        example.put("processId","buffer");
        
        return example;
    }
    public void connect() throws ServiceException, IOException {
        if (fixture == null) {
            return;
        }

        // local server
        String serviceProperty = fixture.getProperty("service");
        if (serviceProperty == null) {
            throw new ServiceException(
                    "Service URL not provided by test fixture");
        }
        url = new URL(serviceProperty);
        processIden = fixture.getProperty("processId");

        wps = new WebProcessingService(url);
    }

    /**
     * run multiple buffer tests with various geometry types
     * 
     * @throws ParseException
     * @throws IOException
     * @throws ServiceException
     */
    public void testExecuteProcessBufferLocal() throws ParseException,
            ServiceException, IOException, ProcessException {
        if (fixture == null)
            return;

        // create the geometries to use for input
        WKTReader reader = new WKTReader(new GeometryFactory());
        Geometry geom1 = (Polygon) reader
                .read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
        Geometry geom2 = (Point) reader.read("POINT (160 200)");
        Geometry geom3 = (LineString) reader
                .read("LINESTRING (100 240, 220 140, 380 240, 480 220)");
        Geometry geom4 = (MultiLineString) reader
                .read("MULTILINESTRING ((140 280, 180 180, 400 260), (340 120, 160 100, 80 200))");
        Geometry geom5 = (MultiPoint) reader
                .read("MULTIPOINT (180 180, 260 280, 340 200)");
        Geometry geom6 = (MultiPolygon) reader
                .read("MULTIPOLYGON (((160 320, 120 140, 360 140, 320 340, 160 320), (440 260, 580 140, 580 240, 440 260)))");

        // run the local buffer execute test for each geom input
        runExecuteProcessBufferLocal(geom1);
        runExecuteProcessBufferLocal(geom2);
        runExecuteProcessBufferLocal(geom3);
        runExecuteProcessBufferLocal(geom4);
        runExecuteProcessBufferLocal(geom5);
        runExecuteProcessBufferLocal(geom6);
    }

    private void runExecuteProcessBufferLocal(Geometry geom1)
            throws ServiceException, IOException, ParseException,
            ProcessException {
        if (fixture == null)
            return;

        WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and execute it
        ProcessOfferingsType processOfferings = capabilities
                .getProcessOfferings();
        EList processes = processOfferings.getProcess();
        // ProcessBriefType process = (ProcessBriefType) processes.get(0);

        // does the server contain the specific process I want
        boolean found = false;
        Iterator iterator = processes.iterator();
        while (iterator.hasNext()) {
            ProcessBriefType process = (ProcessBriefType) iterator.next();
            if (process.getIdentifier().getValue()
                    .equalsIgnoreCase(processIden)) {
                found = true;
                break;
            }
        }

        // exit test if my process doesn't exist on server
        if (!found) {
            return;
        }

        // do a full describeprocess on my process
        DescribeProcessRequest descRequest = wps.createDescribeProcessRequest();
        descRequest.setIdentifier(processIden);
        DescribeProcessResponse descResponse = wps.issueRequest(descRequest);

        // based on the describeprocess, setup the execute
        ProcessDescriptionsType processDesc = descResponse.getProcessDesc();
        ProcessDescriptionType pdt = (ProcessDescriptionType) processDesc
                .getProcessDescription().get(0);
        WPSFactory wpsfactory = new WPSFactory(pdt, this.url);
        Process process = wpsfactory.create();

        // setup the inputs
        Map<String, Object> map = new TreeMap<String, Object>();
        map.put("buffer", 350);
        map.put("geom1", geom1);

        // execute/send-request for the process
        Map<String, Object> results = process.execute(map, null);

        // check that the result is expected
        assertNotNull(results);
        Geometry expected = geom1.buffer(350);
        Geometry result = (Geometry) results.get("result");
        assertNotNull(result);
        // System.out.println(expected);
        // System.out.println(result);
        // assertTrue(expected.equals(result));

    }

    /**
     * Do some more local process tests, such as union
     * 
     * @throws ServiceException
     * @throws IOException
     * @throws ParseException
     */
    public void testExecuteLocalUnion() throws ServiceException, IOException,
            ParseException, ProcessException {
        if (fixture == null)
            return;

        String processIdenLocal = "Union";

        WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and execute it
        ProcessOfferingsType processOfferings = capabilities
                .getProcessOfferings();
        EList processes = processOfferings.getProcess();
        // ProcessBriefType process = (ProcessBriefType) processes.get(0);

        // does the server contain the specific process I want
        boolean found = false;
        Iterator iterator = processes.iterator();
        while (iterator.hasNext()) {
            ProcessBriefType process = (ProcessBriefType) iterator.next();
            if (process.getIdentifier().getValue()
                    .equalsIgnoreCase(processIdenLocal)) {
                found = true;
                break;
            }
        }

        // exit test if my process doesn't exist on server
        if (!found) {
            return;
        }

        // do a full describeprocess on my process
        DescribeProcessRequest descRequest = wps.createDescribeProcessRequest();
        descRequest.setIdentifier(processIdenLocal);
        DescribeProcessResponse descResponse = wps.issueRequest(descRequest);

        // based on the describeprocess, setup the execute
        ProcessDescriptionsType processDesc = descResponse.getProcessDesc();
        ProcessDescriptionType pdt = (ProcessDescriptionType) processDesc
                .getProcessDescription().get(0);
        WPSFactory wpsfactory = new WPSFactory(pdt, this.url);
        Process process = wpsfactory.create();

        // setup the inputs
        Map<String, Object> map = new TreeMap<String, Object>();
        WKTReader reader = new WKTReader(new GeometryFactory());
        List<Geometry> list = new ArrayList<Geometry>();
        Geometry geom1 = (Polygon) reader
                .read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
        Geometry geom2 = (Polygon) reader
                .read("POLYGON((20 30, 30 0, 20 20, 80 20, 20 30))");
        Geometry geom3 = (Polygon) reader
                .read("POLYGON((177 10, 30 88, 40 70, 46 20, 177 10))");
        Geometry geom4 = (Polygon) reader
                .read("POLYGON((5 10, 5 0, 13 10, 5 20, 5 10))");
        list.add(geom1);
        list.add(geom2);
        list.add(geom3);
        list.add(geom4);
        map.put("geom", list);

        // execute/send-request for the process
        Map<String, Object> results = process.execute(map, null);

        // check that the result is expected
        assertNotNull(results);
        // Geometry expected = geom1.union(geom2);
        // expected = expected.union(geom3);
        // expected = expected.union(geom4);
        Geometry result = (Geometry) results.get("result");
        assertNotNull(result);
        // System.out.println(expected);
        // System.out.println(result);

    }

    /**
     * Do some more local union test that should return an exception
     * 
     * @throws ServiceException
     * @throws IOException
     * @throws ParseException
     */
    public void testBADExecuteLocalUnion() throws ServiceException,
            IOException, ParseException, ProcessException {
        if (fixture == null)
            return;

        String processIdenLocal = "Union";

        WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and execute it
        ProcessOfferingsType processOfferings = capabilities
                .getProcessOfferings();
        EList processes = processOfferings.getProcess();
        // ProcessBriefType process = (ProcessBriefType) processes.get(0);

        // does the server contain the specific process I want
        boolean found = false;
        Iterator iterator = processes.iterator();
        while (iterator.hasNext()) {
            ProcessBriefType process = (ProcessBriefType) iterator.next();
            if (process.getIdentifier().getValue()
                    .equalsIgnoreCase(processIdenLocal)) {
                found = true;
                break;
            }
        }

        // exit test if my process doesn't exist on server
        if (!found) {
            return;
        }

        // do a full describeprocess on my process
        DescribeProcessRequest descRequest = wps.createDescribeProcessRequest();
        descRequest.setIdentifier(processIdenLocal);
        DescribeProcessResponse descResponse = wps.issueRequest(descRequest);

        // based on the describeprocess, setup the execute
        ProcessDescriptionsType processDesc = descResponse.getProcessDesc();
        ProcessDescriptionType pdt = (ProcessDescriptionType) processDesc
                .getProcessDescription().get(0);
        WPSFactory wpsfactory = new WPSFactory(pdt, this.url);
        Process process = wpsfactory.create();

        // setup the inputs as empty (which should return an exception)
        Map<String, Object> map = new TreeMap<String, Object>();

        // execute/send-request for the process
        Map<String, Object> results = process.execute(map, null);

        // check that the result is expected (null)
        assertNull(results);

    }

    /**
     * Do some more local process tests, such as double addition
     * 
     * @throws ServiceException
     * @throws IOException
     * @throws ParseException
     */
    public void testExecuteLocalAdd() throws ServiceException, IOException,
            ParseException, ProcessException {
        if (fixture == null)
            return;

        String processIdenLocal = "DoubleAddition";

        WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and execute it
        ProcessOfferingsType processOfferings = capabilities
                .getProcessOfferings();
        EList processes = processOfferings.getProcess();
        // ProcessBriefType process = (ProcessBriefType) processes.get(0);

        // does the server contain the specific process I want
        boolean found = false;
        Iterator iterator = processes.iterator();
        while (iterator.hasNext()) {
            ProcessBriefType process = (ProcessBriefType) iterator.next();
            if (process.getIdentifier().getValue()
                    .equalsIgnoreCase(processIdenLocal)) {
                found = true;
                break;
            }
        }

        // exit test if my process doesn't exist on server
        if (!found) {
            return;
        }

        // do a full describeprocess on my process
        DescribeProcessRequest descRequest = wps.createDescribeProcessRequest();
        descRequest.setIdentifier(processIdenLocal);
        DescribeProcessResponse descResponse = wps.issueRequest(descRequest);

        // based on the describeprocess, setup the execute
        ProcessDescriptionsType processDesc = descResponse.getProcessDesc();
        ProcessDescriptionType pdt = (ProcessDescriptionType) processDesc
                .getProcessDescription().get(0);
        WPSFactory wpsfactory = new WPSFactory(pdt, this.url);
        Process process = wpsfactory.create();

        // setup the inputs
        Map<String, Object> map = new TreeMap<String, Object>();
        Double d1 = 77.84;
        Double d2 = 40039.229;
        map.put("input_a", d1);
        map.put("input_b", d2);

        // execute/send-request for the process
        Map<String, Object> results = process.execute(map, null);

        // check that the result is expected
        assertNotNull(results);
        Double result = (Double) results.get("result");
        // Double result = new Double(value);
        Double expected = 77.84 + 40039.229;
        // System.out.println(expected);
        // System.out.println(result);
        assertEquals(result, expected);

    }

}
