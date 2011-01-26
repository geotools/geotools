/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.arcsde.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.arcsde.data.ArcSDEGeometryBuilder;
import org.geotools.filter.FilterCapabilities;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.And;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.Not;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeExtent;
import com.esri.sde.sdk.client.SeFilter;
import com.esri.sde.sdk.client.SeLayer;
import com.esri.sde.sdk.client.SeShape;
import com.esri.sde.sdk.client.SeShapeFilter;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Encodes the geometry related parts of a filter into a set of <code>SeFilter</code> objects and
 * provides a method to get the resulting filters suitable to set up an SeQuery's spatial
 * constraints.
 * <p>
 * Although not all filters support is coded yet, the strategy to filtering queries for ArcSDE
 * datasources is separated in two parts, the SQL where clause construction, provided by
 * <code>FilterToSQLSDE</code> and the spatial filters (or spatial constraints, in SDE vocabulary)
 * provided here; mirroring the java SDE api approach
 * </p>
 * 
 * @author Gabriel Rold?n
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/filter/GeometryEncoderSDE.java $
 */
public class GeometryEncoderSDE implements FilterVisitor {
    /** Standard java logger */
    private static Logger log = org.geotools.util.logging.Logging.getLogger("org.geotools.filter");

    private static FilterCapabilities capabilities = new FilterCapabilities();

    static {
        capabilities.addAll(FilterCapabilities.LOGICAL_OPENGIS);
        capabilities.addType(Id.class);

        capabilities.addType(BBOX.class);
        capabilities.addType(Contains.class);
        capabilities.addType(Crosses.class);
        capabilities.addType(Disjoint.class);
        capabilities.addType(Equals.class);
        capabilities.addType(Intersects.class);
        capabilities.addType(Overlaps.class);
        capabilities.addType(Within.class);
    }

    private List sdeSpatialFilters = null;

    private SeLayer sdeLayer;

    private SimpleFeatureType featureType;

    /**
     */
    public GeometryEncoderSDE() {
        // intentionally blank
    }

    /**
     */
    public GeometryEncoderSDE(SeLayer layer, SimpleFeatureType featureType) {
        this.sdeLayer = layer;
        this.featureType = featureType;
    }

    public static FilterCapabilities getCapabilities() {
        return capabilities;
    }

    public SeFilter[] getSpatialFilters() {
        SeFilter[] filters = new SeFilter[this.sdeSpatialFilters.size()];

        return (SeFilter[]) this.sdeSpatialFilters.toArray(filters);
    }

    private String getLayerName() throws SeException {
        if (this.sdeLayer == null) {
            throw new IllegalStateException("SDE layer has not been set");
        }
        return this.sdeLayer.getQualifiedName();
    }

    /**
     * overrides just to avoid the "WHERE" keyword
     */
    public void encode(Filter filter) throws GeometryEncoderException {
        this.sdeSpatialFilters = new ArrayList();
        if (Filter.INCLUDE.equals(filter)) {
            return;
        }
        if (capabilities.fullySupports(filter)) {
            filter.accept(this, null);
        } else {
            throw new GeometryEncoderException("Filter type " + filter.getClass()
                    + " not supported");
        }
    }

