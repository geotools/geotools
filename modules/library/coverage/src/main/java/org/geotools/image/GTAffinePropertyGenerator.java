/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image;

import com.sun.media.jai.util.PropertyGeneratorImpl;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.GeometricOpImage;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.OperationRegistry;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.RenderedOp;
import org.geotools.util.factory.Hints;

/**
 * A property generator for the Affine operation that builds a ROI with a sane image layout even
 * with large upscale factors
 *
 * @author Andrea Aime - GeoSolutions
 * @author Daniele Romagnoli - GeoSolutions
 */
public class GTAffinePropertyGenerator extends PropertyGeneratorImpl {
    private static final long serialVersionUID = 6622489670499745306L;

    /** Constructor. */
    public GTAffinePropertyGenerator() {
        super(new String[] {"ROI"}, new Class[] {ROI.class}, new Class[] {RenderedOp.class});
    }

    static boolean registered = false;

    public static synchronized void register(boolean force) {
        if (!registered || force) {
            OperationRegistry registry = JAI.getDefaultInstance().getOperationRegistry();
            registry.addPropertyGenerator("rendered", "Affine", new GTAffinePropertyGenerator());
            registered = true;
        }
    }

    /**
     * Returns the specified property in the rendered layer.
     *
     * @param name Property name.
     * @param opNode Operation node.
     */
    public Object getProperty(String name, Object opNode) {
        validate(name, opNode);

        if (opNode instanceof RenderedOp && name.equalsIgnoreCase("roi")) {
            RenderedOp op = (RenderedOp) opNode;

            ParameterBlock pb = op.getParameterBlock();

            // Retrieve the rendered source image and its ROI.
            RenderedImage src = pb.getRenderedSource(0);
            Object property = src.getProperty("ROI");
            if (property == null
                    || property.equals(java.awt.Image.UndefinedProperty)
                    || !(property instanceof ROI)) {
                // Check on the parameterBlock
                if (pb.getNumParameters() >= 4 && pb.getObjectParameter(3) != null) {
                    property = pb.getObjectParameter(3);
                } else {
                    return java.awt.Image.UndefinedProperty;
                }
            }
            ROI srcROI = (ROI) property;

            // Retrieve the Interpolation object.
            Interpolation interp = (Interpolation) pb.getObjectParameter(1);

            // Determine the effective source bounds.
            Rectangle srcBounds = null;
            PlanarImage dst = op.getRendering();
            if (dst instanceof GeometricOpImage
                    && ((GeometricOpImage) dst).getBorderExtender() == null) {
                srcBounds =
                        new Rectangle(
                                src.getMinX() + interp.getLeftPadding(),
                                src.getMinY() + interp.getTopPadding(),
                                src.getWidth() - interp.getWidth() + 1,
                                src.getHeight() - interp.getHeight() + 1);
            } else {
                srcBounds =
                        new Rectangle(
                                src.getMinX(), src.getMinY(), src.getWidth(), src.getHeight());
            }

            // If necessary, clip the ROI to the effective source bounds.
            if (!srcBounds.contains(srcROI.getBounds())) {
                srcROI = srcROI.intersect(new ROIShape(srcBounds));
            }

            // Retrieve the AffineTransform object.
            AffineTransform transform = (AffineTransform) pb.getObjectParameter(0);

            // Create the transformed ROI.
            ROI dstROI;
            if (srcROI.getClass().equals(ROI.class)) {
                // we need to build an image with the same layout of the op, or we
                // risk of building a very large ROI with super-tiny tiles when
                // doing up-sampling of a single pixel image (high oversample case)
                ParameterBlock paramBlock = new ParameterBlock();
                paramBlock.add(transform);
                paramBlock.add(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
                Hints localHints = new Hints(op.getRenderingHints());
                localHints.remove(JAI.KEY_IMAGE_LAYOUT);
                ImageLayout il = new ImageLayout();
                Rectangle dstBounds = op.getBounds();
                il.setMinX(dstBounds.x);
                il.setMinY(dstBounds.y);
                il.setWidth(dstBounds.width);
                il.setHeight(dstBounds.height);
                il.setTileWidth(op.getTileWidth());
                il.setTileWidth(op.getTileHeight());
                localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, il));

                dstROI = srcROI.performImageOp("Affine", paramBlock, 0, localHints);
            } else {
                // let the geometry based ROIs do their work at the vector level
                dstROI = srcROI.transform((AffineTransform) transform);
            }

            // Retrieve the destination bounds.
            Rectangle dstBounds = op.getBounds();

            // If necessary, clip the transformed ROI to the
            // destination bounds.
            if (!dstBounds.contains(dstROI.getBounds())) {
                dstROI = dstROI.intersect(new ROIShape(dstBounds));
            }

            // Return the transformed and possibly clipped ROI.
            return dstROI;
        }

        return java.awt.Image.UndefinedProperty;
    }
}
