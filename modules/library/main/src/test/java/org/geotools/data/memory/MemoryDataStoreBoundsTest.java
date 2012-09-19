/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.memory;

import org.geotools.data.DataTestCase;
import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * @author Frank Gasdorf, fgdrf@users.sourceforge.net
 * @author Sebastian Graca, ISPiK S.A.
 *
 *
 *
 *
 * @source $URL$
 */
public class MemoryDataStoreBoundsTest extends DataTestCase {
    MemoryDataStore data;
    
	public MemoryDataStoreBoundsTest(String name) {
		super(name);
	}

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        data = new MemoryDataStore();
        data.addFeatures(roadFeatures);
        
        SimpleFeatureType riverTypeWithCrs = SimpleFeatureTypeBuilder.retype(riverType, DefaultGeographicCRS.WGS84);
        data.addFeature(SimpleFeatureBuilder.retype(riverFeatures[0], riverTypeWithCrs));
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        data = null;
        super.tearDown();
    }

	public void testGetBounds() throws Exception {
        // the Bounds of the queried features should be equal to the bounding 
        // box of the road2 feature, because of the road2 FID filter  
        Query query = new DefaultQuery("road", rd2Filter);
        assertEquals(roadFeatures[1].getBounds(), data.getBounds(query));
    }
    
    public void testNoCrs() throws Exception {
        Query query = new Query(roadType.getTypeName());
        ReferencedEnvelope envelope = data.getBounds(query);
        assertNull(envelope.getCoordinateReferenceSystem());
    }

    public void testSetsEnvelopeCrsFromQuery() throws Exception {
        Query query = new Query(riverType.getTypeName());
        query.setCoordinateSystem(DefaultEngineeringCRS.CARTESIAN_2D);
        ReferencedEnvelope envelope = data.getBounds(query);
        assertEquals(DefaultEngineeringCRS.CARTESIAN_2D, envelope.getCoordinateReferenceSystem());
    }

    public void testSetsEnvelopeCrsFromFeatureType() throws Exception {
        Query query = new Query(riverType.getTypeName());
        ReferencedEnvelope envelope = data.getBounds(query);
        assertEquals(DefaultGeographicCRS.WGS84, envelope.getCoordinateReferenceSystem());
    }
}
