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
package org.geotools.coverageio;

import it.geosolutions.imageio.imageioimpl.imagereadmt.DefaultCloneableImageReadParam;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.coverageio.BaseGridCoverage2DReader;
import org.geotools.coverageio.gdal.BaseGDALGridFormat;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.metadata.iso.extent.GeographicBoundingBoxImpl;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.geometry.XRectangle2D;
import org.opengis.geometry.BoundingBox;
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
class RasterLayerRequest {

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
    private ReferencedEnvelope coverageBBox;

    /** WGS84 envelope 2D for this coverage */
    private ReferencedEnvelope coverageGeographicBBox;

    /** The CRS for the coverage */
    private CoordinateReferenceSystem coverageCRS;

    /** The CRS related to the base envelope 2D */
    private CoordinateReferenceSystem coverageCRS2D;

    /** The Coverage name */
    private String coverageName;

    /** The base grid range for the coverage */
    private Rectangle coverageRasterArea;

    private double[] coverageFullResolution;

    private MathTransform2D coverageGridToWorld2D;

    // ////////////////////////////////////////////////////////////////////////
    //
    // Request specific properties
    //
    // ////////////////////////////////////////////////////////////////////////

    /** The envelope requested */
    private BoundingBox requestedBBox;

    /** The desired overview Policy for this request */
    private OverviewPolicy overviewPolicy;

    /** The region where to fit the requested envelope */
    private Rectangle requestedRasterArea;

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
    private Rectangle coverageRequestedRasterArea;

    /**
     * If set to {@code true} a transformation is requested to obtain the
     * desired data. This usually happens when the requested envelope will be
     * adjusted with intersection/crop of the base envelope.
     */
    private boolean adjustGridToWorldSet;

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

	private double[] approximateCoverageWGS84FullResolution;

	private double[] approximateWGS84RequestedResolution;

	private double[] requestedResolution;

    /**
     * Build a new {@code CoverageRequest} given a set of input parameters.
     * 
     * @param params
     *                The {@code GeneralParameterValue}s to initialize this
     *                request
     * @param baseGridCoverage2DReader 
     */
    public RasterLayerRequest(GeneralParameterValue[] params, BaseGridCoverage2DReader reader) {

        // //
        //
        // Parsing parameters
        //
        // //
        if (params != null) {
            for (GeneralParameterValue gParam : params) {
                final ParameterValue<?> param = (ParameterValue<?>) gParam;
                final ReferenceIdentifier name = param.getDescriptor().getName();
                extractParameter(param, name);
            }
        }
        setDefaultParameters();
        setBaseParameters(reader);
    }

