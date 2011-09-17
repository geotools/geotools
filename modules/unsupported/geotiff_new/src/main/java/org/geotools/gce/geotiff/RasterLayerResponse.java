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

import it.geosolutions.imageio.imageioimpl.EnhancedImageReadParam;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.measure.unit.Unit;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.operator.ConstantDescriptor;
import javax.media.jai.operator.FormatDescriptor;
import javax.media.jai.util.ImagingException;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.RasterDescriptor.RasterLoadingResult;
import org.geotools.gce.geotiff.OverviewsController.OverviewLevel;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.util.NumberRange;
import org.geotools.util.SimpleInternationalString;
import org.jaitools.imageutils.ImageLayout2;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.SampleDimension;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;

/**
 * A RasterLayerResponse. An instance of this class is produced everytime a
 * requestCoverage is called to a reader.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 * 
 */
class RasterLayerResponse{
	
    private static final class SimplifiedGridSampleDimension extends GridSampleDimension implements SampleDimension{
        
        	/**
        	 * 
        	 */
        	private static final long serialVersionUID = 2227219522016820587L;
        
        
        	private double nodata;
        	private double minimum;
        	private double maximum;
        	private double scale;
        	private double offset;
        	private Unit<?> unit;
        	private SampleDimensionType type;
        	private ColorInterpretation color;
        	private Category bkg;
        
        	public SimplifiedGridSampleDimension(
        			CharSequence description,
        			SampleDimensionType type, 
        			ColorInterpretation color,
        			double nodata,
        			double minimum, 
        			double maximum, 
        			double scale, 
        			double offset,
        			Unit<?> unit) {
        		super(description,!Double.isNaN(nodata)?
        				new Category[]{new Category(Vocabulary
        	                    .formatInternational(VocabularyKeys.NODATA), new Color[]{new Color(0, 0, 0, 0)} , NumberRange
        	                    .create(nodata, nodata), NumberRange
        	                    .create(nodata, nodata))}:null,unit);
        		this.nodata=nodata;
        		this.minimum=minimum;
        		this.maximum=maximum;
        		this.scale=scale;
        		this.offset=offset;
        		this.unit=unit;
        		this.type=type;
        		this.color=color;
        		this.bkg=new Category("Background", Utils.TRANSPARENT, 0);
        	}
        
        
        
        	@Override
        	public double getMaximumValue() {
        		return maximum;
        	}
        
        	@Override
        	public double getMinimumValue() {
        		return minimum;
        	}
        
        	@Override
        	public double[] getNoDataValues() throws IllegalStateException {
        		return new double[]{nodata};
        	}
        
        	@Override
        	public double getOffset() throws IllegalStateException {
        		return offset;
        	}
        
        	@Override
        	public NumberRange<? extends Number> getRange() {
        		return super.getRange();
        	}
        
        	@Override
        	public SampleDimensionType getSampleDimensionType() {
        		return type;
        	}
        
        	@Override
        	public MathTransform1D getSampleToGeophysics() {
        		return super.getSampleToGeophysics();
        	}
        
        	@Override
        	public Unit<?> getUnits() {
        		return unit;
        	}
        	
        	@Override
        	public double getScale() {
        		return scale;
        	}
        	
        	@Override
        	public ColorInterpretation getColorInterpretation() {
        		return color;
        	}
        
        
        	@Override
        	public Category getBackground() {
        		return bkg;
        	}
        
        	@Override
        	public InternationalString[] getCategoryNames()
        			throws IllegalStateException {
        		return new InternationalString[]{SimpleInternationalString.wrap("Background")};
        	}
        }

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(RasterLayerResponse.class);

    /**
     * The GridCoverage produced after a {@link #compute()} method call
     */
    private GridCoverage2D gridCoverage;

    /** The {@link RasterLayerRequest} originating this response */
    private RasterLayerRequest request;

    /** The coverage factory producing a {@link GridCoverage} from an image */
    private GridCoverageFactory coverageFactory;

    /** The base envelope related to the input coverage */
    private GeneralEnvelope coverageEnvelope;

    private RasterManager rasterManager;

    private Color transparentColor;

    private ReferencedEnvelope finalBBox;

    private Rectangle rasterBounds;

    private MathTransform2D finalGridToWorldCorner;

    private MathTransform2D finalWorldToGridCorner;

    private int overviewsLevel = 0;

    private EnhancedImageReadParam baseReadParameters = new EnhancedImageReadParam();
    
    private MathTransform baseGridToWorld;

    private double[] backgroundValues;

    private Hints hints;

