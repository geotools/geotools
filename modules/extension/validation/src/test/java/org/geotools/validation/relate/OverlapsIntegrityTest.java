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
package org.geotools.validation.relate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.locationtech.jts.geom.Envelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

/**
 * OverlapsIntegrityTest<br>
 *
 * @author bowens<br>
 *     Created Apr 29, 2004<br>
 * @version <br>
 *     <b>Puropse:</b><br>
 */
public class OverlapsIntegrityTest extends SpatialTestCase {

    /** Constructor for OverlapsIntegrityTest. */
    public OverlapsIntegrityTest(String arg0) {
        super(arg0);
    }

    public void testOverlapFilter() throws Exception {
        SimpleFeatureSource line = mds.getFeatureSource("line");

        Filter filter;

        filter = OverlapsIntegrity.filterBBox(new Envelope(), line.getSchema());
        assertEquals("with empty envelope", 1, line.getFeatures(filter).size());

        filter = OverlapsIntegrity.filterBBox(new Envelope(-1, 3, -2, 3), line.getSchema());
        assertEquals("with envelope", 4, line.getFeatures(filter).size());

        Envelope all = line.getBounds();
        if (all == null) {
            // damm lets figure it out
            all = line.getFeatures().getBounds();
        }
        int counter = 0;
        filter = OverlapsIntegrity.filterBBox(all, line.getSchema());
        SimpleFeatureIterator r = line.getFeatures().features();
        for (; r.hasNext(); ) {
            // System.out.println("Loop counter: " + ++counter);
            SimpleFeature victim = r.next();
            // System.out.println("Found line number: " + victim.getID());
            assertTrue("feature " + victim.getID(), filter.evaluate(victim));
        }
        r.close();
        assertEquals("count of all features", 4, line.getFeatures(filter).size());
    }

    public void testValidate() {
        OverlapsIntegrity overlap = new OverlapsIntegrity();
        overlap.setExpected(false);
        overlap.setGeomTypeRefA("my:line");

        Map map = new HashMap();
        try {
            map.put("my:line", mds.getFeatureSource("line"));
        } catch (IOException e1) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e1);
        }

        try {
            vr.setValidation(overlap);
            assertFalse(overlap.validate(map, lineBounds, vr));
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }

    public void testValidateBBox() {
        OverlapsIntegrity overlap = new OverlapsIntegrity();
        overlap.setExpected(false);
        overlap.setGeomTypeRefA("my:line");

        // System.out.println("=========================================");
        Map map = new HashMap();
        try {
            map.put("my:line", mds.getFeatureSource("line"));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e1);
        }

        try {
            // System.out.println("Test Validate BBox");
            // assertFalse(overlap.validate(map, new Envelope(-1,2,-2,3), vr));
            assertFalse(overlap.validate(map, lineBounds, vr));
            // (RoadValidationResults)vr;
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }
}
