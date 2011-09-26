package org.geotools.data.ogr;

import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

import com.vividsolutions.jts.geom.Geometry;

class OGRFilterTranslator {

    static final FilterCapabilities ATTRIBUTE_FILTER_CAPABILITIES;

    static final FilterCapabilities GEOMETRY_FILTER_CAPABILITIES;

    static {
        // attribute filters, these we can encode fully
        ATTRIBUTE_FILTER_CAPABILITIES = new FilterCapabilities();
        ATTRIBUTE_FILTER_CAPABILITIES.addType(PropertyIsBetween.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(PropertyIsEqualTo.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(PropertyIsNotEqualTo.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(PropertyIsGreaterThan.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(PropertyIsLessThan.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(PropertyIsGreaterThanOrEqualTo.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(PropertyIsLessThanOrEqualTo.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(Or.class);
        ATTRIBUTE_FILTER_CAPABILITIES.addType(And.class);

        // we cannot encode these full, but have the capabilities extract
        // any filter that can use geometric intersection as its base (bbox is the only
        // one that we're sure to get support for)
        GEOMETRY_FILTER_CAPABILITIES = new FilterCapabilities();
        GEOMETRY_FILTER_CAPABILITIES.addType(BBOX.class);
        GEOMETRY_FILTER_CAPABILITIES.addType(Contains.class);
        GEOMETRY_FILTER_CAPABILITIES.addType(Crosses.class);
        GEOMETRY_FILTER_CAPABILITIES.addType(Equals.class);
        GEOMETRY_FILTER_CAPABILITIES.addType(Intersects.class);
        GEOMETRY_FILTER_CAPABILITIES.addType(Overlaps.class);
        GEOMETRY_FILTER_CAPABILITIES.addType(Touches.class);
        GEOMETRY_FILTER_CAPABILITIES.addType(Within.class);
    }

    private SimpleFeatureType schema;

    private Filter filter;

    public OGRFilterTranslator(SimpleFeatureType schema, Filter filter) {
        this.schema = schema;
        SimplifyingFilterVisitor simplifier = new SimplifyingFilterVisitor();
        this.filter = (Filter) filter.accept(simplifier, null);
    }

    /**
     * Returns true if this filter can be fully encoded without requiring further post processing
     * 
     * @return
     */
    public boolean isFilterFullySupported() {
        if (filter == Filter.INCLUDE || filter == Filter.EXCLUDE) {
            return true;
        }

        // we can encode fully an attribute filter plus a bbox spatial filter
        PostPreProcessFilterSplittingVisitor visitor = new PostPreProcessFilterSplittingVisitor(
                ATTRIBUTE_FILTER_CAPABILITIES, schema, null);
        filter.accept(visitor, null);
        Filter postFilter = visitor.getFilterPost();
        return postFilter == Filter.INCLUDE || postFilter instanceof BBOX;
    }

    /**
     * Parses the Geotools filter and tries to extract an intersecting geometry that can be used as
     * the OGR spatial filter
     * 
     * @param schema
     * @param filter
     * @return
     */
    public Geometry getSpatialFilter() {
        // TODO: switch to the non deprecated splitter (that no one seems to be using)
        PostPreProcessFilterSplittingVisitor visitor = new PostPreProcessFilterSplittingVisitor(
                GEOMETRY_FILTER_CAPABILITIES, schema, null);
        filter.accept(visitor, null);
        Filter preFilter = visitor.getFilterPre();
        if (preFilter instanceof BinarySpatialOperator) {
            BinarySpatialOperator bso = ((BinarySpatialOperator) preFilter);
            Expression geomExpression = null;
            if (bso.getExpression1() instanceof PropertyName
                    && bso.getExpression2() instanceof Literal) {
                geomExpression = bso.getExpression2();
            } else if (bso.getExpression1() instanceof Literal
                    && bso.getExpression2() instanceof PropertyName) {
                geomExpression = bso.getExpression1();
            }
            if (geomExpression != null) {
                Geometry geom = geomExpression.evaluate(null, Geometry.class);
                return geom;
            }
        }
        return null;
    }

    /**
     * Parses the GeoTools filter and tries to extract an SQL expression that can be used as the OGR
     * attribute filter
     * 
     * @param schema
     * @param filter
     * @return
     */
    public String getAttributeFilter() {
        // TODO: switch to the non deprecated splitter (that no one seems to be using)
        PostPreProcessFilterSplittingVisitor visitor = new PostPreProcessFilterSplittingVisitor(
                ATTRIBUTE_FILTER_CAPABILITIES, schema, null);
        filter.accept(visitor, null);
        Filter preFilter = visitor.getFilterPre();
        if (preFilter != Filter.EXCLUDE && preFilter != Filter.INCLUDE) {
            FilterToRestrictedWhere sqlConverter = new FilterToRestrictedWhere(schema);
            preFilter.accept(sqlConverter, null);
            return sqlConverter.getRestrictedWhere();
        }
        return null;
    }

    public Filter getFilter() {
        return filter;
    }
}
