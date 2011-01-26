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

import junit.framework.TestCase;
import net.opengis.ows11.CodeType;
import net.opengis.ows11.LanguageStringType;
import net.opengis.ows11.Ows11Factory;
import net.opengis.wps10.ComplexDataType;
import net.opengis.wps10.DataInputsType1;
import net.opengis.wps10.DataType;
import net.opengis.wps10.ExecuteResponseType;
import net.opengis.wps10.ExecuteType;
import net.opengis.wps10.InputType;
import net.opengis.wps10.OutputDataType;
import net.opengis.wps10.ProcessOutputsType1;
import net.opengis.wps10.Wps10Factory;

import org.geotools.xml.Encoder;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

public class ExecuteTest extends TestCase {

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

        //cd.getData().add(new GeometryFactory().createPoint(new Coordinate(1, 2)));

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
        //cdata.getData().add(new GeometryFactory().createPoint(new Coordinate(1, 1)));

        Encoder e = new Encoder(new WPSConfiguration());
        e.setIndenting(true);
        e.encode(response, WPS.ExecuteResponse, System.out);
    }
}
