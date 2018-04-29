/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.v2_0.storedquery;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.opengis.wfs20.FeatureTypeType;
import net.opengis.wfs20.ParameterExpressionType;
import net.opengis.wfs20.ParameterType;
import net.opengis.wfs20.StoredQueryDescriptionType;
import net.opengis.wfs20.Wfs20Factory;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.data.wfs.internal.v2_0.FeatureTypeInfoImpl;
import org.geotools.filter.FilterFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.Filter;

public class ParameterTypeFactoryTest {
    Wfs20Factory wfs20Factory;

    FeatureTypeInfoImpl featureType;
    StoredQueryConfiguration config;
    StoredQueryDescriptionType desc;

    @Before
    public void setup() {
        wfs20Factory = Wfs20Factory.eINSTANCE;

        config = new StoredQueryConfiguration();

        // Empty featureType
        FeatureTypeType ftt = wfs20Factory.createFeatureTypeType();
        ftt.setDefaultCRS("EPSG:1234");
        featureType = new FeatureTypeInfoImpl(ftt, new WFSConfig());

        desc = wfs20Factory.createStoredQueryDescriptionType();
    }

    // One parameter, no view params, no mappings => no parameters
    @Test
    public void testSimple() {

        ParameterExpressionType param1 = wfs20Factory.createParameterExpressionType();
        param1.setName("foo");
        desc.getParameter().add(param1);

        ParameterTypeFactory factory = new ParameterTypeFactory(config, desc, featureType);

        List<ParameterType> params =
                factory.buildStoredQueryParameters(new HashMap<String, String>(), null);

        assertEquals(0, params.size());
    }

    // One parameter, one view params (which matches), no mappings => single parameter
    @Test
    public void testSingleParameter() {

        ParameterExpressionType param1 = wfs20Factory.createParameterExpressionType();
        param1.setName("foo");
        desc.getParameter().add(param1);

        ParameterTypeFactory factory = new ParameterTypeFactory(config, desc, featureType);

        Map<String, String> viewParams = new HashMap<String, String>();
        viewParams.put("foo", "bar");

        List<ParameterType> params = factory.buildStoredQueryParameters(viewParams, null);

        assertEquals(1, params.size());

        ParameterType tmp = params.get(0);
        assertEquals("foo", tmp.getName());
        assertEquals("bar", tmp.getValue());
    }

    // One parameter, one view params (which does not match), no mappings => no parameters
    @Test
    public void testUndefinedViewParameter() {

        ParameterExpressionType param1 = wfs20Factory.createParameterExpressionType();
        param1.setName("foo");
        desc.getParameter().add(param1);

        ParameterTypeFactory factory = new ParameterTypeFactory(config, desc, featureType);

        Map<String, String> viewParams = new HashMap<String, String>();
        viewParams.put("not-defined-in-spec", "bar");

        List<ParameterType> params = factory.buildStoredQueryParameters(viewParams, null);

        assertEquals(0, params.size());
    }

    // One parameter, no view params, default value mappings => default mapping
    @Test
    public void testDefaultMapping() {

        ParameterExpressionType param1 = wfs20Factory.createParameterExpressionType();
        param1.setName("foo");
        desc.getParameter().add(param1);

        ParameterMappingDefaultValue paramMapping = new ParameterMappingDefaultValue();
        paramMapping.setParameterName("foo");
        paramMapping.setDefaultValue("tiger");

        config.getStoredQueryParameterMappings().add(paramMapping);

        ParameterTypeFactory factory = new ParameterTypeFactory(config, desc, featureType);

        Map<String, String> viewParams = new HashMap<String, String>();

        List<ParameterType> params = factory.buildStoredQueryParameters(viewParams, null);

        assertEquals(1, params.size());

        ParameterType tmp = params.get(0);
        assertEquals("foo", tmp.getName());
        assertEquals("tiger", tmp.getValue());
    }

    // One parameter, view param for it, default value mappings => view param wins
    @Test
    public void testDefaultMappingOverridden() {

        ParameterExpressionType param1 = wfs20Factory.createParameterExpressionType();
        param1.setName("foo");
        desc.getParameter().add(param1);

        ParameterMappingDefaultValue paramMapping = new ParameterMappingDefaultValue();
        paramMapping.setParameterName("foo");
        paramMapping.setDefaultValue("tiger");

        config.getStoredQueryParameterMappings().add(paramMapping);

        ParameterTypeFactory factory = new ParameterTypeFactory(config, desc, featureType);

        Map<String, String> viewParams = new HashMap<String, String>();
        viewParams.put("foo", "bar");

        List<ParameterType> params = factory.buildStoredQueryParameters(viewParams, null);

        assertEquals(1, params.size());

        ParameterType tmp = params.get(0);
        assertEquals("foo", tmp.getName());
        assertEquals("bar", tmp.getValue());
    }

    // One parameter, no view params, expression mappings (literals only) => test expression value
    @Test
    public void testCQLExpressionParameterLiterals() {

        ParameterExpressionType param1 = wfs20Factory.createParameterExpressionType();
        param1.setName("foo");
        desc.getParameter().add(param1);

        ParameterMappingExpressionValue paramMapping = new ParameterMappingExpressionValue();
        paramMapping.setParameterName("foo");
        paramMapping.setExpressionLanguage("CQL");
        paramMapping.setExpression("numberFormat('0.0', 1+1*2)");

        config.getStoredQueryParameterMappings().add(paramMapping);

        ParameterTypeFactory factory = new ParameterTypeFactory(config, desc, featureType);

        Map<String, String> viewParams = new HashMap<String, String>();

        List<ParameterType> params = factory.buildStoredQueryParameters(viewParams, null);

        assertEquals(1, params.size());

        ParameterType tmp = params.get(0);
        assertEquals("foo", tmp.getName());
        assertEquals("3.0", tmp.getValue());
    }

