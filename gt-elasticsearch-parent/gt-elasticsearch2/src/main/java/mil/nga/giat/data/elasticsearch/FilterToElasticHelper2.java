package mil.nga.giat.data.elasticsearch;

import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.GeoPolygonQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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

class FilterToElasticHelper2 extends FilterToElasticHelper {

    public FilterToElasticHelper2(FilterToElastic delegate) {
        super(delegate);
    }

    @Override
    protected void visitDistanceSpatialOperator(DistanceBufferOperator filter,
            PropertyName property, Literal geometry, boolean swapped,
            Object extraData) {

        super.visitDistanceSpatialOperator(filter, property, geometry, swapped, extraData);
        
//        delegate.filterBuilder = FilterBuilders.geoDistanceFilter(key)
        getDelegate().filterBuilder = QueryBuilders.geoDistanceQuery(key)
                .lat(lat)
                .lon(lon)
                .distance(distance, DistanceUnit.METERS);

        if ((filter instanceof DWithin && swapped)
                || (filter instanceof Beyond && !swapped)) {
            getDelegate().filterBuilder = QueryBuilders.boolQuery().mustNot(getDelegate().filterBuilder);
            // delegate.filterBuilder = FilterBuilders.notFilter(delegate.filterBuilder);
        }
    }

    @Override
    protected void visitComparisonSpatialOperator(BinarySpatialOperator filter,
            PropertyName property, Literal geometry, boolean swapped, Object extraData) {

        super.visitComparisonSpatialOperator(filter, property, geometry, swapped, extraData);
        
        // if geography case, sanitize geometry first
        if(isCurrentGeography()) {
            if(isWorld(this.geometry)) {
                // nothing to filter in this case
                getDelegate().filterBuilder = QueryBuilders.matchAllQuery();
                // delegate.filterBuilder = FilterBuilders.matchAllFilter();
                return;
            } else if(isEmpty(this.geometry)) {
                if(!(filter instanceof Disjoint)) {
                    getDelegate().filterBuilder = QueryBuilders.boolQuery().mustNot(QueryBuilders.matchAllQuery());
                    // delegate.filterBuilder = FilterBuilders.notFilter(FilterBuilders.matchAllFilter());
                } else {
                    getDelegate().filterBuilder = QueryBuilders.matchAllQuery();
                    // delegate.filterBuilder = FilterBuilders.matchAllFilter();
                }
                return;
            }
        }

        visitBinarySpatialOperator(filter, (Expression)property, (Expression)this.geometry, swapped, extraData);
    }

    @Override
    protected void visitGeoShapeBinarySpatialOperator(BinarySpatialOperator filter, Expression e1, Expression e2, 
            boolean swapped, Object extraData) {

        super.visitGeoShapeBinarySpatialOperator(filter, e1, e2, swapped, extraData);

        if (shapeRelation != null) {
            getDelegate().filterBuilder = QueryBuilders.geoShapeQuery(key, shapeBuilder, shapeRelation);
            // delegate.filterBuilder = FilterBuilders.geoShapeFilter(key, shapeBuilder, shapeRelation);
        } else {
            getDelegate().filterBuilder = QueryBuilders.matchAllQuery();
            // delegate.filterBuilder = FilterBuilders.matchAllFilter();
        }
    }

    @Override
    protected void visitGeoPointBinarySpatialOperator(BinarySpatialOperator filter, Expression e1, Expression e2, 
            boolean swapped, Object extraData) {

        super.visitGeoPointBinarySpatialOperator(filter, e1, e2, swapped, extraData);
        
        final Geometry geometry = delegate.currentGeometry;

        if (geometry instanceof Polygon &&
                ((!swapped && filter instanceof Within) 
                        || (swapped && filter instanceof Contains)
                        || filter instanceof Intersects)) {
            final Polygon polygon = (Polygon) geometry;
            final GeoPolygonQueryBuilder geoPolygonFilter;
            geoPolygonFilter = QueryBuilders.geoPolygonQuery(key);
            // final GeoPolygonFilterBuilder geoPolygonFilter;
            // geoPolygonFilter = FilterBuilders.geoPolygonFilter(key);
            for (final Coordinate coordinate : polygon.getCoordinates()) {
                geoPolygonFilter.addPoint(coordinate.y, coordinate.x);
            }
            ((FilterToElastic2) delegate).filterBuilder = geoPolygonFilter;
        } else if (filter instanceof BBOX) {
            final Envelope envelope = geometry.getEnvelopeInternal();
            final double minY = envelope.getMinY();
            final double minX = envelope.getMinX();
            final double maxY = envelope.getMaxY();
            final double maxX = envelope.getMaxX();
            // delegate.filterBuilder = FilterBuilders.geoBoundingBoxFilter(key)
            ((FilterToElastic2) delegate).filterBuilder = QueryBuilders.geoBoundingBoxQuery(key)
                    .topLeft(maxY, minX)
                    .bottomRight(minY, maxX);
        } else {
            FilterToElastic.LOGGER.fine(filter.getClass().getSimpleName() 
                    + " is unsupported for geo_point types");
            delegate.fullySupported = false;
            getDelegate().filterBuilder = QueryBuilders.matchAllQuery();
            // delegate.filterBuilder = FilterBuilders.matchAllFilter();
        }
    }
    
    private FilterToElastic2 getDelegate() {
        return (FilterToElastic2) delegate;
    }

}
