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
package org.geotools.coverageio.gdal;

import it.geosolutions.imageio.gdalframework.GDALCommonIIOImageMetadata;
import it.geosolutions.imageio.pam.PAMDataset;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.DataBuffer;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.datum.PixelInCell;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverageio.BaseGridCoverage2DReader;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.PixelTranslation;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.util.factory.Hints;

/**
 * Base class for GridCoverage data access, leveraging on GDAL Java bindings provided by the ImageIO-Ext project. See <a
 * href="http://imageio-ext.dev.java.net">ImageIO-Ext project</a>.
 *
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 */
public abstract class BaseGDALGridCoverage2DReader extends BaseGridCoverage2DReader implements GridCoverage2DReader {

    protected static final String DEFAULT_WORLDFILE_EXT = ".wld";

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(BaseGDALGridCoverage2DReader.class);

    private PAMDataset pamDataset;

    /**
     * Creates a new instance of a {@link BaseGDALGridCoverage2DReader}. I assume nothing about file extension.
     *
     * @param input Source object for which we want to build a {@link BaseGDALGridCoverage2DReader}.
     * @param hints Hints to be used by this reader throughout his life.
     * @param worldFileExtension the specific world file extension of the underlying format
     * @param formatSpecificSpi an instance of a proper {@code ImageReaderSpi}.
     */
    protected BaseGDALGridCoverage2DReader(
            Object input, final Hints hints, final String worldFileExtension, final ImageReaderSpi formatSpecificSpi)
            throws DataSourceException {
        super(input, hints, worldFileExtension, formatSpecificSpi);
    }

    /**
     * Setting Envelope, GridRange and CRS from the given {@code ImageReader}
     *
     * @param reader the {@code ImageReader} from which to retrieve metadata (if available) for setting properties
     */
    @Override
    protected void setCoverageProperties(ImageReader reader) throws IOException {
        // //
        //
        // Getting common metadata from GDAL
        //
        // //
        final IIOMetadata metadata = reader.getImageMetadata(0);
        if (!(metadata instanceof GDALCommonIIOImageMetadata)) {
            throw new DataSourceException(
                    "Unexpected error! Metadata should be an instance of the expected class: GDALCommonIIOImageMetadata.");
        }
        parseCommonMetadata((GDALCommonIIOImageMetadata) metadata, reader);

        // //
        //
        // Envelope and CRS checks
        //
        // //
        if (this.crs == null) {
            LOGGER.info("crs not found, proceeding with default crs");
            this.crs = AbstractGridFormat.getDefaultCRS();
        }

        if (this.originalEnvelope == null) {
            throw new DataSourceException("Unable to compute the envelope for this coverage");
        }

        // setting the coordinate reference system for the envelope, just to make sure we set it
        this.originalEnvelope.setCoordinateReferenceSystem(this.crs);
    }

    /**
     * Given a {@link GDALCommonIIOImageMetadata} metadata object, retrieves several properties to properly set
     * envelope, gridrange and crs.
     *
     * @param metadata a {@link GDALCommonIIOImageMetadata} metadata instance from where to search needed properties.
     */
    private void parseCommonMetadata(final GDALCommonIIOImageMetadata metadata, ImageReader reader) throws IOException {

        // ////////////////////////////////////////////////////////////////////
        //
        // setting CRS and Envelope directly from GDAL, if available
        //
        // ////////////////////////////////////////////////////////////////////
        // //
        //
        // 1) CRS
        //
        // //
        final Object tempCRS = this.hints.get(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM);
        if (tempCRS != null) {
            this.crs = (CoordinateReferenceSystem) tempCRS;
            LOGGER.log(Level.FINE, "Using default coordinate reference system ");
        } else {

            // //
            //
            // Default CRS override it all
            //
            // //
            // //
            //
            // Let the prj file override the internal representation for the undelrying source of
            // information.
            //
            // //
            parsePRJFile();
            // if there was not prj or the envelope could not be created easily, let's go with the
            // standard metadata.
            if (this.crs == null) {
                final String wkt = metadata.getProjection();

                if (wkt != null && !wkt.equalsIgnoreCase("")) {
                    try {
                        this.crs = CRS.parseWKT(wkt);
                        if (crs != null) {
                            crs = CRS.getHorizontalCRS(crs);
                            final Integer epsgCode = CRS.lookupEpsgCode(this.crs, true);
                            // Force the creation of the CRS directly from the
                            // retrieved EPSG code in order to prevent weird transformation
                            // between "same" CRSs having slight differences.
                            // TODO: cache epsgCode-CRSs
                            if (epsgCode != null) {
                                this.crs = CRS.decode("EPSG:" + epsgCode);
                            }
                        }
                    } catch (FactoryException fe) {
                        // unable to get CRS from WKT
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.log(
                                    Level.FINE, "Unable to get CRS from WKT contained in metadata. Looking for a PRJ.");
                        }
                        // reset crs
                        this.crs = null;
                    }
                }
            }
        }
        // //
        //
        // 2) Grid
        //
        // //
        if (this.originalGridRange == null)
            this.originalGridRange = new GridEnvelope2D(new Rectangle(0, 0, metadata.getWidth(), metadata.getHeight()));

        // NO DATA
        this.nodata = Optional.ofNullable(metadata.getNoDataValues())
                .filter(nd -> nd.length > 0)
                .map(nd -> nd[0])
                .orElse(null);
        if (nodata != null) {
            if (reader.getRawImageType(0).getSampleModel().getDataType() == DataBuffer.TYPE_FLOAT) {
                this.nodata = Double.valueOf(nodata.floatValue());
            }
        }

        // //
        //
        // 3) Envelope
        //
        // //
        //
        // Let's look for a world file first.
        //
        parseWorldFile();
        if (this.originalEnvelope == null) {
            final double[] geoTransform = metadata.getGeoTransformation();
            if (geoTransform != null && geoTransform.length == 6) {
                final AffineTransform tempTransform = new AffineTransform(
                        geoTransform[1],
                        geoTransform[4],
                        geoTransform[2],
                        geoTransform[5],
                        geoTransform[0],
                        geoTransform[3]);
                // ATTENTION: Gdal geotransform does not use the pixel is
                // centre convention like world files.
                if (this.originalEnvelope == null) {
                    try {
                        // Envelope setting
                        this.originalEnvelope = CRS.transform(
                                ProjectiveTransform.create(tempTransform),
                                new GeneralBounds((GridEnvelope2D) this.originalGridRange));
                    } catch (IllegalStateException | TransformException e) {
                        if (LOGGER.isLoggable(Level.WARNING)) {
                            LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
                        }
                    }
                }
                // Grid2World Transformation
                final double tr = -PixelTranslation.getPixelTranslation(PixelInCell.CELL_CORNER);
                tempTransform.translate(tr, tr);
                this.raster2Model = ProjectiveTransform.create(tempTransform);
            }
        }

        this.pamDataset = metadata.getPamDataset();
    }

    @Override
    public synchronized ResourceInfo getInfo(String subname) {
        if (this.resourceInfo != null) {
            return new GDALResourceInfo(this.resourceInfo);
        }

        GDALResourceInfo localInfo = new GDALResourceInfo();
        resourceInfo = localInfo;
        localInfo.setName(subname);
        localInfo.setBounds(new ReferencedEnvelope(this.getOriginalEnvelope()));
        localInfo.setCRS(getCoordinateReferenceSystem());
        localInfo.setTitle(subname);
        localInfo.setPAMDataset(pamDataset);

        return new GDALResourceInfo(this.resourceInfo);
    }
}