    /**
     * @param filter
     * @param sdeMethod
     * @param truth
     *            de default truth value for <code>sdeMethod</code>
     * @param extraData
     *            if an instanceof java.lang.Boolean, <code>truth</code> is and'ed with its boolean
     *            value. May have been set by {@link #visit(Not, Object)} to revert the logical
     *            evaluation criteria.
     */
    private void addSpatialFilter(final BinarySpatialOperator filter, final int sdeMethod,
            final boolean truth, final Object extraData) {
        boolean appliedTruth = truth;
        if (extraData instanceof Boolean) {
            boolean andValue = ((Boolean) extraData).booleanValue();
            appliedTruth = truth && andValue;
        }
        org.opengis.filter.expression.Expression left, right;
        PropertyName propertyExpr;
        Literal geomLiteralExpr;

        left = filter.getExpression1();
        right = filter.getExpression2();
        if (left instanceof PropertyName && right instanceof Literal) {
            propertyExpr = (PropertyName) left;
            geomLiteralExpr = (Literal) right;
        } else if (right instanceof PropertyName && left instanceof Literal) {
            propertyExpr = (PropertyName) right;
            geomLiteralExpr = (Literal) left;
        } else {
            String err = "SDE currently supports one geometry and one "
                    + "attribute expr.  You gave: " + left + ", " + right;
            throw new IllegalArgumentException(err);
        }

        // Should probably assert that attExpr's property name is equal to
        // spatialCol...

        // HACK: we want to support <namespace>:SHAPE, but current FM doesn't
        // support it. I guess we should try stripping the prefix and seeing if
        // that
        // matches...
        final String spatialCol = featureType.getGeometryDescriptor().getLocalName();
        final String rawPropName = propertyExpr.getPropertyName();
        String localPropName = rawPropName;
        if (rawPropName.indexOf(":") != -1) {
            localPropName = rawPropName.substring(rawPropName.indexOf(":") + 1);
        }
        if ("".equals(localPropName)) {
            log.fine("Empty property name found on filter, using default geometry property");
            localPropName = spatialCol;
        }
        if (!rawPropName.equalsIgnoreCase(spatialCol)
                && !localPropName.equalsIgnoreCase(spatialCol)) {
            throw new IllegalArgumentException("When querying against a spatial "
                    + "column, your property name must match the spatial"
                    + " column name.You used '" + propertyExpr.getPropertyName()
                    + "', but the DB's spatial column name is '" + spatialCol + "'");
        }
        Geometry geom = (Geometry) geomLiteralExpr.getValue();

        // To prevent errors in ArcSDE, we first trim the user's Filter
        // geometry to the extents of our layer.
        ArcSDEGeometryBuilder gb = ArcSDEGeometryBuilder.builderFor(Polygon.class);
        SeExtent seExtent = this.sdeLayer.getExtent();

        // If a layer just has one point in it (or one very horizontal or
        // vertical line) then we may have
        // a layer extent that's a point or line. We need to correct this.
        if (seExtent.getMaxX() == seExtent.getMinX()) {
            seExtent = new SeExtent(seExtent.getMinX() - 100, seExtent.getMinY(),
                    seExtent.getMaxX() + 100, seExtent.getMaxY());
        }
        if (seExtent.getMaxY() == seExtent.getMinY()) {
            seExtent = new SeExtent(seExtent.getMinX(), seExtent.getMinY() - 100,
                    seExtent.getMaxX(), seExtent.getMaxY() + 100);
        }

        try {
            SeShape extent = new SeShape(this.sdeLayer.getCoordRef());
            extent.generateRectangle(seExtent);

            Geometry layerEnv = gb.construct(extent, new GeometryFactory());
            geom = geom.intersection(layerEnv); // does the work

            // Now make an SeShape
            SeShape filterShape;

            // this is a bit hacky, but I don't yet know this code well enough
            // to do it right. Basically if the geometry collection is
            // completely
            // outside of the area of the layer then an intersection will return
            // a geometryCollection (two seperate geometries not intersecting
            // will
            // be a collection of two). Passing this into GeometryBuilder causes
            // an exception. So what I did was just look to see if it is a gc
            // and if so then just make a null seshape, as it shouldn't match
            // any features in arcsde. -ch
            if (geom.getClass() == GeometryCollection.class) {
                filterShape = new SeShape(this.sdeLayer.getCoordRef());
            } else {
                gb = ArcSDEGeometryBuilder.builderFor(geom.getClass());
                filterShape = gb.constructShape(geom, this.sdeLayer.getCoordRef());
            }
            // Add the filter to our list
            SeShapeFilter shapeFilter = new SeShapeFilter(getLayerName(),
                    this.sdeLayer.getSpatialColumn(), filterShape, sdeMethod, appliedTruth);
            this.sdeSpatialFilters.add(shapeFilter);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        } catch (SeException se) {
            throw new RuntimeException(se);
        }
    }

