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
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
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

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
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
import com.sun.media.jai.util.Rational;
import com.vividsolutions.jts.geom.Envelope;

/**
 * A helper class for rendering  {@link GridCoverage}  objects. 
 * @author  Simone Giannecchini, GeoSolutions SAS
 * @author  Andrea Aime, GeoSolutions SAS
 * @author  Alessio Fabiani, GeoSolutions SAS
 *
 * @source $URL$
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
                         System.out.println("Rendering debug dir "+debugDir);
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

    // FORMULAE FOR FORWARD MAP are derived as follows
    //     Nearest
    //        Minimum:
    //            srcMin = floor ((dstMin + 0.5 - trans) / scale)
    //            srcMin <= (dstMin + 0.5 - trans) / scale < srcMin + 1
    //            srcMin*scale <= dstMin + 0.5 - trans < (srcMin + 1)*scale
    //            srcMin*scale - 0.5 + trans
    //                       <= dstMin < (srcMin + 1)*scale - 0.5 + trans
    //            Let A = srcMin*scale - 0.5 + trans,
    //            Let B = (srcMin + 1)*scale - 0.5 + trans
    //
    //            dstMin = ceil(A)
    //
    //        Maximum:
    //            Note that srcMax is defined to be srcMin + dimension - 1
    //            srcMax = floor ((dstMax + 0.5 - trans) / scale)
    //            srcMax <= (dstMax + 0.5 - trans) / scale < srcMax + 1
    //            srcMax*scale <= dstMax + 0.5 - trans < (srcMax + 1)*scale
    //            srcMax*scale - 0.5 + trans
    //                       <= dstMax < (srcMax+1) * scale - 0.5 + trans
    //            Let float A = (srcMax + 1) * scale - 0.5 + trans
    //
    //            dstMax = floor(A), if floor(A) < A, else
    //            dstMax = floor(A) - 1
    //            OR dstMax = ceil(A - 1)
    //
    //     Other interpolations
    //
    //        First the source should be shrunk by the padding that is
    //        required for the particular interpolation. Then the
    //        shrunk source should be forward mapped as follows:
    //
    //        Minimum:
    //            srcMin = floor (((dstMin + 0.5 - trans)/scale) - 0.5)
    //            srcMin <= ((dstMin + 0.5 - trans)/scale) - 0.5 < srcMin+1
    //            (srcMin+0.5)*scale <= dstMin+0.5-trans <
    //                                                  (srcMin+1.5)*scale
    //            (srcMin+0.5)*scale - 0.5 + trans
    //                       <= dstMin < (srcMin+1.5)*scale - 0.5 + trans
    //            Let A = (srcMin+0.5)*scale - 0.5 + trans,
    //            Let B = (srcMin+1.5)*scale - 0.5 + trans
    //
    //            dstMin = ceil(A)
    //
    //        Maximum:
    //            srcMax is defined as srcMin + dimension - 1
    //            srcMax = floor (((dstMax + 0.5 - trans) / scale) - 0.5)
    //            srcMax <= ((dstMax + 0.5 - trans)/scale) - 0.5 < srcMax+1
    //            (srcMax+0.5)*scale <= dstMax + 0.5 - trans <
    //                                                   (srcMax+1.5)*scale
    //            (srcMax+0.5)*scale - 0.5 + trans
    //                       <= dstMax < (srcMax+1.5)*scale - 0.5 + trans
    //            Let float A = (srcMax+1.5)*scale - 0.5 + trans
    //
    //            dstMax = floor(A), if floor(A) < A, else
    //            dstMax = floor(A) - 1
    //            OR dstMax = ceil(A - 1)
    //
    
    
        private static float rationalTolerance = 0.000001F;

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
        final GridCoverage2D symbolizerGC;
        final RenderedImage symbolizerImage;
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine(new StringBuilder("Raster Symbolizer ").toString());
        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine(new StringBuffer("Raster Symbolizer ").toString());
        if(symbolizer!=null){
        	final RasterSymbolizerHelper rsp = new RasterSymbolizerHelper (preSymbolizer,this.hints);
        	rsp.visit(symbolizer);
        	symbolizerGC = (GridCoverage2D) rsp.getOutput();
        	symbolizerImage = symbolizerGC.geophysics(false).getRenderedImage();
    	} else {
            symbolizerGC = preSymbolizer;
            symbolizerImage = symbolizerGC.geophysics(false).getRenderedImage();
        }
        if (DEBUG) {
            writeRenderedImage(symbolizerImage,"postSymbolizer");
        }
        
        // ///////////////////////////////////////////////////////////////////
        // Apply opacity if needs be
        // TODO: move this into the RasterSymbolizerHelper
        // ///////////////////////////////////////////////////////////////////
        final RenderedImage finalImage;
        final GridCoverage2D finalGC;
        float opacity = getOpacity(symbolizer);
        if(opacity < 1) {
            ImageWorker ow = new ImageWorker(symbolizerImage);
            finalImage = ow.applyOpacity(opacity).getRenderedImage();
            
            final int numBands=finalImage.getSampleModel().getNumBands();
            final GridSampleDimension [] sd= new GridSampleDimension[numBands];
            for(int i=0;i<numBands;i++) {
                sd[i]= new GridSampleDimension(TypeMap.getColorInterpretation(finalImage.getColorModel(), i).name());
            }
            
            GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(hints);
            finalGC = factory.create(
                    "opacity_"+symbolizerGC.getName().toString(), 
                    finalImage,
                    symbolizerGC.getGridGeometry(),
                    sd,
                    new GridCoverage[]{symbolizerGC},
                    symbolizerGC.getProperties()
                    );
        } else {
            finalImage = symbolizerImage;
            finalGC = symbolizerGC;
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
        final AffineTransform finalRaster2Model = couple.getTransform();
        
        //paranoiac check to avoid that JAI freaks out when computing its internal layouT on images that are too small
        Rectangle2D finalLayout= layoutHelper(
                        finalImage, 
                        (float)finalRaster2Model.getScaleX(), 
                        (float)finalRaster2Model.getScaleY(), 
                        (float)finalRaster2Model.getTranslateX(), 
                        (float)finalRaster2Model.getTranslateY(), 
                        interpolation);
        if(finalLayout.isEmpty()){
                if(LOGGER.isLoggable(java.util.logging.Level.FINE))
                        LOGGER.fine("Unable to create a granuleDescriptor "+this.toString()+ " due to jai scale bug");
                return null;
        }

        // final transformation
        final ImageLayout layout = new ImageLayout(finalImage);
        layout.setTileGridXOffset(0).setTileGridYOffset(0).setTileHeight(tileSizeY).setTileWidth(tileSizeX);
        final RenderingHints localHints = this.hints.clone(); 
            localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
        //add hints to preserve IndexColorModel
        if(interpolation instanceof InterpolationNearest)
            localHints.add(new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE));
        //SG add hints for the border extender
        localHints.add(new RenderingHints(JAI.KEY_BORDER_EXTENDER,BorderExtender.createInstance(BorderExtender.BORDER_COPY)));
    	RenderedImage im=null;
    	try {
    	    ImageWorker iw = new ImageWorker(finalImage);
    	    iw.setRenderingHints(localHints);
    	    iw.affine(finalRaster2Model, interpolation, null);
    	    im = iw.getRenderedImage();
    	} finally {
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
        graphics.setRenderingHints(oldHints);

    }



    private static Rectangle2D layoutHelper(RenderedImage source,
                                        float scaleX,
                                        float scaleY,
                                        float transX,
                                        float transY,
                                        Interpolation interp) {
    
    // Represent the scale factors as Rational numbers.
    	// Since a value of 1.2 is represented as 1.200001 which
    	// throws the forward/backward mapping in certain situations.
    	// Convert the scale and translation factors to Rational numbers
    	Rational scaleXRational = Rational.approximate(scaleX,rationalTolerance);
    	Rational scaleYRational = Rational.approximate(scaleY,rationalTolerance);
    
    	long scaleXRationalNum = (long) scaleXRational.num;
    	long scaleXRationalDenom = (long) scaleXRational.denom;
    	long scaleYRationalNum = (long) scaleYRational.num;
    	long scaleYRationalDenom = (long) scaleYRational.denom;
    
    	Rational transXRational = Rational.approximate(transX,rationalTolerance);
    	Rational transYRational = Rational.approximate(transY,rationalTolerance);
    
    	long transXRationalNum = (long) transXRational.num;
    	long transXRationalDenom = (long) transXRational.denom;
    	long transYRationalNum = (long) transYRational.num;
    	long transYRationalDenom = (long) transYRational.denom;
    
    	int x0 = source.getMinX();
    	int y0 = source.getMinY();
    	int w = source.getWidth();
    	int h = source.getHeight();
    
    	// Variables to store the calculated destination upper left coordinate
    	long dx0Num, dx0Denom, dy0Num, dy0Denom;
    
    	// Variables to store the calculated destination bottom right
    	// coordinate
    	long dx1Num, dx1Denom, dy1Num, dy1Denom;
    
    	// Start calculations for destination
    
    	dx0Num = x0;
    	dx0Denom = 1;
    
    	dy0Num = y0;
    	dy0Denom = 1;
    
    	// Formula requires srcMaxX + 1 = (x0 + w - 1) + 1 = x0 + w
    	dx1Num = x0 + w;
    	dx1Denom = 1;
    
    	// Formula requires srcMaxY + 1 = (y0 + h - 1) + 1 = y0 + h
    	dy1Num = y0 + h;
    	dy1Denom = 1;
    
    	dx0Num *= scaleXRationalNum;
    	dx0Denom *= scaleXRationalDenom;
    
    	dy0Num *= scaleYRationalNum;
    	dy0Denom *= scaleYRationalDenom;
    
    	dx1Num *= scaleXRationalNum;
    	dx1Denom *= scaleXRationalDenom;
    
    	dy1Num *= scaleYRationalNum;
    	dy1Denom *= scaleYRationalDenom;
    
    	// Equivalent to subtracting 0.5
    	dx0Num = 2 * dx0Num - dx0Denom;
    	dx0Denom *= 2;
    
    	dy0Num = 2 * dy0Num - dy0Denom;
    	dy0Denom *= 2;
    
    	// Equivalent to subtracting 1.5
    	dx1Num = 2 * dx1Num - 3 * dx1Denom;
    	dx1Denom *= 2;
    
    	dy1Num = 2 * dy1Num - 3 * dy1Denom;
    	dy1Denom *= 2;
    
    	// Adding translation factors
    
    	// Equivalent to float dx0 += transX
    	dx0Num = dx0Num * transXRationalDenom + transXRationalNum * dx0Denom;
    	dx0Denom *= transXRationalDenom;
    
    	// Equivalent to float dy0 += transY
    	dy0Num = dy0Num * transYRationalDenom + transYRationalNum * dy0Denom;
    	dy0Denom *= transYRationalDenom;
    
    	// Equivalent to float dx1 += transX
    	dx1Num = dx1Num * transXRationalDenom + transXRationalNum * dx1Denom;
    	dx1Denom *= transXRationalDenom;
    
    	// Equivalent to float dy1 += transY
    	dy1Num = dy1Num * transYRationalDenom + transYRationalNum * dy1Denom;
    	dy1Denom *= transYRationalDenom;
    
    	// Get the integral coordinates
    	int l_x0, l_y0, l_x1, l_y1;
    
    	l_x0 = Rational.ceil(dx0Num, dx0Denom);
    	l_y0 = Rational.ceil(dy0Num, dy0Denom);
    
    	l_x1 = Rational.ceil(dx1Num, dx1Denom);
    	l_y1 = Rational.ceil(dy1Num, dy1Denom);
    
    	// Set the top left coordinate of the destination
    	final Rectangle2D retValue= new Rectangle2D.Double();
    	retValue.setFrame(l_x0, l_y0, l_x1 - l_x0 + 1, l_y1 - l_y0 + 1);
    	return retValue;
    }

}
