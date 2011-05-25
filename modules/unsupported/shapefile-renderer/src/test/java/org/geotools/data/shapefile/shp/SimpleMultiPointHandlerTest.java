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
package org.geotools.data.shapefile.shp;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.net.URL;

import junit.framework.TestCase;

import org.geotools.TestData;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.ShapefileRendererUtil;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.shape.SimpleGeometry;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Tests the MultiPointHandler
 * 
 * @author jeichar
 * @since 2.1.x
 *
 * @source $URL$
 */
public class SimpleMultiPointHandlerTest extends TestCase {

    public void testRead() throws Exception {
        URL url = TestData.url("shapes/MultiPointTest.shp");
        ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory()
                .createDataStore(url);

        Envelope env = ds.getFeatureSource().getBounds();
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        MathTransform mt = CRS.findMathTransform(crs,
                DefaultGeographicCRS.WGS84);

        Rectangle rectangle = new Rectangle(300,0,300, 300);
        AffineTransform transform = RendererUtilities.worldToScreenTransform(
                env, rectangle);
        GeneralMatrix matrix = new GeneralMatrix(transform);
        MathTransform at = ReferencingFactoryFinder.getMathTransformFactory(null)
                .createAffineTransform(matrix);
        mt = ReferencingFactoryFinder.getMathTransformFactory(null)
                .createConcatenatedTransform(mt, at);

        ShapefileReader reader=new ShapefileReader(ShapefileRendererUtil.getShpFiles(ds), false, false);
        reader.setHandler(new org.geotools.renderer.shape.shapehandler.simple.MultiPointHandler(reader.getHeader()
                .getShapeType(), env, rectangle, mt, false));
        ds.getSchema();
        Object shape = reader.nextRecord().shape();
        assertNotNull(shape);
        assertTrue(shape instanceof SimpleGeometry);
        int i = 0;
        while (reader.hasNext()) {
            i++;
            shape = reader.nextRecord().shape();
            assertNotNull(shape);
            assertTrue(shape instanceof SimpleGeometry);
        }
        assertEquals(ds.getFeatureSource().getCount(Query.ALL) - 1, i);
    }

}
