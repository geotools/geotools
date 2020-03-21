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
package org.geotools.data.wps;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import net.opengis.ows11.BoundingBoxType;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.Ows11Factory;
import net.opengis.wps10.DataType;
import net.opengis.wps10.DocumentOutputDefinitionType;
import net.opengis.wps10.LiteralDataType;
import net.opengis.wps10.OutputDefinitionType;
import net.opengis.wps10.ResponseDocumentType;
import net.opengis.wps10.ResponseFormType;
import net.opengis.wps10.Wps10Factory;
import org.geotools.data.ows.AbstractGetCapabilitiesRequest;
import org.geotools.data.ows.GetCapabilitiesRequest;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Response;
import org.geotools.data.wps.request.AbstractDescribeProcessRequest;
import org.geotools.data.wps.request.AbstractExecuteProcessRequest;
import org.geotools.data.wps.request.DescribeProcessRequest;
import org.geotools.data.wps.request.ExecuteProcessRequest;
import org.geotools.data.wps.response.DescribeProcessResponse;
import org.geotools.data.wps.response.ExecuteProcessResponse;
import org.geotools.data.wps.response.WPSGetCapabilitiesResponse;
import org.geotools.ows.ServiceException;

/**
 * Provides support for the Web Processing Service 1.0.0 Specification.
 *
 * <p>WPS1_0_0 provides both name and version information that may be checked against a
 * GetCapabilities document during version negotiation.
 *
 * @author gdavis
 */
public class WPS1_0_0 extends WPSSpecification {

    private static final Wps10Factory wpsFactory = Wps10Factory.eINSTANCE;

    private static String processKey(String key) {
        return key.trim().toLowerCase();
    }

    /** Public constructor creates the WMS1_0_0 object. */
    public WPS1_0_0() {}

    /**
     * Expected version attribute for root element.
     *
     * @return the expect version value for this specification
     */
    public String getVersion() {
        return "1.0.0"; // $NON-NLS-1$
    }

    /**
     * Create a request for performing GetCapabilities requests on a 1.0.0 server.
     *
     * @see org.geotools.data.wps.WPSSpecification#createGetCapabilitiesRequest(java.net.URL)
     * @param server a URL that points to the 1.0.0 server
     * @return a AbstractGetCapabilitiesRequest object that can provide a valid request
     */
    public GetCapabilitiesRequest createGetCapabilitiesRequest(URL server) {
        return new GetCapsRequest(server);
    }

    @Override
    public DescribeProcessRequest createDescribeProcessRequest(URL onlineResource)
            throws UnsupportedOperationException {
        return new InternalDescribeProcessRequest(onlineResource, null);
    }

    @Override
    public ExecuteProcessRequest createExecuteProcessRequest(URL onlineResource)
            throws UnsupportedOperationException {
        return new InternalExecuteProcessRequest(onlineResource, null);
    }

    @Override
    public DataType createLiteralInputValue(String literalValue) {
        DataType literalInputValue = wpsFactory.createDataType();
        LiteralDataType literalDataType = wpsFactory.createLiteralDataType();
        literalDataType.setValue(literalValue);
        literalInputValue.setLiteralData(literalDataType);

        return literalInputValue;
    }

    @Override
    public DataType createBoundingBoxInputValue(
            String crs, int dimensions, List<Double> lowerCorner, List<Double> upperCorner) {
        DataType bbox = wpsFactory.createDataType();
        BoundingBoxType bboxType = Ows11Factory.eINSTANCE.createBoundingBoxType();
        bboxType.setCrs(crs);
        bboxType.setDimensions(BigInteger.valueOf(dimensions));
        bboxType.setLowerCorner(lowerCorner);
        bboxType.setUpperCorner(upperCorner);
        bbox.setBoundingBoxData(bboxType);

        return bbox;
    }

    @Override
    public ResponseFormType createResponseForm(
            ResponseDocumentType responseDoc, OutputDefinitionType rawOutput) {
        ResponseFormType responseForm = wpsFactory.createResponseFormType();

        if (responseDoc != null) {
            responseForm.setResponseDocument(responseDoc);
        }

        if (rawOutput != null) {
            responseForm.setRawDataOutput(rawOutput);
        }

        return responseForm;
    }

    @Override
    public OutputDefinitionType createOutputDefinitionType(String identifier) {
        CodeType inputType = Ows11Factory.eINSTANCE.createCodeType();
        inputType.setValue(identifier);

        OutputDefinitionType rawOutput = wpsFactory.createOutputDefinitionType();
        rawOutput.setIdentifier(inputType);

        return rawOutput;
    }

    @Override
    public ResponseDocumentType createResponseDocumentType(
            boolean lineage, boolean status, boolean storeExecuteResponse, String outputType) {
        ResponseDocumentType responseDoc = wpsFactory.createResponseDocumentType();
        responseDoc.setLineage(lineage);
        responseDoc.setStatus(status);
        responseDoc.setStoreExecuteResponse(storeExecuteResponse);

        if (outputType != null) {
            DocumentOutputDefinitionType output = wpsFactory.createDocumentOutputDefinitionType();
            CodeType inputType = Ows11Factory.eINSTANCE.createCodeType();
            inputType.setValue(outputType);
            output.setIdentifier(inputType);
            responseDoc.getOutput().add(output);
        }

        return responseDoc;
    }

    /** We need a custom request object. */
    public static class GetCapsRequest extends AbstractGetCapabilitiesRequest {

        /**
         * Construct a Request compatible with a 1.0.0 Web Process Server.
         *
         * @param urlGetCapabilities URL of GetCapabilities document.
         */
        public GetCapsRequest(URL urlGetCapabilities) {
            super(urlGetCapabilities);
        }

        protected void initVersion() {
            properties.setProperty(VERSION, "1.0.0");
        }

        protected void initRequest() {
            setProperty("REQUEST", "GetCapabilities");
        }

        protected void initService() {
            setProperty("SERVICE", "WPS");
        }

        protected String processKey(String key) {
            return WPS1_0_0.processKey(key);
        }

        public Response createResponse(HTTPResponse httpResponse)
                throws ServiceException, IOException {
            return new WPSGetCapabilitiesResponse(httpResponse, hints);
        }
    }

    public static class InternalDescribeProcessRequest extends AbstractDescribeProcessRequest {

        /** */
        public InternalDescribeProcessRequest(URL onlineResource, Properties properties) {
            super(onlineResource, properties);
        }

        protected void initVersion() {
            setProperty(VERSION, "1.0.0");
        }

        public Response createResponse(HTTPResponse httpResponse)
                throws ServiceException, IOException {
            return new DescribeProcessResponse(httpResponse);
        }
    }

    public static class InternalExecuteProcessRequest extends AbstractExecuteProcessRequest {

        /** */
        public InternalExecuteProcessRequest(URL onlineResource, Properties properties) {
            super(onlineResource, properties);
        }

        protected void initVersion() {
            setProperty(VERSION, "1.0.0");
        }

        public Response createResponse(HTTPResponse httpResponse)
                throws ServiceException, IOException {
            return new ExecuteProcessResponse(
                    httpResponse, responseForm != null && responseForm.getRawDataOutput() != null);
        }
    }
}
