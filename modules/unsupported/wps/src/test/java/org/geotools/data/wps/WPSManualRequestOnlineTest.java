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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.ExceptionReportType;
import net.opengis.ows11.ExceptionType;
import net.opengis.ows11.Ows11Factory;
import net.opengis.wps10.ComplexDataType;
import net.opengis.wps10.DataInputsType1;
import net.opengis.wps10.DataType;
import net.opengis.wps10.DocumentOutputDefinitionType;
import net.opengis.wps10.ExecuteResponseType;
import net.opengis.wps10.ExecuteType;
import net.opengis.wps10.InputDescriptionType;
import net.opengis.wps10.InputReferenceType;
import net.opengis.wps10.InputType;
import net.opengis.wps10.LiteralDataType;
import net.opengis.wps10.MethodType;
import net.opengis.wps10.OutputDataType;
import net.opengis.wps10.OutputDefinitionType;
import net.opengis.wps10.ProcessBriefType;
import net.opengis.wps10.ProcessDescriptionType;
import net.opengis.wps10.ProcessDescriptionsType;
import net.opengis.wps10.ProcessOfferingsType;
import net.opengis.wps10.ResponseDocumentType;
import net.opengis.wps10.ResponseFormType;
import net.opengis.wps10.WPSCapabilitiesType;
import net.opengis.wps10.Wps10Factory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.wps.request.DescribeProcessRequest;
import org.geotools.data.wps.request.ExecuteProcessRequest;
import org.geotools.data.wps.response.DescribeProcessResponse;
import org.geotools.data.wps.response.ExecuteProcessResponse;
import org.geotools.ows.ServiceException;
import org.geotools.test.OnlineTestCase;
import org.geotools.wps.WPS;
import org.geotools.wps.WPSConfiguration;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.EncoderDelegate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.xml.sax.ContentHandler;
import org.xml.sax.ext.LexicalHandler;

/**
 * Test making requests by manually building up requests using the utility methods.
 *
 * @author GDavis
 */
public class WPSManualRequestOnlineTest extends OnlineTestCase {

    private WebProcessingService wps;

    private URL url;

    private String processIden;

    private ResponseFormType response;

