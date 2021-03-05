/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.io.IOException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class DiffWriterTest {

    FeatureReader<SimpleFeatureType, SimpleFeature> reader;
    DiffFeatureWriter writer;
    private Point geom;
    private SimpleFeatureType type;

    @Before
    public void setUp() throws Exception {
        type = DataUtilities.createType("default", "name:String,*geom:Geometry");
        GeometryFactory fac = new GeometryFactory();
        geom = fac.createPoint(new Coordinate(10, 10));

        Diff diff = new Diff();
        diff.add("1", SimpleFeatureBuilder.build(type, new Object[] {"diff1", geom}, "1"));
        diff.modify(
                "original",
                SimpleFeatureBuilder.build(type, new Object[] {"diff2", geom}, "original"));
        reader =
                new TestReader(
                        type,
                        SimpleFeatureBuilder.build(
                                type, new Object[] {"original", geom}, "original"));
        writer =
                new DiffFeatureWriter(reader, diff) {
                    @Override
                    protected void fireNotification(int eventType, ReferencedEnvelope bounds) {}
                };
    }

    @After
    public void cleanup() throws IOException {
        writer.close();
        reader.close();
    }

    @Test
    public void testRemove() throws Exception {
        writer.next();
        SimpleFeature feature = writer.next();
        writer.remove();
        Assert.assertNull(writer.diff.getAdded().get(feature.getID()));
    }

    @Test
    public void testHasNext() throws Exception {
        Assert.assertTrue(writer.hasNext());
        Assert.assertEquals(2, writer.diff.getAdded().size() + writer.diff.getModified().size());
        writer.next();
        Assert.assertTrue(writer.hasNext());
        Assert.assertEquals(2, writer.diff.getAdded().size() + writer.diff.getModified().size());
        writer.next();
        Assert.assertFalse(writer.hasNext());
        Assert.assertEquals(2, writer.diff.getAdded().size() + writer.diff.getModified().size());
    }

    @Test
    public void testWrite() throws IOException, Exception {
        while (writer.hasNext()) {
            writer.next();
        }

        SimpleFeature feature = writer.next();
        feature.setAttribute("name", "new1");

        writer.write();
        Assert.assertEquals(2, writer.diff.getAdded().size());
        feature = writer.next();
        feature.setAttribute("name", "new2");

        writer.write();

        Assert.assertEquals(3, writer.diff.getAdded().size());
    }
}
