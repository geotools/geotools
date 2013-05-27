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

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform2D;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

import static org.easymock.EasyMock.*;
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

        SimpleFeatureIterator reproject = new ReprojectingFeatureCollection(delegate, target).features();
        SimpleFeatureIterator reader = delegate.features();
        try {
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
        finally {
            reproject.close();
            reader.close();
        }
    }

    public void testBounds() throws Exception {
        ReprojectingFeatureCollection rfc = new ReprojectingFeatureCollection(delegate, target);
        ReferencedEnvelope bounds = delegate.getBounds();
        // the reprojection of the full bounds is going to be bigger than the sum of the
        // feature by feature reprojected bounds
        assertTrue(bounds.transform(target, true).contains((BoundingBox) rfc.getBounds()));

        // make sure that the reprojected bounds contain the target CRS
        assertEquals(target, rfc.getBounds().getCoordinateReferenceSystem());
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
    
    public void testLenient() throws Exception {
        CoordinateReferenceSystem lenientTarget;

        lenientTarget = CRS.parseWKT(
                "PROJCS[\"MGI (Ferro) / Austria GK West Zone\",GEOGCS[\"MGI (Ferro)\",DATUM[\"Militar_Geographische_Institut_Ferro\",SPHEROID[\"Bessel 1841\",6377397.155,299.1528128,AUTHORITY[\"EPSG\",\"7004\"]],AUTHORITY[\"EPSG\",\"6805\"]],PRIMEM[\"Ferro\",-17.66666666666667,AUTHORITY[\"EPSG\",\"8909\"]],UNIT[\"degree\",0.01745329251994328,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4805\"]],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],PROJECTION[\"Transverse_Mercator\"],PARAMETER[\"latitude_of_origin\",0],PARAMETER[\"central_meridian\",28],PARAMETER[\"scale_factor\",1],PARAMETER[\"false_easting\",0],PARAMETER[\"false_northing\",-5000000],AUTHORITY[\"EPSG\",\"31251\"],AXIS[\"Y\",EAST],AXIS[\"X\",NORTH]]"
            );
        
        SimpleFeatureIterator reproject = new ReprojectingFeatureCollection( delegate, lenientTarget ).features();
        reproject.close();
    }
    
    public void testDelegateAccepts() throws Exception {
        SimpleFeatureTypeBuilder stb = new SimpleFeatureTypeBuilder();
        stb.setName("test");
        stb.setCRS(DefaultGeographicCRS.WGS84);
        stb.add("geo", Geometry.class);
        stb.add("bar", Integer.class);
        SimpleFeatureType ft = stb.buildFeatureType();

        UniqueVisitor vis = new UniqueVisitor("bar");

        SimpleFeatureCollection delegate = createMock(SimpleFeatureCollection.class);
        expect(delegate.getSchema()).andReturn(ft).anyTimes();
        delegate.accepts(vis, null);
        expectLastCall().once();
        replay(delegate);

        ReprojectingFeatureCollection rfc = new ReprojectingFeatureCollection(delegate, target);
        rfc.accepts(vis, null);
        verify(delegate);

        vis = new UniqueVisitor("geo");
        SimpleFeatureIterator it = createNiceMock(SimpleFeatureIterator.class);
        replay(it);

        delegate = createMock(SimpleFeatureCollection.class);
        expect(delegate.features()).andReturn(it).once();
        expect(delegate.getSchema()).andReturn(ft).anyTimes();
        replay(delegate);

        rfc = new ReprojectingFeatureCollection(delegate, target);
        rfc.accepts(vis, null);
        verify(delegate);
    }

}
