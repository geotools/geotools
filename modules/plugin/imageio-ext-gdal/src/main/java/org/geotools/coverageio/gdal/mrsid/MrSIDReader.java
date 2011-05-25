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
package org.geotools.coverageio.gdal.mrsid;

import it.geosolutions.imageio.plugins.mrsid.MrSIDImageReaderSpi;

import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageReader;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;

/**
 * This class can read a MrSID data source and create a {@link GridCoverage2D}
 * from the data.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 *
 * @source $URL$
 */
public final class MrSIDReader extends BaseGDALGridCoverage2DReader implements
        GridCoverageReader {
    /** Logger. */
    @SuppressWarnings("unused")
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(MrSIDReader.class);

    private final static String worldFileExt = ".sdw";

    /**
     * Creates a new instance of a {@link MrSIDReader}. I assume nothing about
     * file extension.
     * 
     * @param input
     *                Source object for which we want to build a
     *                {@link MrSIDReader}.
     * @throws DataSourceException
     */
    public MrSIDReader(Object input) throws DataSourceException {
        this(input, null);
    }

    /**
     * Creates a new instance of a {@link MrSIDReader}. I assume nothing about
     * file extension.
     * 
     * @param input
     *                Source object for which we want to build a
     *                {@link MrSIDReader}.
     * @param hints
     *                Hints to be used by this reader throughout his life.
     * @throws DataSourceException
     */
    public MrSIDReader(Object input, final Hints hints)
            throws DataSourceException {
        super(input, hints, worldFileExt, new MrSIDImageReaderSpi());
    }

    /**
     * Setting Envelope, GridRange and CRS from the given {@code ImageReader}
     * 
     * @param reader
     *                the {@code ImageReader} from which to retrieve metadata
     *                (if available) for setting properties
     * @throws IOException
     */
    @Override
    protected void setCoverageProperties(ImageReader reader) throws IOException {

        // //
        //
        // Due to a MrSID encoder (old version) bug , georeferencing information
        // could be wrong. Hence we first attempt to get information using
        // alternative ways (worldfile, common metadata) and then try setting
        // empty fields with specific metadata.
        //
        // //
        super.setCoverageProperties(reader);

        if (this.originalGridRange != null &&this.crs != null
                && originalEnvelope != null)
            return;
        
        // Uncomment this section in case we stop using the default CRS.
        // The CRS information coming from GDAL are computed from the
        // same MrSID metadata.
        
//    final IIOMetadata metadata = reader.getImageMetadata(0);
//    if (!(metadata instanceof GDALCommonIIOImageMetadata)) {
//        throw new DataSourceException(
//                "Unexpected error! Metadata should be an instance of the expected class:"
//                        + " GDALCommonIIOImageMetadata.");
//    }
//    final GDALCommonIIOImageMetadata gridMetadata = (GDALCommonIIOImageMetadata) metadata;
//
//    // getting metadata
//    final Node root = gridMetadata
//            .getAsTree(MrSIDIIOImageMetadata.mrsidImageMetadataName);
//
//    // //
//    //
//    // getting Image Properties
//    //
//    // //
//    Node child = root.getFirstChild();
//    NamedNodeMap attributes = child.getAttributes();
//    if (getCoverageGridRange() == null) {
//        final String sWidth = attributes.getNamedItem("IMAGE__WIDTH")
//                .getNodeValue();
//        final String sHeight = attributes.getNamedItem("IMAGE__HEIGHT")
//                .getNodeValue();
//
//        if ((sHeight != null) && (sWidth != null)
//                && !(sWidth.trim().equalsIgnoreCase(""))
//                && !(sHeight.trim().equalsIgnoreCase(""))) {
//            final int width = Integer.parseInt(sWidth);
//            final int height = Integer.parseInt(sHeight);
//            setCoverageGridRange(new GeneralGridRange(new Rectangle(0, 0,
//                    width, height)));
//        }
//    }
//    // //
//    //
//    // getting GeoReferencing Properties
//    //
//    // //
//    child = child.getNextSibling();
//    attributes = child.getAttributes();
//
//    if (getCoverageEnvelope() == null) {
//        final String xResolution = attributes.getNamedItem(
//                "IMAGE__X_RESOLUTION").getNodeValue();
//        final String yResolution = attributes.getNamedItem(
//                "IMAGE__Y_RESOLUTION").getNodeValue();
//        final String xyOrigin = attributes.getNamedItem("IMAGE__XY_ORIGIN")
//                .getNodeValue();
//
//        if ((xResolution != null) && (yResolution != null)
//                && (xyOrigin != null)
//                && !(xResolution.trim().equalsIgnoreCase(""))
//                && !(yResolution.trim().equalsIgnoreCase(""))
//                && !(xyOrigin.trim().equalsIgnoreCase(""))) {
//            double cellsizeX = Double.parseDouble(xResolution);
//            double cellsizeY = Double.parseDouble(yResolution);
//            final String[] origins = xyOrigin.split(",");
//            double xul = Double.parseDouble(origins[0]);
//            double yul = Double.parseDouble(origins[1]);
//
//            xul -= (cellsizeX / 2d);
//            yul -= (cellsizeY / 2d);
//
//            final double xll = xul;
//            final double yur = yul;
//            final int width = getCoverageGridRange().getLength(0);
//            final int height = getCoverageGridRange().getLength(1);
//            final double xur = xul + (cellsizeX * width);
//            final double yll = yul - (cellsizeY * height);
//            setCoverageEnvelope(new GeneralEnvelope(
//                    new double[] { xll, yll }, new double[] { xur, yur }));
//        }
//    }
//    // Retrieving projection Information
//    if (getCoverageCRS() == null) {
//        Node attribute = attributes.getNamedItem("IMG__WKT");
//
//        if (attribute != null) {
//            String wkt = attribute.getNodeValue();
//
//            if ((wkt != null) && (wkt.trim().length() > 0)) {
//                try {
//                    setCoverageCRS(CRS.parseWKT(wkt));
//                } catch (FactoryException fe) {
//                    if (LOGGER.isLoggable(Level.FINE)) {
//                        LOGGER.log(Level.FINE, "Unable to get CRS from"
//                                + " WKT contained in metadata."
//                                + " Looking for a PRJ.");
//                    }
//
//                    // unable to get CRS from WKT
//                    setCoverageCRS(null);
//                }
//            }
//        }
//    }
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
     */
    public Format getFormat() {
        return new MrSIDFormat();
    }
}