    private boolean oversampledRequest = false;

    /**
     * Construct a {@code RasterLayerResponse} given a specific
     * {@link RasterLayerRequest}, a {@code GridCoverageFactory} to produce
     * {@code GridCoverage}s and an {@code ImageReaderSpi} to be used for
     * instantiating an Image Reader for a read operation,
     * 
     * @param request
     *            a {@link RasterLayerRequest} originating this response.
     * @param coverageFactory
     *            a {@code GridCoverageFactory} to produce a {@code GridCoverage} 
     *            when calling the {@link #compute()} method.
     * @param readerSpi
     *            the Image Reader Service provider interface.
     */
    public RasterLayerResponse(final RasterLayerRequest request, final RasterManager rasterManager) {
        this.request = request;
        hints = rasterManager.getHints();
        coverageEnvelope = rasterManager.getCoverageEnvelope();
        baseGridToWorld = rasterManager.getRaster2Model();
        coverageFactory = rasterManager.parent.getGridCoverageFactory();
        this.rasterManager = rasterManager;
        backgroundValues = request.getBackgroundValues();
        transparentColor = request.getInputTransparentColor();        

    }

    /**
     * Compute the coverage request and produce a grid coverage which will be
     * returned by {@link #createResponse()}. The produced grid coverage may be
     * {@code null} in case of empty request.
     * 
     * @return the {@link GridCoverage} produced as computation of this response
     *         using the {@link #compute()} method.
     * @throws IOException
     * @uml.property name="gridCoverage"
     */
    public GridCoverage2D createResponse() throws IOException {
        processRequest();
        return gridCoverage;
    }

    /**
     * @return the {@link RasterLayerRequest} originating this response.
     * 
     * @uml.property name="request"
     */
    public RasterLayerRequest getOriginatingCoverageRequest() {
        return request;
    }
    /**
     * This method creates the GridCoverage2D from the underlying file given a
     * specified envelope, and a requested dimension.
     * 
     * @param iUseJAI
     *            specify if the underlying read process should leverage on a
     *            JAI ImageRead operation or a simple direct call to the {@code
     *            read} method of a proper {@code ImageReader}.
     * @param overviewPolicy
     *            the overview policy which need to be adopted
     * @return a {@code GridCoverage}
     * 
     * @throws java.io.IOException
     */
    private  void processRequest() throws IOException {

            if (request.isEmpty())
            {
                    if(LOGGER.isLoggable(Level.FINE))
                            LOGGER.log(Level.FINE,"Request is empty: "+request.toString());
                    this.gridCoverage=null;
                    return;
            }
            
            // assemble granules
            final RenderedImage mosaic = prepareResponse();
            
            //postproc
            RenderedImage finalRaster = postProcessRaster(mosaic);
            //create the coverage
            gridCoverage = prepareCoverage(finalRaster);
            
    }
    /**
     * This method loads the granules which overlap the requested {@link GeneralEnvelope} using 
     * the provided values for alpha and input ROI.
     */
    private RenderedImage prepareResponse() throws DataSourceException {

        try {

            // select the relevant overview, notice that at this time we have relaxed a bit the 
            // requirement to have the same exact resolution for all the levels, but still we 
            // do not allow for reading the various grid to world transform directly from the 
            // input files, therefore we are assuming that each rasterDescriptor has a scale 
            // and translate only grid to world that can be deduced from its base level dimension 
            // and envelope. The grid to world transforms for the other levels can be computed 
            // accordingly knowning the scale factors.
            if (request.getRequestedBBox() != null && request.getRequestedRasterArea() != null) {
                overviewsLevel = setReadParams(request.getOverviewPolicy(), baseReadParameters, request);
            } else {
                overviewsLevel = 0;
            }
            assert overviewsLevel >= 0;
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Loading level " + overviewsLevel + " with subsampling factors "
                        + baseReadParameters.getSourceXSubsampling() + " "
                        + baseReadParameters.getSourceYSubsampling());
            }

            final BoundingBox cropBBOX = request.getCropBBox();
            if (cropBBOX != null) {
                finalBBox = ReferencedEnvelope.reference(cropBBOX);
            } else {
                finalBBox = new ReferencedEnvelope(coverageEnvelope);
            }
            // compute final world to grid base grid to world for the center of pixels
            final AffineTransform g2w = new AffineTransform((AffineTransform) baseGridToWorld);
            // move it to the corner
            g2w.concatenate(CoverageUtilities.CENTER_TO_CORNER);