    // The Spatial Operator methods (these call to the above visit() method
    public Object visit(BBOX filter, Object extraData) {
        addSpatialFilter(filter, SeFilter.METHOD_ENVP, true, extraData);
        return extraData;
    }

    public Object visit(Beyond filter, Object extraData) {
        // This shouldn't happen since we will have pulled out
        // the unsupported parts before invoking this method
        String msg = "unsupported filter type: " + filter.getClass().getName();
        log.warning(msg);
        return extraData;
    }

    public Object visit(Contains filter, Object extraData) {
        addSpatialFilter(filter, SeFilter.METHOD_PC, true, extraData);
        return extraData;
    }

    public Object visit(Crosses filter, Object extraData) {
        addSpatialFilter(filter, SeFilter.METHOD_LCROSS_OR_CP, true, extraData);
        return extraData;
    }

    public Object visit(Disjoint filter, Object extraData) {
        addSpatialFilter(filter, SeFilter.METHOD_II_OR_ET, false, extraData);
        return extraData;
    }

    public Object visit(DWithin filter, Object extraData) {
        // This shouldn't happen since we will have pulled out
        // the unsupported parts before invoking this method
        String msg = "unsupported filter type: " + filter.getClass().getName();
        log.warning(msg);
        return extraData;
    }

    public Object visit(Equals filter, Object extraData) {
        addSpatialFilter(filter, SeFilter.METHOD_IDENTICAL, true, extraData);
        return extraData;
    }

    public Object visit(Intersects filter, Object extraData) {
        addSpatialFilter(filter, SeFilter.METHOD_II_OR_ET, true, extraData);
        return extraData;
    }

    public Object visit(Overlaps filter, Object extraData) {
        addSpatialFilter(filter, SeFilter.METHOD_II, true, extraData);
        addSpatialFilter(filter, SeFilter.METHOD_PC, false, extraData);
        addSpatialFilter(filter, SeFilter.METHOD_SC, false, extraData);
        return extraData;
    }

    public Object visit(Within filter, Object extraData) {
        addSpatialFilter(filter, SeFilter.METHOD_SC, true, extraData);
        return extraData;
    }

    public Object visit(Touches filter, Object extraData) {
        // This shouldn't happen since we will have pulled out
        // the unsupported parts before invoking this method
        String msg = "unsupported filter type: " + filter.getClass().getName();
        log.warning(msg);
        return extraData;
    }

    // These are the 'just to implement the interface' methods.
    public Object visit(Id filter, Object extraData) {
        return extraData;
    }

    public Object visit(And filter, Object extraData) {
        List<Filter> children = filter.getChildren();
        for (Filter child : children) {
            child.accept(this, extraData);
        }
        return extraData;
    }

    public Object visit(Or filter, Object extraData) {
        List<Filter> children = filter.getChildren();
        for (Filter child : children) {
            child.accept(this, extraData);
        }
        return extraData;
    }

    /**
     * Sets <code>extraData</code> to Boolean.FALSE to revert the truth value of the spatial filter
     * contained, if any.
     */
    public Object visit(Not filter, Object extraData) {
        Boolean truth = Boolean.FALSE;
        Filter negated = filter.getFilter();
        return negated.accept(this, truth);
    }

    public Object visit(ExcludeFilter filter, Object extraData) {
        return extraData;
    }

    public Object visit(IncludeFilter filter, Object extraData) {
        return extraData;
    }

    public Object visit(PropertyIsBetween filter, Object extraData) {
        return extraData;
    }

    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        return extraData;
    }

    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return extraData;
    }

    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return extraData;
    }

    public Object visit(PropertyIsLessThan filter, Object extraData) {
        return extraData;
    }

    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return extraData;
    }

    public Object visit(PropertyIsLike filter, Object extraData) {
        return extraData;
    }

    public Object visit(PropertyIsNotEqualTo filter, Object extraData) {
        return extraData;
    }

    public Object visit(PropertyIsNull filter, Object extraData) {
        return extraData;
    }

    public Object visitNullFilter(Object arg0) {
        return arg0;
    }
}
