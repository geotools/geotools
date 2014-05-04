/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;

import javax.media.jai.Interpolation;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.operation.Crop;
import org.geotools.coverage.processing.operation.Resample;
import org.geotools.coverage.processing.operation.Scale;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.i18n.ErrorKeys;
import org.geotools.renderer.i18n.Errors;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.sun.media.jai.util.Rational;

/**
 * @author Simone Giannecchini, GeoSolutions
 *
 */
final class GridCoverageRendererUtilities {



    static {
    
        // ///////////////////////////////////////////////////////////////////
        //
        // Caching parameters for performing the various operations.
        //      
        // ///////////////////////////////////////////////////////////////////
        final CoverageProcessor processor = new CoverageProcessor(new Hints(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE));
        RESAMPLING_PARAMS = processor.getOperation("Resample").getParameters();
        CROP_PARAMS = processor.getOperation("CoverageCrop").getParameters();
    }
    
    /** Cached factory for the {@link Crop} operation. */
     final static Crop CROP_FACTORY = new Crop();
     
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
    private static float RATIONAL_TOLERANCE = 0.000001F;
    /**
     * Checks whether the provided object is null or not. If it is null it
     * throws an {@link IllegalArgumentException} exception.
     * 
     * @param source
     *            the object to check.
     * @param node
     *            the operation we are trying to run.
     */
    static void ensureNotNull(final Object source, final String name) {
    	if (source == null)
    		throw new IllegalArgumentException(Errors.format(
    				ErrorKeys.NULL_ARGUMENT_$1, name));
    
    }

    /**
     * Checks whether the provided source object is null or not. If it is null
     * it throws an {@link IllegalArgumentException} exception.
     * 
     * @param source
     *            the object to check.
     * @param node
     *            the operation we are trying to run.
     */
    static void ensureSourceNotNull(final Object source,
    		final String name) {
    	if (source == null)
    		throw new IllegalArgumentException(Errors.format(
    				ErrorKeys.SOURCE_CANT_BE_NULL_$1, name));
    
    }

