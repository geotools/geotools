package org.geotools.gce.imagemosaic.catalog;

import it.geosolutions.imageio.utilities.SoftValueHashMap;

import java.awt.geom.AffineTransform;

import org.jaitools.imageutils.ROIGeometry;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.util.AffineTransformation;

/**
 * A ROIGeometry provider that handles multi-scale ROI with some extras:
 * <ul>
 * <li>Caching of reduced resolution of the same ROI</li>
 * <li>Management of the footprint inset</li>
 * </ul>
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 */
public class MultiLevelROI {

    private Geometry originalFootprint;
    
    private Geometry insetFootprint;

    private Geometry granuleBounds;

    private double inset;

    private FootprintInsetPolicy insetPolicy;

    private SoftValueHashMap<AffineTransform, ROIGeometry> roiCache = new SoftValueHashMap<AffineTransform, ROIGeometry>(
            10);

    private boolean empty;

    public MultiLevelROI(Geometry footprint, Geometry granuleBounds, double inset,
            FootprintInsetPolicy insetPolicy) {
        this.originalFootprint = footprint;
        this.granuleBounds = granuleBounds;
        this.inset = inset;
        this.insetPolicy = insetPolicy;
        if(inset > 0) {
            insetFootprint = insetPolicy.applyInset(originalFootprint, granuleBounds, inset);
            this.empty = insetFootprint.isEmpty();
        } else {
            this.empty = originalFootprint.isEmpty();
        }
    }

    public ROIGeometry getTransformedROI(AffineTransform at) {
        if(empty) {
            return null;
        }
        if (at == null) {
            at = new AffineTransform();
        }
        ROIGeometry roiGeometry = roiCache.get(at);
        if (roiGeometry == null) {
            Geometry rescaled;
            if (inset > 0) {
                double scale = Math.min(at.getScaleX(), at.getScaleY());
                double rescaledInset = scale * inset;
                if (rescaledInset < 1) {
                    // just apply a 1 pixel inset on the rescaled geometry 
                    Geometry cloned = (Geometry) originalFootprint.clone();
                    cloned.apply(new AffineTransformation(at.getScaleX(), at.getShearX(), at
                            .getTranslateX(), at.getShearY(), at.getScaleY(), at.getTranslateY()));
                    rescaled = insetPolicy.applyInset(cloned, granuleBounds, 1.5);
                } else {
                    // use the original footprint
                    rescaled = (Geometry) insetFootprint.clone();
                    rescaled.apply(new AffineTransformation(at.getScaleX(), at.getShearX(), at
                            .getTranslateX(), at.getShearY(), at.getScaleY(), at.getTranslateY()));
                }
            } else {
                rescaled = (Geometry) originalFootprint.clone();
                rescaled.apply(new AffineTransformation(at.getScaleX(), at.getShearX(), at
                        .getTranslateX(), at.getShearY(), at.getScaleY(), at.getTranslateY()));
            }

            roiGeometry = new ROIGeometry(rescaled);
            roiCache.put(at, roiGeometry);
        }

        return roiGeometry;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Geometry getFootprint() {
        if(inset == 0) {
            return originalFootprint;
        } else {
            return insetFootprint;
        }
    }
}
