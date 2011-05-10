/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.grid;

import java.util.Arrays;
import java.util.List;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.ortholine.LineOrientation;
import org.geotools.grid.ortholine.OrthoLineControl;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.junit.Test;

/**
 *
 * @author michael
 */
public class LinesTest {

    private static final ReferencedEnvelope BOUNDS =
            new ReferencedEnvelope(110, 120, -45, -10, DefaultGeographicCRS.WGS84);

    @Test
    public void horizontalLines() throws Exception {

        List<OrthoLineControl> controls = Arrays.asList(
                new OrthoLineControl(LineOrientation.HORIZONTAL, 3, 10.0),
                new OrthoLineControl(LineOrientation.HORIZONTAL, 2, 5.0),
                new OrthoLineControl(LineOrientation.HORIZONTAL, 1, 2.0) );
        
        SimpleFeatureSource featureSource = Lines.createLines(BOUNDS, controls);
        SimpleFeatureIterator iterator = featureSource.getFeatures().features();
        try {
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        } finally {
            iterator.close();
        }
    }
}
