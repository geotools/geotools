package org.geotools.map;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.styling.Style;

/**
 * Layer responsible for raster content.
 * 
 * @author Jody Garnett
 */
public abstract class RasterLayer extends StyleLayer {

    public RasterLayer(Style style) {
        super(style);
    }
    public RasterLayer(Style style,String title) {
        super(style,title);
    }
    
    /**
     * Supply a FeatureCollection indicating where the raster is located, we ask that the features
     * use the same coordinate reference system as your raster data and form an outline or foot
     * print of the information you have available.
     * <p>
     * This is an interesting method for a RasterLayer to have; some of the rendering systems
     * are willing to render your raster content as an outline; for this to work they need this
     * method to supply a feature collection indicating where the content is located. The
     * information may also be used to determine if any of your raster content is on screen
     * (and thus needs to be rendered).
     * </p>
     * <p>
     * Note this is a feature collection to allow for raster content that contains more than one
     * image; and is not based bounding boxes (as sometimes rasters are rotated or stretched into
     * position).
     * </p>
     * <p>
     * You may find the {@link FeatureUtilities} useful in wrapping up your raster content.
     * </p>
     * @return SimpleFeatureCollection indicating the location of raster content
     */
    public abstract SimpleFeatureCollection toFeatureCollection();

}
