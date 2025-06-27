/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ogr;

import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.And;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.Or;
import org.geotools.api.filter.PropertyIsBetween;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.PropertyIsGreaterThan;
import org.geotools.api.filter.PropertyIsGreaterThanOrEqualTo;
import org.geotools.api.filter.PropertyIsLessThan;
import org.geotools.api.filter.PropertyIsLessThanOrEqualTo;
import org.geotools.api.filter.PropertyIsNotEqualTo;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.locationtech.jts.geom.Geometry;

/**
 * Helper which translates the GeoTools filters into the filter bits that OGR understands
 *
 * @author Andrea Aime - GeoSolutions
 */
class OGRFilterTranslator {

    static final FilterCapabilities ATTRIBUTE_FILTER_CAPABILITIES;

    static final FilterCapabilities GEOMETRY_FILTER_CAPABILITIES;

    static final FilterCapabilities STRICT_GEOMETRY_FILTER_CAPABILITIES;

    static final FilterCapabilities EXTENDED_FILTER_CAPABILITIES;

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

        // the geometry filters we can encode 1-1
        STRICT_GEOMETRY_FILTER_CAPABILITIES = new FilterCapabilities();
        STRICT_GEOMETRY_FILTER_CAPABILITIES.addType(BBOX.class);

        // the extended caps, which work only assuming there is at most a single bbox filter
        // in the filter to be encoded
        EXTENDED_FILTER_CAPABILITIES = new FilterCapabilities();
        EXTENDED_FILTER_CAPABILITIES.addAll(ATTRIBUTE_FILTER_CAPABILITIES);
        EXTENDED_FILTER_CAPABILITIES.addAll(STRICT_GEOMETRY_FILTER_CAPABILITIES);
    }

    private SimpleFeatureType schema;

    private Filter filter;

    public OGRFilterTranslator(SimpleFeatureType schema, Filter filter) {
        this.schema = schema;
        SimplifyingFilterVisitor simplifier = new SimplifyingFilterVisitor();
        this.filter = (Filter) filter.accept(simplifier, null);
    }

    /** Returns true if this filter can be fully encoded without requiring further post processing */
    public boolean isFilterFullySupported() {
        if (filter == Filter.INCLUDE || filter == Filter.EXCLUDE) {
            return true;
        }

        // we can encode fully an attribute filter plus a bbox spatial filter
        PostPreProcessFilterSplittingVisitor visitor =
                new PostPreProcessFilterSplittingVisitor(ATTRIBUTE_FILTER_CAPABILITIES, schema, null);
        filter.accept(visitor, null);
        Filter postFilter = visitor.getFilterPost();
        return postFilter == Filter.INCLUDE || postFilter instanceof BBOX;
    }

    /** Returns the post filter that could not be encoded */
    public Filter getPostFilter() {
        // see if the query has a single bbox filter (that's how much we're sure to be able to
        // encode)
        PostPreProcessFilterSplittingVisitor visitor =
                new PostPreProcessFilterSplittingVisitor(STRICT_GEOMETRY_FILTER_CAPABILITIES, schema, null);
        filter.accept(visitor, null);
        Filter preFilter = visitor.getFilterPre();

        if (preFilter == null || preFilter instanceof BBOX) {
            // ok, then we can extract using the extended caps
            visitor = new PostPreProcessFilterSplittingVisitor(EXTENDED_FILTER_CAPABILITIES, schema, null);
            filter.accept(visitor, null);
            return visitor.getFilterPost();
        } else {
            // though luck, there is more than a single bbox filter
            visitor = new PostPreProcessFilterSplittingVisitor(ATTRIBUTE_FILTER_CAPABILITIES, schema, null);
            filter.accept(visitor, null);
            return visitor.getFilterPost();
        }
    }

    /**
     * Parses the Geotools filter and tries to extract an intersecting geometry that can be used as the OGR spatial
     * filter
     */
    public Geometry getSpatialFilter() {
        // TODO: switch to the non deprecated splitter (that no one seems to be using)
        PostPreProcessFilterSplittingVisitor visitor =
                new PostPreProcessFilterSplittingVisitor(GEOMETRY_FILTER_CAPABILITIES, schema, null);
        filter.accept(visitor, null);
        Filter preFilter = visitor.getFilterPre();
        if (preFilter instanceof BinarySpatialOperator) {
            BinarySpatialOperator bso = (BinarySpatialOperator) preFilter;
            Expression geomExpression = null;
            if (bso.getExpression1() instanceof PropertyName && bso.getExpression2() instanceof Literal) {
                geomExpression = bso.getExpression2();
            } else if (bso.getExpression1() instanceof Literal && bso.getExpression2() instanceof PropertyName) {
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
     * Parses the GeoTools filter and tries to extract an SQL expression that can be used as the OGR attribute filter
     */
    public String getAttributeFilter() {
        // TODO: switch to the non deprecated splitter (that no one seems to be using)
        PostPreProcessFilterSplittingVisitor visitor =
                new PostPreProcessFilterSplittingVisitor(ATTRIBUTE_FILTER_CAPABILITIES, schema, null);
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