    private void setDefaultParameters() {
        // set the tile size
        if(layout == null)
            setTileSize(BaseGDALGridFormat.SUGGESTED_TILE_SIZE.createValue());
                       
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
        // Requested GridGeometry2D parameter
        //
        // //
        if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
            final GridGeometry2D gg = (GridGeometry2D) param.getValue();
            if (gg == null) {
                return;
            }

            requestedBBox = new ReferencedEnvelope((Envelope) gg.getEnvelope2D());
            requestedRasterArea = gg.getGridRange2D().getBounds();
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
        if (name.equals(BaseGDALGridFormat.USE_MULTITHREADING.getName())) {
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
        if (name.equals(BaseGDALGridFormat.SUGGESTED_TILE_SIZE.getName())) {
            setTileSize(param);
        }
    }

    /**
     * @param param
     */
    private void setTileSize(ParameterValue<?> param) {
            final String suggestedTileSize = (String) param.getValue();
            // Preliminary checks on parameter value
            if ((suggestedTileSize != null)&& (suggestedTileSize.trim().length() > 0)) {
    
                if (suggestedTileSize.contains(BaseGDALGridFormat.TILE_SIZE_SEPARATOR)) {
                    final String[] tilesSize = suggestedTileSize.split(BaseGDALGridFormat.TILE_SIZE_SEPARATOR);
                    if (tilesSize.length == 2) {
                        try {
                            // Getting suggested tile size
                            final int tileWidth = Integer.parseInt(tilesSize[0].trim());
                            final int tileHeight = Integer.parseInt(tilesSize[1].trim());
                            layout = new ImageLayout();
                            layout.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(tileHeight).setTileWidth(tileWidth);
                        } catch (NumberFormatException nfe) {
                            //reset previously set layout
                            layout=null;
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING, "Unable to parse "+ "suggested tile size parameter",nfe);
                            }
                        }
                    }
                }
            }
    }
    
    
    /**
     * Compute this specific request settings all the parameters needed by a
     * visiting {@link RasterLayerResponse} object.
     */
    public synchronized void prepare() {
        try {
        	
            
            // //
            //
        	// Preparation, we are going to set all the relevant params
            //
            // //        	
            prepareCoverageSpatialElements();
            
            // //
            //
            // Adjust requested bounding box and source region in order to fall within the source coverage
            //
            // //
            prepareRequestResponseSpatialElements();

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

            // //
            //
            // Set the read parameters
            //
            // //
            if(requestedBBox != null && !requestedBBox.isEmpty())//&&requestedBBoxInSourceCRS2D!=null&&requestedRasterArea!=null)
            {

            	//set subsampling
            	setReadParameters();
            	
            	// Concatenating an adjustment to the native grid2world is requested since the requested envelope is non empty
                adjustGridToWorldSet=true;
                
                return;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            requestedBBox = null;
            coverageRequestedRasterArea=null;
        } catch (TransformException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            requestedBBox = null;
            coverageRequestedRasterArea=null;
        } catch (FactoryException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            requestedBBox = null;
            coverageRequestedRasterArea=null;
		}

        //make sure we signal the problem
        emptyRequest=true;




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
     * @param refinedRequestedBBox
     *                the crop envelope
     * @return a {@code Rectangle} representing the crop region.
     * @throws TransformException
     *                 in case a problem occurs when going back to raster space.
     */
    private Rectangle getCropRegion()
            throws TransformException {
        final MathTransform gridToWorldTransform = getOriginalGridToWorld(PixelInCell.CELL_CORNER);
        final MathTransform worldToGridTransform = gridToWorldTransform.inverse();
        final GeneralEnvelope rasterArea = CRS.transform(worldToGridTransform,requestedBBox);
        final Rectangle2D ordinates = rasterArea.toRectangle2D();
        // THIS IS FUNDAMENTAL IN ORDER TO AVOID PROBLEMS WHEN DOING TILING
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
     *                {@link OverviewPolicy}. It specifies
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
     * @todo this versions is deeply GDAL based.
     */
    protected void setReadParameters() throws IOException, TransformException {


    	//set source region
    	if(!coverageRequestedRasterArea.isEmpty()) {
    		imageReadParam.setSourceRegion(this.coverageRequestedRasterArea);
    	} else {
    		emptyRequest = true;
    	}
    	
        // //
        //
        // Initialize overview policy
        //
        // //
        if (overviewPolicy == null) {
            overviewPolicy = OverviewPolicy.getDefaultPolicy();
        }

        // //
        //
        // default values for subsampling
        //
        // //
        imageReadParam.setSourceSubsampling(1, 1, 0, 0);

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
//        if (requestedEnvelope != null) {
//            final GridToEnvelopeMapper geMapper = new GridToEnvelopeMapper();
//            geMapper.setEnvelope(requestedEnvelope);
//            geMapper.setGridRange(new GridEnvelope2D(requestedDim));
//
//            // CELL_CORNER comes from GDAL based reader.
//            geMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
//            AffineTransform transform = geMapper.createAffineTransform();
//            requestedRes = CoverageUtilities.getResolution(transform);
//
//            if (LOGGER.isLoggable(Level.FINE)) {
//                LOGGER.log(Level.FINE, "requested resolution: ("
//                        + requestedRes[0] + "," + requestedRes[1] + ")");
//            }
//        }
        
        // ////////////////////////////////////////////////////////////////////
        //
        // DECIMATION ON READING since GDAL will automatically use the
        // overviews
        //
        // ////////////////////////////////////////////////////////////////////        
        double[] requestedRes = null;
        double[] fullRes = null;
        if (approximateWGS84RequestedResolution == null) {
        	requestedRes = requestedResolution;
        	fullRes=coverageFullResolution;
        	
        	
        }
        else
        {
        	requestedRes = approximateWGS84RequestedResolution;
        	fullRes=approximateCoverageWGS84FullResolution;
        }


        if ((requestedRes[0] > fullRes[0])
                || (requestedRes[1] > fullRes[1])) {
            setDecimationParameters(requestedRes,fullRes);

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
     * @param requestedBBox
     *                is the envelope we are requested to load.
     * @param sourceRasterArea
     *                represents the area to load in raster space. This
     *                parameter cannot be null since it gets filled with
     *                whatever the crop region is depending on the
     *                <code>requestedEnvelope</code>.
     * @param requestedRasterArea
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
    private void prepareRequestResponseSpatialElements()
            throws DataSourceException {
        try {
            // ////////////////////////////////////////////////////////////////
            //
            // DO WE HAVE A REQUESTED AREA?
        	//
        	// Check if we have something to load by intersecting the
            // requested envelope with the bounds of this data set.
            //
            // ////////////////////////////////////////////////////////////////
            if (requestedBBox != null) {


                // ////////////////////////////////////////////////////////////
                //
                // ADJUST ENVELOPES AND RASTER REQUESTED AREA to fall withing the coverage bbox
                //
                // ////////////////////////////////////////////////////////////
                adjustRequestedBBox();
                if (requestedBBox == null||requestedBBox.isEmpty())
                {	  	
                    if (LOGGER.isLoggable(Level.FINE)) 
                        LOGGER.log(Level.FINE, "RequestedBBox empty or null");
                	//this means that we do not have anything to load at all!
                    emptyRequest=true;
                    return;
                }

                // /////////////////////////////////////////////////////////////////////
                //
                // CROP SOURCE REGION using the refined requested envelope
                //
                // /////////////////////////////////////////////////////////////////////
                coverageRequestedRasterArea.setRect(getCropRegion());
                if (coverageRequestedRasterArea.isEmpty()) 
                {
                    if (LOGGER.isLoggable(Level.FINE)) 
                        LOGGER.log(Level.FINE, "Requested envelope too small resulting in empty cropped raster region");
                    // TODO: Future versions may define a 1x1 rectangle starting
                    // from the lower coordinate
                    emptyRequest=true;
	                return;
	            }
                if (!coverageRequestedRasterArea.intersects(coverageRasterArea))
                    throw new DataSourceException("The crop region is invalid.");
                XRectangle2D.intersect(coverageRequestedRasterArea, coverageRasterArea, coverageRequestedRasterArea);

                if (LOGGER.isLoggable(Level.FINE)) {
                    StringBuffer sb = new StringBuffer(
                            "Adjusted Requested Envelope = ").append(
                            		requestedBBox.toString()).append("\n")
                            .append("Requested raster dimension = ").append(
                                    requestedRasterArea.toString()).append("\n")
                            .append("Corresponding raster source region = ")
                            .append(coverageRequestedRasterArea.toString());
                    LOGGER.log(Level.FINE, sb.toString());
                }
                return;
            }
        } catch (TransformException e) {
            throw new DataSourceException(
                    "Unable to create a coverage for this source", e);
        } catch (FactoryException e) {
            throw new DataSourceException(
                    "Unable to create a coverage for this source", e);
        }
        
//        get it all!
        requestedBBox=coverageBBox;
        requestedRasterArea=(Rectangle) coverageRasterArea.clone();
        coverageRequestedRasterArea=(Rectangle) coverageRasterArea.clone();
        requestedResolution=coverageFullResolution.clone();

    }

	/**
     * Initialize the 2D properties (CRS and Envelope) of this coverage
     * 
     * @throws FactoryException
     * @throws TransformException
     */
    private void prepareCoverageSpatialElements() throws FactoryException,
            TransformException {
    	//basic initialization
    	coverageGeographicBBox =GridCoverageUtilities.getReferencedEnvelopeFromGeographicBoundingBox(new GeographicBoundingBoxImpl(coverageEnvelope));
        coverageRequestedRasterArea = new Rectangle();
        
        // //
        //
        // Get the original envelope 2d and its spatial reference system
        //
        // //
        coverageCRS2D = CRS.getHorizontalCRS(coverageCRS);
        assert coverageCRS2D.getCoordinateSystem().getDimension() == 2;
        if (coverageCRS.getCoordinateSystem().getDimension() != 2) {
            final MathTransform transform=CRS.findMathTransform(coverageCRS,(CoordinateReferenceSystem) coverageCRS2D);
            final GeneralEnvelope bbox = CRS.transform(transform,coverageEnvelope);
            bbox.setCoordinateReferenceSystem(coverageCRS2D);
			coverageBBox = new ReferencedEnvelope(bbox);
        } else {
        	//it is already a bbox
            coverageBBox = new ReferencedEnvelope(coverageEnvelope);
        }
        
        // compute the approximated full resolution in wgs84
        final GridToEnvelopeMapper geMapper= new GridToEnvelopeMapper(new GridEnvelope2D(coverageRasterArea),coverageGeographicBBox);
        approximateCoverageWGS84FullResolution=CoverageUtilities.getResolution(geMapper.createAffineTransform());
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
    protected void setDecimationParameters(double[] requestedRes,double[] fullResolution) {
        {
            final int w = coverageRasterArea.width;
            final int h = coverageRasterArea.height;
            // ///////////////////////////////////////////////////////////////
            // DECIMATION ON READING
            // Setting subsampling factors with some checkings
            // 1) the subsampling factors cannot be zero
            // 2) the subsampling factors cannot be such that the w or h are 0
            // ///////////////////////////////////////////////////////////////
            if (requestedRes == null) {
                imageReadParam.setSourceSubsampling(1, 1, 0, 0);
            } else {
                int subSamplingFactorX = (int) Math.floor(requestedRes[0]
                        / fullResolution[0]);
                subSamplingFactorX = (subSamplingFactorX == 0) ? 1
                        : subSamplingFactorX;

                while (((w / subSamplingFactorX) <= 0)
                        && (subSamplingFactorX >= 0))
                    subSamplingFactorX--;

                subSamplingFactorX = (subSamplingFactorX == 0) ? 1
                        : subSamplingFactorX;

                int subSamplingFactorY = (int) Math.floor(requestedRes[1]
                        / fullResolution[1]);
                subSamplingFactorY = (subSamplingFactorY == 0) ? 1
                        : subSamplingFactorY;

                while (((h / subSamplingFactorY) <= 0)
                        && (subSamplingFactorY >= 0))
                    subSamplingFactorY--;

                subSamplingFactorY = (subSamplingFactorY == 0) ? 1
                        : subSamplingFactorY;

                imageReadParam.setSourceSubsampling(subSamplingFactorX,
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
    	return PixelTranslation.translate(coverageGridToWorld2D, PixelInCell.CELL_CENTER, pixInCell);
    }

    /**
     * Returns the intersection between the base envelope and the requested
     * envelope.
     * 
     * @param requestedBBox
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
    private void adjustRequestedBBox() throws TransformException,
            FactoryException {

        final CoordinateReferenceSystem requestedBBoxCRS2D = requestedBBox.getCoordinateReferenceSystem();
        try {
        	////
        	//
            // STEP 1: requested BBox to native CRS
        	//
        	////
            MathTransform destinationToSourceTransform = null;
            if (!CRS.equalsIgnoreMetadata(requestedBBoxCRS2D,coverageCRS2D))
                destinationToSourceTransform = CRS.findMathTransform(requestedBBoxCRS2D,coverageCRS2D, true);
            // now transform the requested envelope to source crs
            if (destinationToSourceTransform != null && !destinationToSourceTransform.isIdentity())
            {
            	final GeneralEnvelope temp = CRS.transform(destinationToSourceTransform,requestedBBox);
            	temp.setCoordinateReferenceSystem(coverageCRS2D);
            	requestedBBox= new ReferencedEnvelope(temp);
            	
            }
            else
            	//we do not need to do anything, but we do this in order to aboid problems with the envelope checks
            	requestedBBox=new ReferencedEnvelope(
            			requestedBBox.getMinX(),
            			requestedBBox.getMaxX(),
            			requestedBBox.getMinY(),
            			requestedBBox.getMaxY(),
            			coverageCRS2D);
            

        	////
        	//
            // STEP 2: intersect requested BBox in native CRS with native bbox
        	//
        	////
            // intersect the requested area with the bounds of this
            // layer in native crs
            if (!requestedBBox.intersects((BoundingBox)coverageBBox))
            {
            	requestedBBox =  null;
            	return;
            }
            // XXX Optimize when referenced envelope has intersection method that actually retains the CRS, this is the JTS one
            requestedBBox=new ReferencedEnvelope(((ReferencedEnvelope) requestedBBox).intersection(coverageBBox),coverageCRS2D);
            //compute approximate full resolution
            // compute the approximated full resolution in wgs84
            final GridToEnvelopeMapper geMapper= new GridToEnvelopeMapper(new GridEnvelope2D(requestedRasterArea),requestedBBox);
            requestedResolution=CoverageUtilities.getResolution(geMapper.createAffineTransform());
 
//        	////
//        	//
//            // STEP 3: Go back to destination space with the nevelope
//        	//
//        	////            
//            // transform the intersection envelope from the destination world
//            // space to the requested raster space
//            if (destinationToSourceTransform != null&& !destinationToSourceTransform.isIdentity() )
//            {
//            	final GeneralEnvelope temp =CRS.transform(destinationToSourceTransform.inverse(),requestedBBoxInSourceCRS2D) ;
//            	temp.setCoordinateReferenceSystem(requestedBBoxCRS2D);
//            	requestedBBox = new ReferencedEnvelope(temp);
//            	
//            }
//            else
//            	//we do not need to do anything
//            	requestedBBox=new ReferencedEnvelope(
//            			requestedBBoxInSourceCRS2D.getMinX(),
//            			requestedBBoxInSourceCRS2D.getMaxX(),
//            			requestedBBoxInSourceCRS2D.getMinY(),
//            			requestedBBoxInSourceCRS2D.getMaxY(),
//            			coverageCRS2D);
//            
            
            return;
        } catch (TransformException te) {
            // something bad happened while trying to transform this
            // envelope. let's try with wgs84
            if(LOGGER.isLoggable(Level.FINE))
            	LOGGER.log(Level.FINE,te.getLocalizedMessage(),te);
        } catch (FactoryException fe) {
            // something bad happened while trying to transform this
            // envelope. let's try with wgs84
            if(LOGGER.isLoggable(Level.FINE))
            	LOGGER.log(Level.FINE,fe.getLocalizedMessage(),fe);
        }

        // //
        //
        // If this does not work, we go back to reproject in the wgs84
        // requested envelope
        //              
        // //
    	//convert to WGS84
    	final GeographicBoundingBoxImpl geographicRequestedBBox = new GeographicBoundingBoxImpl(requestedBBox);
        ReferencedEnvelope approximateWGS84requestedBBox =GridCoverageUtilities.getReferencedEnvelopeFromGeographicBoundingBox(geographicRequestedBBox);

        // checking the intersection in wgs84 with the geographicbbox for this coverage
        if (!approximateWGS84requestedBBox.intersects((BoundingBox)coverageGeographicBBox))
        {
        	requestedBBox =  null;
        	return;
        }
        //compute approximate full resolution
        // compute the approximated full resolution in wgs84
        final GridToEnvelopeMapper geMapper= new GridToEnvelopeMapper(new GridEnvelope2D(requestedRasterArea),approximateWGS84requestedBBox);
        approximateWGS84RequestedResolution=CoverageUtilities.getResolution(geMapper.createAffineTransform());

        // intersect with the coverage native WGS84 bbox
        // note that for the moment we got to use general envelope since there is no intersection othrewise
        // TODO fix then we'll have intersection in ReferencedEnvelope
        approximateWGS84requestedBBox=new ReferencedEnvelope(approximateWGS84requestedBBox.intersection(coverageGeographicBBox),DefaultGeographicCRS.WGS84);
        final MathTransform transform = CRS.findMathTransform(DefaultGeographicCRS.WGS84,coverageCRS2D, true);
    	final GeneralEnvelope approximateRequestedBBoInNativeCRS =CRS.transform(transform, approximateWGS84requestedBBox) ;
    	approximateRequestedBBoInNativeCRS.setCoordinateReferenceSystem(coverageCRS2D);
    	requestedBBox = new ReferencedEnvelope(approximateRequestedBBoInNativeCRS);        


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
        input = reader.getInputFile();
        this.coverageEnvelope = reader.getOriginalEnvelope().clone();
        this.coverageRasterArea = ((GridEnvelope2D)reader.getOriginalGridRange());
        this.coverageCRS = reader.getCrs();
        this.coverageName = reader.getCoverageName();
        this.coverageGridToWorld2D = (MathTransform2D) reader.getRaster2Model();
        this.coverageFullResolution = reader.getHighestRes();
        this.hints = reader.getHints().clone();
        if (layout != null) {
            this.hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
        }
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
     * @uml.property name="emptyRequest"
     */
    public synchronized boolean isEmptyRequest() {
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
     * @uml.property name="coverageGridToWorld2D"
     */
    public MathTransform getRaster2Model() {
        return coverageGridToWorld2D;
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

    public boolean isAdjustGridToWorldSet() {
		return adjustGridToWorldSet;
	}
}
