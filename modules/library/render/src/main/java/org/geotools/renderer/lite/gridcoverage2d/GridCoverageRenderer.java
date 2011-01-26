/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite.gridcoverage2d;

// J2SE dependencies
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.media.jai.BorderExtender;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.operator.AffineDescriptor;
import javax.media.jai.operator.ScaleDescriptor;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.operation.Crop;
import org.geotools.coverage.processing.operation.Resample;
import org.geotools.coverage.processing.operation.Scale;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.styling.RasterSymbolizer;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.filter.expression.Expression;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

import com.sun.media.jai.opimage.RIFUtil;
import com.sun.media.jai.opimage.TranslateIntOpImage;
import com.vividsolutions.jts.geom.Envelope;

/**
 * A helper class for rendering  {@link GridCoverage}  objects. 
 * @author  Simone Giannecchini, GeoSolutions SAS
 * @author  Andrea Aime, GeoSolutions SAS
 * @author  Alessio Fabiani, GeoSolutions SAS
 * @source  $URL$
 * @version  $Id$
 *
 */
@SuppressWarnings("deprecation")
public final class GridCoverageRenderer { 
    
    /**
     * This variable is use for testing purposes in order to force this
     * {@link GridCoverageRenderer} to dump images at various steps on the disk.
     */
    private static boolean DEBUG = Boolean
            .getBoolean("org.geotools.renderer.lite.gridcoverage2d.debug");

    private static String debugDir;
    static {
        if (DEBUG) {
            final File tempDir = new File(System.getProperty("user.home"),"gt-renderer");
            if (!tempDir.exists() ) {
                if(!tempDir.mkdir())
                System.out
                        .println("Unable to create debug dir, exiting application!!!");
                DEBUG=false;
                debugDir = null;
            } else
               {
                        debugDir = tempDir.getAbsolutePath();
                         System.out.println("Debug dir "+debugDir);
               }
        }

    }
    
    /**
     * Simple pair class for holding a {@link GridCoverage2D} with the final WorldToGrid.
     * 
     * @author Simone Giannecchini, GeoSolutions SAS.
     * 
     */
    private final static class GCpair {

        private final AffineTransform transform;

        private final GridCoverage2D gc;

        public GCpair(AffineTransform transform, GridCoverage2D gc) {
            this.transform = transform;
            this.gc = gc;
        }

        public AffineTransform getTransform() {
            return transform;
        }

        public GridCoverage2D getGridCoverage() {
            return gc;
        }
    }
    
    /**
     * Helper function
     * * @param symbolizer 
     */
    static float getOpacity(RasterSymbolizer symbolizer) {
            float alpha = 1.0f;
            Expression exp = symbolizer.getOpacity();
            if (exp == null){
                    return alpha;
            }
            Number number = (Number) exp.evaluate(null,Float.class);
            if (number == null){
                    return alpha;
            }
            return number.floatValue();
    } 


    /** Cached factory for the {@link Crop} operation. */
    private final static Crop coverageCropFactory = new Crop();

    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.rendering");

