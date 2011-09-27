/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.map;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.styling.Style;

/**
 * Layer responsible for raster content.
 * @since 8.0
 * @version 8.0
 * @author Jody Garnett
 *
 * @source $URL$
 */
public abstract class RasterLayer extends StyleLayer {

    /**
     * SimpleFeatureSource used to provide the outline of the raster content. Created in a lazy
     * fashion by getFeatureSource().
     */
    protected SimpleFeatureSource source;

    public RasterLayer(Style style) {
        super(style);
    }

    public RasterLayer(Style style, String title) {
        super(style, title);
    }

    /**
     * FetureSource representation of raster contents (in case a vector based renderer wishes to
     * draw a polygon outline).
     * <p>
     * This method uses {@link DataUtilities#source(org.opengis.feature.simple.SimpleFeature[]) to
     * wrap up the result of {@link #toFeatureCollection()}
     */
    @Override
    public synchronized SimpleFeatureSource getFeatureSource() {
        if (this.source == null) {
            if( getUserData().containsKey("source") ){
                // we tried already and got null - toFeatureCollection is not available!
                return null;
            }
            SimpleFeatureCollection featureCollection = toFeatureCollection();
            if( featureCollection != null ){
                this.source = DataUtilities.source(featureCollection);
            }
            // Note we store source in the user data map to communicate with MapLayer
            getUserData().put("source", source);
        }
        return source;
    }

    @Override
    public void dispose() {
        // We assume that preDispose() has been called by the 
        // by the sub-class
        
        if( source != null ){
            getUserData().remove("source");
            source = null;
        }
        super.dispose();
    }
    
    /**
     * Supply a FeatureCollection indicating where the raster is located, we ask that the features
     * use the same coordinate reference system as your raster data and form an outline or foot
     * print of the information you have available.
     * <p>
     * This is an interesting method for a RasterLayer to have; some of the rendering systems are
     * willing to render your raster content as an outline; for this to work they need this method
     * to supply a feature collection indicating where the content is located. The information may
     * also be used to determine if any of your raster content is on screen (and thus needs to be
     * rendered).
     * </p>
     * <p>
     * Note this is a feature collection to allow for raster content that contains more than one
     * image; and is not based bounding boxes (as sometimes rasters are rotated or stretched into
     * position).
     * </p>
     * <p>
     * You may find the {@link FeatureUtilities} useful in wrapping up your raster content.
     * </p>
     * 
     * @return SimpleFeatureCollection indicating the location of raster content
     */
    public abstract SimpleFeatureCollection toFeatureCollection();

}
