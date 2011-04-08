/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.processing;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.ROI;
import javax.media.jai.registry.RenderedRegistryMode;

/**
 * A Artifacts Filter operation descriptor.
 * 
 * Given an input image and a ROI, set the values of pixels outside the ROI
 * to the background value and transform the pixels along the border of the ROI, if less than a
 * specified Luminance threshold value, to a mean of all sourrounding pixels within ROI, having 
 * Luminance greater than threshold. 
 * 
 * @author Daniele Romagnoli, GeoSolutions SAS
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
 public class ArtifactsFilterDescriptor extends OperationDescriptorImpl {

    private static final String[] srcImageNames = {"sourceImage"};

    private static final Class<?>[][] srcImageClasses = {{RenderedImage.class}};

    static final int ROI_ARG = 0;
    static final int BACKGROUND_ARG = 1;
    static final int THRESHOLD_ARG = 2;
    static final int FILTERSIZE_ARG = 3;
    
    private static final int DEFAULT_FILTER_SIZE = 3;
    
    private static final int DEFAULT_THRESHOLD = 10;

    private static final String[] paramNames = {"roi", "backgroundValues", "threshold", "filterSize"};

    private static final Class<?>[] paramClasses = {ROI.class, double[].class, Integer.class, Integer.class};

    private static final Object[] paramDefaults = {(ROI) null, (double[]) null, DEFAULT_THRESHOLD, DEFAULT_FILTER_SIZE} ;

    /** Constructor. */
    public ArtifactsFilterDescriptor() {
        super(new String[][]{
                {"GlobalName", "ArtifactsFilter"},
                {"LocalName", "ArtifactsFilter"},
                {"Vendor", "org.geotools.gce.imagemosaic.processing"},
                {"Description", "Filter pixels along the ROI border with Luminance value less than threshold"},
                {"DocURL", ""},
                {"Version", "1.0.0"},
                {"arg0Desc", String.format("%s (default %s) - a ROI defining working area",
                                paramNames[ROI_ARG], paramDefaults[ROI_ARG])},
                {"arg1Desc", String.format("%s (default %s) - an array of double that define values " 
                                + "for the background ",
                                paramNames[BACKGROUND_ARG], paramDefaults[BACKGROUND_ARG])},
                {"arg2Desc", String.format("%s (default %s) - an integer defining the luminance threshold value",
                                paramNames[THRESHOLD_ARG], paramDefaults[THRESHOLD_ARG]) },
                {"arg3Desc", String.format("%s (default %s) - an integer defining the filterSize",
                            paramNames[FILTERSIZE_ARG], paramDefaults[FILTERSIZE_ARG])},
                },

            new String[]{RenderedRegistryMode.MODE_NAME}, // supported modes
                    srcImageNames, srcImageClasses,
                    paramNames, paramClasses, paramDefaults,
                    null // valid values (none defined)
            );
    }

    public static RenderedImage create( RenderedImage sourceImage, ROI sourceRoi, double[] backgroundValues, final int threshold, RenderingHints hints ) {
        return create(sourceImage, sourceRoi, backgroundValues, threshold, DEFAULT_FILTER_SIZE, hints);
    }
    
    public static RenderedImage create( RenderedImage sourceImage, ROI sourceRoi, double[] backgroundValues, RenderingHints hints ) {
        return create(sourceImage, sourceRoi, backgroundValues, DEFAULT_THRESHOLD, DEFAULT_FILTER_SIZE, hints);
    }
    
    /**
     * Convenience method which constructs a {@link ParameterBlockJAI} and
     * invokes {@code JAI.create("ArtifactsFilter", params) }
     *
     * @param sourceImage the image to be restored
     *
     * @param roi a {@link ROI} defining the working area
     *
     * @param hints an optional RenderingHints object
     *
     * @return a RenderedImage with a band for each requested statistic
     */
    public static RenderedImage create( RenderedImage sourceImage, ROI sourceRoi, double[] backgroundValues, final int threshold, final int filterSize, RenderingHints hints ) {

        ParameterBlockJAI pb = new ParameterBlockJAI("ArtifactsFilter", RenderedRegistryMode.MODE_NAME);

        pb.setSource(srcImageNames[0], sourceImage);
        pb.setParameter(paramNames[ROI_ARG], sourceRoi);
        pb.setParameter(paramNames[BACKGROUND_ARG], backgroundValues);
        pb.setParameter(paramNames[THRESHOLD_ARG], threshold);
        pb.setParameter(paramNames[FILTERSIZE_ARG], filterSize);

        return JAI.create("ArtifactsFilter", pb, hints);
    }

    /**
     * Returns true to indicate that properties are supported
     */
    @Override
    public boolean arePropertiesSupported() {
        return true;
    }

    /**
     * Checks parameters for the following:
     * <ul>
     * <li> Number of sources is 1
     * <li> Data image bands are valid
     * </ul>
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean validateArguments( String modeName, ParameterBlock pb, StringBuffer msg ) {
        if (pb.getNumSources() == 0 || pb.getNumSources() > 1) {
            msg.append("ArtifactsFilter operator takes 1 source image");
            return false;
        }

        // CHECKING Background values
        Object backgroundValues = pb.getObjectParameter(BACKGROUND_ARG);
        double[] bgValues = null;
        if (!(backgroundValues instanceof double[])) {
            msg.append(paramNames[BACKGROUND_ARG] + " arg has to be of type double[]");
            return false;
        } else {
            bgValues = (double[]) backgroundValues;
        }

        // CHECKING DATA IMAGE
        RenderedImage dataImg = pb.getRenderedSource(0);
        Rectangle dataBounds = new Rectangle( dataImg.getMinX(), dataImg.getMinY(),
                dataImg.getWidth(), dataImg.getHeight());

        // CHECKING ROI
        Object roiObject = pb.getObjectParameter(ROI_ARG);
        if (roiObject != null) {
            if (!(roiObject instanceof ROI)) {
                msg.append("The supplied ROI is not a supported class");
                return false;
            }
            final ROI roi = (ROI)roiObject;
            final Rectangle roiBounds = roi.getBounds(); 
            if (!roiBounds.intersects(dataBounds)) {
                msg.append("The supplied ROI does not intersect the source image");
                return false;
            }
        } else {
            msg.append("The ROI parameter is missing ");
        }

        return true;
    }

}
