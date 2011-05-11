package org.geotools.grid;

// IntersectionBuilder start

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.io.IOException;
import java.util.Map;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;


public class IntersectionBuilder extends GridFeatureBuilder {
    final FilterFactory2 ff2 = CommonFactoryFinder.getFilterFactory2(null);
    final GeometryFactory gf = JTSFactoryFinder.getGeometryFactory(null);

    final SimpleFeatureSource source;
    int id = 0;

    public IntersectionBuilder(SimpleFeatureType type, SimpleFeatureSource source) {
        super(type);
        this.source = source;
    }

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
