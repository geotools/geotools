/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.function;

import static org.junit.Assert.*;

import java.awt.RenderingHints.Key;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.TestData;
import org.geotools.data.Parameter;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.process.Process;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;
import org.geotools.process.feature.BufferFeatureCollectionFactory;
import org.geotools.text.Text;
import org.junit.Test;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Function;
import org.opengis.util.InternationalString;

import com.vividsolutions.jts.geom.MultiPolygon;

/**
 * 
 *
 * @source $URL$
 */
public class ProcessFunctionTest {

    FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    @Test
    public void testBuffer() throws Exception {
        URL url = TestData.getResource(TestData.class, "shapes/archsites.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeatureCollection features = store.getFeatureSource().getFeatures();

        // first param, the context feature collection
        Function featuresParam = ff.function("parameter", ff
                .literal(BufferFeatureCollectionFactory.FEATURES.key));
        // second param, the buffer size
        Function bufferParam = ff.function("parameter", ff
                .literal(BufferFeatureCollectionFactory.BUFFER.key), ff.literal(1000));
        // build the function and call it
        Function buffer = ff.function("gt:BufferFeatureCollection", featuresParam, bufferParam);
        
        // check the metadata
        FunctionName fn = buffer.getFunctionName();
        assertEquals("gt:BufferFeatureCollection", fn.getName());
        assertEquals(2, fn.getArgumentCount());
        assertEquals(FeatureCollection.class, fn.getReturn().getType());
        
        // run and check results
        SimpleFeatureCollection buffered = (SimpleFeatureCollection) buffer.evaluate(features);
        assertEquals(features.size(), buffered.size());
        GeometryDescriptor gd = buffered.getSchema().getGeometryDescriptor();
        // is it actually a buffer?
        assertEquals(MultiPolygon.class, gd.getType().getBinding());
    }


    @Test
    public void testUnavailable() throws Exception {
        ProcessFunctionFactory factory = new ProcessFunctionFactory();
        List<FunctionName> list = factory.getFunctionNames();
        assertFalse("available", list.contains(new NameImpl("test", "unavailable")));
    }
}
