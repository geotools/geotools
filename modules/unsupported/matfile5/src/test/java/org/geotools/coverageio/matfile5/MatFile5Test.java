/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.coverageio.matfile5;

import java.awt.RenderingHints;
import java.io.File;

import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverageio.BaseGridCoverage2DReader;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * 
 * Testing {@link MatFile5Reader}
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/matfile5/src/test/java/org/geotools/coverageio/matfile5/MatFile5Test.java $
 */
public final class MatFile5Test extends Assert {

    private final String directory = "";


    // public void test() throws Exception {
    //
    // // get a reader
    // File file = null;
    // file = new File(fileName);
    //
    // // Preparing an useful layout in case the image is striped.
    // final ImageLayout l = new ImageLayout();
    // l.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(512)
    // .setTileWidth(512);
    //
    // Hints hints = new Hints();
    // hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, l));
    //
    // final BaseGridCoverage2DReader reader = new SASTileReader(file, hints);
    //
    // // /////////////////////////////////////////////////////////////////////
    // //
    // // read once
    // //
    // // /////////////////////////////////////////////////////////////////////
    // GridCoverage2D gc = (GridCoverage2D) reader.read(null);
    // forceDataLoading(gc);
    //
    // // /////////////////////////////////////////////////////////////////////
    // //
    // // read again with subsampling and crop
    // //
    // // /////////////////////////////////////////////////////////////////////
    // final double cropFactor = 2.0;
    // final int oldW = gc.getRenderedImage().getWidth();
    // final int oldH = gc.getRenderedImage().getHeight();
    // final Rectangle range = reader.getOriginalGridRange().toRectangle();
    // final GeneralEnvelope oldEnvelope = reader.getOriginalEnvelope();
    // final GeneralEnvelope cropEnvelope = new GeneralEnvelope(new double[] {
    // oldEnvelope.getLowerCorner().getOrdinate(0)
    // + (oldEnvelope.getLength(0) / cropFactor),
    //
    // oldEnvelope.getLowerCorner().getOrdinate(1)
    // + (oldEnvelope.getLength(1) / cropFactor) },
    // new double[] { oldEnvelope.getUpperCorner().getOrdinate(0),
    // oldEnvelope.getUpperCorner().getOrdinate(1) });
    // cropEnvelope.setCoordinateReferenceSystem(reader.getCrs());
    //
    // final ParameterValue gg = (ParameterValue) ((AbstractGridFormat) reader
    // .getFormat()).READ_GRIDGEOMETRY2D.createValue();
    // gg.setValue(new GridGeometry2D(new GeneralGridRange(new Rectangle(0, 0,
    // (int) (range.width / 2.0 / cropFactor),
    // (int) (range.height / 2.0 / cropFactor))), cropEnvelope));
    // gc = (GridCoverage2D) reader.read(new GeneralParameterValue[] { gg });
    // forceDataLoading(gc);
    // }

    @Test
    public void testWrite() throws Exception {
        File fileDir = new File(directory);
        if (fileDir != null && fileDir.isDirectory()) {
            final File files[] = fileDir.listFiles();
            if (files != null) {
                final int numFiles = files.length;
                for (int i = 0; i < numFiles ; i++) {
                    final String path = files[i].getAbsolutePath()
                            .toLowerCase();
                    if (!path.endsWith("mat"))
                        continue;

                    // get a reader
                    File file = files[i];
                    final String fileName = file.getAbsolutePath();
                    final String fileOutputName = new StringBuilder(fileName
                            .substring(0, fileName.length() - 3)).append("tif")
                            .toString();
                    // Preparing an useful layout in case the image is striped.
                    final ImageLayout l = new ImageLayout();
                    l.setTileGridXOffset(0).setTileGridYOffset(0)
                            .setTileHeight(512).setTileWidth(512);

                    Hints hints = new Hints();
                    hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, l));

                    final BaseGridCoverage2DReader reader = new MatFile5Reader(
                            file, hints);

                    // /////////////////////////////////////////////////////////////////////
                    //
                    // read once
                    //
                    // /////////////////////////////////////////////////////////////////////
                    GridCoverage2D gc = (GridCoverage2D) reader.read(null);
                    
                    GeoTiffWriter writer = new GeoTiffWriter(new File(fileOutputName));
                    writer.write(gc, null);
                    
//                    ImageWriter writer = new TIFFImageWriterSpi().createWriterInstance();
//                    writer.setOutput(new FileImageOutputStream(new File(fileOutputName)));
//                    RenderedImage image = gc.getRenderedImage();
//                    writer.write(image);
                    
                    
                    writer.dispose();
                    gc.dispose(true);
                    reader.dispose();
                }
            }
        }
    }
}
