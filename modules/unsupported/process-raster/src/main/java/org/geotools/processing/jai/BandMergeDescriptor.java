/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014 TOPP - www.openplans.org.
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
package org.geotools.processing.jai;

import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderableImage;
import java.util.List;

import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.ROI;
import javax.media.jai.RenderableOp;
import javax.media.jai.RenderedOp;
import javax.media.jai.registry.RenderableRegistryMode;
import javax.media.jai.registry.RenderedRegistryMode;

import org.geotools.processing.jai.nodata.Range;

/**
 * An <code>OperationDescriptor</code> describing the "BandMerge" operation.
 * 
 * <p>
 * The BandMerge operation takes two or more rendered or renderable images, and performs the merging of all the image bands inside a singular
 * multibanded image. Each source image should be non-null. The number of bands of the destination image is the sum of those of all source images.
 * 
 * <p>
 * All the bands of source images are ordered in the order given in a ParameterBlock. If image1 has three bands and image2 has one band, the merged
 * image has 4 bands. Three grayscale images can be used to merge into an RGB image in this way.
 * 
 * If No Data values are present, then the user can define an array of {@link Range} objects(each one for an image) for the No Data and a double value
 * called "destination" no data which will be set instead of the old No data value. If the array length is smaller than that of the input images, then
 * the first Range object is taken.
 * 
 * If a ROI object is used, then it is taken into account during calculations.
 * 
 * If the user wants to do a backward mapping from the destination pixels to each source image pixel, then he must add a parameter called
 * "transformations". This parameter is a List of the transformations to perform on each image. Note that the list size must be equal to that of the
 * sources, because each transformation must be related to only one image. The user should remember that these transformations have to map from the
 * destination image to the source image and not the opposite. If this transformations are not used, then the input images must have the same bounds.
 * 
 * The destination image bound is the intersection of the source image bounds. If the sources don't intersect, the destination will have a width and
 * height of 0. If the transformations are present, then an optional Layout can be used to set the destination image bound.
 * 
 * 
 * <p>
 * <table border=1>
 * <caption>Resource List</caption>
 * <tr>
 * <th>Name</th>
 * <th>Value</th>
 * </tr>
 * <tr>
 * <td>GlobalName</td>
 * <td>BandMergeOp</td>
 * </tr>
 * <tr>
 * <td>LocalName</td>
 * <td>BandMergeOp</td>
 * </tr>
 * <tr>
 * <td>Vendor</td>
 * <td>"org.geotools"</td>
 * </tr>
 * <tr>
 * <td>Description</td>
 * <td>Operation used for merging multiple images into a single multibanded image.</td>
 * </tr>
 * <tr>
 * <td>DocURL</td>
 * <td>Not Defined</td>
 * </tr>
 * <tr>
 * <td>Version</td>
 * <td>1.0</td>
 * </tr>
 * <td>arg0Desc</td>
 * <td>NoData values.</td>
 * </tr>
 * <td>arg1Desc</td>
 * <td>Destination No Data value.</td>
 * </tr>
 * <td>arg2Desc</td>
 * <td>Transformations List.</td>
 * </tr>
 * <td>arg3Desc</td>
 * <td>ROI object to use.</td>
 * </tr>
 * </table>
 * </p>
 * 
 * 
 * 
 * 
 * <p>
 * <table border=1>
 * <caption>Parameter List</caption>
 * <tr>
 * <th>Name</th>
 * <th>Class Type</th>
 * <th>Default Value</th>
 * </tr>
 * <tr>
 * <td>noData</td>
 * <td>Range[]</td>
 * <td>null</td>
 * <tr>
 * <td>destinationNoData</td>
 * <td>Double</td>
 * <td>0</td>
 * <tr>
 * <td>transformations</td>
 * <td>java.util.List</td>
 * <td>null</td>
 * <tr>
 * <td>roi</td>
 * <td>javax.media.jai.ROI</td>
 * <td>null</td>
 * <tr>
 * <tr>
 * </table>
 * </p>
 * <p>
 * 
 * @see javax.media.jai.OperationDescriptor
 * 
 * @author Nicola Lagomarsini, GeoSolutions S.A.S.
 * 
 */
public class BandMergeDescriptor extends OperationDescriptorImpl {

    /**
     * The resource strings that provide the general documentation and specify the parameter list for this operation.
     */
    private static final String[][] resources = {
            { "GlobalName", "BandMergeOp" },
            { "LocalName", "BandMergeOp" },
            { "Vendor", "org.geotools" },
            { "Description",
                    "Operation used for merging multiple images into a single multibanded image" },
            { "DocURL", "Not Defined" }, { "Version", "1.0" }, { "arg0Desc", "NoData values" },
            { "arg1Desc", "Destination No Data value" }, { "arg2Desc", "Transformations List" },
            { "arg3Desc", "ROI object to use" }

    };

