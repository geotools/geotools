package org.geotools.coverage;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Coverage Examples used for sphinx documentation.
 * 
 * @author Jody Garnett
 */
public class CoverageExamples {

@SuppressWarnings("unused")
void exampleGridFormat() throws Exception {
    
    // exampleGridFormat start
    File file = new File("test.tiff");
    
    AbstractGridFormat format = GridFormatFinder.findFormat(file);
    AbstractGridCoverage2DReader reader = format.getReader(file);
    
    GridCoverage2D coverage = reader.read(null);
    // exampleGridFormat end
}

@SuppressWarnings("unused")
void exampleGridCoverageFactory() throws Exception {
    
    ReferencedEnvelope referencedEnvelope = null;
    BufferedImage bufferedImage = null;
    // exampleGridCoverageFactory start
    GridCoverageFactory factory = new GridCoverageFactory();
    GridCoverage2D coverage = factory.create("GridCoverage", bufferedImage, referencedEnvelope);
    // exampleGridCoverageFactory end
}


@SuppressWarnings("unused")
void exampleGridCoverageUsing() throws Exception {
    File file = new File("test.tiff");
    AbstractGridFormat format = GridFormatFinder.findFormat(file);
    AbstractGridCoverage2DReader reader = format.getReader(file);
    // exampleGridCoverageUsing start
    GridCoverage2D coverage = reader.read(null);
    
    CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D();
    Envelope env = coverage.getEnvelope();
    RenderedImage image = coverage.getRenderedImage();
    // exampleGridCoverageUsing end
}

void exampleGridCoverageDirect() throws Exception {
    double x =0;
    double y = 0;
    CoordinateReferenceSystem crs = null;
    
    File file = new File("test.tiff");
    AbstractGridFormat format = GridFormatFinder.findFormat(file);
    AbstractGridCoverage2DReader reader = format.getReader(file);
    // exampleGridCoverageDirect start
    GridCoverage2D coverage = reader.read(null);
    
    // direct access
    DirectPosition position = new DirectPosition2D( crs, x, y);
    
    double[] sample = (double[]) coverage.evaluate( position ); // assume double
    
    // resample with the same array
    sample = coverage.evaluate( position, sample );
    // exampleGridCoverageDirect end
}

}
