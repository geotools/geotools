/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.grid;

// IntersectionBuilder start

import java.io.IOException;
import java.util.Map;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

public class IntersectionBuilder extends GridFeatureBuilder {
    final FilterFactory ff2 = CommonFactoryFinder.getFilterFactory();
    final GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();

    final SimpleFeatureSource source;
    int id = 0;

    public IntersectionBuilder(SimpleFeatureType type, SimpleFeatureSource source) {
        super(type);
        this.source = source;
    }

    @Override
    public void setAttributes(GridElement el, Map<String, Object> attributes) {
        attributes.put("id", ++id);
    }

    @Override
    public boolean getCreateFeature(GridElement el) {
        Coordinate c = ((PolygonElement) el).getCenter();
        Geometry p = gf.createPoint(c);
        Filter filter = ff2.intersects(ff2.property("the_geom"), ff2.literal(p));
        boolean result = false;

        try {
            result = !source.getFeatures(filter).isEmpty();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }

        return result;
    }
}

// IntersectionBuilder end