    /**
     * Input Parameter name
     */
    private static final String[] paramNames = { "noData", "destinationNoData", "transformations",
            "roi" };

    /**
     * Input Parameter class
     */
    private static final Class[] paramClasses = { org.geotools.processing.jai.nodata.Range[].class,
            Double.class, List.class, javax.media.jai.ROI.class };

    /**
     * Input Parameter default values
     */
    private static final Object[] paramDefaults = { null, 0d, null, null };

    /** Constructor. */
    public BandMergeDescriptor() {
        super(resources, paramClasses, paramNames, paramDefaults);
    }

    /** Returns <code>true</code> since renderable operation is supported. */
    public boolean isRenderableSupported() {
        return true;
    }

    /**
     * Merge (possibly multi-banded)images into a multibanded image.
     * 
     * <p>
     * Creates a <code>ParameterBlockJAI</code> from all supplied arguments except <code>hints</code> and invokes
     * {@link JAI#create(String,ParameterBlock,RenderingHints)}.
     * 
     * @see JAI
     * @see ParameterBlockJAI
     * @see RenderedOp
     * 
     * @param noData Array of input No Data Ranges.
     * @param destinationNoData value used by the RenderedOp for setting the output no data value.
     * @param hints The <code>RenderingHints</code> to use. May be <code>null</code>.
     * @param sources Array of source <code>RenderedImage</code>.
     * @return The <code>RenderedOp</code> destination.
     * @throws IllegalArgumentException if <code>sources</code> is <code>null</code>.
     * @throws IllegalArgumentException if a <code>source</code> is <code>null</code>.
     */
    public static RenderedOp create(Range[] noData, double destinationNoData, RenderingHints hints,
            RenderedImage... sources) {
        return create(noData, destinationNoData, hints, null, sources);
    }

    /**
     * Merge (possibly multi-banded)images into a multibanded image.
     * 
     * <p>
     * Creates a <code>ParameterBlockJAI</code> from all supplied arguments except <code>hints</code> and invokes
     * {@link JAI#create(String,ParameterBlock,RenderingHints)}.
     * 
     * @see JAI
     * @see ParameterBlockJAI
     * @see RenderedOp
     * 
     * @param noData Array of input No Data Ranges.
     * @param destinationNoData value used by the RenderedOp for setting the output no data value.
     * @param hints The <code>RenderingHints</code> to use. May be <code>null</code>.
     * @param transform A List of AffineTransformation to use for backward mapping each source image. May be <code>null</code>.
     * @param sources Array of source <code>RenderedImage</code>.
     * @return The <code>RenderedOp</code> destination.
     * @throws IllegalArgumentException if <code>sources</code> is <code>null</code>.
     * @throws IllegalArgumentException if a <code>source</code> is <code>null</code>.
     */
    public static RenderedOp create(Range[] noData, double destinationNoData, RenderingHints hints,
            List<AffineTransform> transform, RenderedImage... sources) {
        return create(noData, destinationNoData, hints, transform, null, sources);
    }

    /**
     * Merge (possibly multi-banded)images into a multibanded image.
     * 
     * <p>
     * Creates a <code>ParameterBlockJAI</code> from all supplied arguments except <code>hints</code> and invokes
     * {@link JAI#create(String,ParameterBlock,RenderingHints)}.
     * 
     * @see JAI
     * @see ParameterBlockJAI
     * @see RenderedOp
     * 
     * @param noData Array of input No Data Ranges.
     * @param destinationNoData value used by the RenderedOp for setting the output no data value.
     * @param hints The <code>RenderingHints</code> to use. May be <code>null</code>.
     * @param transform A List of AffineTransformation to use for backward mapping each source image. May be <code>null</code>.
     * @param roi Input ROI object to use in the bandmerge operation.
     * @param sources Array of source <code>RenderedImage</code>.
     * @return The <code>RenderedOp</code> destination.
     * @throws IllegalArgumentException if <code>sources</code> is <code>null</code>.
     * @throws IllegalArgumentException if a <code>source</code> is <code>null</code>.
     */
    public static RenderedOp create(Range[] noData, double destinationNoData, RenderingHints hints,
            List<AffineTransform> transform, ROI roi, RenderedImage... sources) {
        ParameterBlockJAI pb = new ParameterBlockJAI("BandMergeOp", RenderedRegistryMode.MODE_NAME);
        // Source number
        int numSources = sources.length;
        // Check on the source number
        if (numSources <= 0) {
            throw new IllegalArgumentException("No resources are present");
        }

        // Setting of all the sources
        for (int index = 0; index < numSources; index++) {

            RenderedImage source = sources[index];

            if (source == null) {
                throw new IllegalArgumentException("This resource is null");
            }

            pb.setSource(source, index);
        }

        // Check if the transform object can be used
        if (transform != null && !transform.isEmpty()
                && transform.get(0) instanceof AffineTransform) {
            pb.setParameter("transformations", transform);
        }

        // Setting of the parameters
        pb.setParameter("noData", noData);
        pb.setParameter("destinationNoData", destinationNoData);
        pb.setParameter("roi", roi);

        // Creation of the RenderedOp
        return JAI.create("BandMergeOp", pb, hints);
    }

