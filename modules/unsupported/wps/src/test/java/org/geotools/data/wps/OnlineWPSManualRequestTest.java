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

import junit.framework.TestCase;
import net.opengis.ows11.ExceptionReportType;
import net.opengis.wps10.DataType;
import net.opengis.wps10.ExecuteResponseType;
import net.opengis.wps10.InputDescriptionType;
import net.opengis.wps10.LiteralDataType;
import net.opengis.wps10.OutputDataType;
import net.opengis.wps10.ProcessBriefType;
import net.opengis.wps10.ProcessDescriptionType;
import net.opengis.wps10.ProcessDescriptionsType;
import net.opengis.wps10.ProcessOfferingsType;
import net.opengis.wps10.WPSCapabilitiesType;

import org.eclipse.emf.common.util.EList;
import org.geotools.data.wps.request.DescribeProcessRequest;
import org.geotools.data.wps.request.ExecuteProcessRequest;
import org.geotools.data.wps.response.DescribeProcessResponse;
import org.geotools.data.wps.response.ExecuteProcessResponse;
import org.geotools.ows.ServiceException;
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
 * Test making requests by manually building up requests using the utility methods.
 * 
 * @author GDavis
 * 
 * 
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/wps/src/test/java/org/geotools
 *         /data/wps/OnlineWPSManualRequestTest.java $
 */
public class OnlineWPSManualRequestTest extends OnlineTestCase {

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

    public void testGetCaps() throws ServiceException, IOException {

        // don't run the test if the server is not up
        if (fixture == null )
            return;

        WPSCapabilitiesType capabilities = wps.getCapabilities();
        assertNotNull("capabilities shouldn't be null", capabilities);

        ProcessOfferingsType processOfferings = capabilities
                .getProcessOfferings();
        assertNotNull("process offerings shouldn't be null", processOfferings);
        EList processes = processOfferings.getProcess();
        for (int i = 0; i < processes.size(); i++) {
            ProcessBriefType process = (ProcessBriefType) processes.get(i);
            // System.out.println(process.getTitle());
            assertNotNull("process [" + process + " shouldn't be null",
                    process.getTitle());
        }

    }

    public void testDescribeProcess() throws ServiceException, IOException {

        // don't run the test if the server is not up
        if (fixture == null )
            return;

        WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and describe it
        ProcessOfferingsType processOfferings = capabilities
                .getProcessOfferings();
        EList processes = processOfferings.getProcess();
        ProcessBriefType process = (ProcessBriefType) processes.get(0);

        DescribeProcessRequest request = wps.createDescribeProcessRequest();
        request.setIdentifier(process.getIdentifier().getValue());
        // System.out.println(request.getFinalURL());
        DescribeProcessResponse response = wps.issueRequest(request);
        // System.out.println(response);
        assertNotNull(response);
        assertNotNull(response.getProcessDesc());
    }

