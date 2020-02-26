/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.gtopo30;

import com.sun.media.imageio.stream.RawImageInputStream;
import com.sun.media.imageioimpl.plugins.raw.RawImageReader;
import com.sun.media.imageioimpl.plugins.raw.RawImageReaderSpi;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.renderable.ParameterBlock;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.stream.ImageInputStream;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.image.util.ImageUtilities;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.factory.ReferencingFactoryContainer;
import org.geotools.referencing.factory.ReferencingObjectFactory;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.util.NumberRange;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransformFactory;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.referencing.operation.TransformException;
import si.uom.SI;

/**
 * This class provides a GridCoverageReader for the GTopo30Format.
 *
 * @author Simone Giannecchini
 * @author jeichar
 * @author mkraemer
 */
public final class GTopo30Reader extends AbstractGridCoverage2DReader
        implements GridCoverage2DReader {

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(GTopo30Reader.class);
    /** Cached {@link ImageIO} SPI for creating instances of {@link RawImageReader}. */
    private static final RawImageReaderSpi imageIOSPI = new RawImageReaderSpi();

    private static final String dmext = ".dem";

    private static final String dhext = ".hdr";

    private static final String srext = ".src";

    private static final String shext = ".sch";

    private static final String stext = ".stx";

    private static final String prjext = ".prj";

    /** Dem data header URL */
    private final URL demURL;

    /** Dem statistics file URL */
    private final URL statsURL;

    /** Projection file. */
    private URL prjURL;

    /** The header for this GTOPO30 file. */
    private final GT30Header header;

    /** The file holding the statistics for this GTOPO30 file. */
    private final GT30Stats stats;

    /** The {@link URL} that points to the file to use. */
    private URL urlToUse;

    /** URL of the header file. */
    private final URL demHeaderURL;

    /**
     * GTopo30Reader constructor.
     *
     * @param source The source object (can be a File, an URL or a String representing a File or an
     *     URL).
     * @throws MalformedURLException if the URL does not correspond to one of the GTopo30 files
     * @throws DataSourceException if the given url points to an unrecognized file
     */
    public GTopo30Reader(final Object source) throws IOException {
        this(source, null);
    }

    /**
     * GTopo30Reader constructor.
     *
     * @param source The source object (can be a File, an URL or a String representing a File or an
     *     URL).
     * @throws MalformedURLException if the URL does not correspond to one of the GTopo30 files
     * @throws DataSourceException if the given url points to an unrecognized file
     */
    public GTopo30Reader(final Object source, final Hints hints) throws IOException {
        super(source, hints);

        if (source instanceof File) {
            urlToUse = ((File) source).toURI().toURL();
        } else if (source instanceof URL) {
            // we only allow files
            urlToUse = (URL) source;
        } else if (source instanceof String) {
            try {
                // is it a filename?
                urlToUse = new File((String) source).toURI().toURL();
            } catch (MalformedURLException e) {
                // is it a URL
                urlToUse = new URL((String) source);
            }
        } else {
            throw new IllegalArgumentException("Illegal input argument!");
        }
        this.source = source;
        coverageName = "gtopo30_coverage";
        // ///////////////////////////////////////////////////////////
        //
        // decoding source
        //
        // ///////////////////////////////////////////////////////////
        final String filename;

        filename = URLs.urlToFile(urlToUse).getName();

        boolean recognized = false;
        boolean extUpperCase = false;

        if (filename.endsWith(dmext)
                || filename.endsWith(dhext)
                || filename.endsWith(srext)
                || filename.endsWith(shext)
                || filename.endsWith(stext)
                || filename.endsWith(prjext)) {
            recognized = true;
        } else {

            if (filename.endsWith(dmext.toUpperCase())
                    || filename.endsWith(dhext.toUpperCase())
                    || filename.endsWith(srext.toUpperCase())
                    || filename.endsWith(shext.toUpperCase())
                    || filename.endsWith(stext.toUpperCase())
                    || filename.endsWith(prjext.toUpperCase())) {
                recognized = true;
                extUpperCase = true;
            }
        }

        if (!recognized) {
            throw new IOException("Unrecognized file (file extension doesn't match)");
        }

        this.coverageName = filename.substring(0, filename.length() - 4);
        demURL =
                new URL(
                        urlToUse,
                        this.coverageName + (!extUpperCase ? dmext : dmext.toUpperCase()));
        prjURL =
                new URL(
                        urlToUse,
                        this.coverageName + (!extUpperCase ? prjext : prjext.toUpperCase()));
        demHeaderURL =
                new URL(
                        urlToUse,
                        this.coverageName + (!extUpperCase ? dhext : dhext.toUpperCase()));
        statsURL =
                new URL(
                        urlToUse,
                        this.coverageName + (!extUpperCase ? stext : stext.toUpperCase()));

        // ///////////////////////////////////////////////////////////
        //
        // Reading header and statistics
        //
        // ///////////////////////////////////////////////////////////
        header = new GT30Header(demHeaderURL);
        // get information from the header
        originalGridRange =
                new GridEnvelope2D(new Rectangle(0, 0, header.getNCols(), header.getNRows()));
        stats = new GT30Stats(this.statsURL);

        // ///////////////////////////////////////////////////////////
        //
        // Build the coordinate system and the envelope
        //
        // ///////////////////////////////////////////////////////////
        final Object tempCRS = this.hints.get(Hints.DEFAULT_COORDINATE_REFERENCE_SYSTEM);
        if (tempCRS != null) {
            this.crs = (CoordinateReferenceSystem) tempCRS;
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, "Using forced coordinate reference system ");
        } else crs = initCRS();
        this.originalEnvelope = getBounds(crs);
        final GridToEnvelopeMapper geMapper =
                new GridToEnvelopeMapper(originalGridRange, originalEnvelope);
        geMapper.setPixelAnchor(PixelInCell.CELL_CENTER);
        this.raster2Model = geMapper.createTransform();

        // /////////////////////////////////////////////////////////////////////
        //
        // Compute source Resolution
        //
        // /////////////////////////////////////////////////////////////////////
        highestRes =
                getResolution(
                        originalEnvelope,
                        new Rectangle(0, 0, header.getNCols(), header.getNRows()),
                        crs);
        numOverviews = 0;
        overViewResolutions = null;

        //
        // ImageLayout
        //
        // Prepare temporary colorModel and sample model, needed to build the
        // RawImageInputStream
        final ColorModel cm =
                new ComponentColorModel(
                        ColorSpace.getInstance(ColorSpace.CS_GRAY),
                        false,
                        false,
                        Transparency.OPAQUE,
                        DataBuffer.TYPE_SHORT);
        // building the final image layout
        final Dimension tileSize =
                ImageUtilities.toTileSize(
                        new Dimension(originalGridRange.getSpan(0), originalGridRange.getSpan(1)));
        final SampleModel sm = cm.createCompatibleSampleModel(tileSize.width, tileSize.height);

        ImageLayout il =
                new ImageLayout(0, 0, originalGridRange.getSpan(0), originalGridRange.getSpan(1));
        il.setTileGridXOffset(0)
                .setTileGridYOffset(0)
                .setTileWidth(tileSize.width)
                .setTileHeight(tileSize.height);
        il.setColorModel(cm).setSampleModel(sm);
        setlayout(il);
    }

    /** @see org.opengis.coverage.grid.GridCoverageReader#getFormat() */
    public Format getFormat() {
        return new GTopo30Format();
    }

    /**
     * @see
     *     org.opengis.coverage.grid.GridCoverageReader#read(org.opengis.parameter.GeneralParameterValue[])
     */
    public GridCoverage2D read(final GeneralParameterValue[] params)
            throws java.lang.IllegalArgumentException, java.io.IOException {
        // /////////////////////////////////////////////////////////////////////
        //
        // do we have parameters to use for reading from the specified source
        //
        // /////////////////////////////////////////////////////////////////////
        GeneralEnvelope requestedEnvelope = null;
        Rectangle dim = null;
        OverviewPolicy overviewPolicy = null;
        // /////////////////////////////////////////////////////////////////////
        //
        // Checking params
        //
        // /////////////////////////////////////////////////////////////////////
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                final ParameterValue<?> param = (ParameterValue<?>) params[i];
                final String name = param.getDescriptor().getName().getCode();
                if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName().toString())) {
                    final GridGeometry2D gg = (GridGeometry2D) param.getValue();
                    requestedEnvelope = new GeneralEnvelope((Envelope) gg.getEnvelope2D());
                    dim = gg.getGridRange2D().getBounds();
                    continue;
                }
                if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName().toString())) {
                    overviewPolicy = (OverviewPolicy) param.getValue();
                    continue;
                }
            }
        }

        // /////////////////////////////////////////////////////////////////////
        //
        // Building the required coverage
        //
        // /////////////////////////////////////////////////////////////////////
        return getGridCoverage(requestedEnvelope, dim, overviewPolicy);
    }

    /**
     * Gets the bounding box of this datasource using the default speed of this datasource as set by
     * the implementer.
     *
     * @return The bounding box of the datasource or null if unknown and too expensive for the
     *     method to calculate.
     */
    private GeneralEnvelope getBounds(CoordinateReferenceSystem crs) throws IOException {
        GeneralEnvelope env = new GeneralEnvelope(new double[] {0, 0}, new double[] {0, 0});

        // preparing data for the envelope
        final double xULC = header.getULXMap();
        final double yULC = header.getULYMap();
        final double xDim = header.getXDim(); // dx
        final double yDim = header.getYDim(); // dy
        final int imageWidth = header.getNCols();
        final int imageHeight = header.getNRows();
        final double longMin;
        final double latMax;
        final double longMax;
        final double latMin;

        longMin = xULC - xDim / 2.0;
        latMax = yULC + yDim / 2.0;
        longMax = longMin + imageWidth * xDim;
        latMin = latMax - imageHeight * yDim;

        // longitude
        env.setRange(0, longMin, longMax);
        // latitude
        env.setRange(1, latMin, latMax);

        env.setCoordinateReferenceSystem(crs);

        return env;
    }

    /**
     * Retrieves a grid coverage based on the DEM assoicated to this gtopo coverage. The color
     * palette is fixed and there is no possibility for the final user to change it.
     *
     * @return the GridCoverage object
     * @throws DataSourceException if an error occurs
     */
    private GridCoverage2D getGridCoverage(
            GeneralEnvelope requestedEnvelope, Rectangle dim, OverviewPolicy overviewPolicy)
            throws IOException {
        int hrWidth = originalGridRange.getSpan(0);
        int hrHeight = originalGridRange.getSpan(1);

        // /////////////////////////////////////////////////////////////////////
        //
        // Setting subsampling factors with some checkings
        // 1) the subsampling factors cannot be zero
        // 2) the subsampling factors cannot be such that the w or h are zero
        //
        // /////////////////////////////////////////////////////////////////////
        final ImageReadParam readP = new ImageReadParam();
        final Integer imageChoice;
        try {
            imageChoice = setReadParams(overviewPolicy, readP, requestedEnvelope, dim);
        } catch (TransformException e) {
            throw new DataSourceException(e);
        }

        // /////////////////////////////////////////////////////////////////////
        //
        // Statistics
        //
        // /////////////////////////////////////////////////////////////////////
        final int max = stats.getMax();
        final int min = stats.getMin();

        // /////////////////////////////////////////////////////////////////////
        //
        // Preparing to load
        //
        // /////////////////////////////////////////////////////////////////////
        // trying to create a channel to the file to read
        final File file = URLs.urlToFile(demURL);
        @SuppressWarnings("PMD.CloseResource") // used in deferred loading
        final ImageInputStream iis = ImageIO.createImageInputStream(file);
        if (header.getByteOrder().compareToIgnoreCase("M") == 0) {
            iis.setByteOrder(ByteOrder.BIG_ENDIAN);
        } else {
            iis.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        }

        // Prepare temporary colorModel and sample model, needed to build the
        // RawImageInputStream
        final ImageLayout layout = getImageLayout();
        final ImageTypeSpecifier its =
                new ImageTypeSpecifier(layout.getColorModel(null), layout.getSampleModel(null));

        // Finally, build the image input stream
        @SuppressWarnings("PMD.CloseResource") // used in deferred loading
        final RawImageInputStream raw =
                new RawImageInputStream(
                        iis,
                        its,
                        new long[] {0},
                        new Dimension[] {new Dimension(hrWidth, hrHeight)});

        // building the final image layout
        final ImageLayout il =
                new ImageLayout(
                        0,
                        0,
                        hrWidth / readP.getSourceXSubsampling(),
                        hrHeight / readP.getSourceYSubsampling(),
                        0,
                        0,
                        layout.getTileWidth(null),
                        layout.getTileHeight(null),
                        layout.getSampleModel(null),
                        layout.getColorModel(null));

        // First operator: read the image
        final RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, il);
        final ParameterBlock pbjImageRead = new ParameterBlock();
        pbjImageRead.add(raw);
        pbjImageRead.add(imageChoice);
        pbjImageRead.add(Boolean.FALSE);
        pbjImageRead.add(Boolean.FALSE);
        pbjImageRead.add(Boolean.FALSE);
        pbjImageRead.add(null);
        pbjImageRead.add(null);
        pbjImageRead.add(readP);
        pbjImageRead.add(imageIOSPI.createReaderInstance());
        RenderedOp image = JAI.create("ImageRead", pbjImageRead, hints);

        // sample dimension for this coverage
        final GridSampleDimension band = getSampleDimension(max, min);

        // setting metadata
        final Map<String, Double> metadata = new HashMap<String, Double>();
        metadata.put("maximum", Double.valueOf(stats.getMax()));
        metadata.put("minimum", Double.valueOf(stats.getMin()));
        metadata.put("mean", Double.valueOf(stats.getAverage()));
        metadata.put("std_dev", Double.valueOf(stats.getStdDev()));
        metadata.put("nodata", Double.valueOf(-9999.0));

        // /////////////////////////////////////////////////////////////////////
        //
        // Creating coverage
        //
        // /////////////////////////////////////////////////////////////////////
        // cleaning name
        String coverageName = (new File(this.coverageName)).getName();
        final int extension = coverageName.lastIndexOf(".");
        if (extension != -1) {
            String ext = coverageName.substring(extension + 1);

            if ((dmext.compareToIgnoreCase(ext) == 0)
                    || (dhext.compareToIgnoreCase(ext) == 0)
                    || (srext.compareToIgnoreCase(ext) == 0)
                    || (shext.compareToIgnoreCase(ext) == 0)
                    || (stext.compareToIgnoreCase(ext) == 0)) {
                coverageName = coverageName.substring(0, extension);
            }
        }

        // return the coverage
        return (GridCoverage2D)
                coverageFactory.create(
                        coverageName,
                        image,
                        new GeneralEnvelope(originalEnvelope),
                        new GridSampleDimension[] {band},
                        null,
                        metadata);
    }

    /**
     * This method is responsible for the creation of the CRS for this GTOPO30. The possible options
     * are two, EPSG:4326 and POlar Stereographc. Inc ase an error occurs the default CRS is chosen.
     *
     * @return CoordinateReferenceSystem a CRS for this coverage.
     */
    @SuppressWarnings("deprecation")
    private CoordinateReferenceSystem initCRS() {
        BufferedReader reader = null;
        try {
            // getting a reader
            reader = new BufferedReader(new FileReader(URLs.urlToFile(prjURL)));

            // reading the first line to see if I need to read it all
            final StringBuilder buffer = new StringBuilder(reader.readLine());

            if (buffer != null) {
                String line = buffer.toString().trim();

                if (!line.endsWith("POLAR") && !line.endsWith("GEOGRAPHIC")) {
                    // in case I have a wkt string a need to read it all
                    while ((line = reader.readLine()) != null) buffer.append(line);
                }
            }
            // closing the reader
            reader.close();
            // getting the content
            final String crsDescription = buffer.toString().trim();
            final DefaultGeographicCRS geoCRS =
                    (DefaultGeographicCRS) CRS.decode("EPSG:4326", true);
            if (crsDescription != null) {
                if (crsDescription.endsWith("POLAR")) {
                    // we need to build a polar stereographic crs based on wgs
                    // 84. I am not so sure about the parameters I used. we
                    // should check them again

                    final CartesianCS cartCS =
                            org.geotools.referencing.cs.DefaultCartesianCS.PROJECTED;
                    final MathTransformFactory mtFactory =
                            ReferencingFactoryFinder.getMathTransformFactory(null);
                    final ParameterValueGroup parameters =
                            mtFactory.getDefaultParameters("Polar_Stereographic");
                    parameters.parameter("central_meridian").setValue(0.0);
                    parameters.parameter("latitude_of_origin").setValue(-71.0);
                    parameters.parameter("scale_factor").setValue(1);
                    parameters.parameter("false_easting").setValue(0.0);
                    parameters.parameter("false_northing").setValue(0.0);
                    final ReferencingFactoryContainer factories =
                            ReferencingFactoryContainer.instance(null);
                    final Map<String, String> properties =
                            Collections.singletonMap(
                                    "name", "WGS 84 / Antartic Polar Stereographic");

                    OperationMethod method = null;
                    final MathTransform mt =
                            factories
                                    .getMathTransformFactory()
                                    .createBaseToDerived(geoCRS, parameters, cartCS);
                    if (method == null) {
                        method = factories.getMathTransformFactory().getLastMethodUsed();
                    }
                    return ((ReferencingObjectFactory) factories.getCRSFactory())
                            .createProjectedCRS(properties, method, geoCRS, mt, cartCS);
                }

                if (crsDescription.endsWith("GEOGRAPHIC")) {
                    // in case I do not have a polar stereographic I build my
                    // own CRS using either the supplied wkt
                    // description or, in case none is supplied, a custom
                    // Geographic WGS84 with lon, lat axes.
                    return geoCRS;
                }
                return CRS.parseWKT(crsDescription);
            }
        } catch (IOException e) {
            // do nothing and return a default CRS but write down a message
            LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);

        } catch (FactoryException e) {
            // do nothing and return a default CRS but write down a message
            LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
        } finally {
            if (reader != null)
                try {
                    // freeing
                    reader.close();
                } catch (Exception e1) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.log(Level.FINE, e1.getLocalizedMessage(), e1);
                }
        }
        final CoordinateReferenceSystem crs = AbstractGridFormat.getDefaultCRS();
        LOGGER.info("PRJ file not found, proceeding with default crs");
        return crs;
    }

    /**
     * This method was implemented in order to reformat the input data to double to introduce NaN as
     * NoData instead of using -9999 since such a value for NoData is automatically recognized from
     * the SampleDimension code in order to build the No Data category. This method has been used
     * during tests in order to try the creation of a GTOPO30 file from a floating point coverage.
     *
     * @param max Max value in the input data
     * @param min Min Value in the input data
     * @return the reformatted image.
     */

    /**
     * The purpose of this method is to build the sample dimensions for this coverage.
     *
     * @param max Maximum value for this coverage.
     * @param min Minimum value for this coverage.
     * @return The newly created sample dimensions.
     */
    private static GridSampleDimension getSampleDimension(final int max, final int min) {
        // Create the SampleDimension, with colors and byte transformation
        // needed for visualization

        // unit of measure is meter
        Unit<Length> uom = SI.METRE;

        final Category nan =
                new Category(
                        Vocabulary.format(VocabularyKeys.NODATA),
                        new Color[] {new Color(0, 0, 0, 0)},
                        NumberRange.create(-9999, -9999));
        final GridSampleDimension band =
                new GridSampleDimension("digital-elevation", new Category[] {nan}, uom);

        return band;
    }
}
