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
package org.geotools.data.store;

import java.util.Iterator;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform2D;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 *
 * @source $URL$
 */
public class ReprojectingFeatureCollectionTest extends FeatureCollectionWrapperTestSupport {

    CoordinateReferenceSystem target;

    GeometryCoordinateSequenceTransformer transformer;
    
    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    protected void setUp() throws Exception {
        super.setUp();

        target = CRS.decode("EPSG:3005");

        MathTransform2D tx = (MathTransform2D) ReferencingFactoryFinder
                .getCoordinateOperationFactory(null).createOperation(crs, target)
                .getMathTransform();
        transformer = new GeometryCoordinateSequenceTransformer();
        transformer.setMathTransform(tx);
    }

    public void testNormal() throws Exception {

        Iterator reproject = new ReprojectingFeatureCollection(delegate, target).iterator();
        Iterator reader = delegate.iterator();

        while (reader.hasNext()) {
            SimpleFeature normal = (SimpleFeature) reader.next();
            SimpleFeature reprojected = (SimpleFeature) reproject.next();

            Point p1 = (Point) normal.getAttribute("defaultGeom");
            Point p2 = (Point) reprojected.getAttribute("defaultGeom");
            if (p1 != null) {
                p1 = (Point) transformer.transform(p1);
                assertTrue(p1.equalsExact(p2));
            } else {
                assertNull(p2);
            }

            LineString l1 = (LineString) normal.getAttribute("otherGeom");
            LineString l2 = (LineString) reprojected.getAttribute("otherGeom");
            if (l1 != null) {
                l1 = (LineString) transformer.transform(l1);
                assertTrue(l1.equalsExact(l2));
            } else {
                assertNull(l2);
            }
        }

    }

    public void testBounds() throws Exception {
        ReprojectingFeatureCollection rfc = new ReprojectingFeatureCollection(delegate, target);
        ReferencedEnvelope bounds = delegate.getBounds();
        // the reprojection of the full bounds is going to be bigger than the sum of the
        // feature by feature reprojected bounds
        assertTrue(bounds.transform(target, true).contains((BoundingBox) rfc.getBounds()));
        
    }
    
    public void testFilter() throws Exception {
        ReprojectingFeatureCollection rfc = new ReprojectingFeatureCollection(delegate, target);
        ReferencedEnvelope bounds = delegate.getBounds();
        ReferencedEnvelope rbounds = bounds.transform(target, true);
        
        // check the bounds filtering works the same way in the standard and reprojected case
        BBOX filter = ff.bbox("", bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(), bounds.getMaxY(), 
                CRS.toSRS(delegate.getSchema().getCoordinateReferenceSystem()));
        BBOX rfilter = ff.bbox("", rbounds.getMinX(), rbounds.getMinY(), rbounds.getMaxX(), rbounds.getMaxY(), CRS.toSRS(target));
        assertEquals(delegate.subCollection(filter).size(), rfc.subCollection(rfilter).size());
    }
}