    /**
     * run multiple buffer tests with various geometry types
     * 
     * @throws ParseException
     * @throws IOException
     * @throws ServiceException
     */
    public void testExecuteProcessBufferLocal() throws ParseException,
            ServiceException, IOException {

        // don't run the test if the server is not up or we aren't doing local tests
        if (fixture == null )
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
            throws ServiceException, IOException, ParseException {

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
        // http://geoserver.itc.nl:8080/wps100/WebProcessingService?REQUEST=DescribeProcess&IDENTIFIER=org.n52.wps.server.algorithm.collapse.SimplePolygon2PointCollapse&VERSION=1.0.0&SERVICE=WPS
        DescribeProcessRequest descRequest = wps.createDescribeProcessRequest();
        descRequest.setIdentifier(processIden);
        DescribeProcessResponse descResponse = wps.issueRequest(descRequest);

        // based on the describeprocess, setup the execute
        ProcessDescriptionsType processDesc = descResponse.getProcessDesc();
        ExecuteProcessRequest exeRequest = wps.createExecuteProcessRequest();
        exeRequest.setIdentifier(processIden);

        // set input data
        setLocalInputDataBufferPoly(exeRequest, processDesc, geom1);

        // send the request
        ExecuteProcessResponse response = wps.issueRequest(exeRequest);

        // response should not be null and no exception should occur.
        assertNotNull(response);
        ExecuteResponseType executeResponse = response.getExecuteResponse();
        assertNotNull(executeResponse);
        ExceptionReportType exceptionResponse = response.getExceptionResponse();
        assertNull(exceptionResponse);

        // check that the result is expected
        Geometry expected = geom1.buffer(350);
        EList outputs = executeResponse.getProcessOutputs().getOutput();
        OutputDataType output = (OutputDataType) outputs.get(0);
        Geometry result = (Geometry) output.getData().getComplexData()
                .getData().get(0);
        // System.out.println(expected);
        // System.out.println(result);
        // assertTrue(expected.equals(result));

    }

    private void setLocalInputDataBufferPoly(ExecuteProcessRequest exeRequest,
            ProcessDescriptionsType processDesc, Geometry geom1)
            throws ParseException {

        // this process takes 2 input, a geometry and a buffer amount.
        ProcessDescriptionType pdt = (ProcessDescriptionType) processDesc
                .getProcessDescription().get(0);
        InputDescriptionType idt = (InputDescriptionType) pdt.getDataInputs()
                .getInput().get(0);

        // create input buffer
        int bufferAmnt = 350;

        // create and set the input on the exe request
        if (idt.getIdentifier().getValue().equalsIgnoreCase("buffer")) {
            // set buffer input
            DataType input = WPSUtils.createInputDataType(bufferAmnt, idt);
            List<DataType> list = new ArrayList<DataType>();
            list.add(input);
            exeRequest.addInput(idt.getIdentifier().getValue(), list);
            // set geom input
            idt = (InputDescriptionType) pdt.getDataInputs().getInput().get(1);
            DataType input2 = WPSUtils.createInputDataType(geom1, idt);
            List<DataType> list2 = new ArrayList<DataType>();
            list2.add(input2);
            exeRequest.addInput(idt.getIdentifier().getValue(), list2);
        } else {
            // set geom input
            DataType input2 = WPSUtils.createInputDataType(geom1, idt);
            List<DataType> list2 = new ArrayList<DataType>();
            list2.add(input2);
            exeRequest.addInput(idt.getIdentifier().getValue(), list2);
            // set buffer input
            idt = (InputDescriptionType) pdt.getDataInputs().getInput().get(1);
            DataType input = WPSUtils.createInputDataType(bufferAmnt, idt);
            List<DataType> list = new ArrayList<DataType>();
            list.add(input);
            exeRequest.addInput(idt.getIdentifier().getValue(), list);
        }
    }

    public void testExecuteProcessBuffer52N() throws ServiceException,
            IOException, ParseException {

        // don't run the test if the server is not up or if we are doing local tests
        if (fixture == null )
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
        // http://geoserver.itc.nl:8080/wps100/WebProcessingService?REQUEST=DescribeProcess&IDENTIFIER=org.n52.wps.server.algorithm.collapse.SimplePolygon2PointCollapse&VERSION=1.0.0&SERVICE=WPS
        DescribeProcessRequest descRequest = wps.createDescribeProcessRequest();
        descRequest.setIdentifier(processIden);
        DescribeProcessResponse descResponse = wps.issueRequest(descRequest);

        // based on the describeprocess, setup the execute
        ProcessDescriptionsType processDesc = descResponse.getProcessDesc();
        ExecuteProcessRequest exeRequest = wps.createExecuteProcessRequest();
        exeRequest.setIdentifier(processIden);

        // set input data
        set52NInputData(exeRequest, processDesc);

        // send the request
        ExecuteProcessResponse response = wps.issueRequest(exeRequest);

        // response should not be null and no exception should occur.
        assertNotNull(response);
        assertNotNull(response.getExecuteResponse());
        ExceptionReportType exceptionResponse = response.getExceptionResponse();
        assertNull(exceptionResponse);

    }

    private void set52NInputData(ExecuteProcessRequest exeRequest,
            ProcessDescriptionsType processDesc) throws ParseException {

        // this process takes 1 input, a building polygon to collapse.
        ProcessDescriptionType pdt = (ProcessDescriptionType) processDesc
                .getProcessDescription().get(0);
        InputDescriptionType idt = (InputDescriptionType) pdt.getDataInputs()
                .getInput().get(0);

        // create a polygon for the input
        WKTReader reader = new WKTReader(new GeometryFactory());
        Geometry geom1 = (Polygon) reader
                .read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");

        // create and set the input on the exe request
        DataType input = WPSUtils.createInputDataType(geom1, idt);
        List<DataType> list = new ArrayList<DataType>();
        list.add(input);
        exeRequest.addInput(idt.getIdentifier().getValue(), list);
    }

    /**
     * Do some more local process tests, such as union
     * 
     * @throws ServiceException
     * @throws IOException
     * @throws ParseException
     */
    public void testExecuteLocalUnion() throws ServiceException, IOException,
            ParseException {

        // don't run the test if the server is not up
        if (fixture == null )
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
        ExecuteProcessRequest exeRequest = wps.createExecuteProcessRequest();
        exeRequest.setIdentifier(processIdenLocal);

        setLocalInputDataUnion(exeRequest, processDesc);

        // send the request
        ExecuteProcessResponse response = wps.issueRequest(exeRequest);

        // response should not be null and no exception should occur.
        assertNotNull(response);
        assertNotNull(response.getExecuteResponse());
        ExceptionReportType exceptionResponse = response.getExceptionResponse();
        assertNull(exceptionResponse);

    }

    private void setLocalInputDataUnion(ExecuteProcessRequest exeRequest,
            ProcessDescriptionsType processDesc) throws ParseException {

        // this process takes 2+ inputs, all geometries to union together.
        ProcessDescriptionType pdt = (ProcessDescriptionType) processDesc
                .getProcessDescription().get(0);
        InputDescriptionType idt = (InputDescriptionType) pdt.getDataInputs()
                .getInput().get(0);

        // create polygons for the input
        WKTReader reader = new WKTReader(new GeometryFactory());
        Geometry geom1 = (Polygon) reader
                .read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
        Geometry geom2 = (Polygon) reader
                .read("POLYGON((20 30, 30 0, 20 20, 80 20, 20 30))");
        Geometry geom3 = (Polygon) reader
                .read("POLYGON((177 10, 30 88, 40 70, 46 20, 177 10))");
        Geometry geom4 = (Polygon) reader
                .read("POLYGON((5 10, 5 0, 13 10, 5 20, 5 10))");

        // create and set the input on the exe request
        if (idt.getIdentifier().getValue().equalsIgnoreCase("geom")) {
            // set geom inputs
            List<DataType> list = new ArrayList<DataType>();
            DataType input = WPSUtils.createInputDataType(geom1, idt);
            DataType input2 = WPSUtils.createInputDataType(geom2, idt);
            DataType input3 = WPSUtils.createInputDataType(geom3, idt);
            DataType input4 = WPSUtils.createInputDataType(geom4, idt);
            list.add(input);
            list.add(input2);
            list.add(input3);
            list.add(input4);
            exeRequest.addInput(idt.getIdentifier().getValue(), list);
        }

    }

    /**
     * Do some more local process tests, such as double addtion
     * 
     * @throws ServiceException
     * @throws IOException
     * @throws ParseException
     */
    public void testExecuteLocalAdd() throws ServiceException, IOException,
            ParseException {

        // don't run the test if the server is not up
        if (fixture == null ) return;

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
        ExecuteProcessRequest exeRequest = wps.createExecuteProcessRequest();
        exeRequest.setIdentifier(processIdenLocal);

        setLocalInputDataAdd(exeRequest, processDesc);

        // send the request
        ExecuteProcessResponse response = wps.issueRequest(exeRequest);

        // response should not be null and no exception should occur.
        assertNotNull(response);
        ExecuteResponseType executeResponse = response.getExecuteResponse();
        assertNotNull(executeResponse);
        ExceptionReportType exceptionResponse = response.getExceptionResponse();
        assertNull(exceptionResponse);

        // check result correctness
        EList outputs = executeResponse.getProcessOutputs().getOutput();
        assertTrue(!outputs.isEmpty());
        OutputDataType output = (OutputDataType) outputs.get(0);
        LiteralDataType literalData = output.getData().getLiteralData();
        String value = literalData.getValue();
        Double result = new Double(value);
        Double expected = 77.84 + 40039.229;
        assertEquals(result, expected);

    }

    private void setLocalInputDataAdd(ExecuteProcessRequest exeRequest,
            ProcessDescriptionsType processDesc) throws ParseException {

        // this process takes 2 inputs, two double to add together.
        ProcessDescriptionType pdt = (ProcessDescriptionType) processDesc
                .getProcessDescription().get(0);
        InputDescriptionType idt = (InputDescriptionType) pdt.getDataInputs()
                .getInput().get(0);

        // create doubles for the input
        Double d1 = 77.84;
        Double d2 = 40039.229;

        // create and set the input on the exe request
        List<DataType> list = new ArrayList<DataType>();
        DataType input = WPSUtils.createInputDataType(d1, idt);
        list.add(input);
        exeRequest.addInput(idt.getIdentifier().getValue(), list);

        InputDescriptionType idt2 = (InputDescriptionType) pdt.getDataInputs()
                .getInput().get(1);
        List<DataType> list2 = new ArrayList<DataType>();
        DataType input2 = WPSUtils.createInputDataType(d2, idt2);
        list2.add(input2);
        exeRequest.addInput(idt2.getIdentifier().getValue(), list2);

    }
}