    /**
     * Merge (possibly multi-banded)images into a multibanded image.
     * 
     * <p>
     * Creates a <code>ParameterBlockJAI</code> from all supplied arguments except <code>hints</code> and invokes
     * {@link JAI#createRenderable(String,ParameterBlock,RenderingHints)}.
     * 
     * @see JAI
     * @see ParameterBlockJAI
     * @see RenderedOp
     * 
     * @param noData Array of input No Data Ranges.
     * @param destinationNoData value used by the RenderableOp for setting the output no data value.
     * @param hints The <code>RenderingHints</code> to use. May be <code>null</code>.
     * @param sources Array of source <code>RenderableImage</code>.
     * @return The <code>RenderableOp</code> destination.
     * @throws IllegalArgumentException if <code>sources</code> is <code>null</code>.
     * @throws IllegalArgumentException if a <code>source</code> is <code>null</code>.
     */
    public static RenderableOp createRenderable(Range[] noData, double destinationNoData,
            RenderingHints hints, RenderableImage... sources) {
        return createRenderable(noData, destinationNoData, hints, null, sources);
    }

    /**
     * Merge (possibly multi-banded)images into a multibanded image.
     * 
     * <p>
     * Creates a <code>ParameterBlockJAI</code> from all supplied arguments except <code>hints</code> and invokes
     * {@link JAI#createRenderable(String,ParameterBlock,RenderingHints)}.
     * 
     * @see JAI
     * @see ParameterBlockJAI
     * @see RenderedOp
     * 
     * @param noData Array of input No Data Ranges.
     * @param destinationNoData value used by the RenderableOp for setting the output no data value.
     * @param hints The <code>RenderingHints</code> to use. May be <code>null</code>.
     * @param transform A List of AffineTransformation to use for backward mapping each source image. May be <code>null</code>.
     * @param sources Array of source <code>RenderableImage</code>.
     * @return The <code>RenderableOp</code> destination.
     * @throws IllegalArgumentException if <code>sources</code> is <code>null</code>.
     * @throws IllegalArgumentException if a <code>source</code> is <code>null</code>.
     */
    public static RenderableOp createRenderable(Range[] noData, double destinationNoData,
            RenderingHints hints, List<AffineTransform> transform, RenderableImage... sources) {
        return createRenderable(noData, destinationNoData, hints, transform, null, sources);
    }

    /**
     * Merge (possibly multi-banded)images into a multibanded image.
     * 
     * <p>
     * Creates a <code>ParameterBlockJAI</code> from all supplied arguments except <code>hints</code> and invokes
     * {@link JAI#createRenderable(String,ParameterBlock,RenderingHints)}.
     * 
     * @see JAI
     * @see ParameterBlockJAI
     * @see RenderedOp
     * 
     * @param noData Array of input No Data Ranges.
     * @param destinationNoData value used by the RenderableOp for setting the output no data value.
     * @param hints The <code>RenderingHints</code> to use. May be <code>null</code>.
     * @param transform A List of AffineTransformation to use for backward mapping each source image. May be <code>null</code>.
     * @param roi Input ROI object to use in the bandmerge operation.
     * @param sources Array of source <code>RenderableImage</code>.
     * @return The <code>RenderableOp</code> destination.
     * @throws IllegalArgumentException if <code>sources</code> is <code>null</code>.
     * @throws IllegalArgumentException if a <code>source</code> is <code>null</code>.
     */
    public static RenderableOp createRenderable(Range[] noData, double destinationNoData,
            RenderingHints hints, List<AffineTransform> transform, ROI roi,
            RenderableImage... sources) {
        ParameterBlockJAI pb = new ParameterBlockJAI("BandMergeOp",
                RenderableRegistryMode.MODE_NAME);
        // Source number
        int numSources = sources.length;
        // Check on the source number
        if (numSources <= 0) {
            throw new IllegalArgumentException("No resources are present");
        }
        // Setting of all the sources
        for (int index = 0; index < numSources; index++) {

            RenderableImage source = sources[index];

            if (source == null) {
                throw new IllegalArgumentException("This resource is null");
            }

            pb.setSource(source, index);
        }

        // Check if the transform object can be used
        if (transform != null && !transform.isEmpty()
                && transform.get(0) instanceof AffineTransform) {
            pb.setParameter("transformations", transform);
        }

        // Setting of the parameters
        pb.setParameter("noData", noData);
        pb.setParameter("destinationNoData", destinationNoData);
        pb.setParameter("roi", roi);

        // Creation of the RenderedOp
        return JAI.createRenderable("BandMergeOp", pb, hints);
    }
}
