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
package org.geotools.wps;

import java.math.BigInteger;
import javax.xml.namespace.QName;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.LanguageStringType;
import net.opengis.ows11.Ows11Factory;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.QueryType;
import net.opengis.wps10.ComplexDataType;
import net.opengis.wps10.DataInputsType1;
import net.opengis.wps10.DataType;
import net.opengis.wps10.ExecuteResponseType;
import net.opengis.wps10.ExecuteType;
import net.opengis.wps10.InputReferenceType;
import net.opengis.wps10.InputType;
import net.opengis.wps10.OutputDataType;
import net.opengis.wps10.ProcessOutputsType1;
import net.opengis.wps10.ProcessStartedType;
import net.opengis.wps10.StatusType;
import net.opengis.wps10.Wps10Factory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.Parser;
import org.geotools.xsd.test.XMLTestSupport;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExecuteTest extends XMLTestSupport {

    public void testExecuteEncode() throws Exception {
        Wps10Factory f = Wps10Factory.eINSTANCE;
        ExecuteType ex = f.createExecuteType();

        CodeType id = Ows11Factory.eINSTANCE.createCodeType();
        ex.setIdentifier(id);
        id.setValue("foo");

        DataInputsType1 inputs = f.createDataInputsType1();
        ex.setDataInputs(inputs);

        InputType in = f.createInputType();
        inputs.getInput().add(in);

        DataType data = f.createDataType();
        in.setData(data);

        ComplexDataType cd = f.createComplexDataType();
        data.setComplexData(cd);

        // cd.getData().add(new GeometryFactory().createPoint(new Coordinate(1, 2)));

        Encoder e = new Encoder(new WPSConfiguration());
        e.setIndenting(true);
        e.encode(ex, WPS.Execute, System.out);
    }

    public void testExecuteResponse() throws Exception {
        Wps10Factory f = Wps10Factory.eINSTANCE;
        ExecuteResponseType response = f.createExecuteResponseType();

        ProcessOutputsType1 outputs = f.createProcessOutputsType1();
        response.setProcessOutputs(outputs);

        OutputDataType output = f.createOutputDataType();
        outputs.getOutput().add(output);

        LanguageStringType title = Ows11Factory.eINSTANCE.createLanguageStringType();
        output.setTitle(title);
        title.setValue("foo");

        DataType data = f.createDataType();
        output.setData(data);

        ComplexDataType cdata = f.createComplexDataType();
        data.setComplexData(cdata);
        // cdata.getData().add(new GeometryFactory().createPoint(new Coordinate(1, 1)));

        Encoder e = new Encoder(new WPSConfiguration());
        e.setIndenting(true);
        e.encode(response, WPS.ExecuteResponse, System.out);
    }

    public void testExecuteResponseProgress() throws Exception {
        Wps10Factory f = Wps10Factory.eINSTANCE;
        ExecuteResponseType response = f.createExecuteResponseType();
        StatusType status = f.createStatusType();
        ProcessStartedType ps = f.createProcessStartedType();
        ps.setPercentCompleted(new BigInteger("20"));
        ps.setValue("Working really hard here");
        status.setProcessStarted(ps);
        response.setStatus(status);

        Document dom = encode(response, WPS.ExecuteResponse);
        // print(dom);
        NodeList nodes = dom.getElementsByTagName("wps:ProcessStarted");
        assertEquals(1, nodes.getLength());
        Node psNode = nodes.item(0);
        assertEquals("Working really hard here", psNode.getTextContent());
    }

    public void testParserDelegateNamespaces() throws Exception {
        Parser p = new Parser(new WPSConfiguration());
        ExecuteType exec =
                (ExecuteType)
                        p.parse(
                                getClass()
                                        .getResourceAsStream(
                                                "wpsExecute_inlineGetFeature_request.xml"));
        assertNotNull(exec);
        assertEquals(1, exec.getDataInputs().getInput().size());

        InputType in = (InputType) exec.getDataInputs().getInput().get(0);
        InputReferenceType ref = in.getReference();
        assertNotNull(ref);

        assertTrue(ref.getBody() instanceof GetFeatureType);
        GetFeatureType gft = (GetFeatureType) ref.getBody();

        QName typeName = (QName) ((QueryType) gft.getQuery().get(0)).getTypeName().get(0);
        assertEquals("states", typeName.getLocalPart());
        assertEquals("http://usa.org", typeName.getNamespaceURI());
    }

    public void testFilterParserDelegate() throws Exception {
        Parser p = new Parser(new WPSConfiguration());
        ExecuteType exec =
                (ExecuteType) p.parse(getClass().getResourceAsStream("wpsExecuteFilterInline.xml"));
        assertNotNull(exec);
        assertEquals(1, exec.getDataInputs().getInput().size());

        InputType in = (InputType) exec.getDataInputs().getInput().get(0);
        ComplexDataType cd = in.getData().getComplexData();
        assertNotNull(cd);
        Filter filter = (Filter) cd.getData().get(0);
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Filter expected =
                ff.or(
                        ff.greaterOrEqual(ff.property("PERSONS"), ff.literal("10000000")),
                        ff.lessOrEqual(ff.property("PERSONS"), ff.literal("20000000")));
        assertEquals(expected, filter);
    }

    @Override
    protected Configuration createConfiguration() {
        return new WPSConfiguration();
    }
}
