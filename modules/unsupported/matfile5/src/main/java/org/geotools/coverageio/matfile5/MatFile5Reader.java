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
 */
package org.geotools.coverageio.matfile5;

import it.geosolutions.imageio.matfile5.sas.SASTileImageReaderSpi;
import it.geosolutions.imageio.matfile5.sas.SASTileMetadata;
import it.geosolutions.imageio.matfile5.sas.SASTileMetadata.Channel;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverageio.BaseGridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Base class for GridCoverage data access, leveraging on MatFile reader
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 * @since 2.7.x
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/matfile5/src/main/java/org/geotools/coverageio/matfile5/MatFile5Reader.java $
 */
public class MatFile5Reader extends BaseGridCoverage2DReader implements GridCoverageReader {

    private final static String worldFileExt = ".wld";

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(MatFile5Reader.class.toString());

    /**
     * Creates a new instance of a {@link MatFile5Reader}. I assume nothing
     * about file extension.
     * 
     * @param input
     *                Source object for which we want to build a
     *                {@link MatFile5Reader}.
     * @param hints
     *                Hints to be used by this reader throughout his life.
     * @throws DataSourceException
     */
    public MatFile5Reader(Object input, final Hints hints)
            throws DataSourceException {
        super(input, hints, worldFileExt, new SASTileImageReaderSpi());
    }

    /**
     * Setting Envelope, GridRange and CRS from the given {@code ImageReader}
     * 
     * @param reader
     *                the {@code ImageReader} from which to retrieve metadata
     *                (if available) for setting properties
     * @throws IOException
     */
    protected void setCoverageProperties(ImageReader reader) throws IOException {
        // //
        //
        // Getting metadata
        //
        // //
        final IIOMetadata metadata = reader.getImageMetadata(0);
        final boolean isSAS= metadata instanceof SASTileMetadata;
        
        // //
        //
        // get the CRS INFO
        //
        // //
        final Object tempCRS = this.hints.get(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM);
        if (tempCRS != null) {
            setCoverageCRS((CoordinateReferenceSystem) tempCRS);
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Using forced coordinate reference system "
                        + crs.toWKT());
        } else {
           
            if(!isSAS)
            	parsePRJFile();
            else{
            	getPropertiesFromMetadata((SASTileMetadata) metadata);
            }
            if (getCoverageCRS() == null) {
                parsePRJFile();
            }
            
            if (getCoverageCRS() == null)
                throw new DataSourceException("Coordinate Reference System is not available");            
        }

        // //
        //
        // If no sufficient information have been found to set the
        // envelope, try other ways, such as looking for a WorldFile
        //
        // //
        if(!isSAS)
        	parseWorldFile();
        else{
	        if (getCoverageEnvelope() == null) {
	            parseWorldFile();
	            if (getCoverageEnvelope() == null) {
	                throw new DataSourceException(
	                        "Unavailable envelope for this coverage");
	            }
	        }
        }

        // setting the coordinate reference system for the envelope
        getCoverageEnvelope().setCoordinateReferenceSystem(getCoverageCRS());

        // Additional settings due to "final" methods getOriginalXXX
        originalEnvelope = getCoverageEnvelope();
        originalGridRange = getCoverageGridRange();
        crs = getCoverageCRS();
    }

    /**
     * @param metadata
     * @throws IOException
     */
    private void getPropertiesFromMetadata(SASTileMetadata metadata)
            throws IOException {

        // ////////////////////////////////////////////////////////////////////
        //
        // setting CRS and Envelope
        //
        // ////////////////////////////////////////////////////////////////////
        // //
        //
        // Grid
        //
        // //
        if (getCoverageGridRange() == null)
            setCoverageGridRange(new GridEnvelope2D(new Rectangle(0, 0,
                    metadata.getXPixels(), metadata.getYPixels())));

        // //
        // 
        // Envelope
        //
        // //
        final double longitude = metadata.getLongitude();
        final double latitude = metadata.getLatitude();
        final Channel channel = metadata.getChannel();
        final double orientation = metadata.getOrientation();
        final double xDim = metadata.getXPixelDim();
        final double yDim = metadata.getYPixelDim();
        AffineTransform gt = null;
        try {
            final CoordinateReferenceSystem crs = UTMUtilities.getProperUTM(longitude, latitude);
            setCoverageCRS(crs);
            MathTransform transform = CRS.findMathTransform(org.geotools.referencing.crs.DefaultGeographicCRS.WGS84, crs);
            double[] dest = new double[2];
            transform.transform(new double[] { longitude, latitude }, 0, dest, 0, 1);
            double easting = dest[0];
            double northing = dest[1];
            final double alpha = (orientation * Math.PI) / 180;
            final double cosAlpha = Math.cos(alpha);
            final double sinAlpha = Math.sin(alpha);

            final double[] dx = new double[2];
            final double[] dy = new double[2];

            dx[0] = sinAlpha * xDim;
            dx[1] = channel == Channel.PORT ? -cosAlpha * yDim : cosAlpha * yDim;

            dy[0] = cosAlpha * xDim;
            dy[1] = channel == Channel.PORT ? sinAlpha * yDim : sinAlpha * -yDim;
            
            GeneralMatrix gm = new GeneralMatrix(3);
            gm.setElement(0, 0, dx[0]);
            gm.setElement(0, 1, dx[1]);
            gm.setElement(1, 0, dy[0]);
            gm.setElement(1, 1, dy[1]);
            gm.setElement(0, 2, easting);
            gm.setElement(1, 2, northing);
            gt = gm.toAffineTransform2D();
            
            this.raster2Model = ProjectiveTransform.create(gt);
            MathTransform tempTransform = PixelTranslation.translate(
                    raster2Model, PixelInCell.CELL_CENTER,
                    PixelInCell.CELL_CORNER);

            if (getCoverageEnvelope() == null) {
                // Envelope setting
                final Envelope gridRange = new GeneralEnvelope(
                        getCoverageGridRange().getBounds2D());
                final GeneralEnvelope coverageEnvelope = CRS.transform(
                        tempTransform, gridRange);

                setCoverageEnvelope(coverageEnvelope);

            }
        } catch (IllegalStateException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            }
        } catch (TransformException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            }
        } catch (FactoryException e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            }
        }
    }
   

    public Format getFormat() {
        return new MatFile5Format();
    }
}