    static Rectangle2D layoutHelper(RenderedImage source,
                                        float scaleX,
                                        float scaleY,
                                        float transX,
                                        float transY,
                                        Interpolation interp) {
    
    // Represent the scale factors as Rational numbers.
    	// Since a value of 1.2 is represented as 1.200001 which
    	// throws the forward/backward mapping in certain situations.
    	// Convert the scale and translation factors to Rational numbers
    	Rational scaleXRational = Rational.approximate(scaleX,RATIONAL_TOLERANCE);
    	Rational scaleYRational = Rational.approximate(scaleY,RATIONAL_TOLERANCE);
    
    	long scaleXRationalNum = (long) scaleXRational.num;
    	long scaleXRationalDenom = (long) scaleXRational.denom;
    	long scaleYRationalNum = (long) scaleYRational.num;
    	long scaleYRationalDenom = (long) scaleYRational.denom;
    
    	Rational transXRational = Rational.approximate(transX,RATIONAL_TOLERANCE);
    	Rational transYRational = Rational.approximate(transY,RATIONAL_TOLERANCE);
    
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

    /** Parameters used to control the {@link Resample} operation. */
    final static ParameterValueGroup RESAMPLING_PARAMS;
    
    /** Parameters used to control the {@link Crop} operation. */
    static ParameterValueGroup CROP_PARAMS;
    
    /** Parameters used to control the {@link Scale} operation. */
    static final Resample RESAMPLE_FACTORY = new Resample();
    /**
     * Reprojecting the input coverage using the provided parameters.
     * 
     * @param gc
     * @param crs
     * @param interpolation
     * @return
     * @throws FactoryException
     */
    static GridCoverage2D reproject(
            final GridCoverage2D gc,
            CoordinateReferenceSystem crs, 
            final Interpolation interpolation,
            final GeneralEnvelope destinationEnvelope, 
            final double bkgValues[],
            final Hints hints) throws FactoryException {
        // paranoiac check
        assert CRS.equalsIgnoreMetadata(destinationEnvelope.getCoordinateReferenceSystem(), crs)
                || CRS.findMathTransform(destinationEnvelope.getCoordinateReferenceSystem(), crs)
                        .isIdentity();
    
        final ParameterValueGroup param = (ParameterValueGroup) RESAMPLING_PARAMS.clone();
        param.parameter("source").setValue(gc);
        param.parameter("CoordinateReferenceSystem").setValue(crs);
        param.parameter("InterpolationType").setValue(interpolation);
        if(bkgValues!=null){
            param.parameter("BackgroundValues").setValue(bkgValues);
        }
        return (GridCoverage2D) RESAMPLE_FACTORY.doOperation(param, hints);
    
    }

    /**
     * Cropping the provided coverage to the requested geographic area.
     * 
     * @param gc
     * @param envelope
     * @param crs
     * @return
     */
    static GridCoverage2D crop(
            GridCoverage2D gc,
            GeneralEnvelope envelope, 
            final Hints hints) {
        final GeneralEnvelope oldEnvelope = (GeneralEnvelope) gc.getEnvelope();
        // intersect the envelopes in order to prepare for cropping the coverage
        // down to the neded resolution
        final GeneralEnvelope intersectionEnvelope = new GeneralEnvelope(envelope);
        intersectionEnvelope.setCoordinateReferenceSystem(envelope.getCoordinateReferenceSystem());
        intersectionEnvelope.intersect((GeneralEnvelope) oldEnvelope);
    
        // Do we have something to show? After the crop I could get a null
        // coverage which would mean nothing to show.
        if (intersectionEnvelope.isEmpty()){
            return null;
        }
    
        // crop
        final ParameterValueGroup param = (ParameterValueGroup) CROP_PARAMS.clone();
        param.parameter("source").setValue(gc);
        param.parameter("Envelope").setValue(intersectionEnvelope);
        return (GridCoverage2D) CROP_FACTORY.doOperation(param, hints);
    
    }

    /**
     * @param inputEnvelope 
     * @return
     * @throws Exception 
     */
    static GeneralEnvelope reprojectEnvelope(GeneralEnvelope inputEnvelope, final CoordinateReferenceSystem outputCRS) throws Exception {

        GeneralEnvelope outputEnvelope=null;;
        CoordinateReferenceSystem inputCRS=inputEnvelope.getCoordinateReferenceSystem();
        if (!CRS.equalsIgnoreMetadata(inputCRS,
                outputCRS)) {
            outputEnvelope = CRS.transform(inputEnvelope, outputCRS);
            outputEnvelope.setCoordinateReferenceSystem(outputCRS);
        } 
        // simple copy
        if(outputEnvelope==null){
            outputEnvelope = new GeneralEnvelope(inputEnvelope);
            outputEnvelope.setCoordinateReferenceSystem(inputCRS);
        }
        return null;
    }

    /**
     * @param inputEnvelope
     * @param targetCRS
     * @return 
     * @throws Exception 
     */
    static GeneralEnvelope reprojectEnvelopeWithWGS84Pivot(GeneralEnvelope inputEnvelope,
            CoordinateReferenceSystem targetCRS) throws Exception {
        
        GridCoverageRendererUtilities.ensureNotNull(inputEnvelope, "destinationEnvelope");
        GridCoverageRendererUtilities.ensureNotNull(targetCRS, "coverageCRS");
        final CoordinateReferenceSystem destinationCRS=inputEnvelope.getCoordinateReferenceSystem();

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
            GeneralEnvelope output = CRS.transform(
                    CRS.findMathTransform(destinationCRS, targetCRS, true),//lenient
                    inputEnvelope);
            output.setCoordinateReferenceSystem(targetCRS);
            return output;
        } catch (TransformException te) {
            // //
            //
            // Convert the destination envelope to WGS84 if needed for safer
            // comparisons later on with the original crs of this coverage.
            //
            // //
            final GeneralEnvelope destinationEnvelopeWGS84=GridCoverageRendererUtilities.reprojectEnvelope(inputEnvelope,DefaultGeographicCRS.WGS84);

            // //
            //
            // Convert the destination envelope from WGS84 to the source crs
            // for cropping the provided coverage.
            //
            // //
            return GridCoverageRendererUtilities.reprojectEnvelope(destinationEnvelopeWGS84,targetCRS);

         }
        
    }

    /**
     * @param color
     * @return
     */
    public static double[] colorToArray(Color color) {
        if(color==null){
            return null;
        }
        
        return new double[]{
                color.getRed(),
                color.getGreen(),
                color.getBlue()
        };
    }


}
