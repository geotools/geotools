/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.grid;

import org.geotools.grid.ortholine.OrthoLineFeatureBuilder;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.ortholine.LineOrientation;
import org.geotools.grid.ortholine.OrthoLineDef;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 *
 *
 *
 * @source $URL$
 */
public class LinesTest {

    private static final ReferencedEnvelope BOUNDS =
            new ReferencedEnvelope(110, 150, -45, -10, DefaultGeographicCRS.WGS84);
    
    private static final double TOL = 1.0e-8;

    private static final int[] LEVELS = {3, 2, 1};
    private static final int[] SPACINGS = {10, 5, 2};

    private List<OrthoLineDef> lineDefs;
    
    @Before
    public void setup() {
        lineDefs = new ArrayList<OrthoLineDef>();
    }
    
    @Test
    public void horizontalLines() throws Exception {
        for (int i = 0; i < LEVELS.length; i++) {
            lineDefs.add(new OrthoLineDef(LineOrientation.HORIZONTAL, LEVELS[i], SPACINGS[i]));
        }
        
        SimpleFeatureSource featureSource = Lines.createOrthoLines(BOUNDS, lineDefs);
        assertLineFeatures(featureSource, LineOrientation.HORIZONTAL, BOUNDS.getMinY());
    }

    @Test
    public void verticalLines() throws Exception {
        for (int i = 0; i < LEVELS.length; i++) {
            lineDefs.add(new OrthoLineDef(LineOrientation.VERTICAL, LEVELS[i], SPACINGS[i]));
        }
        
        SimpleFeatureSource featureSource = Lines.createOrthoLines(BOUNDS, lineDefs);
        assertLineFeatures(featureSource, LineOrientation.VERTICAL, BOUNDS.getMinX());
    }
    
    @Test
    public void mixedLines() throws Exception {
        for (int i = 0; i < LEVELS.length; i++) {
            lineDefs.add(new OrthoLineDef(LineOrientation.HORIZONTAL, LEVELS[i], SPACINGS[i]));
            lineDefs.add(new OrthoLineDef(LineOrientation.VERTICAL, LEVELS[i], SPACINGS[i]));
        }

        SimpleFeatureSource featureSource = Lines.createOrthoLines(BOUNDS, lineDefs);
        assertLineFeatures(featureSource, LineOrientation.HORIZONTAL, BOUNDS.getMinY());
        assertLineFeatures(featureSource, LineOrientation.VERTICAL, BOUNDS.getMinX());
    }

    private void assertLineFeatures(SimpleFeatureSource featureSource, 
            LineOrientation lineOrientation, double minOrdinate) throws Exception {

        SimpleFeatureIterator iterator = featureSource.getFeatures().features();
        try {
            Object obj = null;
            while (iterator.hasNext()) {
                SimpleFeature lineFeature = iterator.next();
                
                Geometry geom = (Geometry) lineFeature.getDefaultGeometry();
                Coordinate[] coords = geom.getCoordinates();
                Coordinate c0 = coords[0];
                Coordinate c1 = coords[coords.length - 1];
                
                // Check that this line has the target orientation
                boolean isVertical = (Math.abs(c0.x - c1.x) < TOL);
                if (isVertical != (lineOrientation == LineOrientation.VERTICAL)) {
                    continue;
                }
                
                obj = lineFeature.getAttribute(OrthoLineFeatureBuilder.VALUE_ATTRIBUTE_NAME);
                assertTrue(obj instanceof Number);
                double value = ((Number) obj).doubleValue();
                
                obj = lineFeature.getAttribute(OrthoLineFeatureBuilder.LEVEL_ATTRIBUTE_NAME);
                assertTrue(obj instanceof Number);
                int level = ((Number) obj).intValue();

                int intOrdinate = (int) Math.round(value - minOrdinate);
                boolean ok = false;
                for (int i = 0; i < LEVELS.length; i++) {
                    if (intOrdinate % SPACINGS[i] == 0) {
                        assertEquals(LEVELS[i], level);
                        ok = true;
                        break;
                    }
                }
                
                if (!ok) {
                    fail(String.format("value = %.2f, level = %d", value, level));
                }
            }
        } finally {
            iterator.close();
        }
    }
}
