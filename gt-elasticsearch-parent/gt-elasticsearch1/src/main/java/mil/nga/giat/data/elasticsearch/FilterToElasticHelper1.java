package mil.nga.giat.data.elasticsearch;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.GeoPolygonFilterBuilder;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.DistanceBufferOperator;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Within;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

class FilterToElasticHelper1 extends FilterToElasticHelper {

    public FilterToElasticHelper1(FilterToElastic delegate) {
        super(delegate);
    }

    @Override
    protected void visitDistanceSpatialOperator(DistanceBufferOperator filter,
            PropertyName property, Literal geometry, boolean swapped,
            Object extraData) {

        super.visitDistanceSpatialOperator(filter, property, geometry, swapped, extraData);
        
        getDelegate().filterBuilder = FilterBuilders.geoDistanceFilter(key)
                .lat(lat)
                .lon(lon)
                .distance(distance, DistanceUnit.METERS);

        if ((filter instanceof DWithin && swapped)
                || (filter instanceof Beyond && !swapped)) {
            getDelegate().filterBuilder = FilterBuilders.notFilter(getDelegate().filterBuilder);
        }
    }

    void visitComparisonSpatialOperator(BinarySpatialOperator filter,
            PropertyName property, Literal geometry, boolean swapped, Object extraData) {

        super.visitComparisonSpatialOperator(filter, property, geometry, swapped, extraData);
        
        // if geography case, sanitize geometry first
        if(isCurrentGeography()) {
            if(isWorld(this.geometry)) {
                // nothing to filter in this case
                getDelegate().filterBuilder = FilterBuilders.matchAllFilter();
                return;
            } else if(isEmpty(this.geometry)) {
                if(!(filter instanceof Disjoint)) {
                    getDelegate().filterBuilder = FilterBuilders.notFilter(FilterBuilders.matchAllFilter());
                } else {
                    getDelegate().filterBuilder = FilterBuilders.matchAllFilter();
                }
                return;
            }
        }

        visitBinarySpatialOperator(filter, (Expression)property, (Expression)this.geometry, swapped, extraData);
    }

    void visitGeoShapeBinarySpatialOperator(BinarySpatialOperator filter, Expression e1, Expression e2, 
            boolean swapped, Object extraData) {

        super.visitGeoShapeBinarySpatialOperator(filter, e1, e2, swapped, extraData);
        
        if (shapeRelation != null) {
            getDelegate().filterBuilder = FilterBuilders.geoShapeFilter(key, shapeBuilder, shapeRelation);
        } else {
            getDelegate().filterBuilder = FilterBuilders.matchAllFilter();
        }
    }

    void visitGeoPointBinarySpatialOperator(BinarySpatialOperator filter, Expression e1, Expression e2, 
            boolean swapped, Object extraData) {

        super.visitGeoPointBinarySpatialOperator(filter, e1, e2, swapped, extraData);
        
        final Geometry geometry = delegate.currentGeometry;
        
        if (geometry instanceof Polygon &&
                ((!swapped && filter instanceof Within) 
                        || (swapped && filter instanceof Contains)
                        || filter instanceof Intersects)) {
            final Polygon polygon = (Polygon) geometry;
            final GeoPolygonFilterBuilder geoPolygonFilter;
            geoPolygonFilter = FilterBuilders.geoPolygonFilter(key);
            for (final Coordinate coordinate : polygon.getCoordinates()) {
                geoPolygonFilter.addPoint(coordinate.y, coordinate.x);
            }
            getDelegate().filterBuilder = geoPolygonFilter;
        } else if (filter instanceof BBOX) {
            final Envelope envelope = geometry.getEnvelopeInternal();
            final double minY = envelope.getMinY();
            final double minX = envelope.getMinX();
            final double maxY = envelope.getMaxY();
            final double maxX = envelope.getMaxX();
            getDelegate().filterBuilder = FilterBuilders.geoBoundingBoxFilter(key)
                    .topLeft(maxY, minX)
                    .bottomRight(minY, maxX);
        } else {
            FilterToElastic.LOGGER.fine(filter.getClass().getSimpleName() 
                    + " is unsupported for geo_point types");
            delegate.fullySupported = false;
            getDelegate().filterBuilder = FilterBuilders.matchAllFilter();
        }
    }
    
    private FilterToElastic1 getDelegate() {
        return (FilterToElastic1) delegate;
    }

}
