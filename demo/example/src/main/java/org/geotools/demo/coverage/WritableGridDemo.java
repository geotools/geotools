/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.geotools.demo.coverage;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * @author Michael Bedward
 */
public class WritableGridDemo {

    private static final int WIDTH = 512;

    public static void main(String[] args) {
        GridCoverageFactory gcf = CoverageFactoryFinder.getGridCoverageFactory(null);
        float[][] data = new float[WIDTH][WIDTH];
        GridCoverage2D cov = gcf.create("cov", data, new ReferencedEnvelope(0, WIDTH, 0, WIDTH, null));

        WritableGridCoverage2D writableCov = new WritableGridCoverage2D(cov);

        double centre = WIDTH / 2;
        for (int y = 0; y < WIDTH; y++) {
            double dy = (y - centre) / centre;
            double dy2 = dy*dy;
            for (int x = 0; x < WIDTH; x++) {
                double dx = (x - centre) / centre;
                double d = Math.sqrt(dx*dx + dy2);
                GridCoordinates2D coords = new GridCoordinates2D(x, y);
                writableCov.setValue(coords, (float) Math.sin(8 * Math.PI * d));
            }
        }
        
        writableCov.show();
    }
}
