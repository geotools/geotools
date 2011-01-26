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
package org.geotools.coverage.io;

import it.geosolutions.imageio.imageioimpl.imagereadmt.DefaultCloneableImageReadParam;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.measure.unit.Unit;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;

import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.geometry.XRectangle2D;
import org.opengis.geometry.Envelope;
import org.opengis.metadata.Identifier;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

/**
 * A class to handle coverage requests to a reader.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 */
class BaseCoverageRequest {

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.coverageio");

    enum ReadType {
        DIRECT_READ, JAI_IMAGEREAD, UNSPECIFIED;

        public static ReadType getDefault() {
            return DIRECT_READ;
        }
    };

    private ReadType readType = ReadType.UNSPECIFIED;

    // ////////////////////////////////////////////////////////////////////////
    //
    // Base coverage properties
    //
    // ////////////////////////////////////////////////////////////////////////
    /** The base envelope read from file */
    private GeneralEnvelope coverageEnvelope = null;

    /** The base envelope 2D */
    private Envelope2D baseEnvelope2D;

    /** WGS84 envelope 2D for this coverage */
    private Envelope2D wgs84BaseEnvelope2D;

    /** The CRS for the coverage */
    private CoordinateReferenceSystem coverageCRS = null;

    /** The CRS related to the base envelope 2D */
    private CoordinateReferenceSystem spatialReferenceSystem2D;

    /** The Coverage name */
    private String coverageName;

    /** The base grid range for the coverage */
    private GeneralGridEnvelope baseGridRange;

    private double[] highestRes;

    private MathTransform raster2Model;

    private double[] scaleAndOffset;

    private double[] validRange;
    
    private double[] noDataValues;

    private String longName;

    private Unit unit;

    // ////////////////////////////////////////////////////////////////////////
    //
    // Request specific properties
    //
    // ////////////////////////////////////////////////////////////////////////

    /** The envelope requested */
    private GeneralEnvelope requestedEnvelope2D;

    /**
     * The adjusted requested envelope. It is the envelope obtained by properly
     * intersecting the requested envelope with the base envelope.
     */
    private GeneralEnvelope adjustedRequestedEnvelope2D;

    /** The desired overview Policy for this request */
    private OverviewPolicy overviewPolicy;

    /** The region where to fit the requested envelope */
    private Rectangle requestedRasterDimension;

    /** */
    private MathTransform2D requestedGridToWorld2D;

    private Hints hints;

    /**
     * Specify if a JAI ImageRead operation should use multithreading or not.
     * Note that multithreading is supported using a special JAI ImageReadMT
     * operation
     */
    private boolean useMultithreading = false;

    /**
     * The imageRead parameters involved in the coverage request (source region,
     * subsampling factors) which will be used by a coverageResponse to read
     * data.
     */
    private ImageReadParam imageReadParam = null;

    /** The source */
    private Rectangle sourceRasterRegion;

    /**
     * If set to {@code true} a transformation is requested to obtain the
     * desired data. This usually happens when the requested envelope will be
     * adjusted with intersection/crop of the base envelope.
     */
    private boolean needTransformation;

    /**
     * Set to {@code true} if this request will produce an empty result, and the
     * coverageResponse will produce a {@code null} coverage.
     */
    private boolean emptyRequest;

    /** The input data */
    private File input;

    /**
     * Set to {@code true} if the read operation needed to request data is a JAI
     * Image Read operation. Set to {@code false} if the read operation is a
     * direct {@code ImageReader.read(...)} call.
     */
    private boolean useJAI;

    /** An optional layout to be adopted */
    private ImageLayout layout = null;

    private int imageIndex;

    /**
     * Build a new {@code BaseCoverageRequest} given a set of input parameters.
     * 
     * @param params
     *                The {@code GeneralParameterValue}s to initialize this
     *                request
     * @param baseGridCoverage2DReader
     */
    public BaseCoverageRequest(GeneralParameterValue[] params,
            BaseGridCoverage2DReader baseGridCoverage2DReader) {

        // //
        //
        // Parsing parameters
        //
        // //
        if (params != null) {
            for (GeneralParameterValue gParam : params) {
                final ParameterValue<?> param = (ParameterValue<?>) gParam;
                final ReferenceIdentifier name = param.getDescriptor()
                        .getName();
                extractParameter(param, name);
            }
        }
        setBaseParameters(baseGridCoverage2DReader);
    }