            // keep into account levels and subsampling
            final OverviewLevel level = rasterManager.overviewsController.resolutionsLevels.get(overviewsLevel);
            final OverviewLevel baseLevel = rasterManager.overviewsController.resolutionsLevels.get(0);
            final AffineTransform2D adjustments = new AffineTransform2D(
                    (level.resolutionX / baseLevel.resolutionX)
                            * baseReadParameters.getSourceXSubsampling(), 0, 0,
                    (level.resolutionY / baseLevel.resolutionY)
                            * baseReadParameters.getSourceYSubsampling(), 0, 0);
            g2w.concatenate(adjustments);
            
            // move it to the corner
            finalGridToWorldCorner = new AffineTransform2D(g2w);
            finalWorldToGridCorner = finalGridToWorldCorner.inverse();            
            final GeneralEnvelope tempRasterBounds = CRS.transform(finalWorldToGridCorner, finalBBox);
            rasterBounds=tempRasterBounds.toRectangle2D().getBounds();
            if (rasterBounds.width == 0)
                rasterBounds.width++;
            if (rasterBounds.height == 0)
                rasterBounds.height++;
            
            final double[] requestRes = request.getRequestedResolution();
            final double resX = baseLevel.resolutionX;
            final double resY = baseLevel.resolutionY;            
            if ((requestRes[0] < resX || requestRes[1] < resY) ) {
                // Using the best available resolution
                oversampledRequest = true;
            }             
            if(oversampledRequest)
                rasterBounds.grow(2, 2);            