    // Test that bbox parameters from the context are mapped appropriately
    @Test
    public void testCQLExpressionParameterContextBboxMappings() {
        desc.getParameter()
                .addAll(
                        Arrays.asList(
                                createParam("param1"),
                                createParam("param2"),
                                createParam("param3"),
                                createParam("param4")));

        config.getStoredQueryParameterMappings()
                .addAll(
                        Arrays.asList(
                                createBboxExpressionParam("param1", "bboxMinX"),
                                createBboxExpressionParam("param2", "bboxMinY"),
                                createBboxExpressionParam("param3", "bboxMaxX"),
                                createBboxExpressionParam("param4", "bboxMaxY")));

        ParameterTypeFactory factory = new ParameterTypeFactory(config, desc, featureType);

        Map<String, String> viewParams = new HashMap<String, String>();

        Filter f = new FilterFactoryImpl().bbox("nada", -10.0, -5.0, 10.0, 5.0, "WGS:84");

        List<ParameterType> params = factory.buildStoredQueryParameters(viewParams, f);

        assertEquals(4, params.size());
        // Current implementation preserves order and this code takes advantage of it. Preserving
        // the order is not paramount, though
        ParameterType tmp = params.get(0);
        assertEquals("param1", tmp.getName());
        assertEquals("-10.0", tmp.getValue());

        tmp = params.get(1);
        assertEquals("param2", tmp.getName());
        assertEquals("-5.0", tmp.getValue());

        tmp = params.get(2);
        assertEquals("param3", tmp.getName());
        assertEquals("10.0", tmp.getValue());

        tmp = params.get(3);
        assertEquals("param4", tmp.getName());
        assertEquals("5.0", tmp.getValue());
    }

    private ParameterExpressionType createParam(String name) {
        ParameterExpressionType ret = wfs20Factory.createParameterExpressionType();
        ret.setName(name);
        return ret;
    }

    private ParameterMappingExpressionValue createBboxExpressionParam(String name, String ctx) {
        ParameterMappingExpressionValue ret = new ParameterMappingExpressionValue();
        ret.setParameterName(name);
        ret.setExpressionLanguage("CQL");
        ret.setExpression("numberFormat('0.0', " + ctx + ")");
        return ret;
    }

    // One parameter, no view params, expression mappings (defaultSRS) => test expression value
    @Test
    public void testCQLExpressionSRS() {

        ParameterExpressionType param1 = wfs20Factory.createParameterExpressionType();
        param1.setName("foo");
        desc.getParameter().add(param1);

        ParameterMappingExpressionValue paramMapping = new ParameterMappingExpressionValue();
        paramMapping.setParameterName("foo");
        paramMapping.setExpressionLanguage("CQL");
        paramMapping.setExpression("defaultSRS");

        config.getStoredQueryParameterMappings().add(paramMapping);

        ParameterTypeFactory factory = new ParameterTypeFactory(config, desc, featureType);

        Map<String, String> viewParams = new HashMap<String, String>();

        List<ParameterType> params = factory.buildStoredQueryParameters(viewParams, null);

        assertEquals(1, params.size());

        ParameterType tmp = params.get(0);
        assertEquals("foo", tmp.getName());
        assertEquals("EPSG:1234", tmp.getValue());
    }

    // One parameter, no declared view params, expression mappings (viewparam) => test expression
    // value
    @Test
    public void testCQLExpressionViewParamMapping() {

        ParameterExpressionType param1 = wfs20Factory.createParameterExpressionType();
        param1.setName("foo");
        desc.getParameter().add(param1);

        ParameterMappingExpressionValue paramMapping = new ParameterMappingExpressionValue();
        paramMapping.setParameterName("foo");
        paramMapping.setExpressionLanguage("CQL");
        paramMapping.setExpression("viewparam:mapped");

        config.getStoredQueryParameterMappings().add(paramMapping);

        ParameterTypeFactory factory = new ParameterTypeFactory(config, desc, featureType);

        Map<String, String> viewParams = new HashMap<String, String>();

        viewParams.put("mapped", "stuff");

        List<ParameterType> params = factory.buildStoredQueryParameters(viewParams, null);

        assertEquals(1, params.size());

        ParameterType tmp = params.get(0);
        assertEquals("foo", tmp.getName());
        assertEquals("stuff", tmp.getValue());
    }

    @Test
    public void testCQLExpressionStringConcatenation() {

        ParameterExpressionType param1 = wfs20Factory.createParameterExpressionType();
        param1.setName("foo");
        desc.getParameter().add(param1);

        ParameterMappingExpressionValue paramMapping = new ParameterMappingExpressionValue();
        paramMapping.setParameterName("foo");
        paramMapping.setExpressionLanguage("CQL");
        paramMapping.setExpression("concatenate(numberFormat('0.0', 1+1*2), ',10')");

        config.getStoredQueryParameterMappings().add(paramMapping);

        ParameterTypeFactory factory = new ParameterTypeFactory(config, desc, featureType);

        Map<String, String> viewParams = new HashMap<String, String>();

        List<ParameterType> params = factory.buildStoredQueryParameters(viewParams, null);

        assertEquals(1, params.size());

        ParameterType tmp = params.get(0);
        assertEquals("foo", tmp.getName());
        assertEquals("3.0,10", tmp.getValue());
    }
}