    /**
     * Set the main parameters of this coverage request, getting basic
     * information from the reader.
     * 
     * @param reader
     *                a {@link BaseGridCoverage2DReader} from where to get basic
     *                coverage properties as well as basic parameters to be used
     *                by the incoming read operations.
     */
    private void setBaseParameters(final BaseGridCoverage2DReader reader) {
        this.input = reader.getInputFile();
        this.coverageEnvelope = reader.getCoverageEnvelope().clone();
        this.baseGridRange = reader.getCoverageGridRange();
        this.coverageCRS = reader.getCoverageCRS();
        this.coverageName = reader.getCoverageName();
        this.raster2Model = reader.getRaster2Model();
        this.highestRes = reader.getHighestRes();
        this.hints = reader.getHints().clone();
        this.imageIndex = reader.getImageIndex();
        this.scaleAndOffset = reader.getScaleAndOffset();
        this.validRange = reader.getValidRange();
        this.longName = reader.getLongName();
        this.noDataValues = reader.getNoDataValues();
        this.unit = reader.getUnit();
        if (this.layout != null) {
            this.hints
                    .add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, this.layout));
        }
    }

    /**
     * Set proper fields from the specified input parameter.
     * 
     * @param param
     *                the input {@code ParamaterValue} object
     * @param name
     *                the name of the parameter
     */
    private void extractParameter(ParameterValue<?> param, Identifier name) {

        // //
        //
        // GridGeometry2D parameter
        //
        // //
        if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
            final GridGeometry2D gg = (GridGeometry2D) param.getValue();
            if (gg == null) {
                return;
            }

            requestedGridToWorld2D = gg.getGridToCRS2D();
            requestedEnvelope2D = new GeneralEnvelope((Envelope) gg
                    .getEnvelope2D());
            requestedRasterDimension = gg.getGridRange2D().getBounds();
            return;
        }

        // //
        //
        // Use JAI ImageRead parameter
        //
        // //
        if (name.equals(AbstractGridFormat.USE_JAI_IMAGEREAD.getName())) {
            readType = param.booleanValue() ? ReadType.JAI_IMAGEREAD
                    : ReadType.DIRECT_READ;
            return;
        }

        // //
        //
        // Use Multithreading parameter
        //
        // //
        if (name.equals(BaseGridFormat.USE_MULTITHREADING.getName())) {
            useMultithreading = param.booleanValue();
            return;
        }

        // //
        //
        // Overview Policy parameter
        //
        // //
        if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName())) {
            overviewPolicy = (OverviewPolicy) param.getValue();
            return;
        }

        // //
        //
        // Suggested tile size parameter. It must be specified with
        // the syntax: "TileWidth,TileHeight" (without quotes where TileWidth
        // and TileHeight are integer values)
        //
        // //
        if (name.equals(BaseGridFormat.SUGGESTED_TILE_SIZE.getName())) {
            final String suggestedTileSize = (String) param.getValue();

            // Preliminary checks on parameter value
            if ((suggestedTileSize != null)
                    && (suggestedTileSize.trim().length() > 0)) {

                if (suggestedTileSize
                        .contains(BaseGridFormat.TILE_SIZE_SEPARATOR)) {
                    final String[] tilesSize = suggestedTileSize
                            .split(BaseGridFormat.TILE_SIZE_SEPARATOR);
                    if (tilesSize.length == 2) {
                        try {
                            // Getting suggested tile size
                            final int tileWidth = Integer.parseInt(tilesSize[0]
                                    .trim());
                            final int tileHeight = Integer
                                    .parseInt(tilesSize[1].trim());
                            layout = new ImageLayout();
                            layout.setTileGridXOffset(0).setTileGridYOffset(0)
                                    .setTileHeight(tileHeight).setTileWidth(
                                            tileWidth);
                        } catch (NumberFormatException nfe) {
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING, "Unable to parse "
                                        + "suggested tile size parameter");
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Compute this specific request settings all the parameters needed by a
     * visiting {@link BaseCoverageResponse} object.
     */
    public void prepare() {
        try {
            // //
            //
            // Set envelope and source region
            //
            // //
            sourceRasterRegion = new Rectangle();
            adjustedRequestedEnvelope2D = evaluateRequestedParams(
                    requestedEnvelope2D, sourceRasterRegion,
                    requestedRasterDimension, requestedGridToWorld2D);

            // //
            //
            // Set specific imageIO parameters: type of read operation,
            // imageReadParams
            //
            // //
            useJAI = requestUsesJaiImageread();
            if (useMultithreading) {
                imageReadParam = new DefaultCloneableImageReadParam();
            } else {
                imageReadParam = new ImageReadParam();
            }
            if (adjustedRequestedEnvelope2D != null) {
                final GeneralEnvelope req = (adjustedRequestedEnvelope2D
                        .isEmpty()) ? requestedEnvelope2D
                        : adjustedRequestedEnvelope2D;
                setReadParameters(overviewPolicy, imageReadParam, req,
                        requestedRasterDimension);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            adjustedRequestedEnvelope2D = null;
        } catch (TransformException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            adjustedRequestedEnvelope2D = null;
        }
        if (adjustedRequestedEnvelope2D != null && sourceRasterRegion != null
                && !sourceRasterRegion.isEmpty()) {
            imageReadParam.setSourceRegion(sourceRasterRegion);
        }

        // A transformation is requested in case the requested envelope has been
        // adjusted
        needTransformation = (adjustedRequestedEnvelope2D != null && !adjustedRequestedEnvelope2D
                .isEmpty());

        // In case the adjusted requested envelope is null, no intersection
        // between requested envelope and base envelope have been found. Hence,
        // no valid coverage will be loaded and the request should be considered
        // as producing an empty result.
        emptyRequest = adjustedRequestedEnvelope2D == null;

    }

    /**
     * Check the type of read operation which will be performed and return
     * {@code true} if a JAI imageRead operation need to be performed or
     * {@code false} if a simple read operation is needed.
     * 
     * @return {@code true} if the read operation will use a JAI ImageRead
     *         operation instead of a simple {@code ImageReader.read(...)} call.
     */
    private boolean requestUsesJaiImageread() {
        // //
        //
        // First of all check if the ReadType was already set as part the
        // request parameters
        //
        // //
        if (readType != ReadType.UNSPECIFIED)
            return readType == ReadType.JAI_IMAGEREAD;

        // //
        //
        // Ok, the request did not explicitly set the read type, let's check the
        // hints.
        //
        // //
        if (this.hints != null) {
            final Object o = this.hints.get(Hints.USE_JAI_IMAGEREAD);
            if (o != null) {
                return ((Boolean) o);
            }
        }

        // //
        //
        // Last chance is to use the default read type.
        //
        // //
        readType = ReadType.getDefault();
        return readType == ReadType.JAI_IMAGEREAD;
    }

    /**
     * Return a crop region from a specified envelope, leveraging on the grid to
     * world transformation.
     * 
     * @param envelope
     *                the crop envelope
     * @return a {@code Rectangle} representing the crop region.
     * @throws TransformException
     *                 in case a problem occurs when going back to raster space.
     */
    private Rectangle getCropRegion(GeneralEnvelope envelope)
            throws TransformException {
        final MathTransform gridToWorldTransform = getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        final MathTransform worldToGridTransform = gridToWorldTransform
                .inverse();
        final GeneralEnvelope rasterArea = CRS.transform(worldToGridTransform,
                envelope);
        final Rectangle2D ordinates = rasterArea.toRectangle2D();
        return ordinates.getBounds();
    }

    /**
     * Prepares the read parameters for doing an
     * {@link ImageReader#read(int, ImageReadParam)}.
     * 
     * It sets the passed {@link ImageReadParam} in terms of decimation on
     * reading using the provided requestedEnvelope and requestedDim to evaluate
     * the needed resolution.
     * 
     * @param overviewPolicy
     *                it can be one of
     *                {@link Hints#VALUE_OVERVIEW_POLICY_IGNORE},
     *                {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST},
     *                {@link Hints#VALUE_OVERVIEW_POLICY_QUALITY} or
     *                {@link Hints#VALUE_OVERVIEW_POLICY_SPEED}. It specifies
     *                the policy to compute the overviews level upon request.
     * @param readParam
     *                an instance of {@link ImageReadParam} for setting the
     *                subsampling factors.
     * @param requestedEnvelope
     *                the {@link GeneralEnvelope} we are requesting.
     * @param requestedDim
     *                the requested dimensions.
     * @throws IOException
     * @throws TransformException
     */
    protected void setReadParameters(OverviewPolicy overviewPolicy,
            ImageReadParam readParam, GeneralEnvelope requestedEnvelope,
            Rectangle requestedDim) throws IOException, TransformException {
        double[] requestedRes = null;

        // //
        //
        // Initialize overview policy
        //
        // //
        if (overviewPolicy == null) {
            overviewPolicy = OverviewPolicy.NEAREST;
        }

        // //
        //
        // default values for subsampling
        //
        // //
        readParam.setSourceSubsampling(1, 1, 0, 0);

        // //
        //
        // requested to ignore overviews
        //
        // //
        if (overviewPolicy.equals(OverviewPolicy.IGNORE)) {
            return;
        }

        // //
        //
        // Resolution requested. I am here computing the resolution required
        // by the user.
        //
        // //
        if (requestedEnvelope != null) {
            final GridToEnvelopeMapper geMapper = new GridToEnvelopeMapper();
            geMapper.setEnvelope(requestedEnvelope);
            geMapper.setGridRange(new GeneralGridEnvelope(requestedDim));
            geMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
            AffineTransform transform = geMapper.createAffineTransform();
            requestedRes = CoverageUtilities.getResolution(transform);

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "requested resolution: ("
                        + requestedRes[0] + "," + requestedRes[1] + ")");
            }
        }

        if (requestedRes == null) {
            return;
        }

        // ////////////////////////////////////////////////////////////////////
        //
        // DECIMATION ON READING 
        //
        // ////////////////////////////////////////////////////////////////////
        if ((requestedRes[0] > highestRes[0])
                || (requestedRes[1] > highestRes[1])) {
            setDecimationParameters(readParam, requestedRes);
        }
    }

    /**
     * Evaluates the requested envelope and builds a new adjusted version of it
     * fitting this coverage envelope.
     * 
     * <p>
     * While adjusting the requested envelope this methods also compute the
     * source region as a rectangle which is suitable for a successive read
     * operation with {@link ImageIO} to do crop-on-read.
     * 
     * 
     * @param requestedEnvelope
     *                is the envelope we are requested to load.
     * @param sourceRegion
     *                represents the area to load in raster space. This
     *                parameter cannot be null since it gets filled with
     *                whatever the crop region is depending on the
     *                <code>requestedEnvelope</code>.
     * @param requestedDim
     *                is the requested region where to load data of the
     *                specified envelope.
     * @param readGridToWorld
     *                the Grid to world transformation to be used
     * @return the adjusted requested envelope, empty if no requestedEnvelope
     *         has been specified, {@code null} in case the requested envelope
     *         does not intersect the coverage envelope or in case the adjusted
     *         requested envelope is covered by a too small raster region (an
     *         empty region).
     * 
     * @throws DataSourceException
     *                 in case something bad occurs
     */
    private GeneralEnvelope evaluateRequestedParams(
            GeneralEnvelope requestedEnvelope, Rectangle sourceRegion,
            Rectangle requestedDim, MathTransform2D readGridToWorld)
            throws DataSourceException {
        GeneralEnvelope adjustedRequestedEnvelope = new GeneralEnvelope(2);
        try {
            // ////////////////////////////////////////////////////////////////
            //
            // Check if we have something to load by intersecting the
            // requested envelope with the bounds of this data set.
            //
            // ////////////////////////////////////////////////////////////////
            if (requestedEnvelope != null) {
                initBaseEnvelope2D();
                final GeneralEnvelope requestedEnvelope2D = getRequestedEnvelope2D(requestedEnvelope);

                // ////////////////////////////////////////////////////////////
                //
                // INTERSECT ENVELOPES AND CROP Destination REGION
                //
                // ////////////////////////////////////////////////////////////
                adjustedRequestedEnvelope = getIntersection(
                        requestedEnvelope2D, requestedDim, readGridToWorld);
                if (adjustedRequestedEnvelope == null)
                    return null;

                // /////////////////////////////////////////////////////////////////////
                //
                // CROP SOURCE REGION
                //
                // /////////////////////////////////////////////////////////////////////
                sourceRegion.setRect(getCropRegion(adjustedRequestedEnvelope));
                if (sourceRegion.isEmpty()) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO,
                                "Too small envelope resulting in "
                                        + "empty cropped raster region");
                    }
                    return null;
                    // TODO: Future versions may define a 1x1 rectangle starting
                    // from the lower coordinate
                }
                if (!sourceRegion.intersects(baseGridRange.toRectangle())
                        || sourceRegion.isEmpty())
                    throw new DataSourceException("The crop region is invalid.");
                sourceRegion.setRect(sourceRegion.intersection(baseGridRange
                        .toRectangle()));

                if (LOGGER.isLoggable(Level.FINE)) {
                    StringBuffer sb = new StringBuffer(
                            "Adjusted Requested Envelope = ").append(
                            adjustedRequestedEnvelope.toString()).append("\n")
                            .append("Requested raster dimension = ").append(
                                    requestedDim.toString()).append("\n")
                            .append("Corresponding raster source region = ")
                            .append(sourceRegion.toString());
                    LOGGER.log(Level.FINE, sb.toString());
                }

            } else {
                // don't use the source region. Set an empty one
                sourceRegion.setBounds(new Rectangle(0, 0, Integer.MIN_VALUE,
                        Integer.MIN_VALUE));
            }
        } catch (TransformException e) {
            throw new DataSourceException(
                    "Unable to create a coverage for this source", e);
        } catch (FactoryException e) {
            throw new DataSourceException(
                    "Unable to create a coverage for this source", e);
        }
        return adjustedRequestedEnvelope;
    }

    /**
     * Return a 2D version of a requestedEnvelope
     * 
     * @param requestedEnvelope
     *                the {@code GeneralEnvelope} to be returned as 2D.
     * @return the 2D requested envelope
     * @throws FactoryException
     * @throws TransformException
     */
    private GeneralEnvelope getRequestedEnvelope2D(
            GeneralEnvelope requestedEnvelope) throws FactoryException,
            TransformException {
        GeneralEnvelope requestedEnvelope2D = null;
        final MathTransform transformTo2D;
        CoordinateReferenceSystem requestedEnvelopeCRS2D = requestedEnvelope
                .getCoordinateReferenceSystem();

        // //
        //
        // Find the transformation to 2D
        //
        // //
        if (requestedEnvelopeCRS2D.getCoordinateSystem().getDimension() != 2) {
            transformTo2D = CRS.findMathTransform(requestedEnvelopeCRS2D, CRS
                    .getHorizontalCRS(requestedEnvelopeCRS2D));
            requestedEnvelopeCRS2D = CRS
                    .getHorizontalCRS(requestedEnvelopeCRS2D);
        } else
            transformTo2D = IdentityTransform.create(2);

        if (!transformTo2D.isIdentity()) {
            requestedEnvelope2D = CRS.transform(transformTo2D,
                    requestedEnvelope);
            requestedEnvelope2D
                    .setCoordinateReferenceSystem(requestedEnvelopeCRS2D);
        } else
            requestedEnvelope2D = new GeneralEnvelope(requestedEnvelope);

        assert requestedEnvelopeCRS2D.getCoordinateSystem().getDimension() == 2;
        return requestedEnvelope2D;
    }

    /**
     * Initialize the 2D properties (CRS and Envelope) of this coverage
     * 
     * @throws FactoryException
     * @throws TransformException
     */
    private void initBaseEnvelope2D() throws FactoryException,
            TransformException {
        // //
        //
        // Get the original envelope 2d and its spatial reference system
        //
        // //
        if (spatialReferenceSystem2D == null) {
            if (coverageCRS.getCoordinateSystem().getDimension() != 2) {
                spatialReferenceSystem2D = CRS.getHorizontalCRS(coverageCRS);
                assert spatialReferenceSystem2D.getCoordinateSystem()
                        .getDimension() == 2;
                baseEnvelope2D = new Envelope2D(
                        CRS
                                .transform(
                                        CRS
                                                .findMathTransform(
                                                        coverageCRS,
                                                        (CoordinateReferenceSystem) spatialReferenceSystem2D),
                                        coverageEnvelope));
            } else {
                spatialReferenceSystem2D = coverageCRS;
                baseEnvelope2D = new Envelope2D(coverageEnvelope);
            }
        }
    }

    /**
     * Compute the WGS84 version for the envelope of this coverage
     * 
     * @throws FactoryException
     * @throws TransformException
     */
    private void initWGS84BaseEnvelope() throws FactoryException,
            TransformException {
        synchronized (this) {
            if (wgs84BaseEnvelope2D == null)
                wgs84BaseEnvelope2D = (Envelope2D) getEnvelopeAsWGS84(
                        coverageEnvelope, true);
        }
    }

    /**
     * Get a WGS84 envelope for the specified envelope. The get2D parameter
     * allows to specify if we need the returned coverage as an
     * {@code Envelope2D} or a more general {@code GeneralEnvelope} instance.
     * 
     * 
     * @param envelope
     * @param get2D
     *                if {@code true}, the requested envelope will be an
     *                instance of {@link Envelope2D}. If {@code false} it will
     *                be an instance of {@link GeneralEnvelope
     * @return a WGS84 envelope as {@link Envelope2D} in case of request for a
     *         2D WGS84 Envelope, or a {@link GeneralEnvelope} otherwise.
     * @throws FactoryException
     * @throws TransformException
     */
    private Envelope getEnvelopeAsWGS84(Envelope envelope, boolean get2D)
            throws FactoryException, TransformException {
        Envelope requestedWGS84;
        final MathTransform transformToWGS84;
        final CoordinateReferenceSystem crs = envelope
                .getCoordinateReferenceSystem();

        // //
        //
        // get a math transform to go to WGS84
        //
        // //
        if (!CRS.equalsIgnoreMetadata(crs, DefaultGeographicCRS.WGS84)) {
            transformToWGS84 = CRS.findMathTransform(crs,
                    DefaultGeographicCRS.WGS84, true);
        } else {
            transformToWGS84 = IdentityTransform.create(2);
        }

        // do we need to transform the requested envelope?
        if (!transformToWGS84.isIdentity()) {
            GeneralEnvelope env = CRS.transform(transformToWGS84, envelope);
            if (get2D) {
                requestedWGS84 = new Envelope2D(env);
                ((Envelope2D) requestedWGS84)
                        .setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
            } else {
                requestedWGS84 = env;
                ((GeneralEnvelope) requestedWGS84)
                        .setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
            }

        } else {
            if (get2D)
                return new Envelope2D(envelope);
            else
                return new GeneralEnvelope(envelope);
        }
        return requestedWGS84;
    }

    /**
     * This method is responsible for evaluating possible subsampling factors
     * once the best resolution level has been found in case we have support for
     * overviews, or starting from the original coverage in case there are no
     * overviews available.
     * 
     * @param readP
     *                the imageRead parameter to be set
     * @param requestedRes
     *                the requested resolutions from which to determine the
     *                decimation parameters.
     */
    protected void setDecimationParameters(ImageReadParam readP,
            double[] requestedRes) {
        {
            final int w = baseGridRange.getSpan(0);
            final int h = baseGridRange.getSpan(1);

            // ///////////////////////////////////////////////////////////////
            // DECIMATION ON READING
            // Setting subsampling factors with some checkings
            // 1) the subsampling factors cannot be zero
            // 2) the subsampling factors cannot be such that the w or h are 0
            // ///////////////////////////////////////////////////////////////
            if (requestedRes == null) {
                readP.setSourceSubsampling(1, 1, 0, 0);
            } else {
                int subSamplingFactorX = (int) Math.floor(requestedRes[0]
                        / highestRes[0]);
                subSamplingFactorX = (subSamplingFactorX == 0) ? 1
                        : subSamplingFactorX;

                while (((w / subSamplingFactorX) <= 0)
                        && (subSamplingFactorX >= 0))
                    subSamplingFactorX--;

                subSamplingFactorX = (subSamplingFactorX == 0) ? 1
                        : subSamplingFactorX;

                int subSamplingFactorY = (int) Math.floor(requestedRes[1]
                        / highestRes[1]);
                subSamplingFactorY = (subSamplingFactorY == 0) ? 1
                        : subSamplingFactorY;

                while (((h / subSamplingFactorY) <= 0)
                        && (subSamplingFactorY >= 0))
                    subSamplingFactorY--;

                subSamplingFactorY = (subSamplingFactorY == 0) ? 1
                        : subSamplingFactorY;

                readP.setSourceSubsampling(subSamplingFactorX,
                        subSamplingFactorY, 0, 0);
            }
        }
    }

    /**
     * Retrieves the original grid to world transformation for this
     * {@link AbstractGridCoverage2DReader}.
     * 
     * @param pixInCell
     *                specifies the datum of the transformation we want.
     * @return the original grid to world transformation
     */
    public MathTransform getOriginalGridToWorld(final PixelInCell pixInCell) {
        // we do not have to change the pixel datum
        if (pixInCell == PixelInCell.CELL_CENTER)
            return raster2Model;

        // we do have to change the pixel datum
        if (raster2Model instanceof AffineTransform) {
            final AffineTransform tr = new AffineTransform(
                    (AffineTransform) raster2Model);
            tr.concatenate(AffineTransform.getTranslateInstance(-0.5, -0.5));
            return ProjectiveTransform.create(tr);
        }
        if (raster2Model instanceof IdentityTransform) {
            final AffineTransform tr = new AffineTransform(1, 0, 0, 1, 0, 0);
            tr.concatenate(AffineTransform.getTranslateInstance(-0.5, -0.5));
            return ProjectiveTransform.create(tr);
        }
        throw new IllegalStateException(
                "This grid to world transform is invalud!");
    }

    /**
     * Returns the intersection between the base envelope and the requested
     * envelope.
     * 
     * @param requestedEnvelope2D
     *                the requested 2D envelope to be intersected with the base
     *                envelope.
     * @param requestedDim
     *                is the requested region where to load data of the
     *                specified envelope.
     * @param readGridToWorld
     *                the Grid to world transformation to be used in read
     * @return the resulting intersection of envelopes. In case of empty
     *         intersection, this method is allowed to return {@code null}
     * @throws TransformException
     * @throws FactoryException
     */
    private GeneralEnvelope getIntersection(
            GeneralEnvelope requestedEnvelope2D, Rectangle requestedDim,
            MathTransform2D readGridToWorld) throws TransformException,
            FactoryException {

        GeneralEnvelope adjustedRequestedEnvelope = new GeneralEnvelope(2);
        final CoordinateReferenceSystem requestedEnvelopeCRS2D = requestedEnvelope2D
                .getCoordinateReferenceSystem();
        boolean tryWithWGS84 = false;

        try {
            // convert the requested envelope 2D to this coverage native crs.
            MathTransform transform = null;
            if (!CRS.equalsIgnoreMetadata(requestedEnvelopeCRS2D,
                    spatialReferenceSystem2D))
                transform = CRS.findMathTransform(requestedEnvelopeCRS2D,
                        spatialReferenceSystem2D, true);
            // now transform the requested envelope to source crs
            if (transform != null && !transform.isIdentity())
                adjustedRequestedEnvelope = CRS.transform(transform,
                        requestedEnvelope2D);
            else
                adjustedRequestedEnvelope.setEnvelope(requestedEnvelope2D);

            // intersect the requested area with the bounds of this
            // layer in native crs
            if (!adjustedRequestedEnvelope.intersects(baseEnvelope2D, true))
                return null;
            adjustedRequestedEnvelope.intersect(baseEnvelope2D);
            adjustedRequestedEnvelope
                    .setCoordinateReferenceSystem(spatialReferenceSystem2D);

            // //
            //
            // transform the intersection envelope from the destination world
            // space to the requested raster space
            //
            // //
            final Envelope requestedEnvelopeCropped = (transform != null && !transform
                    .isIdentity()) ? CRS.transform(transform.inverse(),
                    adjustedRequestedEnvelope) : adjustedRequestedEnvelope;
            final Rectangle2D ordinates = CRS.transform(
                    readGridToWorld.inverse(), requestedEnvelopeCropped)
                    .toRectangle2D();
            final GeneralGridEnvelope finalRange = new GeneralGridEnvelope(ordinates
                    .getBounds());
            final Rectangle tempRect = finalRange.toRectangle();
            // check that we stay inside the source rectangle
            XRectangle2D.intersect(tempRect, requestedDim, tempRect);
            requestedDim.setRect(tempRect);
        } catch (TransformException te) {
            // something bad happened while trying to transform this
            // envelope. let's try with wgs84
            tryWithWGS84 = true;
        } catch (FactoryException fe) {
            // something bad happened while trying to transform this
            // envelope. let's try with wgs84
            tryWithWGS84 = true;
        }

        // //
        //
        // If this does not work, we go back to reproject in the wgs84
        // requested envelope
        //              
        // //
        if (tryWithWGS84) {
            initWGS84BaseEnvelope();
            final GeneralEnvelope requestedEnvelopeWGS84 = (GeneralEnvelope) getEnvelopeAsWGS84(
                    requestedEnvelope2D, false);

            // checking the intersection in wgs84
            if (!requestedEnvelopeWGS84.intersects(wgs84BaseEnvelope2D, true))
                return null;

            // intersect
            adjustedRequestedEnvelope = new GeneralEnvelope(
                    requestedEnvelopeWGS84);
            adjustedRequestedEnvelope.intersect(wgs84BaseEnvelope2D);
            adjustedRequestedEnvelope = CRS.transform(CRS.findMathTransform(
                    requestedEnvelopeWGS84.getCoordinateReferenceSystem(),
                    spatialReferenceSystem2D, true), adjustedRequestedEnvelope);
            adjustedRequestedEnvelope
                    .setCoordinateReferenceSystem(spatialReferenceSystem2D);

        }
        return adjustedRequestedEnvelope;
    }

    /**
     * @return
     * @uml.property name="hints"
     */
    public Hints getHints() {
        return hints;
    }

    /**
     * @return
     * @uml.property name="useMultithreading"
     */
    public boolean useMultithreading() {
        return useMultithreading;
    }

    /**
     * @return
     * @uml.property name="imageReadParam"
     */
    public ImageReadParam getImageReadParam() {
        return imageReadParam;
    }

    /**
     * @return
     * @uml.property name="useJAI"
     */
    public boolean useJAI() {
        return useJAI;
    }

    /**
     * @return
     * @uml.property name="needTransformation"
     */
    public boolean needTransformation() {
        return needTransformation;
    }

    /**
     * @return
     * @uml.property name="emptyRequest"
     */
    public boolean isEmptyRequest() {
        return emptyRequest;
    }

    /**
     * @return
     * @uml.property name="input"
     */
    public File getInput() {
        return input;
    }

    /**
     * @return
     * @uml.property name="raster2Model"
     */
    public MathTransform getRaster2Model() {
        return raster2Model;
    }

    /**
     * @return
     * @uml.property name="coverageEnvelope"
     */
    public GeneralEnvelope getCoverageEnvelope() {
        return coverageEnvelope;
    }

    /**
     * @return
     * @uml.property name="coverageCRS"
     */
    public CoordinateReferenceSystem getCoverageCRS() {
        return coverageCRS;
    }

    /**
     * @return
     * @uml.property name="coverageName"
     */
    public String getCoverageName() {
        return coverageName;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public void setImageIndex(int imageIndex) {
        this.imageIndex = imageIndex;
    }

    public void setScaleAndOffset(double[] scaleAndOffset) {
        this.scaleAndOffset = scaleAndOffset.clone();
    }

    public double[] getScaleAndOffset() {
        return scaleAndOffset;
    }

    public double[] getValidRange() {
        return validRange;
    }

    public void setValidRange(double[] validRange) {
        this.validRange = validRange.clone();
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public void setNoDataValues(double[] noDataValues) {
        this.noDataValues = noDataValues;
    }

    public double[] getNoDataValues() {
        return noDataValues;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

}