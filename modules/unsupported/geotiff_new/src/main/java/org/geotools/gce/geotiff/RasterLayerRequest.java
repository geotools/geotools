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
package org.geotools.gce.geotiff;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.geometry.XRectangle2D;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.metadata.Identifier;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * A class to handle coverage requests to a reader for a single 2D layer..
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 */
class RasterLayerRequest {

	/** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RasterLayerRequest.class);

    private ReadType readType = AbstractGridFormat.USE_JAI_IMAGEREAD.getDefaultValue()?ReadType.JAI_IMAGEREAD:ReadType.DIRECT_READ;

    /** The {@link BoundingBox} requested */
    private BoundingBox requestedBBox;
    
    /** The {@link BoundingBox} of the portion of the coverage that intersects the requested bbox */
    private BoundingBox cropBBox;
    
    /** The desired overview Policy for this request */
    private OverviewPolicy overviewPolicy;

    /** The region where to fit the requested envelope */
    private Rectangle requestedRasterArea;
    
    /** The region of the  */
    private Rectangle destinationRasterArea;
	/**
     * Set to {@code true} if this request will produce an empty result, and the
     * coverageResponse will produce a {@code null} coverage.
     */
    private boolean empty;

	private Color inputTransparentColor=GeoTiffFormat.INPUT_TRANSPARENT_COLOR.getDefaultValue();;

//	private Color outputTransparentColor=JP2KFormat.OUTPUT_TRANSPARENT_COLOR.getDefaultValue();;

	private AffineTransform requestedGridToWorld;

	private double[] requestedResolution;

//	private double[] backgroundValues;

	private RasterManager rasterManager;

	private MathTransform destinationToSourceTransform;

	private GeneralEnvelope requestedBBOXInCoverageGeographicCRS;

	private double[] requestedRasterScaleFactors;

	private MathTransform requestCRSToCoverageGeographicCRS2D;

	private GeneralEnvelope approximateRequestedBBoInNativeCRS;

	private Dimension tileDimensions;

//	private boolean multithreadingAllowed;

//	public boolean isMultithreadingAllowed() {
//		return multithreadingAllowed;
//	}