    static {

        // ///////////////////////////////////////////////////////////////////
        //
        // Caching parameters for performing the various operations.
        //	
        // ///////////////////////////////////////////////////////////////////
        final CoverageProcessor processor = new CoverageProcessor(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));
        resampleParams = processor.getOperation("Resample").getParameters();
        cropParams = processor.getOperation("CoverageCrop").getParameters();
    }

    /** The Display (User defined) CRS * */
    private final CoordinateReferenceSystem destinationCRS;

    /** Area we want to draw. */
    private final GeneralEnvelope destinationEnvelope;

    /** Size of the area we want to draw in pixels. */
    private final Rectangle destinationSize;

    private final AffineTransform finalGridToWorld;

    private final AffineTransform finalWorldToGrid;

    private final Hints hints = new Hints();

    /** Parameters used to control the {@link Resample} operation. */
    private final static ParameterValueGroup resampleParams;

    /** Parameters used to control the {@link Crop} operation. */
    private static ParameterValueGroup cropParams;

    /** Parameters used to control the {@link Scale} operation. */
    private static final Resample resampleFactory = new Resample();
    

    /**
     * Creates a new {@link GridCoverageRenderer} object.
     * 
     * @param destinationCRS
     *                the CRS of the {@link GridCoverage2D} to render.
     * @param envelope
     *                delineating the area to be rendered.
     * @param screenSize
     *                at which we want to rendere the source
     *                {@link GridCoverage2D}.
     * @param worldToScreen if not <code>null</code> and if it contains a rotation,
     * this Affine Tranform is used directly to convert from world coordinates
     * to screen coordinates. Otherwise, a standard {@link GridToEnvelopeMapper}
     * is used to calculate the affine transform.
     * 
     * @throws TransformException
     * @throws NoninvertibleTransformException
     */
    public GridCoverageRenderer(final CoordinateReferenceSystem destinationCRS,
            final Envelope envelope, Rectangle screenSize, AffineTransform worldToScreen)
            throws TransformException, NoninvertibleTransformException {

        this(destinationCRS, envelope, screenSize, worldToScreen, null);

    }
    


    /**
     * Creates a new {@link GridCoverageRenderer} object.
     * 
     * @param destinationCRS
     *                the CRS of the {@link GridCoverage2D} to render.
     * @param envelope
     *                delineating the area to be rendered.
     * @param screenSize
     *                at which we want to rendere the source
     *                {@link GridCoverage2D}.
     * @param worldToScreen if not <code>null</code> and if it contains a rotation,
     * this Affine Tranform is used directly to convert from world coordinates
     * to screen coordinates. Otherwise, a standard {@link GridToEnvelopeMapper}
     * is used to calculate the affine transform.
     * 
     * @param hints
     *                {@link RenderingHints} to control this rendering process.
     * @throws TransformException
     * @throws NoninvertibleTransformException
     */
    public GridCoverageRenderer(
            final CoordinateReferenceSystem destinationCRS,
            final Envelope envelope, 
            final Rectangle screenSize,
            final AffineTransform worldToScreen, 
            final RenderingHints hints) throws TransformException,
            NoninvertibleTransformException {

        // ///////////////////////////////////////////////////////////////////
        //
        // Initialize this renderer
        //
        // ///////////////////////////////////////////////////////////////////
        this.destinationSize = screenSize;
        this.destinationCRS = CRS.getHorizontalCRS(destinationCRS);
        if (this.destinationCRS == null)
            throw new TransformException(Errors.format(
                    ErrorKeys.CANT_SEPARATE_CRS_$1, destinationCRS));
        destinationEnvelope = new GeneralEnvelope(new ReferencedEnvelope(envelope, destinationCRS));
        // ///////////////////////////////////////////////////////////////////
        //
        // FINAL DRAWING DIMENSIONS AND RESOLUTION
        // I am here getting the final drawing dimensions (on the device) and
        // the resolution for this rendererbut in the CRS of the source coverage
        // since I am going to compare this info with the same info for the
        // source coverage.
        //
        // ///////////////////////////////////////////////////////////////////
        
        // PHUSTAD : The gridToEnvelopeMapper does not handle rotated views. 
        // 
        if (worldToScreen != null && XAffineTransform.getRotation(worldToScreen) != 0.0) { 
            finalWorldToGrid = new AffineTransform(worldToScreen);
            finalGridToWorld = finalWorldToGrid.createInverse();
        } else {
            final GridToEnvelopeMapper gridToEnvelopeMapper = new GridToEnvelopeMapper();
            gridToEnvelopeMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
            gridToEnvelopeMapper.setGridRange(new GridEnvelope2D(destinationSize));       
            gridToEnvelopeMapper.setEnvelope(destinationEnvelope);            
            finalGridToWorld = new AffineTransform(gridToEnvelopeMapper.createAffineTransform());
            finalWorldToGrid = finalGridToWorld.createInverse();
        }

        //
        // HINTS
        //
        if (hints != null)
            this.hints.add(hints);
        // this prevents users from overriding lenient hint
        this.hints.put(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);
        this.hints.add(ImageUtilities.DONT_REPLACE_INDEX_COLOR_MODEL);

    }

    /**
     * Reprojecting the input coverage using the provided parameters.
     * 
     * @param gc
     * @param crs
     * @param interpolation
     * @return
     * @throws FactoryException
     */
    private static GridCoverage2D resample(final GridCoverage2D gc,
            CoordinateReferenceSystem crs, final Interpolation interpolation,
            final GeneralEnvelope destinationEnvelope, final Hints hints) throws FactoryException {
        // paranoiac check
        assert CRS.equalsIgnoreMetadata(destinationEnvelope
                .getCoordinateReferenceSystem(), crs)
                || CRS
                        .findMathTransform(
                                destinationEnvelope
                                        .getCoordinateReferenceSystem(), crs)
                        .isIdentity();

        final ParameterValueGroup param = (ParameterValueGroup) resampleParams
                .clone();
        param.parameter("source").setValue(gc);
        param.parameter("CoordinateReferenceSystem").setValue(crs);
        param.parameter("InterpolationType").setValue(interpolation);
        return (GridCoverage2D) resampleFactory.doOperation(param, hints);

    }

    /**
     * Cropping the provided coverage to the requested geographic area.
     * 
     * @param gc
     * @param envelope
     * @param crs
     * @return
     */
    private static GridCoverage2D getCroppedCoverage(GridCoverage2D gc,
            GeneralEnvelope envelope, CoordinateReferenceSystem crs, final Hints hints) {
        final GeneralEnvelope oldEnvelope = (GeneralEnvelope) gc.getEnvelope();
        // intersect the envelopes in order to prepare for crooping the coverage
        // down to the neded resolution
        final GeneralEnvelope intersectionEnvelope = new GeneralEnvelope(
                envelope);
        intersectionEnvelope.setCoordinateReferenceSystem(crs);
        intersectionEnvelope.intersect((GeneralEnvelope) oldEnvelope);

        // Do we have something to show? After the crop I could get a null
        // coverage which would mean nothing to show.
        if (intersectionEnvelope.isEmpty())
            return null;

        // crop
        final ParameterValueGroup param = (ParameterValueGroup) cropParams
                .clone();
        param.parameter("source").setValue(gc);
        param.parameter("Envelope").setValue(intersectionEnvelope);
        return (GridCoverage2D) coverageCropFactory.doOperation(param, hints);

    }

    /**
     * Write the provided {@link RenderedImage} in the debug directory with the provided file name.
     * 
     * @param raster
     *            the {@link RenderedImage} that we have to write.
     * @param fileName
     *            a {@link String} indicating where we should write it.
     */
    static void writeRenderedImage(final RenderedImage raster, final String fileName) {
        if (debugDir == null)
            throw new NullPointerException(
                    "Unable to write the provided coverage in the debug directory");
        if (DEBUG == false)
            throw new IllegalStateException(
                    "Unable to write the provided coverage since we are not in debug mode");
        try {
            ImageIO.write(raster, "tiff", new File(debugDir, fileName + ".tiff"));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
    }

    /**
     * Builds a (RenderedImage, AffineTransform) pair that can be used for rendering onto a
     * {@link Graphics2D} or as the basis to build a final image. Will return null if there is
     * nothing to render.
     * 
     * @param gridCoverage
     * @param symbolizer
     * @return
     * @throws FactoryException
     * @throws TransformException
     * @throws NoninvertibleTransformException
     */
    private GCpair prepareFinalImage(
            final GridCoverage2D gridCoverage,
            final RasterSymbolizer symbolizer
            )throws FactoryException, TransformException,NoninvertibleTransformException {

        // Initial checks
    	if(gridCoverage==null)
    		throw new NullPointerException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"gridCoverage"));
    	
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("Drawing coverage "+gridCoverage.toString());
        

        //
        // Getting information about the source coverage like the source CRS,
        // the source envelope and the source geometry.
        //
        final CoordinateReferenceSystem sourceCoverageCRS = gridCoverage.getCoordinateReferenceSystem2D();
        final GeneralEnvelope sourceCoverageEnvelope = (GeneralEnvelope) gridCoverage.getEnvelope();


        //
        // GET THE CRS MAPPING
        //
        // This step I instantiate the MathTransform for going from the source
        // crs to the destination crs.
        //
        // math transform from source to target crs
        final MathTransform sourceCRSToDestinationCRSTransformation = CRS.findMathTransform(sourceCoverageCRS, destinationCRS, true);
        final MathTransform destinationCRSToSourceCRSTransformation = sourceCRSToDestinationCRSTransformation.inverse();
        final boolean doReprojection = !sourceCRSToDestinationCRSTransformation.isIdentity();
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("Transforming coverage envelope with transform "+destinationCRSToSourceCRSTransformation.toWKT());
        
        // //
        //
        // Do we need reprojection?
        //
        // //
        GeneralEnvelope destinationEnvelopeInSourceCRS;
        if (doReprojection) {
            // /////////////////////////////////////////////////////////////////////
            //
            // PHASE 1
            //
            // PREPARING THE REQUESTED ENVELOPE FOR LATER INTERSECTION
            //
            // /////////////////////////////////////////////////////////////////////

            // //
            //
            // Try to convert the destination envelope in the source crs. If
            // this fails we pass through WGS84 as an intermediate step
            //
            // //
            try {
                // convert the destination envelope to the source coverage
                // native crs in order to try and crop it. If we get an error we
                // try to
                // do this in two steps using WGS84 as a pivot. This introduces
                // some erros (it usually
                // increases the envelope we want to check) but it is still
                // useful.
                destinationEnvelopeInSourceCRS = CRS.transform(
                        destinationCRSToSourceCRSTransformation,
                        destinationEnvelope);
            } catch (TransformException te) {
                // //
                //
                // Convert the destination envelope to WGS84 if needed for safer
                // comparisons later on with the original crs of this coverage.
                //
                // //
                final GeneralEnvelope destinationEnvelopeWGS84;
                if (!CRS.equalsIgnoreMetadata(destinationCRS,
                        DefaultGeographicCRS.WGS84)) {
                    // get a math transform to go to WGS84
                    final MathTransform destinationCRSToWGS84transformation = CRS
                            .findMathTransform(destinationCRS,
                                    DefaultGeographicCRS.WGS84, true);
                    if (!destinationCRSToWGS84transformation.isIdentity()) {
                        destinationEnvelopeWGS84 = CRS.transform(
                                destinationCRSToWGS84transformation,
                                destinationEnvelope);
                        destinationEnvelopeWGS84
                                .setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
                    } else {
                        destinationEnvelopeWGS84 = new GeneralEnvelope(
                                destinationEnvelope);
                    }

                } else {
                    destinationEnvelopeWGS84 = new GeneralEnvelope(
                            destinationEnvelope);
                }

                // //
                //
                // Convert the requested envelope from WGS84 to the source crs
                // for cropping the provided coverage.
                //
                // //
                if (!CRS.equalsIgnoreMetadata(sourceCoverageCRS,
                        DefaultGeographicCRS.WGS84)) {
                    // get a math transform to go to WGS84
                    final MathTransform WGS84ToSourceCoverageCRSTransformation = CRS
                            .findMathTransform(DefaultGeographicCRS.WGS84,
                                    sourceCoverageCRS, true);
                    if (!WGS84ToSourceCoverageCRSTransformation.isIdentity()) {
                        destinationEnvelopeInSourceCRS = CRS.transform(
                                WGS84ToSourceCoverageCRSTransformation,
                                destinationEnvelopeWGS84);
                        destinationEnvelopeInSourceCRS
                                .setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
                    } else {
                        destinationEnvelopeInSourceCRS = new GeneralEnvelope(
                                destinationEnvelopeWGS84);
                    }
                } else {
                    destinationEnvelopeInSourceCRS = new GeneralEnvelope(
                            destinationEnvelopeWGS84);
                }

            }
        } else
            destinationEnvelopeInSourceCRS = new GeneralEnvelope(destinationEnvelope);
        // /////////////////////////////////////////////////////////////////////
        //
        // NOW CHECKING THE INTERSECTION IN WGS84
        //
        // //
        //
        // If the two envelopes intersect each other in WGS84 we are
        // reasonably sure that they intersect
        //
        // /////////////////////////////////////////////////////////////////////
        final GeneralEnvelope intersectionEnvelope = new GeneralEnvelope(destinationEnvelopeInSourceCRS);
        intersectionEnvelope.intersect(sourceCoverageEnvelope);
        if (intersectionEnvelope.isEmpty()||intersectionEnvelope.isNull()) {
            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info("The destination envelope does not intersect the envelope of the source coverage.");
            }
            return null;
        }


        final Interpolation interpolation = (Interpolation) hints.get(JAI.KEY_INTERPOLATION);
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("Using interpolation "+interpolation);


        // /////////////////////////////////////////////////////////////////////
        //
        // CROPPING Coverage
        //
        // /////////////////////////////////////////////////////////////////////
        GridCoverage2D preResample=gridCoverage;
    	try{
		    preResample = getCroppedCoverage(gridCoverage, intersectionEnvelope, sourceCoverageCRS,this.hints);
		    if (preResample == null) {
		        // nothing to render, the AOI does not overlap
		        if (LOGGER.isLoggable(Level.FINE))
		            LOGGER.fine("Skipping current coverage because cropped to an empty area");
                return null;
		    }
    	}catch (Throwable t) {
    		////
    		//
    		// If it happens that the crop fails we try to proceed since the crop does only an optimization. Things might
    		// work out anyway.
    		//
    		////
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE,"Crop Failed for reason: "+t.getLocalizedMessage(),t);
            preResample=gridCoverage;
		}
        if (DEBUG) {
            writeRenderedImage(preResample.geophysics(false).getRenderedImage(),"preresample");
        }
            
        
        // /////////////////////////////////////////////////////////////////////
        //
        // REPROJECTION if needed
        //
        // /////////////////////////////////////////////////////////////////////
        GridCoverage2D preSymbolizer;
        if (doReprojection) {
            preSymbolizer = resample(preResample, destinationCRS,interpolation == null ? new InterpolationNearest(): interpolation, destinationEnvelope,this.hints);
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("Reprojecting to crs "+ destinationCRS.toWKT());
        } else
            preSymbolizer = preResample;
        if (DEBUG) {
            writeRenderedImage(preSymbolizer.geophysics(false).getRenderedImage(),"preSymbolizer");
        }


        // ///////////////////////////////////////////////////////////////////
        //
        // RASTERSYMBOLIZER
        //
        // ///////////////////////////////////////////////////////////////////
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine(new StringBuilder("Raster Symbolizer ").toString());
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine(new StringBuffer("Raster Symbolizer ").toString());
        final RenderedImage finalImage;
        final GridCoverage2D finalGC;
        if(symbolizer!=null){
        	final RasterSymbolizerHelper rsp = new RasterSymbolizerHelper (preSymbolizer,this.hints);
        	rsp.visit(symbolizer);
        	finalGC = (GridCoverage2D) rsp.getOutput();
        	finalImage = finalGC.geophysics(false).getRenderedImage();
    	}
        else{
        	finalGC=preSymbolizer;
        	finalImage=finalGC.geophysics(false).getRenderedImage();
        }
        if (DEBUG) {
            writeRenderedImage(finalImage,"postSymbolizer");
        }
        
        // ///////////////////////////////////////////////////////////////////
        //
        // DRAW ME
        // I need the grid to world transform for drawing this grid coverage to
        // the display
        //
        // ///////////////////////////////////////////////////////////////////
        final GridGeometry2D recoloredCoverageGridGeometry = ((GridGeometry2D) finalGC.getGridGeometry());
        // I need to translate half of a pixel since in wms the envelope
        // map to the corners of the raster space not to the center of the
        // pixels.
        final MathTransform2D finalGCTransform=recoloredCoverageGridGeometry.getGridToCRS2D(PixelOrientation.UPPER_LEFT);
        if (!(finalGCTransform instanceof AffineTransform)) {
            throw new UnsupportedOperationException(
                    "Non-affine transformations not yet implemented"); // TODO
        }
        final AffineTransform finalGCgridToWorld = new AffineTransform((AffineTransform) finalGCTransform);


        // //
        //
        // I am going to concatenate the final world to grid transform for the
        // screen area with the grid to world transform of the input coverage.
        //
        // This way i right away position the coverage at the right place in the
        // area of interest for the device.
        //
        // //
        final AffineTransform clonedFinalWorldToGrid = (AffineTransform) finalWorldToGrid.clone();
        clonedFinalWorldToGrid.concatenate(finalGCgridToWorld);

        return new GCpair(clonedFinalWorldToGrid,finalGC);
    }            	

    /**
     * Turns the coverage into a rendered image applying the necessary transformations and the
     * symbolizer
     * 
     * @param gridCoverage
     * @param symbolizer
     * @return The transformed image, or null if the coverage does not lie within the rendering
     *         bounds
     * @throws FactoryException
     * @throws TransformException
     * @throws NoninvertibleTransformException
     */
    public RenderedImage renderImage(
            final GridCoverage2D gridCoverage,
            final RasterSymbolizer symbolizer, 
            final Interpolation interpolation, 
            final Color background,
            final int tileSizeX, 
            final int tileSizeY
            ) throws FactoryException, TransformException, NoninvertibleTransformException {

        // Build the final image and the associated world to grid transformation
        final GCpair couple = prepareFinalImage(gridCoverage, symbolizer);
        if (couple == null)
            return null;
        // NOTICE that at this stage the image we get should be 8 bits, either RGB, RGBA, Gray, GrayA
        // either multiband or indexed. It could also be 16 bits indexed!!!!
        
        final RenderedImage finalImage = couple.getGridCoverage().getRenderedImage();
        final AffineTransform clonedFinalWorldToGrid = couple.getTransform();

        // TODO: optimize translate/scale transformations
        // TODO: use mosaic to merge with a background respecting alpha and transparency
        // TODO: check tolerance value
        // TODO: do we need to pass in any hints?
        
        boolean hasScaleX=!(Math.abs(clonedFinalWorldToGrid.getScaleX()-1) < 1E-2/(finalImage.getWidth()+1-finalImage.getMinX()));
        boolean hasScaleY=!(Math.abs(clonedFinalWorldToGrid.getScaleY()-1) < 1E-2/(finalImage.getHeight()+1-finalImage.getMinY()));
        boolean hasShearX=!(clonedFinalWorldToGrid.getShearX() == 0.0);
        boolean hasShearY=!(clonedFinalWorldToGrid.getShearY() == 0.0);
        boolean hasTranslateX=!(Math.abs(clonedFinalWorldToGrid.getTranslateX()) <  1E-2);
        boolean hasTranslateY=!(Math.abs(clonedFinalWorldToGrid.getTranslateY()) <  1E-2);
        boolean isTranslateXInt=!(Math.abs(clonedFinalWorldToGrid.getTranslateX() - (int) clonedFinalWorldToGrid.getTranslateX()) <  1E-2);
        boolean isTranslateYInt=!(Math.abs(clonedFinalWorldToGrid.getTranslateY() - (int) clonedFinalWorldToGrid.getTranslateY()) <  1E-2);
        
        boolean isIdentity = clonedFinalWorldToGrid.isIdentity() && !hasScaleX&&!hasScaleY &&!hasTranslateX&&!hasTranslateY;
        boolean isScale = hasScaleX&&hasScaleY &&!hasShearX&&!hasShearY;
        
        // TODO how can we check that the a skew is harmless????
        if(isIdentity){
            // TODO check if we are missing anything like tiling or such that comes from hints 
            return finalImage;
        }
        
        // TOLERANCE ON PIXELS SIZE
        
        // Check and see if the affine transform is in fact doing
        // a Translate operation. That is a scale by 1 and no rotation.
        // In which case call translate. Note that only integer translate
        // is applicable. For non-integer translate we'll have to do the
        // affine.
        // If the hints contain an ImageLayout hint, we can't use 
        // TranslateIntOpImage since it isn't capable of dealing with that.
        // Get ImageLayout from renderHints if any.
        ImageLayout layout_ = RIFUtil.getImageLayoutHint(hints);                                
        if ( !hasScaleX &&
             !hasScaleY  &&
              !hasShearX&&
              !hasShearY&&
              isTranslateXInt&&
              isTranslateYInt&&
            layout_ == null) {
            // It's a integer translate
            return new TranslateIntOpImage(finalImage,
            								hints,
                                           (int) clonedFinalWorldToGrid.getShearX(),
                                           (int) clonedFinalWorldToGrid.getShearY());
        }                                
                          
        // final transformation
        final ImageLayout layout = new ImageLayout(finalImage);
        layout.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(tileSizeY).setTileWidth(tileSizeX);
        final RenderingHints hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
        //add hints to preserve IndexColorModel
        if(interpolation instanceof InterpolationNearest)
            hints.add(new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE));
        //SG add hints for the border extender
        hints.add(new RenderingHints(JAI.KEY_BORDER_EXTENDER,BorderExtender.createInstance(BorderExtender.BORDER_COPY)));
    	RenderedImage im=null;
    	try{
    		// scale ?
    		if (isScale){
    			im=ScaleDescriptor.create(	finalImage, 
    									(float) clonedFinalWorldToGrid.getScaleX(),
    									(float) clonedFinalWorldToGrid.getScaleY(),
    									(float) clonedFinalWorldToGrid.getTranslateX(), 
    									(float) clonedFinalWorldToGrid.getTranslateY(), 
    									interpolation, 
    									hints);
    		}else{
                    // use more general affine (but slower)
                    im = AffineDescriptor.create(
                            finalImage, 
                            clonedFinalWorldToGrid, 
                            interpolation,
                            null, 
                            hints);
    		}
    	}finally{
    		if(DEBUG)
    			writeRenderedImage(im, "postAffine");
    	}
    	return im;

    }
    
    /**
     * Paint this grid coverage. The caller must ensure that
     * <code>graphics</code> has an affine transform mapping "real world"
     * coordinates in the coordinate system given by {@link
     * #getCoordinateSystem}.
     * 
     * @param graphics
     *                the {@link Graphics2D} context in which to paint.
     * @param metaBufferedEnvelope
     * @throws FactoryException
     * @throws TransformException
     * @throws NoninvertibleTransformException
     * @throws Exception
     * @throws UnsupportedOperationException
     *                 if the transformation from grid to coordinate system in
     *                 the GridCoverage is not an AffineTransform
     */
    public void paint(
            final Graphics2D graphics,
            final GridCoverage2D gridCoverage, 
            final RasterSymbolizer symbolizer)
            throws FactoryException, TransformException,
            NoninvertibleTransformException {

        //
        // Initial checks
        //
        if(graphics==null)
            throw new NullPointerException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"graphics"));
        if(gridCoverage==null)
            throw new NullPointerException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"gridCoverage"));
        
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine(new StringBuilder("Drawing coverage ").append(gridCoverage.toString()).toString());

        final RenderingHints oldHints = graphics.getRenderingHints();
        graphics.setRenderingHints(this.hints);
        
        
        // Build the final image and the transformation
        GCpair couple = prepareFinalImage(gridCoverage, symbolizer);
        if (couple == null)
            return;

        RenderedImage finalImage = couple.getGridCoverage().getRenderedImage();
        AffineTransform clonedFinalWorldToGrid = couple.getTransform();

        // //
        // Opacity
        // //
        final float alpha = getOpacity(symbolizer);
        final Composite oldAlphaComposite = graphics.getComposite();
        graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        try {
            //debug
            if (DEBUG) {
                writeRenderedImage(finalImage,"final");
            }               

            // //
            // Drawing the Image
            // //
            graphics.drawRenderedImage(finalImage, clonedFinalWorldToGrid);
            
        } catch (Throwable t) {
            try {
                //log the error
                if(LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE,t.getLocalizedMessage(),t);              
                
                // /////////////////////////////////////////////////////////////
                // this is a workaround for a bug in Java2D, we need to convert
                // the image to component color model to make it work just fine.
                // /////////////////////////////////////////////////////////////
                if(t instanceof IllegalArgumentException){
                    if (DEBUG) {
                        writeRenderedImage(finalImage,"preWORKAROUND1");
                    }
                    final RenderedImage image = new ImageWorker(finalImage).forceComponentColorModel(true).getRenderedImage();
                    
                    if (DEBUG) {
                        writeRenderedImage(image,"WORKAROUND1");
                        
                    }
                    graphics.drawRenderedImage(image, clonedFinalWorldToGrid);
                
                }
                else if(t instanceof ImagingOpException)
                // /////////////////////////////////////////////////////////////
                // this is a workaround for a bug in Java2D
                // (see bug 4723021
                // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4723021).
                //
                // AffineTransformOp.filter throws a
                // java.awt.image.ImagingOpException: Unable to tranform src
                // image when a PixelInterleavedSampleModel is used.
                //
                // CUSTOMER WORKAROUND :
                // draw the BufferedImage into a buffered image of type ARGB
                // then perform the affine transform. THIS OPERATION WASTES
                // RESOURCES BY PERFORMING AN ALLOCATION OF MEMORY AND A COPY ON
                // LARGE IMAGES.
                // /////////////////////////////////////////////////////////////
                {
                    BufferedImage buf = 
                        finalImage.getColorModel().hasAlpha()?
                                new BufferedImage(finalImage.getWidth(), finalImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR):
                                new BufferedImage(finalImage.getWidth(), finalImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
                                if (DEBUG) {
                                    writeRenderedImage(buf,"preWORKAROUND2");
                                }
                    final Graphics2D g = (Graphics2D) buf.getGraphics();
                    final int translationX=finalImage.getMinX(),translationY=finalImage.getMinY();
                    g.drawRenderedImage(finalImage, AffineTransform.getTranslateInstance(-translationX, -translationY));
                    g.dispose();
                    if (DEBUG) {
                         writeRenderedImage(buf,"WORKAROUND2");
                    }
                    clonedFinalWorldToGrid.concatenate(AffineTransform.getTranslateInstance(translationX, translationY));
                    graphics.drawImage(buf, clonedFinalWorldToGrid, null);
                    //release
                    buf.flush();
                    buf=null;                   
                }
                else 
                    //log the error
                    if(LOGGER.isLoggable(Level.FINE))
                        LOGGER.fine("Unable to renderer this raster, no workaround found");


            } catch (Throwable t1) {
                // if the workaround fails again, there is really nothing to do
                // :-(
                LOGGER.log(Level.WARNING, t1.getLocalizedMessage(), t1);
            }
        }

        // ///////////////////////////////////////////////////////////////////
        //
        // Restore old elements
        //
        // ///////////////////////////////////////////////////////////////////
        graphics.setComposite(oldAlphaComposite);
        graphics.setRenderingHints(oldHints);

    }

}
