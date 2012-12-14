package org.geotools.image;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;

import javax.media.jai.GeometricOpImage;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.OperationRegistry;
import javax.media.jai.PlanarImage;
import javax.media.jai.PropertyGenerator;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.RenderedOp;
import javax.media.jai.Warp;
import javax.media.jai.operator.ConstantDescriptor;
import javax.media.jai.operator.WarpDescriptor;

import org.geotools.factory.Hints;
import org.jaitools.imageutils.ImageLayout2;

import com.sun.media.jai.util.PropertyGeneratorImpl;
 
/**
 * A property generator for the Warp operation that builds the expected ROI
 * bounds even when the source and target image bounds are not superimposed
 * 
 * @author Andrea Aime - GeoSolutions
 * @author Daniele Romagnoli - GeoSolutions
 */
public class GTWarpPropertyGenerator extends PropertyGeneratorImpl {
    private static final long serialVersionUID = 6622489670499745306L;

    /** Constructor. */
    public GTWarpPropertyGenerator() {
        super(new String[] {"ROI"},
              new Class[] {ROI.class},
              new Class[] {RenderedOp.class});
    }
    
    static boolean registered = false;
    
    public synchronized static void register(boolean force) {
       if(!registered || force) {
           OperationRegistry registry = JAI.getDefaultInstance().getOperationRegistry();
           PropertyGenerator[] stdGenerators = new WarpDescriptor().getPropertyGenerators("rendered");
           registry.addPropertyGenerator("rendered", "Warp", new GTWarpPropertyGenerator());
           registered = true;
       }
    }

    /**
     * Returns the specified property.
     *
     * @param name  Property name.
     * @param opNode Operation node.
     */
    public Object getProperty(String name,
                              Object opNode) {
        validate(name, opNode);

        if(opNode instanceof RenderedOp &&
           name.equalsIgnoreCase("roi")) {
            RenderedOp op = (RenderedOp)opNode;

            ParameterBlock pb = op.getParameterBlock();

            // Retrieve the rendered source image and its ROI.
            RenderedImage src = (RenderedImage)pb.getRenderedSource(0);
            Object property = src.getProperty("ROI");
            if (property == null ||
                property.equals(java.awt.Image.UndefinedProperty) ||
                !(property instanceof ROI)) {
                return java.awt.Image.UndefinedProperty;
            }

            // Return undefined also if source ROI is empty.
            ROI srcROI = (ROI)property;
            if (srcROI.getBounds().isEmpty()) {
                return java.awt.Image.UndefinedProperty;
            }

            // Retrieve the Interpolation object.
            Interpolation interp = (Interpolation)pb.getObjectParameter(1);

            // Determine the effective source bounds.
            Rectangle srcBounds = null;
            PlanarImage dst = op.getRendering();
            if (dst instanceof GeometricOpImage &&
                ((GeometricOpImage)dst).getBorderExtender() == null) {
                srcBounds =
                    new Rectangle(src.getMinX() + interp.getLeftPadding(),
                                  src.getMinY() + interp.getTopPadding(),
                                  src.getWidth() - interp.getWidth() + 1,
                                  src.getHeight() - interp.getHeight() + 1);
            } else {
                srcBounds = new Rectangle(src.getMinX(),
                     src.getMinY(),
                     src.getWidth(),
                     src.getHeight());
            }

            // If necessary, clip the ROI to the effective source bounds.
            if(!srcBounds.contains(srcROI.getBounds())) {
                srcROI = srcROI.intersect(new ROIShape(srcBounds));
            }

            // Retrieve the Warp object.
            Warp warp = (Warp)pb.getObjectParameter(0);

            // Setting constant image to be warped as a ROI
            Rectangle dstBounds = op.getBounds();
            
            // Setting layout of the constant image
            ImageLayout2 layout = new ImageLayout2();
            int minx = (int) srcBounds.getMinX();
            int miny = (int) srcBounds.getMinY();
            int w = (int) srcBounds.getWidth();
            int h = (int) srcBounds.getHeight();
            layout.setMinX(minx);
            layout.setMinY(miny);
            layout.setWidth(w);
            layout.setHeight(h);
            RenderingHints hints = op.getRenderingHints();
            hints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout));
            
            final PlanarImage constantImage = ConstantDescriptor.create(new Float(w), new Float(h), new Byte[]{(byte)255}, hints); 
            
            PlanarImage roiImage = null;
            
            // Make sure to specify tileCache, tileScheduler, tileRecyclier, by cloning hints.
            RenderingHints warpingHints = op.getRenderingHints();
            warpingHints.remove(JAI.KEY_IMAGE_LAYOUT);
            
            // Creating warped roi by the same way (Warp, Interpolation, source ROI) we warped the 
            // input image.
            final ParameterBlock paramBlk = new ParameterBlock();
            paramBlk.addSource(constantImage);
            paramBlk.add(warp);
            paramBlk.add(interp);
            paramBlk.add(null);
            paramBlk.add(srcROI);

            // force in the image layout, this way we get exactly the same
            // as the affine we're eliminating
            Hints localHints = new Hints(op.getRenderingHints());
            localHints.remove(JAI.KEY_IMAGE_LAYOUT);
            ImageLayout il = new ImageLayout();
            il.setMinX(dstBounds.x);
            il.setMinY(dstBounds.y);
            il.setWidth(dstBounds.width);
            il.setHeight(dstBounds.height);
            localHints.put(JAI.KEY_IMAGE_LAYOUT, il);
            roiImage = JAI.create("Warp", paramBlk, localHints);
            ROI dstROI = new ROI(roiImage, 1);

            // If necessary, clip the warped ROI to the destination bounds.
            if(!dstBounds.contains(dstROI.getBounds())) {
                dstROI = dstROI.intersect(new ROIShape(dstBounds));
            }

            // Return the warped and possibly clipped ROI.
            return dstROI;
        }

        return java.awt.Image.UndefinedProperty;
    }
}