	/**
     * Build a new {@code CoverageRequest} given a set of input parameters.
     * 
     * @param params
     *                The {@code GeneralParameterValue}s to initialize this
     *                request
     * @param baseGridCoverage2DReader 
	 * @throws DataSourceException 
     */
    public RasterLayerRequest(final GeneralParameterValue[] params, final RasterManager rasterManager) throws DataSourceException {

        // //
        //
        // Setting default parameters
        //
        // //
    	this.rasterManager=rasterManager;
    	setDefaultParameterValues();
    	
    	
        // //
        //
        // Parsing parameter that can be used to control this request
        //
        // //
        if (params != null) {
            for (GeneralParameterValue gParam : params) {
            	if(gParam instanceof ParameterValue<?>)
            	{                
            		final ParameterValue<?> param = (ParameterValue<?>) gParam;
                    final ReferenceIdentifier name = param.getDescriptor().getName();
                    extractParameter(param, name);
            	}
            }
        }

        // //
        //
        // Set specific imageIO parameters: type of read operation,
        // imageReadParams
        //
        // //
        checkReadType();        
        prepare();
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
	private void setDefaultParameterValues() {
    	final ParameterValueGroup readParams = this.rasterManager.parent.getFormat().getReadParameters();
    	if(readParams==null){
    		if(LOGGER.isLoggable(Level.FINE))
    			LOGGER.fine("No default values for the read parameters!");
    		return;
    	}
    	final List<GeneralParameterDescriptor> parametersDescriptors = readParams.getDescriptor().descriptors();
    	for(GeneralParameterDescriptor descriptor:parametersDescriptors){
    		
    		// we canc get the default vale only with the ParameterDescriptor class
    		if(!(descriptor instanceof ParameterDescriptor))
    			continue;
    		
    		// get name and default value
    		final ParameterDescriptor desc=(ParameterDescriptor) descriptor;
	    	final ReferenceIdentifier name = desc.getName();
	    	final Object value= desc.getDefaultValue();
	    	
	        // //
	        //
	        // Requested GridGeometry2D parameter
	        //
	        // //
	        if (descriptor.getName().equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
	        	if(value==null)
	        		continue;
	            final GridGeometry2D gg = (GridGeometry2D)value;
	            
	
	            requestedBBox = new ReferencedEnvelope((Envelope) gg.getEnvelope2D());
	            requestedRasterArea = gg.getGridRange2D().getBounds();
	            requestedGridToWorld=(AffineTransform) gg.getGridToCRS2D();
	            continue;
	        }
	
	        // //
	        //
	        // Use JAI ImageRead parameter
	        //
	        // //
	        if (name.equals(AbstractGridFormat.USE_JAI_IMAGEREAD.getName())) {
	        	if(value==null)
	        		continue;
	            readType = ((Boolean)value)? ReadType.JAI_IMAGEREAD: ReadType.DIRECT_READ;
	            continue;
	        }
	        
	
	        // //
	        //
	        // Overview Policy parameter
	        //
	        // //
	        if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName())) {
	        	if(value==null)
	        		continue;
	            overviewPolicy = (OverviewPolicy) value;
	            continue;
	        }
	
	        if (name.equals(GeoTiffFormat.INPUT_TRANSPARENT_COLOR.getName())) {
	        	if(value==null)
	        		continue;
				inputTransparentColor = (Color) value;
				// paranoiac check on the provided transparent color
				inputTransparentColor = new Color(
						inputTransparentColor.getRed(),
						inputTransparentColor.getGreen(), 
						inputTransparentColor.getBlue());
				continue;
	
			} 
	        
//			if (name.equals(
//					GeoTiffFormat.OUTPUT_TRANSPARENT_COLOR.getName())) {
//	        	if(value==null)
//	        		continue;
//				outputTransparentColor = (Color) value;
//				// paranoiac check on the provided transparent color
//				outputTransparentColor = new Color(
//						outputTransparentColor.getRed(),
//						outputTransparentColor.getGreen(), 
//						outputTransparentColor.getBlue());
//				continue;
//	
//			}
//			
//			if (name.equals(
//					GeoTiffFormat.BACKGROUND_VALUES.getName())) {
//	        	if(value==null)
//	        		continue;
//				backgroundValues = (double[]) value;
//				continue;
//	
//			}		
//		
//			if (name.equals(ImageMosaicFormat.MAX_ALLOWED_TILES.getName())) {
//	        	if(value==null)
//	        		continue;
//				maximumNumberOfGranules=(Integer)value;
//				continue;
//			}	 
//			
//			
//			if (name.equals(GeoTiffFormat.ALLOW_MULTITHREADING.getName())) {
//	        	if(value==null)
//	        		continue;
//				multithreadingAllowed = ((Boolean) value).booleanValue();
//				continue;
//			}	 		
	       
	        // //
	        //
	        // Suggested tile size parameter. It must be specified with
	        // the syntax: "TileWidth,TileHeight" (without quotes where TileWidth
	        // and TileHeight are integer values)
	        //
	        // //
	        if (name.equals(GeoTiffFormat.SUGGESTED_TILE_SIZE.getName())) {
	            final String suggestedTileSize = (String) value;
	
	            // Preliminary checks on parameter value
	            if ((suggestedTileSize != null)
	                    && (suggestedTileSize.trim().length() > 0)) {
	
	                if (suggestedTileSize
	                        .contains(GeoTiffFormat.TILE_SIZE_SEPARATOR)) {
	                    final String[] tilesSize = suggestedTileSize
	                            .split(GeoTiffFormat.TILE_SIZE_SEPARATOR);
	                    if (tilesSize.length == 2) {
	                        try {
	                            // Getting suggested tile size
	                            final int tileWidth = Integer.valueOf(tilesSize[0] .trim());
	                            final int tileHeight = Integer.valueOf(tilesSize[1].trim());
	                            tileDimensions= new Dimension(tileWidth,tileHeight);
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
        	final Object value = param.getValue();
        	if(value==null)
        		return;
            final GridGeometry2D gg = (GridGeometry2D) param.getValue();
            if (gg == null) {
                return;
            }

            requestedBBox = new ReferencedEnvelope((Envelope) gg.getEnvelope2D());
            requestedRasterArea = gg.getGridRange2D().getBounds();
            requestedGridToWorld=(AffineTransform) gg.getGridToCRS2D();
            return;
        }
        
        // //
        //
        // Use JAI ImageRead parameter
        //
        // //
        if (name.equals(AbstractGridFormat.USE_JAI_IMAGEREAD.getName())) {
        	final Object value = param.getValue();
        	if(value==null)
        		return;
            readType = param.booleanValue() ? ReadType.JAI_IMAGEREAD: ReadType.DIRECT_READ;
            return;
        }


        // //
        //
        // Overview Policy parameter
        //
        // //
        if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName())) {
        	final Object value = param.getValue();
        	if(value==null)
        		return;
            overviewPolicy = (OverviewPolicy) param.getValue();
            return;
        }

        if (name.equals(GeoTiffFormat.INPUT_TRANSPARENT_COLOR.getName())) {
        	final Object value = param.getValue();
        	if(value==null)
        		return;
			inputTransparentColor = (Color) param.getValue();
			// paranoiac check on the provided transparent color
			inputTransparentColor = new Color(
					inputTransparentColor.getRed(),
					inputTransparentColor.getGreen(), 
					inputTransparentColor.getBlue());
			return;

		} 
//
//		if (name.equals(ImageMosaicFormat.FADING.getName())) {
//        	final Object value = param.getValue();
//        	if(value==null)
//        		return;
//			blend = ((Boolean) param.getValue()).booleanValue();
//			return;
//
//		} 
//		if (name.equals(
//				GeoTiffFormat.OUTPUT_TRANSPARENT_COLOR.getName())) {
//        	final Object value = param.getValue();
//        	if(value==null)
//        		return;
//			outputTransparentColor = (Color) param.getValue();
//			// paranoiac check on the provided transparent color
//			outputTransparentColor = new Color(
//					outputTransparentColor.getRed(),
//					outputTransparentColor.getGreen(), 
//					outputTransparentColor.getBlue());
//			return;
//
//		}
//		
//		if (name.equals(
//				GeoTiffFormat.BACKGROUND_VALUES.getName())) {
//        	final Object value = param.getValue();
//        	if(value==null)
//        		return;
//			backgroundValues = (double[]) param.getValue();
//			return;
//
//		}		
//	
//		if (name.equals(ImageMosaicFormat.MAX_ALLOWED_TILES.getName())) {
//        	final Object value = param.getValue();
//        	if(value==null)
//        		return;
//			maximumNumberOfGranules=param.intValue();
//			return;
//		}	 
//		
//		
//		if (name.equals(GeoTiffFormat.ALLOW_MULTITHREADING.getName())) {
//        	final Object value = param.getValue();
//        	if(value==null)
//        		return;
//			multithreadingAllowed = ((Boolean) param.getValue()).booleanValue();
//			return;
//		}	 		
       
        // //
        //
        // Suggested tile size parameter. It must be specified with
        // the syntax: "TileWidth,TileHeight" (without quotes where TileWidth
        // and TileHeight are integer values)
        //
        // //
        if (name.equals(GeoTiffFormat.SUGGESTED_TILE_SIZE.getName())) {
            final String suggestedTileSize = (String) param.getValue();

            // Preliminary checks on parameter value
            if ((suggestedTileSize != null)
                    && (suggestedTileSize.trim().length() > 0)) {

                if (suggestedTileSize
                        .contains(GeoTiffFormat.TILE_SIZE_SEPARATOR)) {
                    final String[] tilesSize = suggestedTileSize
                            .split(GeoTiffFormat.TILE_SIZE_SEPARATOR);
                    if (tilesSize.length == 2) {
                        try {
                            // Getting suggested tile size
                            final int tileWidth = Integer.valueOf(tilesSize[0] .trim());
                            final int tileHeight = Integer.valueOf(tilesSize[1].trim());
                            tileDimensions= new Dimension(tileWidth,tileHeight);
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
     * visiting {@link RasterLayerResponse} object.
     * @throws DataSourceException 
     */
    private void prepare() throws DataSourceException {
            //
            // DO WE HAVE A REQUESTED AREA?
        	//
        	// Check if we have something to load by intersecting the
            // requested envelope with the bounds of this data set.
            //
            if (requestedBBox == null) {
                    
                    //
                    // In case we have nothing to look at we should get the whole coverage
                    //
                    requestedBBox=rasterManager.domainManager.coverageBBox;
                    cropBBox=rasterManager.domainManager.coverageBBox;
                    requestedRasterArea=(Rectangle) rasterManager.domainManager.coverageRasterArea.clone();
                    destinationRasterArea=(Rectangle)rasterManager.domainManager.coverageRasterArea.clone();
                    requestedResolution=rasterManager.domainManager.coverageFullResolution.clone();   
                    // TODO harmonize the various types of transformations
                    requestedGridToWorld=(AffineTransform) rasterManager.domainManager.coverageGridToWorld2D;
                    return;
            }
            
            
            //
            // Adjust requested bounding box and source region in order to fall within the source coverage
            //
           computeRequestSpatialElements();
    }

    private void inspectCoordinateReferenceSystems() throws DataSourceException {
    	// get the crs for the requested bbox
        final CoordinateReferenceSystem requestCRS = CRS.getHorizontalCRS(requestedBBox.getCoordinateReferenceSystem());


    	//
		// Check if the request CRS is different from the coverage native CRS
    	//
        if (!CRS.equalsIgnoreMetadata(requestCRS,rasterManager.domainManager.coverageCRS2D))
			try {
				destinationToSourceTransform = CRS.findMathTransform(requestCRS,rasterManager.domainManager.coverageCRS2D, true);
			} catch (FactoryException e) {
				throw new DataSourceException("Unable to inspect request CRS",e);
			}
        // now transform the requested envelope to source crs
        if (destinationToSourceTransform != null && destinationToSourceTransform.isIdentity())
        	destinationToSourceTransform=null;// the CRS is basically the same
        else if(destinationToSourceTransform instanceof AffineTransform)
        {
            //
			// k, the transformation between the various CRS is not null or the
			// Identity, let's see if it is an affine transform, which case we
			// can incorporate it into the requested grid to world
            //
            // we should not have any problems with regards to BBOX reprojection
        		
        	// update the requested grid to world transformation by pre concatenating the destination to source transform
        	requestedGridToWorld.preConcatenate((AffineTransform) destinationToSourceTransform);
        	
        	//update the requested envelope
        	try {
        		final MathTransform tempTransform = PixelTranslation.translate(
        				ProjectiveTransform.create(requestedGridToWorld), 
        				PixelInCell.CELL_CENTER,
        				PixelInCell.CELL_CORNER);
				requestedBBox=new ReferencedEnvelope(
						CRS.transform( tempTransform,new GeneralEnvelope(requestedRasterArea)));
				
			} catch (MismatchedDimensionException e) {
				throw new DataSourceException("Unable to inspect request CRS",e);
			} catch (TransformException e) {
				throw new DataSourceException("Unable to inspect request CRS",e);
			}
        	
			// now clean up all the traces of the transformations
			destinationToSourceTransform=null;
        }
	}

	/**
     * Check the type of read operation which will be performed and return
     * {@code true} if a JAI imageRead operation need to be performed or
     * {@code false} if a simple read operation is needed.
     * 
     * @return {@code true} if the read operation will use a JAI ImageRead
     *         operation instead of a simple {@code ImageReader.read(...)} call.
     */
    private void checkReadType() {

        if (readType != ReadType.UNSPECIFIED)
            return;
        // //
        //
        // Ok, the request did not explicitly set the read type, let's check the
        // hints.
        //
        // //
        final Hints hints=rasterManager.getHints();
        if (hints != null) {
            final Object o = hints.get(Hints.USE_JAI_IMAGEREAD);
            if (o != null) {
            	readType=(ReadType) o;
            	return;
            }
        }

        // //
        //
        // Last chance is to use the default read type.
        //
        // //
        readType = ReadType.getDefault();
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
     * @throws DataSourceException 
     */
    private void computeCropRasterArea()
            throws  DataSourceException {
    	
    	//we have nothing to crop
    	if(cropBBox==null)
    	{
    		destinationRasterArea=null;
    		return;
    	}
    	
    	//
    	// We need to invert the requested gridToWorld and then adjust the requested raster area are accordingly
    	//
    	
    	// invert the requested grid to world keeping into account the fact that it is related to cell center
    	// while the raster is related to cell corner
    	MathTransform2D requestedWorldToGrid;
		try {
			requestedWorldToGrid = (MathTransform2D) PixelTranslation.translate(ProjectiveTransform.create(requestedGridToWorld), PixelInCell.CELL_CENTER, PixelInCell.CELL_CORNER).inverse();
		} catch (NoninvertibleTransformException e) {
			throw new DataSourceException(e);
		}
			
		
    	if(destinationToSourceTransform==null||destinationToSourceTransform.isIdentity()){

			
			// now get the requested bbox which have been already adjusted and project it back to raster space
			try {
				destinationRasterArea = new GeneralGridEnvelope(CRS.transform(requestedWorldToGrid,new GeneralEnvelope(cropBBox)),PixelInCell.CELL_CORNER,false).toRectangle();
			} catch (IllegalStateException e) {
				throw new DataSourceException(e);
			} catch (TransformException e) { 
				throw new DataSourceException(e);
			}
    	}
    	else
    	{
    		//
    		// reproject the crop bbox back and then crop, notice that we are imposing 
    		//
    		try {
				final GeneralEnvelope cropBBOXInRequestCRS=CRS.transform(destinationToSourceTransform.inverse(), cropBBox);
				cropBBOXInRequestCRS.setCoordinateReferenceSystem(requestedBBox.getCoordinateReferenceSystem());
				//make sure it falls within the requested envelope
				cropBBOXInRequestCRS.intersect(requestedBBox);
				
				//now go back to raster space 
				destinationRasterArea =  new GeneralGridEnvelope(CRS.transform(requestedWorldToGrid,cropBBOXInRequestCRS),PixelInCell.CELL_CORNER,false).toRectangle();
				//intersect with the original requested raster space to be sure that we stay within the requested raster area
				XRectangle2D.intersect(destinationRasterArea, requestedRasterArea, destinationRasterArea);
			} catch (NoninvertibleTransformException e) {
				throw new DataSourceException(e);
			} catch (TransformException e) {
				throw new DataSourceException(e);
			}
    	}
		//is it empty??
        if (destinationRasterArea.isEmpty()) 
        {
            if (LOGGER.isLoggable(Level.FINE)) 
                LOGGER.log(Level.FINE, "Requested envelope too small resulting in empty cropped raster region");
            // TODO: Future versions may define a 1x1 rectangle starting
            // from the lower coordinate
            empty=true;
            return ;
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
     * @param destinationRasterArea
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
     * @throws DataSourceException 
     * 
     * @throws DataSourceException
     *                 in case something bad occurs
     */
    private void computeRequestSpatialElements() throws DataSourceException{
        
        //
		// Inspect the request and precompute transformation between CRS. We
		// also check if we can simply adjust the requested GG in case the
		// request CRS is different from the coverage native CRS but the
		// transformation is simpl ean affine transformation.
    	//
		// In such a case we can simplify our work by adjusting the
		// requested grid to world, preconcatenating the coordinate
		// operation to change CRS
        //
        inspectCoordinateReferenceSystems();
            
        //
        // WE DO HAVE A REQUESTED AREA!
    	//
        //
		// Create the crop bbox in the coverage CRS for cropping it later
		// on. 
        //
        computeCropBBOX();
        if (empty||(cropBBox!=null&&cropBBox.isEmpty()))
        {	  	
            if (LOGGER.isLoggable(Level.FINE)) 
                LOGGER.log(Level.FINE, "RequestedBBox empty or null");
        	//this means that we do not have anything to load at all!
            empty=true;
            return;
        }
        
        //
        // CROP SOURCE REGION using the refined requested envelope
        //
        computeCropRasterArea();     
        if (empty||(destinationRasterArea!=null&&destinationRasterArea.isEmpty()))
        {	  	
            if (LOGGER.isLoggable(Level.FINE)) 
                LOGGER.log(Level.FINE, "CropRasterArea empty or null");
        	//this means that we do not have anything to load at all!
            return;
        }                
        
        if (LOGGER.isLoggable(Level.FINE)) {
            StringBuffer sb = new StringBuffer(
                    "Adjusted Requested Envelope = ")
            		.append(requestedBBox.toString())
            		.append("\n")
                    .append("Requested raster dimension = ")
                    .append(requestedRasterArea.toString())
                    .append("\n")
                    .append("Corresponding raster source region = ")
                            .append(requestedRasterArea.toString());
                    LOGGER.log(Level.FINE, sb.toString());
                }
         //
        // Compute the request resolution from the request
        //
        computeRequestedResolution();

    }

	/**
	 * Computes the requested resolution which is going to be used for selecting
	 * overviews and or deciding decimation factors on the target coverage.
	 * 
	 * <p>
	 * In case the requested envelope is in the same
	 * {@link CoordinateReferenceSystem} of the coverage we compute the
	 * resolution using the requested {@link MathTransform}. Notice that it must
	 * be a {@link LinearTransform} or else we fail.
	 * 
	 * <p>
	 * In case the requested envelope is not in the same {@link CoordinateReferenceSystem} of the coverage we 
	 * 
	 * @throws DataSourceException
	 *             in case something bad happens during reprojections and/or
	 *             intersections.
	 */
	private void computeRequestedResolution() throws DataSourceException
	{
			
		try{
	
			// let's try to get the resolution from the requested gridToWorld
			if(requestedGridToWorld instanceof LinearTransform)
			{

				//
				// the crs of the request and the one of the coverage are NOT the
				// same and the conversion is not , we can get the resolution from envelope + raster directly
				//
				if(destinationToSourceTransform!=null&&!destinationToSourceTransform.isIdentity())
				{

					
			        //
			        // compute the approximated resolution in the request crs, notice that we are
					// assuming a reprojection that keeps the raster area unchanged hence
					// the effect is a degradation of quality, but we take that into account emprically
			        //
					requestedResolution=null;
					
					// compute the raster that correspond to the crop bbox at the highest resolution
					final Rectangle sourceRasterArea = new GeneralGridEnvelope(
							 CRS.transform(
									 PixelTranslation.translate(rasterManager.getRaster2Model(),PixelInCell.CELL_CENTER,PixelInCell.CELL_CORNER).inverse(),
									 cropBBox),PixelInCell.CELL_CORNER,false).toRectangle();
					XRectangle2D.intersect(sourceRasterArea, rasterManager.domainManager.coverageRasterArea, sourceRasterArea);
					if(sourceRasterArea.isEmpty())
						throw new DataSourceException("aaa");
					

					// transform the crop bbox to the request model space
					final GeneralEnvelope envelope=CRS.transform(destinationToSourceTransform.inverse(), cropBBox);
			        final GridToEnvelopeMapper geMapper= new GridToEnvelopeMapper(new GridEnvelope2D(sourceRasterArea),envelope);
			        final AffineTransform tempTransform = geMapper.createAffineTransform();
			        final double scaleX=XAffineTransform.getScaleX0((AffineTransform) requestedGridToWorld)/XAffineTransform.getScaleX0(tempTransform);
			        final double scaleY=XAffineTransform.getScaleY0((AffineTransform) requestedGridToWorld)/XAffineTransform.getScaleY0(tempTransform);
					//
					// empiric adjustment to get a finer resolution to have better quality when reprojecting
			        // TODO make it parametric
					//
			        requestedRasterScaleFactors= new double[2];
			        requestedRasterScaleFactors[0]=scaleX*1.0;
			        requestedRasterScaleFactors[1]=scaleY*1.0;
			        
			        
			        // adjust the final grid to world
			        final GridToEnvelopeMapper geMapper1= new GridToEnvelopeMapper(new GridEnvelope2D(destinationRasterArea),cropBBox);
			        requestedGridToWorld= geMapper1.createAffineTransform();

					
				}
				else
				{

					//
					// the crs of the request and the one of the coverage are the
					// same, we can get the resolution from the grid to world
					//					
//					if(requestedGridToWorld instanceof AffineTransform){
						requestedResolution= new double[]
						                                {
															XAffineTransform.getScaleX0(requestedGridToWorld),
															XAffineTransform.getScaleY0(requestedGridToWorld)
														};
//					}
//					else{
//						// get the matrix
//						final Matrix matrix= ((LinearTransform)requestedGridToWorld).getMatrix();
//						final XAffineTransform transform=new XAffineTransform(
//								matrix.getElement(0, 0),
//								matrix.getElement(1, 0),
//								matrix.getElement(0, 1),
//								matrix.getElement(1, 1),
//								matrix.getElement(0, 2),
//								matrix.getElement(1, 2));
//						requestedResolution= new double[]
//						                                {
//															XAffineTransform.getScaleX0(transform),
//															XAffineTransform.getScaleY0(transform)
//														};
//					}
//		
				}
			}
				
				
			//leave
			return;
		}catch (Throwable e) {
			if(LOGGER.isLoggable(Level.INFO))
				LOGGER.log(Level.INFO,"Unable to compute requested resolution",e);
		}
		
		//
		//use the coverage resolution since we cannot compute the requested one
		//
		LOGGER.log(Level.WARNING,"Unable to compute requested resolution, using highest available");
		requestedResolution=rasterManager.domainManager.coverageFullResolution;
					
	}




    private void computeCropBBOX() throws DataSourceException  {

    	// get the crs for the requested bbox
        final CoordinateReferenceSystem requestCRS = CRS.getHorizontalCRS(requestedBBox.getCoordinateReferenceSystem());
        try {

        	//
			// If this approach succeeds, either the request crs is the same of
			// the coverage crs or the request bbox can be reprojected to that
			// crs
        	//
        	
        	
            // STEP 1: reproject requested BBox to native CRS if needed
            // now transform the requested envelope to source crs
            if (destinationToSourceTransform != null && !destinationToSourceTransform.isIdentity())
            {
            	final GeneralEnvelope temp = CRS.transform(destinationToSourceTransform,requestedBBox);
            	temp.setCoordinateReferenceSystem(rasterManager.domainManager.coverageCRS2D);
            	cropBBox= new ReferencedEnvelope(temp);
            	
            }
            else
            {
            	//we do not need to do anything, but we do this in order to aboid problems with the envelope checks
            	cropBBox=new ReferencedEnvelope(
            			requestedBBox.getMinX(),
            			requestedBBox.getMaxX(),
            			requestedBBox.getMinY(),
            			requestedBBox.getMaxY(),
            			rasterManager.domainManager.coverageCRS2D);
            	
            }            


            //
            // STEP 2: intersect requested BBox in native CRS with coverage native bbox to get the crop bbox
        	//
            // intersect the requested area with the bounds of this
            // layer in native crs
            if (!cropBBox.intersects((BoundingBox)rasterManager.domainManager.coverageBBox))
            {
                cropBBox=null;
                empty=true;
            	return;
            }
            // TODO XXX Optimize when referenced envelope has intersection method that actually retains the CRS, this is the JTS one
            cropBBox=new ReferencedEnvelope(((ReferencedEnvelope) cropBBox).intersection(rasterManager.domainManager.coverageBBox),rasterManager.domainManager.coverageCRS2D);   
            
            return;
        } catch (TransformException te) {
            // something bad happened while trying to transform this
            // envelope. let's try with wgs84
            if(LOGGER.isLoggable(Level.FINE))
            	LOGGER.log(Level.FINE,te.getLocalizedMessage(),te);
        }
        

        try {

	        //
			// If we can not reproject the requested envelope to the native CRS,
			// we go back to reproject in the geographic crs of the native
			// coverage since this usually happens for conversions between CRS
			// whose area of definition is different
	        //              

        	
        	// STEP 1 reproject the requested envelope to the coverage geographic bbox
	        if(destinationToSourceTransform!=null&&!destinationToSourceTransform.isIdentity()){
	        	//try to convert the requested bbox to the coverage geocrs

        		requestedBBOXInCoverageGeographicCRS=CRS.transform(requestCRSToCoverageGeographicCRS2D,requestedBBox);
        		requestedBBOXInCoverageGeographicCRS.setCoordinateReferenceSystem(rasterManager.domainManager.coverageGeographicCRS2D);
	        	
	        }
	        if(requestedBBOXInCoverageGeographicCRS==null)
	        	requestedBBOXInCoverageGeographicCRS= new GeneralEnvelope(requestCRS);
	
	
	        // STEP 2 intersection with the geographic bbox for this coverage
	        if (!requestedBBOXInCoverageGeographicCRS.intersects(rasterManager.domainManager.coverageGeographicBBox,true))
	        {
	            cropBBox=null;
	            empty=true;
	        	return;
	        }
	        // intersect with the coverage native geographic bbox
	        // note that for the moment we got to use general envelope since there is no intersection otherwise
	        requestedBBOXInCoverageGeographicCRS.intersect(rasterManager.domainManager.coverageGeographicBBox);
	        requestedBBOXInCoverageGeographicCRS.setCoordinateReferenceSystem(rasterManager.domainManager.coverageGeographicCRS2D);
	        
	        // now go back to the coverage native CRS in order to compute an approximate requested resolution
	        final MathTransform transform = CRS.findMathTransform(requestedBBOXInCoverageGeographicCRS.getCoordinateReferenceSystem(),rasterManager.domainManager.coverageCRS2D, true);
	        approximateRequestedBBoInNativeCRS = CRS.transform(transform, requestedBBOXInCoverageGeographicCRS);
	    	approximateRequestedBBoInNativeCRS.setCoordinateReferenceSystem(rasterManager.domainManager.coverageCRS2D);
	    	cropBBox = new ReferencedEnvelope(approximateRequestedBBoInNativeCRS);     
	    	
	    	
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

        LOGGER.log(Level.INFO,"We did not manage to crop the requested envelope, we fall back onto loading the whole coverage.");
        cropBBox=null;
    }



    /**
     * @return
     * @uml.property name="empty"
     */
    public boolean isEmpty() {
        return empty;
    }

	public BoundingBox getRequestedBBox() {
		return requestedBBox;
	}

	public OverviewPolicy getOverviewPolicy() {
		return overviewPolicy;
	}

	public Rectangle getRequestedRasterArea() {
		return (Rectangle) (requestedRasterArea!=null?requestedRasterArea.clone():requestedRasterArea);
	}

	public double[] getRequestedResolution() {
		return requestedResolution!=null?requestedResolution.clone():null;
	}

	public Color getInputTransparentColor() {
		return inputTransparentColor;
	}

//	public Color getOutputTransparentColor() {
//		return outputTransparentColor;
//	}
//
//	public int getMaximumNumberOfGranules() {
//		return maximumNumberOfGranules;
//	}
//
//	public double[] getBackgroundValues() {
//		return backgroundValues;
//	}
	
	public ReadType getReadType() {
		return readType;
	}



	public Rectangle getDestinationRasterArea() {
		return destinationRasterArea;
	}

	public BoundingBox getCropBBox() {
		return cropBBox;
	}

	public AffineTransform getRequestedGridToWorld() {
		return requestedGridToWorld;
	}


	public Dimension getTileDimensions() {
		return tileDimensions;
	}

	public double[] getRequestedRasterScaleFactors() {
		return requestedRasterScaleFactors!=null?requestedRasterScaleFactors.clone():requestedRasterScaleFactors;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder= new StringBuilder();
		builder.append("RasterLayerRequest description: \n");
		builder.append("\tRequestedBBox=").append(requestedBBox).append("\n");
		builder.append("\tRequestedRasterArea=").append(requestedRasterArea).append("\n");
		builder.append("\tRequestedGridToWorld=").append(requestedGridToWorld).append("\n");
		builder.append("\tReadType=").append(readType);
		return builder.toString();
	}	
}