    /** The wps.geoserver fixture consisting of service and processId. */
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
            throw new ServiceException("Service URL not provided by test fixture");
        }
        url = new URL(serviceProperty);
        processIden = fixture.getProperty("processId");

        wps = new WebProcessingService(url);
    }

    public void testGetCaps() throws ServiceException, IOException {

        // don't run the test if the server is not up
        if (fixture == null) {
            return;
        }

        WPSCapabilitiesType capabilities = wps.getCapabilities();
        assertNotNull("capabilities shouldn't be null", capabilities);

        ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
        assertNotNull("process offerings shouldn't be null", processOfferings);

        EList processes = processOfferings.getProcess();
        for (int i = 0; i < processes.size(); i++) {
            ProcessBriefType process = (ProcessBriefType) processes.get(i);
            // System.out.println(process.getTitle());
            assertNotNull("process [" + process + " shouldn't be null", process.getTitle());
        }
    }

    public void testDescribeProcess() throws ServiceException, IOException {

        // don't run the test if the server is not up
        if (fixture == null) {
            return;
        }

        WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and describe it
        ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
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

    public void testAddReferenceTypeInput() throws ServiceException, IOException {

        // don't run the test if the server is not up
        if (fixture == null) {
            return;
        }

        ExecuteProcessRequest request = wps.createExecuteProcessRequest();

        // reference to the File
        EObject kmzFileReference = Wps10Factory.eINSTANCE.createInputReferenceType();
        ((InputReferenceType) kmzFileReference).setMimeType("application/vnd.google-earth.kmz");
        ((InputReferenceType) kmzFileReference).setMethod(MethodType.GET_LITERAL);
        ((InputReferenceType) kmzFileReference).setHref("file:///testref");

        request.addInput("input Gliders KMZ file", Arrays.asList(kmzFileReference));

        ByteArrayOutputStream out = null;
        InputStream in = null;
        BufferedReader reader = null;
        try {
            out = new ByteArrayOutputStream();
            request.performPostOutput(out);

            in = new ByteArrayInputStream(out.toByteArray());
            reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder postText = new StringBuilder();

            char[] cbuf = new char[1024];
            int charsRead;
            while ((charsRead = reader.read(cbuf)) != -1) {
                postText = postText.append(cbuf, 0, charsRead);
            }

            assertTrue(postText.toString().contains("wps:Reference"));
        } catch (Exception e) {
            assertFalse(true);
        } finally {
            if (reader != null) {
                reader.close();
            }

            if (out != null) {
                out.close();
            }

            if (in != null) {
                in.close();
            }
        }
    }

    /** run multiple buffer tests with various geometry types */
    public void testExecuteProcessBufferLocal()
            throws ParseException, ServiceException, IOException {

        // don't run the test if the server is not up or we aren't doing local tests
        if (fixture == null) {
            return;
        }

        // create the geometries to use for input
        WKTReader reader = new WKTReader(new GeometryFactory());
        Geometry geom1 = reader.read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
        Geometry geom2 = reader.read("POINT (160 200)");
        Geometry geom3 = reader.read("LINESTRING (100 240, 220 140, 380 240, 480 220)");
        Geometry geom4 =
                reader.read(
                        "MULTILINESTRING ((140 280, 180 180, 400 260), (340 120, 160 100, 80 200))");
        Geometry geom5 = reader.read("MULTIPOINT (180 180, 260 280, 340 200)");
        Geometry geom6 =
                reader.read(
                        "MULTIPOLYGON (((160 320, 120 140, 360 140, 320 340, 160 320), (440 260, 580 140, 580 240, 440 260)))");

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
        ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
        EList processes = processOfferings.getProcess();
        // ProcessBriefType process = (ProcessBriefType) processes.get(0);

        // does the server contain the specific process I want
        boolean found = false;
        Iterator iterator = processes.iterator();
        while (iterator.hasNext()) {
            ProcessBriefType process = (ProcessBriefType) iterator.next();
            if (process.getIdentifier().getValue().equalsIgnoreCase(processIden)) {
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
        Geometry result = (Geometry) output.getData().getComplexData().getData().get(0);
        // System.out.println(expected);
        // System.out.println(result);
        // assertTrue(expected.equals(result));

    }

    private void setLocalInputDataBufferPoly(
            ExecuteProcessRequest exeRequest, ProcessDescriptionsType processDesc, Geometry geom1)
            throws ParseException {

        // this process takes 2 input, a geometry and a buffer amount.
        ProcessDescriptionType pdt =
                (ProcessDescriptionType) processDesc.getProcessDescription().get(0);
        InputDescriptionType idt = (InputDescriptionType) pdt.getDataInputs().getInput().get(0);

        // create input buffer
        int bufferAmnt = 350;

        // create and set the input on the exe request
        if (idt.getIdentifier().getValue().equalsIgnoreCase("buffer")) {
            // set buffer input
            DataType input = WPSUtils.createInputDataType(bufferAmnt, idt);
            List<EObject> list = new ArrayList<EObject>();
            list.add(input);
            exeRequest.addInput(idt.getIdentifier().getValue(), list);
            // set geom input
            idt = (InputDescriptionType) pdt.getDataInputs().getInput().get(1);

            DataType input2 = WPSUtils.createInputDataType(geom1, idt);
            List<EObject> list2 = new ArrayList<EObject>();
            list2.add(input2);
            exeRequest.addInput(idt.getIdentifier().getValue(), list2);
        } else {
            // set geom input
            DataType input2 = WPSUtils.createInputDataType(geom1, idt);
            List<EObject> list2 = new ArrayList<EObject>();
            list2.add(input2);
            exeRequest.addInput(idt.getIdentifier().getValue(), list2);
            // set buffer input
            idt = (InputDescriptionType) pdt.getDataInputs().getInput().get(1);

            DataType input = WPSUtils.createInputDataType(bufferAmnt, idt);
            List<EObject> list = new ArrayList<EObject>();
            list.add(input);
            exeRequest.addInput(idt.getIdentifier().getValue(), list);
        }
    }

    public void testExecuteProcessBuffer52N() throws ServiceException, IOException, ParseException {

        // don't run the test if the server is not up or if we are doing local tests
        if (fixture == null) {
            return;
        }

        WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and execute it
        ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
        EList processes = processOfferings.getProcess();
        // ProcessBriefType process = (ProcessBriefType) processes.get(0);

        // does the server contain the specific process I want
        boolean found = false;
        Iterator iterator = processes.iterator();
        while (iterator.hasNext()) {
            ProcessBriefType process = (ProcessBriefType) iterator.next();
            if (process.getIdentifier().getValue().equalsIgnoreCase(processIden)) {
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

    private void set52NInputData(
            ExecuteProcessRequest exeRequest, ProcessDescriptionsType processDesc)
            throws ParseException {

        // this process takes 1 input, a building polygon to collapse.
        ProcessDescriptionType pdt =
                (ProcessDescriptionType) processDesc.getProcessDescription().get(0);
        InputDescriptionType idt = (InputDescriptionType) pdt.getDataInputs().getInput().get(0);

        // create a polygon for the input
        WKTReader reader = new WKTReader(new GeometryFactory());
        Geometry geom1 = reader.read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");

        // create and set the input on the exe request
        DataType input = WPSUtils.createInputDataType(geom1, idt);
        List<EObject> list = new ArrayList<EObject>();
        list.add(input);
        exeRequest.addInput(idt.getIdentifier().getValue(), list);
    }

    /** Do some more local process tests, such as union */
    public void testExecuteLocalUnion() throws ServiceException, IOException, ParseException {

        // don't run the test if the server is not up
        if (fixture == null) {
            return;
        }

        String processIdenLocal = "JTS:union";

        WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and execute it
        ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
        EList processes = processOfferings.getProcess();
        // ProcessBriefType process = (ProcessBriefType) processes.get(0);

        // does the server contain the specific process I want
        boolean found = false;
        Iterator iterator = processes.iterator();
        while (iterator.hasNext()) {
            ProcessBriefType process = (ProcessBriefType) iterator.next();
            if (process.getIdentifier().getValue().equalsIgnoreCase(processIdenLocal)) {
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

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        exeRequest.performPostOutput(bos);
        // System.out.println(bos.toString());

        // send the request
        ExecuteProcessResponse response = wps.issueRequest(exeRequest);

        // response should not be null and no exception should occur.
        assertNotNull(response);
        assertNotNull(response.getExecuteResponse());

        ExceptionReportType exceptionResponse = response.getExceptionResponse();
        assertNull(exceptionResponse);
    }

    private void setLocalInputDataUnion(
            ExecuteProcessRequest exeRequest, ProcessDescriptionsType processDesc)
            throws ParseException {

        // this process takes 2+ inputs, all geometries to union together.
        ProcessDescriptionType pdt =
                (ProcessDescriptionType) processDesc.getProcessDescription().get(0);
        InputDescriptionType idt = (InputDescriptionType) pdt.getDataInputs().getInput().get(0);

        // create polygons for the input
        String geom1 = "POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))";
        String geom2 = "POLYGON((177 10, 30 88, 40 70, 46 20, 177 10))";
        String geom3 = "POLYGON((5 10, 5 0, 13 10, 5 20, 5 10))";

        // create and set the input on the exe request
        if (idt.getIdentifier().getValue().equalsIgnoreCase("geom")) {
            // set geom inputs
            List<EObject> list = new ArrayList<EObject>();
            DataType input =
                    WPSUtils.createInputDataType(
                            new CDATAEncoder(geom1),
                            WPSUtils.INPUTTYPE_COMPLEXDATA,
                            null,
                            "application/wkt");
            DataType input2 =
                    WPSUtils.createInputDataType(
                            new CDATAEncoder(geom2),
                            WPSUtils.INPUTTYPE_COMPLEXDATA,
                            null,
                            "application/wkt");
            DataType input3 =
                    WPSUtils.createInputDataType(
                            new CDATAEncoder(geom3),
                            WPSUtils.INPUTTYPE_COMPLEXDATA,
                            null,
                            "application/wkt");
            list.add(input);
            list.add(input2);
            list.add(input3);
            exeRequest.addInput(idt.getIdentifier().getValue(), list);
        }
    }

    /** Do some more local process tests, such as double addtion */
    public void testExecuteLocalAdd() throws ServiceException, IOException, ParseException {

        // don't run the test if the server is not up
        if (fixture == null) {
            return;
        }

        String processIdenLocal = "DoubleAddition";

        WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and execute it
        ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
        EList processes = processOfferings.getProcess();
        // ProcessBriefType process = (ProcessBriefType) processes.get(0);

        // does the server contain the specific process I want
        boolean found = false;
        Iterator iterator = processes.iterator();
        while (iterator.hasNext()) {
            ProcessBriefType process = (ProcessBriefType) iterator.next();
            if (process.getIdentifier().getValue().equalsIgnoreCase(processIdenLocal)) {
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
        Double result = Double.valueOf(value);
        Double expected = 77.84 + 40039.229;
        assertEquals(result, expected);
    }

    private void setLocalInputDataAdd(
            ExecuteProcessRequest exeRequest, ProcessDescriptionsType processDesc)
            throws ParseException {

        // this process takes 2 inputs, two double to add together.
        ProcessDescriptionType pdt =
                (ProcessDescriptionType) processDesc.getProcessDescription().get(0);
        InputDescriptionType idt = (InputDescriptionType) pdt.getDataInputs().getInput().get(0);

        // create doubles for the input
        Double d1 = 77.84;
        Double d2 = 40039.229;

        // create and set the input on the exe request
        List<EObject> list = new ArrayList<EObject>();
        DataType input = WPSUtils.createInputDataType(d1, idt);
        list.add(input);
        exeRequest.addInput(idt.getIdentifier().getValue(), list);

        InputDescriptionType idt2 = (InputDescriptionType) pdt.getDataInputs().getInput().get(1);
        List<EObject> list2 = new ArrayList<EObject>();
        DataType input2 = WPSUtils.createInputDataType(d2, idt2);
        list2.add(input2);
        exeRequest.addInput(idt2.getIdentifier().getValue(), list2);
    }

    /** Try to get an area grid in arcgrid format, raw */
    public void testExecuteLocalAreaGrid() throws ServiceException, IOException, ParseException {

        // don't run the test if the server is not up
        if (fixture == null) {
            return;
        }

        String processIdenLocal = "gs:AreaGrid";

        WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and execute it
        ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
        EList processes = processOfferings.getProcess();
        // ProcessBriefType process = (ProcessBriefType) processes.get(0);

        // does the server contain the specific process I want
        boolean found = false;
        Iterator iterator = processes.iterator();
        while (iterator.hasNext()) {
            ProcessBriefType process = (ProcessBriefType) iterator.next();
            if (process.getIdentifier().getValue().equalsIgnoreCase(processIdenLocal)) {
                found = true;

                break;
            }
        }

        // exit test if my process doesn't exist on server
        if (!found) {
            // System.out.println("Skipping, gs:AreaGrid not found!");
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
        exeRequest.addInput(
                "envelope",
                Arrays.asList(
                        wps.createBoundingBoxInputValue(
                                "EPSG:4326",
                                2,
                                Arrays.asList(-180d, -90d),
                                Arrays.asList(180d, 90d))));
        exeRequest.addInput("width", Arrays.asList(wps.createLiteralInputValue("2")));
        exeRequest.addInput("height", Arrays.asList(wps.createLiteralInputValue("1")));
        OutputDefinitionType rawOutput = wps.createOutputDefinitionType("result");
        rawOutput.setMimeType("application/arcgrid");
        ResponseFormType responseForm = wps.createResponseForm(null, rawOutput);
        exeRequest.setResponseForm(responseForm);

        // send the request
        ExecuteProcessResponse response = wps.issueRequest(exeRequest);

        // response should not be null and no exception should occur.
        assertNotNull(response);

        // we should get a raw response, no exception, no response document
        ExecuteResponseType executeResponse = response.getExecuteResponse();
        assertNull(executeResponse);
        ExceptionReportType exceptionResponse = response.getExceptionResponse();
        assertNull(exceptionResponse);

        // check result correctness
        assertEquals("application/arcgrid", response.getRawContentType());
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(response.getRawResponseStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        String arcgrid = sb.toString();
        String expectedHeader =
                "NCOLS 2\n"
                        + "NROWS 1\n"
                        + "XLLCORNER -180.0\n"
                        + "YLLCORNER -90.0\n"
                        + "CELLSIZE 180.0\n"
                        + "NODATA_VALUE -9999";
        assertTrue(arcgrid.startsWith(expectedHeader));
    }

    /**
     * Request for area grid with raw output but wrong parameters, check the response is an
     * exception
     */
    public void testExecuteLocalAreaGridException()
            throws ServiceException, IOException, ParseException {

        // don't run the test if the server is not up
        if (fixture == null) {
            return;
        }

        String processIdenLocal = "gs:AreaGrid";

        WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and execute it
        ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
        EList processes = processOfferings.getProcess();
        // ProcessBriefType process = (ProcessBriefType) processes.get(0);

        // does the server contain the specific process I want
        boolean found = false;
        Iterator iterator = processes.iterator();
        while (iterator.hasNext()) {
            ProcessBriefType process = (ProcessBriefType) iterator.next();
            if (process.getIdentifier().getValue().equalsIgnoreCase(processIdenLocal)) {
                found = true;

                break;
            }
        }

        // exit test if my process doesn't exist on server
        if (!found) {
            // System.out.println("Skipping, gs:AreaGrid not found!");
            return;
        }

        // build the request
        ExecuteProcessRequest exeRequest = wps.createExecuteProcessRequest();
        exeRequest.setIdentifier(processIdenLocal);
        exeRequest.addInput(
                "envelope",
                Arrays.asList(
                        wps.createBoundingBoxInputValue(
                                "EPSG:4326",
                                2,
                                Arrays.asList(-180d, -90d),
                                Arrays.asList(180d, 90d))));
        // don't set the width, height required params
        // exeRequest.addInput("width", Arrays.asList(wps.createLiteralInputValue("abc")));
        // exeRequest.addInput("height", Arrays.asList(wps.createLiteralInputValue("def")));
        OutputDefinitionType rawOutput = wps.createOutputDefinitionType("result");
        rawOutput.setMimeType("application/arcgrid");
        ResponseFormType responseForm = wps.createResponseForm(null, rawOutput);
        exeRequest.setResponseForm(responseForm);

        // send the request
        ExecuteProcessResponse response = wps.issueRequest(exeRequest);

        // response should not be null and no exception should occur.
        assertNotNull(response);

        // we should get a raw response, no exception, no response document
        ExecuteResponseType executeResponse = response.getExecuteResponse();
        assertNotNull(executeResponse);
        assertNotNull(executeResponse.getStatus().getProcessFailed());
        assertNotNull(executeResponse.getStatus().getProcessFailed().getExceptionReport());
    }

    /** Try to get an area grid with output in asynchronous mode */
    public void testExecuteAsynchAreaGrid() throws ServiceException, IOException, ParseException {

        // don't run the test if the server is not up
        if (fixture == null) {
            return;
        }

        String processIdenLocal = "gs:AreaGrid";

        WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and execute it
        ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
        EList processes = processOfferings.getProcess();
        // ProcessBriefType process = (ProcessBriefType) processes.get(0);

        // does the server contain the specific process I want
        boolean found = false;
        Iterator iterator = processes.iterator();
        while (iterator.hasNext()) {
            ProcessBriefType process = (ProcessBriefType) iterator.next();
            if (process.getIdentifier().getValue().equalsIgnoreCase(processIdenLocal)) {
                found = true;

                break;
            }
        }

        // exit test if my process doesn't exist on server
        if (!found) {
            // System.out.println("Skipping, gs:AreaGrid not found!");
            return;
        }

        // based on the describeprocess, setup the execute
        ExecuteProcessRequest exeRequest = wps.createExecuteProcessRequest();
        exeRequest.setIdentifier(processIdenLocal);
        exeRequest.addInput(
                "envelope",
                Arrays.asList(
                        wps.createBoundingBoxInputValue(
                                "EPSG:4326",
                                2,
                                Arrays.asList(-180d, -90d),
                                Arrays.asList(180d, 90d))));
        exeRequest.addInput("width", Arrays.asList(wps.createLiteralInputValue("100")));
        exeRequest.addInput("height", Arrays.asList(wps.createLiteralInputValue("50")));
        ResponseDocumentType doc = wps.createResponseDocumentType(false, true, true, "result");
        DocumentOutputDefinitionType odt = (DocumentOutputDefinitionType) doc.getOutput().get(0);
        odt.setMimeType("application/arcgrid");
        odt.setAsReference(true);
        ResponseFormType responseForm = wps.createResponseForm(doc, null);
        exeRequest.setResponseForm(responseForm);

        // send the request
        ExecuteProcessResponse response = wps.issueRequest(exeRequest);

        // response should not be null and no exception should occur.
        assertNotNull(response);

        // we should get a raw response, no exception, no response document
        ExecuteResponseType executeResponse = response.getExecuteResponse();
        assertNotNull(executeResponse);

        // loop and wait for the process to be complete
        while (executeResponse.getStatus().getProcessFailed() == null
                && executeResponse.getStatus().getProcessSucceeded() == null) {

            String location = executeResponse.getStatusLocation();
            URL url = new URL(location);
            response = wps.issueStatusRequest(url);

            executeResponse = response.getExecuteResponse();
            assertNotNull(executeResponse);
        }

        // check result correctness
        assertEquals(1, executeResponse.getProcessOutputs().getOutput().size());
        OutputDataType output =
                (OutputDataType) executeResponse.getProcessOutputs().getOutput().get(0);

        assertEquals("result", output.getIdentifier().getValue());
        assertEquals("application/arcgrid", output.getReference().getMimeType());
        assertNotNull(output.getReference().getHref());

        URL dataURL = new URL(output.getReference().getHref());
        BufferedReader reader = new BufferedReader(new InputStreamReader(dataURL.openStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        int count = 0;
        while ((line = reader.readLine()) != null && count <= 5) {
            sb.append(line).append("\n");
            count++;
        }
        reader.close();
        String arcgrid = sb.toString();
        String expectedHeader =
                "NCOLS 100\n"
                        + "NROWS 50\n"
                        + "XLLCORNER -180.0\n"
                        + "YLLCORNER -90.0\n"
                        + "CELLSIZE 3.6\n"
                        + "NODATA_VALUE -9999";
        // System.out.println(arcgrid);
        assertTrue(arcgrid.startsWith(expectedHeader));
    }

    /** Test exception parsing on invalid process request */
    public void testInvalidProcess() throws ServiceException, IOException, ParseException {

        // don't run the test if the server is not up
        if (fixture == null) {
            return;
        }

        String processIdenLocal = "gs:InvalidProcessName";

        WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and execute it
        ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
        EList processes = processOfferings.getProcess();
        // ProcessBriefType process = (ProcessBriefType) processes.get(0);

        // does the server contain the specific process I want
        boolean found = false;
        Iterator iterator = processes.iterator();
        while (iterator.hasNext()) {
            ProcessBriefType process = (ProcessBriefType) iterator.next();
            if (process.getIdentifier().getValue().equalsIgnoreCase(processIdenLocal)) {
                found = true;

                break;
            }
        }

        // exit test if my process doesn't exist on server
        if (found) {
            // System.out.println("Skipping, gs:InvalidProcessName has been found!");
            return;
        }

        // setup a fake call to fake process
        ExecuteProcessRequest exeRequest = wps.createExecuteProcessRequest();
        exeRequest.setIdentifier(processIdenLocal);
        ResponseDocumentType doc = wps.createResponseDocumentType(false, true, true, "result");
        DocumentOutputDefinitionType odt = (DocumentOutputDefinitionType) doc.getOutput().get(0);
        odt.setMimeType("application/arcgrid");
        odt.setAsReference(true);
        ResponseFormType responseForm = wps.createResponseForm(doc, null);
        exeRequest.setResponseForm(responseForm);

        // send the request
        ExecuteProcessResponse response = wps.issueRequest(exeRequest);

        // response should not be null and no exception should occur.
        assertNotNull(response);

        // we should get an exception
        ExceptionReportType report = response.getExceptionResponse();
        assertNotNull(report);
        ExceptionType exception = (ExceptionType) report.getException().get(0);
        EList<String> errorMessage = exception.getExceptionText();
        assertTrue(errorMessage.get(0).contains(processIdenLocal));
    }

    /** Make sure we get the proper exception report */
    public void testInvalidParamsRawOutput() throws ServiceException, IOException, ParseException {

        // don't run the test if the server is not up
        if (fixture == null) {
            return;
        }

        String processIdenLocal = "gs:AreaGrid";

        WPSCapabilitiesType capabilities = wps.getCapabilities();

        // get the first process and execute it
        ProcessOfferingsType processOfferings = capabilities.getProcessOfferings();
        EList processes = processOfferings.getProcess();
        // ProcessBriefType process = (ProcessBriefType) processes.get(0);

        // does the server contain the specific process I want
        boolean found = false;
        Iterator iterator = processes.iterator();
        while (iterator.hasNext()) {
            ProcessBriefType process = (ProcessBriefType) iterator.next();
            if (process.getIdentifier().getValue().equalsIgnoreCase(processIdenLocal)) {
                found = true;

                break;
            }
        }

        // exit test if my process doesn't exist on server
        if (!found) {
            // System.out.println("Skipping, gs:AreaGrid not found!");
            return;
        }

        // based on the describeprocess, setup the execute
        ExecuteProcessRequest exeRequest = wps.createExecuteProcessRequest();
        exeRequest.setIdentifier(processIdenLocal);
        // don't send over the inputs
        OutputDefinitionType rawOutput = wps.createOutputDefinitionType("result");
        rawOutput.setMimeType("application/arcgrid");
        ResponseFormType responseForm = wps.createResponseForm(null, rawOutput);
        exeRequest.setResponseForm(responseForm);

        // send the request
        ExecuteProcessResponse response = wps.issueRequest(exeRequest);

        // response should not be null and no exception should occur.
        assertNotNull(response);

        // we should get an exception here too
        ExceptionReportType report = response.getExceptionResponse();
        assertNotNull(report);
    }

    @SuppressWarnings("unchecked")
    public void testChainingCall() throws ServiceException, IOException, ParseException {

        // don't run the test if the server is not up
        if (fixture == null) {
            return;
        }

        // ----

        // reference to the Cascaded Process
        EObject processUnionCascadeReference = Wps10Factory.eINSTANCE.createInputReferenceType();
        ((InputReferenceType) processUnionCascadeReference).setMimeType("application/xml");
        ((InputReferenceType) processUnionCascadeReference).setMethod(MethodType.POST_LITERAL);
        ((InputReferenceType) processUnionCascadeReference).setHref("http://geoserver/wps");

        ExecuteType processUnionExecType = Wps10Factory.eINSTANCE.createExecuteType();
        processUnionExecType.setVersion("1.0.0");
        processUnionExecType.setService("WPS");

        // based on the DescribeProcess, setup the execute

        // ----
        String processIden = "JTS:union";

        CodeType resultsCodeType = Ows11Factory.eINSTANCE.createCodeType();
        resultsCodeType.setValue("result");

        CodeType codeType = Ows11Factory.eINSTANCE.createCodeType();
        codeType.setValue(processIden);

        processUnionExecType.setIdentifier(codeType);

        DataInputsType1 inputtypes = Wps10Factory.eINSTANCE.createDataInputsType1();
        processUnionExecType.setDataInputs(inputtypes);

        ResponseFormType processUnionResponseForm = Wps10Factory.eINSTANCE.createResponseFormType();
        OutputDefinitionType processUnionRawDataOutput =
                Wps10Factory.eINSTANCE.createOutputDefinitionType();
        processUnionRawDataOutput.setIdentifier(resultsCodeType);
        processUnionResponseForm.setRawDataOutput(processUnionRawDataOutput);

        processUnionExecType.setResponseForm(processUnionResponseForm);

        // set inputs

        // POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))
        ComplexDataType cdt1 = Wps10Factory.eINSTANCE.createComplexDataType();
        cdt1.getData().add(0, new CDATAEncoder("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))"));
        cdt1.setMimeType("application/wkt");
        net.opengis.wps10.DataType data1 = Wps10Factory.eINSTANCE.createDataType();
        data1.setComplexData(cdt1);

        InputType input1 = Wps10Factory.eINSTANCE.createInputType();
        CodeType inputIdent = Ows11Factory.eINSTANCE.createCodeType();
        inputIdent.setValue("geom");
        input1.setIdentifier(inputIdent);
        input1.setData(data1);

        processUnionExecType.getDataInputs().getInput().add(input1);

        // POLYGON((2 1, 3 0, 4 1, 3 2, 2 1))
        ComplexDataType cdt2 = Wps10Factory.eINSTANCE.createComplexDataType();
        cdt2.getData().add(0, new CDATAEncoder("POLYGON((2 1, 3 0, 4 1, 3 2, 2 1))"));
        cdt2.setMimeType("application/wkt");
        net.opengis.wps10.DataType data2 = Wps10Factory.eINSTANCE.createDataType();
        data2.setComplexData(cdt2);

        InputType input2 = Wps10Factory.eINSTANCE.createInputType();
        CodeType inputIdent2 = Ows11Factory.eINSTANCE.createCodeType();
        inputIdent2.setValue("geom");
        input2.setIdentifier(inputIdent2);
        input2.setData(data2);

        processUnionExecType.getDataInputs().getInput().add(input2);

        // set the Cascade WPS process Body
        ((InputReferenceType) processUnionCascadeReference)
                .setBody(new WPSEncodeDelegate(processUnionExecType, WPS.Execute));

        // ----
        String areaProcessIdent = "JTS:area";

        ExecuteProcessRequest exeRequest = wps.createExecuteProcessRequest();
        exeRequest.setIdentifier(areaProcessIdent);
        exeRequest.addInput("geom", Arrays.asList(processUnionCascadeReference));

        // send the request
        ExecuteProcessResponse response = wps.issueRequest(exeRequest);
        Object result = response.getExecuteResponse().getProcessOutputs().getOutput().get(0);
        // System.out.println(result);
    }
}

class CDATAEncoder implements EncoderDelegate {
    String cData;

    public CDATAEncoder(String cData) {
        this.cData = cData;
    }

    @Override
    public void encode(ContentHandler output) throws Exception {
        ((LexicalHandler) output).startCDATA();
        Reader r = new StringReader(cData);
        char[] buffer = new char[1024];
        int read;
        while ((read = r.read(buffer)) > 0) {
            output.characters(buffer, 0, read);
        }
        r.close();
        ((LexicalHandler) output).endCDATA();
    }
}

class WPSEncodeDelegate implements EncoderDelegate {

    private Object value;

    private QName qname;

    public WPSEncodeDelegate(Object value, QName qname) {
        this.value = value;
        this.qname = qname;
    }

    @Override
    public void encode(ContentHandler output) throws Exception {
        WPSConfiguration config = new WPSConfiguration();
        Encoder encoder = new Encoder(config);
        encoder.encode(value, qname, output);
    }
}