            RenderedImage theImage=null;
            try {

                RasterLoadingResult result = rasterManager.rasterDescriptor.loadRaster(baseReadParameters, overviewsLevel, finalBBox,
                        finalWorldToGridCorner, request, request.getTileDimensions());
                theImage =result.getRaster();
                if (theImage == null) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "Unable to load the raster with request " + request.toString());
                    }

                }
                
                //
                // Set final transformation
                //
                RasterLayerResponse.this.finalGridToWorldCorner=new AffineTransform2D(result.gridToWorld);


            } catch (ImagingException e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.fine("Unable to load the raster with request "  + request);
                }
                theImage = null;
            } catch (Throwable e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.fine("Unable to load the raster with request " + request);
                }
                theImage = null;
            }

            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Processing loaded raster data ");
            }


            //
            // Did we actually load anything?? Notice that it might happen that either we have 
            // wholes inside the definition area for the image or we had some problem with 
            // missing tiles, therefore it might happen that for some bboxes we don't have 
            // anything to load.
            //
            if (theImage != null) {

                //
                // Create the mosaic image by doing a crop if necessary and also managing the 
                // transparent color if applicable. Be aware that management of the transparent 
                // color involves removing transparency information from the input images.
                //
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Loaded finalBBox " + finalBBox.toString()
                            + " while crop finalBBox " + request.getCropBBox());
                }

                return theImage;

            } else {
                // if we get here that means that we do not have anything to load
                // but still we are inside the definition area for the mosaic,
                // therefore we create a fake coverage using the background values,
                // if provided (defaulting to 0), as well as the compute raster
                // bounds, envelope and grid to world.

                final Number[] values = ImageUtilities.getBackgroundValues(rasterManager.baseImageType.getSampleModel(), backgroundValues);
                // create a constant image with a proper layout
                final RenderedImage finalImage = ConstantDescriptor.create(
                        Float.valueOf(rasterBounds.width),
                        Float.valueOf(rasterBounds.height),
                        values,
                        null);
                if(rasterManager.baseImageType!=null&&rasterManager.baseImageType.getColorModel()!=null){
                    final ImageLayout2 il= new ImageLayout2();
                    il.setColorModel(rasterManager.baseImageType.getColorModel());
                    Dimension tileSize= request.getTileDimensions();
                    if(tileSize==null){
                        tileSize=JAI.getDefaultTileSize();
                    } 
                    il.setSampleModel(rasterManager.baseImageType.getColorModel().createCompatibleSampleModel(tileSize.width, tileSize.height));
                    il.setTileGridXOffset(0).setTileGridYOffset(0).setTileWidth((int)tileSize.getWidth()).setTileHeight((int)tileSize.getHeight());
                    return FormatDescriptor.create(
                            finalImage,
                            Integer.valueOf(il.getSampleModel(null).getDataType()),
                            new RenderingHints(JAI.KEY_IMAGE_LAYOUT,il));
                }
                return finalImage;
            }

        } catch (IOException e) {
            throw new DataSourceException("Unable to create this mosaic", e);
        } catch (TransformException e) {
            throw new DataSourceException("Unable to create this mosaic", e);
        }
    }

    /**
     * This method is responsible for creating a coverage from the supplied {@link RenderedImage}.
     * 
     * @param image
     * @return
     * @throws IOException
     */
    private GridCoverage2D prepareCoverage(RenderedImage image) throws IOException {
        
        // creating bands
        final SampleModel sm=image.getSampleModel();
        final ColorModel cm=image.getColorModel();
        final int numBands = sm.getNumBands();
        final GridSampleDimension[] bands = new GridSampleDimension[numBands];
        // setting bands names.
        for (int i = 0; i < numBands; i++) {
                // color interpretation
        final ColorInterpretation colorInterpretation=TypeMap.getColorInterpretation(cm, i);
        if(colorInterpretation==null)
               throw new IOException("Unrecognized sample dimension type");
        
        // sample dimension type
        final SampleDimensionType st=TypeMap.getSampleDimensionType(sm, i);
            
        // set some no data values, as well as Min and Max values
        final double noData;
        double min=-Double.MAX_VALUE,max=Double.MAX_VALUE;
        if(backgroundValues!=null)
        {
                // sometimes background values are not specified as 1 per each band, therefore we need to be careful
                noData= backgroundValues[backgroundValues.length > i ? i:0];
        }
        else
        {
                if(st.compareTo(SampleDimensionType.REAL_32BITS)==0)
                        noData= Float.NaN;
                else
                        if(st.compareTo(SampleDimensionType.REAL_64BITS)==0)
                                noData= Double.NaN;
                        else
                                if(st.compareTo(SampleDimensionType.SIGNED_16BITS)==0)
                                {
                                        noData=Short.MIN_VALUE;
                                        min=Short.MIN_VALUE;
                                        max=Short.MAX_VALUE;
                                }
                                else
                                        if(st.compareTo(SampleDimensionType.SIGNED_32BITS)==0)
                                        {
                                                noData= Integer.MIN_VALUE;
    
                                                min=Integer.MIN_VALUE;
                                                max=Integer.MAX_VALUE;                                                  
                                        }
                                        else
                                                if(st.compareTo(SampleDimensionType.SIGNED_8BITS)==0)
                                                {
                                                        noData= -128;
                                                        min=-128;
                                                        max=127;
                                                }
                                                else
                                                {
                                                        //unsigned
                                                        noData= 0;
                                                        min=0;
                                                        
                                                        
                                                        // compute max
                                                        if(st.compareTo(SampleDimensionType.UNSIGNED_1BIT)==0)
                                                                max=1;
                                                        else
                                                                if(st.compareTo(SampleDimensionType.UNSIGNED_2BITS)==0)
                                                                        max=3;
                                                                else
                                                                        if(st.compareTo(SampleDimensionType.UNSIGNED_4BITS)==0)
                                                                                max=7;
                                                                        else
                                                                                if(st.compareTo(SampleDimensionType.UNSIGNED_8BITS)==0)
                                                                                        max=255;
                                                                                else
                                                                                        if(st.compareTo(SampleDimensionType.UNSIGNED_16BITS)==0)
                                                                                                max=65535;
                                                                                        else
                                                                                                if(st.compareTo(SampleDimensionType.UNSIGNED_32BITS)==0)
                                                                                                        max=Math.pow(2, 32)-1;
                                                                                                                        
                                                }
                
                                     
        }
        bands[i] = new SimplifiedGridSampleDimension(
                        colorInterpretation.name(),
                        st,
                        colorInterpretation,
                        noData,
                        min,
                        max,
                        1,                                                      //no scale 
                        0,                                                      //no offset
                        null
                        ).geophysics(true);
        }
    
        return coverageFactory.create(
                rasterManager.getCoverageIdentifier(),
                image,
                new GridGeometry2D(
                        new GridEnvelope2D(PlanarImage.wrapRenderedImage(image).getBounds()), 
                        PixelInCell.CELL_CORNER,
                        finalGridToWorldCorner,
                        this.rasterManager.getCoverageCRS(),
                        hints),
                bands,
                null, 
                null);          

    }


    /**
     * This method is responsible for preparing the read param for doing an
     * {@link ImageReader#read(int, ImageReadParam)}.
     * 
     * 
     * <p>
     * This method is responsible for preparing the read param for doing an
     * {@link ImageReader#read(int, ImageReadParam)}. It sets the passed
     * {@link ImageReadParam} in terms of decimation on reading using the
     * provided requestedEnvelope and requestedDim to evaluate the needed
     * resolution. It also returns and {@link Integer} representing the index of
     * the raster to be read when dealing with multipage raster.
     * 
     * @param overviewPolicy
     *            it can be one of {@link Hints#VALUE_OVERVIEW_POLICY_IGNORE},
     *            {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST},
     *            {@link Hints#VALUE_OVERVIEW_POLICY_QUALITY} or
     *            {@link Hints#VALUE_OVERVIEW_POLICY_SPEED}. It specifies the
     *            policy to compute the levels level upon request.
     * @param readParams
     *            an instance of {@link ImageReadParam} for setting the
     *            subsampling factors.
     * @param requestedEnvelope
     *            the {@link GeneralEnvelope} we are requesting.
     * @param requestedDim
     *            the requested dimensions.
     * @return the index of the raster to read in the underlying data sourceFile.
     * @throws IOException
     * @throws TransformException
     */
    private int setReadParams(final OverviewPolicy overviewPolicy,
            final ImageReadParam readParams, final RasterLayerRequest request)
            throws IOException, TransformException {

        // Default image index 0
        int imageChoice = 0;
        // default values for subsampling
        readParams.setSourceSubsampling(1, 1, 0, 0);

        //
        // Init overview policy
        //
        // //
        // when policy is explictly provided it overrides the policy provided
        // using hints.
        final OverviewPolicy policy;
        if (overviewPolicy == null) {
            policy = rasterManager.overviewPolicy;
        } else {
            policy = overviewPolicy;
        }

        // requested to ignore levels
        if (policy.equals(OverviewPolicy.IGNORE)) {
            return imageChoice;
        }

        // levels and decimation
        imageChoice = ReadParamsController.setReadParams(
                request.getRequestedResolution(),
                request.getOverviewPolicy(),
                request.getDecimationPolicy(),
                baseReadParameters,
                request.rasterManager,
                request.rasterManager.overviewsController); // use general levels controller
        return imageChoice;
    }

    private RenderedImage postProcessRaster(RenderedImage image) {
        // alpha on the final mosaic
        if (transparentColor != null) {

            //
            // TRANSPARENT COLOR MANAGEMENT
            //
            //
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Support for alpha on input image ");
            }
            return ImageUtilities.maskColor(transparentColor, image);

        }
