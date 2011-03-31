package org.geotools.coverage;

import java.awt.image.BufferedImage;
import java.io.File;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Coverage Examples used for sphinx documentation.
 * 
 * @author Jody Garnett
 */
public class CoverageExamples {

void exampleGridFormat() throws Exception {
    
    // exampleGridFormat start
    File file = new File("test.tiff");
    
    AbstractGridFormat format = GridFormatFinder.findFormat(file);
    AbstractGridCoverage2DReader reader = format.getReader(file);
    
    GridCoverage2D coverage = reader.read(null);
    // exampleGridFormat end
}

void exampleGridCoverageFactory() throws Exception {
 
    ReferencedEnvelope referencedEnvelope = null;
    BufferedImage bufferedImage = null;
    // exampleGridCoverageFactory start
    GridCoverageFactory factory = new GridCoverageFactory();
    GridCoverage2D coverage = factory.create("GridCoverage", bufferedImage, referencedEnvelope);
    // exampleGridCoverageFactory end
}
}