//        if (!needsReprojection){
//            try {
//                
//                // creating sourceFile grid to world corrected to the pixel corner
//                final AffineTransform sourceGridToWorld = new AffineTransform((AffineTransform) finalGridToWorldCorner);
//                
//                // target world to grid at the corner
//                final AffineTransform targetGridToWorld = new AffineTransform(request.getRequestedGridToWorld());
//                targetGridToWorld.concatenate(CoverageUtilities.CENTER_TO_CORNER);
//                
//                // target world to grid at the corner
//                final AffineTransform targetWorldToGrid=targetGridToWorld.createInverse();
//                // final complete transformation
//                targetWorldToGrid.concatenate(sourceGridToWorld);
//                
//                //update final grid to world
//                finalGridToWorldCorner=new AffineTransform2D(targetGridToWorld);
//                //
//                // Check and see if the affine transform is doing a copy.
//                // If so call the copy operation.
//                //
//                // we are in raster space here, so 1E-3 is safe
//                if(XAffineTransform.isIdentity(targetWorldToGrid, Utils.AFFINE_IDENTITY_EPS))
//                    return image;
//                
//                // create final image
//                // TODO this one could be optimized further depending on how the affine is created
//                //
//                // In case we are asked to use certain tile dimensions we tile
//                // also at this stage in case the read type is Direct since
//                // buffered images comes up untiled and this can affect the
//                // performances of the subsequent affine operation.
//                //
//                final Hints localHints= new Hints(hints);
//                if (hints != null && !hints.containsKey(JAI.KEY_BORDER_EXTENDER)) {
//                    final Object extender = hints.get(JAI.KEY_BORDER_EXTENDER);
//                    if (!(extender != null && extender instanceof BorderExtender)) {
//                        localHints.add(ImageUtilities.EXTEND_BORDER_BY_COPYING);
//                    }
//                }
//                ImageWorker iw = new ImageWorker(image);
//                iw.setRenderingHints(localHints);
//                iw.affine(targetWorldToGrid, new InterpolationNearest(), backgroundValues);
//                image = iw.getRenderedImage();
//            } catch (NoninvertibleTransformException e) {
//                if (LOGGER.isLoggable(Level.SEVERE)){
//                    LOGGER.log(Level.SEVERE, "Unable to create the requested mosaic ", e );
//                }
//            }
//        }
        return image;
    }
}
